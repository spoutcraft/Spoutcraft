package net.minecraft.src;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.cuina.fireandfuel.CodecJLayerMP3;
import org.apache.commons.io.FileUtils;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;
import thedudeguy.paulscode.sound.codecs.CodecJOrbis;

import org.spoutcraft.api.sound.Music;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.packet.PacketMusicChange;

// Spout Start - Unused import
//import paulscode.sound.codecs.CodecJOrbis;
// Spout End
public class SoundManager implements ResourceManagerReloadListener {
	private static final String[] field_130084_a = new String[] {"ogg"};
	
	/** A reference to the sound system. */
	private SoundSystem sndSystem;
	
	/** Set to true when the SoundManager has been initialised. */
	private boolean loaded;

	/** Sound pool containing sounds. */
	private final SoundPool soundPoolSounds;

	/** Sound pool containing streaming audio. */
	private final SoundPool soundPoolStreaming;

	/** Sound pool containing music. */
	private final SoundPool soundPoolMusic;
	
	/**
	 * The last ID used when a sound is played, passed into SoundSystem to give active sounds a unique ID
	 */
	private int latestSoundID;

	/** A reference to the game settings. */
	private final GameSettings options;
	private final File fileAssets;

	/** Identifiers of all currently playing sounds. Type: HashSet<String> */
	private final Set playingSounds = new HashSet();
	private final List field_92072_h = new ArrayList();

	/** RNG. */
	private Random rand = new Random();
	private int ticksBeforeMusic;
	// Spout Start
	private final int SOUND_EFFECTS_PER_TICK = 3;
	private int soundEffectsLimit = SOUND_EFFECTS_PER_TICK;
	// Spout End

	public SoundManager(ResourceManager par1ResourceManager, GameSettings par2GameSettings, File par3File) {
		this.ticksBeforeMusic = this.rand.nextInt(12000);
		this.options = par2GameSettings;
		this.fileAssets = par3File;
		this.soundPoolSounds = new SoundPool(par1ResourceManager, "sound", true);
		this.soundPoolStreaming = new SoundPool(par1ResourceManager, "records", false);
		this.soundPoolMusic = new SoundPool(par1ResourceManager, "music", true);

		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			// Spout Start
			SoundSystemConfig.setCodec("mp3", CodecJLayerMP3.class);
			// Spout End
		} catch (SoundSystemException var5) {
			var5.printStackTrace();
			System.err.println("error linking with the LibraryJavaSound plug-in");
		}

