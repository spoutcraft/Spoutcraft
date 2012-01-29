package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.Bidi;
import java.util.Random;
import javax.imageio.ImageIO;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.GameSettings;
import net.minecraft.src.RenderEngine;
import org.lwjgl.opengl.GL11;
//Spout HD Start
import com.pclewis.mcpatcher.mod.FontUtils;
import com.pclewis.mcpatcher.mod.TextureUtils;

//Spout HD End

public class FontRenderer {

	private int[] charWidth = new int[256];
	public int fontTextureName = 0;
	public int FONT_HEIGHT = 8;
	public Random field_41064_c = new Random();
	private byte[] field_44036_e = new byte[65536];
	private int[] field_44034_f = new int[256];
	private int[] field_44035_g = new int[32];
	private int field_44038_h;
	private RenderEngine field_44039_i;
	private float field_46126_j;
	private float field_46127_k;
	public boolean field_44037_j; // Spout HD private->public
	private boolean field_46125_m;
	public float[] charWidthf; // Spout HD

	public FontRenderer(GameSettings var1, String var2, RenderEngine var3, boolean var4) {
		this.field_44039_i = var3;
		this.field_44037_j = var4;
// Spout HD start
		BufferedImage var5;
		try {
			var5 = TextureUtils.getResourceAsBufferedImage(var2);
			InputStream var6 = RenderEngine.class.getResourceAsStream("/font/glyph_sizes.bin");
			var6.read(this.field_44036_e);
		}
		catch (IOException var17) {
			throw new RuntimeException(var17);
		}

		int var18 = var5.getWidth();
		int var7 = var5.getHeight();
		int[] var8 = new int[var18 * var7];
		var5.getRGB(0, 0, var18, var7, var8, 0, var18);
		this.charWidthf = FontUtils.computeCharWidths(var2, var5, var8, this.charWidth);
// Spout HD end
		this.fontTextureName = var3.allocateAndSetupTexture(var5);
// Spout HD start
		for (int var9 = 0; var9 < 32; ++var9) {
			int var10 = (var9 >> 3 & 1) * 85;
			int var11 = (var9 >> 2 & 1) * 170 + var10;
			int var12 = (var9 >> 1 & 1) * 170 + var10;
			int var13 = (var9 >> 0 & 1) * 170 + var10;
// Spout HD end
			if (var9 == 6) {
				var11 += 85;
			}

			if (var1.anaglyph) {
// Spout HD start
				int var14 = (var11 * 30 + var12 * 59 + var13 * 11) / 100;
				int var15 = (var11 * 30 + var12 * 70) / 100;
				int var16 = (var11 * 30 + var13 * 70) / 100;
				var11 = var14;
// Spout HD end
				var12 = var15;
				var13 = var16;
			}

			if (var9 >= 16) {
				var11 /= 4;
				var12 /= 4;
				var13 /= 4;
			}

			this.field_44035_g[var9] = (var11 & 255) << 16 | (var12 & 255) << 8 | var13 & 255;
		}
	}

