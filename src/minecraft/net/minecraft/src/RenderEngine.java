package net.minecraft.src;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
//Spout HD Start
import net.minecraft.client.Minecraft;

import com.pclewis.mcpatcher.mod.CustomAnimation;
import com.pclewis.mcpatcher.mod.TextureUtils;
import com.pclewis.mcpatcher.mod.TileSize;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Dimension;
import org.lwjgl.opengl.GLContext;

//Spout HD End

public class RenderEngine {

	public static boolean useMipmaps = false;
	private HashMap textureMap = new HashMap();
	private HashMap textureContentsMap = new HashMap();
	private IntHashMap textureNameToImageMap = new IntHashMap();
	private IntBuffer singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
	private ByteBuffer imageData = GLAllocation.createDirectByteBuffer(16777216);
	public List textureList = new ArrayList(); // Spout private -> public
	private Map urlToImageDataMap = new HashMap();
	private GameSettings options;
	public boolean clampTexture = false;
	public boolean blurTexture = false;
	public TexturePackList texturePack; // Spout private -> public
	private BufferedImage missingTextureImage = new BufferedImage(64, 64, 2);
	// Spout Start
	public TexturePackBase oldPack = null;

	// Spout End

	public RenderEngine(TexturePackList par1TexturePackList, GameSettings par2GameSettings) {
		this.texturePack = par1TexturePackList;
		this.options = par2GameSettings;
		Graphics var3 = this.missingTextureImage.getGraphics();
		var3.setColor(Color.WHITE);
		var3.fillRect(0, 0, 64, 64);
		var3.setColor(Color.BLACK);
		var3.drawString("missingtex", 1, 10);
		var3.dispose();
	}

	public int[] getTextureContents(String par1Str) {
		TexturePackBase var2 = this.texturePack.selectedTexturePack;
		int[] var3 = (int[])this.textureContentsMap.get(par1Str);
		if (var3 != null) {
			return var3;
		} else {
			try {
				Object var6 = null;
				if (par1Str.startsWith("##")) {
					// Spout HD Start
					var3 = this.getImageContentsAndAllocate(this.unwrapImageByColumns(TextureUtils.getResourceAsBufferedImage(this, var2, par1Str.substring(2))));
					// Spout HD End
				} else if (par1Str.startsWith("%clamp%")) {
					this.clampTexture = true;
					// Spout HD Start
					var3 = this.getImageContentsAndAllocate(TextureUtils.getResourceAsBufferedImage(this, var2, par1Str.substring(7)));
					// Spout HD End
					this.clampTexture = false;
				} else if (par1Str.startsWith("%blur%")) {
					// Spout HD Start
					this.blurTexture = true;
					this.clampTexture = true;
					var3 = this.getImageContentsAndAllocate(TextureUtils.getResourceAsBufferedImage(this, var2, par1Str.substring(6)));
					this.clampTexture = false;
					// Spout HD end
					this.blurTexture = false;
				} else {
					InputStream var7 = var2.getResourceAsStream(par1Str);
					if (var7 == null) {
						var3 = this.getImageContentsAndAllocate(this.missingTextureImage);
					} else {
						var3 = this.getImageContentsAndAllocate(this.readTextureImage(var7));
					}
				}

				this.textureContentsMap.put(par1Str, var3);
				return var3;
			} catch (IOException var5) {
				var5.printStackTrace();
				int[] var4 = this.getImageContentsAndAllocate(this.missingTextureImage);
				this.textureContentsMap.put(par1Str, var4);
				return var4;
			}
		}
	}

	private int[] getImageContentsAndAllocate(BufferedImage par1BufferedImage) {
		int var2 = par1BufferedImage.getWidth();
		int var3 = par1BufferedImage.getHeight();
		int[] var4 = new int[var2 * var3];
		par1BufferedImage.getRGB(0, 0, var2, var3, var4, 0, var2);
		return var4;
	}

