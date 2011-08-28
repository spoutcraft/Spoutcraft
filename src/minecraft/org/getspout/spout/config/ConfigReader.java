package org.getspout.spout.config;

import java.io.File;
import java.io.FileNotFoundException;

import org.getspout.spout.io.FileUtil;

public class ConfigReader {
	private static boolean clipboardAccess = false;
	public static void read() {
		System.out.println("Reading Configuration");
		File config = new File(FileUtil.getSpoutcraftDirectory(), "spoutcraft.properties");
		try {
			final SettingsHandler settings = new SettingsHandler(config);
			clipboardAccess = settings.getPropertyBoolean("clipboardaccess");
		}
		catch (FileNotFoundException e) {
			System.out.println("Failed to detect configuration!");
		}
		
		System.out.println("Clipboard Access: " + clipboardAccess);
		
		System.out.println("Finished Reading Configuration");
	}
	
	public static boolean isHasClipboardAccess() {
		return clipboardAccess;
	}

}
