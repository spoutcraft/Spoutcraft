package net.minecraft.src;

//Spout Start
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.gui.Color;
import com.pclewis.mcpatcher.mod.Colorizer;
//Spout End
public abstract class WorldProvider {
	public World worldObj;
	public WorldType terrainType;
	public WorldChunkManager worldChunkMgr;
	public boolean isHellWorld = false;
	public boolean hasNoSky = false;
	public float[] lightBrightnessTable = new float[16];
	public int worldType = 0;
	private float[] colorsSunriseSunset = new float[4];

	public final void registerWorld(World par1World) {
		this.worldObj = par1World;
		this.terrainType = par1World.getWorldInfo().getTerrainType();
		this.registerWorldChunkManager();
		this.generateLightBrightnessTable();
	}

	protected void generateLightBrightnessTable() {
		float var1 = 0.0F;

		for (int var2 = 0; var2 <= 15; ++var2) {
			float var3 = 1.0F - (float)var2 / 15.0F;
			this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
		}
	}

	protected void registerWorldChunkManager() {
		if (this.worldObj.getWorldInfo().getTerrainType() == WorldType.field_48636_c) {
			this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 0.5F, 0.5F);
		} else {
			this.worldChunkMgr = new WorldChunkManager(this.worldObj);
		}
	}

	public IChunkProvider getChunkProvider() {
		return (IChunkProvider)(this.terrainType == WorldType.field_48636_c?new ChunkProviderFlat(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled()):new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled()));
	}

	public boolean canCoordinateBeSpawn(int par1, int par2) {
		int var3 = this.worldObj.getFirstUncoveredBlock(par1, par2);
		return var3 == Block.grass.blockID;
	}

	public float calculateCelestialAngle(long par1, float par3) {
		int var4 = (int)(par1 % 24000L);
		float var5 = ((float)var4 + par3) / 24000.0F - 0.25F;
		if (var5 < 0.0F) {
			++var5;
		}

		if (var5 > 1.0F) {
			--var5;
		}

		float var6 = var5;
		var5 = 1.0F - (float)((Math.cos((double)var5 * Math.PI) + 1.0D) / 2.0D);
		var5 = var6 + (var5 - var6) / 3.0F;
		return var5;
	}

	public int getMoonPhase(long par1, float par3) {
		return (int)(par1 / 24000L) % 8;
	}

	public boolean func_48217_e() {
		return true;
	}

	public float[] calcSunriseSunsetColors(float par1, float par2) {
		float var3 = 0.4F;
		float var4 = MathHelper.cos(par1 * (float)Math.PI * 2.0F) - 0.0F;
		float var5 = -0.0F;
		if (var4 >= var5 - var3 && var4 <= var5 + var3) {
			float var6 = (var4 - var5) / var3 * 0.5F + 0.5F;
			float var7 = 1.0F - (1.0F - MathHelper.sin(var6 * (float)Math.PI)) * 0.99F;
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

	public Vec3D getFogColor(float par1, float par2) {
		float var3 = MathHelper.cos(par1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if (var3 < 0.0F) {
			var3 = 0.0F;
		}

		if (var3 > 1.0F) {
			var3 = 1.0F;
		}
		//Spout Start

		float var4;
		float var5;
		float var6;
		if (Colorizer.computeFogColor(Colorizer.COLOR_MAP_FOG0)) {
			var4 = Colorizer.setColor[0];
			var5 = Colorizer.setColor[1];
			var6 = Colorizer.setColor[2];
		} else {
			var4 = 0.7529412F;
			var5 = 0.84705883F;
			var6 = 1.0F;
			Color fogColor = SpoutClient.getInstance().getSkyManager().getFogColor();
			if(fogColor != null){
				var4 = fogColor.getRedF();
				var5 = fogColor.getGreenF();
				var6 = fogColor.getBlueF();
			}
		}
		//Spout End
		var4 *= var3 * 0.94F + 0.06F;
		var5 *= var3 * 0.94F + 0.06F;
		var6 *= var3 * 0.91F + 0.09F;
		return Vec3D.createVector((double)var4, (double)var5, (double)var6);
	}

	public boolean canRespawnHere() {
		return true;
	}

	public static WorldProvider getProviderForDimension(int par0) {
		return (WorldProvider)(par0 == -1?new WorldProviderHell():(par0 == 0?new WorldProviderSurface():(par0 == 1?new WorldProviderEnd():null)));
	}

	public float getCloudHeight() {
		return 128.0F;
	}

	public boolean isSkyColored() {
		return true;
	}

	public ChunkCoordinates getEntrancePortalLocation() {
		return null;
	}

	public int getAverageGroundLevel() {
		return this.terrainType == WorldType.field_48636_c?4:64;
	}

	public boolean getWorldHasNoSky() {
		return this.terrainType != WorldType.field_48636_c && !this.hasNoSky;
	}

	public double func_46065_j() {
		return this.terrainType == WorldType.field_48636_c?1.0D:0.03125D;
	}

	public boolean func_48218_b(int par1, int par2) {
		return false;
	}
}
