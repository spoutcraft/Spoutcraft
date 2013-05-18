package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.mod.CTMUtils$1;
import com.prupe.mcpatcher.mod.CTMUtils$2;
import com.prupe.mcpatcher.mod.CTMUtils$3;
import com.prupe.mcpatcher.mod.TileOverrideImpl$BetterGrass;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Stitcher;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Texture;
import net.minecraft.src.TextureMap;

//Spout Start
import org.spoutcraft.client.config.Configuration;
//Spout End

public class CTMUtils {
	private static final MCLogger logger = MCLogger.getLogger("Connected Textures", "CTM");
	private static final boolean enableStandard = Config.getBoolean("Connected Textures", "standard", true);
	private static final boolean enableNonStandard = Config.getBoolean("Connected Textures", "nonStandard", true);
	private static final boolean enableGrass = Config.getBoolean("Connected Textures", "grass", false);
	private static final int splitTextures = Config.getInt("Connected Textures", "splitTextures", 1);
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
	private static final int CTM_TEXTURE_MAP_INDEX = 2;
	private static final int MAX_CTM_TEXTURE_SIZE;
	private static final ITileOverride[][] blockOverrides = new ITileOverride[Block.blocksList.length][];
	private static final Map tileOverrides = new HashMap();
	private static final List overridesToRegister = new ArrayList();
	private static TileOverrideImpl$BetterGrass betterGrass;
	private static final TexturePackChangeHandler changeHandler;
	private static boolean changeHandlerCalled;
	private static boolean registerIconsCalled;
	static boolean active;
	static TextureMap terrainMap;
	private static BufferedImage missingTextureImage = TileLoader.generateDebugTexture("missing", 64, 64, false);
	private static List ctmMaps = new ArrayList();
	private static TileLoader tileLoader;
	static ITileOverride lastOverride;

	public static void start() {
		lastOverride = null;

		if (terrainMap == null) {
			active = false;
			Tessellator.instance.textureMap = null;
		} else {
			active = true;
			Tessellator.instance.textureMap = terrainMap;
		}
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

	public static Tessellator getTessellator(Icon var0) {
		return TessellatorUtils.getTessellator(Tessellator.instance, var0);
	}

	public static void registerIcons(TextureMap var0, Stitcher var1, String var2, Map var3) {
		TessellatorUtils.registerTextureMap(var0, var2);
		CITUtils.registerIcons(var0, var1, var2, var3);

		if (var2 != null && (var2.equals("terrain") || var2.matches("ctm\\d+"))) {
			registerIconsCalled = true;

			if (!changeHandlerCalled) {
				changeHandler.beforeChange();
			}

			int var4 = 0;
			Iterator var5 = var3.values().iterator();

			while (var5.hasNext()) {
				List var6 = (List)var5.next();

				if (var6 != null && var6.size() > 0) {
					Texture var7 = (Texture)var6.get(0);
					var4 += var7.getWidth() * var7.getHeight();
				}
			}

			if (var2.equals("terrain")) {
				terrainMap = var0;

				if (Configuration.betterGrass !=0) {
					betterGrass = new TileOverrideImpl$BetterGrass(var0, 2, "grass");
					registerOverride(betterGrass);
					registerOverride(new TileOverrideImpl$BetterGrass(var0, 110, "mycel"));
				}

				if (splitTextures > 1) {
					return;
				}
			}

			boolean var10 = false;
			boolean var11 = false;
			Iterator var12 = overridesToRegister.iterator();

			while (var12.hasNext()) {
				ITileOverride var8 = (ITileOverride)var12.next();

				if (var8 != null && !var8.isDisabled()) {
					var4 += var8.getTotalTextureSize();

					if (var4 > MAX_CTM_TEXTURE_SIZE) {
						float var9 = (float)(4 * var4) / 1048576.0F;

						if (splitTextures > 0) {
							var4 -= var8.getTotalTextureSize();
							break;
						}

						if (!var10) {
							var10 = true;
						}
					}

					var8.registerIcons(var0, var1, var3);
					var12.remove();
					var11 = true;
				}
			}

			if (var10 || !var11 && !var2.equals("terrain")) {
				overridesToRegister.clear();
			}
		}
	}

	public static void updateAnimations() {
		Iterator var0 = ctmMaps.iterator();

		while (var0.hasNext()) {
			TextureMap var1 = (TextureMap)var0.next();
			var1.updateAnimations();
		}
	}

	public static void reset() {}

	public static void finish() {
		reset();
		RenderPassAPI.instance.finish();
		Tessellator.instance.textureMap = null;
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
			if (var0.getTotalTextureSize() > 0) {
				overridesToRegister.add(var0);
			}

			Iterator var1;
			int var2;
			String var3;

			if (var0.getMatchingBlocks() != null) {
				for (var1 = var0.getMatchingBlocks().iterator(); var1.hasNext(); blockOverrides[var2] = registerOverride(blockOverrides[var2], var0, "block " + var2 + var3)) {
					var2 = ((Integer)var1.next()).intValue();
					var3 = "";

					if (var2 >= 0 && var2 < Block.blocksList.length && Block.blocksList[var2] != null) {
						var3 = Block.blocksList[var2].getUnlocalizedName2();

						if (var3 == null) {
							var3 = "";
						} else {
							var3 = " (" + var3 + ")";
						}
					}
				}
			}

			if (var0.getMatchingTiles() != null) {
				var1 = var0.getMatchingTiles().iterator();

				while (var1.hasNext()) {
					String var4 = (String)var1.next();
					tileOverrides.put(var4, registerOverride((ITileOverride[])tileOverrides.get(var4), var0, "tile " + var4));
				}
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

	static boolean access$002(boolean var0) {
		changeHandlerCalled = var0;
		return var0;
	}

	static List access$100() {
		return ctmMaps;
	}

	static ITileOverride[][] access$200() {
		return blockOverrides;
	}

	static Map access$300() {
		return tileOverrides;
	}

	static List access$400() {
		return overridesToRegister;
	}

	static TileLoader access$502(TileLoader var0) {
		tileLoader = var0;
		return var0;
	}

	static TileOverrideImpl$BetterGrass access$702(TileOverrideImpl$BetterGrass var0) {
		betterGrass = var0;
		return var0;
	}

	static boolean access$800() {
		return Configuration.isConnectedTextures();
	}

	static boolean access$900() {
		return Configuration.isConnectedTextures();
	}

	static TileLoader access$500() {
		return tileLoader;
	}
	
	static MCLogger access$600() {
		return logger;
	}

	static void access$1000(ITileOverride var0) {
		registerOverride(var0);
	}

	static int access$1100() {
		return splitTextures;
	}

	static boolean access$1202(boolean var0) {
		registerIconsCalled = var0;
		return var0;
	}

	static BufferedImage access$1300() {
		return missingTextureImage;
	}

	static boolean access$1200() {
		return registerIconsCalled;
	}

	static int access$1400() {
		return maxRecursion;
	}

	static {
		try {
			Class.forName("com.prupe.mcpatcher.mod.RenderPass").getMethod("finish", new Class[0]).invoke((Object)null, new Object[0]);
		} catch (Throwable var1) {
			;
		}

		int var0 = Minecraft.getGLMaximumTextureSize();		
		MAX_CTM_TEXTURE_SIZE = var0 * var0 * 7 / 8;
		changeHandler = new CTMUtils$1("Connected Textures", 2);
		TexturePackChangeHandler.register(changeHandler);
	}
}