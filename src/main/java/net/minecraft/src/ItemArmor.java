package net.minecraft.src;

// MCPatcher Start
import com.prupe.mcpatcher.mod.ColorizeEntity;
// MCPatcher End

public class ItemArmor extends Item {

	/** Holds the 'base' maxDamage that each armorType have. */
	private static final int[] maxDamageArray = new int[] {11, 16, 15, 13};
	private static final String[] field_94606_cu = new String[] {"helmetCloth_overlay", "chestplateCloth_overlay", "leggingsCloth_overlay", "bootsCloth_overlay"};
	public static final String[] field_94603_a = new String[] {"slot_empty_helmet", "slot_empty_chestplate", "slot_empty_leggings", "slot_empty_boots"};
	private static final IBehaviorDispenseItem field_96605_cw = new BehaviorDispenseArmor();

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
	private Icon field_94605_cw;
	private Icon field_94604_cx;

	public ItemArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4) {
		super(par1);
		this.material = par2EnumArmorMaterial;
		this.armorType = par4;
		this.renderIndex = par3;
		this.damageReduceAmount = par2EnumArmorMaterial.getDamageReductionAmount(par4);
		this.setMaxDamage(par2EnumArmorMaterial.getDurability(par4));
		this.maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabCombat);
		BlockDispenser.dispenseBehaviorRegistry.putObject(this, field_96605_cw);
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
				// MCPatcher Start
				return ColorizeEntity.undyedLeatherColor;
				// MCPatcher End
			} else {
				NBTTagCompound var3 = var2.getCompoundTag("display");
				// MCPatcher Start
				return var3 == null ? ColorizeEntity.undyedLeatherColor : (var3.hasKey("color") ? var3.getInteger("color") : ColorizeEntity.undyedLeatherColor);
				// MCPatcher End
			}
		}
	}

	/**
	 * Gets an icon index based on an item's damage value and the given render pass
	 */
	public Icon getIconFromDamageForRenderPass(int par1, int par2) {
		return par2 == 1 ? this.field_94605_cw : super.getIconFromDamageForRenderPass(par1, par2);
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

	public void registerIcons(IconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);

		if (this.material == EnumArmorMaterial.CLOTH) {
			this.field_94605_cw = par1IconRegister.registerIcon(field_94606_cu[this.armorType]);
		}

		this.field_94604_cx = par1IconRegister.registerIcon(field_94603_a[this.armorType]);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		int var4 = EntityLiving.getArmorPosition(par1ItemStack) - 1;
		ItemStack var5 = par3EntityPlayer.getCurrentArmor(var4);

		if (var5 == null) {
			par3EntityPlayer.setCurrentItemOrArmor(var4, par1ItemStack.copy());
			par1ItemStack.stackSize = 0;
		}

		return par1ItemStack;
	}

	public static Icon func_94602_b(int par0) {
		switch (par0) {
			case 0:
				return Item.helmetDiamond.field_94604_cx;

			case 1:
				return Item.plateDiamond.field_94604_cx;

			case 2:
				return Item.legsDiamond.field_94604_cx;

			case 3:
				return Item.bootsDiamond.field_94604_cx;

			default:
				return null;
		}
	}

	/**
	 * Returns the 'max damage' factor array for the armor, each piece of armor have a durability factor (that gets
	 * multiplied by armor material factor)
	 */
	static int[] getMaxDamageArray() {
		return maxDamageArray;
	}
}
