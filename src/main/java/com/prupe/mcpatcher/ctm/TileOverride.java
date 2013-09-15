package com.prupe.mcpatcher.ctm;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TileLoader;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.ResourceLocation;

abstract class TileOverride implements ITileOverride {
	private static final MCLogger logger = MCLogger.getLogger("Connected Textures", "CTM");
	static final int BOTTOM_FACE = 0;
	static final int TOP_FACE = 1;
	static final int NORTH_FACE = 2;
	static final int SOUTH_FACE = 3;
	static final int WEST_FACE = 4;
	static final int EAST_FACE = 5;
	static final int REL_L = 0;
	static final int REL_DL = 1;
	static final int REL_D = 2;
	static final int REL_DR = 3;
	static final int REL_R = 4;
	static final int REL_UR = 5;
	static final int REL_U = 6;
	static final int REL_UL = 7;
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
	private final ResourceLocation propertiesFile;
	private final String texturesDirectory;
	private final String baseFilename;
	private final TileLoader tileLoader;
	private final int renderPass;
	private final int weight;
	private final Set<Integer> matchBlocks;
	private final Set<String> matchTiles;
	private final int faces;
	private final int metadata;
	private final int connectType;
	private final boolean innerSeams;
	private final Set<String> biomes;
	private final int minHeight;
	private final int maxHeight;
	private final List<ResourceLocation> tileNames = new ArrayList();
	protected Icon[] icons;
	private boolean disabled;
	private int[] reorient;
	private int rotateUV;
	protected boolean rotateTop;

	static TileOverride create(ResourceLocation propertiesFile, TileLoader tileLoader) {
		if (propertiesFile == null) {
			return null;
		} else {
			Properties properties = TexturePackAPI.getProperties(propertiesFile);

			if (properties == null) {
				return null;
			} else {
				String method = properties.getProperty("method", "default").trim().toLowerCase();
				Object override = null;

				if (!method.equals("default") && !method.equals("glass") && !method.equals("ctm")) {
					if (method.equals("random")) {
						override = new TileOverrideImpl$Random1(propertiesFile, properties, tileLoader);

						if (((TileOverride)override).getNumberOfTiles() == 1) {
							override = new TileOverrideImpl$Fixed(propertiesFile, properties, tileLoader);
						}
					} else if (!method.equals("fixed") && !method.equals("static")) {
						if (!method.equals("bookshelf") && !method.equals("horizontal")) {
							if (!method.equals("horizontal+vertical") && !method.equals("h+v")) {
								if (method.equals("vertical")) {
									override = new TileOverrideImpl$Vertical(propertiesFile, properties, tileLoader);
								} else if (!method.equals("vertical+horizontal") && !method.equals("v+h")) {
									if (!method.equals("sandstone") && !method.equals("top")) {
										if (!method.equals("repeat") && !method.equals("pattern")) {
											logger.error("%s: unknown method \"%s\"", new Object[] {propertiesFile, method});
										} else {
											override = new TileOverrideImpl$Repeat(propertiesFile, properties, tileLoader);
										}
									} else {
										override = new TileOverrideImpl$Top(propertiesFile, properties, tileLoader);
									}
								} else {
									override = new TileOverrideImpl$VerticalHorizontal(propertiesFile, properties, tileLoader);
								}
							} else {
								override = new TileOverrideImpl$HorizontalVertical(propertiesFile, properties, tileLoader);
							}
						} else {
							override = new TileOverrideImpl$Horizontal(propertiesFile, properties, tileLoader);
						}
					} else {
						override = new TileOverrideImpl$Fixed(propertiesFile, properties, tileLoader);
					}
				} else {
					override = new TileOverrideImpl$CTM(propertiesFile, properties, tileLoader);
				}

				if (override != null && !((TileOverride)override).disabled) {
					String status = ((TileOverride)override).checkTileMap();

					if (status != null) {
						((TileOverride)override).error("invalid %s tile map: %s", new Object[] {((TileOverride)override).getMethod(), status});
					}
				}

				return (TileOverride)(override != null && !((TileOverride)override).disabled ? override : null);
			}
		}
	}

