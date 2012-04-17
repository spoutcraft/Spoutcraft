package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.Bidi;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

public class FontRenderer {
	private static final Pattern field_52015_r = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
	private int[] charWidth = new int[256];
	public int fontTextureName = 0;
	public int FONT_HEIGHT = 8;
	public Random fontRandom = new Random();
	private byte[] glyphWidth = new byte[65536];
	private final int[] glyphTextureName = new int[256];
	private int[] colorCode = new int[32];
	private int boundTextureName;
	private final RenderEngine renderEngine;
	private float posX;
	private float posY;
	private boolean unicodeFlag;
	private boolean bidiFlag;
	private float field_50115_n;
	private float field_50116_o;
	private float field_50118_p;
	private float field_50117_q;

	FontRenderer() {
		this.renderEngine = null;
	}

	public FontRenderer(GameSettings par1GameSettings, String par2Str, RenderEngine par3RenderEngine, boolean par4) {
		this.renderEngine = par3RenderEngine;
		this.unicodeFlag = par4;

		BufferedImage var5;
		try {
			var5 = ImageIO.read(RenderEngine.class.getResourceAsStream(par2Str));
			InputStream var6 = RenderEngine.class.getResourceAsStream("/font/glyph_sizes.bin");
			var6.read(this.glyphWidth);
		} catch (IOException var18) {
			throw new RuntimeException(var18);
		}

		int var19 = var5.getWidth();
		int var7 = var5.getHeight();
		int[] var8 = new int[var19 * var7];
		var5.getRGB(0, 0, var19, var7, var8, 0, var19);
		int var9 = 0;

		int var10;
		int var11;
		int var12;
		int var13;
		int var15;
		int var16;
		while (var9 < 256) {
			var10 = var9 % 16;
			var11 = var9 / 16;
			var12 = 7;

			while (true) {
				if (var12 >= 0) {
					var13 = var10 * 8 + var12;
					boolean var14 = true;

					for (var15 = 0; var15 < 8 && var14; ++var15) {
						var16 = (var11 * 8 + var15) * var19;
						int var17 = var8[var13 + var16] & 255;
						if (var17 > 0) {
							var14 = false;
						}
					}

					if (var14) {
						--var12;
						continue;
					}
				}

				if (var9 == 32) {
					var12 = 2;
				}

				this.charWidth[var9] = var12 + 2;
				++var9;
				break;
			}
		}

		this.fontTextureName = par3RenderEngine.allocateAndSetupTexture(var5);

		for (var9 = 0; var9 < 32; ++var9) {
			var10 = (var9 >> 3 & 1) * 85;
			var11 = (var9 >> 2 & 1) * 170 + var10;
			var12 = (var9 >> 1 & 1) * 170 + var10;
			var13 = (var9 >> 0 & 1) * 170 + var10;
			if (var9 == 6) {
				var11 += 85;
			}

			if (par1GameSettings.anaglyph) {
				int var20 = (var11 * 30 + var12 * 59 + var13 * 11) / 100;
				var15 = (var11 * 30 + var12 * 70) / 100;
				var16 = (var11 * 30 + var13 * 70) / 100;
				var11 = var20;
				var12 = var15;
				var13 = var16;
			}

			if (var9 >= 16) {
				var11 /= 4;
				var12 /= 4;
				var13 /= 4;
			}

			this.colorCode[var9] = (var11 & 255) << 16 | (var12 & 255) << 8 | var13 & 255;
		}
	}

	private float func_50112_a(int par1, char par2, boolean par3) {
		return par2 == 32?4.0F:(par1 > 0 && !this.unicodeFlag?this.func_50106_a(par1 + 32, par3):this.func_50111_a(par2, par3));
	}

