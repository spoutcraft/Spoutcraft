package net.minecraft.src;

import java.awt.Canvas;
import java.awt.Dimension;

class CanvasCrashReport extends Canvas {
	public CanvasCrashReport(int i) {
		setPreferredSize(new Dimension(i, i));
		setMinimumSize(new Dimension(i, i));
	}
}
