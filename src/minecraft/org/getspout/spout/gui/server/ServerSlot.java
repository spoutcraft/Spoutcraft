package org.getspout.spout.gui.server;

public class ServerSlot implements Comparable {

	public int ID = 0;
	public String ip = "";
	public String port = "";
	public String name = "";
	public int players = 0;
	public int maxPlayers = 0;
	public String country = "";


	public ServerSlot(int var1) {
		this.ID = var1;
	}

	public int compareTo(ServerSlot var1) {
		return var1.players - this.players;
	}

	// $FF: synthetic method
	// $FF: bridge method
	public int compareTo(Object var1) {
		return this.compareTo((ServerSlot)var1);
	}
}
