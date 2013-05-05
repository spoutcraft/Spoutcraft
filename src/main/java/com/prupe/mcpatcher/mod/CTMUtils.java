package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.TessellatorUtils;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.TileLoader;
import com.prupe.mcpatcher.mod.CTMUtils$1;
import com.prupe.mcpatcher.mod.CTMUtils$2;
import com.prupe.mcpatcher.mod.CTMUtils$3;
import com.prupe.mcpatcher.mod.TileOverrideImpl$BetterGrass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
//Spout Start
import org.spoutcraft.client.config.Configuration;
//Spout End

public class CTMUtils {
	private static final boolean enableStandard = Configuration.isConnectedTextures();
	private static final boolean enableNonStandard = Configuration.isConnectedTextures();
	private static final boolean enableGrass = Configuration.isFancyGrass();
	private static final int maxRecursion = Config.getInt("Connected Textures", "maxRecursion", 4);
	static final int BLOCK_ID_LOG = 17;
	static final int BLOCK_ID_QUARTZ = 155;
	static final int BLOCK_ID_GLASS = 20;
	static final int BLOCK_ID_GLASS_PANE = 102;
	static final int BLOCK_ID_BOOKSHELF = 47;
	static final int BLOCK_ID_GRASS = 2;
	static final int BLOCK_ID_MYCELIUM = 110;
	static final int BLOCK_ID_SNOW = 78;
	static final int BLOCK_ID_CRAFTED_SNOW = 80;
	private static final List allOverrides = new ArrayList();
	private static final ITileOverride[][] blockOverrides = new ITileOverride[Block.blocksList.length][];
	private static final Map tileOverrides = new HashMap();
	private static TileLoader tileLoader;
	private static TileOverrideImpl$BetterGrass betterGrass;
	static boolean active;
	static ITileOverride lastOverride;

	public static void start() {
		lastOverride = null;
		active = tileLoader.setDefaultTextureMap(Tessellator.instance);
	}

	public static Icon getTile(RenderBlocks var0, Block var1, int var2, int var3, int var4, Icon var5, Tessellator var6) {
		return getTile(var0, var1, var2, var3, var4, -1, var5, var6);
	}

	public static Icon getTile(RenderBlocks var0, Block var1, int var2, int var3, int var4, int var5, Icon var6, Tessellator var7) {
		lastOverride = null;

		if (checkFace(var5) && checkBlock(var0, var1)) {
			IBlockAccess var8 = var0.blockAccess;
			CTMUtils$2 var9 = new CTMUtils$2(var1, var6, var8, var2, var3, var4, var5);
			lastOverride = var9.go();

			if (lastOverride != null) {
				var6 = var9.getIcon();
			}
		}

		return lastOverride == null && skipDefaultRendering(var1) ? null : var6;
	}

	public static Icon getTile(RenderBlocks var0, Block var1, int var2, int var3, Tessellator var4) {
		return getTile(var0, var1, var2, var3, var0.getBlockIconFromSideAndMetadata(var1, var2, var3), var4);
	}

	public static Icon getTile(RenderBlocks var0, Block var1, int var2, Tessellator var3) {
		return getTile(var0, var1, var2, 0, var0.getBlockIconFromSide(var1, var2), var3);
	}

	private static Icon getTile(RenderBlocks var0, Block var1, int var2, int var3, Icon var4, Tessellator var5) {
		lastOverride = null;

		if (checkFace(var2) && checkRenderType(var1)) {
			CTMUtils$3 var6 = new CTMUtils$3(var1, var4, var2, var3);
			lastOverride = var6.go();

			if (lastOverride != null) {
				var4 = var6.getIcon();
			}
		}

		return var4;
	}

	public static void reset() {}

	public static void finish() {
		reset();
		RenderPassAPI.instance.finish();
		TessellatorUtils.clearDefaultTextureMap(Tessellator.instance);
		lastOverride = null;
		active = false;
	}

	public static boolean isBetterGrass(IBlockAccess var0, Block var1, int var2, int var3, int var4, int var5) {
		return betterGrass != null && betterGrass.isBetterGrass(var0, var1, var2, var3, var4, var5);
	}

	private static boolean checkBlock(RenderBlocks var0, Block var1) {
		return active && var0.blockAccess != null;
	}

	private static boolean checkFace(int var0) {
		boolean var10000;

		if (active) {
			label25: {
				if (var0 < 0) {
					if (!enableNonStandard) {
						break label25;
					}
				} else if (!enableStandard) {
					break label25;
				}

				var10000 = true;
				return var10000;
			}
		}

		var10000 = false;
		return var10000;
	}

	private static boolean checkRenderType(Block var0) {
		switch (var0.getRenderType()) {
			case 11:
			case 21:
				return false;

			default:
				return true;
		}
	}

	private static boolean skipDefaultRendering(Block var0) {
		return RenderPassAPI.instance.skipDefaultRendering(var0);
	}

	private static void registerOverride(ITileOverride var0) {
		if (var0 != null && !var0.isDisabled()) {
			boolean var1 = false;
			Iterator var2;

			if (var0.getMatchingBlocks() != null) {
				for (var2 = var0.getMatchingBlocks().iterator(); var2.hasNext(); var1 = true) {
					int var3 = ((Integer)var2.next()).intValue();
					String var4 = "";

					if (var3 >= 0 && var3 < Block.blocksList.length && Block.blocksList[var3] != null) {
						var4 = Block.blocksList[var3].getUnlocalizedName2();

						if (var4 == null) {
							var4 = "";
						} else {
							var4 = " (" + var4 + ")";
						}
					}

					blockOverrides[var3] = registerOverride(blockOverrides[var3], var0, "block " + var3 + var4);
				}
			}

			if (var0.getMatchingTiles() != null) {
				for (var2 = var0.getMatchingTiles().iterator(); var2.hasNext(); var1 = true) {
					String var5 = (String)var2.next();
					tileOverrides.put(var5, registerOverride((ITileOverride[])tileOverrides.get(var5), var0, "tile " + var5));
				}
			}

			if (var1) {
				allOverrides.add(var0);
			}
		}
	}

	private static ITileOverride[] registerOverride(ITileOverride[] var0, ITileOverride var1, String var2) {
		if (var0 == null) {
			return new ITileOverride[] {var1};
		} else {
			ITileOverride[] var3 = new ITileOverride[var0.length + 1];
			System.arraycopy(var0, 0, var3, 0, var0.length);
			var3[var0.length] = var1;
			return var3;
		}
	}

	static List access$000() {
		return allOverrides;
	}

	static ITileOverride[][] access$100() {
		return blockOverrides;
	}

	static Map access$200() {
		return tileOverrides;
	}

	static TileLoader access$302(TileLoader var0) {
		tileLoader = var0;
		return var0;
	}

	static TileOverrideImpl$BetterGrass access$502(TileOverrideImpl$BetterGrass var0) {
		betterGrass = var0;
		return var0;
	}

	static boolean access$600() {
		return enableStandard;
	}

	static boolean access$700() {
		return enableNonStandard;
	}

	static TileLoader access$300() {
		return tileLoader;
	}

	static void access$800(ITileOverride var0) {
		registerOverride(var0);
	}

	static boolean access$900() {
		return enableGrass;
	}

	static int access$1000() {
		return maxRecursion;
	}

	static {
		try {
			Class.forName("com.prupe.mcpatcher.mod.RenderPass").getMethod("finish", new Class[0]).invoke((Object)null, new Object[0]);
		} catch (Throwable var1) {
			;
		}

		TexturePackChangeHandler.register(new CTMUtils$1("Connected Textures", 3));
	}
}