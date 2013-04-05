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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.spoutcraft.api.UnsafeClass;

@UnsafeClass
public class GenericContainer extends GenericWidget implements Container {
	protected List<Widget> children = new ArrayList<Widget>();
	protected ContainerType type = ContainerType.VERTICAL;
	protected WidgetAnchor align = WidgetAnchor.TOP_LEFT;
	protected boolean reverse = false;
	protected int minWidthCalc = 0, maxWidthCalc = 427, minHeightCalc = 0, maxHeightCalc = 240;
	protected boolean auto = true;
	protected boolean recalculating = false;
	protected boolean needsLayout = true;

	public GenericContainer() {
	}

	public GenericContainer(Widget... children) {
		// Shortcuts because we don't have any of the insertChild values setup yet
		this.children.addAll(Arrays.asList(children));
		for (Widget child : children) {
			child.setContainer(this);
		}
		updateSize();
	}

	public Container addChild(Widget child) {
		return insertChild(-1, child);
	}

	public Container insertChild(int index, Widget child) {
		if (index < 0 || index > this.children.size()) {
			this.children.add(child);
		} else {
			this.children.add(index, child);
		}
		child.setContainer(this);
		child.savePos();
		child.shiftXPos(super.getX());
		child.shiftYPos(super.getY());
		child.setAnchor(super.getAnchor());
		// Relay out if we are already using layout - otherwise this will return immediately
		if (getScreen() != null) {
			String addon = child.getAddon();
			getScreen().attachWidget(addon, child);
		}
		updateSize();
		deferLayout();
		return this;
	}

	public Container addChildren(Widget... children) {
		for (Widget child : children) {
			this.insertChild(-1, child);
		}
		return this;
	}

	public Widget[] getChildren() {
		Widget[] list = new Widget[children.size()];
		children.toArray(list);
		return list;
	}

	@Override
	public Container setVisible(boolean enable) {
		super.setVisible(enable);
		for (Widget widget : children) {
			widget.setVisible(enable);
		}
		return this;
	}

	@Override
	public Container setPriority(RenderPriority priority) {
		super.setPriority(priority);
		for (Widget widget : children) {
			widget.setPriority(priority);
		}
		return this;
	}

	@Override
	public Container setAnchor(WidgetAnchor anchor) {
		super.setAnchor(anchor);
		for (Widget widget : children) {
			widget.setAnchor(anchor);
		}
		return this;
	}

	public WidgetType getType() {
		return WidgetType.Container;
	}

	@Override
	public Widget setX(int pos) {
		int delta = pos - super.getX();
		super.setX(pos);
		for (Widget widget : children) {
			widget.shiftXPos(delta);
		}
		return this;
	}

	@Override
	public Widget setY(int pos) {
		int delta = pos - super.getY();
		super.setY(pos);
		for (Widget widget : children) {
			widget.shiftYPos(delta);
		}
		return this;
	}

	public Container removeChild(Widget child) {
		children.remove(child);
		child.setContainer(null);
		child.restorePos();
		if (this.screen != null) {
			this.screen.removeWidget(child);
		}
		updateSize();
		deferLayout();
		return this;
	}

	@Override
	public Container setScreen(Screen screen) {
		for (Widget child : children) {
			if (screen != null) {
				screen.attachWidget(getAddon(), child);
			} else if (this.screen != null) {
				this.screen.removeWidget(child);
			}
		}
		super.setScreen(screen);
		return this;
	}

	@Override
	public Container setHeight(int height) {
		if (super.getHeight() != height) {
			super.setHeight(height);
			this.deferLayout();
		}
		return this;
	}

	@Override
	public Container setWidth(int width) {
		if (super.getWidth() != width) {
			super.setWidth(width);
			this.deferLayout();
		}
		return this;
	}

	public Container setLayout(ContainerType type) {
		if (this.type != type) {
			this.type = type;
			deferLayout();
		}
		return this;
	}

	public ContainerType getLayout() {
		return type;
	}

	public Container setAlign(WidgetAnchor align) {
		if (this.align != align) {
			this.align = align;
			deferLayout();
		}
		return this;
	}

	public WidgetAnchor getAlign() {
		return align;
	}

	public Container setReverse(boolean reverse) {
		if (this.reverse != reverse) {
			this.reverse = reverse;
			deferLayout();
		}
		return this;
	}

	public boolean getReverse() {
		return reverse;
	}

	public Container deferLayout() {
		needsLayout = true;
		return this;
	}

