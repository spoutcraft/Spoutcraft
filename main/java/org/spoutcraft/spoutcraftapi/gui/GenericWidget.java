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

import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public abstract class GenericWidget implements Widget {
	protected int upperLeftX = 0;
	protected int upperLeftY = 0;
	protected int width = 0;
	protected int height = 0;
	protected boolean visible = true;
	protected transient boolean dirty = true;
	protected transient Screen screen = null;
	protected RenderPriority priority = RenderPriority.Normal;
	protected UUID id = UUID.randomUUID();
	protected String tooltip = "";
	protected WidgetAnchor anchor = WidgetAnchor.SCALE;
	protected String plugin;

	public GenericWidget() {

	}

	public int getNumBytes() {
		return 38 + PacketUtil.getNumBytes(tooltip) + PacketUtil.getNumBytes(plugin);
	}

	public int getVersion() {
		return 3;
	}

	public GenericWidget(int upperLeftX, int upperLeftY, int width, int height) {
		this.upperLeftX = upperLeftX;
		this.upperLeftY = upperLeftY;
		this.width = width;
		this.height = height;
	}

	public Widget setAnchor(WidgetAnchor anchor) {
		this.anchor = anchor;
		return this;
	}

	public WidgetAnchor getAnchor() {
		return anchor;
	}

	public void readData(DataInputStream input) throws IOException {
		setX(input.readInt());
		setY(input.readInt());
		setWidth(input.readInt());
		setHeight(input.readInt());
		setAnchor(WidgetAnchor.getAnchorFromId(input.readByte()));
		setVisible(input.readBoolean());
		setPriority(RenderPriority.getRenderPriorityFromId(input.readInt()));
		long msb = input.readLong();
		long lsb = input.readLong();
		this.id = new UUID(msb, lsb);
		setTooltip(PacketUtil.readString(input));
		setPlugin(PacketUtil.readString(input));
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(getX());
		output.writeInt(getY());
		output.writeInt((int) getActualWidth());
		output.writeInt((int) getActualHeight());
		output.writeByte(getAnchor().getId());
		output.writeBoolean(isVisible());
		output.writeInt(priority.getId());
		output.writeLong(getId().getMostSignificantBits());
		output.writeLong(getId().getLeastSignificantBits());
		PacketUtil.writeString(output, getTooltip());
		PacketUtil.writeString(output, getPlugin());
	}

	public String getPlugin() {
		return plugin;
	}

	public Widget setPlugin(String plugin) {
		this.plugin = plugin;
		return this;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public boolean isDirty() {
		return dirty;
	}

	public UUID getId() {
		return id;
	}

	public Screen getScreen() {
		return screen;
	}

	public Widget setScreen(Screen screen) {
		this.screen = screen;
		return this;
	}

	public RenderPriority getPriority() {
		return priority;
	}

	public Widget setPriority(RenderPriority priority) {
		this.priority = priority;
		return this;
	}

	public double getActualWidth() {
		return width;
	}

	public Widget setWidth(int width) {
		this.width = width;
		return this;
	}

	public double getActualHeight() {
		return height;
	}

	public double getWidth() {
		return anchor == WidgetAnchor.SCALE ? (getActualWidth() * (getScreen() != null ? (getScreen().getWidth() / 427f) : 1)) : getActualWidth();
	}

	public double getHeight() {
		return anchor == WidgetAnchor.SCALE ? (getActualHeight() * (getScreen() != null ? (getScreen().getHeight() / 240f) : 1)) : getActualHeight();
	}

	public Widget setHeight(int height) {
		this.height = height;
		return this;
	}

	public int getX() {
		return upperLeftX;
	}

	public int getY() {
		return upperLeftY;
	}

	public double getScreenX() {
		double left = upperLeftX * (anchor == WidgetAnchor.SCALE ? (getScreen() != null ? (getScreen().getWidth() / 427f) : 1) : 1);
		switch (anchor) {
			case TOP_CENTER:
			case CENTER_CENTER:
			case BOTTOM_CENTER:
				left += getScreen().getWidth() / 2;
				break;
			case TOP_RIGHT:
			case CENTER_RIGHT:
			case BOTTOM_RIGHT:
				left += getScreen().getWidth();
				break;
		}
		return left;
	}

	public double getScreenY() {
		double top = upperLeftY * (anchor == WidgetAnchor.SCALE ? (getScreen() != null ? (getScreen().getHeight() / 240f) : 1) : 1);
		switch (anchor) {
			case CENTER_LEFT:
			case CENTER_CENTER:
			case CENTER_RIGHT:
				top += getScreen().getHeight() / 2;
				break;
			case BOTTOM_LEFT:
			case BOTTOM_CENTER:
			case BOTTOM_RIGHT:
				top += getScreen().getHeight();
				break;
		}
		return top;
	}

	public Widget setX(int pos) {
		this.upperLeftX = pos;
		return this;
	}

	public Widget setY(int pos) {
		this.upperLeftY = pos;
		return this;
	}

	public Widget shiftXPos(int modX) {
		setX(getX() + modX);
		return this;
	}

	public Widget shiftYPos(int modY) {
		setY(getY() + modY);
		return this;
	}

	public boolean isVisible() {
		return visible;
	}

	public Widget setVisible(boolean enable) {
		visible = enable;
		return this;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Widget && other.hashCode() == hashCode();
	}

	public void onTick() {

	}

	public Widget setTooltip(String tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	public String getTooltip() {
		return tooltip;
	}

	public Widget copy() {
		try {
			Widget copy = getType().getWidgetClass().newInstance();
			copy.setX(getX()).setY(getY()).setWidth((int) getActualWidth()).setHeight((int) getActualHeight()).setVisible(isVisible());
			copy.setPriority(getPriority()).setTooltip(getTooltip()).setAnchor(getAnchor());
			return copy;
		}
		catch (Exception e) {
			throw new IllegalStateException("Unable to create a copy of " + getClass() + ". Does it have a valid widget type?");
		}
	}
}
