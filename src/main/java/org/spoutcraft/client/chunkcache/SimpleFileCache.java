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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SimpleFileCache {
	private static final int VERSION = 2;
	private static final String PREFIX = "SPC";

	private final File dir;
	private final Comparator<File> fileCompare = new FileCompare();
	private final long limit;
	private final int entries;

	private final ConcurrentHashMap<Long, FATEntry> FATMap = new ConcurrentHashMap<Long, FATEntry>();
	private final ConcurrentHashMap<Integer, long[]> FATIMap = new ConcurrentHashMap<Integer, long[]>();
	private final ConcurrentHashMap<Long, Reference<byte[]>> cache = new ConcurrentHashMap<Long, Reference<byte[]>>();
	private final ConcurrentLinkedQueue<MapEntry> newHashQueue = new ConcurrentLinkedQueue<MapEntry>();

	private final AtomicInteger entriesPending = new AtomicInteger();
	private final AtomicInteger fileCount = new AtomicInteger();

	public SimpleFileCache(File dir, int entries, long limit) throws IOException {
		this.dir = dir;
		this.limit = limit;
		this.entries = entries;
		this.entriesPending.set(this.entries);

		if (!dir.isDirectory()) {
			if (dir.isFile()) {
				throw new IOException("Unable to open cache directory");
			} else {
				dir.mkdirs();
			}
		}

		File[] files = dir.listFiles();
		Arrays.sort(files, fileCompare);

		prune(files);

		files = dir.listFiles();
		Arrays.sort(files, fileCompare);

		readFAT(files);

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				writePending(true);
				prune();
			}
		}));
	}

	private void prune() {
		File[] files = dir.listFiles();
		Arrays.sort(files, fileCompare);

		prune(files);
	}

	private void prune(File[] files) {
		int size = 0;

		for (File file : files) {
			if (file.isFile()) {
				size += file.length();
			}
		}
		int cnt = 0;

		while (size > (limit << 10) && cnt < files.length) {
			File current = files[cnt++];
			if (current.isFile()) {
				size -= current.length();
				current.delete();
			}
		}
	}

	private void readFAT(File[] files) throws IOException {
		for (File f : files) {
			if (!isValid(f)) {
				continue;
			}
			boolean delete = false;
			int id = getIntFromName(f);
			DataInputStream din = new DataInputStream(new FileInputStream(f));
			try {
				int version = din.readInt();
				if (version < VERSION) {
					System.out.println("File " + f.getAbsolutePath() + " has out of date version " + version + ", deleting");
					delete = true;
					continue;
				} else if (version > VERSION){
					System.out.println("File " + f.getAbsolutePath() + " has unknown version " + version);
					continue;
				}
				int entries = din.readInt();
				if (entries > (this.entries * 2)) {
					System.out.println("File " + f.getAbsolutePath() + " has to many entries " + entries + ", skipping");
					continue;
				}
				long[] hashArray = new long[entries];
				for (int i = 0; i < entries; i++) {
					long hash = din.readLong();
					FATMap.put(hash, new FATEntry(id, i));
					hashArray[i] = hash;
				}
				FATIMap.put(id, hashArray);
			} finally {
				if (din != null) {
					try {
						din.close();
					} catch (IOException ioe) {
					}
				}
				if (delete) {
					f.delete();
				}
			}
		}
	}

	private byte[] readFile(int id, long needle) throws IOException {
		File f = getFileFromInt(id);

		if (!isValid(f)) {
			return null;
		}

		byte[] returnArray = null;

		DataInputStream din = new DataInputStream(new FileInputStream(f));
		try {
			int version = din.readInt();
			if (version != VERSION) {
				System.out.println("File " + f.getAbsolutePath() + " has out of date version " + version);
				return null;
			}
			int entries = din.readInt();
			if (entries > (this.entries * 2)) {
				System.out.println("File " + f.getAbsolutePath() + " has to many entries " + entries);
				return null;
			}
			long[] hash = new long[entries];

			for (int i = 0; i < entries; i++) {
				hash[i] = din.readLong();
			}

			din = new DataInputStream(new GZIPInputStream(din));

			for (int i = 0; i < entries; i++) {
				byte[] array = new byte[2048];
				din.readFully(array);
				long newHash = PartitionChunk.hash(array);
				if (newHash == hash[i]) {
					cache.put(hash[i], new SoftReference<byte[]>(array));
					if (hash[i] == needle) {
						returnArray = array;
					}
				} else {
					System.out.println("Hash in file " + f.getAbsolutePath() + " has hash data mismatch");
				}
			}
		} finally {
			if (din != null) {
				try {
					din.close();
				} catch (IOException ioe) {
				}
			}
		}

		return returnArray;

	}

	public long putData(byte[] data) {
		MapEntry entry = new MapEntry(data);

		cache.put(entry.getHash(), new HardReference<byte[]>(entry.getData()));
		newHashQueue.add(entry);

		entriesPending.decrementAndGet();

		checkFileWrite();

		return entry.getHash();
	}

	public byte[] getData(long hash) {

		Reference<byte[]> ref = cache.get(hash);

		byte[] data = null;

		if (ref != null) {
			data = ref.get();
		}

		if (data != null) {
			return data;
		}

		FATEntry entry = FATMap.get(hash);

		if (entry == null) {
			return null;
		}

		try {
			data = readFile(entry.getId(), hash);
		} catch (IOException e) {
			File f = getFileFromInt(entry.getId());
			f.delete();
			FATMap.remove(hash);
			long[] fileHashes = FATIMap.remove(entry.getId());
			for (long h : fileHashes) {
				FATMap.remove(h);
			}
			System.out.println("Deleting corrupted chunk cache file " + entry.getId() + ", " + e.getMessage());
			e.printStackTrace();
			return null;
		}

		return data;
	}

	public long[] getNearby(long hash, int range) {
		FATEntry entry = FATMap.get(hash);

		if (entry == null) {
			return null;
		}

		long[] fileHashes = FATIMap.get(entry.getId());

		if (fileHashes == null) {
			System.out.println("Cache FAT inverse map failure");
			return null;
		}

		int index = entry.getIndex();

		if (fileHashes[index] != hash) {
			throw new IllegalStateException("FAT inverse map mismatch, expected " + hash + ", got " + fileHashes[index]);
		}

		int start = index - range;
		int end = index + range;
		if (start < 0) {
			start = 0;
		}
		if (end > fileHashes.length) {
			end = fileHashes.length;
		}

		long[] nearby = new long[end - start];

		int i = index;
		int j = index + 1;
		int k = 0;
		while (i >= start || j < end) {
			if (i >= start) {
				nearby[k++] = fileHashes[i--];
			}
			if (j < end) {
				nearby[k++] = fileHashes[j++];
			}
		}
		if (k != nearby.length) {
			throw new IllegalStateException("File hash array length calculation error");
		}
		return nearby;
	}

	private void checkFileWrite() {
		boolean success = false;
		while (!success) {
			int old = entriesPending.get();
			if (old > 0) {
				success = true;
			} else {
				success = entriesPending.compareAndSet(old, old + entries);
				if (success) {
					writePending(false);
				}
			}
		}
	}

	private void writePending(boolean blocking) {
		int id = fileCount.getAndIncrement();

		ArrayList<MapEntry> entryList = new ArrayList<MapEntry>(entries << 1);

		MapEntry entry;
		while ((entry = newHashQueue.poll()) != null) {
			entryList.add(entry);
		}

		if (entryList.size() > 0) {
			Thread t = new DataWriteThread(id, entryList);
			t.start();
			if (blocking) {
				try {
					t.join();
				} catch (InterruptedException ie) {
					throw new RuntimeException(ie);
				}
			}
		}
	}

	private static boolean isValid(File file) {
		if (file != null) {
			String fileName = file.getName();
			if (!fileName.startsWith(PREFIX)) {
				return false;
			}
			try {
				Integer.parseInt(fileName.substring(3));
			} catch (NumberFormatException nfe) {
				return false;
			}
			return true;
		}
		return false;
	}

	public int getIntFromName(File file) {
		try {
			String fileName = file.getName();
			if (fileName == null || fileName.length() < 3) {
				return 0;
			}
			int id = Integer.parseInt(file.getName().substring(3));
			boolean success = false;
			while (!success) {
				int oldId = fileCount.get();
				if (id < oldId) {
					success = true;
				} else {
					success = fileCount.compareAndSet(oldId, id + 1);
				}
			}
			return id;
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}

	public File getFileFromInt(int id) {
		return new File(dir, PREFIX + id);
	}

	private class FileCompare implements Comparator<File> {

		public int compare(File f1, File f2) {
			return getIntFromName(f1) - getIntFromName(f2);
		}

	}

	private class DataWriteThread extends Thread {

		private final List<MapEntry> entryList;
		private final int id;

		public DataWriteThread(int id, List<MapEntry> entryList) {
			super("SimpleFileCache file write thread, fileid " + id);
			this.id = id;
			this.entryList = entryList;
		}

		public void run() {
			File file = getFileFromInt(id);

			DataOutputStream dos = null;

			try {
				dos = new DataOutputStream(new FileOutputStream(file));

				dos.writeInt(VERSION);
				dos.writeInt(entryList.size());
				for (MapEntry e : entryList) {
					dos.writeLong(e.getHash());
				}

				dos = new DataOutputStream(new GZIPOutputStream(dos));

				int i = 0;
				for (MapEntry e : entryList) {
					byte[] data = e.getData();
					dos.write(data, 0, data.length);
				}
			} catch (IOException ioe) {
				throw new RuntimeException(ioe);
			} finally {
				if (dos != null) {
					try {
						dos.close();
					} catch (IOException ioe) {
					}
				}
			}

			long[] hashArray = new long[entries];
			int i = 0;
			for (MapEntry e : entryList) {
				cache.put(e.getHash(), new SoftReference<byte[]>(e.getData()));
				FATMap.put(e.getHash(), new FATEntry(id, i));
				hashArray[i++] = e.getHash();
			}
			FATIMap.put(id, hashArray);
		}
	}

	private static class FATEntry {
		private final int id;
		private final int index;

		public FATEntry(int id, int index) {
			this.id = id;
			this.index = index;
		}

		public int getId() {
			return id;
		}

		public int getIndex() {
			return index;
		}

	}

	private static class MapEntry {
		private final long hash;
		private final byte[] data;

		public MapEntry(byte[] data) {
			if (data.length != 2048) {
				throw new IllegalArgumentException("Data length must be 2048 bytes long");
			}
			this.data = new byte[data.length];
			System.arraycopy(data, 0, this.data, 0, data.length);
			this.hash = PartitionChunk.hash(this.data);
		}

		public long getHash() {
			return hash;
		}

		public byte[] getData() {
			return data;
		}
	}

	public class HardReference<T> extends SoftReference<T> {
		private final T hard;

		@Override
		public T get() {
			return hard;
		}

		HardReference(T ref) {
			super(ref);
			hard = ref;
		}
	}
}
