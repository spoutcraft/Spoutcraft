package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Random;
import javax.imageio.ImageIO;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GameSettings;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
//Spout HD Start
import com.pclewis.mcpatcher.mod.FontUtils;
import com.pclewis.mcpatcher.mod.TextureUtils;

//Spout HD End

public class FontRenderer {

	private int[] charWidth = new int[256];
	public int fontTextureName = 0;
	public int field_41063_b = 8;
	private int fontDisplayLists;
	private IntBuffer buffer = GLAllocation.createDirectIntBuffer(1024 /* GL_FRONT_LEFT */);
	public Random field_41064_c = new Random();
// Spout HD Start
	public float[] charWidthf;
// Spout HD End

	public FontRenderer(GameSettings var1, String var2, RenderEngine var3) {
		BufferedImage var4;
		try {
			// Spout HD Start
			var4 = TextureUtils.getResourceAsBufferedImage(var2);
			// Spout HD End
		} catch (IOException var18) {
			throw new RuntimeException(var18);
		}

		int var5 = var4.getWidth();
		int var6 = var4.getHeight();
		int[] var7 = new int[var5 * var6];
// Spout HD Start
		var4.getRGB(0, 0, var5, var6, var7, 0, var5);
		this.charWidthf = FontUtils.computeCharWidths(var2, var4, var7, this.charWidth);
		this.fontTextureName = var3.allocateAndSetupTexture(var4);
		this.fontDisplayLists = GLAllocation.generateDisplayLists(288);
		Tessellator var8 = Tessellator.instance;

		int var9;
		int var10;
		int var11;
		for (var9 = 0; var9 < 256; ++var9) {
			GL11.glNewList(this.fontDisplayLists + var9, 4864 /* GL_COMPILE */);
			var8.startDrawingQuads();
			var10 = var9 % 16 * 8;
			var11 = var9 / 16 * 8;
			float var12 = 7.99F;
			float var13 = 0.0F;
			float var14 = 0.0F;
			var8.addVertexWithUV(0.0D, (double)(0.0F + var12), 0.0D, (double)((float)var10 / 128.0F + var13), (double)(((float)var11 + var12) / 128.0F + var14));
			var8.addVertexWithUV((double)(0.0F + var12), (double)(0.0F + var12), 0.0D, (double)(((float)var10 + var12) / 128.0F + var13), (double)(((float)var11 + var12) / 128.0F + var14));
			var8.addVertexWithUV((double)(0.0F + var12), 0.0D, 0.0D, (double)(((float)var10 + var12) / 128.0F + var13), (double)((float)var11 / 128.0F + var14));
			var8.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)((float)var10 / 128.0F + var13), (double)((float)var11 / 128.0F + var14));
			var8.draw();
			GL11.glTranslatef(this.charWidthf[var9], 0.0F, 0.0F);
			GL11.glEndList();
		}

		for (var9 = 0; var9 < 32; ++var9) {
			var10 = (var9 >> 3 & 1) * 85;
			var11 = (var9 >> 2 & 1) * 170 + var10;
			int var19 = (var9 >> 1 & 1) * 170 + var10;
			int var20 = (var9 >> 0 & 1) * 170 + var10;
			if (var9 == 6) {
				var11 += 85;
			}

			boolean var21 = var9 >= 16;
			if (var1.anaglyph) {
				int var15 = (var11 * 30 + var19 * 59 + var20 * 11) / 100;
				int var16 = (var11 * 30 + var19 * 70) / 100;
				int var17 = (var11 * 30 + var20 * 70) / 100;
				var11 = var15;
				var19 = var16;
				var20 = var17;
			}

			if(var21) {
				var11 /= 4;
				var19 /= 4;
				var20 /= 4;
			}

			GL11.glNewList(this.fontDisplayLists + 256 + var9, 4864 /* GL_COMPILE */);
			GL11.glColor3f((float)var11 / 255.0F, (float)var19 / 255.0F, (float)var20 / 255.0F);
			GL11.glEndList();
		}
