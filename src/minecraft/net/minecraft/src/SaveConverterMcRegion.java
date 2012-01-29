package net.minecraft.src;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class SaveConverterMcRegion extends SaveFormatOld {
	public SaveConverterMcRegion(File file) {
		super(file);
	}

	public String getFormatName() {
		return "Scaevolus' McRegion";
	}

	public List getSaveList() {
		ArrayList arraylist = new ArrayList();
		File afile[] = savesDirectory.listFiles();
		File afile1[] = afile;
		int i = afile1.length;
		for (int j = 0; j < i; j++) {
			File file = afile1[j];
			if (!file.isDirectory()) {
				continue;
			}
			String s = file.getName();
			WorldInfo worldinfo = getWorldInfo(s);
			if (worldinfo == null) {
				continue;
			}
			boolean flag = worldinfo.getSaveVersion() != 19132;
			String s1 = worldinfo.getWorldName();
			if (s1 == null || MathHelper.stringNullOrLengthZero(s1)) {
				s1 = s;
			}
			long l = 0L;
			arraylist.add(new SaveFormatComparator(s, s1, worldinfo.getLastTimePlayed(), l, worldinfo.getGameType(), flag, worldinfo.isHardcoreModeEnabled()));
		}

		return arraylist;
	}

	public void flushCache() {
		RegionFileCache.clearRegionFileReferences();
	}

	public ISaveHandler getSaveLoader(String s, boolean flag) {
		return new SaveOldDir(savesDirectory, s, flag);
	}

	public boolean isOldMapFormat(String s) {
		WorldInfo worldinfo = getWorldInfo(s);
		return worldinfo != null && worldinfo.getSaveVersion() == 0;
	}

	public boolean convertMapFormat(String s, IProgressUpdate iprogressupdate) {
		iprogressupdate.setLoadingProgress(0);
		ArrayList arraylist = new ArrayList();
		ArrayList arraylist1 = new ArrayList();
		ArrayList arraylist2 = new ArrayList();
		ArrayList arraylist3 = new ArrayList();
		ArrayList arraylist4 = new ArrayList();
		ArrayList arraylist5 = new ArrayList();
		File file = new File(savesDirectory, s);
		File file1 = new File(file, "DIM-1");
		File file2 = new File(file, "DIM1");
		System.out.println("Scanning folders...");
		func_22183_a(file, arraylist, arraylist1);
		if (file1.exists()) {
			func_22183_a(file1, arraylist2, arraylist3);
		}
		if (file2.exists()) {
			func_22183_a(file2, arraylist4, arraylist5);
		}
		int i = arraylist.size() + arraylist2.size() + arraylist4.size() + arraylist1.size() + arraylist3.size() + arraylist5.size();
		System.out.println((new StringBuilder()).append("Total conversion count is ").append(i).toString());
		func_22181_a(file, arraylist, 0, i, iprogressupdate);
		func_22181_a(file1, arraylist2, arraylist.size(), i, iprogressupdate);
		func_22181_a(file2, arraylist4, arraylist.size() + arraylist2.size(), i, iprogressupdate);
		WorldInfo worldinfo = getWorldInfo(s);
		worldinfo.setSaveVersion(19132);
		ISaveHandler isavehandler = getSaveLoader(s, false);
		isavehandler.saveWorldInfo(worldinfo);
		func_22182_a(arraylist1, arraylist.size() + arraylist2.size(), i, iprogressupdate);
		if (file1.exists()) {
			func_22182_a(arraylist3, arraylist.size() + arraylist2.size() + arraylist1.size(), i, iprogressupdate);
		}
		return true;
	}

	private void func_22183_a(File file, ArrayList arraylist, ArrayList arraylist1) {
		ChunkFolderPattern chunkfolderpattern = new ChunkFolderPattern(null);
		ChunkFilePattern chunkfilepattern = new ChunkFilePattern(null);
		File afile[] = file.listFiles(chunkfolderpattern);
		File afile1[] = afile;
		int i = afile1.length;
		for (int j = 0; j < i; j++) {
			File file1 = afile1[j];
			arraylist1.add(file1);
			File afile2[] = file1.listFiles(chunkfolderpattern);
			File afile3[] = afile2;
			int k = afile3.length;
			for (int l = 0; l < k; l++) {
				File file2 = afile3[l];
				File afile4[] = file2.listFiles(chunkfilepattern);
				File afile5[] = afile4;
				int i1 = afile5.length;
				for (int j1 = 0; j1 < i1; j1++) {
					File file3 = afile5[j1];
					arraylist.add(new ChunkFile(file3));
				}
			}
		}
	}

	private void func_22181_a(File file, ArrayList arraylist, int i, int j, IProgressUpdate iprogressupdate) {
		Collections.sort(arraylist);
		byte abyte0[] = new byte[4096];
		int i1;
		for (Iterator iterator = arraylist.iterator(); iterator.hasNext(); iprogressupdate.setLoadingProgress(i1)) {
			ChunkFile chunkfile = (ChunkFile)iterator.next();
			int k = chunkfile.getXChunk();
			int l = chunkfile.getYChunk();
			RegionFile regionfile = RegionFileCache.createOrLoadRegionFile(file, k, l);
			if (!regionfile.isChunkSaved(k & 0x1f, l & 0x1f)) {
				try {
					DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(chunkfile.getChunkFile()))));
					DataOutputStream dataoutputstream = regionfile.getChunkDataOutputStream(k & 0x1f, l & 0x1f);
					for (int j1 = 0; (j1 = datainputstream.read(abyte0)) != -1;) {
						dataoutputstream.write(abyte0, 0, j1);
					}

					dataoutputstream.close();
					datainputstream.close();
				}
				catch (IOException ioexception) {
					ioexception.printStackTrace();
				}
			}
			i++;
			i1 = (int)Math.round((100D * (double)i) / (double)j);
		}

		RegionFileCache.clearRegionFileReferences();
	}

	private void func_22182_a(ArrayList arraylist, int i, int j, IProgressUpdate iprogressupdate) {
		int k;
		for (Iterator iterator = arraylist.iterator(); iterator.hasNext(); iprogressupdate.setLoadingProgress(k)) {
			File file = (File)iterator.next();
			File afile[] = file.listFiles();
			deleteFiles(afile);
			file.delete();
			i++;
			k = (int)Math.round((100D * (double)i) / (double)j);
		}
	}
}