	private float func_50106_a(int par1, boolean par2) {
		float var3 = (float)(par1 % 16 * 8);
		float var4 = (float)(par1 / 16 * 8);
		float var5 = par2?1.0F:0.0F;
		if (this.boundTextureName != this.fontTextureName) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureName);
			this.boundTextureName = this.fontTextureName;
		}

		float var6 = (float)this.charWidth[par1] - 0.01F;
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(var3 / 128.0F, var4 / 128.0F);
		GL11.glVertex3f(this.posX + var5, this.posY, 0.0F);
		GL11.glTexCoord2f(var3 / 128.0F, (var4 + 7.99F) / 128.0F);
		GL11.glVertex3f(this.posX - var5, this.posY + 7.99F, 0.0F);
		GL11.glTexCoord2f((var3 + var6) / 128.0F, var4 / 128.0F);
		GL11.glVertex3f(this.posX + var6 + var5, this.posY, 0.0F);
		GL11.glTexCoord2f((var3 + var6) / 128.0F, (var4 + 7.99F) / 128.0F);
		GL11.glVertex3f(this.posX + var6 - var5, this.posY + 7.99F, 0.0F);
		GL11.glEnd();
		return (float)this.charWidth[par1];
	}

	private void loadGlyphTexture(int par1) {
		String var3 = String.format("/font/glyph_%02X.png", new Object[]{Integer.valueOf(par1)});

		BufferedImage var2;
		try {
			var2 = ImageIO.read(RenderEngine.class.getResourceAsStream(var3));
		} catch (IOException var5) {
			throw new RuntimeException(var5);
		}

		this.glyphTextureName[par1] = this.renderEngine.allocateAndSetupTexture(var2);
		this.boundTextureName = this.glyphTextureName[par1];
	}

	private float func_50111_a(char par1, boolean par2) {
		if (this.glyphWidth[par1] == 0) {
			return 0.0F;
		} else {
			int var3 = par1 / 256;
			if (this.glyphTextureName[var3] == 0) {
				this.loadGlyphTexture(var3);
			}

			if (this.boundTextureName != this.glyphTextureName[var3]) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.glyphTextureName[var3]);
				this.boundTextureName = this.glyphTextureName[var3];
			}

			int var4 = this.glyphWidth[par1] >>> 4;
			int var5 = this.glyphWidth[par1] & 15;
			float var6 = (float)var4;
			float var7 = (float)(var5 + 1);
			float var8 = (float)(par1 % 16 * 16) + var6;
			float var9 = (float)((par1 & 255) / 16 * 16);
			float var10 = var7 - var6 - 0.02F;
			float var11 = par2?1.0F:0.0F;
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			GL11.glTexCoord2f(var8 / 256.0F, var9 / 256.0F);
			GL11.glVertex3f(this.posX + var11, this.posY, 0.0F);
			GL11.glTexCoord2f(var8 / 256.0F, (var9 + 15.98F) / 256.0F);
			GL11.glVertex3f(this.posX - var11, this.posY + 7.99F, 0.0F);
			GL11.glTexCoord2f((var8 + var10) / 256.0F, var9 / 256.0F);
			GL11.glVertex3f(this.posX + var10 / 2.0F + var11, this.posY, 0.0F);
			GL11.glTexCoord2f((var8 + var10) / 256.0F, (var9 + 15.98F) / 256.0F);
			GL11.glVertex3f(this.posX + var10 / 2.0F - var11, this.posY + 7.99F, 0.0F);
			GL11.glEnd();
			return (var7 - var6) / 2.0F + 1.0F;
		}
	}

	public int drawStringWithShadow(String par1Str, int par2, int par3, int par4) {
		if (this.bidiFlag) {
			par1Str = this.bidiReorder(par1Str);
		}

		int var5 = this.func_50101_a(par1Str, par2 + 1, par3 + 1, par4, true);
		var5 = Math.max(var5, this.func_50101_a(par1Str, par2, par3, par4, false));
		return var5;
	}

	public void drawString(String par1Str, int par2, int par3, int par4) {
		if (this.bidiFlag) {
			par1Str = this.bidiReorder(par1Str);
		}

		this.func_50101_a(par1Str, par2, par3, par4, false);
	}

	private String bidiReorder(String par1Str) {
		if (par1Str != null && Bidi.requiresBidi(par1Str.toCharArray(), 0, par1Str.length())) {
			Bidi var2 = new Bidi(par1Str, -2);
			byte[] var3 = new byte[var2.getRunCount()];
			String[] var4 = new String[var3.length];

			int var7;
			for (int var5 = 0; var5 < var3.length; ++var5) {
				int var6 = var2.getRunStart(var5);
				var7 = var2.getRunLimit(var5);
				int var8 = var2.getRunLevel(var5);
				String var9 = par1Str.substring(var6, var7);
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
					} else {
						for (var14 = var4[var7].length() - 1; var14 >= 0; --var14) {
							char var10 = var4[var7].charAt(var14);
							if (var10 == 40) {
								var10 = 41;
							} else if (var10 == 41) {
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
		} else {
			return par1Str;
		}
	}

	private void renderStringAtPos(String par1Str, boolean par2) {
		boolean var3 = false;
		boolean var4 = false;
		boolean var5 = false;
		boolean var6 = false;
		boolean var7 = false;

		for (int var8 = 0; var8 < par1Str.length(); ++var8) {
			char var9 = par1Str.charAt(var8);
			int var10;
			int var11;
			if (var9 == 167 && var8 + 1 < par1Str.length()) {
				var10 = "0123456789abcdefklmnor".indexOf(par1Str.toLowerCase().charAt(var8 + 1));
				if (var10 < 16) {
					var3 = false;
					var4 = false;
					var7 = false;
					var6 = false;
					var5 = false;
					if (var10 < 0 || var10 > 15) {
						var10 = 15;
					}

					if (par2) {
						var10 += 16;
					}

					var11 = this.colorCode[var10];
					GL11.glColor3f((float)(var11 >> 16) / 255.0F, (float)(var11 >> 8 & 255) / 255.0F, (float)(var11 & 255) / 255.0F);
				} else if (var10 == 16) {
					var3 = true;
				} else if (var10 == 17) {
					var4 = true;
				} else if (var10 == 18) {
					var7 = true;
				} else if (var10 == 19) {
					var6 = true;
				} else if (var10 == 20) {
					var5 = true;
				} else if (var10 == 21) {
					var3 = false;
					var4 = false;
					var7 = false;
					var6 = false;
					var5 = false;
					GL11.glColor4f(this.field_50115_n, this.field_50116_o, this.field_50118_p, this.field_50117_q);
				}

				++var8;
			} else {
				var10 = ChatAllowedCharacters.allowedCharacters.indexOf(var9);
				if (var3 && var10 > 0) {
					do {
						var11 = this.fontRandom.nextInt(ChatAllowedCharacters.allowedCharacters.length());
					} while (this.charWidth[var10 + 32] != this.charWidth[var11 + 32]);

					var10 = var11;
				}

				float var14 = this.func_50112_a(var10, var9, var5);
				if (var4) {
					++this.posX;
					this.func_50112_a(var10, var9, var5);
					--this.posX;
					++var14;
				}

				Tessellator var12;
				if (var7) {
					var12 = Tessellator.instance;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					var12.startDrawingQuads();
					var12.addVertex((double)this.posX, (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0.0D);
					var12.addVertex((double)(this.posX + var14), (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0.0D);
					var12.addVertex((double)(this.posX + var14), (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0F), 0.0D);
					var12.addVertex((double)this.posX, (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0F), 0.0D);
					var12.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				if (var6) {
					var12 = Tessellator.instance;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					var12.startDrawingQuads();
					int var13 = var6?-1:0;
					var12.addVertex((double)(this.posX + (float)var13), (double)(this.posY + (float)this.FONT_HEIGHT), 0.0D);
					var12.addVertex((double)(this.posX + var14), (double)(this.posY + (float)this.FONT_HEIGHT), 0.0D);
					var12.addVertex((double)(this.posX + var14), (double)(this.posY + (float)this.FONT_HEIGHT - 1.0F), 0.0D);
					var12.addVertex((double)(this.posX + (float)var13), (double)(this.posY + (float)this.FONT_HEIGHT - 1.0F), 0.0D);
					var12.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
			}

				this.posX += var14;
		}
	}
	}

	public int func_50101_a(String par1Str, int par2, int par3, int par4, boolean par5) {
		if (par1Str != null) {
			this.boundTextureName = 0;
			if ((par4 & -67108864) == 0) {
				par4 |= -16777216;
			}

			if (par5) {
				par4 = (par4 & 16579836) >> 2 | par4 & -16777216;
			}

			this.field_50115_n = (float)(par4 >> 16 & 255) / 255.0F;
			this.field_50116_o = (float)(par4 >> 8 & 255) / 255.0F;
			this.field_50118_p = (float)(par4 & 255) / 255.0F;
			this.field_50117_q = (float)(par4 >> 24 & 255) / 255.0F;
			GL11.glColor4f(this.field_50115_n, this.field_50116_o, this.field_50118_p, this.field_50117_q);
			this.posX = (float)par2;
			this.posY = (float)par3;
			this.renderStringAtPos(par1Str, par5);
			return (int)this.posX;
		} else {
			return 0;
		}
	}

	public int getStringWidth(String par1Str) {
		if (par1Str == null) {
			return 0;
		} else {
			par1Str = org.bukkit.ChatColor.stripColor(par1Str); //Spout strip colors when calculating width.
			int width2 = 0;
			boolean bold3 = false;

			for (int j4 = 0; j4 < par1Str.length(); ++j4) {
				char var5 = par1Str.charAt(j4);
				int width6 = this.getCharHalfWidth(var5);		//  Spout - Unicode positioning fix
				if (width6 < 0 && j4 < par1Str.length() - 1) {
					++j4;
					var5 = par1Str.charAt(j4);
					if (var5 != 108 && var5 != 76) {
						if (var5 == 114 || var5 == 82) {
							bold3 = false;
						}
					} else {
						bold3 = true;
					}

					width6 = this.getCharHalfWidth(var5);		//  Spout - Unicode positioning fix
				}

				width2 += width6;
				if (bold3) {
					width2+=2;		//  Spout - Unicode positioning fix
				}
			}

			return width2 >> 1;		//  Spout - Unicode positioning fix
		}
	}

	public int func_50105_a(char par1) {
		return getCharHalfWidth(par1) >> 1;		// Spout start - Unicode text positioning fix.
	}

	private int getCharHalfWidth(char par1) {		// Spout end - Unicode text positioning fix.
		if (par1 == 167) {
			return -1;
		} else if (par1 == 32) {		// Spout - Unicode text positioning fix.
			return 8;		// Spout - Unicode text positioning fix.
		} else {
			int var2 = ChatAllowedCharacters.allowedCharacters.indexOf(par1);
			if (var2 >= 0 && !this.unicodeFlag) {
				return this.charWidth[var2 + 32] << 1;		// Spout - Unicode text positioning fix.
			} else if (this.glyphWidth[par1] != 0) {
				int var3 = this.glyphWidth[par1] >> 4;
				int var4 = this.glyphWidth[par1] & 15;

				return (var4 - var3) + 3;		// Spout - Unicode text positioning fix.
			} else {
				return 0;
			}
		}
	}

	public String func_50107_a(String par1Str, int par2) {
		return this.func_50104_a(par1Str, par2, false);
	}

	public String func_50104_a(String par1Str, int width2, boolean par3) {
		width2 += width2;		// Spout - Unicode positioning fix
		StringBuilder string4 = new StringBuilder();
		int width5 = 0;
		int var6 = par3?par1Str.length() - 1:0;
		int step7 = par3?-1:1;
		boolean var8 = false;
		boolean bold9 = false;

		for (int j10 = var6; j10 >= 0 && j10 < par1Str.length() && width5 < width2; j10 += step7) {
			char var11 = par1Str.charAt(j10);
			int width12 = this.getCharHalfWidth(var11);		// Spout - Unicode positioning fix
			if (var8) {
				var8 = false;
				if (var11 != 108 && var11 != 76) {
					if (var11 == 114 || var11 == 82) {
						bold9 = false;
					}
				} else {
					bold9 = true;
				}
			} else if (width12 < 0) {
				var8 = true;
			} else {
				width5 += width12;
				if (bold9) {
					width5+=2;		// Spout - Unicode positioning fix
				}
			}

			if (width5 > width2) {
				break;
			}

			if (par3) {
				string4.insert(0, var11);
			} else {
				string4.append(var11);
			}
		}

		return string4.toString();
	}

	public void drawSplitString(String par1Str, int par2, int par3, int par4, int par5) {
		if (this.bidiFlag) {
			par1Str = this.bidiReorder(par1Str);
		}

		this.renderSplitStringNoShadow(par1Str, par2, par3, par4, par5);
	}

	private void renderSplitStringNoShadow(String par1Str, int par2, int par3, int par4, int par5) {
		this.renderSplitString(par1Str, par2, par3, par4, par5, false);
	}

	private void renderSplitString(String par1Str, int par2, int par3, int par4, int par5, boolean par6) {
		String[] var7 = par1Str.split("\n");
		if (var7.length > 1) {
			for (int var14 = 0; var14 < var7.length; ++var14) {
				this.renderSplitStringNoShadow(var7[var14], par2, par3, par4, par5);
				par3 += this.splitStringWidth(var7[var14], par4);
			}
		} else {
			String[] var8 = par1Str.split(" ");
			int var9 = 0;
			String var10 = "";

			while (var9 < var8.length) {
				String var11;
				for (var11 = var10 + var8[var9++] + " "; var9 < var8.length && this.getStringWidth(var11 + var8[var9]) < par4; var11 = var11 + var8[var9++] + " ") {
					;
				}

				int var12;
				for (; this.getStringWidth(var11) > par4; var11 = var10 + var11.substring(var12)) {
					for (var12 = 0; this.getStringWidth(var11.substring(0, var12 + 1)) <= par4; ++var12) {
						;
					}

					if (var11.substring(0, var12).trim().length() > 0) {
						String var13 = var11.substring(0, var12);
						if (var13.lastIndexOf("\u00a7") >= 0) {
							var10 = "\u00a7" + var13.charAt(var13.lastIndexOf("\u00a7") + 1);
						}

						this.func_50101_a(var13, par2, par3, par5, par6);
						par3 += this.FONT_HEIGHT;
					}
				}

				if (this.getStringWidth(var11.trim()) > 0) {
					if (var11.lastIndexOf("\u00a7") >= 0) {
						var10 = "\u00a7" + var11.charAt(var11.lastIndexOf("\u00a7") + 1);
					}

					this.func_50101_a(var11, par2, par3, par5, par6);
					par3 += this.FONT_HEIGHT;
				}
			}
		}
	}

	public int splitStringWidth(String par1Str, int par2) {
		String[] var3 = par1Str.split("\n");
		int var5;
		if (var3.length > 1) {
			int var9 = 0;

			for (var5 = 0; var5 < var3.length; ++var5) {
				var9 += this.splitStringWidth(var3[var5], par2);
			}

			return var9;
		} else {
			String[] var4 = par1Str.split(" ");
			var5 = 0;
			int var6 = 0;

			while (var5 < var4.length) {
				String var7;
				for (var7 = var4[var5++] + " "; var5 < var4.length && this.getStringWidth(var7 + var4[var5]) < par2; var7 = var7 + var4[var5++] + " ") {
					;
				}

				int var8;
				for (; this.getStringWidth(var7) > par2; var7 = var7.substring(var8)) {
					for (var8 = 0; this.getStringWidth(var7.substring(0, var8 + 1)) <= par2; ++var8) {
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

	public void setUnicodeFlag(boolean par1) {
		this.unicodeFlag = par1;
	}

	public void setBidiFlag(boolean par1) {
		this.bidiFlag = par1;
	}

	public List func_50108_c(String par1Str, int par2) {
		return Arrays.asList(this.func_50113_d(par1Str, par2).split("\n"));
	}

	String func_50113_d(String par1Str, int par2) {
		int var3 = this.func_50102_e(par1Str, par2);
		if (par1Str.length() <= var3) {
			return par1Str;
		} else {
			String var4 = par1Str.substring(0, var3);
			String var5 = func_50114_c(var4) + par1Str.substring(var3 + (par1Str.charAt(var3) == 32?1:0));
			return var4 + "\n" + this.func_50113_d(var5, par2);
		}
	}

	public int func_50102_e(String par1Str, int width2) {
		width2+=width2;		//  Spout - Unicode positioning fix
		int length3 = par1Str.length();
		int width4 = 0;
		int j5 = 0;
		int j6 = -1;

		for (boolean bold7 = false; j5 < length3; ++j5) {
			char var8 = par1Str.charAt(j5);
			switch(var8) {
			case 167:
				if (j5 != length3) {
					++j5;
					char var9 = par1Str.charAt(j5);
					if (var9 != 108 && var9 != 76) {
						if (var9 == 114 || var9 == 82) {
							bold7 = false;
						}
					} else {
						bold7 = true;
					}
				}
				break;
			default:
				if (var8 == 32){		// Spout start - Unicode positioning fix
					j6 = j5;
				}
				width4 += this.getCharHalfWidth(var8);
				if (bold7) {
					width4+=2;		// Spout end - Unicode positioning fix
				}
			}

			if (var8 == 10) {
				++j5;
				j6 = j5;
				break;
			}

			if (width4 > width2) {
				break;
			}
		}

		return j5 != length3 && j6 != -1 && j6 < j5?j6:j5;
	}

	private static boolean func_50110_b(char par0) {
		return par0 >= 48 && par0 <= 57 || par0 >= 97 && par0 <= 102 || par0 >= 65 && par0 <= 70;
	}

	private static boolean func_50109_c(char par0) {
		return par0 >= 107 && par0 <= 111 || par0 >= 75 && par0 <= 79 || par0 == 114 || par0 == 82;
	}

	private static String func_50114_c(String par0Str) {
		String var1 = "";
		int var2 = -1;
		int var3 = par0Str.length();

		while ((var2 = par0Str.indexOf(167, var2 + 1)) != -1) {
			if (var2 < var3 - 1) {
				char var4 = par0Str.charAt(var2 + 1);
				if (func_50110_b(var4)) {
					var1 = "\u00a7" + var4;
				} else if (func_50109_c(var4)) {
					var1 = var1 + "\u00a7" + var4;
				}
			}
		}

		return var1;
	}

	public static String func_52014_d(String par0Str) {
		return field_52015_r.matcher(par0Str).replaceAll("");
	}
}
