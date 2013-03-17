package net.minecraft.src;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
// MCPatcher Start
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.mod.CTMUtils;
import com.prupe.mcpatcher.mod.CustomAnimation;
import com.prupe.mcpatcher.mod.MipmapHelper;
// MCPatcher End
// Spout Start
import net.minecraft.client.Minecraft;
// Spout End

public class RenderEngine {
	private HashMap textureMap = new HashMap();

	/** Texture contents map (key: texture name, value: int[] contents) */
	private HashMap textureContentsMap = new HashMap();

	/** A mapping from GL texture names (integers) to BufferedImage instances */
	private IntHashMap textureNameToImageMap = new IntHashMap();

	/** An IntBuffer storing 1 int used as scratch space in RenderEngine */
	private IntBuffer singleIntBuffer = GLAllocation.createDirectIntBuffer(1);

	/** Stores the image data for the texture. */
	private IntBuffer imageData = GLAllocation.createDirectIntBuffer(4194304);
	// Spout Start - private to public
	public List textureList = new ArrayList();
	// Spout End

	/** A mapping from image URLs to ThreadDownloadImageData instances */
	private Map urlToImageDataMap = new HashMap();

	/** Reference to the GameSettings object */
	private GameSettings options;

	/** Flag set when a texture should not be repeated */
	public boolean clampTexture = false;

	/** Flag set when a texture should use blurry resizing */
	public boolean blurTexture = false;

	/** Texture pack */
	// Spout Start - private to public
	public TexturePackList texturePack;
	// Spout End

	/** Missing texture image */
	private BufferedImage missingTextureImage = new BufferedImage(64, 64, 2);
	// Spout Start
	public ITexturePack oldPack = null;
	// Spout End
	
	private final TextureMap field_94154_l;
	private final TextureMap field_94155_m;
	private int field_94153_n;


	public RenderEngine(TexturePackList par1TexturePackList, GameSettings par2GameSettings) {
		this.texturePack = par1TexturePackList;
		this.options = par2GameSettings;
		Graphics var3 = this.missingTextureImage.getGraphics();
		var3.setColor(Color.WHITE);
		int var4 = 10;
		int var5 = 0;

		while (var4 < 64) {
			String var6 = var5++ % 2 == 0 ? "missing" : "texture";
			var3.drawString(var6, 1, var4);
			var4 += var3.getFont().getSize();

			if (var5 % 2 == 0) {
				var4 += 5;
			}
		}

		var3.dispose();
		this.field_94154_l = new TextureMap(0, "terrain", "textures/blocks/", this.missingTextureImage);
		this.field_94155_m = new TextureMap(1, "items", "textures/items/", this.missingTextureImage);
	}


	// Spout Start - Don't use MCPatchers version
	public int[] getTextureContents(String par1Str) {
		ITexturePack var2 = this.texturePack.getSelectedTexturePack();
		int[] var3 = (int[])this.textureContentsMap.get(par1Str);
		if (var3 != null) {
			return var3;
		} else {
			try {
				int[] var7;
				if (par1Str.startsWith("##")) {				
					var7 = this.getImageContentsAndAllocate(this.unwrapImageByColumns(getResourceAsBufferedImage(this, var2, par1Str.substring(2))));
				} else if (par1Str.startsWith("%clamp%")) {
					this.clampTexture = true;
					var7 = this.getImageContentsAndAllocate(getResourceAsBufferedImage(this, var2, par1Str.substring(7)));
					this.clampTexture = false;
				} else if (par1Str.startsWith("%blur%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					var7 = this.getImageContentsAndAllocate(getResourceAsBufferedImage(this, var2, par1Str.substring(6)));
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
			} catch (IOException var5) {
				var5.printStackTrace();
				int[] var4 = this.getImageContentsAndAllocate(this.missingTextureImage);
				this.textureContentsMap.put(par1Str, var4);
				return var4;
			}
		}
	}
	// Spout End

	private int[] getImageContentsAndAllocate(BufferedImage par1BufferedImage) {
		return this.getImageContents(par1BufferedImage, new int[par1BufferedImage.getWidth() * par1BufferedImage.getHeight()]);
	}

	private int[] getImageContents(BufferedImage par1BufferedImage, int[] par2ArrayOfInteger) {
		// MCPatcher Start
		if (par1BufferedImage == null) {
			return par2ArrayOfInteger;
		} else if (par1BufferedImage == null) {
			return par2ArrayOfInteger;
		} else {
			int var3 = par1BufferedImage.getWidth();
			int var4 = par1BufferedImage.getHeight();
			par1BufferedImage.getRGB(0, 0, var3, var4, par2ArrayOfInteger, 0, var3);
			return par2ArrayOfInteger;
		}
		// MCPatcher End
	}

	public void func_98187_b(String par1Str) {
		this.bindTexture(this.getTexture(par1Str));
	}
	
	public void bindTexture(int par1) {
		if (par1 != this.field_94153_n) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1);
			this.field_94153_n = par1;
		}
	}
	
