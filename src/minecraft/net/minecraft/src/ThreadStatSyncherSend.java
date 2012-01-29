package net.minecraft.src;

import java.util.Map;

class ThreadStatSyncherSend extends Thread {
	final Map field_27233_a;
	final StatsSyncher syncher;

	ThreadStatSyncherSend(StatsSyncher statssyncher, Map map) {
		syncher = statssyncher;
		field_27233_a = map;
	}

	public void run() {
		try {
			StatsSyncher.func_27412_a(syncher, field_27233_a, StatsSyncher.getUnsentDataFile(syncher), StatsSyncher.getUnsentTempFile(syncher), StatsSyncher.getUnsentOldFile(syncher));
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		finally {
			StatsSyncher.setBusy(syncher, false);
		}
	}
}
