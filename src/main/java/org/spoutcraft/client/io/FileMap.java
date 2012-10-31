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
package org.spoutcraft.client.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.spoutcraft.client.chunkcache.PartitionChunk;

public class FileMap {
	private final long size;
	private final int entries;
	private int index;
	private final RandomAccessFile dataFile;
	private final RandomAccessFile FATFile;
	private final RandomAccessFile indexFile;
	private final AtomicLong[] hashes;
	private final ConcurrentHashMap<Long,Integer> FAT = new ConcurrentHashMap<Long,Integer>();

	public FileMap(File dir, String filename, long size, int entries) throws IOException {
		this.size = size;
		this.entries = entries;

		dataFile = new RandomAccessFile(new File(dir, filename + ".dat"), "rw");
		FATFile = new RandomAccessFile(new File(dir, filename + ".fat"), "rw");
		indexFile = new RandomAccessFile(new File(dir, filename + ".index"), "rw");

		if (dataFile.length() < size*entries) {
			dataFile.setLength(this.size * this.entries);
		}

		if (FATFile.length() < entries*8) {
			FATFile.setLength(entries*8);
			FATFile.seek(0);
			FATFile.write(new byte[entries*8]);
		}

		hashes = new AtomicLong[entries];
		FATFile.seek(0);
		for (int i = 0; i < entries; i++) {
			long hash = FATFile.readLong();
			hashes[i] = new AtomicLong(hash);
			FAT.put(hash, i);
		}

		if (indexFile.length() < 4) {
			indexFile.setLength(4);
			indexFile.seek(0);
			indexFile.writeInt(0);
		}

		index = readIndex();
	}

	public void close() throws IOException {
		dataFile.close();
		FATFile.close();
		indexFile.close();
	}

	public void wipe() throws IOException {
		for (int i = 0; i < entries; i++) {
			FATFile.seek(0);
			FATFile.write(new byte[entries*8]);
		}
	}

	public void write(int index, long hash, byte[] data) throws IOException {
		if (data == null) {
			throw new IllegalArgumentException("Null data passed to FileIO.write()");
		}
		if (data.length != size) {
			throw new IllegalArgumentException("Data array of incorrect length (" + data.length + ") passed to FileIO.write()");
		}
		if (PartitionChunk.hash(data) != hash) {
			throw new IllegalArgumentException("Hash mismatch for data passed to FileIO.write()");
		}
		if (index < 0) {
			throw new IllegalArgumentException("negative index");
		}
		index = index % entries;
		long oldHash = hashes[index].get();
		FAT.remove(oldHash, index);

		writeFAT(index, hash);
		writeData(index, data);
	}

	public byte[] readByIndex(int index, byte[] data) throws IOException {
		if (index < 0) {
			throw new IllegalArgumentException("negative index");
		}
		index = index % entries;
		long hash = readFAT(index);
		data = readData(index, data);
		long dataHash = PartitionChunk.hash(data);
		if (dataHash != hash) {
			return null;
		} else {
			return data;
		}
	}

	public byte[] readByHash(long hash, byte[] data) throws IOException {
		Integer index = hashToIndex(hash);
		if (index == null || index < 0) {
			return null;
		}
		index = index % entries;
		data = readData(index, data);
		long dataHash = PartitionChunk.hash(data);
		if (dataHash != hash) {
			this.writeFAT(index, 0);
			return null;
		} else {
			return data;
		}
	}

	public Integer hashToIndex(long hash) {
		return FAT.get(hash);
	}

	public int getIndex() {
		return index % entries;
	}

	public long indexToHash(int index) {
		if (index < 0) {
			index = -index;
			index = index % entries;
			index = entries - index;
			index = index % entries;
		}
		index = index % entries;
		return hashes[index].get();
	}

	public void setIndex(int index) throws IOException {
		this.index = index % entries;
		writeIndex(this.index);
	}

	public void incrementIndex() throws IOException {
		setIndex(index + 1);
		writeIndex(index);
	}

	public int readIndex() throws IOException {
		indexFile.seek(0);
		return indexFile.readInt();
	}

	public void writeIndex(int index) throws IOException {
		indexFile.seek(0);
		indexFile.writeInt(index % entries);
	}

	private long readFAT(int index) throws IOException {
		FATFile.seek((index % entries)*8);
		return FATFile.readLong();
	}

	private void writeFAT(int index, long hash) throws IOException {
		index = index % entries;
		Long oldHash = hashes[index].get();
		FAT.remove(oldHash);
		hashes[index].set(hash);
		if (hash != 0) {
			FAT.put(hash, index);
		}

		FATFile.seek(index*8);
		FATFile.writeLong(hash);
	}

	private byte[] readData(int index, byte[] data) throws IOException {
		index = index % entries;
		dataFile.seek(size*index);
		if (data == null || data.length != size) {
			data = new byte[(int)size];
		}
		dataFile.readFully(data);
		return data;
	}

	private void writeData(int index, byte[] data) throws IOException {
		index = index % entries;
		dataFile.seek(size*index);
		if (data == null || data.length != size) {
			throw new IllegalArgumentException("Incorrect byte array length");
		} else {
			dataFile.write(data);
		}
	}

	public void corruptIndex(int index) throws IOException {
		System.out.println("Corrupting index: " + index);
		byte[] data = readData(index, null);
		data[123] = (byte)(data[123] + 1);
		writeData(index, data);
	}
}
