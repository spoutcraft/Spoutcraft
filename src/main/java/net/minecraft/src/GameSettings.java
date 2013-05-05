package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
// Spout Start
import org.spoutcraft.client.SpoutClient;
// Spout End

public class GameSettings {
	private static final String[] RENDER_DISTANCES = new String[] {"options.renderDistance.far", "options.renderDistance.normal", "options.renderDistance.short", "options.renderDistance.tiny"};
	private static final String[] DIFFICULTIES = new String[] {"options.difficulty.peaceful", "options.difficulty.easy", "options.difficulty.normal", "options.difficulty.hard"};

	/** GUI scale values */
	private static final String[] GUISCALES = new String[] {"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
	private static final String[] CHAT_VISIBILITIES = new String[] {"options.chat.visibility.full", "options.chat.visibility.system", "options.chat.visibility.hidden"};
	private static final String[] PARTICLES = new String[] {"options.particles.all", "options.particles.decreased", "options.particles.minimal"};

	/** Limit framerate labels */
	private static final String[] LIMIT_FRAMERATES = new String[] {"performance.max", "performance.balanced", "performance.powersaver"};
	private static final String[] AMBIENT_OCCLUSIONS = new String[] {"options.ao.off", "options.ao.min", "options.ao.max"};
	public float musicVolume = 1.0F;
	public float soundVolume = 1.0F;
	public float mouseSensitivity = 0.5F;
	public boolean invertMouse = false;
	public int renderDistance = 0;
	public boolean viewBobbing = true;
	public boolean anaglyph = false;

	/** Advanced OpenGL */
	public boolean advancedOpengl = false;
	public int limitFramerate = 1;
	public boolean fancyGraphics = true;

	/** Smooth Lighting */
	public int ambientOcclusion = 2;

	/** Clouds flag */
	public boolean clouds = true;

	/** The name of the selected texture pack. */
	public String skin = "Default";
	public int chatVisibility = 0;
	public boolean chatColours = true;
	public boolean chatLinks = true;
	public boolean chatLinksPrompt = true;
	public float chatOpacity = 1.0F;
	public boolean serverTextures = true;
	public boolean snooperEnabled = true;
	public boolean fullScreen = false;
	public boolean enableVsync = true;
	public boolean hideServerAddress = false;

	/**
	 * Whether to show advanced information on item tooltips, toggled by F3+H
	 */
	public boolean advancedItemTooltips = false;

	/** Whether to pause when the game loses focus, toggled by F3+P */
	public boolean pauseOnLostFocus = true;

	/** Whether to show your cape */
	public boolean showCape = true;
	public boolean touchscreen = false;
	public int overrideWidth = 0;
	public int overrideHeight = 0;
	public boolean heldItemTooltips = true;
	public float chatScale = 1.0F;
	public float chatWidth = 1.0F;
	public float chatHeightUnfocused = 0.44366196F;
	public float chatHeightFocused = 1.0F; 
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
	public KeyBinding keyBindCommand = new KeyBinding("key.command", 53);
	// Spout Start
	public KeyBinding keyBindToggleFog = new KeyBinding("Toggle Fog", Keyboard.KEY_F); 
	public final KeyBinding keySneakToggle = new KeyBinding("Sneak Toggle", Keyboard.KEY_LCONTROL);
	public final KeyBinding keyRunToggle = new KeyBinding("Run Toggle", Keyboard.KEY_RCONTROL);
	public final KeyBinding keyTreadWaterToggle = new KeyBinding("Tread Water Toggle", Keyboard.KEY_Z); 
	public final KeyBinding keyAutoForward = new KeyBinding("Forward Toggle", Keyboard.KEY_G);
	public final KeyBinding keyAutoBackward = new KeyBinding("Backward Toggle", Keyboard.KEY_H);
	public final KeyBinding keyFlyToggle = new KeyBinding("Fly Toggle", Keyboard.KEY_O);
	public final KeyBinding keyFlyForward = new KeyBinding("Fly Foward", 17);
	public final KeyBinding keyFlyLeft = new KeyBinding("Fly Left", 30);
	public final KeyBinding keyFlyBack = new KeyBinding("Fly Back", 31);
	public final KeyBinding keyFlyRight = new KeyBinding("Fly Right", 32);
	public final KeyBinding keyFlyUp = new KeyBinding("Fly Up", Keyboard.KEY_SPACE);
	public final KeyBinding keyFlyDown = new KeyBinding("Fly Down", Keyboard.KEY_LSHIFT);
	public final KeyBinding keyWaypoint = new KeyBinding("Overview Map", Keyboard.KEY_P);
	public final KeyBinding keyHideChat = new KeyBinding("Hide Chat", Keyboard.KEY_M);
	public final KeyBinding[] spoutcraftBindings = {keyBindToggleFog, keySneakToggle, keyRunToggle, keyTreadWaterToggle, keyAutoForward,
			keyAutoBackward, keyFlyToggle, keyFlyForward, keyFlyLeft, keyFlyBack, keyFlyRight, keyFlyUp, keyFlyDown, keyWaypoint, keyHideChat};
	// Spout End
	public KeyBinding[] keyBindings;
	protected Minecraft mc;
	private File optionsFile;
	public int difficulty;
	public boolean hideGUI;
	public int thirdPersonView;

	/** true if debug info should be displayed instead of version */
	public boolean showDebugInfo;
	public boolean showDebugProfilerChart;

	/** The lastServer string. */
	public String lastServer;

	/** No clipping for singleplayer */
	public boolean noclip;

	/** Smooth Camera Toggle */
	public boolean smoothCamera;
	public boolean debugCamEnable;

	/** No clipping movement rate */
	public float noclipRate;

	/** Change rate for debug camera */
	public float debugCamRate;
	public float fovSetting;
	public float gammaSetting;

	/** GUI scale */
	public int guiScale;

	/** Determines amount of particles. 0 = All, 1 = Decreased, 2 = Minimal */
	public int particleSetting;

	/** Game settings language */
	public String language;

	public GameSettings(Minecraft par1Minecraft, File par2File) {
		this.keyBindings = new KeyBinding[] {this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand};
		this.difficulty = 2;
		this.hideGUI = false;
		this.thirdPersonView = 0;
		this.showDebugInfo = false;
		this.showDebugProfilerChart = false;
		this.lastServer = "";
		this.noclip = false;
		this.smoothCamera = false;
		this.debugCamEnable = false;
		this.noclipRate = 1.0F;
		this.debugCamRate = 1.0F;
		this.fovSetting = 0.0F;
		this.gammaSetting = 0.0F;
		this.guiScale = 0;
		this.particleSetting = 0;
		this.language = "en_US";
		this.mc = par1Minecraft;
		this.optionsFile = new File(par2File, "options.txt");
		this.loadOptions();
	}

	public GameSettings() {
		this.keyBindings = new KeyBinding[] {this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand};
		this.difficulty = 2;
		this.hideGUI = false;
		this.thirdPersonView = 0;
		this.showDebugInfo = false;
		this.showDebugProfilerChart = false;
		this.lastServer = "";
		this.noclip = false;
		this.smoothCamera = false;
		this.debugCamEnable = false;
		this.noclipRate = 1.0F;
		this.debugCamRate = 1.0F;
		this.fovSetting = 0.0F;
		this.gammaSetting = 0.0F;
		this.guiScale = 0;
		this.particleSetting = 0;
		this.language = "en_US";
	}

	public String getKeyBindingDescription(int par1) {
		StringTranslate var2 = StringTranslate.getInstance();
		return var2.translateKey(this.keyBindings[par1].keyDescription);
	}

	/**
	 * The string that appears inside the button/slider in the options menu.
	 */
	public String getOptionDisplayString(int par1) {
		int var2 = this.keyBindings[par1].keyCode;
		return getKeyDisplayString(var2);
	}

	/**
	 * Represents a key or mouse button as a string. Args: key
	 */
	public static String getKeyDisplayString(int par0) {
		return par0 < 0 ? StatCollector.translateToLocalFormatted("key.mouseButton", new Object[] {Integer.valueOf(par0 + 101)}): Keyboard.getKeyName(par0);
	}

	/**
	 * Returns whether the specified key binding is currently being pressed.
	 */
	public static boolean isKeyDown(KeyBinding par0KeyBinding) {
		return par0KeyBinding.keyCode < 0 ? Mouse.isButtonDown(par0KeyBinding.keyCode + 100) : Keyboard.isKeyDown(par0KeyBinding.keyCode);
	}

	/**
	 * Sets a key binding.
	 */
	public void setKeyBinding(int par1, int par2) {
		this.keyBindings[par1].keyCode = par2;
		this.saveOptions();
	}

	/**
	 * If the specified option is controlled by a slider (float value), this will set the float value.
	 */
	public void setOptionFloatValue(EnumOptions par1EnumOptions, float par2) {
		if (par1EnumOptions == EnumOptions.MUSIC) {
			this.musicVolume = par2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if (par1EnumOptions == EnumOptions.SOUND) {
			this.soundVolume = par2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if (par1EnumOptions == EnumOptions.SENSITIVITY) {
			this.mouseSensitivity = par2;
		}

		if (par1EnumOptions == EnumOptions.FOV) {
			this.fovSetting = par2;
		}

		if (par1EnumOptions == EnumOptions.GAMMA) {
			this.gammaSetting = par2;
		}

		if (par1EnumOptions == EnumOptions.CHAT_OPACITY) {
			this.chatOpacity = par2;
			this.mc.ingameGUI.getChatGUI().func_96132_b();
		}

		if (par1EnumOptions == EnumOptions.CHAT_HEIGHT_FOCUSED) {
			this.chatHeightFocused = par2;
			this.mc.ingameGUI.getChatGUI().func_96132_b();
		}

		if (par1EnumOptions == EnumOptions.CHAT_HEIGHT_UNFOCUSED) {
			this.chatHeightUnfocused = par2;
			this.mc.ingameGUI.getChatGUI().func_96132_b();
		}

		if (par1EnumOptions == EnumOptions.CHAT_WIDTH) {
			this.chatWidth = par2;
			this.mc.ingameGUI.getChatGUI().func_96132_b();
		}

		if (par1EnumOptions == EnumOptions.CHAT_SCALE) {
			this.chatScale = par2;
			this.mc.ingameGUI.getChatGUI().func_96132_b();
		}
	}

	/**
	 * For non-float options. Toggles the option on/off, or cycles through the list i.e. render distances.
	 */
	public void setOptionValue(EnumOptions par1EnumOptions, int par2) {
		if (par1EnumOptions == EnumOptions.INVERT_MOUSE) {
			this.invertMouse = !this.invertMouse;
		}

		if (par1EnumOptions == EnumOptions.RENDER_DISTANCE) {
			this.renderDistance = this.renderDistance + par2 & 3;
		}

		if (par1EnumOptions == EnumOptions.GUI_SCALE) {
			this.guiScale = this.guiScale + par2 & 3;
		}

		if (par1EnumOptions == EnumOptions.PARTICLES) {
			this.particleSetting = (this.particleSetting + par2) % 3;
		}

		if (par1EnumOptions == EnumOptions.VIEW_BOBBING) {
			this.viewBobbing = !this.viewBobbing;
		}

		if (par1EnumOptions == EnumOptions.RENDER_CLOUDS) {
			this.clouds = !this.clouds;
		}

		if (par1EnumOptions == EnumOptions.ADVANCED_OPENGL) {
			this.advancedOpengl = !this.advancedOpengl;
			this.mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == EnumOptions.ANAGLYPH) {
			this.anaglyph = !this.anaglyph;
			this.mc.renderEngine.refreshTextures();
		}

		if (par1EnumOptions == EnumOptions.FRAMERATE_LIMIT) {
			this.limitFramerate = (this.limitFramerate + par2 + 3) % 3;
		}

		if (par1EnumOptions == EnumOptions.DIFFICULTY) {
			this.difficulty = this.difficulty + par2 & 3;
		}

		if (par1EnumOptions == EnumOptions.GRAPHICS) {
			this.fancyGraphics = !this.fancyGraphics;
			this.mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == EnumOptions.AMBIENT_OCCLUSION) {
			this.ambientOcclusion = (this.ambientOcclusion + par2) % 3;
			this.mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == EnumOptions.CHAT_VISIBILITY) {
			this.chatVisibility = (this.chatVisibility + par2) % 3;
		}

		if (par1EnumOptions == EnumOptions.CHAT_COLOR) {
			this.chatColours = !this.chatColours;
		}

		if (par1EnumOptions == EnumOptions.CHAT_LINKS) {
			this.chatLinks = !this.chatLinks;
		}

		if (par1EnumOptions == EnumOptions.CHAT_LINKS_PROMPT) {
			this.chatLinksPrompt = !this.chatLinksPrompt;
		}

		if (par1EnumOptions == EnumOptions.USE_SERVER_TEXTURES) {
			this.serverTextures = !this.serverTextures;
		}

		if (par1EnumOptions == EnumOptions.SNOOPER_ENABLED) {
			this.snooperEnabled = !this.snooperEnabled;
		}

		if (par1EnumOptions == EnumOptions.SHOW_CAPE) {
			this.showCape = !this.showCape;
		}

		if (par1EnumOptions == EnumOptions.TOUCHSCREEN) {
			this.touchscreen = !this.touchscreen;
		}

		if (par1EnumOptions == EnumOptions.USE_FULLSCREEN) {
			this.fullScreen = !this.fullScreen;

			if (this.mc.isFullScreen() != this.fullScreen) {
				this.mc.toggleFullscreen();
			}
		}

		if (par1EnumOptions == EnumOptions.ENABLE_VSYNC) {
			this.enableVsync = !this.enableVsync;
			Display.setVSyncEnabled(this.enableVsync);
		}

		this.saveOptions();
	}

	public float getOptionFloatValue(EnumOptions par1EnumOptions) {
		return par1EnumOptions == EnumOptions.FOV ? this.fovSetting : (par1EnumOptions == EnumOptions.GAMMA ? this.gammaSetting : (par1EnumOptions == EnumOptions.MUSIC ? this.musicVolume : (par1EnumOptions == EnumOptions.SOUND ? this.soundVolume : (par1EnumOptions == EnumOptions.SENSITIVITY ? this.mouseSensitivity : (par1EnumOptions == EnumOptions.CHAT_OPACITY ? this.chatOpacity : (par1EnumOptions == EnumOptions.CHAT_HEIGHT_FOCUSED ? this.chatHeightFocused : (par1EnumOptions == EnumOptions.CHAT_HEIGHT_UNFOCUSED ? this.chatHeightUnfocused : (par1EnumOptions == EnumOptions.CHAT_SCALE ? this.chatScale : (par1EnumOptions == EnumOptions.CHAT_WIDTH ? this.chatWidth : 0.0F))))))))); 
	}

	public boolean getOptionOrdinalValue(EnumOptions par1EnumOptions) {
		switch (EnumOptionsHelper.enumOptionsMappingHelperArray[par1EnumOptions.ordinal()]) {
			case 1:
				return this.invertMouse;

			case 2:
				return this.viewBobbing;

			case 3:
				return this.anaglyph;

			case 4:
				return this.advancedOpengl;

			case 5:
				return this.clouds;

			case 6:
				return this.chatColours;

			case 7:
				return this.chatLinks;

			case 8:
				return this.chatLinksPrompt;

			case 9:
				return this.serverTextures;

			case 10:
				return this.snooperEnabled;

			case 11:
				return this.fullScreen;

			case 12:
				return this.enableVsync;

			case 13:
				return this.showCape;

			case 14:
				return this.touchscreen;

			default:
				return false;
		}
	}

	/**
	 * Returns the translation of the given index in the given String array. If the index is smaller than 0 or greater
	 * than/equal to the length of the String array, it is changed to 0.
	 */
	private static String getTranslation(String[] par0ArrayOfStr, int par1) {
		if (par1 < 0 || par1 >= par0ArrayOfStr.length) {
			par1 = 0;
		}

		StringTranslate var2 = StringTranslate.getInstance();
		return var2.translateKey(par0ArrayOfStr[par1]);
	}

	/**
	 * Gets a key binding.
	 */
	public String getKeyBinding(EnumOptions par1EnumOptions) {
		StringTranslate var2 = StringTranslate.getInstance();
		String var3 = var2.translateKey(par1EnumOptions.getEnumString()) + ": ";

		if (par1EnumOptions.getEnumFloat()) {
			float var5 = this.getOptionFloatValue(par1EnumOptions);
			return par1EnumOptions == EnumOptions.SENSITIVITY ? (var5 == 0.0F ? var3 + var2.translateKey("options.sensitivity.min") : (var5 == 1.0F ? var3 + var2.translateKey("options.sensitivity.max") : var3 + (int)(var5 * 200.0F) + "%")) : (par1EnumOptions == EnumOptions.FOV ? (var5 == 0.0F ? var3 + var2.translateKey("options.fov.min") : (var5 == 1.0F ? var3 + var2.translateKey("options.fov.max") : var3 + (int)(70.0F + var5 * 40.0F))) : (par1EnumOptions == EnumOptions.GAMMA ? (var5 == 0.0F ? var3 + var2.translateKey("options.gamma.min") : (var5 == 1.0F ? var3 + var2.translateKey("options.gamma.max") : var3 + "+" + (int)(var5 * 100.0F) + "%")) : (par1EnumOptions == EnumOptions.CHAT_OPACITY ? var3 + (int)(var5 * 90.0F + 10.0F) + "%" : (par1EnumOptions == EnumOptions.CHAT_HEIGHT_UNFOCUSED ? var3 + GuiNewChat.func_96130_b(var5) + "px" : (par1EnumOptions == EnumOptions.CHAT_HEIGHT_FOCUSED ? var3 + GuiNewChat.func_96130_b(var5) + "px" : (par1EnumOptions == EnumOptions.CHAT_WIDTH ? var3 + GuiNewChat.func_96128_a(var5) + "px" : (var5 == 0.0F ? var3 + var2.translateKey("options.off") : var3 + (int)(var5 * 100.0F) + "%")))))));
		} else if (par1EnumOptions.getEnumBoolean()) {
			boolean var4 = this.getOptionOrdinalValue(par1EnumOptions);
			return var4 ? var3 + var2.translateKey("options.on") : var3 + var2.translateKey("options.off");
		} else {
			return par1EnumOptions == EnumOptions.RENDER_DISTANCE ? var3 + getTranslation(RENDER_DISTANCES, this.renderDistance) : (par1EnumOptions == EnumOptions.DIFFICULTY ? var3 + getTranslation(DIFFICULTIES, this.difficulty) : (par1EnumOptions == EnumOptions.GUI_SCALE ? var3 + getTranslation(GUISCALES, this.guiScale) : (par1EnumOptions == EnumOptions.CHAT_VISIBILITY ? var3 + getTranslation(CHAT_VISIBILITIES, this.chatVisibility) : (par1EnumOptions == EnumOptions.PARTICLES ? var3 + getTranslation(PARTICLES, this.particleSetting) : (par1EnumOptions == EnumOptions.FRAMERATE_LIMIT ? var3 + getTranslation(LIMIT_FRAMERATES, this.limitFramerate) : (par1EnumOptions == EnumOptions.AMBIENT_OCCLUSION ? var3 + getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion) : (par1EnumOptions == EnumOptions.GRAPHICS ? (this.fancyGraphics ? var3 + var2.translateKey("options.graphics.fancy") : var3 + var2.translateKey("options.graphics.fast")) : var3)))))));
		}
	}

	/**
	 * Loads the options from the options file. It appears that this has replaced the previous 'loadOptions'
	 */
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
						this.particleSetting = Integer.parseInt(var3[1]);
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
						if (var3[1].equals("true")) {
							this.ambientOcclusion = 2;
						} else if (var3[1].equals("false")) {
							this.ambientOcclusion = 0;
						} else {
							this.ambientOcclusion = Integer.parseInt(var3[1]);
						}
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

					if (var3[0].equals("lang") && var3.length >= 2) {
						this.language = var3[1];
					}

					if (var3[0].equals("chatVisibility")) {
						this.chatVisibility = Integer.parseInt(var3[1]);
					}

					if (var3[0].equals("chatColors")) {
						this.chatColours = var3[1].equals("true");
					}

					if (var3[0].equals("chatLinks")) {
						this.chatLinks = var3[1].equals("true");
					}

					if (var3[0].equals("chatLinksPrompt")) {
						this.chatLinksPrompt = var3[1].equals("true");
					}

					if (var3[0].equals("chatOpacity")) {
						this.chatOpacity = this.parseFloat(var3[1]);
					}

					if (var3[0].equals("serverTextures")) {
						this.serverTextures = var3[1].equals("true");
					}

					if (var3[0].equals("snooperEnabled")) {
						this.snooperEnabled = var3[1].equals("true");
					}

					if (var3[0].equals("fullscreen")) {
						this.fullScreen = var3[1].equals("true");
					}

					if (var3[0].equals("enableVsync")) {
						this.enableVsync = var3[1].equals("true");
					}

					if (var3[0].equals("hideServerAddress")) {
						this.hideServerAddress = var3[1].equals("true");
					}

					if (var3[0].equals("advancedItemTooltips")) {
						this.advancedItemTooltips = var3[1].equals("true");
					}

					if (var3[0].equals("pauseOnLostFocus")) {
						this.pauseOnLostFocus = var3[1].equals("true");
					}

					if (var3[0].equals("showCape")) {
						this.showCape = var3[1].equals("true");
					}

					if (var3[0].equals("touchscreen")) {
						this.touchscreen = var3[1].equals("true");
					}

					if (var3[0].equals("overrideHeight")) {
						this.overrideHeight = Integer.parseInt(var3[1]);
					}

					if (var3[0].equals("overrideWidth")) {
						this.overrideWidth = Integer.parseInt(var3[1]);
					}

					if (var3[0].equals("heldItemTooltips")) {
						this.heldItemTooltips = var3[1].equals("true");
					}

					if (var3[0].equals("chatHeightFocused")) {
						this.chatHeightFocused = this.parseFloat(var3[1]);
					}

					if (var3[0].equals("chatHeightUnfocused")) {
						this.chatHeightUnfocused = this.parseFloat(var3[1]);
					}

					if (var3[0].equals("chatScale")) {
						this.chatScale = this.parseFloat(var3[1]);
					}

					if (var3[0].equals("chatWidth")) {
						this.chatWidth = this.parseFloat(var3[1]);
					}

					for (int var4 = 0; var4 < this.keyBindings.length; ++var4) {
						if (var3[0].equals("key_" + this.keyBindings[var4].keyDescription)) {
							this.keyBindings[var4].keyCode = Integer.parseInt(var3[1]);
						}
					}
					// Spout Start
					for (int key = 0; key < this.spoutcraftBindings.length; ++key) {
						if (var3[0].equals("key_" + this.spoutcraftBindings[key].keyDescription)) {
							this.spoutcraftBindings[key].keyCode = Integer.parseInt(var3[1]);
						}
					}
					// Spout End
				} catch (Exception var5) {
					this.mc.getLogAgent().logWarning("Skipping bad option: " + var2);
				}
			}

