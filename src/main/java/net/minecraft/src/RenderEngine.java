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
// Spout HD Start
import net.minecraft.client.Minecraft;

import com.pclewis.mcpatcher.mod.CustomAnimation;
import com.pclewis.mcpatcher.mod.TextureUtils;
import com.pclewis.mcpatcher.mod.TileSize;
// Spout HD End

public class RenderEngine {
	private HashMap textureMap = new HashMap();

	/** Texture contents map (key: texture name, value: int[] contents) */
	private HashMap textureContentsMap = new HashMap();

	/** A mapping from GL texture names (integers) to BufferedImage instances */
	private IntHashMap textureNameToImageMap = new IntHashMap();

	/** An IntBuffer storing 1 int used as scratch space in RenderEngine */
	private IntBuffer singleIntBuffer = GLAllocation.createDirectIntBuffer(1);

	/** Stores the image data for the texture. */
	private ByteBuffer imageData = GLAllocation.createDirectByteBuffer(16777216);
	public List textureList = new ArrayList(); // Spout private -> public

	/** A mapping from image URLs to ThreadDownloadImageData instances */
	private Map urlToImageDataMap = new HashMap();

	/** Reference to the GameSettings object */
	private GameSettings options;

	/** Flag set when a texture should not be repeated */
	public boolean clampTexture = false;

	/** Flag set when a texture should use blurry resizing */
	public boolean blurTexture = false;

	/** Texture pack */
	public TexturePackList texturePack; // Spout private -> public

	/** Missing texture image */
	private BufferedImage missingTextureImage = new BufferedImage(64, 64, 2);
	// Spout Start
	public ITexturePack oldPack = null;
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
		ITexturePack var2 = this.texturePack.getSelectedTexturePack();
		int[] var3 = (int[])this.textureContentsMap.get(par1Str);

