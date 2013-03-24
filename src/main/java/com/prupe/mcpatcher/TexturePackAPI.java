package com.prupe.mcpatcher;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ITexturePack;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TexturePackCustom;
import net.minecraft.src.TexturePackDefault;
import net.minecraft.src.TexturePackFolder;
import net.minecraft.src.TexturePackList;

public class TexturePackAPI {
	public static TexturePackAPI instance = new TexturePackAPI();
	public static boolean enableTextureBorder;
	private static final ArrayList textureMapFields = new ArrayList();

	public static ITexturePack getTexturePack() {
		Minecraft var0 = MCPatcherUtils.getMinecraft();

		if (var0 == null) {
			return null;
		} else {
			TexturePackList var1 = var0.texturePackList;
			return var1 == null ? null : var1.getSelectedTexturePack();
		}
	}

	public static boolean isDefaultTexturePack() {
		return getTexturePack() instanceof TexturePackDefault;
	}

	public static String[] parseTextureName(String var0) {
		String[] var1 = new String[] {null, var0};

		if (var0.startsWith("##")) {
			var1[0] = "##";
			var1[1] = var0.substring(2);
		} else if (var0.startsWith("%")) {
			int var2 = var0.indexOf(37, 1);

			if (var2 > 0) {
				var1[0] = var0.substring(0, var2 + 1);
				var1[1] = var0.substring(var2 + 1);
			}
		}

		return var1;
	}

	public static InputStream getInputStream(String var0) {
		return instance.getInputStreamImpl(var0);
	}

	public static boolean hasResource(String var0) {
		if (var0.endsWith(".png")) {
			return getImage(var0) != null;
		} else if (var0.endsWith(".properties")) {
			return getProperties(var0) != null;
		} else {
			InputStream var1 = getInputStream(var0);
			MCPatcherUtils.close((Closeable)var1);
			return var1 != null;
		}
	}

	public static BufferedImage getImage(String var0) {
		return instance.getImageImpl(var0);
	}

	public static BufferedImage getImage(Object var0, String var1) {
		return getImage(var1);
	}

	public static BufferedImage getImage(Object var0, Object var1, String var2) {
		return getImage(var2);
	}

	public static Properties getProperties(String var0) {
		Properties var1 = new Properties();
		return getProperties(var0, var1) ? var1 : null;
	}

	public static boolean getProperties(String var0, Properties var1) {
		return instance.getPropertiesImpl(var0, var1);
	}

	private static String fixupPath(String var0) {
		if (var0 == null) {
			var0 = "";
		}

		return var0.replace('\\', '/').replaceFirst("^/", "").replaceFirst("/$", "");
	}

	private static boolean directlyContains(String var0, String var1) {
		var0 = fixupPath(var0);
		var1 = fixupPath(var1);

		if (!var1.startsWith(var0)) {
			return false;
		} else {
			var1 = var1.substring(var0.length());
			return var1.matches("/[^/]+/?");
		}
	}

	public static String[] listResources(String var0, String var1) {
		var0 = fixupPath(var0);

		if (var1 == null) {
			var1 = "";
		}

		ITexturePack var2 = getTexturePack();
		ArrayList var3 = new ArrayList();

		if (var2 instanceof TexturePackCustom) {
			ZipFile var4 = ((TexturePackCustom)var2).texturePackZipFile;

			if (var4 != null) {
				Iterator var5 = Collections.list(var4.entries()).iterator();

				while (var5.hasNext()) {
					ZipEntry var6 = (ZipEntry)var5.next();
					String var7 = var6.getName();

					if (!var6.isDirectory() && directlyContains(var0, var7) && var7.endsWith(var1)) {
						var3.add("/" + var7);
					}
				}
			}
		} else if (var2 instanceof TexturePackFolder) {
			File var10 = ((TexturePackFolder)var2).texturePackFile;

			if (var10 != null && var10.isDirectory()) {
				String[] var11 = (new File(var10, var0)).list();

				if (var11 != null) {
					String[] var12 = var11;
					int var13 = var11.length;

					for (int var8 = 0; var8 < var13; ++var8) {
						String var9 = var12[var8];

						if (var9.endsWith(var1)) {
							var3.add("/" + var0 + "/" + var9);
						}
					}
				}
			}
		}

		Collections.sort(var3);
		return (String[])var3.toArray(new String[var3.size()]);
	}

	public static String[] listDirectories(String var0) {
		var0 = fixupPath(var0);
		ITexturePack var1 = getTexturePack();
		ArrayList var2 = new ArrayList();

		if (var1 instanceof TexturePackCustom) {
			ZipFile var3 = ((TexturePackCustom)var1).texturePackZipFile;

			if (var3 != null) {
				Iterator var4 = Collections.list(var3.entries()).iterator();

				while (var4.hasNext()) {
					ZipEntry var5 = (ZipEntry)var4.next();
					String var6 = fixupPath(var5.getName());

					if (var5.isDirectory() && directlyContains(var0, var6)) {
						var2.add("/" + var6);
					}
				}
			}
		} else if (var1 instanceof TexturePackFolder) {
			File var10 = ((TexturePackFolder)var1).texturePackFile;

			if (var10 != null && var10.isDirectory()) {
				File var11 = new File(var10, var0);
				String[] var12 = var11.list();

				if (var12 != null) {
					String[] var13 = var12;
					int var7 = var12.length;

					for (int var8 = 0; var8 < var7; ++var8) {
						String var9 = var13[var8];

						if ((new File(var11, var9)).isDirectory()) {
							var2.add("/" + var0 + "/" + var9);
						}
					}
				}
			}
		}

		Collections.sort(var2);
		return (String[])var2.toArray(new String[var2.size()]);
	}

