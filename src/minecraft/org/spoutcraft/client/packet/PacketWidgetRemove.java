/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.gui.InGameHUD;
import org.spoutcraft.spoutcraftapi.gui.PopupScreen;
import org.spoutcraft.spoutcraftapi.gui.Screen;
import org.spoutcraft.spoutcraftapi.gui.Widget;
import org.spoutcraft.spoutcraftapi.gui.WidgetType;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;

public class PacketWidgetRemove implements SpoutPacket {
	protected Widget widget;
	protected UUID screen;

	public PacketWidgetRemove() {
	}

	public PacketWidgetRemove(Widget widget, UUID screen) {
		this.widget = widget;
		this.screen = screen;
	}

	public void readData(SpoutInputStream input) throws IOException {
		int id = input.readInt();
		long msb = input.readLong();
		long lsb = input.readLong();
		screen = new UUID(msb, lsb);
		WidgetType widgetType = WidgetType.getWidgetFromId(id);
		if (widgetType != null) {
			try {
				widget = widgetType.getWidgetClass().newInstance();
				widget.readData(input);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(widget.getType().getId());
		output.writeLong(screen.getMostSignificantBits());
		output.writeLong(screen.getLeastSignificantBits());
		widget.writeData(output);
	}

	public void run(int playerId) {
		InGameHUD mainScreen = SpoutClient.getInstance().getActivePlayer().getMainScreen();
		PopupScreen popup = mainScreen.getActivePopup();
		Screen overlay = null;
		if (SpoutClient.getHandle().currentScreen != null) {
			overlay = SpoutClient.getHandle().currentScreen.getScreen();
		}

		if (widget instanceof PopupScreen && popup.getId().equals(widget.getId())) {
			// Determine if this is a popup screen and if we need to update it
			mainScreen.closePopup();
		} else if (mainScreen.containsWidget(widget.getId())) {
			//Determine if this is a widget on the main screen
			mainScreen.removeWidget(widget);
		} else if (popup != null && popup.containsWidget(widget.getId())) {
			//Determine if this is a widget on the popup screen
			popup.removeWidget(widget);
		} else if (overlay != null && overlay.containsWidget(widget.getId())) {
			//Determine if this is a widget on an overlay screen
			overlay.removeWidget(widget);
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketWidgetRemove;
	}

	public int getVersion() {
		return 0;
	}

	public void failure(int playerId) {
	}
}
