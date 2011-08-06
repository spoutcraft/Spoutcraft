package org.getspout.spout.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.getspout.spout.io.FileMap;

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
		if(value != null) {
			return value;
		}
		value = overwriteBackup.get(key);
		if(value != null) {
			return value;
		}
		value = f.readByHash(key, data);

		return value;
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
		if(index != null) {
			return;
		}
		index = f.getIndex();
		f.incrementIndex();
		Long oldHash = f.indexToHash(index);
		byte[] oldData = new byte[(int)size];
		oldData = f.readByIndex(index, oldData);
		if(oldData != null) {
			overwriteBackup.put(oldHash, oldData);
			overwriteQueue.add(oldHash);
		}
		f.write(index, key, data);
		
		if(!cache.contains(key)) {
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

}
