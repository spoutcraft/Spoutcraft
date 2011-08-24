/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiIngame;

public class HealthBar extends GenericWidget {
	private int icons = 10;
	private int iconOffset = 8;
	private float dangerPercent = 20f; 

	public HealthBar() {
		super();
		setX(427 / 2 - 91); //122
		setY(208);
		setAnchor(WidgetAnchor.BOTTOM_CENTER);
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 12;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setMaxNumHearts(input.readInt());
		setIconOffset(input.readInt());
		setDangerPercent(input.readFloat());
	}
	
	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(getMaxNumHearts());
		output.writeInt(getIconOffset());
		output.writeFloat(getDangerPercent());
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
		double mid = getScreen() != null ? getScreen().getWidth() / 2 : 427 / 2D;
		double diff = super.getScreenX() - mid - 31;
		return getScreen() != null ? getScreen().getWidth() / 2D - diff : this.getX();
	}
	
	@Override
	public double getScreenY() {
		int diff = (int) (240 - this.getY());
		return getScreen() != null ? getScreen().getHeight() - diff : this.getY();
	}
	
	public void render() {
		int health = Minecraft.theMinecraft.thePlayer.health;
		boolean whiteOutlinedHearts = Minecraft.theMinecraft.thePlayer.heartsLife / 3 % 2 == 1;
		if(Minecraft.theMinecraft.thePlayer.heartsLife < 10) {
			whiteOutlinedHearts = false;
		}
		float healthPercent = health / 0.2f;
		int y = (int)getScreenY();
		if (healthPercent <= getDangerPercent()) {
			y += GuiIngame.rand.nextInt(2);
		}
		float healthPercentPerIcon = 100f / getMaxNumHearts();
		if (isVisible() && getMaxNumHearts() > 0) {
			for (int icon = 0; icon < getMaxNumHearts(); ++icon) {
				boolean full = (icon + 1) * healthPercentPerIcon <= healthPercent;
				boolean half = (icon + 1) * healthPercentPerIcon < healthPercent + healthPercentPerIcon;
				int x = (int)getScreenX() + icon * getIconOffset();
				
				Gui.drawStaticTexturedModalRect(x, y, 16 + (whiteOutlinedHearts ? 1 : 0) * 9, 0, 9, 9, 0f);
				if (whiteOutlinedHearts) {
					if (full) {
						Gui.drawStaticTexturedModalRect(x, y, 70, 0, 9, 9, 0f);
					}
					else if (half) {
						Gui.drawStaticTexturedModalRect(x, y, 79, 0, 9, 9, 0f);
					}
				}

				if (full) {
					Gui.drawStaticTexturedModalRect(x, y, 52, 0, 9, 9, 0f);
				}
				else if (half) {
					Gui.drawStaticTexturedModalRect(x, y, 61, 0, 9, 9, 0f);
				}

			}
		}
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
	
	@Override
	public int getVersion() {
		return super.getVersion() + 1;
	}

}
