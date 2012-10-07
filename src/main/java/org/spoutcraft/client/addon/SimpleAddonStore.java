/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spoutcraft.client.addon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.addon.AddonStore;
import org.spoutcraft.api.addon.AddonStore.DownloadEventDelegate;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.FileUtil;

public class SimpleAddonStore implements AddonStore {
	private HashMap<String, AddonInfo> addons = new HashMap<String, AddonInfo>();
	private boolean loading = false;

	public void downloadAddon(int databaseId, DownloadEventDelegate delegate) {
		// TODO Download the addon
		// TODO Create new instance of AddonInfo with the databaseId and register here.
		/* Addon addon = Spoutcraft.getAddonManager().loadAddon(addonFile);
		AddonInfo info = new AddonInfo(addon);
		info.setDatabaseId(databaseId);
		addons.put(addon, info);
		save(); */
	}

	public void downloadAddon(String name, DownloadEventDelegate delegate) {
		// TODO Get database ID and call
		//downloadAddon(databaseId, delegate);
	}

	public boolean hasUpdate(Addon addon) {
		AddonInfo info = getAddonInfo(addon);
		if (info != null) {
			return info.hasUpdate();
		} else {
			return false;
		}
	}

	public boolean hasInternetAccess(Addon addon) {
		AddonInfo info = getAddonInfo(addon);
		if (info != null) {
			return info.hasInternetAccess();
		} else {
			return false;
		}
	}

	public long getQuota(Addon addon) {
		AddonInfo info = getAddonInfo(addon);
		if (info != null) {
			return info.getQuota();
		} else {
			return 0;
		}
	}

	public boolean isEnabled(Addon addon) {
		AddonInfo info = getAddonInfo(addon);
		if (info != null) {
			return info.isEnabled();
		} else {
			return false;
		}
	}

	public AddonInfo getAddonInfo(Addon addon) {
		String name = addon.getDescription().getName();
		if (addons.containsKey(name)) {
			AddonInfo info = addons.get(name);
			info.setAddon(addon);
			return info;
		} else {
			AddonInfo info = new AddonInfo(name);
			info.setAddon(addon);
			addons.put(name, info);
			save();
			return info;
		}
	}

	public void load() {
		loading = true;
		Yaml yaml = new Yaml();
		try {
			FileReader reader = null;
			reader = new FileReader(getDataFile());
			List<Object> data = (List<Object>) yaml.load(reader);
			for (Object obj:data) {
				try {
					HashMap<String, Object> item = (HashMap<String, Object>) obj;
					String name = (String) item.get("name");
					AddonInfo info = new AddonInfo(name);
					info.setDatabaseId((Integer) item.get("databaseId"));
					info.setQuota((Integer) item.get("quota"));
					info.setEnabled((Boolean) item.get("enabled"));
					info.setHasInternetAccess((Boolean) item.get("internetAccess"));
					addons.put(name, info);
				} catch (ClassCastException ignore) {
					ignore.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
		catch (FileNotFoundException ignore) {}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			loading = false;
		}
	}

	public void save() {
		if (loading) {
			return;
		}
		List<Object> data = new ArrayList<Object>();
		for (AddonInfo info:addons.values()) {
			HashMap<String, Object> item = new HashMap<String, Object>();

			item.put("databaseId", info.getDatabaseId());
			//Identified by name
			if (info.getAddon() == null) {
				continue;
			}
			item.put("name", info.getAddon().getDescription().getName());
			item.put("internetAccess", info.hasInternetAccess());
			item.put("quota", info.getQuota());
			item.put("enabled", info.isEnabled());

			data.add(item);
		}
		Yaml yaml = new Yaml();
		try {
			FileWriter writer = new FileWriter(getDataFile());
			yaml.dump(data, writer);
		} catch(IOException ignore) {}
	}

	private File getDataFile() {
		return new File(FileUtil.getConfigDir(), "addoninfo.yml");
	}
}
