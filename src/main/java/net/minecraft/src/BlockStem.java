package net.minecraft.src;

import java.util.Random;
// MCPatcher Start
import com.prupe.mcpatcher.mod.ColorizeBlock;
// MCPatcher End

public class BlockStem extends BlockFlower {

	/** Defines if it is a Melon or a Pumpkin that the stem is producing. */
	private final Block fruitType;
	private Icon theIcon;

	protected BlockStem(int par1, Block par2Block) {
		super(par1);
		this.fruitType = par2Block;
		this.setTickRandomly(true);
		float var3 = 0.125F;
		this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.25F, 0.5F + var3);
		this.setCreativeTab((CreativeTabs)null);
	}

	/**
	 * Gets passed in the blockID of the block below and supposed to return true if its allowed to grow on the type of
	 * blockID passed in. Args: blockID
	 */
	protected boolean canThisPlantGrowOnThisBlockID(int par1) {
		return par1 == Block.tilledField.blockID;
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		super.updateTick(par1World, par2, par3, par4, par5Random);

		if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9) {
			float var6 = this.getGrowthModifier(par1World, par2, par3, par4);

			if (par5Random.nextInt((int)(25.0F / var6) + 1) == 0) {
				int var7 = par1World.getBlockMetadata(par2, par3, par4);

				if (var7 < 7) {
					++var7;
					par1World.setBlockMetadataWithNotify(par2, par3, par4, var7, 2);
				} else {
					if (par1World.getBlockId(par2 - 1, par3, par4) == this.fruitType.blockID) {
						return;
					}

					if (par1World.getBlockId(par2 + 1, par3, par4) == this.fruitType.blockID) {
						return;
					}

					if (par1World.getBlockId(par2, par3, par4 - 1) == this.fruitType.blockID) {
						return;
					}

					if (par1World.getBlockId(par2, par3, par4 + 1) == this.fruitType.blockID) {
						return;
					}

					int var8 = par5Random.nextInt(4);
					int var9 = par2;
					int var10 = par4;

					if (var8 == 0) {
						var9 = par2 - 1;
					}

					if (var8 == 1) {
						++var9;
					}

					if (var8 == 2) {
						var10 = par4 - 1;
					}

					if (var8 == 3) {
						++var10;
					}

					int var11 = par1World.getBlockId(var9, par3 - 1, var10);

					if (par1World.getBlockId(var9, par3, var10) == 0 && (var11 == Block.tilledField.blockID || var11 == Block.dirt.blockID || var11 == Block.grass.blockID)) {
						par1World.setBlock(var9, par3, var10, this.fruitType.blockID);
					}
				}
			}
		}
	}

	public void fertilizeStem(World par1World, int par2, int par3, int par4) {
		int var5 = par1World.getBlockMetadata(par2, par3, par4) + MathHelper.getRandomIntegerInRange(par1World.rand, 2, 5);

		if (var5 > 7) {
			var5 = 7;
		}

		par1World.setBlockMetadataWithNotify(par2, par3, par4, var5, 2);
	}

	private float getGrowthModifier(World par1World, int par2, int par3, int par4) {
		float var5 = 1.0F;
		int var6 = par1World.getBlockId(par2, par3, par4 - 1);
		int var7 = par1World.getBlockId(par2, par3, par4 + 1);
		int var8 = par1World.getBlockId(par2 - 1, par3, par4);
		int var9 = par1World.getBlockId(par2 + 1, par3, par4);
		int var10 = par1World.getBlockId(par2 - 1, par3, par4 - 1);
		int var11 = par1World.getBlockId(par2 + 1, par3, par4 - 1);
		int var12 = par1World.getBlockId(par2 + 1, par3, par4 + 1);
		int var13 = par1World.getBlockId(par2 - 1, par3, par4 + 1);
		boolean var14 = var8 == this.blockID || var9 == this.blockID;
		boolean var15 = var6 == this.blockID || var7 == this.blockID;
		boolean var16 = var10 == this.blockID || var11 == this.blockID || var12 == this.blockID || var13 == this.blockID;

		for (int var17 = par2 - 1; var17 <= par2 + 1; ++var17) {
			for (int var18 = par4 - 1; var18 <= par4 + 1; ++var18) {
				int var19 = par1World.getBlockId(var17, par3 - 1, var18);
				float var20 = 0.0F;

				if (var19 == Block.tilledField.blockID) {
					var20 = 1.0F;

					if (par1World.getBlockMetadata(var17, par3 - 1, var18) > 0) {
						var20 = 3.0F;
					}
				}

				if (var17 != par2 || var18 != par4) {
					var20 /= 4.0F;
				}

				var5 += var20;
			}
		}

		if (var16 || var14 && var15) {
			var5 /= 2.0F;
		}

		return var5;
	}

	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	public int getRenderColor(int par1) {
		int var2 = par1 * 32;
		int var3 = 255 - par1 * 8;
		int var4 = par1 * 4;
		// MCPatcher Start
		return ColorizeBlock.colorizeStem(var2 << 16 | var3 << 8 | var4, par1);
		// MCPatcher End
	}

	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called when
	 * first determining what to render.
	 */
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		return this.getRenderColor(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		float var1 = 0.125F;
		this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.25F, 0.5F + var1);
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		this.maxY = (double)((float)(par1IBlockAccess.getBlockMetadata(par2, par3, par4) * 2 + 2) / 16.0F);
		float var5 = 0.125F;
		this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var5, 0.5F + var5, (float)this.maxY, 0.5F + var5);
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 19;
	}

	/**
	 * Returns the current state of the stem. Returns -1 if the stem is not fully grown, or a value between 0 and 3 based
	 * on the direction the stem is facing.
	 */
	public int getState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
		return var5 < 7 ? -1 : (par1IBlockAccess.getBlockId(par2 - 1, par3, par4) == this.fruitType.blockID ? 0 : (par1IBlockAccess.getBlockId(par2 + 1, par3, par4) == this.fruitType.blockID ? 1 : (par1IBlockAccess.getBlockId(par2, par3, par4 - 1) == this.fruitType.blockID ? 2 : (par1IBlockAccess.getBlockId(par2, par3, par4 + 1) == this.fruitType.blockID ? 3 : -1))));
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
		super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);

		if (!par1World.isRemote) {
			Item var8 = null;

			if (this.fruitType == Block.pumpkin) {
				var8 = Item.pumpkinSeeds;
			}

			if (this.fruitType == Block.melon) {
				var8 = Item.melonSeeds;
			}

			for (int var9 = 0; var9 < 3; ++var9) {
				if (par1World.rand.nextInt(15) <= par5) {
					this.dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(var8));
				}
			}
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, Random par2Random, int par3) {
		return -1;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(Random par1Random) {
		return 1;
	}

	/**
	 * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
	 */
	public int idPicked(World par1World, int par2, int par3, int par4) {
		return this.fruitType == Block.pumpkin ? Item.pumpkinSeeds.itemID : (this.fruitType == Block.melon ? Item.melonSeeds.itemID : 0);
	}

	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This is
	 * the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
	    this.blockIcon = par1IconRegister.registerIcon("stem_straight");
	    this.theIcon = par1IconRegister.registerIcon("stem_bent"); 
	}

	public Icon func_94368_p() {
		return this.theIcon;
	}
}