	public Container updateLayout() {
		if (!recalculating && super.getWidth() > 0 && super.getHeight() > 0 && !children.isEmpty()) {
			recalculating = true; // Prevent us from getting into a loop
			List<Widget> visibleChildren = new ArrayList<Widget>();
			int totalwidth = 0, totalheight = 0, newwidth, newheight, vcount = 0, hcount = 0;
			int availableWidth = (int) (auto ? getWidth() : getMinWidth()), availableHeight = (int) (auto ? getHeight() : getMinHeight());
			// We only layout visible children, invisible ones have zero physical presence on screen
			for (Widget widget : children) {
				if (widget.isVisible()) {
					visibleChildren.add(widget);
				}
			}
			// Reverse drawing order if we need to
			if (reverse) {
				Collections.reverse(visibleChildren);
			}
			// First - get the total space by fixed widgets and borders
			if (type == ContainerType.OVERLAY) {
				newwidth = availableWidth;
				newheight = availableHeight;
			} else {
				for (Widget widget : visibleChildren) {
					int horiz = widget.getMarginLeft() + widget.getMarginRight();
					int vert = widget.getMarginTop() + widget.getMarginBottom();
					if (widget.isFixed()) {
						horiz += widget.getWidth();
						vert += widget.getHeight();
					}
					if (type == ContainerType.VERTICAL) {
						totalheight += vert;
						if (!widget.isFixed()) {
							vcount++;
						}
					} else if (type == ContainerType.HORIZONTAL) {
						totalwidth += horiz;
						if (!widget.isFixed()) {
							hcount++;
						}
					}
				}
				// Work out the width and height for children
				newwidth = (availableWidth - totalwidth) / Math.max(1, hcount);
				newheight = (availableHeight - totalheight) / Math.max(1, vcount);
				// Deal with minWidth and minHeight - change newwidth/newheight if needed
				for (Widget widget : visibleChildren) {
					if (!widget.isFixed()) {
						if (type == ContainerType.VERTICAL) {
							if (widget.getMinHeight() > newheight) {
								totalheight += widget.getMinHeight() - newheight;
								newheight = (availableHeight - totalheight) / Math.max(1, vcount);
							} else if (newheight >= widget.getMaxHeight()) {
								totalheight += widget.getMaxHeight();
								vcount--;
								newheight = (availableHeight - totalheight) / Math.max(1, vcount);
							}
						} else if (type == ContainerType.HORIZONTAL) {
							if (widget.getMinWidth() > newwidth) {
								totalwidth += widget.getMinWidth() - newwidth;
								newwidth = (availableWidth - totalwidth) / Math.max(1, hcount);
							} else if (newwidth >= widget.getMaxWidth()) {
								totalwidth += widget.getMaxWidth();
								hcount--;
								newwidth = (availableWidth - totalwidth) / Math.max(1, hcount);
							}
						}
					}
				}
				newheight = Math.max(newheight, 0);
				newwidth = Math.max(newwidth, 0);
			}
			totalheight = totalwidth = 0;
			// Resize any non-fixed widgets
			for (Widget widget : visibleChildren) {
				if (!widget.isFixed()) {
					int realheight, realwidth;
					if (auto) {
						realheight = Math.max(widget.getMinHeight(), Math.min(newheight - widget.getMarginTop() - widget.getMarginBottom(), widget.getMaxHeight()));
						realwidth = Math.max(widget.getMinWidth(), Math.min(newwidth - widget.getMarginLeft() - widget.getMarginRight(), widget.getMaxWidth()));
					} else {
						realheight = widget.getMinHeight() == 0 ? newheight - widget.getMarginTop() - widget.getMarginBottom() : widget.getMinHeight();
						realwidth = widget.getMinWidth() == 0 ? newwidth - widget.getMarginLeft() - widget.getMarginRight() : widget.getMinWidth();
					}
					if (widget.getHeight() != realheight || widget.getWidth() != realwidth) {
						widget.setHeight(realheight).setWidth(realwidth);
					}
				}
				if (type == ContainerType.VERTICAL) {
					totalheight += widget.getHeight() + widget.getMarginTop() + widget.getMarginBottom();
				} else {
					totalheight = (int) Math.max(totalheight, widget.getHeight() + widget.getMarginTop() + widget.getMarginBottom());
				}
				if (type == ContainerType.HORIZONTAL) {
					totalwidth += widget.getWidth() + widget.getMarginLeft() + widget.getMarginRight();
				} else {
					totalwidth = (int) Math.max(totalwidth, widget.getWidth() + widget.getMarginLeft() + widget.getMarginRight());
				}
			}
			// Work out the new top-left position taking into account Align
			int left = super.getX();
			int top = super.getY();
			if (align == WidgetAnchor.TOP_CENTER || align == WidgetAnchor.CENTER_CENTER || align == WidgetAnchor.BOTTOM_CENTER) {
				left += (super.getWidth() - totalwidth) / 2;
			} else if (align == WidgetAnchor.TOP_RIGHT || align == WidgetAnchor.CENTER_RIGHT || align == WidgetAnchor.BOTTOM_RIGHT) {
				left += super.getWidth() - totalwidth;
			}
			if (align == WidgetAnchor.CENTER_LEFT || align == WidgetAnchor.CENTER_CENTER || align == WidgetAnchor.CENTER_RIGHT) {
				top += (super.getHeight() - totalheight) / 2;
			} else if (align == WidgetAnchor.BOTTOM_LEFT || align == WidgetAnchor.BOTTOM_CENTER || align == WidgetAnchor.BOTTOM_RIGHT) {
				top += super.getHeight() - totalheight;
			}
			// Move all children into the correct position
			for (Widget widget : visibleChildren) {
				int realtop = top + widget.getMarginTop();
				int realleft = left + widget.getMarginLeft();
				if (widget.getY() != realtop || widget.getX() != realleft) {
					widget.setY(realtop).setX(realleft);
				}
				if (type == ContainerType.VERTICAL) {
					top += widget.getHeight() + widget.getMarginTop() + widget.getMarginBottom();
				} else if (type == ContainerType.HORIZONTAL) {
					left += widget.getWidth() + widget.getMarginLeft() + widget.getMarginRight();
				}
			}
			recalculating = false;
		}
		needsLayout = false;
		return this;
	}

