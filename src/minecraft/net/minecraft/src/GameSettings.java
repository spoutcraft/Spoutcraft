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
import org.spoutcraft.client.SpoutClient;

public class GameSettings {

	private static final String[] RENDER_DISTANCES = new String[] { "options.renderDistance.far", "options.renderDistance.normal", "options.renderDistance.short", "options.renderDistance.tiny" };
	private static final String[] DIFFICULTIES = new String[] { "options.difficulty.peaceful", "options.difficulty.easy", "options.difficulty.normal", "options.difficulty.hard" };
	private static final String[] GUISCALES = new String[] { "options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large" };
	private static final String[] PARTICLES = new String[] { "options.particles.all", "options.particles.decreased", "options.particles.minimal" };
	private static final String[] LIMIT_FRAMERATES = new String[] { "performance.max", "performance.balanced", "performance.powersaver" };
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
	public boolean clouds = true;
	public String skin = "Default";
	public KeyBinding keyBindForward = new KeyBinding("key.forward", 17);
	public KeyBinding keyBindLeft = new KeyBinding("key.left", 30);
	public KeyBinding keyBindBack = new KeyBinding("key.back", 31);
	public KeyBinding keyBindRight = new KeyBinding("key.right", 32);
	public KeyBinding keyBindJump = new KeyBinding("key.jump", 57);
	public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18);
	public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16);
	public KeyBinding keyBindChat = new KeyBinding("key.chat", 20);
	public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42);
	public KeyBinding keyBindAttack = new KeyBinding("key.attack", -100);
	public KeyBinding keyBindUseItem = new KeyBinding("key.use", -99);
	public KeyBinding keyBindPlayerList = new KeyBinding("key.playerlist", 15);
	public KeyBinding keyBindPickBlock = new KeyBinding("key.pickItem", -98);
	//Spout start
	public KeyBinding keyBindToggleFog = new KeyBinding("key.fog", Keyboard.KEY_F); 
	public KeyBinding keySneakToggle = new KeyBinding("Sneak Toggle", Keyboard.KEY_LCONTROL);
	public KeyBinding keyRunToggle = new KeyBinding("Run Toggle", Keyboard.KEY_RCONTROL);
	//Spout end
	public KeyBinding[] keyBindings;
	protected Minecraft mc;
	private File optionsFile;
	public int difficulty;
	public boolean hideGUI;
	public int thirdPersonView;
	public boolean showDebugInfo;
	public String lastServer;
	public boolean noclip;
	public boolean smoothCamera;
	public boolean debugCamEnable;
	public float noclipRate;
	public float debugCamRate;
	public float fovSetting;
	public float gammaSetting;
	public int guiScale;
	public int particles;
	public String field_44018_Q;

	public GameSettings(Minecraft var1, File var2) {
		this.keyBindings = new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindToggleFog, this.keySneakToggle, this.keyRunToggle}; //Spout added fog
		this.difficulty = 2;
		this.hideGUI = false;
		this.thirdPersonView = 0;
		this.showDebugInfo = false;
		this.lastServer = "";
		this.noclip = false;
		this.smoothCamera = false;
		this.debugCamEnable = false;
		this.noclipRate = 1.0F;
		this.debugCamRate = 1.0F;
		this.fovSetting = 0.0F;
		this.gammaSetting = 0.0F;
		this.guiScale = 0;
		this.particles = 0;
		this.mc = var1;
		this.optionsFile = new File(var2, "options.txt");
		this.loadOptions();
	}

	public GameSettings() {
		this.keyBindings = new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindToggleFog}; //Spout added fog
		this.difficulty = 2;
		this.hideGUI = false;
		this.thirdPersonView = 0;
		this.showDebugInfo = false;
		this.lastServer = "";
		this.noclip = false;
		this.smoothCamera = false;
		this.debugCamEnable = false;
		this.noclipRate = 1.0F;
		this.debugCamRate = 1.0F;
		this.fovSetting = 0.0F;
		this.gammaSetting = 0.0F;
		this.guiScale = 0;
		this.particles = 0;
	}

	public String getKeyBindingDescription(int var1) {
		StringTranslate var2 = StringTranslate.getInstance();
		return var2.translateKey(this.keyBindings[var1].keyDescription);
	}

	public String getOptionDisplayString(int var1) {
		int var2 = this.keyBindings[var1].keyCode;
		return getKeyDisplayString(var2);
	}

	public static String getKeyDisplayString(int var0) {
		return var0 < 0 ? StatCollector.translateToLocalFormatted("key.mouseButton", new Object[] { Integer.valueOf(var0 + 101) }) : Keyboard.getKeyName(var0);
	}

	public void setKeyBinding(int var1, int var2) {
		this.keyBindings[var1].keyCode = var2;
		this.saveOptions();
	}

	public void setOptionFloatValue(EnumOptions var1, float var2) {
		if (var1 == EnumOptions.MUSIC) {
			this.musicVolume = var2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if (var1 == EnumOptions.SOUND) {
			this.soundVolume = var2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if (var1 == EnumOptions.SENSITIVITY) {
			this.mouseSensitivity = var2;
		}

		if (var1 == EnumOptions.FOV) {
			this.fovSetting = var2;
		}

		if (var1 == EnumOptions.GAMMA) {
			this.gammaSetting = var2;
		}

	}

	public void setOptionValue(EnumOptions var1, int var2) {
		if (var1 == EnumOptions.INVERT_MOUSE) {
			this.invertMouse = !this.invertMouse;
		}

		if (var1 == EnumOptions.RENDER_DISTANCE) {
			this.renderDistance = this.renderDistance + var2 & 3;
		}

		if (var1 == EnumOptions.GUI_SCALE) {
			this.guiScale = this.guiScale + var2 & 3;
		}

		if (var1 == EnumOptions.PARTICLES) {
			this.particles = (this.particles + var2) % 3;
		}

		if (var1 == EnumOptions.VIEW_BOBBING) {
			this.viewBobbing = !this.viewBobbing;
		}

		if (var1 == EnumOptions.RENDER_CLOUDS) {
			this.clouds = !this.clouds;
		}

		if (var1 == EnumOptions.ADVANCED_OPENGL) {
			this.advancedOpengl = !this.advancedOpengl;
			this.mc.renderGlobal.loadRenderers();
		}

		if (var1 == EnumOptions.ANAGLYPH) {
			this.anaglyph = !this.anaglyph;
			this.mc.renderEngine.refreshTextures();
		}

		if (var1 == EnumOptions.FRAMERATE_LIMIT) {
			this.limitFramerate = (this.limitFramerate + var2 + 3) % 3;
		}

		if (var1 == EnumOptions.DIFFICULTY) {
			this.difficulty = this.difficulty + var2 & 3;
		}

		if (var1 == EnumOptions.GRAPHICS) {
			this.fancyGraphics = !this.fancyGraphics;
			this.mc.renderGlobal.loadRenderers();
		}

		if (var1 == EnumOptions.AMBIENT_OCCLUSION) {
			this.ambientOcclusion = !this.ambientOcclusion;
			this.mc.renderGlobal.loadRenderers();
		}

		this.saveOptions();
	}

	public float getOptionFloatValue(EnumOptions var1) {
		return var1 == EnumOptions.FOV ? this.fovSetting : (var1 == EnumOptions.GAMMA ? this.gammaSetting : (var1 == EnumOptions.MUSIC ? this.musicVolume : (var1 == EnumOptions.SOUND ? this.soundVolume : (var1 == EnumOptions.SENSITIVITY ? this.mouseSensitivity : 0.0F))));
	}

	public boolean getOptionOrdinalValue(EnumOptions var1) {
		switch (EnumOptionsMappingHelper.enumOptionsMappingHelperArray[var1.ordinal()]) {
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
		case 6:
			return this.clouds;
		default:
			return false;
		}
	}

	public String getKeyBinding(EnumOptions var1) {
		StringTranslate var2 = StringTranslate.getInstance();
		String var3 = var2.translateKey(var1.getEnumString()) + ": ";
		if (var1.getEnumFloat()) {
			float var5 = this.getOptionFloatValue(var1);
			return var1 == EnumOptions.SENSITIVITY ? (var5 == 0.0F ? var3 + var2.translateKey("options.sensitivity.min") : (var5 == 1.0F ? var3 + var2.translateKey("options.sensitivity.max") : var3 + (int) (var5 * 200.0F) + "%")) : (var1 == EnumOptions.FOV ? (var5 == 0.0F ? var3 + var2.translateKey("options.fov.min") : (var5 == 1.0F ? var3 + var2.translateKey("options.fov.max") : var3 + (int) (70.0F + var5 * 40.0F))) : (var1 == EnumOptions.GAMMA ? (var5 == 0.0F ? var3 + var2.translateKey("options.gamma.min") : (var5 == 1.0F ? var3 + var2.translateKey("options.gamma.max") : var3 + "+" + (int) (var5 * 100.0F) + "%")) : (var5 == 0.0F ? var3 + var2.translateKey("options.off") : var3 + (int) (var5 * 100.0F) + "%")));
		} else if (var1.getEnumBoolean()) {
			boolean var4 = this.getOptionOrdinalValue(var1);
			return var4 ? var3 + var2.translateKey("options.on") : var3 + var2.translateKey("options.off");
		} else {
			return var1 == EnumOptions.RENDER_DISTANCE ? var3 + var2.translateKey(RENDER_DISTANCES[this.renderDistance]) : (var1 == EnumOptions.DIFFICULTY ? var3 + var2.translateKey(DIFFICULTIES[this.difficulty]) : (var1 == EnumOptions.GUI_SCALE ? var3 + var2.translateKey(GUISCALES[this.guiScale]) : (var1 == EnumOptions.PARTICLES ? var3 + var2.translateKey(PARTICLES[this.particles]) : (var1 == EnumOptions.FRAMERATE_LIMIT ? var3 + StatCollector.translateToLocal(LIMIT_FRAMERATES[this.limitFramerate]) : (var1 == EnumOptions.GRAPHICS ? (this.fancyGraphics ? var3 + var2.translateKey("options.graphics.fancy") : var3 + var2.translateKey("options.graphics.fast")) : var3)))));
		}
	}

	public void loadOptions() {
		try {
			if (!this.optionsFile.exists()) {
				return;
			}

			BufferedReader var1 = new BufferedReader(new FileReader(this.optionsFile));
			String var2 = "";

			while ((var2 = var1.readLine()) != null) {
				try {
					String[] var3 = var2.split(":");
					if (var3[0].equals("music")) {
						this.musicVolume = this.parseFloat(var3[1]);
					}

					if (var3[0].equals("sound")) {
						this.soundVolume = this.parseFloat(var3[1]);
					}

					if (var3[0].equals("mouseSensitivity")) {
						this.mouseSensitivity = this.parseFloat(var3[1]);
					}

					if (var3[0].equals("fov")) {
						this.fovSetting = this.parseFloat(var3[1]);
					}

					if (var3[0].equals("gamma")) {
						this.gammaSetting = this.parseFloat(var3[1]);
					}

					if (var3[0].equals("invertYMouse")) {
						this.invertMouse = var3[1].equals("true");
					}

					if (var3[0].equals("viewDistance")) {
						this.renderDistance = Integer.parseInt(var3[1]);
					}

					if (var3[0].equals("guiScale")) {
						this.guiScale = Integer.parseInt(var3[1]);
					}

					if (var3[0].equals("particles")) {
						this.particles = Integer.parseInt(var3[1]);
					}

					if (var3[0].equals("bobView")) {
						this.viewBobbing = var3[1].equals("true");
					}

					if (var3[0].equals("anaglyph3d")) {
						this.anaglyph = var3[1].equals("true");
					}

					if (var3[0].equals("advancedOpengl")) {
						this.advancedOpengl = var3[1].equals("true");
					}

					if (var3[0].equals("fpsLimit")) {
						this.limitFramerate = Integer.parseInt(var3[1]);
					}

					if (var3[0].equals("difficulty")) {
						this.difficulty = Integer.parseInt(var3[1]);
					}

					if (var3[0].equals("fancyGraphics")) {
						this.fancyGraphics = var3[1].equals("true");
					}

					if (var3[0].equals("ao")) {
						this.ambientOcclusion = var3[1].equals("true");
					}

					if (var3[0].equals("clouds")) {
						this.clouds = var3[1].equals("true");
					}

					if (var3[0].equals("skin")) {
						this.skin = var3[1];
					}

					if (var3[0].equals("lastServer") && var3.length >= 2) {
						this.lastServer = var3[1];
					}

					if(var3[0].equals("lang") && var3.length >= 2) {
								this.field_44018_Q = var3[1];
					}

					for (int var4 = 0; var4 < this.keyBindings.length; ++var4) {
						if (var3[0].equals("key_" + this.keyBindings[var4].keyDescription)) {
							this.keyBindings[var4].keyCode = Integer.parseInt(var3[1]);
						}
					}
				} catch (Exception var5) {
					System.out.println("Skipping bad option: " + var2);
				}
			}

			KeyBinding.resetKeyBindingArrayAndHash();
			var1.close();
		} catch (Exception var6) {
			System.out.println("Failed to load options");
			var6.printStackTrace();
		}

	}

	private float parseFloat(String var1) {
		return var1.equals("true") ? 1.0F : (var1.equals("false") ? 0.0F : Float.parseFloat(var1));
	}

	public void saveOptions() {
		boolean oldLock = SpoutClient.disableSandbox();;
		try {
			PrintWriter var1 = new PrintWriter(new FileWriter(this.optionsFile));
			var1.println("music:" + this.musicVolume);
			var1.println("sound:" + this.soundVolume);
			var1.println("invertYMouse:" + this.invertMouse);
			var1.println("mouseSensitivity:" + this.mouseSensitivity);
			var1.println("fov:" + this.fovSetting);
			var1.println("gamma:" + this.gammaSetting);
			var1.println("viewDistance:" + this.renderDistance);
			var1.println("guiScale:" + this.guiScale);
			var1.println("particles:" + this.particles);
			var1.println("bobView:" + this.viewBobbing);
			var1.println("anaglyph3d:" + this.anaglyph);
			var1.println("advancedOpengl:" + this.advancedOpengl);
			var1.println("fpsLimit:" + this.limitFramerate);
			var1.println("difficulty:" + this.difficulty);
			var1.println("fancyGraphics:" + this.fancyGraphics);
			var1.println("ao:" + this.ambientOcclusion);
			var1.println("clouds:" + this.clouds);
			var1.println("skin:" + this.skin);
			var1.println("lastServer:" + this.lastServer);
			var1.println("lang:" + this.field_44018_Q);

			for (int var2 = 0; var2 < this.keyBindings.length; ++var2) {
				var1.println("key_" + this.keyBindings[var2].keyDescription + ":" + this.keyBindings[var2].keyCode);
			}

			var1.close();
		} catch (Exception var3) {
			System.out.println("Failed to save options");
			var3.printStackTrace();
		} finally {
			SpoutClient.enableSandbox(oldLock);
		}

	}

	public boolean shouldRenderClouds() {
		return this.renderDistance < 2 && this.clouds;
	}

}
