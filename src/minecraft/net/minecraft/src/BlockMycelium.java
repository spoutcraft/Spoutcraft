package net.minecraft.src;

import java.util.Random;

public class BlockMycelium extends Block {
	public static int[][] grassMatrix; //Spout

	protected BlockMycelium(int par1) {
		super(par1, Material.grass);
		this.blockIndexInTexture = 77;
		this.setTickRandomly(true);
		//Spout start
		if (grassMatrix == null) {
			grassMatrix = new int[4][2];
			grassMatrix[0][1] = -1;
			grassMatrix[1][1] = 1;
			grassMatrix[2][0] = -1;
			grassMatrix[3][0] = 1;
		}
		//Spout end
	}

	public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
		return par1 == 1?78:(par1 == 0?2:77);
	}

	public int getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		if (par5 == 1) {
			return 78;
		} else if (par5 == 0) {
			return 2;
		} else {
			//Spout start
			Material var6 = par1IBlockAccess.getBlockMaterial(par2, par3 + 1, par4);
			par5 -= 2;
			if (var6 != Material.snow && var6 != Material.craftedSnow) {
				return par1IBlockAccess.getBlockId(par2 + BlockGrass.grassMatrix[par5][0], par3 - 1, par4 + BlockGrass.grassMatrix[par5][1]) != 110?77:78;
			} else {
				var6 = par1IBlockAccess.getBlockMaterial(par2 + BlockGrass.grassMatrix[par5][0], par3, par4 + BlockGrass.grassMatrix[par5][1]);
				return var6 != Material.snow && var6 != Material.craftedSnow?68:66;
			}
			//Spout end
		}
	}

	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		if (!par1World.isRemote) {
			if (par1World.getBlockLightValue(par2, par3 + 1, par4) < 4 && Block.lightOpacity[par1World.getBlockId(par2, par3 + 1, par4)] > 2) {
				par1World.setBlockWithNotify(par2, par3, par4, Block.dirt.blockID);
			} else if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9) {
				for (int var6 = 0; var6 < 4; ++var6) {
					int var7 = par2 + par5Random.nextInt(3) - 1;
					int var8 = par3 + par5Random.nextInt(5) - 3;
					int var9 = par4 + par5Random.nextInt(3) - 1;
					int var10 = par1World.getBlockId(var7, var8 + 1, var9);
					if (par1World.getBlockId(var7, var8, var9) == Block.dirt.blockID && par1World.getBlockLightValue(var7, var8 + 1, var9) >= 4 && Block.lightOpacity[var10] <= 2) {
						par1World.setBlockWithNotify(var7, var8, var9, this.blockID);
					}
				}
			}
		}
	}

	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		super.randomDisplayTick(par1World, par2, par3, par4, par5Random);
		if (par5Random.nextInt(10) == 0) {
			par1World.spawnParticle("townaura", (double)((float)par2 + par5Random.nextFloat()), (double)((float)par3 + 1.1F), (double)((float)par4 + par5Random.nextFloat()), 0.0D, 0.0D, 0.0D);
		}
	}

	public int idDropped(int par1, Random par2Random, int par3) {
		return Block.dirt.idDropped(0, par2Random, par3);
	}
}
