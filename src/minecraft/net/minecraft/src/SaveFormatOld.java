package net.minecraft.src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveFormatOld
	implements ISaveFormat {
	protected final File savesDirectory;

	public SaveFormatOld(File file) {
		if (!file.exists()) {
			file.mkdirs();
		}
		savesDirectory = file;
	}

	public String getFormatName() {
		return "Old Format";
	}

	public List getSaveList() {
		ArrayList arraylist = new ArrayList();
		for (int i = 0; i < 5; i++) {
			String s = (new StringBuilder()).append("World").append(i + 1).toString();
			WorldInfo worldinfo = getWorldInfo(s);
			if (worldinfo != null) {
				arraylist.add(new SaveFormatComparator(s, "", worldinfo.getLastTimePlayed(), worldinfo.getSizeOnDisk(), worldinfo.getGameType(), false, worldinfo.isHardcoreModeEnabled()));
			}
		}

		return arraylist;
	}

	public void flushCache() {
	}

	public WorldInfo getWorldInfo(String s) {
		File file = new File(savesDirectory, s);
		if (!file.exists()) {
			return null;
		}
		File file1 = new File(file, "level.dat");
		if (file1.exists()) {
			try {
				NBTTagCompound nbttagcompound = CompressedStreamTools.loadGzippedCompoundFromOutputStream(new FileInputStream(file1));
				NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Data");
				return new WorldInfo(nbttagcompound2);
			}
			catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		file1 = new File(file, "level.dat_old");
		if (file1.exists()) {
			try {
				NBTTagCompound nbttagcompound1 = CompressedStreamTools.loadGzippedCompoundFromOutputStream(new FileInputStream(file1));
				NBTTagCompound nbttagcompound3 = nbttagcompound1.getCompoundTag("Data");
				return new WorldInfo(nbttagcompound3);
			}
			catch (Exception exception1) {
				exception1.printStackTrace();
			}
		}
		return null;
	}

	public void renameWorld(String s, String s1) {
		File file = new File(savesDirectory, s);
		if (!file.exists()) {
			return;
		}
		File file1 = new File(file, "level.dat");
		if (file1.exists()) {
			try {
				NBTTagCompound nbttagcompound = CompressedStreamTools.loadGzippedCompoundFromOutputStream(new FileInputStream(file1));
				NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
				nbttagcompound1.setString("LevelName", s1);
				CompressedStreamTools.writeGzippedCompoundToOutputStream(nbttagcompound, new FileOutputStream(file1));
			}
			catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public void deleteWorldDirectory(String s) {
		File file = new File(savesDirectory, s);
		if (!file.exists()) {
			return;
		}
		else {
			deleteFiles(file.listFiles());
			file.delete();
			return;
		}
	}

	protected static void deleteFiles(File afile[]) {
		for (int i = 0; i < afile.length; i++) {
			if (afile[i].isDirectory()) {
				System.out.println((new StringBuilder()).append("Deleting ").append(afile[i]).toString());
				deleteFiles(afile[i].listFiles());
			}
			afile[i].delete();
		}
	}

	public ISaveHandler getSaveLoader(String s, boolean flag) {
		return new SaveHandler(savesDirectory, s, flag);
	}

	public boolean isOldMapFormat(String s) {
		return false;
	}

	public boolean convertMapFormat(String s, IProgressUpdate iprogressupdate) {
		return false;
	}
}
