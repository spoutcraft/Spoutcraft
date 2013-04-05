package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.mod.TileOverrideImpl$CTM;
import com.prupe.mcpatcher.mod.TileOverrideImpl$Fixed;
import com.prupe.mcpatcher.mod.TileOverrideImpl$Horizontal;
import com.prupe.mcpatcher.mod.TileOverrideImpl$Random1;
import com.prupe.mcpatcher.mod.TileOverrideImpl$Repeat;
import com.prupe.mcpatcher.mod.TileOverrideImpl$Top;
import com.prupe.mcpatcher.mod.TileOverrideImpl$Vertical;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.Stitcher;
import net.minecraft.src.TextureMap;

abstract class TileOverride implements ITileOverride {
	static final int BOTTOM_FACE = 0;
	static final int TOP_FACE = 1;
	static final int NORTH_FACE = 2;
	static final int SOUTH_FACE = 3;
	static final int WEST_FACE = 4;
	static final int EAST_FACE = 5;
	private static final int META_MASK = 65535;
	private static final int ORIENTATION_U_D = 0;
	private static final int ORIENTATION_E_W = 65536;
	private static final int ORIENTATION_N_S = 131072;
	private static final int ORIENTATION_E_W_2 = 196608;
	private static final int ORIENTATION_N_S_2 = 262144;
	private static final int[][] ROTATE_UV_MAP = new int[][] {{4, 5, 2, 3, 1, 0, 2, -2, 2, -2, 0, 0}, {2, 3, 1, 0, 4, 5, 0, 0, 0, 0, -2, 2}, {4, 5, 2, 3, 1, 0, 2, -2, -2, -2, 0, 0}, {2, 3, 1, 0, 4, 5, 0, 0, 0, 0, -2, -2}};
	private static final int[] GO_DOWN = new int[] {0, -1, 0};
	private static final int[] GO_UP = new int[] {0, 1, 0};
	private static final int[] GO_NORTH = new int[] {0, 0, -1};
	private static final int[] GO_SOUTH = new int[] {0, 0, 1};
	private static final int[] GO_WEST = new int[] { -1, 0, 0};
	private static final int[] GO_EAST = new int[] {1, 0, 0};
	private static final int[][] NORMALS = new int[][] {GO_DOWN, GO_UP, GO_NORTH, GO_SOUTH, GO_WEST, GO_EAST};
	protected static final int[][][] NEIGHBOR_OFFSET = new int[][][] {{GO_WEST, add(GO_WEST, GO_SOUTH), GO_SOUTH, add(GO_EAST, GO_SOUTH), GO_EAST, add(GO_EAST, GO_NORTH), GO_NORTH, add(GO_WEST, GO_NORTH)}, {GO_WEST, add(GO_WEST, GO_SOUTH), GO_SOUTH, add(GO_EAST, GO_SOUTH), GO_EAST, add(GO_EAST, GO_NORTH), GO_NORTH, add(GO_WEST, GO_NORTH)}, {GO_EAST, add(GO_EAST, GO_DOWN), GO_DOWN, add(GO_WEST, GO_DOWN), GO_WEST, add(GO_WEST, GO_UP), GO_UP, add(GO_EAST, GO_UP)}, {GO_WEST, add(GO_WEST, GO_DOWN), GO_DOWN, add(GO_EAST, GO_DOWN), GO_EAST, add(GO_EAST, GO_UP), GO_UP, add(GO_WEST, GO_UP)}, {GO_NORTH, add(GO_NORTH, GO_DOWN), GO_DOWN, add(GO_SOUTH, GO_DOWN), GO_SOUTH, add(GO_SOUTH, GO_UP), GO_UP, add(GO_NORTH, GO_UP)}, {GO_SOUTH, add(GO_SOUTH, GO_DOWN), GO_DOWN, add(GO_NORTH, GO_DOWN), GO_NORTH, add(GO_NORTH, GO_UP), GO_UP, add(GO_SOUTH, GO_UP)}};
	private static final int CONNECT_BY_BLOCK = 0;
	private static final int CONNECT_BY_TILE = 1;
	private static final int CONNECT_BY_MATERIAL = 2;
	private static Method getBiomeNameAt;
	private static Field maxBorder;
	private final String propertiesFile;
	private final String texturesDirectory;
	private final String propertiesName;
	private final String directoryName;
	private final TileLoader tileLoader;
	private final int renderPass;
	private final Set matchBlocks;
	private final Set matchTiles;
	private final int faces;
	private final int metadata;
	private final int connectType;
	private final boolean innerSeams;
	private final Set biomes;
	private final int minHeight;
	private final int maxHeight;
	private final List tileNames = new ArrayList();
	protected Icon[] icons;
	private boolean disabled;
	private int[] reorient;
	private int rotateUV;
	protected boolean rotateTop;

