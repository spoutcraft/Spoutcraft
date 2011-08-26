package net.minecraft.src;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GameSettings;
import net.minecraft.src.IBlockAccess;

import org.getspout.spout.client.SpoutClient;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.pclewis.mcpatcher.mod.TileSize;

public class Config {

	private static GameSettings gameSettings = null;
	private static Minecraft minecraft = null;
	private static float[] lightLevels = null;
	private static int iconWidthTerrain = 16;
	private static int iconWidthItems = 16;
	private static Map foundClassesMap = new HashMap();
	private static boolean fontRendererUpdated = false;
	private static File logFile = null;
	public static final Boolean DEF_FOG_FANCY = Boolean.valueOf(true);
	public static final Float DEF_FOG_START = Float.valueOf(0.2F);
	public static final Boolean DEF_OPTIMIZE_RENDER_DISTANCE = Boolean.valueOf(false);
	public static final Boolean DEF_OCCLUSION_ENABLED = Boolean.valueOf(false);
	public static final Integer DEF_MIPMAP_LEVEL = Integer.valueOf(0);
	public static final Integer DEF_MIPMAP_TYPE = Integer.valueOf(9984 /*GL_NEAREST_MIPMAP_NEAREST*/);
	public static final Float DEF_ALPHA_FUNC_LEVEL = Float.valueOf(0.1F);
	public static final Boolean DEF_LOAD_CHUNKS_FAR = Boolean.valueOf(false);
	public static final Integer DEF_PRELOADED_CHUNKS = Integer.valueOf(0);
	public static final Integer DEF_CHUNKS_LIMIT = Integer.valueOf(25);
	public static final Integer DEF_UPDATES_PER_FRAME = Integer.valueOf(3);
	public static final Boolean DEF_DYNAMIC_UPDATES = Boolean.valueOf(false);


	private static String getVersion() {
		return "OptiFine_1.7.3_HD_F";
	}

