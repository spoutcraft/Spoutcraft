package net.minecraft.src;

class NetworkReaderThread extends Thread {
	final NetworkManager netManager;

	NetworkReaderThread(NetworkManager networkmanager, String s) {
		super(s);
		netManager = networkmanager;
	}

	public void run() {
		synchronized (NetworkManager.threadSyncObject) {
			NetworkManager.numReadThreads++;
		}
		try {
			while (NetworkManager.isRunning(netManager) && !NetworkManager.isServerTerminating(netManager)) {
				while (NetworkManager.readNetworkPacket(netManager)) ;
				try {
					sleep(2L);
				}
				catch (InterruptedException interruptedexception) { }
			}
		}
		finally {
			synchronized (NetworkManager.threadSyncObject) {
				NetworkManager.numReadThreads--;
			}
		}
	}
}
