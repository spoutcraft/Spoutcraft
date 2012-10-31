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
package org.spoutcraft.client;

import java.lang.String;

import net.minecraft.src.GuiConnecting;

import org.spoutcraft.client.gui.server.ServerItem;

public class ReconnectManager {
	static String hostName = null;
	static int portNum = -1;
	public static boolean serverTeleport = false;

	public static void detectKick(String infoString, String reason, Object[] objects) {
		portNum = -1;

		if (infoString == null || reason == null || objects == null) {
			return;
		}

		if (objects.length == 0 || !(objects[0] instanceof String)) {
			return;
		}

		reason = (String) objects[0];

		if (infoString.contains("disconnect.disconnected")) {
			if (reason.indexOf("[Serverport]") == 0 || reason.indexOf("[Redirect]") == 0) {
				String[] split = reason.split(":");
				if (split.length == 3) {
					hostName = split[1].trim();
					try {
						portNum = Integer.parseInt(split[2].trim());
					} catch (Exception e) {
						portNum = -1;
					}
				} else if (split.length == 2) {
					hostName = split[1].trim();
					portNum = ServerItem.DEFAULT_PORT;
				}
			}
		}
	}

	public static void teleport(net.minecraft.client.Minecraft mc) {
		if (portNum != -1 && hostName != null) {
			mc.displayGuiScreen(new GuiConnecting(mc, hostName, portNum));
			serverTeleport = true;
		}
	}
}
