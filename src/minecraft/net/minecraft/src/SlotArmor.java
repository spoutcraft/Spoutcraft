package net.minecraft.src;

class SlotArmor extends Slot {
	final int armorType;
	final ContainerPlayer inventory;

	SlotArmor(ContainerPlayer containerplayer, IInventory iinventory, int i, int j, int k, int l) {
		super(iinventory, i, j, k);
		inventory = containerplayer;
		armorType = l;
	}

	public int getSlotStackLimit() {
		return 1;
	}

	public boolean isItemValid(ItemStack itemstack) {
		if (itemstack.getItem() instanceof ItemArmor) {
			return ((ItemArmor)itemstack.getItem()).armorType == armorType;
		}
		if (itemstack.getItem().shiftedIndex == Block.pumpkin.blockID) {
			return armorType == 0;
		}
		else {
			return false;
		}
	}
}
