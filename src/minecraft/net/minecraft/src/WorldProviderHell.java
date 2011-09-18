package net.minecraft.src;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkProviderHell;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.Vec3D;
import net.minecraft.src.WorldChunkManagerHell;
import net.minecraft.src.WorldProvider;

public class WorldProviderHell extends WorldProvider {

	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 1.0F, 0.0F);
		this.isNether = true;
		this.isHellWorld = true;
		this.hasNoSky = true;
		this.worldType = -1;
	}

	public Vec3D func_4096_a(float var1, float var2) {
		//Spout Start
		Color fogColor = SpoutClient.getInstance().getSkyManager().getFogColor();
		if(fogColor != null) {
			return Vec3D.createVector(fogColor.getRedF(), fogColor.getGreenF(), fogColor.getBlueF());
		} else {
			return Vec3D.createVector(0.20000000298023224D, 0.029999999329447746D, 0.029999999329447746D);
		}
		//Spout End	
	}

	protected void generateLightBrightnessTable() {
		float var1 = 0.1F;

		for(int var2 = 0; var2 <= 15; ++var2) {
			float var3 = 1.0F - (float)var2 / 15.0F;
			this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
		}

	}

	public IChunkProvider getChunkProvider() {
		return new ChunkProviderHell(this.worldObj, this.worldObj.getRandomSeed());
	}

	public boolean canCoordinateBeSpawn(int var1, int var2) {
		int var3 = this.worldObj.getFirstUncoveredBlock(var1, var2);
		return var3 == Block.bedrock.blockID?false:(var3 == 0?false:Block.opaqueCubeLookup[var3]);
	}

	public float calculateCelestialAngle(long var1, float var3) {
		return 0.5F;
	}

	public boolean canRespawnHere() {
		return false;
	}
}
