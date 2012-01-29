package net.minecraft.src;

import java.io.*;

public class NBTTagLong extends NBTBase {
	public long longValue;

	public NBTTagLong(String s) {
		super(s);
	}

	public NBTTagLong(String s, long l) {
		super(s);
		longValue = l;
	}

	void writeTagContents(DataOutput dataoutput)
	throws IOException {
		dataoutput.writeLong(longValue);
	}

	void readTagContents(DataInput datainput)
	throws IOException {
		longValue = datainput.readLong();
	}

	public byte getType() {
		return 4;
	}

	public String toString() {
		return (new StringBuilder()).append("").append(longValue).toString();
	}

	public NBTBase cloneTag() {
		return new NBTTagLong(getKey(), longValue);
	}

	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			NBTTagLong nbttaglong = (NBTTagLong)obj;
			return longValue == nbttaglong.longValue;
		}
		else {
			return false;
		}
	}
}
