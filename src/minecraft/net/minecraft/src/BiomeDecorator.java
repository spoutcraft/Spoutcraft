package net.minecraft.src;

import java.util.Random;

public class BiomeDecorator {
	protected World currentWorld;
	protected Random randomGenerator;
	protected int chunk_X;
	protected int chunk_Z;
	protected BiomeGenBase biome;
	protected WorldGenerator clayGen;
	protected WorldGenerator sandGen;
	protected WorldGenerator gravelAsSandGen;
	protected WorldGenerator dirtGen;
	protected WorldGenerator gravelGen;
	protected WorldGenerator coalGen;
	protected WorldGenerator ironGen;
	protected WorldGenerator goldGen;
	protected WorldGenerator redstoneGen;
	protected WorldGenerator diamondGen;
	protected WorldGenerator lapisGen;
	protected WorldGenerator plantYellowGen;
	protected WorldGenerator plantRedGen;
	protected WorldGenerator mushroomBrownGen;
	protected WorldGenerator mushroomRedGen;
	protected WorldGenerator bigMushroomGen;
	protected WorldGenerator reedGen;
	protected WorldGenerator cactusGen;
	protected WorldGenerator waterlilyGen;
	protected int waterlilyPerChunk;
	protected int treesPerChunk;
	protected int flowersPerChunk;
	protected int grassPerChunk;
	protected int deadBushPerChunk;
	protected int mushroomsPerChunk;
	protected int reedsPerChunk;
	protected int cactiPerChunk;
	protected int sandPerChunk;
	protected int sandPerChunk2;
	protected int clayPerChunk;
	protected int bigMushroomsPerChunk;
	public boolean generateLakes;

	public BiomeDecorator(BiomeGenBase biomegenbase) {
		clayGen = new WorldGenClay(4);
		sandGen = new WorldGenSand(7, Block.sand.blockID);
		gravelAsSandGen = new WorldGenSand(6, Block.gravel.blockID);
		dirtGen = new WorldGenMinable(Block.dirt.blockID, 32);
		gravelGen = new WorldGenMinable(Block.gravel.blockID, 32);
		coalGen = new WorldGenMinable(Block.oreCoal.blockID, 16);
		ironGen = new WorldGenMinable(Block.oreIron.blockID, 8);
		goldGen = new WorldGenMinable(Block.oreGold.blockID, 8);
		redstoneGen = new WorldGenMinable(Block.oreRedstone.blockID, 7);
		diamondGen = new WorldGenMinable(Block.oreDiamond.blockID, 7);
		lapisGen = new WorldGenMinable(Block.oreLapis.blockID, 6);
		plantYellowGen = new WorldGenFlowers(Block.plantYellow.blockID);
		plantRedGen = new WorldGenFlowers(Block.plantRed.blockID);
		mushroomBrownGen = new WorldGenFlowers(Block.mushroomBrown.blockID);
		mushroomRedGen = new WorldGenFlowers(Block.mushroomRed.blockID);
		bigMushroomGen = new WorldGenBigMushroom();
		reedGen = new WorldGenReed();
		cactusGen = new WorldGenCactus();
		waterlilyGen = new MapGenWaterlily();
		waterlilyPerChunk = 0;
		treesPerChunk = 0;
		flowersPerChunk = 2;
		grassPerChunk = 1;
		deadBushPerChunk = 0;
		mushroomsPerChunk = 0;
		reedsPerChunk = 0;
		cactiPerChunk = 0;
		sandPerChunk = 1;
		sandPerChunk2 = 3;
		clayPerChunk = 1;
		bigMushroomsPerChunk = 0;
		generateLakes = true;
		biome = biomegenbase;
	}

	public void decorate(World world, Random random, int i, int j) {
		if (currentWorld != null) {
			throw new RuntimeException("Already decorating!!");
		}
		else {
			currentWorld = world;
			randomGenerator = random;
			chunk_X = i;
			chunk_Z = j;
			decorate();
			currentWorld = null;
			randomGenerator = null;
			return;
		}
	}

