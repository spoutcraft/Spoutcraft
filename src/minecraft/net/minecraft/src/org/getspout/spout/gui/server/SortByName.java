package org.getspout.spout.gui.server;

import java.util.Comparator;

public class SortByName implements Comparator {

	public int compare(Object server, Object server2) {
		if (server instanceof mcSBServer && server2 instanceof mcSBServer) {
			return ((mcSBServer)server).name.compareTo(((mcSBServer)server2).name);
		}
		return -1;
	}
}
