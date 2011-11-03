package org.getspout.spout.gui.server;

import net.minecraft.src.GuiConnecting;

import org.getspout.spout.client.SpoutClient;

public class ServerManager {
	private FavoritesModel favorites;
	private ServerListModel serverList;
	
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
	public void join(ServerItem serverItem) {
		join(serverItem.getIp(), serverItem.getPort());
	}

	/**
	 * Joins the server on the given ip:port
	 * @param ip
	 * @param port
	 */
	public void join(String ip, int port) {
		SpoutClient.getHandle().displayGuiScreen(new GuiConnecting(SpoutClient.getHandle(), ip, port));
	}
}