	private int[] getImageContents(BufferedImage par1BufferedImage, int[] par2ArrayOfInteger) {
		// Spout HD start
		if (par1BufferedImage == null) {
			return par2ArrayOfInteger;
		} else {
			int var3 = par1BufferedImage.getWidth();
			int var4 = par1BufferedImage.getHeight();
			par1BufferedImage.getRGB(0, 0, var3, var4, par2ArrayOfInteger, 0, var3);
			return par2ArrayOfInteger;
		}
		// Spout HD end
	}

	public int getTexture(String par1Str) {
		TexturePackBase var2 = this.texturePack.selectedTexturePack;
		Integer var3 = (Integer)this.textureMap.get(par1Str);
		if (var3 != null) {
			return var3.intValue();
		} else {
			try {
				this.singleIntBuffer.clear();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				int var6 = this.singleIntBuffer.get(0);
				if (par1Str.startsWith("##")) {
					// Spout HD Start
					this.setupTexture(this.unwrapImageByColumns(TextureUtils.getResourceAsBufferedImage(this, var2, par1Str.substring(2))), var6);
					// Spout HD End
				} else if (par1Str.startsWith("%clamp%")) {
					this.clampTexture = true;
					// Spout HD Start
					this.setupTexture(TextureUtils.getResourceAsBufferedImage(this, var2, par1Str.substring(7)), var6);
					// Spout HD End
					this.clampTexture = false;
				} else if (par1Str.startsWith("%blur%")) {
					// Spout HD Start
					this.blurTexture = true;
					this.setupTexture(TextureUtils.getResourceAsBufferedImage(this, var2, par1Str.substring(6)), var6);
					this.blurTexture = false;
					// Spout HD end
				} else if (par1Str.startsWith("%blurclamp%")) {
					// Spout HD Start
					this.blurTexture = true;
					this.clampTexture = true;
					this.setupTexture(TextureUtils.getResourceAsBufferedImage(this, var2, par1Str.substring(11)), var6);
					this.blurTexture = false;
					this.clampTexture = false;
					// Spout HD end
				} else {
					InputStream var7 = var2.getResourceAsStream(par1Str);
					if (var7 == null) {
						this.setupTexture(this.missingTextureImage, var6);
					} else {
						this.setupTexture(this.readTextureImage(var7), var6);
					}
				}

				this.textureMap.put(par1Str, Integer.valueOf(var6));
				return var6;
			} catch (Exception var5) {
				var5.printStackTrace();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				int var4 = this.singleIntBuffer.get(0);
				this.setupTexture(this.missingTextureImage, var4);
				this.textureMap.put(par1Str, Integer.valueOf(var4));
				return var4;
			}
		}
	}

	private BufferedImage unwrapImageByColumns(BufferedImage par1BufferedImage) {
		int var2 = par1BufferedImage.getWidth() / 16;
		BufferedImage var3 = new BufferedImage(16, par1BufferedImage.getHeight() * var2, 2);
		Graphics var4 = var3.getGraphics();

		for (int var5 = 0; var5 < var2; ++var5) {
			var4.drawImage(par1BufferedImage, -var5 * 16, var5 * par1BufferedImage.getHeight(), (ImageObserver)null);
		}

		var4.dispose();
		return var3;
	}

	public int allocateAndSetupTexture(BufferedImage par1BufferedImage) {
		this.singleIntBuffer.clear();
		GLAllocation.generateTextureNames(this.singleIntBuffer);
		int var2 = this.singleIntBuffer.get(0);
		this.setupTexture(par1BufferedImage, var2);
		this.textureNameToImageMap.addKey(var2, par1BufferedImage);
		return var2;
	}

	public void setupTexture(BufferedImage texture, int var2) {
		// Spout HD Start
		if (texture != null) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
			if (useMipmaps) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			} else {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			}

