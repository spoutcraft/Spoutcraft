package net.minecraft.client;


import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;

import net.minecraft.src.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

import com.pclewis.mcpatcher.mod.Colorizer;
import com.pclewis.mcpatcher.mod.TextureUtils;

import org.bukkit.ChatColor;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.chunkcache.HeightMap;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.client.controls.SimpleKeyBindingManager;
import org.spoutcraft.client.gui.ScreenUtil;
import org.spoutcraft.client.gui.minimap.MinimapConfig;
import org.spoutcraft.client.gui.minimap.MinimapUtils;
import org.spoutcraft.client.gui.minimap.Waypoint;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.packet.PacketScreenAction;
import org.spoutcraft.client.packet.ScreenAction;
import org.spoutcraft.client.packet.SpoutPacket;
import org.spoutcraft.client.player.ClientPlayer;
import org.spoutcraft.client.spoutworth.SpoutWorth;
import org.spoutcraft.spoutcraftapi.addon.AddonLoadOrder;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.event.screen.ScreenCloseEvent;
import org.spoutcraft.spoutcraftapi.event.screen.ScreenEvent;
import org.spoutcraft.spoutcraftapi.event.screen.ScreenOpenEvent;
import org.spoutcraft.spoutcraftapi.gui.PopupScreen;
import org.spoutcraft.spoutcraftapi.gui.Screen;
import org.spoutcraft.spoutcraftapi.gui.ScreenType;

//Spout End

public abstract class Minecraft implements Runnable, IPlayerUsage {

	// public static byte[] field_71444_a = new byte[10485760]; //Spout unused
	private ServerData field_71422_O;

	public static Minecraft theMinecraft; // Spout private -> public
	public PlayerControllerMP field_71442_b;
	private boolean fullscreen = false;
	private boolean hasCrashed = false;
	private CrashReport field_71433_S;
	public int displayWidth;
	public int displayHeight;
	private Timer timer = new Timer(20.0F);
	private PlayerUsageSnooper field_71427_U = new PlayerUsageSnooper("client", this);
	public WorldClient field_71441_e;
	public RenderGlobal renderGlobal;
	public EntityClientPlayerMP field_71439_g;
	public EntityLiving renderViewEntity;
	public EffectRenderer effectRenderer;
	public Session session = null;
	public String minecraftUri;
	public Canvas mcCanvas;
	public boolean hideQuitButton = false;
	public volatile boolean isGamePaused = false;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
	public FontRenderer standardGalacticFontRenderer;
	public GuiScreen currentScreen = null;
	public LoadingScreenRenderer loadingScreen;
	public EntityRenderer entityRenderer;
	private ThreadDownloadResources downloadResourcesThread;
	private int leftClickCounter = 0;
	private int tempDisplayWidth;
	private int tempDisplayHeight;
	private IntegratedServer field_71437_Z;
	public GuiAchievement guiAchievement = new GuiAchievement(this);
	public GuiIngame ingameGUI;
	public boolean skipRenderWorld = false;
	public ModelBiped playerModelBiped = new ModelBiped(0.0F);
	public MovingObjectPosition objectMouseOver = null;
	public GameSettings gameSettings;
	protected MinecraftApplet mcApplet;
	public SoundManager sndManager = new SoundManager();
	public MouseHelper mouseHelper;
	public TexturePackList texturePackList;
	public File mcDataDir;
	private ISaveFormat saveLoader;
	private static int field_71470_ab;
	private int rightClickDelayTimer = 0;
	private boolean field_71468_ad;
	public StatFileWriter statFileWriter;
	private String serverName;
	private int serverPort;
	private TextureWaterFX textureWaterFX = new TextureWaterFX();
	private TextureLavaFX textureLavaFX = new TextureLavaFX();

	/**
	 * Makes sure it doesn't keep taking screenshots when both buttons are down.
	 */
	boolean isTakingScreenshot = false;

	/**
	 * Does the actual gameplay have focus. If so then mouse and keys will effect the player instead of menus.
	 */
	public boolean inGameHasFocus = false;
	long systemTime = func_71386_F();

	/** Join player counter */
	private int joinPlayerCounter = 0;
	private boolean field_71459_aj;
	private NetworkManager field_71453_ak;
	private boolean field_71455_al;
	public final Profiler field_71424_I = new Profiler();

	private static File minecraftDir = null;
	public volatile boolean running = true;
	public String debug = "";
	long debugUpdateTime = func_71386_F();
	int fpsCounter = 0;
	boolean isTakingScreenshot = false;
	long prevFrameTime = -1L;
	private String debugProfilerName = "root";
	public boolean inGameHasFocus = false;
	public boolean isRaining = false;
	long systemTime = System.currentTimeMillis();
	private int joinPlayerCounter = 0;
	// Spout Start
	public static Thread mainThread;
	private boolean shutdown = false;
	public static boolean spoutcraftLauncher = true;
	public static boolean portable = false;
	public static int framesPerSecond = 0;

	// Spout End

