package net.minecraft.src;

import java.awt.Canvas;
import net.minecraft.client.MinecraftApplet;

public class CanvasMinecraftApplet extends Canvas {
	final MinecraftApplet mcApplet;

	public CanvasMinecraftApplet(MinecraftApplet minecraftapplet) {
		mcApplet = minecraftapplet;
	}

	public synchronized void addNotify() {
		super.addNotify();
		mcApplet.startMainThread();
	}

	public synchronized void removeNotify() {
		mcApplet.shutdown();
		super.removeNotify();
	}
}
