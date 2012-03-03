package net.minecraft.src;

import java.io.File;
import java.util.Random;
import net.minecraft.src.CodecMus;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.GameSettings;
import net.minecraft.src.MathHelper;
import net.minecraft.src.SoundPool;
import net.minecraft.src.SoundPoolEntry;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

//Spout Start
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.packet.*;
import org.spoutcraft.spoutcraftapi.sound.Music;
//Spout End

public class SoundManager {

	private static SoundSystem sndSystem;
	private SoundPool soundPoolSounds = new SoundPool();
	private SoundPool soundPoolStreaming = new SoundPool();
	private SoundPool soundPoolMusic = new SoundPool();
	private int latestSoundID = 0;
	private GameSettings options;
	private static boolean loaded = false;
	private Random rand = new Random();
	private int ticksBeforeMusic;

	public SoundManager() {
		this.ticksBeforeMusic = this.rand.nextInt(12000);
	}

	public void loadSoundSettings(GameSettings par1GameSettings) {
		this.soundPoolStreaming.isGetRandomSound = false;
		this.options = par1GameSettings;
		if (!loaded && (par1GameSettings == null || par1GameSettings.soundVolume != 0.0F || par1GameSettings.musicVolume != 0.0F)) {
			this.tryToSetLibraryAndCodecs();
		}

	}

