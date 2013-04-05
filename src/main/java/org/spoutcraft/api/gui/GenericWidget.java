/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
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
package org.spoutcraft.api.gui;

import java.io.IOException;
import java.util.UUID;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.UnsafeClass;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.api.property.Property;
import org.spoutcraft.api.property.PropertyObject;

@UnsafeClass
public abstract class GenericWidget extends PropertyObject implements Widget {
	/**
	 * Set if this is Spoutcraft (client), cleared if it is Spout (server)...
	 */
	static final protected transient boolean isSpoutcraft = true;
	protected Rectangle geometry = new Rectangle(0, 0, 0, 0);
	protected boolean visible = true;
	protected transient Screen screen = null;
	protected RenderPriority priority = RenderPriority.Normal;
	protected UUID id = UUID.randomUUID();
	protected String tooltip = "";
	protected WidgetAnchor anchor = WidgetAnchor.TOP_LEFT;
	protected String addon = "Spoutcraft";
	// Client side layout
	protected Container container = null;
	protected boolean fixed = false;
	protected int marginTop = 0, marginRight = 0, marginBottom = 0, marginLeft = 0;
	protected int minWidth = 0, maxWidth = 427, minHeight = 0, maxHeight = 240;
	protected int orig_x = 0, orig_y = 0;
	// Animation
	protected WidgetAnim animType = WidgetAnim.NONE;
	protected float animValue = 1f;
	protected short animCount = 0;
	protected short animTicks = 20;
	protected final byte ANIM_REPEAT = (1<<0);
	protected final byte ANIM_RESET = (1<<1);
	protected final byte ANIM_RUNNING = (1<<2);
	protected final byte ANIM_STOPPING = (1<<3);
	protected byte animFlags = 0;
	protected transient int animTick = 0; // Current tick
	protected transient int animFrame = 0; // Current frame
	protected transient int animStart = 0; // Start value per type

	public GenericWidget() {
		initProperties();
	}

	private void initProperties() {
		addProperty("geometry", new Property() {
			public void set(Object value) {
				setGeometry((Rectangle) value);
			}

			public Object get() {
				return getGeometry();
			}
		});
	}

	final public boolean isSpoutcraft() {
		return isSpoutcraft;
	}

	public int getVersion() {
		return 6;
	}

	public GenericWidget(int X, int Y, int width, int height) {
		setGeometry(new Rectangle(X, Y, width, height));
		initProperties();
	}

	public Widget setAnchor(WidgetAnchor anchor) {
		this.anchor = anchor;
		return this;
	}

	public WidgetAnchor getAnchor() {
		return anchor;
	}

	public void readData(SpoutInputStream input) throws IOException {
		setX(input.readInt());
		setY(input.readInt());
		setWidth(input.readInt());
		setHeight(input.readInt());
		setAnchor(WidgetAnchor.getAnchorFromId(input.read()));
		setVisible(input.readBoolean());
		setPriority(RenderPriority.getRenderPriorityFromId(input.readInt()));
		setTooltip(input.readString());
		setAddon(input.readString());
		setAddon(addon);
		animType = WidgetAnim.getAnimationFromId(input.read());
		animFlags = (byte) input.read();
		animValue = input.readFloat();
		animTicks = input.readShort();
		animCount = input.readShort();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(getX());
		output.writeInt(getY());
		output.writeInt((int) getActualWidth());
		output.writeInt((int) getActualHeight());
		output.write(getAnchor().getId());
		output.writeBoolean(isVisible());
		output.writeInt(priority.getId());
		output.writeString(getTooltip());
		output.writeString(getAddon());
		output.write(animType.getId());
		output.write(animFlags);
		output.writeFloat(animValue);
		output.writeShort(animTicks);
		output.writeShort(animCount);
	}

	public String getAddon() {
		return addon;
	}

