package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer; //Spout HD
import java.util.Random;

public class BlockLeaves extends BlockLeavesBase {

	private int baseIndexInPNG;
	int[] adjacentTreeBlocks;

	protected BlockLeaves(int par1, int par2) {
		super(par1, par2, Material.leaves, false);
		this.baseIndexInPNG = par2;
		this.setTickRandomly(true);
	}

	public int getBlockColor() {
		double var1 = 0.5D;
		double var3 = 1.0D;
		return ColorizerFoliage.getFoliageColor(var1, var3);
	}

	public int getRenderColor(int par1) {
		return (par1 & 3) == 1?ColorizerFoliage.getFoliageColorPine():((par1 & 3) == 2?ColorizerFoliage.getFoliageColorBirch():ColorizerFoliage.getFoliageColorBasic());
	}

	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
		if ((var5 & 3) == 1) {
			return Colorizer.colorizeBiome(ColorizerFoliage.getFoliageColorPine(), Colorizer.COLOR_MAP_PINE, par2, par3, par4); //Spout 
		} else if ((var5 & 3) == 2) {
			return Colorizer.colorizeBiome(ColorizerFoliage.getFoliageColorBirch(), Colorizer.COLOR_MAP_BIRCH, par2, par3, par4); //Spout
		} else {
			int var6 = 0;
			int var7 = 0;
			int var8 = 0;

			for (int var9 = -1; var9 <= 1; ++var9) {
				for (int var10 = -1; var10 <= 1; ++var10) {
					int var11 = par1IBlockAccess.func_48454_a(par2 + var10, par4 + var9).func_48412_k();
					var6 += (var11 & 16711680) >> 16;
					var7 += (var11 & 65280) >> 8;
					var8 += var11 & 255;
				}
			}

			return (var6 / 9 & 255) << 16 | (var7 / 9 & 255) << 8 | var8 / 9 & 255;
		}
	}

	public void onBlockRemoval(World par1World, int par2, int par3, int par4) {
		byte var5 = 1;
		int var6 = var5 + 1;
		if (par1World.checkChunksExist(par2 - var6, par3 - var6, par4 - var6, par2 + var6, par3 + var6, par4 + var6)) {
			for (int var7 = -var5; var7 <= var5; ++var7) {
				for (int var8 = -var5; var8 <= var5; ++var8) {
					for (int var9 = -var5; var9 <= var5; ++var9) {
						int var10 = par1World.getBlockId(par2 + var7, par3 + var8, par4 + var9);
						if (var10 == Block.leaves.blockID) {
							int var11 = par1World.getBlockMetadata(par2 + var7, par3 + var8, par4 + var9);
							par1World.setBlockMetadata(par2 + var7, par3 + var8, par4 + var9, var11 | 8);
						}
					}
				}
			}
		}
	}

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
					par1World.setBlockMetadata(par2, par3, par4, var6 & -9);
				} else {
					this.removeLeaves(par1World, par2, par3, par4);
				}
			}
		}
	}

	private void removeLeaves(World par1World, int par2, int par3, int par4) {
		this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
		par1World.setBlockWithNotify(par2, par3, par4, 0);
	}

	public int quantityDropped(Random par1Random) {
		return par1Random.nextInt(20) == 0?1:0;
	}

	public int idDropped(int par1, Random par2Random, int par3) {
		return Block.sapling.blockID;
	}

	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
		if (!par1World.isRemote) {
			byte var8 = 20;
			if ((par5 & 3) == 3) {
				var8 = 40;
			}

			if (par1World.rand.nextInt(var8) == 0) {
				int var9 = this.idDropped(par5, par1World.rand, par7);
				this.dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(var9, 1, this.damageDropped(par5)));
			}

			if ((par5 & 3) == 0 && par1World.rand.nextInt(200) == 0) {
				this.dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(Item.appleRed, 1, 0));
			}
		}
	}

	public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
		if (!par1World.isRemote && par2EntityPlayer.getCurrentEquippedItem() != null && par2EntityPlayer.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex) {
			par2EntityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
			this.dropBlockAsItem_do(par1World, par3, par4, par5, new ItemStack(Block.leaves.blockID, 1, par6 & 3));
		} else {
			super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
		}
	}

	protected int damageDropped(int par1) {
		return par1 & 3;
	}

	public boolean isOpaqueCube() {
		return !this.graphicsLevel;
	}

	public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
		return (par2 & 3) == 1?this.blockIndexInTexture + 80:((par2 & 3) == 3?this.blockIndexInTexture + 144:this.blockIndexInTexture);
	}

	public void setGraphicsLevel(boolean par1) {
		this.graphicsLevel = par1;
		this.blockIndexInTexture = this.baseIndexInPNG + (par1?0:1);
	}

	public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		super.onEntityWalking(par1World, par2, par3, par4, par5Entity);
	}
}