// Spout HD End
	}

	public void drawStringWithShadow(String var1, int var2, int var3, int var4) {
		this.renderString(var1, var2 + 1, var3 + 1, var4, true);
		this.drawString(var1, var2, var3, var4);
	}

	public void drawString(String var1, int var2, int var3, int var4) {
		this.renderString(var1, var2, var3, var4, false);
	}

	// Spout Start
	// Yay, I completely decoded the field names!
	public void renderStringInGame(String text, double x, double y, double z, float yaw, int color, float scale) {
		if (text != null) {
			int i;

			GL11.glEnable('\u803a');
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureName);
			float red = (float) (color >> 16 & 255) / 255.0F;
			float green = (float) (color >> 8 & 255) / 255.0F;
			float blue = (float) (color & 255) / 255.0F;
			float alpha = (float) (color >> 24 & 255) / 255.0F;
			if (alpha == 0.0F) {
				alpha = 1.0F;
			}

			GL11.glColor4f(red, green, blue, alpha);
			this.buffer.clear();
			GL11.glPushMatrix(); // Matrix of the whole text
			GL11.glTranslated(x, y, z);
			GL11.glScaled(-0.014 * scale, -0.014 * scale, -0.014 * scale);
			GL11.glRotatef(yaw, 0, 1.0F, 0);
			GL11.glPushMatrix(); // Matrix to move the text to center
			GL11.glTranslated(-getStringWidth(text) / 2, 0, 0); // Move text to
																// center
			for (i = 0; i < text.length(); ++i) {
				int colorBit;
				for (; text.length() > i + 1 && text.charAt(i) == 167; i += 2) {
					colorBit = "0123456789abcdef".indexOf(text.toLowerCase().charAt(i + 1));
					if (colorBit < 0 || colorBit > 15) {
						colorBit = 15;
					}

					this.buffer.put(this.fontDisplayLists + 256 + colorBit);
					if (this.buffer.remaining() == 0) {
						this.buffer.flip();
						GL11.glCallLists(this.buffer);
						this.buffer.clear();
					}
				}

				if (i < text.length()) {
					colorBit = ChatAllowedCharacters.allowedCharacters.indexOf(text.charAt(i));
					if (colorBit >= 0) {
						this.buffer.put(this.fontDisplayLists + colorBit + 32);
					}
				}

				if (this.buffer.remaining() == 0) {
					this.buffer.flip();
					GL11.glCallLists(this.buffer);
					this.buffer.clear();
				}
			}

			this.buffer.flip();
			GL11.glCallLists(this.buffer);
			GL11.glPopMatrix();
			GL11.glPopMatrix();
			GL11.glDisable('\u803a');
		}
	}

	// Spout End
	public void renderString(String var1, int var2, int var3, int var4, boolean var5) {
		if (var1 != null) {
			boolean var6 = false;
			int var7;
			if (var5) {
				var7 = var4 & -16777216;
				var4 = (var4 & 16579836) >> 2;
				var4 += var7;
			}

			GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, this.fontTextureName);
			float var11 = (float) (var4 >> 16 & 255) / 255.0F;
			float var8 = (float) (var4 >> 8 & 255) / 255.0F;
			float var9 = (float) (var4 & 255) / 255.0F;
			float var10 = (float) (var4 >> 24 & 255) / 255.0F;
			if (var10 == 0.0F) {
				var10 = 1.0F;
			}

			GL11.glColor4f(var11, var8, var9, var10);
			this.buffer.clear();
			GL11.glPushMatrix();
			GL11.glTranslatef((float) var2, (float) var3, 0.0F);

			for (var7 = 0; var7 < var1.length(); ++var7) {
				int var14;
				for (; var1.length() > var7 + 1 && var1.charAt(var7) == 167; var7 += 2) {
					char var13 = var1.toLowerCase().charAt(var7 + 1);
					if (var13 == 107) {
						var6 = true;
					} else {
						var6 = false;
						var14 = "0123456789abcdef".indexOf(var13);
						if (var14 < 0 || var14 > 15) {
							var14 = 15;
						}

						this.buffer.put(this.fontDisplayLists + 256 + var14 + (var5 ? 16 : 0));
						if (this.buffer.remaining() == 0) {
							this.buffer.flip();
							GL11.glCallLists(this.buffer);
							this.buffer.clear();
						}
					}
				}

				if (var7 < var1.length()) {
					int var12 = ChatAllowedCharacters.allowedCharacters.indexOf(var1.charAt(var7));
					if (var12 >= 0) {
						if (var6) {
							boolean var15 = false;

							do {
								var14 = this.field_41064_c.nextInt(ChatAllowedCharacters.allowedCharacters.length());
							} while (this.charWidth[var12 + 32] != this.charWidth[var14 + 32]);

							this.buffer.put(this.fontDisplayLists + 256 + this.field_41064_c.nextInt(2) + 8 + (var5 ? 16 : 0));
							this.buffer.put(this.fontDisplayLists + var14 + 32);
						} else {
							this.buffer.put(this.fontDisplayLists + var12 + 32);
						}
					}
				}

				if (this.buffer.remaining() == 0) {
					this.buffer.flip();
					GL11.glCallLists(this.buffer);
					this.buffer.clear();
				}
			}

			this.buffer.flip();
			GL11.glCallLists(this.buffer);
			GL11.glPopMatrix();
		}
	}

	public int getStringWidth(String var1) {
		if (var1 == null) {
			return 0;
		} else {
// Spout HD Start
			float var2 = 0.0F;
// Spout HD End
			for (int var3 = 0; var3 < var1.length(); ++var3) {
				if (var1.charAt(var3) == 167) {
					++var3;
				} else {
					int var4 = ChatAllowedCharacters.allowedCharacters.indexOf(var1.charAt(var3));
					if (var4 >= 0) {
// Spout HD Start
						var2 += this.charWidthf[var4 + 32];
// Spout HD End
					}
				}
			}
// Spout HD Start
			return Math.round(var2);
// Spout HD End
		}
	}

	public void drawSplitString(String var1, int var2, int var3, int var4, int var5) {
		this.func_40609_a(var1, var2, var3, var4, var5, false);
	}

	public void func_40609_a(String var1, int var2, int var3, int var4, int var5, boolean var6) {
		String[] var7 = var1.split("\n");
		if (var7.length > 1) {
			for (int var14 = 0; var14 < var7.length; ++var14) {
				this.drawSplitString(var7[var14], var2, var3, var4, var5);
				var3 += this.splitStringWidth(var7[var14], var4);
			}

		} else {
			String[] var8 = var1.split(" ");
			int var9 = 0;
			String var10 = "";

			while (var9 < var8.length) {
				String var11;
				for (var11 = var10 + var8[var9++] + " "; var9 < var8.length && this.getStringWidth(var11 + var8[var9]) < var4; var11 = var11 + var8[var9++] + " ") {
					;
				}

				int var12;
				for (; this.getStringWidth(var11) > var4; var11 = var10 + var11.substring(var12)) {
					for (var12 = 0; this.getStringWidth(var11.substring(0, var12 + 1)) <= var4; ++var12) {
						;
					}

					if (var11.substring(0, var12).trim().length() > 0) {
						String var13 = var11.substring(0, var12);
						if (var13.lastIndexOf("\u00a7") >= 0) {
							var10 = "\u00a7" + var13.charAt(var13.lastIndexOf("\u00a7") + 1);
						}

						this.renderString(var13, var2, var3, var5, var6);
						var3 += this.field_41063_b;
					}
				}

				if (this.getStringWidth(var11.trim()) > 0) {
					if (var11.lastIndexOf("\u00a7") >= 0) {
						var10 = "\u00a7" + var11.charAt(var11.lastIndexOf("\u00a7") + 1);
					}

					this.renderString(var11, var2, var3, var5, var6);
					var3 += this.field_41063_b;
				}
			}

		}
	}

	public int splitStringWidth(String var1, int var2) {
		String[] var3 = var1.split("\n");
		int var5;
		if (var3.length > 1) {
			int var9 = 0;

			for (var5 = 0; var5 < var3.length; ++var5) {
				var9 += this.splitStringWidth(var3[var5], var2);
			}

			return var9;
		} else {
			String[] var4 = var1.split(" ");
			var5 = 0;
			int var6 = 0;

			while (var5 < var4.length) {
				String var7;
				for (var7 = var4[var5++] + " "; var5 < var4.length && this.getStringWidth(var7 + var4[var5]) < var2; var7 = var7 + var4[var5++] + " ") {
					;
				}

				int var8;
				for (; this.getStringWidth(var7) > var2; var7 = var7.substring(var8)) {
					for (var8 = 0; this.getStringWidth(var7.substring(0, var8 + 1)) <= var2; ++var8) {
						;
					}

					if (var7.substring(0, var8).trim().length() > 0) {
						var6 += this.field_41063_b;
					}
				}

				if (var7.trim().length() > 0) {
					var6 += this.field_41063_b;
				}
			}
// Spout HD Start
			if(var6 < this.field_41063_b) {
				var6 += this.field_41063_b;
			}

			return var6;
// Spout HD Start
		}
	}

	// Spout HD Start
	public void initialize(GameSettings var1, String var2, RenderEngine var3) {
		this.charWidth = new int[256];
		this.fontTextureName = 0;
		this.field_41063_b = 8;
		this.buffer = GLAllocation.createDirectIntBuffer(1024 /* GL_FRONT_LEFT */);
		this.field_41064_c = new Random();

		BufferedImage var4;
		try {
			var4 = TextureUtils.getResourceAsBufferedImage(var2);
		} catch (IOException var18) {
			throw new RuntimeException(var18);
		}

		int var5 = var4.getWidth();
		int var6 = var4.getHeight();
		int[] var7 = new int[var5 * var6];
		var4.getRGB(0, 0, var5, var6, var7, 0, var5);
		this.charWidthf = FontUtils.computeCharWidths(var2, var4, var7, this.charWidth);
		this.fontTextureName = var3.allocateAndSetupTexture(var4);
		this.fontDisplayLists = GLAllocation.generateDisplayLists(288);
		Tessellator var8 = Tessellator.instance;

		int var9;
		int var10;
		int var11;
		for (var9 = 0; var9 < 256; ++var9) {
			GL11.glNewList(this.fontDisplayLists + var9, 4864 /* GL_COMPILE */);
			var8.startDrawingQuads();
			var10 = var9 % 16 * 8;
			var11 = var9 / 16 * 8;
			float var12 = 7.99F;
			float var13 = 0.0F;
			float var14 = 0.0F;
			var8.addVertexWithUV(0.0D, (double)(0.0F + var12), 0.0D, (double)((float)var10 / 128.0F + var13), (double)(((float)var11 + var12) / 128.0F + var14));
			var8.addVertexWithUV((double)(0.0F + var12), (double)(0.0F + var12), 0.0D, (double)(((float)var10 + var12) / 128.0F + var13), (double)(((float)var11 + var12) / 128.0F + var14));
			var8.addVertexWithUV((double)(0.0F + var12), 0.0D, 0.0D, (double)(((float)var10 + var12) / 128.0F + var13), (double)((float)var11 / 128.0F + var14));
			var8.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)((float)var10 / 128.0F + var13), (double)((float)var11 / 128.0F + var14));
			var8.draw();
			GL11.glTranslatef(this.charWidthf[var9], 0.0F, 0.0F);
			GL11.glEndList();
		}

		for (var9 = 0; var9 < 32; ++var9) {
			var10 = (var9 >> 3 & 1) * 85;
			var11 = (var9 >> 2 & 1) * 170 + var10;
			int var19 = (var9 >> 1 & 1) * 170 + var10;
			int var20 = (var9 >> 0 & 1) * 170 + var10;
			if (var9 == 6) {
				var11 += 85;
			}

			boolean var21 = var9 >= 16;
			if (var1.anaglyph) {
				int var15 = (var11 * 30 + var19 * 59 + var20 * 11) / 100;
				int var16 = (var11 * 30 + var19 * 70) / 100;
				int var17 = (var11 * 30 + var20 * 70) / 100;
				var11 = var15;
				var19 = var16;
				var20 = var17;
			}

			if(var21) {
				var11 /= 4;
				var19 /= 4;
				var20 /= 4;
			}

			GL11.glNewList(this.fontDisplayLists + 256 + var9, 4864 /* GL_COMPILE */);
			GL11.glColor3f((float)var11 / 255.0F, (float)var19 / 255.0F, (float)var20 / 255.0F);
			GL11.glEndList();
		}
		// Spout HD End
	}
}
