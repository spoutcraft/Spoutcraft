package org.getspout.spout.chunkcache;

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
import net.minecraft.src.EntityClientPlayerMP;

import org.getspout.spout.packet.CustomPacket;
import org.getspout.spout.packet.PacketCacheHashUpdate;
import org.getspout.spout.util.ChunkHash;
import org.getspout.spout.util.PersistentMap;

public class ChunkCache {

	private final static int FULL_CHUNK = (128 * 16 * 16 * 5) / 2;
	private final static int CACHED_SIZE = FULL_CHUNK + 40*8;

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
	
	private static byte[] hashData = new byte[CACHED_SIZE];
	
	public static AtomicInteger averageChunkSize = new AtomicInteger();
	private static AtomicInteger chunks = new AtomicInteger();
	private static AtomicInteger totalData = new AtomicInteger();
	public static AtomicInteger hitPercentage = new AtomicInteger();
	private static AtomicInteger hits = new AtomicInteger();
	private static AtomicInteger cacheAttempts = new AtomicInteger();
	public static AtomicLong loggingStart = new AtomicLong();
	public static AtomicInteger totalPacketUp = new AtomicInteger();
	public static AtomicInteger totalPacketDown = new AtomicInteger();
	
	public static byte[] handle(byte[] chunkData, Inflater inflater, int chunkSize) throws IOException {

		if(chunkData.length != FULL_CHUNK) {
			return chunkData;
		}
		
		int d = totalData.addAndGet(chunkSize);
		int c = chunks.incrementAndGet();
		
		if(c != 0) {
			averageChunkSize.set(d/c);
		}
				
		try {
			int hashSize = inflater.inflate(hashData, FULL_CHUNK, 40*8);
			if(hashSize != 40*8) {
				return chunkData;
			}
		} catch (DataFormatException e) {
			return chunkData;
		}

		int cacheHit = 0;
		
		for(int i = 0; i < 40; i++) {
			long hash = PartitionChunk.getHash(hashData, i);
			byte[] partitionData = p.get(hash, partition);
			if(hash == 0 || partitionData == null) {
				PartitionChunk.copyFromChunkData(chunkData, i, partition);
				hash = ChunkHash.hash(partition);
				p.put(hash, partition);
				processOverwriteQueue();
			} else {
				cacheHit++;
				PartitionChunk.copyToChunkData(chunkData, i, partitionData);
			}
			
			// Send hints to server about possible nearby hashes
			if(hashes.add(hash)) {
				Integer index = p.getIndex(hash);
				if(index != null) {
					for(int j = index - 1024; j < index + 1024; j++) {
						Long nearbyHash = p.getHash(j);
						if(nearbyHash != null && !hashes.contains(nearbyHash)) {
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
				((EntityClientPlayerMP)Minecraft.theMinecraft.thePlayer).sendQueue.addToSendQueue(new CustomPacket(new PacketCacheHashUpdate(true, nearbyHashes)));
			}
		}
		
		int h = hits.addAndGet(cacheHit);
		int a = cacheAttempts.addAndGet(40);
		if(a != 0) {
			hitPercentage.set((100 * h) / a);
		}
				
		byte[] cachedChunkData = new byte[81920];
		System.arraycopy(chunkData, 0, cachedChunkData, 0, 81920);

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
			((EntityClientPlayerMP)Minecraft.theMinecraft.thePlayer).sendQueue.addToSendQueue(new CustomPacket(new PacketCacheHashUpdate(false, overwrittenHashes)));
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
