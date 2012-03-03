package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;  //Spout HD
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BlockRedstoneWire extends Block {
	private boolean wiresProvidePower = true;
	private Set blocksNeedingUpdate = new HashSet();

	public BlockRedstoneWire(int par1, int par2) {
		super(par1, par2, Material.circuits);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
	}

	public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
		return this.blockIndexInTexture;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 5;
	}

	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		return 8388608;
	}

	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		return par1World.isBlockNormalCube(par2, par3 - 1, par4) || par1World.getBlockId(par2, par3 - 1, par4) == Block.glowStone.blockID;
	}

	private void updateAndPropagateCurrentStrength(World par1World, int par2, int par3, int par4) {
		this.calculateCurrentChanges(par1World, par2, par3, par4, par2, par3, par4);
		ArrayList var5 = new ArrayList(this.blocksNeedingUpdate);
		this.blocksNeedingUpdate.clear();

		for (int var6 = 0; var6 < var5.size(); ++var6) {
			ChunkPosition var7 = (ChunkPosition)var5.get(var6);
			par1World.notifyBlocksOfNeighborChange(var7.x, var7.y, var7.z, this.blockID);
		}
	}

	private void calculateCurrentChanges(World par1World, int par2, int par3, int par4, int par5, int par6, int par7) {
		int var8 = par1World.getBlockMetadata(par2, par3, par4);
		int var9 = 0;
		this.wiresProvidePower = false;
		boolean var10 = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
		this.wiresProvidePower = true;
		int var11;
		int var12;
		int var13;
		if (var10) {
			var9 = 15;
		} else {
			for (var11 = 0; var11 < 4; ++var11) {
				var12 = par2;
				var13 = par4;
				if (var11 == 0) {
					var12 = par2 - 1;
				}

				if (var11 == 1) {
					++var12;
				}

				if (var11 == 2) {
					var13 = par4 - 1;
				}

				if (var11 == 3) {
					++var13;
				}

				if (var12 != par5 || par3 != par6 || var13 != par7) {
					var9 = this.getMaxCurrentStrength(par1World, var12, par3, var13, var9);
				}

				if (par1World.isBlockNormalCube(var12, par3, var13) && !par1World.isBlockNormalCube(par2, par3 + 1, par4)) {
					if (var12 != par5 || par3 + 1 != par6 || var13 != par7) {
						var9 = this.getMaxCurrentStrength(par1World, var12, par3 + 1, var13, var9);
					}
				} else if (!par1World.isBlockNormalCube(var12, par3, var13) && (var12 != par5 || par3 - 1 != par6 || var13 != par7)) {
					var9 = this.getMaxCurrentStrength(par1World, var12, par3 - 1, var13, var9);
				}
			}

			if (var9 > 0) {
				--var9;
			} else {
				var9 = 0;
			}
		}

		if (var8 != var9) {
			par1World.editingBlocks = true;
			par1World.setBlockMetadataWithNotify(par2, par3, par4, var9);
			par1World.markBlocksDirty(par2, par3, par4, par2, par3, par4);
			par1World.editingBlocks = false;

			for (var11 = 0; var11 < 4; ++var11) {
				var12 = par2;
				var13 = par4;
				int var14 = par3 - 1;
				if (var11 == 0) {
					var12 = par2 - 1;
				}

				if (var11 == 1) {
					++var12;
				}

				if (var11 == 2) {
					var13 = par4 - 1;
				}

				if (var11 == 3) {
					++var13;
				}

				if (par1World.isBlockNormalCube(var12, par3, var13)) {
					var14 += 2;
				}

				boolean var15 = false;
				int var16 = this.getMaxCurrentStrength(par1World, var12, par3, var13, -1);
				var9 = par1World.getBlockMetadata(par2, par3, par4);
				if (var9 > 0) {
					--var9;
				}

				if (var16 >= 0 && var16 != var9) {
					this.calculateCurrentChanges(par1World, var12, par3, var13, par2, par3, par4);
				}

				var16 = this.getMaxCurrentStrength(par1World, var12, var14, var13, -1);
				var9 = par1World.getBlockMetadata(par2, par3, par4);
				if (var9 > 0) {
					--var9;
				}

				if (var16 >= 0 && var16 != var9) {
					this.calculateCurrentChanges(par1World, var12, var14, var13, par2, par3, par4);
				}
			}

			if (var8 < var9 || var9 == 0) {
				this.blocksNeedingUpdate.add(new ChunkPosition(par2, par3, par4));
				this.blocksNeedingUpdate.add(new ChunkPosition(par2 - 1, par3, par4));
				this.blocksNeedingUpdate.add(new ChunkPosition(par2 + 1, par3, par4));
				this.blocksNeedingUpdate.add(new ChunkPosition(par2, par3 - 1, par4));
				this.blocksNeedingUpdate.add(new ChunkPosition(par2, par3 + 1, par4));
				this.blocksNeedingUpdate.add(new ChunkPosition(par2, par3, par4 - 1));
				this.blocksNeedingUpdate.add(new ChunkPosition(par2, par3, par4 + 1));
			}
		}
	}

	private void notifyWireNeighborsOfNeighborChange(World par1World, int par2, int par3, int par4) {
		if (par1World.getBlockId(par2, par3, par4) == this.blockID) {
			par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this.blockID);
		}
	}

	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		super.onBlockAdded(par1World, par2, par3, par4);
		if (!par1World.isRemote) {
			this.updateAndPropagateCurrentStrength(par1World, par2, par3, par4);
			par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);
			this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3, par4);
			this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3, par4);
			this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3, par4 - 1);
			this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3, par4 + 1);
			if (par1World.isBlockNormalCube(par2 - 1, par3, par4)) {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3 + 1, par4);
			} else {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3 - 1, par4);
			}

			if (par1World.isBlockNormalCube(par2 + 1, par3, par4)) {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3 + 1, par4);
			} else {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3 - 1, par4);
			}

			if (par1World.isBlockNormalCube(par2, par3, par4 - 1)) {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 + 1, par4 - 1);
			} else {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 - 1, par4 - 1);
			}

			if (par1World.isBlockNormalCube(par2, par3, par4 + 1)) {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 + 1, par4 + 1);
			} else {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 - 1, par4 + 1);
			}
		}
	}

	public void onBlockRemoval(World par1World, int par2, int par3, int par4) {
		super.onBlockRemoval(par1World, par2, par3, par4);
		if (!par1World.isRemote) {
			par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, this.blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, this.blockID);
			this.updateAndPropagateCurrentStrength(par1World, par2, par3, par4);
			this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3, par4);
			this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3, par4);
			this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3, par4 - 1);
			this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3, par4 + 1);
			if (par1World.isBlockNormalCube(par2 - 1, par3, par4)) {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3 + 1, par4);
			} else {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3 - 1, par4);
			}

			if (par1World.isBlockNormalCube(par2 + 1, par3, par4)) {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3 + 1, par4);
			} else {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3 - 1, par4);
			}

			if (par1World.isBlockNormalCube(par2, par3, par4 - 1)) {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 + 1, par4 - 1);
			} else {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 - 1, par4 - 1);
			}

			if (par1World.isBlockNormalCube(par2, par3, par4 + 1)) {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 + 1, par4 + 1);
			} else {
				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 - 1, par4 + 1);
			}
		}
	}

	public int getMaxCurrentStrength(World par1World, int par2, int par3, int par4, int par5) { //Spout private -> public
		if (par1World.getBlockId(par2, par3, par4) != this.blockID) {
			return par5;
		} else {
			int var6 = par1World.getBlockMetadata(par2, par3, par4);
			return var6 > par5?var6:par5;
		}
	}

	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		if (!par1World.isRemote) {
			int var6 = par1World.getBlockMetadata(par2, par3, par4);
			boolean var7 = this.canPlaceBlockAt(par1World, par2, par3, par4);
			if (!var7) {
				this.dropBlockAsItem(par1World, par2, par3, par4, var6, 0);
				par1World.setBlockWithNotify(par2, par3, par4, 0);
			} else {
				this.updateAndPropagateCurrentStrength(par1World, par2, par3, par4);
			}

			super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
		}
	}

	public int idDropped(int par1, Random par2Random, int par3) {
		return Item.redstone.shiftedIndex;
	}

	public boolean isIndirectlyPoweringTo(World par1World, int par2, int par3, int par4, int par5) {
		return !this.wiresProvidePower?false:this.isPoweringTo(par1World, par2, par3, par4, par5);
	}

	public boolean isPoweringTo(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		if (!this.wiresProvidePower) {
			return false;
		} else if (par1IBlockAccess.getBlockMetadata(par2, par3, par4) == 0) {
			return false;
		} else if (par5 == 1) {
			return true;
		} else {
			boolean var6 = isPoweredOrRepeater(par1IBlockAccess, par2 - 1, par3, par4, 1) || !par1IBlockAccess.isBlockNormalCube(par2 - 1, par3, par4) && isPoweredOrRepeater(par1IBlockAccess, par2 - 1, par3 - 1, par4, -1);
			boolean var7 = isPoweredOrRepeater(par1IBlockAccess, par2 + 1, par3, par4, 3) || !par1IBlockAccess.isBlockNormalCube(par2 + 1, par3, par4) && isPoweredOrRepeater(par1IBlockAccess, par2 + 1, par3 - 1, par4, -1);
			boolean var8 = isPoweredOrRepeater(par1IBlockAccess, par2, par3, par4 - 1, 2) || !par1IBlockAccess.isBlockNormalCube(par2, par3, par4 - 1) && isPoweredOrRepeater(par1IBlockAccess, par2, par3 - 1, par4 - 1, -1);
			boolean var9 = isPoweredOrRepeater(par1IBlockAccess, par2, par3, par4 + 1, 0) || !par1IBlockAccess.isBlockNormalCube(par2, par3, par4 + 1) && isPoweredOrRepeater(par1IBlockAccess, par2, par3 - 1, par4 + 1, -1);
			if (!par1IBlockAccess.isBlockNormalCube(par2, par3 + 1, par4)) {
				if (par1IBlockAccess.isBlockNormalCube(par2 - 1, par3, par4) && isPoweredOrRepeater(par1IBlockAccess, par2 - 1, par3 + 1, par4, -1)) {
					var6 = true;
				}

				if (par1IBlockAccess.isBlockNormalCube(par2 + 1, par3, par4) && isPoweredOrRepeater(par1IBlockAccess, par2 + 1, par3 + 1, par4, -1)) {
					var7 = true;
				}

				if (par1IBlockAccess.isBlockNormalCube(par2, par3, par4 - 1) && isPoweredOrRepeater(par1IBlockAccess, par2, par3 + 1, par4 - 1, -1)) {
					var8 = true;
				}

				if (par1IBlockAccess.isBlockNormalCube(par2, par3, par4 + 1) && isPoweredOrRepeater(par1IBlockAccess, par2, par3 + 1, par4 + 1, -1)) {
					var9 = true;
				}
			}

			return !var8 && !var7 && !var6 && !var9 && par5 >= 2 && par5 <= 5?true:(par5 == 2 && var8 && !var6 && !var7?true:(par5 == 3 && var9 && !var6 && !var7?true:(par5 == 4 && var6 && !var8 && !var9?true:par5 == 5 && var7 && !var8 && !var9)));
		}
	}

	public boolean canProvidePower() {
		return this.wiresProvidePower;
	}

	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		int var6 = par1World.getBlockMetadata(par2, par3, par4);
		if (var6 > 0) {
			double var7 = (double)par2 + 0.5D + ((double)par5Random.nextFloat() - 0.5D) * 0.2D;
			double var9 = (double)((float)par3 + 0.0625F);
			double var11 = (double)par4 + 0.5D + ((double)par5Random.nextFloat() - 0.5D) * 0.2D;
			 //Spout HD start
			float var14;
			float var15;
			float var16;
			if (Colorizer.computeRedstoneWireColor(var6)) {
				var14 = Colorizer.setColor[0];
				var15 = Colorizer.setColor[1];
				var16 = Colorizer.setColor[2];
			} else {
				float var13 = (float)var6 / 15.0F;
				var14 = var13 * 0.6F + 0.4F;
				if (var6 == 0) {
					var14 = 0.0F;
				}

				var15 = var13 * var13 * 0.7F - 0.5F;
				var16 = var13 * var13 * 0.6F - 0.7F;
			}
 			//Spout HD end
			if (var15 < 0.0F) {
				var15 = 0.0F;
			}

			if (var16 < 0.0F) {
				var16 = 0.0F;
			}

			par1World.spawnParticle("reddust", var7, var9, var11, (double)var14, (double)var15, (double)var16);
		}
	}

	public static boolean isPowerProviderOrWire(IBlockAccess par0IBlockAccess, int par1, int par2, int par3, int par4) {
		int var5 = par0IBlockAccess.getBlockId(par1, par2, par3);
		if (var5 == Block.redstoneWire.blockID) {
			return true;
		} else if (var5 == 0) {
			return false;
		} else if (var5 != Block.redstoneRepeaterIdle.blockID && var5 != Block.redstoneRepeaterActive.blockID) {
			return Block.blocksList[var5].canProvidePower() && par4 != -1;
		} else {
			int var6 = par0IBlockAccess.getBlockMetadata(par1, par2, par3);
			return par4 == (var6 & 3) || par4 == Direction.footInvisibleFaceRemap[var6 & 3];
		}
	}

	public static boolean isPoweredOrRepeater(IBlockAccess par0IBlockAccess, int par1, int par2, int par3, int par4) {
		if (isPowerProviderOrWire(par0IBlockAccess, par1, par2, par3, par4)) {
			return true;
		} else {
			int var5 = par0IBlockAccess.getBlockId(par1, par2, par3);
			if (var5 == Block.redstoneRepeaterActive.blockID) {
				int var6 = par0IBlockAccess.getBlockMetadata(par1, par2, par3);
				return par4 == (var6 & 3);
			} else {
				return false;
			}
		}
	}
}
