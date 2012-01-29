package net.minecraft.src;

public class TileEntityRecordPlayer extends TileEntity {
	public int record;

	public TileEntityRecordPlayer() {
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		record = nbttagcompound.getInteger("Record");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		if (record > 0) {
			nbttagcompound.setInteger("Record", record);
		}
	}
}