	private void func_44031_a(int var1) {
		float var2 = (float)(var1 % 16 * 8);
		float var3 = (float)(var1 / 16 * 8);
		if (this.field_44038_h != this.fontTextureName) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureName);
			this.field_44038_h = this.fontTextureName;
		}

		float var4 = this.charWidthf[var1] - 0.01F;// Spout HD
		GL11.glBegin(5);
		GL11.glTexCoord2f(var2 / 128.0F, var3 / 128.0F);
		GL11.glVertex3f(this.field_46126_j, this.field_46127_k, 0.0F);
		GL11.glTexCoord2f(var2 / 128.0F, (var3 + 7.99F) / 128.0F);
		GL11.glVertex3f(this.field_46126_j, this.field_46127_k + 7.99F, 0.0F);
		GL11.glTexCoord2f((var2 + var4) / 128.0F, var3 / 128.0F);
		GL11.glVertex3f(this.field_46126_j + var4, this.field_46127_k, 0.0F);
		GL11.glTexCoord2f((var2 + var4) / 128.0F, (var3 + 7.99F) / 128.0F);
		GL11.glVertex3f(this.field_46126_j + var4, this.field_46127_k + 7.99F, 0.0F);
		GL11.glEnd();
		this.field_46126_j += this.charWidthf[var1];// Spout HD
	}

	private void func_44030_b(int var1) {
		String var3 = String.format("/font/glyph_%02X.png", new Object[] {Integer.valueOf(var1)});

		BufferedImage var2;
		try {
			var2 = ImageIO.read(RenderEngine.class.getResourceAsStream(var3.toString()));
		}
		catch (IOException var5) {
			throw new RuntimeException(var5);
		}

		this.field_44034_f[var1] = this.field_44039_i.allocateAndSetupTexture(var2);
		this.field_44038_h = this.field_44034_f[var1];
	}

	private void func_44033_a(char var1) {
		if (this.field_44036_e[var1] != 0) {
			int var2 = var1 / 256;
			if (this.field_44034_f[var2] == 0) {
				this.func_44030_b(var2);
			}

			if (this.field_44038_h != this.field_44034_f[var2]) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.field_44034_f[var2]);
				this.field_44038_h = this.field_44034_f[var2];
			}

			int var3 = this.field_44036_e[var1] >>> 4;
			int var4 = this.field_44036_e[var1] & 15;
			float var5 = (float)var3;
			float var6 = (float)(var4 + 1);
			float var7 = (float)(var1 % 16 * 16) + var5;
			float var8 = (float)((var1 & 255) / 16 * 16);
			float var9 = var6 - var5 - 0.02F;
			GL11.glBegin(5);
			GL11.glTexCoord2f(var7 / 256.0F, var8 / 256.0F);
			GL11.glVertex3f(this.field_46126_j, this.field_46127_k, 0.0F);
			GL11.glTexCoord2f(var7 / 256.0F, (var8 + 15.98F) / 256.0F);
			GL11.glVertex3f(this.field_46126_j, this.field_46127_k + 7.99F, 0.0F);
			GL11.glTexCoord2f((var7 + var9) / 256.0F, var8 / 256.0F);
			GL11.glVertex3f(this.field_46126_j + var9 / 2.0F, this.field_46127_k, 0.0F);
			GL11.glTexCoord2f((var7 + var9) / 256.0F, (var8 + 15.98F) / 256.0F);
			GL11.glVertex3f(this.field_46126_j + var9 / 2.0F, this.field_46127_k + 7.99F, 0.0F);
			GL11.glEnd();
			this.field_46126_j += (var6 - var5) / 2.0F + 1.0F;
		}
	}

	public void drawStringWithShadow(String var1, int var2, int var3, int var4) {
		if (this.field_46125_m) {
			var1 = this.func_46121_b(var1);
		}

		this.renderString(var1, var2 + 1, var3 + 1, var4, true);
		this.renderString(var1, var2, var3, var4, false);
	}

	public void drawString(String var1, int var2, int var3, int var4) {
		if (this.field_46125_m) {
			var1 = this.func_46121_b(var1);
		}

		this.renderString(var1, var2, var3, var4, false);
	}

	private String func_46121_b(String var1) {
		if (var1 != null && Bidi.requiresBidi(var1.toCharArray(), 0, var1.length())) {
			Bidi var2 = new Bidi(var1, -2);
			byte[] var3 = new byte[var2.getRunCount()];
			String[] var4 = new String[var3.length];

			int var7;
			for (int var5 = 0; var5 < var3.length; ++var5) {
				int var6 = var2.getRunStart(var5);
				var7 = var2.getRunLimit(var5);
				int var8 = var2.getRunLevel(var5);
				String var9 = var1.substring(var6, var7);
				var3[var5] = (byte)var8;
				var4[var5] = var9;
			}

			String[] var11 = (String[])var4.clone();
			Bidi.reorderVisually(var3, 0, var4, 0, var3.length);
			StringBuilder var12 = new StringBuilder();
			var7 = 0;

			while (var7 < var4.length) {
				byte var13 = var3[var7];
				int var14 = 0;

				while (true) {
					if (var14 < var11.length) {
						if (!var11[var14].equals(var4[var7])) {
							++var14;
							continue;
					}

						var13 = var3[var14];
					}

					if ((var13 & 1) == 0) {
						var12.append(var4[var7]);
					}
					else {
						for (var14 = var4[var7].length() - 1; var14 >= 0; --var14) {
							char var10 = var4[var7].charAt(var14);
							if (var10 == 40) {
								var10 = 41;
							}
							else if (var10 == 41) {
								var10 = 40;
				}

							var12.append(var10);
					}
				}

					++var7;
					break;
				}
			}

			return var12.toString();
		}
		else {
			return var1;
		}
	}

	private void func_44029_a(String var1, boolean var2) {
		boolean var3 = false;

		for (int var4 = 0; var4 < var1.length(); ++var4) {
			char var5 = var1.charAt(var4);
			int var6;
			int var7;
			if (var5 == 167 && var4 + 1 < var1.length()) {
				var6 = "0123456789abcdefk".indexOf(var1.toLowerCase().charAt(var4 + 1));
				if (var6 == 16) {
					var3 = true;
						}
				else {
					var3 = false;
					if (var6 < 0 || var6 > 15) {
						var6 = 15;
					}

					if (var2) {
						var6 += 16;
				}

					var7 = this.field_44035_g[var6];
					GL11.glColor3f((float)(var7 >> 16) / 255.0F, (float)(var7 >> 8 & 255) / 255.0F, (float)(var7 & 255) / 255.0F);
				}

				++var4;
			}
			else {
				var6 = ChatAllowedCharacters.allowedCharacters.indexOf(var5);
				if (var3 && var6 > 0) {
							do {
						var7 = this.field_41064_c.nextInt(ChatAllowedCharacters.allowedCharacters.length());
					}
					while (this.charWidth[var6 + 32] != this.charWidth[var7 + 32]);

					var6 = var7;
				}

				if (var5 == 32) {
					this.field_46126_j += 4.0F;
				}
				else if (var6 > 0 && !this.field_44037_j) {
					this.func_44031_a(var6 + 32);
				}
				else {
					this.func_44033_a(var5);
						}
					}
				}
	}

	private void renderString(String var1, int var2, int var3, int var4, boolean var5) {
		if (var1 != null) {
			this.field_44038_h = 0;
			if ((var4 & -67108864) == 0) {
				var4 |= -16777216;
				}

			if (var5) {
				var4 = (var4 & 16579836) >> 2 | var4 & -16777216;
			}

			GL11.glColor4f((float)(var4 >> 16 & 255) / 255.0F, (float)(var4 >> 8 & 255) / 255.0F, (float)(var4 & 255) / 255.0F, (float)(var4 >> 24 & 255) / 255.0F);
			this.field_46126_j = (float)var2;
			this.field_46127_k = (float)var3;
			this.func_44029_a(var1, var5);
		}
	}

	public int getStringWidth(String var1) {
		if (var1 == null) {
			return 0;
		}
		else {
			float var2 = 0.0F;// Spout HD

			for (int var3 = 0; var3 < var1.length(); ++var3) {
				char var4 = var1.charAt(var3);
				if (var4 == 167) {
					++var3;
					}
				else {
					int var5 = ChatAllowedCharacters.allowedCharacters.indexOf(var4);
					if (var5 >= 0 && !this.field_44037_j) {
						var2 += this.charWidthf[var5 + 32];// Spout HD
					}
					else if (this.field_44036_e[var4] != 0) {
						int var6 = this.field_44036_e[var4] >> 4;
						int var7 = this.field_44036_e[var4] & 15;
						if (var7 > 7) {
							var7 = 15;
							var6 = 0;
				}

						++var7;
						var2 += (float)((var7 - var6) / 2 + 1);// Spout HD
					}
			}
			}

			return Math.round(var2);// Spout HD
		}
	}

	public void drawSplitString(String var1, int var2, int var3, int var4, int var5) {
		if (this.field_46125_m) {
			var1 = this.func_46121_b(var1);
		}

		this.func_46124_b(var1, var2, var3, var4, var5);
	}

	private void func_46124_b(String var1, int var2, int var3, int var4, int var5) {
		this.func_46122_b(var1, var2, var3, var4, var5, false);
	}

	public void func_40609_a(String var1, int var2, int var3, int var4, int var5, boolean var6) {
		if (this.field_46125_m) {
			var1 = this.func_46121_b(var1);
		}

		this.func_46122_b(var1, var2, var3, var4, var5, var6);
	}

	private void func_46122_b(String var1, int var2, int var3, int var4, int var5, boolean var6) {
		String[] var7 = var1.split("\n");
		if (var7.length > 1) {
			for (int var14 = 0; var14 < var7.length; ++var14) {
				this.func_46124_b(var7[var14], var2, var3, var4, var5);
				var3 += this.splitStringWidth(var7[var14], var4);
			}
		}
		else {
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
						var3 += this.FONT_HEIGHT;
					}
				}

				if (this.getStringWidth(var11.trim()) > 0) {
					if (var11.lastIndexOf("\u00a7") >= 0) {
						var10 = "\u00a7" + var11.charAt(var11.lastIndexOf("\u00a7") + 1);
					}

					this.renderString(var11, var2, var3, var5, var6);
					var3 += this.FONT_HEIGHT;
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
		}
		else {
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
						var6 += this.FONT_HEIGHT;
					}
				}

				if (var7.trim().length() > 0) {
					var6 += this.FONT_HEIGHT;
				}
			}

			if (var6 < this.FONT_HEIGHT) {
				var6 += this.FONT_HEIGHT;
			}

			return var6;
		}
	}

	public void func_44032_a(boolean var1) {
		this.field_44037_j = var1;
	}

	public void func_46123_b(boolean var1) {
		this.field_46125_m = var1;
	}
