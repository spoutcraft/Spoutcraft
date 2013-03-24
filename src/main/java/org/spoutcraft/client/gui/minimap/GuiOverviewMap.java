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
package org.spoutcraft.client.gui.minimap;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Mouse;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.Control;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericCheckBox;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericScrollArea;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.api.gui.Orientation;
import org.spoutcraft.api.gui.Point;
import org.spoutcraft.api.gui.RenderPriority;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.chunkcache.HeightMap;
import org.spoutcraft.client.gui.GuiSpoutScreen;

public class GuiOverviewMap extends GuiSpoutScreen {
	private MapWidget map;
	private Boolean minimapEnabled;
	private Label title, menuTitle, noRenderLabel;
	private Button buttonDone, buttonWaypoint, buttonFocus, buttonCloseMenu, buttonZoomIn, buttonZoomOut, buttonShowPlayer, buttonReset, buttonSave, buttonDeathpoints;
	private GenericScrollArea hoverMenu;

	private boolean dragging = false;
	private int dragStartX = -1, dragStartY = -1;
	Point coords = null;
	int y = -1;
	Waypoint clickedWaypoint = null;
	private int focus_mode = -1;
	private static final int FOCUS_SET = 1;
	private static final int FOCUS_REMOVE = 3;
	private int waypoint_mode = -1;
	private static final int WAYPOINT_ADD = 1;
	private static final int WAYPOINT_EDIT = 2;

	@Override
	protected void createInstances() {
		title = new GenericLabel("Overview Map");
		noRenderLabel = new GenericLabel("The overview map will not work until the minimap is enabled.");
		minimapEnabled = MinimapConfig.getInstance().isEnabled();
		buttonDone = new GenericButton("Done");
		buttonZoomIn = new GenericButton("+");
		buttonZoomOut = new GenericButton("-");
		buttonShowPlayer = new GenericButton("Player");
		buttonReset = new GenericButton("Reset View");
		buttonSave = new GenericButton("Save to Desktop");
		buttonDeathpoints = new GenericCheckBox("Deathpoints");
		((GenericCheckBox)buttonDeathpoints).setChecked(MinimapConfig.getInstance().isDeathpoints());
		map = new MapWidget(this);
		map.setGeometry(0, 0, width, height);
		map.scrollTo(map.getPlayerPosition(), false, 0);

		if (minimapEnabled == false) {
			getScreen().attachWidgets("Spoutcraft", noRenderLabel, buttonDone);
		} else {
			getScreen().attachWidgets("Spoutcraft", map, title, buttonDone, buttonZoomIn, buttonZoomOut, buttonShowPlayer, buttonReset, buttonSave, buttonDeathpoints);
		}

		hoverMenu = new GenericScrollArea();
		hoverMenu.setBackgroundColor(new Color(0x55ffffff));
		hoverMenu.setPriority(RenderPriority.Lowest);
		menuTitle = new GenericLabel("What do you want to do?");
		buttonWaypoint = new GenericButton("Add Waypoint");
		buttonFocus = new GenericButton("Set Focus");
		buttonFocus.setTooltip("If a waypoint is in focus, the direction\nto it will be drawn on the minimap.");
		buttonCloseMenu = new GenericButton("Close");
		hoverMenu.attachWidgets("Spoutcraft", buttonFocus, buttonWaypoint, buttonCloseMenu, menuTitle);

		setMenuVisible(false);
		getScreen().attachWidget("Spoutcraft", hoverMenu);
	}

