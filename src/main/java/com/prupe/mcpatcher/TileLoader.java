package com.prupe.mcpatcher;

import com.prupe.mcpatcher.TileLoader$1;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Icon;
import net.minecraft.src.StitchHolder;
import net.minecraft.src.Stitcher;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Texture;
import net.minecraft.src.TextureManager;
import net.minecraft.src.TextureMap;

public class TileLoader {
	private static final List loaders = new ArrayList();
	private static final boolean debugTextures = Config.getBoolean("Connected Textures", "debugTextures", false);
	private static final int splitTextures = Config.getInt("Connected Textures", "splitTextures", 1);
	private static String overrideTextureName;
	private static final TexturePackChangeHandler changeHandler;
	private static boolean changeHandlerCalled;
	private static boolean registerIconsCalled;
	private static final Set overflowMaps = new HashSet();
	private static final int OVERFLOW_TEXTURE_MAP_INDEX = 2;
	private static final long MAX_TILESHEET_SIZE;
	private static final BufferedImage missingTextureImage = generateDebugTexture("missing", 64, 64, false);
	protected final String mapName;
	protected final boolean allowOverflow;
	private int overflowIndex;
	private TextureMap textureMap;
	private final Set tilesToRegister = new HashSet();
	private final Map tileTextures = new HashMap();
	private final Map iconMap = new HashMap();

	public static void registerIcons(TextureMap var0, Stitcher var1, String var2, Map var3) {
		registerIconsCalled = true;

		if (!changeHandlerCalled) {
			changeHandler.beforeChange();
		}

		TessellatorUtils.registerTextureMap(var0, var2);
		Iterator var4 = loaders.iterator();

		while (var4.hasNext()) {
			TileLoader var5 = (TileLoader)var4.next();

			if (var5.textureMap == null && var2.equals(var5.mapName)) {
				var5.textureMap = var0;
			}

			if (var5.isForThisMap(var2)) {
				while (!var5.tilesToRegister.isEmpty() && var5.registerOneIcon(var0, var1, var2, var3)) {
					;
				}
			}
		}
	}

	public static String getOverridePath(String var0, String var1, String var2) {
		String var3;

		if (var1.startsWith("/")) {
			var3 = var1.substring(1).replaceFirst("\\.[^.]+$", "") + var2;
		} else {
			var3 = var0 + var1 + var2;
		}

		return var3;
	}

	public static String getOverrideTextureName(String var0) {
		if (overrideTextureName == null) {
			return var0;
		} else {
			return overrideTextureName;
		}
	}

	public static void updateAnimations() {
		Iterator var0 = overflowMaps.iterator();

		while (var0.hasNext()) {
			TextureMap var1 = (TextureMap)var0.next();
			var1.updateAnimations();
		}
	}

	public static BufferedImage generateDebugTexture(String var0, int var1, int var2, boolean var3) {
		BufferedImage var4 = new BufferedImage(var1, var2, 2);
		Graphics var5 = var4.getGraphics();
		var5.setColor(var3 ? new Color(0, 255, 255, 128) : Color.WHITE);
		var5.fillRect(0, 0, var1, var2);
		var5.setColor(var3 ? Color.RED : Color.BLACK);
		int var6 = 10;

		if (var3) {
			var6 += var2 / 2;
		}

		int var7 = var1 / 8;

		if (var7 <= 0) {
			return var4;
		} else {
			while (var0.length() % var7 != 0) {
				var0 = var0 + " ";
			}

			while (var6 < var2 && !var0.equals("")) {
				var5.drawString(var0.substring(0, var7), 1, var6);
				var6 += var5.getFont().getSize();
				var0 = var0.substring(var7);
			}

			return var4;
		}
	}

	static void init() {}

	public TileLoader(String var1, boolean var2) {
		this.mapName = var1;
		this.allowOverflow = var2;
		loaders.add(this);
	}

	private static long getTextureSize(List var0) {
		if (var0.isEmpty()) {
			return 0L;
		} else {
			Texture var1 = (Texture)var0.get(0);
			return var1 == null ? 0L : (long)(4 * var1.getWidth() * var1.getHeight());
		}
	}

	private static long getTextureSize(Collection var0) {
		long var1 = 0L;
		List var4;

		for (Iterator var3 = var0.iterator(); var3.hasNext(); var1 += getTextureSize(var4)) {
			var4 = (List)var3.next();
		}

		return var1;
	}

