package net.minecraft.src;

import java.io.*;
import java.util.*;

public class ChunkLoader
	implements IChunkLoader {
	private File saveDir;
	private boolean createIfNecessary;

	public ChunkLoader(File file, boolean flag) {
		saveDir = file;
		createIfNecessary = flag;
	}

	private File chunkFileForXZ(int i, int j) {
		String s = (new StringBuilder()).append("c.").append(Integer.toString(i, 36)).append(".").append(Integer.toString(j, 36)).append(".dat").toString();
		String s1 = Integer.toString(i & 0x3f, 36);
		String s2 = Integer.toString(j & 0x3f, 36);
		File file = new File(saveDir, s1);
		if (!file.exists()) {
			if (createIfNecessary) {
				file.mkdir();
			}
			else {
				return null;
			}
		}
		file = new File(file, s2);
		if (!file.exists()) {
			if (createIfNecessary) {
				file.mkdir();
			}
			else {
				return null;
			}
		}
		file = new File(file, s);
		if (!file.exists() && !createIfNecessary) {
			return null;
		}
		else {
			return file;
		}
	}

	public Chunk loadChunk(World world, int i, int j)
	throws IOException {
		File file = chunkFileForXZ(i, j);
		if (file != null && file.exists()) {
			try {
				FileInputStream fileinputstream = new FileInputStream(file);
				NBTTagCompound nbttagcompound = CompressedStreamTools.loadGzippedCompoundFromOutputStream(fileinputstream);
				if (!nbttagcompound.hasKey("Level")) {
					System.out.println((new StringBuilder()).append("Chunk file at ").append(i).append(",").append(j).append(" is missing level data, skipping").toString());
					return null;
				}
				if (!nbttagcompound.getCompoundTag("Level").hasKey("Blocks")) {
					System.out.println((new StringBuilder()).append("Chunk file at ").append(i).append(",").append(j).append(" is missing block data, skipping").toString());
					return null;
				}
				Chunk chunk = loadChunkIntoWorldFromCompound(world, nbttagcompound.getCompoundTag("Level"));
				if (!chunk.isAtLocation(i, j)) {
					System.out.println((new StringBuilder()).append("Chunk file at ").append(i).append(",").append(j).append(" is in the wrong location; relocating. (Expected ").append(i).append(", ").append(j).append(", got ").append(chunk.xPosition).append(", ").append(chunk.zPosition).append(")").toString());
					nbttagcompound.setInteger("xPos", i);
					nbttagcompound.setInteger("zPos", j);
					chunk = loadChunkIntoWorldFromCompound(world, nbttagcompound.getCompoundTag("Level"));
				}
				chunk.removeUnknownBlocks();
				return chunk;
			}
			catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		return null;
	}

	public void saveChunk(World world, Chunk chunk)
	throws IOException {
		world.checkSessionLock();
		File file = chunkFileForXZ(chunk.xPosition, chunk.zPosition);
		if (file.exists()) {
			WorldInfo worldinfo = world.getWorldInfo();
			worldinfo.setSizeOnDisk(worldinfo.getSizeOnDisk() - file.length());
		}
		try {
			File file1 = new File(saveDir, "tmp_chunk.dat");
			FileOutputStream fileoutputstream = new FileOutputStream(file1);
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound.setTag("Level", nbttagcompound1);
			storeChunkInCompound(chunk, world, nbttagcompound1);
			CompressedStreamTools.writeGzippedCompoundToOutputStream(nbttagcompound, fileoutputstream);
			fileoutputstream.close();
			if (file.exists()) {
				file.delete();
			}
			file1.renameTo(file);
			WorldInfo worldinfo1 = world.getWorldInfo();
			worldinfo1.setSizeOnDisk(worldinfo1.getSizeOnDisk() + file.length());
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static void storeChunkInCompound(Chunk chunk, World world, NBTTagCompound nbttagcompound) {
		world.checkSessionLock();
		nbttagcompound.setInteger("xPos", chunk.xPosition);
		nbttagcompound.setInteger("zPos", chunk.zPosition);
		nbttagcompound.setLong("LastUpdate", world.getWorldTime());
		nbttagcompound.setByteArray("Blocks", chunk.blocks);
		nbttagcompound.setByteArray("Data", chunk.data.data);
		nbttagcompound.setByteArray("SkyLight", chunk.skylightMap.data);
		nbttagcompound.setByteArray("BlockLight", chunk.blocklightMap.data);
		nbttagcompound.setByteArray("HeightMap", chunk.heightMap);
		nbttagcompound.setBoolean("TerrainPopulated", chunk.isTerrainPopulated);
		chunk.hasEntities = false;
		NBTTagList nbttaglist = new NBTTagList();
		label0:
		for (int i = 0; i < chunk.entities.length; i++) {
			Iterator iterator = chunk.entities[i].iterator();
			do {
				if (!iterator.hasNext()) {
					continue label0;
				}
				Entity entity = (Entity)iterator.next();
				chunk.hasEntities = true;
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				if (entity.addEntityID(nbttagcompound1)) {
					nbttaglist.setTag(nbttagcompound1);
				}
			}
			while (true);
		}

		nbttagcompound.setTag("Entities", nbttaglist);
		NBTTagList nbttaglist1 = new NBTTagList();
		NBTTagCompound nbttagcompound2;
		for (Iterator iterator1 = chunk.chunkTileEntityMap.values().iterator(); iterator1.hasNext(); nbttaglist1.setTag(nbttagcompound2)) {
			TileEntity tileentity = (TileEntity)iterator1.next();
			nbttagcompound2 = new NBTTagCompound();
			tileentity.writeToNBT(nbttagcompound2);
		}

		nbttagcompound.setTag("TileEntities", nbttaglist1);
		List list = world.func_41081_a(chunk, false);
		if (list != null) {
			long l = world.getWorldTime();
			NBTTagList nbttaglist2 = new NBTTagList();
			NBTTagCompound nbttagcompound3;
			for (Iterator iterator2 = list.iterator(); iterator2.hasNext(); nbttaglist2.setTag(nbttagcompound3)) {
				NextTickListEntry nextticklistentry = (NextTickListEntry)iterator2.next();
				nbttagcompound3 = new NBTTagCompound();
				nbttagcompound3.setInteger("i", nextticklistentry.blockID);
				nbttagcompound3.setInteger("x", nextticklistentry.xCoord);
				nbttagcompound3.setInteger("y", nextticklistentry.yCoord);
				nbttagcompound3.setInteger("z", nextticklistentry.zCoord);
				nbttagcompound3.setInteger("t", (int)(nextticklistentry.scheduledTime - l));
			}

			nbttagcompound.setTag("TileTicks", nbttaglist2);
		}
	}

	public static Chunk loadChunkIntoWorldFromCompound(World world, NBTTagCompound nbttagcompound) {
		int i = nbttagcompound.getInteger("xPos");
		int j = nbttagcompound.getInteger("zPos");
		Chunk chunk = new Chunk(world, i, j);
		chunk.blocks = nbttagcompound.getByteArray("Blocks");
		chunk.data = new NibbleArray(nbttagcompound.getByteArray("Data"), world.heightShift);
		chunk.skylightMap = new NibbleArray(nbttagcompound.getByteArray("SkyLight"), world.heightShift);
		chunk.blocklightMap = new NibbleArray(nbttagcompound.getByteArray("BlockLight"), world.heightShift);
		chunk.heightMap = nbttagcompound.getByteArray("HeightMap");
		chunk.isTerrainPopulated = nbttagcompound.getBoolean("TerrainPopulated");
		if (!chunk.data.isValid()) {
			chunk.data = new NibbleArray(chunk.blocks.length, world.heightShift);
		}
		if (chunk.heightMap == null || !chunk.skylightMap.isValid()) {
			chunk.heightMap = new byte[256];
			chunk.skylightMap = new NibbleArray(chunk.blocks.length, world.heightShift);
			chunk.generateSkylightMap();
		}
		if (!chunk.blocklightMap.isValid()) {
			chunk.blocklightMap = new NibbleArray(chunk.blocks.length, world.heightShift);
			chunk.func_1014_a();
		}
		NBTTagList nbttaglist = nbttagcompound.getTagList("Entities");
		if (nbttaglist != null) {
			for (int k = 0; k < nbttaglist.tagCount(); k++) {
				NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(k);
				Entity entity = EntityList.createEntityFromNBT(nbttagcompound1, world);
				chunk.hasEntities = true;
				if (entity != null) {
					chunk.addEntity(entity);
				}
			}
		}
		NBTTagList nbttaglist1 = nbttagcompound.getTagList("TileEntities");
		if (nbttaglist1 != null) {
			for (int l = 0; l < nbttaglist1.tagCount(); l++) {
				NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbttaglist1.tagAt(l);
				TileEntity tileentity = TileEntity.createAndLoadEntity(nbttagcompound2);
				if (tileentity != null) {
					chunk.addTileEntity(tileentity);
				}
			}
		}
		if (nbttagcompound.hasKey("TileTicks")) {
			NBTTagList nbttaglist2 = nbttagcompound.getTagList("TileTicks");
			if (nbttaglist2 != null) {
				for (int i1 = 0; i1 < nbttaglist2.tagCount(); i1++) {
					NBTTagCompound nbttagcompound3 = (NBTTagCompound)nbttaglist2.tagAt(i1);
					world.func_41083_e(nbttagcompound3.getInteger("x"), nbttagcompound3.getInteger("y"), nbttagcompound3.getInteger("z"), nbttagcompound3.getInteger("i"), nbttagcompound3.getInteger("t"));
				}
			}
		}
		return chunk;
	}

	public void func_814_a() {
	}

	public void saveExtraData() {
	}

	public void saveExtraChunkData(World world, Chunk chunk)
	throws IOException {
	}
}
