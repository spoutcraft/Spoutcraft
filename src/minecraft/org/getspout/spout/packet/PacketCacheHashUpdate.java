/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.chunkcache.ChunkCache;

public class PacketCacheHashUpdate implements SpoutPacket {
	public long[] hashes;
	public boolean add;
	public boolean reset = false;
	
	public PacketCacheHashUpdate() {
		hashes = new long[0];
		add = false;
	}
	
	public PacketCacheHashUpdate(boolean add, long[] hashes) {
		this.add = add;
		this.hashes = new long[hashes.length];
		System.arraycopy(hashes, 0, this.hashes, 0, hashes.length);
	}
	
	public int getNumBytes() {
		return 6 + 8 * hashes.length;
	}

	public void readData(DataInputStream input) throws IOException {
		this.add = input.readBoolean();
		this.reset = input.readBoolean();
		int length = input.readInt();
		this.hashes = new long[length];
		for(int i = 0; i < length; i++) {
			this.hashes[i] = input.readLong();
		}
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeBoolean(this.add);
		output.writeBoolean(this.reset);
		output.writeInt(hashes.length);
		for(int i = 0; i < hashes.length; i++) {
			output.writeLong(hashes[i]);
		}
	}

	public void run(int id) {
		if(this.reset) {
			ChunkCache.reset();
		} else if(!this.add) {
			for(long hash : this.hashes) {
				ChunkCache.removeOverwriteBackup(hash);
			}
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketCacheHashUpdate;
	}
	
	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void failure(int playerId) {
		
	}

}
