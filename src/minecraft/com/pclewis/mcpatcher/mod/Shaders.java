package com.pclewis.mcpatcher.mod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderGlobal;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexProgram;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.client.io.FileUtil;

public class Shaders {
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
	private static int shadowPassInterval = 0;
	private static int shadowMapWidth = 1024;
	private static int shadowMapHeight = 1024;
	private static float shadowMapFOV = 25.0F;
	private static float shadowMapHalfPlane = 30.0F;
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
	private static int colorAttachments = 0;
	private static IntBuffer dfbDrawBuffers = null;
	private static IntBuffer dfbTextures = null;
	private static IntBuffer dfbRenderBuffers = null;
	private static int dfb = 0;
	private static int dfbDepthBuffer = 0;
	public static int activeProgram = 0;
	public static final int ProgramNone = 0;
	public static final int ProgramBasic = 1;
	public static final int ProgramTextured = 2;
	public static final int ProgramTexturedLit = 3;
	public static final int ProgramTerrain = 4;
	public static final int ProgramWater = 5;
	public static final int ProgramHand = 6;
	public static final int ProgramWeather = 7;
	public static final int ProgramComposite = 8;
	public static final int ProgramFinal = 9;
	public static final int ProgramCount = 10;
	private static String[] programNames = new String[]{"", "gbuffers_basic", "gbuffers_textured", "gbuffers_textured_lit", "gbuffers_terrain", "gbuffers_water", "gbuffers_hand", "gbuffers_weather", "composite", "final"};
	private static int[] programBackups = new int[]{0, 0, 1, 2, 3, 4, 3, 3, 0, 0};
	private static int[] programs = new int[10];
	private static boolean enabled= true;
	
