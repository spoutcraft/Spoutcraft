package net.minecraft.src;

import java.util.*;

public abstract class Container {
	public List inventoryItemStacks;
	public List inventorySlots;
	public int windowId;
	private short field_20917_a;
	protected List crafters;
	private Set field_20918_b;

	public Container() {
		inventoryItemStacks = new ArrayList();
		inventorySlots = new ArrayList();
		windowId = 0;
		field_20917_a = 0;
		crafters = new ArrayList();
		field_20918_b = new HashSet();
	}

	protected void addSlot(Slot slot) {
		slot.slotNumber = inventorySlots.size();
		inventorySlots.add(slot);
		inventoryItemStacks.add(null);
	}

	public void updateCraftingResults() {
		for (int i = 0; i < inventorySlots.size(); i++) {
			ItemStack itemstack = ((Slot)inventorySlots.get(i)).getStack();
			ItemStack itemstack1 = (ItemStack)inventoryItemStacks.get(i);
			if (ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
				continue;
			}
			itemstack1 = itemstack != null ? itemstack.copy() : null;
			inventoryItemStacks.set(i, itemstack1);
			for (int j = 0; j < crafters.size(); j++) {
				((ICrafting)crafters.get(j)).updateCraftingInventorySlot(this, i, itemstack1);
			}
		}
	}

	public boolean enchantItem(EntityPlayer entityplayer, int i) {
		return false;
	}

	public Slot getSlot(int i) {
		return (Slot)inventorySlots.get(i);
	}

	public ItemStack transferStackInSlot(int i) {
		Slot slot = (Slot)inventorySlots.get(i);
		if (slot != null) {
			return slot.getStack();
		}
		else {
			return null;
		}
	}

	public ItemStack slotClick(int i, int j, boolean flag, EntityPlayer entityplayer) {
		ItemStack itemstack = null;
		if (j > 1) {
			return null;
		}
		if (j == 0 || j == 1) {
			InventoryPlayer inventoryplayer = entityplayer.inventory;
			if (i == -999) {
				if (inventoryplayer.getItemStack() != null && i == -999) {
					if (j == 0) {
						entityplayer.dropPlayerItem(inventoryplayer.getItemStack());
						inventoryplayer.setItemStack(null);
					}
					if (j == 1) {
						entityplayer.dropPlayerItem(inventoryplayer.getItemStack().splitStack(1));
						if (inventoryplayer.getItemStack().stackSize == 0) {
							inventoryplayer.setItemStack(null);
						}
					}
				}
			}
			else if (flag) {
				ItemStack itemstack1 = transferStackInSlot(i);
				if (itemstack1 != null) {
					int k = itemstack1.itemID;
					itemstack = itemstack1.copy();
					Slot slot1 = (Slot)inventorySlots.get(i);
					if (slot1 != null && slot1.getStack() != null && slot1.getStack().itemID == k) {
						retrySlotClick(i, j, flag, entityplayer);
					}
				}
			}
			else {
				if (i < 0) {
					return null;
				}
				Slot slot = (Slot)inventorySlots.get(i);
				if (slot != null) {
					slot.onSlotChanged();
					ItemStack itemstack2 = slot.getStack();
					ItemStack itemstack3 = inventoryplayer.getItemStack();
					if (itemstack2 != null) {
						itemstack = itemstack2.copy();
					}
					if (itemstack2 == null) {
						if (itemstack3 != null && slot.isItemValid(itemstack3)) {
							int l = j != 0 ? 1 : itemstack3.stackSize;
							if (l > slot.getSlotStackLimit()) {
								l = slot.getSlotStackLimit();
							}
							slot.putStack(itemstack3.splitStack(l));
							if (itemstack3.stackSize == 0) {
								inventoryplayer.setItemStack(null);
							}
						}
					}
					else if (itemstack3 == null) {
						int i1 = j != 0 ? (itemstack2.stackSize + 1) / 2 : itemstack2.stackSize;
						ItemStack itemstack5 = slot.decrStackSize(i1);
						inventoryplayer.setItemStack(itemstack5);
						if (itemstack2.stackSize == 0) {
							slot.putStack(null);
						}
						slot.onPickupFromSlot(inventoryplayer.getItemStack());
					}
					else if (slot.isItemValid(itemstack3)) {
						if (itemstack2.itemID != itemstack3.itemID || itemstack2.getHasSubtypes() && itemstack2.getItemDamage() != itemstack3.getItemDamage() || !ItemStack.func_46154_a(itemstack2, itemstack3)) {
							if (itemstack3.stackSize <= slot.getSlotStackLimit()) {
								ItemStack itemstack4 = itemstack2;
								slot.putStack(itemstack3);
								inventoryplayer.setItemStack(itemstack4);
							}
						}
						else {
							int j1 = j != 0 ? 1 : itemstack3.stackSize;
							if (j1 > slot.getSlotStackLimit() - itemstack2.stackSize) {
								j1 = slot.getSlotStackLimit() - itemstack2.stackSize;
							}
							if (j1 > itemstack3.getMaxStackSize() - itemstack2.stackSize) {
								j1 = itemstack3.getMaxStackSize() - itemstack2.stackSize;
							}
							itemstack3.splitStack(j1);
							if (itemstack3.stackSize == 0) {
								inventoryplayer.setItemStack(null);
							}
							itemstack2.stackSize += j1;
						}
					}
					else if (itemstack2.itemID == itemstack3.itemID && itemstack3.getMaxStackSize() > 1 && (!itemstack2.getHasSubtypes() || itemstack2.getItemDamage() == itemstack3.getItemDamage()) && ItemStack.func_46154_a(itemstack2, itemstack3)) {
						int k1 = itemstack2.stackSize;
						if (k1 > 0 && k1 + itemstack3.stackSize <= itemstack3.getMaxStackSize()) {
							itemstack3.stackSize += k1;
							itemstack2.splitStack(k1);
							if (itemstack2.stackSize == 0) {
								slot.putStack(null);
							}
							slot.onPickupFromSlot(inventoryplayer.getItemStack());
						}
					}
				}
			}
		}
		return itemstack;
	}

