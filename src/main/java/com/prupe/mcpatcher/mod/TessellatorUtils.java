package com.prupe.mcpatcher.mod;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.src.Icon;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureMap;

public class TessellatorUtils {
	private static final Integer MAGIC_VALUE = Integer.valueOf(305419896);
	private static final Map textureMapNames = new WeakHashMap();
	private static final Map iconMap = new HashMap();
	private static final Map iconsByName = new HashMap();
	private static Field[] fieldsToCopy;
	public static boolean haveBufferSize;

	static Tessellator getTessellator(Tessellator var0, Icon var1) {
		TextureMap var2 = (TextureMap)iconMap.get(var1);

		if (var2 != null && var2 != CTMUtils.terrainMap) {
			Tessellator var3 = (Tessellator)var0.children.get(var2);

			if (var3 == null) {
				String var4 = (String)textureMapNames.get(var2);

				if (var4 == null) {
					var4 = var2.toString();
				}

				var3 = new Tessellator(2097152);
				copyFields(var0, var3, true);
				var3.textureMap = var2;
				var0.children.put(var2, var3);
			} else {
				copyFields(var0, var3, false);
			}

			return var3;
		} else {
			return var0;
		}
	}

	static void registerTextureMap(TextureMap var0, String var1) {
		textureMapNames.put(var0, var1);
	}

	static void registerIcon(TextureMap var0, Icon var1) {
		iconMap.put(var1, var0);
		iconsByName.put(var1.getIconName(), var1);
	}

	static Icon getIconByName(String var0) {
		return (Icon)iconsByName.get(var0);
	}

	private static Field[] getFieldsToCopy(Tessellator var0) {
		int var1;

		if (haveBufferSize) {
			var1 = var0.bufferSize;
			var0.bufferSize = MAGIC_VALUE.intValue();
		} else {
			var1 = 0;
		}

		int var2 = var0.vertexCount;
		int var3 = var0.addedVertices;
		int var4 = var0.rawBufferIndex;
		var0.vertexCount = MAGIC_VALUE.intValue();
		var0.addedVertices = MAGIC_VALUE.intValue();
		var0.rawBufferIndex = MAGIC_VALUE.intValue();
		ArrayList var5 = new ArrayList();
		Field[] var6 = Tessellator.class.getDeclaredFields();
		int var7 = var6.length;

		for (int var8 = 0; var8 < var7; ++var8) {
			Field var9 = var6[var8];

			try {
				Class var10 = var9.getType();
				int var11 = var9.getModifiers();

				if (!Modifier.isStatic(var11) && var10.isPrimitive() && !var9.getName().equals("rawBufferSize")) {
					var9.setAccessible(true);

					if (var10 != Integer.TYPE || !MAGIC_VALUE.equals(var9.get(var0))) {
						var5.add(var9);
					}
				}
			} catch (Throwable var12) {
				var12.printStackTrace();
			}
		}

		if (!haveBufferSize) {
			var0.bufferSize = var1;
		}

		var0.vertexCount = var2;
		var0.addedVertices = var3;
		var0.rawBufferIndex = var4;
		return (Field[])var5.toArray(new Field[var5.size()]);
	}

	private static void copyFields(Tessellator var0, Tessellator var1, boolean var2) {
		if (fieldsToCopy == null) {
			fieldsToCopy = getFieldsToCopy(var0);
		}

		Field[] var3 = fieldsToCopy;
		int var4 = var3.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			Field var6 = var3[var5];

			try {
				Object var7 = var6.get(var0);

				var6.set(var1, var7);
			} catch (IllegalAccessException var8) {
				var8.printStackTrace();
			}
		}

		if (var0.isDrawing && !var1.isDrawing) {
			var1.startDrawing(var0.drawMode);
		} else if (!var0.isDrawing && var1.isDrawing) {
			var1.reset();
		}
	}

	static void clear(Tessellator var0) {
		Iterator var1 = var0.children.values().iterator();

		while (var1.hasNext()) {
			Tessellator var2 = (Tessellator)var1.next();
			clear(var2);
		}

		var0.children.clear();
		textureMapNames.clear();
		iconMap.clear();
		iconsByName.clear();
	}

	public static void resetChildren(Tessellator var0) {
		Iterator var1 = var0.children.values().iterator();

		while (var1.hasNext()) {
			Tessellator var2 = (Tessellator)var1.next();
			var2.reset();
		}
	}

	public static int drawChildren(int var0, Tessellator var1) {
		Tessellator var3;

		for (Iterator var2 = var1.children.values().iterator(); var2.hasNext(); var0 += var3.draw()) {
			var3 = (Tessellator)var2.next();
		}

		return var0;
	}

	public static void startDrawingChildren(Tessellator var0, int var1) {
		Iterator var2 = var0.children.values().iterator();

		while (var2.hasNext()) {
			Tessellator var3 = (Tessellator)var2.next();
			var3.startDrawing(var1);
		}
	}

	private static String toString(Tessellator var0) {
		if (var0 == null) {
			return "Tessellator{null}";
		} else {
			String var1 = var0.toString();
			TextureMap var2 = var0.textureMap;

			if (var2 != null) {
				String var3 = (String)textureMapNames.get(var2);

				if (var3 == null) {
					var1 = var2.toString();
				} else {
					var1 = var3;
				}
			}

			return String.format("Tessellator{%s, isDrawing=%s, %d children}", new Object[] {var1, Boolean.valueOf(var0.isDrawing), Integer.valueOf(var0.children.size())});
		}
	}
}