	public static int getTextureIfLoaded(String var0) {
		RenderEngine var1 = MCPatcherUtils.getMinecraft().renderEngine;
		Iterator var2 = textureMapFields.iterator();

		while (var2.hasNext()) {
			Field var3 = (Field)var2.next();

			try {
				HashMap var4 = (HashMap)var3.get(var1);

				if (var4 != null) {
					Object var5 = var4.get(var0);

					if (var5 instanceof Integer) {
						return ((Integer)var5).intValue();
					}
				}
			} catch (IllegalAccessException var6) {
				;
			}
		}

		return -1;
	}

	public static boolean isTextureLoaded(String var0) {
		return getTextureIfLoaded(var0) >= 0;
	}

	public static void bindTexture(String var0) {
		MCPatcherUtils.getMinecraft().renderEngine.bindTexture(var0);
	}

	public static void bindTexture(int var0) {
		MCPatcherUtils.getMinecraft().renderEngine.bindTexture(var0);
	}

	public static void clearBoundTexture() {
		MCPatcherUtils.getMinecraft().renderEngine.resetBoundTexture();
	}

	public static int unloadTexture(String var0) {
		int var1 = getTextureIfLoaded(var0);

		if (var1 >= 0) {
			RenderEngine var2 = MCPatcherUtils.getMinecraft().renderEngine;
			var2.deleteTexture(var1);
			Iterator var3 = textureMapFields.iterator();

			while (var3.hasNext()) {
				Field var4 = (Field)var3.next();

				try {
					HashMap var5 = (HashMap)var4.get(var2);

					if (var5 != null) {
						var5.remove(var0);
					}
				} catch (IllegalAccessException var6) {
					;
				}
			}
		}

		return var1;
	}

	public static void deleteTexture(int var0) {
		if (var0 >= 0) {
			MCPatcherUtils.getMinecraft().renderEngine.deleteTexture(var0);
		}
	}

	public static String getTextureName(int var0) {
		if (var0 >= 0) {
			RenderEngine var1 = MCPatcherUtils.getMinecraft().renderEngine;
			Iterator var2 = textureMapFields.iterator();

			while (var2.hasNext()) {
				Field var3 = (Field)var2.next();

				try {
					HashMap var4 = (HashMap)var3.get(var1);
					Iterator var5 = var4.entrySet().iterator();

					while (var5.hasNext()) {
						Object var6 = var5.next();
						Entry var7 = (Entry)var6;
						Object var8 = var7.getValue();
						Object var9 = var7.getKey();

						if (var8 instanceof Integer && var9 instanceof String && ((Integer)var8).intValue() == var0) {
							return (String)var9;
						}
					}
				} catch (IllegalAccessException var10) {
					;
				}
			}
		}

		return null;
	}

	public static IntBuffer getIntBuffer(IntBuffer var0, int[] var1) {
		var0.clear();
		int var2 = var0.capacity();
		int var3 = var1.length;

		if (var3 > var2) {
			var0 = ByteBuffer.allocateDirect(4 * var3).order(var0.order()).asIntBuffer();
		}

		var0.put(var1);
		var0.position(0).limit(var3);
		return var0;
	}

	protected InputStream getInputStreamImpl(String var1) {
		var1 = parseTextureName(var1)[1];
		ITexturePack var2 = getTexturePack();

		if (var2 == null) {
			return TexturePackAPI.class.getResourceAsStream(var1);
		} else {
			try {
				return var2.getResourceAsStream(var1);
			} catch (Throwable var4) {
				return null;
			}
		}
	}

	protected BufferedImage getImageImpl(String var1) {
		InputStream var2 = getInputStream(var1);
		BufferedImage var3 = null;

		if (var2 != null) {
			try {
				var3 = ImageIO.read(var2);
			} catch (IOException var8) {
				var8.printStackTrace();
			} finally {
				MCPatcherUtils.close((Closeable)var2);
			}
		}

		return var3;
	}

	protected boolean getPropertiesImpl(String var1, Properties var2) {
		if (var2 != null) {
			InputStream var3 = getInputStream(var1);
			boolean var4;

			try {
				if (var3 == null) {
					return false;
				}

				var2.load(var3);
				var4 = true;
			} catch (IOException var8) {
				var8.printStackTrace();
				return false;
			} finally {
				MCPatcherUtils.close((Closeable)var3);
			}

			return var4;
		} else {
			return false;
		}
	}

	static {
		try {
			Field[] var0 = RenderEngine.class.getDeclaredFields();
			int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2) {
				Field var3 = var0[var2];

				if (HashMap.class.isAssignableFrom(var3.getType())) {
					var3.setAccessible(true);
					textureMapFields.add(var3);
				}
			}
		} catch (Throwable var4) {
			var4.printStackTrace();
		}
	}
}