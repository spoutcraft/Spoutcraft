package net.minecraft.src;

import java.util.List;
// MCPatcher Start
import com.prupe.mcpatcher.mod.ColorizeBlock;
// MCPatcher End

public class BlockLilyPad extends BlockFlower {
	protected BlockLilyPad(int par1) {
		super(par1);
		float var2 = 0.5F;
		float var3 = 0.015625F;
		this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var3, 0.5F + var2);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 23;
	}

	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the mask.)
	 * Parameters: World, X, Y, Z, mask, list, colliding entity
	 */
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
		if (par7Entity == null || !(par7Entity instanceof EntityBoat)) {
			super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		}
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
	 * cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return AxisAlignedBB.getAABBPool().getAABB((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY, (double)par4 + this.maxZ);
	}

	public int getBlockColor() {
		// MCPatcher Start
		return ColorizeBlock.getLilyPadColor();
		// MCPatcher End
	}

	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	public int getRenderColor(int par1) {
		// MCPatcher Start
		return ColorizeBlock.getLilyPadColor();
		// MCPatcher End
	}

	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called when
	 * first determining what to render.
	 */
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		// MCPatcher Start
		return ColorizeBlock.getLilyPadColor();
		// MCPatcher End
	}

	/**
	 * Gets passed in the blockID of the block below and supposed to return true if its allowed to grow on the type of
	 * blockID passed in. Args: blockID
	 */
	protected boolean canThisPlantGrowOnThisBlockID(int par1) {
		return par1 == Block.waterStill.blockID;
	}

	/**
	 * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
	 */
	public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
		return par3 >= 0 && par3 < 256 ? par1World.getBlockMaterial(par2, par3 - 1, par4) == Material.water && par1World.getBlockMetadata(par2, par3 - 1, par4) == 0 : false;
	}
}