	protected void retrySlotClick(int i, int j, boolean flag, EntityPlayer entityplayer) {
		slotClick(i, j, flag, entityplayer);
	}

	public void onCraftGuiClosed(EntityPlayer entityplayer) {
		InventoryPlayer inventoryplayer = entityplayer.inventory;
		if (inventoryplayer.getItemStack() != null) {
			entityplayer.dropPlayerItem(inventoryplayer.getItemStack());
			inventoryplayer.setItemStack(null);
		}
	}

	public void onCraftMatrixChanged(IInventory iinventory) {
		updateCraftingResults();
	}

	public void putStackInSlot(int i, ItemStack itemstack) {
		getSlot(i).putStack(itemstack);
	}

	public void putStacksInSlots(ItemStack aitemstack[]) {
		for (int i = 0; i < aitemstack.length; i++) {
			getSlot(i).putStack(aitemstack[i]);
		}
	}

	public void updateProgressBar(int i, int j) {
	}

	public short func_20111_a(InventoryPlayer inventoryplayer) {
		field_20917_a++;
		return field_20917_a;
	}

	public void func_20113_a(short word0) {
	}

	public void func_20110_b(short word0) {
	}

	public abstract boolean canInteractWith(EntityPlayer entityplayer);

	protected boolean mergeItemStack(ItemStack itemstack, int i, int j, boolean flag) {
		boolean flag1 = false;
		int k = i;
		if (flag) {
			k = j - 1;
		}
		if (itemstack.isStackable()) {
			while (itemstack.stackSize > 0 && (!flag && k < j || flag && k >= i)) {
				Slot slot = (Slot)inventorySlots.get(k);
				ItemStack itemstack1 = slot.getStack();
				if (itemstack1 != null && itemstack1.itemID == itemstack.itemID && (!itemstack.getHasSubtypes() || itemstack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.func_46154_a(itemstack, itemstack1)) {
					int i1 = itemstack1.stackSize + itemstack.stackSize;
					if (i1 <= itemstack.getMaxStackSize()) {
						itemstack.stackSize = 0;
						itemstack1.stackSize = i1;
						slot.onSlotChanged();
						flag1 = true;
					}
					else if (itemstack1.stackSize < itemstack.getMaxStackSize()) {
						itemstack.stackSize -= itemstack.getMaxStackSize() - itemstack1.stackSize;
						itemstack1.stackSize = itemstack.getMaxStackSize();
						slot.onSlotChanged();
						flag1 = true;
					}
				}
				if (flag) {
					k--;
				}
				else {
					k++;
				}
			}
		}
		if (itemstack.stackSize > 0) {
			int l;
			if (flag) {
				l = j - 1;
			}
			else {
				l = i;
			}
			do {
				if ((flag || l >= j) && (!flag || l < i)) {
					break;
				}
				Slot slot1 = (Slot)inventorySlots.get(l);
				ItemStack itemstack2 = slot1.getStack();
				if (itemstack2 == null) {
					slot1.putStack(itemstack.copy());
					slot1.onSlotChanged();
					itemstack.stackSize = 0;
					flag1 = true;
					break;
				}
				if (flag) {
					l--;
				}
				else {
					l++;
				}
			}
			while (true);
		}
		return flag1;
	}
}
