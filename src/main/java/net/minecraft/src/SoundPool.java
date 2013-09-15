package net.minecraft.src;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SoundPool {

	/** The RNG used by SoundPool. */
	private final Random rand = new Random();

	/**
	 * Maps a name (can be sound/newsound/streaming/music/newmusic) to a list of SoundPoolEntry's.
	 */
	private final Map nameToSoundPoolEntriesMapping = Maps.newHashMap();
	private final ResourceManager soundResourceManager;
	private final String soundType;
	private final boolean isGetRandomSound;

	public SoundPool(ResourceManager par1ResourceManager, String par2Str, boolean par3) {
		this.soundResourceManager = par1ResourceManager;
		this.soundType = par2Str;
		this.isGetRandomSound = par3;
	}

	/**
	 * Adds a sound to this sound pool.
	 */
	public void addSound(String par1Str) {
		try {
			String var2 = par1Str;
			par1Str = par1Str.substring(0, par1Str.indexOf("."));

			if (this.isGetRandomSound) {
				while (Character.isDigit(par1Str.charAt(par1Str.length() - 1))) {
					par1Str = par1Str.substring(0, par1Str.length() - 1);
				}
			}

			par1Str = par1Str.replaceAll("/", ".");
			Object var3 = (List)this.nameToSoundPoolEntriesMapping.get(par1Str);

			if (var3 == null) {
				var3 = Lists.newArrayList();
				this.nameToSoundPoolEntriesMapping.put(par1Str, var3);
			}

			((List)var3).add(new SoundPoolEntry(var2, this.func_110654_c(var2)));
		} catch (MalformedURLException var4) {
			var4.printStackTrace();
			throw new RuntimeException(var4);
		}
	}

	private URL func_110654_c(String par1Str) throws MalformedURLException {
		ResourceLocation var2 = new ResourceLocation(par1Str);
		String var3 = String.format("%s:%s:%s/%s", new Object[] {"mcsounddomain", var2.getResourceDomain(), this.soundType, var2.getResourcePath()});
		SoundPoolProtocolHandler var4 = new SoundPoolProtocolHandler(this);
		return new URL((URL)null, var3, var4);
	}

	/**
	 * gets a random sound from the specified (by name, can be sound/newsound/streaming/music/newmusic) sound pool.
	 */
	public SoundPoolEntry getRandomSoundFromSoundPool(String par1Str) {
		List var2 = (List)this.nameToSoundPoolEntriesMapping.get(par1Str);
		return var2 == null ? null : (SoundPoolEntry)var2.get(this.rand.nextInt(var2.size()));
	}

	/**
	 * Gets a random SoundPoolEntry.
	 */
	public SoundPoolEntry getRandomSound() {
		if (this.nameToSoundPoolEntriesMapping.isEmpty()) {
			return null;
		} else {
			ArrayList var1 = Lists.newArrayList(this.nameToSoundPoolEntriesMapping.keySet());
			return this.getRandomSoundFromSoundPool((String)var1.get(this.rand.nextInt(var1.size())));
		}
	}

	static ResourceManager func_110655_a(SoundPool par0SoundPool) {
		return par0SoundPool.soundResourceManager;
	}

	// Spout Start
	public SoundPoolEntry getSoundFromSoundPool(String s, int id) {
		List list = (List)nameToSoundPoolEntriesMapping.get(s);
		if (list == null) {
			return null;
		}
		return (SoundPoolEntry)list.get(id);
	}

	public SoundPoolEntry addCustomSound(String sound, File file) {
		try {
			if(!nameToSoundPoolEntriesMapping.containsKey(sound)) {
				nameToSoundPoolEntriesMapping.put(sound, new ArrayList());
			}
			SoundPoolEntry soundpoolentry = new SoundPoolEntry(sound, file.toURI().toURL());
			((List)nameToSoundPoolEntriesMapping.get(sound)).add(soundpoolentry);
			return soundpoolentry;
		}
		catch(MalformedURLException malformedurlexception) {
			return null;
		}
	}
	// Spout End
}
