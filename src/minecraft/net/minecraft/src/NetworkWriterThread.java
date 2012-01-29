package net.minecraft.src;

import java.io.DataOutputStream;
import java.io.IOException;

class NetworkWriterThread extends Thread {
	final NetworkManager netManager;

	NetworkWriterThread(NetworkManager networkmanager, String s) {
		super(s);
		netManager = networkmanager;
	}

	public void run() {
		synchronized (NetworkManager.threadSyncObject) {
			NetworkManager.numWriteThreads++;
		}
		try {
			while (NetworkManager.isRunning(netManager)) {
				while (NetworkManager.sendNetworkPacket(netManager)) ;
				try {
					if (NetworkManager.getOutputStream(netManager) != null) {
						NetworkManager.getOutputStream(netManager).flush();
					}
				}
				catch (IOException ioexception) {
					if (!NetworkManager.getIsTerminating(netManager)) {
						NetworkManager.func_30005_a(netManager, ioexception);
					}
					ioexception.printStackTrace();
				}
				try {
					sleep(2L);
				}
				catch (InterruptedException interruptedexception) { }
			}
		}
		finally {
			synchronized (NetworkManager.threadSyncObject) {
				NetworkManager.numWriteThreads--;
			}
		}
	}
}
