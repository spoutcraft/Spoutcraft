package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.src.Tessellator;

public class SuperTessellator extends Tessellator {
	private static final MCLogger logger = MCLogger.getLogger("Connected Textures", "CTM");
	private static final Integer MAGIC_VALUE = Integer.valueOf(305419896);
	static SuperTessellator a;
	private final HashMap children = new HashMap();
	private final Field[] fieldsToCopy;

	public SuperTessellator(int var1) {
		super(var1);
		logger.fine("new %s(%d)", new Object[] {this.getClass().getSimpleName(), Integer.valueOf(var1)});
		this.fieldsToCopy = this.getFieldsToCopy();
		a = this;
	}

	Tessellator getTessellator(int var1) {
		if (var1 == CTMUtils.terrainTexture) {
			return this;
		} else {
			Tessellator var2 = (Tessellator)this.children.get(Integer.valueOf(var1));

			if (var2 == null) {
				logger.finer("new tessellator for texture %d", new Object[] {Integer.valueOf(var1)});
				var2 = new Tessellator(Math.max(this.bufferSize / 16, 131072));

				var2.texture = var1;
				this.copyFields(var2, true);
				this.children.put(Integer.valueOf(var1), var2);
			} else {
				this.copyFields(var2, false);
			}

			return var2;
		}
	}

	void clearTessellators() {
		this.children.clear();
	}

	private Field[] getFieldsToCopy() {
		int var1 = this.bufferSize;
		this.bufferSize = MAGIC_VALUE.intValue();

		int var2 = this.vertexCount;
		int var3 = this.addedVertices;
		int var4 = this.rawBufferIndex;
		int var5 = this.texture;
		this.vertexCount = MAGIC_VALUE.intValue();
		this.addedVertices = MAGIC_VALUE.intValue();
		this.rawBufferIndex = MAGIC_VALUE.intValue();
		this.texture = MAGIC_VALUE.intValue();
		ArrayList var6 = new ArrayList();
		Field[] var7 = Tessellator.class.getDeclaredFields();
		int var8 = var7.length;

		for (int var9 = 0; var9 < var8; ++var9) {
			Field var10 = var7[var9];

			try {
				Class var11 = var10.getType();
				int var12 = var10.getModifiers();

				if (!Modifier.isStatic(var12) && var11.isPrimitive() && !var10.getName().equals("rawBufferSize")) {
					var10.setAccessible(true);

					if (var11 != Integer.TYPE || !MAGIC_VALUE.equals(var10.get(this))) {
						logger.finest("copy %s %s %s", new Object[] {Modifier.toString(var10.getModifiers()), var10.getType().toString(), var10.getName()});
						var6.add(var10);
					}
				}
			} catch (Throwable var13) {
				var13.printStackTrace();
			}
		}

		this.bufferSize = var1;

		this.vertexCount = var2;
		this.addedVertices = var3;
		this.rawBufferIndex = var4;
		this.texture = var5;
		return (Field[])var6.toArray(new Field[var6.size()]);
	}

	private void copyFields(Tessellator var1, boolean var2) {
		Field[] var3 = this.fieldsToCopy;
		int var4 = var3.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			Field var6 = var3[var5];

			try {
				Object var7 = var6.get(this);

				if (var2) {
					logger.finest("copy %s %s %s = %s", new Object[] {Modifier.toString(var6.getModifiers()), var6.getType().toString(), var6.getName(), var7.toString()});
				}

				var6.set(var1, var7);
			} catch (IllegalAccessException var8) {
				var8.printStackTrace();
			}
		}

		if (this.isDrawing && !var1.isDrawing) {
			var1.startDrawing(this.drawMode);
		} else if (!this.isDrawing && var1.isDrawing) {
			var1.reset();
		}
	}

	/**
	 * Clears the tessellator state in preparation for new drawing.
	 */
	public void reset() {
		super.reset();
		Iterator var1 = this.children.values().iterator();

		while (var1.hasNext()) {
			Tessellator var2 = (Tessellator)var1.next();
			var2.reset();
		}
	}

	/**
	 * Draws the data set up in this tessellator and resets the state to prepare for new drawing.
	 */
	public int draw() {
		int var1 = 0;
		Tessellator var3;

		for (Iterator var2 = this.children.values().iterator(); var2.hasNext(); var1 += var3.draw()) {
			var3 = (Tessellator)var2.next();
		}

		return var1 + super.draw();
	}

	/**
	 * Resets tessellator state and prepares for drawing (with the specified draw mode).
	 */
	public void startDrawing(int var1) {
		super.startDrawing(var1);
		Iterator var2 = this.children.values().iterator();

		while (var2.hasNext()) {
			Tessellator var3 = (Tessellator)var2.next();
			var3.startDrawing(var1);
		}
	}
}
