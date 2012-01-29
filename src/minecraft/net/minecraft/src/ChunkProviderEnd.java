package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ChunkProviderEnd
	implements IChunkProvider {
	private Random endRNG;
	private NoiseGeneratorOctaves field_40393_j;
	private NoiseGeneratorOctaves field_40394_k;
	private NoiseGeneratorOctaves field_40391_l;
	public NoiseGeneratorOctaves field_40388_a;
	public NoiseGeneratorOctaves field_40386_b;
	private World endWorld;
	private double densities[];
	private BiomeGenBase biomesForGeneration[];
	double field_40387_c[];
	double field_40384_d[];
	double field_40385_e[];
	double field_40382_f[];
	double field_40383_g[];
	int field_40395_h[][];

	public ChunkProviderEnd(World world, long l) {
		field_40395_h = new int[32][32];
		endWorld = world;
		endRNG = new Random(l);
		field_40393_j = new NoiseGeneratorOctaves(endRNG, 16);
		field_40394_k = new NoiseGeneratorOctaves(endRNG, 16);
		field_40391_l = new NoiseGeneratorOctaves(endRNG, 8);
		field_40388_a = new NoiseGeneratorOctaves(endRNG, 10);
		field_40386_b = new NoiseGeneratorOctaves(endRNG, 16);
	}

	public void func_40380_a(int i, int j, byte abyte0[], BiomeGenBase abiomegenbase[]) {
		byte byte0 = 2;
		int k = byte0 + 1;
		int l = endWorld.worldHeight / 4 + 1;
		int i1 = byte0 + 1;
		densities = func_40379_a(densities, i * byte0, 0, j * byte0, k, l, i1);
		for (int j1 = 0; j1 < byte0; j1++) {
			for (int k1 = 0; k1 < byte0; k1++) {
				for (int l1 = 0; l1 < endWorld.worldHeight / 4; l1++) {
					double d = 0.25D;
					double d1 = densities[((j1 + 0) * i1 + (k1 + 0)) * l + (l1 + 0)];
					double d2 = densities[((j1 + 0) * i1 + (k1 + 1)) * l + (l1 + 0)];
					double d3 = densities[((j1 + 1) * i1 + (k1 + 0)) * l + (l1 + 0)];
					double d4 = densities[((j1 + 1) * i1 + (k1 + 1)) * l + (l1 + 0)];
					double d5 = (densities[((j1 + 0) * i1 + (k1 + 0)) * l + (l1 + 1)] - d1) * d;
					double d6 = (densities[((j1 + 0) * i1 + (k1 + 1)) * l + (l1 + 1)] - d2) * d;
					double d7 = (densities[((j1 + 1) * i1 + (k1 + 0)) * l + (l1 + 1)] - d3) * d;
					double d8 = (densities[((j1 + 1) * i1 + (k1 + 1)) * l + (l1 + 1)] - d4) * d;
					for (int i2 = 0; i2 < 4; i2++) {
						double d9 = 0.125D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;
						for (int j2 = 0; j2 < 8; j2++) {
							int k2 = j2 + j1 * 8 << endWorld.xShift | 0 + k1 * 8 << endWorld.heightShift | l1 * 4 + i2;
							int l2 = 1 << endWorld.heightShift;
							double d14 = 0.125D;
							double d15 = d10;
							double d16 = (d11 - d10) * d14;
							for (int i3 = 0; i3 < 8; i3++) {
								int j3 = 0;
								if (d15 > 0.0D) {
									j3 = Block.whiteStone.blockID;
								}
								abyte0[k2] = (byte)j3;
								k2 += l2;
								d15 += d16;
							}

							d10 += d12;
							d11 += d13;
						}

						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
			}
		}
	}

	public void func_40381_b(int i, int j, byte abyte0[], BiomeGenBase abiomegenbase[]) {
		for (int k = 0; k < 16; k++) {
			for (int l = 0; l < 16; l++) {
				int i1 = 1;
				int j1 = -1;
				byte byte0 = (byte)Block.whiteStone.blockID;
				byte byte1 = (byte)Block.whiteStone.blockID;
				for (int k1 = endWorld.worldMaxY; k1 >= 0; k1--) {
					int l1 = (l * 16 + k) * endWorld.worldHeight + k1;
					byte byte2 = abyte0[l1];
					if (byte2 == 0) {
						j1 = -1;
						continue;
					}
					if (byte2 != Block.stone.blockID) {
						continue;
					}
					if (j1 == -1) {
						if (i1 <= 0) {
							byte0 = 0;
							byte1 = (byte)Block.whiteStone.blockID;
						}
						j1 = i1;
						if (k1 >= 0) {
							abyte0[l1] = byte0;
						}
						else {
							abyte0[l1] = byte1;
						}
						continue;
					}
					if (j1 > 0) {
						j1--;
						abyte0[l1] = byte1;
					}
				}
			}
		}
	}

	public Chunk loadChunk(int i, int j) {
		return provideChunk(i, j);
	}

	public Chunk provideChunk(int i, int j) {
		endRNG.setSeed((long)i * 0x4f9939f508L + (long)j * 0x1ef1565bd5L);
		byte abyte0[] = new byte[16 * endWorld.worldHeight * 16];
		Chunk chunk = new Chunk(endWorld, abyte0, i, j);
		biomesForGeneration = endWorld.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, i * 16, j * 16, 16, 16);
		func_40380_a(i, j, abyte0, biomesForGeneration);
		func_40381_b(i, j, abyte0, biomesForGeneration);
		chunk.generateSkylightMap();
		return chunk;
	}

	private double[] func_40379_a(double ad[], int i, int j, int k, int l, int i1, int j1) {
		if (ad == null) {
			ad = new double[l * i1 * j1];
		}
		double d = 684.41200000000003D;
		double d1 = 684.41200000000003D;
		field_40382_f = field_40388_a.func_4109_a(field_40382_f, i, k, l, j1, 1.121D, 1.121D, 0.5D);
		field_40383_g = field_40386_b.func_4109_a(field_40383_g, i, k, l, j1, 200D, 200D, 0.5D);
		d *= 2D;
		field_40387_c = field_40391_l.generateNoiseOctaves(field_40387_c, i, j, k, l, i1, j1, d / 80D, d1 / 160D, d / 80D);
		field_40384_d = field_40393_j.generateNoiseOctaves(field_40384_d, i, j, k, l, i1, j1, d, d1, d);
		field_40385_e = field_40394_k.generateNoiseOctaves(field_40385_e, i, j, k, l, i1, j1, d, d1, d);
		int k1 = 0;
		int l1 = 0;
		for (int i2 = 0; i2 < l; i2++) {
			for (int j2 = 0; j2 < j1; j2++) {
				double d2 = (field_40382_f[l1] + 256D) / 512D;
				if (d2 > 1.0D) {
					d2 = 1.0D;
				}
				double d3 = field_40383_g[l1] / 8000D;
				if (d3 < 0.0D) {
					d3 = -d3 * 0.29999999999999999D;
				}
				d3 = d3 * 3D - 2D;
				float f = (float)((i2 + i) - 0) / 1.0F;
				float f1 = (float)((j2 + k) - 0) / 1.0F;
				float f2 = 100F - MathHelper.sqrt_float(f * f + f1 * f1) * 8F;
				if (f2 > 80F) {
					f2 = 80F;
				}
				if (f2 < -100F) {
					f2 = -100F;
				}
				if (d3 > 1.0D) {
					d3 = 1.0D;
				}
				d3 /= 8D;
				d3 = 0.0D;
				if (d2 < 0.0D) {
					d2 = 0.0D;
				}
				d2 += 0.5D;
				d3 = (d3 * (double)i1) / 16D;
				l1++;
				double d4 = (double)i1 / 2D;
				for (int k2 = 0; k2 < i1; k2++) {
					double d5 = 0.0D;
					double d6 = (((double)k2 - d4) * 8D) / d2;
					if (d6 < 0.0D) {
						d6 *= -1D;
					}
					double d7 = field_40384_d[k1] / 512D;
					double d8 = field_40385_e[k1] / 512D;
					double d9 = (field_40387_c[k1] / 10D + 1.0D) / 2D;
					if (d9 < 0.0D) {
						d5 = d7;
					}
					else if (d9 > 1.0D) {
						d5 = d8;
					}
					else {
						d5 = d7 + (d8 - d7) * d9;
					}
					d5 -= 8D;
					d5 += f2;
					int l2 = 2;
					if (k2 > i1 / 2 - l2) {
						double d10 = (float)(k2 - (i1 / 2 - l2)) / 64F;
						if (d10 < 0.0D) {
							d10 = 0.0D;
						}
						if (d10 > 1.0D) {
							d10 = 1.0D;
						}
						d5 = d5 * (1.0D - d10) + -3000D * d10;
					}
					l2 = 8;
					if (k2 < l2) {
						double d11 = (float)(l2 - k2) / ((float)l2 - 1.0F);
						d5 = d5 * (1.0D - d11) + -30D * d11;
					}
					ad[k1] = d5;
					k1++;
				}
			}
		}

		return ad;
	}

	public boolean chunkExists(int i, int j) {
		return true;
	}

	public void populate(IChunkProvider ichunkprovider, int i, int j) {
		BlockSand.fallInstantly = true;
		int k = i * 16;
		int l = j * 16;
		BiomeGenBase biomegenbase = endWorld.getWorldChunkManager().getBiomeGenAt(k + 16, l + 16);
		biomegenbase.func_35477_a(endWorld, endWorld.rand, k, l);
		BlockSand.fallInstantly = false;
	}

	public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
		return true;
	}

	public boolean unload100OldestChunks() {
		return false;
	}

	public boolean canSave() {
		return true;
	}

	public String makeString() {
		return "RandomLevelSource";
	}

	public List func_40377_a(EnumCreatureType enumcreaturetype, int i, int j, int k) {
		WorldChunkManager worldchunkmanager = endWorld.getWorldChunkManager();
		if (worldchunkmanager == null) {
			return null;
		}
		BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAtChunkCoord(new ChunkCoordIntPair(i >> 4, k >> 4));
		if (biomegenbase == null) {
			return null;
		}
		else {
			return biomegenbase.getSpawnableList(enumcreaturetype);
		}
	}

	public ChunkPosition func_40376_a(World world, String s, int i, int j, int k) {
		return null;
	}
}
