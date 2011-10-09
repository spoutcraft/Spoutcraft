/*
 * This file is part of Spoutcraft API (http://wiki.getspout.org/).
 * 
 * Spoutcraft API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft API is distributed in the hope that it will be useful,
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
	protected int X = 0;
	protected int Y = 0;
	protected int width = 0;
	protected int height = 0;
	protected boolean visible = true;
	protected transient boolean dirty = true;
	protected transient Screen screen = null;
	protected RenderPriority priority = RenderPriority.Normal;
	protected UUID id = UUID.randomUUID();
	protected String tooltip = "";
	protected WidgetAnchor anchor = WidgetAnchor.SCALE;
	protected String plugin = "Spoutcraft";
	// Client side layout
	protected Container container = null;
	protected boolean fixed = false;
	protected int marginTop = 0, marginRight = 0, marginBottom = 0, marginLeft = 0;
	protected int minWidth = 0, maxWidth = 427, minHeight = 0, maxHeight = 240;
	protected int orig_x = 0, orig_y = 0;

	public GenericWidget() {

	}

	public int getNumBytes() {
		return 38 + PacketUtil.getNumBytes(tooltip) + PacketUtil.getNumBytes(plugin);
	}

	public int getVersion() {
		return 3;
	}

	public GenericWidget(int X, int Y, int width, int height) {
		this.X = X;
		this.Y = Y;
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
	
	public Widget setScreen(String plugin, Screen screen) {
		if (this.screen != null && screen != null && screen != this.screen) {
			this.screen.removeWidget(this);
		}
		this.screen = screen;
		if (plugin != null) {
			this.plugin = plugin;
		}
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
		return X;
	}

	public int getY() {
		return Y;
	}

	public double getScreenX() {
		double left = X * (anchor == WidgetAnchor.SCALE ? (getScreen() != null ? (getScreen().getWidth() / 427f) : 1) : 1);
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
		double top = Y * (anchor == WidgetAnchor.SCALE ? (getScreen() != null ? (getScreen().getHeight() / 240f) : 1) : 1);
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
		this.X = pos;
		return this;
	}

	public Widget setY(int pos) {
		this.Y = pos;
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
	
	public Widget setTooltip(String t){
		tooltip = t;
		return this;
	}
	
	public String getTooltip(){
		return tooltip;
	}
	
	public Container getContainer() {
		return container;
	}
	
	public void setContainer(Container container) {
		if (this.container != null && container != null && container != this.container) {
			this.container.removeChild(this);
		}
		this.container = container;
	}

	public Widget setFixed(boolean fixed) {
		if (this.fixed != fixed) {
			this.fixed = fixed;
			updateSize();
		}
		return this;
	}

	public boolean isFixed() {
		return fixed;
	}

	public Widget setMargin(int marginAll) {
		return setMargin(marginAll, marginAll, marginAll, marginAll);
	}
	
	public Widget setMargin(int marginTopBottom, int marginLeftRight) {
		return setMargin(marginTopBottom, marginLeftRight, marginTopBottom, marginLeftRight);
	}
	
	public Widget setMargin(int marginTop, int marginLeftRight, int marginBottom) {
		return setMargin(marginTop, marginLeftRight, marginBottom, marginLeftRight);
	}
	
	public Widget setMargin(int marginTop, int marginRight, int marginBottom, int marginLeft) {
		if (this.marginTop != marginTop || this.marginRight != marginRight || this.marginBottom != marginBottom || this.marginLeft != marginLeft) {
			this.marginTop = marginTop;
			this.marginRight = marginRight;
			this.marginBottom = marginBottom;
			this.marginLeft = marginLeft;
			updateSize();
		}
		return this;
	}

	public Widget setMarginTop(int marginTop) {
		if (this.marginTop != marginTop) {
			this.marginTop = marginTop;
			updateSize();
		}
		return this;
	}

	public Widget setMarginRight(int marginRight) {
		if (this.marginRight != marginRight) {
			this.marginRight = marginRight;
			updateSize();
		}
		return this;
	}

	public Widget setMarginBottom(int marginBottom) {
		if (this.marginBottom != marginBottom) {
			this.marginBottom = marginBottom;
			updateSize();
		}
		return this;
	}

	public Widget setMarginLeft(int marginLeft) {
		if (this.marginLeft != marginLeft) {
			this.marginLeft = marginLeft;
			updateSize();
		}
		return this;
	}

	public int getMarginTop() {
		return marginTop;
	}
	
	public int getMarginRight() {
		return marginRight;
	}
	
	public int getMarginBottom() {
		return marginBottom;
	}
	
	public int getMarginLeft() {
		return marginLeft;
	}

	public Widget setMinWidth(int min) {
		min = Math.max(min, 0);
		if (minWidth != min) {
			minWidth = min;
			updateSize();
			setWidth(width); // Enforce our new size if needed
		}
		return this;
	}

	public int getMinWidth() {
		return minWidth;
	}

	public Widget setMaxWidth(int max) {
		max = max <= 0 ? 427 : max;
		if (maxWidth != max) {
			maxWidth = max;
			updateSize();
			setWidth(width); // Enforce our new size if needed
		}
		return this;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public Widget setMinHeight(int min) {
		min = Math.max(min, 0);
		if (minHeight != min) {
			minHeight = min;
			updateSize();
			setHeight(height); // Enforce our new size if needed
		}
		return this;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public Widget setMaxHeight(int max) {
		max = max <= 0 ? 240 : max;
		if (maxHeight != max) {
			maxHeight = max;
			updateSize();
			setHeight(height); // Enforce our new size if needed
		}
		return this;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public Widget savePos() {
		orig_x = X;
		orig_y = Y;
		return this;
	}

	public Widget restorePos() {
		X = orig_x;
		Y = orig_y;
		return this;
	}
	
	public Widget copy() {
		try {
			Widget copy = getType().getWidgetClass().newInstance();
			copy.setX(getX()).setY(getY()).setWidth((int) getWidth()).setHeight((int) getHeight()).setVisible(isVisible());
			copy.setPriority(getPriority()).setTooltip(getTooltip()).setAnchor(getAnchor());
			copy.setMargin(getMarginTop(), getMarginRight(), getMarginBottom(), getMarginLeft());
			copy.setMinWidth(getMinWidth()).setMaxWidth(getMaxWidth()).setMinHeight(getMinHeight()).setMaxHeight(getMaxHeight());
			copy.setFixed(isFixed());
			return copy;
		}
		catch (Exception e) {
			throw new IllegalStateException("Unable to create a copy of " + getClass() + ". Does it have a valid widget type?");
		}
	}

	public Widget updateSize() {
		if (container != null) {
			container.updateSize();
		}
		return this;
	}
}
