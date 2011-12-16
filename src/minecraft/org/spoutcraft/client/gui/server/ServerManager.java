package org.spoutcraft.client.gui.server;

import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiScreen;

import org.spoutcraft.client.SpoutClient;

public class ServerManager {
	private FavoritesModel favorites;
	private ServerListModel serverList;
	private GuiScreen joinedFrom;
	private String screenName;
	
	public void init() {
		favorites = new FavoritesModel();
		favorites.load();
		serverList = new ServerListModel();
	}
	
	public FavoritesModel getFavorites() {
		return favorites;
	}
	
	public ServerListModel getServerList() {
		return serverList;
	}

	/**
	 * Joins the server given by the serverItem
	 * @param serverItem
	 */
	public void join(ServerItem serverItem, GuiScreen from, String name) {
		join(serverItem.getIp(), serverItem.getPort(), from, name);
	}

	/**
	 * Joins the server on the given ip:port
	 * @param ip
	 * @param port
	 */
	public void join(String ip, int port, GuiScreen from, String name) {
		this.joinedFrom = from;
		this.screenName = name;
		SpoutClient.getHandle().displayGuiScreen(new GuiConnecting(SpoutClient.getHandle(), ip, port));
	}
	
	public GuiScreen getJoinedFrom() {
		return joinedFrom;
	}
	
	public String getJoinedFromName() {
		return screenName;
	}
}