	public static void setup(int type) {
		System.out.println("OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
		System.out.println("OpenGL Platform: " + GL11.glGetString(GL11.GL_VENDOR));
		
		if(!(enabled)) return;
		mc = Minecraft.theMinecraft;
		int var0 = GL11.glGetInteger(GL20.GL_MAX_DRAW_BUFFERS);
		System.out.println("GL_MAX_DRAW_BUFFERS = " + var0);
		
		colorAttachments = 4;
		
		String mode = "";
		switch(type) {
			case 1: mode = "low/"; break;
			case 2: mode = "medium/"; break;
			case 3: mode = "high/"; break;
			case 4: mode = new File(Minecraft.getMinecraftDir(), "shaders").getAbsolutePath() + "/"; break;
			default: mode = "low/";
		}

		int var1;
		for (var1 = 0; var1 < 10; ++var1) {
			if (programNames[var1].equals("")) {
				programs[var1] = 0;
			} else {
				programs[var1] = setupProgram(mode + programNames[var1] + ".vsh", mode + programNames[var1] + ".fsh");
			}
		}

		if (colorAttachments > var0) {
			System.out.println("Not enough draw buffers!");
		}

		for (var1 = 0; var1 < 10; ++var1) {
			for (int var2 = var1; programs[var1] == 0 && var2 != programBackups[var2]; var2 = programBackups[var2]) {
				programs[var1] = programs[programBackups[var2]];
			}
		}

		dfbDrawBuffers = BufferUtils.createIntBuffer(colorAttachments);

		for (var1 = 0; var1 < colorAttachments; ++var1) {
			dfbDrawBuffers.put(var1, 36064 + var1);
		}

		dfbTextures = BufferUtils.createIntBuffer(colorAttachments);
		dfbRenderBuffers = BufferUtils.createIntBuffer(colorAttachments);
	}

	public static void init(int type) {
		setup(type);
		resize();
		setupShadowMap();
		isInitialized = true;
	}
	
	public static boolean isOpenGL2() {
		try {
			String version = GL11.glGetString(GL11.GL_VERSION);
			return Integer.parseInt(String.valueOf(version.charAt(0))) > 1;
		}
		catch (Exception e) {
			return false;
		}
	}

	public static void destroy() {
		if(!(enabled)) return;
		for (int var0 = 0; var0 < 10; ++var0) {
			if (programs[var0] != 0) {
				ARBShaderObjects.glDeleteObjectARB(programs[var0]);
				programs[var0] = 0;
			}
		}
	}

	public static void glEnableWrapper(int var0) {
		GL11.glEnable(var0);
		if(!(enabled)) return;
		if (var0 == 3553) {
			if (activeProgram == 1) {
				useProgram(lightmapEnabled?3:2);
			}
		} else if (var0 == 2912) {
			fogEnabled = true;
			setProgramUniform1i("fogMode", GL11.glGetInteger(GL11.GL_FOG_MODE));
		}
	}

	public static void glDisableWrapper(int var0) {
		GL11.glDisable(var0);
		if(!(enabled)) return;
		if (var0 == 3553) {
			if (activeProgram == 2 || activeProgram == 3) {
				useProgram(1);
			}
		} else if (var0 == 2912) {
			fogEnabled = false;
			setProgramUniform1i("fogMode", 0);
		}
	}

	public static void enableLightmap() {
		if(!(enabled)) return;
		lightmapEnabled = true;
		if (activeProgram == 2) {
			useProgram(3);
		}
	}

	public static void disableLightmap() {
		if(!(enabled)) return;
		lightmapEnabled = false;
		if (activeProgram == 3) {
			useProgram(2);
		}
	}

	public static void setClearColor(float var0, float var1, float var2) {
		if(!(enabled)) return;
		clearColor[0] = var0;
		clearColor[1] = var1;
		clearColor[2] = var2;
		if (isShadowPass) {
			GL11.glClearColor(clearColor[0], clearColor[1], clearColor[2], 1.0F);
			GL11.glClear(16640);
		} else {
			GL20.glDrawBuffers(dfbDrawBuffers);
			GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
			GL11.glClear(16640);
			GL20.glDrawBuffers(36064);
			GL11.glClearColor(clearColor[0], clearColor[1], clearColor[2], 1.0F);
			GL11.glClear(16640);
			GL20.glDrawBuffers(36065);
			GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glClear(16640);
			GL20.glDrawBuffers(dfbDrawBuffers);
		}
	}

	public static void setCamera(float var0) {
		if(!(enabled)) return;
		EntityLiving var1 = mc.renderViewEntity;
		double var2 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var0;
		double var4 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var0;
		double var6 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var0;
		if (isShadowPass) {
			GL11.glViewport(0, 0, shadowMapWidth, shadowMapHeight);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			if (shadowMapIsOrtho) {
				GL11.glOrtho((double)(-shadowMapHalfPlane), (double)shadowMapHalfPlane, (double)(-shadowMapHalfPlane), (double)shadowMapHalfPlane, 0.05D, 256.0D);
			} else {
				GLU.gluPerspective(shadowMapFOV, (float)shadowMapWidth / (float)shadowMapHeight, 0.05F, 256.0F);
			}

			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -100.0F);
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			float var8 = -mc.theWorld.getCelestialAngle(var0) * 360.0F;
			if ((double)var8 < -90.0D && (double)var8 > -270.0D) {
				GL11.glRotatef(var8 + 180.0F, 0.0F, 0.0F, 1.0F);
			} else {
				GL11.glRotatef(var8, 0.0F, 0.0F, 1.0F);
			}

			if (shadowMapIsOrtho) {
				GL11.glTranslatef((float)var2 % 5.0F, (float)var4 % 5.0F, (float)var6 % 5.0F);
			}

			shadowProjection = BufferUtils.createFloatBuffer(16);
			GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, shadowProjection);
			shadowProjectionInverse = invertMat4x(shadowProjection);
			shadowModelView = BufferUtils.createFloatBuffer(16);
			GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, shadowModelView);
			shadowModelViewInverse = invertMat4x(shadowModelView);
		} else {
			previousProjection = projection;
			projection = BufferUtils.createFloatBuffer(16);
			GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
			projectionInverse = invertMat4x(projection);
			previousModelView = modelView;
			modelView = BufferUtils.createFloatBuffer(16);
			GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelView);
			modelViewInverse = invertMat4x(modelView);
			previousCameraPosition[0] = cameraPosition[0];
			previousCameraPosition[1] = cameraPosition[1];
			previousCameraPosition[2] = cameraPosition[2];
			cameraPosition[0] = var2;
			cameraPosition[1] = var4;
			cameraPosition[2] = var6;
		}
	}
	
	public static boolean isShadowPass() {
		return isShadowPass;
	}

	public static void beginRender(Minecraft var0, float var1, long var2) {
		if(!(enabled)) return;
		if (!isShadowPass) {
			mc = var0;
			if (!isInitialized) {
				init(ConfigReader.shaderType);
			}

			if (mc.displayWidth != renderWidth || mc.displayHeight != renderHeight) {
				resize();
			}

			//shadowPassInterval = 0;
			if (shadowPassInterval > 0 && --shadowPassCounter <= 0) {
				preShadowPassThirdPersonView = mc.gameSettings.thirdPersonView;
				mc.gameSettings.thirdPersonView = 1;
				isShadowPass = true;
				shadowPassCounter = shadowPassInterval;
				EXTFramebufferObject.glBindFramebufferEXT(36160, sfb);
				useProgram(0);
				mc.entityRenderer.renderWorld(var1, var2);
				GL11.glFlush();
				isShadowPass = false;
				mc.gameSettings.thirdPersonView = preShadowPassThirdPersonView;
			}

			EXTFramebufferObject.glBindFramebufferEXT(36160, dfb);
			useProgram(lightmapEnabled?3:2);
		}
	}

	public static void endRender() {
		if(!(enabled)) return;
		if (!isShadowPass) {
			GL11.glPushMatrix();
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, 1.0D, 0.0D, 1.0D, 0.0D, 1.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			useProgram(8);
			GL20.glDrawBuffers(dfbDrawBuffers);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(0));
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(1));
			GL13.glActiveTexture(GL13.GL_TEXTURE2);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(2));
			GL13.glActiveTexture(GL13.GL_TEXTURE3);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(3));
			if (colorAttachments >= 5) {
				GL13.glActiveTexture(GL13.GL_TEXTURE4);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(4));
				if (colorAttachments >= 6) {
					GL13.glActiveTexture(GL13.GL_TEXTURE5);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(5));
					if (colorAttachments >= 7) {
						GL13.glActiveTexture(GL13.GL_TEXTURE6);
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(6));
					}
				}
			}

			if (shadowPassInterval > 0) {
				GL13.glActiveTexture(GL13.GL_TEXTURE7);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, sfbDepthTexture);
			}

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 0.0F);
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 0.0F);
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, 1.0F, 0.0F);
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex3f(0.0F, 1.0F, 0.0F);
			GL11.glEnd();
			EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
			useProgram(9);
			GL11.glClearColor(clearColor[0], clearColor[1], clearColor[2], 1.0F);
			GL11.glClear(16640);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(0));
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(1));
			GL13.glActiveTexture(GL13.GL_TEXTURE2);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(2));
			GL13.glActiveTexture(GL13.GL_TEXTURE3);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(3));
			if (colorAttachments >= 5) {
				GL13.glActiveTexture(GL13.GL_TEXTURE4);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(4));
				if (colorAttachments >= 6) {
					GL13.glActiveTexture(GL13.GL_TEXTURE5);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(5));
					if (colorAttachments >= 7) {
						GL13.glActiveTexture(GL13.GL_TEXTURE6);
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(6));
					}
				}
			}

			if (shadowPassInterval > 0) {
				GL13.glActiveTexture(GL13.GL_TEXTURE7);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, sfbDepthTexture);
			}

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0.0F, 0.0F);
			GL11.glVertex3f(0.0F, 0.0F, 0.0F);
			GL11.glTexCoord2f(1.0F, 0.0F);
			GL11.glVertex3f(1.0F, 0.0F, 0.0F);
			GL11.glTexCoord2f(1.0F, 1.0F);
			GL11.glVertex3f(1.0F, 1.0F, 0.0F);
			GL11.glTexCoord2f(0.0F, 1.0F);
			GL11.glVertex3f(0.0F, 1.0F, 0.0F);
			GL11.glEnd();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glPopMatrix();
			useProgram(0);
		}
	}

	public static void beginTerrain() {
		if(!(enabled)) return;
		useProgram(4);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain_nh.png"));
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain_s.png"));
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		FloatBuffer var0 = BufferUtils.createFloatBuffer(16);
	}

	public static void endTerrain() {
		if(!(enabled)) return;
		useProgram(lightmapEnabled?3:2);
	}

	public static void beginWater() {
		if(!(enabled)) return;
		useProgram(5);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain_nh.png"));
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain_s.png"));
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	}

	public static void endWater() {
		if(!(enabled)) return;
		useProgram(lightmapEnabled?3:2);
	}

	public static void beginHand() {
		if(!(enabled)) return;
		boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_BLEND);
		useProgram(6);
		//restore the proper state
		if (!blend) {
			GL11.glDisable(GL11.GL_BLEND);
		}
	}

	public static void endHand() {
		if(!(enabled)) return;
		boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_BLEND);
		useProgram(lightmapEnabled?3:2);
		if (isShadowPass) {
			EXTFramebufferObject.glBindFramebufferEXT(36160, sfb);
		}
		//restore the proper state
		if (blend) {
			GL11.glEnable(GL11.GL_BLEND);
		}
	}

	public static void beginWeather() {
		if(!(enabled)) return;
		GL11.glEnable(GL11.GL_BLEND);
		useProgram(7);
		if (isShadowPass) {
			EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
		}
	}

	public static void endWeather() {
		if(!(enabled)) return;
		GL11.glDisable(GL11.GL_BLEND);
		useProgram(lightmapEnabled?3:2);
	}

	private static void resize() {
		if(!(enabled)) return;
		renderWidth = mc.displayWidth;
		renderHeight = mc.displayHeight;
		setupFrameBuffer();
	}

	private static void setupShadowMap() {
		if(!(enabled)) return;
		setupShadowFrameBuffer();
	}

	private static int setupProgram(String var0, String var1) {
		if(!(enabled)) return -1;
		int var2 = ARBShaderObjects.glCreateProgramObjectARB();
		int var3 = 0;
		int var4 = 0;
		if (var2 != 0) {
			var3 = createVertShader(var0);
			var4 = createFragShader(var1);
		}

		if (var3 == 0 && var4 == 0) {
			if (var2 != 0) {
				ARBShaderObjects.glDeleteObjectARB(var2);
				var2 = 0;
			}
		} else {
			if (var3 != 0) {
				ARBShaderObjects.glAttachObjectARB(var2, var3);
			}

			if (var4 != 0) {
				ARBShaderObjects.glAttachObjectARB(var2, var4);
			}

			if (entityAttrib >= 0) {
				ARBVertexShader.glBindAttribLocationARB(var2, entityAttrib, "mc_Entity");
			}

			ARBShaderObjects.glLinkProgramARB(var2);
			ARBShaderObjects.glValidateProgramARB(var2);
			printLogInfo(var2);
		}

		return var2;
	}

	public static void useProgram(int var0) {
		if(!(enabled)) return;
		if (activeProgram != var0) {
			if (isShadowPass) {
				activeProgram = 0;
				ARBShaderObjects.glUseProgramObjectARB(programs[0]);
			} else {
				activeProgram = var0;
				ARBShaderObjects.glUseProgramObjectARB(programs[var0]);
				if (programs[var0] == 0) {
					activeProgram = 0;
				} else {
					if (var0 == 2) {
						setProgramUniform1i("texture", 0);
					} else if (var0 != 3 && var0 != 6 && var0 != 7) {
						if (var0 != 4 && var0 != 5) {
							if (var0 == 8 || var0 == 9) {
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
						} else {
							setProgramUniform1i("texture", 0);
							setProgramUniform1i("lightmap", 1);
							setProgramUniform1i("normals", 2);
							setProgramUniform1i("specular", 3);
						}
					} else {
						setProgramUniform1i("texture", 0);
						setProgramUniform1i("lightmap", 1);
					}

					ItemStack var1 = mc.thePlayer.inventory.getCurrentItem();
					setProgramUniform1i("heldItemId", var1 == null?-1:var1.itemID);
					setProgramUniform1i("heldBlockLightValue", var1 != null && var1.itemID < 256?Block.lightValue[var1.itemID]:0);
					setProgramUniform1i("fogMode", fogEnabled?GL11.glGetInteger(GL11.GL_FOG_MODE):0);
					setProgramUniform1i("worldTime", (int)(mc.theWorld.getWorldTime() % 24000L));
					setProgramUniform1f("aspectRatio", (float)renderWidth / (float)renderHeight);
					setProgramUniform1f("viewWidth", (float)renderWidth);
					setProgramUniform1f("viewHeight", (float)renderHeight);
					setProgramUniform1f("near", 0.05F);
					setProgramUniform1f("far", (float)(256 >> mc.gameSettings.renderDistance));
					setProgramUniform3f("sunPosition", sunPosition[0], sunPosition[1], sunPosition[2]);
					setProgramUniform3f("moonPosition", moonPosition[0], moonPosition[1], moonPosition[2]);
					setProgramUniform3f("previousCameraPosition", (float)previousCameraPosition[0], (float)previousCameraPosition[1], (float)previousCameraPosition[2]);
					setProgramUniform3f("cameraPosition", (float)cameraPosition[0], (float)cameraPosition[1], (float)cameraPosition[2]);
					setProgramUniformMatrix4ARB("gbufferModelView", false, modelView);
					setProgramUniformMatrix4ARB("gbufferModelViewInverse", false, modelViewInverse);
				}
			}
		}
	}

	public static void setProgramUniform1i(String var0, int var1) {
		if(!(enabled)) return;
		if (activeProgram != 0) {
			int var2 = ARBShaderObjects.glGetUniformLocationARB(programs[activeProgram], var0);
			ARBShaderObjects.glUniform1iARB(var2, var1);
		}
	}

	public static void setProgramUniform1f(String var0, float var1) {
		if(!(enabled)) return;
		if (activeProgram != 0) {
			int var2 = ARBShaderObjects.glGetUniformLocationARB(programs[activeProgram], var0);
			ARBShaderObjects.glUniform1fARB(var2, var1);
		}
	}

	public static void setProgramUniform3f(String var0, float var1, float var2, float var3) {
		if(!(enabled)) return;
		if (activeProgram != 0) {
			int var4 = ARBShaderObjects.glGetUniformLocationARB(programs[activeProgram], var0);
			ARBShaderObjects.glUniform3fARB(var4, var1, var2, var3);
		}
	}

	public static void setProgramUniformMatrix4ARB(String var0, boolean var1, FloatBuffer var2) {
		if(!(enabled)) return;
		if (activeProgram != 0 && var2 != null) {
			int var3 = GL20.glGetUniformLocation(programs[activeProgram], var0);
			ARBShaderObjects.glUniformMatrix4ARB(var3, var1, var2);
		}
	}

	public static void setCelestialPosition() {
		if(!(enabled)) return;
		FloatBuffer var0 = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, var0);
		float[] var1 = new float[16];
		var0.get(var1, 0, 16);
		float[] var2 = multiplyMat4xVec4(var1, new float[]{0.0F, 100.0F, 0.0F, 0.0F});
		sunPosition = var2;
		float[] var3 = multiplyMat4xVec4(var1, new float[]{0.0F, -100.0F, 0.0F, 0.0F});
		moonPosition = var3;
	}

	public static void glDrawArraysWrapper(int var0, int var1, int var2, ShortBuffer var3) {
		if(!(enabled)) {
			GL11.glDrawArrays(var0, var1, var2);
			return;
		}
		if (entityAttrib >= 0) {
			ARBVertexProgram.glEnableVertexAttribArrayARB(entityAttrib);
			ARBVertexProgram.glVertexAttribPointerARB(entityAttrib, 2, false, false, 4, (ShortBuffer)var3.position(0));
		}
			GL11.glDrawArrays(var0, var1, var2);

		if (entityAttrib >= 0) {
			ARBVertexProgram.glDisableVertexAttribArrayARB(entityAttrib);
		}
	}

	public static int sortAndRenderWrapper(RenderGlobal var0, EntityLiving var1, int var2, double var3) {
		if(!(enabled)) {
			return var0.sortAndRender(var1, var2, var3);
		}
		if (var2 == 0) {
			beginTerrain();
		} else if (var2 == 1) {
			//beginWater();
		}

		int var5 = var0.sortAndRender(var1, var2, var3);
		if (var2 == 0) {
			endTerrain();
		} else if (var2 == 1) {
			//endWater();
		}

		return var5;
	}

	private static float[] multiplyMat4xVec4(float[] var0, float[] var1) {
		if(!(enabled)) return null;
		float[] var2 = new float[]{var0[0] * var1[0] + var0[4] * var1[1] + var0[8] * var1[2] + var0[12] * var1[3], var0[1] * var1[0] + var0[5] * var1[1] + var0[9] * var1[2] + var0[13] * var1[3], var0[2] * var1[0] + var0[6] * var1[1] + var0[10] * var1[2] + var0[14] * var1[3], var0[3] * var1[0] + var0[7] * var1[1] + var0[11] * var1[2] + var0[15] * var1[3]};
		return var2;
	}

	private static FloatBuffer invertMat4x(FloatBuffer var0) {
		if(!(enabled)) return null;
		float[] var1 = new float[16];
		float[] var2 = new float[16];

		int var4;
		for (var4 = 0; var4 < 16; ++var4) {
			var1[var4] = var0.get(var4);
		}

		var2[0] = var1[5] * var1[10] * var1[15] - var1[5] * var1[11] * var1[14] - var1[9] * var1[6] * var1[15] + var1[9] * var1[7] * var1[14] + var1[13] * var1[6] * var1[11] - var1[13] * var1[7] * var1[10];
		var2[4] = -var1[4] * var1[10] * var1[15] + var1[4] * var1[11] * var1[14] + var1[8] * var1[6] * var1[15] - var1[8] * var1[7] * var1[14] - var1[12] * var1[6] * var1[11] + var1[12] * var1[7] * var1[10];
		var2[8] = var1[4] * var1[9] * var1[15] - var1[4] * var1[11] * var1[13] - var1[8] * var1[5] * var1[15] + var1[8] * var1[7] * var1[13] + var1[12] * var1[5] * var1[11] - var1[12] * var1[7] * var1[9];
		var2[12] = -var1[4] * var1[9] * var1[14] + var1[4] * var1[10] * var1[13] + var1[8] * var1[5] * var1[14] - var1[8] * var1[6] * var1[13] - var1[12] * var1[5] * var1[10] + var1[12] * var1[6] * var1[9];
		var2[1] = -var1[1] * var1[10] * var1[15] + var1[1] * var1[11] * var1[14] + var1[9] * var1[2] * var1[15] - var1[9] * var1[3] * var1[14] - var1[13] * var1[2] * var1[11] + var1[13] * var1[3] * var1[10];
		var2[5] = var1[0] * var1[10] * var1[15] - var1[0] * var1[11] * var1[14] - var1[8] * var1[2] * var1[15] + var1[8] * var1[3] * var1[14] + var1[12] * var1[2] * var1[11] - var1[12] * var1[3] * var1[10];
		var2[9] = -var1[0] * var1[9] * var1[15] + var1[0] * var1[11] * var1[13] + var1[8] * var1[1] * var1[15] - var1[8] * var1[3] * var1[13] - var1[12] * var1[1] * var1[11] + var1[12] * var1[3] * var1[9];
		var2[13] = var1[0] * var1[9] * var1[14] - var1[0] * var1[10] * var1[13] - var1[8] * var1[1] * var1[14] + var1[8] * var1[2] * var1[13] + var1[12] * var1[1] * var1[10] - var1[12] * var1[2] * var1[9];
		var2[2] = var1[1] * var1[6] * var1[15] - var1[1] * var1[7] * var1[14] - var1[5] * var1[2] * var1[15] + var1[5] * var1[3] * var1[14] + var1[13] * var1[2] * var1[7] - var1[13] * var1[3] * var1[6];
		var2[6] = -var1[0] * var1[6] * var1[15] + var1[0] * var1[7] * var1[14] + var1[4] * var1[2] * var1[15] - var1[4] * var1[3] * var1[14] - var1[12] * var1[2] * var1[7] + var1[12] * var1[3] * var1[6];
		var2[10] = var1[0] * var1[5] * var1[15] - var1[0] * var1[7] * var1[13] - var1[4] * var1[1] * var1[15] + var1[4] * var1[3] * var1[13] + var1[12] * var1[1] * var1[7] - var1[12] * var1[3] * var1[5];
		var2[14] = -var1[0] * var1[5] * var1[14] + var1[0] * var1[6] * var1[13] + var1[4] * var1[1] * var1[14] - var1[4] * var1[2] * var1[13] - var1[12] * var1[1] * var1[6] + var1[12] * var1[2] * var1[5];
		var2[3] = -var1[1] * var1[6] * var1[11] + var1[1] * var1[7] * var1[10] + var1[5] * var1[2] * var1[11] - var1[5] * var1[3] * var1[10] - var1[9] * var1[2] * var1[7] + var1[9] * var1[3] * var1[6];
		var2[7] = var1[0] * var1[6] * var1[11] - var1[0] * var1[7] * var1[10] - var1[4] * var1[2] * var1[11] + var1[4] * var1[3] * var1[10] + var1[8] * var1[2] * var1[7] - var1[8] * var1[3] * var1[6];
		var2[11] = -var1[0] * var1[5] * var1[11] + var1[0] * var1[7] * var1[9] + var1[4] * var1[1] * var1[11] - var1[4] * var1[3] * var1[9] - var1[8] * var1[1] * var1[7] + var1[8] * var1[3] * var1[5];
		var2[15] = var1[0] * var1[5] * var1[10] - var1[0] * var1[6] * var1[9] - var1[4] * var1[1] * var1[10] + var1[4] * var1[2] * var1[9] + var1[8] * var1[1] * var1[6] - var1[8] * var1[2] * var1[5];
		float var3 = var1[0] * var2[0] + var1[1] * var2[4] + var1[2] * var2[8] + var1[3] * var2[12];
		FloatBuffer var5 = BufferUtils.createFloatBuffer(16);
		if ((double)var3 == 0.0D) {
			return var5;
		} else {
			for (var4 = 0; var4 < 16; ++var4) {
				var5.put(var4, var2[var4] / var3);
			}

			return var5;
		}
	}
	
	private static BufferedReader getShaderFile(String fileName, int id) {
		BufferedReader reader;
		final String origFileName = fileName;
		fileName = "/res/shaders/" + fileName;
		
		//Try inside jar
		try {
			reader = new BufferedReader(new InputStreamReader(Shaders.class.getResourceAsStream(fileName)));
			return reader;
		}
		catch (Exception ignore) { }
		
		//Try MCP path
		try {
			File file = new File(FileUtil.getSpoutcraftDirectory().getAbsolutePath() + "/../../" + fileName);
			reader = new BufferedReader(new FileReader(file));
			return reader;
		}
		catch (Exception ignore) { }
		
		//Try Eclipse Path
		try {
			File file = new File(FileUtil.getSpoutcraftDirectory().getAbsolutePath() + "/../../../" + fileName);
			reader = new BufferedReader(new FileReader(file));
			return reader;
		}
		catch (Exception ignore) { }
		
		//Try External File Path
		try {
			File file = new File(origFileName);
			reader = new BufferedReader(new FileReader(file));
			return reader;
		}
		catch (Exception ignore) { }
		
		//Failed
		System.out.println("Couldn\'t find shader file " + fileName + "!");
		ARBShaderObjects.glDeleteObjectARB(id);
		return null;
	}

	private static int createVertShader(String var0) {
		if(!(enabled)) return -1;
		int var1 = ARBShaderObjects.glCreateShaderObjectARB(35633);
		if (var1 == 0) {
			return 0;
		} else {
			String var2 = "";

			BufferedReader var4 = getShaderFile(var0, var1);
			if (var4 == null) {
				return 0;
			}

			String var3;
			try {
				while ((var3 = var4.readLine()) != null) {
					var2 = var2 + var3 + "\n";
					if (var3.matches("attribute [_a-zA-Z0-9]+ mc_Entity.*")) {
						entityAttrib = 10;
					}
				}
			} catch (Exception var9) {
				System.out.println("Couldn\'t read " + var0 + "!");
				ARBShaderObjects.glDeleteObjectARB(var1);
				return 0;
			}

			ARBShaderObjects.glShaderSourceARB(var1, var2);
			ARBShaderObjects.glCompileShaderARB(var1);
			printLogInfo(var1);
			return var1;
		}
	}

	private static int createFragShader(String var0) {
		if(!(enabled)) return -1;
		int var1 = ARBShaderObjects.glCreateShaderObjectARB(35632);
		if (var1 == 0) {
			return 0;
		} else {
			String var2 = "";

			BufferedReader var4 = getShaderFile(var0, var1);
			if (var4 == null) {
				return 0;
			}

			String var3;
			try {
				while ((var3 = var4.readLine()) != null) {
					var2 = var2 + var3 + "\n";
					if (colorAttachments < 5 && var3.matches("uniform [ _a-zA-Z0-9]+ gaux1;.*")) {
						colorAttachments = 5;
					} else if (colorAttachments < 6 && var3.matches("uniform [ _a-zA-Z0-9]+ gaux2;.*")) {
						colorAttachments = 6;
					} else if (colorAttachments < 7 && var3.matches("uniform [ _a-zA-Z0-9]+ gaux3;.*")) {
						colorAttachments = 7;
					} else if (colorAttachments < 8 && var3.matches("uniform [ _a-zA-Z0-9]+ shadow;.*")) {
						shadowPassInterval = 1;
						colorAttachments = 8;
					} else {
						String[] var5;
						if (var3.matches("/\\* SHADOWRES:[0-9]+ \\*/.*")) {
							var5 = var3.split("(:| )", 4);
							System.out.println("Shadow map resolution: " + var5[2]);
							shadowMapWidth = shadowMapHeight = Integer.parseInt(var5[2]);
						} else if (var3.matches("/\\* SHADOWFOV:[0-9\\.]+ \\*/.*")) {
							var5 = var3.split("(:| )", 4);
							System.out.println("Shadow map field of view: " + var5[2]);
							shadowMapFOV = Float.parseFloat(var5[2]);
							shadowMapIsOrtho = false;
						} else if (var3.matches("/\\* SHADOWHPL:[0-9\\.]+ \\*/.*")) {
							var5 = var3.split("(:| )", 4);
							System.out.println("Shadow map half-plane: " + var5[2]);
							shadowMapHalfPlane = Float.parseFloat(var5[2]);
							shadowMapIsOrtho = true;
						}
					}
				}
			} catch (Exception var9) {
				System.out.println("Couldn\'t read " + var0 + "!");
				ARBShaderObjects.glDeleteObjectARB(var1);
				return 0;
			}

			ARBShaderObjects.glShaderSourceARB(var1, var2);
			ARBShaderObjects.glCompileShaderARB(var1);
			printLogInfo(var1);
			return var1;
		}
	}

	private static boolean printLogInfo(int var0) {
		if(!(enabled)) return false;
		IntBuffer var1 = BufferUtils.createIntBuffer(1);
		ARBShaderObjects.glGetObjectParameterARB(var0, 35716, var1);
		int var2 = var1.get();
		if (var2 > 1) {
			ByteBuffer var3 = BufferUtils.createByteBuffer(var2);
			var1.flip();
			ARBShaderObjects.glGetInfoLogARB(var0, var1, var3);
			byte[] var4 = new byte[var2];
			var3.get(var4);
			String var5 = new String(var4);
			System.out.println("Info log:\n" + var5);
			return false;
		} else {
			return true;
		}
	}

	private static void setupFrameBuffer() {
		if(!(enabled)) return;
		setupRenderTextures();
		if (dfb != 0) {
			EXTFramebufferObject.glDeleteFramebuffersEXT(dfb);
			EXTFramebufferObject.glDeleteRenderbuffersEXT(dfbRenderBuffers);
		}

		dfb = EXTFramebufferObject.glGenFramebuffersEXT();
		EXTFramebufferObject.glBindFramebufferEXT(36160, dfb);
		EXTFramebufferObject.glGenRenderbuffersEXT(dfbRenderBuffers);

		int var0;
		for (var0 = 0; var0 < colorAttachments; ++var0) {
			EXTFramebufferObject.glBindRenderbufferEXT(36161, dfbRenderBuffers.get(var0));
			if (var0 == 1) {
				EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34837, renderWidth, renderHeight);
			} else {
				EXTFramebufferObject.glRenderbufferStorageEXT(36161, 6408, renderWidth, renderHeight);
			}

			EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, dfbDrawBuffers.get(var0), 36161, dfbRenderBuffers.get(var0));
			EXTFramebufferObject.glFramebufferTexture2DEXT(36160, dfbDrawBuffers.get(var0), 3553, dfbTextures.get(var0), 0);
		}

		EXTFramebufferObject.glDeleteRenderbuffersEXT(dfbDepthBuffer);
		dfbDepthBuffer = EXTFramebufferObject.glGenRenderbuffersEXT();
		EXTFramebufferObject.glBindRenderbufferEXT(36161, dfbDepthBuffer);
		EXTFramebufferObject.glRenderbufferStorageEXT(36161, 6402, renderWidth, renderHeight);
		EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, dfbDepthBuffer);
		var0 = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
		if (var0 != 36053) {
			System.out.println("Failed creating framebuffer! (Status " + var0 + ")");
		}
	}

	private static void setupShadowFrameBuffer() {
		if(!(enabled)) return;
		if (shadowPassInterval > 0) {
			setupShadowRenderTexture();
			EXTFramebufferObject.glDeleteFramebuffersEXT(sfb);
			sfb = EXTFramebufferObject.glGenFramebuffersEXT();
			EXTFramebufferObject.glBindFramebufferEXT(36160, sfb);
			GL11.glDrawBuffer(0);
			GL11.glReadBuffer(0);
			EXTFramebufferObject.glDeleteRenderbuffersEXT(sfbDepthBuffer);
			sfbDepthBuffer = EXTFramebufferObject.glGenRenderbuffersEXT();
			EXTFramebufferObject.glBindRenderbufferEXT(36161, sfbDepthBuffer);
			EXTFramebufferObject.glRenderbufferStorageEXT(36161, 6402, shadowMapWidth, shadowMapHeight);
			EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, sfbDepthBuffer);
			EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, sfbDepthTexture, 0);
			int var0 = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
			if (var0 != 36053) {
				System.out.println("Failed creating shadow framebuffer! (Status " + var0 + ")");
			}
		}
	}

	private static void setupRenderTextures() {
		if(!(enabled)) return;
		GL11.glDeleteTextures(dfbTextures);
		GL11.glGenTextures(dfbTextures);

		for (int var0 = 0; var0 < colorAttachments; ++var0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, dfbTextures.get(var0));
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			ByteBuffer var1;
			if (var0 == 1) {
				var1 = ByteBuffer.allocateDirect(renderWidth * renderHeight * 4 * 4);
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, 34837, renderWidth, renderHeight, 0, GL11.GL_RGBA, GL11.GL_FLOAT, var1);
			} else {
				var1 = ByteBuffer.allocateDirect(renderWidth * renderHeight * 4);
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, renderWidth, renderHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, var1);
			}
		}
	}

	private static void setupShadowRenderTexture() {
		if(!(enabled)) return;
		if (shadowPassInterval > 0) {
			GL11.glDeleteTextures(sfbDepthTexture);
			sfbDepthTexture = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, sfbDepthTexture);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10496.0F);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10496.0F);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			ByteBuffer var0 = ByteBuffer.allocateDirect(shadowMapWidth * shadowMapHeight * 4);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, shadowMapWidth, shadowMapHeight, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, var0);
		}
	}

	public static void setMode(int shaderType) {
		enabled = shaderType > 0;
		destroy();
		if (Minecraft.theMinecraft.theWorld == null) {
			setup(shaderType);
		}
		else {
			init(shaderType);
		}
	}
	
	public static boolean isEnabled() {
		return enabled;
	}
}