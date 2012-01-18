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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import org.bukkit.util.config.Configuration;

public class MirrorUtils {
	private static boolean updated = false;
	private static File mirrorsYML = new File(FileUtil.getSpoutcraftDirectory(), "mirrors.yml");
	private static final Random rand = new Random();

	static {
		updated = mirrorsYML.exists();
	}

	public static String getMirrorUrl(String mirrorURI, String fallbackUrl) {
		try {
			Map<String, Integer> mirrors = getMirrors();
			Set<Entry<String, Integer>> set = mirrors.entrySet();

			ArrayList<String> goodMirrors = new ArrayList<String>(mirrors.size());
			Iterator<Entry<String, Integer>> iterator = set.iterator();
			while (iterator.hasNext()) {
				Entry<String, Integer> e = iterator.next();
				String mirror = "http://" + e.getKey() + "/" + mirrorURI;
				if (isAddressReachable(mirror)) {
					goodMirrors.add(e.getKey());
				}
			}

			//safe fast return
			if (goodMirrors.size() == 1) {
				return "http://" + goodMirrors.get(0) + "/" + mirrorURI;
			}

			//the for loop may fail if random numbers are unlucky, in which case we want to try again
			while (goodMirrors.size() > 0) {
				int random = rand.nextInt(10 * mirrors.size());
				int index = random / 10;
				for (int i = index; i < goodMirrors.size() + index; i++) {
					int j = i;
					if (j >= goodMirrors.size()) j-= goodMirrors.size();
					int roll = rand.nextInt(100);
					int chance = mirrors.get(goodMirrors.get(j));
					if (roll < chance) {
						String mirror = "http://" + goodMirrors.get(j) + "/" + mirrorURI;
						return mirror;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fallbackUrl;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Integer> getMirrors() {
		Configuration config = getMirrorsYML();
		return (Map<String, Integer>) config.getProperty("mirrors");
	}

	public static boolean isAddressReachable(String url) {
		try {
			URL test = new URL(url);
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection urlConnect = (HttpURLConnection)test.openConnection();
			urlConnect.setRequestMethod("HEAD");
			return (urlConnect.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			return false;
		}
	}

	public static Configuration getMirrorsYML() {
		updateMirrorsYMLCache();
		Configuration config = new Configuration(mirrorsYML);
		config.load();
		return config;
	}

	public static void updateMirrorsYMLCache() {
		if (!updated) {
			try {
				URL url = new URL("http://get.spout.org/mirrors.yml");
				HttpURLConnection con = (HttpURLConnection)(url.openConnection());
				System.setProperty("http.agent", "");
				con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30");
				OutputStream os = new FileOutputStream(mirrorsYML);
				InputStream is = con.getInputStream();
				IOUtils.copy(is, os);
				is.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			updated = true;
		}
	}
}
