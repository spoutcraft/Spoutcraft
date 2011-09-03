package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockBreakable;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockPortal extends BlockBreakable {

	public BlockPortal(int var1, int var2) {
		super(var1, var2, Material.portal, false);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
		return null;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
		float var5;
		float var6;
		if(var1.getBlockId(var2 - 1, var3, var4) != this.blockID && var1.getBlockId(var2 + 1, var3, var4) != this.blockID) {
			var5 = 0.125F;
			var6 = 0.5F;
			this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
		} else {
			var5 = 0.5F;
			var6 = 0.125F;
			this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
		}

	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean tryToCreatePortal(World var1, int var2, int var3, int var4) {
		byte var5 = 0;
		byte var6 = 0;
		if(var1.getBlockId(var2 - 1, var3, var4) == Block.obsidian.blockID || var1.getBlockId(var2 + 1, var3, var4) == Block.obsidian.blockID) {
			var5 = 1;
		}

		if(var1.getBlockId(var2, var3, var4 - 1) == Block.obsidian.blockID || var1.getBlockId(var2, var3, var4 + 1) == Block.obsidian.blockID) {
			var6 = 1;
		}

		if(var5 == var6) {
			return false;
		} else {
			if(var1.getBlockId(var2 - var5, var3, var4 - var6) == 0) {
				var2 -= var5;
				var4 -= var6;
			}

			int var7;
			int var8;
			for(var7 = -1; var7 <= 2; ++var7) {
				for(var8 = -1; var8 <= 3; ++var8) {
					boolean var9 = var7 == -1 || var7 == 2 || var8 == -1 || var8 == 3;
					if(var7 != -1 && var7 != 2 || var8 != -1 && var8 != 3) {
						int var10 = var1.getBlockId(var2 + var5 * var7, var3 + var8, var4 + var6 * var7);
						if(var9) {
							if(var10 != Block.obsidian.blockID) {
								return false;
							}
						} else if(var10 != 0 && var10 != Block.fire.blockID) {
							return false;
						}
					}
				}
			}

			var1.editingBlocks = true;

			for(var7 = 0; var7 < 2; ++var7) {
				for(var8 = 0; var8 < 3; ++var8) {
					var1.setBlockWithNotify(var2 + var5 * var7, var3 + var8, var4 + var6 * var7, Block.portal.blockID);
				}
			}

			var1.editingBlocks = false;
			return true;
		}
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		byte var6 = 0;
		byte var7 = 1;
		if(var1.getBlockId(var2 - 1, var3, var4) == this.blockID || var1.getBlockId(var2 + 1, var3, var4) == this.blockID) {
			var6 = 1;
			var7 = 0;
		}

		int var8;
		for(var8 = var3; var1.getBlockId(var2, var8 - 1, var4) == this.blockID; --var8) {
			;
		}

		if(var1.getBlockId(var2, var8 - 1, var4) != Block.obsidian.blockID) {
			var1.setBlockWithNotify(var2, var3, var4, 0);
		} else {
			int var9;
			for(var9 = 1; var9 < 4 && var1.getBlockId(var2, var8 + var9, var4) == this.blockID; ++var9) {
				;
			}

			if(var9 == 3 && var1.getBlockId(var2, var8 + var9, var4) == Block.obsidian.blockID) {
				boolean var10 = var1.getBlockId(var2 - 1, var3, var4) == this.blockID || var1.getBlockId(var2 + 1, var3, var4) == this.blockID;
				boolean var11 = var1.getBlockId(var2, var3, var4 - 1) == this.blockID || var1.getBlockId(var2, var3, var4 + 1) == this.blockID;
				if(var10 && var11) {
					var1.setBlockWithNotify(var2, var3, var4, 0);
				} else if((var1.getBlockId(var2 + var6, var3, var4 + var7) != Block.obsidian.blockID || var1.getBlockId(var2 - var6, var3, var4 - var7) != this.blockID) && (var1.getBlockId(var2 - var6, var3, var4 - var7) != Block.obsidian.blockID || var1.getBlockId(var2 + var6, var3, var4 + var7) != this.blockID)) {
					var1.setBlockWithNotify(var2, var3, var4, 0);
				}
			} else {
				var1.setBlockWithNotify(var2, var3, var4, 0);
			}
		}
	}

	public boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5) {
		if(var1.getBlockId(var2, var3, var4) == this.blockID) {
			return false;
		} else {
			boolean var6 = var1.getBlockId(var2 - 1, var3, var4) == this.blockID && var1.getBlockId(var2 - 2, var3, var4) != this.blockID;
			boolean var7 = var1.getBlockId(var2 + 1, var3, var4) == this.blockID && var1.getBlockId(var2 + 2, var3, var4) != this.blockID;
			boolean var8 = var1.getBlockId(var2, var3, var4 - 1) == this.blockID && var1.getBlockId(var2, var3, var4 - 2) != this.blockID;
			boolean var9 = var1.getBlockId(var2, var3, var4 + 1) == this.blockID && var1.getBlockId(var2, var3, var4 + 2) != this.blockID;
			boolean var10 = var6 || var7;
			boolean var11 = var8 || var9;
			return var10 && var5 == 4?true:(var10 && var5 == 5?true:(var11 && var5 == 2?true:var11 && var5 == 3));
		}
	}

	public int quantityDropped(Random var1) {
		return 0;
	}

	public int getRenderBlockPass() {
		return 2;
	}

	public void onEntityCollidedWithBlock(World var1, int var2, int var3, int var4, Entity var5) {
		if(var5.ridingEntity == null && var5.riddenByEntity == null) {
			var5.setInPortal();
		}

	}

	public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
		if(var5.nextInt(100) == 0) {
			var1.playSoundEffect((double)var2 + 0.5D, (double)var3 + 0.5D, (double)var4 + 0.5D, "portal.portal", 1.0F, var5.nextFloat() * 0.4F + 0.8F);
		}

		for(int var6 = 0; var6 < 4; ++var6) {
			double var7 = (double)((float)var2 + var5.nextFloat());
			double var9 = (double)((float)var3 + var5.nextFloat());
			double var11 = (double)((float)var4 + var5.nextFloat());
			double var13 = 0.0D;
			double var15 = 0.0D;
			double var17 = 0.0D;
			int var19 = var5.nextInt(2) * 2 - 1;
			var13 = ((double)var5.nextFloat() - 0.5D) * 0.5D;
			var15 = ((double)var5.nextFloat() - 0.5D) * 0.5D;
			var17 = ((double)var5.nextFloat() - 0.5D) * 0.5D;
			if(var1.getBlockId(var2 - 1, var3, var4) != this.blockID && var1.getBlockId(var2 + 1, var3, var4) != this.blockID) {
				var7 = (double)var2 + 0.5D + 0.25D * (double)var19;
				var13 = (double)(var5.nextFloat() * 2.0F * (float)var19);
			} else {
				var11 = (double)var4 + 0.5D + 0.25D * (double)var19;
				var17 = (double)(var5.nextFloat() * 2.0F * (float)var19);
			}

			var1.spawnParticle("portal", var7, var9, var11, var13, var15, var17);
		}

	}
}