	private static void checkOpenGlCaps() {
		log("");
		log(getVersion());
		log("" + new Date());
		log("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
		log("Java: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
		log("VM: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
		log("LWJGL: " + Sys.getVersion());
		log("OpenGL: " + GL11.glGetString(7937 /*GL_RENDERER*/) + " version " + GL11.glGetString(7938 /*GL_VERSION*/) + ", " + GL11.glGetString(7936 /*GL_VENDOR*/));
		int var0 = getOpenGlVersion();
		String var1 = "" + var0 / 10 + "." + var0 % 10;
		log("OpenGL Version: " + var1);
		if(!GLContext.getCapabilities().OpenGL12) {
			log("OpenGL Mipmap levels: Not available (GL12.GL_TEXTURE_MAX_LEVEL)");
		}

		if(!GLContext.getCapabilities().GL_NV_fog_distance) {
			log("OpenGL Fancy fog: Not available (GL_NV_fog_distance)");
		}

		if(!GLContext.getCapabilities().GL_ARB_occlusion_query) {
			log("OpenGL Occlussion culling: Not available (GL_ARB_occlusion_query)");
		}

	}

	public static boolean isFancyFogAvailable() {
		return GLContext.getCapabilities().GL_NV_fog_distance;
	}

	public static boolean isOcclusionAvailable() {
		return GLContext.getCapabilities().GL_ARB_occlusion_query;
	}

	private static int getOpenGlVersion() {
		return !GLContext.getCapabilities().OpenGL11?10:(!GLContext.getCapabilities().OpenGL12?11:(!GLContext.getCapabilities().OpenGL13?12:(!GLContext.getCapabilities().OpenGL14?13:(!GLContext.getCapabilities().OpenGL15?14:(!GLContext.getCapabilities().OpenGL20?15:(!GLContext.getCapabilities().OpenGL21?20:(!GLContext.getCapabilities().OpenGL30?21:(!GLContext.getCapabilities().OpenGL31?30:(!GLContext.getCapabilities().OpenGL32?31:(!GLContext.getCapabilities().OpenGL33?32:(!GLContext.getCapabilities().OpenGL40?33:40)))))))))));
	}

	public static void setGameSettings(GameSettings var0) {
		if(gameSettings == null) {
			checkOpenGlCaps();
		}

		gameSettings = var0;
	}

	public static boolean isUseMipmaps() {
		int var0 = getMipmapLevel();
		return var0 > 0; 
	}
	
	public static boolean canUseMipmaps() {
		return TileSize.int_size <= 16;
	}

	public static int getMipmapLevel() {
		return gameSettings == null || !canUseMipmaps() ?DEF_MIPMAP_LEVEL.intValue():gameSettings.ofMipmapLevel; //Temporarily removed
	}

	public static int getMipmapType() {
		return gameSettings == null?DEF_MIPMAP_TYPE.intValue():(gameSettings.ofMipmapLinear?9986 /*GL_NEAREST_MIPMAP_LINEAR*/:9984 /*GL_NEAREST_MIPMAP_NEAREST*/); //Temporarily removed
	}

	public static boolean isUseAlphaFunc() {
		float var0 = getAlphaFuncLevel();
		return var0 > DEF_ALPHA_FUNC_LEVEL.floatValue() + 1.0E-5F;
	}

	public static float getAlphaFuncLevel() {
		return DEF_ALPHA_FUNC_LEVEL.floatValue();
	}

	public static boolean isFogFancy() {
		return !GLContext.getCapabilities().GL_NV_fog_distance?false:(gameSettings == null?false:gameSettings.ofFogFancy);
	}

	public static float getFogStart() {
		return gameSettings == null?DEF_FOG_START.floatValue():gameSettings.ofFogStart;
	}

	public static boolean isOcclusionEnabled() {
		return gameSettings == null?DEF_OCCLUSION_ENABLED.booleanValue():gameSettings.advancedOpengl;
	}

	public static boolean isOcclusionFancy() {
		return !isOcclusionEnabled()?false:(gameSettings == null?false:gameSettings.ofOcclusionFancy);
	}

	public static boolean isLoadChunksFar() {
		return gameSettings == null?DEF_LOAD_CHUNKS_FAR.booleanValue():gameSettings.ofLoadFar;
	}

	public static int getPreloadedChunks() {
		return gameSettings == null?DEF_PRELOADED_CHUNKS.intValue():gameSettings.ofPreloadedChunks;
	}

	public static void dbg(String var0) {
		System.out.println(var0);
	}

	public static void log(String var0) {
		dbg(var0);

		try {
			if(logFile == null) {
				logFile = new File(Minecraft.getMinecraftDir(), "optifog.log");
				logFile.delete();
				logFile.createNewFile();
			}

			FileOutputStream var1 = new FileOutputStream(logFile, true);
			OutputStreamWriter var2 = new OutputStreamWriter(var1, "ASCII");

			try {
				var2.write(var0);
				var2.write("\n");
				var2.flush();
			} finally {
				var2.close();
			}
		} catch (IOException var7) {
			var7.printStackTrace();
		}

	}

	public static int getUpdatesPerFrame() {
		return gameSettings != null?gameSettings.ofChunkUpdates:1;
	}

	public static boolean isDynamicUpdates() {
		return gameSettings != null?gameSettings.ofChunkUpdatesDynamic:true;
	}

	public static boolean isRainFancy() {
		return gameSettings.ofRain == 0?gameSettings.fancyGraphics:gameSettings.ofRain == 2;
	}

	public static boolean isWaterFancy() {
		return gameSettings.ofWater == 0?gameSettings.fancyGraphics:gameSettings.ofWater == 2;
	}

	public static boolean isRainOff() {
		return gameSettings.ofRain == 3 && SpoutClient.getInstance().isCheatMode();
	}

	public static boolean isCloudsFancy() {
		return gameSettings.ofClouds == 0?gameSettings.fancyGraphics:gameSettings.ofClouds == 2;
	}

	public static boolean isCloudsOff() {
		return gameSettings.ofClouds == 3 && SpoutClient.getInstance().isCheatMode();
	}

	public static boolean isTreesFancy() {
		return gameSettings.ofTrees == 0?gameSettings.fancyGraphics:gameSettings.ofTrees == 2;
	}

	public static boolean isGrassFancy() {
		return gameSettings.ofGrass == 0?gameSettings.fancyGraphics:gameSettings.ofGrass == 2;
	}

	public static int limit(int var0, int var1, int var2) {
		return var0 < var1?var1:(var0 > var2?var2:var0);
	}

	public static float limit(float var0, float var1, float var2) {
		return var0 < var1?var1:(var0 > var2?var2:var0);
	}

	public static boolean isAnimatedWater() {
		return gameSettings != null?gameSettings.ofAnimatedWater != 2:true;
	}

	public static boolean isGeneratedWater() {
		return gameSettings != null?gameSettings.ofAnimatedWater == 1:true;
	}

	public static boolean isAnimatedPortal() {
		return gameSettings != null?gameSettings.ofAnimatedPortal:true;
	}

	public static boolean isAnimatedLava() {
		return gameSettings != null?gameSettings.ofAnimatedLava != 2:true;
	}

	public static boolean isGeneratedLava() {
		return gameSettings != null?gameSettings.ofAnimatedLava == 1:true;
	}

	public static boolean isAnimatedFire() {
		return gameSettings != null?gameSettings.ofAnimatedFire:true;
	}

	public static boolean isAnimatedRedstone() {
		return gameSettings != null?gameSettings.ofAnimatedRedstone:true;
	}

	public static boolean isAnimatedExplosion() {
		return gameSettings != null?gameSettings.ofAnimatedExplosion:true;
	}

	public static boolean isAnimatedFlame() {
		return gameSettings != null?gameSettings.ofAnimatedFlame:true;
	}

	public static boolean isAnimatedSmoke() {
		return gameSettings != null?gameSettings.ofAnimatedSmoke:true;
	}

	public static float getAmbientOcclusionLevel() {
		return gameSettings != null?gameSettings.ofAoLevel:0.0F;
	}

	public static float fixAoLight(float var0, float var1) {
		if(lightLevels == null) {
			return var0;
		} else {
			float var2 = lightLevels[0];
			float var3 = lightLevels[1];
			if(var0 > var2) {
				return var0;
			} else if(var1 <= var3) {
				return var0;
			} else {
				float var4 = 1.0F - getAmbientOcclusionLevel();
				return var0 + (var1 - var0) * var4;
			}
		}
	}

	public static void setLightLevels(float[] var0) {
		lightLevels = var0;
	}

	public static boolean callBoolean(String var0, String var1, Object[] var2) {
		try {
			Class var3 = getClass(var0);
			if(var3 == null) {
				return false;
			} else {
				Method var4 = getMethod(var3, var1, var2);
				if(var4 == null) {
					return false;
				} else {
					Boolean var5 = (Boolean)var4.invoke((Object)null, var2);
					return var5.booleanValue();
				}
			}
		} catch (Throwable var6) {
			var6.printStackTrace();
			return false;
		}
	}

	public static void callVoid(String var0, String var1, Object[] var2) {
		try {
			Class var3 = getClass(var0);
			if(var3 == null) {
				return;
			}

			Method var4 = getMethod(var3, var1, var2);
			if(var4 == null) {
				return;
			}

			var4.invoke((Object)null, var2);
		} catch (Throwable var5) {
			var5.printStackTrace();
		}

	}

	public static void callVoid(Object var0, String var1, Object[] var2) {
		try {
			if(var0 == null) {
				return;
			}

			Class var3 = var0.getClass();
			if(var3 == null) {
				return;
			}

			Method var4 = getMethod(var3, var1, var2);
			if(var4 == null) {
				return;
			}

			var4.invoke(var0, var2);
		} catch (Throwable var5) {
			var5.printStackTrace();
		}

	}

	public static Object getFieldValue(String var0, String var1) {
		try {
			Class var2 = getClass(var0);
			if(var2 == null) {
				return null;
			} else {
				Field var3 = var2.getDeclaredField(var1);
				if(var3 == null) {
					return null;
				} else {
					Object var4 = var3.get((Object)null);
					return var4;
				}
			}
		} catch (Throwable var5) {
			var5.printStackTrace();
			return null;
		}
	}

	public static Object getFieldValue(Object var0, String var1) {
		try {
			if(var0 == null) {
				return null;
			} else {
				Class var2 = var0.getClass();
				if(var2 == null) {
					return null;
				} else {
					Field var3 = var2.getField(var1);
					if(var3 == null) {
						return null;
					} else {
						Object var4 = var3.get(var0);
						return var4;
					}
				}
			}
		} catch (Throwable var5) {
			var5.printStackTrace();
			return null;
		}
	}

	private static Method getMethod(Class var0, String var1, Object[] var2) {
		Method[] var3 = var0.getMethods();

		for(int var4 = 0; var4 < var3.length; ++var4) {
			Method var5 = var3[var4];
			if(var5.getName().equals(var1) && var5.getParameterTypes().length == var2.length) {
				return var5;
			}
		}

		dbg("No method found for: " + var0.getName() + "." + var1 + "(" + arrayToString(var2) + ")");
		return null;
	}

	public static String arrayToString(Object[] var0) {
		StringBuffer var1 = new StringBuffer(var0.length * 5);

		for(int var2 = 0; var2 < var0.length; ++var2) {
			Object var3 = var0[var2];
			if(var2 > 0) {
				var1.append(", ");
			}

			var1.append(String.valueOf(var3));
		}

		return var1.toString();
	}

	public static boolean hasModLoader() {
		Class var0 = getClass("ModLoader");
		return var0 != null;
	}

	private static Class getClass(String var0) {
		Class var1 = (Class)foundClassesMap.get(var0);
		if(var1 != null) {
			return var1;
		} else if(foundClassesMap.containsKey(var0)) {
			return null;
		} else {
			try {
				var1 = Class.forName(var0);
			} catch (ClassNotFoundException var3) {
				log("Class not found: " + var0);
			} catch (Throwable var4) {
				var4.printStackTrace();
			}

			foundClassesMap.put(var0, var1);
			return var1;
		}
	}

	public static void setMinecraft(Minecraft var0) {
		minecraft = var0;
	}

	public static Minecraft getMinecraft() {
		return minecraft;
	}

	public static int getIconWidthTerrain() {
		return iconWidthTerrain;
	}

	public static int getIconWidthItems() {
		return iconWidthItems;
	}

	public static void setIconWidthItems(int var0) {
		iconWidthItems = var0;
	}

	public static void setIconWidthTerrain(int var0) {
		iconWidthTerrain = var0;
	}

	public static int getMaxDynamicTileWidth() {
		return 64;
	}

	public static int getSideGrassTexture(IBlockAccess var0, int var1, int var2, int var3, int var4) {
		if(!isBetterGrass()) {
			return 3;
		} else {
			if(isBetterGrassFancy()) {
				--var2;
				switch(var4) {
				case 2:
					--var3;
					break;
				case 3:
					++var3;
					break;
				case 4:
					--var1;
					break;
				case 5:
					++var1;
				}

				int var5 = var0.getBlockId(var1, var2, var3);
				if(var5 != 2) {
					return 3;
				}
			}

			return 0;
		}
	}

	public static int getSideSnowGrassTexture(IBlockAccess var0, int var1, int var2, int var3, int var4) {
		if(!isBetterGrass()) {
			return 68;
		} else {
			if(isBetterGrassFancy()) {
				switch(var4) {
				case 2:
					--var3;
					break;
				case 3:
					++var3;
					break;
				case 4:
					--var1;
					break;
				case 5:
					++var1;
				}

				int var5 = var0.getBlockId(var1, var2, var3);
				if(var5 != 78 && var5 != 80) {
					return 68;
				}
			}

			return 66;
		}
	}

	public static boolean isBetterGrass() {
		return gameSettings == null?false:gameSettings.ofBetterGrass != 3;
	}

	public static boolean isBetterGrassFancy() {
		return gameSettings == null?false:gameSettings.ofBetterGrass == 2;
	}

	public static boolean isFontRendererUpdated() {
		return fontRendererUpdated;
	}

	public static void setFontRendererUpdated(boolean var0) {
		fontRendererUpdated = var0;
	}

	public static boolean isWeatherEnabled() {
		return gameSettings == null?true:gameSettings.ofWeather || !SpoutClient.getInstance().isCheatMode();
	}

	public static boolean isSkyEnabled() {
		return gameSettings == null?true:gameSettings.ofSky || !SpoutClient.getInstance().isCheatMode(); 
	}

	public static boolean isStarsEnabled() {
		return gameSettings == null?true:gameSettings.ofStars || !SpoutClient.getInstance().isCheatMode();
	}

	public static boolean isFarView() {
		return gameSettings == null?false:gameSettings.ofFarView;
	}

	public static void sleep(long var0) {
		try {
			Thread.currentThread();
			Thread.sleep(var0);
		} catch (InterruptedException var3) {
			var3.printStackTrace();
		}

	}

	public static void sleep(long var0, int var2) {
		try {
			Thread.currentThread();
			Thread.sleep(var0, var2);
		} catch (InterruptedException var4) {
			var4.printStackTrace();
		}

	}

	public static boolean isTimeDayOnly() {
		return gameSettings == null?false:gameSettings.ofTime == 1 && SpoutClient.getInstance().isCheatMode();
	}

	public static boolean isTimeNightOnly() {
		return gameSettings == null?false:gameSettings.ofTime == 2 && SpoutClient.getInstance().isCheatMode();
	}

	public static boolean isClearWater() {
		return gameSettings == null?false:gameSettings.ofClearWater && SpoutClient.getInstance().isCheatMode();
	}

}
