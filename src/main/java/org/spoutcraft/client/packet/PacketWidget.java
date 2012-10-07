/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spoutcraft.client.packet;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.UUID;

import org.spoutcraft.api.gui.GenericWidget;
import org.spoutcraft.api.gui.InGameHUD;
import org.spoutcraft.api.gui.OverlayScreen;
import org.spoutcraft.api.gui.PopupScreen;
import org.spoutcraft.api.gui.Screen;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.api.gui.WidgetType;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.*;

public class PacketWidget implements SpoutPacket {
	private Widget widget;
	protected UUID screen;
	private static final int[] nags;
	ByteBuffer widgetData;
	WidgetType widgetType;
	private int version;
	private UUID widgetId;

	static HashMap<UUID, Widget> allWidgets = new HashMap<UUID, Widget>();

	static {
		nags = new int[WidgetType.getNumWidgetTypes()];
		for (int i = 0; i < WidgetType.getNumWidgetTypes(); i++) {
			nags[i] = CustomPacket.NAG_MSG_AMT;
		}
	}

	public PacketWidget() {
	}

	public PacketWidget(Widget widget, UUID screen) {
		this.widget = widget;
		this.screen = screen;
	}

	public void readData(SpoutInputStream input) throws IOException {
		int id = input.readInt();
		screen = input.readUUID();
		widgetId = input.readUUID();

		int size = input.readInt();
		version = input.readShort();
		byte[] widgetData = new byte[size];
		input.read(widgetData);
		this.widgetData = ByteBuffer.wrap(widgetData);
		widgetType = WidgetType.getWidgetFromId(id);

		/*SpoutInputStream data = new SpoutInputStream(ByteBuffer.wrap(widgetData));
		if (widgetType != null) {
			try {
				widget = widgetType.getWidgetClass().newInstance();
				if (widget.getVersion() == version) {
					widget.readData(data);
				} else {
					if (nags[id]-- > 0) {
						System.out.println("Received invalid widget: " + widgetType.getWidgetClass().getSimpleName() + " v: " + version + " current v: " + widget.getVersion());
					}
					widget = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(widget.getType().getId());
		output.writeUUID(widget.getScreen().getId());
		output.writeUUID(widget.getId());

		SpoutOutputStream data = new SpoutOutputStream();
		widget.writeData(data);
		ByteBuffer buffer = data.getRawBuffer();
		byte[] widgetData = new byte[buffer.capacity() - buffer.remaining()];
		System.arraycopy(buffer.array(), 0, widgetData, 0, widgetData.length);

		output.writeInt(widgetData.length);
		output.writeShort((short) widget.getVersion());
		output.write(widgetData);
	}

	public void run(int playerId) {
		try {
			if (allWidgets.containsKey(widgetId)) {
				widget = allWidgets.get(widgetId);
				if (widget.getVersion() == version) {
					widget.readData(new SpoutInputStream(widgetData));
				}
			} else {
				widget = widgetType.getWidgetClass().newInstance();

				// Hackish way to set the ID without a setter
				((GenericWidget) widget).setId(widgetId);
				if (widget.getVersion() == version) {
					widget.readData(new SpoutInputStream(widgetData));
				} else {
					if (nags[widgetType.getId()]-- > 0) {
						System.out.println("Received invalid widget: " + widgetType.getWidgetClass().getSimpleName() + " v: " + version + " current v: " + widget.getVersion());
					}
					widget = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (widget != null) {
			allWidgets.put(widgetId, widget);
			InGameHUD mainScreen = SpoutClient.getInstance().getActivePlayer().getMainScreen();
			PopupScreen popup = mainScreen.getActivePopup();
			Screen overlay = null;

			if (SpoutClient.getHandle().currentScreen != null) {
				overlay = SpoutClient.getHandle().currentScreen.getScreen();
			}
			// Determine if this is a popup screen and if we need to update it
			if (widget instanceof PopupScreen) {
				if (popup != null) {
					if (widget.getId().equals(popup.getId())) {
						if (SpoutClient.getHandle().currentScreen instanceof CustomScreen) {
							((CustomScreen)SpoutClient.getHandle().currentScreen).update((PopupScreen)widget);
						}
					} else {
						mainScreen.closePopup();
						mainScreen.attachPopupScreen((PopupScreen)widget);
					}
				} else {
					mainScreen.attachPopupScreen((PopupScreen)widget);
				}
			} else if (widget instanceof OverlayScreen) { // Determine if this screen overrides another screen
				if (SpoutClient.getHandle().currentScreen != null) {
					SpoutClient.getHandle().currentScreen.update((OverlayScreen)widget);
					overlay = (OverlayScreen)widget;
				}
			} else if (screen.equals(mainScreen.getId())) { // Determine if this is a widget on the main screen
				if (mainScreen.containsWidget(widget.getId())) {
					mainScreen.updateWidget(widget);
					widget.setScreen(mainScreen);
				} else {
					widget.setScreen(mainScreen);
					mainScreen.attachWidget(widget.getAddon(), widget);
				}
			} else if (popup != null && screen.equals(popup.getId())) { // Determine if this is a widget on the popup screen
				if (popup.containsWidget(widget.getId())) {
					popup.updateWidget(widget);
					widget.setScreen(popup);
				} else {
					widget.setScreen(popup);
					popup.attachWidget(widget.getAddon(), widget);
				}
			} else if (overlay != null && screen.equals(overlay.getId())) { // Determine if this is a widget on an overlay screen
				if (overlay.containsWidget(widget.getId())) {
					overlay.updateWidget(widget);
					widget.setScreen(overlay);
				} else {
					widget.setScreen(overlay);
					overlay.attachWidget(widget.getAddon(), widget);
				}
			}
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketWidget;
	}

	public int getVersion() {
		return 2;
	}

	public void failure(int playerId) {
	}
}
