package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.BlockBreakable;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockIce extends BlockBreakable {

	public BlockIce(int var1, int var2) {
		super(var1, var2, Material.ice, false);
		this.slipperiness = 0.98F;
		this.setTickOnLoad(true);
	}

	public int getRenderBlockPass() {
		return 1;
	}

	public boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5) {
		return super.shouldSideBeRendered(var1, var2, var3, var4, 1 - var5);
	}

	public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6) {
		super.harvestBlock(var1, var2, var3, var4, var5, var6);
		Material var7 = var1.getBlockMaterial(var3, var4 - 1, var5);
		if (var7.getIsSolid() || var7.getIsLiquid()) {
			var1.setBlockWithNotify(var3, var4, var5, Block.waterMoving.blockID);
		}

	}

	public int quantityDropped(Random var1) {
		return 0;
	}

	public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		if (var1.getSavedLightValue(EnumSkyBlock.Block, var2, var3, var4) > 11 - Block.lightOpacity[this.blockID]) {
			this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), 0);
			var1.setBlockWithNotify(var2, var3, var4, Block.waterStill.blockID);
		}

	}

	public int getMobilityFlag() {
		return 0;
	}

	protected ItemStack createStackedBlock(int var1) {
		return null;
	}
}
