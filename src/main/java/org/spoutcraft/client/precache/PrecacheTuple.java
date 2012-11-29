package org.spoutcraft.client.precache;

public class PrecacheTuple {
	
	private String plugin;
	private String version;
	private long crc;
	
	/**
	 * A tuple for zip -> crc
	 * @param plugin - Name of the plugin
	 * @param version - plugin version
	 * @param crc - crc of the precache file
	 */
	public PrecacheTuple(String plugin, String version, long crc) {
		this.plugin = plugin;
		this.version = version;
		this.crc = crc;
	}
	
	public String getPlugin() {
		return plugin;
	}
	
	public String getVersion() {
		return version;
	}
	
	public long getCrc() {
		return crc;
	}
	
	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public void setCrc(long crc) {
		this.crc = crc;
	}
	
}
