package net.minecraft.src;

import java.util.List;
import java.util.Random;
// MCPatcher Start
import com.prupe.mcpatcher.mod.ColorizeBlock;
import com.prupe.mcpatcher.mod.Colorizer;
// MCPatcher End

public class BlockLeaves extends BlockLeavesBase {
	public static final String[] LEAF_TYPES = new String[] {"oak", "spruce", "birch", "jungle"};
	public static final String[][] field_94396_b = new String[][] {{"leaves", "leaves_spruce", "leaves", "leaves_jungle"}, {"leaves_opaque", "leaves_spruce_opaque", "leaves_opaque", "leaves_jungle_opaque"}};
	private int field_94394_cP;
	private Icon[][] iconArray = new Icon[2][];
	int[] adjacentTreeBlocks;

	protected BlockLeaves(int par1) {
		super(par1, Material.leaves, false);
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	public int getBlockColor() {
		double var1 = 0.5D;
		double var3 = 1.0D;
		return ColorizerFoliage.getFoliageColor(var1, var3);
	}

	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	public int getRenderColor(int par1) {
		return (par1 & 3) == 1 ? ColorizerFoliage.getFoliageColorPine() : ((par1 & 3) == 2 ? ColorizerFoliage.getFoliageColorBirch() : ColorizerFoliage.getFoliageColorBasic());
	}

	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called when
	 * first determining what to render.
	 */
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

		if ((var5 & 3) == 1) {
			// MCPatcher Start
			return ColorizeBlock.colorizeBiomeWithBlending(ColorizerFoliage.getFoliageColorPine(), Colorizer.COLOR_MAP_PINE, par2, par3, par4);
			// MCPatcher End
		} else if ((var5 & 3) == 2) {
			// MCPatcher Start
			return ColorizeBlock.colorizeBiomeWithBlending(ColorizerFoliage.getFoliageColorBirch(), Colorizer.COLOR_MAP_BIRCH, par2, par3, par4);
			// MCPatcher End
		} else {
			int var6 = 0;
			int var7 = 0;
			int var8 = 0;

			for (int var9 = -1; var9 <= 1; ++var9) {
				for (int var10 = -1; var10 <= 1; ++var10) {
					int var11 = par1IBlockAccess.getBiomeGenForCoords(par2 + var10, par4 + var9).getBiomeFoliageColor();
					var6 += (var11 & 16711680) >> 16;
					var7 += (var11 & 65280) >> 8;
					var8 += var11 & 255;
				}
			}

			return (var6 / 9 & 255) << 16 | (var7 / 9 & 255) << 8 | var8 / 9 & 255;
		}
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update, as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		byte var7 = 1;
		int var8 = var7 + 1;

		if (par1World.checkChunksExist(par2 - var8, par3 - var8, par4 - var8, par2 + var8, par3 + var8, par4 + var8)) {
			for (int var9 = -var7; var9 <= var7; ++var9) {
				for (int var10 = -var7; var10 <= var7; ++var10) {
					for (int var11 = -var7; var11 <= var7; ++var11) {
						int var12 = par1World.getBlockId(par2 + var9, par3 + var10, par4 + var11);

						if (var12 == Block.leaves.blockID) {
							int var13 = par1World.getBlockMetadata(par2 + var9, par3 + var10, par4 + var11);
							par1World.setBlockMetadataWithNotify(par2 + var9, par3 + var10, par4 + var11, var13 | 8, 4);
						}
					}
				}
			}
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		if (!par1World.isRemote) {
			int var6 = par1World.getBlockMetadata(par2, par3, par4);

			if ((var6 & 8) != 0 && (var6 & 4) == 0) {
				byte var7 = 4;
				int var8 = var7 + 1;
				byte var9 = 32;
				int var10 = var9 * var9;
				int var11 = var9 / 2;

				if (this.adjacentTreeBlocks == null) {
					this.adjacentTreeBlocks = new int[var9 * var9 * var9];
				}

				int var12;

				if (par1World.checkChunksExist(par2 - var8, par3 - var8, par4 - var8, par2 + var8, par3 + var8, par4 + var8)) {
					int var13;
					int var14;
					int var15;

					for (var12 = -var7; var12 <= var7; ++var12) {
						for (var13 = -var7; var13 <= var7; ++var13) {
							for (var14 = -var7; var14 <= var7; ++var14) {
								var15 = par1World.getBlockId(par2 + var12, par3 + var13, par4 + var14);

								if (var15 == Block.wood.blockID) {
									this.adjacentTreeBlocks[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = 0;
								} else if (var15 == Block.leaves.blockID) {
									this.adjacentTreeBlocks[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -2;
								} else {
									this.adjacentTreeBlocks[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -1;
								}
							}
						}
					}

					for (var12 = 1; var12 <= 4; ++var12) {
						for (var13 = -var7; var13 <= var7; ++var13) {
							for (var14 = -var7; var14 <= var7; ++var14) {
								for (var15 = -var7; var15 <= var7; ++var15) {
									if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11) * var9 + var15 + var11] == var12 - 1) {
										if (this.adjacentTreeBlocks[(var13 + var11 - 1) * var10 + (var14 + var11) * var9 + var15 + var11] == -2) {
											this.adjacentTreeBlocks[(var13 + var11 - 1) * var10 + (var14 + var11) * var9 + var15 + var11] = var12;
										}

										if (this.adjacentTreeBlocks[(var13 + var11 + 1) * var10 + (var14 + var11) * var9 + var15 + var11] == -2) {
											this.adjacentTreeBlocks[(var13 + var11 + 1) * var10 + (var14 + var11) * var9 + var15 + var11] = var12;
										}

										if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11 - 1) * var9 + var15 + var11] == -2) {
											this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11 - 1) * var9 + var15 + var11] = var12;
										}

										if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11 + 1) * var9 + var15 + var11] == -2) {
											this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11 + 1) * var9 + var15 + var11] = var12;
										}

