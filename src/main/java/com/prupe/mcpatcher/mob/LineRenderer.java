package com.prupe.mcpatcher.mob;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.InputHandler;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import java.util.Properties;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

public class LineRenderer {
	private static final MCLogger logger = MCLogger.getLogger("Random Mobs");
	private static final double D_WIDTH = 9.765625E-4D;
	private static final double D_POS = 0.00390625D;
	private static final boolean enable = Config.getBoolean("Random Mobs", "leashLine", true);
	private static final LineRenderer[] renderers = new LineRenderer[2];
	private final ResourceLocation texture;
	private final double width;
	private final double a;
	private final double b;
	private final double sx;
	private final double sy;
	private final double sz;
	private final int segments;
	private final double tileFactor;
	private final boolean active;
	private final InputHandler keyboard;
	private double plusWidth;
	private double plusTile;
	private double plusSX;
	private double plusSY;
	private double plusSZ;

	public static boolean renderLine(int type, double x, double y, double z, double dx, double dy, double dz) {
		LineRenderer renderer = renderers[type];
		return renderer != null && renderer.render(x, y, z, dx, dy, dz);
	}

	static void reset() {
		if (enable) {
			setup(0, "fishingline", 0.0075D, 0.0D, 0.25D, 16);
			setup(1, "lead", 0.025D, 1.3333333333333333D, 0.125D, 24);
		}
	}

	private static void setup(int type, String name, double defaultWidth, double a, double b, int segments) {
		LineRenderer renderer = new LineRenderer(name, defaultWidth, a, b, segments);

		if (renderer.active) {
			logger.fine("using %s", new Object[] {renderer});
			renderers[type] = renderer;
		} else {
			logger.fine("%s not found", new Object[] {renderer});
			renderers[type] = null;
		}
	}

	private LineRenderer(String name, double width, double a, double b, int segments) {
		this.texture = TexturePackAPI.newMCPatcherResourceLocation("line/" + name + ".png");
		this.active = TexturePackAPI.hasResource(this.texture);
		Properties properties = TexturePackAPI.getProperties(TexturePackAPI.transformResourceLocation(this.texture, ".png", ".properties"));
		this.width = MCPatcherUtils.getDoubleProperty(properties, "width", width);
		this.a = MCPatcherUtils.getDoubleProperty(properties, "a", a);
		this.b = MCPatcherUtils.getDoubleProperty(properties, "b", b);
		this.sx = MCPatcherUtils.getDoubleProperty(properties, "sx", 0.0D);
		this.sy = MCPatcherUtils.getDoubleProperty(properties, "sy", 0.0D);
		this.sz = MCPatcherUtils.getDoubleProperty(properties, "sz", 0.0D);
		this.segments = MCPatcherUtils.getIntProperty(properties, "segments", segments);
		this.tileFactor = MCPatcherUtils.getDoubleProperty(properties, "tileFactor", 24.0D);
		this.keyboard = new InputHandler(name, MCPatcherUtils.getBooleanProperty(properties, "debug", false));
	}

	private boolean render(double x, double y, double z, double dx, double dy, double dz) {
		if (this.keyboard.isKeyDown(55)) {
			return false;
		} else {
			boolean changed = false;

			if (this.keyboard.isEnabled()) {
				if (this.keyboard.isKeyPressed(78)) {
					changed = true;
					this.plusWidth += 9.765625E-4D;
				} else if (this.keyboard.isKeyPressed(74)) {
					changed = true;
					this.plusWidth -= 9.765625E-4D;
				} else if (this.keyboard.isKeyPressed(181)) {
					changed = true;
					this.plusWidth = this.plusTile = this.plusSX = this.plusSY = this.plusSZ = 0.0D;
				} else if (this.keyboard.isKeyPressed(81)) {
					changed = true;
					--this.plusTile;
				} else if (this.keyboard.isKeyPressed(73)) {
					changed = true;
					++this.plusTile;
				} else if (this.keyboard.isKeyDown(75)) {
					changed = true;
					this.plusSX -= 0.00390625D;
				} else if (this.keyboard.isKeyDown(77)) {
					changed = true;
					this.plusSX += 0.00390625D;
				} else if (this.keyboard.isKeyDown(79)) {
					changed = true;
					this.plusSY -= 0.00390625D;
				} else if (this.keyboard.isKeyDown(71)) {
					changed = true;
					this.plusSY += 0.00390625D;
				} else if (this.keyboard.isKeyDown(80)) {
					changed = true;
					this.plusSZ += 0.00390625D;
				} else if (this.keyboard.isKeyDown(72)) {
					changed = true;
					this.plusSZ -= 0.00390625D;
				}
			}

			TexturePackAPI.bindTexture(this.texture);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			dx += this.sx + this.plusSX;
			dy += this.sy + this.plusSY;
			dz += this.sz + this.plusSZ;
			double x0 = x;
			double y0 = y + this.a + this.b;
			double z0 = z;
			double u0 = 0.0D;
			double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
			double t = this.tileFactor + this.plusTile;
			double w = this.width + this.plusWidth;

			if (changed) {
				logger.info("%s: dx=%f, dy=%f, dz=%f, len=%f(*%d=%f), slen=%f", new Object[] {this, Double.valueOf(dx), Double.valueOf(dy), Double.valueOf(dz), Double.valueOf(len), Integer.valueOf((int)t), Double.valueOf(len * t), Double.valueOf(len * t / (double)this.segments)});
				System.out.printf("width=%f\n", new Object[] {Double.valueOf(w)});
				System.out.printf("tileFactor=%f\n", new Object[] {Double.valueOf(t)});
				System.out.printf("sx=%f\n", new Object[] {Double.valueOf(this.sx + this.plusSX)});
				System.out.printf("sy=%f\n", new Object[] {Double.valueOf(this.sy + this.plusSY)});
				System.out.printf("sz=%f\n", new Object[] {Double.valueOf(this.sz + this.plusSZ)});
			}

			len *= t / (double)this.segments;

			for (int i = 1; i <= this.segments; ++i) {
				double s = (double)i / (double)this.segments;
				double x1 = x + s * dx;
				double y1 = y + (s * s + s) * 0.5D * dy + this.a * (1.0D - s) + this.b;
				double z1 = z + s * dz;
				double u1 = (double)(this.segments - i) * len;
				tessellator.addVertexWithUV(x0, y0, z0, u0, 1.0D);
				tessellator.addVertexWithUV(x1, y1, z1, u1, 1.0D);
				tessellator.addVertexWithUV(x1 + w, y1 + w, z1, u1, 0.0D);
				tessellator.addVertexWithUV(x0 + w, y0 + w, z0, u0, 0.0D);
				tessellator.addVertexWithUV(x0, y0 + w, z0, u0, 1.0D);
				tessellator.addVertexWithUV(x1, y1 + w, z1, u1, 1.0D);
				tessellator.addVertexWithUV(x1 + w, y1, z1 + w, u1, 0.0D);
				tessellator.addVertexWithUV(x0 + w, y0, z0 + w, u0, 0.0D);
				x0 = x1;
				y0 = y1;
				z0 = z1;
				u0 = u1;
			}

			tessellator.draw();
			GL11.glEnable(GL11.GL_CULL_FACE);
			return true;
		}
	}

	public String toString() {
		return "LineRenderer{" + this.texture + ", " + (this.width + this.plusWidth) + "}";
	}
}
