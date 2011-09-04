package org.spoutcraft.spoutcraftapi;

public final class Spoutcraft {
	
	private static Client client = null;
	
	public static void setClient(Client argClient) {
		if (client != null) {
			throw new UnsupportedOperationException("Cannot redefine singleton Client");
		}
		client = argClient;
	}
	
}
