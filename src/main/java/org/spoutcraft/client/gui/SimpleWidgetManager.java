/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org/>
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
package org.spoutcraft.client.gui;

import org.spoutcraft.api.gui.Control;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.api.gui.WidgetManager;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.packet.builtin.PacketFocusUpdate;
import org.spoutcraft.client.packet.builtin.PacketWidget;

public class SimpleWidgetManager implements WidgetManager {
	public void sendWidgetUpdate(Widget widget) {
		if (widget.getScreen() == null) {
			return;
		}
		PacketWidget update = new PacketWidget(widget, widget.getScreen().getId());
		SpoutClient.getInstance().getPacketManager().sendSpoutPacket(update);
	}

	public void sendFocusUpdate(Control control, boolean focus) {
		PacketFocusUpdate update = new PacketFocusUpdate(control, focus);
		SpoutClient.getInstance().getPacketManager().sendSpoutPacket(update);
	}
}
