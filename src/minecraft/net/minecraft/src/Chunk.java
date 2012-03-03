package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.Entity;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.ExtendedBlockStorage;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NibbleArray;
import net.minecraft.src.Profiler;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.WorldChunkManager;

//Spout start
import org.spoutcraft.client.block.SpoutcraftChunk;
//Spout end

public class Chunk {

	public static boolean isLit;
	private ExtendedBlockStorage[] field_48505_p;
	private byte[] field_48504_q;
	public int[] precipitationHeightMap;
	public boolean[] updateSkylightColumns;
	public boolean isChunkLoaded;
	public World worldObj;
	public int[] field_48501_f;
	public final int xPosition;
	public final int zPosition;
	private boolean field_40741_v;
	public Map chunkTileEntityMap;
	public List[] field_48502_j;
	public boolean isTerrainPopulated;
	public boolean isModified;
	public boolean hasEntities;
	public long lastSaveTime;
	private int field_48503_s;
	boolean field_35846_u;

	//Spout start
	public SpoutcraftChunk spoutChunk;
	int grassColorCache = -1;
	int waterColorCache = -1;

	//Spout end

	public Chunk(World par1World, int par2, int par3) {
		this.field_48505_p = new ExtendedBlockStorage[16];
		this.field_48504_q = new byte[256];
		this.precipitationHeightMap = new int[256];
		this.updateSkylightColumns = new boolean[256];
		this.field_40741_v = false;
		this.chunkTileEntityMap = new HashMap();
		this.isTerrainPopulated = false;
		this.isModified = false;
		this.hasEntities = false;
		this.lastSaveTime = 0L;
		this.field_48503_s = 4096;
		this.field_35846_u = false;
		this.field_48502_j = new List[16];
		this.worldObj = par1World;
		this.xPosition = par2;
		this.zPosition = par3;
		this.field_48501_f = new int[256];

		for (int var4 = 0; var4 < this.field_48502_j.length; ++var4) {
			this.field_48502_j[var4] = new ArrayList();
		}

		Arrays.fill(this.precipitationHeightMap, -999);
		Arrays.fill(this.field_48504_q, (byte)-1);

		//Spout start
		spoutChunk = new SpoutcraftChunk(this);
		//Spout end
	}

	public Chunk(World par1World, byte[] par2ArrayOfByte, int par3, int par4) {
		this(par1World, par3, par4);
		int var5 = par2ArrayOfByte.length / 256;

		for (int var6 = 0; var6 < 16; ++var6) {
			for (int var7 = 0; var7 < 16; ++var7) {
				for (int var8 = 0; var8 < var5; ++var8) {
					byte var9 = par2ArrayOfByte[var6 << 11 | var7 << 7 | var8];
					if (var9 != 0) {
						int var10 = var8 >> 4;
					if (this.field_48505_p[var10] == null) {
						this.field_48505_p[var10] = new ExtendedBlockStorage(var10 << 4);
					}

					this.field_48505_p[var10].func_48691_a(var6, var8 & 15, var7, var9);
					}
				}
			}
		}

	}

	public boolean isAtLocation(int par1, int par2) {
		return par1 == this.xPosition && par2 == this.zPosition;
	}

	public int getHeightValue(int par1, int par2) {
		return this.field_48501_f[par2 << 4 | par1];
	}

	public int func_48498_h() {
		for (int var1 = this.field_48505_p.length - 1; var1 >= 0; --var1) {
			if (this.field_48505_p[var1] != null) {
				return this.field_48505_p[var1].func_48707_c();
			}
		}

		return 0;
	}

	public ExtendedBlockStorage[] func_48495_i() {
		return this.field_48505_p;
	}

	public void generateHeightMap() {
		int var1 = this.func_48498_h();

		for (int var2 = 0; var2 < 16; ++var2) {
			int var3 = 0;

			while (var3 < 16) {
				this.precipitationHeightMap[var2 + (var3 << 4)] = -999;
				int var4 = var1 + 16 - 1;

				while (true) {
					if (var4 > 0) {
						int var5 = this.getBlockID(var2, var4 - 1, var3);
						if (Block.lightOpacity[var5] == 0) {
							--var4;
							continue;
						}

						this.field_48501_f[var3 << 4 | var2] = var4;
					}

					++var3;
					break;
				}
			}
		}

		this.isModified = true;
	}

