package net.minecraft.src;

import com.pclewis.mcpatcher.TexturePackAPI;
import com.pclewis.mcpatcher.mod.CustomAnimation;
import com.pclewis.mcpatcher.mod.MipmapHelper;
import com.pclewis.mcpatcher.mod.TextureUtils;
import com.pclewis.mcpatcher.mod.TileSize;
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
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

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
	private List textureList = new ArrayList();

	/** A mapping from image URLs to ThreadDownloadImageData instances */
	private Map urlToImageDataMap = new HashMap();

	/** Reference to the GameSettings object */
	private GameSettings options;

	/** Flag set when a texture should not be repeated */
	public boolean clampTexture = false;

	/** Flag set when a texture should use blurry resizing */
	public boolean blurTexture = false;

	/** Texture pack */
	private TexturePackList texturePack;

	/** Missing texture image */
	private BufferedImage missingTextureImage = new BufferedImage(64, 64, 2);

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
					var7 = this.getImageContentsAndAllocate(this.unwrapImageByColumns(TextureUtils.getImage(this, var2, par1Str.substring(2))));
				} else if (par1Str.startsWith("%clamp%")) {
					this.clampTexture = true;
					var7 = this.getImageContentsAndAllocate(TextureUtils.getImage(this, var2, par1Str.substring(7)));
					this.clampTexture = false;
				} else if (par1Str.startsWith("%blur%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					var7 = this.getImageContentsAndAllocate(TextureUtils.getImage(this, var2, par1Str.substring(6)));
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
		if (par1BufferedImage == null) {
			return par2ArrayOfInteger;
		} else {
			int var3 = par1BufferedImage.getWidth();
			int var4 = par1BufferedImage.getHeight();
			par1BufferedImage.getRGB(0, 0, var3, var4, par2ArrayOfInteger, 0, var3);
			return par2ArrayOfInteger;
		}
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
					MipmapHelper.setupTexture(this, this.unwrapImageByColumns(TextureUtils.getImage(this, var6, par1Str.substring(2))), var3, par1Str);
				} else if (par1Str.startsWith("%clamp%")) {
					this.clampTexture = true;
					MipmapHelper.setupTexture(this, TextureUtils.getImage(this, var6, par1Str.substring(7)), var3, par1Str);
					this.clampTexture = false;
				} else if (par1Str.startsWith("%blur%")) {
					this.blurTexture = true;
					MipmapHelper.setupTexture(this, TextureUtils.getImage(this, var6, par1Str.substring(6)), var3, par1Str);
					this.blurTexture = false;
				} else if (par1Str.startsWith("%blurclamp%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					MipmapHelper.setupTexture(this, TextureUtils.getImage(this, var6, par1Str.substring(11)), var3, par1Str);
					this.blurTexture = false;
					this.clampTexture = false;
				} else {
					MipmapHelper.setupTexture(this, TexturePackAPI.getImage(par1Str), var3, par1Str);
				}

				this.textureMap.put(par1Str, Integer.valueOf(var3));
				return var3;
			} catch (Exception var5) {
				var5.printStackTrace();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				int var4 = this.singleIntBuffer.get(0);
				MipmapHelper.setupTexture(this, this.missingTextureImage, var4, par1Str);
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
		if (MipmapHelper.currentLevel == 0) {
			if (par1BufferedImage == null) {
				return;
			}

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
		}

		int var3 = par1BufferedImage.getWidth();
		int var4 = par1BufferedImage.getHeight();
		int[] var5 = new int[var3 * var4];
		byte[] var6 = new byte[var3 * var4 * 4];
		par1BufferedImage.getRGB(0, 0, var3, var4, var5, 0, var3);

		for (int var7 = 0; var7 < var5.length; ++var7) {
			int var8 = var5[var7] >> 24 & 255;
			int var9 = var5[var7] >> 16 & 255;
			int var10 = var5[var7] >> 8 & 255;
			int var11 = var5[var7] & 255;

			if (this.options != null && this.options.anaglyph) {
				int var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
				int var13 = (var9 * 30 + var10 * 70) / 100;
				int var14 = (var9 * 30 + var11 * 70) / 100;
				var9 = var12;
				var10 = var13;
				var11 = var14;
			}

			var6[var7 * 4 + 0] = (byte)var9;
			var6[var7 * 4 + 1] = (byte)var10;
			var6[var7 * 4 + 2] = (byte)var11;
			var6[var7 * 4 + 3] = (byte)var8;
		}

		this.imageData = TextureUtils.getByteBuffer(this.imageData, var6);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, MipmapHelper.currentLevel, GL11.GL_RGBA, var3, var4, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
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

		this.imageData = TextureUtils.getByteBuffer(this.imageData, var5);
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

	/**
	 * Checks if urlToImageDataMap has image data for the given key
	 */
	public boolean hasImageData(String par1Str) {
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
				if (var2.textureName >= 0) {
					this.deleteTexture(var2.textureName);
				}

				this.urlToImageDataMap.remove(par1Str);
			}
		}
	}

	public void registerTextureFX(TextureFX par1TextureFX) {
		TextureUtils.registerTextureFX(this.textureList, par1TextureFX);
	}

	public void updateDynamicTextures() {
		int var1 = -1;

		for (int var2 = 0; var2 < this.textureList.size(); ++var2) {
			TextureFX var3 = (TextureFX)this.textureList.get(var2);
			var3.anaglyphEnabled = this.options.anaglyph;
			var3.onTick();
			var1 = this.updateDynamicTexture(var3, var1);
		}

		CustomAnimation.updateAll();
	}

	/**
	 * Updates a single dynamic texture
	 */
	public int updateDynamicTexture(TextureFX par1TextureFX, int par2) {
		this.imageData = TextureUtils.getByteBuffer(this.imageData, par1TextureFX.imageData);

		if (par1TextureFX.iconIndex != par2) {
			par1TextureFX.bindImage(this);
			par2 = par1TextureFX.iconIndex;
		}

		for (int var3 = 0; var3 < par1TextureFX.tileSize; ++var3) {
			for (int var4 = 0; var4 < par1TextureFX.tileSize; ++var4) {
				MipmapHelper.glTexSubImage2D(3553, 0, par1TextureFX.iconIndex % 16 * TileSize.int_size + var3 * TileSize.int_size, par1TextureFX.iconIndex / 16 * TileSize.int_size + var4 * TileSize.int_size, TileSize.int_size, TileSize.int_size, 6408, 5121, this.imageData, par1TextureFX);
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
				if (var9.startsWith("##")) {
					var4 = this.unwrapImageByColumns(TextureUtils.getImage(this, var1, var9.substring(2)));
				} else if (var9.startsWith("%clamp%")) {
					this.clampTexture = true;
					var4 = TextureUtils.getImage(this, var1, var9.substring(7));
				} else if (var9.startsWith("%blur%")) {
					this.blurTexture = true;
					var4 = TextureUtils.getImage(this, var1, var9.substring(6));
				} else if (var9.startsWith("%blurclamp%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					var4 = TextureUtils.getImage(this, var1, var9.substring(11));
				} else {
					var4 = TextureUtils.getImage(this, var1, var9);
				}

				int var5 = ((Integer)this.textureMap.get(var9)).intValue();
				MipmapHelper.setupTexture(this, var4, var5, var9);
				this.blurTexture = false;
				this.clampTexture = false;
			} catch (IOException var7) {
				var7.printStackTrace();
			}
		}

		var2 = this.textureContentsMap.keySet().iterator();

		while (var2.hasNext()) {
			var9 = (String)var2.next();

			try {
				if (var9.startsWith("##")) {
					var4 = this.unwrapImageByColumns(TextureUtils.getImage(this, var1, var9.substring(2)));
				} else if (var9.startsWith("%clamp%")) {
					this.clampTexture = true;
					var4 = TextureUtils.getImage(this, var1, var9.substring(7));
				} else if (var9.startsWith("%blur%")) {
					this.blurTexture = true;
					var4 = TextureUtils.getImage(this, var1, var9.substring(6));
				} else {
					var4 = TextureUtils.getImage(this, var1, var9);
				}

				this.getImageContents(var4, (int[])this.textureContentsMap.get(var9));
				this.blurTexture = false;
				this.clampTexture = false;
			} catch (IOException var6) {
				var6.printStackTrace();
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

	public void reloadTextures(Minecraft var1) {
		this.imageData = GLAllocation.createDirectByteBuffer(TileSize.int_glBufferSize);
		this.refreshTextures();
		TextureUtils.refreshTextureFX(this.textureList);
	}
}
