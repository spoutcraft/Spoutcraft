package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.MapGenBase;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class MapGenRavine extends MapGenBase {

	private float[] field_35627_a = new float[1024 * 4]; //Spout increased

	protected void func_35626_a(long var1, int var3, int var4, byte[] var5, double var6, double var8, double var10, float var12, float var13, float var14, int var15, int var16, double var17) {
		Random var19 = new Random(var1);
		double var20 = (double)(var3 * 16 + 8);
		double var22 = (double)(var4 * 16 + 8);
		float var24 = 0.0F;
		float var25 = 0.0F;
		if(var16 <= 0) {
			int var26 = this.range * 16 - 16;
			var16 = var26 - var19.nextInt(var26 / 4);
		}

		boolean var54 = false;
		if(var15 == -1) {
			var15 = var16 / 2;
			var54 = true;
		}

		float var27 = 1.0F;

		for(int var28 = 0; var28 < this.worldObj.worldHeight; ++var28) {
			if(var28 == 0 || var19.nextInt(3) == 0) {
			var27 = 1.0F + var19.nextFloat() * var19.nextFloat() * 1.0F;
			}

			this.field_35627_a[var28] = var27 * var27;
		}

		for(; var15 < var16; ++var15) {
			double var53 = 1.5D + (double)(MathHelper.sin((float)var15 * 3.1415927F / (float)var16) * var12 * 1.0F);
			double var30 = var53 * var17;
			var53 *= (double)var19.nextFloat() * 0.25D + 0.75D;
			var30 *= (double)var19.nextFloat() * 0.25D + 0.75D;
			float var32 = MathHelper.cos(var14);
			float var33 = MathHelper.sin(var14);
			var6 += (double)(MathHelper.cos(var13) * var32);
			var8 += (double)var33;
			var10 += (double)(MathHelper.sin(var13) * var32);
			var14 *= 0.7F;
			var14 += var25 * 0.05F;
			var13 += var24 * 0.05F;
			var25 *= 0.8F;
			var24 *= 0.5F;
			var25 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 2.0F;
			var24 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 4.0F;
			if(var54 || var19.nextInt(4) != 0) {
			double var34 = var6 - var20;
			double var36 = var10 - var22;
			double var38 = (double)(var16 - var15);
			double var40 = (double)(var12 + 2.0F + 16.0F);
			if(var34 * var34 + var36 * var36 - var38 * var38 > var40 * var40) {
				return;
			}

			if(var6 >= var20 - 16.0D - var53 * 2.0D && var10 >= var22 - 16.0D - var53 * 2.0D && var6 <= var20 + 16.0D + var53 * 2.0D && var10 <= var22 + 16.0D + var53 * 2.0D) {
				int var56 = MathHelper.floor_double(var6 - var53) - var3 * 16 - 1;
				int var35 = MathHelper.floor_double(var6 + var53) - var3 * 16 + 1;
				int var55 = MathHelper.floor_double(var8 - var30) - 1;
				int var37 = MathHelper.floor_double(var8 + var30) + 1;
				int var57 = MathHelper.floor_double(var10 - var53) - var4 * 16 - 1;
				int var39 = MathHelper.floor_double(var10 + var53) - var4 * 16 + 1;
				if(var56 < 0) {
					var56 = 0;
				}

				if(var35 > 16) {
					var35 = 16;
				}

				if(var55 < 1) {
					var55 = 1;
				}

				if(var37 > this.worldObj.worldHeight - 8) {
					var37 = this.worldObj.worldHeight - 8;
				}

				if(var57 < 0) {
					var57 = 0;
				}

				if(var39 > 16) {
					var39 = 16;
				}

				boolean var58 = false;

				int var41;
				int var44;
				for(var41 = var56; !var58 && var41 < var35; ++var41) {
					for(int var42 = var57; !var58 && var42 < var39; ++var42) {
						for(int var43 = var37 + 1; !var58 && var43 >= var55 - 1; --var43) {
						var44 = (var41 * 16 + var42) * this.worldObj.worldHeight + var43;
						if(var43 >= 0 && var43 < this.worldObj.worldHeight) {
							if(var5[var44] == Block.waterMoving.blockID || var5[var44] == Block.waterStill.blockID) {
								var58 = true;
							}

							if(var43 != var55 - 1 && var41 != var56 && var41 != var35 - 1 && var42 != var57 && var42 != var39 - 1) {
								var43 = var55;
							}
						}
						}
					}
				}

				if(!var58) {
					for(var41 = var56; var41 < var35; ++var41) {
						double var59 = ((double)(var41 + var3 * 16) + 0.5D - var6) / var53;

						for(var44 = var57; var44 < var39; ++var44) {
						double var45 = ((double)(var44 + var4 * 16) + 0.5D - var10) / var53;
						int var47 = (var41 * 16 + var44) * this.worldObj.worldHeight + var37;
						boolean var48 = false;
						if(var59 * var59 + var45 * var45 < 1.0D) {
							for(int var49 = var37 - 1; var49 >= var55; --var49) {
								double var50 = ((double)var49 + 0.5D - var8) / var30;
								if((var59 * var59 + var45 * var45) * (double)this.field_35627_a[var49] + var50 * var50 / 6.0D < 1.0D) {
									byte var52 = var5[var47];
									if(var52 == Block.grass.blockID) {
									var48 = true;
									}

									if(var52 == Block.stone.blockID || var52 == Block.dirt.blockID || var52 == Block.grass.blockID) {
									if(var49 < 10) {
										var5[var47] = (byte)Block.lavaMoving.blockID;
									} else {
										var5[var47] = 0;
										if(var48 && var5[var47 - 1] == Block.dirt.blockID) {
											var5[var47 - 1] = this.worldObj.getWorldChunkManager().getBiomeGenAt(var41 + var3 * 16, var44 + var4 * 16).topBlock;
										}
									}
									}
								}

								--var47;
							}
						}
						}
					}

					if(var54) {
						break;
					}
				}
			}
			}
		}

	}

	protected void recursiveGenerate(World var1, int var2, int var3, int var4, int var5, byte[] var6) {
		if(this.rand.nextInt(50) == 0) {
			double var7 = (double)(var2 * 16 + this.rand.nextInt(16));
			double var9 = (double)(this.rand.nextInt(this.rand.nextInt(40) + 8) + 20);
			double var11 = (double)(var3 * 16 + this.rand.nextInt(16));
			byte var13 = 1;

			for(int var14 = 0; var14 < var13; ++var14) {
			float var15 = this.rand.nextFloat() * 3.1415927F * 2.0F;
			float var16 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
			float var17 = (this.rand.nextFloat() * 2.0F + this.rand.nextFloat()) * 2.0F;
			this.func_35626_a(this.rand.nextLong(), var4, var5, var6, var7, var9, var11, var17, var15, var16, 0, 0, 3.0D);
			}

		}
	}
}
