package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class ThreadSleepForever extends Thread {
	final Minecraft mc;

	public ThreadSleepForever(Minecraft minecraft, String s) {
		super(s);
		mc = minecraft;
		setDaemon(true);
		start();
	}

	public void run() {
		while (mc.running) {
			try {
				Thread.sleep(0x7fffffffL);
			}
			catch (InterruptedException interruptedexception) { }
		}
	}
}
