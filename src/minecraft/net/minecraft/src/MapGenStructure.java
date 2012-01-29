package net.minecraft.src;

import java.util.*;

public abstract class MapGenStructure extends MapGenBase {
	protected HashMap coordMap;

	public MapGenStructure() {
		coordMap = new HashMap();
	}

	public void generate(IChunkProvider ichunkprovider, World world, int i, int j, byte abyte0[]) {
		super.generate(ichunkprovider, world, i, j, abyte0);
	}

	protected void recursiveGenerate(World world, int i, int j, int k, int l, byte abyte0[]) {
		if (coordMap.containsKey(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)))) {
			return;
		}
		int i1 = rand.nextInt();
		if (canSpawnStructureAtCoords(i, j)) {
			StructureStart structurestart = getStructureStart(i, j);
			coordMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)), structurestart);
		}
	}

	public boolean generateStructuresInChunk(World world, Random random, int i, int j) {
		int k = (i << 4) + 8;
		int l = (j << 4) + 8;
		boolean flag = false;
		Iterator iterator = coordMap.values().iterator();
		do {
			if (!iterator.hasNext()) {
				break;
			}
			StructureStart structurestart = (StructureStart)iterator.next();
			if (structurestart.isSizeableStructure() && structurestart.getBoundingBox().isInsideStructureBB(k, l, k + 15, l + 15)) {
				structurestart.generateStructure(world, random, new StructureBoundingBox(k, l, k + 15, l + 15));
				flag = true;
			}
		}
		while (true);
		return flag;
	}

	public boolean func_40483_a(int i, int j, int k) {
		Iterator iterator = coordMap.values().iterator();
		label0:
		do {
			if (iterator.hasNext()) {
				StructureStart structurestart = (StructureStart)iterator.next();
				if (!structurestart.isSizeableStructure() || !structurestart.getBoundingBox().isInsideStructureBB(i, k, i, k)) {
					continue;
				}
				Iterator iterator1 = structurestart.func_40560_b().iterator();
				StructureComponent structurecomponent;
				do {
					if (!iterator1.hasNext()) {
						continue label0;
					}
					structurecomponent = (StructureComponent)iterator1.next();
				}
				while (!structurecomponent.getBoundingBox().isVecInside(i, j, k));
				break;
			}
			else {
				return false;
			}
		}
		while (true);
		return true;
	}

	public ChunkPosition func_40484_a(World world, int i, int j, int k) {
		worldObj = world;
		rand.setSeed(world.getWorldSeed());
		long l = rand.nextLong();
		long l1 = rand.nextLong();
		long l2 = (long)(i >> 4) * l;
		long l3 = (long)(k >> 4) * l1;
		rand.setSeed(l2 ^ l3 ^ world.getWorldSeed());
		recursiveGenerate(world, i >> 4, k >> 4, 0, 0, null);
		double d = 1.7976931348623157E+308D;
		ChunkPosition chunkposition = null;
		Object obj = coordMap.values().iterator();
		do {
			if (!((Iterator) (obj)).hasNext()) {
				break;
			}
			StructureStart structurestart = (StructureStart)((Iterator) (obj)).next();
			if (structurestart.isSizeableStructure()) {
				StructureComponent structurecomponent = (StructureComponent)structurestart.func_40560_b().get(0);
				ChunkPosition chunkposition2 = structurecomponent.func_40008_a_();
				int i1 = chunkposition2.x - i;
				int k1 = chunkposition2.y - j;
				int j2 = chunkposition2.z - k;
				double d1 = i1 + i1 * k1 * k1 + j2 * j2;
				if (d1 < d) {
					d = d1;
					chunkposition = chunkposition2;
				}
			}
		}
		while (true);
		if (chunkposition != null) {
			return chunkposition;
		}
		obj = func_40482_a();
		if (obj != null) {
			ChunkPosition chunkposition1 = null;
			Iterator iterator = ((List) (obj)).iterator();
			do {
				if (!iterator.hasNext()) {
					break;
				}
				ChunkPosition chunkposition3 = (ChunkPosition)iterator.next();
				int j1 = chunkposition3.x - i;
				int i2 = chunkposition3.y - j;
				int k2 = chunkposition3.z - k;
				double d2 = j1 + j1 * i2 * i2 + k2 * k2;
				if (d2 < d) {
					d = d2;
					chunkposition1 = chunkposition3;
				}
			}
			while (true);
			return chunkposition1;
		}
		else {
			return null;
		}
	}

	protected List func_40482_a() {
		return null;
	}

	protected abstract boolean canSpawnStructureAtCoords(int i, int j);

	protected abstract StructureStart getStructureStart(int i, int j);
}
