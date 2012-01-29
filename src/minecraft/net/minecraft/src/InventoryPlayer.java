package net.minecraft.src;

public class InventoryPlayer
	implements IInventory {
	public ItemStack mainInventory[];
	public ItemStack armorInventory[];
	public int currentItem;
	public EntityPlayer player;
	private ItemStack itemStack;
	public boolean inventoryChanged;

	public InventoryPlayer(EntityPlayer entityplayer) {
		mainInventory = new ItemStack[36];
		armorInventory = new ItemStack[4];
		currentItem = 0;
		inventoryChanged = false;
		player = entityplayer;
	}

	public ItemStack getCurrentItem() {
		if (currentItem < 9 && currentItem >= 0) {
			return mainInventory[currentItem];
		}
		else {
			return null;
		}
	}

	private int getInventorySlotContainItem(int i) {
		for (int j = 0; j < mainInventory.length; j++) {
			if (mainInventory[j] != null && mainInventory[j].itemID == i) {
				return j;
			}
		}

		return -1;
	}

	private int getInventorySlotContainItemAndDamage(int i, int j) {
		for (int k = 0; k < mainInventory.length; k++) {
			if (mainInventory[k] != null && mainInventory[k].itemID == i && mainInventory[k].getItemDamage() == j) {
				return k;
			}
		}

		return -1;
	}

	private int storeItemStack(ItemStack itemstack) {
		for (int i = 0; i < mainInventory.length; i++) {
			if (mainInventory[i] != null && mainInventory[i].itemID == itemstack.itemID && mainInventory[i].isStackable() && mainInventory[i].stackSize < mainInventory[i].getMaxStackSize() && mainInventory[i].stackSize < getInventoryStackLimit() && (!mainInventory[i].getHasSubtypes() || mainInventory[i].getItemDamage() == itemstack.getItemDamage()) && ItemStack.func_46154_a(mainInventory[i], itemstack)) {
				return i;
			}
		}

		return -1;
	}

	private int getFirstEmptyStack() {
		for (int i = 0; i < mainInventory.length; i++) {
			if (mainInventory[i] == null) {
				return i;
			}
		}

		return -1;
	}

	public void setCurrentItem(int i, int j, boolean flag, boolean flag1) {
		int k = -1;
		if (flag) {
			k = getInventorySlotContainItemAndDamage(i, j);
		}
		else {
			k = getInventorySlotContainItem(i);
		}
		if (k >= 0 && k < 9) {
			currentItem = k;
			return;
		}
		else {
			return;
		}
	}

	public void changeCurrentItem(int i) {
		if (i > 0) {
			i = 1;
		}
		if (i < 0) {
			i = -1;
		}
		for (currentItem -= i; currentItem < 0; currentItem += 9) { }
		for (; currentItem >= 9; currentItem -= 9) { }
	}

	private int storePartialItemStack(ItemStack itemstack) {
		int i = itemstack.itemID;
		int j = itemstack.stackSize;
		if (itemstack.getMaxStackSize() == 1) {
			int k = getFirstEmptyStack();
			if (k < 0) {
				return j;
			}
			if (mainInventory[k] == null) {
				mainInventory[k] = ItemStack.copyItemStack(itemstack);
			}
			return 0;
		}
		int l = storeItemStack(itemstack);
		if (l < 0) {
			l = getFirstEmptyStack();
		}
		if (l < 0) {
			return j;
		}
		if (mainInventory[l] == null) {
			mainInventory[l] = new ItemStack(i, 0, itemstack.getItemDamage());
			if (itemstack.hasTagCompound()) {
				mainInventory[l].setTagCompound((NBTTagCompound)itemstack.getTagCompound().cloneTag());
			}
		}
		int i1 = j;
		if (i1 > mainInventory[l].getMaxStackSize() - mainInventory[l].stackSize) {
			i1 = mainInventory[l].getMaxStackSize() - mainInventory[l].stackSize;
		}
		if (i1 > getInventoryStackLimit() - mainInventory[l].stackSize) {
			i1 = getInventoryStackLimit() - mainInventory[l].stackSize;
		}
		if (i1 == 0) {
			return j;
		}
		else {
			j -= i1;
			mainInventory[l].stackSize += i1;
			mainInventory[l].animationsToGo = 5;
			return j;
		}
	}

	public void decrementAnimations() {
		for (int i = 0; i < mainInventory.length; i++) {
			if (mainInventory[i] != null) {
				mainInventory[i].updateAnimation(player.worldObj, player, i, currentItem == i);
			}
		}
	}

	public boolean consumeInventoryItem(int i) {
		int j = getInventorySlotContainItem(i);
		if (j < 0) {
			return false;
		}
		if (--mainInventory[j].stackSize <= 0) {
			mainInventory[j] = null;
		}
		return true;
	}

	public boolean hasItem(int i) {
		int j = getInventorySlotContainItem(i);
		return j >= 0;
	}

	public boolean addItemStackToInventory(ItemStack itemstack) {
		if (!itemstack.isItemDamaged()) {
			int i;
			do {
				i = itemstack.stackSize;
				itemstack.stackSize = storePartialItemStack(itemstack);
			}
			while (itemstack.stackSize > 0 && itemstack.stackSize < i);
			if (itemstack.stackSize == i && player.capabilities.depleteBuckets) {
				itemstack.stackSize = 0;
				return true;
			}
			else {
				return itemstack.stackSize < i;
			}
		}
		int j = getFirstEmptyStack();
		if (j >= 0) {
			mainInventory[j] = ItemStack.copyItemStack(itemstack);
			mainInventory[j].animationsToGo = 5;
			itemstack.stackSize = 0;
			return true;
		}
		if (player.capabilities.depleteBuckets) {
			itemstack.stackSize = 0;
			return true;
		}
		else {
			return false;
		}
	}

	public ItemStack decrStackSize(int i, int j) {
		ItemStack aitemstack[] = mainInventory;
		if (i >= mainInventory.length) {
			aitemstack = armorInventory;
			i -= mainInventory.length;
		}
		if (aitemstack[i] != null) {
			if (aitemstack[i].stackSize <= j) {
				ItemStack itemstack = aitemstack[i];
				aitemstack[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = aitemstack[i].splitStack(j);
			if (aitemstack[i].stackSize == 0) {
				aitemstack[i] = null;
			}
			return itemstack1;
		}
		else {
			return null;
		}
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		ItemStack aitemstack[] = mainInventory;
		if (i >= aitemstack.length) {
			i -= aitemstack.length;
			aitemstack = armorInventory;
		}
		aitemstack[i] = itemstack;
	}

	public float getStrVsBlock(Block block) {
		float f = 1.0F;
		if (mainInventory[currentItem] != null) {
			f *= mainInventory[currentItem].getStrVsBlock(block);
		}
		return f;
	}

	public NBTTagList writeToNBT(NBTTagList nbttaglist) {
		for (int i = 0; i < mainInventory.length; i++) {
			if (mainInventory[i] != null) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte)i);
				mainInventory[i].writeToNBT(nbttagcompound);
				nbttaglist.setTag(nbttagcompound);
			}
		}

		for (int j = 0; j < armorInventory.length; j++) {
			if (armorInventory[j] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)(j + 100));
				armorInventory[j].writeToNBT(nbttagcompound1);
				nbttaglist.setTag(nbttagcompound1);
			}
		}

		return nbttaglist;
	}

	public void readFromNBT(NBTTagList nbttaglist) {
		mainInventory = new ItemStack[36];
		armorInventory = new ItemStack[4];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);
			int j = nbttagcompound.getByte("Slot") & 0xff;
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
			if (itemstack == null) {
				continue;
			}
			if (j >= 0 && j < mainInventory.length) {
				mainInventory[j] = itemstack;
			}
			if (j >= 100 && j < armorInventory.length + 100) {
				armorInventory[j - 100] = itemstack;
			}
		}
	}

	public int getSizeInventory() {
		return mainInventory.length + 4;
	}

	public ItemStack getStackInSlot(int i) {
		ItemStack aitemstack[] = mainInventory;
		if (i >= aitemstack.length) {
			i -= aitemstack.length;
			aitemstack = armorInventory;
		}
		return aitemstack[i];
	}

	public String getInvName() {
		return "Inventory";
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public int getDamageVsEntity(Entity entity) {
		ItemStack itemstack = getStackInSlot(currentItem);
		if (itemstack != null) {
			return itemstack.getDamageVsEntity(entity);
		}
		else {
			return 1;
		}
	}

	public boolean canHarvestBlock(Block block) {
		if (block.blockMaterial.getIsHarvestable()) {
			return true;
		}
		ItemStack itemstack = getStackInSlot(currentItem);
		if (itemstack != null) {
			return itemstack.canHarvestBlock(block);
		}
		else {
			return false;
		}
	}

	public ItemStack armorItemInSlot(int i) {
		return armorInventory[i];
	}

	public int getTotalArmorValue() {
		int i = 0;
		for (int j = 0; j < armorInventory.length; j++) {
			if (armorInventory[j] != null && (armorInventory[j].getItem() instanceof ItemArmor)) {
				int k = ((ItemArmor)armorInventory[j].getItem()).damageReduceAmount;
				i += k;
			}
		}

		return i;
	}

	public void damageArmor(int i) {
		i /= 4;
		if (i < 1) {
			i = 1;
		}
		for (int j = 0; j < armorInventory.length; j++) {
			if (armorInventory[j] == null || !(armorInventory[j].getItem() instanceof ItemArmor)) {
				continue;
			}
			armorInventory[j].damageItem(i, player);
			if (armorInventory[j].stackSize == 0) {
				armorInventory[j].onItemDestroyedByUse(player);
				armorInventory[j] = null;
			}
		}
	}

	public void dropAllItems() {
		for (int i = 0; i < mainInventory.length; i++) {
			if (mainInventory[i] != null) {
				player.dropPlayerItemWithRandomChoice(mainInventory[i], true);
				mainInventory[i] = null;
			}
		}

		for (int j = 0; j < armorInventory.length; j++) {
			if (armorInventory[j] != null) {
				player.dropPlayerItemWithRandomChoice(armorInventory[j], true);
				armorInventory[j] = null;
			}
		}
	}

	public void onInventoryChanged() {
		inventoryChanged = true;
	}

	public void setItemStack(ItemStack itemstack) {
		itemStack = itemstack;
		player.onItemStackChanged(itemstack);
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (player.isDead) {
			return false;
		}
		return entityplayer.getDistanceSqToEntity(player) <= 64D;
	}

	public boolean hasItemStack(ItemStack itemstack) {
		for (int i = 0; i < armorInventory.length; i++) {
			if (armorInventory[i] != null && armorInventory[i].isStackEqual(itemstack)) {
				return true;
			}
		}

		for (int j = 0; j < mainInventory.length; j++) {
			if (mainInventory[j] != null && mainInventory[j].isStackEqual(itemstack)) {
				return true;
			}
		}

		return false;
	}

	public void openChest() {
	}

	public void closeChest() {
	}

	public void copyInventory(InventoryPlayer inventoryplayer) {
		for (int i = 0; i < mainInventory.length; i++) {
			mainInventory[i] = ItemStack.copyItemStack(inventoryplayer.mainInventory[i]);
		}

		for (int j = 0; j < armorInventory.length; j++) {
			armorInventory[j] = ItemStack.copyItemStack(inventoryplayer.armorInventory[j]);
		}
	}
}
