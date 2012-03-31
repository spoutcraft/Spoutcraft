package net.minecraft.src;

public class Slot {
	public final int slotIndex; //Spout private -> public
	public final IInventory inventory;
	public int slotNumber;
	public int xDisplayPosition;
	public int yDisplayPosition;

	public Slot(IInventory par1IInventory, int par2, int par3, int par4) {
		this.inventory = par1IInventory;
		this.slotIndex = par2;
		this.xDisplayPosition = par3;
		this.yDisplayPosition = par4;
	}

	public void func_48433_a(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		if (par1ItemStack != null && par2ItemStack != null) {
			if (par1ItemStack.itemID == par2ItemStack.itemID) {
				int var3 = par2ItemStack.stackSize - par1ItemStack.stackSize;
				if (var3 > 0) {
					this.func_48435_a(par1ItemStack, var3);
				}
			}
		}
	}

	protected void func_48435_a(ItemStack par1ItemStack, int par2) {}

	protected void func_48434_c(ItemStack par1ItemStack) {}

	public void onPickupFromSlot(ItemStack par1ItemStack) {
		this.onSlotChanged();
	}

	public boolean isItemValid(ItemStack par1ItemStack) {
		return true;
	}

	public ItemStack getStack() {
		return this.inventory.getStackInSlot(this.slotIndex);
	}

	public boolean getHasStack() {
		return this.getStack() != null;
	}

	public void putStack(ItemStack par1ItemStack) {
		this.inventory.setInventorySlotContents(this.slotIndex, par1ItemStack);
		this.onSlotChanged();
	}

	public void onSlotChanged() {
		this.inventory.onInventoryChanged();
	}

	public int getSlotStackLimit() {
		return this.inventory.getInventoryStackLimit();
	}

	public int getBackgroundIconIndex() {
		return -1;
	}

	public ItemStack decrStackSize(int par1) {
		return this.inventory.decrStackSize(this.slotIndex, par1);
	}
}
