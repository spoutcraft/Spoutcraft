package net.minecraft.src;

import java.io.*;

public class NBTTagFloat extends NBTBase {
	public float floatValue;

	public NBTTagFloat(String s) {
		super(s);
	}

	public NBTTagFloat(String s, float f) {
		super(s);
		floatValue = f;
	}

	void writeTagContents(DataOutput dataoutput)
	throws IOException {
		dataoutput.writeFloat(floatValue);
	}

	void readTagContents(DataInput datainput)
	throws IOException {
		floatValue = datainput.readFloat();
	}

	public byte getType() {
		return 5;
	}

	public String toString() {
		return (new StringBuilder()).append("").append(floatValue).toString();
	}

	public NBTBase cloneTag() {
		return new NBTTagFloat(getKey(), floatValue);
	}

	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			NBTTagFloat nbttagfloat = (NBTTagFloat)obj;
			return floatValue == nbttagfloat.floatValue;
		}
		else {
			return false;
		}
	}
}
