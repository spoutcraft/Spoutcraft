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
package org.spoutcraft.client.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.spoutcraft.client.io.FileMap;

public class PersistentMap {
	private final FileMap f;
	private final CacheMap<Long, byte[]> cache = new CacheMap<Long, byte[]>();
	private final HashMap<Long, byte[]> overwriteBackup = new HashMap<Long, byte[]>();
	private final ConcurrentLinkedQueue<Long> overwriteQueue = new ConcurrentLinkedQueue<Long>();
	private final long size;

	public PersistentMap(File dir, String filename, long size, int entries) throws IOException {
		f = new FileMap(dir, filename, size, entries);
		this.size = size;
	}

	public byte[] get(Long key, byte[] data) throws IOException {
		byte[] value = cache.get(key);
		if (value != null) {
			return value;
		}
		value = overwriteBackup.get(key);
		if (value != null) {
			return value;
		}
		value = f.readByHash(key, data);

		return value;
	}

	public void wipeFile() throws IOException {
		f.wipe();
	}

	public void reset() {
		overwriteQueue.clear();
		overwriteBackup.clear();
	}

	public Integer getIndex(long key) {
		return f.hashToIndex(key);
	}

	public Long getHash(int index) {
		return f.indexToHash(index);
	}

	public void put(Long key, byte[] data) throws IOException {
		Integer index = f.hashToIndex(key);
		if (index != null) {
			return;
		}
		index = f.getIndex();
		f.incrementIndex();
		Long oldHash = f.indexToHash(index);
		byte[] oldData = new byte[(int)size];
		oldData = f.readByIndex(index, oldData);
		if (oldData != null) {
			overwriteBackup.put(oldHash, oldData);
			overwriteQueue.add(oldHash);
		}
		f.write(index, key, data);

		if (!cache.contains(key)) {
			byte[] dataCopy = new byte[data.length];
			System.arraycopy(data, 0, dataCopy, 0, data.length);
			cache.put(key, dataCopy);
		}
	}

	public Long getOverwritten() {
		return overwriteQueue.poll();
	}

	public byte[] removeOverwriteBackup(Long key) {
		cache.remove(key);
		return overwriteBackup.remove(key);
	}

	public boolean corruptionTest(long hash) {
		Integer index = f.hashToIndex(hash);
		try {
			if (index != null) {
				f.corruptIndex(index);
				return true;
			} else {
				System.out.println(hash + " doesn't exist, cannot corrupt");
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}
}
