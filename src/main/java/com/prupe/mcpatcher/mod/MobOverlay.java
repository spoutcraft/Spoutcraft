package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.TexturePackAPI;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Tessellator;

public class MobOverlay {
	private static final String MOOSHROOM_OVERLAY = "/mob/redcow_overlay.png";
	private static final String SNOWMAN_OVERLAY = "/mob/snowman_overlay.png";
	private static final double MOO_X0 = -0.45D;
	private static final double MOO_X1 = 0.45D;
	private static final double MOO_Y0 = -0.5D;
	private static final double MOO_Y1 = 0.5D;
	private static final double MOO_Z0 = -0.45D;
	private static final double MOO_Z1 = 0.45D;
	private static final double SNOW_X0 = -0.5D;
	private static final double SNOW_X1 = 0.5D;
	private static final double SNOW_Y0 = -0.5D;
	private static final double SNOW_Y1 = 0.5D;
	private static final double SNOW_Z0 = -0.5D;
	private static final double SNOW_Z1 = 0.5D;
	private static boolean overlayActive;
	private static int overlayCounter;
	private static boolean haveMooshroom;
	private static boolean haveSnowman;
	public static String snowmanOverlayTexture;

	static void reset() {
		haveMooshroom = TexturePackAPI.hasResource("/mob/redcow_overlay.png");
		haveSnowman = TexturePackAPI.hasResource("/mob/snowman_overlay.png");
	}

	public static String setupMooshroom(EntityLiving var0, String var1) {
		overlayCounter = 0;

		if (haveMooshroom) {
			overlayActive = true;
			return MobRandomizer.randomTexture(var0, "/mob/redcow_overlay.png");
		} else {
			overlayActive = false;
			return var1;
		}
	}

	public static boolean renderMooshroomOverlay() {
		if (overlayActive && overlayCounter < 3) {
			float var0 = (float)overlayCounter / 3.0F;
			float var1 = (float)(++overlayCounter) / 3.0F;
			Tessellator var2 = Tessellator.instance;
			var2.startDrawingQuads();
			var2.addVertexWithUV(-0.45D, 0.5D, -0.45D, (double)var0, 0.0D);
			var2.addVertexWithUV(-0.45D, -0.5D, -0.45D, (double)var0, 1.0D);
			var2.addVertexWithUV(0.45D, -0.5D, 0.45D, (double)var1, 1.0D);
			var2.addVertexWithUV(0.45D, 0.5D, 0.45D, (double)var1, 0.0D);
			var2.addVertexWithUV(0.45D, 0.5D, 0.45D, (double)var0, 0.0D);
			var2.addVertexWithUV(0.45D, -0.5D, 0.45D, (double)var0, 1.0D);
			var2.addVertexWithUV(-0.45D, -0.5D, -0.45D, (double)var1, 1.0D);
			var2.addVertexWithUV(-0.45D, 0.5D, -0.45D, (double)var1, 0.0D);
			var2.addVertexWithUV(-0.45D, 0.5D, 0.45D, (double)var0, 0.0D);
			var2.addVertexWithUV(-0.45D, -0.5D, 0.45D, (double)var0, 1.0D);
			var2.addVertexWithUV(0.45D, -0.5D, -0.45D, (double)var1, 1.0D);
			var2.addVertexWithUV(0.45D, 0.5D, -0.45D, (double)var1, 0.0D);
			var2.addVertexWithUV(0.45D, 0.5D, -0.45D, (double)var0, 0.0D);
			var2.addVertexWithUV(0.45D, -0.5D, -0.45D, (double)var0, 1.0D);
			var2.addVertexWithUV(-0.45D, -0.5D, 0.45D, (double)var1, 1.0D);
			var2.addVertexWithUV(-0.45D, 0.5D, 0.45D, (double)var1, 0.0D);
			var2.draw();
		}

		return overlayActive;
	}

	public static void finishMooshroom() {
		overlayCounter = 0;
		overlayActive = false;
	}

	public static boolean setupSnowman(EntityLiving var0) {
		if (haveSnowman) {
			snowmanOverlayTexture = MobRandomizer.randomTexture(var0, "/mob/snowman_overlay.png");
			return true;
		} else {
			snowmanOverlayTexture = null;
			return false;
		}
	}

	public static void renderSnowmanOverlay() {
		Tessellator var0 = Tessellator.instance;
		var0.startDrawingQuads();
		double[] var1 = new double[4];
		getTileCoordinates(0, var1);
		var0.addVertexWithUV(0.5D, -0.5D, -0.5D, var1[0], var1[2]);
		var0.addVertexWithUV(0.5D, -0.5D, 0.5D, var1[1], var1[2]);
		var0.addVertexWithUV(-0.5D, -0.5D, 0.5D, var1[1], var1[3]);
		var0.addVertexWithUV(-0.5D, -0.5D, -0.5D, var1[0], var1[3]);
		getTileCoordinates(1, var1);
		var0.addVertexWithUV(-0.5D, 0.5D, -0.5D, var1[0], var1[2]);
		var0.addVertexWithUV(-0.5D, 0.5D, 0.5D, var1[1], var1[2]);
		var0.addVertexWithUV(0.5D, 0.5D, 0.5D, var1[1], var1[3]);
		var0.addVertexWithUV(0.5D, 0.5D, -0.5D, var1[0], var1[3]);
		getTileCoordinates(2, var1);
		var0.addVertexWithUV(-0.5D, 0.5D, 0.5D, var1[0], var1[2]);
		var0.addVertexWithUV(-0.5D, 0.5D, -0.5D, var1[1], var1[2]);
		var0.addVertexWithUV(-0.5D, -0.5D, -0.5D, var1[1], var1[3]);
		var0.addVertexWithUV(-0.5D, -0.5D, 0.5D, var1[0], var1[3]);
		getTileCoordinates(3, var1);
		var0.addVertexWithUV(-0.5D, 0.5D, -0.5D, var1[0], var1[2]);
		var0.addVertexWithUV(0.5D, 0.5D, -0.5D, var1[1], var1[2]);
		var0.addVertexWithUV(0.5D, -0.5D, -0.5D, var1[1], var1[3]);
		var0.addVertexWithUV(-0.5D, -0.5D, -0.5D, var1[0], var1[3]);
		getTileCoordinates(4, var1);
		var0.addVertexWithUV(0.5D, 0.5D, -0.5D, var1[0], var1[2]);
		var0.addVertexWithUV(0.5D, 0.5D, 0.5D, var1[1], var1[2]);
		var0.addVertexWithUV(0.5D, -0.5D, 0.5D, var1[1], var1[3]);
		var0.addVertexWithUV(0.5D, -0.5D, -0.5D, var1[0], var1[3]);
		getTileCoordinates(5, var1);
		var0.addVertexWithUV(0.5D, 0.5D, 0.5D, var1[0], var1[2]);
		var0.addVertexWithUV(-0.5D, 0.5D, 0.5D, var1[1], var1[2]);
		var0.addVertexWithUV(-0.5D, -0.5D, 0.5D, var1[1], var1[3]);
		var0.addVertexWithUV(0.5D, -0.5D, 0.5D, var1[0], var1[3]);
		var0.draw();
	}

	private static void getTileCoordinates(int var0, double[] var1) {
		var1[0] = (double)(var0 % 3) / 3.0D;
		var1[1] = var1[0] + 0.3333333333333333D;
		var1[2] = (double)(var0 / 3) / 2.0D;
		var1[3] = var1[2] + 0.5D;
	}
}
