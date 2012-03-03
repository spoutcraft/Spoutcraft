package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.mod.CustomAnimation;
import com.pclewis.mcpatcher.mod.TileSize;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.ColorizerWater;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.TextureCompassFX;
import net.minecraft.src.TextureFX;
import net.minecraft.src.TextureFlamesFX;
import net.minecraft.src.TextureLavaFX;
import net.minecraft.src.TextureLavaFlowFX;
import net.minecraft.src.TexturePackBase;
import net.minecraft.src.TexturePackCustom;
import net.minecraft.src.TexturePackDefault;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.TexturePortalFX;
import net.minecraft.src.TextureWatchFX;
import net.minecraft.src.TextureWaterFX;
import net.minecraft.src.TextureWaterFlowFX;

import org.spoutcraft.client.SpoutClient;

public class TextureUtils {
	public static Minecraft minecraft;
	private static boolean animatedFire = true;
	private static boolean animatedLava = true;
	private static boolean animatedWater = true;
	private static boolean animatedPortal = true;
	private static boolean customFire = true;
	private static boolean customLava = true;
	private static boolean customWater = true;
	private static boolean customPortal = true;
    private static boolean customOther = true;
	public static final int LAVA_STILL_TEXTURE_INDEX = 237;
	public static final int LAVA_FLOWING_TEXTURE_INDEX = 238;
	public static final int WATER_STILL_TEXTURE_INDEX = 205;
	public static final int WATER_FLOWING_TEXTURE_INDEX = 206;
	public static final int FIRE_E_W_TEXTURE_INDEX = 31;
	public static final int FIRE_N_S_TEXTURE_INDEX = 47;
	public static final int PORTAL_TEXTURE_INDEX = 14;
	private static Map<String, Integer> expectedColumns = new HashMap<String, Integer>();
	private static boolean reclaimGLMemory = false;
	private static TexturePackBase lastTexturePack = null;
	private static Map<String, BufferedImage> cache = new HashMap<String, BufferedImage>();

	public static boolean setTileSize() {
		int size = getTileSize();
		customWater = hasResource("/custom_water_still.png") || hasResource("/custom_water_flowing.png")
			|| hasResource("/anim/custom_water_still.png") || hasResource("/anim/custom_water_flowing.png");
		customLava = hasResource("/custom_lava_still.png") || hasResource("/custom_lava_flowing.png")
				|| hasResource("/anim/custom_lava_still.png") || hasResource("/anim/custom_lava_flowing.png");
		if (size == TileSize.int_size) {
			//unchanged
			return false;
		} else {
			TileSize.setTileSize(size);
			return true;
		}
	}

	public static void setFontRenderer() {
		//MCPatcherUtils.log("setFontRenderer()", new Object[0]);
		Minecraft game = SpoutClient.getHandle();
		game.fontRenderer.initialize(game.gameSettings, "/font/default.png", game.renderEngine);
		if (game.standardGalacticFontRenderer != game.fontRenderer) {
			game.standardGalacticFontRenderer.initialize(game.gameSettings, "/font/alternate.png", game.renderEngine);
		}

	}

	public static void registerTextureFX(List var0, TextureFX var1) {
		TextureFX var2 = refreshTextureFX(var1);
		if (var2 != null) {
			var0.add(var2);
			var2.onTick();
		}

	}

	private static TextureFX refreshTextureFX(TextureFX var0) {
		if (!(var0 instanceof TextureCompassFX) && !(var0 instanceof TextureWatchFX) && !(var0 instanceof TextureLavaFX) && !(var0 instanceof TextureLavaFlowFX) && !(var0 instanceof TextureWaterFX) && !(var0 instanceof TextureWaterFlowFX) && !(var0 instanceof TextureFlamesFX) && !(var0 instanceof TexturePortalFX)) {
			System.out.printf("attempting to refresh unknown animation %s\n", new Object[]{var0.getClass().getName()});
			Minecraft var1 = SpoutClient.getHandle();
			Class var2 = var0.getClass();

			for (int var3 = 0; var3 < 3; ++var3) {
				try {
					Constructor var4;
					switch(var3) {
					case 0:
						var4 = var2.getConstructor(new Class[]{Minecraft.class, Integer.TYPE});
						return (TextureFX)var4.newInstance(new Object[]{var1, Integer.valueOf(TileSize.int_size)});
					case 1:
						var4 = var2.getConstructor(new Class[]{Minecraft.class});
						return (TextureFX)var4.newInstance(new Object[]{var1});
					case 2:
						var4 = var2.getConstructor(new Class[0]);
						return (TextureFX)var4.newInstance(new Object[0]);
					}
				} catch (NoSuchMethodException var6) {
					;
				} catch (IllegalAccessException var7) {
					;
				} catch (Exception var8) {
					var8.printStackTrace();
				}
			}

			if (var0.imageData.length != TileSize.int_numBytes) {
				//MCPatcherUtils.log("resizing %s buffer from %d to %d bytes", new Object[]{var2.getName(), Integer.valueOf(var0.imageData.length), Integer.valueOf(TileSize.int_numBytes)});
				var0.imageData = new byte[TileSize.int_numBytes];
			}

			return var0;
		} else {
			return null;
		}
	}

