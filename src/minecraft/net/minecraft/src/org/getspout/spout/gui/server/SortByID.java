package org.getspout.spout.gui.server;

import java.util.Comparator;

public class SortByID implements Comparator {

	public int compare(Object server, Object server2) {
		if (server instanceof mcSBServer && server2 instanceof mcSBServer) {
			return ((mcSBServer)server).ID - (((mcSBServer)server2).ID);
		}
		return -1;
	}
}
