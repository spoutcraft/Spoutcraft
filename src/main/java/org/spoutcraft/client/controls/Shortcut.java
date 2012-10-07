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
package org.spoutcraft.client.controls;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.Packet3Chat;

import org.spoutcraft.api.keyboard.AbstractBinding;
import org.spoutcraft.client.SpoutClient;

public class Shortcut extends AbstractBinding implements Serializable {
	private static final long serialVersionUID = 4365592803468257957L;

	private String title = "";
	private ArrayList<String> commands = new ArrayList<String>();
	private int delay = 0;

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

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getDelay() {
		return delay;
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
		return false;
	}

	@Override
	public void summon(int key, final boolean keyReleased, final int screen) {
		new Thread() {
			public void run() {
				if (keyReleased && screen == 0) {
					for (String cmd:getCommands()) {
						if (SpoutClient.getHandle().isMultiplayerWorld()) {
							EntityClientPlayerMP player = (EntityClientPlayerMP)SpoutClient.getHandle().thePlayer;
							player.sendQueue.addToSendQueue(new Packet3Chat(cmd));
						}
						if (delay > 0) {
							try {
								sleep(delay);
							} catch (InterruptedException e) {}
						}
					}
				}
			};
		}.start();
	}
}
