package net.minecraft.src;

class SlotEnchantment extends Slot {
	final ContainerEnchantment field_40443_a;

	SlotEnchantment(ContainerEnchantment containerenchantment, IInventory iinventory, int i, int j, int k) {
		super(iinventory, i, j, k);
		field_40443_a = containerenchantment;
	}

	public boolean isItemValid(ItemStack itemstack) {
		return true;
	}
}
