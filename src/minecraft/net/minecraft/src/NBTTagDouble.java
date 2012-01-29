package net.minecraft.src;

import java.io.*;

public class NBTTagDouble extends NBTBase {
	public double doubleValue;

	public NBTTagDouble(String s) {
		super(s);
	}

	public NBTTagDouble(String s, double d) {
		super(s);
		doubleValue = d;
	}

	void writeTagContents(DataOutput dataoutput)
	throws IOException {
		dataoutput.writeDouble(doubleValue);
	}

	void readTagContents(DataInput datainput)
	throws IOException {
		doubleValue = datainput.readDouble();
	}

	public byte getType() {
		return 6;
	}

	public String toString() {
		return (new StringBuilder()).append("").append(doubleValue).toString();
	}

	public NBTBase cloneTag() {
		return new NBTTagDouble(getKey(), doubleValue);
	}

	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			NBTTagDouble nbttagdouble = (NBTTagDouble)obj;
			return doubleValue == nbttagdouble.doubleValue;
		}
		else {
			return false;
		}
	}
}
