package net.minecraft.src;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

public class SaveHandler
	implements ISaveHandler {
	private static final Logger logger = Logger.getLogger("Minecraft");
	private final File saveDirectory;
	private final File playersDirectory;
	private final File mapDataDir;
	private final long now = System.currentTimeMillis();
	private final String saveDirectoryName;

	public SaveHandler(File file, String s, boolean flag) {
		saveDirectory = new File(file, s);
		saveDirectory.mkdirs();
		playersDirectory = new File(saveDirectory, "players");
		mapDataDir = new File(saveDirectory, "data");
		mapDataDir.mkdirs();
		saveDirectoryName = s;
		if (flag) {
			playersDirectory.mkdirs();
		}
		createSessionLock();
	}

	private void createSessionLock() {
		try {
			File file = new File(saveDirectory, "session.lock");
			DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));
			try {
				dataoutputstream.writeLong(now);
			}
			finally {
				dataoutputstream.close();
			}
		}
		catch (IOException ioexception) {
			ioexception.printStackTrace();
			throw new RuntimeException("Failed to check session lock, aborting");
		}
	}

	protected File getSaveDirectory() {
		return saveDirectory;
	}

	public void checkSessionLock() {
		try {
			File file = new File(saveDirectory, "session.lock");
			DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));
			try {
				if (datainputstream.readLong() != now) {
					throw new MinecraftException("The save is being accessed from another location, aborting");
				}
			}
			finally {
				datainputstream.close();
			}
		}
		catch (IOException ioexception) {
			throw new MinecraftException("Failed to check session lock, aborting");
		}
	}

	public IChunkLoader getChunkLoader(WorldProvider worldprovider) {
		if (worldprovider instanceof WorldProviderHell) {
			File file = new File(saveDirectory, "DIM-1");
			file.mkdirs();
			return new ChunkLoader(file, true);
		}
		if (worldprovider instanceof WorldProviderEnd) {
			File file1 = new File(saveDirectory, "DIM1");
			file1.mkdirs();
			return new ChunkLoader(file1, true);
		}
		else {
			return new ChunkLoader(saveDirectory, true);
		}
	}

	public WorldInfo loadWorldInfo() {
		File file = new File(saveDirectory, "level.dat");
		if (file.exists()) {
			try {
				NBTTagCompound nbttagcompound = CompressedStreamTools.loadGzippedCompoundFromOutputStream(new FileInputStream(file));
				NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Data");
				return new WorldInfo(nbttagcompound2);
			}
			catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		file = new File(saveDirectory, "level.dat_old");
		if (file.exists()) {
			try {
				NBTTagCompound nbttagcompound1 = CompressedStreamTools.loadGzippedCompoundFromOutputStream(new FileInputStream(file));
				NBTTagCompound nbttagcompound3 = nbttagcompound1.getCompoundTag("Data");
				return new WorldInfo(nbttagcompound3);
			}
			catch (Exception exception1) {
				exception1.printStackTrace();
			}
		}
		return null;
	}

	public void saveWorldInfoAndPlayer(WorldInfo worldinfo, List list) {
		NBTTagCompound nbttagcompound = worldinfo.getNBTTagCompoundWithPlayer(list);
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		nbttagcompound1.setTag("Data", nbttagcompound);
		try {
			File file = new File(saveDirectory, "level.dat_new");
			File file1 = new File(saveDirectory, "level.dat_old");
			File file2 = new File(saveDirectory, "level.dat");
			CompressedStreamTools.writeGzippedCompoundToOutputStream(nbttagcompound1, new FileOutputStream(file));
			if (file1.exists()) {
				file1.delete();
			}
			file2.renameTo(file1);
			if (file2.exists()) {
				file2.delete();
			}
			file.renameTo(file2);
			if (file.exists()) {
				file.delete();
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void saveWorldInfo(WorldInfo worldinfo) {
		NBTTagCompound nbttagcompound = worldinfo.getNBTTagCompound();
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		nbttagcompound1.setTag("Data", nbttagcompound);
		try {
			File file = new File(saveDirectory, "level.dat_new");
			File file1 = new File(saveDirectory, "level.dat_old");
			File file2 = new File(saveDirectory, "level.dat");
			CompressedStreamTools.writeGzippedCompoundToOutputStream(nbttagcompound1, new FileOutputStream(file));
			if (file1.exists()) {
				file1.delete();
			}
			file2.renameTo(file1);
			if (file2.exists()) {
				file2.delete();
			}
			file.renameTo(file2);
			if (file.exists()) {
				file.delete();
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public File getMapFile(String s) {
		return new File(mapDataDir, (new StringBuilder()).append(s).append(".dat").toString());
	}

	public String getSaveDirectoryName() {
		return saveDirectoryName;
	}
}
