package org.getspout.spout.gui.server;

import java.util.Comparator;

public class SortByMax implements Comparator {

	public int compare(Object server, Object server2) {
		if (server instanceof mcSBServer && server2 instanceof mcSBServer) {
			return ((mcSBServer)server).maxPlayers - (((mcSBServer)server2).maxPlayers);
		}
		return -1;
	}
}
