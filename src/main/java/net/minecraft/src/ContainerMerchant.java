package net.minecraft.src;

public class ContainerMerchant extends Container {
	private IMerchant field_75178_e;
	private InventoryMerchant merchantInventory;
	private final World field_75177_g;

	public ContainerMerchant(InventoryPlayer par1InventoryPlayer, IMerchant par2IMerchant, World par3World) {
		this.field_75178_e = par2IMerchant;
		this.field_75177_g = par3World;
		this.merchantInventory = new InventoryMerchant(par1InventoryPlayer.player, par2IMerchant);
		this.addSlotToContainer(new Slot(this.merchantInventory, 0, 36, 53));
		this.addSlotToContainer(new Slot(this.merchantInventory, 1, 62, 53));
		this.addSlotToContainer(new SlotMerchantResult(par1InventoryPlayer.player, par2IMerchant, this.merchantInventory, 2, 120, 53));
		int var4;

		for (var4 = 0; var4 < 3; ++var4) {
			for (int var5 = 0; var5 < 9; ++var5) {
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var5 + var4 * 9 + 9, 8 + var5 * 18, 84 + var4 * 18));
			}
		}

		for (var4 = 0; var4 < 9; ++var4) {
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var4, 8 + var4 * 18, 142));
		}
	}

	public InventoryMerchant getMerchantInventory() {
		return this.merchantInventory;
	}

	public void addCraftingToCrafters(ICrafting par1ICrafting) {
		super.addCraftingToCrafters(par1ICrafting);
	}

	// Spout Start
	public IInventory getIInventory() {
		return merchantInventory;
	}
	// Spout End
	
	/**
	 * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
	 */
	public void updateCraftingResults() {
		super.updateCraftingResults();
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	public void onCraftMatrixChanged(IInventory par1IInventory) {
		this.merchantInventory.resetRecipeAndSlots();
		super.onCraftMatrixChanged(par1IInventory);
	}

	public void setCurrentRecipeIndex(int par1) {
		this.merchantInventory.setCurrentRecipeIndex(par1);
	}

	public void updateProgressBar(int par1, int par2) {}

	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return this.field_75178_e.getCustomer() == par1EntityPlayer;
	}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift clicking.
	 */
	public ItemStack transferStackInSlot(int par1) {
		ItemStack var2 = null;
		Slot var3 = (Slot)this.inventorySlots.get(par1);

		if (var3 != null && var3.getHasStack()) {
			ItemStack var4 = var3.getStack();
			var2 = var4.copy();

			if (par1 == 2) {
				if (!this.mergeItemStack(var4, 3, 39, true)) {
					return null;
				}

				var3.onSlotChange(var4, var2);
			} else if (par1 != 0 && par1 != 1) {
				if (par1 >= 3 && par1 < 30) {
					if (!this.mergeItemStack(var4, 30, 39, false)) {
						return null;
					}
				} else if (par1 >= 30 && par1 < 39 && !this.mergeItemStack(var4, 3, 30, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(var4, 3, 39, false)) {
				return null;
			}

			if (var4.stackSize == 0) {
				var3.putStack((ItemStack)null);
			} else {
				var3.onSlotChanged();
			}

			if (var4.stackSize == var2.stackSize) {
				return null;
			}

			var3.onPickupFromSlot(var4);
		}

		return var2;
	}

	/**
	 * Callback for when the crafting gui is closed.
	 */
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
		super.onCraftGuiClosed(par1EntityPlayer);
		this.field_75178_e.setCustomer((EntityPlayer)null);
		super.onCraftGuiClosed(par1EntityPlayer);

		if (!this.field_75177_g.isRemote) {
			ItemStack var2 = this.merchantInventory.getStackInSlotOnClosing(0);

			if (var2 != null) {
				par1EntityPlayer.dropPlayerItem(var2);
			}

			var2 = this.merchantInventory.getStackInSlotOnClosing(1);

			if (var2 != null) {
				par1EntityPlayer.dropPlayerItem(var2);
			}
		}
	}
}