			KeyBinding.resetKeyBindingArrayAndHash();
			var1.close();
		} catch (Exception var6) {
			this.mc.getLogAgent().logWarning("Failed to load options");
			var6.printStackTrace();
		}
	}

	/**
	 * Parses a string into a float.
	 */
	private float parseFloat(String par1Str) {
		return par1Str.equals("true") ? 1.0F : (par1Str.equals("false") ? 0.0F : Float.parseFloat(par1Str));
	}

	/**
	 * Saves the options to the options file.
	 */
	public void saveOptions() {
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
			var1.println("particles:" + this.particleSetting);
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
			var1.println("lang:" + this.language);
			var1.println("chatVisibility:" + this.chatVisibility);
			var1.println("chatColors:" + this.chatColours);
			var1.println("chatLinks:" + this.chatLinks);
			var1.println("chatLinksPrompt:" + this.chatLinksPrompt);
			var1.println("chatOpacity:" + this.chatOpacity);
			var1.println("serverTextures:" + this.serverTextures);
			var1.println("snooperEnabled:" + this.snooperEnabled);
			var1.println("fullscreen:" + this.fullScreen);
			var1.println("enableVsync:" + this.enableVsync);
			var1.println("hideServerAddress:" + this.hideServerAddress);
			var1.println("advancedItemTooltips:" + this.advancedItemTooltips);
			var1.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
			var1.println("showCape:" + this.showCape);
			var1.println("touchscreen:" + this.touchscreen);
			var1.println("overrideWidth:" + this.overrideWidth);
			var1.println("overrideHeight:" + this.overrideHeight);
			var1.println("heldItemTooltips:" + this.heldItemTooltips);
			var1.println("chatHeightFocused:" + this.chatHeightFocused);
			var1.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
			var1.println("chatScale:" + this.chatScale);
			var1.println("chatWidth:" + this.chatWidth); 

			for (int var2 = 0; var2 < this.keyBindings.length; ++var2) {
				var1.println("key_" + this.keyBindings[var2].keyDescription + ":" + this.keyBindings[var2].keyCode);
			}
			// Spout Start
			for (int key = 0; key < this.spoutcraftBindings.length; ++key) {
				var1.println("key_" + this.spoutcraftBindings[key].keyDescription + ":" + this.spoutcraftBindings[key].keyCode);
			}
			// Spout End

			var1.close();
		} catch (Exception var3) {
			this.mc.getLogAgent().logWarning("Failed to save options");
			var3.printStackTrace();
		}
		this.sendSettingsToServer();
	}

	/**
	 * Send a client info packet with settings information to the server
	 */
	public void sendSettingsToServer() {
		if (this.mc.thePlayer != null) {
			this.mc.thePlayer.sendQueue.addToSendQueue(new Packet204ClientInfo(this.language, this.renderDistance, this.chatVisibility, this.chatColours, this.difficulty, this.showCape));
		}
	}

	/**
	 * Should render clouds
	 */
	public boolean shouldRenderClouds() {
		return this.renderDistance < 2 && this.clouds;
	}
}
