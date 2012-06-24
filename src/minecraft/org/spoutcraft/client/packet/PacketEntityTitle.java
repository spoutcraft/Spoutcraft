/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.SpoutcraftWorld;
import org.spoutcraft.spoutcraftapi.entity.LivingEntity;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;

public class PacketEntityTitle implements SpoutPacket {
	public String title;
	public int entityId;
	public PacketEntityTitle() {
	}

	public PacketEntityTitle(int entityId, String title) {
		this.entityId = entityId;
		this.title = title;
	}

	public void readData(SpoutInputStream input) throws IOException {
		entityId = input.readInt();
		title = input.readString();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(entityId);
		output.writeString(title);
	}

	public void run(int id) {
		Entity e = SpoutClient.getInstance().getEntityFromId(entityId);
		if (e != null && e instanceof EntityLiving) {
			LivingEntity living = (LivingEntity)e.spoutEntity;
			if (title.equals("reset")) {
				living.resetTitle();
				SpoutcraftWorld spworld = (SpoutcraftWorld) living.getWorld();
				spworld.getHandle().customTitles.remove(entityId);
			} else {
				living.setTitle(title);
				SpoutcraftWorld spworld = (SpoutcraftWorld) living.getWorld();
				spworld.getHandle().customTitles.put(living.getEntityId(), title);
			}
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketEntityTitle;
	}

	public int getVersion() {
		return 0;
	}

	public void failure(int playerId) {
	}
}
