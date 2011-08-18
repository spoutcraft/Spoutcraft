package org.getspout.spout.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class CustomTextureManager {
	static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	
	public static void downloadTexture(String url) {
		String fileName = FileUtil.getFileName(url);
		if (!FileUtil.isImageFile(fileName)) {
			System.out.println("Rejecting download of invalid texture: " + fileName);
			return;
		}
		if (!isTextureDownloading(url) && !isTextureDownloaded(url)) {
			Download download = new Download(fileName, FileUtil.getTextureCacheDirectory(), url, null);
			FileDownloadThread.getInstance().addToDownloadQueue(download);
		}
	}
	
	public static boolean isTextureDownloading(String url) {
		return FileDownloadThread.getInstance().isDownloading(url);
	}
	
	public static boolean isTextureDownloaded(String Url) {
		return (new File(FileUtil.getTextureCacheDirectory(), FileUtil.getFileName(Url))).exists();
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
	
	public static String getTextureFromUrl(String Url) {
		if (!isTextureDownloaded(Url)) {
			return null;
		}
		File download = new File(FileUtil.getTextureCacheDirectory(), FileUtil.getFileName(Url));
		try {
			return download.getCanonicalPath();
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}