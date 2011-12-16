package org.spoutcraft.client.addon;

import org.spoutcraft.client.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.addon.Addon;

public class AddonInfo {
	private Addon addon;
	private int databaseId = -1;
	private boolean internetAccess;
	private boolean enabled = true;
	private long quota = 0;
	private boolean updateAvailable = false;
	private String name = "";
	
	public AddonInfo(String name) {
		this.name = name;
	}
	
	public Addon getAddon() {
		return addon;
	}
	
	public void setAddon(Addon addon) {
		this.addon = addon;
		SpoutClient.getInstance().getAddonStore().save();
	}
	
	public int getDatabaseId() {
		return databaseId;
	}
	
	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
		SpoutClient.getInstance().getAddonStore().save();
	}
	
	public boolean hasInternetAccess() {
		return internetAccess;
	}
	
	public void setHasInternetAccess(boolean mayAccessInternet) {
		this.internetAccess = mayAccessInternet;
		SpoutClient.getInstance().getAddonStore().save();
	}
	
	public long getQuota() {
		return quota;
	}
	
	public void setQuota(long quota) {
		this.quota = quota;
		SpoutClient.getInstance().getAddonStore().save();
	}
	
	public boolean hasUpdate() {
		return updateAvailable;
	}
	
	public void setHasUpdate(boolean updateAvailable) {
		this.updateAvailable = updateAvailable;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		SpoutClient.getInstance().getAddonStore().save();
	}

	public boolean isEnabled() {
		return enabled;
	}
}