	protected void decorate() {
		generateOres();
		for (int i = 0; i < sandPerChunk2; i++) {
			int i1 = chunk_X + randomGenerator.nextInt(16) + 8;
			int k5 = chunk_Z + randomGenerator.nextInt(16) + 8;
			sandGen.generate(currentWorld, randomGenerator, i1, currentWorld.getTopSolidOrLiquidBlock(i1, k5), k5);
		}

		for (int j = 0; j < clayPerChunk; j++) {
			int j1 = chunk_X + randomGenerator.nextInt(16) + 8;
			int l5 = chunk_Z + randomGenerator.nextInt(16) + 8;
			clayGen.generate(currentWorld, randomGenerator, j1, currentWorld.getTopSolidOrLiquidBlock(j1, l5), l5);
		}

		for (int k = 0; k < sandPerChunk; k++) {
			int k1 = chunk_X + randomGenerator.nextInt(16) + 8;
			int i6 = chunk_Z + randomGenerator.nextInt(16) + 8;
			sandGen.generate(currentWorld, randomGenerator, k1, currentWorld.getTopSolidOrLiquidBlock(k1, i6), i6);
		}

		int l = treesPerChunk;
		if (randomGenerator.nextInt(10) == 0) {
			l++;
		}
		for (int l1 = 0; l1 < l; l1++) {
			int j6 = chunk_X + randomGenerator.nextInt(16) + 8;
			int k10 = chunk_Z + randomGenerator.nextInt(16) + 8;
			WorldGenerator worldgenerator = biome.getRandomWorldGenForTrees(randomGenerator);
			worldgenerator.func_517_a(1.0D, 1.0D, 1.0D);
			worldgenerator.generate(currentWorld, randomGenerator, j6, currentWorld.getHeightValue(j6, k10), k10);
		}

		for (int i2 = 0; i2 < bigMushroomsPerChunk; i2++) {
			int k6 = chunk_X + randomGenerator.nextInt(16) + 8;
			int l10 = chunk_Z + randomGenerator.nextInt(16) + 8;
			bigMushroomGen.generate(currentWorld, randomGenerator, k6, currentWorld.getHeightValue(k6, l10), l10);
		}

		for (int j2 = 0; j2 < flowersPerChunk; j2++) {
			int l6 = chunk_X + randomGenerator.nextInt(16) + 8;
			int i11 = randomGenerator.nextInt(currentWorld.worldHeight);
			int l14 = chunk_Z + randomGenerator.nextInt(16) + 8;
			plantYellowGen.generate(currentWorld, randomGenerator, l6, i11, l14);
			if (randomGenerator.nextInt(4) == 0) {
				int i7 = chunk_X + randomGenerator.nextInt(16) + 8;
				int j11 = randomGenerator.nextInt(currentWorld.worldHeight);
				int i15 = chunk_Z + randomGenerator.nextInt(16) + 8;
				plantRedGen.generate(currentWorld, randomGenerator, i7, j11, i15);
			}
		}

		for (int k2 = 0; k2 < grassPerChunk; k2++) {
			int j7 = 1;
			int k11 = chunk_X + randomGenerator.nextInt(16) + 8;
			int j15 = randomGenerator.nextInt(currentWorld.worldHeight);
			int l17 = chunk_Z + randomGenerator.nextInt(16) + 8;
			(new WorldGenTallGrass(Block.tallGrass.blockID, j7)).generate(currentWorld, randomGenerator, k11, j15, l17);
		}

		for (int l2 = 0; l2 < deadBushPerChunk; l2++) {
			int k7 = chunk_X + randomGenerator.nextInt(16) + 8;
			int l11 = randomGenerator.nextInt(currentWorld.worldHeight);
			int k15 = chunk_Z + randomGenerator.nextInt(16) + 8;
			(new WorldGenDeadBush(Block.deadBush.blockID)).generate(currentWorld, randomGenerator, k7, l11, k15);
		}

		for (int i3 = 0; i3 < waterlilyPerChunk; i3++) {
			int l7 = chunk_X + randomGenerator.nextInt(16) + 8;
			int i12 = chunk_Z + randomGenerator.nextInt(16) + 8;
			int l15;
			for (l15 = randomGenerator.nextInt(currentWorld.worldHeight); l15 > 0 && currentWorld.getBlockId(l7, l15 - 1, i12) == 0; l15--) { }
			waterlilyGen.generate(currentWorld, randomGenerator, l7, l15, i12);
		}

		for (int j3 = 0; j3 < mushroomsPerChunk; j3++) {
			if (randomGenerator.nextInt(4) == 0) {
				int i8 = chunk_X + randomGenerator.nextInt(16) + 8;
				int j12 = chunk_Z + randomGenerator.nextInt(16) + 8;
				int i16 = currentWorld.getHeightValue(i8, j12);
				mushroomBrownGen.generate(currentWorld, randomGenerator, i8, i16, j12);
			}
			if (randomGenerator.nextInt(8) == 0) {
				int j8 = chunk_X + randomGenerator.nextInt(16) + 8;
				int k12 = chunk_Z + randomGenerator.nextInt(16) + 8;
				int j16 = randomGenerator.nextInt(currentWorld.worldHeight);
				mushroomRedGen.generate(currentWorld, randomGenerator, j8, j16, k12);
			}
		}

		if (randomGenerator.nextInt(4) == 0) {
			int k3 = chunk_X + randomGenerator.nextInt(16) + 8;
			int k8 = randomGenerator.nextInt(currentWorld.worldHeight);
			int l12 = chunk_Z + randomGenerator.nextInt(16) + 8;
			mushroomBrownGen.generate(currentWorld, randomGenerator, k3, k8, l12);
		}
		if (randomGenerator.nextInt(8) == 0) {
			int l3 = chunk_X + randomGenerator.nextInt(16) + 8;
			int l8 = randomGenerator.nextInt(currentWorld.worldHeight);
			int i13 = chunk_Z + randomGenerator.nextInt(16) + 8;
			mushroomRedGen.generate(currentWorld, randomGenerator, l3, l8, i13);
		}
		for (int i4 = 0; i4 < reedsPerChunk; i4++) {
			int i9 = chunk_X + randomGenerator.nextInt(16) + 8;
			int j13 = chunk_Z + randomGenerator.nextInt(16) + 8;
			int k16 = randomGenerator.nextInt(currentWorld.worldHeight);
			reedGen.generate(currentWorld, randomGenerator, i9, k16, j13);
		}

		for (int j4 = 0; j4 < 10; j4++) {
			int j9 = chunk_X + randomGenerator.nextInt(16) + 8;
			int k13 = randomGenerator.nextInt(currentWorld.worldHeight);
			int l16 = chunk_Z + randomGenerator.nextInt(16) + 8;
			reedGen.generate(currentWorld, randomGenerator, j9, k13, l16);
		}

		if (randomGenerator.nextInt(32) == 0) {
			int k4 = chunk_X + randomGenerator.nextInt(16) + 8;
			int k9 = randomGenerator.nextInt(currentWorld.worldHeight);
			int l13 = chunk_Z + randomGenerator.nextInt(16) + 8;
			(new WorldGenPumpkin()).generate(currentWorld, randomGenerator, k4, k9, l13);
		}
		for (int l4 = 0; l4 < cactiPerChunk; l4++) {
			int l9 = chunk_X + randomGenerator.nextInt(16) + 8;
			int i14 = randomGenerator.nextInt(currentWorld.worldHeight);
			int i17 = chunk_Z + randomGenerator.nextInt(16) + 8;
			cactusGen.generate(currentWorld, randomGenerator, l9, i14, i17);
		}

		if (generateLakes) {
			for (int i5 = 0; i5 < 50; i5++) {
				int i10 = chunk_X + randomGenerator.nextInt(16) + 8;
				int j14 = randomGenerator.nextInt(randomGenerator.nextInt(currentWorld.worldHeight - 8) + 8);
				int j17 = chunk_Z + randomGenerator.nextInt(16) + 8;
				(new WorldGenLiquids(Block.waterMoving.blockID)).generate(currentWorld, randomGenerator, i10, j14, j17);
			}

			for (int j5 = 0; j5 < 20; j5++) {
				int j10 = chunk_X + randomGenerator.nextInt(16) + 8;
				int k14 = randomGenerator.nextInt(randomGenerator.nextInt(randomGenerator.nextInt(currentWorld.worldHeight - 16) + 8) + 8);
				int k17 = chunk_Z + randomGenerator.nextInt(16) + 8;
				(new WorldGenLiquids(Block.lavaMoving.blockID)).generate(currentWorld, randomGenerator, j10, k14, k17);
			}
		}
	}

