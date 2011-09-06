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

public class BubbleBar extends GenericWidget implements Widget{
	private int icons = 10;
	private int iconOffset = 8;
	public BubbleBar() {
		super();
		setX(427 / 2 - 91); //122
		setY(199);
		setAnchor(WidgetAnchor.BOTTOM_CENTER);
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 8;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setMaxNumBubbles(input.readInt());
		setIconOffset(input.readInt());
	}
	
	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(getMaxNumBubbles());
		output.writeInt(getIconOffset());
	}
	
	public WidgetType getType() {
		return WidgetType.BubbleBar;
	}
	
	public UUID getId() {
		return new UUID(0, 1);
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
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}
	

	/**
	 * Gets the maximum number of bubbles displayed on the HUD.
	 * 
	 * Air is scaled to fit the number of bubbles appropriately.
	 * @return bubbles displayed
	 */
	public int getMaxNumBubbles() {
		return icons;
	}
	
	/**
	 * Sets the maximum number of bubbles displayed on the HUD.
	 * 
	 * Air is scaled to fit the number of bubbles appropriately.
	 * @param bubbles to display
	 * @return this
	 */
	public BubbleBar setMaxNumBubbles(int bubbles) {
		this.icons = bubbles;
		return this;
	}
	
	/**
	 * Gets the number of pixels each bubbles is offset when drawing the next bubble.
	 * @return pixel offset
	 */
	public int getIconOffset() {
		return iconOffset;
	}
	
	/**
	 * Sets the number of pixels each bubbles is offset when drawing the next bubble.
	 * @param offset when drawing hearts
	 * @return this
	 */
	public BubbleBar setIconOffset(int offset) {
		iconOffset = offset;
		return this;
	}
	
	@Override
	public int getVersion() {
		return super.getVersion() + 1;
	}
}
