package net.minecraft.src;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;

public class MapStorage {
	private ISaveHandler saveHandler;
	private Map loadedDataMap;
	private List loadedDataList;
	private Map idCounts;

	public MapStorage(ISaveHandler isavehandler) {
		loadedDataMap = new HashMap();
		loadedDataList = new ArrayList();
		idCounts = new HashMap();
		saveHandler = isavehandler;
		loadIdCounts();
	}

	public WorldSavedData loadData(Class class1, String s) {
		WorldSavedData worldsaveddata = (WorldSavedData)loadedDataMap.get(s);
		if (worldsaveddata != null) {
			return worldsaveddata;
		}
		if (saveHandler != null) {
			try {
				File file = saveHandler.getMapFile(s);
				if (file != null && file.exists()) {
					try {
						worldsaveddata = (WorldSavedData)class1.getConstructor(new Class[] {
						            java.lang.String.class
						        }).newInstance(new Object[] {
						                    s
						                });
					}
					catch (Exception exception1) {
						throw new RuntimeException((new StringBuilder()).append("Failed to instantiate ").append(class1.toString()).toString(), exception1);
					}
					FileInputStream fileinputstream = new FileInputStream(file);
					NBTTagCompound nbttagcompound = CompressedStreamTools.loadGzippedCompoundFromOutputStream(fileinputstream);
					fileinputstream.close();
					worldsaveddata.readFromNBT(nbttagcompound.getCompoundTag("data"));
				}
			}
			catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		if (worldsaveddata != null) {
			loadedDataMap.put(s, worldsaveddata);
			loadedDataList.add(worldsaveddata);
		}
		return worldsaveddata;
	}

	public void setData(String s, WorldSavedData worldsaveddata) {
		if (worldsaveddata == null) {
			throw new RuntimeException("Can't set null data");
		}
		if (loadedDataMap.containsKey(s)) {
			loadedDataList.remove(loadedDataMap.remove(s));
		}
		loadedDataMap.put(s, worldsaveddata);
		loadedDataList.add(worldsaveddata);
	}

	public void saveAllData() {
		for (int i = 0; i < loadedDataList.size(); i++) {
			WorldSavedData worldsaveddata = (WorldSavedData)loadedDataList.get(i);
			if (worldsaveddata.isDirty()) {
				saveData(worldsaveddata);
				worldsaveddata.setDirty(false);
			}
		}
	}

	private void saveData(WorldSavedData worldsaveddata) {
		if (saveHandler == null) {
			return;
		}
		try {
			File file = saveHandler.getMapFile(worldsaveddata.mapName);
			if (file != null) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				worldsaveddata.writeToNBT(nbttagcompound);
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setCompoundTag("data", nbttagcompound);
				FileOutputStream fileoutputstream = new FileOutputStream(file);
				CompressedStreamTools.writeGzippedCompoundToOutputStream(nbttagcompound1, fileoutputstream);
				fileoutputstream.close();
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void loadIdCounts() {
		try {
			idCounts.clear();
			if (saveHandler == null) {
				return;
			}
			File file = saveHandler.getMapFile("idcounts");
			if (file != null && file.exists()) {
				DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));
				NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);
				datainputstream.close();
				Iterator iterator = nbttagcompound.getTags().iterator();
				do {
					if (!iterator.hasNext()) {
						break;
					}
					NBTBase nbtbase = (NBTBase)iterator.next();
					if (nbtbase instanceof NBTTagShort) {
						NBTTagShort nbttagshort = (NBTTagShort)nbtbase;
						String s = nbttagshort.getKey();
						short word0 = nbttagshort.shortValue;
						idCounts.put(s, Short.valueOf(word0));
					}
				}
				while (true);
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public int getUniqueDataId(String s) {
		Short short1 = (Short)idCounts.get(s);
		if (short1 == null) {
			short1 = Short.valueOf((short)0);
		}
		else {
			Short short2 = short1;
			Short short3 = short1 = Short.valueOf((short)(short1.shortValue() + 1));
			Short _tmp = short2;
		}
		idCounts.put(s, short1);
		if (saveHandler == null) {
			return short1.shortValue();
		}
		try {
			File file = saveHandler.getMapFile("idcounts");
			if (file != null) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				String s1;
				short word0;
				for (Iterator iterator = idCounts.keySet().iterator(); iterator.hasNext(); nbttagcompound.setShort(s1, word0)) {
					s1 = (String)iterator.next();
					word0 = ((Short)idCounts.get(s1)).shortValue();
				}

				DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));
				CompressedStreamTools.writeTo(nbttagcompound, dataoutputstream);
				dataoutputstream.close();
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		return short1.shortValue();
	}
}
