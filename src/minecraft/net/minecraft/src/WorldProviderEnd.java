package net.minecraft.src;

public class WorldProviderEnd extends WorldProvider {
	public WorldProviderEnd() {
	}

	public void registerWorldChunkManager() {
		worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.5F, 0.0F);
		worldType = 1;
		hasNoSky = true;
		isAlternateDimension = true;
	}

	public IChunkProvider getChunkProvider() {
		return new ChunkProviderEnd(worldObj, worldObj.getWorldSeed());
	}

	public float calculateCelestialAngle(long l, float f) {
		return 0.0F;
	}

	public float[] calcSunriseSunsetColors(float f, float f1) {
		return null;
	}

	public Vec3D getFogColor(float f, float f1) {
		int i = 0x8080a0;
		float f2 = MathHelper.cos(f * 3.141593F * 2.0F) * 2.0F + 0.5F;
		if (f2 < 0.0F) {
			f2 = 0.0F;
		}
		if (f2 > 1.0F) {
			f2 = 1.0F;
		}
		float f3 = (float)(i >> 16 & 0xff) / 255F;
		float f4 = (float)(i >> 8 & 0xff) / 255F;
		float f5 = (float)(i & 0xff) / 255F;
		f3 *= f2 * 0.0F + 0.15F;
		f4 *= f2 * 0.0F + 0.15F;
		f5 *= f2 * 0.0F + 0.15F;
		return Vec3D.createVector(f3, f4, f5);
	}

	public boolean func_28112_c() {
		return false;
	}

	public boolean canRespawnHere() {
		return false;
	}

	public float getCloudHeight() {
		return 8F;
	}

	public boolean canCoordinateBeSpawn(int i, int j) {
		int k = worldObj.getFirstUncoveredBlock(i, j);
		if (k == 0) {
			return false;
		}
		else {
			return Block.blocksList[k].blockMaterial.getIsSolid();
		}
	}

	public ChunkCoordinates getEntrancePortalLocation() {
		return new ChunkCoordinates(100, 50, 0);
	}

	public int func_46066_g() {
		return 50;
	}
}