	protected TileOverride(ResourceLocation propertiesFile, Properties properties, TileLoader tileLoader) {
		this.propertiesFile = propertiesFile;
		this.texturesDirectory = propertiesFile.func_110623_a().replaceFirst("/[^/]*$", "");
		this.baseFilename = propertiesFile.func_110623_a().replaceFirst(".*/", "").replaceFirst("\\.properties$", "");
		this.tileLoader = tileLoader;
		this.loadIcons(properties);

		if (this.tileNames.isEmpty()) {
			this.error("no images found in %s/", new Object[] {this.texturesDirectory});
		}

		String[] mappings = new String[Block.blocksList.length];
		int flags;

		for (flags = 0; flags < Block.blocksList.length; ++flags) {
			Block meta = Block.blocksList[flags];

			if (meta != null) {
				mappings[flags] = meta.getUnlocalizedName();
			}
		}

		this.matchBlocks = this.getIDList(properties, "matchBlocks", "block", mappings);
		this.matchTiles = this.getIDList(properties, "matchTiles");

		if (this.matchBlocks.isEmpty() && this.matchTiles.isEmpty()) {
			this.matchTiles.add(this.baseFilename);
		}

		flags = 0;
		String[] var12 = properties.getProperty("faces", "all").trim().toLowerCase().split("\\s+");
		int connectType1 = var12.length;
		int biomes;
		String biomeList;

		for (biomes = 0; biomes < connectType1; ++biomes) {
			biomeList = var12[biomes];

			if (biomeList.equals("bottom")) {
				flags |= 1;
			} else if (biomeList.equals("top")) {
				flags |= 2;
			} else if (biomeList.equals("north")) {
				flags |= 4;
			} else if (biomeList.equals("south")) {
				flags |= 8;
			} else if (biomeList.equals("east")) {
				flags |= 32;
			} else if (biomeList.equals("west")) {
				flags |= 16;
			} else if (!biomeList.equals("side") && !biomeList.equals("sides")) {
				if (biomeList.equals("all")) {
					flags = -1;
				}
			} else {
				flags |= 60;
			}
		}

		this.faces = flags;
		int var11 = 0;
		int[] var13 = MCPatcherUtils.parseIntegerList(properties.getProperty("metadata", "0-31"), 0, 31);
		biomes = var13.length;

		for (int var15 = 0; var15 < biomes; ++var15) {
			int i = var13[var15];
			var11 |= 1 << i;
		}

		this.metadata = var11;
		String var14 = properties.getProperty("connect", "").trim().toLowerCase();

		if (var14.equals("")) {
			this.connectType = this.matchTiles.isEmpty() ? 0 : 1;
		} else if (var14.equals("block")) {
			this.connectType = 0;
		} else if (var14.equals("tile")) {
			this.connectType = 1;
		} else if (var14.equals("material")) {
			this.connectType = 2;
		} else {
			this.error("invalid connect type %s", new Object[] {var14});
			this.connectType = 0;
		}

		this.innerSeams = MCPatcherUtils.getBooleanProperty(properties, "innerSeams", false);
		HashSet var16 = new HashSet();
		biomeList = properties.getProperty("biomes", "").trim().toLowerCase();

		if (!biomeList.equals("")) {
			Collections.addAll(var16, biomeList.split("\\s+"));
		}

		if (var16.isEmpty()) {
			var16 = null;
		}

		this.biomes = var16;
		this.minHeight = MCPatcherUtils.getIntProperty(properties, "minHeight", -1);
		this.maxHeight = MCPatcherUtils.getIntProperty(properties, "maxHeight", Integer.MAX_VALUE);
		this.renderPass = MCPatcherUtils.getIntProperty(properties, "renderPass", -1);

		if (this.renderPass > 3) {
			this.error("renderPass must be 0-3", new Object[0]);
		} else if (this.renderPass >= 0 && !this.matchTiles.isEmpty()) {
			this.error("renderPass=%d must be block-based not tile-based", new Object[] {Integer.valueOf(this.renderPass)});
		}

		this.weight = MCPatcherUtils.getIntProperty(properties, "weight", 0);
	}

