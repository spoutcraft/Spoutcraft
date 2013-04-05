package net.minecraft.src;

import com.prupe.mcpatcher.mod.ColorizeItem;
import java.util.Iterator;
import java.util.List;

public class ItemMonsterPlacer extends Item {
	private Icon theIcon;

	public ItemMonsterPlacer(int par1) {
		super(par1);
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	public String getItemDisplayName(ItemStack par1ItemStack) {
		String var2 = ("" + StatCollector.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
		String var3 = EntityList.getStringFromID(par1ItemStack.getItemDamage());

		if (var3 != null) {
			var2 = var2 + " " + StatCollector.translateToLocal("entity." + var3 + ".name");
		}

		return var2;
	}

	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		EntityEggInfo var3 = (EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(par1ItemStack.getItemDamage()));
		// MCPatcher Start
		return var3 != null ? (par2 == 0 ? ColorizeItem.colorizeSpawnerEgg(var3.primaryColor, par1ItemStack.getItemDamage(), par2) : ColorizeItem.colorizeSpawnerEgg(var3.secondaryColor, par1ItemStack.getItemDamage(), par2)) : ColorizeItem.colorizeSpawnerEgg(16777215, par1ItemStack.getItemDamage(), par2);
		// MCPatcher End
	}

	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	/**
	 * Gets an icon index based on an item's damage value and the given render pass
	 */
	public Icon getIconFromDamageForRenderPass(int par1, int par2) {
		return par2 > 0 ? this.theIcon : super.getIconFromDamageForRenderPass(par1, par2);
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		if (par3World.isRemote) {
			return true;
		} else {
			int var11 = par3World.getBlockId(par4, par5, par6);
			par4 += Facing.offsetsXForSide[par7];
			par5 += Facing.offsetsYForSide[par7];
			par6 += Facing.offsetsZForSide[par7];
			double var12 = 0.0D;

			if (par7 == 1 && Block.blocksList[var11] != null && Block.blocksList[var11].getRenderType() == 11) {
				var12 = 0.5D;
			}

			Entity var14 = spawnCreature(par3World, par1ItemStack.getItemDamage(), (double)par4 + 0.5D, (double)par5 + var12, (double)par6 + 0.5D);

			if (var14 != null) {
				if (var14 instanceof EntityLiving && par1ItemStack.hasDisplayName()) {
					((EntityLiving)var14).func_94058_c(par1ItemStack.getDisplayName());
				}

				if (!par2EntityPlayer.capabilities.isCreativeMode) {
					--par1ItemStack.stackSize;
				}
			}

			return true;
		}
	}

	/**
	 * Spawns the creature specified by the egg's type in the location specified by the last three parameters. Parameters:
	 * world, entityID, x, y, z.
	 */
	public static Entity spawnCreature(World par0World, int par1, double par2, double par4, double par6) {
		if (!EntityList.entityEggs.containsKey(Integer.valueOf(par1))) {
			return null;
		} else {
			Entity var8 = null;

			for (int var9 = 0; var9 < 1; ++var9) {
				var8 = EntityList.createEntityByID(par1, par0World);

				if (var8 != null && var8 instanceof EntityLiving) {
					EntityLiving var10 = (EntityLiving)var8;
					var8.setLocationAndAngles(par2, par4, par6, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
					var10.rotationYawHead = var10.rotationYaw;
					var10.renderYawOffset = var10.rotationYaw;
					var10.initCreature();
					par0World.spawnEntityInWorld(var8);
					var10.playLivingSound();
				}
			}

			return var8;
		}
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		Iterator var4 = EntityList.entityEggs.values().iterator();

		while (var4.hasNext()) {
			EntityEggInfo var5 = (EntityEggInfo)var4.next();
			par3List.add(new ItemStack(par1, 1, var5.spawnedID));
		}
	}

	public void updateIcons(IconRegister par1IconRegister) {
		super.updateIcons(par1IconRegister);
		this.theIcon = par1IconRegister.registerIcon("monsterPlacer_overlay");
	}
}
