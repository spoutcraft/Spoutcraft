package com.prupe.mcpatcher;

import com.prupe.mcpatcher.TexturePackChangeHandler$1;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.zip.ZipFile;
import net.minecraft.src.ITexturePack;
import net.minecraft.src.TexturePackCustom;
import net.minecraft.src.TexturePackList;

public abstract class TexturePackChangeHandler {
	private static final ArrayList handlers = new ArrayList();
	private static boolean changing;
	private static long startTime;
	private static long startMem;
	private static final boolean autoRefreshTextures = Config.getBoolean("autoRefreshTextures", false);
	private static long lastCheckTime;
	private boolean updateNeeded;
	protected final String name;
	protected final int order;

	public TexturePackChangeHandler(String var1, int var2) {
		this.name = var1;
		this.order = var2;
	}

	public void initialize() {
		this.beforeChange();
		this.afterChange();
	}

	public void refresh() {
		this.beforeChange();
		this.afterChange();
	}

	public abstract void beforeChange();

	public abstract void afterChange();

	protected void setUpdateNeeded(boolean var1) {
		this.updateNeeded = var1;
	}

	public static void scheduleTexturePackRefresh() {
		MCPatcherUtils.getMinecraft().scheduleTexturePackRefresh();
	}

	public static void register(TexturePackChangeHandler var0) {
		if (var0 != null) {
			if (TexturePackAPI.getTexturePack() != null) {
				try {
					var0.initialize();
				} catch (Throwable var2) {
					var2.printStackTrace();
				}
			}

			handlers.add(var0);
			Collections.sort(handlers, new TexturePackChangeHandler$1());
		}
	}

	public static void earlyInitialize(String var0, String var1) {
		try {
			Class.forName(var0).getDeclaredMethod(var1, new Class[0]).invoke((Object)null, new Object[0]);
		} catch (Throwable var3) {
			;
		}
	}

	public static void checkForTexturePackChange() {
		Iterator var0 = handlers.iterator();

		while (var0.hasNext()) {
			TexturePackChangeHandler var1 = (TexturePackChangeHandler)var0.next();

			if (var1.updateNeeded) {
				var1.updateNeeded = false;

				try {
					var1.refresh();
				} catch (Throwable var3) {
					var3.printStackTrace();
				}
			}
		}

		ITexturePack var4 = TexturePackAPI.getTexturePack();

		if (var4 instanceof TexturePackCustom) {
			checkFileChange(MCPatcherUtils.getMinecraft().texturePackList, (TexturePackCustom)var4);
		}
	}

	public static void beforeChange1() {
		changing = true;
		startTime = System.currentTimeMillis();
		Runtime var0 = Runtime.getRuntime();
		startMem = var0.totalMemory() - var0.freeMemory();
		Iterator var1 = handlers.iterator();

		while (var1.hasNext()) {
			TexturePackChangeHandler var2 = (TexturePackChangeHandler)var1.next();

			try {
				var2.beforeChange();
			} catch (Throwable var4) {
				var4.printStackTrace();
			}
		}
	}

	public static void afterChange1() {
		Iterator var0 = handlers.iterator();

		while (var0.hasNext()) {
			TexturePackChangeHandler var1 = (TexturePackChangeHandler)var0.next();

			try {
				var1.afterChange();
			} catch (Throwable var5) {
				var5.printStackTrace();
			}
		}

		System.gc();
		long var6 = System.currentTimeMillis() - startTime;
		Runtime var2 = Runtime.getRuntime();
		long var3 = var2.totalMemory() - var2.freeMemory() - startMem;
		changing = false;
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
						MCPatcherUtils.close(var1);
						MCPatcherUtils.close(var2);
						var3 = new ZipFile(var0.tmpFile);
						var0.origZip = var0.texturePackZipFile;
						var0.texturePackZipFile = var3;
						var3 = null;
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
					scheduleTexturePackRefresh();
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