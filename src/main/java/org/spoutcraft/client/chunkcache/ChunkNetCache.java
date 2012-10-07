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
package org.spoutcraft.client.chunkcache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.FileUtil;

public class ChunkNetCache {
	private static final SimpleFileCache p;

	private static final byte[] partition = new byte[2048];

	private static final HashSet<Long> hashes = new HashSet<Long>();
	private static final ArrayList<Long> hashQueue = new ArrayList<Long>(1025);

	static {
		File dir = new File(FileUtil.getCacheDir(), "chunk");

		dir.mkdirs();

		try {
			p = new SimpleFileCache(dir, 2048, 64 * 1024);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static AtomicInteger averageChunkSize = new AtomicInteger();
	private static AtomicInteger chunks = new AtomicInteger();
	private static AtomicInteger totalData = new AtomicInteger();
	public static AtomicInteger hitPercentage = new AtomicInteger();
	private static AtomicInteger hits = new AtomicInteger();
	private static AtomicInteger cacheAttempts = new AtomicInteger();
	public static AtomicLong loggingStart = new AtomicLong();
	public static AtomicInteger totalPacketUp = new AtomicInteger();
	public static AtomicInteger totalPacketDown = new AtomicInteger();
	public static AtomicBoolean cacheInUse = new AtomicBoolean(false);

	public static byte[] handle(byte[] chunkData, int decompressedSize, int compressedSize, int numChunks, int cx, int cz) throws IOException {

		int d = totalData.addAndGet(compressedSize);
		int c = chunks.addAndGet(numChunks);

		if (c != 0) {
			averageChunkSize.set((10 * d)/c);
		}

		if ((decompressedSize & 0x01) == 0) {
			int h = 0;
			int segments = decompressedSize >> 11;
			updateCacheAttempts(h, segments);
			return crop(chunkData, decompressedSize);
		}

		int dataLength = PartitionChunk.getInt(chunkData, 0, decompressedSize - 5);
		long CRC = PartitionChunk.getHash(chunkData, 0, decompressedSize - 13);

		int segments = dataLength >> 11;
		if ((dataLength & 0x7FF) != 0) {
			segments++;
		}

		byte[] newChunkData = new byte[dataLength];
		System.arraycopy(chunkData, 0, newChunkData, 0, dataLength);

		int cacheHit = 0;

		for (int i = 0; i < segments; i++) {
			long hash = PartitionChunk.getHash(chunkData, i, dataLength);
			byte[] partitionData = p.getData(hash);

			if (hash == 0) {
				PartitionChunk.copyFromChunkData(chunkData, i, partition, dataLength);
				hash = p.putData(partition);
			} else if (partitionData == null) {
				System.out.println("Cache Error: Unable to find hash " + Long.toHexString(hash));
				return null;
			} else {
				cacheHit++;
				PartitionChunk.copyToChunkData(newChunkData, i, partitionData, dataLength);
			}

			// Send hints to server about possible nearby hashes
			if (hashes.add(hash)) {
				long[] nearby = p.getNearby(hash, 1024);
				if (nearby != null) {
					for (int j = 0; j < nearby.length; j++) {
						long nearbyHash = nearby[j];
						if (nearbyHash != 0 && hashes.add(nearbyHash)) {
							hashQueue.add(nearbyHash);
						}
					}
				}
				nearby = new long[hashQueue.size()];
				for (int j = 0; j < nearby.length; j++) {
					nearby[j] = hashQueue.get(j);
				}
				hashQueue.clear();
				sendHashHints(nearby);
			}
		}

		int h = hits.addAndGet(cacheHit);
		updateCacheAttempts(h, segments);

		long CRCNew = PartitionChunk.hash(newChunkData);

		if (CRCNew != CRC) {
			System.out.println("Cache Error: CRC mismatch, received: " + CRC + " CRC of data: " + CRCNew);
			System.out.println("Cache Error: Chunk coords: " + cx + " " + cz);
			return null;
		}

		cacheInUse.set(true);

		return newChunkData;
	}

	private static void updateCacheAttempts(int h, int segments) {
		int a = cacheAttempts.addAndGet(segments);
		if (a != 0) {
			hitPercentage.set((100 * h) / a);
		}
	}

	public static void sendHashHints(long[] array) {
		int s = 0;
		while (s < array.length) {
			int hashCount = Math.min(10, array.length - s);
			byte[] hashes = new byte[hashCount << 3];
			for (int i = 0; i < hashCount; i++) {
				PartitionChunk.setHash(hashes, i, array[i + s], 0);
			}
			SpoutClient.getInstance().getPacketManager().sendCustomPacket("ChkCache:setHash", hashes);
			s += 10;
		}
	}

	public static byte[] crop(byte[] in, int maxLength) {
		if (in.length <= maxLength) {
			return in;
		} else {
			byte[] newArray = new byte[maxLength];
			System.arraycopy(in, 0, newArray, 0, maxLength);
			return newArray;
		}
	}

	public static void reset() {
		hashes.clear();
		//p.prune();
		loggingStart.set(System.currentTimeMillis());
		totalPacketUp.set(0);
		totalPacketDown.set(0);
		totalData.set(0);
		chunks.set(0);
		hits.set(0);
		cacheAttempts.set(0);
		cacheInUse.set(false);
	}
}
