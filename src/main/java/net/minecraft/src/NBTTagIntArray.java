package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTTagIntArray extends NBTBase {

	/** The array of saved integers */
	public int[] intArray;

	public NBTTagIntArray(String par1Str) {
		super(par1Str);
	}

	public NBTTagIntArray(String par1Str, int[] par2ArrayOfInteger) {
		super(par1Str);
		this.intArray = par2ArrayOfInteger;
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension classes
	 */
	void write(DataOutput par1DataOutput) throws IOException {
		par1DataOutput.writeInt(this.intArray.length);

		for (int var2 = 0; var2 < this.intArray.length; ++var2) {
			par1DataOutput.writeInt(this.intArray[var2]);
		}
	}

	/**
	 * Read the actual data contents of the tag, implemented in NBT extension classes
	 */
	void load(DataInput par1DataInput, int par2) throws IOException {
		int var3 = par1DataInput.readInt();
		this.intArray = new int[var3];

		for (int var4 = 0; var4 < var3; ++var4) {
			this.intArray[var4] = par1DataInput.readInt();
		}
	}

	/**
	 * Gets the type byte for the tag.
	 */
	public byte getId() {
		return (byte)11;
	}

	public String toString() {
		return "[" + this.intArray.length + " bytes]";
	}

	/**
	 * Creates a clone of the tag.
	 */
	public NBTBase copy() {
		int[] var1 = new int[this.intArray.length];
		System.arraycopy(this.intArray, 0, var1, 0, this.intArray.length);
		return new NBTTagIntArray(this.getName(), var1);
	}

	public boolean equals(Object par1Obj) {
		if (!super.equals(par1Obj)) {
			return false;
		} else {
			NBTTagIntArray var2 = (NBTTagIntArray)par1Obj;
			return this.intArray == null && var2.intArray == null || this.intArray != null && Arrays.equals(this.intArray, var2.intArray);
		}
	}

	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(this.intArray);
	}
}
