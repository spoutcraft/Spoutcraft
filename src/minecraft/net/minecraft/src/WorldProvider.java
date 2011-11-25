package net.minecraft.src;

import net.minecraft.src.Block;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ChunkProviderGenerate;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import net.minecraft.src.WorldChunkManager;
import net.minecraft.src.WorldProviderEnd;
import net.minecraft.src.WorldProviderHell;
import net.minecraft.src.WorldProviderSurface;
//Spout Start
import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.gui.Color;
//Spout End
public abstract class WorldProvider {

	public World worldObj;
	public WorldChunkManager worldChunkMgr;
	public boolean isNether = false;
	public boolean isHellWorld = false;
	public boolean hasNoSky = false;
	public float[] lightBrightnessTable = new float[16];
	public int worldType = 0;
	private float[] colorsSunriseSunset = new float[4];

	public final void registerWorld(World var1) {
		this.worldObj = var1;
		this.registerWorldChunkManager();
		this.generateLightBrightnessTable();
	}

	protected void generateLightBrightnessTable() {
		float var1 = 0.0F;

		for (int var2 = 0; var2 <= 15; ++var2) {
			float var3 = 1.0F - (float) var2 / 15.0F;
			this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
		}

	}

	protected void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManager(this.worldObj);
	}

	public IChunkProvider getChunkProvider() {
		return new ChunkProviderGenerate(this.worldObj, this.worldObj.getWorldSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
	}

	public boolean canCoordinateBeSpawn(int var1, int var2) {
		int var3 = this.worldObj.getFirstUncoveredBlock(var1, var2);
		return var3 == Block.grass.blockID;
	}

	public float calculateCelestialAngle(long var1, float var3) {
		int var4 = (int) (var1 % 24000L);
		float var5 = ((float) var4 + var3) / 24000.0F - 0.25F;
		if (var5 < 0.0F) {
			++var5;
		}

		if (var5 > 1.0F) {
			--var5;
		}

		float var6 = var5;
		var5 = 1.0F - (float) ((Math.cos((double) var5 * 3.141592653589793D) + 1.0D) / 2.0D);
		var5 = var6 + (var5 - var6) / 3.0F;
		return var5;
	}

	public int func_40470_b(long var1, float var3) {
		return (int) (var1 / 24000L) % 8;
	}

	public float[] calcSunriseSunsetColors(float var1, float var2) {
		float var3 = 0.4F;
		float var4 = MathHelper.cos(var1 * 3.1415927F * 2.0F) - 0.0F;
		float var5 = -0.0F;
		if (var4 >= var5 - var3 && var4 <= var5 + var3) {
			float var6 = (var4 - var5) / var3 * 0.5F + 0.5F;
			float var7 = 1.0F - (1.0F - MathHelper.sin(var6 * 3.1415927F)) * 0.99F;
			var7 *= var7;
			this.colorsSunriseSunset[0] = var6 * 0.3F + 0.7F;
			this.colorsSunriseSunset[1] = var6 * var6 * 0.7F + 0.2F;
			this.colorsSunriseSunset[2] = var6 * var6 * 0.0F + 0.2F;
			this.colorsSunriseSunset[3] = var7;
			return this.colorsSunriseSunset;
		} else {
			return null;
		}
	}

	public Vec3D getFogColor(float var1, float var2) {
		float var3 = MathHelper.cos(var1 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
		if (var3 < 0.0F) {
			var3 = 0.0F;
		}

		if (var3 > 1.0F) {
			var3 = 1.0F;
		}
		//Spout Start
		
		float var4 = 0.7529412F;
		float var5 = 0.8470588F;
		float var6 = 1.0F;
		Color fogColor = SpoutClient.getInstance().getSkyManager().getFogColor();
		if(fogColor != null){
			var4 = fogColor.getRedF();
			var5 = fogColor.getGreenF();
			var6 = fogColor.getBlueF();
		}
		//Spout End
		var4 *= var3 * 0.94F + 0.06F;
		var5 *= var3 * 0.94F + 0.06F;
		var6 *= var3 * 0.91F + 0.09F;
		return Vec3D.createVector((double) var4, (double) var5, (double) var6);
	}

	public boolean canRespawnHere() {
		return true;
	}

	public static WorldProvider getProviderForDimension(int var0) {
		return (WorldProvider) (var0 == -1 ? new WorldProviderHell() : (var0 == 0 ? new WorldProviderSurface() : (var0 == 1 ? new WorldProviderEnd() : null)));
	}

	public float getCloudHeight() {
		return (float) this.worldObj.field_35472_c;
	}

	public boolean func_28112_c() {
		return true;
	}

	public ChunkCoordinates func_40469_f() {
		return null;
	}
}
