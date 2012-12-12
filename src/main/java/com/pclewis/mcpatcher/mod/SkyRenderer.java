package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

public class SkyRenderer {
    private static RenderEngine renderEngine;
    private static double worldTime;
    private static float celestialAngle;
    private static float rainStrength;

    private static final HashMap<Integer, WorldEntry> worldSkies = new HashMap<Integer, WorldEntry>();
    private static WorldEntry currentWorld;

    public static boolean active;

    static {
        TexturePackAPI.ChangeHandler.register(new TexturePackAPI.ChangeHandler(MCPatcherUtils.BETTER_SKIES, 2) {
            @Override
            protected void onChange() {
                worldSkies.clear();
                World world = MCPatcherUtils.getMinecraft().getWorld();
                if (world != null) {
                    getWorldEntry(world.getWorldInfo().getDimension());
                }
            }
        });
    }

    public static void setup(World world, RenderEngine renderEngine, float partialTick, float celestialAngle) {
        if (TexturePackAPI.isDefaultTexturePack()) {
            active = false;
        } else {
            int worldType = MCPatcherUtils.getMinecraft().getWorld().getWorldInfo().getDimension();
            WorldEntry newEntry = getWorldEntry(worldType);
            if (newEntry != currentWorld && currentWorld != null) {
                currentWorld.unloadTextures();
            }
            currentWorld = newEntry;
            active = currentWorld.active();
            if (active) {
                SkyRenderer.renderEngine = renderEngine;
                worldTime = world.getWorldTime() + partialTick;
                rainStrength = 1.0f - world.getRainStrength(partialTick);
                SkyRenderer.celestialAngle = celestialAngle;
            }
        }
    }

    public static void renderAll() {
        if (active) {
            currentWorld.renderAll(Tessellator.instance);
        }
    }

    public static String setupCelestialObject(String defaultTexture) {
        if (active) {
            Layer.clearBlendingMethod();
            Layer layer = currentWorld.getCelestialObject(defaultTexture);
            if (layer != null) {
                layer.setBlendingMethod(rainStrength);
                return layer.texture;
            }
        }
        return defaultTexture;
    }

    private static WorldEntry getWorldEntry(int worldType) {
        WorldEntry entry = worldSkies.get(worldType);
        if (entry == null) {
            entry = new WorldEntry(worldType);
            worldSkies.put(worldType, entry);
        }
        return entry;
    }

    private static class WorldEntry {
        private final int worldType;
        private final ArrayList<Layer> skies;
        private final HashMap<String, Layer> objects;
        private final HashSet<String> textures;

        WorldEntry(int worldType) {
            this.worldType = worldType;
            skies = new ArrayList<Layer>();
            objects = new HashMap<String, Layer>();
            textures = new HashSet<String>();
            loadSkies();
            loadCelestialObject("sun", "/terrain/sun.png");
            loadCelestialObject("moon", "/terrain/moon_phases.png");
        }

        private void loadSkies() {
            for (int i = -1; ; i++) {
                String prefix = "/terrain/sky" + worldType + "/sky" + (i < 0 ? "" : "" + i);
                Layer layer = Layer.create(prefix);
                if (layer == null) {
                    if (i > 0) {
                        break;
                    }
                } else if (layer.valid) {
                    skies.add(layer);
                    textures.add(layer.texture);
                }
            }
        }

        private void loadCelestialObject(String objName, String textureName) {
            String prefix = "/terrain/sky" + worldType + "/" + objName;
            Properties properties = TexturePackAPI.getProperties(prefix + ".properties");
            if (properties != null) {
                properties.setProperty("fade", "false");
                properties.setProperty("rotate", "true");
                Layer layer = new Layer(prefix, properties);
                if (layer.valid) {
                    objects.put(textureName, layer);
                }
            }
        }

        boolean active() {
            return !skies.isEmpty() || !objects.isEmpty();
        }

        void renderAll(Tessellator tessellator) {
            HashSet<String> texturesNeeded = new HashSet<String>();
            for (Layer layer : skies) {
                if (layer.prepare()) {
                    texturesNeeded.add(layer.texture);
                }
            }
            HashSet<String> texturesToUnload = new HashSet<String>();
            texturesToUnload.addAll(textures);
            texturesToUnload.removeAll(texturesNeeded);
            for (String s : texturesToUnload) {
                TexturePackAPI.unloadTexture(s);
            }
            for (Layer layer : skies) {
                if (layer.brightness > 0.0f) {
                    layer.render(tessellator);
                    Layer.clearBlendingMethod();
                }
            }
        }

        Layer getCelestialObject(String defaultTexture) {
            return objects.get(defaultTexture);
        }

        void unloadTextures() {
            for (Layer layer : skies) {
                TexturePackAPI.unloadTexture(layer.texture);
            }
        }
    }

