package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.ChunkBlockMap;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.Entity;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NibbleArray;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
//Spout start
import org.getspout.spout.block.SpoutcraftChunk;
//Spout end

public class Chunk {

	public static boolean isLit;
	public byte[] blocks;
	public boolean isChunkLoaded;
	public World worldObj;
	public NibbleArray data;
	public NibbleArray skylightMap;
	public NibbleArray blocklightMap;
	public byte[] heightMap;
	public int lowestBlockHeight;
	public final int xPosition;
	public final int zPosition;
	public Map chunkTileEntityMap;
	public List[] entities;
	public boolean isTerrainPopulated;
	public boolean isModified;
	public boolean neverSave;
	public boolean hasEntities;
	public long lastSaveTime;
	//Spout start
	public SpoutcraftChunk spoutChunk;
	//Spout end

	public Chunk(World var1, int var2, int var3) {
		this.chunkTileEntityMap = new HashMap();
		this.entities = new List[8];
		this.isTerrainPopulated = false;
		this.isModified = false;
		this.hasEntities = false;
		this.lastSaveTime = 0L;
		this.worldObj = var1;
		this.xPosition = var2;
		this.zPosition = var3;
		this.heightMap = new byte[256];

		for(int var4 = 0; var4 < this.entities.length; ++var4) {
			this.entities[var4] = new ArrayList();
		}

		//Spout start
		spoutChunk = new SpoutcraftChunk(this);
		//Spout end
	}

	public Chunk(World var1, byte[] var2, int var3, int var4) {
		this(var1, var3, var4);
		this.blocks = var2;
		this.data = new NibbleArray(var2.length);
		this.skylightMap = new NibbleArray(var2.length);
		this.blocklightMap = new NibbleArray(var2.length);
	}

	public boolean isAtLocation(int var1, int var2) {
		return var1 == this.xPosition && var2 == this.zPosition;
	}

	public int getHeightValue(int var1, int var2) {
		return this.heightMap[var2 << 4 | var1] & 255;
	}

	public void func_1014_a() {}

	public void generateHeightMap() {
		int var1 = 127;

		for(int var2 = 0; var2 < 16; ++var2) {
			for(int var3 = 0; var3 < 16; ++var3) {
				int var4 = 127;

				for(int var5 = var2 << 11 | var3 << 7; var4 > 0 && Block.lightOpacity[this.blocks[var5 + var4 - 1] & 255] == 0; --var4) {
					;
				}

				this.heightMap[var3 << 4 | var2] = (byte)var4;
				if(var4 < var1) {
					var1 = var4;
				}
			}
		}

		this.lowestBlockHeight = var1;
		this.isModified = true;
	}

	public void func_1024_c() {
		int var1 = 127;

		int var2;
		int var3;
		for(var2 = 0; var2 < 16; ++var2) {
			for(var3 = 0; var3 < 16; ++var3) {
				int var4 = 127;

				int var5;
				for(var5 = var2 << 11 | var3 << 7; var4 > 0 && Block.lightOpacity[this.blocks[var5 + var4 - 1] & 255] == 0; --var4) {
					;
				}

				this.heightMap[var3 << 4 | var2] = (byte)var4;
				if(var4 < var1) {
					var1 = var4;
				}

				if(!this.worldObj.worldProvider.hasNoSky) {
					int var6 = 15;
					int var7 = 127;

					do {
						var6 -= Block.lightOpacity[this.blocks[var5 + var7] & 255];
						if(var6 > 0) {
							this.skylightMap.setNibble(var2, var7, var3, var6);
						}

						--var7;
					} while(var7 > 0 && var6 > 0);
				}
			}
		}

		this.lowestBlockHeight = var1;

		for(var2 = 0; var2 < 16; ++var2) {
			for(var3 = 0; var3 < 16; ++var3) {
				this.func_996_c(var2, var3);
			}
		}

		this.isModified = true;
	}

	public void func_4143_d() {}

