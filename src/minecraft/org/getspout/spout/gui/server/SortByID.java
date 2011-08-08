package org.getspout.spout.gui.server;

import java.util.Comparator;

public class SortByID implements Comparator {

	public int compare(Object server, Object server2) {
		if (server instanceof ServerSlot && server2 instanceof ServerSlot) {
			return ((ServerSlot)server).ID - (((ServerSlot)server2).ID);
		}
		return -1;
	}
}
