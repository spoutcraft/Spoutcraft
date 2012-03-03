package net.minecraft.src;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.src.SoundPoolEntry;

public class SoundPool {

	private Random rand = new Random();
	private Map nameToSoundPoolEntriesMapping = new HashMap();
	private List allSoundPoolEntries = new ArrayList();
	public int numberOfSoundPoolEntries = 0;
	public boolean isGetRandomSound = true;

	public SoundPoolEntry addSound(String par1Str, File par2File) {
		try {
			String var3 = par1Str;
			par1Str = par1Str.substring(0, par1Str.indexOf("."));
			if (this.isGetRandomSound) {
				while (Character.isDigit(par1Str.charAt(par1Str.length() - 1))) {
					par1Str = par1Str.substring(0, par1Str.length() - 1);
				}
			}

			par1Str = par1Str.replaceAll("/", ".");
			if (!this.nameToSoundPoolEntriesMapping.containsKey(par1Str)) {
				this.nameToSoundPoolEntriesMapping.put(par1Str, new ArrayList());
			}

			SoundPoolEntry var4 = new SoundPoolEntry(var3, par2File.toURI().toURL());
			((List)this.nameToSoundPoolEntriesMapping.get(par1Str)).add(var4);
			this.allSoundPoolEntries.add(var4);
			++this.numberOfSoundPoolEntries;
			return var4;
		} catch (MalformedURLException var5) {
			var5.printStackTrace();
			throw new RuntimeException(var5);
		}
	}

	public SoundPoolEntry getRandomSoundFromSoundPool(String par1Str) {
		List var2 = (List)this.nameToSoundPoolEntriesMapping.get(par1Str);
		return var2 == null?null:(SoundPoolEntry)var2.get(this.rand.nextInt(var2.size()));
	}

	public SoundPoolEntry getRandomSound() {
		return this.allSoundPoolEntries.size() == 0?null:(SoundPoolEntry)this.allSoundPoolEntries.get(this.rand.nextInt(this.allSoundPoolEntries.size()));
	}

	//Spout Start
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
	
	//Spout End
}
