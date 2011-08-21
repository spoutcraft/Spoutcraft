package org.getspout.spout.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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

	
	public static long getCRC(File file, byte[] buffer) {

		FileInputStream in = null;
		
		try {
			in = new FileInputStream(file);
			return getCRC(in, buffer);
		} catch (FileNotFoundException e) {
			return 0;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public static long getCRC(URL url, byte[] buffer) {
		
		InputStream in = null;
		
		try {
			in = url.openStream();
			return getCRC(in, buffer);
		} catch (IOException e) {
			return 0;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		
	}

	public static long getCRC(InputStream in, byte[] buffer) {
		
		long hash = 1;
		
		int read = 0;
		int i;
		while (read >= 0) {
			try {
				read = in.read(buffer);
				for (i=0; i < read; i++) {
					hash += (hash << 5) + (long)buffer[i];
				}
			} catch (IOException ioe) {
				return 0;
			}
		}

		return hash;
		
	}
}