	@Override
	protected void layoutWidgets() {
		title.setX(width / 2 - SpoutClient.getHandle().fontRenderer.getStringWidth(title.getText()) / 2);
		title.setY(5);

		noRenderLabel.setX(width / 2 - SpoutClient.getHandle().fontRenderer.getStringWidth(noRenderLabel.getText()) / 2);
		noRenderLabel.setY(height / 2);

		map.setGeometry(0, 0, width, height);
		map.setPriority(RenderPriority.Highest);

		buttonZoomIn.setGeometry(5, height - 25, 20, 20);
		buttonZoomOut.setGeometry(25, height - 25, 20, 20);
		buttonDone.setGeometry(width - 55, height - 25, 50, 20);
		buttonShowPlayer.setGeometry(50, height - 25, 50, 20);
		buttonReset.setGeometry(105, height - 25, 75, 20);
		buttonSave.setGeometry(185, height - 25, 100, 20);
		buttonDeathpoints.setGeometry(290, height - 25, 75, 20);

		hoverMenu.setGeometry(width / 2 - 320 / 2, height / 2 - 46 / 2, 320, 46);
		int w = SpoutClient.getHandle().fontRenderer.getStringWidth(menuTitle.getText());
		menuTitle.setGeometry(320 / 2 - w / 2, 5, w, 11);
		buttonWaypoint.setGeometry(5, 21, 100, 20);
		buttonFocus.setGeometry(110, 21, 100, 20);
		buttonCloseMenu.setGeometry(215, 21, 100, 20);
	}

