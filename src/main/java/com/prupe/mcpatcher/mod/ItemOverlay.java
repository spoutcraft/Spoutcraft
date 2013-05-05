package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.BlendMethod;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import java.util.Properties;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

class ItemOverlay {
	static final int AVERAGE = 0;
	static final int CYCLE = 1;
	static final int TOP = 2;
	private static final float ITEM_2D_THICKNESS = 0.0625F;
	private final String texture;
	private final BlendMethod blendMethod;
	private final float rotation;
	private final float speed;
	final float duration;
	final int weight;
	final int applyMethod;
	final int limit;
	final int groupID;

	static void beginOuter2D() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthFunc(GL11.GL_GREATER);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
	}

	static void endOuter2D() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	static void beginOuter3D() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthFunc(GL11.GL_EQUAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
	}

	static void endOuter3D() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	static ItemOverlay create(ItemOverride var0, Properties var1) {
		String var2 = MCPatcherUtils.getStringProperty(var1, "blend", "add");
		BlendMethod var3 = BlendMethod.parse(var2);

		if (var3 == null) {
			var0.error("unknown blend type %s", new Object[] {var2});
			return null;
		} else {
			float var4 = MCPatcherUtils.getFloatProperty(var1, "rotation", 0.0F);
			float var5 = MCPatcherUtils.getFloatProperty(var1, "speed", 0.0F);
			float var6 = MCPatcherUtils.getFloatProperty(var1, "duration", 1.0F);
			int var7 = MCPatcherUtils.getIntProperty(var1, "weight", 1);
			String[] var9 = MCPatcherUtils.getStringProperty(var1, "apply", "average").toLowerCase().split("\\s+");
			byte var8;

			if (var9[0].equals("average")) {
				var8 = 0;
			} else if (var9[0].equals("top")) {
				var8 = 2;
			} else {
				if (!var9[0].equals("cycle")) {
					var0.error("unknown apply type %s", new Object[] {var9[0]});
					return null;
				}

				var8 = 1;
			}

			int var10 = 255;

			if (var9.length > 1) {
				try {
					var10 = Integer.parseInt(var9[1]);
				} catch (NumberFormatException var12) {
					;
				}
			}

			var10 = Math.max(Math.min(var10, 255), 0);
			return new ItemOverlay(var0.textureName, var3, var4, var5, var6, var7, var8, var10);
		}
	}

	ItemOverlay(String var1, BlendMethod var2, float var3, float var4, float var5, int var6, int var7, int var8) {
		this.texture = var1;
		this.blendMethod = var2;
		this.rotation = var3;
		this.speed = var4;
		this.duration = var5;
		this.weight = var6;
		this.applyMethod = var7;
		this.limit = var8;
		this.groupID = var8 << 2 | var7;
	}

	void render2D(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		if (var2 > 0.0F) {
			if (var2 > 1.0F) {
				var2 = 1.0F;
			}

			this.begin(var2);
			var1.startDrawingQuads();
			var1.addVertexWithUV((double)var3, (double)var4, (double)var7, 0.0D, 0.0D);
			var1.addVertexWithUV((double)var3, (double)var6, (double)var7, 0.0D, 1.0D);
			var1.addVertexWithUV((double)var5, (double)var6, (double)var7, 1.0D, 1.0D);
			var1.addVertexWithUV((double)var5, (double)var4, (double)var7, 1.0D, 0.0D);
			var1.draw();
			this.end();
		}
	}

	void render3D(Tessellator var1, float var2, int var3, int var4) {
		if (var2 > 0.0F) {
			if (var2 > 1.0F) {
				var2 = 1.0F;
			}

			this.begin(var2);
			ItemRenderer.renderItemIn2D(var1, 0.0F, 0.0F, 1.0F, 1.0F, var3, var4, 0.0625F);
			this.end();
		}
	}

	void beginArmor(float var1) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthFunc(GL11.GL_EQUAL);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		this.begin(var1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	void endArmor() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		this.end();
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	private void begin(float var1) {
		TexturePackAPI.bindTexture(this.texture);
		this.blendMethod.applyBlending();
		this.blendMethod.applyFade(var1);
		GL11.glPushMatrix();

		if (this.speed != 0.0F) {
			float var2 = (float)(System.currentTimeMillis() % 3000L) / 3000.0F * 8.0F;
			GL11.glTranslatef(var2 * this.speed, 0.0F, 0.0F);
		}

		GL11.glRotatef(this.rotation, 0.0F, 0.0F, 1.0F);
	}

	private void end() {
		GL11.glPopMatrix();
	}
}
