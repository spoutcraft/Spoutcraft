package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.BlendMethod;
import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import java.util.Properties;
import net.minecraft.src.EntityFX;
import net.minecraft.src.EntityFireworkOverlayFX;
import net.minecraft.src.EntityFireworkSparkFX;
import org.lwjgl.opengl.GL11;
// Spout Start
import org.spoutcraft.client.config.Configuration;
// Spout End

public class FireworksHelper {
	private static final int LIT_LAYER = 3;
	private static final int EXTRA_LAYER = 4;
	private static final String PARTICLES_PROPERTIES = "/particles.properties";
	// Spout Start
	private static final boolean enable = Configuration.isFancyClouds();
	// Spout End
	private static BlendMethod blendMethod;

	public static int getFXLayer(EntityFX var0) {
		return enable && (var0 instanceof EntityFireworkSparkFX || var0 instanceof EntityFireworkOverlayFX) ? 4 : var0.getFXLayer();
	}

	public static boolean skipThisLayer(boolean var0, int var1) {
		return var0 || var1 == 3 || !enable && var1 > 3;
	}

	public static void setParticleBlendMethod(int var0) {
		if (enable && var0 == 4 && blendMethod != null) {
			blendMethod.applyBlending();
		} else {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
	}

	static void reload() {
		Properties var0 = TexturePackAPI.getProperties("/particles.properties");
		String var1 = MCPatcherUtils.getStringProperty(var0, "blend.4", "add");
		blendMethod = BlendMethod.parse(var1);
	}
}
