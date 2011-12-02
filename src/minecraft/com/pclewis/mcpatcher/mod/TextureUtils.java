package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.mod.CustomAnimation;
import com.pclewis.mcpatcher.mod.TileSize;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

import org.getspout.spout.client.SpoutClient;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.ColorizerWater;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.TextureCompassFX;
import net.minecraft.src.TextureFX;
import net.minecraft.src.TextureFlamesFX;
import net.minecraft.src.TextureLavaFX;
import net.minecraft.src.TextureLavaFlowFX;
import net.minecraft.src.TexturePackBase;
import net.minecraft.src.TexturePackDefault;
import net.minecraft.src.TexturePortalFX;
import net.minecraft.src.TextureWatchFX;
import net.minecraft.src.TextureWaterFX;
import net.minecraft.src.TextureWaterFlowFX;

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
	private static HashMap expectedColumns = new HashMap();
	private static boolean useTextureCache = false;
    private static boolean reclaimGLMemory = false;
	private static TexturePackBase lastTexturePack = null;
	private static HashMap cache = new HashMap();


	public static boolean setTileSize() {
		int var0 = getTileSize();
		if(var0 == TileSize.int_size) {
			//unchanged
			return false;
		} else {
			TileSize.setTileSize(var0);
			return true;
		}
	}

	public static void setFontRenderer() {
		//MCPatcherUtils.log("setFontRenderer()", new Object[0]);
		Minecraft var0 = SpoutClient.getHandle();
		var0.fontRenderer.initialize(var0.gameSettings, "/font/default.png", var0.renderEngine);
		if(var0.standardGalacticFontRenderer != var0.fontRenderer) {
			var0.standardGalacticFontRenderer.initialize(var0.gameSettings, "/font/alternate.png", var0.renderEngine);
		}

	}

	public static void registerTextureFX(List var0, TextureFX var1) {
		TextureFX var2 = refreshTextureFX(var1);
		if(var2 != null) {
			var0.add(var2);
			var2.onTick();
		}

	}

	private static TextureFX refreshTextureFX(TextureFX var0) {
		if(!(var0 instanceof TextureCompassFX) && !(var0 instanceof TextureWatchFX) && !(var0 instanceof TextureLavaFX) && !(var0 instanceof TextureLavaFlowFX) && !(var0 instanceof TextureWaterFX) && !(var0 instanceof TextureWaterFlowFX) && !(var0 instanceof TextureFlamesFX) && !(var0 instanceof TexturePortalFX) && !(var0 instanceof CustomAnimation)) {
			System.out.printf("attempting to refresh unknown animation %s\n", new Object[]{var0.getClass().getName()});
			Minecraft var1 = SpoutClient.getHandle();
			Class var2 = var0.getClass();

			for(int var3 = 0; var3 < 3; ++var3) {
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

			if(var0.imageData.length != TileSize.int_numBytes) {
				//MCPatcherUtils.log("resizing %s buffer from %d to %d bytes", new Object[]{var2.getName(), Integer.valueOf(var0.imageData.length), Integer.valueOf(TileSize.int_numBytes)});
				var0.imageData = new byte[TileSize.int_numBytes];
			}

			return var0;
		} else {
			return null;
		}
	}

	public static void refreshTextureFX(List var0) {
		//MCPatcherUtils.log("refreshTextureFX()", new Object[0]);
		ArrayList var1 = new ArrayList();
		Iterator var2 = var0.iterator();

		while(var2.hasNext()) {
			TextureFX var3 = (TextureFX)var2.next();
			TextureFX var4 = refreshTextureFX(var3);
			if(var4 != null) {
				var1.add(var4);
			}
		}

		var0.clear();
		Minecraft var9 = SpoutClient.getHandle();
		var0.add(new TextureCompassFX(var9));
		var0.add(new TextureWatchFX(var9));
		TexturePackBase var10 = getSelectedTexturePack();
		boolean var11 = var10 == null || var10 instanceof TexturePackDefault;
		if(!var11 && customLava) {
			var0.add(new CustomAnimation(237, 0, 1, "lava_still", -1, -1));
			var0.add(new CustomAnimation(238, 0, 2, "lava_flowing", 3, 6));
		} else if(animatedLava) {
			var0.add(new TextureLavaFX());
			var0.add(new TextureLavaFlowFX());
		}

		if(!var11 && customWater) {
			var0.add(new CustomAnimation(205, 0, 1, "water_still", -1, -1));
			var0.add(new CustomAnimation(206, 0, 2, "water_flowing", 0, 0));
		} else if(animatedWater) {
			var0.add(new TextureWaterFX());
			var0.add(new TextureWaterFlowFX());
		}

		if(!var11 && customFire && hasResource("/custom_fire_e_w.png") && hasResource("/custom_fire_n_s.png")) {
			var0.add(new CustomAnimation(47, 0, 1, "fire_n_s", 2, 4));
			var0.add(new CustomAnimation(31, 0, 1, "fire_e_w", 2, 4));
		} else if(animatedFire) {
			var0.add(new TextureFlamesFX(0));
			var0.add(new TextureFlamesFX(1));
		}

		if(!var11 && customPortal && hasResource("/custom_portal.png")) {
			var0.add(new CustomAnimation(14, 0, 1, "portal", -1, -1));
		} else if(animatedPortal) {
			var0.add(new TexturePortalFX());
		}

		if(customOther) {
			for(int var5 = 0; var5 < 2; ++var5) {
				String var6 = var5 == 0?"terrain":"item";

				for(int var7 = 0; var7 < 256; ++var7) {
					String var8 = "/custom_" + var6 + "_" + var7 + ".png";
					if(hasResource(var8)) {
						var0.add(new CustomAnimation(var7, var5, 1, var6 + "_" + var7, 2, 4));
					}
				}
		}
		}

		Iterator var12 = var1.iterator();

		TextureFX var13;
		while(var12.hasNext()) {
			var13 = (TextureFX)var12.next();
			var0.add(var13);
		}

		var12 = var0.iterator();

		while(var12.hasNext()) {
			var13 = (TextureFX)var12.next();
			var13.onTick();
		}

		if(ColorizerWater.waterBuffer != ColorizerFoliage.foliageBuffer) {
			refreshColorizer(ColorizerWater.waterBuffer, "/misc/watercolor.png");
		}

		refreshColorizer(ColorizerGrass.grassBuffer, "/misc/grasscolor.png");
		refreshColorizer(ColorizerFoliage.foliageBuffer, "/misc/foliagecolor.png");
		System.gc();
	}

	public static TexturePackBase getSelectedTexturePack() {
		Minecraft var0 = SpoutClient.getHandle();
		return var0 == null?null:(var0.texturePackList == null?null:var0.texturePackList.selectedTexturePack);
	}

	public static String getTexturePackName(TexturePackBase var0) {
		return var0 == null?"Default":var0.texturePackFileName;
	}

	public static ByteBuffer getByteBuffer(ByteBuffer var0, byte[] var1) {
		var0.clear();
		int var2 = var0.capacity();
		int var3 = var1.length;
		if(var3 > var2 || reclaimGLMemory && var2 >= 4 * var3) {
			var0 = GLAllocation.createDirectByteBuffer(var3);
		}

		var0.put(var1);
		var0.position(0).limit(var3);
		TileSize.int_glBufferSize = var3;
		return var0;
	}

	public static boolean isRequiredResource(String var0) {
		return !var0.startsWith("/custom_") && !var0.equals("/terrain_nh.png") && !var0.equals("/terrain_s.png") && !var0.matches("^/font/.*\\.properties$") && !var0.matches("^/mob/.*\\d+.png$");
	}

	public static InputStream getResourceAsStream(TexturePackBase var0, String var1) {
		boolean wasLocked = SpoutClient.isSandboxed();
		SpoutClient.disableSandbox();
		if(var0 != null) var0.func_6482_a();
		try {
			InputStream var2 = null;
			if(var0 != null) {
				try {
					var2 = var0.getResourceAsStream(var1);
				} catch (Exception var4) {
					var4.printStackTrace();
				}
			}
			
			if(var2 == null) {
				var2 = TextureUtils.class.getResourceAsStream(var1);
			}
	
			if(var2 == null && isRequiredResource(var1)) {
				var2 = Thread.currentThread().getContextClassLoader().getResourceAsStream(var1);
			}
	
			return var2;
		}
		finally {
			if (wasLocked) {
				SpoutClient.enableSandbox();
			}
		}
	}

	public static InputStream getResourceAsStream(String var0) {
		return getResourceAsStream(getSelectedTexturePack(), var0);
	}

	public static BufferedImage getResourceAsBufferedImage(TexturePackBase var0, String var1) throws IOException {
		boolean wasLocked = SpoutClient.isSandboxed();
		SpoutClient.disableSandbox();
		try {
			BufferedImage var2 = null;
			boolean var3 = false;
			if(useTextureCache && var0 == lastTexturePack) {
				var2 = (BufferedImage)cache.get(var1);
				if(var2 != null) {
					var3 = true;
				}
			}
	
			if(var2 == null) {
				InputStream var4 = getResourceAsStream(var0, var1);
				if(var4 != null) {
					try {
						var2 = ImageIO.read(var4);
					} finally {
						MCPatcherUtils.close((Closeable)var4);
					}
				}
			}
			
			if(var2 == null) {
				//Search local files (downloaded texture)
				FileImageInputStream imageStream = null;
				try {
					File test = new File(var1);
					if (test.exists()) {
						imageStream = new FileImageInputStream(test);
						var2 = ImageIO.read(imageStream);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
	
			if(var2 == null) {
				throw new IOException(var1 + " image is null");
			} else {
				if(useTextureCache && !var3 && var0 != lastTexturePack) {
					cache.clear();
				}
	
				if(!var3) {
				Integer var11;
				if(var1.matches("^/custom_\\w+_\\d+\\.png$")) {
					var11 = Integer.valueOf(1);
				} else {
					var11 = (Integer)expectedColumns.get(var1);
				}

					if(var11 != null && var2.getWidth() != var11.intValue() * TileSize.int_size) {
						var2 = resizeImage(var2, var11.intValue() * TileSize.int_size);
					}
	
					if(useTextureCache) {
						lastTexturePack = var0;
						cache.put(var1, var2);
					}
	
				if(var1.matches("^/mob/.*_eyes\\d*\\.png$")) {
					//int var5 = 0;
	
						for(int var6 = 0; var6 < var2.getWidth(); ++var6) {
							for(int var7 = 0; var7 < var2.getHeight(); ++var7) {
								int var8 = var2.getRGB(var6, var7);
								if((var8 & -16777216) == 0 && var8 != 0) {
									var2.setRGB(var6, var7, 0);
									//++var5;
								}
							}
						}
	
				}
			}
	
				return var2;
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

	public static int getTileSize(TexturePackBase var0) {
		int var1 = 0;
		Iterator var2 = expectedColumns.entrySet().iterator();

		while(var2.hasNext()) {
			Entry var3 = (Entry)var2.next();
			InputStream var4 = null;

			try {
				var4 = getResourceAsStream(var0, (String)var3.getKey());
				if(var4 != null) {
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

		return var1 > 0?var1:16;
	}

	public static int getTileSize() {
		return getTileSize(getSelectedTexturePack());
	}

	public static boolean hasResource(TexturePackBase var0, String var1) {
		InputStream var2 = getResourceAsStream(var0, var1);
		boolean var3 = var2 != null;
		MCPatcherUtils.close((Closeable)var2);
		return var3;
	}

	public static boolean hasResource(String var0) {
		return hasResource(getSelectedTexturePack(), var0);
	}

	private static BufferedImage resizeImage(BufferedImage var0, int var1) {
		int var2 = var0.getHeight() * var1 / var0.getWidth();
		BufferedImage var3 = new BufferedImage(var1, var2, 2);
		Graphics2D var4 = var3.createGraphics();
		var4.drawImage(var0, 0, 0, var1, var2, (ImageObserver)null);
		return var3;
	}

	private static void refreshColorizer(int[] var0, String var1) {
		try {
			BufferedImage var2 = getResourceAsBufferedImage(var1);
			if(var2 != null) {
				var2.getRGB(0, 0, 256, 256, var0, 0, 256);
			}
		} catch (IOException var3) {
			var3.printStackTrace();
		}

	}

	static {
		expectedColumns.put("/terrain.png", Integer.valueOf(16));
		expectedColumns.put("/gui/items.png", Integer.valueOf(16));
		expectedColumns.put("/misc/dial.png", Integer.valueOf(1));
		expectedColumns.put("/custom_lava_still.png", Integer.valueOf(1));
		expectedColumns.put("/custom_lava_flowing.png", Integer.valueOf(1));
		expectedColumns.put("/custom_water_still.png", Integer.valueOf(1));
		expectedColumns.put("/custom_water_flowing.png", Integer.valueOf(1));
		expectedColumns.put("/custom_fire_n_s.png", Integer.valueOf(1));
		expectedColumns.put("/custom_fire_e_w.png", Integer.valueOf(1));
		expectedColumns.put("/custom_portal.png", Integer.valueOf(1));
	}
}
