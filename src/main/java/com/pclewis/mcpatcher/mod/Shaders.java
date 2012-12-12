// Original code written by daxnitro.  Used with permission.
// http://nitrous.daxnitro.com/repo/
// daxnitro [at] gmail.com

package com.pclewis.mcpatcher.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderGlobal;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexProgram;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.ARBTextureFloat.GL_RGB32F_ARB;
import static org.lwjgl.opengl.ARBVertexShader.GL_VERTEX_SHADER_ARB;
import static org.lwjgl.opengl.ARBVertexShader.glBindAttribLocationARB;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class Shaders {
    private Shaders() {
    }

    public static void init() {
        int maxDrawBuffers = glGetInteger(GL_MAX_DRAW_BUFFERS);

        System.out.println("GL_MAX_DRAW_BUFFERS = " + maxDrawBuffers);

        colorAttachments = 4;

        for (int i = 0; i < ProgramCount; ++i) {
            if (programNames[i].equals("")) {
                programs[i] = 0;
            } else {
                programs[i] = setupProgram("shaders/" + programNames[i] + ".vsh", "shaders/" + programNames[i] + ".fsh");
            }
        }

        if (colorAttachments > maxDrawBuffers) {
            System.out.println("Not enough draw buffers!");
        }

        for (int i = 0; i < ProgramCount; ++i) {
            for (int n = i; programs[i] == 0; n = programBackups[n]) {
                if (n == programBackups[n]) {
                    break;
                }
                programs[i] = programs[programBackups[n]];
            }
        }

        dfbDrawBuffers = BufferUtils.createIntBuffer(colorAttachments);
        for (int i = 0; i < colorAttachments; ++i) {
            dfbDrawBuffers.put(i, GL_COLOR_ATTACHMENT0_EXT + i);
        }

        dfbTextures = BufferUtils.createIntBuffer(colorAttachments);
        dfbRenderBuffers = BufferUtils.createIntBuffer(colorAttachments);

        resize();
        setupShadowMap();
        isInitialized = true;
    }

    public static void destroy() {
        for (int i = 0; i < ProgramCount; ++i) {
            if (programs[i] != 0) {
                glDeleteObjectARB(programs[i]);
                programs[i] = 0;
            }
        }
    }

    public static void glEnableWrapper(int cap) {
        glEnable(cap);
        if (cap == GL_TEXTURE_2D) {
            if (activeProgram == ProgramBasic) {
                useProgram(lightmapEnabled ? ProgramTexturedLit : ProgramTextured);
            }
        } else if (cap == GL_FOG) {
            fogEnabled = true;
            setProgramUniform1i("fogMode", glGetInteger(GL_FOG_MODE));
        }
    }

    public static void glDisableWrapper(int cap) {
        glDisable(cap);
        if (cap == GL_TEXTURE_2D) {
            if (activeProgram == ProgramTextured || activeProgram == ProgramTexturedLit) {
                useProgram(ProgramBasic);
            }
        } else if (cap == GL_FOG) {
            fogEnabled = false;
            setProgramUniform1i("fogMode", 0);
        }
    }

    public static void enableLightmap() {
        lightmapEnabled = true;
        if (activeProgram == ProgramTextured) {
            useProgram(ProgramTexturedLit);
        }
    }

    public static void disableLightmap() {
        lightmapEnabled = false;
        if (activeProgram == ProgramTexturedLit) {
            useProgram(ProgramTextured);
        }
    }

    public static void setClearColor(float red, float green, float blue) {
        clearColor[0] = red;
        clearColor[1] = green;
        clearColor[2] = blue;

        if (isShadowPass) {
            glClearColor(clearColor[0], clearColor[1], clearColor[2], 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            return;
        }

        glDrawBuffers(dfbDrawBuffers);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDrawBuffers(GL_COLOR_ATTACHMENT0_EXT);
        glClearColor(clearColor[0], clearColor[1], clearColor[2], 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDrawBuffers(GL_COLOR_ATTACHMENT1_EXT);
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDrawBuffers(dfbDrawBuffers);
    }

    public static void setCamera(float f) {
        EntityLiving viewEntity = mc.renderViewEntity;

        double x = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * f;
        double y = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * f;
        double z = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * f;

        if (isShadowPass) {
            glViewport(0, 0, shadowMapWidth, shadowMapHeight);

            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();

            if (shadowMapIsOrtho) {
                glOrtho(-shadowMapHalfPlane, shadowMapHalfPlane, -shadowMapHalfPlane, shadowMapHalfPlane, 0.05f, 256.0f);
            } else {
                // just backwards compatibility. it's only used when SHADOWFOV is set in the shaders.
                gluPerspective(shadowMapFOV, (float) shadowMapWidth / (float) shadowMapHeight, 0.05f, 256.0f);
            }

            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glTranslatef(0.0f, 0.0f, -100.0f);
            glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            float angle = -mc.getWorld().getCelestialAngle(f) * 360.0f;
            if (angle < -90.0 && angle > -270.0) {
                // night time
                glRotatef(angle + 180.0f, 0.0f, 0.0f, 1.0f);
            } else {
                // day time
                glRotatef(angle, 0.0f, 0.0f, 1.0f);
            }
            if (shadowMapIsOrtho) {
                // reduces jitter
                glTranslatef((float) x % 5.0f, (float) y % 5.0f, (float) z % 5.0f);
            }

            shadowProjection = BufferUtils.createFloatBuffer(16);
            glGetFloat(GL_PROJECTION_MATRIX, shadowProjection);
            shadowProjectionInverse = invertMat4x(shadowProjection);

            shadowModelView = BufferUtils.createFloatBuffer(16);
            glGetFloat(GL_MODELVIEW_MATRIX, shadowModelView);
            shadowModelViewInverse = invertMat4x(shadowModelView);
            return;
        }

        previousProjection = projection;
        projection = BufferUtils.createFloatBuffer(16);
        glGetFloat(GL_PROJECTION_MATRIX, projection);
        projectionInverse = invertMat4x(projection);

        previousModelView = modelView;
        modelView = BufferUtils.createFloatBuffer(16);
        glGetFloat(GL_MODELVIEW_MATRIX, modelView);
        modelViewInverse = invertMat4x(modelView);

        previousCameraPosition[0] = cameraPosition[0];
        previousCameraPosition[1] = cameraPosition[1];
        previousCameraPosition[2] = cameraPosition[2];

        cameraPosition[0] = x;
        cameraPosition[1] = y;
        cameraPosition[2] = z;
    }

    public static void beginRender(Minecraft minecraft, float f, long l) {
        if (isShadowPass) {
            return;
        }

        mc = minecraft;

        if (!isInitialized) {
            init();
        }
        if (mc.displayWidth != renderWidth || mc.displayHeight != renderHeight) {
            resize();
        }

        if (shadowPassInterval > 0 && --shadowPassCounter <= 0) {
            // do shadow pass
            preShadowPassThirdPersonView = mc.gameSettings.thirdPersonView;

            mc.gameSettings.thirdPersonView = 1;

            isShadowPass = true;
            shadowPassCounter = shadowPassInterval;

            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, sfb);

            useProgram(ProgramNone);

            mc.entityRenderer.renderWorld(f, l);

            glFlush();

            isShadowPass = false;

            mc.gameSettings.thirdPersonView = preShadowPassThirdPersonView;
        }

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, dfb);

        useProgram(lightmapEnabled ? ProgramTexturedLit : ProgramTextured);
    }

    public static void endRender() {
        if (isShadowPass) {
            return;
        }

        glPushMatrix();

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);

        // composite

        glDisable(GL_BLEND);

        useProgram(ProgramComposite);

        glDrawBuffers(dfbDrawBuffers);

        glBindTexture(GL_TEXTURE_2D, dfbTextures.get(0));
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, dfbTextures.get(1));
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, dfbTextures.get(2));
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, dfbTextures.get(3));

        if (colorAttachments >= 5) {
            glActiveTexture(GL_TEXTURE4);
            glBindTexture(GL_TEXTURE_2D, dfbTextures.get(4));
            if (colorAttachments >= 6) {
                glActiveTexture(GL_TEXTURE5);
                glBindTexture(GL_TEXTURE_2D, dfbTextures.get(5));
                if (colorAttachments >= 7) {
                    glActiveTexture(GL_TEXTURE6);
                    glBindTexture(GL_TEXTURE_2D, dfbTextures.get(6));
                }
            }
        }

        if (shadowPassInterval > 0) {
            glActiveTexture(GL_TEXTURE7);
            glBindTexture(GL_TEXTURE_2D, sfbDepthTexture);
        }

        glActiveTexture(GL_TEXTURE0);

        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, 0.0f);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3f(1.0f, 0.0f, 0.0f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, 0.0f);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3f(0.0f, 1.0f, 0.0f);
        glEnd();

        // final

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

        useProgram(ProgramFinal);

        glClearColor(clearColor[0], clearColor[1], clearColor[2], 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glBindTexture(GL_TEXTURE_2D, dfbTextures.get(0));
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, dfbTextures.get(1));
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, dfbTextures.get(2));
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, dfbTextures.get(3));

        if (colorAttachments >= 5) {
            glActiveTexture(GL_TEXTURE4);
            glBindTexture(GL_TEXTURE_2D, dfbTextures.get(4));
            if (colorAttachments >= 6) {
                glActiveTexture(GL_TEXTURE5);
                glBindTexture(GL_TEXTURE_2D, dfbTextures.get(5));
                if (colorAttachments >= 7) {
                    glActiveTexture(GL_TEXTURE6);
                    glBindTexture(GL_TEXTURE_2D, dfbTextures.get(6));
                }
            }
        }

        if (shadowPassInterval > 0) {
            glActiveTexture(GL_TEXTURE7);
            glBindTexture(GL_TEXTURE_2D, sfbDepthTexture);
        }

        glActiveTexture(GL_TEXTURE0);

        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, 0.0f);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3f(1.0f, 0.0f, 0.0f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, 0.0f);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3f(0.0f, 1.0f, 0.0f);
        glEnd();

        glEnable(GL_BLEND);

        glPopMatrix();

        useProgram(ProgramNone);
    }

    public static void beginTerrain() {
        useProgram(Shaders.ProgramTerrain);
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain_nh.png"));
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain_s.png"));
        glActiveTexture(GL_TEXTURE0);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
    }

    public static void endTerrain() {
        useProgram(lightmapEnabled ? ProgramTexturedLit : ProgramTextured);
    }

    public static void beginWater() {
        useProgram(Shaders.ProgramWater);
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain_nh.png"));
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain_s.png"));
        glActiveTexture(GL_TEXTURE0);
    }

    public static void endWater() {
        useProgram(lightmapEnabled ? ProgramTexturedLit : ProgramTextured);
    }

    public static void beginHand() {
        glEnable(GL_BLEND);
        useProgram(Shaders.ProgramHand);
    }

    public static void endHand() {
        glDisable(GL_BLEND);
        useProgram(lightmapEnabled ? ProgramTexturedLit : ProgramTextured);

        if (isShadowPass) {
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, sfb); // was set to 0 in beginWeather()
        }
    }

    public static void beginWeather() {
        glEnable(GL_BLEND);
        useProgram(Shaders.ProgramWeather);

        if (isShadowPass) {
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0); // will be set to sbf in endHand()
        }
    }

    public static void endWeather() {
        glDisable(GL_BLEND);
        useProgram(lightmapEnabled ? ProgramTexturedLit : ProgramTextured);
    }

    private static void resize() {
        renderWidth = mc.displayWidth;
        renderHeight = mc.displayHeight;
        setupFrameBuffer();
    }

    private static void setupShadowMap() {
        setupShadowFrameBuffer();
    }

    private static int setupProgram(String vShaderPath, String fShaderPath) {
        int program = glCreateProgramObjectARB();

        int vShader = 0;
        int fShader = 0;

        if (program != 0) {
            vShader = createVertShader(vShaderPath);
            fShader = createFragShader(fShaderPath);
        }

        if (vShader != 0 || fShader != 0) {
            if (vShader != 0) {
                glAttachObjectARB(program, vShader);
            }
            if (fShader != 0) {
                glAttachObjectARB(program, fShader);
            }
            if (entityAttrib >= 0) {
                glBindAttribLocationARB(program, entityAttrib, "mc_Entity");
            }
            glLinkProgramARB(program);
            glValidateProgramARB(program);
            printLogInfo(program);
        } else if (program != 0) {
            glDeleteObjectARB(program);
            program = 0;
        }

        return program;
    }

    public static void useProgram(int program) {
        if (activeProgram == program) {
            return;
        } else if (isShadowPass) {
            activeProgram = ProgramNone;
            glUseProgramObjectARB(programs[ProgramNone]);
            return;
        }
        activeProgram = program;
        glUseProgramObjectARB(programs[program]);
        if (programs[program] == 0) {
            activeProgram = ProgramNone;
            return;
        } else if (program == ProgramTextured) {
            setProgramUniform1i("texture", 0);
        } else if (program == ProgramTexturedLit || program == ProgramHand || program == ProgramWeather) {
            setProgramUniform1i("texture", 0);
            setProgramUniform1i("lightmap", 1);
        } else if (program == ProgramTerrain || program == ProgramWater) {
            setProgramUniform1i("texture", 0);
            setProgramUniform1i("lightmap", 1);
            setProgramUniform1i("normals", 2);
            setProgramUniform1i("specular", 3);
        } else if (program == ProgramComposite || program == ProgramFinal) {
            setProgramUniform1i("gcolor", 0);
            setProgramUniform1i("gdepth", 1);
            setProgramUniform1i("gnormal", 2);
            setProgramUniform1i("composite", 3);
            setProgramUniform1i("gaux1", 4);
            setProgramUniform1i("gaux2", 5);
            setProgramUniform1i("gaux3", 6);
            setProgramUniform1i("shadow", 7);
            setProgramUniformMatrix4ARB("gbufferPreviousProjection", false, previousProjection);
            setProgramUniformMatrix4ARB("gbufferProjection", false, projection);
            setProgramUniformMatrix4ARB("gbufferProjectionInverse", false, projectionInverse);
            setProgramUniformMatrix4ARB("gbufferPreviousModelView", false, previousModelView);
            if (shadowPassInterval > 0) {
                setProgramUniformMatrix4ARB("shadowProjection", false, shadowProjection);
                setProgramUniformMatrix4ARB("shadowProjectionInverse", false, shadowProjectionInverse);
                setProgramUniformMatrix4ARB("shadowModelView", false, shadowModelView);
                setProgramUniformMatrix4ARB("shadowModelViewInverse", false, shadowModelViewInverse);
            }
        }
        ItemStack stack = mc.thePlayer.inventory.getCurrentItem();
        setProgramUniform1i("heldItemId", (stack == null ? -1 : stack.itemID));
        setProgramUniform1i("heldBlockLightValue", (stack == null || stack.itemID >= 256 ? 0 : Block.lightValue[stack.itemID]));
        setProgramUniform1i("fogMode", (fogEnabled ? glGetInteger(GL_FOG_MODE) : 0));
        setProgramUniform1i("worldTime", (int) (mc.getWorld().getWorldTime() % 24000L));
        setProgramUniform1f("aspectRatio", (float) renderWidth / (float) renderHeight);
        setProgramUniform1f("viewWidth", (float) renderWidth);
        setProgramUniform1f("viewHeight", (float) renderHeight);
        setProgramUniform1f("near", 0.05F);
        setProgramUniform1f("far", 256 >> mc.gameSettings.renderDistance);
        setProgramUniform3f("sunPosition", sunPosition[0], sunPosition[1], sunPosition[2]);
        setProgramUniform3f("moonPosition", moonPosition[0], moonPosition[1], moonPosition[2]);
        setProgramUniform3f("previousCameraPosition", (float) previousCameraPosition[0], (float) previousCameraPosition[1], (float) previousCameraPosition[2]);
        setProgramUniform3f("cameraPosition", (float) cameraPosition[0], (float) cameraPosition[1], (float) cameraPosition[2]);
        setProgramUniformMatrix4ARB("gbufferModelView", false, modelView);
        setProgramUniformMatrix4ARB("gbufferModelViewInverse", false, modelViewInverse);
    }

    public static void setProgramUniform1i(String name, int x) {
        if (activeProgram == ProgramNone) {
            return;
        }
        int uniform = glGetUniformLocationARB(programs[activeProgram], name);
        glUniform1iARB(uniform, x);
    }

    public static void setProgramUniform1f(String name, float x) {
        if (activeProgram == ProgramNone) {
            return;
        }
        int uniform = glGetUniformLocationARB(programs[activeProgram], name);
        glUniform1fARB(uniform, x);
    }

    public static void setProgramUniform3f(String name, float x, float y, float z) {
        if (activeProgram == ProgramNone) {
            return;
        }
        int uniform = glGetUniformLocationARB(programs[activeProgram], name);
        glUniform3fARB(uniform, x, y, z);
    }

    public static void setProgramUniformMatrix4ARB(String name, boolean transpose, FloatBuffer matrix) {
        if (activeProgram == ProgramNone || matrix == null) {
            return;
        }
        int uniform = glGetUniformLocation(programs[activeProgram], name);
        glUniformMatrix4ARB(uniform, transpose, matrix);
    }

    public static void setCelestialPosition() {
        // This is called when the current matrix is the modelview matrix based on the celestial angle.
        // The sun is at (0, 100, 0), and the moon is at (0, -100, 0).
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        glGetFloat(GL_MODELVIEW_MATRIX, modelView);
        float[] mv = new float[16];
        modelView.get(mv, 0, 16);
        float[] sunPos = multiplyMat4xVec4(mv, new float[]{0.0F, 100.0F, 0.0F, 0.0F});
        sunPosition = sunPos;
        float[] moonPos = multiplyMat4xVec4(mv, new float[]{0.0F, -100.0F, 0.0F, 0.0F});
        moonPosition = moonPos;
    }

    public static void glDrawArraysWrapper(int mode, int first, int count, ShortBuffer shortBuffer) {
        if (entityAttrib >= 0) {
            ARBVertexProgram.glEnableVertexAttribArrayARB(entityAttrib);
            ARBVertexProgram.glVertexAttribPointerARB(entityAttrib, 2, false, false, 4, (ShortBuffer) shortBuffer.position(0));
        }
        GL11.glDrawArrays(mode, first, count);
        if (entityAttrib >= 0) {
            ARBVertexProgram.glDisableVertexAttribArrayARB(entityAttrib);
        }
    }

    public static int sortAndRenderWrapper(RenderGlobal renderGlobal, EntityLiving entityLiving, int i, double d) {
        if (i == 0) {
            beginTerrain();
        } else if (i == 1) {
            beginWater();
        }
        int result = renderGlobal.sortAndRender(entityLiving, i, d);
        if (i == 0) {
            endTerrain();
        } else if (i == 1) {
            endWater();
        }
        return result;
    }

    private static float[] multiplyMat4xVec4(float[] ta, float[] tb) {
        float[] mout = new float[4];
        mout[0] = ta[0] * tb[0] + ta[4] * tb[1] + ta[8] * tb[2] + ta[12] * tb[3];
        mout[1] = ta[1] * tb[0] + ta[5] * tb[1] + ta[9] * tb[2] + ta[13] * tb[3];
        mout[2] = ta[2] * tb[0] + ta[6] * tb[1] + ta[10] * tb[2] + ta[14] * tb[3];
        mout[3] = ta[3] * tb[0] + ta[7] * tb[1] + ta[11] * tb[2] + ta[15] * tb[3];
        return mout;
    }

    private static FloatBuffer invertMat4x(FloatBuffer matin) {
        float[] m = new float[16];
        float[] inv = new float[16];
        float det;
        int i;

        for (i = 0; i < 16; ++i) {
            m[i] = matin.get(i);
        }

        inv[0] = m[5] * m[10] * m[15] - m[5] * m[11] * m[14] - m[9] * m[6] * m[15] + m[9] * m[7] * m[14] + m[13] * m[6] * m[11] - m[13] * m[7] * m[10];
        inv[4] = -m[4] * m[10] * m[15] + m[4] * m[11] * m[14] + m[8] * m[6] * m[15] - m[8] * m[7] * m[14] - m[12] * m[6] * m[11] + m[12] * m[7] * m[10];
        inv[8] = m[4] * m[9] * m[15] - m[4] * m[11] * m[13] - m[8] * m[5] * m[15] + m[8] * m[7] * m[13] + m[12] * m[5] * m[11] - m[12] * m[7] * m[9];
        inv[12] = -m[4] * m[9] * m[14] + m[4] * m[10] * m[13] + m[8] * m[5] * m[14] - m[8] * m[6] * m[13] - m[12] * m[5] * m[10] + m[12] * m[6] * m[9];
        inv[1] = -m[1] * m[10] * m[15] + m[1] * m[11] * m[14] + m[9] * m[2] * m[15] - m[9] * m[3] * m[14] - m[13] * m[2] * m[11] + m[13] * m[3] * m[10];
        inv[5] = m[0] * m[10] * m[15] - m[0] * m[11] * m[14] - m[8] * m[2] * m[15] + m[8] * m[3] * m[14] + m[12] * m[2] * m[11] - m[12] * m[3] * m[10];
        inv[9] = -m[0] * m[9] * m[15] + m[0] * m[11] * m[13] + m[8] * m[1] * m[15] - m[8] * m[3] * m[13] - m[12] * m[1] * m[11] + m[12] * m[3] * m[9];
        inv[13] = m[0] * m[9] * m[14] - m[0] * m[10] * m[13] - m[8] * m[1] * m[14] + m[8] * m[2] * m[13] + m[12] * m[1] * m[10] - m[12] * m[2] * m[9];
        inv[2] = m[1] * m[6] * m[15] - m[1] * m[7] * m[14] - m[5] * m[2] * m[15] + m[5] * m[3] * m[14] + m[13] * m[2] * m[7] - m[13] * m[3] * m[6];
        inv[6] = -m[0] * m[6] * m[15] + m[0] * m[7] * m[14] + m[4] * m[2] * m[15] - m[4] * m[3] * m[14] - m[12] * m[2] * m[7] + m[12] * m[3] * m[6];
        inv[10] = m[0] * m[5] * m[15] - m[0] * m[7] * m[13] - m[4] * m[1] * m[15] + m[4] * m[3] * m[13] + m[12] * m[1] * m[7] - m[12] * m[3] * m[5];
        inv[14] = -m[0] * m[5] * m[14] + m[0] * m[6] * m[13] + m[4] * m[1] * m[14] - m[4] * m[2] * m[13] - m[12] * m[1] * m[6] + m[12] * m[2] * m[5];
        inv[3] = -m[1] * m[6] * m[11] + m[1] * m[7] * m[10] + m[5] * m[2] * m[11] - m[5] * m[3] * m[10] - m[9] * m[2] * m[7] + m[9] * m[3] * m[6];
        inv[7] = m[0] * m[6] * m[11] - m[0] * m[7] * m[10] - m[4] * m[2] * m[11] + m[4] * m[3] * m[10] + m[8] * m[2] * m[7] - m[8] * m[3] * m[6];
        inv[11] = -m[0] * m[5] * m[11] + m[0] * m[7] * m[9] + m[4] * m[1] * m[11] - m[4] * m[3] * m[9] - m[8] * m[1] * m[7] + m[8] * m[3] * m[5];
        inv[15] = m[0] * m[5] * m[10] - m[0] * m[6] * m[9] - m[4] * m[1] * m[10] + m[4] * m[2] * m[9] + m[8] * m[1] * m[6] - m[8] * m[2] * m[5];

        det = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];

        FloatBuffer invout = BufferUtils.createFloatBuffer(16);

        if (det == 0.0) {
            // no inverse :(
            return invout; // not actually the inverse
        }


        for (i = 0; i < 16; ++i) {
            invout.put(i, inv[i] / det);
        }

        return invout;
    }

    private static int createVertShader(String filename) {
        int vertShader = glCreateShaderObjectARB(GL_VERTEX_SHADER_ARB);
        if (vertShader == 0) {
            return 0;
        }
        String vertexCode = "";
        String line;

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader((Shaders.class).getResourceAsStream(filename)));
        } catch (Exception e) {
            try {
                reader = new BufferedReader(new FileReader(new File(filename)));
            } catch (Exception e2) {
                System.out.println("Couldn't open " + filename + "!");
                glDeleteObjectARB(vertShader);
                return 0;
            }
        }

        try {
            while ((line = reader.readLine()) != null) {
                vertexCode += line + "\n";
                if (line.matches("attribute [_a-zA-Z0-9]+ mc_Entity.*")) {
                    entityAttrib = 10;
                }
            }
        } catch (Exception e) {
            System.out.println("Couldn't read " + filename + "!");
            glDeleteObjectARB(vertShader);
            return 0;
        }

        glShaderSourceARB(vertShader, vertexCode);
        glCompileShaderARB(vertShader);
        printLogInfo(vertShader);
        return vertShader;
    }

    private static int createFragShader(String filename) {
        int fragShader = glCreateShaderObjectARB(GL_FRAGMENT_SHADER_ARB);
        if (fragShader == 0) {
            return 0;
        }
        String fragCode = "";
        String line;

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader((Shaders.class).getResourceAsStream(filename)));
        } catch (Exception e) {
            try {
                reader = new BufferedReader(new FileReader(new File(filename)));
            } catch (Exception e2) {
                System.out.println("Couldn't open " + filename + "!");
                glDeleteObjectARB(fragShader);
                return 0;
            }
        }

        try {
            while ((line = reader.readLine()) != null) {
                fragCode += line + "\n";
                if (colorAttachments < 5 && line.matches("uniform [ _a-zA-Z0-9]+ gaux1;.*")) {
                    colorAttachments = 5;
                } else if (colorAttachments < 6 && line.matches("uniform [ _a-zA-Z0-9]+ gaux2;.*")) {
                    colorAttachments = 6;
                } else if (colorAttachments < 7 && line.matches("uniform [ _a-zA-Z0-9]+ gaux3;.*")) {
                    colorAttachments = 7;
                } else if (colorAttachments < 8 && line.matches("uniform [ _a-zA-Z0-9]+ shadow;.*")) {
                    shadowPassInterval = 1;
                    colorAttachments = 8;
                } else if (line.matches("/\\* SHADOWRES:[0-9]+ \\*/.*")) {
                    String[] parts = line.split("(:| )", 4);
                    System.out.println("Shadow map resolution: " + parts[2]);
                    shadowMapWidth = shadowMapHeight = Integer.parseInt(parts[2]);
                } else if (line.matches("/\\* SHADOWFOV:[0-9\\.]+ \\*/.*")) {
                    String[] parts = line.split("(:| )", 4);
                    System.out.println("Shadow map field of view: " + parts[2]);
                    shadowMapFOV = Float.parseFloat(parts[2]);
                    shadowMapIsOrtho = false;
                } else if (line.matches("/\\* SHADOWHPL:[0-9\\.]+ \\*/.*")) {
                    String[] parts = line.split("(:| )", 4);
                    System.out.println("Shadow map half-plane: " + parts[2]);
                    shadowMapHalfPlane = Float.parseFloat(parts[2]);
                    shadowMapIsOrtho = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Couldn't read " + filename + "!");
            glDeleteObjectARB(fragShader);
            return 0;
        }

        glShaderSourceARB(fragShader, fragCode);
        glCompileShaderARB(fragShader);
        printLogInfo(fragShader);
        return fragShader;
    }

    private static boolean printLogInfo(int obj) {
        IntBuffer iVal = BufferUtils.createIntBuffer(1);
        glGetObjectParameterARB(obj, GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);

        int length = iVal.get();
        if (length > 1) {
            ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
            iVal.flip();
            glGetInfoLogARB(obj, iVal, infoLog);
            byte[] infoBytes = new byte[length];
            infoLog.get(infoBytes);
            String out = new String(infoBytes);
            System.out.println("Info log:\n" + out);
            return false;
        }
        return true;
    }

    private static void setupFrameBuffer() {
        setupRenderTextures();

        if (dfb != 0) {
            glDeleteFramebuffersEXT(dfb);
            glDeleteRenderbuffersEXT(dfbRenderBuffers);
        }

        dfb = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, dfb);

        glGenRenderbuffersEXT(dfbRenderBuffers);

        for (int i = 0; i < colorAttachments; ++i) {
            glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, dfbRenderBuffers.get(i));
            if (i == 1) { // depth buffer
                glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_RGB32F_ARB, renderWidth, renderHeight);
            } else {
                glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_RGBA, renderWidth, renderHeight);
            }
            glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, dfbDrawBuffers.get(i), GL_RENDERBUFFER_EXT, dfbRenderBuffers.get(i));
            glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, dfbDrawBuffers.get(i), GL_TEXTURE_2D, dfbTextures.get(i), 0);
        }

        glDeleteRenderbuffersEXT(dfbDepthBuffer);
        dfbDepthBuffer = glGenRenderbuffersEXT();
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, dfbDepthBuffer);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT, renderWidth, renderHeight);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, dfbDepthBuffer);

        int status = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);
        if (status != GL_FRAMEBUFFER_COMPLETE_EXT) {
            System.out.println("Failed creating framebuffer! (Status " + status + ")");
        }
    }

    private static void setupShadowFrameBuffer() {
        if (shadowPassInterval <= 0) {
            return;
        }

        setupShadowRenderTexture();

        glDeleteFramebuffersEXT(sfb);

        sfb = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, sfb);

        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);

        glDeleteRenderbuffersEXT(sfbDepthBuffer);
        sfbDepthBuffer = glGenRenderbuffersEXT();
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, sfbDepthBuffer);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT, shadowMapWidth, shadowMapHeight);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, sfbDepthBuffer);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_TEXTURE_2D, sfbDepthTexture, 0);

        int status = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);
        if (status != GL_FRAMEBUFFER_COMPLETE_EXT) {
            System.out.println("Failed creating shadow framebuffer! (Status " + status + ")");
        }
    }

    private static void setupRenderTextures() {
        glDeleteTextures(dfbTextures);
        glGenTextures(dfbTextures);

        for (int i = 0; i < colorAttachments; ++i) {
            glBindTexture(GL_TEXTURE_2D, dfbTextures.get(i));
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            if (i == 1) { // depth buffer
                ByteBuffer buffer = ByteBuffer.allocateDirect(renderWidth * renderHeight * 4 * 4);
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F_ARB, renderWidth, renderHeight, 0, GL_RGBA, GL11.GL_FLOAT, buffer);
            } else {
                ByteBuffer buffer = ByteBuffer.allocateDirect(renderWidth * renderHeight * 4);
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, renderWidth, renderHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            }
        }
    }

    private static void setupShadowRenderTexture() {
        if (shadowPassInterval <= 0) {
            return;
        }

        // depth
        glDeleteTextures(sfbDepthTexture);
        sfbDepthTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, sfbDepthTexture);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        ByteBuffer buffer = ByteBuffer.allocateDirect(shadowMapWidth * shadowMapHeight * 4);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, shadowMapWidth, shadowMapHeight, 0, GL_DEPTH_COMPONENT, GL11.GL_FLOAT, buffer);
    }

    private static boolean isInitialized = false;

    private static int renderWidth = 0;
    private static int renderHeight = 0;

    private static Minecraft mc = null;

    private static float[] sunPosition = new float[3];
    private static float[] moonPosition = new float[3];

    private static float[] clearColor = new float[3];

    private static boolean lightmapEnabled = false;
    private static boolean fogEnabled = true;

    public static int entityAttrib = -1;

    private static FloatBuffer previousProjection = null;

    private static FloatBuffer projection = null;
    private static FloatBuffer projectionInverse = null;

    private static FloatBuffer previousModelView = null;

    private static FloatBuffer modelView = null;
    private static FloatBuffer modelViewInverse = null;

    private static double[] previousCameraPosition = new double[3];
    private static double[] cameraPosition = new double[3];

    // Shadow stuff

    // configuration
    private static int shadowPassInterval = 0;
    private static int shadowMapWidth = 1024;
    private static int shadowMapHeight = 1024;
    private static float shadowMapFOV = 25.0f;
    private static float shadowMapHalfPlane = 30.0f;
    private static boolean shadowMapIsOrtho = true;

    private static int shadowPassCounter = 0;

    private static int preShadowPassThirdPersonView;

    private static boolean isShadowPass = false;

    private static int sfb = 0;
    private static int sfbColorTexture = 0;
    private static int sfbDepthTexture = 0;
    private static int sfbRenderBuffer = 0;
    private static int sfbDepthBuffer = 0;

    private static FloatBuffer shadowProjection = null;
    private static FloatBuffer shadowProjectionInverse = null;

    private static FloatBuffer shadowModelView = null;
    private static FloatBuffer shadowModelViewInverse = null;

    // Color attachment stuff

    private static int colorAttachments = 0;

    private static IntBuffer dfbDrawBuffers = null;

    private static IntBuffer dfbTextures = null;
    private static IntBuffer dfbRenderBuffers = null;

    private static int dfb = 0;
    private static int dfbDepthBuffer = 0;

    // Program stuff

    public static int activeProgram = 0;

    public final static int ProgramNone = 0;
    public final static int ProgramBasic = 1;
    public final static int ProgramTextured = 2;
    public final static int ProgramTexturedLit = 3;
    public final static int ProgramTerrain = 4;
    public final static int ProgramWater = 5;
    public final static int ProgramHand = 6;
    public final static int ProgramWeather = 7;
    public final static int ProgramComposite = 8;
    public final static int ProgramFinal = 9;
    public final static int ProgramCount = 10;

    private static String[] programNames = new String[]{
        "",
        "gbuffers_basic",
        "gbuffers_textured",
        "gbuffers_textured_lit",
        "gbuffers_terrain",
        "gbuffers_water",
        "gbuffers_hand",
        "gbuffers_weather",
        "composite",
        "final",
    };

    private static int[] programBackups = new int[]{
        ProgramNone,            // none
        ProgramNone,            // basic
        ProgramBasic,           // textured
        ProgramTextured,        // textured/lit
        ProgramTexturedLit,     // terrain
        ProgramTerrain,         // water
        ProgramTexturedLit,     // hand
        ProgramTexturedLit,     // weather
        ProgramNone,            // composite
        ProgramNone,            // final
    };

    private static int[] programs = new int[ProgramCount];
}