	static TileOverride create(String var0, TileLoader var1) {
		if (var0 == null) {
			return null;
		} else {
			Properties var2 = TexturePackAPI.getProperties(var0);

			if (var2 == null) {
				return null;
			} else {
				String var3 = var2.getProperty("method", "default").trim().toLowerCase();
				Object var4 = null;

				if (!var3.equals("default") && !var3.equals("glass") && !var3.equals("ctm")) {
					if (var3.equals("random")) {
						var4 = new TileOverrideImpl$Random1(var0, var2, var1);
					} else if (!var3.equals("fixed") && !var3.equals("static")) {
						if (!var3.equals("bookshelf") && !var3.equals("horizontal")) {
							if (var3.equals("vertical")) {
								var4 = new TileOverrideImpl$Vertical(var0, var2, var1);
							} else if (!var3.equals("sandstone") && !var3.equals("top")) {
								var4 = new TileOverrideImpl$Repeat(var0, var2, var1);
							} else {
								var4 = new TileOverrideImpl$Top(var0, var2, var1);
							}
						} else {
							var4 = new TileOverrideImpl$Horizontal(var0, var2, var1);
						}
					} else {
						var4 = new TileOverrideImpl$Fixed(var0, var2, var1);
					}
				} else {
					var4 = new TileOverrideImpl$CTM(var0, var2, var1);
				}

				if (var4 != null && !((TileOverride)var4).disabled) {
					String var5 = ((TileOverride)var4).checkTileMap();

					if (var5 != null) {
						((TileOverride)var4).error("invalid %s tile map: %s", new Object[] {((TileOverride)var4).getMethod(), var5});
					}
				}

				return (TileOverride)(var4 != null && !((TileOverride)var4).disabled ? var4 : null);
			}
		}
	}

	protected TileOverride(String var1, Properties var2, TileLoader var3) {
		this.propertiesFile = var1;
		this.texturesDirectory = var1.replaceFirst("/[^/]*$", "");
		this.directoryName = this.texturesDirectory.replaceAll(".*/", "");
		this.propertiesName = var1.replaceFirst(".*/", "").replaceFirst("\\.properties$", "");
		this.tileLoader = var3;

		try {
			TexturePackAPI.enableTextureBorder = true;
			this.loadIcons(var2);

			if (this.tileNames.isEmpty()) {
				this.error("no images found in %s/", new Object[] {this.texturesDirectory});
			}
		} finally {
			TexturePackAPI.enableTextureBorder = false;
		}

		String[] var4 = new String[Block.blocksList.length];
		int var5;

		for (var5 = 0; var5 < Block.blocksList.length; ++var5) {
			Block var6 = Block.blocksList[var5];

			if (var6 != null) {
				var4[var5] = var6.getUnlocalizedName2();
			}
		}

		this.matchBlocks = this.getIDList(var2, "matchBlocks", "block", var4);
		this.matchTiles = this.getIDList(var2, "matchTiles");

		if (this.matchBlocks.isEmpty() && this.matchTiles.isEmpty()) {
			this.matchTiles.add(this.propertiesName);
		}

		var5 = 0;
		String[] var14 = var2.getProperty("faces", "all").trim().toLowerCase().split("\\s+");
		int var7 = var14.length;
		int var8;
		String var9;

		for (var8 = 0; var8 < var7; ++var8) {
			var9 = var14[var8];

			if (var9.equals("bottom")) {
				var5 |= 1;
			} else if (var9.equals("top")) {
				var5 |= 2;
			} else if (var9.equals("north")) {
				var5 |= 4;
			} else if (var9.equals("south")) {
				var5 |= 8;
			} else if (var9.equals("east")) {
				var5 |= 32;
			} else if (var9.equals("west")) {
				var5 |= 16;
			} else if (!var9.equals("side") && !var9.equals("sides")) {
				if (var9.equals("all")) {
					var5 = -1;
				}
			} else {
				var5 |= 60;
			}
		}

		this.faces = var5;
		int var13 = 0;
		int[] var15 = MCPatcherUtils.parseIntegerList(var2.getProperty("metadata", "0-31"), 0, 31);
		var8 = var15.length;

		for (int var17 = 0; var17 < var8; ++var17) {
			int var10 = var15[var17];
			var13 |= 1 << var10;
		}

		this.metadata = var13;
		String var16 = var2.getProperty("connect", "").trim().toLowerCase();

		if (var16.equals("")) {
			this.connectType = this.matchTiles.isEmpty() ? 0 : 1;
		} else if (var16.equals("block")) {
			this.connectType = 0;
		} else if (var16.equals("tile")) {
			this.connectType = 1;
		} else if (var16.equals("material")) {
			this.connectType = 2;
		} else {
			this.error("invalid connect type %s", new Object[] {var16});
			this.connectType = 0;
		}

		this.innerSeams = MCPatcherUtils.getBooleanProperty(var2, "innerSeams", false);
		HashSet var18 = new HashSet();
		var9 = var2.getProperty("biomes", "").trim().toLowerCase();

		if (!var9.equals("")) {
			Collections.addAll(var18, var9.split("\\s+"));
		}

		if (var18.isEmpty()) {
			var18 = null;
		}

		this.biomes = var18;
		this.minHeight = MCPatcherUtils.getIntProperty(var2, "minHeight", -1);
		this.maxHeight = MCPatcherUtils.getIntProperty(var2, "maxHeight", Integer.MAX_VALUE);
		this.renderPass = MCPatcherUtils.getIntProperty(var2, "renderPass", -1);

		if (this.renderPass > 3) {
			this.error("renderPass must be 0-3", new Object[0]);
		} else if (this.renderPass >= 0 && !this.matchTiles.isEmpty()) {
			this.error("renderPass=%d must be block-based not tile-based", new Object[] {Integer.valueOf(this.renderPass)});
		}
	}

