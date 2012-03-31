package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class ItemStack { //Spout final -> gone
	public int stackSize;
	public int animationsToGo;
	public int itemID;
	public NBTTagCompound stackTagCompound;
	private int itemDamage;

	public ItemStack(Block par1Block) {
		this(par1Block, 1);
	}

	public ItemStack(Block par1Block, int par2) {
		this(par1Block.blockID, par2, 0);
	}

	public ItemStack(Block par1Block, int par2, int par3) {
		this(par1Block.blockID, par2, par3);
	}

	public ItemStack(Item par1Item) {
		this(par1Item.shiftedIndex, 1, 0);
	}

	public ItemStack(Item par1Item, int par2) {
		this(par1Item.shiftedIndex, par2, 0);
	}

	public ItemStack(Item par1Item, int par2, int par3) {
		this(par1Item.shiftedIndex, par2, par3);
	}

	public ItemStack(int par1, int par2, int par3) {
		this.stackSize = 0;
		this.itemID = par1;
		this.stackSize = par2;
		this.itemDamage = par3;
	}

	public static ItemStack loadItemStackFromNBT(NBTTagCompound par0NBTTagCompound) {
		ItemStack var1 = new ItemStack();
		var1.readFromNBT(par0NBTTagCompound);
		return var1.getItem() != null?var1:null;
	}

	private ItemStack() {
		this.stackSize = 0;
	}

	public ItemStack splitStack(int par1) {
		ItemStack var2 = new ItemStack(this.itemID, par1, this.itemDamage);
		if (this.stackTagCompound != null) {
			var2.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
		}

		this.stackSize -= par1;
		return var2;
	}

	public Item getItem() {
		return Item.itemsList[this.itemID];
	}

	public int getIconIndex() {
		return this.getItem().getIconIndex(this);
	}

	public boolean useItem(EntityPlayer par1EntityPlayer, World par2World, int par3, int par4, int par5, int par6) {
		boolean var7 = this.getItem().onItemUse(this, par1EntityPlayer, par2World, par3, par4, par5, par6);
		if (var7) {
			par1EntityPlayer.addStat(StatList.objectUseStats[this.itemID], 1);
		}

		return var7;
	}

	public float getStrVsBlock(Block par1Block) {
		return this.getItem().getStrVsBlock(this, par1Block);
	}

	public ItemStack useItemRightClick(World par1World, EntityPlayer par2EntityPlayer) {
		return this.getItem().onItemRightClick(this, par1World, par2EntityPlayer);
	}

	public ItemStack onFoodEaten(World par1World, EntityPlayer par2EntityPlayer) {
		return this.getItem().onFoodEaten(this, par1World, par2EntityPlayer);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setShort("id", (short)this.itemID);
		par1NBTTagCompound.setByte("Count", (byte)this.stackSize);
		par1NBTTagCompound.setShort("Damage", (short)this.itemDamage);
		if (this.stackTagCompound != null) {
			par1NBTTagCompound.setTag("tag", this.stackTagCompound);
		}

		return par1NBTTagCompound;
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		this.itemID = par1NBTTagCompound.getShort("id");
		this.stackSize = par1NBTTagCompound.getByte("Count");
		this.itemDamage = par1NBTTagCompound.getShort("Damage");
		if (par1NBTTagCompound.hasKey("tag")) {
			this.stackTagCompound = par1NBTTagCompound.getCompoundTag("tag");
		}
	}

	public int getMaxStackSize() {
		return this.getItem().getItemStackLimit();
	}

	public boolean isStackable() {
		return this.getMaxStackSize() > 1 && (!this.isItemStackDamageable() || !this.isItemDamaged());
	}

	public boolean isItemStackDamageable() {
		return Item.itemsList[this.itemID].getMaxDamage() > 0;
	}

	public boolean getHasSubtypes() {
		return Item.itemsList[this.itemID].getHasSubtypes();
	}

	public boolean isItemDamaged() {
		return this.isItemStackDamageable() && this.itemDamage > 0;
	}

	public int getItemDamageForDisplay() {
		return this.itemDamage;
	}

	public int getItemDamage() {
		return this.itemDamage;
	}

	public void setItemDamage(int par1) {
		this.itemDamage = par1;
	}

	public int getMaxDamage() {
		return Item.itemsList[this.itemID].getMaxDamage();
	}

	public void damageItem(int par1, EntityLiving par2EntityLiving) {
		if (this.isItemStackDamageable()) {
			if (par1 > 0 && par2EntityLiving instanceof EntityPlayer) {
				int var3 = EnchantmentHelper.getUnbreakingModifier(((EntityPlayer)par2EntityLiving).inventory);
				if (var3 > 0 && par2EntityLiving.worldObj.rand.nextInt(var3 + 1) > 0) {
					return;
				}
			}

			this.itemDamage += par1;
			if (this.itemDamage > this.getMaxDamage()) {
				par2EntityLiving.renderBrokenItemStack(this);
				if (par2EntityLiving instanceof EntityPlayer) {
					((EntityPlayer)par2EntityLiving).addStat(StatList.objectBreakStats[this.itemID], 1);
				}

				--this.stackSize;
				if (this.stackSize < 0) {
					this.stackSize = 0;
				}

				this.itemDamage = 0;
			}
		}
	}

	public void hitEntity(EntityLiving par1EntityLiving, EntityPlayer par2EntityPlayer) {
		boolean var3 = Item.itemsList[this.itemID].hitEntity(this, par1EntityLiving, par2EntityPlayer);
		if (var3) {
			par2EntityPlayer.addStat(StatList.objectUseStats[this.itemID], 1);
		}
	}

	public void onDestroyBlock(int par1, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) {
		boolean var6 = Item.itemsList[this.itemID].onBlockDestroyed(this, par1, par2, par3, par4, par5EntityPlayer);
		if (var6) {
			par5EntityPlayer.addStat(StatList.objectUseStats[this.itemID], 1);
		}
	}

	public int getDamageVsEntity(Entity par1Entity) {
		return Item.itemsList[this.itemID].getDamageVsEntity(par1Entity);
	}

	public boolean canHarvestBlock(Block par1Block) {
		return Item.itemsList[this.itemID].canHarvestBlock(par1Block);
	}

	public void onItemDestroyedByUse(EntityPlayer par1EntityPlayer) {}

	public void useItemOnEntity(EntityLiving par1EntityLiving) {
		Item.itemsList[this.itemID].useItemOnEntity(this, par1EntityLiving);
	}

	public ItemStack copy() {
		ItemStack var1 = new ItemStack(this.itemID, this.stackSize, this.itemDamage);
		if (this.stackTagCompound != null) {
			var1.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
			if (!var1.stackTagCompound.equals(this.stackTagCompound)) {
				return var1;
			}
		}

		return var1;
	}

	public static boolean func_46154_a(ItemStack par0ItemStack, ItemStack par1ItemStack) {
		return par0ItemStack == null && par1ItemStack == null?true:(par0ItemStack != null && par1ItemStack != null?(par0ItemStack.stackTagCompound == null && par1ItemStack.stackTagCompound != null?false:par0ItemStack.stackTagCompound == null || par0ItemStack.stackTagCompound.equals(par1ItemStack.stackTagCompound)):false);
	}

	public static boolean areItemStacksEqual(ItemStack par0ItemStack, ItemStack par1ItemStack) {
		return par0ItemStack == null && par1ItemStack == null?true:(par0ItemStack != null && par1ItemStack != null?par0ItemStack.isItemStackEqual(par1ItemStack):false);
	}

	private boolean isItemStackEqual(ItemStack par1ItemStack) {
		return this.stackSize != par1ItemStack.stackSize?false:(this.itemID != par1ItemStack.itemID?false:(this.itemDamage != par1ItemStack.itemDamage?false:(this.stackTagCompound == null && par1ItemStack.stackTagCompound != null?false:this.stackTagCompound == null || this.stackTagCompound.equals(par1ItemStack.stackTagCompound))));
	}

	public boolean isItemEqual(ItemStack par1ItemStack) {
		return this.itemID == par1ItemStack.itemID && this.itemDamage == par1ItemStack.itemDamage;
	}

	public static ItemStack copyItemStack(ItemStack par0ItemStack) {
		return par0ItemStack == null?null:par0ItemStack.copy();
	}

	public String toString() {
		return this.stackSize + "x" + Item.itemsList[this.itemID].getItemName() + "@" + this.itemDamage;
	}

	public void updateAnimation(World par1World, Entity par2Entity, int par3, boolean par4) {
		if (this.animationsToGo > 0) {
			--this.animationsToGo;
		}

		Item.itemsList[this.itemID].onUpdate(this, par1World, par2Entity, par3, par4);
	}

	public void onCrafting(World par1World, EntityPlayer par2EntityPlayer, int par3) {
		par2EntityPlayer.addStat(StatList.objectCraftStats[this.itemID], par3);
		Item.itemsList[this.itemID].onCreated(this, par1World, par2EntityPlayer);
	}

	public boolean isStackEqual(ItemStack par1ItemStack) {
		return this.itemID == par1ItemStack.itemID && this.stackSize == par1ItemStack.stackSize && this.itemDamage == par1ItemStack.itemDamage;
	}

	public int getMaxItemUseDuration() {
		return this.getItem().getMaxItemUseDuration(this);
	}

	public EnumAction getItemUseAction() {
		return this.getItem().getItemUseAction(this);
	}

	public void onPlayerStoppedUsing(World par1World, EntityPlayer par2EntityPlayer, int par3) {
		this.getItem().onPlayerStoppedUsing(this, par1World, par2EntityPlayer, par3);
	}

	public boolean hasTagCompound() {
		return this.stackTagCompound != null;
	}

	public NBTTagCompound getTagCompound() {
		return this.stackTagCompound;
	}

	public NBTTagList getEnchantmentTagList() {
		return this.stackTagCompound == null?null:(NBTTagList)this.stackTagCompound.getTag("ench");
	}

	public void setTagCompound(NBTTagCompound par1NBTTagCompound) {
		this.stackTagCompound = par1NBTTagCompound;
	}

	public List getItemNameandInformation() {
		ArrayList var1 = new ArrayList();
		Item var2 = Item.itemsList[this.itemID];
		var1.add(var2.getItemDisplayName(this));
		var2.addInformation(this, var1);
		if (this.hasTagCompound()) {
			NBTTagList var3 = this.getEnchantmentTagList();
			if (var3 != null) {
				for (int var4 = 0; var4 < var3.tagCount(); ++var4) {
					short var5 = ((NBTTagCompound)var3.tagAt(var4)).getShort("id");
					short var6 = ((NBTTagCompound)var3.tagAt(var4)).getShort("lvl");
					if (Enchantment.enchantmentsList[var5] != null) {
						var1.add(Enchantment.enchantmentsList[var5].getTranslatedName(var6));
					}
				}
			}
		}

		return var1;
	}

	public boolean hasEffect() {
		return this.getItem().hasEffect(this);
	}

	public EnumRarity getRarity() {
		return this.getItem().getRarity(this);
	}

	public boolean isItemEnchantable() {
		return !this.getItem().isItemTool(this)?false:!this.isItemEnchanted();
	}

	public void addEnchantment(Enchantment par1Enchantment, int par2) {
		if (this.stackTagCompound == null) {
			this.setTagCompound(new NBTTagCompound());
		}

		if (!this.stackTagCompound.hasKey("ench")) {
			this.stackTagCompound.setTag("ench", new NBTTagList("ench"));
		}

		NBTTagList var3 = (NBTTagList)this.stackTagCompound.getTag("ench");
		NBTTagCompound var4 = new NBTTagCompound();
		var4.setShort("id", (short)par1Enchantment.effectId);
		var4.setShort("lvl", (short)((byte)par2));
		var3.appendTag(var4);
	}

	public boolean isItemEnchanted() {
		return this.stackTagCompound != null && this.stackTagCompound.hasKey("ench");
	}
}
