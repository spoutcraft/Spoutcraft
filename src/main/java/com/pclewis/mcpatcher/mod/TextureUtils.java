package com.pclewis.mcpatcher.mod;

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
import net.minecraft.src.TexturePackCustom;
import net.minecraft.src.TexturePackDefault;
import net.minecraft.src.TexturePackFolder;
import net.minecraft.src.TexturePackImplementation;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.TexturePortalFX;
import net.minecraft.src.TextureWatchFX;
import net.minecraft.src.TextureWaterFX;
import net.minecraft.src.TextureWaterFlowFX;

import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import com.pclewis.mcpatcher.mod.TextureUtils$1;

public class TextureUtils {
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
	private static HashMap expectedColumns = new HashMap();
	private static boolean useTextureCache = false;
	private static boolean reclaimGLMemory = false;
	private static boolean autoRefreshTextures = false;
	private static TexturePackImplementation lastTexturePack = null;
	private static HashMap cache = new HashMap();
	private static int textureRefreshCount;
	private static final String ALL_ITEMS = "/gui/allitems.png";
	private static final String ALL_ITEMSX = "/gui/allitemsx.png";
	public static boolean oldCreativeGui;
	private static boolean bindImageReentry;

	public static boolean setTileSize() {
		int var0 = getTileSize();
		TexturePackAPI.ChangeHandler.checkForTexturePackChange();

		if (var0 == TileSize.int_size) {
			return false;
		} else {
			TileSize.setTileSize(var0);
			return true;
		}
	}

	private static void setFontRenderer(Minecraft var0, FontRenderer var1, String var2) {
		boolean var3 = var1.unicodeFlag;
		var1.initialize(var0.gameSettings, var2, var0.renderEngine);
		var1.unicodeFlag = var3;
	}

	public static void setFontRenderer() {
		Minecraft var0 = MCPatcherUtils.getMinecraft();
		setFontRenderer(var0, var0.fontRenderer, "/font/default.png");

		if (var0.standardGalacticFontRenderer != var0.fontRenderer) {
			setFontRenderer(var0, var0.standardGalacticFontRenderer, "/font/alternate.png");
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
			Minecraft var1 = MCPatcherUtils.getMinecraft();
			Class var2 = var0.getClass();

			for (int var3 = 0; var3 < 3; ++var3) {
				try {
					Constructor var4;

					switch (var3) {
						case 0:
							var4 = var2.getConstructor(new Class[] {Minecraft.class, Integer.TYPE});
							return (TextureFX)var4.newInstance(new Object[] {var1, Integer.valueOf(TileSize.int_size)});

						case 1:
							var4 = var2.getConstructor(new Class[] {Minecraft.class});
							return (TextureFX)var4.newInstance(new Object[] {var1});

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
				var0.imageData = new byte[TileSize.int_numBytes];
			}

			return var0;
		} else {
			return null;
		}
	}

	public static void refreshTextureFX(List var0) {
		ArrayList var1 = new ArrayList();
		Iterator var2 = var0.iterator();

		while (var2.hasNext()) {
			TextureFX var3 = (TextureFX)var2.next();
			TextureFX var4 = refreshTextureFX(var3);

			if (var4 != null) {
				var1.add(var4);
			}
		}

		var0.clear();
		CustomAnimation.clear();
		Minecraft var25 = MCPatcherUtils.getMinecraft();
		var0.add(new TextureCompassFX(var25));
		var0.add(new TextureWatchFX(var25));
		TexturePackImplementation var26 = getSelectedTexturePack();
		boolean var27 = var26 == null || var26 instanceof TexturePackDefault;

		if (!var27 && customLava) {
			CustomAnimation.addStripOrTile("/terrain.png", "lava_still", 237, 1, -1, -1);
			CustomAnimation.addStripOrTile("/terrain.png", "lava_flowing", 238, 2, 3, 6);
		} else if (animatedLava) {
			var0.add(new TextureLavaFX());
			var0.add(new TextureLavaFlowFX());
		}

		if (!var27 && customWater) {
			CustomAnimation.addStripOrTile("/terrain.png", "water_still", 205, 1, -1, -1);
			CustomAnimation.addStripOrTile("/terrain.png", "water_flowing", 206, 2, 0, 0);
		} else if (animatedWater) {
			var0.add(new TextureWaterFX());
			var0.add(new TextureWaterFlowFX());
		}

		if (!var27 && customFire && hasResource("/anim/custom_fire_e_w.png") && hasResource("/anim/custom_fire_n_s.png")) {
			CustomAnimation.addStrip("/terrain.png", "fire_n_s", 47, 1);
			CustomAnimation.addStrip("/terrain.png", "fire_e_w", 31, 1);
		} else if (animatedFire) {
			var0.add(new TextureFlamesFX(0));
			var0.add(new TextureFlamesFX(1));
		}

		if (!var27 && customPortal && hasResource("/anim/custom_portal.png")) {
			CustomAnimation.addStrip("/terrain.png", "portal", 14, 1);
		} else if (animatedPortal) {
			var0.add(new TexturePortalFX());
		}

		if (customOther) {
			addOtherTextureFX("/terrain.png", "terrain");
			addOtherTextureFX("/gui/items.png", "item");

			if (!(var26 instanceof TexturePackDefault)) {
				if (var26 instanceof TexturePackCustom) {
					TexturePackCustom var5 = (TexturePackCustom)var26;
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
							} catch (IOException var23) {
								var23.printStackTrace();
							} finally {
								MCPatcherUtils.close((Closeable)var9);
							}
						}
					}
				} else if (var26 instanceof TexturePackFolder) {
					File var28 = ((TexturePackFolder)var26).getFolder();

					if (var28 != null) {
						var28 = new File(var28, "anim");

						if (var28.isDirectory()) {
							File[] var31 = var28.listFiles(new TextureUtils$1());
							int var32 = var31.length;

							for (int var33 = 0; var33 < var32; ++var33) {
								File var34 = var31[var33];
								FileInputStream var35 = null;

								try {
									var35 = new FileInputStream(var34);
									Properties var11 = new Properties();
									var11.load(var35);
									CustomAnimation.addStrip(var11);
								} catch (IOException var21) {
									var21.printStackTrace();
								} finally {
									MCPatcherUtils.close((Closeable)var35);
								}
							}
						}
					}
				}
			}
		}

