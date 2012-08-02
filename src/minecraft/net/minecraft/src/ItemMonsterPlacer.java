package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;  // Spout HD

import java.util.Iterator;
import java.util.List;

public class ItemMonsterPlacer extends Item {
	public ItemMonsterPlacer(int par1) {
		super(par1);
		this.setHasSubtypes(true);
		this.func_77637_a(CreativeTabs.field_78026_f);
	}

	public String getItemDisplayName(ItemStack par1ItemStack) {
		String var2 = ("" + StatCollector.translateToLocal(this.getItemName() + ".name")).trim();
		String var3 = EntityList.getStringFromID(par1ItemStack.getItemDamage());

		if (var3 != null) {
			var2 = var2 + " " + StatCollector.translateToLocal("entity." + var3 + ".name");
		}

		return var2;
	}

	public int getColorFromDamage(int par1, int par2) {
		EntityEggInfo var3 = (EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(par1));
		return var3 != null ? (par2 == 0 ? Colorizer.colorizeSpawnerEgg(var3.primaryColor, par1, par2) : Colorizer.colorizeSpawnerEgg(var3.secondaryColor, par1, par2)) : Colorizer.colorizeSpawnerEgg(16777215, par1, par2); //Spout HD
	}

	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	/**
	 * Gets an icon index based on an item's damage value and the given render pass
	 */
	public int getIconFromDamageForRenderPass(int par1, int par2) {
		return par2 > 0 ? super.getIconFromDamageForRenderPass(par1, par2) + 16 : super.getIconFromDamageForRenderPass(par1, par2);
	}

	public boolean func_77648_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		if (par3World.isRemote) {
			return true;
		} else {
			int var11 = par3World.getBlockId(par4, par5, par6);
			par4 += Facing.offsetsXForSide[par7];
			par5 += Facing.offsetsYForSide[par7];
			par6 += Facing.offsetsZForSide[par7];
			double var12 = 0.0D;

			if (par7 == 1 && var11 == Block.fence.blockID || var11 == Block.netherFence.blockID) {
				var12 = 0.5D;
			}

			if (spawnCreature(par3World, par1ItemStack.getItemDamage(), (double)par4 + 0.5D, (double)par5 + var12, (double)par6 + 0.5D) && !par2EntityPlayer.capabilities.isCreativeMode) {
				--par1ItemStack.stackSize;
			}

			return true;
		}
	}

	/**
	 * Spawns the creature specified by the egg's type in the location specified by the last three parameters. Parameters:
	 * world, entityID, x, y, z.
	 */
	public static boolean spawnCreature(World par0World, int par1, double par2, double par4, double par6) {
		if (!EntityList.entityEggs.containsKey(Integer.valueOf(par1))) {
			return false;
		} else {
			Entity var8 = EntityList.createEntityByID(par1, par0World);

			if (var8 != null) {
				var8.setLocationAndAngles(par2, par4, par6, par0World.rand.nextFloat() * 360.0F, 0.0F);

				if (var8 instanceof EntityVillager) {
					EntityVillager var9 = (EntityVillager)var8;
					var9.setProfession(var9.getRNG().nextInt(5));
					par0World.spawnEntityInWorld(var9);
					return true;
				}

				par0World.spawnEntityInWorld(var8);
				((EntityLiving)var8).playLivingSound();
			}

			return var8 != null;
		}
	}

	public void func_77633_a(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		Iterator var4 = EntityList.entityEggs.values().iterator();

		while (var4.hasNext()) {
			EntityEggInfo var5 = (EntityEggInfo)var4.next();
			par3List.add(new ItemStack(par1, 1, var5.spawnedID));
		}
	}
}
