package com.pclewis.mcpatcher;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TexturePackCustom;
import net.minecraft.src.TexturePackDefault;
import net.minecraft.src.TexturePackFolder;
import net.minecraft.src.TexturePackImplementation;
import net.minecraft.src.TexturePackList;

public class TexturePackAPI {
	private static final MCLogger logger = MCLogger.getLogger("Texture Pack");
	public static TexturePackAPI instance = new TexturePackAPI();
	public static boolean loadFontFromTexturePack;
	private static final ArrayList textureMapFields = new ArrayList();
	private static TexturePackImplementation texturePack;

	public static TexturePackImplementation getTexturePack() {
		return texturePack;
	}

	static TexturePackImplementation getCurrentTexturePack() {
		Minecraft var0 = MCPatcherUtils.getMinecraft();

		if (var0 == null) {
			return null;
		} else {
			TexturePackList var1 = var0.texturePackList;
			return (TexturePackImplementation) (var1 == null ? null : var1.getSelectedTexturePack());
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

	public static Properties getProperties(String var0) {
		Properties var1 = new Properties();
		return getProperties(var0, var1) ? var1 : null;
	}

	public static boolean getProperties(String var0, Properties var1) {
		return instance.getPropertiesImpl(var0, var1);
	}

	public static String[] listResources(String var0, String var1) {
		if (var0 == null) {
			var0 = "";
		}

		if (var0.startsWith("/")) {
			var0 = var0.substring(1);
		}

		if (var1 == null) {
			var1 = "";
		}

		ArrayList var2 = new ArrayList();

		if (!(texturePack instanceof TexturePackDefault)) {
			if (texturePack instanceof TexturePackCustom) {
				ZipFile var3 = ((TexturePackCustom)texturePack).texturePackZipFile;

				if (var3 != null) {
					Iterator var4 = Collections.list(var3.entries()).iterator();

					while (var4.hasNext()) {
						ZipEntry var5 = (ZipEntry)var4.next();
						String var6 = var5.getName();

						if (var6.startsWith(var0) && var6.endsWith(var1)) {
							var2.add("/" + var6);
						}
					}
				}
			} else if (texturePack instanceof TexturePackFolder) {
				File var9 = ((TexturePackFolder)texturePack).getFolder();

				if (var9 != null && var9.isDirectory()) {
					String[] var10 = (new File(var9, var0)).list();

					if (var10 != null) {
						String[] var11 = var10;
						int var12 = var10.length;

						for (int var7 = 0; var7 < var12; ++var7) {
							String var8 = var11[var7];

							if (var8.endsWith(var1)) {
								var2.add("/" + (new File(new File(var0), var8)).getPath().replace('\\', '/'));
							}
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

	public static int unloadTexture(String var0) {
		int var1 = getTextureIfLoaded(var0);

		if (var1 >= 0) {
			logger.finest("unloading texture %s", new Object[] {var0});
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

	protected InputStream getInputStreamImpl(String var1) {
		var1 = parseTextureName(var1)[1];

		if (!loadFontFromTexturePack && var1.startsWith("/font/")) {
			return TexturePackAPI.class.getResourceAsStream(var1);
		} else if (texturePack == null) {
			TexturePackImplementation var2 = getCurrentTexturePack();
			return var2 == null ? TexturePackAPI.class.getResourceAsStream(var1) : var2.getResourceAsStream(var1);
		} else {
			return texturePack.getResourceAsStream(var1);
		}
	}

	protected BufferedImage getImageImpl(String var1) {
		InputStream var2 = getInputStream(var1);
		BufferedImage var3 = null;

		if (var2 != null) {
			try {
				var3 = ImageIO.read(var2);
			} catch (IOException var8) {
				logger.severe("could not read %s", new Object[] {var1});
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
				logger.severe("could not read %s", new Object[0]);
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

	static TexturePackImplementation access$000() {
		return texturePack;
	}

	static MCLogger access$100() {
		return logger;
	}

	static TexturePackImplementation access$002(TexturePackImplementation var0) {
		texturePack = var0;
		return var0;
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
	public abstract static class ChangeHandler {
		private static final ArrayList handlers = new ArrayList();
		private static boolean changing;
		private static final boolean autoRefreshTextures = MCPatcherUtils.getBoolean("autoRefreshTextures", false);
		private static long lastCheckTime;
		protected final String name;
		protected final int order;

		protected ChangeHandler(String var1, int var2) {
			this.name = var1;
			this.order = var2;
		}

		protected abstract void onChange();

		public static void register(ChangeHandler var0) {
			if (var0 != null) {
				if (TexturePackAPI.access$000() != null) {
					try {
						TexturePackAPI.access$100().info("initializing %s...", new Object[] {var0.name});
						var0.onChange();
					} catch (Throwable var2) {
						var2.printStackTrace();
						TexturePackAPI.access$100().severe("%s initialization failed", new Object[] {var0.name});
					}
				}

				handlers.add(var0);
				TexturePackAPI.access$100().fine("registered texture pack handler %s, priority %d", new Object[] {var0.name, Integer.valueOf(var0.order)});
                Collections.sort(handlers, new Comparator<ChangeHandler>() {
                    public int compare(ChangeHandler o1, ChangeHandler o2) {
                        return o1.order - o2.order;
                    }
                });
			}
		}
	
		//TODO:  Error: incompatible types
		/*
		public static void change() {
			for (ChangeHandler handler : handlers) {
				try {
					handler.onChange();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
		*/
		
		public static void checkForTexturePackChange() {
			Minecraft var0 = MCPatcherUtils.getMinecraft();

			if (var0 != null) {
				TexturePackList var1 = var0.texturePackList;

				if (var1 != null) {
					TexturePackImplementation var2 = (TexturePackImplementation) var1.getSelectedTexturePack();

					if (var2 != TexturePackAPI.access$000()) {
						changeTexturePack(var2);
					} else if (var2 instanceof TexturePackCustom) {
						checkFileChange(var1, (TexturePackCustom)var2);
					}
				}
			}
		}

		private static void changeTexturePack(TexturePackImplementation var0) {
			if (var0 != null && !changing) {
				changing = true;
				long var1 = -System.currentTimeMillis();
				Runtime var3 = Runtime.getRuntime();
				long var4 = -(var3.totalMemory() - var3.freeMemory());

				if (TexturePackAPI.access$000() == null) {
					TexturePackAPI.access$100().info("\nsetting texture pack to %s", new Object[] {var0.texturePackID});
				} else if (TexturePackAPI.access$000() == var0) {
					TexturePackAPI.access$100().info("\nreloading texture pack %s", new Object[] {var0.texturePackID});
				} else {
					TexturePackAPI.access$100().info("\nchanging texture pack from %s to %s", new Object[] {TexturePackAPI.access$000().texturePackID, var0.texturePackID});
				}

				TexturePackAPI.access$002(var0);
				Iterator var6 = handlers.iterator();

				while (var6.hasNext()) {
					ChangeHandler var7 = (ChangeHandler)var6.next();

					try {
						TexturePackAPI.access$100().info("refreshing %s...", new Object[] {var7.name});
						var7.onChange();
					} catch (Throwable var9) {
						var9.printStackTrace();
						TexturePackAPI.access$100().severe("%s refresh failed", new Object[] {var7.name});
					}
				}

				System.gc();
				var1 += System.currentTimeMillis();
				var4 += var3.totalMemory() - var3.freeMemory();
				TexturePackAPI.access$100().info("done (%.3fs elapsed, mem usage %+.1fMB)\n", new Object[] {Double.valueOf((double)var1 / 1000.0D), Double.valueOf((double)var4 / 1048576.0D)});
				changing = false;
			}
		}

		private static boolean openTexturePackFile(TexturePackCustom var0) {
			if (var0.texturePackZipFile == null) {
				return false;
			} else if (var0.origZip != null) {
				return true;
			} else {
				FileInputStream var1 = null;
				FileOutputStream var2 = null;
				ZipFile var3 = null;
				boolean var5;

				try {
					var0.lastModified = var0.texturePackFile.lastModified();
					var0.tmpFile = File.createTempFile("tmpmc", ".zip");
					var0.tmpFile.deleteOnExit();
					MCPatcherUtils.close(var0.texturePackZipFile);
					var1 = new FileInputStream(var0.texturePackFile);
					var2 = new FileOutputStream(var0.tmpFile);
					byte[] var4 = new byte[65536];

					while (true) {
						int var11 = var1.read(var4);

						if (var11 <= 0) {
							MCPatcherUtils.close((Closeable)var1);
							MCPatcherUtils.close((Closeable)var2);
							var3 = new ZipFile(var0.tmpFile);
							var0.origZip = var0.texturePackZipFile;
							var0.texturePackZipFile = var3;
							var3 = null;
							TexturePackAPI.access$100().fine("copied %s to %s, lastModified = %d", new Object[] {var0.texturePackFile.getPath(), var0.tmpFile.getPath(), Long.valueOf(var0.lastModified)});
							return true;
						}

						var2.write(var4, 0, var11);
					}
				} catch (IOException var9) {
					var9.printStackTrace();
					var5 = false;
				} finally {
					MCPatcherUtils.close((Closeable)var1);
					MCPatcherUtils.close((Closeable)var2);
					MCPatcherUtils.close(var3);
				}

				return var5;
			}
		}

		private static void closeTexturePackFile(TexturePackCustom var0) {
			if (var0.origZip != null) {
				MCPatcherUtils.close(var0.texturePackZipFile);
				var0.texturePackZipFile = var0.origZip;
				var0.origZip = null;
				var0.tmpFile.delete();
				TexturePackAPI.access$100().fine("deleted %s", new Object[] {var0.tmpFile.getPath()});
				var0.tmpFile = null;
			}
		}

		private static boolean checkFileChange(TexturePackList var0, TexturePackCustom var1) {
			if (autoRefreshTextures && openTexturePackFile(var1)) {
				long var2 = System.currentTimeMillis();

				if (var2 - lastCheckTime < 1000L) {
					return false;
				} else {
					lastCheckTime = var2;
					long var4 = var1.texturePackFile.lastModified();

					if (var4 != var1.lastModified && var4 != 0L && var1.lastModified != 0L) {
						TexturePackAPI.access$100().finer("%s lastModified changed from %d to %d", new Object[] {var1.texturePackFile.getName(), Long.valueOf(var1.lastModified), Long.valueOf(var4)});
						ZipFile var6 = null;
						label66: {
							boolean var8;

							try {
								var6 = new ZipFile(var1.texturePackFile);
								break label66;
							} catch (IOException var12) {
								var8 = false;
							} finally {
								MCPatcherUtils.close(var6);
							}

							return var8;
						}
						closeTexturePackFile(var1);
						var0.updateAvaliableTexturePacks();
						return true;
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		}
	}
}