	private boolean addIcon(ResourceLocation resource) {
		this.tileNames.add(resource);
		return this.tileLoader.preloadTile(resource, this.renderPass > 2);
	}

	private void loadIcons(Properties properties) {
		this.tileNames.clear();
		String tileList = properties.getProperty("tiles", "").trim();

		if (tileList.equals("")) {
			int range = 0;

			while (true) {
				ResourceLocation arr$ = TileLoader.parseTileAddress(this.propertiesFile, String.valueOf(range));

				if (!TexturePackAPI.hasResource(arr$) || !this.addIcon(arr$)) {
					break;
				}

				++range;
			}
		} else {
			Pattern var14 = Pattern.compile("(\\d+)-(\\d+)");
			String[] var15 = tileList.split("\\s+");
			int len$ = var15.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				String token = var15[i$];
				Matcher matcher = var14.matcher(token);

				if (!token.equals("")) {
					if (matcher.matches()) {
						try {
							int resource = Integer.parseInt(matcher.group(1));
							int to = Integer.parseInt(matcher.group(2));

							for (int i = resource; i <= to; ++i) {
								ResourceLocation resource1 = TileLoader.parseTileAddress(this.propertiesFile, String.valueOf(i));

								if (TexturePackAPI.hasResource(resource1)) {
									this.addIcon(resource1);
								} else {
									this.warn("could not find image %s", new Object[] {resource1});
								}
							}
						} catch (NumberFormatException var13) {
							var13.printStackTrace();
						}
					} else {
						ResourceLocation var16 = TileLoader.parseTileAddress(this.propertiesFile, token);

						if (var16 == null) {
							this.tileNames.add((ResourceLocation)null);
						} else if (TexturePackAPI.hasResource(var16)) {
							this.addIcon(var16);
						} else {
							this.warn("could not find image %s", new Object[] {var16});
						}
					}
				}
			}
		}
	}

	private Set<Integer> getIDList(Properties properties, String key, String type, String[] mappings) {
		HashSet list = new HashSet();
		String property = properties.getProperty(key, "");
		String[] m = property.split("\\s+");
		int e = m.length;
		label53:

		for (int i$ = 0; i$ < e; ++i$) {
			String token = m[i$];

			if (!token.equals("")) {
				int i;

				if (token.matches("\\d+")) {
					try {
						i = Integer.parseInt(token);

						if (i >= 0 && i < mappings.length) {
							list.add(Integer.valueOf(i));
						} else {
							this.warn("%s value %d is out of range", new Object[] {key, Integer.valueOf(i)});
						}
					} catch (NumberFormatException var13) {
						var13.printStackTrace();
					}
				} else {
					for (i = 0; i < mappings.length; ++i) {
						if (token.equals(mappings[i])) {
							list.add(Integer.valueOf(i));
							continue label53;
						}
					}

					this.warn("unknown %s value %s", new Object[] {key, token});
				}
			}
		}

		if (list.isEmpty()) {
			Matcher var14 = Pattern.compile(type + "(\\d+)").matcher(this.baseFilename);

			if (var14.find()) {
				try {
					list.add(Integer.valueOf(Integer.parseInt(var14.group(1))));
				} catch (NumberFormatException var12) {
					var12.printStackTrace();
				}
			}
		}

		return list;
	}

	private Set<String> getIDList(Properties properties, String key) {
		HashSet list = new HashSet();
		String property = properties.getProperty(key, "");
		String[] arr$ = property.split("\\s+");
		int len$ = arr$.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			String token = arr$[i$];

			if (!token.equals("")) {
				if (token.contains("/")) {
					if (!token.endsWith(".png")) {
						token = token + ".png";
					}

					ResourceLocation resource = TexturePackAPI.parseResourceLocation(this.propertiesFile, token);

					if (resource != null) {
						list.add(resource.toString());
					}
				} else {
					list.add(token);
				}
			}
		}

		return list;
	}

	private static int[] add(int[] a, int[] b) {
		if (a.length != b.length) {
			throw new RuntimeException("arrays to add are not same length");
		} else {
			int[] c = new int[a.length];

			for (int i = 0; i < c.length; ++i) {
				c[i] = a[i] + b[i];
			}

			return c;
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

	public final void registerIcons() {
		this.icons = new Icon[this.tileNames.size()];

		for (int i = 0; i < this.icons.length; ++i) {
			this.icons[i] = this.tileLoader.getIcon((ResourceLocation)this.tileNames.get(i));
		}
	}

	final void error(String format, Object ... params) {
		if (this.propertiesFile != null) {
			logger.error(this.propertiesFile + ": " + format, params);
		}

		this.disabled = true;
	}

	final void warn(String format, Object ... params) {
		if (this.propertiesFile != null) {
			logger.warning(this.propertiesFile + ": " + format, params);
		}
	}

	public final boolean isDisabled() {
		return this.disabled;
	}

	public final Set<Integer> getMatchingBlocks() {
		return this.matchBlocks;
	}

	public final Set<String> getMatchingTiles() {
		return this.matchTiles;
	}

	public final int getRenderPass() {
		return this.renderPass;
	}

	public final int getWeight() {
		return this.weight;
	}

	public int compareTo(ITileOverride o) {
		int result = o.getWeight() - this.getWeight();
		return result != 0 ? result : (o instanceof TileOverride ? this.baseFilename.compareTo(((TileOverride)o).baseFilename) : -1);
	}

	final boolean shouldConnect(IBlockAccess blockAccess, Block block, Icon icon, int i, int j, int k, int face, int[] offset) {
		int blockID = block.blockID;
		int metadata = blockAccess.getBlockMetadata(i, j, k);
		i += offset[0];
		j += offset[1];
		k += offset[2];
		int neighborID = blockAccess.getBlockId(i, j, k);
		int neighborMeta = blockAccess.getBlockMetadata(i, j, k);
		Block neighbor = Block.blocksList[neighborID];

		if (this.exclude(neighbor, face, neighborMeta)) {
			return false;
		} else {
			int orientation = getOrientationFromMetadata(blockID, metadata);
			int neighborOrientation = getOrientationFromMetadata(neighborID, neighborMeta);

			if ((orientation & -65536) != (neighborOrientation & -65536)) {
				return false;
			} else if (this.metadata != -1 && (orientation & 65535) != (neighborOrientation & 65535)) {
				return false;
			} else {
				if (face >= 0 && this.innerSeams) {
					int[] normal = NORMALS[face];

					if (!neighbor.shouldSideBeRendered(blockAccess, i + normal[0], j + normal[1], k + normal[2], face)) {
						return false;
					}
				}

				switch (this.connectType) {
					case 0:
						return neighborID == blockID;

					case 1:
						return neighbor.getBlockTexture(blockAccess, i, j, k, face) == icon;

					case 2:
						return block.blockMaterial == neighbor.blockMaterial;

					default:
						return false;
				}
			}
		}
	}

	final int reorient(int face) {
		return face >= 0 && face <= 5 && this.reorient != null ? this.reorient[face] : face;
	}

	final int rotateUV(int neighbor) {
		return neighbor + this.rotateUV & 7;
	}

	final boolean exclude(Block block, int face, int metadata) {
		if (block == null) {
			return true;
		} else if ((this.faces & 1 << this.reorient(face)) == 0) {
			return true;
		} else {
			if (this.metadata != -1 && metadata >= 0 && metadata < 32) {
				int altMetadata = getOrientationFromMetadata(block.blockID, metadata) & 65535;

				if ((this.metadata & (1 << metadata | 1 << altMetadata)) == 0) {
					return true;
				}
			}

			return false;
		}
	}

	private static int getOrientationFromMetadata(int blockID, int metadata) {
		int newMeta = metadata;
		int orientation = 0;

		switch (blockID) {
			case 17:
				newMeta = metadata & -13;

				switch (metadata & 12) {
					case 4:
						orientation = 65536;
						return orientation | newMeta;

					case 8:
						orientation = 131072;
						return orientation | newMeta;

					default:
						return orientation | newMeta;
				}

			case 155:
				switch (metadata) {
					case 3:
						newMeta = 2;
						orientation = 196608;
						break;

					case 4:
						newMeta = 2;
						orientation = 262144;
				}
		}

		return orientation | newMeta;
	}

	private void setupOrientation(int orientation, int face) {
		switch (orientation & -65536) {
			case 65536:
				this.reorient = ROTATE_UV_MAP[0];
				this.rotateUV = ROTATE_UV_MAP[0][face + 6];
				this.rotateTop = true;
				break;

			case 131072:
				this.reorient = ROTATE_UV_MAP[1];
				this.rotateUV = ROTATE_UV_MAP[1][face + 6];
				this.rotateTop = false;
				break;

			case 196608:
				this.reorient = ROTATE_UV_MAP[2];
				this.rotateUV = ROTATE_UV_MAP[2][face + 6];
				this.rotateTop = true;
				break;

			case 262144:
				this.reorient = ROTATE_UV_MAP[3];
				this.rotateUV = ROTATE_UV_MAP[3][face + 6];
				this.rotateTop = false;
				break;

			default:
				this.reorient = null;
				this.rotateUV = 0;
				this.rotateTop = false;
		}
	}

	public final Icon getTile(IBlockAccess blockAccess, Block block, Icon origIcon, int i, int j, int k, int face) {
		if (this.icons == null) {
			this.error("no images loaded, disabling", new Object[0]);
			return null;
		} else if (face < 0 && this.requiresFace()) {
			this.error("method=%s is not supported for non-standard blocks", new Object[] {this.getMethod()});
			return null;
		} else if (block != null && !RenderPassAPI.instance.skipThisRenderPass(block, this.renderPass)) {
			int metadata = blockAccess.getBlockMetadata(i, j, k);
			this.setupOrientation(getOrientationFromMetadata(block.blockID, metadata), face);

			if (this.exclude(block, face, metadata)) {
				return null;
			} else if (j >= this.minHeight && j <= this.maxHeight) {
				if (this.biomes != null && getBiomeNameAt != null) {
					try {
						if (!this.biomes.contains(getBiomeNameAt.invoke((Object)null, new Object[] {Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k)}))) {
							return null;
						}
					} catch (Throwable var10) {
						var10.printStackTrace();
						getBiomeNameAt = null;
					}
				}

				return this.getTileImpl(blockAccess, block, origIcon, i, j, k, face);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public final Icon getTile(Block block, Icon origIcon, int face, int metadata) {
		if (this.icons == null) {
			this.error("no images loaded, disabling", new Object[0]);
			return null;
		} else if (face < 0 && this.requiresFace()) {
			this.error("method=%s is not supported for non-standard blocks", new Object[] {this.getMethod()});
			return null;
		} else if (this.minHeight < 0 && this.maxHeight >= Integer.MAX_VALUE && this.biomes == null) {
			this.setupOrientation(getOrientationFromMetadata(block.blockID, metadata), face);
			return this.exclude(block, face, metadata) ? null : this.getTileImpl(block, origIcon, face, metadata);
		} else {
			return null;
		}
	}

	abstract String getMethod();

	abstract Icon getTileImpl(IBlockAccess var1, Block var2, Icon var3, int var4, int var5, int var6, int var7);

	abstract Icon getTileImpl(Block var1, Icon var2, int var3, int var4);
	
	static {
		try {
			Class e = Class.forName("com.prupe.mcpatcher.cc.BiomeHelper");
			getBiomeNameAt = e.getDeclaredMethod("getBiomeNameAt", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE});
			getBiomeNameAt.setAccessible(true);
		} catch (Throwable var1) {
			;
		}

		if (getBiomeNameAt == null) {
			logger.warning("biome integration failed", new Object[0]);
		} else {
			logger.fine("biome integration active", new Object[0]);
		}
	}
}
