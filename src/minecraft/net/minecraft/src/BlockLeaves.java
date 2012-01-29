package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer; //Spout HD
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLeavesBase;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.StatList;
import net.minecraft.src.World;

public class BlockLeaves extends BlockLeavesBase {

	private int baseIndexInPNG;
	int[] adjacentTreeBlocks;

	protected BlockLeaves(int var1, int var2) {
		super(var1, var2, Material.leaves, false);
		this.baseIndexInPNG = var2;
		this.setTickOnLoad(true);
	}

	public int getBlockColor() {
		double var1 = 0.5D;
		double var3 = 1.0D;
		return ColorizerFoliage.getFoliageColor(var1, var3);
	}

	public int getRenderColor(int var1) {
		return (var1 & 1) == 1 ? ColorizerFoliage.getFoliageColorPine() : ((var1 & 2) == 2 ? ColorizerFoliage.getFoliageColorBirch() : ColorizerFoliage.getFoliageColorBasic());
	}

	public int colorMultiplier(IBlockAccess var1, int var2, int var3, int var4) {
		int var5 = var1.getBlockMetadata(var2, var3, var4);
		if ((var5 & 1) == 1) {
			return Colorizer.colorizeBiome(ColorizerFoliage.getFoliageColorPine(), Colorizer.COLOR_MAP_PINE, var1.getWorldChunkManager(), var2, var3, var4); //Spout HD
		}
		else if ((var5 & 2) == 2) {
			return Colorizer.colorizeBiome(ColorizerFoliage.getFoliageColorBirch(), Colorizer.COLOR_MAP_BIRCH, var1.getWorldChunkManager(), var2, var3, var4); //Spout HD
		}
		else {
			int var6 = 0;
			int var7 = 0;
			int var8 = 0;

			for (int var9 = -1; var9 <= 1; ++var9) {
				for (int var10 = -1; var10 <= 1; ++var10) {
					int var11 = var1.getWorldChunkManager().getBiomeGenAt(var2 + var10, var4 + var9).getFoliageColorAtCoords(var1, var2 + var10, var3, var4 + var9);
					var6 += (var11 & 16711680) >> 16;
					var7 += (var11 & '\uff00') >> 8;
					var8 += var11 & 255;
				}
			}

			return (var6 / 9 & 255) << 16 | (var7 / 9 & 255) << 8 | var8 / 9 & 255;
		}
	}

	public void onBlockRemoval(World var1, int var2, int var3, int var4) {
		byte var5 = 1;
		int var6 = var5 + 1;
		if (var1.checkChunksExist(var2 - var6, var3 - var6, var4 - var6, var2 + var6, var3 + var6, var4 + var6)) {
			for (int var7 = -var5; var7 <= var5; ++var7) {
				for (int var8 = -var5; var8 <= var5; ++var8) {
					for (int var9 = -var5; var9 <= var5; ++var9) {
						int var10 = var1.getBlockId(var2 + var7, var3 + var8, var4 + var9);
						if (var10 == Block.leaves.blockID) {
							int var11 = var1.getBlockMetadata(var2 + var7, var3 + var8, var4 + var9);
							var1.setBlockMetadata(var2 + var7, var3 + var8, var4 + var9, var11 | 8);
						}
					}
				}
			}
		}

	}

	public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		if (!var1.multiplayerWorld) {
			int var6 = var1.getBlockMetadata(var2, var3, var4);
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
				if (var1.checkChunksExist(var2 - var8, var3 - var8, var4 - var8, var2 + var8, var3 + var8, var4 + var8)) {
					int var13;
					int var14;
					int var15;
					for (var12 = -var7; var12 <= var7; ++var12) {
						for (var13 = -var7; var13 <= var7; ++var13) {
							for (var14 = -var7; var14 <= var7; ++var14) {
								var15 = var1.getBlockId(var2 + var12, var3 + var13, var4 + var14);
								if (var15 == Block.wood.blockID) {
									this.adjacentTreeBlocks[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = 0;
								}
								else if (var15 == Block.leaves.blockID) {
									this.adjacentTreeBlocks[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -2;
								}
								else {
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
					var1.setBlockMetadata(var2, var3, var4, var6 & -9);
				}
				else {
					this.removeLeaves(var1, var2, var3, var4);
				}
			}

		}
	}

	private void removeLeaves(World var1, int var2, int var3, int var4) {
		this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), 0);
		var1.setBlockWithNotify(var2, var3, var4, 0);
	}

	public int quantityDropped(Random var1) {
		return var1.nextInt(20) == 0 ? 1 : 0;
	}

	public int idDropped(int var1, Random var2, int var3) {
		return Block.sapling.blockID;
	}

	public void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, int var5, float var6, int var7) {
		super.dropBlockAsItemWithChance(var1, var2, var3, var4, var5, var6, var7);
		if (!var1.multiplayerWorld && (var5 & 3) == 0 && var1.rand.nextInt(200) == 0) {
			this.dropBlockAsItem_do(var1, var2, var3, var4, new ItemStack(Item.appleRed, 1, 0));
		}

	}

	public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6) {
		if (!var1.multiplayerWorld && var2.getCurrentEquippedItem() != null && var2.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex) {
			var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
			this.dropBlockAsItem_do(var1, var3, var4, var5, new ItemStack(Block.leaves.blockID, 1, var6 & 3));
		}
		else {
			super.harvestBlock(var1, var2, var3, var4, var5, var6);
		}

	}

	protected int damageDropped(int var1) {
		return var1 & 3;
	}

	public boolean isOpaqueCube() {
		return !this.graphicsLevel;
	}

	public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
		return (var2 & 3) == 1 ? this.blockIndexInTexture + 80 : this.blockIndexInTexture;
	}

	public void setGraphicsLevel(boolean var1) {
		this.graphicsLevel = var1;
		this.blockIndexInTexture = this.baseIndexInPNG + (var1 ? 0 : 1);
	}

	public void onEntityWalking(World var1, int var2, int var3, int var4, Entity var5) {
		super.onEntityWalking(var1, var2, var3, var4, var5);
	}
}
