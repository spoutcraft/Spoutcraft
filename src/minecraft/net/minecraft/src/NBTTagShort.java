package net.minecraft.src;

import java.io.*;

public class NBTTagShort extends NBTBase {
	public short shortValue;

	public NBTTagShort(String s) {
		super(s);
	}

	public NBTTagShort(String s, short word0) {
		super(s);
		shortValue = word0;
	}

	void writeTagContents(DataOutput dataoutput)
	throws IOException {
		dataoutput.writeShort(shortValue);
	}

	void readTagContents(DataInput datainput)
	throws IOException {
		shortValue = datainput.readShort();
	}

	public byte getType() {
		return 2;
	}

	public String toString() {
		return (new StringBuilder()).append("").append(shortValue).toString();
	}

	public NBTBase cloneTag() {
		return new NBTTagShort(getKey(), shortValue);
	}

	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			NBTTagShort nbttagshort = (NBTTagShort)obj;
			return shortValue == nbttagshort.shortValue;
		}
		else {
			return false;
		}
	}
}
