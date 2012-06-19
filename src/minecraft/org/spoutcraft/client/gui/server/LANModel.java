package org.spoutcraft.client.gui.server;

import java.io.IOException;
import java.util.Iterator;

import javax.jmdns.JmDNS;

public class LANModel extends ServerModel {
	protected JmDNS dns;
	private static final String SERVICE = "_pipework._tcp.local.";

	public LANModel() {
		try {
			dns = JmDNS.create();
			dns.addServiceListener(SERVICE, new MinecraftServiceListener(this));
			System.out.println("Listening for ZeroConf Service '"+SERVICE+"' ...");
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
		while (iter.hasNext()) {
			ServerItem item = iter.next();
			if (unqalifiedServiceName.equals(item.getTitle())) {
				iter.remove();
				return;
			}
		}
	}
}
