package net.minecraft.src;

import java.io.*;

public class NBTTagByte extends NBTBase {
	public byte byteValue;

	public NBTTagByte(String s) {
		super(s);
	}

	public NBTTagByte(String s, byte byte0) {
		super(s);
		byteValue = byte0;
	}

	void writeTagContents(DataOutput dataoutput)
	throws IOException {
		dataoutput.writeByte(byteValue);
	}

	void readTagContents(DataInput datainput)
	throws IOException {
		byteValue = datainput.readByte();
	}

	public byte getType() {
		return 1;
	}

	public String toString() {
		return (new StringBuilder()).append("").append(byteValue).toString();
	}

	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			NBTTagByte nbttagbyte = (NBTTagByte)obj;
			return byteValue == nbttagbyte.byteValue;
		}
		else {
			return false;
		}
	}

	public NBTBase cloneTag() {
		return new NBTTagByte(getKey(), byteValue);
	}
}
