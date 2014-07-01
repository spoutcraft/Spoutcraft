package com.prupe.mcpatcher.hd;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.hd.CustomAnimation$1;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import net.minecraft.src.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class CustomAnimation implements Comparable<CustomAnimation> {
	private static final MCLogger logger = MCLogger.getLogger("Custom Animations", "Animation");
	private static final boolean enable = Config.getBoolean("Extended HD", "animations", true);
	private static final Map<ResourceLocation, Properties> pending = new HashMap();
	private static final List<CustomAnimation> animations = new ArrayList();
	private final ResourceLocation propertiesName;
	private final ResourceLocation dstName;
	private final ResourceLocation srcName;
	private final int mipmapLevel;
	private final ByteBuffer imageData;
	private final int x;
	private final int y;
	private final int w;
	private final int h;
	private int currentFrame;
	private int currentDelay;
	private int numFrames;
	private int[] tileOrder;
	private int[] tileDelay;
	private final int numTiles;
	private boolean error;

	public static void updateAll() {
		if (!pending.isEmpty()) {
			try {
				checkPendingAnimations();
			} catch (Throwable var2) {
				var2.printStackTrace();
				logger.error("%d remaining animations cleared", new Object[] {Integer.valueOf(pending.size())});
				pending.clear();
			}
		}

		Iterator i$ = animations.iterator();

		while (i$.hasNext()) {
			CustomAnimation animation = (CustomAnimation)i$.next();
			animation.update();
		}
	}

	private static void checkPendingAnimations() {
		ArrayList done = new ArrayList();
		Iterator i$ = pending.entrySet().iterator();

		while (i$.hasNext()) {
			Entry name = (Entry)i$.next();
			ResourceLocation name1 = (ResourceLocation)name.getKey();
			Properties properties = (Properties)name.getValue();
			ResourceLocation textureName = TexturePackAPI.parseResourceLocation(name1, MCPatcherUtils.getStringProperty(properties, "to", ""));

			if (TexturePackAPI.isTextureLoaded(textureName)) {
				addStrip(name1, properties);
				done.add(name1);
			}
		}

		if (!done.isEmpty()) {
			i$ = done.iterator();

			while (i$.hasNext()) {
				ResourceLocation name2 = (ResourceLocation)i$.next();
				pending.remove(name2);
			}

			Collections.sort(animations);
		}
	}

	private static void addStrip(ResourceLocation propertiesName, Properties properties) {
		ResourceLocation dstName = TexturePackAPI.parseResourceLocation(propertiesName, properties.getProperty("to", ""));

		if (dstName == null) {
			logger.error("%s: missing to= property", new Object[0]);
		} else {
			ResourceLocation srcName = TexturePackAPI.parseResourceLocation(propertiesName, properties.getProperty("from", ""));

			if (srcName == null) {
				logger.error("%s: missing from= property", new Object[0]);
			} else {
				BufferedImage srcImage = TexturePackAPI.getImage(srcName);

				if (srcImage == null) {
					logger.error("%s: image %s not found in texture pack", new Object[] {propertiesName, srcName});
				} else {
					int x = MCPatcherUtils.getIntProperty(properties, "x", 0);
					int y = MCPatcherUtils.getIntProperty(properties, "y", 0);
					int w = MCPatcherUtils.getIntProperty(properties, "w", 0);
					int h = MCPatcherUtils.getIntProperty(properties, "h", 0);

					if (dstName.toString().startsWith("minecraft:textures/atlas/")) {
						logger.error("%s: animations cannot have a target of %s", new Object[] {dstName});
					} else if (x >= 0 && y >= 0 && w > 0 && h > 0) {
						TexturePackAPI.bindTexture(dstName);
						int dstWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
						int dstHeight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
						int levels = MipmapHelper.getMipmapLevelsForCurrentTexture();

						if (x + w <= dstWidth && y + h <= dstHeight) {
							int width = srcImage.getWidth();
							int height = srcImage.getHeight();

							if (width != w) {
								srcImage = resizeImage(srcImage, w);
								width = srcImage.getWidth();
								height = srcImage.getHeight();
							}

							if (width == w && height >= h) {
								ByteBuffer imageData = ByteBuffer.allocateDirect(4 * width * height);
								int[] argb = new int[width * height];
								byte[] rgba = new byte[4 * width * height];
								srcImage.getRGB(0, 0, width, height, argb, 0, width);
								ARGBtoRGBA(argb, rgba);
								imageData.put(rgba).flip();

								for (int mipmapLevel = 0; mipmapLevel <= levels; ++mipmapLevel) {
									add(new CustomAnimation(propertiesName, srcName, dstName, mipmapLevel, x, y, w, h, imageData, height / h, properties));

									if (((x | y | w | h) & 1) != 0 || w <= 0 || h <= 0) {
										break;
									}

									ByteBuffer newImage = ByteBuffer.allocateDirect(width * height);
									MipmapHelper.scaleHalf(imageData.asIntBuffer(), width, height, newImage.asIntBuffer(), 0);
									imageData = newImage;
									width >>= 1;
									height >>= 1;
									x >>= 1;
									y >>= 1;
									w >>= 1;
									h >>= 1;
								}
							} else {
								logger.error("%s: %s dimensions %dx%d do not match %dx%d", new Object[] {propertiesName, srcName, Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(w), Integer.valueOf(h)});
							}
						} else {
							logger.error("%s: %s dimensions x=%d,y=%d,w=%d,h=%d exceed %s size %dx%d", new Object[] {propertiesName, srcName, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(w), Integer.valueOf(h), dstName, Integer.valueOf(dstWidth), Integer.valueOf(dstHeight)});
						}
					} else {
						logger.error("%s: %s has invalid dimensions x=%d,y=%d,w=%d,h=%d", new Object[] {propertiesName, srcName, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(w), Integer.valueOf(h)});
					}
				}
			}
		}
	}

	private static void add(CustomAnimation animation) {
		if (animation != null) {
			animations.add(animation);

			if (animation.mipmapLevel == 0) {
				logger.fine("new %s", new Object[] {animation});
			}
		}
	}

	private CustomAnimation(ResourceLocation propertiesName, ResourceLocation srcName, ResourceLocation dstName, int mipmapLevel, int x, int y, int w, int h, ByteBuffer imageData, int numFrames, Properties properties) {
		this.propertiesName = propertiesName;
		this.srcName = srcName;
		this.dstName = dstName;
		this.mipmapLevel = mipmapLevel;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.imageData = imageData;
		this.numFrames = numFrames;
		this.currentFrame = -1;
		this.numTiles = numFrames;
		this.loadProperties(properties);
	}

	void update() {
		if (!this.error) {
			int texture = TexturePackAPI.getTextureIfLoaded(this.dstName);

			if (texture >= 0) {
				if (--this.currentDelay <= 0) {
					if (++this.currentFrame >= this.numFrames) {
						this.currentFrame = 0;
					}

					TexturePackAPI.bindTexture(texture);
					this.update(texture, 0, 0);
					int glError = GL11.glGetError();

					if (glError != 0) {
						logger.severe("%s: %s", new Object[] {this, GLU.gluErrorString(glError)});
						this.error = true;
					} else {
						this.currentDelay = this.getDelay();
					}
				}
			}
		}
	}

	public int compareTo(CustomAnimation o) {
		return this.dstName.toString().compareTo(o.dstName.toString());
	}

	public String toString() {
		return String.format("CustomAnimation{%s %s %dx%d -> %s%s @ %d,%d (%d frames)}", new Object[] {this.propertiesName, this.srcName, Integer.valueOf(this.w), Integer.valueOf(this.h), this.dstName, this.mipmapLevel > 0 ? "#" + this.mipmapLevel : "", Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.numFrames)});
	}

	private static void ARGBtoRGBA(int[] src, byte[] dest) {
		for (int i = 0; i < src.length; ++i) {
			int v = src[i];
			dest[i * 4 + 3] = (byte)(v >> 24 & 255);
			dest[i * 4 + 0] = (byte)(v >> 16 & 255);
			dest[i * 4 + 1] = (byte)(v >> 8 & 255);
			dest[i * 4 + 2] = (byte)(v >> 0 & 255);
		}
	}

	private static BufferedImage resizeImage(BufferedImage image, int width) {
		if (width == image.getWidth()) {
			return image;
		} else {
			int height = image.getHeight() * width / image.getWidth();
			logger.finer("resizing to %dx%d", new Object[] {Integer.valueOf(width), Integer.valueOf(height)});
			BufferedImage newImage = new BufferedImage(width, height, 2);
			Graphics2D graphics2D = newImage.createGraphics();
			graphics2D.drawImage(image, 0, 0, width, height, (ImageObserver)null);
			return newImage;
		}
	}

	private void loadProperties(Properties properties) {
		this.loadTileOrder(properties);
		int i;

		if (this.tileOrder == null) {
			this.tileOrder = new int[this.numFrames];

			for (i = 0; i < this.numFrames; ++i) {
				this.tileOrder[i] = i % this.numTiles;
			}
		}

		this.tileDelay = new int[this.numFrames];
		this.loadTileDelay(properties);

		for (i = 0; i < this.numFrames; ++i) {
			this.tileDelay[i] = Math.max(this.tileDelay[i], 1);
		}
	}

	private void loadTileOrder(Properties properties) {
		if (properties != null) {
			int i;

			for (i = 0; getIntValue(properties, "tile.", i) != null; ++i) {
				;
			}

			if (i > 0) {
				this.numFrames = i;
				this.tileOrder = new int[this.numFrames];

				for (i = 0; i < this.numFrames; ++i) {
					this.tileOrder[i] = Math.abs(getIntValue(properties, "tile.", i).intValue()) % this.numTiles;
				}
			}
		}
	}

	private void loadTileDelay(Properties properties) {
		if (properties != null) {
			Integer defaultValue = getIntValue(properties, "duration");

			for (int i = 0; i < this.numFrames; ++i) {
				Integer value = getIntValue(properties, "duration.", i);

				if (value != null) {
					this.tileDelay[i] = value.intValue();
				} else if (defaultValue != null) {
					this.tileDelay[i] = defaultValue.intValue();
				}
			}
		}
	}

	private static Integer getIntValue(Properties properties, String key) {
		try {
			String e = properties.getProperty(key);

			if (e != null && e.matches("^\\d+$")) {
				return Integer.valueOf(Integer.parseInt(e));
			}
		} catch (NumberFormatException var3) {
			;
		}

		return null;
	}

	private static Integer getIntValue(Properties properties, String prefix, int index) {
		return getIntValue(properties, prefix + index);
	}

	private void update(int texture, int dx, int dy) {
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, this.mipmapLevel, this.x + dx, this.y + dy, this.w, this.h, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData.position(4 * this.w * this.h * this.tileOrder[this.currentFrame]));
	}

	private int getDelay() {
		return this.tileDelay[this.currentFrame];
	}

	static Map access$000() {
		return pending;
	}

	static MCLogger access$100() {
		return logger;
	}

	static List access$200() {
		return animations;
	}

	static boolean access$300() {
		return enable;
	}

	static {
		TexturePackChangeHandler.register(new CustomAnimation$1("Extended HD", 1));
	}
}
