package com.prupe.mcpatcher.sky;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.sky.SkyRenderer$1;
import com.prupe.mcpatcher.sky.SkyRenderer$Layer;
import com.prupe.mcpatcher.sky.SkyRenderer$WorldEntry;
import java.util.HashMap;
import net.minecraft.src.Minecraft;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

public class SkyRenderer {
	private static final MCLogger logger = MCLogger.getLogger("Better Skies");
	private static final boolean enable = Config.getBoolean("Better Skies", "skybox", true);
	private static double worldTime;
	private static float celestialAngle;
	private static float rainStrength;
	private static final HashMap<Integer, SkyRenderer$WorldEntry> worldSkies = new HashMap();
	private static SkyRenderer$WorldEntry currentWorld;
	public static boolean active;

	public static void setup(World world, float partialTick, float celestialAngle) {
		if (TexturePackAPI.isDefaultTexturePack()) {
			active = false;
		} else {
			int worldType = Minecraft.getMinecraft().theWorld.provider.dimensionId;
			SkyRenderer$WorldEntry newEntry = getWorldEntry(worldType);

			if (newEntry != currentWorld && currentWorld != null) {
				currentWorld.unloadTextures();
			}

			currentWorld = newEntry;
			active = currentWorld.active();

			if (active) {
				worldTime = (double)((float)world.getWorldTime() + partialTick);
				rainStrength = 1.0F - world.getRainStrength(partialTick);
				celestialAngle = celestialAngle;
			}
		}
	}

	public static void renderAll() {
		if (active) {
			currentWorld.renderAll(Tessellator.instance);
		}
	}

	public static ResourceLocation setupCelestialObject(ResourceLocation defaultTexture) {
		if (active) {
			SkyRenderer$Layer.clearBlendingMethod();
			SkyRenderer$Layer layer = currentWorld.getCelestialObject(defaultTexture);

			if (layer != null) {
				layer.setBlendingMethod(rainStrength);
				return SkyRenderer$Layer.access$300(layer);
			}
		}

		return defaultTexture;
	}

	private static SkyRenderer$WorldEntry getWorldEntry(int worldType) {
		SkyRenderer$WorldEntry entry = (SkyRenderer$WorldEntry)worldSkies.get(Integer.valueOf(worldType));

		if (entry == null) {
			entry = new SkyRenderer$WorldEntry(worldType);
			worldSkies.put(Integer.valueOf(worldType), entry);
		}

		return entry;
	}

	static HashMap access$000() {
		return worldSkies;
	}

	static boolean access$100() {
		return enable;
	}

	static SkyRenderer$WorldEntry access$200(int x0) {
		return getWorldEntry(x0);
	}

	static MCLogger access$400() {
		return logger;
	}

	static float access$500() {
		return rainStrength;
	}

	static double access$600() {
		return worldTime;
	}

	static float access$700() {
		return celestialAngle;
	}

	static {
		TexturePackChangeHandler.register(new SkyRenderer$1("Better Skies", 2));
	}
}
