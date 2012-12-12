package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RenderPass {
    private static final MCLogger logger = MCLogger.getLogger(MCPatcherUtils.BETTER_GLASS);

    private static final String RENDERPASS_PROPERTIES = "/renderpass.properties";

    private static final int[] baseRenderPass = new int[Block.blocksList.length];
    private static final int[] extraRenderPass = new int[Block.blocksList.length];

    private static int srcBlend;
    private static int dstBlend;
    private static boolean enableLightmap;

    private static int renderPass = -1;
    private static int maxRenderPass = 1;
    private static boolean ambientOcclusion;

    static {
        RenderPassAPI.instance = new RenderPassAPI() {
            @Override
            boolean skipDefaultRendering(Block block) {
                return renderPass > 1;
            }

            @Override
            boolean skipThisRenderPass(Block block, int pass) {
                if (pass < 0) {
                    pass = block.getRenderBlockPass();
                }
                return pass != renderPass;
            }

            @Override
            void clear() {
                maxRenderPass = 1;
                Arrays.fill(baseRenderPass, -1);
                Arrays.fill(extraRenderPass, -1);

                for (int i = 0; i < Block.blocksList.length; i++) {
                    Block block = Block.blocksList[i];
                    if (block != null) {
                        baseRenderPass[i] = block.getRenderBlockPass();
                    }
                }
            }

            @Override
            void setRenderPassForBlock(Block block, int pass) {
                if (pass < 0) {
                    return;
                }
                if (pass <= 2) {
                    baseRenderPass[block.blockID] = pass;
                } else {
                    extraRenderPass[block.blockID] = pass;
                }
                maxRenderPass = Math.max(maxRenderPass, pass);
            }

            @Override
            void finish() {
                RenderPass.finish();
            }
        };

        TexturePackAPI.ChangeHandler.register(new TexturePackAPI.ChangeHandler(MCPatcherUtils.BETTER_GLASS, 3) {
            @Override
            protected void onChange() {
                srcBlend = GL11.GL_SRC_ALPHA;
                dstBlend = GL11.GL_ONE_MINUS_SRC_ALPHA;
                enableLightmap = true;

                Properties properties = TexturePackAPI.getProperties(RENDERPASS_PROPERTIES);
                if (properties != null) {
                    String method = properties.getProperty("blend.3", "alpha").trim().toLowerCase();
                    Matcher matcher = Pattern.compile("(\\d+)\\s+(\\d+)").matcher(method);
                    if (method.equals("alpha")) {
                        // nothing
                    } else if (method.equals("overlay") || method.equals("color")) {
                        srcBlend = GL11.GL_DST_COLOR;
                        dstBlend = GL11.GL_SRC_COLOR;
                        enableLightmap = false;
                    } else if (matcher.matches()) {
                        try {
                            srcBlend = Integer.parseInt(matcher.group(1));
                            dstBlend = Integer.parseInt(matcher.group(2));
                        } catch (NumberFormatException e) {
                        }
                    } else {
                        logger.severe("%s: unknown blend method '%s'", RENDERPASS_PROPERTIES, method);
                    }

                    String value = properties.getProperty("enableLightmap.3", "").trim().toLowerCase();
                    if (!value.equals("")) {
                        enableLightmap = Boolean.parseBoolean(value);
                    }
                }
            }
        });
    }

    public static void start(int pass) {
        finish();
        renderPass = pass;
    }

    public static void finish() {
        renderPass = -1;
    }

    public static boolean skipAllRenderPasses(boolean[] skipRenderPass) {
        for (boolean b : skipRenderPass) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    public static int getBlockRenderPass(Block block) {
        if (renderPass <= 2) {
            return baseRenderPass[block.blockID];
        } else {
            return extraRenderPass[block.blockID];
        }
    }

    public static boolean canRenderInPass(Block block, int pass, boolean renderThis) {
        if (baseRenderPass[block.blockID] < 2 && extraRenderPass[block.blockID] < 2) {
            return renderThis;
        } else {
            return pass == getBlockRenderPass(block);
        }
    }

    public static boolean shouldSideBeRendered(Block block, IBlockAccess blockAccess, int i, int j, int k, int face) {
        if (block.shouldSideBeRendered(blockAccess, i, j, k, face)) {
            return true;
        } else {
            return extraRenderPass[blockAccess.getBlockId(i, j, k)] >= 0 && extraRenderPass[block.blockID] < 0;
        }
    }

    public static boolean setAmbientOcclusion(boolean ambientOcclusion) {
        RenderPass.ambientOcclusion = ambientOcclusion;
        return ambientOcclusion;
    }

    public static float getAOBaseMultiplier(float multiplier) {
        return renderPass > 2 && !enableLightmap ? 1.0f : multiplier;
    }

    public static void doRenderPass(RenderGlobal renderer, EntityLiving camera, int pass, double partialTick) {
        if (pass > maxRenderPass) {
            return;
        }
        switch (pass) {
            case 2:
                GL11.glDisable(GL11.GL_CULL_FACE);
                renderer.sortAndRender(camera, pass, partialTick);
                GL11.glEnable(GL11.GL_CULL_FACE);
                break;

            case 3:
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPolygonOffset(-2.0f, -2.0f);
                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_CULL_FACE);
                if (ambientOcclusion) {
                    GL11.glShadeModel(GL11.GL_SMOOTH);
                }
                GL11.glBlendFunc(srcBlend, dstBlend);

                renderer.sortAndRender(camera, pass, partialTick);

                GL11.glPolygonOffset(0.0f, 0.0f);
                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glShadeModel(GL11.GL_FLAT);
                break;

            default:
                break;
        }
    }

    public static void enableDisableLightmap(EntityRenderer renderer, double partialTick, int pass) {
        if (enableLightmap || pass != 3) {
            renderer.enableLightmap(partialTick);
        } else {
            renderer.disableLightmap(partialTick);
        }
    }
}
