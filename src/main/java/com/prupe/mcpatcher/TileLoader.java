package com.prupe.mcpatcher;

import com.prupe.mcpatcher.TileLoader$1;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Minecraft;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureAtlasSprite;
import net.minecraft.src.TextureMap;

public class TileLoader {
	private static final MCLogger logger = MCLogger.getLogger("Tilesheet");
	private static final List<TileLoader> loaders = new ArrayList();
	private static final ResourceLocation BLANK_RESOURCE = new ResourceLocation("mcpatcher/blank.png");
	private static final boolean debugTextures = Config.getBoolean("Connected Textures", "debugTextures", false);
	private static final int splitTextures = Config.getInt("Connected Textures", "splitTextures", 1);
	private static final Map<String, String> specialTextures = new HashMap();
	private static final TexturePackChangeHandler changeHandler;
	private static boolean changeHandlerCalled;
	private static boolean registerIconsCalled;
	private static final Set<TextureMap> overflowMaps = new HashSet();
	private static final int OVERFLOW_TEXTURE_MAP_INDEX = 2;
	private static final long MAX_TILESHEET_SIZE;
	protected final String mapName;
	protected final boolean allowOverflow;
	protected final MCLogger subLogger;
	private int overflowIndex;
	private TextureMap baseTextureMap;
	private Map<String, TextureAtlasSprite> baseTexturesByName;
	private final Set<ResourceLocation> tilesToRegister = new HashSet();
	private final Map<ResourceLocation, BufferedImage> tileImages = new HashMap();
	private final Map<String, Icon> iconMap = new HashMap();

	public static void registerIcons(TextureMap textureMap, String mapName, Map<String, TextureAtlasSprite> map) {
		logger.fine("before registerIcons(%s) %d icons", new Object[] {mapName, Integer.valueOf(map.size())});
		mapName = mapName.replaceFirst("/$", "");
		registerIconsCalled = true;

		if (!changeHandlerCalled) {
			logger.severe("beforeChange was not called, invoking directly", new Object[0]);
			changeHandler.beforeChange();
		}

		TessellatorUtils.registerTextureMap(textureMap, mapName);
		Iterator i$ = loaders.iterator();

		while (i$.hasNext()) {
			TileLoader loader = (TileLoader)i$.next();

			if (loader.baseTextureMap == null && mapName.equals(loader.mapName)) {
				loader.baseTextureMap = textureMap;
				loader.baseTexturesByName = map;
			}

			if (loader.isForThisMap(mapName) && !loader.tilesToRegister.isEmpty()) {
				loader.subLogger.fine("adding icons to %s (%d remaining)", new Object[] {mapName, Integer.valueOf(loader.tilesToRegister.size()), mapName});

				while (!loader.tilesToRegister.isEmpty() && loader.registerOneIcon(textureMap, mapName, map)) {
					;
				}

				loader.subLogger.fine("done adding icons to %s (%d remaining)", new Object[] {mapName, Integer.valueOf(loader.tilesToRegister.size()), mapName});
			}
		}

		logger.fine("after registerIcons(%s) %d icons", new Object[] {mapName, Integer.valueOf(map.size())});
	}

	public static String getOverridePath(String prefix, String name, String ext) {
		String path;

		if (name.endsWith(".png")) {
			path = name.replaceFirst("\\.[^.]+$", "") + ext;
		} else {
			path = prefix;

			if (!prefix.endsWith("/")) {
				path = prefix + "/";
			}

			path = path + name;
			path = path + ext;
		}

		logger.finer("getOverridePath(%s, %s, %s) -> %s", new Object[] {prefix, name, ext, path});
		return path;
	}

	public static boolean isSpecialTexture(TextureMap map, String texture, String special) {
		return special.equals(texture) || special.equals(specialTextures.get(texture));
	}

	public static BufferedImage getOverrideImage(ResourceLocation resource) throws IOException {
		Iterator i$ = loaders.iterator();
		BufferedImage image;

		do {
			if (!i$.hasNext()) {
				image = TexturePackAPI.getImage(resource);

				if (image == null) {
					throw new FileNotFoundException(resource + " not found");
				}

				return image;
			}

			TileLoader loader = (TileLoader)i$.next();
			image = (BufferedImage)loader.tileImages.get(resource);
		} while (image == null);

		return image;
	}

