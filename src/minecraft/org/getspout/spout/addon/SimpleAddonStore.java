package org.getspout.spout.addon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.FileUtil;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.addon.AddonStore;
import org.spoutcraft.spoutcraftapi.addon.AddonStore.DownloadEventDelegate;
import org.yaml.snakeyaml.Yaml;

public class SimpleAddonStore implements AddonStore {

	private HashMap<String, AddonInfo> addons = new HashMap<String, AddonInfo>();
	private boolean loading = false;

	public void downloadAddon(int databaseId, DownloadEventDelegate delegate) {
		//TODO download the addon
		//TODO create new instance of AddonInfo with the databaseId and register here.
//		Addon addon = Spoutcraft.getAddonManager().loadAddon(addonFile);
//		AddonInfo info = new AddonInfo(addon);
//		info.setDatabaseId(databaseId);
//		addons.put(addon, info);
//		save();
	}

	public void downloadAddon(String name, DownloadEventDelegate delegate) {
		// TODO get database-id and call
//		downloadAddon(databaseId, delegate);
	}

	public boolean hasUpdate(Addon addon) {
		AddonInfo info = getAddonInfo(addon);
		if(info != null) {
			return info.hasUpdate();
		} else {
			return false;
		}
	}

	public boolean hasInternetAccess(Addon addon) {
		AddonInfo info = getAddonInfo(addon);
		if(info != null) {
			return info.hasInternetAccess();
		} else {
			return false;
		}
	}

	public long getQuota(Addon addon) {
		AddonInfo info = getAddonInfo(addon);
		if(info != null) {
			return info.getQuota();
		} else {
			return 0;
		}
	}

	public boolean isEnabled(Addon addon) {
		AddonInfo info = getAddonInfo(addon);
		if(info != null) {
			return info.isEnabled();
		} else {
			return false;
		}
	}

	public AddonInfo getAddonInfo(Addon addon) {
		String name = addon.getDescription().getName();
		if(addons.containsKey(name)) {
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
			for(Object obj:data) {
				try {
					HashMap<String, Object> item = (HashMap<String, Object>) obj;
					String name = (String) item.get("name");
					AddonInfo info = new AddonInfo(name);
					info.setDatabaseId((Integer) item.get("databaseId"));
					info.setQuota((Integer) item.get("quota"));
					info.setEnabled((Boolean) item.get("enabled"));
					info.setHasInternetAccess((Boolean) item.get("internetAccess"));
					addons.put(name, info);
					System.out.println("Loaded addon info for "+name);
					
				} catch (ClassCastException ignore) {
					ignore.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException ignore) {
			ignore.printStackTrace();
		} finally {
			loading = false;
		}
	}

	public void save() {
		if(loading) {
			return;
		}
		List<Object> data = new ArrayList<Object>();
		for(AddonInfo info:addons.values()) {
			HashMap<String, Object> item = new HashMap<String, Object>();

			item.put("databaseId", info.getDatabaseId());
			//Identified by name
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
		return new File(FileUtil.getSpoutcraftDirectory(), "addoninfo.yml");
	}

}
