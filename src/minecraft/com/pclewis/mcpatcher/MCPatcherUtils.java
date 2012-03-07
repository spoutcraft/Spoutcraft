package com.pclewis.mcpatcher;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;

public class MCPatcherUtils {
	public static void close(Closeable var0) {
		if (var0 != null) {
			try {
				var0.close();
			} catch (IOException var2) {
				var2.printStackTrace();
			}
		}
	}

	public static void close(ZipFile var0) {
		if (var0 != null) {
			try {
				var0.close();
			} catch (IOException var2) {
				var2.printStackTrace();
			}
		}
	}

	public static BufferedImage readImage(InputStream var0) {
		BufferedImage var1 = null;
		if (var0 != null) {
			try {
				var1 = ImageIO.read(var0);
			} catch (IOException var6) {
				var6.printStackTrace();
			} finally {
				close((Closeable)var0);
			}
		}

		return var1;
	}

	public static int[] getImageRGB(BufferedImage var0) {
		if (var0 == null) {
			return null;
		} else {
			int var1 = var0.getWidth();
			int var2 = var0.getHeight();
			int[] var3 = new int[var1 * var2];
			var0.getRGB(0, 0, var1, var2, var3, 0, var1);
			return var3;
		}
	}
}
