package org.getspout.spout.gui.server;

public class ServerSlot implements Comparable {

	public int ID = 0;
	public String ip = "";
	public String port = "";
	public String name = "";
	public int players = 0;
	public int maxPlayers = 0;
	public String country = "";


	public ServerSlot(int ID) {
		this.ID = ID;
	}

	public int compareTo(ServerSlot other) {
		return this.players - other.players;
	}

	// $FF: synthetic method
	// $FF: bridge method
	public int compareTo(Object other) {
		return this.compareTo((ServerSlot)other);
	}
}
