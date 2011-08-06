package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.chunkcache.ChunkCache;

public class PacketCacheHashUpdate implements SpoutPacket {
	public long[] hashes;
	public boolean add;
	
	public PacketCacheHashUpdate() {
	}
	
	public PacketCacheHashUpdate(boolean add, long[] hashes) {
		this.add = add;
		this.hashes = new long[hashes.length];
		System.arraycopy(hashes, 0, this.hashes, 0, hashes.length);
	}

	public int getNumBytes() {
		return 5 + 8 * hashes.length;
	}

	public void readData(DataInputStream input) throws IOException {
		this.add = input.readBoolean();
		int length = input.readInt();
		this.hashes = new long[length];
		for(int i = 0; i < length; i++) {
			this.hashes[i] = input.readLong();
		}
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeBoolean(this.add);
		output.writeInt(hashes.length);
		for(int i = 0; i < hashes.length; i++) {
			output.writeLong(hashes[i]);
		}
	}

	public void run(int id) {
		if(!this.add) {
			for(long hash : this.hashes) {
				ChunkCache.removeOverwriteBackup(hash);
			}
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketCacheHashUpdate;
	}

}
