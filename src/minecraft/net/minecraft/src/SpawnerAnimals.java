package net.minecraft.src;

import gnu.trove.iterator.TLongIterator;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.BlockBed;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.PathEntity;
import net.minecraft.src.PathPoint;
import net.minecraft.src.Pathfinder;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.WeightedRandom;
import net.minecraft.src.World;
//Spout start
import org.spoutcraft.spoutcraftapi.util.map.TIntPairHashSet;

//Spout end

public final class SpawnerAnimals {

	//Spout start
	private static TIntPairHashSet eligibleChunksForSpawning = new TIntPairHashSet();
	//Spout end
	protected static final Class[] nightSpawnEntities = new Class[]{EntitySpider.class, EntityZombie.class, EntitySkeleton.class};


	protected static ChunkPosition getRandomSpawningPointInChunk(World var0, int var1, int var2) {
		int var3 = var1 + var0.rand.nextInt(16);
		Random var10000 = var0.rand;
		var0.getClass();
		int var4 = var10000.nextInt(128);
		int var5 = var2 + var0.rand.nextInt(16);
		return new ChunkPosition(var3, var4, var5);
	}

	public static final int performSpawning(World var0, boolean var1, boolean var2) {
		if(!var1 && !var2) {
			return 0;
		} else {
			eligibleChunksForSpawning.clear();

			int var3;
			int var6;
			for(var3 = 0; var3 < var0.playerEntities.size(); ++var3) {
				EntityPlayer var4 = (EntityPlayer)var0.playerEntities.get(var3);
				int var5 = MathHelper.floor_double(var4.posX / 16.0D);
				var6 = MathHelper.floor_double(var4.posZ / 16.0D);
				byte var7 = 8;

				for(int var8 = -var7; var8 <= var7; ++var8) {
					for(int var9 = -var7; var9 <= var7; ++var9) {
						eligibleChunksForSpawning.add(var8 + var5, var9 + var6); //Spout
					}
				}
			}

			var3 = 0;
			ChunkCoordinates var33 = var0.getSpawnPoint();
			EnumCreatureType[] var34 = EnumCreatureType.values();
			var6 = var34.length;

			for(int var35 = 0; var35 < var6; ++var35) {
				EnumCreatureType var36 = var34[var35];
				if((!var36.getPeacefulCreature() || var2) && (var36.getPeacefulCreature() || var1) && var0.countEntities(var36.getCreatureClass()) <= var36.getMaxNumberOfCreature() * eligibleChunksForSpawning.size() / 256) {
					TLongIterator var37 = eligibleChunksForSpawning.iterator(); //Spout

					label91:
					while(var37.hasNext()) {
						//Spout start
						long next = var37.next();
						int chunkX = TIntPairHashSet.longToKey1(next);
						int chunkZ = TIntPairHashSet.longToKey2(next);
						BiomeGenBase var11 = var0.getWorldChunkManager().getBiomeGenAt(chunkX, chunkZ);
						//Spout end
						List var12 = var11.getSpawnableList(var36);
						if(var12 != null && !var12.isEmpty()) {
							SpawnListEntry var13 = (SpawnListEntry)WeightedRandom.func_35733_a(var0.rand, var12);
							ChunkPosition var14 = getRandomSpawningPointInChunk(var0, chunkX * 16, chunkZ * 16); //Spout
							int var15 = var14.x;
							int var16 = var14.y;
							int var17 = var14.z;
							if(!var0.isBlockNormalCube(var15, var16, var17) && var0.getBlockMaterial(var15, var16, var17) == var36.getCreatureMaterial()) {
								int var18 = 0;

								for(int var19 = 0; var19 < 3; ++var19) {
									int var20 = var15;
									int var21 = var16;
									int var22 = var17;
									byte var23 = 6;

									for(int var24 = 0; var24 < 4; ++var24) {
										var20 += var0.rand.nextInt(var23) - var0.rand.nextInt(var23);
										var21 += var0.rand.nextInt(1) - var0.rand.nextInt(1);
										var22 += var0.rand.nextInt(var23) - var0.rand.nextInt(var23);
										if(canCreatureTypeSpawnAtLocation(var36, var0, var20, var21, var22)) {
											float var25 = (float)var20 + 0.5F;
											float var26 = (float)var21;
											float var27 = (float)var22 + 0.5F;
											if(var0.getClosestPlayer((double)var25, (double)var26, (double)var27, 24.0D) == null) {
												float var28 = var25 - (float)var33.posX;
												float var29 = var26 - (float)var33.posY;
												float var30 = var27 - (float)var33.posZ;
												float var31 = var28 * var28 + var29 * var29 + var30 * var30;
												if(var31 >= 576.0F) {
													EntityLiving var38;
													try {
														var38 = (EntityLiving)var13.entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{var0});
													} catch (Exception var32) {
														var32.printStackTrace();
														return var3;
													}

													var38.setLocationAndAngles((double)var25, (double)var26, (double)var27, var0.rand.nextFloat() * 360.0F, 0.0F);
													if(var38.getCanSpawnHere()) {
														++var18;
														var0.entityJoinedWorld(var38);
														creatureSpecificInit(var38, var0, var25, var26, var27);
														if(var18 >= var38.getMaxSpawnedInChunk()) {
															continue label91;
														}
													}

													var3 += var18;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			return var3;
		}
	}

	private static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType var0, World var1, int var2, int var3, int var4) {
		return var0.getCreatureMaterial() == Material.water?var1.getBlockMaterial(var2, var3, var4).getIsLiquid() && !var1.isBlockNormalCube(var2, var3 + 1, var4):var1.isBlockNormalCube(var2, var3 - 1, var4) && !var1.isBlockNormalCube(var2, var3, var4) && !var1.getBlockMaterial(var2, var3, var4).getIsLiquid() && !var1.isBlockNormalCube(var2, var3 + 1, var4);
	}

	private static void creatureSpecificInit(EntityLiving var0, World var1, float var2, float var3, float var4) {
		if(var0 instanceof EntitySpider && var1.rand.nextInt(100) == 0) {
			EntitySkeleton var5 = new EntitySkeleton(var1);
			var5.setLocationAndAngles((double)var2, (double)var3, (double)var4, var0.rotationYaw, 0.0F);
			var1.entityJoinedWorld(var5);
			var5.mountEntity(var0);
		} else if(var0 instanceof EntitySheep) {
			((EntitySheep)var0).setFleeceColor(EntitySheep.getRandomFleeceColor(var1.rand));
		}

	}

	public static boolean performSleepSpawning(World var0, List var1) {
		boolean var2 = false;
		Pathfinder var3 = new Pathfinder(var0);
		Iterator var4 = var1.iterator();

		while(var4.hasNext()) {
			EntityPlayer var5 = (EntityPlayer)var4.next();
			Class[] var6 = nightSpawnEntities;
			if(var6 != null && var6.length != 0) {
				boolean var7 = false;

				for(int var8 = 0; var8 < 20 && !var7; ++var8) {
					int var9 = MathHelper.floor_double(var5.posX) + var0.rand.nextInt(32) - var0.rand.nextInt(32);
					int var10 = MathHelper.floor_double(var5.posZ) + var0.rand.nextInt(32) - var0.rand.nextInt(32);
					int var11 = MathHelper.floor_double(var5.posY) + var0.rand.nextInt(16) - var0.rand.nextInt(16);
					if(var11 < 1) {
						var11 = 1;
					} else {
						var0.getClass();
						if(var11 > 128) {
							var0.getClass();
							var11 = 128;
						}
					}

					int var12 = var0.rand.nextInt(var6.length);

					int var13;
					for(var13 = var11; var13 > 2 && !var0.isBlockNormalCube(var9, var13 - 1, var10); --var13) {
						;
					}

					while(!canCreatureTypeSpawnAtLocation(EnumCreatureType.monster, var0, var9, var13, var10) && var13 < var11 + 16) {
						var0.getClass();
						if(var13 >= 128) {
							break;
						}

						++var13;
					}

					if(var13 < var11 + 16) {
						var0.getClass();
						if(var13 < 128) {
							float var14 = (float)var9 + 0.5F;
							float var15 = (float)var13;
							float var16 = (float)var10 + 0.5F;

							EntityLiving var17;
							try {
								var17 = (EntityLiving)var6[var12].getConstructor(new Class[]{World.class}).newInstance(new Object[]{var0});
							} catch (Exception var21) {
								var21.printStackTrace();
								return var2;
							}

							var17.setLocationAndAngles((double)var14, (double)var15, (double)var16, var0.rand.nextFloat() * 360.0F, 0.0F);
							if(var17.getCanSpawnHere()) {
								PathEntity var18 = var3.createEntityPathTo(var17, var5, 32.0F);
								if(var18 != null && var18.pathLength > 1) {
									PathPoint var19 = var18.getPathEnd();
									if(Math.abs((double)var19.xCoord - var5.posX) < 1.5D && Math.abs((double)var19.zCoord - var5.posZ) < 1.5D && Math.abs((double)var19.yCoord - var5.posY) < 1.5D) {
										ChunkCoordinates var20 = BlockBed.getNearestEmptyChunkCoordinates(var0, MathHelper.floor_double(var5.posX), MathHelper.floor_double(var5.posY), MathHelper.floor_double(var5.posZ), 1);
										if(var20 == null) {
											var20 = new ChunkCoordinates(var9, var13 + 1, var10);
										}

										var17.setLocationAndAngles((double)((float)var20.posX + 0.5F), (double)var20.posY, (double)((float)var20.posZ + 0.5F), 0.0F, 0.0F);
										var0.entityJoinedWorld(var17);
										creatureSpecificInit(var17, var0, (float)var20.posX + 0.5F, (float)var20.posY, (float)var20.posZ + 0.5F);
										var5.wakeUpPlayer(true, false, false);
										var17.playLivingSound();
										var2 = true;
										var7 = true;
									}
								}
							}
						}
					}
				}
			}
		}

		return var2;
	}

	public static void func_35957_a(World var0, BiomeGenBase var1, int var2, int var3, int var4, int var5, Random var6) {
		List var7 = var1.getSpawnableList(EnumCreatureType.creature);
		if(!var7.isEmpty()) {
			while(var6.nextFloat() < var1.getBiome()) {
				SpawnListEntry var8 = (SpawnListEntry)WeightedRandom.func_35733_a(var0.rand, var7);
				int var9 = var8.field_35591_b + var6.nextInt(1 + var8.field_35592_c - var8.field_35591_b);
				int var10 = var2 + var6.nextInt(var4);
				int var11 = var3 + var6.nextInt(var5);
				int var12 = var10;
				int var13 = var11;

				for(int var14 = 0; var14 < var9; ++var14) {
					boolean var15 = false;

					for(int var16 = 0; !var15 && var16 < 4; ++var16) {
						int var17 = var0.getTopSolidOrLiquidBlock(var10, var11);
						if(canCreatureTypeSpawnAtLocation(EnumCreatureType.creature, var0, var10, var17, var11)) {
							float var18 = (float)var10 + 0.5F;
							float var19 = (float)var17;
							float var20 = (float)var11 + 0.5F;

							EntityLiving var21;
							try {
								var21 = (EntityLiving)var8.entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{var0});
							} catch (Exception var23) {
								var23.printStackTrace();
								continue;
							}

							var21.setLocationAndAngles((double)var18, (double)var19, (double)var20, var6.nextFloat() * 360.0F, 0.0F);
							var0.entityJoinedWorld(var21);
							creatureSpecificInit(var21, var0, var18, var19, var20);
							var15 = true;
						}

						var10 += var6.nextInt(5) - var6.nextInt(5);

						for(var11 += var6.nextInt(5) - var6.nextInt(5); var10 < var2 || var10 >= var2 + var4 || var11 < var3 || var11 >= var3 + var4; var11 = var13 + var6.nextInt(5) - var6.nextInt(5)) {
							var10 = var12 + var6.nextInt(5) - var6.nextInt(5);
						}
					}
				}
			}

		}
	}

}
