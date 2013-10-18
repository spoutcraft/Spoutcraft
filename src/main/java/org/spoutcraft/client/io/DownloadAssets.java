/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
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
package org.spoutcraft.client.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.spoutcraft.client.io.FileUtil;

public class DownloadAssets {
	
	public static void getHttpAssets() {
		// Get web services assets
		downloadFile(FileUtil.getConfigDir(), "vip.yml", "http://get.spout.org/vip.yml", true);
		downloadFile(FileUtil.getConfigDir(), "special.yml", "http://get.spout.org/special.yml", false);
		downloadFile(FileUtil.getConfigDir(), "splashes.txt", "http://cdn.spout.org/splashes.txt", false);
	}
	
	public static void importOldConfig() {
		// This method must execute before constructores creates blank ones.
		if (!(new File(FileUtil.getConfigDir() + "/bindings.yml").exists())) {
			// Needs Bindings
		}
		
		if (!(new File(FileUtil.getConfigDir() + "/favorites.yml").exists())) {
			// Needs Favorites
		}
		
		if (!(new File(FileUtil.getConfigDir() + "/minimap.yml").exists())) {
			// Needs MiniMap Config
		}
		
		if (!(new File(FileUtil.getConfigDir() + "/shortcuts.yml").exists())) {
			// Needs Shortcuts
		}
	}
		
	public static void threadedDownloadFile(File destination, String filename, String url) {		
		Download download = new Download(filename, destination, url, null);
		FileDownloadThread.getInstance().addToDownloadQueue(download);
	}
	
	public static void downloadFile(File destination, String filename, String url, boolean forceOverwrite) {
		File getFile = new File(destination, File.separator + filename);
		// Refresh every day if file exists and not forced overwrite.
		if (!getFile.exists() || forceOverwrite || (System.currentTimeMillis() - getFile.lastModified() > (1L * 24 * 60 * 60 * 1000))) {
			try {
				URL test = new URL(url);
				HttpURLConnection urlConnect = (HttpURLConnection) test.openConnection();
				System.setProperty("http.agent", "");
				urlConnect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.162 Safari/535.19");
				File temp = new File(FileUtil.getConfigDir(), "getFile.temp");
				if (temp.exists()) {
					temp.delete();
				}

				getFile.delete();

				FileUtils.copyInputStreamToFile(urlConnect.getInputStream(), temp);
				FileUtils.moveFile(temp, getFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
