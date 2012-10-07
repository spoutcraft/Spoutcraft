/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
			System.out.println("Listening for ZeroConf Service '" + SERVICE + "' ...");
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
