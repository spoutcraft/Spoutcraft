package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class HealthBar extends GenericWidget {
	private int icons = 10;
	private int iconOffset = 8;
	private float dangerPercent = 20f; 

	public HealthBar() {
		super();
		setX(427 / 2 - 91); //157
		setY(208);
	}
	
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setX(427 / 2 - 91);
		setY(208);
	}
	
	public WidgetType getType() {
		return WidgetType.HealthBar;
	}
	
	@Override
	public UUID getId() {
		return new UUID(0, 4);
	}
	
	@Override
	public double getScreenX() {
		if (getX() == 157) {
			return getScreen().getWidth() / 2 - 91;
		}
		return super.getScreenX();
	}
	
	@Override
	public double getScreenY() {
		if (getY() == 208) {
			return getScreen().getHeight() - 32;
		}
		return super.getScreenY();
	}
	
	public void render() {
		
	}
	
	public int getMaxNumHearts() {
		return icons;
	}
	
	public HealthBar setMaxNumHearts(int icons) {
		this.icons = icons;
		return this;
	}
	
	public int getIconOffset() {
		return iconOffset;
	}
	
	public HealthBar setIconOffset(int offset) {
		iconOffset = offset;
		return this;
	}
	
	public float getDangerPercent() {
		return dangerPercent;
	}
	
	public HealthBar setDangerPercent(float percent) {
		dangerPercent = percent;
		return this;
	}

}