										if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11) * var9 + (var15 + var11 - 1)] == -2) {
											this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11) * var9 + (var15 + var11 - 1)] = var12;
										}

										if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11) * var9 + var15 + var11 + 1] == -2) {
											this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14 + var11) * var9 + var15 + var11 + 1] = var12;
										}
									}
								}
							}
						}
					}
				}

				var12 = this.adjacentTreeBlocks[var11 * var10 + var11 * var9 + var11];

				if (var12 >= 0) {
					par1World.setBlockMetadataWithNotify(par2, par3, par4, var6 & -9, 4);
				} else {
					this.removeLeaves(par1World, par2, par3, par4);
				}
			}
		}
	}

	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		if (par1World.canLightningStrikeAt(par2, par3 + 1, par4) && !par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && par5Random.nextInt(15) == 1) {
			double var6 = (double)((float)par2 + par5Random.nextFloat());
			double var8 = (double)par3 - 0.05D;
			double var10 = (double)((float)par4 + par5Random.nextFloat());
			par1World.spawnParticle("dripWater", var6, var8, var10, 0.0D, 0.0D, 0.0D);
		}
	}

	private void removeLeaves(World par1World, int par2, int par3, int par4) {
		this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
		par1World.setBlockToAir(par2, par3, par4); 
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(Random par1Random) {
		return par1Random.nextInt(20) == 0 ? 1 : 0;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, Random par2Random, int par3) {
		return Block.sapling.blockID;
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
		if (!par1World.isRemote) {
			int var8 = 20;

			if ((par5 & 3) == 3) {
				var8 = 40;
			}

			if (par7 > 0) {
				var8 -= 2 << par7;

				if (var8 < 10) {
					var8 = 10;
				}
			}

			if (par1World.rand.nextInt(var8) == 0) {
				int var9 = this.idDropped(par5, par1World.rand, par7);
				this.dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(var9, 1, this.damageDropped(par5)));
			}

			var8 = 200;

			if (par7 > 0) {
				var8 -= 10 << par7;

				if (var8 < 40) {
					var8 = 40;
				}
			}

			if ((par5 & 3) == 0 && par1World.rand.nextInt(var8) == 0) {
				this.dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(Item.appleRed, 1, 0));
			}
		}
	}

	/**
	 * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the block
	 * and l is the block's subtype/damage.
	 */
	public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
		if (!par1World.isRemote && par2EntityPlayer.getCurrentEquippedItem() != null && par2EntityPlayer.getCurrentEquippedItem().itemID == Item.shears.itemID) {
			par2EntityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
			this.dropBlockAsItem_do(par1World, par3, par4, par5, new ItemStack(Block.leaves.blockID, 1, par6 & 3));
		} else {
			super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
		}
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	public int damageDropped(int par1) {
		return par1 & 3;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube() {
		return !this.graphicsLevel;
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	public Icon getBlockTextureFromSideAndMetadata(int par1, int par2) {
		return (par2 & 3) == 1 ? this.iconArray[this.field_94394_cP][1] : ((par2 & 3) == 3 ? this.iconArray[this.field_94394_cP][3] : this.iconArray[this.field_94394_cP][0]); 
	}

	/**
	 * Pass true to draw this block using fancy graphics, or false for fast graphics.
	 */
	public void setGraphicsLevel(boolean par1) {
		this.graphicsLevel = par1;
		this.field_94394_cP = par1 ? 0 : 1;
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
		par3List.add(new ItemStack(par1, 1, 3));
	}

	/**
	 * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage and
	 * is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
	 */
	protected ItemStack createStackedBlock(int par1) {
		return new ItemStack(this.blockID, 1, par1 & 3);
	}

	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This is
	 * the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) { 
		for (int var2 = 0; var2 < field_94396_b.length; ++var2) {
			this.iconArray[var2] = new Icon[field_94396_b[var2].length];

			for (int var3 = 0; var3 < field_94396_b[var2].length; ++var3) {
				this.iconArray[var2][var3] = par1IconRegister.registerIcon(field_94396_b[var2][var3]); 
			}
		}
	}
}
