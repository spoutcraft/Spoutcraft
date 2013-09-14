package com.prupe.mcpatcher;

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
	private static final MCLogger logger = MCLogger.getLogger("Tilesheet");
	private static final Integer MAGIC_VALUE = Integer.valueOf(305419896);
	private static final Map<TextureMap, String> textureMapNames = new WeakHashMap();
	private static final Map<Icon, TextureMap> iconMap = new HashMap();
	private static Field[] fieldsToCopy;
	public static boolean haveBufferSize;

	public static void clearDefaultTextureMap(Tessellator tessellator) {
		tessellator.textureMap = null;
	}

	public static Tessellator getTessellator(Tessellator tessellator, Icon icon) {
		TextureMap textureMap = (TextureMap)iconMap.get(icon);

		if (textureMap == null) {
			return tessellator;
		} else {
			Tessellator newTessellator = (Tessellator)tessellator.children.get(textureMap);

			if (newTessellator == null) {
				String mapName = (String)textureMapNames.get(textureMap);

				if (mapName == null) {
					mapName = textureMap.toString();
				}

				logger.fine("new Tessellator for texture map %s gl texture %d", new Object[] {mapName, Integer.valueOf(textureMap.field_110553_a)});
				newTessellator = new Tessellator(2097152);
				copyFields(tessellator, newTessellator, true);
				newTessellator.textureMap = textureMap;
				tessellator.children.put(textureMap, newTessellator);
			} else {
				copyFields(tessellator, newTessellator, false);
			}

			return newTessellator;
		}
	}

	static void registerTextureMap(TextureMap textureMap, String name) {
		textureMapNames.put(textureMap, name);
	}

	static void registerIcon(TextureMap textureMap, Icon icon) {
		iconMap.put(icon, textureMap);
	}

	private static Field[] getFieldsToCopy(Tessellator tessellator) {
		int saveBufferSize;

		if (haveBufferSize) {
			saveBufferSize = tessellator.bufferSize;
			tessellator.bufferSize = MAGIC_VALUE.intValue();
		} else {
			saveBufferSize = 0;
		}

		int saveVertexCount = tessellator.vertexCount;
		int saveAddedVertices = tessellator.addedVertices;
		int saveRawBufferIndex = tessellator.rawBufferIndex;
		tessellator.vertexCount = MAGIC_VALUE.intValue();
		tessellator.addedVertices = MAGIC_VALUE.intValue();
		tessellator.rawBufferIndex = MAGIC_VALUE.intValue();
		ArrayList fields = new ArrayList();
		Field[] arr$ = Tessellator.class.getDeclaredFields();
		int len$ = arr$.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			Field f = arr$[i$];

			try {
				Class e = f.getType();
				int modifiers = f.getModifiers();

				if (!Modifier.isStatic(modifiers) && e.isPrimitive() && !f.getName().equals("rawBufferSize")) {
					f.setAccessible(true);

					if (e != Integer.TYPE || !MAGIC_VALUE.equals(f.get(tessellator))) {
						logger.finest("copy %s %s %s", new Object[] {Modifier.toString(f.getModifiers()), f.getType().toString(), f.getName()});
						fields.add(f);
					}
				}
			} catch (Throwable var12) {
				var12.printStackTrace();
			}
		}

		if (!haveBufferSize) {
			tessellator.bufferSize = saveBufferSize;
		}

		tessellator.vertexCount = saveVertexCount;
		tessellator.addedVertices = saveAddedVertices;
		tessellator.rawBufferIndex = saveRawBufferIndex;
		return (Field[])fields.toArray(new Field[fields.size()]);
	}

	private static void copyFields(Tessellator a, Tessellator b, boolean isNew) {
		if (fieldsToCopy == null) {
			fieldsToCopy = getFieldsToCopy(a);
		}

		Field[] arr$ = fieldsToCopy;
		int len$ = arr$.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			Field field = arr$[i$];

			try {
				Object e = field.get(a);

				if (isNew) {
					logger.finest("copy %s %s %s = %s", new Object[] {Modifier.toString(field.getModifiers()), field.getType(), field.getName(), e});
				}

				field.set(b, e);
			} catch (IllegalAccessException var8) {
				var8.printStackTrace();
			}
		}

		if (a.isDrawing && !b.isDrawing) {
			b.startDrawing(a.drawMode);
		} else if (!a.isDrawing && b.isDrawing) {
			b.reset();
		}
	}

	static void clear(Tessellator tessellator) {
		Iterator i$ = tessellator.children.values().iterator();

		while (i$.hasNext()) {
			Tessellator child = (Tessellator)i$.next();
			clear(child);
		}

		tessellator.children.clear();
		textureMapNames.clear();
		iconMap.clear();
	}

	public static void resetChildren(Tessellator tessellator) {
		Iterator i$ = tessellator.children.values().iterator();

		while (i$.hasNext()) {
			Tessellator child = (Tessellator)i$.next();
			child.reset();
		}
	}

	public static int drawChildren(int sum, Tessellator tessellator) {
		Tessellator child;

		for (Iterator i$ = tessellator.children.values().iterator(); i$.hasNext(); sum += child.draw()) {
			child = (Tessellator)i$.next();
		}

		return sum;
	}

	public static void startDrawingChildren(Tessellator tessellator, int drawMode) {
		Iterator i$ = tessellator.children.values().iterator();

		while (i$.hasNext()) {
			Tessellator child = (Tessellator)i$.next();
			child.startDrawing(drawMode);
		}
	}

	private static String toString(Tessellator tessellator) {
		if (tessellator == null) {
			return "Tessellator{null}";
		} else {
			String desc = tessellator.toString();
			TextureMap textureMap = tessellator.textureMap;

			if (textureMap != null) {
				String mapName = (String)textureMapNames.get(textureMap);

				if (mapName == null) {
					desc = textureMap.toString();
				} else {
					desc = mapName;
				}
			}

			return String.format("Tessellator{%s, isDrawing=%s, %d children}", new Object[] {desc, Boolean.valueOf(tessellator.isDrawing), Integer.valueOf(tessellator.children.size())});
		}
	}
}
