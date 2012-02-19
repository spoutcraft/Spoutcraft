/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.controls;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.Packet3Chat;

import org.lwjgl.input.Keyboard;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.keyboard.AbstractBinding;

public class Shortcut extends AbstractBinding implements Serializable {
	private static final long serialVersionUID = 4365592803468257957L;

	private String title = "";
	private ArrayList<String> commands = new ArrayList<String>();

	public Shortcut() {
	}

	public void addCommand(String cmd) {
		commands.add(cmd);
	}

	public List<String> getCommands() {
		return commands;
	}

	public void clear() {
		commands.clear();
	}

	public void removeCommand(int i) {
		commands.remove(i);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setCommands(ArrayList<String> commands) {
		this.commands = commands;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		return true;
	}

	@Override
	public void summon(int key, boolean keyReleased, int screen) {
		if (!SimpleKeyBindingManager.isModifierKey(key) && keyReleased && screen == 0) {
			for (String cmd:getCommands()) {
				if (SpoutClient.getHandle().isMultiplayerWorld()) {
					EntityClientPlayerMP player = (EntityClientPlayerMP)SpoutClient.getHandle().thePlayer;
					player.sendQueue.addToSendQueue(new Packet3Chat(cmd));
				}
			}
		}
	}
}
