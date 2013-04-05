package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BlockPane extends Block {

	/**
	 * Holds the texture index of the side of the pane (the thin lateral side)
	 */
	private final String sideTextureIndex;

	/**
	 * If this field is true, the pane block drops itself when destroyed (like the iron fences), otherwise, it's just
	 * destroyed (like glass panes)
	 */
	private final boolean canDropItself;
	private final String field_94402_c;
	private Icon theIcon;

	protected BlockPane(int par1, String par2Str, String par3Str, Material par4Material, boolean par5) {
		super(par1, par4Material);
		this.sideTextureIndex = par3Str;
		this.canDropItself = par5;
		this.field_94402_c = par2Str;
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, Random par2Random, int par3) {
		return !this.canDropItself ? 0 : super.idDropped(par1, par2Random, par3);
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 18;
	}

	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	 * coordinates.  Args: blockAccess, x, y, z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		int var6 = par1IBlockAccess.getBlockId(par2, par3, par4);
		return var6 == this.blockID ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
	}

	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the mask.)
	 * Parameters: World, X, Y, Z, mask, list, colliding entity
	 */
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
		boolean var8 = this.canThisPaneConnectToThisBlockID(par1World.getBlockId(par2, par3, par4 - 1));
		boolean var9 = this.canThisPaneConnectToThisBlockID(par1World.getBlockId(par2, par3, par4 + 1));
		boolean var10 = this.canThisPaneConnectToThisBlockID(par1World.getBlockId(par2 - 1, par3, par4));
		boolean var11 = this.canThisPaneConnectToThisBlockID(par1World.getBlockId(par2 + 1, par3, par4));

		if ((!var10 || !var11) && (var10 || var11 || var8 || var9)) {
			if (var10 && !var11) {
				this.setBlockBounds(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
				super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
			} else if (!var10 && var11) {
				this.setBlockBounds(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
				super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
			}
		} else {
			this.setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
			super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		}

		if ((!var8 || !var9) && (var10 || var11 || var8 || var9)) {
			if (var8 && !var9) {
				this.setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
				super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
			} else if (!var8 && var9) {
				this.setBlockBounds(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
				super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
			}
		} else {
			this.setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
			super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		}
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		float var5 = 0.4375F;
		float var6 = 0.5625F;
		float var7 = 0.4375F;
		float var8 = 0.5625F;
		boolean var9 = this.canThisPaneConnectToThisBlockID(par1IBlockAccess.getBlockId(par2, par3, par4 - 1));
		boolean var10 = this.canThisPaneConnectToThisBlockID(par1IBlockAccess.getBlockId(par2, par3, par4 + 1));
		boolean var11 = this.canThisPaneConnectToThisBlockID(par1IBlockAccess.getBlockId(par2 - 1, par3, par4));
		boolean var12 = this.canThisPaneConnectToThisBlockID(par1IBlockAccess.getBlockId(par2 + 1, par3, par4));

		if ((!var11 || !var12) && (var11 || var12 || var9 || var10)) {
			if (var11 && !var12) {
				var5 = 0.0F;
			} else if (!var11 && var12) {
				var6 = 1.0F;
			}
		} else {
			var5 = 0.0F;
			var6 = 1.0F;
		}

		if ((!var9 || !var10) && (var11 || var12 || var9 || var10)) {
			if (var9 && !var10) {
				var7 = 0.0F;
			} else if (!var9 && var10) {
				var8 = 1.0F;
			}
		} else {
			var7 = 0.0F;
			var8 = 1.0F;
		}

		this.setBlockBounds(var5, 0.0F, var7, var6, 1.0F, var8);
	}

	/**
	 * Returns the texture index of the thin side of the pane.
	 */
	public Icon getSideTextureIndex() {
		return this.theIcon;
	}

	/**
	 * Gets passed in the blockID of the block adjacent and supposed to return true if its allowed to connect to the type
	 * of blockID passed in. Args: blockID
	 */
	public final boolean canThisPaneConnectToThisBlockID(int par1) {
		// Spout Start - Add cloth and stoneBrick IDs
		return Block.opaqueCubeLookup[par1] || par1 == this.blockID || par1 == Block.glass.blockID || par1 == Block.cloth.blockID || par1 == Block.stoneBrick.blockID;
		// Spout End
	}

	/**
	 * Return true if a player with Silk Touch can harvest this block directly, and not its normal drops.
	 */
	protected boolean canSilkHarvest() {
		return true;
	}

	/**
	 * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage and
	 * is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
	 */
	protected ItemStack createStackedBlock(int par1) {
		return new ItemStack(this.blockID, 1, par1);
	}

	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This is
	 * the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon(this.field_94402_c);
		this.theIcon = par1IconRegister.registerIcon(this.sideTextureIndex); 
	}
}
