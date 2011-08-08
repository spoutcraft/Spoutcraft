package org.getspout.spout.gui.server;

import java.util.Comparator;

public class SortByMax implements Comparator {

	public int compare(Object server, Object server2) {
		if (server instanceof ServerSlot && server2 instanceof ServerSlot) {
			return ((ServerSlot)server).maxPlayers - (((ServerSlot)server2).maxPlayers);
		}
		return -1;
	}
}
