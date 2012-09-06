package net.minecraft.src;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import net.minecraft.client.Minecraft;
import org.spoutcraft.client.SpoutClient;

public final class GameWindowListener extends WindowAdapter {

	public void windowClosing(WindowEvent par1WindowEvent) {
		SpoutClient.getHandle().shutdown();

		try {
			SpoutClient.getHandle().mainThread.join(10000L);
		} catch (InterruptedException var4) {
			try {
				SpoutClient.getHandle().shutdownMinecraftApplet();
			} catch (Exception var3) {
				var3.printStackTrace();
			}
		}
		System.exit(0);
	}
}