	public static void updateAnimations() {
		Iterator i$ = overflowMaps.iterator();

		while (i$.hasNext()) {
			TextureMap textureMap = (TextureMap)i$.next();
			textureMap.updateAnimations();
		}
	}

	public static BufferedImage generateDebugTexture(String text, int width, int height, boolean alternate) {
		BufferedImage image = new BufferedImage(width, height, 2);
		Graphics graphics = image.getGraphics();
		graphics.setColor(alternate ? new Color(0, 255, 255, 128) : Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(alternate ? Color.RED : Color.BLACK);
		int ypos = 10;

		if (alternate) {
			ypos += height / 2;
		}

		int charsPerRow = width / 8;

		if (charsPerRow <= 0) {
			return image;
		} else {
			while (text.length() % charsPerRow != 0) {
				text = text + " ";
			}

			while (ypos < height && !text.equals("")) {
				graphics.drawString(text.substring(0, charsPerRow), 1, ypos);
				ypos += graphics.getFont().getSize();
				text = text.substring(charsPerRow);
			}

			return image;
		}
	}

	static void init() {}

	public static ResourceLocation getBlocksAtlas() {
		return TextureMap.locationBlocksTexture;
	}

	public static ResourceLocation getItemsAtlas() {
		return TextureMap.locationItemsTexture;
	}

	public TileLoader(String mapName, boolean allowOverflow, MCLogger logger) {
		this.mapName = mapName;
		this.allowOverflow = allowOverflow;
		this.subLogger = logger;
		loaders.add(this);
	}

	private static long getTextureSize(TextureAtlasSprite texture) {
		return texture == null ? 0L : (long)(4 * texture.getIconWidth() * texture.getIconHeight());
	}

	private static long getTextureSize(Collection<TextureAtlasSprite> textures) {
		long size = 0L;
		TextureAtlasSprite texture;

		for (Iterator i$ = textures.iterator(); i$.hasNext(); size += getTextureSize(texture)) {
			texture = (TextureAtlasSprite)i$.next();
		}

		return size;
	}

	public static ResourceLocation getDefaultAddress(ResourceLocation propertiesAddress) {
		return TexturePackAPI.transformResourceLocation(propertiesAddress, ".properties", ".png");
	}

	public static ResourceLocation parseTileAddress(ResourceLocation propertiesAddress, String value) {
		if (value == null) {
			return null;
		} else if (value.equals("blank")) {
			return BLANK_RESOURCE;
		} else if (!value.equals("null") && !value.equals("none") && !value.equals("default") && !value.equals("")) {
			if (!value.endsWith(".png")) {
				value = value + ".png";
			}

			if (!value.contains("/")) {
				value = propertiesAddress.getResourcePath().replaceFirst("[^/]+$", "") + value;
			}

			return TexturePackAPI.parseResourceLocation(propertiesAddress, value);
		} else {
			return null;
		}
	}

	public boolean preloadTile(ResourceLocation resource, boolean alternate, String special) {
		if (this.tileImages.containsKey(resource)) {
			return true;
		} else {
			BufferedImage image = null;

			if (!debugTextures) {
				image = TexturePackAPI.getImage(resource);

				if (image == null) {
					this.subLogger.warning("missing %s", new Object[] {resource});
				}
			}

			if (image == null) {
				image = generateDebugTexture(resource.getResourcePath(), 64, 64, alternate);
			}

			this.tilesToRegister.add(resource);
			this.tileImages.put(resource, image);

			if (special != null) {
				specialTextures.put(resource.toString(), special);
			}

			return true;
		}
	}

	public boolean preloadTile(ResourceLocation resource, boolean alternate) {
		return this.preloadTile(resource, alternate, (String)null);
	}

	protected boolean isForThisMap(String mapName) {
		return this.allowOverflow && splitTextures > 1 ? mapName.startsWith(this.mapName + "_overflow") : mapName.startsWith(this.mapName);
	}

	private boolean registerDefaultIcon(String name) {
		if (name.startsWith(this.mapName) && name.endsWith(".png") && this.baseTextureMap != null) {
			String defaultName = name.substring(this.mapName.length()).replaceFirst("\\.png$", "");
			TextureAtlasSprite texture = (TextureAtlasSprite)this.baseTexturesByName.get(defaultName);

			if (texture != null) {
				this.subLogger.finer("%s -> existing icon %s", new Object[] {name, defaultName});
				this.iconMap.put(name, texture);
				return true;
			}
		}

		return false;
	}

	private boolean registerOneIcon(TextureMap textureMap, String mapName, Map<String, TextureAtlasSprite> map) {
		ResourceLocation resource = (ResourceLocation)this.tilesToRegister.iterator().next();
		String name = resource.toString();

		if (this.registerDefaultIcon(name)) {
			this.tilesToRegister.remove(resource);
			return true;
		} else {
			BufferedImage image = (BufferedImage)this.tileImages.get(resource);

			if (image == null) {
				this.subLogger.error("tile for %s unexpectedly missing", new Object[] {resource});
				this.tilesToRegister.remove(resource);
				return true;
			} else {
				int width = image.getWidth();
				int height = image.getHeight();
				long currentSize = getTextureSize(map.values());
				long newSize = (long)(4 * width * width);

				if (newSize + currentSize > MAX_TILESHEET_SIZE) {
					float icon1 = (float)currentSize / 1048576.0F;

					if (currentSize <= 0L) {
						this.subLogger.error("%s too big for any tilesheet (%.1fMB), dropping", new Object[] {name, Float.valueOf(icon1)});
						this.tilesToRegister.remove(resource);
						return true;
					} else {
						this.subLogger.warning("%s nearly full (%.1fMB), will start a new tilesheet", new Object[] {mapName, Float.valueOf(icon1)});
						return false;
					}
				} else {
					Icon icon = textureMap.registerIcon(name);
					map.put(name, (TextureAtlasSprite)icon);

					if (mapName.contains("_overflow")) {
						TessellatorUtils.registerIcon(textureMap, icon);
					}

					this.iconMap.put(name, icon);
					String extra = width == height ? "" : ", " + height / width + " frames";
					this.subLogger.finer("%s -> %s icon %dx%d%s", new Object[] {name, mapName, Integer.valueOf(width), Integer.valueOf(width), extra});
					this.tilesToRegister.remove(resource);
					return true;
				}
			}
		}
	}

	public void finish() {
		this.tilesToRegister.clear();
		this.tileImages.clear();
	}

	public Icon getIcon(String name) {
		if (name != null && !name.equals("")) {
			Icon icon = (Icon)this.iconMap.get(name);

			if (icon == null && this.baseTexturesByName != null) {
				icon = (Icon)this.baseTexturesByName.get(name);
			}

			return icon;
		} else {
			return null;
		}
	}

	public Icon getIcon(ResourceLocation resource) {
		return resource == null ? null : this.getIcon(resource.toString());
	}

	public boolean setDefaultTextureMap(Tessellator tessellator) {
		tessellator.textureMap = this.baseTextureMap;
		return this.baseTextureMap != null;
	}

	static boolean access$002(boolean x0) {
		changeHandlerCalled = x0;
		return x0;
	}

	static Set access$100() {
		return overflowMaps;
	}

	static List access$200() {
		return loaders;
	}

	static Map access$300() {
		return specialTextures;
	}

	static Set access$400(TileLoader x0) {
		return x0.tilesToRegister;
	}

	static int access$500() {
		return splitTextures;
	}

	static boolean access$602(boolean x0) {
		registerIconsCalled = x0;
		return x0;
	}

	static int access$704(TileLoader x0) {
		return ++x0.overflowIndex;
	}

	static MCLogger access$800() {
		return logger;
	}

	static boolean access$600() {
		return registerIconsCalled;
	}

	static {
		long maxSize = 4096L;

		try {
			maxSize = (long)Minecraft.getGLMaximumTextureSize();
		} catch (Throwable var3) {
			var3.printStackTrace();
		}

		MAX_TILESHEET_SIZE = maxSize * maxSize * 4L * 7L / 8L;
		logger.config("max texture size is %dx%d (%.1fMB)", new Object[] {Long.valueOf(maxSize), Long.valueOf(maxSize), Float.valueOf((float)MAX_TILESHEET_SIZE / 1048576.0F)});
		changeHandler = new TileLoader$1("Tilesheet API", 2);
		TexturePackChangeHandler.register(changeHandler);
	}
}
