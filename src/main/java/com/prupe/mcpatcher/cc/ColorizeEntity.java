package com.prupe.mcpatcher.cc;

import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import java.util.Properties;
import java.util.Random;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.ItemDye;
import net.minecraft.src.ResourceLocation;

public class ColorizeEntity {
	private static final ResourceLocation LAVA_DROP_COLORS = TexturePackAPI.newMCPatcherResourceLocation("colormap/lavadrop.png");
	private static final ResourceLocation MYCELIUM_COLORS = TexturePackAPI.newMCPatcherResourceLocation("colormap/myceliumparticle.png");
	private static final ResourceLocation XPORB_COLORS = TexturePackAPI.newMCPatcherResourceLocation("colormap/xporb.png");
	static float[] waterBaseColor;
	private static float[] lavaDropColors;
	public static float[] portalColor = new float[] {1.0F, 0.3F, 0.9F};
	private static final Random random = new Random();
	private static int[] myceliumColors;
	private static int[] xpOrbColors;
	private static final int[] origDyeColors = (int[])ItemDye.dyeColors.clone();
	private static final float[][] origFleeceColors = new float[EntitySheep.fleeceColorTable.length][];
	public static final float[][] armorColors = new float[EntitySheep.fleeceColorTable.length][];
	public static int undyedLeatherColor;
	public static final float[][] collarColors = new float[EntitySheep.fleeceColorTable.length][];

	static void reset() {
		waterBaseColor = new float[] {0.2F, 0.3F, 1.0F};
		portalColor = new float[] {1.0F, 0.3F, 0.9F};
		lavaDropColors = null;
		System.arraycopy(origDyeColors, 0, ItemDye.dyeColors, 0, origDyeColors.length);

		for (int i = 0; i < origFleeceColors.length; ++i) {
			EntitySheep.fleeceColorTable[i] = (float[])origFleeceColors[i].clone();
			armorColors[i] = (float[])origFleeceColors[i].clone();
			collarColors[i] = (float[])origFleeceColors[i].clone();
		}

		undyedLeatherColor = 10511680;
		myceliumColors = null;
		xpOrbColors = null;
	}

	static void reloadParticleColors(Properties properties) {
		Colorizer.loadFloatColor("drop.water", waterBaseColor);
		Colorizer.loadFloatColor("particle.water", waterBaseColor);
		Colorizer.loadFloatColor("particle.portal", portalColor);
		int[] rgb = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage(LAVA_DROP_COLORS));

		if (rgb != null) {
			lavaDropColors = new float[3 * rgb.length];

			for (int i = 0; i < rgb.length; ++i) {
				Colorizer.intToFloat3(rgb[i], lavaDropColors, 3 * i);
			}
		}

		myceliumColors = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage(MYCELIUM_COLORS));
	}

	static void reloadDyeColors(Properties properties) {
		int i;

		for (i = 0; i < ItemDye.dyeColors.length; ++i) {
			Colorizer.loadIntColor("dye." + Colorizer.getStringKey(ItemDye.dyeColorNames, i), ItemDye.dyeColors, i);
		}

		for (i = 0; i < EntitySheep.fleeceColorTable.length; ++i) {
			String key = Colorizer.getStringKey(ItemDye.dyeColorNames, EntitySheep.fleeceColorTable.length - 1 - i);
			Colorizer.loadFloatColor("sheep." + key, EntitySheep.fleeceColorTable[i]);
			Colorizer.loadFloatColor("armor." + key, armorColors[i]);
			Colorizer.loadFloatColor("collar." + key, collarColors[i]);
		}

		undyedLeatherColor = Colorizer.loadIntColor("armor.default", undyedLeatherColor);
	}

	static void reloadXPOrbColors(Properties properties) {
		xpOrbColors = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage(XPORB_COLORS));
	}

	public static int colorizeXPOrb(int origColor, float timer) {
		return xpOrbColors != null && xpOrbColors.length != 0 ? xpOrbColors[(int)((Math.sin((double)timer / 4.0D) + 1.0D) * (double)(xpOrbColors.length - 1) / 2.0D)] : origColor;
	}

	public static boolean computeLavaDropColor(int age) {
		if (lavaDropColors == null) {
			return false;
		} else {
			int offset = 3 * Math.max(Math.min(lavaDropColors.length / 3 - 1, age - 20), 0);
			System.arraycopy(lavaDropColors, offset, Colorizer.setColor, 0, 3);
			return true;
		}
	}

	public static boolean computeMyceliumParticleColor() {
		if (myceliumColors == null) {
			return false;
		} else {
			Colorizer.setColorF(myceliumColors[random.nextInt(myceliumColors.length)]);
			return true;
		}
	}

	public static int getPotionEffectColor(int defaultColor, EntityLivingBase entity) {
		return defaultColor == 0 ? defaultColor : entity.overridePotionColor;
	}

	static {
		try {
			for (int e = 0; e < EntitySheep.fleeceColorTable.length; ++e) {
				origFleeceColors[e] = (float[])EntitySheep.fleeceColorTable[e].clone();
				armorColors[e] = (float[])EntitySheep.fleeceColorTable[e].clone();
				collarColors[e] = (float[])EntitySheep.fleeceColorTable[e].clone();
			}

			reset();
		} catch (Throwable var1) {
			var1.printStackTrace();
		}
	}
}