	public void generateSkylightMap() {
		int var1 = this.func_48498_h();

		int var2;
		int var3;
		for (var2 = 0; var2 < 16; ++var2) {
			var3 = 0;

			while (var3 < 16) {
				this.precipitationHeightMap[var2 + (var3 << 4)] = -999;
				int var4 = var1 + 16 - 1;

				while (true) {
					if (var4 > 0) {
						if (this.func_48499_b(var2, var4 - 1, var3) == 0) {
							--var4;
							continue;
						}

						this.field_48501_f[var3 << 4 | var2] = var4;
					}

					if (!this.worldObj.worldProvider.hasNoSky) {
						var4 = 15;
						int var5 = var1 + 16 - 1;

						do {
							var4 -= this.func_48499_b(var2, var5, var3);
							if (var4 > 0) {
								ExtendedBlockStorage var6 = this.field_48505_p[var5 >> 4];
								if (var6 != null) {
									var6.func_48702_c(var2, var5 & 15, var3, var4);
									this.worldObj.func_48464_p((this.xPosition << 4) + var2, var5, (this.zPosition << 4) + var3);
								}
							}

							--var5;
						} while (var5 > 0 && var4 > 0);
					}

					++var3;
					break;
				}
			}
		}

		this.isModified = true;

		for (var2 = 0; var2 < 16; ++var2) {
			for (var3 = 0; var3 < 16; ++var3) {
				this.propagateSkylightOcclusion(var2, var3);
			}
		}

	}

	public void func_4143_d() {}

	private void propagateSkylightOcclusion(int par1, int par2) {
		this.updateSkylightColumns[par1 + par2 * 16] = true;
		this.field_40741_v = true;
	}

	private void updateSkylight_do() {
		Profiler.startSection("recheckGaps");
		if (this.worldObj.doChunksNearChunkExist(this.xPosition * 16 + 8, 0, this.zPosition * 16 + 8, 16)) {
			for (int var1 = 0; var1 < 16; ++var1) {
				for (int var2 = 0; var2 < 16; ++var2) {
					if (this.updateSkylightColumns[var1 + var2 * 16]) {
						this.updateSkylightColumns[var1 + var2 * 16] = false;
						int var3 = this.getHeightValue(var1, var2);
						int var4 = this.xPosition * 16 + var1;
						int var5 = this.zPosition * 16 + var2;
						int var6 = this.worldObj.getHeightValue(var4 - 1, var5);
						int var7 = this.worldObj.getHeightValue(var4 + 1, var5);
						int var8 = this.worldObj.getHeightValue(var4, var5 - 1);
						int var9 = this.worldObj.getHeightValue(var4, var5 + 1);
						if (var7 < var6) {
							var6 = var7;
						}

						if (var8 < var6) {
							var6 = var8;
						}

						if (var9 < var6) {
							var6 = var9;
						}

						this.checkSkylightNeighborHeight(var4, var5, var6);
						this.checkSkylightNeighborHeight(var4 - 1, var5, var3);
						this.checkSkylightNeighborHeight(var4 + 1, var5, var3);
						this.checkSkylightNeighborHeight(var4, var5 - 1, var3);
						this.checkSkylightNeighborHeight(var4, var5 + 1, var3);
					}
				}
			}

			this.field_40741_v = false;
		}

		Profiler.endSection();
	}

	private void checkSkylightNeighborHeight(int par1, int par2, int par3) {
		int var4 = this.worldObj.getHeightValue(par1, par2);
		if (var4 > par3) {
			this.updateSkylightNeighborHeight(par1, par2, par3, var4 + 1);
		} else if (var4 < par3) {
			this.updateSkylightNeighborHeight(par1, par2, var4, par3 + 1);
		}

	}

	private void updateSkylightNeighborHeight(int par1, int par2, int par3, int par4) {
		if (par4 > par3 && this.worldObj.doChunksNearChunkExist(par1, 0, par2, 16)) {
			for (int var5 = par3; var5 < par4; ++var5) {
				this.worldObj.updateLightByType(EnumSkyBlock.Sky, par1, var5, par2);
			}

			this.isModified = true;
		}

	}