		Iterator var29 = var1.iterator();
		TextureFX var30;

		while (var29.hasNext()) {
			var30 = (TextureFX)var29.next();
			var0.add(var30);
		}

		var29 = var0.iterator();

		while (var29.hasNext()) {
			var30 = (TextureFX)var29.next();
			var30.onTick();
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

	public static TexturePackImplementation getSelectedTexturePack() {
		Minecraft var0 = MCPatcherUtils.getMinecraft();
		return (TexturePackImplementation) (var0 == null ? null : (var0.texturePackList == null ? null : var0.texturePackList.getSelectedTexturePack()));
	}

	public static String getTexturePackName(TexturePackImplementation var0) {
		return var0 == null ? "Default" : var0.texturePackFileName;
	}

	public static ByteBuffer getByteBuffer(ByteBuffer var0, byte[] var1) {
		var0.clear();
		int var2 = var0.capacity();
		int var3 = var1.length;

		if (var3 > var2 || reclaimGLMemory && var2 >= 4 * var3) {
			var0 = GLAllocation.createDirectByteBuffer(var3);
		}

		var0.put(var1);
		var0.position(0).limit(var3);
		TileSize.int_glBufferSize = var3;
		return var0;
	}

	public static boolean isRequiredResource(String var0) {
		return var0.equals("/terrain.png") || var0.equals("/gui/items.png");
	}

	static boolean isCustomTerrainItemResource(String var0) {
		var0 = var0.replaceFirst("^/anim", "").replaceFirst("\\.(png|properties)$", "");
		return var0.equals("/custom_lava_still") || var0.equals("/custom_lava_flowing") || var0.equals("/custom_water_still") || var0.equals("/custom_water_flowing") || var0.equals("/custom_fire_n_s") || var0.equals("/custom_fire_e_w") || var0.equals("/custom_portal") || var0.matches("^/custom_(terrain|item)_\\d+$");
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

	public static InputStream getResourceAsStream(String var0) {
		return getResourceAsStream(getSelectedTexturePack(), var0);
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
					MCPatcherUtils.close((Closeable)var4);
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

	public static BufferedImage getResourceAsBufferedImage(String var0) throws IOException {
		return getResourceAsBufferedImage(getSelectedTexturePack(), var0);
	}

	public static BufferedImage getResourceAsBufferedImage(Object var0, String var1) throws IOException {
		return getResourceAsBufferedImage(var1);
	}

	public static BufferedImage getResourceAsBufferedImage(Object var0, Object var1, String var2) throws IOException {
		return getResourceAsBufferedImage(var2);
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
				var10.printStackTrace();
			} finally {
				MCPatcherUtils.close((Closeable)var4);
			}
		}

		return var1 > 0 ? var1 : 16;
	}

	public static int getTileSize() {
		return getTileSize(getSelectedTexturePack());
	}

	public static boolean hasResource(TexturePackImplementation var0, String var1) {
		InputStream var2 = getResourceAsStream(var0, var1);
		boolean var3 = var2 != null;
		MCPatcherUtils.close((Closeable)var2);
		return var3;
	}

	public static boolean hasResource(String var0) {
		return hasResource(getSelectedTexturePack(), var0);
	}

	static BufferedImage resizeImage(BufferedImage var0, int var1) {
		int var2 = var0.getHeight() * var1 / var0.getWidth();
		BufferedImage var3 = new BufferedImage(var1, var2, 2);
		Graphics2D var4 = var3.createGraphics();
		var4.drawImage(var0, 0, 0, var1, var2, (ImageObserver)null);
		return var3;
	}

	private static void refreshColorizer(int[] var0, String var1) {
		try {
			BufferedImage var2 = getResourceAsBufferedImage(var1);

			if (var2 != null) {
				var2.getRGB(0, 0, 256, 256, var0, 0, 256);
			}
		} catch (IOException var3) {
			var3.printStackTrace();
		}
	}

	public static void openTexturePackFile(TexturePackCustom var0) {
		if (autoRefreshTextures && var0.texturePackFileName != null) {
			FileInputStream var1 = null;
			FileOutputStream var2 = null;
			ZipFile var3 = null;

			try {
				var0.lastModified = var0.texturePackFile.lastModified();
				var0.tmpFile = File.createTempFile("tmpmc", ".zip");
				var0.tmpFile.deleteOnExit();
				MCPatcherUtils.close(var0.texturePackZipFile);
				var1 = new FileInputStream(var0.texturePackFileName);
				var2 = new FileOutputStream(var0.tmpFile);
				byte[] var4 = new byte[65536];

				while (true) {
					int var5 = var1.read(var4);

					if (var5 <= 0) {
						MCPatcherUtils.close((Closeable)var1);
						MCPatcherUtils.close((Closeable)var2);
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
			} finally {
				MCPatcherUtils.close((Closeable)var1);
				MCPatcherUtils.close((Closeable)var2);
				MCPatcherUtils.close(var3);
			}
		}
	}

	public static void closeTexturePackFile(TexturePackCustom var0) {
		if (var0.origZip != null) {
			MCPatcherUtils.close(var0.texturePackZipFile);
			var0.texturePackZipFile = var0.origZip;
			var0.origZip = null;
			var0.tmpFile.delete();
			var0.tmpFile = null;
		}
	}

	public static void checkTexturePackChange(Minecraft var0) {
		if (autoRefreshTextures && ++textureRefreshCount >= 16) {
			textureRefreshCount = 0;
			TexturePackList var1 = var0.texturePackList;

			if (var1.getSelectedTexturePack() instanceof TexturePackCustom) {
				TexturePackCustom var2 = (TexturePackCustom)var1.getSelectedTexturePack();
				long var3 = var2.texturePackFile.lastModified();

				if (var3 != var2.lastModified && var3 != 0L && var2.lastModified != 0L) {
					ZipFile var5 = null;
					label90: {
						try {
							var5 = new ZipFile(var2.texturePackFile);
							break label90;
						} catch (IOException var11) {
							;
						} finally {
							MCPatcherUtils.close(var5);
						}

						return;
					}
					var2.closeTexturePackFile();
					var1.createTexturePackDirs();
					Iterator var6 = var1.availableTexturePacks().iterator();

					while (var6.hasNext()) {
						TexturePackImplementation var7 = (TexturePackImplementation)var6.next();

						if (var7 instanceof TexturePackCustom) {
							TexturePackCustom var8 = (TexturePackCustom)var7;

							if (var8.texturePackFileName.equals(var2.texturePackFileName)) {
								var1.a(var8);
								var0.renderEngine.setTileSize(var0);
								return;
							}
						}
					}
					var1.setTexturePack(var1.getDefaultTexturePack());
					//var1.a((TexturePackImplementation) TexturePackList.);
					var0.renderEngine.setTileSize(var0);
				}
			}
		}
	}

	public static boolean bindImageBegin() {
		if (bindImageReentry) {
			return false;
		} else {
			bindImageReentry = true;
			return true;
		}
	}

	public static void bindImageEnd() {
		bindImageReentry = false;
	}

	static {
		expectedColumns.put("/terrain.png", Integer.valueOf(16));
		expectedColumns.put("/gui/items.png", Integer.valueOf(16));
		expectedColumns.put("/misc/dial.png", Integer.valueOf(1));
	}
}