	public Widget setAddon(String addon) {
		if (addon != null) {
			this.addon = addon;
		}
		return this;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Screen getScreen() {
		return screen;
	}

	public Widget setScreen(Screen screen) {
//		System.out.println("Screen of " +this + " set to " + screen);
		this.screen = screen;
		return this;
	}

	public Widget setScreen(String addon, Screen screen) {
		if (this.screen != null && screen != null && screen != this.screen) {
			this.screen.removeWidget(this);
		}
		this.screen = screen;
		if (addon != null) {
			this.addon = addon;
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
		return getGeometry().getWidth();
	}

	public Widget setWidth(int width) {
		getGeometry().setWidth(width);
		return this;
	}

	public double getActualHeight() {
		return getGeometry().getHeight();
	}

	public double getWidth() {
		return anchor == WidgetAnchor.SCALE ? (getActualWidth() * (getScreen() != null ? (getScreen().getWidth() / 427f) : 1)) : getActualWidth();
	}

	public double getHeight() {
		return anchor == WidgetAnchor.SCALE ? (getActualHeight() * (getScreen() != null ? (getScreen().getHeight() / 240f) : 1)) : getActualHeight();
	}

	public Widget setHeight(int height) {
		getGeometry().setHeight(height);
		return this;
	}

	public int getX() {
		return getGeometry().getX();
	}

	public int getY() {
		return getGeometry().getY();
	}

	public double getScreenX() {
		double left = getX() * (anchor == WidgetAnchor.SCALE ? (getScreen() != null ? (getScreen().getWidth() / 427f) : 1) : 1);
		switch (anchor) {
			case TOP_CENTER:
			case CENTER_CENTER:
			case BOTTOM_CENTER:
				left += getScreenWidth() / 2;
				break;
			case TOP_RIGHT:
			case CENTER_RIGHT:
			case BOTTOM_RIGHT:
				left += getScreenWidth();
				break;
		}
		return left;
	}

	public double getScreenY() {
		double top = getY() * (anchor == WidgetAnchor.SCALE ? (getScreen() != null ? (getScreen().getHeight() / 240f) : 1) : 1);
		switch (anchor) {
			case CENTER_LEFT:
			case CENTER_CENTER:
			case CENTER_RIGHT:
				top += getScreenHeight() / 2;
				break;
			case BOTTOM_LEFT:
			case BOTTOM_CENTER:
			case BOTTOM_RIGHT:
				top += getScreenHeight();
				break;
		}
		return top;
	}

	private double getScreenWidth() {
		if (getScreen() == null) {
			return Spoutcraft.getRenderDelegate().getScreenWidth();
		}
		return getScreen().getWidth();
	}

	private double getScreenHeight() {
		if (getScreen() == null) {
			return Spoutcraft.getRenderDelegate().getScreenHeight();
		}
		return getScreen().getHeight();
	}

	public Widget setX(int pos) {
		getGeometry().setX(pos);
		return this;
	}

	public Widget setY(int pos) {
		getGeometry().setY(pos);
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

	public Widget setTooltip(String t) {
		tooltip = t;
		return this;
	}

	public String getTooltip() {
		return tooltip;
	}

	public Container getContainer() {
		return container;
	}

	public boolean hasContainer() {
		return container != null;
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
			setWidth((int) getWidth()); // Enforce our new size if needed
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
			setWidth((int) getWidth()); // Enforce our new size if needed
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
			setHeight((int) getHeight()); // Enforce our new size if needed
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
			setHeight((int) getHeight()); // Enforce our new size if needed
		}
		return this;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public Widget savePos() {
		orig_x = getX();
		orig_y = getY();
		return this;
	}

	public Widget restorePos() {
		setX(orig_x);
		setY(orig_y);
		return this;
	}

	public Widget copy() {
		try {
			Widget copy = getType().getWidgetClass().newInstance();
			copy.setGeometry(getGeometry().clone()) //
					.setVisible(isVisible()) //
					.setPriority(getPriority()) //
					.setTooltip(getTooltip()) //
					.setAnchor(getAnchor()) //
					.setMargin(getMarginTop(), getMarginRight(), getMarginBottom(), getMarginLeft()) //
					.setMinWidth(getMinWidth()) //
					.setMaxWidth(getMaxWidth()) //
					.setMinHeight(getMinHeight()) //
					.setMaxHeight(getMaxHeight()) //
					.setFixed(isFixed()) //
					.animate(animType, animValue, animCount, animTicks, (animFlags & ANIM_REPEAT) != 0, (animFlags & ANIM_RESET) != 0);
			return copy;
		} catch (Exception e) {
			throw new IllegalStateException("Unable to create a copy of " + getClass() + ". Does it have a valid widget type?");
		}
	}

	public Widget updateSize() {
		if (container != null) {
			container.updateSize();
		}
		return this;
	}

	public double getActualX() {
		if (screen == null) {
			return getScreenX();
		} else {
			if (screen instanceof ScrollArea) {
				ScrollArea sa = (ScrollArea) screen;
				return getScreenX() - sa.getScrollPosition(Orientation.HORIZONTAL) + sa.getScreenX();
			} else {
				return getScreenX();// + screen.getScreenX();
			}
		}
	}

	public double getActualY() {
		if (screen == null) {
			return getScreenY();
		} else {
			if (screen instanceof ScrollArea) {
				ScrollArea sa = (ScrollArea) screen;
				return getScreenY() - sa.getScrollPosition(Orientation.VERTICAL) + sa.getScreenY();
			} else {
				return getScreenY();// + screen.getScreenY();
			}
		}
	}

	public Widget animate(WidgetAnim type, float value, short count, short ticks) {
		animate(type, value, count, ticks, true, true);
		return this;
	}

	public Widget animate(WidgetAnim type, float value, short count, short ticks, boolean repeat) {
		animate(type, value, count, ticks, repeat, true);
		return this;
	}

	public Widget animate(WidgetAnim type, float value, short count, short ticks, boolean repeat, boolean reset) {
		if (!type.check(this)) {
			throw new UnsupportedOperationException("Cannot use Animation." + type.name() + " on " + getType().toString());
		}
		animType = type;
		animValue = value;
		animCount = count;
		animTicks = ticks;
		animFlags = (byte) ((repeat ? ANIM_REPEAT : 0) | (reset ? ANIM_RESET : 0));
		animTick = 0;
		animFrame = 0;
		return this;
	}

	public Widget animateStart() {
		if (animType != WidgetAnim.NONE) {
			animFlags |= ANIM_RUNNING;
			switch (animType) {
				case POS_X:
					animStart = getX();
					break;
				case POS_Y:
					animStart = getY();
					break;
				case WIDTH:
					animStart = (int) getWidth();
					break;
				case HEIGHT:
					animStart = (int) getHeight();
					break;
				case OFFSET_LEFT:
					animStart = ((Texture) this).getLeft();
					break;
				case OFFSET_TOP:
					animStart = ((Texture) this).getTop();
					break;
			}
			animStart -= animFrame * animCount;
		}
		return this;
	}

	public Widget animateStop(boolean finish) {
		if ((animFlags & ANIM_RUNNING) != 0 && finish) {
			animFlags |= ANIM_STOPPING;
		} else {
			animFlags &= ~ANIM_RUNNING;
		}
		return this;
	}

	public void onAnimate() {
		if ((animFlags & ANIM_RUNNING) == 0 || animTicks == 0 || ++animTick % animTicks != 0) {
			return;
		}
		// We're running, and it's ready for our next frame...
		if (++animFrame == animCount) {
			animFrame = 0;
			if ((animFlags & ANIM_STOPPING) != 0 || (animFlags & ANIM_REPEAT) == 0) {
				animFlags &= ~ANIM_RUNNING;
				if ((animFlags & ANIM_RESET) == 0) {
					return;
				}
			}
		}
		int value = animStart + (int)Math.floor(animFrame * animValue);
		switch (animType) {
			case POS_X:
				setX(value);
				break;
			case POS_Y:
				setY(value);
				break;
			case WIDTH:
				setWidth(value);
				break;
			case HEIGHT:
				setHeight(value);
				break;
			case OFFSET_LEFT:
				((Texture) this).setLeft(value);
				break;
			case OFFSET_TOP:
				((Texture) this).setTop(value);
				break;
		}
	}

	public void onAnimateStop() {
	}

	public Rectangle getGeometry() {
		return geometry;
	}

	public Widget setGeometry(Rectangle geometry) {
		this.geometry = geometry;
		return this;
	}

	public Widget setGeometry(int x, int y, int width, int height) {
		getGeometry().setX(x);
		getGeometry().setY(y);
		getGeometry().setWidth(width);
		getGeometry().setHeight(height);
		return this;
	}
}