	private boolean addIcon(String var1) {
		return this.tileLoader.preload(var1, this.tileNames, this.renderPass > 2);
	}

	private void loadIcons(Properties var1) {
		this.tileNames.clear();
		String var2 = var1.getProperty("tiles", "").trim();

		if (var2.equals("")) {
			int var3 = 0;

			while (true) {
				String var4 = this.texturesDirectory + "/" + var3 + ".png";

				if (!TexturePackAPI.hasResource(var4) || !this.addIcon(var4)) {
					break;
				}

				++var3;
			}
		} else {
			Pattern var14 = Pattern.compile("(\\d+)-(\\d+)");
			String[] var15 = var2.split("\\s+");
			int var5 = var15.length;

			for (int var6 = 0; var6 < var5; ++var6) {
				String var7 = var15[var6];
				Matcher var8 = var14.matcher(var7);

				if (!var7.equals("")) {
					if (!var7.equals("null") && !var7.equals("none") && !var7.equals("default")) {
						if (var8.matches()) {
							try {
								int var9 = Integer.parseInt(var8.group(1));
								int var10 = Integer.parseInt(var8.group(2));

								for (int var11 = var9; var11 <= var10; ++var11) {
									String var12 = this.texturesDirectory + "/" + var11 + ".png";
								}
							} catch (NumberFormatException var13) {
								var13.printStackTrace();
							}
						}
					} else {
						this.tileNames.add((Object)null);
					}
				}
			}
		}
	}

	private Set getIDList(Properties var1, String var2, String var3, String[] var4) {
		HashSet var5 = new HashSet();
		String var6 = var1.getProperty(var2, "");
		String[] var7 = var6.split("\\s+");
		int var8 = var7.length;
		label53:

		for (int var9 = 0; var9 < var8; ++var9) {
			String var10 = var7[var9];

			if (!var10.equals("")) {
				int var11;

				if (var10.matches("\\d+")) {
					try {
						var11 = Integer.parseInt(var10);

						if (var11 >= 0 && var11 < var4.length) {
							var5.add(Integer.valueOf(var11));
						}
					} catch (NumberFormatException var13) {
						var13.printStackTrace();
					}
				} else {
					for (var11 = 0; var11 < var4.length; ++var11) {
						if (var10.equals(var4[var11])) {
							var5.add(Integer.valueOf(var11));
							continue label53;
						}
					}
				}
			}
		}

		if (var5.isEmpty()) {
			Matcher var14 = Pattern.compile(var3 + "(\\d+)").matcher(this.propertiesName);

			if (var14.find()) {
				try {
					var5.add(Integer.valueOf(Integer.parseInt(var14.group(1))));
				} catch (NumberFormatException var12) {
					var12.printStackTrace();
				}
			}
		}

		return var5;
	}

