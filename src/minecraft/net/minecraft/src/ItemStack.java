package net.minecraft.src;

import java.util.*;

public final class ItemStack {
	public int stackSize;
	public int animationsToGo;
	public int itemID;
	public NBTTagCompound stackTagCompound;
	private int itemDamage;

	public ItemStack(Block block) {
		this(block, 1);
	}

	public ItemStack(Block block, int i) {
		this(block.blockID, i, 0);
	}

	public ItemStack(Block block, int i, int j) {
		this(block.blockID, i, j);
	}

	public ItemStack(Item item) {
		this(item.shiftedIndex, 1, 0);
	}

	public ItemStack(Item item, int i) {
		this(item.shiftedIndex, i, 0);
	}

	public ItemStack(Item item, int i, int j) {
		this(item.shiftedIndex, i, j);
	}

	public ItemStack(int i, int j, int k) {
		stackSize = 0;
		itemID = i;
		stackSize = j;
		itemDamage = k;
	}

	public static ItemStack loadItemStackFromNBT(NBTTagCompound nbttagcompound) {
		ItemStack itemstack = new ItemStack();
		itemstack.readFromNBT(nbttagcompound);
		return itemstack.getItem() == null ? null : itemstack;
	}

	private ItemStack() {
		stackSize = 0;
	}

	public ItemStack splitStack(int i) {
		ItemStack itemstack = new ItemStack(itemID, i, itemDamage);
		if (stackTagCompound != null) {
			itemstack.stackTagCompound = (NBTTagCompound)stackTagCompound.cloneTag();
		}
		stackSize -= i;
		return itemstack;
	}

	public Item getItem() {
		return Item.itemsList[itemID];
	}

	public int getIconIndex() {
		return getItem().getIconIndex(this);
	}

	public boolean useItem(EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
		boolean flag = getItem().onItemUse(this, entityplayer, world, i, j, k, l);
		if (flag) {
			entityplayer.addStat(StatList.objectUseStats[itemID], 1);
		}
		return flag;
	}

	public float getStrVsBlock(Block block) {
		return getItem().getStrVsBlock(this, block);
	}

	public ItemStack useItemRightClick(World world, EntityPlayer entityplayer) {
		return getItem().onItemRightClick(this, world, entityplayer);
	}

	public ItemStack onFoodEaten(World world, EntityPlayer entityplayer) {
		return getItem().onFoodEaten(this, world, entityplayer);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort("id", (short)itemID);
		nbttagcompound.setByte("Count", (byte)stackSize);
		nbttagcompound.setShort("Damage", (short)itemDamage);
		if (stackTagCompound != null) {
			nbttagcompound.setTag("tag", stackTagCompound);
		}
		return nbttagcompound;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		itemID = nbttagcompound.getShort("id");
		stackSize = nbttagcompound.getByte("Count");
		itemDamage = nbttagcompound.getShort("Damage");
		if (nbttagcompound.hasKey("tag")) {
			stackTagCompound = nbttagcompound.getCompoundTag("tag");
		}
	}

	public int getMaxStackSize() {
		return getItem().getItemStackLimit();
	}

	public boolean isStackable() {
		return getMaxStackSize() > 1 && (!isItemStackDamageable() || !isItemDamaged());
	}

	public boolean isItemStackDamageable() {
		return Item.itemsList[itemID].getMaxDamage() > 0;
	}

	public boolean getHasSubtypes() {
		return Item.itemsList[itemID].getHasSubtypes();
	}

	public boolean isItemDamaged() {
		return isItemStackDamageable() && itemDamage > 0;
	}

	public int getItemDamageForDisplay() {
		return itemDamage;
	}

	public int getItemDamage() {
		return itemDamage;
	}

	public void setItemDamage(int i) {
		itemDamage = i;
	}

	public int getMaxDamage() {
		return Item.itemsList[itemID].getMaxDamage();
	}

	public void damageItem(int i, EntityLiving entityliving) {
		if (!isItemStackDamageable()) {
			return;
		}
		if (i > 0 && (entityliving instanceof EntityPlayer)) {
			int j = EnchantmentHelper.getUnbreakingModifier(((EntityPlayer)entityliving).inventory);
			if (j > 0 && entityliving.worldObj.rand.nextInt(j + 1) > 0) {
				return;
			}
		}
		itemDamage += i;
		if (itemDamage > getMaxDamage()) {
			entityliving.func_41005_b(this);
			if (entityliving instanceof EntityPlayer) {
				((EntityPlayer)entityliving).addStat(StatList.objectBreakStats[itemID], 1);
			}
			stackSize--;
			if (stackSize < 0) {
				stackSize = 0;
			}
			itemDamage = 0;
		}
	}

	public void hitEntity(EntityLiving entityliving, EntityPlayer entityplayer) {
		boolean flag = Item.itemsList[itemID].hitEntity(this, entityliving, entityplayer);
		if (flag) {
			entityplayer.addStat(StatList.objectUseStats[itemID], 1);
		}
	}

	public void onDestroyBlock(int i, int j, int k, int l, EntityPlayer entityplayer) {
		boolean flag = Item.itemsList[itemID].onBlockDestroyed(this, i, j, k, l, entityplayer);
		if (flag) {
			entityplayer.addStat(StatList.objectUseStats[itemID], 1);
		}
	}

	public int getDamageVsEntity(Entity entity) {
		return Item.itemsList[itemID].getDamageVsEntity(entity);
	}

	public boolean canHarvestBlock(Block block) {
		return Item.itemsList[itemID].canHarvestBlock(block);
	}

	public void onItemDestroyedByUse(EntityPlayer entityplayer) {
	}

