package net.minecraft.src;

import java.io.*;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.*;

public class RegionFileCache {
	private static final Map regionsByFilename = new HashMap();

	private RegionFileCache() {
	}

	public static synchronized RegionFile createOrLoadRegionFile(File file, int i, int j) {
		File file1 = new File(file, "region");
		File file2 = new File(file1, (new StringBuilder()).append("r.").append(i >> 5).append(".").append(j >> 5).append(".mcr").toString());
		Reference reference = (Reference)regionsByFilename.get(file2);
		if (reference != null) {
			RegionFile regionfile = (RegionFile)reference.get();
			if (regionfile != null) {
				return regionfile;
			}
		}
		if (!file1.exists()) {
			file1.mkdirs();
		}
		if (regionsByFilename.size() >= 256) {
			clearRegionFileReferences();
		}
		RegionFile regionfile1 = new RegionFile(file2);
		regionsByFilename.put(file2, new SoftReference(regionfile1));
		return regionfile1;
	}

	public static synchronized void clearRegionFileReferences() {
		Iterator iterator = regionsByFilename.values().iterator();
		do {
			if (!iterator.hasNext()) {
				break;
			}
			Reference reference = (Reference)iterator.next();
			try {
				RegionFile regionfile = (RegionFile)reference.get();
				if (regionfile != null) {
					regionfile.close();
				}
			}
			catch (IOException ioexception) {
				ioexception.printStackTrace();
			}
		}
		while (true);
		regionsByFilename.clear();
	}

	public static DataInputStream getChunkInputStream(File file, int i, int j) {
		RegionFile regionfile = createOrLoadRegionFile(file, i, j);
		return regionfile.getChunkDataInputStream(i & 0x1f, j & 0x1f);
	}

	public static DataOutputStream getChunkOutputStream(File file, int i, int j) {
		RegionFile regionfile = createOrLoadRegionFile(file, i, j);
		return regionfile.getChunkDataOutputStream(i & 0x1f, j & 0x1f);
	}
}