	private Set getIDList(Properties var1, String var2) {
		HashSet var3 = new HashSet();
		String var4 = var1.getProperty(var2, "");
		String[] var5 = var4.split("\\s+");
		int var6 = var5.length;

		for (int var7 = 0; var7 < var6; ++var7) {
			String var8 = var5[var7];

			if (!var8.equals("")) {
				var3.add(var8);
			}
		}

		return var3;
	}

	private static int[] add(int[] var0, int[] var1) {
		if (var0.length != var1.length) {
			throw new RuntimeException("arrays to add are not same length");
		} else {
			int[] var2 = new int[var0.length];

			for (int var3 = 0; var3 < var2.length; ++var3) {
				var2[var3] = var0[var3] + var1[var3];
			}

			return var2;
		}
	}

	protected int getNumberOfTiles() {
		return this.tileNames.size();
	}

	String checkTileMap() {
		return null;
	}

	boolean requiresFace() {
		return false;
	}

	public String toString() {
		return String.format("%s[%s]", new Object[] {this.getMethod(), this.propertiesFile});
	}

	public final int getTotalTextureSize() {
		return this.tileLoader.getTextureSize(this.tileNames);
	}

	public final void registerIcons(TextureMap var1, Stitcher var2, Map var3) {
		this.icons = this.tileLoader.registerIcons(var1, var2, var3, this.tileNames);
	}

	final void error(String var1, Object ... var2) {
		this.disabled = true;
	}

	public final boolean isDisabled() {
		return this.disabled;
	}

	public final Set getMatchingBlocks() {
		return this.matchBlocks;
	}

	public final Set getMatchingTiles() {
		return this.matchTiles;
	}

	public final int getRenderPass() {
		return this.renderPass;
	}

	final boolean shouldConnect(IBlockAccess var1, Block var2, Icon var3, int var4, int var5, int var6, int var7, int[] var8) {
		int var9 = var2.blockID;
		int var10 = var1.getBlockMetadata(var4, var5, var6);
		var4 += var8[0];
		var5 += var8[1];
		var6 += var8[2];
		int var11 = var1.getBlockId(var4, var5, var6);
		int var12 = var1.getBlockMetadata(var4, var5, var6);
		Block var13 = Block.blocksList[var11];

		if (this.exclude(var13, var7, var12)) {
			return false;
		} else {
			int var14 = getOrientationFromMetadata(var9, var10);
			int var15 = getOrientationFromMetadata(var11, var12);

			if ((var14 & -65536) != (var15 & -65536)) {
				return false;
			} else if (this.metadata != -1 && (var14 & 65535) != (var15 & 65535)) {
				return false;
			} else {
				if (var7 >= 0 && this.innerSeams) {
					int[] var16 = NORMALS[var7];

					if (!var13.shouldSideBeRendered(var1, var4 + var16[0], var5 + var16[1], var6 + var16[2], var7)) {
						return false;
					}
				}

				switch (this.connectType) {
					case 0:
						return var11 == var9;

					case 1:
						return var13.getBlockTexture(var1, var4, var5, var6, var7) == var3;

					case 2:
						return var2.blockMaterial == var13.blockMaterial;

					default:
						return false;
				}
			}
		}
	}

	final int reorient(int var1) {
		return var1 >= 0 && var1 <= 5 && this.reorient != null ? this.reorient[var1] : var1;
	}

	final int rotateUV(int var1) {
		return var1 + this.rotateUV & 7;
	}

	final boolean exclude(Block var1, int var2, int var3) {
		if (var1 == null) {
			return true;
		} else if ((this.faces & 1 << this.reorient(var2)) == 0) {
			return true;
		} else {
			if (this.metadata != -1 && var3 >= 0 && var3 < 32) {
				int var4 = getOrientationFromMetadata(var1.blockID, var3) & 65535;

				if ((this.metadata & (1 << var3 | 1 << var4)) == 0) {
					return true;
				}
			}

			return false;
		}
	}