	public void useItemOnEntity(EntityLiving entityliving) {
		Item.itemsList[itemID].useItemOnEntity(this, entityliving);
	}

	public ItemStack copy() {
		ItemStack itemstack = new ItemStack(itemID, stackSize, itemDamage);
		if (stackTagCompound != null) {
			itemstack.stackTagCompound = (NBTTagCompound)stackTagCompound.cloneTag();
			if (!itemstack.stackTagCompound.equals(stackTagCompound)) {
				return itemstack;
			}
		}
		return itemstack;
	}

	public static boolean func_46154_a(ItemStack itemstack, ItemStack itemstack1) {
		if (itemstack == null && itemstack1 == null) {
			return true;
		}
		if (itemstack == null || itemstack1 == null) {
			return false;
		}
		if (itemstack.stackTagCompound == null && itemstack1.stackTagCompound != null) {
			return false;
		}
		return itemstack.stackTagCompound == null || itemstack.stackTagCompound.equals(itemstack1.stackTagCompound);
	}

	public static boolean areItemStacksEqual(ItemStack itemstack, ItemStack itemstack1) {
		if (itemstack == null && itemstack1 == null) {
			return true;
		}
		if (itemstack == null || itemstack1 == null) {
			return false;
		}
		else {
			return itemstack.isItemStackEqual(itemstack1);
		}
	}

	private boolean isItemStackEqual(ItemStack itemstack) {
		if (stackSize != itemstack.stackSize) {
			return false;
		}
		if (itemID != itemstack.itemID) {
			return false;
		}
		if (itemDamage != itemstack.itemDamage) {
			return false;
		}
		if (stackTagCompound == null && itemstack.stackTagCompound != null) {
			return false;
		}
		return stackTagCompound == null || stackTagCompound.equals(itemstack.stackTagCompound);
	}

	public boolean isItemEqual(ItemStack itemstack) {
		return itemID == itemstack.itemID && itemDamage == itemstack.itemDamage;
	}

	public static ItemStack copyItemStack(ItemStack itemstack) {
		return itemstack != null ? itemstack.copy() : null;
	}

	public String toString() {
		return (new StringBuilder()).append(stackSize).append("x").append(Item.itemsList[itemID].getItemName()).append("@").append(itemDamage).toString();
	}

	public void updateAnimation(World world, Entity entity, int i, boolean flag) {
		if (animationsToGo > 0) {
			animationsToGo--;
		}
		Item.itemsList[itemID].onUpdate(this, world, entity, i, flag);
	}

	public void onCrafting(World world, EntityPlayer entityplayer) {
		entityplayer.addStat(StatList.objectCraftStats[itemID], stackSize);
		Item.itemsList[itemID].onCreated(this, world, entityplayer);
	}

	public boolean isStackEqual(ItemStack itemstack) {
		return itemID == itemstack.itemID && stackSize == itemstack.stackSize && itemDamage == itemstack.itemDamage;
	}

	public int getMaxItemUseDuration() {
		return getItem().getMaxItemUseDuration(this);
	}

	public EnumAction getItemUseAction() {
		return getItem().getItemUseAction(this);
	}

	public void onPlayerStoppedUsing(World world, EntityPlayer entityplayer, int i) {
		getItem().onPlayerStoppedUsing(this, world, entityplayer, i);
	}

	public boolean hasTagCompound() {
		return stackTagCompound != null;
	}

	public NBTTagCompound getTagCompound() {
		return stackTagCompound;
	}

	public NBTTagList getEnchantmentTagList() {
		if (stackTagCompound == null) {
			return null;
		}
		else {
			return (NBTTagList)stackTagCompound.getTag("ench");
		}
	}

	public void setTagCompound(NBTTagCompound nbttagcompound) {
		stackTagCompound = nbttagcompound;
	}

	public List getItemNameandInformation() {
		ArrayList arraylist = new ArrayList();
		Item item = Item.itemsList[itemID];
		arraylist.add(item.getItemDisplayName(this));
		item.addInformation(this, arraylist);
		if (hasTagCompound()) {
			NBTTagList nbttaglist = getEnchantmentTagList();
			if (nbttaglist != null) {
				for (int i = 0; i < nbttaglist.tagCount(); i++) {
					short word0 = ((NBTTagCompound)nbttaglist.tagAt(i)).getShort("id");
					short word1 = ((NBTTagCompound)nbttaglist.tagAt(i)).getShort("lvl");
					if (Enchantment.enchantmentsList[word0] != null) {
						arraylist.add(Enchantment.enchantmentsList[word0].getTranslatedName(word1));
					}
				}
			}
		}
		return arraylist;
	}

	public boolean func_40713_r() {
		return getItem().hasEffect(this);
	}

	public EnumRarity func_40707_s() {
		return getItem().getRarity(this);
	}

	public boolean isItemEnchantable() {
		if (!getItem().isItemTool(this)) {
			return false;
		}
		return !isItemEnchanted();
	}

	public void addEnchantment(Enchantment enchantment, int i) {
		if (stackTagCompound == null) {
			setTagCompound(new NBTTagCompound());
		}
		if (!stackTagCompound.hasKey("ench")) {
			stackTagCompound.setTag("ench", new NBTTagList("ench"));
		}
		NBTTagList nbttaglist = (NBTTagList)stackTagCompound.getTag("ench");
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setShort("id", (short)enchantment.effectId);
		nbttagcompound.setShort("lvl", (byte)i);
		nbttaglist.setTag(nbttagcompound);
	}

	public boolean isItemEnchanted() {
		return stackTagCompound != null && stackTagCompound.hasKey("ench");
	}
}