	public static void refreshTextureFX(List<TextureFX> textureFXs) {
		//MCPatcherUtils.log("refreshTextureFX()", new Object[0]);
		ArrayList var1 = new ArrayList();
		Iterator var2 = textureFXs.iterator();

		while (var2.hasNext()) {
			TextureFX var3 = (TextureFX)var2.next();
			TextureFX var4 = refreshTextureFX(var3);
			if (var4 != null) {
				var1.add(var4);
			}
		}

		textureFXs.clear();
		CustomAnimation.clear();
		Minecraft game = SpoutClient.getHandle();
		textureFXs.add(new TextureCompassFX(game));
		textureFXs.add(new TextureWatchFX(game));
		TexturePackBase var17 = getSelectedTexturePack();
		boolean var18 = var17 == null || var17 instanceof TexturePackDefault;
		if (!var18 && customLava) {
			CustomAnimation.addStripOrTile("/terrain.png", "lava_still", 237, 1, -1, -1);
			CustomAnimation.addStripOrTile("/terrain.png", "lava_flowing", 238, 2, 3, 6);
		} else if (animatedLava) {
			textureFXs.add(new TextureLavaFX());
			textureFXs.add(new TextureLavaFlowFX());
		}

		if (!var18 && customWater) {
			CustomAnimation.addStripOrTile("/terrain.png", "water_still", 205, 1, -1, -1);
			CustomAnimation.addStripOrTile("/terrain.png", "water_flowing", 206, 2, 0, 0);
		} else if (animatedWater) {
			textureFXs.add(new TextureWaterFX());
			textureFXs.add(new TextureWaterFlowFX());
		}

		if (!var18 && customFire && hasResource("/anim/custom_fire_e_w.png") && hasResource("/anim/custom_fire_n_s.png")) {
			CustomAnimation.addStrip("/terrain.png", "fire_n_s", 47, 1);
			CustomAnimation.addStrip("/terrain.png", "fire_e_w", 31, 1);
		} else if (animatedFire) {
			textureFXs.add(new TextureFlamesFX(0));
			textureFXs.add(new TextureFlamesFX(1));
		}

		if (!var18 && customPortal && hasResource("/anim/custom_portal.png")) {
			CustomAnimation.addStrip("/terrain.png", "portal", 14, 1);
		} else if (animatedPortal) {
			textureFXs.add(new TexturePortalFX());
		}

		if (customOther) {
			addOtherTextureFX("/terrain.png", "terrain");
			addOtherTextureFX("/gui/items.png", "item");
			if (var17 instanceof TexturePackCustom) {
				TexturePackCustom var5 = (TexturePackCustom)var17;
				Iterator var6 = Collections.list(var5.texturePackZipFile.entries()).iterator();

				while (var6.hasNext()) {
					ZipEntry var7 = (ZipEntry)var6.next();
					String var8 = "/" + var7.getName();
					if (var8.startsWith("/anim/") && var8.endsWith(".properties") && !isCustomTerrainItemResource(var8)) {
						InputStream var9 = null;

						try {
							var9 = var5.texturePackZipFile.getInputStream(var7);
							Properties var10 = new Properties();
							var10.load(var9);
							CustomAnimation.addStrip(var10);
						} catch (IOException var14) {
							var14.printStackTrace();
						} finally {
							MCPatcherUtils.close((Closeable)var9);
						}
					}
				}
			}
		}

		Iterator var12 = var1.iterator();

		TextureFX var13;
		while (var12.hasNext()) {
			var13 = (TextureFX)var12.next();
			textureFXs.add(var13);
		}

		var12 = textureFXs.iterator();

		while (var12.hasNext()) {
			var13 = (TextureFX)var12.next();
			var13.onTick();
		}

		CustomAnimation.updateAll();
		if (ColorizerWater.waterBuffer != ColorizerFoliage.foliageBuffer) {
			refreshColorizer(ColorizerWater.waterBuffer, "/misc/watercolor.png");
		}

		refreshColorizer(ColorizerGrass.grassBuffer, "/misc/grasscolor.png");
		refreshColorizer(ColorizerFoliage.foliageBuffer, "/misc/foliagecolor.png");
		System.gc();
	}

	private static void addOtherTextureFX(String var0, String var1) {
		for (int var2 = 0; var2 < 256; ++var2) {
			String var3 = "/anim/custom_" + var1 + "_" + var2 + ".png";
			if (hasResource(var3)) {
				CustomAnimation.addStrip(var0, var1 + "_" + var2, var2, 1);
			}
		}
	}

