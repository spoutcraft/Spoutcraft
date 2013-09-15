package com.prupe.mcpatcher.mob;

import com.prupe.mcpatcher.TexturePackAPI;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.Tessellator;

public class MobOverlay {
	private static final ResourceLocation MOOSHROOM_OVERLAY = TexturePackAPI.newMCPatcherResourceLocation("mob/cow/mooshroom_overlay.png");
	private static final ResourceLocation SNOWMAN_OVERLAY = TexturePackAPI.newMCPatcherResourceLocation("mob/snowman_overlay.png");
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
	public static ResourceLocation snowmanOverlayTexture;

	static void reset() {
		haveMooshroom = TexturePackAPI.hasResource(MOOSHROOM_OVERLAY);
		haveSnowman = TexturePackAPI.hasResource(SNOWMAN_OVERLAY);
	}

	public static ResourceLocation setupMooshroom(EntityLivingBase entity, ResourceLocation defaultTexture) {
		overlayCounter = 0;

		if (haveMooshroom) {
			overlayActive = true;
			return MobRandomizer.randomTexture(entity, MOOSHROOM_OVERLAY);
		} else {
			overlayActive = false;
			return defaultTexture;
		}
	}

	public static boolean renderMooshroomOverlay() {
		if (overlayActive && overlayCounter < 3) {
			float tileX0 = (float)overlayCounter / 3.0F;
			float tileX1 = (float)(++overlayCounter) / 3.0F;
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(-0.45D, 0.5D, -0.45D, (double)tileX0, 0.0D);
			tessellator.addVertexWithUV(-0.45D, -0.5D, -0.45D, (double)tileX0, 1.0D);
			tessellator.addVertexWithUV(0.45D, -0.5D, 0.45D, (double)tileX1, 1.0D);
			tessellator.addVertexWithUV(0.45D, 0.5D, 0.45D, (double)tileX1, 0.0D);
			tessellator.addVertexWithUV(0.45D, 0.5D, 0.45D, (double)tileX0, 0.0D);
			tessellator.addVertexWithUV(0.45D, -0.5D, 0.45D, (double)tileX0, 1.0D);
			tessellator.addVertexWithUV(-0.45D, -0.5D, -0.45D, (double)tileX1, 1.0D);
			tessellator.addVertexWithUV(-0.45D, 0.5D, -0.45D, (double)tileX1, 0.0D);
			tessellator.addVertexWithUV(-0.45D, 0.5D, 0.45D, (double)tileX0, 0.0D);
			tessellator.addVertexWithUV(-0.45D, -0.5D, 0.45D, (double)tileX0, 1.0D);
			tessellator.addVertexWithUV(0.45D, -0.5D, -0.45D, (double)tileX1, 1.0D);
			tessellator.addVertexWithUV(0.45D, 0.5D, -0.45D, (double)tileX1, 0.0D);
			tessellator.addVertexWithUV(0.45D, 0.5D, -0.45D, (double)tileX0, 0.0D);
			tessellator.addVertexWithUV(0.45D, -0.5D, -0.45D, (double)tileX0, 1.0D);
			tessellator.addVertexWithUV(-0.45D, -0.5D, 0.45D, (double)tileX1, 1.0D);
			tessellator.addVertexWithUV(-0.45D, 0.5D, 0.45D, (double)tileX1, 0.0D);
			tessellator.draw();
		}

		return overlayActive;
	}

	public static void finishMooshroom() {
		overlayCounter = 0;
		overlayActive = false;
	}

	public static boolean setupSnowman(EntityLivingBase entity) {
		if (haveSnowman) {
			snowmanOverlayTexture = MobRandomizer.randomTexture(entity, SNOWMAN_OVERLAY);
			return true;
		} else {
			snowmanOverlayTexture = null;
			return false;
		}
	}

	public static void renderSnowmanOverlay() {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		double[] c = new double[4];
		getTileCoordinates(0, c);
		tessellator.addVertexWithUV(0.5D, -0.5D, -0.5D, c[0], c[2]);
		tessellator.addVertexWithUV(0.5D, -0.5D, 0.5D, c[1], c[2]);
		tessellator.addVertexWithUV(-0.5D, -0.5D, 0.5D, c[1], c[3]);
		tessellator.addVertexWithUV(-0.5D, -0.5D, -0.5D, c[0], c[3]);
		getTileCoordinates(1, c);
		tessellator.addVertexWithUV(-0.5D, 0.5D, -0.5D, c[0], c[2]);
		tessellator.addVertexWithUV(-0.5D, 0.5D, 0.5D, c[1], c[2]);
		tessellator.addVertexWithUV(0.5D, 0.5D, 0.5D, c[1], c[3]);
		tessellator.addVertexWithUV(0.5D, 0.5D, -0.5D, c[0], c[3]);
		getTileCoordinates(2, c);
		tessellator.addVertexWithUV(-0.5D, 0.5D, 0.5D, c[0], c[2]);
		tessellator.addVertexWithUV(-0.5D, 0.5D, -0.5D, c[1], c[2]);
		tessellator.addVertexWithUV(-0.5D, -0.5D, -0.5D, c[1], c[3]);
		tessellator.addVertexWithUV(-0.5D, -0.5D, 0.5D, c[0], c[3]);
		getTileCoordinates(3, c);
		tessellator.addVertexWithUV(-0.5D, 0.5D, -0.5D, c[0], c[2]);
		tessellator.addVertexWithUV(0.5D, 0.5D, -0.5D, c[1], c[2]);
		tessellator.addVertexWithUV(0.5D, -0.5D, -0.5D, c[1], c[3]);
		tessellator.addVertexWithUV(-0.5D, -0.5D, -0.5D, c[0], c[3]);
		getTileCoordinates(4, c);
		tessellator.addVertexWithUV(0.5D, 0.5D, -0.5D, c[0], c[2]);
		tessellator.addVertexWithUV(0.5D, 0.5D, 0.5D, c[1], c[2]);
		tessellator.addVertexWithUV(0.5D, -0.5D, 0.5D, c[1], c[3]);
		tessellator.addVertexWithUV(0.5D, -0.5D, -0.5D, c[0], c[3]);
		getTileCoordinates(5, c);
		tessellator.addVertexWithUV(0.5D, 0.5D, 0.5D, c[0], c[2]);
		tessellator.addVertexWithUV(-0.5D, 0.5D, 0.5D, c[1], c[2]);
		tessellator.addVertexWithUV(-0.5D, -0.5D, 0.5D, c[1], c[3]);
		tessellator.addVertexWithUV(0.5D, -0.5D, 0.5D, c[0], c[3]);
		tessellator.draw();
	}

	private static void getTileCoordinates(int tileNum, double[] c) {
		c[0] = (double)(tileNum % 3) / 3.0D;
		c[1] = c[0] + 0.3333333333333333D;
		c[2] = (double)(tileNum / 3) / 2.0D;
		c[3] = c[2] + 0.5D;
	}
}