	private void func_996_c(int var1, int var2) {
		int var3 = this.getHeightValue(var1, var2);
		int var4 = this.xPosition * 16 + var1;
		int var5 = this.zPosition * 16 + var2;
		this.func_1020_f(var4 - 1, var5, var3);
		this.func_1020_f(var4 + 1, var5, var3);
		this.func_1020_f(var4, var5 - 1, var3);
		this.func_1020_f(var4, var5 + 1, var3);
	}

	private void func_1020_f(int var1, int var2, int var3) {
		int var4 = this.worldObj.getHeightValue(var1, var2);
		if(var4 > var3) {
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, var1, var3, var2, var1, var4, var2);
			this.isModified = true;
		} else if(var4 < var3) {
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, var1, var4, var2, var1, var3, var2);
			this.isModified = true;
		}

	}

	private void func_1003_g(int var1, int var2, int var3) {
		int var4 = this.heightMap[var3 << 4 | var1] & 255;
		int var5 = var4;
		if(var2 > var4) {
			var5 = var2;
		}

		for(int var6 = var1 << 11 | var3 << 7; var5 > 0 && Block.lightOpacity[this.blocks[var6 + var5 - 1] & 255] == 0; --var5) {
			;
		}

		if(var5 != var4) {
			this.worldObj.markBlocksDirtyVertical(var1, var3, var5, var4);
			this.heightMap[var3 << 4 | var1] = (byte)var5;
			int var7;
			int var8;
			int var9;
			if(var5 < this.lowestBlockHeight) {
				this.lowestBlockHeight = var5;
			} else {
				var7 = 127;

				for(var8 = 0; var8 < 16; ++var8) {
					for(var9 = 0; var9 < 16; ++var9) {
						if((this.heightMap[var9 << 4 | var8] & 255) < var7) {
							var7 = this.heightMap[var9 << 4 | var8] & 255;
						}
					}
				}

				this.lowestBlockHeight = var7;
			}

			var7 = this.xPosition * 16 + var1;
			var8 = this.zPosition * 16 + var3;
			if(var5 < var4) {
				for(var9 = var5; var9 < var4; ++var9) {
					this.skylightMap.setNibble(var1, var9, var3, 15);
				}
			} else {
				this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, var7, var4, var8, var7, var5, var8);

				for(var9 = var4; var9 < var5; ++var9) {
					this.skylightMap.setNibble(var1, var9, var3, 0);
				}
			}

			var9 = 15;

			int var10;
			for(var10 = var5; var5 > 0 && var9 > 0; this.skylightMap.setNibble(var1, var5, var3, var9)) {
				--var5;
				int var11 = Block.lightOpacity[this.getBlockID(var1, var5, var3)];
				if(var11 == 0) {
					var11 = 1;
				}

				var9 -= var11;
				if(var9 < 0) {
					var9 = 0;
				}
			}

			while(var5 > 0 && Block.lightOpacity[this.getBlockID(var1, var5 - 1, var3)] == 0) {
				--var5;
			}

			if(var5 != var10) {
				this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, var7 - 1, var5, var8 - 1, var7 + 1, var10, var8 + 1);
			}

			this.isModified = true;
		}
	}

	public int getBlockID(int var1, int var2, int var3) {
		return this.blocks[var1 << 11 | var3 << 7 | var2] & 255;
	}

	public boolean setBlockIDWithMetadata(int var1, int var2, int var3, int var4, int var5) {
		byte var6 = (byte)var4;
		int var7 = this.heightMap[var3 << 4 | var1] & 255;
		int var8 = this.blocks[var1 << 11 | var3 << 7 | var2] & 255;
		if(var8 == var4 && this.data.getNibble(var1, var2, var3) == var5) {
			return false;
		} else {
			int var9 = this.xPosition * 16 + var1;
			int var10 = this.zPosition * 16 + var3;
			this.blocks[var1 << 11 | var3 << 7 | var2] = (byte)(var6 & 255);
			if(var8 != 0 && !this.worldObj.multiplayerWorld) {
				Block.blocksList[var8].onBlockRemoval(this.worldObj, var9, var2, var10);
			}

			this.data.setNibble(var1, var2, var3, var5);
			if(!this.worldObj.worldProvider.hasNoSky) {
				if(Block.lightOpacity[var6 & 255] != 0) {
					if(var2 >= var7) {
						this.func_1003_g(var1, var2 + 1, var3);
					}
				} else if(var2 == var7 - 1) {
					this.func_1003_g(var1, var2, var3);
				}

				this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, var9, var2, var10, var9, var2, var10);
			}

			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Block, var9, var2, var10, var9, var2, var10);
			this.func_996_c(var1, var3);
			this.data.setNibble(var1, var2, var3, var5);
			if(var4 != 0) {
				Block.blocksList[var4].onBlockAdded(this.worldObj, var9, var2, var10);
			}

			this.isModified = true;
			return true;
		}
	}

	public boolean setBlockID(int var1, int var2, int var3, int var4) {
		byte var5 = (byte)var4;
		int var6 = this.heightMap[var3 << 4 | var1] & 255;
		int var7 = this.blocks[var1 << 11 | var3 << 7 | var2] & 255;
		if(var7 == var4) {
			return false;
		} else {
			int var8 = this.xPosition * 16 + var1;
			int var9 = this.zPosition * 16 + var3;
			this.blocks[var1 << 11 | var3 << 7 | var2] = (byte)(var5 & 255);
			if(var7 != 0) {
				Block.blocksList[var7].onBlockRemoval(this.worldObj, var8, var2, var9);
			}

			this.data.setNibble(var1, var2, var3, 0);
			if(Block.lightOpacity[var5 & 255] != 0) {
				if(var2 >= var6) {
					this.func_1003_g(var1, var2 + 1, var3);
				}
			} else if(var2 == var6 - 1) {
				this.func_1003_g(var1, var2, var3);
			}

			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, var8, var2, var9, var8, var2, var9);
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Block, var8, var2, var9, var8, var2, var9);
			this.func_996_c(var1, var3);
			if(var4 != 0 && !this.worldObj.multiplayerWorld) {
				Block.blocksList[var4].onBlockAdded(this.worldObj, var8, var2, var9);
			}

			this.isModified = true;
			return true;
		}
	}

	public int getBlockMetadata(int var1, int var2, int var3) {
		return this.data.getNibble(var1, var2, var3);
	}

	public void setBlockMetadata(int var1, int var2, int var3, int var4) {
		this.isModified = true;
		this.data.setNibble(var1, var2, var3, var4);
	}

	public int getSavedLightValue(EnumSkyBlock var1, int var2, int var3, int var4) {
		return var1 == EnumSkyBlock.Sky?this.skylightMap.getNibble(var2, var3, var4):(var1 == EnumSkyBlock.Block?this.blocklightMap.getNibble(var2, var3, var4):0);
	}

	public void setLightValue(EnumSkyBlock var1, int var2, int var3, int var4, int var5) {
		this.isModified = true;
		if(var1 == EnumSkyBlock.Sky) {
			this.skylightMap.setNibble(var2, var3, var4, var5);
		} else {
			if(var1 != EnumSkyBlock.Block) {
				return;
			}

			this.blocklightMap.setNibble(var2, var3, var4, var5);
		}

	}

	public int getBlockLightValue(int var1, int var2, int var3, int var4) {
		int var5 = this.skylightMap.getNibble(var1, var2, var3);
		if(var5 > 0) {
			isLit = true;
		}

		var5 -= var4;
		int var6 = this.blocklightMap.getNibble(var1, var2, var3);
		if(var6 > var5) {
			var5 = var6;
		}

		return var5;
	}

	public void addEntity(Entity var1) {
		this.hasEntities = true;
		int var2 = MathHelper.floor_double(var1.posX / 16.0D);
		int var3 = MathHelper.floor_double(var1.posZ / 16.0D);
		if(var2 != this.xPosition || var3 != this.zPosition) {
			System.out.println("Wrong location! " + var1);
			Thread.dumpStack();
		}

		int var4 = MathHelper.floor_double(var1.posY / 16.0D);
		if(var4 < 0) {
			var4 = 0;
		}

		if(var4 >= this.entities.length) {
			var4 = this.entities.length - 1;
		}

		var1.addedToChunk = true;
		var1.chunkCoordX = this.xPosition;
		var1.chunkCoordY = var4;
		var1.chunkCoordZ = this.zPosition;
		this.entities[var4].add(var1);
	}

	public void removeEntity(Entity var1) {
		this.removeEntityAtIndex(var1, var1.chunkCoordY);
	}

	public void removeEntityAtIndex(Entity var1, int var2) {
		if(var2 < 0) {
			var2 = 0;
		}

		if(var2 >= this.entities.length) {
			var2 = this.entities.length - 1;
		}

		this.entities[var2].remove(var1);
	}

	public boolean canBlockSeeTheSky(int var1, int var2, int var3) {
		return var2 >= (this.heightMap[var3 << 4 | var1] & 255);
	}

	public TileEntity getChunkBlockTileEntity(int var1, int var2, int var3) {
		ChunkPosition var4 = new ChunkPosition(var1, var2, var3);
		TileEntity var5 = (TileEntity)this.chunkTileEntityMap.get(var4);
		if(var5 == null) {
			int var6 = this.getBlockID(var1, var2, var3);
			if(!Block.isBlockContainer[var6]) {
				return null;
			}

			BlockContainer var7 = (BlockContainer)Block.blocksList[var6];
			var7.onBlockAdded(this.worldObj, this.xPosition * 16 + var1, var2, this.zPosition * 16 + var3);
			var5 = (TileEntity)this.chunkTileEntityMap.get(var4);
		}

		if(var5 != null && var5.func_31006_g()) {
			this.chunkTileEntityMap.remove(var4);
			return null;
		} else {
			return var5;
		}
	}

	public void addTileEntity(TileEntity var1) {
		int var2 = var1.xCoord - this.xPosition * 16;
		int var3 = var1.yCoord;
		int var4 = var1.zCoord - this.zPosition * 16;
		this.setChunkBlockTileEntity(var2, var3, var4, var1);
		if(this.isChunkLoaded) {
			this.worldObj.loadedTileEntityList.add(var1);
		}

	}

	public void setChunkBlockTileEntity(int var1, int var2, int var3, TileEntity var4) {
		ChunkPosition var5 = new ChunkPosition(var1, var2, var3);
		var4.worldObj = this.worldObj;
		var4.xCoord = this.xPosition * 16 + var1;
		var4.yCoord = var2;
		var4.zCoord = this.zPosition * 16 + var3;
		if(this.getBlockID(var1, var2, var3) != 0 && Block.blocksList[this.getBlockID(var1, var2, var3)] instanceof BlockContainer) {
			var4.func_31004_j();
			this.chunkTileEntityMap.put(var5, var4);
		} else {
			System.out.println("Attempted to place a tile entity where there was no entity tile!");
		}
	}

	public void removeChunkBlockTileEntity(int var1, int var2, int var3) {
		ChunkPosition var4 = new ChunkPosition(var1, var2, var3);
		if(this.isChunkLoaded) {
			TileEntity var5 = (TileEntity)this.chunkTileEntityMap.remove(var4);
			if(var5 != null) {
				var5.func_31005_i();
			}
		}

	}

	public void onChunkLoad() {
		this.isChunkLoaded = true;
		this.worldObj.func_31054_a(this.chunkTileEntityMap.values());

		for(int var1 = 0; var1 < this.entities.length; ++var1) {
			this.worldObj.func_636_a(this.entities[var1]);
		}

	}

	public void onChunkUnload() {
		this.isChunkLoaded = false;
		Iterator var1 = this.chunkTileEntityMap.values().iterator();

		while(var1.hasNext()) {
			TileEntity var2 = (TileEntity)var1.next();
			var2.func_31005_i();
		}

		for(int var3 = 0; var3 < this.entities.length; ++var3) {
			this.worldObj.func_632_b(this.entities[var3]);
		}

	}

	public void setChunkModified() {
		this.isModified = true;
	}

	public void getEntitiesWithinAABBForEntity(Entity var1, AxisAlignedBB var2, List var3) {
		int var4 = MathHelper.floor_double((var2.minY - 2.0D) / 16.0D);
		int var5 = MathHelper.floor_double((var2.maxY + 2.0D) / 16.0D);
		if(var4 < 0) {
			var4 = 0;
		}

		if(var5 >= this.entities.length) {
			var5 = this.entities.length - 1;
		}

		for(int var6 = var4; var6 <= var5; ++var6) {
			List var7 = this.entities[var6];

			for(int var8 = 0; var8 < var7.size(); ++var8) {
				Entity var9 = (Entity)var7.get(var8);
				if(var9 != var1 && var9.boundingBox.intersectsWith(var2)) {
					var3.add(var9);
				}
			}
		}

	}

	public void getEntitiesOfTypeWithinAAAB(Class var1, AxisAlignedBB var2, List var3) {
		int var4 = MathHelper.floor_double((var2.minY - 2.0D) / 16.0D);
		int var5 = MathHelper.floor_double((var2.maxY + 2.0D) / 16.0D);
		if(var4 < 0) {
			var4 = 0;
		}

		if(var5 >= this.entities.length) {
			var5 = this.entities.length - 1;
		}

		for(int var6 = var4; var6 <= var5; ++var6) {
			List var7 = this.entities[var6];

			for(int var8 = 0; var8 < var7.size(); ++var8) {
				Entity var9 = (Entity)var7.get(var8);
				if(var1.isAssignableFrom(var9.getClass()) && var9.boundingBox.intersectsWith(var2)) {
					var3.add(var9);
				}
			}
		}

	}

	public boolean needsSaving(boolean var1) {
		if(this.neverSave) {
			return false;
		} else {
			if(var1) {
				if(this.hasEntities && this.worldObj.getWorldTime() != this.lastSaveTime) {
					return true;
				}
			} else if(this.hasEntities && this.worldObj.getWorldTime() >= this.lastSaveTime + 600L) {
				return true;
			}

			return this.isModified;
		}
	}

	public int setChunkData(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
		int var9;
		int var10;
		int var11;
		int var12;
		for(var9 = var2; var9 < var5; ++var9) {
			for(var10 = var4; var10 < var7; ++var10) {
				var11 = var9 << 11 | var10 << 7 | var3;
				var12 = var6 - var3;
				System.arraycopy(var1, var8, this.blocks, var11, var12);
				var8 += var12;
			}
		}

		this.generateHeightMap();

		for(var9 = var2; var9 < var5; ++var9) {
			for(var10 = var4; var10 < var7; ++var10) {
				var11 = (var9 << 11 | var10 << 7 | var3) >> 1;
				var12 = (var6 - var3) / 2;
				System.arraycopy(var1, var8, this.data.data, var11, var12);
				var8 += var12;
			}
		}

		for(var9 = var2; var9 < var5; ++var9) {
			for(var10 = var4; var10 < var7; ++var10) {
				var11 = (var9 << 11 | var10 << 7 | var3) >> 1;
				var12 = (var6 - var3) / 2;
				System.arraycopy(var1, var8, this.blocklightMap.data, var11, var12);
				var8 += var12;
			}
		}

		for(var9 = var2; var9 < var5; ++var9) {
			for(var10 = var4; var10 < var7; ++var10) {
				var11 = (var9 << 11 | var10 << 7 | var3) >> 1;
				var12 = (var6 - var3) / 2;
				System.arraycopy(var1, var8, this.skylightMap.data, var11, var12);
				var8 += var12;
			}
		}

		return var8;
	}

	public Random func_997_a(long var1) {
		return new Random(this.worldObj.getRandomSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ var1);
	}

	public boolean func_21167_h() {
		return false;
	}

	public void func_25124_i() {
		ChunkBlockMap.func_26002_a(this.blocks);
	}
}
