package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
// MCPatcher Start
import com.prupe.mcpatcher.mod.ColorizeBlock;
import com.prupe.mcpatcher.mod.Colorizer;
// MCPatcher End

public class BlockRedstoneWire extends Block {

	/**
	 * When false, power transmission methods do not look at other redstone wires. Used internally during
	 * updateCurrentStrength.
	 */
	private boolean wiresProvidePower = true;
	private Set blocksNeedingUpdate = new HashSet();
	private Icon field_94413_c;
	private Icon field_94410_cO;
	private Icon field_94411_cP;
	private Icon field_94412_cQ;

	public BlockRedstoneWire(int par1) {
		super(par1, Material.circuits);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
	 * cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
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
		return 5;
	}

	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called when
	 * first determining what to render.
	 */
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		// MCPatcher Start
		return ColorizeBlock.colorizeRedstoneWire(par1IBlockAccess, par2, par3, par4, 8388608);
		// MCPatcher End
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		return par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) || par1World.getBlockId(par2, par3 - 1, par4) == Block.glowStone.blockID;
	}

	/**
	 * Sets the strength of the wire current (0-15) for this block based on neighboring blocks and propagates to
	 * neighboring redstone wires
	 */
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
		byte var9 = 0;
		int var15 = this.getMaxCurrentStrength(par1World, par5, par6, par7, var9);
		this.wiresProvidePower = false;
		int var10 = par1World.getStrongestIndirectPower(par2, par3, par4); 
		this.wiresProvidePower = true;

		if (var10 > 0 && var10 > var15 - 1) {
			var15 = var10;
		}

		int var11 = 0;

		for (int var12 = 0; var12 < 4; ++var12) {
			int var13 = par2;
			int var14 = par4;

			if (var12 == 0) {
				var13 = par2 - 1;
			}

			if (var12 == 1) {
				++var13;
			}

			if (var12 == 2) {
				var14 = par4 - 1;
			}

			if (var12 == 3) {
				++var14;
			}

			if (var13 != par5 || var14 != par7) {
				var11 = this.getMaxCurrentStrength(par1World, var13, par3, var14, var11);
			}

			if (par1World.isBlockNormalCube(var13, par3, var14) && !par1World.isBlockNormalCube(par2, par3 + 1, par4)) {
				if ((var13 != par5 || var14 != par7) && par3 >= par6) {
					var11 = this.getMaxCurrentStrength(par1World, var13, par3 + 1, var14, var11);
				}
			} else if (!par1World.isBlockNormalCube(var13, par3, var14) && (var13 != par5 || var14 != par7) && par3 <= par6) {
				var11 = this.getMaxCurrentStrength(par1World, var13, par3 - 1, var14, var11);
			}
		}

		if (var11 > var15) {
			var15 = var11 - 1;
		} else if (var15 > 0) {
			--var15;
		} else {
			var15 = 0;
		}

		if (var10 > var15 - 1) {
			var15 = var10;
		}

		if (var8 != var15) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, var15, 2);
			this.blocksNeedingUpdate.add(new ChunkPosition(par2, par3, par4));
			this.blocksNeedingUpdate.add(new ChunkPosition(par2 - 1, par3, par4));
			this.blocksNeedingUpdate.add(new ChunkPosition(par2 + 1, par3, par4));
			this.blocksNeedingUpdate.add(new ChunkPosition(par2, par3 - 1, par4));
			this.blocksNeedingUpdate.add(new ChunkPosition(par2, par3 + 1, par4));
			this.blocksNeedingUpdate.add(new ChunkPosition(par2, par3, par4 - 1));
			this.blocksNeedingUpdate.add(new ChunkPosition(par2, par3, par4 + 1));
		}
	}

	/**
	 * Calls World.notifyBlocksOfNeighborChange() for all neighboring blocks, but only if the given block is a redstone
	 * wire.
	 */
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

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
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

	/**
	 * ejects contained items into the world, and notifies neighbours of an update, as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		super.breakBlock(par1World, par2, par3, par4, par5, par6);

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

	/**
	 * Returns the current strength at the specified block if it is greater than the passed value, or the passed value
	 * otherwise. Signature: (world, x, y, z, strength)
	 */
	// Spout Start - private to public
	public int getMaxCurrentStrength(World par1World, int par2, int par3, int par4, int par5) {
	// Spout End
		if (par1World.getBlockId(par2, par3, par4) != this.blockID) {
			return par5;
		} else {
			int var6 = par1World.getBlockMetadata(par2, par3, par4);
			return var6 > par5 ? var6 : par5;
		}
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		if (!par1World.isRemote) {
			boolean var6 = this.canPlaceBlockAt(par1World, par2, par3, par4);

			if (var6) {
				this.updateAndPropagateCurrentStrength(par1World, par2, par3, par4);
			} else {
				this.dropBlockAsItem(par1World, par2, par3, par4, 0, 0);
				par1World.setBlockToAir(par2, par3, par4);
			}

			super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, Random par2Random, int par3) {
		return Item.redstone.itemID;
	}

	/**
	 * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
	 * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return !this.wiresProvidePower ? 0 : this.isProvidingWeakPower(par1IBlockAccess, par2, par3, par4, par5);
	}

	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
	 * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X, Y,
	 * Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		if (!this.wiresProvidePower) {
			return 0;
		} else {
			int var6 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

			if (var6 == 0) {
				return 0;
			} else if (par5 == 1) {
				return var6;
			} else {
				boolean var7 = isPoweredOrRepeater(par1IBlockAccess, par2 - 1, par3, par4, 1) || !par1IBlockAccess.isBlockNormalCube(par2 - 1, par3, par4) && isPoweredOrRepeater(par1IBlockAccess, par2 - 1, par3 - 1, par4, -1);
				boolean var8 = isPoweredOrRepeater(par1IBlockAccess, par2 + 1, par3, par4, 3) || !par1IBlockAccess.isBlockNormalCube(par2 + 1, par3, par4) && isPoweredOrRepeater(par1IBlockAccess, par2 + 1, par3 - 1, par4, -1);
				boolean var9 = isPoweredOrRepeater(par1IBlockAccess, par2, par3, par4 - 1, 2) || !par1IBlockAccess.isBlockNormalCube(par2, par3, par4 - 1) && isPoweredOrRepeater(par1IBlockAccess, par2, par3 - 1, par4 - 1, -1);
				boolean var10 = isPoweredOrRepeater(par1IBlockAccess, par2, par3, par4 + 1, 0) || !par1IBlockAccess.isBlockNormalCube(par2, par3, par4 + 1) && isPoweredOrRepeater(par1IBlockAccess, par2, par3 - 1, par4 + 1, -1);

				if (!par1IBlockAccess.isBlockNormalCube(par2, par3 + 1, par4)) {
					if (par1IBlockAccess.isBlockNormalCube(par2 - 1, par3, par4) && isPoweredOrRepeater(par1IBlockAccess, par2 - 1, par3 + 1, par4, -1)) {
						var7 = true;
					}

					if (par1IBlockAccess.isBlockNormalCube(par2 + 1, par3, par4) && isPoweredOrRepeater(par1IBlockAccess, par2 + 1, par3 + 1, par4, -1)) {
						var8 = true;
					}

					if (par1IBlockAccess.isBlockNormalCube(par2, par3, par4 - 1) && isPoweredOrRepeater(par1IBlockAccess, par2, par3 + 1, par4 - 1, -1)) {
						var9 = true;
					}

					if (par1IBlockAccess.isBlockNormalCube(par2, par3, par4 + 1) && isPoweredOrRepeater(par1IBlockAccess, par2, par3 + 1, par4 + 1, -1)) {
						var10 = true;
					}
				}

				return !var9 && !var8 && !var7 && !var10 && par5 >= 2 && par5 <= 5 ? var6 : (par5 == 2 && var9 && !var7 && !var8 ? var6 : (par5 == 3 && var10 && !var7 && !var8 ? var6 : (par5 == 4 && var7 && !var9 && !var10 ? var6 : (par5 == 5 && var8 && !var9 && !var10 ? var6 : 0))));
			}
		}
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 */
	public boolean canProvidePower() {
		return this.wiresProvidePower;
	}

	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		int var6 = par1World.getBlockMetadata(par2, par3, par4);

		if (var6 > 0) {
			double var7 = (double)par2 + 0.5D + ((double)par5Random.nextFloat() - 0.5D) * 0.2D;
			double var9 = (double)((float)par3 + 0.0625F);
			double var11 = (double)par4 + 0.5D + ((double)par5Random.nextFloat() - 0.5D) * 0.2D;
			// MCPatcher Start
			float var16;
			float var14;
			float var15;

			if (ColorizeBlock.computeRedstoneWireColor(var6)) {
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
			// MCPatcher End

			if (var15 < 0.0F) {
				var15 = 0.0F;
			}

			if (var16 < 0.0F) {
				var16 = 0.0F;
			}

			par1World.spawnParticle("reddust", var7, var9, var11, (double)var14, (double)var15, (double)var16);
		}
	}

	/**
	 * Returns true if redstone wire can connect to the specified block. Params: World, X, Y, Z, side (not a normal notch-
	 * side, this can be 0, 1, 2, 3 or -1)
	 */
	public static boolean isPowerProviderOrWire(IBlockAccess par0IBlockAccess, int par1, int par2, int par3, int par4) {
		int var5 = par0IBlockAccess.getBlockId(par1, par2, par3);

		if (var5 == Block.redstoneWire.blockID) {
			return true;
		} else if (var5 == 0) {
			return false;
		} else if (!Block.redstoneRepeaterIdle.func_94487_f(var5)) {
			return Block.blocksList[var5].canProvidePower() && par4 != -1;
		} else {
			int var6 = par0IBlockAccess.getBlockMetadata(par1, par2, par3);
			return par4 == (var6 & 3) || par4 == Direction.footInvisibleFaceRemap[var6 & 3];
		}
	}

	/**
	 * Returns true if the block coordinate passed can provide power, or is a redstone wire, or if its a repeater that is
	 * powered.
	 */
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

	/**
	 * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
	 */
	public int idPicked(World par1World, int par2, int par3, int par4) {
		return Item.redstone.itemID;
	}

	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This is
	 * the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.field_94413_c = par1IconRegister.registerIcon("redstoneDust_cross");
		this.field_94410_cO = par1IconRegister.registerIcon("redstoneDust_line");
		this.field_94411_cP = par1IconRegister.registerIcon("redstoneDust_cross_overlay");
		this.field_94412_cQ = par1IconRegister.registerIcon("redstoneDust_line_overlay");
		this.blockIcon = this.field_94413_c; 
	}

	public static Icon func_94409_b(String par0Str) {
		return par0Str == "redstoneDust_cross" ? Block.redstoneWire.field_94413_c : (par0Str == "redstoneDust_line" ? Block.redstoneWire.field_94410_cO : (par0Str == "redstoneDust_cross_overlay" ? Block.redstoneWire.field_94411_cP : (par0Str == "redstoneDust_line_overlay" ? Block.redstoneWire.field_94412_cQ : null)));
	}
}
