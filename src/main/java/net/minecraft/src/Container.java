package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Container {

	/** the list of all items(stacks) for the corresponding slot */
	public List inventoryItemStacks = new ArrayList();

	/** the list of all slots in the inventory */
	public List inventorySlots = new ArrayList();
	public int windowId = 0;
	private short transactionID = 0;

	/**
	 * list of all people that need to be notified when this craftinventory changes
	 */
	protected List crafters = new ArrayList();
	private Set playerList = new HashSet();
	// Spout Start
	public IInventory getIInventory() {
		return null;
	}
	
	public boolean isSortableInventory() {
		return false;
	}
	// Spout End

	/**
	 * the slot is assumed empty
	 */
	protected Slot addSlotToContainer(Slot par1Slot) {
		par1Slot.slotNumber = this.inventorySlots.size();
		this.inventorySlots.add(par1Slot);
		this.inventoryItemStacks.add((Object)null);
		return par1Slot;
	}

	public void addCraftingToCrafters(ICrafting par1ICrafting) {
		if (this.crafters.contains(par1ICrafting)) {
			throw new IllegalArgumentException("Listener already listening");
		} else {
			this.crafters.add(par1ICrafting);
			par1ICrafting.sendContainerAndContentsToPlayer(this, this.getInventory());
			this.updateCraftingResults();
		}
	}

	/**
	 * Remove this crafting listener from the listener list.
	 */
	public void removeCraftingFromCrafters(ICrafting par1ICrafting) {
		this.crafters.remove(par1ICrafting);
	}

	/**
	 * returns a list if itemStacks, for each slot.
	 */
	public List getInventory() {
		ArrayList var1 = new ArrayList();

		for (int var2 = 0; var2 < this.inventorySlots.size(); ++var2) {
			var1.add(((Slot)this.inventorySlots.get(var2)).getStack());
		}

		return var1;
	}

	/**
	 * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
	 */
	public void updateCraftingResults() {
		for (int var1 = 0; var1 < this.inventorySlots.size(); ++var1) {
			ItemStack var2 = ((Slot)this.inventorySlots.get(var1)).getStack();
			ItemStack var3 = (ItemStack)this.inventoryItemStacks.get(var1);

			if (!ItemStack.areItemStacksEqual(var3, var2)) {
				var3 = var2 == null ? null : var2.copy();
				this.inventoryItemStacks.set(var1, var3);

				for (int var4 = 0; var4 < this.crafters.size(); ++var4) {
					((ICrafting)this.crafters.get(var4)).sendSlotContents(this, var1, var3);
				}
			}
		}
	}

	/**
	 * enchants the item on the table using the specified slot; also deducts XP from player
	 */
	public boolean enchantItem(EntityPlayer par1EntityPlayer, int par2) {
		return false;
	}

	public Slot getSlotFromInventory(IInventory par1IInventory, int par2) {
		for (int var3 = 0; var3 < this.inventorySlots.size(); ++var3) {
			Slot var4 = (Slot)this.inventorySlots.get(var3);

			if (var4.isSlotInInventory(par1IInventory, par2)) {
				return var4;
			}
		}

		return null;
	}

	public Slot getSlot(int par1) {
		return (Slot)this.inventorySlots.get(par1);
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	 */
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		Slot var3 = (Slot)this.inventorySlots.get(par2);
		return var3 != null ? var3.getStack() : null;
	}

	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer) {
		ItemStack var5 = null;
		InventoryPlayer var6 = par4EntityPlayer.inventory;
		Slot var7;
		ItemStack var8;
		int var10;
		ItemStack var11;

		if ((par3 == 0 || par3 == 1) && (par2 == 0 || par2 == 1)) {
			if (par1 == -999) {
				if (var6.getItemStack() != null && par1 == -999) {
					if (par2 == 0) {
						par4EntityPlayer.dropPlayerItem(var6.getItemStack());
						var6.setItemStack((ItemStack)null);
					}

					if (par2 == 1) {
						par4EntityPlayer.dropPlayerItem(var6.getItemStack().splitStack(1));

						if (var6.getItemStack().stackSize == 0) {
							var6.setItemStack((ItemStack)null);
						}
					}
				}
			} else if (par3 == 1) {
				var7 = (Slot)this.inventorySlots.get(par1);

				if (var7 != null && var7.canTakeStack(par4EntityPlayer)) {
					var8 = this.transferStackInSlot(par4EntityPlayer, par1);

					if (var8 != null) {
						int var12 = var8.itemID;
						var5 = var8.copy();

						if (var7 != null && var7.getStack() != null && var7.getStack().itemID == var12) {
							this.retrySlotClick(par1, par2, true, par4EntityPlayer);
						}
					}
				}
			} else {
				if (par1 < 0) {
					return null;
				}

				var7 = (Slot)this.inventorySlots.get(par1);

				if (var7 != null) {
					var8 = var7.getStack();
					ItemStack var13 = var6.getItemStack();

					if (var8 != null) {
						var5 = var8.copy();
					}

					if (var8 == null) {
						if (var13 != null && var7.isItemValid(var13)) {
							var10 = par2 == 0 ? var13.stackSize : 1;

							if (var10 > var7.getSlotStackLimit()) {
								var10 = var7.getSlotStackLimit();
							}

							var7.putStack(var13.splitStack(var10));

							if (var13.stackSize == 0) {
								var6.setItemStack((ItemStack)null);
							}
						}
					} else if (var7.canTakeStack(par4EntityPlayer)) {
						if (var13 == null) {
							var10 = par2 == 0 ? var8.stackSize : (var8.stackSize + 1) / 2;
							var11 = var7.decrStackSize(var10);
							var6.setItemStack(var11);

							if (var8.stackSize == 0) {
								var7.putStack((ItemStack)null);
							}

							var7.onPickupFromSlot(par4EntityPlayer, var6.getItemStack());
						} else if (var7.isItemValid(var13)) {
							if (var8.itemID == var13.itemID && (!var8.getHasSubtypes() || var8.getItemDamage() == var13.getItemDamage()) && ItemStack.areItemStackTagsEqual(var8, var13)) {
								var10 = par2 == 0 ? var13.stackSize : 1;

								if (var10 > var7.getSlotStackLimit() - var8.stackSize) {
									var10 = var7.getSlotStackLimit() - var8.stackSize;
								}

								if (var10 > var13.getMaxStackSize() - var8.stackSize) {
									var10 = var13.getMaxStackSize() - var8.stackSize;
								}

								var13.splitStack(var10);

								if (var13.stackSize == 0) {
									var6.setItemStack((ItemStack)null);
								}

								var8.stackSize += var10;
							} else if (var13.stackSize <= var7.getSlotStackLimit()) {
								var7.putStack(var13);
								var6.setItemStack(var8);
							}
						} else if (var8.itemID == var13.itemID && var13.getMaxStackSize() > 1 && (!var8.getHasSubtypes() || var8.getItemDamage() == var13.getItemDamage()) && ItemStack.areItemStackTagsEqual(var8, var13)) {
							var10 = var8.stackSize;

							if (var10 > 0 && var10 + var13.stackSize <= var13.getMaxStackSize()) {
								var13.stackSize += var10;
								var8 = var7.decrStackSize(var10);

								if (var8.stackSize == 0) {
									var7.putStack((ItemStack)null);
								}

								var7.onPickupFromSlot(par4EntityPlayer, var6.getItemStack());
							}
						}
					}

					var7.onSlotChanged();
				}
			}
		} else if (par3 == 2 && par2 >= 0 && par2 < 9) {
			var7 = (Slot)this.inventorySlots.get(par1);

			if (var7.canTakeStack(par4EntityPlayer)) {
				var8 = var6.getStackInSlot(par2);
				boolean var9 = var8 == null || var7.inventory == var6 && var7.isItemValid(var8);
				var10 = -1;

				if (!var9) {
					var10 = var6.getFirstEmptyStack();
					var9 |= var10 > -1;
				}

				if (var7.getHasStack() && var9) {
					var11 = var7.getStack();
					var6.setInventorySlotContents(par2, var11);

					if ((var7.inventory != var6 || !var7.isItemValid(var8)) && var8 != null) {
						if (var10 > -1) {
							var6.addItemStackToInventory(var8);
							var7.putStack((ItemStack)null);
							var7.onPickupFromSlot(par4EntityPlayer, var11);
						}
					} else {
						var7.putStack(var8);
						var7.onPickupFromSlot(par4EntityPlayer, var11);
					}
				} else if (!var7.getHasStack() && var8 != null && var7.isItemValid(var8)) {
					var6.setInventorySlotContents(par2, (ItemStack)null);
					var7.putStack(var8);
				}
			}
		} else if (par3 == 3 && par4EntityPlayer.capabilities.isCreativeMode && var6.getItemStack() == null && par1 >= 0) {
			var7 = (Slot)this.inventorySlots.get(par1);

			if (var7 != null && var7.getHasStack()) {
				var8 = var7.getStack().copy();
				var8.stackSize = var8.getMaxStackSize();
				var6.setItemStack(var8);
			}
		}

		return var5;
	}

	protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {
		this.slotClick(par1, par2, 1, par4EntityPlayer);
	}

	/**
	 * Callback for when the crafting gui is closed.
	 */
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
		InventoryPlayer var2 = par1EntityPlayer.inventory;

		if (var2.getItemStack() != null) {
			par1EntityPlayer.dropPlayerItem(var2.getItemStack());
			var2.setItemStack((ItemStack)null);
		}
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	public void onCraftMatrixChanged(IInventory par1IInventory) {
		this.updateCraftingResults();
	}

	/**
	 * args: slotID, itemStack to put in slot
	 */
	public void putStackInSlot(int par1, ItemStack par2ItemStack) {
		this.getSlot(par1).putStack(par2ItemStack);
	}

	/**
	 * places itemstacks in first x slots, x being aitemstack.lenght
	 */
	public void putStacksInSlots(ItemStack[] par1ArrayOfItemStack) {
		for (int var2 = 0; var2 < par1ArrayOfItemStack.length; ++var2) {
			this.getSlot(var2).putStack(par1ArrayOfItemStack[var2]);
		}
	}

	public void updateProgressBar(int par1, int par2) {}

	/**
	 * Gets a unique transaction ID. Parameter is unused.
	 */
	public short getNextTransactionID(InventoryPlayer par1InventoryPlayer) {
		++this.transactionID;
		return this.transactionID;
	}

	/**
	 * NotUsing because adding a player twice is an error
	 */
	public boolean isPlayerNotUsingContainer(EntityPlayer par1EntityPlayer) {
		return !this.playerList.contains(par1EntityPlayer);
	}

	/**
	 * adds or removes the player from the container based on par2
	 */
	public void setPlayerIsPresent(EntityPlayer par1EntityPlayer, boolean par2) {
		if (par2) {
			this.playerList.remove(par1EntityPlayer);
		} else {
			this.playerList.add(par1EntityPlayer);
		}
	}

	public abstract boolean canInteractWith(EntityPlayer var1);

	/**
	 * merges provided ItemStack with the first avaliable one in the container/player inventory
	 */
	protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4) {
		boolean var5 = false;
		int var6 = par2;

		if (par4) {
			var6 = par3 - 1;
		}

		Slot var7;
		ItemStack var8;

		if (par1ItemStack.isStackable()) {
			while (par1ItemStack.stackSize > 0 && (!par4 && var6 < par3 || par4 && var6 >= par2)) {
				var7 = (Slot)this.inventorySlots.get(var6);
				var8 = var7.getStack();

				if (var8 != null && var8.itemID == par1ItemStack.itemID && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == var8.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, var8)) {
					int var9 = var8.stackSize + par1ItemStack.stackSize;

					if (var9 <= par1ItemStack.getMaxStackSize()) {
						par1ItemStack.stackSize = 0;
						var8.stackSize = var9;
						var7.onSlotChanged();
						var5 = true;
					} else if (var8.stackSize < par1ItemStack.getMaxStackSize()) {
						par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - var8.stackSize;
						var8.stackSize = par1ItemStack.getMaxStackSize();
						var7.onSlotChanged();
						var5 = true;
					}
				}

				if (par4) {
					--var6;
				} else {
					++var6;
				}
			}
		}

		if (par1ItemStack.stackSize > 0) {
			if (par4) {
				var6 = par3 - 1;
			} else {
				var6 = par2;
			}

			while (!par4 && var6 < par3 || par4 && var6 >= par2) {
				var7 = (Slot)this.inventorySlots.get(var6);
				var8 = var7.getStack();

				if (var8 == null) {
					var7.putStack(par1ItemStack.copy());
					var7.onSlotChanged();
					par1ItemStack.stackSize = 0;
					var5 = true;
					break;
				}

				if (par4) {
					--var6;
				} else {
					++var6;
				}
			}
		}

		return var5;
	}
}