			if (this.blurTexture) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			}

			if (this.clampTexture) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
			} else {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			}
			int textureWidth = texture.getWidth();
			int textureHeight = texture.getHeight();
			int[] texData = new int[textureWidth * textureHeight];
			byte[] texColors = new byte[textureWidth * textureHeight * 4];
			texture.getRGB(0, 0, textureWidth, textureHeight, texData, 0, textureWidth);

			int r;
			int g;
			int b;
			int a;
			int j;
			int var13;
			int var14;
			for (int i = 0; i < texData.length; ++i) {
				r = texData[i] >> 24 & 255;
				g = texData[i] >> 16 & 255;
				b = texData[i] >> 8 & 255;
				a = texData[i] & 255;
				if (this.options != null && this.options.anaglyph) {
					j = (g * 30 + b * 59 + a * 11) / 100;
					var13 = (g * 30 + b * 70) / 100;
					var14 = (g * 30 + a * 70) / 100;
					g = j;
					b = var13;
					a = var14;
				}

				texColors[i * 4 + 0] = (byte) g;
				texColors[i * 4 + 1] = (byte) b;
				texColors[i * 4 + 2] = (byte) a;
				texColors[i * 4 + 3] = (byte) r;
			}

			this.imageData = TextureUtils.getByteBuffer(this.imageData, texColors);

			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureWidth, textureHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
			if (useMipmaps) {
				for (int i = 1; i <= 4; ++i) {
					// slide the colors into proper bit positions?
					// IE: r g b a
					// 00 - 00 - 00 - 00
					// so you can do something like
					// int col = r + g + b + a;

					r = textureWidth >> i - 1;
					g = textureWidth >> i;
					b = textureHeight >> i;

					for (a = 0; a < g; ++a) {
						for (j = 0; j < b; ++j) {
							var13 = this.imageData.getInt((a * 2 + 0 + (j * 2 + 0) * r) * 4);
							var14 = this.imageData.getInt((a * 2 + 1 + (j * 2 + 0) * r) * 4);
							int var15 = this.imageData.getInt((a * 2 + 1 + (j * 2 + 1) * r) * 4);
							int var16 = this.imageData.getInt((a * 2 + 0 + (j * 2 + 1) * r) * 4);
							int var17 = this.alphaBlend(this.alphaBlend(var13, var14), this.alphaBlend(var15, var16));
							this.imageData.putInt((a + j * g) * 4, var17);
						}
					}

					GL11.glTexImage2D(GL11.GL_TEXTURE_2D, i, GL11.GL_RGBA, g, b, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
				}
			}

		}
	}

	// Spout HD end

	public void createTextureFromBytes(int[] par1ArrayOfInteger, int par2, int par3, int par4) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, par4);
		if (useMipmaps) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}

		if (this.blurTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		}

		if (this.clampTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		byte[] var5 = new byte[par2 * par3 * 4];

		for (int var6 = 0; var6 < par1ArrayOfInteger.length; ++var6) {
			int var7 = par1ArrayOfInteger[var6] >> 24 & 255;
			int var8 = par1ArrayOfInteger[var6] >> 16 & 255;
			int var9 = par1ArrayOfInteger[var6] >> 8 & 255;
			int var10 = par1ArrayOfInteger[var6] & 255;
			if (this.options != null && this.options.anaglyph) {
				int var11 = (var8 * 30 + var9 * 59 + var10 * 11) / 100;
				int var12 = (var8 * 30 + var9 * 70) / 100;
				int var13 = (var8 * 30 + var10 * 70) / 100;
				var8 = var11;
				var9 = var12;
				var10 = var13;
			}

			var5[var6 * 4 + 0] = (byte)var8;
			var5[var6 * 4 + 1] = (byte)var9;
			var5[var6 * 4 + 2] = (byte)var10;
			var5[var6 * 4 + 3] = (byte)var7;
		}

		// Spout HD Start
		this.imageData = TextureUtils.getByteBuffer(this.imageData, var5);
		// Spout HD End
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, par2, par3, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
	}

	public void deleteTexture(int par1) {
		this.textureNameToImageMap.removeObject(par1);
		this.singleIntBuffer.clear();
		this.singleIntBuffer.put(par1);
		this.singleIntBuffer.flip();
		GL11.glDeleteTextures(this.singleIntBuffer);
	}

	public int getTextureForDownloadableImage(String par1Str, String par2Str) {
		ThreadDownloadImageData var3 = (ThreadDownloadImageData)this.urlToImageDataMap.get(par1Str);
		if (var3 != null && var3.image != null && !var3.textureSetupComplete) {
			if (var3.textureName < 0) {
				var3.textureName = this.allocateAndSetupTexture(var3.image);
			} else {
				this.setupTexture(var3.image, var3.textureName);
			}

			var3.textureSetupComplete = true;
		}

		return var3 != null && var3.textureName >= 0?var3.textureName:(par2Str == null?-1:this.getTexture(par2Str));
	}

	public ThreadDownloadImageData obtainImageData(String par1Str, ImageBuffer par2ImageBuffer) {
		ThreadDownloadImageData var3 = (ThreadDownloadImageData)this.urlToImageDataMap.get(par1Str);
		if (var3 == null) {
			this.urlToImageDataMap.put(par1Str, new ThreadDownloadImageData(par1Str, par2ImageBuffer));
		} else {
			++var3.referenceCount;
		}

		return var3;
	}

	public void releaseImageData(String par1Str) {
		ThreadDownloadImageData var2 = (ThreadDownloadImageData)this.urlToImageDataMap.get(par1Str);
		if (var2 != null) {
			--var2.referenceCount;
			if (var2.referenceCount == 0) {
				// Spout Start
				if (Minecraft.theMinecraft.theWorld != null) {
					List<EntityPlayer> players = Minecraft.theMinecraft.theWorld.playerEntities;
					for (EntityPlayer player : players) {
						if (player.skinUrl != null && player.skinUrl.equals(par1Str)) {
							var2.referenceCount++;
						}
						if (player.playerCloakUrl != null && player.playerCloakUrl.equals(par1Str)) {
							var2.referenceCount++;
						}
					}
				}
				if (var2.referenceCount > 0) {
					return;
				}
				// Spout End
				if (var2.textureName >= 0) {
					this.deleteTexture(var2.textureName);
				}

				this.urlToImageDataMap.remove(par1Str);
			}
		}
	}

	public void registerTextureFX(TextureFX par1TextureFX) {
		// Spout HD start
		TextureUtils.registerTextureFX(this.textureList, par1TextureFX);
		// Spout HD end
	}

	public void updateDynamicTextures() {
		int var1 = -1;

		for (int var2 = 0; var2 < this.textureList.size(); ++var2) {
			TextureFX var3 = (TextureFX)this.textureList.get(var2);
			var3.anaglyphEnabled = this.options.anaglyph;
			var3.onTick();
//Spout HD Start
			this.imageData = TextureUtils.getByteBuffer(this.imageData, var3.imageData);
//Spout HD end
			if (var3.iconIndex != var1) {
				var3.bindImage(this);
				var1 = var3.iconIndex;
			}

			for (int var4 = 0; var4 < var3.tileSize; ++var4) {
				for (int var5 = 0; var5 < var3.tileSize; ++var5) {
					GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, var3.iconIndex % 16 * TileSize.int_size + var4 * TileSize.int_size, var3.iconIndex / 16 * TileSize.int_size + var5 * TileSize.int_size, TileSize.int_size, TileSize.int_size, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
				}
			}
		}

		CustomAnimation.updateAll();
	}

	private int alphaBlend(int par1, int par2) {
		int var3 = (par1 & -16777216) >> 24 & 255;
		int var4 = (par2 & -16777216) >> 24 & 255;
		short var5 = 255;
		short var15;
		short var16;
		if (var3 + var4 < 255) {
			var5 = 0;
			var15 = 1;
			var16 = 1;
		} else if (var3 > var4) {
			var15 = 255;
			var16 = 1;
		} else {
			var15 = 1;
			var16 = 255;
		}

		int var6 = (par1 >> 16 & 255) * var15;
		int var7 = (par1 >> 8 & 255) * var15;
		int var8 = (par1 & 255) * var15;
		int var9 = (par2 >> 16 & 255) * var16;
		int var10 = (par2 >> 8 & 255) * var16;
		int var11 = (par2 & 255) * var16;
		int var12 = (var6 + var9) / (var15 + var16);
		int var13 = (var7 + var10) / (var15 + var16);
		int var14 = (var8 + var11) / (var15 + var16);
		return var5 << 24 | var12 << 16 | var13 << 8 | var14;
	}

	public void refreshTextures() {
		TexturePackBase var1 = this.texturePack.selectedTexturePack;
		Iterator var2 = this.textureNameToImageMap.getKeySet().iterator();

		BufferedImage var4;
		while (var2.hasNext()) {
			int var3 = ((Integer)var2.next()).intValue();
			var4 = (BufferedImage)this.textureNameToImageMap.lookup(var3);
			this.setupTexture(var4, var3);
		}

		ThreadDownloadImageData var8;
		for (var2 = this.urlToImageDataMap.values().iterator(); var2.hasNext(); var8.textureSetupComplete = false) {
			var8 = (ThreadDownloadImageData)var2.next();
		}

		var2 = this.textureMap.keySet().iterator();

		String var9;
		while (var2.hasNext()) {
			var9 = (String)var2.next();

			try {
				// Spout HD Start
				if (var9.startsWith("##")) {
					var4 = this.unwrapImageByColumns(TextureUtils.getResourceAsBufferedImage(this, var1, var9.substring(2)));
				} else if (var9.startsWith("%clamp%")) {
					this.clampTexture = true;
					var4 = TextureUtils.getResourceAsBufferedImage(this, var1, var9.substring(7));
				} else if (var9.startsWith("%blur%")) {
					this.blurTexture = true;
					var4 = TextureUtils.getResourceAsBufferedImage(this, var1, var9.substring(6));
				} else if (var9.startsWith("%blurclamp%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					var4 = TextureUtils.getResourceAsBufferedImage(this, var1, var9.substring(11));
				} else {
					var4 = TextureUtils.getResourceAsBufferedImage(this, var1, var9);
				}
				if (var4 == null) {
					var2.remove();
					continue;
				}
				// Spout HD End

				int var5 = ((Integer)this.textureMap.get(var9)).intValue();
				this.setupTexture(var4, var5);
				this.blurTexture = false;
				this.clampTexture = false;
			} catch (IOException var7) {
				// Spout HD Start
				// Gracefully handle errors
				var2.remove();
				// var6.printStackTrace();
				// Spout HD End
			}
		}

		var2 = this.textureContentsMap.keySet().iterator();

		while (var2.hasNext()) {
			var9 = (String)var2.next();

			try {
				// Spout HD Start
				if (var9.startsWith("##")) {
					var4 = this.unwrapImageByColumns(TextureUtils.getResourceAsBufferedImage(this, var1, var9.substring(2)));
				} else if (var9.startsWith("%clamp%")) {
					this.clampTexture = true;
					var4 = TextureUtils.getResourceAsBufferedImage(this, var1, var9.substring(7));
				} else if (var9.startsWith("%blur%")) {
					this.blurTexture = true;
					var4 = TextureUtils.getResourceAsBufferedImage(this, var1, var9.substring(6));
				} else {
					var4 = TextureUtils.getResourceAsBufferedImage(this, var1, var9);
				}
				if (var4 == null) {
					var2.remove();
					continue;
				}
				// Spout HD End

				this.getImageContents(var4, (int[])this.textureContentsMap.get(var9));
				this.blurTexture = false;
				this.clampTexture = false;
			} catch (IOException var6) {
				//Spout HD Start
				//Gracefully handle errors
				var2.remove();
				//var6.printStackTrace();
				//Spout HD End
			}
		}
	}

	private BufferedImage readTextureImage(InputStream par1InputStream) throws IOException {
		BufferedImage var2 = ImageIO.read(par1InputStream);
		par1InputStream.close();
		return var2;
	}

	public void bindTexture(int par1) {
		if (par1 >= 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1);
		}
	}

	//Spout HD Start
	public void setTileSize(Minecraft minecraft) {
		this.imageData = GLAllocation.createDirectByteBuffer(TileSize.int_glBufferSize);
		this.refreshTextures();
		TextureUtils.refreshTextureFX(this.textureList);
	}
	//Spout HD End
}
