package net.minecraft.src;

import java.io.*;
import java.util.*;

public class NBTTagList extends NBTBase {
	public List tagList; //Spout: private to public
	private byte tagType;

	public NBTTagList() {
		super("");
		tagList = new ArrayList();
	}

	public NBTTagList(String par1Str) {
		super(par1Str);
		tagList = new ArrayList();
	}

	void write(DataOutput par1DataOutput) throws IOException {
		if (tagList.size() > 0) {
			tagType = ((NBTBase)tagList.get(0)).getId();
		} else {
			tagType = 1;
		}

		par1DataOutput.writeByte(tagType);
		par1DataOutput.writeInt(tagList.size());

		for (int i = 0; i < tagList.size(); i++) {
			((NBTBase)tagList.get(i)).write(par1DataOutput);
		}
	}

	void load(DataInput par1DataInput) throws IOException {
		tagType = par1DataInput.readByte();
		int i = par1DataInput.readInt();
		tagList = new ArrayList();

		for (int j = 0; j < i; j++) {
			NBTBase nbtbase = NBTBase.newTag(tagType, null);
			nbtbase.load(par1DataInput);
			tagList.add(nbtbase);
		}
	}

	public byte getId() {
		return 9;
	}

	public String toString() {
		return (new StringBuilder()).append("").append(tagList.size()).append(" entries of type ").append(NBTBase.getTagName(tagType)).toString();
	}

	public void appendTag(NBTBase par1NBTBase) {
		tagType = par1NBTBase.getId();
		tagList.add(par1NBTBase);
	}

	public NBTBase tagAt(int par1) {
		return (NBTBase)tagList.get(par1);
	}

	public int tagCount() {
		return tagList.size();
	}

	public NBTBase copy() {
		NBTTagList nbttaglist = new NBTTagList(getName());
		nbttaglist.tagType = tagType;
		NBTBase nbtbase1;

		for (Iterator iterator = tagList.iterator(); iterator.hasNext(); nbttaglist.tagList.add(nbtbase1)) {
			NBTBase nbtbase = (NBTBase)iterator.next();
			nbtbase1 = nbtbase.copy();
		}

		return nbttaglist;
	}

	public boolean equals(Object par1Obj) {
		if (super.equals(par1Obj)) {
			NBTTagList nbttaglist = (NBTTagList)par1Obj;

			if (tagType == nbttaglist.tagType) {
				return tagList.equals(nbttaglist.tagList);
			}
		}

		return false;
	}

	public int hashCode() {
		return super.hashCode() ^ tagList.hashCode();
	}
}
