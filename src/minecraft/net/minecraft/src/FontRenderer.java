package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;
import com.pclewis.mcpatcher.mod.FontUtils;
import com.pclewis.mcpatcher.mod.TextureUtils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.Bidi;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import org.lwjgl.opengl.GL11;

//Spout rewritten - not even going to try to figure out where the changes are...
public class FontRenderer {
	private static final Pattern field_52015_r = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
	private int[] charWidth = new int[256];
	public int fontTextureName = 0;
	public int FONT_HEIGHT = 8;
	public Random fontRandom = new Random();
	private byte[] glyphWidth = new byte[65536];
	private int[] glyphTextureName = new int[256];
	private int[] colorCode = new int[32];
	private int boundTextureName;
	private RenderEngine renderEngine;
	private float posX;
	private float posY;
	public boolean unicodeFlag;
	private boolean bidiFlag;
	public float[] charWidthf;

	//begin Spout AlphaText
	private int defaultColor;	//default/base RGB for string
	private int defaultAlpha;	//default/base alpha for string
	private int currentColor;	//current RGB for string
	private int currentAlpha;	//current alpha for string
	//end spout AlphaText

	FontRenderer() {
		this.renderEngine = null;
	}

	public FontRenderer(GameSettings par1GameSettings, String par2Str, RenderEngine par3RenderEngine, boolean par4) {
		this.renderEngine = par3RenderEngine;
		this.unicodeFlag = par4;

		BufferedImage var5;
		try {
			var5 = TextureUtils.getResourceAsBufferedImage(par2Str);
			InputStream var6 = RenderEngine.class.getResourceAsStream("/font/glyph_sizes.bin");
			var6.read(this.glyphWidth);
		} catch (IOException var17) {
			throw new RuntimeException(var17);
		}

		int var18 = var5.getWidth();
		int var7 = var5.getHeight();
		int[] var8 = new int[var18 * var7];
		var5.getRGB(0, 0, var18, var7, var8, 0, var18);
		this.charWidthf = FontUtils.computeCharWidths(par2Str, var5, var8, this.charWidth);
		this.fontTextureName = par3RenderEngine.allocateAndSetupTexture(var5);

		for (int var9 = 0; var9 < 32; ++var9) {
			int var10 = (var9 >> 3 & 1) * 85;
			int var11 = (var9 >> 2 & 1) * 170 + var10;
			int var12 = (var9 >> 1 & 1) * 170 + var10;
			int var13 = (var9 >> 0 & 1) * 170 + var10;
			if (var9 == 6) {
				var11 += 85;
			}

			if (par1GameSettings.anaglyph) {
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

			this.colorCode[var9] = (var11 & 255) << 16 | (var12 & 255) << 8 | var13 & 255;
		}
	}

	private float renderCharAtPos(int par1, char par2, boolean par3) {
		return par2 == 32?this.charWidthf[32]:(par1 > 0 && !this.unicodeFlag?this.renderDefaultChar(par1 + 32, par3):this.renderUnicodeChar(par2, par3));
	}

	//Begin Spout AlphaText
	private float renderDefaultChar(int k, boolean italic)
	{
		float Xk = ((k % 16) * 8 ) / 128F;
		float Yk = ((k / 16) * 8 ) / 128F;
		float Xi = italic ? 1.0F : 0.0F;
		if (this.boundTextureName != this.fontTextureName) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureName);
			this.boundTextureName = this.fontTextureName;
		}
		float Xw = this.charWidthf[k] - 0.01F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawing(GL11.GL_TRIANGLE_STRIP);
		tessellator.setColorRGBA(currentColor >> 16 & 0xff, currentColor >> 8 & 0xff, currentColor & 0xff, currentAlpha);
		tessellator.addVertexWithUV(this.posX      + Xi, this.posY,         0.0F, Xk,               Yk               );
		tessellator.addVertexWithUV(this.posX      - Xi, this.posY + 7.99F, 0.0F, Xk,               Yk + 0.062421873F); //(7.99F / 128F)
		tessellator.addVertexWithUV(this.posX + Xw + Xi, this.posY,         0.0F, Xk + (Xw / 128F), Yk               );
		tessellator.addVertexWithUV(this.posX + Xw - Xi, this.posY + 7.99F, 0.0F, Xk + (Xw / 128F), Yk + 0.062421873F); //(7.99F / 128F)
		tessellator.draw();
		return this.charWidthf[k];
	}
	//end Spout AlphaText


	private void loadGlyphTexture(int par1) {
		String var3 = String.format("/font/glyph_%02X.png", new Object[]{Integer.valueOf(par1)});

		BufferedImage var2;
		try {
			var2 = TextureUtils.getResourceAsBufferedImage(var3);
		} catch (IOException var5) {
			throw new RuntimeException(var5);
		}

		this.glyphTextureName[par1] = this.renderEngine.allocateAndSetupTexture(var2);
		this.boundTextureName = this.glyphTextureName[par1];
	}

	//begin Spout AlphaText
	private float renderUnicodeChar(char c, boolean italic)
	{
		if (this.glyphWidth[c] == 0) {
			return 0F;
		}
		int page = c / 256;
		if (this.glyphTextureName[page] == 0) {
			this.loadGlyphTexture(page);
		}
		if (this.boundTextureName != this.glyphTextureName[page]) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.glyphTextureName[page]);
			this.boundTextureName = this.glyphTextureName[page];
		}
		int j = this.glyphWidth[c] >>> 4;
		int k = this.glyphWidth[c] & 0xf;
		float X0 = (float)j;
		float X1 = (float)(k + 1);
		float Xc = ((float)((c % 16) * 16) + X0) / 256F;
		float Yc = (float)(((c & 0xff) / 16) * 16) / 256F;
		float Xw = (X1 - X0 /*- 0.02F*/) / 2F;
		float Xi = italic ? 1F : 0F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawing(GL11.GL_TRIANGLE_STRIP);
		tessellator.setColorRGBA(currentColor >> 16 & 0xff, currentColor >> 8 & 0xff, (currentColor & 0xff), currentAlpha);
		tessellator.addVertexWithUV(this.posX      + Xi, this.posY,      0F, Xc,               Yc               );
		tessellator.addVertexWithUV(this.posX      - Xi, this.posY + 8F, 0F, Xc,               Yc + 0.0625F);//(15.98F / 256F)
		tessellator.addVertexWithUV(this.posX + Xw + Xi, this.posY,      0F, Xc + (Xw / 128F), Yc               );//0.062421873F
		tessellator.addVertexWithUV(this.posX + Xw - Xi, this.posY + 8F, 0F, Xc + (Xw / 128F), Yc + 0.0625F);//  6.24218732e-002
		tessellator.draw();
		return (X1 - X0) / 2F + 1F;
	}
	//end Spout AlphaText

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
						currentAlpha = defaultAlpha * 10 / 7; // Spout AlphaText - transparent shadows improve readability for black text.
					}
					currentColor = Colorizer.colorizeText(this.colorCode[var10], var10); // Spout AlphaText - sets current Color from Colorizer. Alpha from Colorizer is discarded.
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
					currentColor=defaultColor; // Spout AlphaText - returns to default RGBA values
					currentAlpha=defaultAlpha;
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

				float var14 = this.renderCharAtPos(var10, var9, var5);
				if (var4) {
					++this.posX;
					this.renderCharAtPos(var10, var9, var5);
					--this.posX;
					++var14;
				}

				Tessellator var12;
				if (var7) {
					var12 = Tessellator.instance;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					var12.startDrawingQuads();
					var12.setColorRGBA(currentColor >> 16 & 0xff, currentColor >> 8 & 0xff, currentColor & 0xff, currentAlpha);  //Spout AlphaText - uses tessellator to set Color.
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
					var12.setColorRGBA(currentColor >> 16 & 0xff, currentColor >> 8 & 0xff, currentColor & 0xff, currentAlpha); //Spout AlphaText - uses tessellator to set Color.
					// int var13 = var6?-1:0;	// Spout AlphaText - I do not see this as necessary, var6 (underline) should always be true at this point.
					var12.addVertex((double)(this.posX - 1.0F /* (float)var13*/), (double)(this.posY + (float)this.FONT_HEIGHT), 0.0D);
					var12.addVertex((double)(this.posX + var14), (double)(this.posY + (float)this.FONT_HEIGHT), 0.0D);
					var12.addVertex((double)(this.posX + var14), (double)(this.posY + (float)this.FONT_HEIGHT - 1.0F), 0.0D);
					var12.addVertex((double)(this.posX - 1.0F /* (float)var13*/), (double)(this.posY + (float)this.FONT_HEIGHT - 1.0F), 0.0D);
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
			par4 = Colorizer.colorizeText(par4);
			if ((par4 & -67108864) == 0) {
				par4 |= -16777216;
			}

			if (par5) {
				par4 = (par4 & 16579836) >> 2 | par4 & -16777216;
			}
			// begin Spout AlphaText
			defaultColor = par4;	// Stores default RGBA values for this string
			defaultAlpha = (par4 >> 24 & 0xff);
			currentColor = par4;	// Stores current RGBA for this string.
			currentAlpha = defaultAlpha;
			// end Spout AlphaText
			this.posX = (float)par2;
			this.posY = (float)par3;
			this.renderStringAtPos(par1Str, par5);
			return (int)this.posX;
		} else {
			return 0;
		}
	}

	public int getStringWidth(String par1Str) {
		return (int)FontUtils.getStringWidthf(this, org.bukkit.ChatColor.stripColor(par1Str));
	}
	
	public int getCharWidth(char ch) {
		return func_50105_a(ch);
	}

	public int func_50105_a(char par1) {
		if (par1 == 167) {
			return -1;
		} else {
			int var2 = ChatAllowedCharacters.allowedCharacters.indexOf(par1);
			if (var2 >= 0 && !this.unicodeFlag) {
				return this.charWidth[var2 + 32];
			} else if (this.glyphWidth[par1] != 0) {
				int var3 = this.glyphWidth[par1] >> 4;
				int var4 = this.glyphWidth[par1] & 15;
				if (var4 > 7) {
					var4 = 15;
					var3 = 0;
				}

				++var4;
				return (2 + var4 - var3) / 2; // Spout Alpha Text - precision error correction.
			} else {
				return 0;
			}
		}
	}

	public String func_50107_a(String par1Str, int par2) {
		return this.func_50104_a(par1Str, par2, false);
	}

	public String func_50104_a(String par1Str, int par2, boolean par3) {
		StringBuilder var4 = new StringBuilder();
		int var5 = 0;
		int var6 = par3?par1Str.length() - 1:0;
		int var7 = par3?-1:1;
		boolean var8 = false;
		boolean var9 = false;

		for (int var10 = var6; var10 >= 0 && var10 < par1Str.length() && var5 < par2; var10 += var7) {
			char var11 = par1Str.charAt(var10);
			int var12 = this.func_50105_a(var11);
			if (var8) {
				var8 = false;
				if (var11 != 108 && var11 != 76) {
					if (var11 == 114 || var11 == 82 || isFormatColor(var11)) {
						var9 = false;
					}
				} else {
					var9 = true;
				}
			} else if (var12 < 0) {
				var8 = true;
			} else {
				var5 += var12;
				if (var9) {
					++var5;
				}
			}

			if (var5 > par2) {
				break;
			}

			if (par3) {
				var4.insert(0, var11);
			} else {
				var4.append(var11);
			}
		}

		return var4.toString();
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
		int var3 = this.sizeStringToWidth(par1Str, par2);
		if (par1Str.length() <= var3) {
			return par1Str;
		} else {
			String var4 = par1Str.substring(0, var3);
			String var5 = getFormatFromString(var4) + par1Str.substring(var3 + (par1Str.charAt(var3) == 32?1:0));
			return var4 + "\n" + this.func_50113_d(var5, par2);
		}
	}

	private int sizeStringToWidth(String par1Str, int par2) {
		int var3 = par1Str.length();
		int var4 = 0;
		int var5 = 0;
		int var6 = -1;

		for (boolean var7 = false; var5 < var3; ++var5) {
			char var8 = par1Str.charAt(var5);
			switch(var8) {
			case 32:
				var6 = var5;
			case 167:
				if (var5 != var3) {
					++var5;
					char var9 = par1Str.charAt(var5);
					if (var9 != 108 && var9 != 76) {
						if (var9 == 114 || var9 == 82 || isFormatColor(var9)) {
							var7 = false;
						}
					} else {
						var7 = true;
					}
				}
				break;
			default:
				var4 += this.func_50105_a(var8);
				if (var7) {
					++var4;
				}
			}

			if (var8 == 10) {
				++var5;
				var6 = var5;
				break;
			}

			if (var4 > par2) {
				break;
			}
		}

		return var5 != var3 && var6 != -1 && var6 < var5?var6:var5;
	}

	private static boolean isFormatColor(char par0) {
		return par0 >= 48 && par0 <= 57 || par0 >= 97 && par0 <= 102 || par0 >= 65 && par0 <= 70;
	}

	private static boolean isFormatStyle(char par0) {
		return par0 >= 107 && par0 <= 111 || par0 >= 75 && par0 <= 79 || par0 == 114 || par0 == 82;
	}

	private static String getFormatFromString(String par0Str) {
		String var1 = "";
		int var2 = -1;
		int var3 = par0Str.length();

		while ((var2 = par0Str.indexOf(167, var2 + 1)) != -1) {
			if (var2 < var3 - 1) {
				char var4 = par0Str.charAt(var2 + 1);
				if (isFormatColor(var4)) {
					var1 = "\u00a7" + var4;
				} else if (isFormatStyle(var4)) {
					var1 = var1 + "\u00a7" + var4;
				}
			}
		}

		return var1;
	}

	public static String func_52014_d(String par0Str) {
		return field_52015_r.matcher(par0Str).replaceAll("");
	}

	public void initialize(GameSettings var1, String var2, RenderEngine var3) {
		boolean var4 = false;
		this.charWidth = new int[256];
		this.fontTextureName = 0;
		this.FONT_HEIGHT = 8;
		this.fontRandom = new Random();
		this.glyphWidth = new byte[65536];
		this.glyphTextureName = new int[256];
		this.colorCode = new int[32];
		this.renderEngine = var3;
		this.unicodeFlag = var4;

		BufferedImage var5;
		try {
			var5 = TextureUtils.getResourceAsBufferedImage(var2);
			InputStream var6 = RenderEngine.class.getResourceAsStream("/font/glyph_sizes.bin");
			var6.read(this.glyphWidth);
		} catch (IOException var17) {
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

			this.colorCode[var9] = (var11 & 255) << 16 | (var12 & 255) << 8 | var13 & 255;
		}
	}
}