	private static int getOrientationFromMetadata(int var0, int var1) {
		int var2 = var1;
		int var3 = 0;

		switch (var0) {
			case 17:
				var2 = var1 & -13;

				switch (var1 & 12) {
					case 4:
						var3 = 65536;
						return var3 | var2;

					case 8:
						var3 = 131072;
						return var3 | var2;

					default:
						return var3 | var2;
				}

			case 155:
				switch (var1) {
					case 3:
						var2 = 2;
						var3 = 196608;
						break;

					case 4:
						var2 = 2;
						var3 = 262144;
				}
		}

		return var3 | var2;
	}

	private void setupOrientation(int var1, int var2) {
		switch (var1 & -65536) {
			case 65536:
				this.reorient = ROTATE_UV_MAP[0];
				this.rotateUV = ROTATE_UV_MAP[0][var2 + 6];
				this.rotateTop = true;
				break;

			case 131072:
				this.reorient = ROTATE_UV_MAP[1];
				this.rotateUV = ROTATE_UV_MAP[1][var2 + 6];
				this.rotateTop = false;
				break;

			case 196608:
				this.reorient = ROTATE_UV_MAP[2];
				this.rotateUV = ROTATE_UV_MAP[2][var2 + 6];
				this.rotateTop = true;
				break;

			case 262144:
				this.reorient = ROTATE_UV_MAP[3];
				this.rotateUV = ROTATE_UV_MAP[3][var2 + 6];
				this.rotateTop = false;
				break;

			default:
				this.reorient = null;
				this.rotateUV = 0;
				this.rotateTop = false;
		}
	}

	public final Icon getTile(IBlockAccess var1, Block var2, Icon var3, int var4, int var5, int var6, int var7) {
		if (this.icons == null) {
			this.error("no images loaded, disabling", new Object[0]);
			return null;
		} else if (var7 < 0 && this.requiresFace()) {
			this.error("method=%s is not supported for non-standard blocks", new Object[] {this.getMethod()});
			return null;
		} else if (var2 != null && !RenderPassAPI.instance.skipThisRenderPass(var2, this.renderPass)) {
			int var8 = var1.getBlockMetadata(var4, var5, var6);
			this.setupOrientation(getOrientationFromMetadata(var2.blockID, var8), var7);

			if (this.exclude(var2, var7, var8)) {
				return null;
			} else if (var5 >= this.minHeight && var5 <= this.maxHeight) {
				if (this.biomes != null && getBiomeNameAt != null) {
					try {
						if (!this.biomes.contains(getBiomeNameAt.invoke((Object)null, new Object[] {Integer.valueOf(var4), Integer.valueOf(var5), Integer.valueOf(var6)}))) {
							return null;
						}
					} catch (Throwable var10) {
						var10.printStackTrace();
						getBiomeNameAt = null;
					}
				}

				return this.getTileImpl(var1, var2, var3, var4, var5, var6, var7);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public final Icon getTile(Block var1, Icon var2, int var3, int var4) {
		if (this.icons == null) {
			this.error("no images loaded, disabling", new Object[0]);
			return null;
		} else if (var3 < 0 && this.requiresFace()) {
			this.error("method=%s is not supported for non-standard blocks", new Object[] {this.getMethod()});
			return null;
		} else if (this.minHeight < 0 && this.maxHeight >= Integer.MAX_VALUE && this.biomes == null) {
			this.setupOrientation(getOrientationFromMetadata(var1.blockID, var4), var3);
			return this.exclude(var1, var3, var4) ? null : this.getTileImpl(var1, var2, var3, var4);
		} else {
			return null;
		}
	}

	abstract String getMethod();

	abstract Icon getTileImpl(IBlockAccess var1, Block var2, Icon var3, int var4, int var5, int var6, int var7);

	abstract Icon getTileImpl(Block var1, Icon var2, int var3, int var4);

	static {
		try {
			Class var0 = Class.forName("com.prupe.mcpatcher.mod.BiomeHelper");
			getBiomeNameAt = var0.getDeclaredMethod("getBiomeNameAt", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE});
		} catch (Throwable var2) {
			;
		}

		try {
			maxBorder = Class.forName("com.prupe.mcpatcher.mod.AAHelper").getDeclaredField("maxBorder");
		} catch (Throwable var1) {
			;
		}
	}
}
