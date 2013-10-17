package com.prupe.mcpatcher.sky;

import com.prupe.mcpatcher.BlendMethod;
import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import java.util.Properties;
import net.minecraft.src.EntityFX;
import net.minecraft.src.EntityFireworkOverlayFX;
import net.minecraft.src.EntityFireworkSparkFX;
import net.minecraft.src.ResourceLocation;
import org.lwjgl.opengl.GL11;

//Spout Start
import org.spoutcraft.client.config.Configuration;
//Spout End

public class FireworksHelper {
	private static final int LIT_LAYER = 3;
	private static final int EXTRA_LAYER = 4;
	private static final ResourceLocation PARTICLE_PROPERTIES = TexturePackAPI.newMCPatcherResourceLocation("particle.properties");
	private static final MCLogger logger = MCLogger.getLogger("Better Skies");
	private static final boolean enable = Config.getBoolean("Better Skies", "brightenFireworks", true);
	private static BlendMethod blendMethod;

	public static int getFXLayer(EntityFX entity) {
		return enable && (entity instanceof EntityFireworkSparkFX || entity instanceof EntityFireworkOverlayFX) ? 4 : entity.getFXLayer();
	}

	public static boolean skipThisLayer(boolean skip, int layer) {
		return skip || layer == 3 || !enable && layer > 3;
	}

	public static void setParticleBlendMethod(int layer) {
		if (Configuration.isFancyClouds() && layer == 4 && blendMethod != null) {
			blendMethod.applyBlending();
		} else {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
	}

	static void reload() {
		Properties properties = TexturePackAPI.getProperties(PARTICLE_PROPERTIES);
		String blend = MCPatcherUtils.getStringProperty(properties, "blend.4", "add");
		blendMethod = BlendMethod.parse(blend);

		if (blendMethod == null) {
			logger.error("%s: unknown blend method %s", new Object[] {PARTICLE_PROPERTIES, blend});
		} else if (enable) {
			logger.config("using %s blending for fireworks particles", new Object[] {blendMethod});
		} else {
			logger.config("using default blending for fireworks particles", new Object[0]);
		}
	}
}
