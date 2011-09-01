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
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class CustomTextureManager {
	static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	static HashMap<String, File> cacheTextureFiles = new HashMap<String, File>();
	
	public static void downloadTexture(String url) {
		downloadTexture(null, url);
	}
	
	public static void downloadTexture(String plugin, String url) {
		String fileName = FileUtil.getFileName(url);
		if (!FileUtil.isImageFile(fileName)) {
			System.out.println("Rejecting download of invalid texture: " + fileName);
			return;
		}
		if (!isTextureDownloading(url) && !isTextureDownloaded(plugin, url)) {
			Download download = new Download(fileName, FileUtil.getTextureCacheDirectory(), url, null);
			FileDownloadThread.getInstance().addToDownloadQueue(download);
		}
	}
	
	public static boolean isTextureDownloading(String url) {
		return FileDownloadThread.getInstance().isDownloading(url);
	}
	
	public static boolean isTextureDownloaded(String url) {
		return (getTextureFile(null, url)).exists();
	}
	
	public static boolean isTextureDownloaded(String plugin, String url) {
		return (getTextureFile(plugin, url)).exists();
	}
	
	public static File getTextureFile(String plugin, String url) {
		String fileName = FileUtil.getFileName(url);
		File cache = cacheTextureFiles.get(fileName);
		if (cache != null) {
			return cache;
		}
		if (plugin != null) {
			File file = FileUtil.findTextureFile(plugin, fileName);
			if (file != null) {
				cacheTextureFiles.put(fileName, file);
				return file;
			}
		}
		return new File(FileUtil.getTextureCacheDirectory(), fileName);
	}
	
	public static Texture getTextureFromPath(String path) {
		if (!textures.containsKey(path)) {
			Texture texture = null;
			try {
				texture = TextureLoader.getTexture("PNG", new FileInputStream(path), true);
			}
			catch (IOException e) { }
			if (texture == null) {
				System.out.println("Error loading texture: " + path);
				return null;
			}
			textures.put(path, texture);
		}
		return textures.get(path);
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
		File download = new File(FileUtil.getTextureCacheDirectory(), FileUtil.getFileName(url));
		try {
			return download.getCanonicalPath();
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}