	public boolean preloadTile(String var1, boolean var2) {
		if (this.tileTextures.containsKey(var1)) {
			return true;
		} else {
			Object var3;

			try {
				overrideTextureName = var1;

				if (!debugTextures && TexturePackAPI.hasResource(var1)) {
					var3 = TextureManager.instance().createTexture(var1.replaceFirst("^/", ""));

					if (var3 == null || ((List)var3).isEmpty()) {
						boolean var10 = false;
						return var10;
					}
				} else {
					BufferedImage var4 = generateDebugTexture(var1, 64, 64, var2);
					Texture var5 = TextureManager.instance().makeTexture(var1, 2, var4.getWidth(), var4.getHeight(), 10496, 6408, 9728, 9728, false, var4);

					if (var5 == null) {
						boolean var6 = false;
						return var6;
					}

					var3 = new ArrayList();
					((List)var3).add(var5);
				}
			} finally {
				overrideTextureName = null;
			}

			this.tilesToRegister.add(var1);
			this.tileTextures.put(var1, var3);
			return true;
		}
	}

	protected boolean isForThisMap(String var1) {
		return this.allowOverflow && splitTextures > 1 ? var1.startsWith(this.mapName + "_overflow") : var1.startsWith(this.mapName);
	}

	private boolean registerOneIcon(TextureMap var1, Stitcher var2, String var3, Map var4) {
		String var5 = (String)this.tilesToRegister.iterator().next();
		List var6 = (List)this.tileTextures.get(var5);

		if (var6 != null && !var6.isEmpty()) {
			long var7 = getTextureSize(var4.values());
			long var9 = getTextureSize(var6);

			if (var9 + var7 > MAX_TILESHEET_SIZE) {
				float var15 = (float)var7 / 1048576.0F;

				if (var7 <= 0L) {
					this.tilesToRegister.remove(var5);
					return true;
				} else {
					return false;
				}
			} else {
				Texture var11 = (Texture)var6.get(0);
				StitchHolder var12 = new StitchHolder(var11);
				var2.addStitchHolder(var12);
				var4.put(var12, var6);
				Icon var13 = var1.registerIcon(var5);

				if (var3.contains("_overflow")) {
					TessellatorUtils.registerIcon(var1, var13);
				}

				this.iconMap.put(var5, var13);
				String var14 = var6.size() > 1 ? ", " + var6.size() + " frames" : "";
				this.tilesToRegister.remove(var5);
				return true;
			}
		} else {
			this.tilesToRegister.remove(var5);
			return true;
		}
	}

	public void finish() {
		this.tilesToRegister.clear();
		this.tileTextures.clear();
	}

	public Icon getIcon(String var1) {
		Icon var2 = (Icon)this.iconMap.get(var1);

		if (var2 == null && this.textureMap != null) {
			var2 = (Icon)this.textureMap.mapTexturesStiched.get(var1);
		}

		return var2;
	}

	public boolean setDefaultTextureMap(Tessellator var1) {
		var1.textureMap = this.textureMap;
		return this.textureMap != null;
	}

	static boolean access$002(boolean var0) {
		changeHandlerCalled = var0;
		return var0;
	}

	static Set access$100() {
		return overflowMaps;
	}

	static List access$200() {
		return loaders;
	}

	static Set access$300(TileLoader var0) {
		return var0.tilesToRegister;
	}

	static int access$400() {
		return splitTextures;
	}

	static boolean access$502(boolean var0) {
		registerIconsCalled = var0;
		return var0;
	}

	static int access$604(TileLoader var0) {
		return ++var0.overflowIndex;
	}

	static BufferedImage access$800() {
		System.out.println("SpoutDebug: returning missing Texture");
		return missingTextureImage;
	}

	static boolean access$500() {
		return registerIconsCalled;
	}

	static {
		long var0 = 4096L;

		try {
			var0 = (long)Minecraft.getGLMaximumTextureSize();
		} catch (Throwable var3) {
			var3.printStackTrace();
		}

		MAX_TILESHEET_SIZE = var0 * var0 * 4L * 7L / 8L;
		changeHandler = new TileLoader$1("Tilesheet API", 2);
		TexturePackChangeHandler.register(changeHandler);
	}
}
