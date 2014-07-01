package net.minecraft.src;

import java.util.Iterator;
import java.util.Map;

public class ContainerRepair extends Container {

	/** Here comes out item you merged and/or renamed. */
	private IInventory outputSlot = new InventoryCraftResult();

	/**
	 * The 2slots where you put your items in that you want to merge and/or rename.
	 */
	private IInventory inputSlots = new ContainerRepairINNER1(this, "Repair", true, 2);
	private World theWorld;
	private int field_82861_i;
	private int field_82858_j;
	private int field_82859_k;

	/** The maximum cost of repairing/renaming in the anvil. */
	public int maximumCost;

	/** determined by damage of input item and stackSize of repair materials */
	private int stackSizeToBeUsedInRepair;
	private String repairedItemName;

	/** The player that has this container open. */
	private final EntityPlayer thePlayer;

	public ContainerRepair(InventoryPlayer par1InventoryPlayer, World par2World, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
		this.theWorld = par2World;
		this.field_82861_i = par3;
		this.field_82858_j = par4;
		this.field_82859_k = par5;
		this.thePlayer = par6EntityPlayer;
		this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
		this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
		this.addSlotToContainer(new ContainerRepairINNER2(this, this.outputSlot, 2, 134, 47, par2World, par3, par4, par5));
		int var7;

		for (var7 = 0; var7 < 3; ++var7) {
			for (int var8 = 0; var8 < 9; ++var8) {
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var8 + var7 * 9 + 9, 8 + var8 * 18, 84 + var7 * 18));
			}
		}

		for (var7 = 0; var7 < 9; ++var7) {
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var7, 8 + var7 * 18, 142));
		}
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	public void onCraftMatrixChanged(IInventory par1IInventory) {
		super.onCraftMatrixChanged(par1IInventory);

		if (par1IInventory == this.inputSlots) {
			this.updateRepairOutput();
		}
	}

	/**
	 * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
	 */
	public void updateRepairOutput() {
		ItemStack var1 = this.inputSlots.getStackInSlot(0);
		this.maximumCost = 0;
		int var2 = 0;
		byte var3 = 0;
		int var4 = 0;

		if (var1 == null) {
			this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
			this.maximumCost = 0;
		} else {
			ItemStack var5 = var1.copy();
			ItemStack var6 = this.inputSlots.getStackInSlot(1);
			Map var7 = EnchantmentHelper.getEnchantments(var5);
			boolean var8 = false;
			int var19 = var3 + var1.getRepairCost() + (var6 == null ? 0 : var6.getRepairCost());
			this.stackSizeToBeUsedInRepair = 0;
			int var9;
			int var10;
			int var11;
			int var13;
			int var14;
			Iterator var21;
			Enchantment var22;

			if (var6 != null) {
				var8 = var6.itemID == Item.enchantedBook.itemID && Item.enchantedBook.func_92110_g(var6).tagCount() > 0;

				if (var5.isItemStackDamageable() && Item.itemsList[var5.itemID].getIsRepairable(var1, var6)) {
					var9 = Math.min(var5.getItemDamageForDisplay(), var5.getMaxDamage() / 4);

					if (var9 <= 0) {
						this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
						this.maximumCost = 0;
						return;
					}

					for (var10 = 0; var9 > 0 && var10 < var6.stackSize; ++var10) {
						var11 = var5.getItemDamageForDisplay() - var9;
						var5.setItemDamage(var11);
						var2 += Math.max(1, var9 / 100) + var7.size();
						var9 = Math.min(var5.getItemDamageForDisplay(), var5.getMaxDamage() / 4);
					}

					this.stackSizeToBeUsedInRepair = var10;
				} else {
					if (!var8 && (var5.itemID != var6.itemID || !var5.isItemStackDamageable())) {
						this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
						this.maximumCost = 0;
						return;
					}

					if (var5.isItemStackDamageable() && !var8) {
						var9 = var1.getMaxDamage() - var1.getItemDamageForDisplay();
						var10 = var6.getMaxDamage() - var6.getItemDamageForDisplay();
						var11 = var10 + var5.getMaxDamage() * 12 / 100;
						int var12 = var9 + var11;
						var13 = var5.getMaxDamage() - var12;

						if (var13 < 0) {
							var13 = 0;
						}

						if (var13 < var5.getItemDamage()) {
							var5.setItemDamage(var13);
							var2 += Math.max(1, var11 / 100);
						}
					}

					Map var20 = EnchantmentHelper.getEnchantments(var6);
					var21 = var20.keySet().iterator();

					while (var21.hasNext()) {
						var11 = ((Integer)var21.next()).intValue();
						var22 = Enchantment.enchantmentsList[var11];
						var13 = var7.containsKey(Integer.valueOf(var11)) ? ((Integer)var7.get(Integer.valueOf(var11))).intValue() : 0;
						var14 = ((Integer)var20.get(Integer.valueOf(var11))).intValue();
						int var10000;

						if (var13 == var14) {
							++var14;
							var10000 = var14;
						} else {
							var10000 = Math.max(var14, var13);
						}

						var14 = var10000;
						int var15 = var14 - var13;
						boolean var16 = var22.canApply(var1);

						if (this.thePlayer.capabilities.isCreativeMode || var1.itemID == ItemEnchantedBook.enchantedBook.itemID) {
							var16 = true;
						}

						Iterator var17 = var7.keySet().iterator();

						while (var17.hasNext()) {
							int var18 = ((Integer)var17.next()).intValue();

							if (var18 != var11 && !var22.canApplyTogether(Enchantment.enchantmentsList[var18])) {
								var16 = false;
								var2 += var15;
							}
						}

						if (var16) {
							if (var14 > var22.getMaxLevel()) {
								var14 = var22.getMaxLevel();
							}

							var7.put(Integer.valueOf(var11), Integer.valueOf(var14));
							int var23 = 0;

							switch (var22.getWeight()) {
								case 1:
									var23 = 8;
									break;

								case 2:
									var23 = 4;

								case 3:
								case 4:
								case 6:
								case 7:
								case 8:
								case 9:
								default:
									break;

								case 5:
									var23 = 2;
									break;

								case 10:
									var23 = 1;
							}

							if (var8) {
								var23 = Math.max(1, var23 / 2);
							}

							var2 += var23 * var15;
						}
					}
				}
			}

			if (org.apache.commons.lang3.StringUtils.isBlank(this.repairedItemName)) {
				if (var1.hasDisplayName()) {
					var4 = var1.isItemStackDamageable() ? 7 : var1.stackSize * 5;
					var2 += var4;
					var5.func_135074_t();
				}
			} else if (!this.repairedItemName.equals(var1.getDisplayName())) {
				var4 = var1.isItemStackDamageable() ? 7 : var1.stackSize * 5;
				var2 += var4;

				if (var1.hasDisplayName()) {
					var19 += var4 / 2;
				}

				var5.setItemName(this.repairedItemName);
			}

			var9 = 0;

			for (var21 = var7.keySet().iterator(); var21.hasNext(); var19 += var9 + var13 * var14) {
				var11 = ((Integer)var21.next()).intValue();
				var22 = Enchantment.enchantmentsList[var11];
				var13 = ((Integer)var7.get(Integer.valueOf(var11))).intValue();
				var14 = 0;
				++var9;

				switch (var22.getWeight()) {
					case 1:
						var14 = 8;
						break;

					case 2:
						var14 = 4;

					case 3:
					case 4:
					case 6:
					case 7:
					case 8:
					case 9:
					default:
						break;

					case 5:
						var14 = 2;
						break;

					case 10:
						var14 = 1;
				}

				if (var8) {
					var14 = Math.max(1, var14 / 2);
				}
			}

			if (var8) {
				var19 = Math.max(1, var19 / 2);
			}

			this.maximumCost = var19 + var2;

			if (var2 <= 0) {
				var5 = null;
			}

			if (var4 == var2 && var4 > 0 && this.maximumCost >= 40) {
				this.maximumCost = 39;
			}

			if (this.maximumCost >= 40 && !this.thePlayer.capabilities.isCreativeMode) {
				var5 = null;
			}

			if (var5 != null) {
				var10 = var5.getRepairCost();

				if (var6 != null && var10 < var6.getRepairCost()) {
					var10 = var6.getRepairCost();
				}

				if (var5.hasDisplayName()) {
					var10 -= 9;
				}

				if (var10 < 0) {
					var10 = 0;
				}

				var10 += 2;
				var5.setRepairCost(var10);
				EnchantmentHelper.setEnchantments(var7, var5);
			}

			this.outputSlot.setInventorySlotContents(0, var5);
			this.detectAndSendChanges();
		}
	}

	public void addCraftingToCrafters(ICrafting par1ICrafting) {
		super.addCraftingToCrafters(par1ICrafting);
		par1ICrafting.sendProgressBarUpdate(this, 0, this.maximumCost);
	}

	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			this.maximumCost = par2;
		}
	}

	/**
	 * Called when the container is closed.
	 */
	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);

		if (!this.theWorld.isRemote) {
			for (int var2 = 0; var2 < this.inputSlots.getSizeInventory(); ++var2) {
				ItemStack var3 = this.inputSlots.getStackInSlotOnClosing(var2);

				if (var3 != null) {
					par1EntityPlayer.dropPlayerItem(var3);
				}
			}
		}
	}

	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return this.theWorld.getBlockId(this.field_82861_i, this.field_82858_j, this.field_82859_k) != Block.anvil.blockID ? false : par1EntityPlayer.getDistanceSq((double)this.field_82861_i + 0.5D, (double)this.field_82858_j + 0.5D, (double)this.field_82859_k + 0.5D) <= 64.0D;
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

			if (par2 == 2) {
				if (!this.mergeItemStack(var5, 3, 39, true)) {
					return null;
				}

				var4.onSlotChange(var5, var3);
			} else if (par2 != 0 && par2 != 1) {
				if (par2 >= 3 && par2 < 39 && !this.mergeItemStack(var5, 0, 2, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(var5, 3, 39, false)) {
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

	/**
	 * used by the Anvil GUI to update the Item Name being typed by the player
	 */
	public void updateItemName(String par1Str) {
		this.repairedItemName = par1Str;

		if (this.getSlot(2).getHasStack()) {
			ItemStack var2 = this.getSlot(2).getStack();

			if (org.apache.commons.lang3.StringUtils.isBlank(par1Str)) {
				var2.func_135074_t();
			} else {
				var2.setItemName(this.repairedItemName);
			}
		}

		this.updateRepairOutput();
	}

	static IInventory getRepairInputInventory(ContainerRepair par0ContainerRepair) {
		return par0ContainerRepair.inputSlots;
	}

	static int getStackSizeUsedInRepair(ContainerRepair par0ContainerRepair) {
		return par0ContainerRepair.stackSizeToBeUsedInRepair;
	}
}