	@Override
	public void onTick() {
		if (needsLayout) {
			updateLayout();
		}
	}

	@Override
	public int getMinWidth() {
		return Math.max(super.getMinWidth(), minWidthCalc);
	}

	@Override
	public int getMaxWidth() {
		return Math.min(super.getMaxWidth(), maxWidthCalc);
	}

	@Override
	public int getMinHeight() {
		return Math.max(super.getMinHeight(), minHeightCalc);
	}

	@Override
	public int getMaxHeight() {
		return Math.min(super.getMaxHeight(), maxHeightCalc);
	}

	@Override
	public Container updateSize() {
		if (!recalculating && !isFixed()) {
			recalculating = true; // Prevent us from getting into a loop due to both trickle down and push up
			int minwidth = 0, maxwidth = 0, minheight = 0, maxheight = 0,  minhoriz, maxhoriz, minvert, maxvert;
			// Work out the minimum and maximum dimensions for the contents of this container
			for (Widget widget : children) {
				if (widget.isVisible()) {
					if (widget instanceof Container) { // Trickle down to children
						((Container) widget).updateSize();
					}
					minhoriz = maxhoriz = widget.getMarginLeft() + widget.getMarginRight();
					minvert = maxvert = widget.getMarginTop() + widget.getMarginBottom();
					if (widget.isFixed()) {
						minhoriz += widget.getWidth();
						maxhoriz += widget.getWidth();
						minvert += widget.getHeight();
						maxvert += widget.getHeight();
					} else {
						minhoriz += widget.getMinWidth();
						maxhoriz += widget.getMaxWidth();
						minvert += widget.getMinHeight();
						maxvert += widget.getMaxHeight();
					}
					if (type == ContainerType.HORIZONTAL) {
						minwidth += minhoriz;
						maxwidth += maxhoriz;
					} else {
						minwidth = Math.max(minwidth, minhoriz);
						if (type == ContainerType.OVERLAY) {
							maxwidth = Math.max(maxwidth, maxhoriz);
						} else {
							maxwidth = Math.min(maxwidth, maxhoriz);
						}
					}
					if (type == ContainerType.VERTICAL) {
						minheight += minvert;
						maxheight += maxvert;
					} else {
						minheight = Math.max(minheight, minvert);
						if (type == ContainerType.OVERLAY) {
							maxheight = Math.max(maxheight, maxvert);
						} else {
							maxheight = Math.min(maxheight, maxvert);
						}
					}
				}
			}
			minwidth = Math.min(minwidth, 427);
			maxwidth = Math.min(maxwidth == 0 ? 427 : maxwidth, 427);
			minheight = Math.min(minheight, 240);
			maxheight = Math.min(maxheight == 0 ? 240 : maxheight, 240);
			// Check if the dimensions have changed
			if (minwidth != minWidthCalc || maxwidth != maxWidthCalc || minheight != minHeightCalc || maxheight != maxHeightCalc) {
				minWidthCalc = minwidth;
				maxWidthCalc = maxwidth;
				minHeightCalc = minheight;
				maxHeightCalc = maxheight;
				deferLayout();
				if (hasContainer()) { // Push up to parents
					getContainer().updateSize();
					getContainer().deferLayout();
				}
			}
			recalculating = false;
		}
		return this;
	}

	public Container setAuto(boolean auto) {
		this.auto = auto;
		return this;
	}

	public boolean isAuto() {
		return auto;
	}

	public void render() {
	}
}
