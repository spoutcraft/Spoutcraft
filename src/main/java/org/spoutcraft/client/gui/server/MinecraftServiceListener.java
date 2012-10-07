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

import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

public class MinecraftServiceListener implements ServiceListener {
	private LANModel model;
	private Timer timer;

	public MinecraftServiceListener(LANModel lanModel) {
		this.model = lanModel;
		this.timer = new Timer();
	}

	@Override
	public void serviceAdded(final ServiceEvent arg0) {
		//System.out.println("Service Added " + arg0);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				model.dns.requestServiceInfo(arg0.getType(), arg0.getName(), 5000);
			}
		}, 200);
	}

	@Override
	public void serviceRemoved(ServiceEvent arg0) {
		//System.out.println("Service Removed " + arg0);
		model.removeItem(arg0.getName());
	}

	@Override
	public void serviceResolved(ServiceEvent arg0) {
		//System.out.println("Service Resolved " + arg0);
		InetAddress[] addresses = arg0.getInfo().getInetAddresses();
		if (addresses.length > 0) {
			InetAddress address = addresses[0];
			ServerItem item = new ServerItem(arg0.getName(), address.getHostAddress(), arg0.getInfo().getPort(), -1);
			model.addItem(item);
		}
	}
}
