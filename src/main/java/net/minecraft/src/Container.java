package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class Container {

	/** the list of all items(stacks) for the corresponding slot */
	public List inventoryItemStacks = new ArrayList();

	/** the list of all slots in the inventory */
	public List inventorySlots = new ArrayList();
	public int windowId;
	private short transactionID;
	private int field_94535_f = -1;
	private int field_94536_g;
	private final Set field_94537_h = new HashSet();

	/**
	 * list of all people that need to be notified when this craftinventory changes
	 */
	protected List crafters = new ArrayList();
	private Set playerList = new HashSet();

	/**
	 * the slot is assumed empty
	 */

	// Spout Start - Inventory sorting
	public IInventory getIInventory() {
		return null;
	}

	public boolean isSortableInventory() {
		return false;
	}
	// Spout End

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
			this.detectAndSendChanges();
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
	 * Looks for changes made in the container, sends them to every listener.
	 */
	public void detectAndSendChanges() {
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
		int var9;
		ItemStack var17;

		if (par3 == 5) {
			int var7 = this.field_94536_g;
			this.field_94536_g = func_94532_c(par2);

			if ((var7 != 1 || this.field_94536_g != 2) && var7 != this.field_94536_g) {
				this.func_94533_d();
			} else if (var6.getItemStack() == null) {
				this.func_94533_d();
			} else if (this.field_94536_g == 0) {
				this.field_94535_f = func_94529_b(par2);

				if (func_94528_d(this.field_94535_f)) {
					this.field_94536_g = 1;
					this.field_94537_h.clear();
				} else {
					this.func_94533_d();
				}
			} else if (this.field_94536_g == 1) {
				Slot var8 = (Slot)this.inventorySlots.get(par1);

				if (var8 != null && func_94527_a(var8, var6.getItemStack(), true) && var8.isItemValid(var6.getItemStack()) && var6.getItemStack().stackSize > this.field_94537_h.size() && this.canDragIntoSlot(var8)) {
					this.field_94537_h.add(var8);
				}
			} else if (this.field_94536_g == 2) {
				if (!this.field_94537_h.isEmpty()) {
					var17 = var6.getItemStack().copy();
					var9 = var6.getItemStack().stackSize;
					Iterator var10 = this.field_94537_h.iterator();

					while (var10.hasNext()) {
						Slot var11 = (Slot)var10.next();

						if (var11 != null && func_94527_a(var11, var6.getItemStack(), true) && var11.isItemValid(var6.getItemStack()) && var6.getItemStack().stackSize >= this.field_94537_h.size() && this.canDragIntoSlot(var11)) {
							ItemStack var12 = var17.copy();
							int var13 = var11.getHasStack() ? var11.getStack().stackSize : 0;
							func_94525_a(this.field_94537_h, this.field_94535_f, var12, var13);

							if (var12.stackSize > var12.getMaxStackSize()) {
								var12.stackSize = var12.getMaxStackSize();
							}

							if (var12.stackSize > var11.getSlotStackLimit()) {
								var12.stackSize = var11.getSlotStackLimit();
							}

							var9 -= var12.stackSize - var13;
							var11.putStack(var12);
						}
					}

					var17.stackSize = var9;

					if (var17.stackSize <= 0) {
						var17 = null;
					}

					var6.setItemStack(var17);
				}

				this.func_94533_d();
			} else {
				this.func_94533_d();
			}
		} else if (this.field_94536_g != 0) {
			this.func_94533_d();
		} else {
			Slot var16;
			int var21;
			ItemStack var23;

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
					if (par1 < 0) {
						return null;
					}

					var16 = (Slot)this.inventorySlots.get(par1);

					if (var16 != null && var16.canTakeStack(par4EntityPlayer)) {
						var17 = this.transferStackInSlot(par4EntityPlayer, par1);

						if (var17 != null) {
							var9 = var17.itemID;
							var5 = var17.copy();

							if (var16 != null && var16.getStack() != null && var16.getStack().itemID == var9) {
								this.retrySlotClick(par1, par2, true, par4EntityPlayer);
							}
						}
					}
				} else {
					if (par1 < 0) {
						return null;
					}

					var16 = (Slot)this.inventorySlots.get(par1);

					if (var16 != null) {
						var17 = var16.getStack();
						ItemStack var19 = var6.getItemStack();

						if (var17 != null) {
							var5 = var17.copy();
						}

						if (var17 == null) {
							if (var19 != null && var16.isItemValid(var19)) {
								var21 = par2 == 0 ? var19.stackSize : 1;

								if (var21 > var16.getSlotStackLimit()) {
									var21 = var16.getSlotStackLimit();
								}

								if (var19.stackSize >= var21) {
									var16.putStack(var19.splitStack(var21));
								}

								if (var19.stackSize == 0) {
									var6.setItemStack((ItemStack)null);
								}
							}
						} else if (var16.canTakeStack(par4EntityPlayer)) {
							if (var19 == null) {
								var21 = par2 == 0 ? var17.stackSize : (var17.stackSize + 1) / 2;
								var23 = var16.decrStackSize(var21);
								var6.setItemStack(var23);

								if (var17.stackSize == 0) {
									var16.putStack((ItemStack)null);
								}

								var16.onPickupFromSlot(par4EntityPlayer, var6.getItemStack());
							} else if (var16.isItemValid(var19)) {
								if (var17.itemID == var19.itemID && var17.getItemDamage() == var19.getItemDamage() && ItemStack.areItemStackTagsEqual(var17, var19)) {
									var21 = par2 == 0 ? var19.stackSize : 1;

									if (var21 > var16.getSlotStackLimit() - var17.stackSize) {
										var21 = var16.getSlotStackLimit() - var17.stackSize;
									}

									if (var21 > var19.getMaxStackSize() - var17.stackSize) {
										var21 = var19.getMaxStackSize() - var17.stackSize;
									}

									var19.splitStack(var21);

									if (var19.stackSize == 0) {
										var6.setItemStack((ItemStack)null);
									}

									var17.stackSize += var21;
								} else if (var19.stackSize <= var16.getSlotStackLimit()) {
									var16.putStack(var19);
									var6.setItemStack(var17);
								}
							} else if (var17.itemID == var19.itemID && var19.getMaxStackSize() > 1 && (!var17.getHasSubtypes() || var17.getItemDamage() == var19.getItemDamage()) && ItemStack.areItemStackTagsEqual(var17, var19)) {
								var21 = var17.stackSize;

								if (var21 > 0 && var21 + var19.stackSize <= var19.getMaxStackSize()) {
									var19.stackSize += var21;
									var17 = var16.decrStackSize(var21);

									if (var17.stackSize == 0) {
										var16.putStack((ItemStack)null);
									}

									var16.onPickupFromSlot(par4EntityPlayer, var6.getItemStack());
								}
							}
						}

						var16.onSlotChanged();
					}
				}
			} else if (par3 == 2 && par2 >= 0 && par2 < 9) {
				var16 = (Slot)this.inventorySlots.get(par1);

				if (var16.canTakeStack(par4EntityPlayer)) {
					var17 = var6.getStackInSlot(par2);
					boolean var18 = var17 == null || var16.inventory == var6 && var16.isItemValid(var17);
					var21 = -1;

					if (!var18) {
						var21 = var6.getFirstEmptyStack();
						var18 |= var21 > -1;
					}

					if (var16.getHasStack() && var18) {
						var23 = var16.getStack();
						var6.setInventorySlotContents(par2, var23.copy());

						if ((var16.inventory != var6 || !var16.isItemValid(var17)) && var17 != null) {
							if (var21 > -1) {
								var6.addItemStackToInventory(var17);
								var16.decrStackSize(var23.stackSize);
								var16.putStack((ItemStack)null);
								var16.onPickupFromSlot(par4EntityPlayer, var23);
							}
						} else {
							var16.decrStackSize(var23.stackSize);
							var16.putStack(var17);
							var16.onPickupFromSlot(par4EntityPlayer, var23);
						}
					} else if (!var16.getHasStack() && var17 != null && var16.isItemValid(var17)) {
						var6.setInventorySlotContents(par2, (ItemStack)null);
						var16.putStack(var17);
					}
				}
			} else if (par3 == 3 && par4EntityPlayer.capabilities.isCreativeMode && var6.getItemStack() == null && par1 >= 0) {
				var16 = (Slot)this.inventorySlots.get(par1);

				if (var16 != null && var16.getHasStack()) {
					var17 = var16.getStack().copy();
					var17.stackSize = var17.getMaxStackSize();
					var6.setItemStack(var17);
				}
			} else if (par3 == 4 && var6.getItemStack() == null && par1 >= 0) {
				var16 = (Slot)this.inventorySlots.get(par1);

				if (var16 != null && var16.getHasStack() && var16.canTakeStack(par4EntityPlayer)) {
					var17 = var16.decrStackSize(par2 == 0 ? 1 : var16.getStack().stackSize);
					var16.onPickupFromSlot(par4EntityPlayer, var17);
					par4EntityPlayer.dropPlayerItem(var17);
				}
			} else if (par3 == 6 && par1 >= 0) {
				var16 = (Slot)this.inventorySlots.get(par1);
				var17 = var6.getItemStack();

				if (var17 != null && (var16 == null || !var16.getHasStack() || !var16.canTakeStack(par4EntityPlayer))) {
					var9 = par2 == 0 ? 0 : this.inventorySlots.size() - 1;
					var21 = par2 == 0 ? 1 : -1;

					for (int var20 = 0; var20 < 2; ++var20) {
						for (int var22 = var9; var22 >= 0 && var22 < this.inventorySlots.size() && var17.stackSize < var17.getMaxStackSize(); var22 += var21) {
							Slot var24 = (Slot)this.inventorySlots.get(var22);

							if (var24.getHasStack() && func_94527_a(var24, var17, true) && var24.canTakeStack(par4EntityPlayer) && this.func_94530_a(var17, var24) && (var20 != 0 || var24.getStack().stackSize != var24.getStack().getMaxStackSize())) {
								int var14 = Math.min(var17.getMaxStackSize() - var17.stackSize, var24.getStack().stackSize);
								ItemStack var15 = var24.decrStackSize(var14);
								var17.stackSize += var14;

								if (var15.stackSize <= 0) {
									var24.putStack((ItemStack)null);
								}

								var24.onPickupFromSlot(par4EntityPlayer, var15);
							}
						}
					}
				}

				this.detectAndSendChanges();
			}
		}

		return var5;
	}

	public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot) {
		return true;
	}

	protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {
		this.slotClick(par1, par2, 1, par4EntityPlayer);
	}

	/**
	 * Called when the container is closed.
	 */
	 public void onContainerClosed(EntityPlayer par1EntityPlayer) {
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
		 this.detectAndSendChanges();
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

	 public static int func_94529_b(int par0) {
		 return par0 >> 2 & 3;
	 }

	 public static int func_94532_c(int par0) {
		 return par0 & 3;
	 }

	 public static int func_94534_d(int par0, int par1) {
		 return par0 & 3 | (par1 & 3) << 2;
	 }

	 public static boolean func_94528_d(int par0) {
		 return par0 == 0 || par0 == 1;
	 }

	 protected void func_94533_d() {
		 this.field_94536_g = 0;
		 this.field_94537_h.clear();
	 }

	 public static boolean func_94527_a(Slot par0Slot, ItemStack par1ItemStack, boolean par2) {
		 boolean var3 = par0Slot == null || !par0Slot.getHasStack();

		 if (par0Slot != null && par0Slot.getHasStack() && par1ItemStack != null && par1ItemStack.isItemEqual(par0Slot.getStack()) && ItemStack.areItemStackTagsEqual(par0Slot.getStack(), par1ItemStack)) {
			 int var10002 = par2 ? 0 : par1ItemStack.stackSize;
			 var3 |= par0Slot.getStack().stackSize + var10002 <= par1ItemStack.getMaxStackSize();
		 }

		 return var3;
	 }

	 public static void func_94525_a(Set par0Set, int par1, ItemStack par2ItemStack, int par3) {
		 switch (par1) {
		 case 0:
			 par2ItemStack.stackSize = MathHelper.floor_float((float)par2ItemStack.stackSize / (float)par0Set.size());
			 break;

		 case 1:
			 par2ItemStack.stackSize = 1;
		 }

		 par2ItemStack.stackSize += par3;
	 }

	 /**
	  * Returns true if the player can "drag-spilt" items into this slot,. returns true by default. Called to check if the
	  * slot can be added to a list of Slots to split the held ItemStack across.
	  */
	 public boolean canDragIntoSlot(Slot par1Slot) {
		 return true;
	 }

	 public static int calcRedstoneFromInventory(IInventory par0IInventory) {
		 if (par0IInventory == null) {
			 return 0;
		 } else {
			 int var1 = 0;
			 float var2 = 0.0F;

			 for (int var3 = 0; var3 < par0IInventory.getSizeInventory(); ++var3) {
				 ItemStack var4 = par0IInventory.getStackInSlot(var3);

				 if (var4 != null) {
					 var2 += (float)var4.stackSize / (float)Math.min(par0IInventory.getInventoryStackLimit(), var4.getMaxStackSize());
					 ++var1;
				 }
			 }

			 var2 /= (float)par0IInventory.getSizeInventory();
			 return MathHelper.floor_float(var2 * 14.0F) + (var1 > 0 ? 1 : 0);
		 }
	 }
}