	@Override
	public void buttonClicked(Button btn) {
		if (btn == buttonDone) {
			mc.displayGuiScreen(null);
		}
		if (btn == buttonZoomIn) {
			map.zoomBy(1.5);
		}
		if (btn == buttonZoomOut) {
			map.zoomBy(1.0 / 1.5);
		}
		if (btn == buttonShowPlayer) {
			map.showPlayer(500);
		}
		if (btn == buttonWaypoint) {
			switch(waypoint_mode) {
				case WAYPOINT_ADD:
					int x = coords.getX();
					int z = coords.getY();
					SpoutClient.getHandle().displayGuiScreen(new GuiAddWaypoint(this, x, y, z));
					break;
				case WAYPOINT_EDIT:
					SpoutClient.getHandle().displayGuiScreen(new GuiAddWaypoint(this, clickedWaypoint));
					break;
			}
			setMenuVisible(false);
		}
		if (btn == buttonCloseMenu) {
			setMenuVisible(false);
		}
		if (btn == buttonFocus) {
			switch(focus_mode) {
				case FOCUS_SET:
					if (clickedWaypoint != null) {
						MinimapConfig.getInstance().setFocussedWaypoint(clickedWaypoint);
					}
					break;
				case FOCUS_REMOVE:
					MinimapConfig.getInstance().setFocussedWaypoint(null);
					break;
			}
			setMenuVisible(false);
		}
		if (btn == buttonReset) {
			map.reset();
		}
		if (btn == buttonSave) {
			if (map.saveToDesktop()) {
				Label label = new FadingLabel("Saved to Desktop!", 500).setTextColor(new Color(0x7FFF00));
				label.setGeometry(width / 2 - Spoutcraft.getMinecraftFont().getTextWidth(label.getText()) / 2, height / 2, 100, 12);
				getScreen().attachWidgets("Spoutcraft", label);
			} else {
				Label label = new FadingLabel("Failed to save Minimap!", 500).setTextColor(new Color(0xEE0000));
				label.setGeometry(width / 2 - Spoutcraft.getMinecraftFont().getTextWidth(label.getText()) / 2, height / 2, 100, 12);
				getScreen().attachWidgets("Spoutcraft", label);
			}
		}
		if (btn == buttonDeathpoints) {
			MinimapConfig.getInstance().setDeathpoints(!MinimapConfig.getInstance().isDeathpoints());
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int button) {
		if (button == 0 && isInBoundingRect(map, x, y) && !hitsWidget(x, y, map, title)) {
			setMenuVisible(false);
			dragStartX = x;
			dragStartY = y;
		}
		super.mouseClicked(x, y, button);
	}

	private boolean hitsWidget(int x, int y, Widget ...exclude) {
		for (Widget widget:getScreen().getAttachedWidgets(true)) {
			if (isInBoundingRect(widget, x, y)) {
				if (ArrayUtils.contains(exclude, widget) || !widget.isVisible()) {
					continue;
				}
				return true;
			}
		}
		return false;
	}

	private void setMenuVisible(boolean visible) {
		hoverMenu.setVisible(visible);
		for (Widget w:hoverMenu.getAttachedWidgets(true)) {
			w.setVisible(visible);
			if (w instanceof Control) {
				((Control) w).setEnabled(visible);
			}
		}
	}

	private boolean withinManhattanLength(Point center, Point clicked, float length) {
		int cx = center.getX();
		int cy = center.getY();
		int x = clicked.getX();
		int y = clicked.getY();
		return x >= cx - length && x <= cx + length && y >= cy - length && y <= cy + length;
	}

	private Waypoint getClickedWaypoint(int x, int z) {
		Point clicked = new Point(x,z);
		for (Waypoint waypoint:MinimapConfig.getInstance().getAllWaypoints(MinimapUtils.getWorldName())) {
			if (withinManhattanLength(new Point(waypoint.x, waypoint.z), clicked, (float) (2f/map.scale))) {
				return waypoint;
			}
		}
		return null;
	}

	@Override
	protected void mouseMovedOrUp(int x, int y, int button) {
		if (button == 0 && !Mouse.isButtonDown(button)) {
			if (dragStartX == x && dragStartY == y && !dragging) {
				setMenuVisible(true);
				coords = map.mapOutsideToCoords(new Point(x, y));
				clickedWaypoint = getClickedWaypoint(coords.getX(), coords.getY());
				focus_mode = -1;
				if (withinManhattanLength(map.getPlayerPosition(), coords, 2)) {
					coords = map.getPlayerPosition();
					this.y = (int) SpoutClient.getHandle().thePlayer.posY;
				} else {
					this.y = HeightMap.getHeightMap(MinimapUtils.getWorldName()).getHeight(coords.getX(), coords.getY());
				}

				if (clickedWaypoint == null) {
					waypoint_mode = WAYPOINT_ADD;
					buttonWaypoint.setText("Add Waypoint");
				} else {
					waypoint_mode = WAYPOINT_EDIT;
					buttonWaypoint.setText("Edit Waypoint");
				}
				Waypoint waypoint = MinimapConfig.getInstance().getFocussedWaypoint();
				buttonFocus.setEnabled(true);
				if ((clickedWaypoint == null || clickedWaypoint == waypoint) && waypoint != null) {
					buttonFocus.setText("Remove Focus");
					focus_mode = FOCUS_REMOVE;
					clickedWaypoint = waypoint;
				} else if (clickedWaypoint != null) {
					focus_mode = FOCUS_SET;
					buttonFocus.setText("Set Focus");
				} else {
					buttonFocus.setText("Set Focus");
					buttonFocus.setEnabled(false);
				}
				menuTitle.setText(SpoutClient.getInstance().isCoordsCheat() ? "Position (" + coords.getX() + ":" + this.y + ":" + coords.getY() + ")" : "Position not shown");
			}
			dragging = false;
			dragStartX = -1;
			dragStartY = -1;
		}
		if (dragging || dragStartX != -1) {
			dragging = true;
			int mX = (int) map.getScrollPosition(Orientation.HORIZONTAL);
			int mY = (int) map.getScrollPosition(Orientation.VERTICAL);
			mX -= x - dragStartX;
			mY -= y - dragStartY;
			map.setScrollPosition(Orientation.HORIZONTAL, mX);
			map.setScrollPosition(Orientation.VERTICAL, mY);

			dragStartX = x;
			dragStartY = y;
		}
		super.mouseMovedOrUp(x, y, button);
	}

	@Override
	protected void handleScroll(int x, int y, int scroll) {
		if (scroll > 0) {
			map.zoomBy(1.8);
			map.scrollTo(map.mapOutsideToCoords(new Point(x, y)), true, 200);
		} else if (scroll < 0) {
			map.zoomBy(1/1.8);
			map.scrollTo(map.mapOutsideToCoords(new Point(x, y)), true, 200);
		}
	}
}

class FadingLabel extends GenericLabel {
	int ticks;
	int ticksPassed = 0;
	FadingLabel(String text, int ticks) {
		super(text);
		this.ticks = ticks;
	}

	public void render() {
		ticksPassed++;
		float opacity = 1F - (float)ticksPassed / (float)ticks;
		if (ticksPassed > ticks) {
			setVisible(false);
		}
		this.setTextColor(getTextColor().setAlpha(opacity));
		super.render();
	}
}
