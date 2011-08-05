package org.getspout.spout.io;

import java.io.File;
import java.io.IOException;

public class CustomTextureManager {
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