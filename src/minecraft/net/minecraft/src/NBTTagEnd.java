package net.minecraft.src;

import java.io.*;

public class NBTTagEnd extends NBTBase {
	public NBTTagEnd() {
		super(null);
	}

	void readTagContents(DataInput datainput)
	throws IOException {
	}

	void writeTagContents(DataOutput dataoutput)
	throws IOException {
	}

	public byte getType() {
		return 0;
	}

	public String toString() {
		return "END";
	}

	public NBTBase cloneTag() {
		return new NBTTagEnd();
	}

	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
