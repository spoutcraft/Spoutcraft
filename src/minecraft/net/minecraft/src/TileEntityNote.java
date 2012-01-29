package net.minecraft.src;

public class TileEntityNote extends TileEntity {
	public byte note;
	public boolean previousRedstoneState;

	public TileEntityNote() {
		note = 0;
		previousRedstoneState = false;
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setByte("note", note);
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		note = nbttagcompound.getByte("note");
		if (note < 0) {
			note = 0;
		}
		if (note > 24) {
			note = 24;
		}
	}

	public void changePitch() {
		note = (byte)((note + 1) % 25);
		onInventoryChanged();
	}

	public void triggerNote(World world, int i, int j, int k) {
		if (world.getBlockMaterial(i, j + 1, k) != Material.air) {
			return;
		}
		Material material = world.getBlockMaterial(i, j - 1, k);
		byte byte0 = 0;
		if (material == Material.rock) {
			byte0 = 1;
		}
		if (material == Material.sand) {
			byte0 = 2;
		}
		if (material == Material.glass) {
			byte0 = 3;
		}
		if (material == Material.wood) {
			byte0 = 4;
		}
		world.playNoteAt(i, j, k, byte0, note);
	}
}
