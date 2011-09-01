// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

public class TileEntitySign extends TileEntity
{
	public String signText[] = {
			"", "", "", ""
	};
	public int lineBeingEdited;
	public int columnBeingEdited; //Spoutcraft
	private boolean field_25062_c;

	public TileEntitySign()
	{
		lineBeingEdited = -1;
		field_25062_c = true;
	}

	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setString("Text1", signText[0]);
		nbttagcompound.setString("Text2", signText[1]);
		nbttagcompound.setString("Text3", signText[2]);
		nbttagcompound.setString("Text4", signText[3]);
	}

	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		field_25062_c = false;
		super.readFromNBT(nbttagcompound);
		for(int i = 0; i < 4; i++)
		{
			signText[i] = nbttagcompound.getString((new StringBuilder()).append("Text").append(i + 1).toString());
			if(signText[i].length() > 15)
			{
				signText[i] = signText[i].substring(0, 15);
			}
		}

	}
}
