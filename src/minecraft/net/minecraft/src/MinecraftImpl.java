package net.minecraft.src;

import java.awt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;

public final class MinecraftImpl extends Minecraft {
	final Frame mcFrame;

	public MinecraftImpl(Component component, Canvas canvas, MinecraftApplet minecraftapplet, int i, int j, boolean flag, Frame frame) {
		super(component, canvas, minecraftapplet, i, j, flag);
		mcFrame = frame;
	}

	public void displayUnexpectedThrowable(UnexpectedThrowable unexpectedthrowable) {
		mcFrame.removeAll();
		mcFrame.add(new PanelCrashReport(unexpectedthrowable), "Center");
		mcFrame.validate();
	}
}
