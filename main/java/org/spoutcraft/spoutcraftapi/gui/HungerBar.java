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
package org.spoutcraft.spoutcraftapi.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.spoutcraft.spoutcraftapi.Spoutcraft;

public class HungerBar extends GenericWidget {
	private int icons = 10;
	private int iconOffset = 8;
	private int updateCounter = 0;

	public HungerBar() {
		super();
		setX(427 / 2 + 82); // 295
		setY(201);
		setAnchor(WidgetAnchor.BOTTOM_CENTER);
	}

	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 8;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setNumOfIcons(input.readInt());
		setIconOffset(input.readInt());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(getNumOfIcons());
		output.writeInt(getIconOffset());
	}

	@Override
	public double getScreenX() {
		double mid = getScreen() != null ? getScreen().getWidth() / 2 : 427 / 2D;
		double diff = super.getScreenX() - mid - 376;
		return getScreen() != null ? getScreen().getWidth() / 2D - diff : this.getX();
	}

	@Override
	public double getScreenY() {
		int diff = (int) (240 - this.getY());
		return getScreen() != null ? getScreen().getHeight() - diff : this.getY();
	}

	/**
	 * Gets the maximum number of food icons to display on the HUD.
	 * 
	 * Hunger bar is scaled to fit the number of icons appropriately.
	 * 
	 * @return icons displayed
	 */
	public int getNumOfIcons() {
		return icons;
	}

	/**
	 * Sets the maximum number of food icons to display on the HUD.
	 * 
	 * Hunger bar is scaled to fit the number of icons appropriately.
	 * 
	 * @param icons to display
	 * @return this
	 */
	public HungerBar setNumOfIcons(int icons) {
		this.icons = icons;
		return this;
	}

	/**
	 * Gets the number of pixels each icon is offset when drawing the next icon.
	 * 
	 * @return pixel offset
	 */
	public int getIconOffset() {
		return iconOffset;
	}

	/**
	 * Sets the number of pixels each icon is offset when drawing the next icon.
	 * 
	 * @param iconOffset when drawing icons
	 * @return this
	 */
	public HungerBar setIconOffset(int iconOffset) {
		this.iconOffset = iconOffset;
		return this;
	}

	@Override
	public void onTick() {
		super.onTick();
		updateCounter++;
	}

	public int getUpdateCounter() {
		return updateCounter;
	}

	public WidgetType getType() {
		return WidgetType.HungerBar;
	}

	public UUID getId() {
		return new UUID(0, 5);
	}

	public void render() {
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}

	@Override
	public int getVersion() {
		return super.getVersion() + 1;
	}
}
