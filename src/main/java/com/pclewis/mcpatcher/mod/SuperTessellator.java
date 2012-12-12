package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import net.minecraft.src.Tessellator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

public class SuperTessellator extends Tessellator {
    private static final MCLogger logger = MCLogger.getLogger(MCPatcherUtils.CONNECTED_TEXTURES, "CTM");

    private static final Integer MAGIC_VALUE = 0x12345678;

    static SuperTessellator instance;

    private final HashMap<Integer, Tessellator> children = new HashMap<Integer, Tessellator>();
    private final Field[] fieldsToCopy;
    private final boolean isForge;

    public SuperTessellator(int bufferSize) {
        super(bufferSize);
        logger.fine("new %s(%d)", getClass().getSimpleName(), bufferSize);
        isForge = false;
        fieldsToCopy = getFieldsToCopy();
        SuperTessellator.instance = this;
    }

    public SuperTessellator() {
        super();
        logger.fine("new %s()", getClass().getSimpleName());
        isForge = true;
        fieldsToCopy = getFieldsToCopy();
        SuperTessellator.instance = this;
    }

    Tessellator getTessellator(int texture) {
        if (texture == CTMUtils.terrainTexture) {
            return this;
        }
        Tessellator newTessellator = children.get(texture);
        if (newTessellator == null) {
            logger.finer("new tessellator for texture %d", texture);
            if (isForge) {
                newTessellator = new Tessellator();
            } else {
                newTessellator = new Tessellator(Math.max(bufferSize / 16, 131072));
            }
            newTessellator.texture = texture;
            copyFields(newTessellator, true);
            children.put(texture, newTessellator);
        } else {
            copyFields(newTessellator, false);
        }
        return newTessellator;
    }

    void clearTessellators() {
        children.clear();
    }

    private Field[] getFieldsToCopy() {
        int saveBufferSize;
        if (isForge) {
            saveBufferSize = 0;
        } else {
            saveBufferSize = bufferSize;
            bufferSize = MAGIC_VALUE;
        }
        int saveVertexCount = vertexCount;
        int saveAddedVertices = addedVertices;
        int saveRawBufferIndex = rawBufferIndex;
        int saveTexture = texture;
        vertexCount = MAGIC_VALUE;
        addedVertices = MAGIC_VALUE;
        rawBufferIndex = MAGIC_VALUE;
        texture = MAGIC_VALUE;
        ArrayList<Field> fields = new ArrayList<Field>();
        for (Field f : Tessellator.class.getDeclaredFields()) {
            try {
                Class<?> type = f.getType();
                int modifiers = f.getModifiers();
                if (!Modifier.isStatic(modifiers) && type.isPrimitive() && !f.getName().equals("rawBufferSize")) {
                    f.setAccessible(true);
                    if (type == Integer.TYPE && MAGIC_VALUE.equals(f.get(this))) {
                        continue;
                    }
                    logger.finest("copy %s %s %s", Modifier.toString(f.getModifiers()), f.getType().toString(), f.getName());
                    fields.add(f);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (!isForge) {
            bufferSize = saveBufferSize;
        }
        vertexCount = saveVertexCount;
        addedVertices = saveAddedVertices;
        rawBufferIndex = saveRawBufferIndex;
        texture = saveTexture;
        return fields.toArray(new Field[fields.size()]);
    }

    private void copyFields(Tessellator newTessellator, boolean isNew) {
        for (Field f : fieldsToCopy) {
            try {
                Object value = f.get(this);
                if (isNew) {
                    logger.finest("copy %s %s %s = %s", Modifier.toString(f.getModifiers()), f.getType().toString(), f.getName(), value.toString());
                }
                f.set(newTessellator, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (isDrawing && !newTessellator.isDrawing) {
            newTessellator.startDrawing(drawMode);
        } else if (!isDrawing && newTessellator.isDrawing) {
            newTessellator.reset();
        }
    }

    @Override
    public void reset() {
        super.reset();
        for (Tessellator t : children.values()) {
            t.reset();
        }
    }

    @Override
    public int draw() {
        int total = 0;
        for (Tessellator t : children.values()) {
            total += t.draw();
        }
        return total + super.draw();
    }

    @Override
    public void startDrawing(int drawMode) {
        super.startDrawing(drawMode);
        for (Tessellator t : children.values()) {
            t.startDrawing(drawMode);
        }
    }
}
