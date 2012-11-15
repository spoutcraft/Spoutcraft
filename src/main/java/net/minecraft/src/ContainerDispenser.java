package net.minecraft.src;

public class ContainerDispenser extends Container {
	private TileEntityDispenser tileEntityDispenser;

	public ContainerDispenser(IInventory par1IInventory, TileEntityDispenser par2TileEntityDispenser) {
		this.tileEntityDispenser = par2TileEntityDispenser;
		int var3;
		int var4;

		for (var3 = 0; var3 < 3; ++var3) {
			for (var4 = 0; var4 < 3; ++var4) {
				this.addSlotToContainer(new Slot(par2TileEntityDispenser, var4 + var3 * 3, 62 + var4 * 18, 17 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 3; ++var3) {
			for (var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(par1IInventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			this.addSlotToContainer(new Slot(par1IInventory, var3, 8 + var3 * 18, 142));
		}
	}
	
	// Spout Start
	public IInventory getIInventory() {
		return tileEntityDispenser;
	}
	// Spout End

	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return this.tileEntityDispenser.isUseableByPlayer(par1EntityPlayer);
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	 */
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		ItemStack var3 = null;
		Slot var4 = (Slot)this.inventorySlots.get(par2);

		if (var4 != null && var4.getHasStack()) {
			ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			if (par2 < 9) {
				if (!this.mergeItemStack(var5, 9, 45, true)) {
					return null;
				}
			} else if (!this.mergeItemStack(var5, 0, 9, false)) {
				return null;
			}

			if (var5.stackSize == 0) {
				var4.putStack((ItemStack)null);
			} else {
				var4.onSlotChanged();
			}

			if (var5.stackSize == var3.stackSize) {
				return null;
			}

			var4.onPickupFromSlot(par1EntityPlayer, var5);
		}

		return var3;
	}
}
