package com.prupe.mcpatcher.hd;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import net.minecraft.src.Resource;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.SimpleResource;
import net.minecraft.src.TextureAtlasSprite;
import org.lwjgl.opengl.PixelFormat;

public class AAHelper {
	private static final MCLogger logger = MCLogger.getLogger("Mipmap");
	private static final int debugColor = Config.getBoolean("Extended HD", "debugBorder", false) ? -16776961 : 0;
	private static final int aaSamples = Config.getInt("Extended HD", "antiAliasing", 1);
	private static Field addressField;

	public static PixelFormat setupPixelFormat(PixelFormat pixelFormat) {
		if (aaSamples > 1) {
			logger.config("setting AA samples to %d", new Object[] {Integer.valueOf(aaSamples)});
			return pixelFormat.withSamples(aaSamples);
		} else {
			return pixelFormat;
		}
	}

	public static BufferedImage addBorder(TextureAtlasSprite stitched, Resource resource, BufferedImage input) {
		if (input != null && resource instanceof SimpleResource && addressField != null) {
			ResourceLocation name;

			try {
				name = (ResourceLocation)addressField.get(resource);
			} catch (IllegalAccessException var14) {
				var14.printStackTrace();
				addressField = null;
				return input;
			}

			input = MipmapHelper.fixTransparency(name, input);

			if (!(stitched instanceof BorderedTexture)) {
				return input;
			} else {
				int width = input.getWidth();
				int height = input.getHeight();
				int numFrames = height / width;
				height = width;
				int border = getBorderWidth(width);
				((BorderedTexture)stitched).setBorderWidth(border);

				if (border <= 0) {
					return input;
				} else {
					int newWidth = width + 2 * border;
					int newHeight = width + 2 * border;
					BufferedImage output = new BufferedImage(newWidth, numFrames * newHeight, 2);

					for (int frame = 0; frame < numFrames; ++frame) {
						int sy = frame * height;
						int dy = frame * newHeight;
						copyRegion(input, 0, sy, output, 0, dy, border, border, true, true);
						copyRegion(input, 0, sy, output, border, dy, width, border, false, true);
						copyRegion(input, width - border, sy, output, width + border, dy, border, border, true, true);
						copyRegion(input, 0, sy, output, 0, dy + border, border, width, true, false);
						copyRegion(input, 0, sy, output, border, dy + border, width, height, false, false);
						copyRegion(input, width - border, sy, output, width + border, dy + border, border, width, true, false);
						copyRegion(input, 0, sy + height - border, output, 0, dy + height + border, border, border, true, true);
						copyRegion(input, 0, sy + height - border, output, border, dy + height + border, width, border, false, true);
						copyRegion(input, width - border, sy + height - border, output, width + border, dy + height + border, border, border, true, true);
						addDebugOutline(output, dy, width, height, border);
					}

					return output;
				}
			}
		} else {
			return input;
		}
	}

	static boolean useAAForTexture(String texture) {
		return (aaSamples > 1 || MipmapHelper.anisoLevel > 1) && MipmapHelper.useMipmapsForTexture(texture);
	}

	private static int getBorderWidth(int size) {
		int border;

		if (aaSamples <= 1 && MipmapHelper.anisoLevel <= 1) {
			border = 0;
		} else if (MipmapHelper.mipmapEnabled && MipmapHelper.maxMipmapLevel > 0) {
			border = 1 << Math.max(Math.min(MipmapHelper.maxMipmapLevel, 4), 0);
		} else {
			border = 2;
		}

		return Math.min(border, size);
	}

	private static void copyRegion(BufferedImage input, int sx, int sy, BufferedImage output, int dx, int dy, int w, int h, boolean flipX, boolean flipY) {
		int[] rgb = new int[w * h];
		input.getRGB(sx, sy, w, h, rgb, 0, w);

		if (!flipX && !flipY) {
			output.setRGB(dx, dy, w, h, rgb, 0, w);
		} else {
			int[] rgbFlipped = new int[w * h];

			for (int i = 0; i < w; ++i) {
				for (int j = 0; j < h; ++j) {
					rgbFlipped[w * j + i] = rgb[w * (flipY ? h - 1 - j : j) + (flipX ? w - 1 - i : i)];
				}
			}

			output.setRGB(dx, dy, w, h, rgbFlipped, 0, w);
		}
	}

	private static void addDebugOutline(BufferedImage output, int dy, int width, int height, int border) {
		if (debugColor != 0) {
			int i;

			for (i = 0; i < width; ++i) {
				output.setRGB(i + border, dy + border, debugColor);
				output.setRGB(i + border, dy + height + border, debugColor);
			}

			for (i = 0; i < height; ++i) {
				output.setRGB(border, dy + i + border, debugColor);
				output.setRGB(height + border, dy + i + border, debugColor);
			}
		}
	}

	static {
		Field[] arr$ = SimpleResource.class.getDeclaredFields();
		int len$ = arr$.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			Field f = arr$[i$];

			if (ResourceLocation.class.isAssignableFrom(f.getType())) {
				f.setAccessible(true);
				addressField = f;
				break;
			}
		}
	}
}
