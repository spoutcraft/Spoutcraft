/*
 * This file is part of Spoutcraft (http://spout.org).
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
package org.spoutcraft.client.chunkcache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import net.minecraft.client.Minecraft;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.packet.PacketCacheHashUpdate;
import org.spoutcraft.client.packet.PacketChunkRefresh;
import org.spoutcraft.client.util.ChunkHash;
import org.spoutcraft.client.util.PersistentMap;

public class ChunkCache {

	private final static int FULL_CHUNK = (128 * 16 * 16 * 5) / 2;

	private static final PersistentMap p;

	private static final byte[] partition = new byte[2048];

	private static final HashSet<Long> hashes = new HashSet<Long>();
	private static final ArrayList<Long> hashQueue = new ArrayList<Long>(1025);
	private static final LinkedList<Long> overwriteQueue = new LinkedList<Long>();

	static {
		File dir = new File(Minecraft.getMinecraftDir(), "chunkcache");

		dir.mkdirs();

		try {
			p = new PersistentMap(dir, "cache", 2048, 24*1024);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static byte[] hashData = new byte[2048];
	private static byte[] blank = new byte[2048];
	
	public static AtomicInteger averageChunkSize = new AtomicInteger();
	private static AtomicInteger chunks = new AtomicInteger();
	private static AtomicInteger totalData = new AtomicInteger();
	public static AtomicInteger hitPercentage = new AtomicInteger();
	private static AtomicInteger hits = new AtomicInteger();
	private static AtomicInteger cacheAttempts = new AtomicInteger();
	public static AtomicLong loggingStart = new AtomicLong();
	public static AtomicInteger totalPacketUp = new AtomicInteger();
	public static AtomicInteger totalPacketDown = new AtomicInteger();
	
	public static byte[] handle(byte[] chunkData, Inflater inflater, int chunkSize, int cx, int cz) throws IOException {

		if(chunkData.length % FULL_CHUNK != 0) {
			return chunkData;
		}
		
		int segments = chunkData.length >> 11;
		int height = chunkData.length / 640;
		int heightBits = 31 - Integer.numberOfLeadingZeros(height);
		int cachedSize = segments * (2048 + 8) + 8;
		if (hashData.length < cachedSize) {
			hashData = new byte[cachedSize];
		}
		
		int d = totalData.addAndGet(chunkSize);
		int c = chunks.incrementAndGet();
		
		if(c != 0) {
			averageChunkSize.set(d/c);
		}
				
		long CRC = 0;
		boolean CRCProvided;
		try {
			int hashSize = inflater.inflate(hashData, chunkData.length, segments*8 + 8);
			if(hashSize == segments * 8 + 8) {
				CRC = PartitionChunk.getHash(hashData, segments, heightBits);
				CRCProvided = true;
			} else {
				return chunkData;
			}
		} catch (DataFormatException e) {
			return chunkData;
		}
		
		int cacheHit = 0;
		
		for(int i = 0; i < segments; i++) {
			long hash = PartitionChunk.getHash(hashData, i, heightBits);
			byte[] partitionData = p.get(hash, partition);

			if(hash == 0) {
				PartitionChunk.copyFromChunkData(chunkData, i, partition, heightBits);
				hash = ChunkHash.hash(partition);
				p.put(hash, partition);
				processOverwriteQueue();
			} else if (partitionData == null) {
				long[] brokenHash = new long[1];
				brokenHash[0] = hash;
				SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketCacheHashUpdate(false, brokenHash));
				PartitionChunk.copyToChunkData(chunkData, i, blank, heightBits);
			} else {
				cacheHit++;
				PartitionChunk.copyToChunkData(chunkData, i, partitionData, heightBits);
			}
			
			// Send hints to server about possible nearby hashes
			if(hashes.add(hash)) {
				Integer index = p.getIndex(hash);
				if(index != null) {
					for(int j = index - 1024; j < index + 1024; j++) {
						Long nearbyHash = p.getHash(j);
						if(nearbyHash != null && !hashes.contains(nearbyHash) && nearbyHash != 0) {
							hashQueue.add(nearbyHash);
							hashes.add(nearbyHash);
						}
					}
				}
				long[] nearbyHashes = new long[hashQueue.size()];
				for(int j = 0; j < nearbyHashes.length; j++) {
					nearbyHashes[j] = hashQueue.get(j);
				}
				hashQueue.clear();
				SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketCacheHashUpdate(true, nearbyHashes));
			}
		}
		
		int h = hits.addAndGet(cacheHit);
		int a = cacheAttempts.addAndGet(segments);
		if(a != 0) {
			hitPercentage.set((100 * h) / a);
		}
				
		byte[] cachedChunkData = new byte[segments * 2048];
		System.arraycopy(chunkData, 0, cachedChunkData, 0, segments * 2048);
		
		long CRCNew = ChunkHash.hash(cachedChunkData);
		
		if (CRCProvided && CRCNew != CRC) {
			System.out.println("CRC error, received: " + CRC + " CRC of data: " + CRCNew);
			System.out.println("Requesting chunk resend: " + cx + " " + cz);
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketChunkRefresh(cx, cz));
		}
		
		return cachedChunkData;
	}

	private static void processOverwriteQueue() {
		Long hash;
		while((hash = p.getOverwritten()) != null) {
			if(hashes.contains(hash)) {
				overwriteQueue.add(hash);
			} else {
				p.removeOverwriteBackup(hash);
			}
		}
		if(overwriteQueue.size() > 128) {
			long[] overwrittenHashes = new long[overwriteQueue.size()];
			for(int j = 0; j < overwrittenHashes.length; j++) {
				overwrittenHashes[j] = overwriteQueue.removeFirst();
			}
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketCacheHashUpdate(false, overwrittenHashes));
		}
	}

	public static byte[] removeOverwriteBackup(long hash) {
		return p.removeOverwriteBackup(hash);
	}

	public static void reset() {
		hashes.clear();
		p.reset();
		loggingStart.set(System.currentTimeMillis());
		totalPacketUp.set(0);
		totalPacketDown.set(0);
		totalData.set(0);
		chunks.set(0);
		hits.set(0);
		cacheAttempts.set(0);
	}

}
