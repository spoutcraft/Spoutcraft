package net.minecraft.src;

import java.io.PrintStream;
import java.util.*;

public class MapGenStronghold extends MapGenStructure {
	private BiomeGenBase allowedBiomeGenBases[];
	private boolean ranBiomeCheck;
	private ChunkCoordIntPair structureCoords[];

	public MapGenStronghold() {
		allowedBiomeGenBases = (new BiomeGenBase[] {
		            BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.swampland, BiomeGenBase.taiga, BiomeGenBase.icePlains, BiomeGenBase.iceMountains, BiomeGenBase.field_46049_s, BiomeGenBase.field_46048_t, BiomeGenBase.field_46046_v
		        });
		structureCoords = new ChunkCoordIntPair[3];
	}

	protected boolean canSpawnStructureAtCoords(int i, int j) {
		if (!ranBiomeCheck) {
			Random random = new Random();
			random.setSeed(worldObj.getWorldSeed());
			double d = random.nextDouble() * 3.1415926535897931D * 2D;
			for (int i1 = 0; i1 < structureCoords.length; i1++) {
				double d1 = (1.25D + random.nextDouble()) * 32D;
				int j1 = (int)Math.round(Math.cos(d) * d1);
				int k1 = (int)Math.round(Math.sin(d) * d1);
				ArrayList arraylist = new ArrayList();
				BiomeGenBase abiomegenbase[] = allowedBiomeGenBases;
				int l1 = abiomegenbase.length;
				for (int i2 = 0; i2 < l1; i2++) {
					BiomeGenBase biomegenbase = abiomegenbase[i2];
					arraylist.add(biomegenbase);
				}

				ChunkPosition chunkposition = worldObj.getWorldChunkManager().func_35556_a((j1 << 4) + 8, (k1 << 4) + 8, 112, arraylist, random);
				if (chunkposition != null) {
					j1 = chunkposition.x >> 4;
					k1 = chunkposition.z >> 4;
				}
				else {
					System.out.println((new StringBuilder()).append("Placed stronghold in INVALID biome at (").append(j1).append(", ").append(k1).append(")").toString());
				}
				structureCoords[i1] = new ChunkCoordIntPair(j1, k1);
				d += 6.2831853071795862D / (double)structureCoords.length;
			}

			ranBiomeCheck = true;
		}
		ChunkCoordIntPair achunkcoordintpair[] = structureCoords;
		int k = achunkcoordintpair.length;
		for (int l = 0; l < k; l++) {
			ChunkCoordIntPair chunkcoordintpair = achunkcoordintpair[l];
			if (i == chunkcoordintpair.chunkXPos && j == chunkcoordintpair.chunkZPos) {
				System.out.println((new StringBuilder()).append(i).append(", ").append(j).toString());
				return true;
			}
		}

		return false;
	}

	protected List func_40482_a() {
		ArrayList arraylist = new ArrayList();
		ChunkCoordIntPair achunkcoordintpair[] = structureCoords;
		int i = achunkcoordintpair.length;
		for (int j = 0; j < i; j++) {
			ChunkCoordIntPair chunkcoordintpair = achunkcoordintpair[j];
			if (chunkcoordintpair != null) {
				arraylist.add(chunkcoordintpair.func_40737_a(64));
			}
		}

		return arraylist;
	}

	protected StructureStart getStructureStart(int i, int j) {
		StructureStrongholdStart structurestrongholdstart;
		for (structurestrongholdstart = new StructureStrongholdStart(worldObj, rand, i, j); structurestrongholdstart.func_40560_b().isEmpty() || ((ComponentStrongholdStairs2)structurestrongholdstart.func_40560_b().get(0)).field_40009_b == null; structurestrongholdstart = new StructureStrongholdStart(worldObj, rand, i, j)) { }
		return structurestrongholdstart;
	}
}
