package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

class ContainerCreative extends Container {

	/** the list of items in this container */
	public List itemList = new ArrayList();

	public ContainerCreative(EntityPlayer par1EntityPlayer) {
		InventoryPlayer var2 = par1EntityPlayer.inventory;
		int var3;

		for (var3 = 0; var3 < 5; ++var3) {
			for (int var4 = 0; var4 < 9; ++var4) {
				this.func_75146_a(new Slot(GuiContainerCreative.getInventory(), var3 * 9 + var4, 9 + var4 * 18, 18 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			this.func_75146_a(new Slot(var2, var3, 9 + var3 * 18, 112));
		}

		this.scrollTo(0.0F);
	}

	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return true;
	}

	/**
	 * Updates the gui slots ItemStack's based on scroll position.
	 */
	public void scrollTo(float par1) {
		int var2 = this.itemList.size() / 9 - 5 + 1;
		int var3 = (int)((double)(par1 * (float)var2) + 0.5D);

		if (var3 < 0) {
			var3 = 0;
		}

		for (int var4 = 0; var4 < 5; ++var4) {
			for (int var5 = 0; var5 < 9; ++var5) {
				int var6 = var5 + (var4 + var3) * 9;

				if (var6 >= 0 && var6 < this.itemList.size()) {
					GuiContainerCreative.getInventory().setInventorySlotContents(var5 + var4 * 9, (ItemStack)this.itemList.get(var6));
				} else {
					GuiContainerCreative.getInventory().setInventorySlotContents(var5 + var4 * 9, (ItemStack)null);
				}
			}
		}
	}

	public boolean func_75184_d() {
		return this.itemList.size() > 45;
	}

	// Spout start
	public IInventory getInventory() {
		return null;
	}
	// Spout end

	protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift clicking.
	 */
	public ItemStack transferStackInSlot(int par1) {
		if (par1 >= this.inventorySlots.size() - 9 && par1 < this.inventorySlots.size()) {
			Slot var2 = (Slot)this.inventorySlots.get(par1);

			if (var2 != null && var2.getHasStack()) {
				var2.putStack((ItemStack)null);
			}
		}

		return null;
	}
}
