/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spout.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import net.minecraft.client.Minecraft;

import org.getspout.spout.client.SpoutClient;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class CustomTextureManager {
	static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	static HashMap<String, File> cacheTextureFiles = new HashMap<String, File>();
	
	public static void downloadTexture(String url) {
		downloadTexture(null, url);
	}
	
	public static void downloadTexture(String plugin, String url) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		
		String fileName = FileUtil.getFileName(url);
		if (!FileUtil.isImageFile(fileName)) {
			System.out.println("Rejecting download of invalid texture: " + fileName);
		}
		else if (!isTextureDownloading(url) && !isTextureDownloaded(plugin, url)) {
			Download download = new Download(fileName, FileUtil.getTextureCacheDirectory(), url, null);
			FileDownloadThread.getInstance().addToDownloadQueue(download);
		}
		
		if (wasSandboxed) {
			SpoutClient.enableSandbox();
		}
	}
	
	public static boolean isTextureDownloading(String url) {
		return FileDownloadThread.getInstance().isDownloading(url);
	}
	
	public static boolean isTextureDownloaded(String url) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		
		boolean result = getTextureFile(null, url).exists();
		
		if (wasSandboxed) {
			SpoutClient.enableSandbox();
		}
		return result;
	}
	
	public static boolean isTextureDownloaded(String addon, String url) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		
		boolean result = getTextureFile(addon, url).exists();
		
		if (wasSandboxed) {
			SpoutClient.enableSandbox();
		}
		return result;
	}
	
	public static File getTextureFile(String plugin, String url) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		
		String fileName = FileUtil.getFileName(url);
		File cache = cacheTextureFiles.get(plugin + File.separator + fileName);
		File result = new File(FileUtil.getTextureCacheDirectory(), fileName);
		if (cache != null) {
			result = cache;
		}
		if (plugin != null) {
			File file = FileUtil.findTextureFile(plugin, fileName);
			if (file != null) {
				cacheTextureFiles.put(plugin + File.separator + fileName, file);
				result = file;
			}
		}
		
		if (wasSandboxed) {
			SpoutClient.enableSandbox();
		}
		return result;
	}
	
	public static Texture getTextureFromPath(String path) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		
		Texture texture = null;
		if (!textures.containsKey(path)) {
			try {
				//System.out.println("Loading Texture: " + path);
				FileInputStream stream = new FileInputStream(path);
				if (stream.available() > 0) {
					texture = TextureLoader.getTexture("PNG", stream, true,  GL11.GL_NEAREST);
				}
				stream.close();
			}
			catch (IOException e) { }
			if (texture == null) {
				System.out.println("Error loading texture: " + path);
			}
			else {
				textures.put(path, texture);
			}
		}
		else {
			texture = textures.get(path);
		}
		
		if (wasSandboxed) {
			SpoutClient.enableSandbox();
		}
		
		return texture;
	}
	
	public static Texture getTextureFromJar(String path) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		
		Texture texture = null;
		if (!textures.containsKey(path)) {
			try {
				//System.out.println("Loading Texture: " + path);
				InputStream stream = Minecraft.class.getResourceAsStream(path);
				texture = TextureLoader.getTexture("PNG", stream, true,  GL11.GL_NEAREST);
				stream.close();
			}
			catch (Exception e) { }
			if (texture == null) {
				texture = getTextureFromPath(FileUtil.getSpoutcraftDirectory().getAbsolutePath() + "/../.."+ path);
				if(texture == null) {
					System.out.println("Error loading texture: " + path);
				}
			}
			else {
				textures.put(path, texture);
			}
		}
		else {
			texture = textures.get(path);
		}
		
		if (wasSandboxed) {
			SpoutClient.enableSandbox();
		}
		return texture;
	}
	
	public static void resetTextures() {
		for (Texture texture : textures.values()) {
			texture.release();
		}
		cacheTextureFiles.clear();
		textures.clear();
	}
	
	public static Texture getTextureFromUrl(String url) {
		return getTextureFromUrl(null, url);
	}
	
	public static Texture getTextureFromUrl(String plugin, String url) {
		File texture = getTextureFile(plugin, url);
		if (!texture.exists()) {
			return null;
		}
		try {
			return getTextureFromPath(texture.getCanonicalPath());
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
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
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		
		File download = new File(FileUtil.getTextureCacheDirectory(), FileUtil.getFileName(url));
		try {
			String path = download.getCanonicalPath();
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
			return path;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		if (wasSandboxed) {
			SpoutClient.enableSandbox();
		}
		
		return null;
	}
}