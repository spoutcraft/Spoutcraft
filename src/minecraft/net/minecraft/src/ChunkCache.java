/*
 * Serves no purpose. Just no.

package net.minecraft.src;

import net.minecraft.src.Block;
import net.minecraft.src.Chunk;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.WorldChunkManager;

public class ChunkCache implements IBlockAccess {

	private int chunkX;
	private int chunkZ;
	private Chunk[][] chunkArray;
	private World worldObj;

	public ChunkCache(World var1, int var2, int var3, int var4, int var5, int var6, int var7) {
		this.worldObj = var1;
		this.chunkX = var2 >> 4;
		this.chunkZ = var4 >> 4;
		int var8 = var5 >> 4;
		int var9 = var7 >> 4;
		this.chunkArray = new Chunk[var8 - this.chunkX + 1][var9 - this.chunkZ + 1];

		for (int var10 = this.chunkX; var10 <= var8; ++var10) {
			for (int var11 = this.chunkZ; var11 <= var9; ++var11) {
				this.chunkArray[var10 - this.chunkX][var11 - this.chunkZ] = var1.getChunkFromChunkCoords(var10, var11);
			}
		}

	}

	public int getBlockId(int var1, int var2, int var3) {
		if (var2 < 0) {
			return 0;
		}
		else if (var2 >= this.worldObj.worldHeight) {
			return 0;
		}
		else {
			int var4 = (var1 >> 4) - this.chunkX;
			int var5 = (var3 >> 4) - this.chunkZ;
			if (var4 >= 0 && var4 < this.chunkArray.length && var5 >= 0 && var5 < this.chunkArray[var4].length) {
				Chunk var6 = this.chunkArray[var4][var5];
				return var6 == null ? 0 : var6.getBlockID(var1 & 15, var2, var3 & 15);
			}
			else {
				return 0;
			}
		}
	}

	public TileEntity getBlockTileEntity(int var1, int var2, int var3) {
		int var4 = (var1 >> 4) - this.chunkX;
		int var5 = (var3 >> 4) - this.chunkZ;
		return this.chunkArray[var4][var5].getChunkBlockTileEntity(var1 & 15, var2, var3 & 15);
	}

	public float getBrightness(int var1, int var2, int var3, int var4) {
		int var5 = this.getLightValue(var1, var2, var3);
		if (var5 < var4) {
			var5 = var4;
		}

		return this.worldObj.worldProvider.lightBrightnessTable[var5];
	}

	public int getLightBrightnessForSkyBlocks(int var1, int var2, int var3, int var4) {
		int var5 = this.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, var1, var2, var3);
		int var6 = this.getSkyBlockTypeBrightness(EnumSkyBlock.Block, var1, var2, var3);
		if (var6 < var4) {
			var6 = var4;
		}

		return var5 << 20 | var6 << 4;
	}

	public float getLightBrightness(int var1, int var2, int var3) {
		return this.worldObj.worldProvider.lightBrightnessTable[this.getLightValue(var1, var2, var3)];
	}

	public int getLightValue(int var1, int var2, int var3) {
		return this.getLightValueExt(var1, var2, var3, true);
	}

	public int getLightValueExt(int var1, int var2, int var3, boolean var4) {
		if (var1 >= -30000000 && var3 >= -30000000 && var1 < 30000000 && var3 <= 30000000) {
			int var5;
			int var6;
			if (var4) {
				var5 = this.getBlockId(var1, var2, var3);
				if (var5 == Block.stairSingle.blockID || var5 == Block.tilledField.blockID || var5 == Block.stairCompactPlanks.blockID || var5 == Block.stairCompactCobblestone.blockID) {
					var6 = this.getLightValueExt(var1, var2 + 1, var3, false);
					int var7 = this.getLightValueExt(var1 + 1, var2, var3, false);
					int var8 = this.getLightValueExt(var1 - 1, var2, var3, false);
					int var9 = this.getLightValueExt(var1, var2, var3 + 1, false);
					int var10 = this.getLightValueExt(var1, var2, var3 - 1, false);
					if (var7 > var6) {
						var6 = var7;
					}

					if (var8 > var6) {
						var6 = var8;
					}

					if (var9 > var6) {
						var6 = var9;
					}

					if (var10 > var6) {
						var6 = var10;
					}

					return var6;
				}
			}

			if (var2 < 0) {
				return 0;
			}
			else if (var2 >= this.worldObj.worldHeight) {
				var5 = 15 - this.worldObj.skylightSubtracted;
				if (var5 < 0) {
					var5 = 0;
				}

				return var5;
			}
			else {
				var5 = (var1 >> 4) - this.chunkX;
				var6 = (var3 >> 4) - this.chunkZ;
				return this.chunkArray[var5][var6].getBlockLightValue(var1 & 15, var2, var3 & 15, this.worldObj.skylightSubtracted);
			}
		}
		else {
			return 15;
		}
	}

	public int getBlockMetadata(int var1, int var2, int var3) {
		if (var2 < 0) {
			return 0;
		}
		else if (var2 >= this.worldObj.worldHeight) {
			return 0;
		}
		else {
			int var4 = (var1 >> 4) - this.chunkX;
			int var5 = (var3 >> 4) - this.chunkZ;
			return this.chunkArray[var4][var5].getBlockMetadata(var1 & 15, var2, var3 & 15);
		}
	}

	public Material getBlockMaterial(int var1, int var2, int var3) {
		int var4 = this.getBlockId(var1, var2, var3);
		return var4 == 0 ? Material.air : Block.blocksList[var4].blockMaterial;
	}

	public WorldChunkManager getWorldChunkManager() {
		return this.worldObj.getWorldChunkManager();
	}

	public boolean isBlockOpaqueCube(int var1, int var2, int var3) {
		Block var4 = Block.blocksList[this.getBlockId(var1, var2, var3)];
		return var4 == null ? false : var4.isOpaqueCube();
	}

	public boolean isBlockNormalCube(int var1, int var2, int var3) {
		Block var4 = Block.blocksList[this.getBlockId(var1, var2, var3)];
		return var4 == null ? false : var4.blockMaterial.getIsSolid() && var4.renderAsNormalBlock();
	}

	public boolean isAirBlock(int var1, int var2, int var3) {
		Block var4 = Block.blocksList[this.getBlockId(var1, var2, var3)];
		return var4 == null;
	}

	public int getSkyBlockTypeBrightness(EnumSkyBlock var1, int var2, int var3, int var4) {
		if (var3 < 0) {
			var3 = 0;
		}

		if (var3 >= this.worldObj.worldHeight) {
			var3 = this.worldObj.worldHeight - 1;
		}

		if (var3 >= 0 && var3 < this.worldObj.worldHeight && var2 >= -30000000 && var4 >= -30000000 && var2 < 30000000 && var4 <= 30000000) {
			int var5;
			int var6;
			if (Block.useNeighborBrightness[this.getBlockId(var2, var3, var4)]) {
				var5 = this.getSpecialBlockBrightness(var1, var2, var3 + 1, var4);
				var6 = this.getSpecialBlockBrightness(var1, var2 + 1, var3, var4);
				int var7 = this.getSpecialBlockBrightness(var1, var2 - 1, var3, var4);
				int var8 = this.getSpecialBlockBrightness(var1, var2, var3, var4 + 1);
				int var9 = this.getSpecialBlockBrightness(var1, var2, var3, var4 - 1);
				if (var6 > var5) {
					var5 = var6;
				}

				if (var7 > var5) {
					var5 = var7;
				}

				if (var8 > var5) {
					var5 = var8;
				}

				if (var9 > var5) {
					var5 = var9;
				}

				return var5;
			}
			else {
				var5 = (var2 >> 4) - this.chunkX;
				var6 = (var4 >> 4) - this.chunkZ;
				return this.chunkArray[var5][var6].getSavedLightValue(var1, var2 & 15, var3, var4 & 15);
			}
		}
		else {
			return var1.defaultLightValue;
		}
	}

	public int getSpecialBlockBrightness(EnumSkyBlock var1, int var2, int var3, int var4) {
		if (var3 < 0) {
			var3 = 0;
		}

		if (var3 >= this.worldObj.worldHeight) {
			var3 = this.worldObj.worldHeight - 1;
		}

		if (var3 >= 0 && var3 < this.worldObj.worldHeight && var2 >= -30000000 && var4 >= -30000000 && var2 < 30000000 && var4 <= 30000000) {
			int var5 = (var2 >> 4) - this.chunkX;
			int var6 = (var4 >> 4) - this.chunkZ;
			return this.chunkArray[var5][var6].getSavedLightValue(var1, var2 & 15, var3, var4 & 15);
		}
		else {
			return var1.defaultLightValue;
		}
	}

	public int getWorldHeight() {
		return this.worldObj.worldHeight;
	}
}
*/
