package net.minecraft.src;

import java.io.*;

public class NBTTagInt extends NBTBase {
	public int intValue;

	public NBTTagInt(String s) {
		super(s);
	}

	public NBTTagInt(String s, int i) {
		super(s);
		intValue = i;
	}

	void writeTagContents(DataOutput dataoutput)
	throws IOException {
		dataoutput.writeInt(intValue);
	}

	void readTagContents(DataInput datainput)
	throws IOException {
		intValue = datainput.readInt();
	}

	public byte getType() {
		return 3;
	}

	public String toString() {
		return (new StringBuilder()).append("").append(intValue).toString();
	}

	public NBTBase cloneTag() {
		return new NBTTagInt(getKey(), intValue);
	}

	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			NBTTagInt nbttagint = (NBTTagInt)obj;
			return intValue == nbttagint.intValue;
		}
		else {
			return false;
		}
	}
}
