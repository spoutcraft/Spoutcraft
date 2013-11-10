package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.TessellatorUtils;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.TileLoader;
import com.prupe.mcpatcher.ctm.CTMUtils$1;
import com.prupe.mcpatcher.ctm.CTMUtils$2;
import com.prupe.mcpatcher.ctm.CTMUtils$3;
import com.prupe.mcpatcher.ctm.TileOverrideImpl$BetterGrass;
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

public class CTMUtils {
	private static final MCLogger logger = MCLogger.getLogger("Connected Textures", "CTM");
	private static final boolean enableStandard = Config.getBoolean("Connected Textures", "standard", true);
	private static final boolean enableNonStandard = Config.getBoolean("Connected Textures", "nonStandard", true);
	private static final boolean enableGrass = Config.getBoolean("Connected Textures", "grass", false);
	private static final int maxRecursion = Config.getInt("Connected Textures", "maxRecursion", 4);
	static final int BLOCK_ID_GRASS = 2;
	static final int BLOCK_ID_MYCELIUM = 110;
	static final int BLOCK_ID_SNOW = 78;
	static final int BLOCK_ID_CRAFTED_SNOW = 80;
	private static final List<ITileOverride> allOverrides = new ArrayList();
	private static final ITileOverride[][] blockOverrides = new ITileOverride[Block.blocksList.length][];
	private static final Map<String, ITileOverride[]> tileOverrides = new HashMap();
	private static TileLoader tileLoader;
	private static TileOverrideImpl$BetterGrass betterGrass;
	static boolean active;
	static ITileOverride lastOverride;

	public static void start() {
		lastOverride = null;
		active = tileLoader.setDefaultTextureMap(Tessellator.instance);
	}

	public static Icon getTile(RenderBlocks renderBlocks, Block block, int i, int j, int k, Icon origIcon, Tessellator tessellator) {
		return getTile(renderBlocks, block, i, j, k, -1, origIcon, tessellator);
	}

	public static Icon getTile(RenderBlocks renderBlocks, Block block, int i, int j, int k, int face, Icon icon, Tessellator tessellator) {
		lastOverride = null;

		if (checkFace(face) && checkBlock(renderBlocks, block)) {
			IBlockAccess blockAccess = renderBlocks.blockAccess;
			CTMUtils$2 iterator = new CTMUtils$2(block, icon, blockAccess, i, j, k, face);
			lastOverride = iterator.go();

			if (lastOverride != null) {
				icon = iterator.getIcon();
			}
		}

		return lastOverride == null && skipDefaultRendering(block) ? null : icon;
	}

	public static Icon getTile(RenderBlocks renderBlocks, Block block, int face, int metadata, Tessellator tessellator) {
		return getTile(renderBlocks, block, face, metadata, renderBlocks.getBlockIconFromSideAndMetadata(block, face, metadata), tessellator);
	}

	public static Icon getTile(RenderBlocks renderBlocks, Block block, int face, Tessellator tessellator) {
		return getTile(renderBlocks, block, face, 0, renderBlocks.getBlockIconFromSide(block, face), tessellator);
	}

	private static Icon getTile(RenderBlocks renderBlocks, Block block, int face, int metadata, Icon icon, Tessellator tessellator) {
		lastOverride = null;

		if (checkFace(face) && checkRenderType(block)) {
			CTMUtils$3 iterator = new CTMUtils$3(block, icon, face, metadata);
			lastOverride = iterator.go();

			if (lastOverride != null) {
				icon = iterator.getIcon();
			}
		}

		return icon;
	}

	public static void reset() {}

	public static void finish() {
		reset();
		RenderPassAPI.instance.finish();
		TessellatorUtils.clearDefaultTextureMap(Tessellator.instance);
		lastOverride = null;
		active = false;
	}

	public static boolean isBetterGrass(IBlockAccess blockAccess, Block block, int i, int j, int k, int face) {
		return betterGrass != null && betterGrass.isBetterGrass(blockAccess, block, i, j, k, face);
	}

	private static boolean checkBlock(RenderBlocks renderBlocks, Block block) {
		return active && renderBlocks.blockAccess != null;
	}

	private static boolean checkFace(int face) {
		boolean var10000;

		if (active) {
			label25: {
				if (face < 0) {
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

	private static boolean checkRenderType(Block block) {
		switch (block.getRenderType()) {
			case 11:
			case 21:
				return false;

			default:
				return true;
		}
	}

	private static boolean skipDefaultRendering(Block block) {
		return RenderPassAPI.instance.skipDefaultRendering(block);
	}

	private static void registerOverride(ITileOverride override) {
		if (override != null && !override.isDisabled()) {
			boolean registered = false;
			Iterator i$;

			if (override.getMatchingBlocks() != null) {
				for (i$ = override.getMatchingBlocks().iterator(); i$.hasNext(); registered = true) {
					int name = ((Integer)i$.next()).intValue();
					String blockName = "";

					if (name >= 0 && name < Block.blocksList.length && Block.blocksList[name] != null) {
						blockName = Block.blocksList[name].getUnlocalizedName();

						if (blockName == null) {
							blockName = "";
						} else {
							blockName = " (" + blockName.replaceFirst("^tile\\.", "") + ")";
						}
					}

					blockOverrides[name] = registerOverride(blockOverrides[name], override, "block " + name + blockName);
				}
			}

			if (override.getMatchingTiles() != null) {
				for (i$ = override.getMatchingTiles().iterator(); i$.hasNext(); registered = true) {
					String name1 = (String)i$.next();
					tileOverrides.put(name1, registerOverride((ITileOverride[])tileOverrides.get(name1), override, "tile " + name1));
				}
			}

			if (registered) {
				allOverrides.add(override);
			}
		}
	}

	private static ITileOverride[] registerOverride(ITileOverride[] overrides, ITileOverride override, String description) {
		logger.fine("using %s for %s", new Object[] {override, description});

		if (overrides == null) {
			return new ITileOverride[] {override};
		} else {
			ITileOverride[] newList = new ITileOverride[overrides.length + 1];
			System.arraycopy(overrides, 0, newList, 0, overrides.length);
			newList[overrides.length] = override;
			return newList;
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

	static TileLoader access$302(TileLoader x0) {
		tileLoader = x0;
		return x0;
	}

	static MCLogger access$400() {
		return logger;
	}

	static TileOverrideImpl$BetterGrass access$502(TileOverrideImpl$BetterGrass x0) {
		betterGrass = x0;
		return x0;
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

	static void access$800(ITileOverride x0) {
		registerOverride(x0);
	}

	static boolean access$900() {
		return enableGrass;
	}

	static int access$1000() {
		return maxRecursion;
	}

	static {
		try {
			Class.forName("com.prupe.mcpatcher.ctm.RenderPass").getMethod("finish", new Class[0]).invoke((Object)null, new Object[0]);
		} catch (Throwable var1) {
			;
		}

		TexturePackChangeHandler.register(new CTMUtils$1("Connected Textures", 3));
	}
}
