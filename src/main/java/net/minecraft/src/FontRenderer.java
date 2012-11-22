package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;
import com.pclewis.mcpatcher.mod.FontUtils;
import com.pclewis.mcpatcher.mod.TextureUtils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.Bidi;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import java.util.regex.Pattern;
import org.lwjgl.opengl.GL11;

// Spout rewritten - not even going to try to figure out where the changes are...
public class FontRenderer {
	private int[] charWidth = new int[256];
	public int fontTextureName = 0;

	/** the height in pixels of default text */
	public int FONT_HEIGHT = 8; // Spout smaller text
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
	private float boldOffset;	//Meow, the stroke width/offset used for rendering bold text.
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
		this.boldOffset = par4?0.5F:1F;

		BufferedImage var5;
		try {
			var5 = TextureUtils.getResourceAsBufferedImage((Object)RenderEngine.class, par2Str); // Spout HD
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

	// begin Spout AlphaText
	private float renderCharAtPos(int ki, char ch, boolean italic, boolean bold) {
		if (ch==' ') {
			return this.charWidthf[32];
		} else if (ki > 0 && !this.unicodeFlag) {
			if(bold) {
				this.posX+=boldOffset;
				this.renderDefaultChar(ki + 32, italic, false);
				this.posX-=boldOffset;
			}
			return (this.renderDefaultChar(ki + 32, italic, bold) + (bold?boldOffset:0F));
		}
		if (bold) {
			this.posX+=boldOffset;
			this.renderUnicodeChar(ch, italic);
			this.posX-=boldOffset;
		}
		return (this.renderUnicodeChar(ch, italic) + (bold?boldOffset:0F));
	}
	// end Spout AlphaText

	// begin Spout AlphaText
	private float renderDefaultChar(int k, boolean italic, boolean bold) {
		float Xk = (float)(k % 16) / 16F;
		float Yk = (float)(k / 16) / 16F;
		float Xi = italic ? 1.0F : 0.0F;
		float Xb = bold ? 0.5F : 0.0F;
		if (this.boundTextureName != this.fontTextureName) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureName);
			this.boundTextureName = this.fontTextureName;
		}
		float Xw = this.charWidthf[k] + 0.01F; //offset inverse of previous value, unknown cause.
		Tessellator tessellator = Tessellator.instance; //tessellator allocates buffers for GL11, etc.
		tessellator.startDrawing(GL11.GL_TRIANGLE_STRIP);
		tessellator.setColorRGBA(currentColor >> 16 & 0xff, currentColor >> 8 & 0xff, currentColor & 0xff, currentAlpha);
		tessellator.addVertexWithUV(this.posX           + Xi,   this.posY,         0.0D, Xk,               Yk               );
		tessellator.addVertexWithUV(this.posX           - Xi,   this.posY + 8.00F, 0.0D, Xk,               Yk + 0.062421873F); //(7.99F / 128F)
		tessellator.addVertexWithUV(this.posX + Xw + Xb + Xi,   this.posY,         0.0D, Xk + (Xw / 128F), Yk               );
		tessellator.addVertexWithUV((this.posX + Xw + Xb) - Xi, this.posY + 8.00F, 0.0D, Xk + (Xw / 128F), Yk + 0.062421873F);
		tessellator.draw();
		return this.charWidthf[k];
	}

	private void loadGlyphTexture(int par1) {
		String var3 = String.format("/font/glyph_%02X.png", new Object[]{Integer.valueOf(par1)});

		BufferedImage var2;
		try {
			var2 = TextureUtils.getResourceAsBufferedImage((Object)RenderEngine.class, var3);
		} catch (IOException var5) {
			throw new RuntimeException(var5);
		}

		this.glyphTextureName[par1] = this.renderEngine.allocateAndSetupTexture(var2);
		this.boundTextureName = this.glyphTextureName[par1];
	}

	//begin Spout AlphaText
	private float renderUnicodeChar(char c, boolean italic) {
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
		float X1 = (float)((k + 1) - j) / 2F;
		float Xc = ((float)((c % 16) * 16) + j) / 256F;
		float Yc = (float)((c & 0xff) / 16) / 16F;
		float Xw = X1 + 0.01F; //inverse of previous value, unknown cause for this to flip.
		float Xi = italic ? 1F : 0F;
		Tessellator tessellator = Tessellator.instance; //tessellator allocates buffers for GL11, etc.
		tessellator.startDrawing(GL11.GL_TRIANGLE_STRIP);
		tessellator.setColorRGBA(currentColor >> 16 & 0xff, currentColor >> 8 & 0xff, currentColor & 0xff, currentAlpha);
		tessellator.addVertexWithUV(this.posX      + Xi, this.posY,         0D, Xc,               Yc);
		tessellator.addVertexWithUV(this.posX      - Xi, this.posY + 7.99F, 0D, Xc,               Yc + 0.062421873F);
		tessellator.addVertexWithUV(this.posX + Xw + Xi, this.posY,         0D, Xc + (Xw / 128F), Yc);
		tessellator.addVertexWithUV(this.posX + Xw - Xi, this.posY + 7.99F, 0D, Xc + (Xw / 128F), Yc + 0.062421873F);
		tessellator.draw();
		return X1 + 1F;
	}
	// end Spout AlphaText


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
			// Spout Stupid unicode box character fix
			if (var9 < 30) {
				continue;
			}
			// Spout End
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
					currentColor = Colorizer.colorizeText(this.colorCode[var10], var10); // Spout AlphaText - sets current Colour (but will discard alpha) from Colorizer
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

				float var14 = this.renderCharAtPos(var10, var9, var5, var4); // Spout AlphaText

				Tessellator var12;
				if (var7) {
					var12 = Tessellator.instance;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					var12.startDrawingQuads();
					var12.setColorRGBA(currentColor >> 16 & 0xff, currentColor >> 8 & 0xff, currentColor & 0xff, currentAlpha);  // Spout AlphaText - uses tessellator to set colour now.
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
					var12.setColorRGBA(currentColor >> 16 & 0xff, currentColor >> 8 & 0xff, currentColor & 0xff, currentAlpha); // Spout AlphaText - uses tessellator to set colour now.
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
			//begin Spout AlphaText
			defaultColor = par4;	// Spout AlphaText - Stores default RGBA values for this string
			defaultAlpha = (par4 >> 24 & 0xff);
			currentColor = par4;	// Spout AlphaText - Stores current RGBA for this string.
			currentAlpha = defaultAlpha;
			//end Spout AlphaText
			this.posX = (float)par2;
			this.posY = (float)par3;
			this.renderStringAtPos(par1Str, par5);
			GL11.glColor4f((float)(currentColor>>16 & 0xff)/255.0F, (float)(currentColor>>8 & 0xff)/255.0F, (float)(currentColor & 0xff)/255.0F, (float)currentAlpha/255.0F);		// Spout AlphaText - some mods rely on fontRenderer to make this specific call to set the drawing colour to that last used in a string.
			return (int)this.posX;
		} else {
			return 0;
		}
	}

	// begin Spout AlphaText
	final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)\u00A7[0-F]");
	public int getStringWidth(String par1Str) {
		if (par1Str == null) {
			return 0;
		}
		par1Str = par1Str.replaceAll("(?i)\u00A7[0-F]", ""); //strip ordinary colors
		float widthStr = 0F;
		boolean bold = false;
		for (int i = 0; i < par1Str.length(); i++) {
			char ch = par1Str.charAt(i);
			float widthCh = getCharWidthFloat(ch);
			if (widthCh < 0F && i < par1Str.length() - 1) {
				char chSel = par1Str.charAt(++i);
				if (chSel == 'l' || chSel == 'L') {
					bold = true;
				}
				else if (chSel == 'r' || chSel == 'R' || isFormatColor(chSel)) {
					bold = false;
				}
				widthCh = getCharWidthFloat(chSel);
			}
			widthStr += widthCh;
			if (bold && ch!=' ') {
				widthStr+=boldOffset;
			}
		}
		return Math.round(widthStr);
	}
	// end Spout AlphaText

	// begin Spout AlphaText.
	public int getCharWidth(char ch) {
		if (ch == 167) {
			return -1;
		}
		return (int)getCharWidthFloat(ch);
	}
	// end Spout Text Alpha

	// begin Spout AlphaText.
	public int func_50105_a(char ch) {
		if (ch == 167) {
			return -1;
		}
		return (int)getCharWidthFloat(ch);
	}
	// end Spout Text Alpha

	// begin Spout AlphaText - This is the true calculation for char widths.
	public float getCharWidthFloat(char ch){
		if (ch < 30) {
			return 0F;
		}
		if (ch == 167) {
			return -1F;
		} else if (ch == 32) {  // Spout AlphaText - returns the width of spaces AS RENDERED.
			return charWidthf[32];
		} else {
			int chCh = ChatAllowedCharacters.allowedCharacters.indexOf(ch);
			if (chCh >= 0 && !this.unicodeFlag) {
				return this.charWidthf[chCh + 32];
			} else if (this.glyphWidth[ch] != 0) {
				int j = this.glyphWidth[ch] >>> 4;
				int k = this.glyphWidth[ch] & 0xf;
				float X1 = (float)((k + 1) - j) / 2F;
				return X1 + 1F; // Spout AlphaText - Prevents precision error from appearing
			} else {
				return 0F;
			}
		}
	}
	// end Spout Text Alpha

	public String trimStringToWidth(String par1Str, int par2) {
		return this.trimStringToWidth(par1Str, par2, false);
	}

	// begin Spout AlphaText - TrimStringToWidth, returns a trimmed string to the specified length. Also handles RTL conversion.
	public String trimStringToWidth(String par1Str, int width2, boolean RTL) {
		float widthWrp = (float)width2;
		float widthStr = 0;
		StringBuilder str0 = new StringBuilder();
		int frm = RTL?par1Str.length() - 1:0;
		int stp = RTL?-1:1;
		boolean sel = false;
		boolean bold = false;

		for (int ii = frm; ii >= 0 && ii < par1Str.length() && widthStr < widthWrp; ii += stp) {
			char ch = par1Str.charAt(ii);
			float widthCh = this.getCharWidthFloat(ch);
			if (sel) {
				sel = false;
				if (ch != 108 && ch != 76) {
					if (ch == 114 || ch == 82 || isFormatColor(ch)) {
						bold = false;
					}
				} else {
					bold = true;
				}
			} else if (widthCh < 0) {
				sel = true;
			} else {
				widthStr += widthCh;
				if (bold && ch!=' ') {
					widthStr+=boldOffset;
				}
			}

			if (widthStr > widthWrp) {
				break;
			}

			if (RTL) {
				str0.insert(0, ch);
			} else {
				str0.append(ch);
			}
		}
		return str0.toString();
	}
	//end Spout AlphaText

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
		this.boldOffset = par1?0.5F:1F;
	}

	/**
	 * Get unicodeFlag controlling whether strings should be rendered with Unicode fonts instead of the default.png font.
	 */
	public boolean getUnicodeFlag() {
		return this.unicodeFlag;
	}

	public void setBidiFlag(boolean par1) {
		this.bidiFlag = par1;
	}

	public List func_50108_c(String par1Str, int par2) {
		return Arrays.asList(this.func_50113_d(par1Str, par2).split("\n"));
	}


	String func_50113_d(String par1Str, int par2) {
		return this.wrapStringToWidth(par1Str,par2); // Spout AlphaText
	}

	// Spout AlphaText - describes how many characters of a given input string will fit within the specified screen width.
	private int sizeStringToWidth(String par1Str, int width2) {
		float widthSz = (float)width2;
		float widthCh = 0F;
		int lenStr = par1Str.length();
		int lenSp = -1;
		boolean bold = false;
		int ii = 0;
		for (; ii < lenStr; ++ii) {
			char ch = par1Str.charAt(ii);
			switch(ch) {
			case 32:
				lenSp = ii;
			case 167:
				if (ii != lenStr) {
					++ii;
					char chSel = par1Str.charAt(ii);
					if (chSel != 108 && chSel != 76) {
						if (chSel == 114 || chSel == 82 || isFormatColor(chSel)) { 
							bold = false;
						}
					} else {
						bold = true;
					}
				}
				break;
			default:
				widthCh += this.getCharWidthFloat(ch);
				if (bold && ch!=' ') {
					widthCh+=boldOffset;
				}
			}

			if (ch == 10) {
				++ii;
				lenSp = ii;
				break;
			}

			if (widthCh > widthSz) {
				break;
			}
		}

		return ii != lenStr && lenSp != -1 && lenSp < ii?lenSp:ii;
	}
	//end Spout AlphaText

	private static boolean isFormatColor(char par0) {
		return par0 >= '0' && /*par0 <= '9' || par0 >= 'A' &&*/ par0 <= 'F' || par0 >= 'a' && par0 <= 'f'; // Spout AlphaText - Close enough
	}

	private static boolean isFormatStyle(char par0) {
		return par0 >= 'k' && par0 <= 'o' || par0 >= 'K' && par0 <= 'O' || par0 == 'r' || par0 == 'R';
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

	//begin Spout AlphaText /** Meow, uses \n to split lines. */
	public String wrapStringToWidth(String par1str, int width2) {
		float maxWidth = width2;
		float subWidth = 0;
		int spaceIndex = -1;
		StringBuilder selectRandom = new StringBuilder("");
		char selectColor = 0;
		char c;
		boolean bold = false;
		StringBuilder ArrStr = new StringBuilder("");
		for (int i = 0; i < par1str.length(); i++) {
			c = par1str.charAt(i);
			if (c == '\247' && i + 1 < par1str.length()) {
				i++;
				c = Character.toUpperCase(par1str.charAt(i));
				if ((c >= '0' && /*c <= '9') || (c >= 'A' &&*/ c <= 'F')){	//close enough.
					selectColor = c;
					selectRandom.setLength(0);
					bold = false;
				} else if (c == 'r'){
					selectRandom.setLength(0);
					selectColor = 0;
					bold = false;
				} else {
					selectRandom.append("\u00a7" + c);
					if (c == 'l') {
						bold = true;
					}
				}
				continue;
			}
			if (c == ' ') {
				spaceIndex = i;
			}
			if (c =='\n') {
				ArrStr.append(par1str.substring(0,i)+'\n');
				par1str=par1str.substring(i+1);
				if (par1str.length() > 0){
					if (selectRandom.length()!=0){
						par1str=selectRandom.toString() + par1str;
					}
					if (selectColor!=0){
						par1str="\247" + selectColor + par1str;
					}
				}
				i=0;
				subWidth = 0;
				spaceIndex = -1;
				continue;
			}
			subWidth += getCharWidthFloat(c);
			if (subWidth >= maxWidth) {
				if (spaceIndex > 4 && c!=' ') {
					i=spaceIndex;
				} else if (bold) {
					subWidth += boldOffset;
				}
				ArrStr.append(par1str.substring(0,i)+'\n');
				par1str=par1str.substring(i+1);
				if (par1str.length() > 0){
					if (selectRandom.length()!=0){
						par1str=selectRandom.toString() + par1str;
					}
					if (selectColor!=0){
						par1str="\247" + selectColor + par1str;
					}
				}
				i=0;
				subWidth = 0;
				spaceIndex = -1;
			}
		}
		if (par1str.length()>0) {
			ArrStr.append(par1str);
		}
		return ArrStr.toString();
	}
	//end Spout AlphaText
	// Spout HD
	public void initialize(GameSettings var1, String var2, RenderEngine var3) {
		boolean var4 = false;
		this.charWidth = new int[256];
		this.fontTextureName = 0;
		this.FONT_HEIGHT = 9;
		this.fontRandom = new Random();
		this.glyphWidth = new byte[65536];
		this.glyphTextureName = new int[256];
		this.colorCode = new int[32];
	/*	this.field_78303_s = false; Unknown merges due to AlphaText's mass renaming
		this.field_78302_t = false;
		this.field_78301_u = false;
		this.field_78300_v = false;
		this.field_78299_w = false;*/
		this.renderEngine = var3;
		this.unicodeFlag = var4;
		BufferedImage var5;

		try {
			var5 = TextureUtils.getResourceAsBufferedImage((Object)RenderEngine.class, var2);
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
	
	public boolean getBidiFlag() {
 		return this.bidiFlag;
 	}
	// Spout HD
	
	String wrapFormattedStringToWidth(String par1Str, int par2) {
		int var3 = this.sizeStringToWidth(par1Str, par2);
		
		if (par1Str.length() <= var3) {
			return par1Str;
		} else {
			String var4 = par1Str.substring(0, var3);
			String var5 = getFormatFromString(var4) + par1Str.substring(var3 + (par1Str.charAt(var3) == 32 ? 1 : 0));
			return var4 + "\n" + this.wrapFormattedStringToWidth(var5, par2);
		}
	}
	
	public List listFormattedStringToWidth(String par1Str, int par2) {
		return Arrays.asList(this.wrapFormattedStringToWidth(par1Str, par2).split("\n"));
	}
}

