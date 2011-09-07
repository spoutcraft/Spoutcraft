/*
 * This file is part of Spout API (http://wiki.getspout.org/).
 * 
 * Spout API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spout API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.Entity;

import org.getspout.spout.client.SpoutClient;

public class PacketSetVelocity implements SpoutPacket {
	private double motX = 0;
	private double motY = 0;
	private double motZ = 0;
	private int entityId = 0;
	
	public PacketSetVelocity() {
		
	}
	
	public PacketSetVelocity(int entityId, double motX, double motY, double motZ) {
		this.entityId = entityId;
		this.motX = motX;
		this.motY = motY;
		this.motZ = motZ;
	}

	public int getNumBytes() {
		return 28;
	}

	public void readData(DataInputStream input) throws IOException {
		entityId = input.readInt();
		motX = input.readDouble();
		motY = input.readDouble();
		motZ = input.readDouble();
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(entityId);
		output.writeDouble(motX);
		output.writeDouble(motY);
		output.writeDouble(motZ);
	}

	public void run(int playerId) {
		Entity e = SpoutClient.getInstance().getEntityFromId(entityId);
		if (e != null && !Double.isNaN(motX) && !Double.isNaN(motY) && !Double.isNaN(motZ)) {
			e.motionX = motX;
			e.motionY = motY;
			e.motionZ = motZ;
		}
	}

	public void failure(int playerId) {
		
	}

	public PacketType getPacketType() {
		return PacketType.PacketSetVelocity;
	}

	public int getVersion() {
		return 1;
	}
}