	private void tryToSetLibraryAndCodecs() {
		try {
			float var1 = this.options.soundVolume;
			float var2 = this.options.musicVolume;
			this.options.soundVolume = 0.0F;
			this.options.musicVolume = 0.0F;
			this.options.saveOptions();
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
			SoundSystemConfig.setCodec("mus", CodecMus.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			sndSystem = new SoundSystem();
			this.options.soundVolume = var1;
			this.options.musicVolume = var2;
			this.options.saveOptions();
		} catch (Throwable var3) {
			var3.printStackTrace();
			System.err.println("error linking with the LibraryJavaSound plug-in");
		}

		loaded = true;
	}

	public void onSoundOptionsChanged() {
		if (!loaded && (this.options.soundVolume != 0.0F || this.options.musicVolume != 0.0F)) {
			this.tryToSetLibraryAndCodecs();
		}

		if (loaded) {
			if (this.options.musicVolume == 0.0F) {
				sndSystem.stop("BgMusic");
			} else {
				sndSystem.setVolume("BgMusic", this.options.musicVolume);
			}
		}

	}

	public void closeMinecraft() {
		if (loaded) {
			sndSystem.cleanup();
		}

	}

	public void addSound(String par1Str, File par2File) {
		this.soundPoolSounds.addSound(par1Str, par2File);
	}

	public void addStreaming(String par1Str, File par2File) {
		this.soundPoolStreaming.addSound(par1Str, par2File);
	}

	public void addMusic(String par1Str, File par2File) {
		this.soundPoolMusic.addSound(par1Str, par2File);
	}

	public void playRandomMusicIfReady() {
		if (loaded && this.options.musicVolume != 0.0F) {
			if (!sndSystem.playing("BgMusic") && !sndSystem.playing("streaming")) {
				if (this.ticksBeforeMusic > 0) {
					--this.ticksBeforeMusic;
					return;
				}

				SoundPoolEntry var1 = this.soundPoolMusic.getRandomSound();
				if (var1 != null) {
					//Spout start
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
							}
							else if (allowed) {
								var1 = waitingSound;
								waitingSound = null;
								allowed = false;
								cancelled = false;
							}
							else if (cancelled) {
								var1 = null;
								allowed = false;
								cancelled = false;
								ticksBeforeMusic = rand.nextInt(12000) + 12000;
								return;
							}
							else {
								return;
							}
						}
					}
					//Spout end
					this.ticksBeforeMusic = this.rand.nextInt(12000) + 12000;
					sndSystem.backgroundMusic("BgMusic", var1.soundUrl, var1.soundName, false);
					sndSystem.setVolume("BgMusic", this.options.musicVolume);
					sndSystem.play("BgMusic");
				}
			}

		}
	}

	public void setListener(EntityLiving par1EntityLiving, float par2) {
		if (loaded && this.options.soundVolume != 0.0F) {
			if (par1EntityLiving != null) {
				float var3 = par1EntityLiving.prevRotationYaw + (par1EntityLiving.rotationYaw - par1EntityLiving.prevRotationYaw) * par2;
				double var4 = par1EntityLiving.prevPosX + (par1EntityLiving.posX - par1EntityLiving.prevPosX) * (double)par2;
				double var6 = par1EntityLiving.prevPosY + (par1EntityLiving.posY - par1EntityLiving.prevPosY) * (double)par2;
				double var8 = par1EntityLiving.prevPosZ + (par1EntityLiving.posZ - par1EntityLiving.prevPosZ) * (double)par2;
				float var10 = MathHelper.cos(-var3 * 0.017453292F - 3.1415927F);
				float var11 = MathHelper.sin(-var3 * 0.017453292F - 3.1415927F);
				float var12 = -var11;
				float var13 = 0.0F;
				float var14 = -var10;
				float var15 = 0.0F;
				float var16 = 1.0F;
				float var17 = 0.0F;
				sndSystem.setListenerPosition((float)var4, (float)var6, (float)var8);
				sndSystem.setListenerOrientation(var12, var13, var14, var15, var16, var17);
			}
		}
	}

	public void playStreaming(String par1Str, float par2, float par3, float par4, float par5, float par6) {
		if (loaded && this.options.soundVolume != 0.0F) {
			String var7 = "streaming";
			if (sndSystem.playing("streaming")) {
				sndSystem.stop("streaming");
			}

			if (par1Str != null) {
				SoundPoolEntry var8 = this.soundPoolStreaming.getRandomSoundFromSoundPool(par1Str);
				if (var8 != null && par5 > 0.0F) {
					if (sndSystem.playing("BgMusic")) {
						sndSystem.stop("BgMusic");
					}

					float var9 = 16.0F;
					sndSystem.newStreamingSource(true, var7, var8.soundUrl, var8.soundName, false, par2, par3, par4, 2, var9 * 4.0F);
					sndSystem.setVolume(var7, 0.5F * this.options.soundVolume);
					sndSystem.play(var7);
				}

			}
		}
	}

	//Spout start
	public void playSound(String s, float f, float f1, float f2, float f3, float f4) {
		playSound(s, f, f1, f2, f3, f4, -1, 1.0F);
	}
	
	public void playSound(String s, float f, float f1, float f2, float f3, float f4, int soundId, float volume)
	{
		if(!loaded || options.soundVolume == 0.0F)
		{
			return;
		}
		SoundPoolEntry soundpoolentry = soundPoolSounds.getRandomSoundFromSoundPool(s);
		if(soundpoolentry != null && f3 > 0.0F)
		{
			latestSoundID = (latestSoundID + 1) % 256;
			String s1;
			if (soundId == -1) s1 = (new StringBuilder()).append("sound_").append(latestSoundID).toString();
			else s1 = (new StringBuilder()).append("sound_").append(soundId).toString();
			float f5 = 16F;
			if(f3 > 1.0F)
			{
				f5 *= f3;
			}
			sndSystem.newSource(f3 > 1.0F, s1, soundpoolentry.soundUrl, soundpoolentry.soundName, false, f, f1, f2, 2, f5);
			sndSystem.setPitch(s1, f4);
			if(f3 > 1.0F)
			{
				f3 = 1.0F;
			}
			f3 *= volume;
			sndSystem.setVolume(s1, f3 * options.soundVolume);
			sndSystem.play(s1);
		}
	}
	
	public void playSoundFX(String s, float f, float f1) {
		playSoundFX(s, f, f1, -1, 1.0F);
	}
	
	public void playSoundFX(String s, float f, float f1, int soundId, float volume)
	{
		if(!loaded || options.soundVolume == 0.0F)
		{
			return;
		}
		SoundPoolEntry soundpoolentry = soundPoolSounds.getRandomSoundFromSoundPool(s);
		if(soundpoolentry != null)
		{
			latestSoundID = (latestSoundID + 1) % 256;
			String s1;
			if (soundId == -1) s1 = (new StringBuilder()).append("sound_").append(latestSoundID).toString();
			else s1 = (new StringBuilder()).append("sound_").append(soundId).toString();
			sndSystem.newSource(false, s1, soundpoolentry.soundUrl, soundpoolentry.soundName, false, 0.0F, 0.0F, 0.0F, 0, 0.0F);
			if(f > 1.0F)
			{
				f = 1.0F;
			}
			f *= 0.25F;
			f *= volume;
			sndSystem.setPitch(s1, f1);
			sndSystem.setVolume(s1, f * options.soundVolume);
			sndSystem.play(s1);
		}
	}
	
	public void playMusic(String music, int id, float volume) {
		playMusic(music, id, 0, 0, 0, volume, 0F);
	}
	
	public void playMusic(String music, int id, int x, int y, int z, float volume, float distance) {
		if(!loaded || options.musicVolume == 0.0F)
		{
			return;
		}
		stopMusic();
		SoundPoolEntry soundpoolentry = soundPoolMusic.getSoundFromSoundPool(music, id);
		if(soundpoolentry != null) {
			ticksBeforeMusic = rand.nextInt(12000) + 12000;
			if (distance > 0F) {
				sndSystem.removeSource("BgMusic");
				sndSystem.newStreamingSource(false, "BgMusic", soundpoolentry.soundUrl, soundpoolentry.soundName, false, x, y, z, 2, distance);
			}
			else {
				sndSystem.backgroundMusic("BgMusic", soundpoolentry.soundUrl, soundpoolentry.soundName, false);
			}
			sndSystem.setVolume("BgMusic", options.musicVolume * volume);
			sndSystem.play("BgMusic");
		}
	}
	
	public void playCustomSoundEffect(String effect, float volume) {
		playCustomSoundEffect(effect, 0, 0, 0, volume, 0F);
	}
	
	public void playCustomSoundEffect(String music, int x, int y, int z, float volume, float distance) {
		if(!loaded || options.soundVolume == 0.0F)
		{
			return;
		}
		SoundPoolEntry soundpoolentry = soundPoolSounds.getRandomSoundFromSoundPool(music);
		if (soundpoolentry != null) {
			String source;
			if (distance > 0F) {
				source = sndSystem.quickStream(false, soundpoolentry.soundUrl, soundpoolentry.soundName, false, x, y, z, 2, distance);
			}
			else {
				source = sndSystem.quickStream(false, soundpoolentry.soundUrl, soundpoolentry.soundName, false, 0.0F, 0.0F, 0.0F, 0, 0.0F);
			}
			sndSystem.setVolume(source, volume * options.soundVolume);
			sndSystem.play(source);
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
		if (sndSystem != null) {
			if(sndSystem.playing("BgMusic")) {
				sndSystem.stop("BgMusic");
			}
			if(sndSystem.playing("streaming")){
				sndSystem.stop("streaming");
			}
		}
	}
	
	public void fadeOut(int time){
		if(sndSystem.playing("BgMusic")) {
			sndSystem.fadeOut("BgMusic", null, time);
		}
		if(sndSystem.playing("streaming")){
			sndSystem.fadeOut("streaming", null, time);
		}
	}
	
	public void resetTime() {
		ticksBeforeMusic = rand.nextInt(12000) + 12000;
	}
	
	public SoundPoolEntry waitingSound = null;
	public boolean allowed = false;
	public boolean cancelled = false;
	//Spout end

}
