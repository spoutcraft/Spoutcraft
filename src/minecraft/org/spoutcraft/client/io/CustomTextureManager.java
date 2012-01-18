/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import net.minecraft.client.Minecraft;

import org.spoutcraft.client.SpoutClient;

public class CustomTextureManager {
	static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	static HashMap<String, File> cacheTextureFiles = new HashMap<String, File>();

	public static void downloadTexture(String url) {
		downloadTexture(null, url, false);
	}

	public static void downloadTexture(String url, boolean ignoreEnding) {
		downloadTexture(null, url, ignoreEnding);
	}

	public static void downloadTexture(String plugin, String url, boolean ignoreEnding) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		SpoutClient.disableSandbox();
		try {
			String fileName = FileUtil.getFileName(url);
			if (!ignoreEnding && !FileUtil.isImageFile(fileName)) {
				System.out.println("Rejecting download of invalid texture: " + fileName);
			} else if (!isTextureDownloading(url) && !isTextureDownloaded(plugin, url)) {
				File dir = FileUtil.getTempDirectory();
				if (plugin != null) {
					dir = new File(FileUtil.getCacheDirectory(), plugin);
					dir.mkdir();
				}
				Download download = new Download(fileName, dir, url, null);
				FileDownloadThread.getInstance().addToDownloadQueue(download);
			}
		} finally {
			SpoutClient.enableSandbox(wasSandboxed);
		}
	}

	public static void downloadTexture(String plugin, String url) {
		downloadTexture(plugin, url, false);
	}

	public static boolean isTextureDownloading(String url) {
		return FileDownloadThread.getInstance().isDownloading(url);
	}

	public static boolean isTextureDownloaded(String url) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		SpoutClient.disableSandbox();
		try {
			return getTextureFile(null, url).exists();
		} finally {
			SpoutClient.enableSandbox(wasSandboxed);
		}
	}

	public static boolean isTextureDownloaded(String addon, String url) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		SpoutClient.disableSandbox();
		try {
			return getTextureFile(addon, url).exists();
		} finally {
			SpoutClient.enableSandbox(wasSandboxed);
		}
	}

	public static File getTextureFile(String plugin, String url) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		SpoutClient.disableSandbox();
		try {
			String fileName = FileUtil.getFileName(url);
			File cache = cacheTextureFiles.get(plugin + File.separator + fileName);
			if (cache != null) {
				return cache;
			}
			if (plugin != null) {
				File file = FileUtil.findFile(plugin, fileName);
				if (file != null) {
					cacheTextureFiles.put(plugin + File.separator + fileName, file);
					return file;
				}
			}
			return new File(FileUtil.getTempDirectory(), fileName);
		} finally {
			SpoutClient.enableSandbox(wasSandboxed);
		}
	}

	public static Texture getTextureFromPath(String path) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		SpoutClient.disableSandbox();
		try {
			if (textures.containsKey(path)) {
				return textures.get(path);
			}
			Texture texture = null;
			try {
				FileInputStream stream = new FileInputStream(path);
				if (stream.available() > 0) {
					texture = TextureLoader.getTexture("PNG", stream, true,  GL11.GL_NEAREST);
				}
				stream.close();
			} catch (IOException e) { }

			if (texture != null) {
				textures.put(path, texture);
			}
			return texture;
		} finally {
			SpoutClient.enableSandbox(wasSandboxed);
		}
	}

	public static Texture getTextureFromJar(String path) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		SpoutClient.disableSandbox();
		try {
			if (textures.containsKey(path)) {
				return textures.get(path);
			}

			Texture texture = null;
			//Check inside jar
			try {
				InputStream stream = Minecraft.class.getResourceAsStream(path);
				texture = TextureLoader.getTexture("PNG", stream, true,  GL11.GL_NEAREST);
				stream.close();
			} catch (Exception e) { }
			//Check MCP/Eclipse Path
			if (texture == null) {
				texture = getTextureFromPath(FileUtil.getSpoutcraftDirectory().getAbsolutePath() + "/../.."+ path);
			}

			if (texture != null) {
				textures.put(path, texture);
			}

			return texture;
		} finally {
			SpoutClient.enableSandbox(wasSandboxed);
		}
	}

	public static void resetTextures() {
		for (Texture texture : textures.values()) {
			texture.release();
		}
		cacheTextureFiles.clear();
		textures.clear();
	}

	public static Texture getTextureFromUrl(String url) {
		return getTextureFromUrl(null, url, true);
	}

	public static Texture getTextureFromUrl(String url, boolean download) {
		return getTextureFromUrl(null, url);
	}

	public static Texture getTextureFromUrl(String plugin, String url) {
		return getTextureFromUrl(plugin, url, true);
	}

	public static Texture getTextureFromUrl(String plugin, String url, boolean download) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		SpoutClient.disableSandbox();
		try {
			File texture = getTextureFile(plugin, url);
			if (!texture.exists()) {
				return null;
			}
			try {
				return getTextureFromPath(texture.getCanonicalPath());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} finally {
			SpoutClient.enableSandbox(wasSandboxed);
		}
	}

	public static String getTexturePathFromUrl(String url) {
		return getTexturePathFromUrl(null, url);
	}

	public static String getTexturePathFromUrl(String plugin, String url) {
		if (!isTextureDownloaded(plugin, url)) {
			return null;
		}

		boolean wasSandboxed = SpoutClient.isSandboxed();
		SpoutClient.disableSandbox();
		try {
			File download = new File(FileUtil.getTempDirectory(), FileUtil.getFileName(url));
			try {
				return download.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			SpoutClient.enableSandbox(wasSandboxed);
		}
		return null;
	}
}
