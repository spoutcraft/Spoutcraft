package net.minecraft.src;

import java.util.List;

public class BlockLilyPad extends BlockFlower {
	protected BlockLilyPad(int par1, int par2) {
		super(par1, par2);
		float var3 = 0.5F;
		float var4 = 0.015625F;
		this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var4, 0.5F + var3);
		this.func_71849_a(CreativeTabs.field_78031_c);
	}

	public int getRenderType() {
		return 23;
	}

	public void func_71871_a(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
		if (par7Entity == null || !(par7Entity instanceof EntityBoat)) {
			super.func_71871_a(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		}
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return AxisAlignedBB.func_72332_a().func_72299_a((double) par2 + this.minX, (double) par3 + this.minY, (double) par4 + this.minZ, (double) par2 + this.maxX, (double) par3 + this.maxY, (double) par4 + this.maxZ);
	}

	public int getBlockColor() {
		return 2129968;
	}

	public int getRenderColor(int par1) {
		return 2129968;
	}

	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		return 2129968;
	}

	protected boolean canThisPlantGrowOnThisBlockID(int par1) {
		return par1 == Block.waterStill.blockID;
	}

	public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
		return par3 >= 0 && par3 < 256 ? par1World.getBlockMaterial(par2, par3 - 1, par4) == Material.water && par1World.getBlockMetadata(par2, par3 - 1, par4) == 0 : false;
	}
}
