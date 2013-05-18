package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.TexturePackAPI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.src.Icon;
import net.minecraft.src.StitchHolder;
import net.minecraft.src.Stitcher;
import net.minecraft.src.Texture;
import net.minecraft.src.TextureManager;
import net.minecraft.src.TextureMap;

public class TileLoader {
	private static final MCLogger logger = MCLogger.getLogger("Connected Textures", "CTM");
	private static final boolean debugTextures = Config.getBoolean("Connected Textures", "debugTextures", false);
	private static String overrideTextureName;
	private final MCLogger subLogger;
	private final Map tileTextures = new HashMap();
	private final Map loadedIcons = new HashMap();

	public static String getOverridePath(String var0, String var1, String var2) {
		String var3;

		if (var1.startsWith("/")) {
			var3 = var1.substring(1).replaceFirst("\\.[^.]+$", "") + var2;
		} else {
			var3 = var0 + var1 + var2;
		}

		logger.finer("getOverridePath(%s, %s, %s) -> %s", new Object[] {var0, var1, var2, var3});
		return var3;
	}

	public static String getOverrideTextureName(String var0) {
		if (overrideTextureName == null) {
			if (var0.matches("^\\d+$")) {
				logger.warning("no override set for %s", new Object[] {var0});
			}

			return var0;
		} else {
			logger.finer("getOverrideTextureName(%s) -> %s", new Object[] {var0, overrideTextureName});
			return overrideTextureName;
		}
	}

	TileLoader(MCLogger var1) {
		this.subLogger = var1;
	}

	static BufferedImage generateDebugTexture(String var0, int var1, int var2, boolean var3) {
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

	boolean preload(String var1, List var2, boolean var3) {
		if (!var1.toLowerCase().endsWith(".png")) {
			var1 = var1 + ".png";
		}

		if (this.tileTextures.containsKey(var1)) {
			var2.add(var1);
			return true;
		} else {
			Object var4;
			label94: {
				boolean var5;

				try {
					overrideTextureName = var1;

					if (debugTextures || !TexturePackAPI.hasResource(var1)) {
						BufferedImage var11 = generateDebugTexture(var1, 64, 64, var3);
						Texture var6 = TextureManager.instance().makeTexture(var1, 2, var11.getWidth(), var11.getHeight(), 10496, 6408, 9728, 9728, false, var11);

						if (var6 == null) {
							boolean var7 = false;
							return var7;
						}

						var4 = new ArrayList();
						((List)var4).add(var6);
						break label94;
					}

					var4 = TextureManager.instance().createTexture(var1.replaceFirst("^/", ""));

					if (var4 != null && !((List)var4).isEmpty()) {
						break label94;
					}

					var5 = false;
				} finally {
					overrideTextureName = null;
				}

				return var5;
			}
			var2.add(var1);
			this.tileTextures.put(var1, var4);
			return true;
		}
	}

	Icon[] registerIcons(TextureMap var1, Stitcher var2, Map var3, List var4) {
		Icon[] var5 = new Icon[var4.size()];

		for (int var6 = 0; var6 < var4.size(); ++var6) {
			String var7 = (String)var4.get(var6);

			if (var7 != null) {
				var5[var6] = (Icon)this.loadedIcons.get(var7);

				if (var5[var6] == null) {
					List var8 = (List)this.tileTextures.get(var7);

					if (var8 != null && !var8.isEmpty()) {
						Texture var9 = (Texture)var8.get(0);
						StitchHolder var10 = new StitchHolder(var9);
						var2.addStitchHolder(var10);
						var3.put(var10, var8);
						var5[var6] = var1.registerIcon(var7);
						TessellatorUtils.registerIcon(var1, var5[var6]);
						this.loadedIcons.put(var7, var5[var6]);
						String var11 = var8.size() > 1 ? ", " + var8.size() + " frames" : "";
						this.subLogger.finer("%s -> icon: %dx%d%s", new Object[] {var7, Integer.valueOf(var9.getWidth()), Integer.valueOf(var9.getHeight()), var11});
					} else {
						this.subLogger.error("tile for %s unexpectedly missing", new Object[] {var7});
					}
				}
			}
		}

		return var5;
	}

	int getTextureSize(List var1) {
		HashSet var2 = new HashSet();
		var2.addAll(var1);
		int var3 = 0;
		String var5;

		for (Iterator var4 = var2.iterator(); var4.hasNext(); var3 += this.getTextureSize(var5)) {
			var5 = (String)var4.next();
		}

		return var3;
	}

	int getTextureSize(String var1) {
		if (var1 == null) {
			return 0;
		} else {
			List var2 = (List)this.tileTextures.get(var1);
			return var2 != null && !var2.isEmpty() && this.loadedIcons.get(var1) == null ? ((Texture)var2.get(0)).getWidth() * ((Texture)var2.get(0)).getHeight() : 0;
		}
	}

	void finish() {
		this.tileTextures.clear();
	}
}
