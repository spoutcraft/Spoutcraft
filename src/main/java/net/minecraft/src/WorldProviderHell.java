package net.minecraft.src;

// Spout Start
import org.spoutcraft.client.SpoutClient;
import com.pclewis.mcpatcher.mod.Colorizer;
import org.spoutcraft.spoutcraftapi.gui.Color;
// Spout End

public class WorldProviderHell extends WorldProvider {
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 1.0F, 0.0F);
		this.isHellWorld = true;
		this.hasNoSky = true;
		this.worldType = -1;
	}

	public Vec3 getFogColor(float par1, float par2) {
		// Spout Start
		Color fogColor = SpoutClient.getInstance().getSkyManager().getFogColor();
		if (fogColor != null) {
			return Vec3.getVec3Pool().getVecFromPool(fogColor.getRedF(), fogColor.getGreenF(), fogColor.getBlueF());
		} else {
			return Vec3.getVec3Pool().getVecFromPool((double)Colorizer.netherFogColor[0], (double)Colorizer.netherFogColor[1], (double)Colorizer.netherFogColor[2]);
		}
		// Spout End
	}

	protected void generateLightBrightnessTable() {
		float var1 = 0.1F;

		for (int var2 = 0; var2 <= 15; ++var2) {
			float var3 = 1.0F - (float)var2 / 15.0F;
			this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
		}
	}

	public IChunkProvider getChunkProvider() {
		return new ChunkProviderHell(this.worldObj, this.worldObj.getSeed());
	}

	public boolean isSurfaceWorld() {
		return false;
	}

	public boolean canCoordinateBeSpawn(int par1, int par2) {
		return false;
	}

	public float calculateCelestialAngle(long par1, float par3) {
		return 0.5F;
	}

	public boolean canRespawnHere() {
		return false;
	}

	/**
	 * Returns true if the given X,Z coordinate should show environmental fog.
	 */
	public boolean doesXZShowFog(int par1, int par2) {
		return true;
	}
	
	public String func_80007_l() {
		return "Nether";
	}
}
