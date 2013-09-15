package com.prupe.mcpatcher;

import com.prupe.mcpatcher.TexturePackChangeHandler$1;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.src.Minecraft;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.ResourcePack;
import net.minecraft.src.SimpleTexture;
import net.minecraft.src.TextureManager;
import net.minecraft.src.TextureObject;

public abstract class TexturePackChangeHandler {
	private static final MCLogger logger = MCLogger.getLogger("Texture Pack");
	private static final ArrayList<TexturePackChangeHandler> handlers = new ArrayList();
	private static boolean initializing;
	private static boolean changing;
	private static long startTime;
	private static long startMem;
	private boolean updateNeeded;
	protected final String name;
	protected final int order;

	public TexturePackChangeHandler(String name, int order) {
		this.name = name;
		this.order = order;
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

	public void afterChange2() {}

	protected void setUpdateNeeded(boolean updateNeeded) {
		this.updateNeeded = updateNeeded;
	}

	public static void scheduleTexturePackRefresh() {
		//MCPatcherUtils.getMinecraft().scheduleTexturePackRefresh();
	}

	public static void register(TexturePackChangeHandler handler) {
		if (handler != null) {
			if (Minecraft.getMinecraft().getResourceManager() != null) {
				try {
					logger.info("initializing %s...", new Object[] {handler.name});
					handler.initialize();
				} catch (Throwable var2) {
					var2.printStackTrace();
					logger.severe("%s initialization failed", new Object[] {handler.name});
				}
			}

			handlers.add(handler);
			logger.fine("registered texture pack handler %s, priority %d", new Object[] {handler.name, Integer.valueOf(handler.order)});
			Collections.sort(handlers, new TexturePackChangeHandler$1());
		}
	}

	public static void earlyInitialize(String className, String methodName) {
		try {
			logger.fine("calling %s.%s", new Object[] {className, methodName});
			Class.forName(className).getDeclaredMethod(methodName, new Class[0]).invoke((Object)null, new Object[0]);
		} catch (Throwable var3) {
			;
		}
	}

	public static void checkForTexturePackChange() {
		Iterator i$ = handlers.iterator();

		while (i$.hasNext()) {
			TexturePackChangeHandler handler = (TexturePackChangeHandler)i$.next();

			if (handler.updateNeeded) {
				handler.updateNeeded = false;

				try {
					logger.info("refreshing %s...", new Object[] {handler.name});
					handler.refresh();
				} catch (Throwable var3) {
					var3.printStackTrace();
					logger.severe("%s refresh failed", new Object[] {handler.name});
				}
			}
		}
	}

	public static void beforeChange1(boolean initializing1) {
		logger.finer("beforeChange1(%s) initializing=%s changing=%s", new Object[] {Boolean.valueOf(initializing1), Boolean.valueOf(initializing), Boolean.valueOf(changing)});

		if (initializing1) {
			logger.finer("skipping beforeChange1 because we are still initializing", new Object[0]);
			initializing = true;
		} else if (changing && !initializing) {
			(new RuntimeException("unexpected recursive call to TexturePackChangeHandler")).printStackTrace();
		} else {
			changing = true;
			startTime = System.currentTimeMillis();
			Runtime runtime = Runtime.getRuntime();
			startMem = runtime.totalMemory() - runtime.freeMemory();
			List resourcePacks = TexturePackAPI.getResourcePacks((String)null);
			logger.fine("%s resource packs (%d selected):", new Object[] {initializing ? "initializing" : "changing", Integer.valueOf(resourcePacks.size())});
			Iterator textureManager = resourcePacks.iterator();

			while (textureManager.hasNext()) {
				ResourcePack texturesToUnload = (ResourcePack)textureManager.next();
				logger.fine("resource pack: %s", new Object[] {texturesToUnload});
			}

			textureManager = handlers.iterator();

			while (textureManager.hasNext()) {
				TexturePackChangeHandler texturesToUnload1 = (TexturePackChangeHandler)textureManager.next();

				try {
					logger.info("refreshing %s (pre)...", new Object[] {texturesToUnload1.name});
					texturesToUnload1.beforeChange();
				} catch (Throwable var9) {
					var9.printStackTrace();
					logger.severe("%s.beforeChange failed", new Object[] {texturesToUnload1.name});
				}
			}

			TextureManager textureManager1 = MCPatcherUtils.getMinecraft().getTextureManager();

			if (textureManager1 != null) {
				HashSet texturesToUnload2 = new HashSet();
				Iterator i$ = textureManager1.mapTextureObjects.entrySet().iterator();

				while (i$.hasNext()) {
					Entry resource = (Entry)i$.next();
					ResourceLocation resource1 = (ResourceLocation)resource.getKey();
					TextureObject texture = (TextureObject)resource.getValue();

					if (texture instanceof SimpleTexture && !TexturePackAPI.hasResource(resource1)) {
						texturesToUnload2.add(resource1);
					}
				}

				i$ = texturesToUnload2.iterator();

				while (i$.hasNext()) {
					ResourceLocation resource2 = (ResourceLocation)i$.next();
					TexturePackAPI.unloadTexture(resource2);
				}
			}
		}
	}

	public static void afterChange1(boolean initializing1) {
		logger.finer("afterChange1(%s) initializing=%s changing=%s", new Object[] {Boolean.valueOf(initializing1), Boolean.valueOf(initializing), Boolean.valueOf(changing)});

		if (initializing && !initializing1) {
			logger.finer("deferring afterChange1 because we are still initializing", new Object[0]);
		} else {
			Iterator timeDiff = handlers.iterator();
			TexturePackChangeHandler handler;

			while (timeDiff.hasNext()) {
				handler = (TexturePackChangeHandler)timeDiff.next();

				try {
					logger.info("refreshing %s (post)...", new Object[] {handler.name});
					handler.afterChange();
				} catch (Throwable var7) {
					var7.printStackTrace();
					logger.severe("%s.afterChange failed", new Object[] {handler.name});
				}
			}

			for (int var8 = handlers.size() - 1; var8 >= 0; --var8) {
				handler = (TexturePackChangeHandler)handlers.get(var8);

				try {
					handler.afterChange2();
				} catch (Throwable var6) {
					var6.printStackTrace();
					logger.severe("%s.afterChange2 failed", new Object[] {handler.name});
				}
			}

			System.gc();
			long var9 = System.currentTimeMillis() - startTime;
			Runtime runtime = Runtime.getRuntime();
			long memDiff = runtime.totalMemory() - runtime.freeMemory() - startMem;
			logger.info("done (%.3fs elapsed, mem usage %+.1fMB)\n", new Object[] {Double.valueOf((double)var9 / 1000.0D), Double.valueOf((double)memDiff / 1048576.0D)});
			changing = false;
			initializing = false;
		}
	}
}
