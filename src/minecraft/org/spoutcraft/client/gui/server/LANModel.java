package org.spoutcraft.client.gui.server;

import java.io.IOException;
import java.util.Iterator;

import javax.jmdns.JmDNS;

public class LANModel extends ServerModel {
	protected JmDNS dns;
	
	public LANModel() {
		try {
			dns = JmDNS.create();
			dns.addServiceListener("_pipework._tcp.local.", new MinecraftServiceListener(this));
			System.out.println("Listening for service...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void addItem(ServerItem item) {
		items.add(item);
		item.poll();
	}
	
	protected void removeItem(String unqalifiedServiceName) {
		Iterator<ServerItem> iter = items.iterator();
		while(iter.hasNext()) {
			ServerItem item = iter.next();
			if(unqalifiedServiceName.equals(item.getTitle())) {
				iter.remove();
				return;
			}
		}
	}

}
