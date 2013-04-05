package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.mod.SkyRenderer$1;
import com.prupe.mcpatcher.mod.SkyRenderer$Layer;
import com.prupe.mcpatcher.mod.SkyRenderer$WorldEntry;
import java.util.HashMap;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

public class SkyRenderer {
	private static final boolean enable = Config.getBoolean("Better Skies", "skybox", true);
	private static RenderEngine renderEngine;
	private static double worldTime;
	private static float celestialAngle;
	private static float rainStrength;
	private static final HashMap worldSkies = new HashMap();
	private static SkyRenderer$WorldEntry currentWorld;
	public static boolean active;

	public static void setup(World var0, RenderEngine var1, float var2, float var3) {
		if (TexturePackAPI.isDefaultTexturePack()) {
			active = false;
		} else {
			int var4 = MCPatcherUtils.getMinecraft().theWorld.provider.dimensionId;
			SkyRenderer$WorldEntry var5 = getWorldEntry(var4);

			if (var5 != currentWorld && currentWorld != null) {
				currentWorld.unloadTextures();
			}

			currentWorld = var5;
			active = currentWorld.active();

			if (active) {
				renderEngine = var1;
				worldTime = (double)((float)var0.getWorldTime() + var2);
				rainStrength = 1.0F - var0.getRainStrength(var2);
				celestialAngle = var3;
			}
		}
	}

	public static void renderAll() {
		if (active) {
			currentWorld.renderAll(Tessellator.instance);
		}
	}

	public static String setupCelestialObject(String var0) {
		if (active) {
			SkyRenderer$Layer.clearBlendingMethod();
			SkyRenderer$Layer var1 = currentWorld.getCelestialObject(var0);

			if (var1 != null) {
				var1.setBlendingMethod(rainStrength);
				return SkyRenderer$Layer.access$300(var1);
			}
		}

		return var0;
	}

	private static SkyRenderer$WorldEntry getWorldEntry(int var0) {
		SkyRenderer$WorldEntry var1 = (SkyRenderer$WorldEntry)worldSkies.get(Integer.valueOf(var0));

		if (var1 == null) {
			var1 = new SkyRenderer$WorldEntry(var0);
			worldSkies.put(Integer.valueOf(var0), var1);
		}

		return var1;
	}

	static HashMap access$000() {
		return worldSkies;
	}

	static boolean access$100() {
		return enable;
	}

	static SkyRenderer$WorldEntry access$200(int var0) {
		return getWorldEntry(var0);
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