	public Minecraft(Canvas par1Canvas, MinecraftApplet par2MinecraftApplet, int par3, int par4, boolean par5) {
		StatList.func_75919_a();
		this.tempDisplayHeight = par4;
		this.fullscreen = par5;
		this.mcApplet = par2MinecraftApplet;
		Packet3Chat.maxChatLength = 32767;
		this.func_71389_H();
		this.mcCanvas = par1Canvas;
		this.displayWidth = par3;
		this.displayHeight = par4;
		this.fullscreen = par5;
		theMinecraft = this;

		// Spout Start
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				shutdownMinecraftApplet();
			}
		}));
		// Spout End
	}

	private void func_71389_H() {
		ThreadClientSleep var1 = new ThreadClientSleep(this, "Timer hack thread");
		var1.setDaemon(true);
		var1.start();
	}

	public void func_71404_a(CrashReport par1CrashReport) {
		this.hasCrashed = true;
		this.field_71433_S = par1CrashReport;
	}

	public void func_71377_b(CrashReport par1CrashReport) {
		this.hasCrashed = true;
		this.func_71406_c(par1CrashReport);
	}

	public abstract void func_71406_c(CrashReport var1);

	public void setServer(String par1Str, int par2) {
		this.serverName = par1Str;
		this.serverPort = par2;
	}

	public void startGame() throws LWJGLException {
		if (this.mcCanvas != null) {
			Graphics var1 = this.mcCanvas.getGraphics();
			if (var1 != null) {
				var1.setColor(Color.BLACK);
				var1.fillRect(0, 0, this.displayWidth, this.displayHeight);
				var1.dispose();
			}

			Display.setParent(this.mcCanvas);
		} else if (this.fullscreen) {
			Display.setFullscreen(true);
			this.displayWidth = Display.getDisplayMode().getWidth();
			this.displayHeight = Display.getDisplayMode().getHeight();
			if (this.displayWidth <= 0) {
				this.displayWidth = 1;
			}

			if (this.displayHeight <= 0) {
				this.displayHeight = 1;
			}
		} else {
			Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
		}

		Display.setTitle("Minecraft Minecraft 1.3.1");
		System.out.println("LWJGL Version: " + Sys.getVersion());

		try {
			Display.create((new PixelFormat()).withDepthBits(24));
		} catch (LWJGLException var5) {
			var5.printStackTrace();

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException var4) {
				;
			}

			Display.create();
		}

		OpenGlHelper.initializeTextures();
		this.mcDataDir = getMinecraftDir();
		this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
		this.gameSettings = new GameSettings(this, this.mcDataDir);
		this.texturePackList = new TexturePackList(this, this.mcDataDir, this);
		// Spout Start
		System.out.println("Launching Spoutcraft " + SpoutClient.getClientVersion());
		// Spout End
		this.renderEngine = new RenderEngine(this.texturePackList, this.gameSettings);
		// Spout Start
		TextureUtils.setTileSize();
		this.renderEngine.setTileSize(this);
		// Spout End
		//this.loadScreen();
		this.fontRenderer = new FontRenderer(this.gameSettings, "/font/default.png", this.renderEngine, false);
		this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, "/font/alternate.png", this.renderEngine, false);
		if (this.gameSettings.language != null) {
			StringTranslate.getInstance().setLanguage(this.gameSettings.language);
			this.fontRenderer.setUnicodeFlag(StringTranslate.getInstance().isUnicode());
			this.fontRenderer.setBidiFlag(StringTranslate.isBidirectional(this.gameSettings.language));
		}

		ColorizerWater.setWaterBiomeColorizer(this.renderEngine.getTextureContents("/misc/watercolor.png"));
		ColorizerGrass.setGrassBiomeColorizer(this.renderEngine.getTextureContents("/misc/grasscolor.png"));
		ColorizerFoliage.getFoilageBiomeColorizer(this.renderEngine.getTextureContents("/misc/foliagecolor.png"));
		this.entityRenderer = new EntityRenderer(this);
		RenderManager.instance.itemRenderer = new ItemRenderer(this);
		this.statFileWriter = new StatFileWriter(this.session, this.mcDataDir);
		AchievementList.openInventory.setStatStringFormatter(new StatStringFormatKeyInv(this));
		this.loadScreen();
		Keyboard.create(); // Spout 
		Mouse.create();
		this.mouseHelper = new MouseHelper(this.mcCanvas);

		try {
			Controllers.create();
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		func_52004_D();
		this.checkGLError("Pre startup");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		this.checkGLError("Startup");
		this.glCapabilities = new OpenGlCapsChecker();
		this.sndManager.loadSoundSettings(this.gameSettings);
		this.renderGlobal = new RenderGlobal(this, this.renderEngine);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		this.effectRenderer = new EffectRenderer(this.field_71441_e, this.renderEngine);

		try {
			this.downloadResourcesThread = new ThreadDownloadResources(this.mcDataDir, this);
			this.downloadResourcesThread.start();
		} catch (Exception var3) {
			;
		}

		this.checkGLError("Post startup");
		this.ingameGUI = new GuiIngame(this);
		if (this.serverName != null) {
			this.displayGuiScreen(new GuiConnecting(this, this.serverName, this.serverPort));
		} else {
			this.displayGuiScreen(new org.spoutcraft.client.gui.mainmenu.MainMenu()); //Spout
		}

		this.loadingScreen = new LoadingScreenRenderer(this);

		if (this.gameSettings.field_74353_u && !this.fullscreen) {
			this.toggleFullscreen();
		}

		// Spout Start
		SpoutClient.getInstance().loadAddons();
		SpoutClient.getInstance().enableAddons(AddonLoadOrder.GAMESTART);
		// Spout End
	}

	private void loadScreen() throws LWJGLException {
		ScaledResolution var1 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
		GL11.glClear(16640);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, var1.func_78327_c(), var1.func_78324_d(), 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		Tessellator var2 = Tessellator.instance;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/title/mojang.png"));
		var2.startDrawingQuads();
		var2.setColorOpaque_I(16777215);
		var2.addVertexWithUV(0.0D, (double) this.displayHeight, 0.0D, 0.0D, 0.0D);
		var2.addVertexWithUV((double) this.displayWidth, (double) this.displayHeight, 0.0D, 0.0D, 0.0D);
		var2.addVertexWithUV((double) this.displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
		var2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		var2.draw();
		short var3 = 256;
		short var4 = 256;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var2.setColorOpaque_I(16777215);
		this.scaledTessellator((var1.getScaledWidth() - var3) / 2, (var1.getScaledHeight() - var4) / 2, 0, 0, var3, var4);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		Display.swapBuffers();
	}

	public void scaledTessellator(int par1, int par2, int par3, int par4, int par5, int par6) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV((double) (par1 + 0), (double) (par2 + par6), 0.0D, (double) ((float) (par3 + 0) * var7), (double) ((float) (par4 + par6) * var8));
		var9.addVertexWithUV((double) (par1 + par5), (double) (par2 + par6), 0.0D, (double) ((float) (par3 + par5) * var7), (double) ((float) (par4 + par6) * var8));
		var9.addVertexWithUV((double) (par1 + par5), (double) (par2 + 0), 0.0D, (double) ((float) (par3 + par5) * var7), (double) ((float) (par4 + 0) * var8));
		var9.addVertexWithUV((double) (par1 + 0), (double) (par2 + 0), 0.0D, (double) ((float) (par3 + 0) * var7), (double) ((float) (par4 + 0) * var8));
		var9.draw();
	}

	public static File getMinecraftDir() {
		if (minecraftDir == null) {
			// Spout Start
			String workingDirName = "minecraft";
			if (spoutcraftLauncher)
				workingDirName = "spoutcraft";
			if (portable) {
				File portableDir = new File(workingDirName);
				if (portableDir.exists() || portableDir.mkdirs()) {
					minecraftDir = portableDir;
				} else {
					throw new RuntimeException("The working directory could not be created: " + portableDir);
				}
			} else {
				minecraftDir = getAppDir(workingDirName);
			}
			// Spout End
		}

		return minecraftDir;
	}

	public static File getAppDir(String par0Str) {
		String var1 = System.getProperty("user.home", ".");
		File var2;
		switch (EnumOSMappingHelper.field_74533_a[getOs().ordinal()]) {
			case 1:
			case 2:
				var2 = new File(var1, '.' + par0Str + '/');
				break;
			case 3:
				String var3 = System.getenv("APPDATA");
				if (var3 != null) {
					var2 = new File(var3, "." + par0Str + '/');
				} else {
					var2 = new File(var1, '.' + par0Str + '/');
				}
				break;
			case 4:
				var2 = new File(var1, "Library/Application Support/" + par0Str);
				break;
			default:
				var2 = new File(var1, par0Str + '/');
		}

		if (!var2.exists() && !var2.mkdirs()) {
			throw new RuntimeException("The working directory could not be created: " + var2);
		} else {
			return var2;
		}
	}

	public static EnumOS2 getOs() {
		String var0 = System.getProperty("os.name").toLowerCase();
		return var0.contains("win") ? EnumOS2.windows : (var0.contains("mac") ? EnumOS2.macos : (var0.contains("solaris") ? EnumOS2.solaris : (var0.contains("sunos") ? EnumOS2.solaris : (var0.contains("linux") ? EnumOS2.linux : (var0.contains("unix") ? EnumOS2.linux : EnumOS2.unknown)))));
	}

	public ISaveFormat getSaveLoader() {
		return this.saveLoader;
	}

	// Spout Start
	private GuiScreen previousScreen = null;

	public void displayGuiScreen(GuiScreen screen) {
		displayGuiScreen(screen, true);
	}

	public void displayGuiScreen(GuiScreen screen, boolean notify) {
		// Part of Original function
		if (screen == null && this.field_71441_e == null) {
			screen = new org.spoutcraft.client.gui.mainmenu.MainMenu();
		} else if (screen == null && this.field_71439_g.health <= 0) {
			screen = new GuiGameOver();
		}
		
		//Reset Gui
		if (screen instanceof org.spoutcraft.client.gui.mainmenu.MainMenu || screen instanceof org.spoutcraft.client.gui.server.GuiFavorites || screen instanceof org.spoutcraft.client.gui.error.GuiConnectionLost) {
			ClientPlayer.getInstance().resetMainScreen();
			SpoutClient.getInstance().setSpoutActive(false);
		}

		ScreenType display = ScreenUtil.getType(screen);

		if (notify && field_71439_g != null && field_71441_e != null) {
			// Screen closed
			ScreenEvent event = null;
			SpoutPacket packet = null;
			Screen widget = null;
			if (this.currentScreen != null && screen == null) {
				packet = new PacketScreenAction(ScreenAction.Close, ScreenUtil.getType(this.currentScreen));
				event = ScreenCloseEvent.getInstance((Player) field_71439_g.spoutEntity, currentScreen.getScreen(), display);
				widget = currentScreen.getScreen();
			}
			// Screen opened
			if (screen != null && this.currentScreen == null) {
				packet = new PacketScreenAction(ScreenAction.Open, display);
				event = ScreenOpenEvent.getInstance((Player) field_71439_g.spoutEntity, screen.getScreen(), display);
				widget = screen.getScreen();
			}
			// Screen swapped
			if (screen != null && this.currentScreen != null) { // Hopefully just a submenu
				packet = new PacketScreenAction(ScreenAction.Open, display);
				event = ScreenOpenEvent.getInstance((Player) field_71439_g.spoutEntity, screen.getScreen(), display);
				widget = screen.getScreen();
			}
			boolean cancel = false;
			if (event != null) {
				SpoutClient.getInstance().getAddonManager().callEvent(event);
				cancel = event.isCancelled();
			}
			if (!cancel && packet != null) {
				SpoutClient.getInstance().getPacketManager().sendSpoutPacket(packet);
				if (widget instanceof PopupScreen) {
					((PopupScreen) widget).close();
				}
			}
			if (cancel) {
				return;
			}
		}
		if (!(this.currentScreen instanceof GuiErrorScreen)) {
			if (this.currentScreen != null) {
				this.currentScreen.onGuiClosed();
			}

			this.statFileWriter.syncStats();

			boolean wasSandboxed = SpoutClient.isSandboxed();
			if (wasSandboxed) {
				SpoutClient.disableSandbox();
			}
			this.statFileWriter.syncStats();
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}

			if (field_71441_e == null && field_71439_g == null && this.ingameGUI != null) {
				this.ingameGUI.clearChatMessages();
			}

			if (previousScreen != null || screen != null) {
				previousScreen = this.currentScreen;
			}

			this.currentScreen = screen;

			if (screen != null) {
				this.setIngameNotInFocus();
				ScaledResolution var2 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
				int var3 = var2.getScaledWidth();
				int var4 = var2.getScaledHeight();
				screen.setWorldAndResolution(this, var3, var4);
				this.skipRenderWorld = false;
			} else {
				this.setIngameFocus();
			}
		}
	}

	public void displayPreviousScreen() {
		displayGuiScreen(previousScreen, false);
		previousScreen = null;
	}

	public void clearPreviousScreen() {
		previousScreen = null;
	}
	// Spout End

	private void checkGLError(String par1Str) {
		int var2 = GL11.glGetError();
		if (var2 != 0) {
			String var3 = GLU.gluErrorString(var2);
			
			//Spout start
			System.out.println("-----------------A GL Error has Occured!-----------------");
			System.out.println("OpenGL Information");
			System.out.println("    Vendor: " + GL11.glGetString(GL11.GL_VENDOR));
			System.out.println("    OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
			System.out.println("    GLSL Version: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
			System.out.println("    Max Texture Units: " + GL11.glGetInteger(GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS));
			
			System.out.println("Error at " + par1Str);
			System.out.println(var2 + ": " + var3);
			
			System.out.println("Spoutcraft Configuration Information");
			try {
				for (Field f : ConfigReader.class.getFields()) {
					System.out.println("    " + f.getName() + " : " + f.get(null));
				}
			} catch (Exception ignore) { }
			throw new RuntimeException("OpenGL Error occured!");
			//Spout end
		}
	}

	public void shutdownMinecraftApplet() {
		// Spout Start
		if (shutdown) {
			return;
		}
		SpoutClient.getInstance().disableAddons();
		shutdown = true;
		SpoutClient.disableSandbox();
		// Spout End
		try {
			this.statFileWriter.syncStats();
			if (this.mcApplet != null) {
				this.mcApplet.clearApplet();
			}

			try {
				if (this.downloadResourcesThread != null) {
					this.downloadResourcesThread.closeMinecraft();
				}
			} catch (Exception var9) {
				;
			}

			System.out.println("Stopping!");

			try {
				this.func_71403_a((WorldClient) null);
			} catch (Throwable var8) {
				;
			}

			try {
				GLAllocation.deleteTexturesAndDisplayLists();
			} catch (Throwable var7) {
				;
			}

			this.sndManager.closeMinecraft();
			Mouse.destroy();
			Keyboard.destroy();
		} finally {
			Display.destroy();
			if (!this.hasCrashed) {
				System.exit(0);
			}
		}

		System.gc();
	}

	public void run() {
		this.running = true;

		try {
			this.startGame();
		} catch (Exception var11) {
			var11.printStackTrace();
			this.func_71377_b(this.func_71396_d(new CrashReport("Failed to start game", var11)));
			return;
		}

		try {
			while (this.running) {
				if (this.hasCrashed && this.field_71433_S != null) {
					this.func_71377_b(this.field_71433_S);
					return;
				}

				if (this.field_71468_ad) {
					this.field_71468_ad = false;
					this.renderEngine.refreshTextures();
				}

				try {
					this.runGameLoop();
				} catch (MinecraftException var9) {
					this.theWorld = null;
					this.changeWorld1((World) null);
					this.displayGuiScreen(new GuiConflictWarning());
				} catch (OutOfMemoryError var10) {
					this.freeMemory();
					this.displayGuiScreen(new GuiMemoryErrorScreen());
					System.gc();
				}
				// Spout start
				catch (Throwable t) {
					// try to handle errors gracefuly
					try {
						if (SpoutClient.isSandboxed()) {
							SpoutClient.disableSandbox();
						}
						this.field_71441_e = null;
						this.changeWorld1((World) null);

						this.displayGuiScreen(new org.spoutcraft.client.gui.error.GuiUnexpectedError());

						t.printStackTrace();
					} catch (Throwable failed) {
						SpoutClient.disableSandbox();
						failed.printStackTrace();
						throw new RuntimeException(t);
					}
				}
			}
			// Spout end
		} catch (MinecraftError var12) {
			;
		} catch (ReportedException var13) {
			this.func_71396_d(var13.func_71575_a());
			this.freeMemory();
			var13.printStackTrace();
			this.func_71377_b(var13.func_71575_a());
		} catch  (Throwable var13) {
			CrashReport var2 = this.func_71396_d(new CrashReport("Unexpected error", var14));
			this.freeMemory();
			var14.printStackTrace();
			this.func_71377_b(var2);
		} finally {
			//Spout Start
			if(field_71441_e != null) {
				HeightMap map = HeightMap.getHeightMap(MinimapUtils.getWorldName());
				if(map.isDirty()) {
					map.saveThreaded();
				}
			}
			HeightMap.joinSaveThread();
			//Spout End
			this.shutdownMinecraftApplet();
		}
	}

	private void runGameLoop() {
		//Spout start
		Colorizer.setupBlockAccess(this.field_71441_e, true);
		mainThread = Thread.currentThread();
		if (sndManager != null) {
			sndManager.tick();
		}
		//Spout end

		if (this.mcApplet != null && !this.mcApplet.isActive()) {
			this.running = false;
		} else {
			// Spout start
			if (field_71441_e == null) {
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {

				}
			}
			// Spout end
			AxisAlignedBB.func_72332_a().func_72298_a();
			Vec3.func_72437_a().func_72343_a();
			this.field_71424_I.startSection("root");

			if (this.mcCanvas == null && Display.isCloseRequested()) {
				this.shutdown();
			}

			if (this.isGamePaused && this.field_71441_e != null) {
				float var1 = this.timer.renderPartialTicks;
				this.timer.updateTimer();
				this.timer.renderPartialTicks = var1;
			} else {
				this.timer.updateTimer();
			}

			long var6 = System.nanoTime();
			this.field_71424_I.startSection("tick");

			for (int var3 = 0; var3 < this.timer.elapsedTicks; ++var3) {
				++this.ticksRan;

				try {
					this.runTick();
				} catch (MinecraftException var5) {
					this.theWorld = null;
					this.changeWorld1((World) null);
					this.displayGuiScreen(new GuiConflictWarning());
				}
			}

			this.field_71424_I.endStartSection("preRenderErrors");
			long var7 = System.nanoTime() - var6;
			this.checkGLError("Pre render");
			//RenderBlocks.fancyGrass = this.gameSettings.fancyGraphics; // Spout removed
			this.field_71424_I.startSection("sound");
			this.sndManager.setListener(this.field_71439_g, this.timer.renderPartialTicks);
			this.field_71424_I.endStartSection("updatelights");
			if (this.field_71441_e != null) {
				this.field_71441_e.updatingLighting();
			}

			this.field_71424_I.endSection();
			this.field_71424_I.startSection("render");
			this.field_71424_I.startSection("display");
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			if (!Keyboard.isKeyDown(65)) {
				Display.update();
			}

			if (this.field_71439_g != null && this.field_71439_g.isEntityInsideOpaqueBlock()) {
				this.gameSettings.thirdPersonView = 0;
			}

			this.field_71424_I.endSection();
			if (!this.skipRenderWorld) {
				this.field_71424_I.endStartSection("gameRenderer");
				this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
				this.field_71424_I.endSection();
			}

			GL11.glFlush();
			this.field_71424_I.endSection();
			if (!Display.isActive() && this.fullscreen) {
				this.toggleFullscreen();
			}

			if (this.gameSettings.showDebugInfo && this.gameSettings.field_74329_Q) {
				if (!this.field_71424_I.profilingEnabled) {
					this.field_71424_I.clearProfiling();
				}

				this.field_71424_I.profilingEnabled = true;
				this.displayDebugInfo(var7);
			} else {
				this.field_71424_I.profilingEnabled = false;
				this.prevFrameTime = System.nanoTime();
			}

			this.guiAchievement.updateAchievementWindow();
			this.field_71424_I.startSection("root");
			Thread.yield();
			if (Keyboard.isKeyDown(65)) {
				Display.update();
			}

			this.screenshotListener();
			if (this.mcCanvas != null && !this.fullscreen && (this.mcCanvas.getWidth() != this.displayWidth || this.mcCanvas.getHeight() != this.displayHeight)) {
				this.displayWidth = this.mcCanvas.getWidth();
				this.displayHeight = this.mcCanvas.getHeight();
				if (this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if (this.displayHeight <= 0) {
					this.displayHeight = 1;
				}

				this.resize(this.displayWidth, this.displayHeight);
			}

			this.checkGLError("Post render");
			++this.fpsCounter;
			boolean var5 = this.isGamePaused;
			this.isGamePaused = this.func_71356_B() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame() && !this.field_71437_Z.func_71344_c();

			if (this.func_71387_A() && this.field_71439_g != null && this.field_71439_g.sendQueue != null && this.isGamePaused != var5) {
				((MemoryConnection)this.field_71439_g.sendQueue.func_72548_f()).func_74437_a(this.isGamePaused);
			}

			while (func_71386_F() >= this.debugUpdateTime + 1000L) {
				field_71470_ab = this.fpsCounter;
				this.debug = field_71470_ab + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
				WorldRenderer.chunksUpdated = 0;
				this.debugUpdateTime += 1000L;
				// Spout start
				framesPerSecond = fpsCounter;
				SpoutWorth.getInstance().updateFPS(framesPerSecond);
				// Spout end
				this.fpsCounter = 0;
				this.field_71427_U.func_76471_b();

				if (!this.field_71427_U.func_76468_d()) {
					this.field_71427_U.func_76463_a();
				}
			}

			this.field_71424_I.endSection();

			if (this.gameSettings.limitFramerate > 0) {
				EntityRenderer var10000 = this.entityRenderer;
				Display.sync(EntityRenderer.func_78465_a(this.gameSettings.limitFramerate));
			}
		}
	}

	public void freeMemory() {
		try {
			// field_71444_a = new byte[0]; // Spout removed
			this.renderGlobal.func_72728_f();
		} catch (Throwable var4) {
			;
		}

		try {
			System.gc();
			AxisAlignedBB.func_72332_a().func_72300_b();
			Vec3.func_72437_a().func_72344_b();
		} catch (Throwable var3) {
			;
		}

		try {
			System.gc();
			this.func_71403_a((WorldClient) null);
		} catch (Throwable var2) {
			;
		}

		System.gc();
	}

	private void screenshotListener() {
		if (Keyboard.isKeyDown(60)) {
			if (!this.isTakingScreenshot) {
				this.isTakingScreenshot = true;
				if(theWorld != null) this.ingameGUI.func_73827_b().func_73765_a(ScreenShotHelper.saveScreenshot(minecraftDir, this.displayWidth, this.displayHeight)); // Spout - Null check
			}
		} else {
			this.isTakingScreenshot = false;
		}
	}

	private void updateDebugProfilerName(int par1) {
		List var2 = this.field_71424_I.getProfilingData(this.debugProfilerName);
		if (var2 != null && !var2.isEmpty()) {
			ProfilerResult var3 = (ProfilerResult) var2.remove(0);
			if (par1 == 0) {
				if (var3.field_76331_c.length() > 0) {
					int var4 = this.debugProfilerName.lastIndexOf(".");
					if (var4 >= 0) {
						this.debugProfilerName = this.debugProfilerName.substring(0, var4);
					}
				}
			} else {
				--par1;
				if (par1 < var2.size() && !((ProfilerResult) var2.get(par1)).field_76331_c.equals("unspecified")) {
					if (this.debugProfilerName.length() > 0) {
						this.debugProfilerName = this.debugProfilerName + ".";
					}

					this.debugProfilerName = this.debugProfilerName + ((ProfilerResult) var2.get(par1)).field_76331_c;
				}
			}
		}
	}

	private void displayDebugInfo(long par1) {
		// Spout start
		// Only show if no other screens are up
		if (currentScreen != null) {
			return;
		}
		// Spout end
		if (this.field_71424_I.profilingEnabled) {
			List var3 = this.field_71424_I.getProfilingData(this.debugProfilerName);
			ProfilerResult var4 = (ProfilerResult)var3.remove(0);
			GL11.glClear(256);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, (double)this.displayWidth, (double)this.displayHeight, 0.0D, 1000.0D, 3000.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
			GL11.glLineWidth(1.0F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			Tessellator var5 = Tessellator.instance;
			short var6 = 160;
			int var7 = this.displayWidth - var6 - 10;
			int var8 = this.displayHeight - var6 * 2;
			GL11.glEnable(GL11.GL_BLEND);
			var5.startDrawingQuads();
			var5.setColorRGBA_I(0, 200);
			var5.addVertex((double)((float)var7 - (float)var6 * 1.1F), (double)((float)var8 - (float)var6 * 0.6F - 16.0F), 0.0D);
			var5.addVertex((double)((float)var7 - (float)var6 * 1.1F), (double)(var8 + var6 * 2), 0.0D);
			var5.addVertex((double)((float)var7 + (float)var6 * 1.1F), (double)(var8 + var6 * 2), 0.0D);
			var5.addVertex((double)((float)var7 + (float)var6 * 1.1F), (double)((float)var8 - (float)var6 * 0.6F - 16.0F), 0.0D);
			var5.draw();
			GL11.glDisable(GL11.GL_BLEND);
			double var9 = 0.0D;
			int var13;

			for (int var11 = 0; var11 < var3.size(); ++var11) {
				ProfilerResult var12 = (ProfilerResult)var3.get(var11);
				var13 = MathHelper.floor_double(var12.field_76332_a / 4.0D) + 1;
				var5.startDrawing(6);
				var5.setColorOpaque_I(var12.func_76329_a());
				var5.addVertex((double)var7, (double)var8, 0.0D);
				int var14;
				float var15;
				float var17;
				float var16;

				for (var14 = var13; var14 >= 0; --var14) {
					var15 = (float)((var9 + var12.field_76332_a * (double)var14 / (double)var13) * Math.PI * 2.0D / 100.0D);
					var16 = MathHelper.sin(var15) * (float)var6;
					var17 = MathHelper.cos(var15) * (float)var6 * 0.5F;
					var5.addVertex((double)((float)var7 + var16), (double)((float)var8 - var17), 0.0D);
				}

				var5.draw();
				var5.startDrawing(5);
				var5.setColorOpaque_I((var12.func_76329_a() & 16711422) >> 1);

				for (var14 = var13; var14 >= 0; --var14) {
					var15 = (float)((var9 + var12.field_76332_a * (double)var14 / (double)var13) * Math.PI * 2.0D / 100.0D);
					var16 = MathHelper.sin(var15) * (float)var6;
					var17 = MathHelper.cos(var15) * (float)var6 * 0.5F;
					var5.addVertex((double)((float)var7 + var16), (double)((float)var8 - var17), 0.0D);
					var5.addVertex((double)((float)var7 + var16), (double)((float)var8 - var17 + 10.0F), 0.0D);
				}

				var5.draw();
				var9 += var12.field_76332_a;
			}

			DecimalFormat var19 = new DecimalFormat("##0.00");
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			String var18 = "";

			if (!var4.field_76331_c.equals("unspecified")) {
				var18 = var18 + "[0] ";
			}

			if (var4.field_76331_c.length() == 0) {
				var18 = var18 + "ROOT ";
			} else {
				var18 = var18 + var4.field_76331_c + " ";
			}

			var13 = 16777215;
			this.fontRenderer.drawStringWithShadow(var18, var7 - var6, var8 - var6 / 2 - 16, var13);
			this.fontRenderer.drawStringWithShadow(var18 = var19.format(var4.field_76330_b) + "%", var7 + var6 - this.fontRenderer.getStringWidth(var18), var8 - var6 / 2 - 16, var13);

			for (int var21 = 0; var21 < var3.size(); ++var21) {
				ProfilerResult var20 = (ProfilerResult)var3.get(var21);
				String var22 = "";

				if (var20.field_76331_c.equals("unspecified")) {
					var22 = var22 + "[?] ";
				} else {
					var22 = var22 + "[" + (var21 + 1) + "] ";
				}

				var22 = var22 + var20.field_76331_c;
				this.fontRenderer.drawStringWithShadow(var22, var7 - var6, var8 + var6 / 2 + var21 * 8 + 20, var20.func_76329_a());
				this.fontRenderer.drawStringWithShadow(var22 = var19.format(var20.field_76332_a) + "%", var7 + var6 - 50 - this.fontRenderer.getStringWidth(var22), var8 + var6 / 2 + var21 * 8 + 20, var20.func_76329_a());
				this.fontRenderer.drawStringWithShadow(var22 = var19.format(var20.field_76330_b) + "%", var7 + var6 - this.fontRenderer.getStringWidth(var22), var8 + var6 / 2 + var21 * 8 + 20, var20.func_76329_a());
			}
		}
	}

	public void shutdown() {
		this.running = false;
	}

	// Spout Start
	public void setIngameFocus() {
		setIngameFocus(true);
	}

	public void setIngameFocus(boolean close) {
		if (Display.isActive()) {
			if (!this.inGameHasFocus) {
				this.inGameHasFocus = true;
				this.mouseHelper.grabMouseCursor();
				if (close) {
					this.displayGuiScreen((GuiScreen) null);
				}
				this.leftClickCounter = 10000;
			}
		}
	}

	// Spout End

	public void setIngameNotInFocus() {
		if (this.inGameHasFocus) {
			KeyBinding.unPressAllKeys();
			this.inGameHasFocus = false;
			this.mouseHelper.ungrabMouseCursor();
		}
	}

	public void displayInGameMenu() {
		if (this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

	private void sendClickBlockToController(int par1, boolean par2) {
		if (!par2) {
			this.leftClickCounter = 0;
		}

		if (par1 != 0 || this.leftClickCounter <= 0) {
			if (par2 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE && par1 == 0) {
				int var3 = this.objectMouseOver.blockX;
				int var4 = this.objectMouseOver.blockY;
				int var5 = this.objectMouseOver.blockZ;
				this.field_71442_b.onPlayerDamageBlock(var3, var4, var5, this.objectMouseOver.sideHit);
				if (this.field_71439_g.canPlayerEdit(var3, var4, var5)) {
					this.effectRenderer.addBlockHitEffects(var3, var4, var5, this.objectMouseOver.sideHit);
					this.field_71439_g.swingItem();
				}
			} else {
				this.field_71442_b.resetBlockRemoving();
			}
		}
	}

	private void clickMouse(int par1) {
		if (par1 != 0 || this.leftClickCounter <= 0) {
			if (par1 == 0) {
				this.field_71439_g.swingItem();
			}

			if (par1 == 1) {
				this.rightClickDelayTimer = 4;
			}

			boolean var2 = true;
			ItemStack var3 = this.field_71439_g.inventory.getCurrentItem();
			if (this.objectMouseOver == null) {
				if (par1 == 0 && this.field_71442_b.isNotCreative()) {
					this.leftClickCounter = 10;
				}
			} else if (this.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY) {
				if (par1 == 0) {
					this.field_71442_b.attackEntity(this.field_71439_g, this.objectMouseOver.entityHit);
				}

				if (par1 == 1 && this.field_71442_b.func_78768_b(this.field_71439_g, this.objectMouseOver.entityHit)) {
					var2 = false;
				}
			} else if (this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
				int var4 = this.objectMouseOver.blockX;
				int var5 = this.objectMouseOver.blockY;
				int var6 = this.objectMouseOver.blockZ;
				int var7 = this.objectMouseOver.sideHit;
				if (par1 == 0) {
					this.field_71442_b.clickBlock(var4, var5, var6, this.objectMouseOver.sideHit);
				} else {
					int var8 = var3 != null ? var3.stackSize : 0;

					if (this.field_71442_b.func_78760_a(this.field_71439_g, this.field_71441_e, var3, var4, var5, var6, var7, this.objectMouseOver.hitVec)) {
						var2 = false;
						this.field_71439_g.swingItem();
					}

					if (var3 == null) {
						return;
					}

					if (var3.stackSize == 0) {
						this.field_71439_g.inventory.mainInventory[this.field_71439_g.inventory.currentItem] = null;
					} else if (var3.stackSize != var8 || this.field_71442_b.isInCreativeMode()) {
						this.entityRenderer.itemRenderer.func_78444_b();
					}
				}
			}

			if (var2 && par1 == 1) {
				ItemStack var9 = this.field_71439_g.inventory.getCurrentItem();
				if (var9 != null && this.field_71442_b.sendUseItem(this.field_71439_g, this.field_71441_e, var9)) {
					this.entityRenderer.itemRenderer.func_9450_c();
				}
			}
		}
	}

	public void toggleFullscreen() {
		try {
			this.fullscreen = !this.fullscreen;
			if (this.fullscreen) {
				Display.setDisplayMode(Display.getDesktopDisplayMode());
				this.displayWidth = Display.getDisplayMode().getWidth();
				this.displayHeight = Display.getDisplayMode().getHeight();
				if (this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if (this.displayHeight <= 0) {
					this.displayHeight = 1;
				}
			} else {
				if (this.mcCanvas != null) {
					this.displayWidth = this.mcCanvas.getWidth();
					this.displayHeight = this.mcCanvas.getHeight();
				} else {
					this.displayWidth = this.tempDisplayWidth;
					this.displayHeight = this.tempDisplayHeight;
				}

				if (this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if (this.displayHeight <= 0) {
					this.displayHeight = 1;
				}
			}

			if (this.currentScreen != null) {
				this.resize(this.displayWidth, this.displayHeight);
			}

			Display.setFullscreen(this.fullscreen);
			Display.setVSyncEnabled(this.gameSettings.field_74352_v);
			Display.update();
		} catch (Exception var2) {
			var2.printStackTrace();
		}
	}

	public void resize(int par1, int par2) { // spout -> private to public
		this.displayWidth = par1 <= 0 ? 1 : par1;
		this.displayHeight = par2 <= 0 ? 1 : par2;
		if (par1 <= 0) {
			par1 = 1;
		}

		if (par2 <= 0) {
			par2 = 1;
		}

		this.displayWidth = par1;
		this.displayHeight = par2;
		if (this.currentScreen != null) {
			ScaledResolution var3 = new ScaledResolution(this.gameSettings, par1, par2);
			int var4 = var3.getScaledWidth();
			int var5 = var3.getScaledHeight();
			this.currentScreen.setWorldAndResolution(this, var4, var5);
		}
	}

	private void startThreadCheckHasPaid() {
		(new ThreadCheckHasPaid(this)).start();
	}

	public void runTick() {
		if (this.rightClickDelayTimer > 0) {
			--this.rightClickDelayTimer;
		}

		if (this.ticksRan == 6000) {
			this.startThreadCheckHasPaid();
		}

		this.field_71424_I.startSection("stats");
		this.statFileWriter.func_77449_e();
		this.field_71424_I.endStartSection("gui");
		if (!this.isGamePaused) {
			this.ingameGUI.updateTick();
		}

		this.field_71424_I.endStartSection("pick");
		this.entityRenderer.getMouseOver(1.0F);

		this.field_71424_I.endStartSection("gameMode");

		if (!this.isGamePaused && this.field_71441_e != null) {
			this.field_71442_b.updateController();
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
		this.field_71424_I.endStartSection("textures");
		if (!this.isGamePaused) {
			this.renderEngine.updateDynamicTextures();
		}

		if (this.currentScreen == null && this.field_71439_g != null) {
			if (this.field_71439_g.getHealth() <= 0) {
				this.displayGuiScreen((GuiScreen) null);
			} else if (this.field_71439_g.isPlayerSleeping() && this.field_71441_e != null && this.field_71441_e.isRemote) {
				this.displayGuiScreen(new GuiSleepMP());
			}
		} else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.field_71439_g.isPlayerSleeping()) {
			this.displayGuiScreen((GuiScreen) null);
		}

		if (this.currentScreen != null) {
			this.leftClickCounter = 10000;
		}

		if (this.currentScreen != null) {
			this.currentScreen.handleInput();
			if (this.currentScreen != null) {
				this.currentScreen.guiParticles.update();
				this.currentScreen.updateScreen();
			}
		}

		if (this.currentScreen == null || this.currentScreen.allowUserInput) {
			this.field_71424_I.endStartSection("mouse");

			while (Mouse.next()) {
				// Spout Start
				if(!(Mouse.getEventButton()>2))
					((SimpleKeyBindingManager) SpoutClient.getInstance().getKeyBindingManager()).pressKey(Mouse.getEventButton()-100, Mouse.getEventButtonState(), ScreenUtil.getType(currentScreen).getCode());
				// Spout End
				KeyBinding.setKeyBindState(Mouse.getEventButton() - 100, Mouse.getEventButtonState());
				if (Mouse.getEventButtonState()) {
					KeyBinding.onTick(Mouse.getEventButton() - 100);
				}

				long var1 = func_71386_F() - this.systemTime;
				if (var1 <= 200L) {
					int var3 = Mouse.getEventDWheel();
					if (var3 != 0) {
						this.field_71439_g.inventory.changeCurrentItem(var3);
						if (this.gameSettings.noclip) {
							if (var3 > 0) {
								var3 = 1;
							}

							if (var3 < 0) {
								var3 = -1;
							}

							this.gameSettings.noclipRate += (float) var3 * 0.25F;
						}
					}

					if (this.currentScreen == null) {
						if (!this.inGameHasFocus && Mouse.getEventButtonState()) {
							this.setIngameFocus();
						}
					} else if (this.currentScreen != null) {
						this.currentScreen.handleMouseInput();
					}
				}
			}

			if (this.leftClickCounter > 0) {
				--this.leftClickCounter;
			}

			this.field_71424_I.endStartSection("keyboard");
			boolean var4;

			while (Keyboard.next()) {
				// Spout Start
				((SimpleKeyBindingManager) SpoutClient.getInstance().getKeyBindingManager()).pressKey(Keyboard.getEventKey(), Keyboard.getEventKeyState(), ScreenUtil.getType(currentScreen).getCode());
				this.field_71439_g.handleKeyPress(Keyboard.getEventKey(), Keyboard.getEventKeyState()); // Spout handle key press
				// Spout End

				KeyBinding.setKeyBindState(Keyboard.getEventKey(), Keyboard.getEventKeyState());
				if (Keyboard.getEventKeyState()) {
					KeyBinding.onTick(Keyboard.getEventKey());
				}

				if (Keyboard.getEventKeyState()) {
					if (Keyboard.getEventKey() == 87) {
						this.toggleFullscreen();
					} else {
						if (this.currentScreen != null) {
							this.currentScreen.handleKeyboardInput();
						} else {
							if (Keyboard.getEventKey() == 1) {
								this.displayInGameMenu();
							}

							if (Keyboard.getEventKey() == 31 && Keyboard.isKeyDown(61)) {
								this.forceReload();
							}

							if (Keyboard.getEventKey() == 20 && Keyboard.isKeyDown(61)) {
								this.renderEngine.refreshTextures();
							}

							if (Keyboard.getEventKey() == 33 && Keyboard.isKeyDown(61)) {
								var4 = Keyboard.isKeyDown(42) | Keyboard.isKeyDown(54);
								this.gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, var4 ? -1 : 1);
							}

							if (Keyboard.getEventKey() == 30 && Keyboard.isKeyDown(61)) {
								this.renderGlobal.loadRenderers();
							}

							if (Keyboard.getEventKey() == 59) {
								this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
							}

							if (Keyboard.getEventKey() == 61) {
								this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
								this.gameSettings.field_74329_Q = !GuiScreen.isShiftKeyDown();
							}

							if (Keyboard.getEventKey() == 63) {
								++this.gameSettings.thirdPersonView;
								if (this.gameSettings.thirdPersonView > 2) {
									this.gameSettings.thirdPersonView = 0;
								}
							}

							if (Keyboard.getEventKey() == 66) {
								this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
							}
						}

						int var5;
						//Spout start
						if (ConfigReader.hotbarQuickKeysEnabled) { 
							for (var5 = 0; var5 < 9; ++var5) {
								if (Keyboard.getEventKey() == 2 + var5) {
									this.field_71439_g.inventory.currentItem = var5;
								}
							}
						}
						//Spout end

						if (this.gameSettings.showDebugInfo && this.gameSettings.field_74329_Q) {
							if (Keyboard.getEventKey() == 11) {
								this.updateDebugProfilerName(0);
							}

							for (var5 = 0; var5 < 9; ++var5) {
								if (Keyboard.getEventKey() == 2 + var5) {
									this.updateDebugProfilerName(var5 + 1);
								}
							}
						}
					}
				}
			}

			var4 = this.gameSettings.field_74343_n != 2;

			while (this.gameSettings.keyBindInventory.isPressed()) {
				this.displayGuiScreen(new GuiInventory(this.field_71439_g));
			}

			while (this.gameSettings.keyBindDrop.isPressed() && var4) {
				this.field_71439_g.dropOneItem();
			}
			while (this.isMultiplayerWorld() && this.gameSettings.keyBindChat.isPressed()) {
				this.displayGuiScreen(new GuiChat());  // Spout removed "/" in GuiChat constructor
			}

			// Spout start
			// Open chat in SP with debug key
			if (currentScreen == null && !isMultiplayerWorld() && Keyboard.getEventKey() == Keyboard.KEY_GRAVE) {
				this.displayGuiScreen(new GuiChat());
				thePlayer.sendChatMessage(ChatColor.RED + "Debug Console Opened");
			}
			if (currentScreen == null && this.isMultiplayerWorld() && Keyboard.getEventKey() == Keyboard.KEY_SLASH) {
				GuiChat chat = new GuiChat();
				chat.message = "/";
				chat.cursorPosition = 1;
				this.displayGuiScreen(chat);
			}
			// Spout end

			if (this.field_71439_g.isUsingItem()) {
				if (!this.gameSettings.keyBindUseItem.pressed) {
					this.field_71442_b.onStoppedUsingItem(this.field_71439_g);
				}

				label309:
				while (true) {
					if (!this.gameSettings.keyBindAttack.isPressed()) {
						while (this.gameSettings.keyBindUseItem.isPressed()) {
							;
						}

						while (true) {
							if (this.gameSettings.keyBindPickBlock.isPressed()) {
								continue;
							}
							break label309;
						}
					}
				}
			} else {
				while (this.gameSettings.keyBindAttack.isPressed()) {
					this.clickMouse(0);
				}

				while (this.gameSettings.keyBindUseItem.isPressed()) {
					this.clickMouse(1);
				}

				while (this.gameSettings.keyBindPickBlock.isPressed()) {
					this.clickMiddleMouseButton();
				}
			}

			if (this.gameSettings.keyBindUseItem.pressed && this.rightClickDelayTimer == 0 && !this.field_71439_g.isUsingItem()) {
				this.clickMouse(1);
			}

			this.sendClickBlockToController(0, this.currentScreen == null && this.gameSettings.keyBindAttack.pressed && this.inGameHasFocus);
		}

		if (this.field_71441_e != null) {
			if (this.field_71439_g != null) {
				++this.joinPlayerCounter;
				if (this.joinPlayerCounter == 30) {
					this.joinPlayerCounter = 0;
					this.field_71441_e.joinEntityInSurroundings(this.field_71439_g);
				}
			}

			this.field_71424_I.endStartSection("gameRenderer");

			if (!this.isGamePaused) {
				this.entityRenderer.updateRenderer();
			}

			this.field_71424_I.endStartSection("levelRenderer");
			if (!this.isGamePaused) {
				this.renderGlobal.updateClouds();
			}

			this.field_71424_I.endStartSection("level");
			if (!this.isGamePaused) {
				if (this.field_71441_e.lightningFlash > 0) {
					--this.field_71441_e.lightningFlash;
				}

				this.field_71441_e.updateEntities();
			}

			if (!this.isGamePaused || this.isMultiplayerWorld()) {
				this.field_71441_e.setAllowedSpawnTypes(this.field_71441_e.difficultySetting > 0, true);
				this.field_71441_e.tick();
			}

			this.field_71424_I.endStartSection("animateTick");
			if (!this.isGamePaused && this.field_71441_e != null) {
				this.field_71441_e.func_73029_E(MathHelper.floor_double(this.field_71439_g.posX), MathHelper.floor_double(this.field_71439_g.posY), MathHelper.floor_double(this.field_71439_g.posZ));
			}

			this.field_71424_I.endStartSection("particles");
			if (!this.isGamePaused) {
				this.effectRenderer.updateEffects();
			}
		} else if (this.field_71453_ak != null) {
			this.field_71424_I.endStartSection("pendingConnection");
			this.field_71453_ak.processReadPackets();
		}

		this.field_71424_I.endSection();
		this.systemTime = func_71386_F();
	}

	private void forceReload() {
		System.out.println("FORCING RELOAD!");
		//Spout Start
		CustomTextureManager.resetTextures();
		//Spout End
		this.sndManager = new SoundManager();
		this.sndManager.loadSoundSettings(this.gameSettings);
		this.downloadResourcesThread.reloadResources();
	}

	public void func_71371_a(String par1Str, String par2Str, WorldSettings par3WorldSettings) {
		this.func_71403_a((WorldClient)null);
		System.gc();
		ISaveHandler var4 = this.saveLoader.getSaveLoader(par1Str, false);
		WorldInfo var5 = var4.loadWorldInfo();

		if (var5 == null && par3WorldSettings != null) {
			this.statFileWriter.readStat(StatList.createWorldStat, 1);
			var5 = new WorldInfo(par3WorldSettings, par1Str);
			var4.saveWorldInfo(var5);
		}

		if (par3WorldSettings == null) {
			par3WorldSettings = new WorldSettings(var5);
		}

		this.statFileWriter.readStat(StatList.startGameStat, 1);
		this.field_71437_Z = new IntegratedServer(this, par1Str, par2Str, par3WorldSettings);
		this.field_71437_Z.func_71256_s();
		this.field_71455_al = true;
		this.loadingScreen.displaySavingString(StatCollector.translateToLocal("menu.loadingLevel"));

		while (!this.field_71437_Z.func_71200_ad()) {
			String var6 = this.field_71437_Z.func_71195_b_();

			if (var6 != null) {
				this.loadingScreen.displayLoadingString(StatCollector.translateToLocal(var6));
			} else {
				this.loadingScreen.displayLoadingString("");
			}

			try {
				Thread.sleep(200L);
			} catch (InterruptedException var9) {
				;
			}
		}

		this.displayGuiScreen((GuiScreen)null);

		try {
			NetClientHandler var10 = new NetClientHandler(this, this.field_71437_Z);
			this.field_71453_ak = var10.func_72548_f();
		} catch (IOException var8) {
			this.func_71377_b(this.func_71396_d(new CrashReport("Connecting to integrated server", var8)));
		}
	}

	public void func_71403_a(WorldClient par1WorldClient) {
		this.func_71353_a(par1WorldClient, "");
	}

	public void func_71353_a(WorldClient par1WorldClient, String par2Str) {
		// Spout Start
		if (par1World != null) {
			SpoutClient.getInstance().enableAddons(AddonLoadOrder.PREWORLD);
		}
		// Spout End
		this.statFileWriter.syncStats();

		if (par1WorldClient == null) {
			NetClientHandler var3 = this.getSendQueue();

			if (var3 != null) {
				var3.func_72547_c();
			}

			if (this.field_71453_ak != null) {
				this.field_71453_ak.func_74431_f();
			}

			if (this.field_71437_Z != null) {
				this.field_71437_Z.func_71263_m();
			}

			this.field_71437_Z = null;
		}

		this.renderViewEntity = null;
		this.field_71453_ak = null;

		if (this.loadingScreen != null) {
			this.loadingScreen.printText(par2Str);
			this.loadingScreen.displayLoadingString("");
		}

		if (par1WorldClient == null && this.field_71441_e != null) {
			if (this.texturePackList.func_77295_a()) {
				this.texturePackList.func_77304_b();
			}

			this.func_71351_a((ServerData)null);
			this.field_71455_al = false;
		}

		this.sndManager.playStreaming((String)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		this.field_71441_e = par1WorldClient;

		if (par1WorldClient != null) {
			if (this.renderGlobal != null) {
				this.renderGlobal.func_72732_a(par1WorldClient);
			}

			if (this.effectRenderer != null) {
				this.effectRenderer.clearEffects(par1WorldClient);
			}

			if (this.field_71439_g == null) {
				this.field_71439_g = this.field_71442_b.func_78754_a(par1WorldClient);
				this.field_71442_b.flipPlayer(this.field_71439_g);
			}

			this.field_71439_g.preparePlayerToSpawn();
			par1WorldClient.spawnEntityInWorld(this.field_71439_g);
			this.field_71439_g.movementInput = new MovementInputFromOptions(this.gameSettings);
			this.field_71442_b.func_78748_a(this.field_71439_g);
			this.renderViewEntity = this.field_71439_g;
			// Spout Start
			SpoutClient.getInstance().onWorldEnter();
			SpoutClient.getInstance().enableAddons(AddonLoadOrder.POSTWORLD);
			// Spout End
		} else {
			this.saveLoader.flushCache();
			this.field_71439_g = null;
			// Spout Start
			if (renderEngine.oldPack != null) {
				renderEngine.texturePack.setTexturePack(renderEngine.oldPack);
				renderEngine.oldPack = null;
			}
			renderEngine.refreshTextures();
			SpoutClient.getInstance().onWorldExit();
			SpoutClient.getInstance().disableAddons();
			SpoutClient.getInstance().clearPermissions();
			// Spout End
		}

		System.gc();
		this.systemTime = 0L;
	}

	/**
	 * Installs a resource. Currently only sounds are download so this method just adds them to the SoundManager.
	 */
	public void installResource(String par1Str, File par2File) {
		int var3 = par1Str.indexOf("/");
		String var4 = par1Str.substring(0, var3);
		par1Str = par1Str.substring(var3 + 1);

		if (!var4.equalsIgnoreCase("sound") && !var4.equalsIgnoreCase("newsound")) {
			if (var4.equalsIgnoreCase("streaming")) {
				this.sndManager.addStreaming(par1Str, par2File);
			} else if (var4.equalsIgnoreCase("music") || var4.equalsIgnoreCase("newmusic")) {
				this.sndManager.addMusic(par1Str, par2File);
			}
		} else {
			this.sndManager.addSound(par1Str, par2File);
		}
	}

	public String debugInfoRenders() {
		return this.renderGlobal.getDebugInfoRenders();
	}

	public String getEntityDebug() {
		return this.renderGlobal.getDebugInfoEntities();
	}

	public String getWorldProviderName() {
		return this.field_71441_e.getProviderName();
	}

	public String debugInfoEntities() {
		return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.field_71441_e.getDebugLoadedEntities();
	}

	public void func_71354_a(int par1) {
		this.field_71441_e.setSpawnLocation();
		this.field_71441_e.func_73022_a();
		int var2 = 0;

		if (this.field_71439_g != null) {
			var2 = this.field_71439_g.entityId;
			this.field_71441_e.setEntityDead(this.field_71439_g);
		}

		this.renderViewEntity = null;
		this.field_71439_g = this.field_71442_b.func_78754_a(this.field_71441_e);
		this.field_71439_g.dimension = par1;
		this.renderViewEntity = this.field_71439_g;
		this.field_71439_g.preparePlayerToSpawn();
		this.field_71441_e.spawnEntityInWorld(this.field_71439_g);
		this.field_71442_b.flipPlayer(this.field_71439_g);
		this.field_71439_g.movementInput = new MovementInputFromOptions(this.gameSettings);
		this.field_71439_g.entityId = var2;
		this.field_71442_b.func_78748_a(this.field_71439_g);
		// Spout start
		EntityPlayer var9 = this.field_71439_g;
		if (var9 != null) {
			this.field_71439_g.setData(var9.getData()); //even in MP still need to copy Spout data across
			if (var9.health <= 0) {
				String name = "Death " + new SimpleDateFormat("dd-MM-yyyy").format(new Date());
				Waypoint death = new Waypoint(name, (int)var9.posX, (int)var9.posY, (int)var9.posZ, true);
				death.deathpoint = true;
				MinimapConfig.getInstance().addWaypoint(death);
			}
		}

		if (this.currentScreen instanceof GuiGameOver) {
			this.displayGuiScreen((GuiScreen)null);
		}
	}

	void func_71390_a(boolean par1) {
		this.field_71459_aj = par1;
	}

	public final boolean func_71355_q() {
		return this.field_71459_aj;
	}

	public NetClientHandler getSendQueue() {
		return this.field_71439_g != null ? this.field_71439_g.sendQueue : null;
	}

	public static void main(String[] par0ArrayOfStr) {
		HashMap var1 = new HashMap();
		boolean var2 = false;
		boolean var3 = true;
		boolean var4 = false;
		String var5 = "Player" + func_71386_F() % 1000L;

		if (par0ArrayOfStr.length > 0) {
			var5 = par0ArrayOfStr[0];
		}

		String var6 = "-";

		if (par0ArrayOfStr.length > 1) {
			var6 = par0ArrayOfStr[1];
		}

		for (int var7 = 2; var7 < par0ArrayOfStr.length; ++var7) {
			String var8 = par0ArrayOfStr[var7];
			String var10000;

			if (var7 == par0ArrayOfStr.length - 1) {
				var10000 = null;
			} else {
				var10000 = par0ArrayOfStr[var7 + 1];
			}

			boolean var10 = false;

			if (!var8.equals("-demo") && !var8.equals("--demo")) {
				if (var8.equals("--applet")) {
					var3 = false;
				}
			} else {
				var2 = true;
			}

			if (var10) {
				++var7;
			}
		}

		var1.put("demo", "" + var2);
		var1.put("stand-alone", "" + var3);
		var1.put("username", var5);
		var1.put("fullscreen", "" + var4);
		var1.put("sessionid", var6);
		Frame var12 = new Frame();
		var12.setTitle("Minecraft");
		var12.setBackground(Color.BLACK);
		JPanel var11 = new JPanel();
		var12.setLayout(new BorderLayout());
		var11.setPreferredSize(new Dimension(854, 480));
		var12.add(var11, "Center");
		var12.pack();
		var12.setLocationRelativeTo((Component)null);
		var12.setVisible(true);
		var12.addWindowListener(new GameWindowListener());
		MinecraftFakeLauncher var9 = new MinecraftFakeLauncher(var1);
		MinecraftApplet var13 = new MinecraftApplet();
		var13.setStub(var9);
		var9.setLayout(new BorderLayout());
		var9.add(var13, "Center");
		var9.validate();
		var12.removeAll();
		var12.setLayout(new BorderLayout());
		var12.add(var9, "Center");
		var12.validate();
		var13.init();
		var13.start();
		Runtime.getRuntime().addShutdownHook(new ThreadShutdown());
	}

	public static boolean isGuiEnabled() {
		return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
	}

	public static boolean isFancyGraphicsEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
	}

	public static boolean isAmbientOcclusionEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.ambientOcclusion;
	}

	public static boolean isDebugInfoEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.showDebugInfo;
	}

	public boolean handleClientCommand(String par1Str) {
		return !par1Str.startsWith("/") ? false : false;
	}

	private void clickMiddleMouseButton() {
		if (this.objectMouseOver != null) {
			boolean var1 = this.field_71439_g.capabilities.isCreativeMode;
			int var3 = 0;
			boolean var4 = false;
			int var2;
			int var5;

			if (this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
				var5 = this.objectMouseOver.blockX;
				int var6 = this.objectMouseOver.blockY;
				int var7 = this.objectMouseOver.blockZ;
				Block var8 = Block.blocksList[this.field_71441_e.getBlockId(var5, var6, var7)];

				if (var8 == null) {
					return;
				}

				var2 = var8.func_71922_a(this.field_71441_e, var5, var6, var7);

				if (var2 == 0) {
					return;
				}

				var4 = Item.itemsList[var2].getHasSubtypes();
				int var9 = var2 >= 256 ? var8.blockID : var2;
				var3 = Block.blocksList[var9].func_71873_h(this.field_71441_e, var5, var6, var7);
			} else {
				if (this.objectMouseOver.typeOfHit != EnumMovingObjectType.ENTITY || this.objectMouseOver.entityHit == null || !var1) {
					return;
				}

				if (this.objectMouseOver.entityHit instanceof EntityPainting) {
					var2 = Item.painting.shiftedIndex;
				} else if (this.objectMouseOver.entityHit instanceof EntityMinecart) {
					EntityMinecart var10 = (EntityMinecart)this.objectMouseOver.entityHit;

					if (var10.minecartType == 2) {
						var2 = Item.minecartPowered.shiftedIndex;
					} else if (var10.minecartType == 1) {
						var2 = Item.minecartCrate.shiftedIndex;
					} else {
						var2 = Item.minecartEmpty.shiftedIndex;
					}
				} else if (this.objectMouseOver.entityHit instanceof EntityBoat) {
					var2 = Item.boat.shiftedIndex;
				} else {
					var2 = Item.monsterPlacer.shiftedIndex;
					var3 = EntityList.getEntityID(this.objectMouseOver.entityHit);
					var4 = true;

					if (var3 <= 0 || !EntityList.entityEggs.containsKey(Integer.valueOf(var3))) {
						return;
					}
				}
			}

			this.field_71439_g.inventory.setCurrentItem(var2, var3, var4, var1);

			if (var1) {
				var5 = this.field_71439_g.inventorySlots.inventorySlots.size() - 9 + this.field_71439_g.inventory.currentItem;
				this.field_71442_b.sendSlotPacket(this.field_71439_g.inventory.getStackInSlot(this.field_71439_g.inventory.currentItem), var5);
			}
		}
	}


	public CrashReport func_71396_d(CrashReport par1CrashReport) {
		par1CrashReport.func_71500_a("LWJGL", new CallableLWJGLVersion(this));
		par1CrashReport.func_71500_a("OpenGL", new CallableGLInfo(this));
		par1CrashReport.func_71500_a("Is Modded", new CallableModded(this));
		par1CrashReport.func_71500_a("Type", new CallableType2(this));
		par1CrashReport.func_71500_a("Texture Pack", new CallableTexturePack(this));
		par1CrashReport.func_71500_a("Profiler Position", new CallableClientProfiler(this));

		if (this.field_71441_e != null) {
			this.field_71441_e.func_72914_a(par1CrashReport);
		}

		return par1CrashReport;
	}

	public static Minecraft func_71410_x() {
		return theMinecraft;
	}
	public void func_71395_y() {
		this.field_71468_ad = true;
	}

	public void func_70000_a(PlayerUsageSnooper par1PlayerUsageSnooper) {
		par1PlayerUsageSnooper.addData("fps", Integer.valueOf(field_71470_ab));
		par1PlayerUsageSnooper.addData("texpack_name", this.texturePackList.func_77292_e().func_77538_c());
		par1PlayerUsageSnooper.addData("texpack_resolution", Integer.valueOf(this.texturePackList.func_77292_e().func_77534_f()));
		par1PlayerUsageSnooper.addData("vsync_enabled", Boolean.valueOf(this.gameSettings.field_74352_v));
		par1PlayerUsageSnooper.addData("display_frequency", Integer.valueOf(Display.getDisplayMode().getFrequency()));
		par1PlayerUsageSnooper.addData("display_type", this.fullscreen ? "fullscreen" : "windowed");
	}

	public void func_70001_b(PlayerUsageSnooper par1PlayerUsageSnooper) {
		par1PlayerUsageSnooper.addData("opengl_version", GL11.glGetString(GL11.GL_VERSION));
		par1PlayerUsageSnooper.addData("opengl_vendor", GL11.glGetString(GL11.GL_VENDOR));
		par1PlayerUsageSnooper.addData("client_brand", ClientBrandRetriever.getClientModName());
		par1PlayerUsageSnooper.addData("applet", Boolean.valueOf(this.hideQuitButton));
		ContextCapabilities var2 = GLContext.getCapabilities();
		par1PlayerUsageSnooper.addData("gl_caps[ARB_multitexture]", Boolean.valueOf(var2.GL_ARB_multitexture));
		par1PlayerUsageSnooper.addData("gl_caps[ARB_multisample]", Boolean.valueOf(var2.GL_ARB_multisample));
		par1PlayerUsageSnooper.addData("gl_caps[ARB_texture_cube_map]", Boolean.valueOf(var2.GL_ARB_texture_cube_map));
		par1PlayerUsageSnooper.addData("gl_caps[ARB_vertex_blend]", Boolean.valueOf(var2.GL_ARB_vertex_blend));
		par1PlayerUsageSnooper.addData("gl_caps[ARB_matrix_palette]", Boolean.valueOf(var2.GL_ARB_matrix_palette));
		par1PlayerUsageSnooper.addData("gl_caps[ARB_vertex_program]", Boolean.valueOf(var2.GL_ARB_vertex_program));
		par1PlayerUsageSnooper.addData("gl_caps[ARB_vertex_shader]", Boolean.valueOf(var2.GL_ARB_vertex_shader));
		par1PlayerUsageSnooper.addData("gl_caps[ARB_fragment_program]", Boolean.valueOf(var2.GL_ARB_fragment_program));
		par1PlayerUsageSnooper.addData("gl_caps[ARB_fragment_shader]", Boolean.valueOf(var2.GL_ARB_fragment_shader));
		par1PlayerUsageSnooper.addData("gl_caps[ARB_shader_objects]", Boolean.valueOf(var2.GL_ARB_shader_objects));
		par1PlayerUsageSnooper.addData("gl_caps[ARB_vertex_buffer_object]", Boolean.valueOf(var2.GL_ARB_vertex_buffer_object));
		par1PlayerUsageSnooper.addData("gl_caps[ARB_framebuffer_object]", Boolean.valueOf(var2.GL_ARB_framebuffer_object));
		par1PlayerUsageSnooper.addData("gl_caps[ARB_pixel_buffer_object]", Boolean.valueOf(var2.GL_ARB_pixel_buffer_object));
		par1PlayerUsageSnooper.addData("gl_caps[ARB_uniform_buffer_object]", Boolean.valueOf(var2.GL_ARB_uniform_buffer_object));
		par1PlayerUsageSnooper.addData("gl_caps[ARB_texture_non_power_of_two]", Boolean.valueOf(var2.GL_ARB_texture_non_power_of_two));
		par1PlayerUsageSnooper.addData("gl_max_texture_size", Integer.valueOf(func_71369_N()));
	}

	private static int func_71369_N() {
		for (int var0 = 16384; var0 > 0; var0 >>= 1) {
			GL11.glTexImage2D(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_RGBA, var0, var0, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
			int var1 = GL11.glGetTexLevelParameteri(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);

			if (var1 != 0) {
				return var0;
			}
		}

		return -1;
	}

	public boolean func_70002_Q() {
		return this.gameSettings.field_74355_t;
	}

	public void func_71351_a(ServerData par1ServerData) {
		this.field_71422_O = par1ServerData;
	}

	public ServerData func_71362_z() {
		return this.field_71422_O;
	}

	public boolean func_71387_A() {
		return this.field_71455_al;
	}

	public boolean func_71356_B() {
		return this.field_71455_al && this.field_71437_Z != null;
	}

	public IntegratedServer func_71401_C() {
		return this.field_71437_Z;
	}

	public static void func_71363_D() {
		if (theMinecraft != null) {
			IntegratedServer var0 = theMinecraft.func_71401_C();

			if (var0 != null) {
				var0.func_71260_j();
			}
		}
	}

	public PlayerUsageSnooper func_71378_E() {
		return this.field_71427_U;
	}

	public static long func_71386_F() {
		return Sys.getTime() * 1000L / Sys.getTimerResolution();
	}

	public boolean func_71372_G() {
		return this.fullscreen;
	}
}
