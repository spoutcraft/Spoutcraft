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
import java.util.UUID;

import org.spoutcraft.api.gui.InGameHUD;
import org.spoutcraft.api.gui.PopupScreen;
import org.spoutcraft.api.gui.Screen;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.api.gui.WidgetType;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;

public class PacketWidgetRemove implements SpoutPacket {
	protected UUID screen;
	protected UUID widget;

	public PacketWidgetRemove() {
	}

	public PacketWidgetRemove(Widget widget, UUID screen) {
		this.widget = widget.getId();
	}

	public void readData(SpoutInputStream input) throws IOException {
		widget = input.readUUID();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeUUID(widget);
	}

	public void run(int playerId) {
		InGameHUD mainScreen = SpoutClient.getInstance().getActivePlayer().getMainScreen();
		PopupScreen popup = mainScreen.getActivePopup();

		Widget w = PacketWidget.allWidgets.get(widget);

		if (w != null && w.getScreen() != null && !(w instanceof Screen)) {
			w.getScreen().removeWidget(w);
		}

		if (w instanceof PopupScreen && popup.getId().equals(w.getId())) {
			// Determine if this is a popup screen and if we need to update it
			mainScreen.closePopup();
		}

		PacketWidget.allWidgets.remove(widget);
	}

	public PacketType getPacketType() {
		return PacketType.PacketWidgetRemove;
	}

	public int getVersion() {
		return 1;
	}

	public void failure(int playerId) {
	}
}
