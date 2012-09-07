package org.spoutcraft.client.gui.server;

import org.spoutcraft.client.SpoutClient;

import net.minecraft.src.ServerData;

/**
 * Extends ServerData to store additional spout server details. 
 *
 */
public class SpoutServerData extends ServerData {

	private ServerItem serverItem;
	
	/**
	 * Construct more closely matches Minecraft ServerData
	 * @param name - Server Name
	 * @param ip - Server ip and port in ip:port format
	 */
	public SpoutServerData(String name, String ip) {
		super(name, ip);
	}

	/**
	 * Construct more closely matches Spoutcraft data from a ServerItem
	 * @param name - Server Name
	 * @param ip - Ip Address
	 * @param port - port
	 * @param uid - databaseId
	 */
	public SpoutServerData(String name, ServerItem serverItem) {
		this(name, serverItem.getIp() + ":" + String.valueOf(serverItem.getPort()));
		setServerItem(serverItem);
		if (serverItem.getAcceptsTextures() != null) setAcceptsTextures(serverItem.getAcceptsTextures());
	}
	
	@Override
	public void setAcceptsTextures(boolean accepts) {
		this.getServerItem().setAcceptsTextures(accepts);
		super.setAcceptsTextures(accepts);
	}
	
	/**
	 * Sets the serveritem entry for this server
	 * @param uid
	 */
	public void setServerItem(ServerItem serverItem) {
		this.serverItem = serverItem;
	}
	
	/**
	 * Gets the serveritem entry for this server
	 * @return
	 */
	public ServerItem getServerItem() {
		return serverItem;
	}
}
