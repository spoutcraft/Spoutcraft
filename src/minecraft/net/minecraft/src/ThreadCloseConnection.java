package net.minecraft.src;

class ThreadCloseConnection extends Thread {
	final NetworkManager netManager;

	ThreadCloseConnection(NetworkManager networkmanager) {
		netManager = networkmanager;
	}

	public void run() {
		try {
			Thread.sleep(2000L);
			if (NetworkManager.isRunning(netManager)) {
				NetworkManager.getWriteThread(netManager).interrupt();
				netManager.networkShutdown("disconnect.closed", new Object[0]);
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