	private void relightBlock(int par1, int par2, int par3) {
		int var4 = this.field_48501_f[par3 << 4 | par1];
		int var5 = var4;
		if (par2 > var4) {
			var5 = par2;
		}

		while (var5 > 0 && this.func_48499_b(par1, var5 - 1, par3) == 0) {
			--var5;
		}

		if (var5 != var4) {
			this.worldObj.markBlocksDirtyVertical(par1, par3, var5, var4);
			this.field_48501_f[par3 << 4 | par1] = var5;
			int var6 = this.xPosition * 16 + par1;
			int var7 = this.zPosition * 16 + par3;
			int var8;
			int var12;
			if (!this.worldObj.worldProvider.hasNoSky) {
				ExtendedBlockStorage var9;
				if (var5 < var4) {
					for (var8 = var5; var8 < var4; ++var8) {
						var9 = this.field_48505_p[var8 >> 4];
						if (var9 != null) {
							var9.func_48702_c(par1, var8 & 15, par3, 15);
							this.worldObj.func_48464_p((this.xPosition << 4) + par1, var8, (this.zPosition << 4) + par3);
						}
					}
				} else {
					for (var8 = var4; var8 < var5; ++var8) {
						var9 = this.field_48505_p[var8 >> 4];
						if (var9 != null) {
							var9.func_48702_c(par1, var8 & 15, par3, 0);
							this.worldObj.func_48464_p((this.xPosition << 4) + par1, var8, (this.zPosition << 4) + par3);
						}
					}
				}

				var8 = 15;

				while (var5 > 0 && var8 > 0) {
					--var5;
					var12 = this.func_48499_b(par1, var5, par3);
					if (var12 == 0) {
						var12 = 1;
					}

					var8 -= var12;
					if (var8 < 0) {
						var8 = 0;
					}

					ExtendedBlockStorage var10 = this.field_48505_p[var5 >> 4];
					if (var10 != null) {
						var10.func_48702_c(par1, var5 & 15, par3, var8);
					}
				}
			}

			var8 = this.field_48501_f[par3 << 4 | par1];
			var12 = var4;
			int var13 = var8;
			if (var8 < var4) {
				var12 = var8;
				var13 = var4;
			}

			if (!this.worldObj.worldProvider.hasNoSky) {
				this.updateSkylightNeighborHeight(var6 - 1, var7, var12, var13);
				this.updateSkylightNeighborHeight(var6 + 1, var7, var12, var13);
				this.updateSkylightNeighborHeight(var6, var7 - 1, var12, var13);
				this.updateSkylightNeighborHeight(var6, var7 + 1, var12, var13);
				this.updateSkylightNeighborHeight(var6, var7, var12, var13);
			}

			this.isModified = true;
		}
	}

	public int func_48499_b(int par1, int par2, int par3) {
		return Block.lightOpacity[this.getBlockID(par1, par2, par3)];
	}

	public int getBlockID(int par1, int par2, int par3) {
		ExtendedBlockStorage var4 = this.field_48505_p[par2 >> 4];
		return var4 != null?var4.func_48703_a(par1, par2 & 15, par3):0;
	}

	public int getBlockMetadata(int par1, int par2, int par3) {
		ExtendedBlockStorage var4 = this.field_48505_p[par2 >> 4];
		return var4 != null?var4.func_48694_b(par1, par2 & 15, par3):0;
	}

	public boolean setBlockID(int par1, int par2, int par3, int par4) {
		return this.setBlockIDWithMetadata(par1, par2, par3, par4, 0);
	}

	public boolean setBlockIDWithMetadata(int par1, int par2, int par3, int par4, int par5) {
		int var6 = par3 << 4 | par1;
		if (par2 >= this.precipitationHeightMap[var6] - 1) {
			this.precipitationHeightMap[var6] = -999;
		}

		int var7 = this.field_48501_f[var6];
		int var8 = this.getBlockID(par1, par2, par3);
		if (var8 == par4 && this.getBlockMetadata(par1, par2, par3) == par5) {
			return false;
		} else {
			ExtendedBlockStorage var9 = this.field_48505_p[par2 >> 4];
			boolean var10 = false;
			if (var9 == null) {
				if (par4 == 0) {
					return false;
				}

				var9 = this.field_48505_p[par2 >> 4] = new ExtendedBlockStorage(par2 >> 4 << 4);
				var10 = par2 >= var7;
			}

			var9.func_48691_a(par1, par2 & 15, par3, par4);
			int var11 = this.xPosition * 16 + par1;
			int var12 = this.zPosition * 16 + par3;
			if (var8 != 0) {
				if (!this.worldObj.isRemote) {
					Block.blocksList[var8].onBlockRemoval(this.worldObj, var11, par2, var12);
				} else if (Block.blocksList[var8] instanceof BlockContainer && var8 != par4) {
					this.worldObj.removeBlockTileEntity(var11, par2, var12);
				}
			}

			var9.func_48690_b(par1, par2 & 15, par3, par5);
			if (var10) {
				this.generateSkylightMap();
			} else {
				if (Block.lightOpacity[par4 & 4095] > 0) {
					if (par2 > var7) {
						this.relightBlock(par1, par2 + 1, par3);
					}
				} else if (par2 == var7 - 1) {
					this.relightBlock(par1, par2, par3);
				}

				this.propagateSkylightOcclusion(par1, par3);
			}

			TileEntity var13;
			if (par4 != 0) {
				if (!this.worldObj.isRemote) {
					Block.blocksList[par4].onBlockAdded(this.worldObj, var11, par2, var12);
				}

				if (Block.blocksList[par4] instanceof BlockContainer) {
					var13 = this.getChunkBlockTileEntity(par1, par2, par3);
					if (var13 == null) {
						var13 = ((BlockContainer)Block.blocksList[par4]).getBlockEntity();
						this.worldObj.setBlockTileEntity(var11, par2, var12, var13);
					}

					if (var13 != null) {
						var13.updateContainingBlockInfo();
					}
				}
			} else if (var8 > 0 && Block.blocksList[var8] instanceof BlockContainer) {
				var13 = this.getChunkBlockTileEntity(par1, par2, par3);
				if (var13 != null) {
					var13.updateContainingBlockInfo();
				}
			}

			this.isModified = true;
			return true;
		}
	}

