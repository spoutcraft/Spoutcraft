package net.minecraft.src;

public abstract class WorldSavedData {
	public final String mapName;
	private boolean dirty;

	public WorldSavedData(String s) {
		mapName = s;
	}

	public abstract void readFromNBT(NBTTagCompound nbttagcompound);

	public abstract void writeToNBT(NBTTagCompound nbttagcompound);

	public void markDirty() {
		setDirty(true);
	}

	public void setDirty(boolean flag) {
		dirty = flag;
	}

	public boolean isDirty() {
		return dirty;
	}
}
