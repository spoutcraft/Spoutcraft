package org.getspout.spout.gui.server;

import java.util.Comparator;

public class SortByName implements Comparator {

	public int compare(Object server, Object server2) {
		if (server instanceof ServerSlot && server2 instanceof ServerSlot) {
			return ((ServerSlot)server).name.compareTo(((ServerSlot)server2).name);
		}
		return -1;
	}
}
