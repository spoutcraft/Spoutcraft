package org.spoutcraft.client.gui.server;

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
		System.out.println("Service Added "+arg0);
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				model.dns.requestServiceInfo(arg0.getType(), arg0.getName(), 5000);
			}
		}, 200);
	}

	@Override
	public void serviceRemoved(ServiceEvent arg0) {
		System.out.println("Service Removed "+arg0);
		model.removeItem(arg0.getName());
	}

	@Override
	public void serviceResolved(ServiceEvent arg0) {
		System.out.println("Service Resolved "+arg0);
		ServerItem item = new ServerItem(arg0.getName(), arg0.getInfo().getHostAddress(), arg0.getInfo().getPort(), -1);
		model.addItem(item);
	}
}
