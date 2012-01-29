package net.minecraft.src;

import java.io.*;
import java.util.*;

public class DataWatcher {
	private static final HashMap dataTypes;
	private final Map watchedObjects = new HashMap();
	private boolean objectChanged;

	public DataWatcher() {
	}

	public void addObject(int i, Object obj) {
		Integer integer = (Integer)dataTypes.get(obj.getClass());
		if (integer == null) {
			throw new IllegalArgumentException((new StringBuilder()).append("Unknown data type: ").append(obj.getClass()).toString());
		}
		if (i > 31) {
			throw new IllegalArgumentException((new StringBuilder()).append("Data value id is too big with ").append(i).append("! (Max is ").append(31).append(")").toString());
		}
		if (watchedObjects.containsKey(Integer.valueOf(i))) {
			throw new IllegalArgumentException((new StringBuilder()).append("Duplicate id value for ").append(i).append("!").toString());
		}
		else {
			WatchableObject watchableobject = new WatchableObject(integer.intValue(), i, obj);
			watchedObjects.put(Integer.valueOf(i), watchableobject);
			return;
		}
	}

	public byte getWatchableObjectByte(int i) {
		return ((Byte)((WatchableObject)watchedObjects.get(Integer.valueOf(i))).getObject()).byteValue();
	}

	public short getWatchableObjectShort(int i) {
		return ((Short)((WatchableObject)watchedObjects.get(Integer.valueOf(i))).getObject()).shortValue();
	}

	public int getWatchableObjectInt(int i) {
		return ((Integer)((WatchableObject)watchedObjects.get(Integer.valueOf(i))).getObject()).intValue();
	}

	public String getWatchableObjectString(int i) {
		return (String)((WatchableObject)watchedObjects.get(Integer.valueOf(i))).getObject();
	}

	public void updateObject(int i, Object obj) {
		WatchableObject watchableobject = (WatchableObject)watchedObjects.get(Integer.valueOf(i));
		if (!obj.equals(watchableobject.getObject())) {
			watchableobject.setObject(obj);
			watchableobject.setWatching(true);
			objectChanged = true;
		}
	}

	public static void writeObjectsInListToStream(List list, DataOutputStream dataoutputstream)
	throws IOException {
		if (list != null) {
			WatchableObject watchableobject;
			for (Iterator iterator = list.iterator(); iterator.hasNext(); writeWatchableObject(dataoutputstream, watchableobject)) {
				watchableobject = (WatchableObject)iterator.next();
			}
		}
		dataoutputstream.writeByte(127);
	}

	public void writeWatchableObjects(DataOutputStream dataoutputstream)
	throws IOException {
		WatchableObject watchableobject;
		for (Iterator iterator = watchedObjects.values().iterator(); iterator.hasNext(); writeWatchableObject(dataoutputstream, watchableobject)) {
			watchableobject = (WatchableObject)iterator.next();
		}

		dataoutputstream.writeByte(127);
	}

	private static void writeWatchableObject(DataOutputStream dataoutputstream, WatchableObject watchableobject)
	throws IOException {
		int i = (watchableobject.getObjectType() << 5 | watchableobject.getDataValueId() & 0x1f) & 0xff;
		dataoutputstream.writeByte(i);
		switch (watchableobject.getObjectType()) {
			case 0:
				dataoutputstream.writeByte(((Byte)watchableobject.getObject()).byteValue());
				break;

			case 1:
				dataoutputstream.writeShort(((Short)watchableobject.getObject()).shortValue());
				break;

			case 2:
				dataoutputstream.writeInt(((Integer)watchableobject.getObject()).intValue());
				break;

			case 3:
				dataoutputstream.writeFloat(((Float)watchableobject.getObject()).floatValue());
				break;

			case 4:
				Packet.writeString((String)watchableobject.getObject(), dataoutputstream);
				break;

			case 5:
				ItemStack itemstack = (ItemStack)watchableobject.getObject();
				dataoutputstream.writeShort(itemstack.getItem().shiftedIndex);
				dataoutputstream.writeByte(itemstack.stackSize);
				dataoutputstream.writeShort(itemstack.getItemDamage());
				break;

			case 6:
				ChunkCoordinates chunkcoordinates = (ChunkCoordinates)watchableobject.getObject();
				dataoutputstream.writeInt(chunkcoordinates.posX);
				dataoutputstream.writeInt(chunkcoordinates.posY);
				dataoutputstream.writeInt(chunkcoordinates.posZ);
				break;
		}
	}

	public static List readWatchableObjects(DataInputStream datainputstream)
	throws IOException {
		ArrayList arraylist = null;
		for (byte byte0 = datainputstream.readByte(); byte0 != 127; byte0 = datainputstream.readByte()) {
			if (arraylist == null) {
				arraylist = new ArrayList();
			}
			int i = (byte0 & 0xe0) >> 5;
			int j = byte0 & 0x1f;
			WatchableObject watchableobject = null;
			switch (i) {
				case 0:
					watchableobject = new WatchableObject(i, j, Byte.valueOf(datainputstream.readByte()));
					break;

				case 1:
					watchableobject = new WatchableObject(i, j, Short.valueOf(datainputstream.readShort()));
					break;

				case 2:
					watchableobject = new WatchableObject(i, j, Integer.valueOf(datainputstream.readInt()));
					break;

				case 3:
					watchableobject = new WatchableObject(i, j, Float.valueOf(datainputstream.readFloat()));
					break;

				case 4:
					watchableobject = new WatchableObject(i, j, Packet.readString(datainputstream, 64));
					break;

				case 5:
					short word0 = datainputstream.readShort();
					byte byte1 = datainputstream.readByte();
					short word1 = datainputstream.readShort();
					watchableobject = new WatchableObject(i, j, new ItemStack(word0, byte1, word1));
					break;

				case 6:
					int k = datainputstream.readInt();
					int l = datainputstream.readInt();
					int i1 = datainputstream.readInt();
					watchableobject = new WatchableObject(i, j, new ChunkCoordinates(k, l, i1));
					break;
			}
			arraylist.add(watchableobject);
		}

		return arraylist;
	}

	public void updateWatchedObjectsFromList(List list) {
		Iterator iterator = list.iterator();
		do {
			if (!iterator.hasNext()) {
				break;
			}
			WatchableObject watchableobject = (WatchableObject)iterator.next();
			WatchableObject watchableobject1 = (WatchableObject)watchedObjects.get(Integer.valueOf(watchableobject.getDataValueId()));
			if (watchableobject1 != null) {
				watchableobject1.setObject(watchableobject.getObject());
			}
		}
		while (true);
	}

	static Class _mthclass$(String s) {
		try {
			return Class.forName(s);
		}
		catch (ClassNotFoundException classnotfoundexception) {
			throw new NoClassDefFoundError(classnotfoundexception.getMessage());
		}
	}

	static {
		dataTypes = new HashMap();
		dataTypes.put(java.lang.Byte.class, Integer.valueOf(0));
		dataTypes.put(java.lang.Short.class, Integer.valueOf(1));
		dataTypes.put(java.lang.Integer.class, Integer.valueOf(2));
		dataTypes.put(java.lang.Float.class, Integer.valueOf(3));
		dataTypes.put(java.lang.String.class, Integer.valueOf(4));
		dataTypes.put(net.minecraft.src.ItemStack.class, Integer.valueOf(5));
		dataTypes.put(net.minecraft.src.ChunkCoordinates.class, Integer.valueOf(6));
	}
}
