package net.minecraft.src;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
// Spout Start
import org.spoutcraft.client.SpoutClient;
// Spout End

public final class GameWindowListener extends WindowAdapter {
	public void windowClosing(WindowEvent par1WindowEvent) {
		// Spout Start
		SpoutClient.getHandle().shutdown();

		try {
			SpoutClient.getHandle().mainThread.join(10000L);
		} catch (InterruptedException var4) {
		}
		System.exit(0);
		// Spout End
	}
}