	public void func_98185_a() {
		this.field_94153_n = -1;
	}	
	
	
	// Spout Start - Don't use MCPatchers Version
	public int getTexture(String par1Str) {
		if (par1Str.equals("/terrain.png")) {
			this.field_94154_l.func_94246_d().func_94277_a(0);
			return this.field_94154_l.func_94246_d().func_94282_c();
		} else if (par1Str.equals("/gui/items.png")) {
			this.field_94155_m.func_94246_d().func_94277_a(0);
			return this.field_94155_m.func_94246_d().func_94282_c();
		} else {
			Integer var2 = (Integer)this.textureMap.get(par1Str);

			if (var2 != null) {
				return var2.intValue();
			} else {
				String var8 = par1Str;

				try {
					int var3 = GLAllocation.generateTextureNames();
					boolean var9 = par1Str.startsWith("%blur%");

					if (var9) {
						par1Str = par1Str.substring(6);
					}

					boolean var5 = par1Str.startsWith("%clamp%");

					if (var5) {
						par1Str = par1Str.substring(7);
					}

					InputStream var6 = this.texturePack.getSelectedTexturePack().getResourceAsStream(par1Str);

					if (var6 == null) {
						MipmapHelper.setupTexture(this, this.missingTextureImage, var3, var9, var5, par1Str);
					} else {
						MipmapHelper.setupTexture(this, this.readTextureImage(var6), var3, var9, var5, par1Str);
					}

					this.textureMap.put(var8, Integer.valueOf(var3));
					return var3;
				} catch (Exception var7) {
					var7.printStackTrace();
					int var4 = GLAllocation.generateTextureNames();
					this.setupTexture(this.missingTextureImage, var4);
					this.textureMap.put(par1Str, Integer.valueOf(var4));
					return var4;
				}
			}
		}
	}

	// Spout End

	/**
	 * Takes an image with multiple 16-pixel-wide columns and creates a new 16-pixel-wide image where the columns are
	 * stacked vertically
	 */
	public int allocateAndSetupTexture(BufferedImage par1BufferedImage) {
		int var2 = GLAllocation.generateTextureNames();
		this.setupTexture(par1BufferedImage, var2);
		this.textureNameToImageMap.addKey(var2, par1BufferedImage);
		return var2;
	}
	
	/**
	 * Copy the supplied image onto the specified OpenGL texture
	 */
	public void setupTexture(BufferedImage par1BufferedImage, int par2) {
		if (par1BufferedImage != null) {
			this.func_98184_a(par1BufferedImage, par2, false, false);
		}
	}

