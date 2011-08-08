package org.getspout.spout.io;

import java.io.File;

import net.minecraft.client.Minecraft;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class FileUtil {
	public static File getCacheDirectory() {
		File directory = new File(Minecraft.getMinecraftDir(), "spout");
		if (!directory.exists()) {
			directory.mkdir();
		}
		return directory;
	}

	public static File getTempDirectory() {
		File directory = new File(getCacheDirectory(), "temp");
		if (!directory.exists()) {
			directory.mkdir();
		}
		return directory;
	}

	public static void deleteTempDirectory() {
		try {
			FileUtils.deleteDirectory(getTempDirectory());
		}
		catch (Exception e) {}
		try {
			FileUtils.deleteDirectory(getTextureCacheDirectory());
		}
		catch (Exception e) {}
	}

	public static File getAudioCacheDirectory() {
		File directory = new File(getCacheDirectory(), "audiocache");
		if (!directory.exists()) {
			directory.mkdir();
		}
		return directory;
	}

	public static File getTextureCacheDirectory() {
		File directory = new File(getCacheDirectory(), "texturecache");
		if (!directory.exists()) {
			directory.mkdir();
		}
		return directory;
	}

	public static File getTexturePackDirectory() {
		File directory = new File(Minecraft.getMinecraftDir(), "texturepacks");
		if (!directory.exists()) {
			directory.mkdir();
		}
		return directory;
	}
	
	public static String getFileName(String Url) {
		int slashIndex = Url.lastIndexOf('/');
		int dotIndex = Url.lastIndexOf('.', slashIndex);
		if (dotIndex == -1 || dotIndex < slashIndex) {
				return Url.substring(slashIndex + 1).replaceAll("%20", " ");
		}
		return Url.substring(slashIndex + 1, dotIndex).replaceAll("%20", " ");
	}
	
	public static boolean isAudioFile(String file) {
		String extension = FilenameUtils.getExtension(file);
		if (extension != null) {
			return extension.equalsIgnoreCase("ogg") || extension.equalsIgnoreCase("wav") || extension.matches(".*[mM][iI][dD][iI]?$");
		}
		return false;
	}
	
	public static boolean isImageFile(String file) {
		String extension = FilenameUtils.getExtension(file);
		if (extension != null) {
			return extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpg");
		}
		return false;
	}
	
	public static boolean isZippedFile(String file) {
		String extension = FilenameUtils.getExtension(file);
		if (extension != null) {
			return extension.equalsIgnoreCase("zip");
		}
		return false;
	}
}