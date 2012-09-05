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
import java.util.HashSet;
import net.minecraft.src.Chunk;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;

/**
 *
 * @author ZNickq
 */
public class PacketCustomChunks implements SpoutPacket {

	private int howMany;
	private int[] chunkX, chunkZ;
	
	public PacketCustomChunks(HashSet<Chunk> toSend) {
		howMany = toSend.size();
		chunkX = new int[howMany];
		chunkZ = new int [howMany];
		int current = 0;
		for(Chunk ch : toSend) {
			chunkX[current] = ch.xPosition;
			chunkZ[current] = ch.zPosition;
			current++;
		}
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		howMany = input.readInt();
		chunkX = new int[howMany];
		chunkZ = new int[howMany];
		for (int i = 0; i < howMany; i++) {
			chunkX[i] = input.readInt();
			chunkZ[i] = input.readInt();
		}
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(howMany);
		for (int i = 0; i < howMany; i++) {
			output.writeInt(chunkX[i]);
			output.writeInt(chunkZ[i]);
		}
	}

	@Override
	public void run(int playerId) {
	}

	@Override
	public void failure(int playerId) {
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketCustomChunks;
	}

	@Override
	public int getVersion() {
		return 1;
	}
}
