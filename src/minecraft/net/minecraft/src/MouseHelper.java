package net.minecraft.src;

import java.awt.Component;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

public class MouseHelper {
	private Component windowComponent;
	private Cursor cursor;
	public int deltaX;
	public int deltaY;
	private int field_1115_e;

	public MouseHelper(Component component) {
		field_1115_e = 10;
		windowComponent = component;
		IntBuffer intbuffer = GLAllocation.createDirectIntBuffer(1);
		intbuffer.put(0);
		intbuffer.flip();
		IntBuffer intbuffer1 = GLAllocation.createDirectIntBuffer(1024);
		try {
			cursor = new Cursor(32, 32, 16, 16, 1, intbuffer1, intbuffer);
		}
		catch (LWJGLException lwjglexception) {
			lwjglexception.printStackTrace();
		}
	}

	public void grabMouseCursor() {
		Mouse.setGrabbed(true);
		deltaX = 0;
		deltaY = 0;
	}

	public void ungrabMouseCursor() {
		Mouse.setCursorPosition(windowComponent.getWidth() / 2, windowComponent.getHeight() / 2);
		Mouse.setGrabbed(false);
	}

	public void mouseXYChange() {
		deltaX = Mouse.getDX();
		deltaY = Mouse.getDY();
	}
}
