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
	
	/**
	 * Gets the maximum number of hearts displayed on the HUD.
	 * 
	 * Health is scaled to fit the number of hearts appropriately.
	 * @return hearts displayed
	 */
	public int getMaxNumHearts() {
		return icons;
	}
	
	/**
	 * Sets the maximum number of hearts displayed on the HUD.
	 * 
	 * Health is scaled to fit the number of hearts appropriately.
	 * @param hearts to display
	 * @return this
	 */
	public HealthBar setMaxNumHearts(int hearts) {
		this.icons = hearts;
		return this;
	}
	
	/**
	 * Gets the number of pixels each heart is offset when drawing the next heart.
	 * @return pixel offset
	 */
	public int getIconOffset() {
		return iconOffset;
	}
	
	/**
	 * Sets the number of pixels each heart is offset when drawing the next heart.
	 * @param offset when drawing hearts
	 * @return this
	 */
	public HealthBar setIconOffset(int offset) {
		iconOffset = offset;
		return this;
	}
	
	/**
	 * Gets the percent of health a player needs to fall to or below in order for the hearts to begin blinking.
	 * 
	 * Valid percents are between zero and one hundred, inclusive.
	 * @return danger percent
	 */
	public float getDangerPercent() {
		return dangerPercent;
	}
	
	/**
	 * Sets the percent of health a player needs to fall to or below in order for the hearts to begin blinking.
	 * 
	 * Valid percents are between zero and one hundred, inclusive.
	 * @param percent
	 * @return this
	 */
	public HealthBar setDangerPercent(float percent) {
		dangerPercent = percent;
		return this;
	}

}
