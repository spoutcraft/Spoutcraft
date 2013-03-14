package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import java.util.Properties;
import java.util.Random;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.ItemDye;

public class ColorizeEntity {
	private static final String LAVA_DROP_COLORS = "/misc/lavadropcolor.png";
	private static final String MYCELIUM_COLORS = "/misc/myceliumparticlecolor.png";
	private static final String XPORB_COLORS = "/misc/xporbcolor.png";
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

		for (int var0 = 0; var0 < origFleeceColors.length; ++var0) {
			EntitySheep.fleeceColorTable[var0] = (float[])origFleeceColors[var0].clone();
			armorColors[var0] = (float[])origFleeceColors[var0].clone();
			collarColors[var0] = (float[])origFleeceColors[var0].clone();
		}

		undyedLeatherColor = 10511680;
		myceliumColors = null;
		xpOrbColors = null;
	}

	static void reloadParticleColors(Properties var0) {
		Colorizer.loadFloatColor("drop.water", waterBaseColor);
		Colorizer.loadFloatColor("particle.water", waterBaseColor);
		Colorizer.loadFloatColor("particle.portal", portalColor);
		int[] var1 = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage("/misc/lavadropcolor.png"));

		if (var1 != null) {
			lavaDropColors = new float[3 * var1.length];

			for (int var2 = 0; var2 < var1.length; ++var2) {
				Colorizer.intToFloat3(var1[var2], lavaDropColors, 3 * var2);
			}
		}

		myceliumColors = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage("/misc/myceliumparticlecolor.png"));
	}

	static void reloadDyeColors(Properties var0) {
		int var1;

		for (var1 = 0; var1 < ItemDye.dyeColors.length; ++var1) {
			Colorizer.loadIntColor("dye." + Colorizer.getStringKey(ItemDye.dyeColorNames, var1), ItemDye.dyeColors, var1);
		}

		for (var1 = 0; var1 < EntitySheep.fleeceColorTable.length; ++var1) {
			String var2 = Colorizer.getStringKey(ItemDye.dyeColorNames, EntitySheep.fleeceColorTable.length - 1 - var1);
			Colorizer.loadFloatColor("sheep." + var2, EntitySheep.fleeceColorTable[var1]);
			Colorizer.loadFloatColor("armor." + var2, armorColors[var1]);
			Colorizer.loadFloatColor("collar." + var2, collarColors[var1]);
		}

		undyedLeatherColor = Colorizer.loadIntColor("armor.default", undyedLeatherColor);
	}

	static void reloadXPOrbColors(Properties var0) {
		xpOrbColors = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage("/misc/xporbcolor.png"));
	}

	public static int colorizeXPOrb(int var0, float var1) {
		return xpOrbColors != null && xpOrbColors.length != 0 ? xpOrbColors[(int)((Math.sin((double)var1 / 4.0D) + 1.0D) * (double)(xpOrbColors.length - 1) / 2.0D)] : var0;
	}

	public static boolean computeLavaDropColor(int var0) {
		if (lavaDropColors == null) {
			return false;
		} else {
			int var1 = 3 * Math.max(Math.min(lavaDropColors.length / 3 - 1, var0 - 20), 0);
			System.arraycopy(lavaDropColors, var1, Colorizer.setColor, 0, 3);
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

	static {
		try {
			for (int var0 = 0; var0 < EntitySheep.fleeceColorTable.length; ++var0) {
				origFleeceColors[var0] = (float[])EntitySheep.fleeceColorTable[var0].clone();
				armorColors[var0] = (float[])EntitySheep.fleeceColorTable[var0].clone();
				collarColors[var0] = (float[])EntitySheep.fleeceColorTable[var0].clone();
			}

			reset();
		} catch (Throwable var1) {
			var1.printStackTrace();
		}
	}
}