	public void func_98184_a(BufferedImage par1BufferedImage, int par2, boolean par3, boolean par4) {
		if (MipmapHelper.currentLevel == 0) {
			if (par1BufferedImage == null) {
				return;
			}

			this.bindTexture(par2);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

			if (par3) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			}

			if (par4) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
			} else {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			}
		}

		int var5 = par1BufferedImage.getWidth();
		int var6 = par1BufferedImage.getHeight();
		int[] var7 = new int[var5 * var6];
		par1BufferedImage.getRGB(0, 0, var5, var6, var7, 0, var5);

		if (this.options != null && this.options.anaglyph) {
			var7 = this.func_98186_a(var7);
		}

		this.imageData = TexturePackAPI.getIntBuffer(this.imageData, var7);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, MipmapHelper.currentLevel, GL11.GL_RGBA, var5, var6, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, this.imageData);
	}

	private int[] func_98186_a(int[] par1ArrayOfInteger) {
		int[] var2 = new int[par1ArrayOfInteger.length];

		for (int var3 = 0; var3 < par1ArrayOfInteger.length; ++var3) {
			int var4 = par1ArrayOfInteger[var3] >> 24 & 255;
			int var5 = par1ArrayOfInteger[var3] >> 16 & 255;
			int var6 = par1ArrayOfInteger[var3] >> 8 & 255;
			int var7 = par1ArrayOfInteger[var3] & 255;
			int var8 = (var5 * 30 + var6 * 59 + var7 * 11) / 100;
			int var9 = (var5 * 30 + var6 * 70) / 100;
			int var10 = (var5 * 30 + var7 * 70) / 100;
			var2[var3] = var4 << 24 | var8 << 16 | var9 << 8 | var10;
		}

		return var2;
	}

	public void createTextureFromBytes(int[] par1ArrayOfInteger, int par2, int par3, int par4) {
		this.bindTexture(par4);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		if (this.options != null && this.options.anaglyph) {
			par1ArrayOfInteger = this.func_98186_a(par1ArrayOfInteger);
		}

		this.imageData = TexturePackAPI.getIntBuffer(this.imageData, par1ArrayOfInteger);
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, par2, par3, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, this.imageData);
	}
	
	/**
	 * Deletes a single GL texture
	 */
	public void deleteTexture(int par1) {
		this.textureNameToImageMap.removeObject(par1);
		GL11.glDeleteTextures(par1);
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

	public void updateDynamicTextures() {
		CTMUtils.updateAnimations();
		this.field_94154_l.func_94248_c();
		this.field_94155_m.func_94248_c();
		CustomAnimation.updateAll();
	}

	/**
	 * Call setupTexture on all currently-loaded textures again to account for changes in rendering options
	 */
	public void refreshTextures() {
		TexturePackChangeHandler.beforeChange1();
		ITexturePack var1 = this.texturePack.getSelectedTexturePack();
		this.func_94152_c();
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
				// Spout Start - Dont use MCPatchers Version
				if (var9.startsWith("##")) {
					var4 = this.unwrapImageByColumns(getResourceAsBufferedImage(this, var1, var9.substring(2)));
				} else if (var9.startsWith("%clamp%")) {
					this.clampTexture = true;
					var4 = getResourceAsBufferedImage(this, var1, var9.substring(7));
				} else if (var9.startsWith("%blur%")) {
					this.blurTexture = true;
					var4 = getResourceAsBufferedImage(this, var1, var9.substring(6));
				} else if (var9.startsWith("%blurclamp%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					var4 = getResourceAsBufferedImage(this, var1, var9.substring(11));
				} else {
					var4 = getResourceAsBufferedImage(this, var1, var9);
				}
				if (var4 == null) {
					var2.remove();
					continue;
				}
				// MCPatcher End

				int var5 = ((Integer)this.textureMap.get(var9)).intValue();
				this.setupTexture(var4, var5);
				this.blurTexture = false;
				this.clampTexture = false;
			} catch (IOException var7) {
				// MCPatcher Start - Gracefully handle errors
				var2.remove();
				// var7.printStackTrace();
				// MCPatcher End
			}
		}

		var2 = this.textureContentsMap.keySet().iterator();

		while (var2.hasNext()) {
			var9 = (String)var2.next();

			try {
				// Spout Start - Don't use MCPatchers Version
				if (var9.startsWith("##")) {
					var4 = this.unwrapImageByColumns(getResourceAsBufferedImage(this, var1, var9.substring(2)));
				} else if (var9.startsWith("%clamp%")) {
					this.clampTexture = true;
					var4 = getResourceAsBufferedImage(this, var1, var9.substring(7));
				} else if (var9.startsWith("%blur%")) {
					this.blurTexture = true;
					var4 = getResourceAsBufferedImage(this, var1, var9.substring(6));
				} else {
					var4 = getResourceAsBufferedImage(this, var1, var9);
				}
				if (var4 == null) {
					var2.remove();
					continue;
				}
				// Spout End

				this.getImageContents(var4, (int[])this.textureContentsMap.get(var9));
				this.blurTexture = false;
				this.clampTexture = false;
			} catch (IOException var6) {
				// MCPatcher Start - Gracefully handle errors
				var2.remove();
				//var6.printStackTrace();
				// MCPatcher End
			}
		}
		Minecraft.getMinecraft().fontRenderer.func_98304_a();
		Minecraft.getMinecraft().standardGalacticFontRenderer.func_98304_a();
		TexturePackChangeHandler.afterChange1();
	}



	/**
	 * Returns a BufferedImage read off the provided input stream.  Args: inputStream
	 */
	private BufferedImage readTextureImage(InputStream par1InputStream) throws IOException {
		if (par1InputStream == null) {
			return null;
		} else {
		BufferedImage var2 = ImageIO.read(par1InputStream);
		par1InputStream.close();
		return var2;
		}
	}

	public void func_94152_c() {
		TexturePackAPI.enableTextureBorder = true;
		this.field_94154_l.func_94247_b();
		TexturePackAPI.enableTextureBorder = false;
		this.field_94155_m.func_94247_b();
	}

	public Icon func_96448_c(int par1) {
		switch (par1) {
			case 0:
				return this.field_94154_l.func_96455_e();

			case 1:
			default:
				return this.field_94155_m.func_96455_e();
		}
	}
	
	// Spout Start
		public static BufferedImage getResourceAsBufferedImage(String var0) throws IOException {
			return getResourceAsBufferedImage(getSelectedTexturePack(), var0);
		}

		public static BufferedImage getResourceAsBufferedImage(Object var0, String var1) throws IOException {
			return getResourceAsBufferedImage(var1);
		}

		public static BufferedImage getResourceAsBufferedImage(Object var0, Object var1, String var2) throws IOException {
			return getResourceAsBufferedImage(var2);
		}
		
		public static int getTileSize() {
			return getTileSize(getSelectedTexturePack());
		}
		
		public static int getTileSize(TexturePackImplementation var0) {
			int var1 = 0;
			Iterator var2 = expectedColumns.entrySet().iterator();
			while (var2.hasNext()) {
				Entry var3 = (Entry)var2.next();
				InputStream var4 = null;
				try {
					var4 = getResourceAsStream(var0, (String)var3.getKey());
					if (var4 != null) {
						BufferedImage var5 = ImageIO.read(var4);
						int var6 = var5.getWidth() / ((Integer)var3.getValue()).intValue();
						var1 = Math.max(var1, var6);
					}
				} catch (Exception var10) {
				} finally {
					MCPatcherUtils.close(var4);
				}
			}	
			return var1 > 0 ? var1 : 16;
		}

		public static InputStream getResourceAsStream(TexturePackImplementation var0, String var1) {
			InputStream var2 = null;

			if (oldCreativeGui && var1.equals("/gui/allitems.png")) {
				var2 = getResourceAsStream(var0, "/gui/allitemsx.png");

				if (var2 != null) {
					return var2;
				}
			}

			if (var0 != null) {
				try {
					var2 = var0.getResourceAsStream(var1);
				} catch (Exception var4) {
					var4.printStackTrace();
				}
			}

			if (var2 == null) {
				var2 = TextureUtils.class.getResourceAsStream(var1);
			}

			if (var2 == null && var1.startsWith("/anim/custom_")) {
				var2 = getResourceAsStream(var0, var1.substring(5));
			}

			if (var2 == null && isRequiredResource(var1)) {
				var2 = Thread.currentThread().getContextClassLoader().getResourceAsStream(var1);
			}

			return var2;
		}

		public static BufferedImage getResourceAsBufferedImage(TexturePackImplementation var0, String texture) throws IOException {
			BufferedImage image = null;
			boolean var3 = false;

			if (useTextureCache && var0 == lastTexturePack) {
				image = (BufferedImage)cache.get(texture);

				if (image != null) {
					var3 = true;
				}
			}

			if (image == null) {
				InputStream var4 = getResourceAsStream(var0, texture);

				if (var4 != null) {
					try {
						image = ImageIO.read(var4);
					} finally {
						MCPatcherUtils.close(var4);
					}
				}
			}

			if (image == null) {
				// Search local files (downloaded texture)
				FileImageInputStream imageStream = null;
				try {
					File test = new File(texture);
					if (test.exists()) {
						imageStream = new FileImageInputStream(test);
						image = ImageIO.read(imageStream);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					if (imageStream != null) {
						try {
							imageStream.close();
						} catch (Exception e) { }
					}
				}
			}

			if (image == null) {
				if (isRequiredResource(texture)) {
					throw new IOException(texture + " image is null");
				} else {
					return null;
				}
			} else {
				if (useTextureCache && !var3 && var0 != lastTexturePack) {
					cache.clear();
				}

				if (!var3) {
					Integer var11;

					if (isCustomTerrainItemResource(texture)) {
						var11 = Integer.valueOf(1);
					} else {
						var11 = (Integer)expectedColumns.get(texture);
					}

					if (var11 != null && image.getWidth() != var11.intValue() * TileSize.int_size) {
						image = resizeImage(image, var11.intValue() * TileSize.int_size);
					}

					if (useTextureCache) {
						lastTexturePack = var0;
						cache.put(texture, image);
					}

					if (texture.matches("^/mob/.*_eyes\\d*\\.png$")) {
						int var5 = 0;

						for (int var6 = 0; var6 < image.getWidth(); ++var6) {
							for (int var7 = 0; var7 < image.getHeight(); ++var7) {
								int var8 = image.getRGB(var6, var7);

								if ((var8 & -16777216) == 0 && var8 != 0) {
									image.setRGB(var6, var7, 0);
									++var5;
								}
							}
						}
					}
				}

				return image;
			}
		}

		public static InputStream getResourceAsStream(String var0) {
			return getResourceAsStream(getSelectedTexturePack(), var0);
		}
		
		public static boolean isRequiredResource(String var0) {
			return var0.equals("/terrain.png") || var0.equals("/gui/items.png");
		}
		
		public static TexturePackImplementation getSelectedTexturePack() {
			Minecraft var0 = MCPatcherUtils.getMinecraft();
			return (TexturePackImplementation) (var0 == null ? null : (var0.texturePackList == null ? null : var0.texturePackList.getSelectedTexturePack()));
		}

		public static String getTexturePackName(TexturePackImplementation var0) {
			return var0 == null ? "Default" : var0.texturePackFileName;
		}

		// Spout End
}