	public static TexturePackBase getSelectedTexturePack() {
		if (SpoutClient.getHandle().texturePackList == null) {
			return null;
		}
		return SpoutClient.getHandle().texturePackList.selectedTexturePack;
	}

	public static String getTexturePackName(TexturePackBase texturePack) {
		return texturePack == null?"Default":texturePack.texturePackFileName;
	}

	public static ByteBuffer getByteBuffer(ByteBuffer buffer, byte[] data) {
		buffer.clear();
		int var2 = buffer.capacity();
		int var3 = data.length;
		if (var3 > var2 || reclaimGLMemory && var2 >= 4 * var3) {
			buffer = GLAllocation.createDirectByteBuffer(var3);
		}

		buffer.put(data);
		buffer.position(0).limit(var3);
		TileSize.int_glBufferSize = var3;
		return buffer;
	}

	public static boolean isRequiredResource(String texture) {
		return !texture.startsWith("/custom_") && !texture.startsWith("/anim/custom_") && !texture.equals("/terrain_nh.png") && !texture.equals("/terrain_s.png") && !texture.matches("^/font/.*\\.properties$") && !texture.matches("^/mob/.*\\d+.png$");
	}

	static boolean isCustomTerrainItemResource(String var0) {
		var0 = var0.replaceFirst("^/anim", "").replaceFirst("\\.(png|properties)$", "");
		return var0.equals("/custom_lava_still") || var0.equals("/custom_lava_flowing") || var0.equals("/custom_water_still") || var0.equals("/custom_water_flowing") || var0.equals("/custom_fire_n_s") || var0.equals("/custom_fire_e_w") || var0.equals("/custom_portal") || var0.matches("^/custom_(terrain|item)_\\d+$");
	}

	public static InputStream getResourceAsStream(TexturePackBase texturePack, String texture) {
		boolean wasLocked = SpoutClient.isSandboxed();
		SpoutClient.disableSandbox();
		if (texturePack != null) texturePack.func_6482_a();
		try {
			InputStream var2 = null;
			if (texturePack != null) {
				try {
					var2 = texturePack.getResourceAsStream(texture);
				} catch (Exception var4) {
					var4.printStackTrace();
				}
			}

			if (var2 == null) {
				var2 = TextureUtils.class.getResourceAsStream(texture);
			}

			if (var2 == null && texture.startsWith("/anim/custom_")) {
				var2 = getResourceAsStream(texturePack, texture.substring(5));
			}

			if (var2 == null && isRequiredResource(texture)) {
				var2 = Thread.currentThread().getContextClassLoader().getResourceAsStream(texture);
			}

			return var2;
		}
		finally {
			if (wasLocked) {
				SpoutClient.enableSandbox();
			}
		}
	}

	public static InputStream getResourceAsStream(String texture) {
		return getResourceAsStream(getSelectedTexturePack(), texture);
	}

	public static BufferedImage getResourceAsBufferedImage(TexturePackBase texturePack, String texture) throws IOException {
		boolean wasLocked = SpoutClient.isSandboxed();
		SpoutClient.disableSandbox();
		try {
			BufferedImage image = null;
			boolean found = false;
			if (texturePack == lastTexturePack) {
				image = (BufferedImage)cache.get(texture);
				if (image != null) {
					found = true;
				}
			}

			if (image == null) {
				InputStream stream = getResourceAsStream(texturePack, texture);
				if (stream != null) {
					try {
						image = ImageIO.read(stream);
					}
					finally {
						if (stream != null) {
							try {
								stream.close();
							} catch (Exception e) { }
						}
					}
				}
			}

			if (image == null) {
				//Search local files (downloaded texture)
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
				throw new IOException(texture + " image is null");
			} else {
				if (texturePack != lastTexturePack) {
					cache.clear();
				}

				if (!found) {
					Integer size = 1;
					if (!texture.matches("^(/anim)?/custom_.*\\.png$")) {
						size = expectedColumns.get(texture);
					}

					if (size != null && image.getWidth() != size * TileSize.int_size) {
						image = resizeImage(image, size * TileSize.int_size);
					}

					lastTexturePack = texturePack;
					cache.put(texture, image);

				if (texture.matches("^/mob/.*_eyes\\d*\\.png$")) {
					for (int pixelX = 0; pixelX < image.getWidth(); ++pixelX) {
						for (int pixelY = 0; pixelY < image.getHeight(); ++pixelY) {
							int color = image.getRGB(pixelX, pixelY);
							if ((color & -16777216) == 0 && color != 0) {
								image.setRGB(pixelX, pixelY, 0);
							}
						}
					}
				}
			}
				return image;
			}
		}
		finally {
			if (wasLocked) {
				SpoutClient.enableSandbox();
			}
		}
	}

