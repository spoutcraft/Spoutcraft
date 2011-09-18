package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EnumOptions;
import net.minecraft.src.EnumOptionsMappingHelper;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StringTranslate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display; //Spout

public class GameSettings {

	private static final String[] RENDER_DISTANCES = new String[]{"options.renderDistance.far", "options.renderDistance.normal", "options.renderDistance.short", "options.renderDistance.tiny"};
	private static final String[] DIFFICULTIES = new String[]{"options.difficulty.peaceful", "options.difficulty.easy", "options.difficulty.normal", "options.difficulty.hard"};
	private static final String[] GUISCALES = new String[]{"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
	private static final String[] LIMIT_FRAMERATES = new String[]{"performance.max", "performance.balanced", "performance.powersaver"};
	public float musicVolume = 1.0F;
	public float soundVolume = 1.0F;
	public float mouseSensitivity = 0.5F;
	public boolean invertMouse = false;
	public int renderDistance = 0;
	public boolean viewBobbing = true;
	public boolean anaglyph = false;
	public boolean advancedOpengl = false;
	public int limitFramerate = 1;
	public boolean fancyGraphics = true;
	public boolean ambientOcclusion = true;
//Spout Start
	public boolean ofFogFancy = false;
	public float ofFogStart = 0.8F;
	public int ofMipmapLevel = 0;
	public boolean ofMipmapLinear = false;
	public boolean ofLoadFar = false;
	public int ofPreloadedChunks = 0;
	public boolean ofOcclusionFancy = false;
	public boolean ofSmoothFps = false;
	public float ofBrightness = 0.0F;
	public float ofAoLevel = 0.0F;
	public int ofClouds = 0;
	public float ofCloudsHeight = 0.0F;
	public int ofTrees = 0;
	public int ofGrass = 0;
	public int ofRain = 0;
	public int ofWater = 0;
	public int ofBetterGrass = 3;
	public int ofAutoSaveTicks = 4000;
	public byte fastDebugMode = 0;
	public boolean ofWeather = true;
	public boolean ofSky = true;
	public boolean ofStars = true;
	public int ofChunkUpdates = 1;
	public boolean ofChunkUpdatesDynamic = true;
	public boolean ofFarView = false;
	public int ofTime = 0;
	public boolean ofClearWater = false;
	public int ofAnimatedWater = 0;
	public int ofAnimatedLava = 0;
	public boolean ofAnimatedFire = true;
	public boolean ofAnimatedPortal = true;
	public boolean ofAnimatedRedstone = true;
	public boolean ofAnimatedExplosion = true;
	public boolean ofAnimatedFlame = true;
	public boolean ofAnimatedSmoke = true;
	public static final int DEFAULT = 0;
	public static final int FAST = 1;
	public static final int FANCY = 2;
	public static final int OFF = 3;
	public static final int ANIM_ON = 0;
	public static final int ANIM_GENERATED = 1;
	public static final int ANIM_OFF = 2;
	public String skin = "Default";
//Spout End
	public KeyBinding keyBindForward = new KeyBinding("key.forward", 17);
	public KeyBinding keyBindLeft = new KeyBinding("key.left", 30);
	public KeyBinding keyBindBack = new KeyBinding("key.back", 31);
	public KeyBinding keyBindRight = new KeyBinding("key.right", 32);
	public KeyBinding keyBindJump = new KeyBinding("key.jump", 57);
	public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18);
	public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16);
	public KeyBinding keyBindChat = new KeyBinding("key.chat", 20);
	public KeyBinding keyBindToggleFog = new KeyBinding("key.fog", 33); //Spout restored Fog key
	public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42);
	public KeyBinding field_35382_v = new KeyBinding("key.attack", -100);
	public KeyBinding field_35381_w = new KeyBinding("key.use", -99);
	public KeyBinding field_35384_x = new KeyBinding("key.playerlist", 15);
	public KeyBinding field_35383_y = new KeyBinding("key.pickItem", -98);
	public KeyBinding[] keyBindings;
	protected Minecraft mc;
	private File optionsFile;
	public int difficulty;
	public boolean hideGUI;
	public boolean thirdPersonView;
	public boolean showDebugInfo;
	public String lastServer;
	public boolean field_22275_C;
	public boolean smoothCamera;
	public boolean debugCamEnable;
	public float field_22272_F;
	public float field_22271_G;
	public float field_35379_L;
	public float field_35380_M;
	public int guiScale;


	public GameSettings(Minecraft var1, File var2) {
		this.keyBindings = new KeyBinding[]{this.field_35382_v, this.field_35381_w, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindToggleFog, this.field_35384_x, this.field_35383_y}; //Spout restored Fog key
		this.difficulty = 2;
		this.hideGUI = false;
		this.thirdPersonView = false;
		this.showDebugInfo = false;
		this.lastServer = "";
		this.field_22275_C = false;
		this.smoothCamera = false;
		this.debugCamEnable = false;
		this.field_22272_F = 1.0F;
		this.field_22271_G = 1.0F;
		this.field_35379_L = 0.0F;
		this.field_35380_M = 0.0F;
		this.guiScale = 0;
		this.mc = var1;
		this.optionsFile = new File(var2, "options.txt");
		this.loadOptions();
	}

	public GameSettings() {
		this.keyBindings = new KeyBinding[]{this.field_35382_v, this.field_35381_w, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.field_35384_x, this.field_35383_y};
		this.difficulty = 2;
		this.hideGUI = false;
		this.thirdPersonView = false;
		this.showDebugInfo = false;
		this.lastServer = "";
		this.field_22275_C = false;
		this.smoothCamera = false;
		this.debugCamEnable = false;
		this.field_22272_F = 1.0F;
		this.field_22271_G = 1.0F;
		this.field_35379_L = 0.0F;
		this.field_35380_M = 0.0F;
		this.guiScale = 0;
	}

	public String getKeyBindingDescription(int var1) {
		StringTranslate var2 = StringTranslate.getInstance();
		return var2.translateKey(this.keyBindings[var1].keyDescription);
	}

	public String getOptionDisplayString(int var1) {
		int var2 = this.keyBindings[var1].keyCode;
		return var2 < 0?StatCollector.translateToLocalFormatted("key.mouseButton", new Object[]{Integer.valueOf(var2 + 101)}):Keyboard.getKeyName(var2);
	}

	public void setKeyBinding(int var1, int var2) {
		this.keyBindings[var1].keyCode = var2;
		this.saveOptions();
	}

	public void setOptionFloatValue(EnumOptions var1, float var2) {
		if(var1 == EnumOptions.MUSIC) {
			this.musicVolume = var2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(var1 == EnumOptions.SOUND) {
			this.soundVolume = var2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(var1 == EnumOptions.SENSITIVITY) {
			this.mouseSensitivity = var2;
		}
		if(var1 == EnumOptions.FOV) {
			this.field_35379_L = var2;
		}

		if(var1 == EnumOptions.GAMMA) {
			this.field_35380_M = var2;
		}
//Spout Start
		if(var1 == EnumOptions.BRIGHTNESS) {
			this.ofBrightness = var2;
			this.updateWorldLightLevels();
		}

		if(var1 == EnumOptions.CLOUD_HEIGHT) {
			this.ofCloudsHeight = var2;
		}

		if(var1 == EnumOptions.AO_LEVEL) {
			this.ofAoLevel = var2;
			this.ambientOcclusion = this.ofAoLevel > 0.0F;
			this.mc.renderGlobal.loadRenderers();
		}

	}

	private void updateWorldLightLevels() {
		if(this.mc.entityRenderer != null) {
			this.mc.entityRenderer.updateWorldLightLevels();
		}

		if(this.mc.renderGlobal != null) {
			this.mc.renderGlobal.markAllRenderersDirty();
		}

	}

	private void updateWaterOpacity() {
		byte var1 = 3;
		if(this.ofClearWater) {
			var1 = 1;
		}

		Block.waterStill.setLightOpacity(var1);
		Block.waterMoving.setLightOpacity(var1);
		if(this.mc.theWorld != null) {
			/*IChunkProvider var2 = this.mc.theWorld.chunkProvider;
			if(var2 != null) {
				for(int var3 = -512; var3 < 512; ++var3) {
					for(int var4 = -512; var4 < 512; ++var4) {
						if(var2.chunkExists(var3, var4)) {
							Chunk var5 = var2.provideChunk(var3, var4);
							if(var5 != null) {
								byte[] var6 = var5.skylightMap.data;

								for(int var7 = 0; var7 < var6.length; ++var7) {
									var6[var7] = 0;
								}

								var5.func_1024_c();
							}
						}
					}
				}
			}*/
			this.mc.renderGlobal.markAllRenderersDirty();
		}
	}
//Spout End
	public void setOptionValue(EnumOptions var1, int var2) {
		if(var1 == EnumOptions.INVERT_MOUSE) {
			this.invertMouse = !this.invertMouse;
		}

		if(var1 == EnumOptions.RENDER_DISTANCE) {
			this.renderDistance = this.renderDistance + var2 & 3;
		}

		if(var1 == EnumOptions.GUI_SCALE) {
			this.guiScale = this.guiScale + var2 & 3;
		}

		if(var1 == EnumOptions.VIEW_BOBBING) {
			this.viewBobbing = !this.viewBobbing;
		}

		if(var1 == EnumOptions.ADVANCED_OPENGL) {
//Spout Start
			if(!Config.isOcclusionAvailable()) {
				this.ofOcclusionFancy = false;
				this.advancedOpengl = false;
			} else if(!this.advancedOpengl) {
				this.advancedOpengl = true;
				this.ofOcclusionFancy = false;
			} else if(!this.ofOcclusionFancy) {
				this.ofOcclusionFancy = true;
			} else {
				this.ofOcclusionFancy = false;
				this.advancedOpengl = false;
			}

			this.mc.renderGlobal.setAllRenderesVisible();
//Spout End
		}

		if(var1 == EnumOptions.ANAGLYPH) {
			this.anaglyph = !this.anaglyph;
			this.mc.renderEngine.refreshTextures();
		}

		if(var1 == EnumOptions.FRAMERATE_LIMIT) {
//Spout Start
			this.limitFramerate = (this.limitFramerate + var2) % 4;
			Display.setVSyncEnabled(this.limitFramerate == 3);
//Spout End
		}

		if(var1 == EnumOptions.DIFFICULTY) {
			this.difficulty = this.difficulty + var2 & 3;
		}

		if(var1 == EnumOptions.GRAPHICS) {
			this.fancyGraphics = !this.fancyGraphics;
			if (this.mc.theWorld != null) {
				this.mc.renderGlobal.markAllRenderersDirty();
			}
		}

		if(var1 == EnumOptions.AMBIENT_OCCLUSION) {
			this.ambientOcclusion = !this.ambientOcclusion;
			if (this.mc.theWorld != null) {
				this.mc.renderGlobal.markAllRenderersDirty();
			}
		}
//Spout Start
		if(var1 == EnumOptions.FOG_FANCY) {
			if(!Config.isFancyFogAvailable()) {
				this.ofFogFancy = false;
			} else {
				this.ofFogFancy = !this.ofFogFancy;
			}
		}

		if(var1 == EnumOptions.FOG_START) {
			this.ofFogStart += 0.2F;
			if(this.ofFogStart > 0.81F) {
				this.ofFogStart = 0.2F;
			}
		}
		/*
		if(var1 == EnumOptions.MIPMAP_LEVEL) {
			++this.ofMipmapLevel;
			if(this.ofMipmapLevel > 4) {
				this.ofMipmapLevel = 0;
			}

			this.mc.renderEngine.setTileSize(this.mc);
			this.mc.renderEngine.refreshTextures();
		}

		if(var1 == EnumOptions.MIPMAP_TYPE) {
			this.ofMipmapLinear = !this.ofMipmapLinear;
			this.mc.renderEngine.setTileSize(this.mc);
			this.mc.renderEngine.refreshTextures();
		}
		*/
		if(var1 == EnumOptions.LOAD_FAR) {
			this.ofLoadFar = !this.ofLoadFar;
			if (this.mc.theWorld != null && !this.ofLoadFar) {
				this.mc.renderGlobal.markAllRenderersDirty();
			}
		}

		if(var1 == EnumOptions.PRELOADED_CHUNKS) {
			this.ofPreloadedChunks += 2;
			if(this.ofPreloadedChunks > 8) {
				this.ofPreloadedChunks = 0;
			}
			if (this.mc.theWorld != null) {
				this.mc.renderGlobal.markAllRenderersDirty();
			}
		}

		if(var1 == EnumOptions.SMOOTH_FPS) {
			this.ofSmoothFps = !this.ofSmoothFps;
		}

		if(var1 == EnumOptions.CLOUDS) {
			++this.ofClouds;
			if(this.ofClouds > 3) {
				this.ofClouds = 0;
			}
		}

		if(var1 == EnumOptions.TREES) {
			++this.ofTrees;
			if(this.ofTrees > 2) {
				this.ofTrees = 0;
			}
			if (this.mc.theWorld != null) {
				this.mc.renderGlobal.markAllRenderersDirty();
			}
		}

		if(var1 == EnumOptions.GRASS) {
			++this.ofGrass;
			if(this.ofGrass > 2) {
				this.ofGrass = 0;
			}

			RenderBlocks.fancyGrass = Config.isGrassFancy();
			if (this.mc.theWorld != null) {
				this.mc.renderGlobal.markAllRenderersDirty();
			}
		}

		if(var1 == EnumOptions.RAIN) {
			++this.ofRain;
			if(this.ofRain > 3) {
				this.ofRain = 0;
			}
		}

		if(var1 == EnumOptions.WATER) {
			++this.ofWater;
			if(this.ofWater > 2) {
				this.ofWater = 0;
			}
		}

		if(var1 == EnumOptions.ANIMATED_WATER) {
			++this.ofAnimatedWater;
			if(this.ofAnimatedWater > 2) {
				this.ofAnimatedWater = 0;
			}

			this.mc.renderEngine.refreshTextures();
		}

		if(var1 == EnumOptions.ANIMATED_LAVA) {
			++this.ofAnimatedLava;
			if(this.ofAnimatedLava > 2) {
				this.ofAnimatedLava = 0;
			}

			this.mc.renderEngine.refreshTextures();
		}

		if(var1 == EnumOptions.ANIMATED_FIRE) {
			this.ofAnimatedFire = !this.ofAnimatedFire;
			this.mc.renderEngine.refreshTextures();
		}

		if(var1 == EnumOptions.ANIMATED_PORTAL) {
			this.ofAnimatedPortal = !this.ofAnimatedPortal;
			this.mc.renderEngine.refreshTextures();
		}

		if(var1 == EnumOptions.ANIMATED_REDSTONE) {
			this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
		}

		if(var1 == EnumOptions.ANIMATED_EXPLOSION) {
			this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
		}

		if(var1 == EnumOptions.ANIMATED_FLAME) {
			this.ofAnimatedFlame = !this.ofAnimatedFlame;
		}

		if(var1 == EnumOptions.ANIMATED_SMOKE) {
			this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
		}

		if(var1 == EnumOptions.FAST_DEBUG_INFO) {
			fastDebugMode++;
			if (fastDebugMode > 2) {
				fastDebugMode = 0;
			}
		}

		if(var1 == EnumOptions.AUTOSAVE_TICKS) {
			this.ofAutoSaveTicks *= 10;
			if(this.ofAutoSaveTicks > '\u9c40') {
				this.ofAutoSaveTicks = 40;
			}
		}

		if(var1 == EnumOptions.BETTER_GRASS) {
			++this.ofBetterGrass;
			if(this.ofBetterGrass > 3) {
				this.ofBetterGrass = 1;
			}
			if (this.mc.theWorld != null) {
				this.mc.renderGlobal.markAllRenderersDirty();
			}
		}

		if(var1 == EnumOptions.WEATHER) {
			this.ofWeather = !this.ofWeather;
		}

		if(var1 == EnumOptions.SKY) {
			this.ofSky = !this.ofSky;
		}

		if(var1 == EnumOptions.STARS) {
			this.ofStars = !this.ofStars;
		}

		if(var1 == EnumOptions.CHUNK_UPDATES) {
			++this.ofChunkUpdates;
			if(this.ofChunkUpdates > 5) {
				this.ofChunkUpdates = 1;
			}
		}

		if(var1 == EnumOptions.CHUNK_UPDATES_DYNAMIC) {
			this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
		}

		if(var1 == EnumOptions.FAR_VIEW) {
			this.ofFarView = !this.ofFarView;
			if (this.mc.theWorld != null) {
				this.mc.renderGlobal.markAllRenderersDirty();
			}
		}

		if(var1 == EnumOptions.TIME) {
			++this.ofTime;
			if(this.ofTime > 2) {
				this.ofTime = 0;
			}
		}

		if(var1 == EnumOptions.CLEAR_WATER) {
			this.ofClearWater = !this.ofClearWater;
			this.updateWaterOpacity();
		}
//Spout End
		this.saveOptions();
	}

	public float getOptionFloatValue(EnumOptions var1) {
		return var1 == EnumOptions.FOV?this.field_35379_L:(var1 == EnumOptions.GAMMA?this.field_35380_M:(var1 == EnumOptions.MUSIC?this.musicVolume:(var1 == EnumOptions.SOUND?this.soundVolume:(var1 == EnumOptions.SENSITIVITY?this.mouseSensitivity:(var1 == EnumOptions.BRIGHTNESS?this.ofBrightness:(var1 == EnumOptions.CLOUD_HEIGHT?this.ofCloudsHeight:(var1 == EnumOptions.AO_LEVEL?this.ofAoLevel:0.0F))))))); //Spout
	}

	public boolean getOptionOrdinalValue(EnumOptions var1) {
		switch(EnumOptionsMappingHelper.enumOptionsMappingHelperArray[var1.ordinal()]) {
		case 1:
			return this.invertMouse;
		case 2:
			return this.viewBobbing;
		case 3:
			return this.anaglyph;
		case 4:
			return this.advancedOpengl;
		case 5:
			return this.ambientOcclusion;
		default:
			return false;
		}
	}

	public String getKeyBinding(EnumOptions var1) {
		StringTranslate var2 = StringTranslate.getInstance();
//Spout Start
		String var3 = var2.translateKey(var1.getEnumString());
		if(var3 == null) {
			var3 = var1.getEnumString();
		}

		String var4 = var3 + ": ";
		if(var1.getEnumFloat()) {
			float var6 = this.getOptionFloatValue(var1);
			return var1 == EnumOptions.SENSITIVITY?(var6 == 0.0F?var4 + var2.translateKey("options.sensitivity.min"):(var6 == 1.0F?var4 + var2.translateKey("options.sensitivity.max"):var4 + (int)(var6 * 200.0F) + "%")):(var6 == 0.0F?var4 + var2.translateKey("options.off"):var4 + (int)(var6 * 100.0F) + "%");
		} else if(var1 == EnumOptions.ADVANCED_OPENGL) {
			return !this.advancedOpengl?var4 + "OFF":(this.ofOcclusionFancy?var4 + "Fancy":var4 + "Fast");
		} else if(var1.getEnumBoolean()) {
			boolean var5 = this.getOptionOrdinalValue(var1);
			return var5?var4 + var2.translateKey("options.on"):var4 + var2.translateKey("options.off");
		} else if(var1 == EnumOptions.RENDER_DISTANCE) {
			return var4 + var2.translateKey(RENDER_DISTANCES[this.renderDistance]);
		} else if(var1 == EnumOptions.DIFFICULTY) {
			return var4 + var2.translateKey(DIFFICULTIES[this.difficulty]);
		} else if(var1 == EnumOptions.GUI_SCALE) {
			return var4 + var2.translateKey(GUISCALES[this.guiScale]);
		} else if(var1 == EnumOptions.FRAMERATE_LIMIT) {
			return this.limitFramerate == 3?var4 + "VSync":var4 + StatCollector.translateToLocal(LIMIT_FRAMERATES[this.limitFramerate]);
		} else if(var1 == EnumOptions.FOG_FANCY) {
			return this.ofFogFancy?var4 + "Fancy":var4 + "Fast";
		} else if(var1 == EnumOptions.FOG_START) {
			return var4 + this.ofFogStart;
		}/* else if(var1 == EnumOptions.MIPMAP_LEVEL) {
			return var4 + this.ofMipmapLevel;
		} else if(var1 == EnumOptions.MIPMAP_TYPE) {
			return this.ofMipmapLinear?var4 + "Linear":var4 + "Nearest";
		}*/ else if(var1 == EnumOptions.LOAD_FAR) {
			return this.ofLoadFar?var4 + "ON":var4 + "OFF";
		} else if(var1 == EnumOptions.PRELOADED_CHUNKS) {
			return this.ofPreloadedChunks == 0?var4 + "OFF":var4 + this.ofPreloadedChunks;
		} else if(var1 == EnumOptions.SMOOTH_FPS) {
			return this.ofSmoothFps?var4 + "ON":var4 + "OFF";
		} else if(var1 == EnumOptions.CLOUDS) {
			switch(this.ofClouds) {
			case 1:
				return var4 + "Fast";
			case 2:
				return var4 + "Fancy";
			case 3:
				return var4 + "OFF";
			default:
				return var4 + "Default";
			}
		} else if(var1 == EnumOptions.TREES) {
			switch(this.ofTrees) {
			case 1:
				return var4 + "Fast";
			case 2:
				return var4 + "Fancy";
			default:
				return var4 + "Default";
			}
		} else if(var1 == EnumOptions.GRASS) {
			switch(this.ofGrass) {
			case 1:
				return var4 + "Fast";
			case 2:
				return var4 + "Fancy";
			default:
				return var4 + "Default";
			}
		} else if(var1 == EnumOptions.RAIN) {
			switch(this.ofRain) {
			case 1:
				return var4 + "Fast";
			case 2:
				return var4 + "Fancy";
			case 3:
				return var4 + "OFF";
			default:
				return var4 + "Default";
			}
		} else if(var1 == EnumOptions.WATER) {
			switch(this.ofWater) {
			case 1:
				return var4 + "Fast";
			case 2:
				return var4 + "Fancy";
			case 3:
				return var4 + "OFF";
			default:
				return var4 + "Default";
			}
		} else if(var1 == EnumOptions.ANIMATED_WATER) {
			switch(this.ofAnimatedWater) {
			case 1:
				return var4 + "Dynamic";
			case 2:
				return var4 + "OFF";
			default:
				return var4 + "ON";
			}
		} else if(var1 == EnumOptions.ANIMATED_LAVA) {
			switch(this.ofAnimatedLava) {
			case 1:
				return var4 + "Dynamic";
			case 2:
				return var4 + "OFF";
			default:
				return var4 + "ON";
			}
		} else if(var1 == EnumOptions.ANIMATED_FIRE) {
			return this.ofAnimatedFire?var4 + "ON":var4 + "OFF";
		} else if(var1 == EnumOptions.ANIMATED_PORTAL) {
			return this.ofAnimatedPortal?var4 + "ON":var4 + "OFF";
		} else if(var1 == EnumOptions.ANIMATED_REDSTONE) {
			return this.ofAnimatedRedstone?var4 + "ON":var4 + "OFF";
		} else if(var1 == EnumOptions.ANIMATED_EXPLOSION) {
			return this.ofAnimatedExplosion?var4 + "ON":var4 + "OFF";
		} else if(var1 == EnumOptions.ANIMATED_FLAME) {
			return this.ofAnimatedFlame?var4 + "ON":var4 + "OFF";
		} else if(var1 == EnumOptions.ANIMATED_SMOKE) {
			return this.ofAnimatedSmoke?var4 + "ON":var4 + "OFF";
		} else if(var1 == EnumOptions.FAST_DEBUG_INFO) {
			if (fastDebugMode == 0) {
				return var4 + "OFF";
			}
			if (fastDebugMode == 1) {
				return var4 + "ON";
			}
			return var4 + "FPS Only";
		} else if(var1 == EnumOptions.AUTOSAVE_TICKS) {
			return this.ofAutoSaveTicks <= 40?var4 + "Default (2s)":(this.ofAutoSaveTicks <= 400?var4 + "20s":(this.ofAutoSaveTicks <= 4000?var4 + "3min":var4 + "30min"));
		} else if(var1 == EnumOptions.BETTER_GRASS) {
			switch(this.ofBetterGrass) {
			case 1:
				return var4 + "Fast";
			case 2:
				return var4 + "Fancy";
			default:
				return var4 + "OFF";
			}
		} else {
			return var1 == EnumOptions.WEATHER?(this.ofWeather?var4 + "ON":var4 + "OFF"):(var1 == EnumOptions.SKY?(this.ofSky?var4 + "ON":var4 + "OFF"):(var1 == EnumOptions.STARS?(this.ofStars?var4 + "ON":var4 + "OFF"):(var1 == EnumOptions.CHUNK_UPDATES?var4 + this.ofChunkUpdates:(var1 == EnumOptions.CHUNK_UPDATES_DYNAMIC?(this.ofChunkUpdatesDynamic?var4 + "ON":var4 + "OFF"):(var1 == EnumOptions.FAR_VIEW?(this.ofFarView?var4 + "ON":var4 + "OFF"):(var1 == EnumOptions.TIME?(this.ofTime == 1?var4 + "Day Only":(this.ofTime == 2?var4 + "Night Only":var4 + "Default")):(var1 == EnumOptions.CLEAR_WATER?(this.ofClearWater?var4 + "ON":var4 + "OFF"):(var1 == EnumOptions.GRAPHICS?(this.fancyGraphics?var4 + var2.translateKey("options.graphics.fancy"):var4 + var2.translateKey("options.graphics.fast")):var4))))))));
		}
	}
//Spout End
	public void loadOptions() {
		try {
			if(!this.optionsFile.exists()) {
				return;
			}

			BufferedReader var1 = new BufferedReader(new FileReader(this.optionsFile));
			String var2 = "";

			while((var2 = var1.readLine()) != null) {
				try {
					String[] var3 = var2.split(":");
					if(var3[0].equals("music")) {
						this.musicVolume = this.parseFloat(var3[1]);
					}

					if(var3[0].equals("sound")) {
						this.soundVolume = this.parseFloat(var3[1]);
					}

					if(var3[0].equals("mouseSensitivity")) {
						this.mouseSensitivity = this.parseFloat(var3[1]);
					}

					if(var3[0].equals("fov")) {
						this.field_35379_L = this.parseFloat(var3[1]);
					}

					if(var3[0].equals("gamma")) {
						this.field_35380_M = this.parseFloat(var3[1]);
					}

					if(var3[0].equals("invertYMouse")) {
						this.invertMouse = var3[1].equals("true");
					}

					if(var3[0].equals("viewDistance")) {
						this.renderDistance = Integer.parseInt(var3[1]);
					}

					if(var3[0].equals("guiScale")) {
						this.guiScale = Integer.parseInt(var3[1]);
					}

					if(var3[0].equals("bobView")) {
						this.viewBobbing = var3[1].equals("true");
					}

					if(var3[0].equals("anaglyph3d")) {
						this.anaglyph = var3[1].equals("true");
					}

					if(var3[0].equals("advancedOpengl")) {
						this.advancedOpengl = var3[1].equals("true");
					}

					if(var3[0].equals("fpsLimit")) {
						this.limitFramerate = Integer.parseInt(var3[1]);
						Display.setVSyncEnabled(this.limitFramerate == 3); //Spout Start
					}

					if(var3[0].equals("difficulty")) {
						this.difficulty = Integer.parseInt(var3[1]);
					}

					if(var3[0].equals("fancyGraphics")) {
						this.fancyGraphics = var3[1].equals("true");
					}

					if(var3[0].equals("ao")) {
						this.ambientOcclusion = var3[1].equals("true");
						//Spout Start
						if(this.ambientOcclusion) {
							this.ofAoLevel = 1.0F;
						} else {
							this.ofAoLevel = 0.0F;
						}
						//Spout End
					}

					if(var3[0].equals("skin")) {
						this.skin = var3[1];
					}

					if(var3[0].equals("lastServer") && var3.length >= 2) {
						this.lastServer = var3[1];
					}

					for(int var4 = 0; var4 < this.keyBindings.length; ++var4) {
						if(var3[0].equals("key_" + this.keyBindings[var4].keyDescription)) {
							this.keyBindings[var4].keyCode = Integer.parseInt(var3[1]);
						}
					}
//Spout Start
					if(var3[0].equals("ofFogFancy") && var3.length >= 2) {
						this.ofFogFancy = var3[1].equals("true");
					}

					if(var3[0].equals("ofFogStart") && var3.length >= 2) {
						this.ofFogStart = Float.valueOf(var3[1]).floatValue();
						if(this.ofFogStart < 0.2F) {
							this.ofFogStart = 0.2F;
						}

						if(this.ofFogStart > 0.81F) {
							this.ofFogStart = 0.8F;
						}
					}

					if(var3[0].equals("ofMipmapLevel") && var3.length >= 2) {
						this.ofMipmapLevel = Integer.valueOf(var3[1]).intValue();
						if(this.ofMipmapLevel < 0) {
							this.ofMipmapLevel = 0;
						}

						if(this.ofMipmapLevel > 4) {
							this.ofMipmapLevel = 4;
						}
					}

					if(var3[0].equals("ofMipmapLinear") && var3.length >= 2) {
						this.ofMipmapLinear = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("ofLoadFar") && var3.length >= 2) {
						this.ofLoadFar = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("ofPreloadedChunks") && var3.length >= 2) {
						this.ofPreloadedChunks = Integer.valueOf(var3[1]).intValue();
						if(this.ofPreloadedChunks < 0) {
							this.ofPreloadedChunks = 0;
						}

						if(this.ofPreloadedChunks > 8) {
							this.ofPreloadedChunks = 8;
						}
					}

					if(var3[0].equals("ofOcclusionFancy") && var3.length >= 2) {
						this.ofOcclusionFancy = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("ofSmoothFps") && var3.length >= 2) {
						this.ofSmoothFps = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("ofBrightness") && var3.length >= 2) {
						this.ofBrightness = Float.valueOf(var3[1]).floatValue();
						this.ofBrightness = Config.limit(this.ofBrightness, 0.0F, 1.0F);
						this.updateWorldLightLevels();
					}

					if(var3[0].equals("ofAoLevel") && var3.length >= 2) {
						this.ofAoLevel = Float.valueOf(var3[1]).floatValue();
						this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0F, 1.0F);
						this.ambientOcclusion = this.ofAoLevel > 0.0F;
					}

					if(var3[0].equals("ofClouds") && var3.length >= 2) {
						this.ofClouds = Integer.valueOf(var3[1]).intValue();
						this.ofClouds = Config.limit(this.ofClouds, 0, 3);
					}

					if(var3[0].equals("ofCloudsHeight") && var3.length >= 2) {
						this.ofCloudsHeight = Float.valueOf(var3[1]).floatValue();
						this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0F, 1.0F);
					}

					if(var3[0].equals("ofTrees") && var3.length >= 2) {
						this.ofTrees = Integer.valueOf(var3[1]).intValue();
						this.ofTrees = Config.limit(this.ofTrees, 0, 2);
					}

					if(var3[0].equals("ofGrass") && var3.length >= 2) {
						this.ofGrass = Integer.valueOf(var3[1]).intValue();
						this.ofGrass = Config.limit(this.ofGrass, 0, 2);
					}

					if(var3[0].equals("ofRain") && var3.length >= 2) {
						this.ofRain = Integer.valueOf(var3[1]).intValue();
						this.ofRain = Config.limit(this.ofRain, 0, 3);
					}

					if(var3[0].equals("ofWater") && var3.length >= 2) {
						this.ofWater = Integer.valueOf(var3[1]).intValue();
						this.ofWater = Config.limit(this.ofWater, 0, 3);
					}

					if(var3[0].equals("ofAnimatedWater") && var3.length >= 2) {
						this.ofAnimatedWater = Integer.valueOf(var3[1]).intValue();
						this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
					}

					if(var3[0].equals("ofAnimatedLava") && var3.length >= 2) {
						this.ofAnimatedLava = Integer.valueOf(var3[1]).intValue();
						this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
					}

					if(var3[0].equals("ofAnimatedFire") && var3.length >= 2) {
						this.ofAnimatedFire = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("ofAnimatedPortal") && var3.length >= 2) {
						this.ofAnimatedPortal = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("ofAnimatedRedstone") && var3.length >= 2) {
						this.ofAnimatedRedstone = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("ofAnimatedExplosion") && var3.length >= 2) {
						this.ofAnimatedExplosion = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("ofAnimatedFlame") && var3.length >= 2) {
						this.ofAnimatedFlame = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("ofAnimatedSmoke") && var3.length >= 2) {
						this.ofAnimatedSmoke = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("fastDebugMode") && var3.length >= 2) {
						this.fastDebugMode = (byte) Integer.valueOf(var3[1]).intValue();
					}

					if(var3[0].equals("ofAutoSaveTicks") && var3.length >= 2) {
						this.ofAutoSaveTicks = Integer.valueOf(var3[1]).intValue();
						this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, '\u9c40');
					}

					if(var3[0].equals("ofBetterGrass") && var3.length >= 2) {
						this.ofBetterGrass = Integer.valueOf(var3[1]).intValue();
						this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
					}

					if(var3[0].equals("ofWeather") && var3.length >= 2) {
						this.ofWeather = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("ofSky") && var3.length >= 2) {
						this.ofSky = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("ofStars") && var3.length >= 2) {
						this.ofStars = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("ofChunkUpdates") && var3.length >= 2) {
						this.ofChunkUpdates = Integer.valueOf(var3[1]).intValue();
						this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
					}

					if(var3[0].equals("ofChunkUpdatesDynamic") && var3.length >= 2) {
						this.ofChunkUpdatesDynamic = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("ofFarView") && var3.length >= 2) {
						this.ofFarView = Boolean.valueOf(var3[1]).booleanValue();
					}

					if(var3[0].equals("ofTime") && var3.length >= 2) {
						this.ofTime = Integer.valueOf(var3[1]).intValue();
						this.ofTime = Config.limit(this.ofTime, 0, 2);
					}

					if(var3[0].equals("ofClearWater") && var3.length >= 2) {
						this.ofClearWater = Boolean.valueOf(var3[1]).booleanValue();
						this.updateWaterOpacity();
					}
//Spout End
				} catch (Exception var5) {
					System.out.println("Skipping bad option: " + var2);
				}
			}

			KeyBinding.func_35961_b();
			var1.close();
		} catch (Exception var6) {
			System.out.println("Failed to load options");
			var6.printStackTrace();
		}

	}

	private float parseFloat(String var1) {
		return var1.equals("true")?1.0F:(var1.equals("false")?0.0F:Float.parseFloat(var1));
	}

	public void saveOptions() {
		try {
			PrintWriter var1 = new PrintWriter(new FileWriter(this.optionsFile));
			var1.println("music:" + this.musicVolume);
			var1.println("sound:" + this.soundVolume);
			var1.println("invertYMouse:" + this.invertMouse);
			var1.println("mouseSensitivity:" + this.mouseSensitivity);
			var1.println("fov:" + this.field_35379_L);
			var1.println("gamma:" + this.field_35380_M);
			var1.println("viewDistance:" + this.renderDistance);
			var1.println("guiScale:" + this.guiScale);
			var1.println("bobView:" + this.viewBobbing);
			var1.println("anaglyph3d:" + this.anaglyph);
			var1.println("advancedOpengl:" + this.advancedOpengl);
			var1.println("fpsLimit:" + this.limitFramerate);
			var1.println("difficulty:" + this.difficulty);
			var1.println("fancyGraphics:" + this.fancyGraphics);
			var1.println("ao:" + this.ambientOcclusion);
			var1.println("skin:" + this.skin);
			var1.println("lastServer:" + this.lastServer);

			for(int var2 = 0; var2 < this.keyBindings.length; ++var2) {
				var1.println("key_" + this.keyBindings[var2].keyDescription + ":" + this.keyBindings[var2].keyCode);
			}
//Spout Start
			var1.println("ofFogFancy:" + this.ofFogFancy);
			var1.println("ofFogStart:" + this.ofFogStart);
			var1.println("ofMipmapLevel:" + this.ofMipmapLevel);
			var1.println("ofMipmapLinear:" + this.ofMipmapLinear);
			var1.println("ofLoadFar:" + this.ofLoadFar);
			var1.println("ofPreloadedChunks:" + this.ofPreloadedChunks);
			var1.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
			var1.println("ofSmoothFps:" + this.ofSmoothFps);
			var1.println("ofBrightness:" + this.ofBrightness);
			var1.println("ofAoLevel:" + this.ofAoLevel);
			var1.println("ofClouds:" + this.ofClouds);
			var1.println("ofCloudsHeight:" + this.ofCloudsHeight);
			var1.println("ofTrees:" + this.ofTrees);
			var1.println("ofGrass:" + this.ofGrass);
			var1.println("ofRain:" + this.ofRain);
			var1.println("ofWater:" + this.ofWater);
			var1.println("ofAnimatedWater:" + this.ofAnimatedWater);
			var1.println("ofAnimatedLava:" + this.ofAnimatedLava);
			var1.println("ofAnimatedFire:" + this.ofAnimatedFire);
			var1.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
			var1.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
			var1.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
			var1.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
			var1.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
			var1.println("fastDebugMode:" + this.fastDebugMode);
			var1.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
			var1.println("ofBetterGrass:" + this.ofBetterGrass);
			var1.println("ofWeather:" + this.ofWeather);
			var1.println("ofSky:" + this.ofSky);
			var1.println("ofStars:" + this.ofStars);
			var1.println("ofChunkUpdates:" + this.ofChunkUpdates);
			var1.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
			var1.println("ofFarView:" + this.ofFarView);
			var1.println("ofTime:" + this.ofTime);
			var1.println("ofClearWater:" + this.ofClearWater);
//Spout End
			var1.close();
		} catch (Exception var3) {
			System.out.println("Failed to save options");
			var3.printStackTrace();
		}

	}

}
