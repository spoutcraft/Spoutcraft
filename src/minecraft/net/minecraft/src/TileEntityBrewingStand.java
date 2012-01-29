package net.minecraft.src;

public class TileEntityBrewingStand extends TileEntity
	implements IInventory {
	private ItemStack brewingItemStacks[];
	private int brewTime;
	private int filledSlots;
	private int ingredientID;

	public TileEntityBrewingStand() {
		brewingItemStacks = new ItemStack[4];
	}

	public String getInvName() {
		return "Brewing Stand";
	}

	public int getSizeInventory() {
		return brewingItemStacks.length;
	}

	public void updateEntity() {
		if (brewTime > 0) {
			brewTime--;
			if (brewTime == 0) {
				brewPotions();
				onInventoryChanged();
			}
			else if (!canBrew()) {
				brewTime = 0;
				onInventoryChanged();
			}
			else if (ingredientID != brewingItemStacks[3].itemID) {
				brewTime = 0;
				onInventoryChanged();
			}
		}
		else if (canBrew()) {
			brewTime = 400;
			ingredientID = brewingItemStacks[3].itemID;
		}
		int i = getFilledSlots();
		if (i != filledSlots) {
			filledSlots = i;
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, i);
		}
		super.updateEntity();
	}

	public int getBrewTime() {
		return brewTime;
	}

	private boolean canBrew() {
		if (brewingItemStacks[3] == null || brewingItemStacks[3].stackSize <= 0) {
			return false;
		}
		ItemStack itemstack = brewingItemStacks[3];
		if (!Item.itemsList[itemstack.itemID].isValidBrewingIngredient()) {
			return false;
		}
		boolean flag = false;
		for (int i = 0; i < 3; i++) {
			if (brewingItemStacks[i] == null || brewingItemStacks[i].itemID != Item.potion.shiftedIndex) {
				continue;
			}
			int j = brewingItemStacks[i].getItemDamage();
			int k = getPotionResult(j, itemstack);
			if (!ItemPotion.isSplash(j) && ItemPotion.isSplash(k)) {
				flag = true;
				break;
			}
			java.util.List list = Item.potion.getEffectNamesFromDamage(j);
			java.util.List list1 = Item.potion.getEffectNamesFromDamage(k);
			if (j > 0 && list == list1 || list != null && (list.equals(list1) || list1 == null) || j == k) {
				continue;
			}
			flag = true;
			break;
		}

		return flag;
	}

	private void brewPotions() {
		if (!canBrew()) {
			return;
		}
		ItemStack itemstack = brewingItemStacks[3];
		for (int i = 0; i < 3; i++) {
			if (brewingItemStacks[i] == null || brewingItemStacks[i].itemID != Item.potion.shiftedIndex) {
				continue;
			}
			int j = brewingItemStacks[i].getItemDamage();
			int k = getPotionResult(j, itemstack);
			java.util.List list = Item.potion.getEffectNamesFromDamage(j);
			java.util.List list1 = Item.potion.getEffectNamesFromDamage(k);
			if (j > 0 && list == list1 || list != null && (list.equals(list1) || list1 == null)) {
				if (!ItemPotion.isSplash(j) && ItemPotion.isSplash(k)) {
					brewingItemStacks[i].setItemDamage(k);
				}
				continue;
			}
			if (j != k) {
				brewingItemStacks[i].setItemDamage(k);
			}
		}

		if (Item.itemsList[itemstack.itemID].hasContainerItem()) {
			brewingItemStacks[3] = new ItemStack(Item.itemsList[itemstack.itemID].getContainerItem());
		}
		else {
			brewingItemStacks[3].stackSize--;
			if (brewingItemStacks[3].stackSize <= 0) {
				brewingItemStacks[3] = null;
			}
		}
	}

	private int getPotionResult(int i, ItemStack itemstack) {
		if (itemstack == null) {
			return i;
		}
		if (Item.itemsList[itemstack.itemID].isValidBrewingIngredient()) {
			return PotionHelper.brewPotionData(i, Item.itemsList[itemstack.itemID].getPotionModifier());
		}
		else {
			return i;
		}
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		brewingItemStacks = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < brewingItemStacks.length) {
				brewingItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		brewTime = nbttagcompound.getShort("BrewTime");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setShort("BrewTime", (short)brewTime);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < brewingItemStacks.length; i++) {
			if (brewingItemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				brewingItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.setTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	public ItemStack getStackInSlot(int i) {
		if (i >= 0 && i < brewingItemStacks.length) {
			return brewingItemStacks[i];
		}
		else {
			return null;
		}
	}

	public ItemStack decrStackSize(int i, int j) {
		if (i >= 0 && i < brewingItemStacks.length) {
			ItemStack itemstack = brewingItemStacks[i];
			brewingItemStacks[i] = null;
			return itemstack;
		}
		else {
			return null;
		}
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if (i >= 0 && i < brewingItemStacks.length) {
			brewingItemStacks[i] = itemstack;
		}
	}

	public int getInventoryStackLimit() {
		return 1;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		}
		return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
	}

	public void openChest() {
	}

	public void closeChest() {
	}

	public void setBrewTime(int i) {
		brewTime = i;
	}

	public int getFilledSlots() {
		int i = 0;
		for (int j = 0; j < 3; j++) {
			if (brewingItemStacks[j] != null) {
				i |= 1 << j;
			}
		}

		return i;
	}
}
