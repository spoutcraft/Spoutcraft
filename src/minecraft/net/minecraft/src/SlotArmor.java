package net.minecraft.src;

import org.spoutcraft.spoutcraftapi.material.MaterialData;

class SlotArmor extends Slot {

	/**
	 * The armor type that can be placed on that slot, it uses the same values of armorType field on ItemArmor.
	 */
	final int armorType;

	/**
	 * The parent class of this clot, ContainerPlayer, SlotArmor is a Anon inner class.
	 */
	final ContainerPlayer parent;

	SlotArmor(ContainerPlayer par1ContainerPlayer, IInventory par2IInventory, int par3, int par4, int par5, int par6) {
		super(par2IInventory, par3, par4, par5);
		this.parent = par1ContainerPlayer;
		this.armorType = par6;
	}

	/**
	 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case of
	 * armor slots)
	 */
	public int getSlotStackLimit() {
		return 1;
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	public boolean isItemValid(ItemStack par1ItemStack) {
		//Spout start
		if (par1ItemStack.itemID == 318) {
			org.spoutcraft.spoutcraftapi.material.CustomItem item = MaterialData.getCustomItem(par1ItemStack.getItemDamage());
			if( item != null )
				return (item instanceof org.spoutcraft.spoutcraftapi.material.item.GenericCustomArmor);
		}
		// Spout end
		return par1ItemStack.getItem() instanceof ItemArmor ? ((ItemArmor)par1ItemStack.getItem()).armorType == this.armorType : (par1ItemStack.getItem().shiftedIndex == Block.pumpkin.blockID ? this.armorType == 0 : false);
	}

	/**
	 * Returns the icon index on items.png that is used as background image of the slot.
	 */
	public int getBackgroundIconIndex() {
		return 15 + this.armorType * 16;
	}
}
