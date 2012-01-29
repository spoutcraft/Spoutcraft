package net.minecraft.src;

import java.io.*;

public class NBTTagString extends NBTBase {
	public String stringValue;

	public NBTTagString(String s) {
		super(s);
	}

	public NBTTagString(String s, String s1) {
		super(s);
		stringValue = s1;
		if (s1 == null) {
			throw new IllegalArgumentException("Empty string not allowed");
		}
		else {
			return;
		}
	}

	void writeTagContents(DataOutput dataoutput)
	throws IOException {
		dataoutput.writeUTF(stringValue);
	}

	void readTagContents(DataInput datainput)
	throws IOException {
		stringValue = datainput.readUTF();
	}

	public byte getType() {
		return 8;
	}

	public String toString() {
		return (new StringBuilder()).append("").append(stringValue).toString();
	}

	public NBTBase cloneTag() {
		return new NBTTagString(getKey(), stringValue);
	}

	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			NBTTagString nbttagstring = (NBTTagString)obj;
			return stringValue == null && nbttagstring.stringValue == null || stringValue != null && stringValue.equals(nbttagstring.stringValue);
		}
		else {
			return false;
		}
	}
}