	public static BufferedImage getResourceAsBufferedImage(String var0) throws IOException {
		return getResourceAsBufferedImage(getSelectedTexturePack(), var0);
	}

	public static BufferedImage getResourceAsBufferedImage(Object var0, Object var1, String var2) throws IOException {
		return getResourceAsBufferedImage(var2);
	}

	public static int getTileSize(TexturePackBase texturePack) {
		int max = 0;
		Iterator<Entry<String, Integer>> i = expectedColumns.entrySet().iterator();

		while (i.hasNext()) {
			Entry<String, Integer> next = i.next();
			InputStream stream = null;

			try {
				stream = getResourceAsStream(texturePack, next.getKey());
				if (stream != null) {
					BufferedImage image = ImageIO.read(stream);
					int imageSize = image.getWidth() / next.getValue();
					max = Math.max(max, imageSize);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (Exception e) { }
				}
			}
		}

		return max > 0? max : 16;
	}

	public static int getTileSize() {
		return getTileSize(getSelectedTexturePack());
	}

	public static boolean hasResource(TexturePackBase texturePack, String texture) {
		InputStream stream = null;
		try {
			stream = getResourceAsStream(texturePack, texture);
			return stream != null;
		}
		finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) { }
			}
		}
	}

	public static boolean hasResource(String texture) {
		return hasResource(getSelectedTexturePack(), texture);
	}

	static BufferedImage resizeImage(BufferedImage image, int width) {
		int height = image.getHeight() * width / image.getWidth();
		BufferedImage resizedImage = new BufferedImage(width, height, 2);
		Graphics2D grahics = resizedImage.createGraphics();
		grahics.drawImage(image, 0, 0, width, height, (ImageObserver)null);
		return resizedImage;
	}

	private static void refreshColorizer(int[] data, String texture) {
		try {
			BufferedImage image = getResourceAsBufferedImage(texture);
			if (image != null) {
				image.getRGB(0, 0, 256, 256, data, 0, 256);
			}
		} catch (IOException var3) {
			var3.printStackTrace();
		}
	}

	public static void openTexturePackFile(TexturePackCustom var0) {
		if (var0.texturePackZipFile != null) {
			FileInputStream var1 = null;
			FileOutputStream var2 = null;
			ZipFile var3 = null;

			try {
				var0.lastModified = var0.texturePackFile.lastModified();
				var0.tmpFile = File.createTempFile("tmpmc", ".zip");
				var0.tmpFile.deleteOnExit();
				if (var0.texturePackZipFile != null) {
					try {
						var0.texturePackZipFile.close();
					} catch (IOException e) {}
				}
				var1 = new FileInputStream(var0.texturePackFile);
				var2 = new FileOutputStream(var0.tmpFile);
				byte[] var4 = new byte[65536];

				while (true) {
					int var5 = var1.read(var4);
					if (var5 <= 0) {
						if (var1 != null) {
							try {
								var1.close();
							} catch (IOException e) {}
						}
						if (var2 != null) {
							try {
								var2.close();
							} catch (IOException e) {}
						}
						var3 = new ZipFile(var0.tmpFile);
						var0.origZip = var0.texturePackZipFile;
						var0.texturePackZipFile = var3;
						var3 = null;
						break;
					}

					var2.write(var4, 0, var5);
				}
			} catch (IOException var9) {
				var9.printStackTrace();
			}
			finally {
				if (var1 != null) {
					try {
						var1.close();
					} catch (IOException e) {}
				}
				if (var2 != null) {
					try {
						var2.close();
					} catch (IOException e) {}
				}
				if (var3 != null) {
					try {
						var3.close();
					} catch (IOException e) {}
				}
			}
		}
	}

	public static void closeTexturePackFile(TexturePackCustom var0) {
		if (var0.origZip != null) {
			if (var0 != null) {
				try {
					var0.origZip.close();
				} catch (IOException e) {}
			}
			var0.texturePackZipFile = var0.origZip;
			var0.origZip = null;
			var0.tmpFile.delete();
			var0.tmpFile = null;
		}
	}

	static {
		expectedColumns.put("/terrain.png", 16);
		expectedColumns.put("/gui/items.png", 16);
		expectedColumns.put("/misc/dial.png", 1);
		expectedColumns.put("/custom_lava_still.png", 1);
		expectedColumns.put("/custom_lava_flowing.png", 1);
		expectedColumns.put("/custom_water_still.png", 1);
		expectedColumns.put("/custom_water_flowing.png", 1);
		expectedColumns.put("/custom_fire_n_s.png", 1);
		expectedColumns.put("/custom_fire_e_w.png", 1);
		expectedColumns.put("/custom_portal.png", 1);

		expectedColumns = Collections.unmodifiableMap(expectedColumns);
	}
}