	protected void genStandardOre1(int i, WorldGenerator worldgenerator, int j, int k) {
		for (int l = 0; l < i; l++) {
			int i1 = chunk_X + randomGenerator.nextInt(16);
			int j1 = randomGenerator.nextInt(k - j) + j;
			int k1 = chunk_Z + randomGenerator.nextInt(16);
			worldgenerator.generate(currentWorld, randomGenerator, i1, j1, k1);
		}
	}

	protected void genStandardOre2(int i, WorldGenerator worldgenerator, int j, int k) {
		for (int l = 0; l < i; l++) {
			int i1 = chunk_X + randomGenerator.nextInt(16);
			int j1 = randomGenerator.nextInt(k) + randomGenerator.nextInt(k) + (j - k);
			int k1 = chunk_Z + randomGenerator.nextInt(16);
			worldgenerator.generate(currentWorld, randomGenerator, i1, j1, k1);
		}
	}

	protected void generateOres() {
		genStandardOre1(20, dirtGen, 0, currentWorld.worldHeight);
		genStandardOre1(10, gravelGen, 0, currentWorld.worldHeight);
		genStandardOre1(20, coalGen, 0, currentWorld.worldHeight);
		genStandardOre1(20, ironGen, 0, currentWorld.worldHeight / 2);
		genStandardOre1(2, goldGen, 0, currentWorld.worldHeight / 4);
		genStandardOre1(8, redstoneGen, 0, currentWorld.worldHeight / 8);
		genStandardOre1(1, diamondGen, 0, currentWorld.worldHeight / 8);
		genStandardOre2(1, lapisGen, currentWorld.worldHeight / 8, currentWorld.worldHeight / 8);
	}
}
