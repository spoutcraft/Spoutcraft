package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCPatcherUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TextureUtils {
	private static boolean animatedFire;
	private static boolean animatedLava;
	private static boolean animatedWater;
	private static boolean animatedPortal;
	private static boolean customFire;
	private static boolean customLava;
	private static boolean customWater;
	private static boolean customPortal;
	private static boolean customOther;

	public static final int LAVA_STILL_TEXTURE_INDEX = 14 * 16 + 13;  // Block.lavaStill.blockIndexInTexture
	public static final int LAVA_FLOWING_TEXTURE_INDEX = LAVA_STILL_TEXTURE_INDEX + 1; // Block.lavaMoving.blockIndexInTexture
	public static final int WATER_STILL_TEXTURE_INDEX = 12 * 16 + 13; // Block.waterStill.blockIndexInTexture
	public static final int WATER_FLOWING_TEXTURE_INDEX = WATER_STILL_TEXTURE_INDEX + 1; // Block.waterMoving.blockIndexInTexture
	public static final int FIRE_E_W_TEXTURE_INDEX = 1 * 16 + 15; // Block.fire.blockIndexInTexture;
	public static final int FIRE_N_S_TEXTURE_INDEX = FIRE_E_W_TEXTURE_INDEX + 16;
	public static final int PORTAL_TEXTURE_INDEX = 0 * 16 + 14; // Block.portal.blockIndexInTexture

	private static HashMap<String, Integer> expectedColumns = new HashMap<String, Integer>();

	private static boolean useTextureCache;
	private static boolean reclaimGLMemory;
	private static boolean autoRefreshTextures;
	private static TexturePackBase lastTexturePack = null;
	private static HashMap<String, BufferedImage> cache = new HashMap<String, BufferedImage>();

	private static int textureRefreshCount;

	static {
		animatedFire = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "animatedFire", true);
		animatedLava = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "animatedLava", true);
		animatedWater = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "animatedWater", true);
		animatedPortal = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "animatedPortal", true);
		customFire = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "customFire", true);
		customLava = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "customLava", true);
		customWater = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "customWater", true);
		customPortal = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "customPortal", true);
		customOther = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "customOther", true);

		useTextureCache = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "useTextureCache", false);
		reclaimGLMemory = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "reclaimGLMemory", false);
		autoRefreshTextures = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "autoRefreshTextures", false);

		expectedColumns.put("/terrain.png", 16);
		expectedColumns.put("/gui/items.png", 16);
		expectedColumns.put("/misc/dial.png", 1);
	}

	public static boolean setTileSize() {
		MCPatcherUtils.log("\nchanging skin to %s", getTexturePackName(getSelectedTexturePack()));
		int size = getTileSize();
		if (size == TileSize.int_size) {
			MCPatcherUtils.log("tile size %d unchanged", size);
			return false;
		} else {
			MCPatcherUtils.log("setting tile size to %d (was %d)", size, TileSize.int_size);
			TileSize.setTileSize(size);
			return true;
		}
	}

	private static void setFontRenderer(Minecraft minecraft, FontRenderer fontRenderer, String filename) {
		boolean saveUnicode = fontRenderer.unicodeFlag;
		fontRenderer.initialize(minecraft.gameSettings, filename, minecraft.renderEngine);
		fontRenderer.unicodeFlag = saveUnicode;
	}

	public static void setFontRenderer() {
		MCPatcherUtils.log("setFontRenderer()");
		Minecraft minecraft = MCPatcherUtils.getMinecraft();
		setFontRenderer(minecraft, minecraft.fontRenderer, "/font/default.png");
		if (minecraft.standardGalacticFontRenderer != minecraft.fontRenderer) {
			setFontRenderer(minecraft, minecraft.standardGalacticFontRenderer, "/font/alternate.png");
		}
	}

	public static void registerTextureFX(java.util.List<TextureFX> textureList, TextureFX textureFX) {
		TextureFX fx = refreshTextureFX(textureFX);
		if (fx != null) {
			MCPatcherUtils.log("registering new TextureFX class %s", textureFX.getClass().getName());
			textureList.add(fx);
			fx.onTick();
		}
	}

	private static TextureFX refreshTextureFX(TextureFX textureFX) {
		if (textureFX instanceof TextureCompassFX ||
			textureFX instanceof TextureWatchFX ||
			textureFX instanceof TextureLavaFX ||
			textureFX instanceof TextureLavaFlowFX ||
			textureFX instanceof TextureWaterFX ||
			textureFX instanceof TextureWaterFlowFX ||
			textureFX instanceof TextureFlamesFX ||
			textureFX instanceof TexturePortalFX) {
			return null;
		}
		System.out.printf("attempting to refresh unknown animation %s\n", textureFX.getClass().getName());
		Minecraft minecraft = MCPatcherUtils.getMinecraft();
		Class<? extends TextureFX> textureFXClass = textureFX.getClass();
		for (int i = 0; i < 3; i++) {
			Constructor<? extends TextureFX> constructor;
			try {
				switch (i) {
					case 0:
						constructor = textureFXClass.getConstructor(Minecraft.class, Integer.TYPE);
						return constructor.newInstance(minecraft, TileSize.int_size);

					case 1:
						constructor = textureFXClass.getConstructor(Minecraft.class);
						return constructor.newInstance(minecraft);

					case 2:
						constructor = textureFXClass.getConstructor();
						return constructor.newInstance();

					default:
						break;
				}
			} catch (NoSuchMethodException e) {
			} catch (IllegalAccessException e) {
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (textureFX.imageData.length != TileSize.int_numBytes) {
			MCPatcherUtils.log("resizing %s buffer from %d to %d bytes",
				textureFXClass.getName(), textureFX.imageData.length, TileSize.int_numBytes
			);
			textureFX.imageData = new byte[TileSize.int_numBytes];
		}
		return textureFX;
	}

	public static void refreshTextureFX(java.util.List<TextureFX> textureList) {
		MCPatcherUtils.log("refreshTextureFX()");

		ArrayList<TextureFX> savedTextureFX = new ArrayList<TextureFX>();
		for (TextureFX t : textureList) {
			TextureFX fx = refreshTextureFX(t);
			if (fx != null) {
				savedTextureFX.add(fx);
			}
		}
		textureList.clear();
		CustomAnimation.clear();

		Minecraft minecraft = MCPatcherUtils.getMinecraft();
		textureList.add(new TextureCompassFX(minecraft));
		textureList.add(new TextureWatchFX(minecraft));

		TexturePackBase selectedTexturePack = getSelectedTexturePack();
		boolean isDefault = (selectedTexturePack == null || selectedTexturePack instanceof TexturePackDefault);

		if (!isDefault && customLava) {
			CustomAnimation.addStripOrTile("/terrain.png", "lava_still", LAVA_STILL_TEXTURE_INDEX, 1, -1, -1);
			CustomAnimation.addStripOrTile("/terrain.png", "lava_flowing", LAVA_FLOWING_TEXTURE_INDEX, 2, 3, 6);
		} else if (animatedLava) {
			textureList.add(new TextureLavaFX());
			textureList.add(new TextureLavaFlowFX());
		}

		if (!isDefault && customWater) {
			CustomAnimation.addStripOrTile("/terrain.png", "water_still", WATER_STILL_TEXTURE_INDEX, 1, -1, -1);
			CustomAnimation.addStripOrTile("/terrain.png", "water_flowing", WATER_FLOWING_TEXTURE_INDEX, 2, 0, 0);
		} else if (animatedWater) {
			textureList.add(new TextureWaterFX());
			textureList.add(new TextureWaterFlowFX());
		}

		if (!isDefault && customFire && hasResource("/anim/custom_fire_e_w.png") && hasResource("/anim/custom_fire_n_s.png")) {
			CustomAnimation.addStrip("/terrain.png", "fire_n_s", FIRE_N_S_TEXTURE_INDEX, 1);
			CustomAnimation.addStrip("/terrain.png", "fire_e_w", FIRE_E_W_TEXTURE_INDEX, 1);
		} else if (animatedFire) {
			textureList.add(new TextureFlamesFX(0));
			textureList.add(new TextureFlamesFX(1));
		}

		if (!isDefault && customPortal && hasResource("/anim/custom_portal.png")) {
			CustomAnimation.addStrip("/terrain.png", "portal", PORTAL_TEXTURE_INDEX, 1);
		} else if (animatedPortal) {
			textureList.add(new TexturePortalFX());
		}

		if (customOther) {
			addOtherTextureFX("/terrain.png", "terrain");
			addOtherTextureFX("/gui/items.png", "item");
			if (selectedTexturePack instanceof TexturePackCustom) {
				TexturePackCustom custom = (TexturePackCustom) selectedTexturePack;
				for (ZipEntry entry : Collections.list(custom.texturePackZipFile.entries())) {
					String name = "/" + entry.getName();
					if (name.startsWith("/anim/") && name.endsWith(".properties") && !isCustomTerrainItemResource(name)) {
						InputStream inputStream = null;
						try {
							inputStream = custom.texturePackZipFile.getInputStream(entry);
							Properties properties = new Properties();
							properties.load(inputStream);
							CustomAnimation.addStrip(properties);
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							MCPatcherUtils.close(inputStream);
						}
					}
				}
			}
		}

		for (TextureFX t : savedTextureFX) {
			textureList.add(t);
		}

		for (TextureFX t : textureList) {
			t.onTick();
		}

		CustomAnimation.updateAll();

		if (ColorizerWater.waterBuffer != ColorizerFoliage.foliageBuffer) {
			refreshColorizer(ColorizerWater.waterBuffer, "/misc/watercolor.png");
		}
		refreshColorizer(ColorizerGrass.grassBuffer, "/misc/grasscolor.png");
		refreshColorizer(ColorizerFoliage.foliageBuffer, "/misc/foliagecolor.png");

		System.gc();
	}
	
	private static void addOtherTextureFX(String textureName, String imageName) {
		for (int tileNum = 0; tileNum < 256; tileNum++) {
			String resource = "/anim/custom_" + imageName + "_" + tileNum + ".png";
			if (hasResource(resource)) {
				CustomAnimation.addStrip(textureName, imageName + "_" + tileNum, tileNum, 1);
			}
		}
	}

	public static TexturePackBase getSelectedTexturePack() {
		Minecraft minecraft = MCPatcherUtils.getMinecraft();
		return minecraft == null ? null :
			minecraft.texturePackList == null ? null :
				minecraft.texturePackList.selectedTexturePack;
	}

	public static String getTexturePackName(TexturePackBase texturePack) {
		return texturePack == null ? "Default" : texturePack.texturePackFileName;
	}

	public static ByteBuffer getByteBuffer(ByteBuffer buffer, byte[] data) {
		buffer.clear();
		final int have = buffer.capacity();
		final int needed = data.length;
		if (needed > have || (reclaimGLMemory && have >= 4 * needed)) {
			//MCPatcherUtils.log("resizing gl buffer from 0x%x to 0x%x", have, needed);
			buffer = GLAllocation.createDirectByteBuffer(needed);
		}
		buffer.put(data);
		buffer.position(0).limit(needed);
		TileSize.int_glBufferSize = needed;
		return buffer;
	}

	public static boolean isRequiredResource(String resource) {
		return !(resource.startsWith("/custom_") ||
			resource.startsWith("/anim/custom_") ||
			resource.equals("/terrain_nh.png") ||
			resource.equals("/terrain_s.png") ||
			resource.matches("^/font/.*\\.properties$") ||
			resource.matches("^/mob/.*\\d+.png$")
		);
	}
	
	static boolean isCustomTerrainItemResource(String resource) {
		resource = resource.replaceFirst("^/anim", "").replaceFirst("\\.(png|properties)$", "");
		return resource.equals("/custom_lava_still") ||
			resource.equals("/custom_lava_flowing") ||
			resource.equals("/custom_water_still") ||
			resource.equals("/custom_water_flowing") ||
			resource.equals("/custom_fire_n_s") ||
			resource.equals("/custom_fire_e_w") ||
			resource.equals("/custom_portal") ||
			resource.matches("^/custom_(terrain|item)_\\d+$");
	}

	public static InputStream getResourceAsStream(TexturePackBase texturePack, String resource) {
		InputStream is = null;
		if (texturePack != null) {
			try {
				is = texturePack.getResourceAsStream(resource);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (is == null) {
			is = TextureUtils.class.getResourceAsStream(resource);
		}
		if (is == null && resource.startsWith("/anim/custom_")) {
			is = getResourceAsStream(texturePack, resource.substring(5));
		}
		if (is == null && isRequiredResource(resource)) {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			MCPatcherUtils.warn("falling back on thread class loader for %s: %s",
				resource, (is == null ? "failed" : "success")
			);
		}
		return is;
	}

	public static InputStream getResourceAsStream(String resource) {
		return getResourceAsStream(getSelectedTexturePack(), resource);
	}

	public static BufferedImage getResourceAsBufferedImage(TexturePackBase texturePack, String resource) throws IOException {
		BufferedImage image = null;
		boolean cached = false;

		if (useTextureCache && texturePack == lastTexturePack) {
			image = cache.get(resource);
			if (image != null) {
				cached = true;
			}
		}

		if (image == null) {
			InputStream is = getResourceAsStream(texturePack, resource);
			if (is != null) {
				try {
					image = ImageIO.read(is);
				} finally {
					MCPatcherUtils.close(is);
				}
			}
		}

		if (image == null) {
			if (isRequiredResource(resource)) {
				throw new IOException(resource + " image is null");
			} else {
				return null;
			}
		}

		if (useTextureCache && !cached && texturePack != lastTexturePack) {
			MCPatcherUtils.log("clearing texture cache (%d items)", cache.size());
			cache.clear();
		}
		MCPatcherUtils.log("opened %s %dx%d from %s",
			resource, image.getWidth(), image.getHeight(), (cached ? "cache" : getTexturePackName(texturePack))
		);
		if (!cached) {
			Integer i;
			if (isCustomTerrainItemResource(resource)) {
				i = 1;
			} else {
				i = expectedColumns.get(resource);
			}
			if (i != null && image.getWidth() != i * TileSize.int_size) {
				image = resizeImage(image, i * TileSize.int_size);
			}
			if (useTextureCache) {
				lastTexturePack = texturePack;
				cache.put(resource, image);
			}
			if (resource.matches("^/mob/.*_eyes\\d*\\.png$")) {
				int p = 0;
				for (int x = 0; x < image.getWidth(); x++) {
					for (int y = 0; y < image.getHeight(); y++) {
						int argb = image.getRGB(x, y);
						if ((argb & 0xff000000) == 0 && argb != 0) {
							image.setRGB(x, y, 0);
							p++;
						}
					}
				}
				if (p > 0) {
					MCPatcherUtils.log("  fixed %d transparent pixels", p, resource);
				}
			}
		}

		return image;
	}

	public static BufferedImage getResourceAsBufferedImage(String resource) throws IOException {
		return getResourceAsBufferedImage(getSelectedTexturePack(), resource);
	}

	public static BufferedImage getResourceAsBufferedImage(Object o1, Object o2, String resource) throws IOException {
		return getResourceAsBufferedImage(resource);
	}

	public static int getTileSize(TexturePackBase texturePack) {
		int size = 0;
		for (Map.Entry<String, Integer> entry : expectedColumns.entrySet()) {
			InputStream is = null;
			try {
				is = getResourceAsStream(texturePack, entry.getKey());
				if (is != null) {
					BufferedImage bi = ImageIO.read(is);
					int newSize = bi.getWidth() / entry.getValue();
					MCPatcherUtils.log("  %s tile size is %d", entry.getKey(), newSize);
					size = Math.max(size, newSize);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				MCPatcherUtils.close(is);
			}
		}
		return size > 0 ? size : 16;
	}

	public static int getTileSize() {
		return getTileSize(getSelectedTexturePack());
	}

	public static boolean hasResource(TexturePackBase texturePack, String resource) {
		InputStream is = getResourceAsStream(texturePack, resource);
		boolean has = (is != null);
		MCPatcherUtils.close(is);
		return has;
	}

	public static boolean hasResource(String s) {
		return hasResource(getSelectedTexturePack(), s);
	}

	static BufferedImage resizeImage(BufferedImage image, int width) {
		int height = image.getHeight() * width / image.getWidth();
		MCPatcherUtils.log("  resizing to %dx%d", width, height);
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = newImage.createGraphics();
		graphics2D.drawImage(image, 0, 0, width, height, null);
		return newImage;
	}

	private static void refreshColorizer(int[] colorBuffer, String resource) {
		try {
			BufferedImage bi = getResourceAsBufferedImage(resource);
			if (bi != null) {
				bi.getRGB(0, 0, 256, 256, colorBuffer, 0, 256);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void openTexturePackFile(TexturePackCustom pack) {
		if (!autoRefreshTextures || pack.texturePackZipFile == null) {
			return;
		}
		InputStream input = null;
		OutputStream output = null;
		ZipFile newZipFile = null;
		try {
			pack.lastModified = pack.texturePackFile.lastModified();
			pack.tmpFile = File.createTempFile("tmpmc", ".zip");
			pack.tmpFile.deleteOnExit();
			MCPatcherUtils.close(pack.texturePackZipFile);
			input = new FileInputStream(pack.texturePackFile);
			output = new FileOutputStream(pack.tmpFile);
			byte[] buffer = new byte[65536];
			while (true) {
				int nread = input.read(buffer);
				if (nread <= 0) {
					break;
				}
				output.write(buffer, 0, nread);
			}
			MCPatcherUtils.close(input);
			MCPatcherUtils.close(output);
			newZipFile = new ZipFile(pack.tmpFile);
			pack.origZip = pack.texturePackZipFile;
			pack.texturePackZipFile = newZipFile;
			newZipFile = null;
			MCPatcherUtils.log("copied %s to %s, lastModified = %d", pack.texturePackFile.getPath(), pack.tmpFile.getPath(), pack.lastModified);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			MCPatcherUtils.close(input);
			MCPatcherUtils.close(output);
			MCPatcherUtils.close(newZipFile);
		}
	}

	public static void closeTexturePackFile(TexturePackCustom pack) {
		if (pack.origZip != null) {
			MCPatcherUtils.close(pack.texturePackZipFile);
			pack.texturePackZipFile = pack.origZip;
			pack.origZip = null;
			pack.tmpFile.delete();
			MCPatcherUtils.log("deleted %s", pack.tmpFile.getPath());
			pack.tmpFile = null;
		}
	}

	public static void checkTexturePackChange(Minecraft minecraft) {
		if (!autoRefreshTextures || ++textureRefreshCount < 16) {
			return;
		}
		textureRefreshCount = 0;
		TexturePackList list = minecraft.texturePackList;
		if (!(list.selectedTexturePack instanceof TexturePackCustom)) {
			return;
		}
		TexturePackCustom pack = (TexturePackCustom) list.selectedTexturePack;
		long lastModified = pack.texturePackFile.lastModified();
		if (lastModified == pack.lastModified || lastModified == 0 || pack.lastModified == 0) {
			return;
		}
		MCPatcherUtils.log("%s lastModified changed from %d to %d", pack.texturePackFile.getPath(), pack.lastModified, lastModified);
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(pack.texturePackFile);
		} catch (IOException e) {
			// file is still being written
			return;
		} finally {
			MCPatcherUtils.close(zipFile);
		}
		pack.closeTexturePackFile();
		list.updateAvaliableTexturePacks();
		for (TexturePackBase tp : list.availableTexturePacks()) {
			if (!(tp instanceof TexturePackCustom)) {
				continue;
			}
			TexturePackCustom tpc = (TexturePackCustom) tp;
			if (tpc.texturePackFile.equals(pack.texturePackFile)) {
				MCPatcherUtils.log("setting new texture pack");
				list.selectedTexturePack = list.defaultTexturePack;
				list.setTexturePack(tpc);
				minecraft.renderEngine.setTileSize(minecraft);
				return;
			}
		}
		MCPatcherUtils.log("selected texture pack not found after refresh, switching to default");
		list.setTexturePack(list.defaultTexturePack);
		minecraft.renderEngine.setTileSize(minecraft);
	}
}