package net.minecraft.src;

// MCPatcher Start
import com.prupe.mcpatcher.mod.ColorizeWorld;
// MCPatcher End
// Spout Start
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.client.SpoutClient;
// Spout End

public class WorldProviderHell extends WorldProvider {

	/**
	 * creates a new world chunk manager for WorldProvider
	 */
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 1.0F, 0.0F);
		this.isHellWorld = true;
		this.hasNoSky = true;
		this.dimensionId = -1;
	}

	/**
	 * Return Vec3D with biome specific fog color
	 */
	public Vec3 getFogColor(float par1, float par2) {
		// Spout Start
		Color fogColor = SpoutClient.getInstance().getSkyManager().getFogColor();
		if (fogColor != null) {
			return Vec3.createVectorHelper(fogColor.getRedF(), fogColor.getGreenF(), fogColor.getBlueF());
		} else {
		// MCPatcher Start
		return this.worldObj.getWorldVec3Pool().getVecFromPool((double)ColorizeWorld.netherFogColor[0], (double)ColorizeWorld.netherFogColor[1], (double)ColorizeWorld.netherFogColor[2]);
		// MCPatcher End
		}
		// Spout End
	}

	/**
	 * Creates the light to brightness table
	 */
	protected void generateLightBrightnessTable() {
		float var1 = 0.1F;

		for (int var2 = 0; var2 <= 15; ++var2) {
			float var3 = 1.0F - (float)var2 / 15.0F;
			this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
		}
	}

	/**
	 * Returns a new chunk provider which generates chunks for this world
	 */
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderHell(this.worldObj, this.worldObj.getSeed());
	}

	/**
	 * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
	 */
	public boolean isSurfaceWorld() {
		return false;
	}

	/**
	 * Will check if the x, z position specified is alright to be set as the map spawn point
	 */
	public boolean canCoordinateBeSpawn(int par1, int par2) {
		return false;
	}

	/**
	 * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
	 */
	public float calculateCelestialAngle(long par1, float par3) {
		return 0.5F;
	}

	/**
	 * True if the player can respawn in this dimension (true = overworld, false = nether).
	 */
	public boolean canRespawnHere() {
		return false;
	}

	/**
	 * Returns true if the given X,Z coordinate should show environmental fog.
	 */
	public boolean doesXZShowFog(int par1, int par2) {
		return true;
	}

	/**
	 * Returns the dimension's name, e.g. "The End", "Nether", or "Overworld".
	 */
	public String getDimensionName() {
		return "Nether";
	}
}
