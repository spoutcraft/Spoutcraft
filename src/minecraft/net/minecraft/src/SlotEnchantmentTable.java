package net.minecraft.src;

class SlotEnchantmentTable extends InventoryBasic {
	final ContainerEnchantment field_40070_a;

	SlotEnchantmentTable(ContainerEnchantment containerenchantment, String s, int i) {
		super(s, i);
		field_40070_a = containerenchantment;
	}

	public int getInventoryStackLimit() {
		return 1;
	}

	public void onInventoryChanged() {
		super.onInventoryChanged();
		field_40070_a.onCraftMatrixChanged(this);
	}
}