    private static class Layer {
        private static final int SECS_PER_DAY = 24 * 60 * 60;
        private static final int TICKS_PER_DAY = 24000;
        private static final double TOD_OFFSET = -0.25;

        private static final double SKY_DISTANCE = 100.0;

        private static final int METHOD_ADD = 1;
        private static final int METHOD_SUBTRACT = 2;
        private static final int METHOD_MULTIPLY = 3;
        private static final int METHOD_DODGE = 4;
        private static final int METHOD_BURN = 5;
        private static final int METHOD_SCREEN = 6;
        private static final int METHOD_REPLACE = 7;

        private String prefix;
        private Properties properties;
        private String texture;
        private boolean fade;
        private boolean rotate;
        private float[] axis;
        private float speed;
        private int blendMethod;

        private double a;
        private double b;
        private double c;

        boolean valid;

        float brightness;

        static Layer create(String prefix) {
            Properties properties = TexturePackAPI.getProperties(prefix + ".properties");
            if (properties == null) {
                return null;
            } else {
                return new Layer(prefix, properties);
            }
        }

        Layer(String prefix, Properties properties) {
            this.prefix = prefix;
            this.properties = properties;
            valid = true;
            valid = (readTexture() && readRotation() & readBlendingMethod() && readFadeTimers());
        }

        private boolean readTexture() {
            texture = properties.getProperty("source", prefix + ".png");
            if (TexturePackAPI.hasResource(texture)) {
                return true;
            } else {
                return addError("source texture %s not found", texture);
            }
        }

        private boolean readRotation() {
            rotate = Boolean.parseBoolean(properties.getProperty("rotate", "true"));
            if (rotate) {
                try {
                    speed = Float.parseFloat(properties.getProperty("speed", "1.0"));
                } catch (NumberFormatException e) {
                    return addError("invalid rotation speed");
                }

                String value = properties.getProperty("axis", "0.0 0.0 1.0").trim().toLowerCase();
                String[] tokens = value.split("\\s+");
                if (tokens.length == 3) {
                    float x;
                    float y;
                    float z;
                    try {
                        x = Float.parseFloat(tokens[0]);
                        y = Float.parseFloat(tokens[1]);
                        z = Float.parseFloat(tokens[2]);
                    } catch (NumberFormatException e) {
                        return addError("invalid rotation axis");
                    }
                    if (x * x + y * y + z * z == 0.0f) {
                        return addError("rotation axis cannot be 0");
                    }
                    axis = new float[]{z, y, -x};
                } else {
                    return addError("invalid rotate value %s", value);
                }
            }
            return true;
        }

        private boolean readBlendingMethod() {
            String value = properties.getProperty("blend", "add").trim().toLowerCase();
            if (value.equals("add")) {
                blendMethod = METHOD_ADD;
            } else if (value.equals("subtract")) {
                blendMethod = METHOD_SUBTRACT;
            } else if (value.equals("multiply")) {
                blendMethod = METHOD_MULTIPLY;
            } else if (value.equals("dodge")) {
                blendMethod = METHOD_DODGE;
            } else if (value.equals("burn")) {
                blendMethod = METHOD_BURN;
            } else if (value.equals("screen")) {
                blendMethod = METHOD_SCREEN;
            } else if (value.equals("replace")) {
                blendMethod = METHOD_REPLACE;
            } else {
                return addError("unknown blend method %s", value);
            }
            return true;
        }

        private boolean readFadeTimers() {
            fade = Boolean.parseBoolean(properties.getProperty("fade", "true"));
            if (!fade) {
                return true;
            }
            int startFadeIn = parseTime(properties, "startFadeIn");
            int endFadeIn = parseTime(properties, "endFadeIn");
            int endFadeOut = parseTime(properties, "endFadeOut");
            if (!valid) {
                return false;
            }
            while (endFadeIn <= startFadeIn) {
                endFadeIn += SECS_PER_DAY;
            }
            while (endFadeOut <= endFadeIn) {
                endFadeOut += SECS_PER_DAY;
            }
            if (endFadeOut - startFadeIn >= SECS_PER_DAY) {
                return addError("fade times must fall within a 24 hour period");
            }
            int startFadeOut = startFadeIn + endFadeOut - endFadeIn;

            // f(x) = a cos x + b sin x + c
            // f(s0) = 0
            // f(s1) = 1
            // f(e1) = 0
            // Solve for a, b, c using Cramer's rule.
            double s0 = normalize(startFadeIn, SECS_PER_DAY, TOD_OFFSET);
            double s1 = normalize(endFadeIn, SECS_PER_DAY, TOD_OFFSET);
            double e0 = normalize(startFadeOut, SECS_PER_DAY, TOD_OFFSET);
            double e1 = normalize(endFadeOut, SECS_PER_DAY, TOD_OFFSET);
            double det = Math.cos(s0) * Math.sin(s1) + Math.cos(e1) * Math.sin(s0) + Math.cos(s1) * Math.sin(e1) -
                Math.cos(s0) * Math.sin(e1) - Math.cos(s1) * Math.sin(s0) - Math.cos(e1) * Math.sin(s1);
            if (det == 0.0) {
                return addError("determinant is 0");
            }
            a = (Math.sin(e1) - Math.sin(s0)) / det;
            b = (Math.cos(s0) - Math.cos(e1)) / det;
            c = (Math.cos(e1) * Math.sin(s0) - Math.cos(s0) * Math.sin(e1)) / det;
            return true;
        }