		if (var3 != null) {
			return var3;
		} else {
			try {
				Object var4 = null;
				int[] var7;

				if (par1Str.startsWith("##")) {
					// Spout HD Start
					var7 = this.getImageContentsAndAllocate(this.unwrapImageByColumns(TextureUtils.getResourceAsBufferedImage(this, var2, par1Str.substring(2))));
					// Spout HD End
				} else if (par1Str.startsWith("%clamp%")) {
					this.clampTexture = true;
					// Spout HD Start
					var7 = this.getImageContentsAndAllocate(TextureUtils.getResourceAsBufferedImage(this, var2, par1Str.substring(7)));
					// Spout HD End
					this.clampTexture = false;
				} else if (par1Str.startsWith("%blur%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					// Spout HD Start
					var7 = this.getImageContentsAndAllocate(TextureUtils.getResourceAsBufferedImage(this, var2, par1Str.substring(6)));
					// Spout HD Start
					this.clampTexture = false;
					this.blurTexture = false;
				} else {
					InputStream var8 = var2.getResourceAsStream(par1Str);

					if (var8 == null) {
						var7 = this.getImageContentsAndAllocate(this.missingTextureImage);
					} else {
						var7 = this.getImageContentsAndAllocate(this.readTextureImage(var8));
					}
				}

				this.textureContentsMap.put(par1Str, var7);
				return var7;
			} catch (IOException var6) {
				var6.printStackTrace();
				int[] var5 = this.getImageContentsAndAllocate(this.missingTextureImage);
				this.textureContentsMap.put(par1Str, var5);
				return var5;
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
		// Spout HD Start
		if (par1BufferedImage == null) {
			return par2ArrayOfInteger;
		} else {
			int var3 = par1BufferedImage.getWidth();
			int var4 = par1BufferedImage.getHeight();
			par1BufferedImage.getRGB(0, 0, var3, var4, par2ArrayOfInteger, 0, var3);
			return par2ArrayOfInteger;
		}
		// Spout HD End
	}

	public int getTexture(String par1Str) {
		Integer var2 = (Integer)this.textureMap.get(par1Str);

		if (var2 != null) {
			return var2.intValue();
		} else {
			ITexturePack var6 = this.texturePack.getSelectedTexturePack();

			try {
				this.singleIntBuffer.clear();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				int var3 = this.singleIntBuffer.get(0);

				if (par1Str.startsWith("##")) {
					// Spout HD Start
					this.setupTexture(this.unwrapImageByColumns(TextureUtils.getResourceAsBufferedImage(this, var6, par1Str.substring(2))), var3);
					// Spout HD End
				} else if (par1Str.startsWith("%clamp%")) {
					this.clampTexture = true;
					// Spout HD Start
					this.setupTexture(TextureUtils.getResourceAsBufferedImage(this, var6, par1Str.substring(7)), var3);
					// Spout HD End
					this.clampTexture = false;
				} else if (par1Str.startsWith("%blur%")) {
					this.blurTexture = true;
					// Spout HD Start
					this.setupTexture(TextureUtils.getResourceAsBufferedImage(this, var6, par1Str.substring(6)), var3);
					// Spout HD End
					this.blurTexture = false;
				} else if (par1Str.startsWith("%blurclamp%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					// Spout HD Start
					this.setupTexture(TextureUtils.getResourceAsBufferedImage(this, var6, par1Str.substring(11)), var3);
					// Spout HD End
					this.blurTexture = false;
					this.clampTexture = false;
				} else {
					this.setupTexture(TextureUtils.getResourceAsBufferedImage(par1Str), var3); // Spout HD
				}

				this.textureMap.put(par1Str, Integer.valueOf(var3));
				return var3;
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

	/**
	 * Takes an image with multiple 16-pixel-wide columns and creates a new 16-pixel-wide image where the columns are
	 * stacked vertically
	 */
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

	/**
	 * Copy the supplied image onto a newly-allocated OpenGL texture, returning the allocated texture name
	 */
	public int allocateAndSetupTexture(BufferedImage par1BufferedImage) {
		this.singleIntBuffer.clear();
		GLAllocation.generateTextureNames(this.singleIntBuffer);
		int var2 = this.singleIntBuffer.get(0);
		this.setupTexture(par1BufferedImage, var2);
		this.textureNameToImageMap.addKey(var2, par1BufferedImage);
		return var2;
	}

	/**
	 * Copy the supplied image onto the specified OpenGL texture
	 */
	public void setupTexture(BufferedImage par1BufferedImage, int par2) {
		if (par1BufferedImage != null) {    // Spout HD
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, par2);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

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

			// Spout Start
			int textureWidth = par1BufferedImage.getWidth();
			int textureHeight = par1BufferedImage.getHeight();
			int[] texData = new int[textureWidth * textureHeight];
			byte[] texColors = new byte[textureWidth * textureHeight * 4];
			//Performance reasons
			boolean handled = false;
			try {
				java.awt.image.DataBuffer buf = par1BufferedImage.getRaster().getDataBuffer();
				if (buf instanceof java.awt.image.DataBufferInt) {
					int[] srcbuf = ((java.awt.image.DataBufferInt) buf).getData();
					System.arraycopy(srcbuf, 0, texData, 0, srcbuf.length);
					handled = true;
				}
			}
			catch (Exception ignore) { }
			if (!handled) {
				par1BufferedImage.getRGB(0, 0, textureWidth, textureHeight, texData, 0, textureWidth);
			}

			int var8;
			int var9;
			int var10;
			int var11;
			int var12;
			int var13;
			int var14;

		for (int var7 = 0; var7 < texData.length; ++var7) {
			var8 = texData[var7] >> 24 & 255;
			var9 = texData[var7] >> 16 & 255;
			var10 = texData[var7] >> 8 & 255;
			var11 = texData[var7] & 255;

			if (this.options != null && this.options.anaglyph) {
				var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
				var13 = (var9 * 30 + var10 * 70) / 100;
				var14 = (var9 * 30 + var11 * 70) / 100;
				var9 = var12;
				var10 = var13;
				var11 = var14;
			}

			texColors[var7 * 4 + 0] = (byte)var9;
			texColors[var7 * 4 + 1] = (byte)var10;
			texColors[var7 * 4 + 2] = (byte)var11;
			texColors[var7 * 4 + 3] = (byte)var8;
		}
		//Spout End

		this.imageData = TextureUtils.getByteBuffer(this.imageData, texColors);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureWidth, textureHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
		}
		// Spout HD End
	}

	public void createTextureFromBytes(int[] par1ArrayOfInteger, int par2, int par3, int par4) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, par4);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

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

	/**
	 * Deletes a single GL texture
	 */
	public void deleteTexture(int par1) {
		this.textureNameToImageMap.removeObject(par1);
		this.singleIntBuffer.clear();
		this.singleIntBuffer.put(par1);
		this.singleIntBuffer.flip();
		GL11.glDeleteTextures(this.singleIntBuffer);
	}

	/**
	 * Takes a URL of a downloadable image and the name of the local image to be used as a fallback.  If the image has been
	 * downloaded, returns the GL texture of the downloaded image, otherwise returns the GL texture of the fallback image.
	 */
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

		return var3 != null && var3.textureName >= 0 ? var3.textureName : (par2Str == null ? -1 : this.getTexture(par2Str));
	}

	public boolean func_82773_c(String par1Str) {
		return this.urlToImageDataMap.containsKey(par1Str);
	}

	/**
	 * Return a ThreadDownloadImageData instance for the given URL.  If it does not already exist, it is created and uses
	 * the passed ImageBuffer.  If it does, its reference count is incremented.
	 */
	public ThreadDownloadImageData obtainImageData(String par1Str, IImageBuffer par2IImageBuffer) {
		ThreadDownloadImageData var3 = (ThreadDownloadImageData)this.urlToImageDataMap.get(par1Str);

		if (var3 == null) {
			this.urlToImageDataMap.put(par1Str, new ThreadDownloadImageData(par1Str, par2IImageBuffer));
		} else {
			++var3.referenceCount;
		}

		return var3;
	}

	/**
	 * Decrements the reference count for a given URL, deleting the image data if the reference count hits 0
	 */
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
		// Spout HD Start
		TextureUtils.registerTextureFX(this.textureList, par1TextureFX);
		// Spout HD End
	}

	public void updateDynamicTextures() {
		int var1 = -1;

		for (int var2 = 0; var2 < this.textureList.size(); ++var2) {
			TextureFX var3 = (TextureFX)this.textureList.get(var2);
			var3.anaglyphEnabled = this.options.anaglyph;
			var3.onTick();
			var1 = this.func_82772_a(var3, var1);
		}
	}

	public int func_82772_a(TextureFX par1TextureFX, int par2) {
		// Spout HD Start
		this.imageData = TextureUtils.getByteBuffer(this.imageData, par1TextureFX.imageData);
		// Spout HD End

		if (par1TextureFX.iconIndex != par2) {
			par1TextureFX.bindImage(this);
			par2 = par1TextureFX.iconIndex;
		}

		for (int var3 = 0; var3 < par1TextureFX.tileSize; ++var3) {
			for (int var4 = 0; var4 < par1TextureFX.tileSize; ++var4) {
				GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, par1TextureFX.iconIndex % 16 * 16 + var3 * 16, par1TextureFX.iconIndex / 16 * 16 + var4 * 16, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
			}
		}

		return par2;
	}

	/**
	 * Call setupTexture on all currently-loaded textures again to account for changes in rendering options
	 */
	public void refreshTextures() {
		ITexturePack var1 = this.texturePack.getSelectedTexturePack();
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
				// Spout HD Start
				//Gracefully handle errors
				var2.remove();
				//var6.printStackTrace();
				// Spout HD End
			}
		}
	}

	/**
	 * Returns a BufferedImage read off the provided input stream.  Args: inputStream
	 */
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

	// Spout HD Start
	public void setTileSize(Minecraft var1) {
		this.imageData = GLAllocation.createDirectByteBuffer(TileSize.int_glBufferSize);
		this.refreshTextures();
		TextureUtils.refreshTextureFX(this.textureList);
	}
	// Spout HD End
}
