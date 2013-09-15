package net.minecraft.src;

import java.net.URL;

public class SoundPoolEntry {
	// Spout Start
	public String soundName;
	public URL soundUrl;
	// Spout End

	public SoundPoolEntry(String par1Str, URL par2URL) {
		this.soundName = par1Str;
		this.soundUrl = par2URL;
	}

	public String getSoundName() {
		return this.soundName;
	}

	public URL getSoundUrl() {
		return this.soundUrl;
	}
}