        private boolean addError(String format, Object... params) {
            valid = false;
            return false;
        }

        private int parseTime(Properties properties, String key) {
            String s = properties.getProperty(key, "").trim();
            if ("".equals(s)) {
                addError("missing value for %s", key);
                return -1;
            }
            String[] t = s.split(":");
            if (t.length >= 2) {
                try {
                    int hh = Integer.parseInt(t[0].trim());
                    int mm = Integer.parseInt(t[1].trim());
                    int ss;
                    if (t.length >= 3) {
                        ss = Integer.parseInt(t[2].trim());
                    } else {
                        ss = 0;
                    }
                    return (60 * 60 * hh + 60 * mm + ss) % SECS_PER_DAY;
                } catch (NumberFormatException e) {
                }
            }
            addError("invalid %s time %s", key, s);
            return -1;
        }

        private static double normalize(double time, int period, double offset) {
            return 2.0 * Math.PI * (time / period + offset);
        }

        private double f(double x) {
            return a * Math.cos(x) + b * Math.sin(x) + c;
        }

        boolean prepare() {
            brightness = rainStrength;
            if (fade) {
                double x = normalize(worldTime, TICKS_PER_DAY, 0.0);
                brightness *= (float) f(x);
            }

            if (brightness <= 0.0f) {
                return false;
            }
            if (brightness > 1.0f) {
                brightness = 1.0f;
            }
            return true;
        }

        boolean render(Tessellator tessellator) {
            renderEngine.bindTexture(renderEngine.getTexture(texture));
            setBlendingMethod(brightness);

            GL11.glPushMatrix();

            if (rotate) {
                GL11.glRotatef(celestialAngle * 360.0f * speed, axis[0], axis[1], axis[2]);
            }

            // north
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
            drawTile(tessellator, 4);

            // top
            GL11.glPushMatrix();
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            drawTile(tessellator, 1);
            GL11.glPopMatrix();

            // bottom
            GL11.glPushMatrix();
            GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
            drawTile(tessellator, 0);
            GL11.glPopMatrix();

            // west
            GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
            drawTile(tessellator, 5);

            // south
            GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
            drawTile(tessellator, 2);

            // east
            GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
            drawTile(tessellator, 3);

            GL11.glPopMatrix();

            return true;
        }

        private static void drawTile(Tessellator tessellator, int tile) {
            double tileX = (tile % 3) / 3.0;
            double tileY = (tile / 3) / 2.0;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE, tileX, tileY);
            tessellator.addVertexWithUV(-SKY_DISTANCE, -SKY_DISTANCE, SKY_DISTANCE, tileX, tileY + 0.5);
            tessellator.addVertexWithUV(SKY_DISTANCE, -SKY_DISTANCE, SKY_DISTANCE, tileX + 1.0 / 3.0, tileY + 0.5);
            tessellator.addVertexWithUV(SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE, tileX + 1.0 / 3.0, tileY);
            tessellator.draw();
        }

        void setBlendingMethod(float brightness) {
            if (blendMethod == METHOD_ADD || blendMethod == METHOD_REPLACE) {
                GL11.glColor4f(1.0f, 1.0f, 1.0f, brightness);
            } else if (blendMethod == METHOD_MULTIPLY) {
                GL11.glColor4f(brightness, brightness, brightness, brightness);
            } else {
                GL11.glColor4f(brightness, brightness, brightness, 1.0f);
            }

            switch (blendMethod) {
                case METHOD_ADD:
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                    break;

                case METHOD_SUBTRACT:
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
                    break;

                case METHOD_MULTIPLY:
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    break;

                case METHOD_DODGE:
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                    break;

                case METHOD_BURN:
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
                    break;

                case METHOD_SCREEN:
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_COLOR);
                    break;

                case METHOD_REPLACE:
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                    GL11.glDisable(GL11.GL_BLEND);
                    break;

                default:
                    break;
            }

            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        static void clearBlendingMethod() {
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, rainStrength);
        }
    }
}