		this.loadSounds();
	}

	public void onResourceManagerReload(ResourceManager par1ResourceManager) {
		this.stopAllSounds();
		this.cleanup();
		this.tryToSetLibraryAndCodecs();
	}

	private void loadSounds() {
		if (this.fileAssets.isDirectory()) {
			Collection var1 = FileUtils.listFiles(this.fileAssets, field_130084_a, true);
			Iterator var2 = var1.iterator();

			while (var2.hasNext()) {
				File var3 = (File)var2.next();
				this.loadSoundFile(var3);
			}
		}
	}

	private void loadSoundFile(File par1File) {
		String var2 = this.fileAssets.toURI().relativize(par1File.toURI()).getPath();
		int var3 = var2.indexOf("/");

		if (var3 != -1) {
			String var4 = var2.substring(0, var3);
			var2 = var2.substring(var3 + 1);

			if ("sound".equalsIgnoreCase(var4)) {
				this.addSound(var2);
			} else if ("records".equalsIgnoreCase(var4)) {
				this.addStreaming(var2);
			} else if ("music".equalsIgnoreCase(var4)) {
				this.addMusic(var2);
			}
		}
	}


	/**
	 * Tries to add the paulscode library and the relevant codecs. If it fails, the volumes (sound and music) will be set
	 * to zero in the options file.
	 */
	private synchronized void tryToSetLibraryAndCodecs() {
		if (!this.loaded) {
			float var1 = this.options.soundVolume;
			float var2 = this.options.musicVolume;
			this.options.soundVolume = 0.0F;
			this.options.musicVolume = 0.0F;
			this.options.saveOptions();

			try {
				(new Thread(new SoundManagerINNER1(this))).start();
				this.options.soundVolume = var1;
				this.options.musicVolume = var2;
			} catch (RuntimeException var4) {
				var4.printStackTrace();
				System.err.println("error starting SoundSystem turning off sounds & music");
				this.options.soundVolume = 0.0F;
				this.options.musicVolume = 0.0F;
			}

			this.options.saveOptions();
		}
	}

	/**
	 * Called when one of the sound level options has changed.
	 */
	public void onSoundOptionsChanged() {
		if (this.loaded) {
			if (this.options.musicVolume == 0.0F) {
				this.sndSystem.stop("BgMusic");
				this.sndSystem.stop("streaming");
			} else {
				this.sndSystem.setVolume("BgMusic", this.options.musicVolume);
				this.sndSystem.setVolume("streaming", this.options.musicVolume);
			}
		}
	}

	/**
	 * Cleans up the Sound System
	 */
	public void cleanup() {
		if (this.loaded) {
			this.sndSystem.cleanup();
			this.loaded = false;
		}
	}

	/**
	 * Adds a sounds with the name from the file. Args: name, file
	 */
	public void addSound(String par1Str) {
		this.soundPoolSounds.addSound(par1Str);
	}

	/**
	 * Adds an audio file to the streaming SoundPool.
	 */
	public void addStreaming(String par1Str) {
		this.soundPoolStreaming.addSound(par1Str);
	}

	/**
	 * Adds an audio file to the music SoundPool.
	 */
	public void addMusic(String par1Str) {
		this.soundPoolMusic.addSound(par1Str);
	}


	/**
	 * If its time to play new music it starts it up.
	 */
	public void playRandomMusicIfReady() {
		if (this.loaded && this.options.musicVolume != 0.0F) {
			if (!this.sndSystem.playing("BgMusic") && !this.sndSystem.playing("streaming")) {
				if (this.ticksBeforeMusic > 0) {
					--this.ticksBeforeMusic;
				} else {
					SoundPoolEntry var1 = this.soundPoolMusic.getRandomSound();

					if (var1 != null) {
						// Spout Start
						if (SpoutClient.getInstance().isSpoutEnabled()) {
							EntityPlayer player = SpoutClient.getHandle().thePlayer;
							if (player instanceof EntityClientPlayerMP) {
								if (waitingSound == null) {
									Music music = Music.getMusicFromName(var1.soundName);
									if (music != null) {
										waitingSound = var1;
										SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketMusicChange(music.getId(), (int)options.musicVolume * 100));
										return;
									}
								} else if (allowed) {
									var1 = waitingSound;
									waitingSound = null;
									allowed = false;
									cancelled = false;
								} else if (cancelled) {
									var1 = null;
									allowed = false;
									cancelled = false;
									ticksBeforeMusic = rand.nextInt(12000) + 12000;
									return;
								} else {
									return;
								}
							}
						}
						// Spout End
						this.ticksBeforeMusic = this.rand.nextInt(12000) + 12000;
						this.sndSystem.backgroundMusic("BgMusic", var1.getSoundUrl(), var1.getSoundName(), false);
						sndSystem.setVolume("BgMusic", this.options.musicVolume);
						sndSystem.play("BgMusic");
					}
				}
			}
		}
	}

	/**
	 * Sets the listener of sounds
	 */
	public void setListener(EntityLivingBase par1EntityLivingBase, float par2) {
		if (this.loaded && this.options.soundVolume != 0.0F && par1EntityLivingBase != null) {
			float var3 = par1EntityLivingBase.prevRotationPitch + (par1EntityLivingBase.rotationPitch - par1EntityLivingBase.prevRotationPitch) * par2;
			float var4 = par1EntityLivingBase.prevRotationYaw + (par1EntityLivingBase.rotationYaw - par1EntityLivingBase.prevRotationYaw) * par2;
			double var5 = par1EntityLivingBase.prevPosX + (par1EntityLivingBase.posX - par1EntityLivingBase.prevPosX) * (double)par2;
			double var7 = par1EntityLivingBase.prevPosY + (par1EntityLivingBase.posY - par1EntityLivingBase.prevPosY) * (double)par2;
			double var9 = par1EntityLivingBase.prevPosZ + (par1EntityLivingBase.posZ - par1EntityLivingBase.prevPosZ) * (double)par2;
			float var11 = MathHelper.cos(-var4 * 0.017453292F - (float)Math.PI);
			float var12 = MathHelper.sin(-var4 * 0.017453292F - (float)Math.PI);
			float var13 = -var12;
			float var14 = -MathHelper.sin(-var3 * 0.017453292F - (float)Math.PI);
			float var15 = -var11;
			float var16 = 0.0F;
			float var17 = 1.0F;
			float var18 = 0.0F;
			this.sndSystem.setListenerPosition((float)var5, (float)var7, (float)var9);
			this.sndSystem.setListenerOrientation(var13, var14, var15, var16, var17, var18);
		}
	}

	/**
	 * Stops all currently playing sounds
	 */
	public void stopAllSounds() {
		if (this.loaded) {
			Iterator var1 = this.playingSounds.iterator();

			while (var1.hasNext()) {
				String var2 = (String)var1.next();
				this.sndSystem.stop(var2);
			}

			this.playingSounds.clear();
		}
	}

	public void playStreaming(String par1Str, float par2, float par3, float par4) {
		if (this.loaded && (this.options.soundVolume != 0.0F || par1Str == null)) {
			String var5 = "streaming";

			if (this.sndSystem.playing(var5)) {
				this.sndSystem.stop(var5);
			}

			if (par1Str != null) {
				SoundPoolEntry var6 = this.soundPoolStreaming.getRandomSoundFromSoundPool(par1Str);

				if (var6 != null) {
					if (this.sndSystem.playing("BgMusic")) {
						this.sndSystem.stop("BgMusic");
					}

					this.sndSystem.newStreamingSource(true, var5, var6.getSoundUrl(), var6.getSoundName(), false, par2, par3, par4, 2, 64.0F);
					this.sndSystem.setVolume(var5, 0.5F * this.options.soundVolume);
					this.sndSystem.play(var5);
				}
			}
		}
	}

	/**
	 * Updates the sound associated with the entity with that entity's position and velocity. Args: the entity
	 */
	public void updateSoundLocation(Entity par1Entity) {
		this.updateSoundLocation(par1Entity, par1Entity);
	}

	/**
	 * Updates the sound associated with soundEntity with the position and velocity of trackEntity. Args: soundEntity,
	 * trackEntity
	 */
	public void updateSoundLocation(Entity par1Entity, Entity par2Entity) {
		String var3 = "entity_" + par1Entity.entityId;

		if (this.playingSounds.contains(var3)) {
			if (this.sndSystem.playing(var3)) {
				this.sndSystem.setPosition(var3, (float)par2Entity.posX, (float)par2Entity.posY, (float)par2Entity.posZ);
				this.sndSystem.setVelocity(var3, (float)par2Entity.motionX, (float)par2Entity.motionY, (float)par2Entity.motionZ);
			} else {
				this.playingSounds.remove(var3);
			}
		}
	}

	/**
	 * Returns true if a sound is currently associated with the given entity, or false otherwise.
	 */
	public boolean isEntitySoundPlaying(Entity par1Entity) {
		if (par1Entity != null && this.loaded) {
			String var2 = "entity_" + par1Entity.entityId;
			return this.sndSystem.playing(var2);
		} else {
			return false;
		}
	}

	/**
	 * Stops playing the sound associated with the given entity
	 */
	public void stopEntitySound(Entity par1Entity) {
		if (par1Entity != null && this.loaded) {
			String var2 = "entity_" + par1Entity.entityId;

			if (this.playingSounds.contains(var2)) {
				if (this.sndSystem.playing(var2)) {
					this.sndSystem.stop(var2);
				}

				this.playingSounds.remove(var2);
			}
		}
	}

	/**
	 * Sets the volume of the sound associated with the given entity, if one is playing. The volume is scaled by the global
	 * sound volume. Args: the entity, the volume (from 0 to 1)
	 */
	public void setEntitySoundVolume(Entity par1Entity, float par2) {
		if (par1Entity != null && this.loaded && this.options.soundVolume != 0.0F) {
			String var3 = "entity_" + par1Entity.entityId;

			if (this.sndSystem.playing(var3)) {
				this.sndSystem.setVolume(var3, par2 * this.options.soundVolume);
			}
		}
	}

	/**
	 * Sets the pitch of the sound associated with the given entity, if one is playing. Args: the entity, the pitch
	 */
	public void setEntitySoundPitch(Entity par1Entity, float par2) {
		if (par1Entity != null && this.loaded && this.options.soundVolume != 0.0F) {
			String var3 = "entity_" + par1Entity.entityId;

			if (this.sndSystem.playing(var3)) {
				this.sndSystem.setPitch(var3, par2);
			}
		}
	}

	/**
	 * If a sound is already playing from the given entity, update the position and velocity of that sound to match the
	 * entity. Otherwise, start playing a sound from that entity. Setting the last flag to true will prevent other sounds
	 * from overriding this one. Args: The sound name, the entity, the volume, the pitch, priority
	 */
	public void playEntitySound(String par1Str, Entity par2Entity, float par3, float par4, boolean par5) {
		if (this.loaded && (this.options.soundVolume != 0.0F || par1Str == null) && par2Entity != null) {
			String var6 = "entity_" + par2Entity.entityId;

			if (this.playingSounds.contains(var6)) {
				this.updateSoundLocation(par2Entity);
			} else {
				if (this.sndSystem.playing(var6)) {
					this.sndSystem.stop(var6);
				}

				if (par1Str != null) {
					SoundPoolEntry var7 = this.soundPoolSounds.getRandomSoundFromSoundPool(par1Str);

					if (var7 != null && par3 > 0.0F) {
						float var8 = 16.0F;

						if (par3 > 1.0F) {
							var8 *= par3;
						}

						this.sndSystem.newSource(par5, var6, var7.getSoundUrl(), var7.getSoundName(), false, (float)par2Entity.posX, (float)par2Entity.posY, (float)par2Entity.posZ, 2, var8);
						this.sndSystem.setLooping(var6, true);
						this.sndSystem.setPitch(var6, par4);

						if (par3 > 1.0F) {
							par3 = 1.0F;
						}

						this.sndSystem.setVolume(var6, par3 * this.options.soundVolume);
						this.sndSystem.setVelocity(var6, (float)par2Entity.motionX, (float)par2Entity.motionY, (float)par2Entity.motionZ);
						this.sndSystem.play(var6);
						this.playingSounds.add(var6);
					}
				}
			}
		}
	}

	// Spout Start
	/**
	 * Plays a sound. Args: soundName, x, y, z, volume, pitch
	 * calls upon the replacement method so the original calls function properly.
	 */
	public void playSound(String s, float f, float f1, float f2, float f3, float f4) {
		playSound(s, f, f1, f2, f3, f4, -1, 1.0F);
	}

	/**
	 * Replaces the original playSound method
	 * @param s - effect name.
	 * @param f - x
	 * @param f1 - y
	 * @param f2 - z
	 * @param f3 - pitch
	 * @param f4 - distance
	 * @param variationId - variation of the sound effect. Set to -1 to select a random variation or if no variations exist
	 * @param volume - volume
	 */
	public void playSound(String s, float f, float f1, float f2, float f3, float f4, int variationId, float volume) {
		if (!loaded || options.soundVolume == 0.0F) {
			return;
		}

		if (soundEffectsLimit-- <= 0) {
			return;
		}

		SoundPoolEntry soundpoolentry;

		if (variationId > -1) {
			soundpoolentry = soundPoolSounds.getSoundFromSoundPool(s, variationId);
		} else {
			soundpoolentry = soundPoolSounds.getRandomSoundFromSoundPool(s);
		}

		if (soundpoolentry != null && f3 > 0.0F) {
			latestSoundID = (latestSoundID + 1) % 256;
			String s1;

			if (variationId == -1) {
				s1 = (new StringBuilder()).append("sound_").append(latestSoundID).toString();
			} else {
				s1 = (new StringBuilder()).append("sound_").append(variationId).toString();
			}

			float f5 = 16F;
			if (f3 > 1.0F){
				f5 *= f3;
			}
			this.sndSystem.newSource(f3 > 1.0F, s1, soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false, f, f1, f2, 2, f5);			
			this.sndSystem.setPitch(s1, f4);
			if (f3 > 1.0F) {
				f3 = 1.0F;
			}
			f3 *= volume;
			this.sndSystem.setVolume(s1, f3 * options.soundVolume);
			this.sndSystem.play(s1);
		}
	}

	/**
	 * Plays a sound effect with the volume and pitch of the parameters passed. The sound isn't affected by position of the
	 * player (full volume and center balanced)
	 */
	public void playSoundFX(String s, float f, float f1) {
		playSoundFX(s, f, f1, -1, 1.0F);
	}

	public void playSoundFX(String s, float f, float f1, int soundId, float volume) {
		if (!loaded || options.soundVolume == 0.0F) {
			return;
		}
		if (soundEffectsLimit-- <= 0) {
			return;
		}
		SoundPoolEntry soundpoolentry = soundPoolSounds.getRandomSoundFromSoundPool(s);
		if (soundpoolentry != null) {
			latestSoundID = (latestSoundID + 1) % 256;
			String s1;

			if (soundId == -1) {
				s1 = (new StringBuilder()).append("sound_").append(latestSoundID).toString();
			} else {
				s1 = (new StringBuilder()).append("sound_").append(soundId).toString();
			}
			sndSystem.newSource(false, s1, soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false, 0.0F, 0.0F, 0.0F, 0, 0.0F);
			if (f > 1.0F) {
				f = 1.0F;
			}
			f *= 0.25F;
			f *= volume;
			this.sndSystem.setPitch(s1, f1);
			this.sndSystem.setVolume(s1, f * options.soundVolume);
			this.sndSystem.play(s1);
		}
	}

	public void playMusic(String music, int id, float volume) {
		playMusic(music, id, 0, 0, 0, volume, 0F);
	}

	public void playMusic(String music, int id, int x, int y, int z, float volume, float distance) {
		if (!loaded || options.musicVolume == 0.0F) {
			return;
		}
		stopMusic();

		SoundPoolEntry soundpoolentry = soundPoolMusic.getSoundFromSoundPool(music, id);
		if (soundpoolentry != null) {
			ticksBeforeMusic = rand.nextInt(12000) + 12000;

			if (distance > 0F) {
				this.sndSystem.removeSource("BgMusic");
				this.sndSystem.newStreamingSource(false, "BgMusic", soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false, x, y, z, 2, distance);
			} else {
				this.sndSystem.backgroundMusic("BgMusic", soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false);
			}
			this.sndSystem.setVolume("BgMusic", options.musicVolume * volume);
			this.sndSystem.play("BgMusic");
		}
	}

	public void playCustomSoundEffect(String effect, float volume) {
		playCustomSoundEffect(effect, 0, 0, 0, volume, 0F);
	}

	public void playCustomSoundEffect(String music, int x, int y, int z, float volume, float distance) {
		if (!loaded || options.soundVolume == 0.0F) {
			return;
		}

		if (soundEffectsLimit-- <= 0) {
			return;
		}

		SoundPoolEntry soundpoolentry = soundPoolSounds.getRandomSoundFromSoundPool(music);
		if (soundpoolentry != null) {
			String source;
			if (distance > 0F) {
				source = sndSystem.quickStream(false, soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false, x, y, z, 2, distance);
			} else {
				source = sndSystem.quickStream(false, soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false, 0.0F, 0.0F, 0.0F, 0, 0.0F);
			}
			this.sndSystem.setVolume(source, volume * options.soundVolume);
			this.sndSystem.play(source);
		}
	}

	public void preload(String music) {
		sndSystem.loadSound(music);
	}

	public boolean hasMusic(String sound, int id) {
		return soundPoolMusic.getSoundFromSoundPool(sound, id) != null;
	}

	public boolean hasSoundEffect(String sound, int id) {
		return soundPoolSounds.getSoundFromSoundPool(sound, id) != null;
	}

	public void addCustomSoundEffect(String sound, File song) {
		soundPoolSounds.addCustomSound(sound, song);
	}

	public void addCustomMusic(String sound, File song) {
		soundPoolMusic.addCustomSound(sound, song);
	}

	public void stopMusic() {
		if (this.sndSystem != null) {
			if (this.sndSystem.playing("BgMusic")) {
				this.sndSystem.stop("BgMusic");
			}

			if (this.sndSystem.playing("streaming")) {
				this.sndSystem.stop("streaming");
			}
		}
	}

	public void fadeOut(int time) {
		if (this.sndSystem.playing("BgMusic")) {
			this.sndSystem.fadeOut("BgMusic", null, time);
		}

		if (this.sndSystem.playing("streaming")){
			this.sndSystem.fadeOut("streaming", null, time);
		}
	}

	public void resetTime() {
		ticksBeforeMusic = rand.nextInt(12000) + 12000;
	}

	public void tick() {
		soundEffectsLimit = SOUND_EFFECTS_PER_TICK;
	}

	public SoundPoolEntry waitingSound = null;
	public boolean allowed = false;
	public boolean cancelled = false;
	// Spout End

	/**
	 * Pauses all currently playing sounds
	 */
	public void pauseAllSounds() {
		Iterator var1 = this.playingSounds.iterator();

		while (var1.hasNext()) {
			String var2 = (String)var1.next();
			this.sndSystem.pause(var2);
		}
	}

	/**
	 * Resumes playing all currently playing sounds (after pauseAllSounds)
	 */
	public void resumeAllSounds() {
		Iterator var1 = this.playingSounds.iterator();

		while (var1.hasNext()) {
			String var2 = (String)var1.next();
			this.sndSystem.play(var2);
		}
	}

	public void func_92071_g() {
		if (!this.field_92072_h.isEmpty()) {
			Iterator var1 = this.field_92072_h.iterator();

			while (var1.hasNext()) {
				ScheduledSound var2 = (ScheduledSound)var1.next();
				--var2.field_92064_g;

				if (var2.field_92064_g <= 0) {
					this.playSound(var2.field_92069_a, var2.field_92067_b, var2.field_92068_c, var2.field_92065_d, var2.field_92066_e, var2.field_92063_f);
					var1.remove();
				}
			}
		}
	}

	public void func_92070_a(String par1Str, float par2, float par3, float par4, float par5, float par6, int par7) {
		this.field_92072_h.add(new ScheduledSound(par1Str, par2, par3, par4, par5, par6, par7));
	}
	
	static SoundSystem func_130080_a(SoundManager par0SoundManager, SoundSystem par1SoundSystem) {
		return par0SoundManager.sndSystem = par1SoundSystem;
	}

	static boolean func_130082_a(SoundManager par0SoundManager, boolean par1) {
		return par0SoundManager.loaded = par1;
	}
}