// Spout HD start
	public void initialize(GameSettings var1, String var2, RenderEngine var3) {
		boolean var4 = false;
		this.charWidth = new int[256];
		this.fontTextureName = 0;
		this.FONT_HEIGHT = 8;
		this.field_41064_c = new Random();
		this.field_44036_e = new byte[65536];
		this.field_44034_f = new int[256];
		this.field_44035_g = new int[32];
		this.field_44039_i = var3;
		this.field_44037_j = var4;

		BufferedImage var5;
		try {
			var5 = TextureUtils.getResourceAsBufferedImage(var2);
			InputStream var6 = RenderEngine.class.getResourceAsStream("/font/glyph_sizes.bin");
			var6.read(this.field_44036_e);
		}
		catch (IOException var17) {
			throw new RuntimeException(var17);
		}

		int var18 = var5.getWidth();
		int var7 = var5.getHeight();
		int[] var8 = new int[var18 * var7];
		var5.getRGB(0, 0, var18, var7, var8, 0, var18);
		this.charWidthf = FontUtils.computeCharWidths(var2, var5, var8, this.charWidth);
		this.fontTextureName = var3.allocateAndSetupTexture(var5);

		for (int var9 = 0; var9 < 32; ++var9) {
			int var10 = (var9 >> 3 & 1) * 85;
			int var11 = (var9 >> 2 & 1) * 170 + var10;
			int var12 = (var9 >> 1 & 1) * 170 + var10;
			int var13 = (var9 >> 0 & 1) * 170 + var10;
			if (var9 == 6) {
				var11 += 85;
			}

			if (var1.anaglyph) {
				int var14 = (var11 * 30 + var12 * 59 + var13 * 11) / 100;
				int var15 = (var11 * 30 + var12 * 70) / 100;
				int var16 = (var11 * 30 + var13 * 70) / 100;
				var11 = var14;
				var12 = var15;
				var13 = var16;
			}

			if (var9 >= 16) {
				var11 /= 4;
				var12 /= 4;
				var13 /= 4;
			}

			this.field_44035_g[var9] = (var11 & 255) << 16 | (var12 & 255) << 8 | var13 & 255;
		}
		// Spout HD End
	}
}