	public boolean setBlockMetadata(int par1, int par2, int par3, int par4) {
		ExtendedBlockStorage var5 = this.field_48505_p[par2 >> 4];
		if (var5 == null) {
			return false;
		} else {
			int var6 = var5.func_48694_b(par1, par2 & 15, par3);
			if (var6 == par4) {
				return false;
			} else {
				this.isModified = true;
				var5.func_48690_b(par1, par2 & 15, par3, par4);
				int var7 = var5.func_48703_a(par1, par2 & 15, par3);
				if (var7 > 0 && Block.blocksList[var7] instanceof BlockContainer) {
					TileEntity var8 = this.getChunkBlockTileEntity(par1, par2, par3);
					if (var8 != null) {
						var8.updateContainingBlockInfo();
						var8.blockMetadata = par4;
					}
				}

				return true;
			}
		}
	}

	public int getSavedLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4) {
		ExtendedBlockStorage var5 = this.field_48505_p[par3 >> 4];
		return var5 == null?par1EnumSkyBlock.defaultLightValue:(par1EnumSkyBlock == EnumSkyBlock.Sky?var5.func_48709_c(par2, par3 & 15, par4):(par1EnumSkyBlock == EnumSkyBlock.Block?var5.func_48712_d(par2, par3 & 15, par4):par1EnumSkyBlock.defaultLightValue));
	}

	public void setLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4, int par5) {
		ExtendedBlockStorage var6 = this.field_48505_p[par3 >> 4];
		if (var6 == null) {
			var6 = this.field_48505_p[par3 >> 4] = new ExtendedBlockStorage(par3 >> 4 << 4);
			this.generateSkylightMap();
		}

		this.isModified = true;
		if (par1EnumSkyBlock == EnumSkyBlock.Sky) {
			if (!this.worldObj.worldProvider.hasNoSky) {
				var6.func_48702_c(par2, par3 & 15, par4, par5);
			}
		} else {
			if (par1EnumSkyBlock != EnumSkyBlock.Block) {
				return;
			}

			var6.func_48699_d(par2, par3 & 15, par4, par5);
		}

	}

	public int getBlockLightValue(int par1, int par2, int par3, int par4) {
		ExtendedBlockStorage var5 = this.field_48505_p[par2 >> 4];
		if (var5 == null) {
			return !this.worldObj.worldProvider.hasNoSky && par4 < EnumSkyBlock.Sky.defaultLightValue?EnumSkyBlock.Sky.defaultLightValue - par4:0;
		} else {
			int var6 = this.worldObj.worldProvider.hasNoSky?0:var5.func_48709_c(par1, par2 & 15, par3);
			if (var6 > 0) {
				isLit = true;
			}

			var6 -= par4;
			int var7 = var5.func_48712_d(par1, par2 & 15, par3);
			if (var7 > var6) {
				var6 = var7;
			}

			return var6;
		}
	}

	public void addEntity(Entity par1Entity) {
		this.hasEntities = true;
		int var2 = MathHelper.floor_double(par1Entity.posX / 16.0D);
		int var3 = MathHelper.floor_double(par1Entity.posZ / 16.0D);
		if (var2 != this.xPosition || var3 != this.zPosition) {
			System.out.println("Wrong location! " + par1Entity);
			Thread.dumpStack();
		}

		int var4 = MathHelper.floor_double(par1Entity.posY / 16.0D);
		if (var4 < 0) {
			var4 = 0;
		}

		if (var4 >= this.field_48502_j.length) {
			var4 = this.field_48502_j.length - 1;
		}

		par1Entity.addedToChunk = true;
		par1Entity.chunkCoordX = this.xPosition;
		par1Entity.chunkCoordY = var4;
		par1Entity.chunkCoordZ = this.zPosition;
		this.field_48502_j[var4].add(par1Entity);
	}

	public void removeEntity(Entity par1Entity) {
		this.removeEntityAtIndex(par1Entity, par1Entity.chunkCoordY);
	}

	public void removeEntityAtIndex(Entity par1Entity, int par2) {
		if (par2 < 0) {
			par2 = 0;
		}

		if (par2 >= this.field_48502_j.length) {
			par2 = this.field_48502_j.length - 1;
		}

		this.field_48502_j[par2].remove(par1Entity);
	}

	public boolean canBlockSeeTheSky(int par1, int par2, int par3) {
		return par2 >= this.field_48501_f[par3 << 4 | par1];
	}

	public TileEntity getChunkBlockTileEntity(int par1, int par2, int par3) {
		ChunkPosition var4 = new ChunkPosition(par1, par2, par3);
		TileEntity var5 = (TileEntity)this.chunkTileEntityMap.get(var4);
		if (var5 == null) {
			int var6 = this.getBlockID(par1, par2, par3);
			if (var6 <= 0 || !Block.blocksList[var6].func_48205_p()) {
				return null;
			}

			if (var5 == null) {
				var5 = ((BlockContainer)Block.blocksList[var6]).getBlockEntity();
				this.worldObj.setBlockTileEntity(this.xPosition * 16 + par1, par2, this.zPosition * 16 + par3, var5);
			}

			var5 = (TileEntity)this.chunkTileEntityMap.get(var4);
		}

		if (var5 != null && var5.isInvalid()) {
			this.chunkTileEntityMap.remove(var4);
			return null;
		} else {
			return var5;
		}
	}

	public void addTileEntity(TileEntity par1TileEntity) {
		int var2 = par1TileEntity.xCoord - this.xPosition * 16;
		int var3 = par1TileEntity.yCoord;
		int var4 = par1TileEntity.zCoord - this.zPosition * 16;
		this.setChunkBlockTileEntity(var2, var3, var4, par1TileEntity);
		if (this.isChunkLoaded) {
			this.worldObj.loadedTileEntityList.add(par1TileEntity);
		}

	}

	public void setChunkBlockTileEntity(int par1, int par2, int par3, TileEntity par4TileEntity) {
		ChunkPosition var5 = new ChunkPosition(par1, par2, par3);
		par4TileEntity.worldObj = this.worldObj;
		par4TileEntity.xCoord = this.xPosition * 16 + par1;
		par4TileEntity.yCoord = par2;
		par4TileEntity.zCoord = this.zPosition * 16 + par3;
		if (this.getBlockID(par1, par2, par3) != 0 && Block.blocksList[this.getBlockID(par1, par2, par3)] instanceof BlockContainer) {
			par4TileEntity.validate();
			this.chunkTileEntityMap.put(var5, par4TileEntity);
		}
	}

	public void removeChunkBlockTileEntity(int par1, int par2, int par3) {
		ChunkPosition var4 = new ChunkPosition(par1, par2, par3);
		if (this.isChunkLoaded) {
			TileEntity var5 = (TileEntity)this.chunkTileEntityMap.remove(var4);
			if (var5 != null) {
				var5.invalidate();
			}
		}

	}

	public void onChunkLoad() {
		this.isChunkLoaded = true;
		this.worldObj.addTileEntity(this.chunkTileEntityMap.values());

		for (int var1 = 0; var1 < this.field_48502_j.length; ++var1) {
			this.worldObj.addLoadedEntities(this.field_48502_j[var1]);
		}

		//Spout start
		//note: onChunkLoad is only called in SP
		SpoutcraftChunk.loadedChunks.add(spoutChunk);
		//Spout end
	}

	public void onChunkUnload() {
		this.isChunkLoaded = false;
		Iterator var1 = this.chunkTileEntityMap.values().iterator();

		while (var1.hasNext()) {
			TileEntity var2 = (TileEntity)var1.next();
			this.worldObj.markTileEntityForDespawn(var2);
		}

		for (int var3 = 0; var3 < this.field_48502_j.length; ++var3) {
			this.worldObj.unloadEntities(this.field_48502_j[var3]);
		}

		//Spout start
		SpoutcraftChunk.loadedChunks.remove(spoutChunk);
		//Spout end
	}

	public void setChunkModified() {
		this.isModified = true;
	}

	public void getEntitiesWithinAABBForEntity(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB, List par3List) {
		int var4 = MathHelper.floor_double((par2AxisAlignedBB.minY - 2.0D) / 16.0D);
		int var5 = MathHelper.floor_double((par2AxisAlignedBB.maxY + 2.0D) / 16.0D);
		if (var4 < 0) {
			var4 = 0;
		}

		if (var5 >= this.field_48502_j.length) {
			var5 = this.field_48502_j.length - 1;
		}

		for (int var6 = var4; var6 <= var5; ++var6) {
			List var7 = this.field_48502_j[var6];

			for (int var8 = 0; var8 < var7.size(); ++var8) {
				Entity var9 = (Entity)var7.get(var8);
				if (var9 != par1Entity && var9.boundingBox.intersectsWith(par2AxisAlignedBB)) {
					par3List.add(var9);
					Entity[] var10 = var9.getParts();
					if (var10 != null) {
						for (int var11 = 0; var11 < var10.length; ++var11) {
							var9 = var10[var11];
							if (var9 != par1Entity && var9.boundingBox.intersectsWith(par2AxisAlignedBB)) {
								par3List.add(var9);
							}
						}
					}
				}
			}
		}

	}

	public void getEntitiesOfTypeWithinAAAB(Class par1Class, AxisAlignedBB par2AxisAlignedBB, List par3List) {
		int var4 = MathHelper.floor_double((par2AxisAlignedBB.minY - 2.0D) / 16.0D);
		int var5 = MathHelper.floor_double((par2AxisAlignedBB.maxY + 2.0D) / 16.0D);
		if (var4 < 0) {
			var4 = 0;
		} else if (var4 >= this.field_48502_j.length) {
			var4 = this.field_48502_j.length - 1;
		}

		if (var5 >= this.field_48502_j.length) {
			var5 = this.field_48502_j.length - 1;
		} else if (var5 < 0) {
			var5 = 0;
		}

		for (int var6 = var4; var6 <= var5; ++var6) {
			List var7 = this.field_48502_j[var6];

			for (int var8 = 0; var8 < var7.size(); ++var8) {
				Entity var9 = (Entity)var7.get(var8);
				if (par1Class.isAssignableFrom(var9.getClass()) && var9.boundingBox.intersectsWith(par2AxisAlignedBB)) {
					par3List.add(var9);
				}
			}
		}

	}

	public boolean needsSaving(boolean par1) {
		if (par1) {
			if (this.hasEntities && this.worldObj.getWorldTime() != this.lastSaveTime) {
				return true;
			}
		} else if (this.hasEntities && this.worldObj.getWorldTime() >= this.lastSaveTime + 600L) {
			return true;
		}

		return this.isModified;
	}

	public Random getRandomWithSeed(long par1) {
		return new Random(this.worldObj.getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ par1);
	}

	public boolean isEmpty() {
		return false;
	}

	public void removeUnknownBlocks() {
		ExtendedBlockStorage[] var1 = this.field_48505_p;
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			ExtendedBlockStorage var4 = var1[var3];
			if (var4 != null) {
				var4.func_48711_e();
			}
		}

	}

	public void populateChunk(IChunkProvider par1IChunkProvider, IChunkProvider par2IChunkProvider, int par3, int par4) {
		if (!this.isTerrainPopulated && par1IChunkProvider.chunkExists(par3 + 1, par4 + 1) && par1IChunkProvider.chunkExists(par3, par4 + 1) && par1IChunkProvider.chunkExists(par3 + 1, par4)) {
			par1IChunkProvider.populate(par2IChunkProvider, par3, par4);
		}

		if (par1IChunkProvider.chunkExists(par3 - 1, par4) && !par1IChunkProvider.provideChunk(par3 - 1, par4).isTerrainPopulated && par1IChunkProvider.chunkExists(par3 - 1, par4 + 1) && par1IChunkProvider.chunkExists(par3, par4 + 1) && par1IChunkProvider.chunkExists(par3 - 1, par4 + 1)) {
			par1IChunkProvider.populate(par2IChunkProvider, par3 - 1, par4);
		}

		if (par1IChunkProvider.chunkExists(par3, par4 - 1) && !par1IChunkProvider.provideChunk(par3, par4 - 1).isTerrainPopulated && par1IChunkProvider.chunkExists(par3 + 1, par4 - 1) && par1IChunkProvider.chunkExists(par3 + 1, par4 - 1) && par1IChunkProvider.chunkExists(par3 + 1, par4)) {
			par1IChunkProvider.populate(par2IChunkProvider, par3, par4 - 1);
		}

		if (par1IChunkProvider.chunkExists(par3 - 1, par4 - 1) && !par1IChunkProvider.provideChunk(par3 - 1, par4 - 1).isTerrainPopulated && par1IChunkProvider.chunkExists(par3, par4 - 1) && par1IChunkProvider.chunkExists(par3 - 1, par4)) {
			par1IChunkProvider.populate(par2IChunkProvider, par3 - 1, par4 - 1);
		}

	}

	public int getPrecipitationHeight(int par1, int par2) {
		int var3 = par1 | par2 << 4;
		int var4 = this.precipitationHeightMap[var3];
		if (var4 == -999) {
			int var5 = this.func_48498_h() + 15;
			var4 = -1;

			while (var5 > 0 && var4 == -1) {
				int var6 = this.getBlockID(par1, var5, par2);
				Material var7 = var6 == 0?Material.air:Block.blocksList[var6].blockMaterial;
				if (!var7.blocksMovement() && !var7.isLiquid()) {
					--var5;
				} else {
					var4 = var5 + 1;
				}
			}

			this.precipitationHeightMap[var3] = var4;
		}

		return var4;
	}

	public void updateSkylight() {
		if (this.field_40741_v && !this.worldObj.worldProvider.hasNoSky) {
			this.updateSkylight_do();
		}

	}

	public ChunkCoordIntPair getChunkCoordIntPair() {
		return new ChunkCoordIntPair(this.xPosition, this.zPosition);
	}

	public boolean func_48492_c(int par1, int par2) {
		if (par1 < 0) {
			par1 = 0;
		}

		if (par2 >= 256) {
			par2 = 255;
		}

		for (int var3 = par1; var3 <= par2; var3 += 16) {
			ExtendedBlockStorage var4 = this.field_48505_p[var3 >> 4];
			if (var4 != null && !var4.func_48693_a()) {
				return false;
			}
		}

		return true;
	}

	public void func_48500_a(ExtendedBlockStorage[] par1ArrayOfExtendedBlockStorage) {
		this.field_48505_p = par1ArrayOfExtendedBlockStorage;
	}

	public void func_48494_a(byte[] par1ArrayOfByte, int par2, int par3, boolean par4) {
		int var5 = 0;

		int var6;
		for (var6 = 0; var6 < this.field_48505_p.length; ++var6) {
			if ((par2 & 1 << var6) != 0) {
				if (this.field_48505_p[var6] == null) {
					this.field_48505_p[var6] = new ExtendedBlockStorage(var6 << 4);
				}

				byte[] var7 = this.field_48505_p[var6].func_48692_g();
				System.arraycopy(par1ArrayOfByte, var5, var7, 0, var7.length);
				var5 += var7.length;
			} else if (par4 && this.field_48505_p[var6] != null) {
				this.field_48505_p[var6] = null;
			}
		}

		NibbleArray var8;
		for (var6 = 0; var6 < this.field_48505_p.length; ++var6) {
			if ((par2 & 1 << var6) != 0 && this.field_48505_p[var6] != null) {
				var8 = this.field_48505_p[var6].func_48697_j();
				System.arraycopy(par1ArrayOfByte, var5, var8.data, 0, var8.data.length);
				var5 += var8.data.length;
			}
		}

		for (var6 = 0; var6 < this.field_48505_p.length; ++var6) {
			if ((par2 & 1 << var6) != 0 && this.field_48505_p[var6] != null) {
				var8 = this.field_48505_p[var6].func_48705_k();
				System.arraycopy(par1ArrayOfByte, var5, var8.data, 0, var8.data.length);
				var5 += var8.data.length;
			}
		}

		for (var6 = 0; var6 < this.field_48505_p.length; ++var6) {
			if ((par2 & 1 << var6) != 0 && this.field_48505_p[var6] != null) {
				var8 = this.field_48505_p[var6].func_48714_l();
				System.arraycopy(par1ArrayOfByte, var5, var8.data, 0, var8.data.length);
				var5 += var8.data.length;
			}
		}

		for (var6 = 0; var6 < this.field_48505_p.length; ++var6) {
			if ((par3 & 1 << var6) != 0) {
				if (this.field_48505_p[var6] == null) {
					var5 += 2048;
				} else {
					var8 = this.field_48505_p[var6].func_48704_i();
					if (var8 == null) {
						var8 = this.field_48505_p[var6].func_48696_m();
					}

					System.arraycopy(par1ArrayOfByte, var5, var8.data, 0, var8.data.length);
					var5 += var8.data.length;
				}
			} else if (par4 && this.field_48505_p[var6] != null && this.field_48505_p[var6].func_48704_i() != null) {
				this.field_48505_p[var6].func_48715_h();
			}
		}

		if (par4) {
			System.arraycopy(par1ArrayOfByte, var5, this.field_48504_q, 0, this.field_48504_q.length);
			int var10000 = var5 + this.field_48504_q.length;
		}

		for (var6 = 0; var6 < this.field_48505_p.length; ++var6) {
			if (this.field_48505_p[var6] != null && (par2 & 1 << var6) != 0) {
				this.field_48505_p[var6].func_48708_d();
			}
		}

		this.generateHeightMap();
		Iterator var10 = this.chunkTileEntityMap.values().iterator();

		while (var10.hasNext()) {
			TileEntity var9 = (TileEntity)var10.next();
			var9.updateContainingBlockInfo();
		}

	}

	public BiomeGenBase func_48490_a(int par1, int par2, WorldChunkManager par3WorldChunkManager) {
		int var4 = this.field_48504_q[par2 << 4 | par1] & 255;
		if (var4 == 255) {
			BiomeGenBase var5 = par3WorldChunkManager.getBiomeGenAt((this.xPosition << 4) + par1, (this.zPosition << 4) + par2);
			var4 = var5.biomeID;
			this.field_48504_q[par2 << 4 | par1] = (byte)(var4 & 255);
		}

		return BiomeGenBase.biomeList[var4] == null?BiomeGenBase.plains:BiomeGenBase.biomeList[var4];
	}

	public byte[] func_48493_m() {
		return this.field_48504_q;
	}

	public void func_48497_a(byte[] par1ArrayOfByte) {
		this.field_48504_q = par1ArrayOfByte;
	}

	public void func_48496_n() {
		this.field_48503_s = 0;
	}

	public void func_48491_o() {
		for (int var1 = 0; var1 < 8; ++var1) {
			if (this.field_48503_s >= 4096) {
				return;
			}

			int var2 = this.field_48503_s % 16;
			int var3 = this.field_48503_s / 16 % 16;
			int var4 = this.field_48503_s / 256;
			++this.field_48503_s;
			int var5 = (this.xPosition << 4) + var3;
			int var6 = (this.zPosition << 4) + var4;

			for (int var7 = 0; var7 < 16; ++var7) {
				int var8 = (var2 << 4) + var7;
				if (this.field_48505_p[var2] == null && (var7 == 0 || var7 == 15 || var3 == 0 || var3 == 15 || var4 == 0 || var4 == 15) || this.field_48505_p[var2] != null && this.field_48505_p[var2].func_48703_a(var3, var7, var4) == 0) {
					if (Block.lightValue[this.worldObj.getBlockId(var5, var8 - 1, var6)] > 0) {
						this.worldObj.updateAllLightTypes(var5, var8 - 1, var6);
					}

					if (Block.lightValue[this.worldObj.getBlockId(var5, var8 + 1, var6)] > 0) {
						this.worldObj.updateAllLightTypes(var5, var8 + 1, var6);
					}

					if (Block.lightValue[this.worldObj.getBlockId(var5 - 1, var8, var6)] > 0) {
						this.worldObj.updateAllLightTypes(var5 - 1, var8, var6);
					}

					if (Block.lightValue[this.worldObj.getBlockId(var5 + 1, var8, var6)] > 0) {
						this.worldObj.updateAllLightTypes(var5 + 1, var8, var6);
					}

					if (Block.lightValue[this.worldObj.getBlockId(var5, var8, var6 - 1)] > 0) {
						this.worldObj.updateAllLightTypes(var5, var8, var6 - 1);
					}

					if (Block.lightValue[this.worldObj.getBlockId(var5, var8, var6 + 1)] > 0) {
						this.worldObj.updateAllLightTypes(var5, var8, var6 + 1);
					}

					this.worldObj.updateAllLightTypes(var5, var8, var6);
				}
			}
		}

	}
}
