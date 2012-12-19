package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;

public class ItemArmor extends Item {

	/** Holds the 'base' maxDamage that each armorType have. */
	private static final int[] maxDamageArray = new int[] {11, 16, 15, 13};

	/**
	 * Stores the armor type: 0 is helmet, 1 is plate, 2 is legs and 3 is boots
	 */
	public final int armorType;

	/** Holds the amount of damage that the armor reduces at full durability. */
	public final int damageReduceAmount;

	/**
	 * Used on RenderPlayer to select the correspondent armor to be rendered on the player: 0 is cloth, 1 is chain, 2 is
	 * iron, 3 is diamond and 4 is gold.
	 */
	public final int renderIndex;

	/** The EnumArmorMaterial used for this ItemArmor */
	private final EnumArmorMaterial material;

	public ItemArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4) {
		super(par1);
		this.material = par2EnumArmorMaterial;
		this.armorType = par4;
		this.renderIndex = par3;
		this.damageReduceAmount = par2EnumArmorMaterial.getDamageReductionAmount(par4);
		this.setMaxDamage(par2EnumArmorMaterial.getDurability(par4));
		this.maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabCombat);
	}

	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if (par2 > 0) {
			return 16777215;
		} else {
			int var3 = this.getColor(par1ItemStack);

			if (var3 < 0) {
				var3 = 16777215;
			}

			return var3;
		}
	}

	public boolean requiresMultipleRenderPasses() {
		return this.material == EnumArmorMaterial.CLOTH;
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based on material.
	 */
	public int getItemEnchantability() {
		return this.material.getEnchantability();
	}

	/**
	 * Return the armor material for this armor item.
	 */
	public EnumArmorMaterial getArmorMaterial() {
		return this.material;
	}

	/**
	 * Return whether the specified armor ItemStack has a color.
	 */
	public boolean hasColor(ItemStack par1ItemStack) {
		return this.material != EnumArmorMaterial.CLOTH ? false : (!par1ItemStack.hasTagCompound() ? false : (!par1ItemStack.getTagCompound().hasKey("display") ? false : par1ItemStack.getTagCompound().getCompoundTag("display").hasKey("color")));
	}

	/**
	 * Return the color for the specified armor ItemStack.
	 */
	public int getColor(ItemStack par1ItemStack) {
		if (this.material != EnumArmorMaterial.CLOTH) {
			return -1;
		} else {
			NBTTagCompound var2 = par1ItemStack.getTagCompound();

			if (var2 == null) {
				return Colorizer.undyedLeatherColor;
			} else {
				NBTTagCompound var3 = var2.getCompoundTag("display");
				return var3 == null ? Colorizer.undyedLeatherColor : (var3.hasKey("color") ? var3.getInteger("color") : Colorizer.undyedLeatherColor);
			}
		}
	}

	/**
	 * Gets an icon index based on an item's damage value and the given render pass
	 */
	public int getIconFromDamageForRenderPass(int par1, int par2) {
		return par2 == 1 ? this.iconIndex + 144 : super.getIconFromDamageForRenderPass(par1, par2);
	}

	/**
	 * Remove the color from the specified armor ItemStack.
	 */
	public void removeColor(ItemStack par1ItemStack) {
		if (this.material == EnumArmorMaterial.CLOTH) {
			NBTTagCompound var2 = par1ItemStack.getTagCompound();

			if (var2 != null) {
				NBTTagCompound var3 = var2.getCompoundTag("display");

				if (var3.hasKey("color")) {
					var3.removeTag("color");
				}
			}
		}
	}

	public void func_82813_b(ItemStack par1ItemStack, int par2) {
		if (this.material != EnumArmorMaterial.CLOTH) {
			throw new UnsupportedOperationException("Can\'t dye non-leather!");
		} else {
			NBTTagCompound var3 = par1ItemStack.getTagCompound();

			if (var3 == null) {
				var3 = new NBTTagCompound();
				par1ItemStack.setTagCompound(var3);
			}

			NBTTagCompound var4 = var3.getCompoundTag("display");

			if (!var3.hasKey("display")) {
				var3.setCompoundTag("display", var4);
			}

			var4.setInteger("color", par2);
		}
	}

	/**
	 * Return whether this item is repairable in an anvil.
	 */
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return this.material.getArmorCraftingMaterial() == par2ItemStack.itemID ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	/**
	 * Returns the 'max damage' factor array for the armor, each piece of armor have a durability factor (that gets
	 * multiplied by armor material factor)
	 */
	static int[] getMaxDamageArray() {
		return maxDamageArray;
	}
}
