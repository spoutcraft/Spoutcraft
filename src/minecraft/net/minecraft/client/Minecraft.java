package net.minecraft.client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.io.File;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.src.AchievementList;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ChunkProviderLoadOrGenerate;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.ColorizerWater;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.EnumOS2;
import net.minecraft.src.EnumOSMappingHelper;
import net.minecraft.src.EnumOptions;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GameWindowListener;
import net.minecraft.src.GuiAchievement;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiConflictWarning;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiErrorScreen;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSleepMP;
import net.minecraft.src.GuiUnused;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MinecraftError;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.MinecraftImpl;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.MouseHelper;
import net.minecraft.src.MovementInputFromOptions;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.OpenGlCapsChecker;
import net.minecraft.src.PlayerController;
import net.minecraft.src.PlayerControllerTest;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.SaveConverterMcRegion;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.ScreenShotHelper;
import net.minecraft.src.Session;
import net.minecraft.src.SoundManager;
import net.minecraft.src.StatFileWriter;
import net.minecraft.src.StatList;
import net.minecraft.src.StatStringFormatKeyInv;
import net.minecraft.src.Teleporter;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureCompassFX;
import net.minecraft.src.TextureFlamesFX;
import net.minecraft.src.TextureLavaFX;
import net.minecraft.src.TextureLavaFlowFX;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.TexturePortalFX;
import net.minecraft.src.TextureWatchFX;
import net.minecraft.src.TextureWaterFX;
import net.minecraft.src.TextureWaterFlowFX;
import net.minecraft.src.ThreadCheckHasPaid;
import net.minecraft.src.ThreadDownloadResources;
import net.minecraft.src.ThreadSleepForever;
import net.minecraft.src.Timer;
import net.minecraft.src.UnexpectedThrowable;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldRenderer;
import net.minecraft.src.WorldSettings;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

//Spout Start
import com.pclewis.mcpatcher.mod.TextureUtils;
import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.addon.AddonLoadOrder;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.event.screen.ScreenCloseEvent;
import org.spoutcraft.spoutcraftapi.event.screen.ScreenOpenEvent;
import org.spoutcraft.spoutcraftapi.gui.ScreenType;
import org.getspout.spout.gui.ScreenUtil;
import org.getspout.spout.packet.PacketScreenAction;
import org.getspout.spout.packet.ScreenAction;
//Spout End

public abstract class Minecraft implements Runnable {

	// public static byte[] field_28006_b = new byte[10485760]; //Spout unused
	public static Minecraft theMinecraft; // Spout private -> public
	public PlayerController playerController;
	private boolean fullscreen = false;
	private boolean hasCrashed = false;
	public int displayWidth;
	public int displayHeight;
	private OpenGlCapsChecker glCapabilities;
	private Timer timer = new Timer(20.0F);
	public World theWorld;
	public RenderGlobal renderGlobal;
	public EntityPlayerSP thePlayer;
	public EntityLiving renderViewEntity;
	public EffectRenderer effectRenderer;
	public Session session = null;
	public String minecraftUri;
	public Canvas mcCanvas;
	public boolean hideQuitButton = true;
	public volatile boolean isGamePaused = false;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
	public GuiScreen currentScreen = null;
	public LoadingScreenRenderer loadingScreen;
	public EntityRenderer entityRenderer;
	private ThreadDownloadResources downloadResourcesThread;
	private int ticksRan = 0;
	private int leftClickCounter = 0;
	private int tempDisplayWidth;
	private int tempDisplayHeight;
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
	public static long[] frameTimes = new long[512];
	public static long[] tickTimes = new long[512];
	public static int numRecordedFrameTimes = 0;
	public static long hasPaidCheckTime = 0L;
	private int field_35001_ab = 0;
	public StatFileWriter statFileWriter;
	private String serverName;
	private int serverPort;
	private TextureWaterFX textureWaterFX = new TextureWaterFX();
	private TextureLavaFX textureLavaFX = new TextureLavaFX();
	private static File minecraftDir = null;
	public volatile boolean running = true;
	public String debug = "";
	boolean isTakingScreenshot = false;
	long prevFrameTime = -1L;
	public boolean inGameHasFocus = false;
	public boolean isRaining = false;
	long systemTime = System.currentTimeMillis();
	private int joinPlayerCounter = 0;
	// Spout Start
	private boolean shutdown = false;
	public static boolean spoutcraftLauncher = false;
	public static boolean portable = false;
	public static int framesPerSecond = 0;
	// Spout End

	public Minecraft(Component var1, Canvas var2, MinecraftApplet var3, int var4, int var5, boolean var6) {
		StatList.func_27360_a();
		this.tempDisplayHeight = var5;
		this.fullscreen = var6;
		this.mcApplet = var3;
		new ThreadSleepForever(this, "Timer hack thread");
		this.mcCanvas = var2;
		this.displayWidth = var4;
		this.displayHeight = var5;
		this.fullscreen = var6;
		if(var3 == null || "true".equals(var3.getParameter("stand-alone"))) {
			this.hideQuitButton = false;
		}

		theMinecraft = this;
		// Spout Start
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				shutdownMinecraftApplet();
			}
		}));
		// Spout End
	}

	public void onMinecraftCrash(UnexpectedThrowable var1) {
		this.hasCrashed = true;
		this.displayUnexpectedThrowable(var1);
	}

	public abstract void displayUnexpectedThrowable(UnexpectedThrowable var1);

	public void setServer(String var1, int var2) {
		this.serverName = var1;
		this.serverPort = var2;
	}

	public void startGame() throws LWJGLException {
		if(this.mcCanvas != null) {
			Graphics var1 = this.mcCanvas.getGraphics();
			if(var1 != null) {
				var1.setColor(Color.BLACK);
				var1.fillRect(0, 0, this.displayWidth, this.displayHeight);
				var1.dispose();
			}

			Display.setParent(this.mcCanvas);
		} else if(this.fullscreen) {
			Display.setFullscreen(true);
			this.displayWidth = Display.getDisplayMode().getWidth();
			this.displayHeight = Display.getDisplayMode().getHeight();
			if(this.displayWidth <= 0) {
				this.displayWidth = 1;
			}

			if(this.displayHeight <= 0) {
				this.displayHeight = 1;
			}
		} else {
			Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
		}

		Display.setTitle("Minecraft Minecraft Beta 1.8.1");

		try {
			PixelFormat var7 = new PixelFormat();
			var7 = var7.withDepthBits(24);
			Display.create(var7);
		} catch (LWJGLException var6) {
			var6.printStackTrace();

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException var5) {
				;
			}

			Display.create();
		}

		this.mcDataDir = getMinecraftDir();
		this.saveLoader = new SaveConverterMcRegion(new File(this.mcDataDir, "saves"));
		this.gameSettings = new GameSettings(this, this.mcDataDir);
		this.texturePackList = new TexturePackList(this, this.mcDataDir);
		// Spout Start
		TextureUtils.setMinecraft(this);
		// Spout End
		this.renderEngine = new RenderEngine(this.texturePackList, this.gameSettings);
		// Spout Start
		TextureUtils.setTileSize();
		this.renderEngine.setTileSize(this);
		// Spout End
		this.fontRenderer = new FontRenderer(this.gameSettings, "/font/default.png", this.renderEngine);
		ColorizerWater.func_28182_a(this.renderEngine.getTextureContents("/misc/watercolor.png"));
		ColorizerGrass.func_28181_a(this.renderEngine.getTextureContents("/misc/grasscolor.png"));
		ColorizerFoliage.func_28152_a(this.renderEngine.getTextureContents("/misc/foliagecolor.png"));
		this.entityRenderer = new EntityRenderer(this);
		RenderManager.instance.itemRenderer = new ItemRenderer(this);
		this.statFileWriter = new StatFileWriter(this.session, this.mcDataDir);
		AchievementList.openInventory.setStatStringFormatter(new StatStringFormatKeyInv(this));
		this.loadScreen();
		Keyboard.create();
		Mouse.create();
		this.mouseHelper = new MouseHelper(this.mcCanvas);

		try {
			Controllers.create();
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		this.checkGLError("Pre startup");
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
		GL11.glShadeModel(7425 /*GL_SMOOTH*/);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
		GL11.glDepthFunc(515);
		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
		GL11.glAlphaFunc(516, 0.1F);
		GL11.glCullFace(1029 /*GL_BACK*/);
		GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
		this.checkGLError("Startup");
		this.glCapabilities = new OpenGlCapsChecker();
		this.sndManager.loadSoundSettings(this.gameSettings);
		this.renderEngine.registerTextureFX(this.textureLavaFX);
		this.renderEngine.registerTextureFX(this.textureWaterFX);
		this.renderEngine.registerTextureFX(new TexturePortalFX());
		this.renderEngine.registerTextureFX(new TextureCompassFX(this));
		this.renderEngine.registerTextureFX(new TextureWatchFX(this));
		this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
		this.renderEngine.registerTextureFX(new TextureLavaFlowFX());
		this.renderEngine.registerTextureFX(new TextureFlamesFX(0));
		this.renderEngine.registerTextureFX(new TextureFlamesFX(1));
		this.renderGlobal = new RenderGlobal(this, this.renderEngine);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

		try {
			this.downloadResourcesThread = new ThreadDownloadResources(this.mcDataDir, this);
			this.downloadResourcesThread.start();
		} catch (Exception var3) {
			;
		}

		this.checkGLError("Post startup");
		this.ingameGUI = new GuiIngame(this);
		if(this.serverName != null) {
			this.displayGuiScreen(new GuiConnecting(this, this.serverName, this.serverPort));
		} else {
			this.displayGuiScreen(new GuiMainMenu());
		}

		this.loadingScreen = new LoadingScreenRenderer(this);
	}

	private void loadScreen() throws LWJGLException {
		ScaledResolution var1 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
		GL11.glClear(16640);
		GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, var1.scaledWidthD, var1.scaledHeightD, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
		Tessellator var2 = Tessellator.instance;
		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
		GL11.glDisable(2912 /*GL_FOG*/);
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.renderEngine.getTexture("/title/mojang.png"));
		var2.startDrawingQuads();
		var2.setColorOpaque_I(16777215);
		var2.addVertexWithUV(0.0D, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
		var2.addVertexWithUV((double)this.displayWidth, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
		var2.addVertexWithUV((double)this.displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
		var2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		var2.draw();
		short var3 = 256;
		short var4 = 256;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var2.setColorOpaque_I(16777215);
		this.scaledTessellator((var1.getScaledWidth() - var3) / 2, (var1.getScaledHeight() - var4) / 2, 0, 0, var3, var4);
		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glDisable(2912 /*GL_FOG*/);
		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
		GL11.glAlphaFunc(516, 0.1F);
		Display.swapBuffers();
	}

	public void scaledTessellator(int var1, int var2, int var3, int var4, int var5, int var6) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV((double)(var1 + 0), (double)(var2 + var6), 0.0D, (double)((float)(var3 + 0) * var7), (double)((float)(var4 + var6) * var8));
		var9.addVertexWithUV((double)(var1 + var5), (double)(var2 + var6), 0.0D, (double)((float)(var3 + var5) * var7), (double)((float)(var4 + var6) * var8));
		var9.addVertexWithUV((double)(var1 + var5), (double)(var2 + 0), 0.0D, (double)((float)(var3 + var5) * var7), (double)((float)(var4 + 0) * var8));
		var9.addVertexWithUV((double)(var1 + 0), (double)(var2 + 0), 0.0D, (double)((float)(var3 + 0) * var7), (double)((float)(var4 + 0) * var8));
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

	public static File getAppDir(String var0) {
		String var1 = System.getProperty("user.home", ".");
		File var2;
		switch(EnumOSMappingHelper.enumOSMappingArray[getOs().ordinal()]) {
		case 1:
		case 2:
			var2 = new File(var1, '.' + var0 + '/');
			break;
		case 3:
			String var3 = System.getenv("APPDATA");
			if(var3 != null) {
				var2 = new File(var3, "." + var0 + '/');
			} else {
				var2 = new File(var1, '.' + var0 + '/');
			}
			break;
		case 4:
			var2 = new File(var1, "Library/Application Support/" + var0);
			break;
		default:
			var2 = new File(var1, var0 + '/');
		}

		if(!var2.exists() && !var2.mkdirs()) {
			throw new RuntimeException("The working directory could not be created: " + var2);
		} else {
			return var2;
		}
	}

	private static EnumOS2 getOs() {
		String var0 = System.getProperty("os.name").toLowerCase();
		return var0.contains("win")?EnumOS2.windows:(var0.contains("mac")?EnumOS2.macos:(var0.contains("solaris")?EnumOS2.solaris:(var0.contains("sunos")?EnumOS2.solaris:(var0.contains("linux")?EnumOS2.linux:(var0.contains("unix")?EnumOS2.linux:EnumOS2.unknown)))));
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
		if (screen == null && this.theWorld == null) {
			screen = new GuiMainMenu();
		} else if (screen == null && this.thePlayer.health <= 0) {
			screen = new GuiGameOver();
		}

		ScreenType display = ScreenUtil.getType(screen);
		if (previousScreen != null || screen != null) {
			previousScreen = this.currentScreen;
		}
		if (notify && thePlayer != null && theWorld != null) {
			// Screen closed
			if (this.currentScreen != null && screen == null) {
				SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketScreenAction(ScreenAction.Close, ScreenUtil.getType(this.currentScreen)));
				SpoutClient.getInstance().getAddonManager().callEvent(ScreenCloseEvent.getInstance((Player)thePlayer.spoutEntity, currentScreen.screen, display));
			}
			// Screen opened
			if (screen != null && this.currentScreen == null) {
				SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketScreenAction(ScreenAction.Open, display));
				SpoutClient.getInstance().getAddonManager().callEvent(ScreenOpenEvent.getInstance((Player)thePlayer.spoutEntity, screen.screen, display));
			}
			// Screen swapped
			if (screen != null && this.currentScreen != null) { // Hopefully just a submenu
				SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketScreenAction(ScreenAction.Open, display));
				SpoutClient.getInstance().getAddonManager().callEvent(ScreenOpenEvent.getInstance((Player)thePlayer.spoutEntity, screen.screen, display));
			}
		}
		if (!(this.currentScreen instanceof GuiUnused)) {
			if (this.currentScreen != null) {
				this.currentScreen.onGuiClosed();
			}

			if (screen instanceof GuiMainMenu) {
				this.statFileWriter.func_27175_b();
			}

			this.statFileWriter.syncStats();

			if (screen instanceof GuiMainMenu) {
				this.ingameGUI.clearChatMessages();
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
		System.out.println(previousScreen);
		displayGuiScreen(previousScreen, false);
		previousScreen = null;
	}

	// Spout End

	private void checkGLError(String var1) {
		int var2 = GL11.glGetError();
		if(var2 != 0) {
			String var3 = GLU.gluErrorString(var2);
			System.out.println("########## GL ERROR ##########");
			System.out.println("@ " + var1);
			System.out.println(var2 + ": " + var3);
		}

	}

	public void shutdownMinecraftApplet() {
		// Spout Start
		if (shutdown) {
			return;
		}
		shutdown = true;
		// Spout End
		try {
			this.statFileWriter.func_27175_b();
			this.statFileWriter.syncStats();
			if(this.mcApplet != null) {
				this.mcApplet.clearApplet();
			}

			try {
				if (renderEngine.oldPack != null) {
					renderEngine.texturePack.setTexturePack(renderEngine.oldPack);
				}
			} catch (Exception e) {

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
				this.changeWorld1((World)null);
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
			if(!this.hasCrashed) {
				System.exit(0);
			}

		}

		System.gc();
	}

	public void run() {
		this.running = true;

		try {
			this.startGame();
		} catch (Exception var17) {
			var17.printStackTrace();
			this.onMinecraftCrash(new UnexpectedThrowable("Failed to start game", var17));
			return;
		}

		try {
			long var1 = System.currentTimeMillis();
			int var3 = 0;

			while(this.running) {
				try {
					if(this.mcApplet != null && !this.mcApplet.isActive()) {
						break;
					}

					AxisAlignedBB.clearBoundingBoxPool();
					Vec3D.initialize();
					if(this.mcCanvas == null && Display.isCloseRequested()) {
						this.shutdown();
					}

					if(this.isGamePaused && this.theWorld != null) {
						float var4 = this.timer.renderPartialTicks;
						this.timer.updateTimer();
						this.timer.renderPartialTicks = var4;
					} else {
						this.timer.updateTimer();
					}

					long var23 = System.nanoTime();

					for(int var6 = 0; var6 < this.timer.elapsedTicks; ++var6) {
						++this.ticksRan;

						try {
							this.runTick();
						} catch (MinecraftException var16) {
							this.theWorld = null;
							this.changeWorld1((World)null);
							this.displayGuiScreen(new GuiConflictWarning());
						}
					}

					long var24 = System.nanoTime() - var23;
					this.checkGLError("Pre render");
					RenderBlocks.fancyGrass = this.gameSettings.fancyGraphics;
					this.sndManager.func_338_a(this.thePlayer, this.timer.renderPartialTicks);
					GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
					if(this.theWorld != null) {
						this.theWorld.updatingLighting();
					}

					if(!Keyboard.isKeyDown(65)) {
						Display.update();
					}

					if(this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
						this.gameSettings.thirdPersonView = false;
					}

					if(!this.skipRenderWorld) {
						if(this.playerController != null) {
							this.playerController.setPartialTime(this.timer.renderPartialTicks);
						}

						this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
					}

					if(!Display.isActive() && this.fullscreen) {
						this.toggleFullscreen();
					}

					if(this.gameSettings.showDebugInfo) {
						this.displayDebugInfo(var24);
					} else {
						this.prevFrameTime = System.nanoTime();
					}

					this.guiAchievement.updateAchievementWindow();
					Thread.yield();
					if(Keyboard.isKeyDown(65)) {
						Display.update();
					}

					this.screenshotListener();
					if(this.mcCanvas != null && !this.fullscreen && (this.mcCanvas.getWidth() != this.displayWidth || this.mcCanvas.getHeight() != this.displayHeight)) {
						this.displayWidth = this.mcCanvas.getWidth();
						this.displayHeight = this.mcCanvas.getHeight();
						if(this.displayWidth <= 0) {
							this.displayWidth = 1;
						}

						if(this.displayHeight <= 0) {
							this.displayHeight = 1;
						}

						this.resize(this.displayWidth, this.displayHeight);
					}

					this.checkGLError("Post render");
					++var3;

					for(this.isGamePaused = !this.isMultiplayerWorld() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame(); System.currentTimeMillis() >= var1 + 1000L; var3 = 0) {
						this.debug = var3 + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
						WorldRenderer.chunksUpdated = 0;
						var1 += 1000L;
						//Spout Start
						framesPerSecond = var3;
						//Spout End
					}
				} catch (MinecraftException var18) {
					this.theWorld = null;
					this.changeWorld1((World)null);
					this.displayGuiScreen(new GuiConflictWarning());
				} catch (OutOfMemoryError var19) {
					this.func_28002_e();
					this.displayGuiScreen(new GuiErrorScreen());
					System.gc();
				}
			}
		} catch (MinecraftError var20) {
			;
		} catch (Throwable var21) {
			this.func_28002_e();
			var21.printStackTrace();
			this.onMinecraftCrash(new UnexpectedThrowable("Unexpected error", var21));
		} finally {
			this.shutdownMinecraftApplet();
		}

	}

	public void func_28002_e() {
		try {
			// field_28006_b = new byte[0];
			this.renderGlobal.func_28137_f();
		} catch (Throwable var4) {
			;
		}

		try {
			System.gc();
			AxisAlignedBB.clearBoundingBoxes();
			Vec3D.clearVectorList();
		} catch (Throwable var3) {
			;
		}

		try {
			System.gc();
			this.changeWorld1((World)null);
		} catch (Throwable var2) {
			;
		}

		System.gc();
	}

	private void screenshotListener() {
		if(Keyboard.isKeyDown(60)) {
			if(!this.isTakingScreenshot) {
				this.isTakingScreenshot = true;
				this.ingameGUI.addChatMessage(ScreenShotHelper.saveScreenshot(minecraftDir, this.displayWidth, this.displayHeight));
			}
		} else {
			this.isTakingScreenshot = false;
		}

	}

	private void displayDebugInfo(long var1) {
		long var3 = 16666666L;
		if(this.prevFrameTime == -1L) {
			this.prevFrameTime = System.nanoTime();
		}

		long var5 = System.nanoTime();
		tickTimes[numRecordedFrameTimes & frameTimes.length - 1] = var1;
		frameTimes[numRecordedFrameTimes++ & frameTimes.length - 1] = var5 - this.prevFrameTime;
		this.prevFrameTime = var5;
		GL11.glClear(256);
		GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)this.displayWidth, (double)this.displayHeight, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glLineWidth(1.0F);
		GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
		Tessellator var7 = Tessellator.instance;
		var7.startDrawing(7);
		int var8 = (int)(var3 / 200000L);
		var7.setColorOpaque_I(536870912);
		var7.addVertex(0.0D, (double)(this.displayHeight - var8), 0.0D);
		var7.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)frameTimes.length, (double)(this.displayHeight - var8), 0.0D);
		var7.setColorOpaque_I(538968064);
		var7.addVertex(0.0D, (double)(this.displayHeight - var8 * 2), 0.0D);
		var7.addVertex(0.0D, (double)(this.displayHeight - var8), 0.0D);
		var7.addVertex((double)frameTimes.length, (double)(this.displayHeight - var8), 0.0D);
		var7.addVertex((double)frameTimes.length, (double)(this.displayHeight - var8 * 2), 0.0D);
		var7.draw();
		long var9 = 0L;

		int var11;
		for(var11 = 0; var11 < frameTimes.length; ++var11) {
			var9 += frameTimes[var11];
		}

		var11 = (int)(var9 / 200000L / (long)frameTimes.length);
		var7.startDrawing(7);
		var7.setColorOpaque_I(541065216);
		var7.addVertex(0.0D, (double)(this.displayHeight - var11), 0.0D);
		var7.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)frameTimes.length, (double)(this.displayHeight - var11), 0.0D);
		var7.draw();
		var7.startDrawing(1);

		for(int var12 = 0; var12 < frameTimes.length; ++var12) {
			int var13 = (var12 - numRecordedFrameTimes & frameTimes.length - 1) * 255 / frameTimes.length;
			int var14 = var13 * var13 / 255;
			var14 = var14 * var14 / 255;
			int var15 = var14 * var14 / 255;
			var15 = var15 * var15 / 255;
			if(frameTimes[var12] > var3) {
				var7.setColorOpaque_I(-16777216 + var14 * 65536);
			} else {
				var7.setColorOpaque_I(-16777216 + var14 * 256);
			}

			long var16 = frameTimes[var12] / 200000L;
			long var18 = tickTimes[var12] / 200000L;
			var7.addVertex((double)((float)var12 + 0.5F), (double)((float)((long)this.displayHeight - var16) + 0.5F), 0.0D);
			var7.addVertex((double)((float)var12 + 0.5F), (double)((float)this.displayHeight + 0.5F), 0.0D);
			var7.setColorOpaque_I(-16777216 + var14 * 65536 + var14 * 256 + var14 * 1);
			var7.addVertex((double)((float)var12 + 0.5F), (double)((float)((long)this.displayHeight - var16) + 0.5F), 0.0D);
			var7.addVertex((double)((float)var12 + 0.5F), (double)((float)((long)this.displayHeight - (var16 - var18)) + 0.5F), 0.0D);
		}

		var7.draw();
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
	}

	public void shutdown() {
		this.running = false;
	}

	// Spout Start
	public void setIngameFocus() {
		setIngameFocus(true);
	}

	public void setIngameFocus(boolean close) {
		if(Display.isActive()) {
			if(!this.inGameHasFocus) {
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
		if(this.inGameHasFocus) {
			KeyBinding.unPressAllKeys();
			this.inGameHasFocus = false;
			this.mouseHelper.ungrabMouseCursor();
		}
	}

	public void displayInGameMenu() {
		if(this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

	private void sendClickBlockToController(int var1, boolean var2) {
		if(!var2) {
			this.leftClickCounter = 0;
		}

		if(var1 != 0 || this.leftClickCounter <= 0) {
			if(var2 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE && var1 == 0) {
				int var3 = this.objectMouseOver.blockX;
				int var4 = this.objectMouseOver.blockY;
				int var5 = this.objectMouseOver.blockZ;
				this.playerController.sendBlockRemoving(var3, var4, var5, this.objectMouseOver.sideHit);
				if(this.thePlayer.func_35190_e(var3, var4, var5)) {
					this.effectRenderer.addBlockHitEffects(var3, var4, var5, this.objectMouseOver.sideHit);
					this.thePlayer.swingItem();
				}
			} else {
				this.playerController.resetBlockRemoving();
			}

		}
	}

	private void clickMouse(int var1) {
		if(var1 != 0 || this.leftClickCounter <= 0) {
			if(var1 == 0) {
				this.thePlayer.swingItem();
			}

			if(var1 == 1) {
				this.field_35001_ab = 6;
			}

			boolean var2 = true;
			ItemStack var3 = this.thePlayer.inventory.getCurrentItem();
			if(this.objectMouseOver == null) {
				if(var1 == 0 && this.playerController.func_35641_g()) {
					this.leftClickCounter = 10;
				}
			} else if(this.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY) {
				if(var1 == 0) {
					this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
				}

				if(var1 == 1) {
					this.playerController.interactWithEntity(this.thePlayer, this.objectMouseOver.entityHit);
				}
			} else if(this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
				int var4 = this.objectMouseOver.blockX;
				int var5 = this.objectMouseOver.blockY;
				int var6 = this.objectMouseOver.blockZ;
				int var7 = this.objectMouseOver.sideHit;
				if(var1 == 0) {
					this.playerController.clickBlock(var4, var5, var6, this.objectMouseOver.sideHit);
				} else {
					int var9 = var3 != null?var3.stackSize:0;
					if(this.playerController.sendPlaceBlock(this.thePlayer, this.theWorld, var3, var4, var5, var6, var7)) {
						var2 = false;
						this.thePlayer.swingItem();
					}

					if(var3 == null) {
						return;
					}

					if(var3.stackSize == 0) {
						this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
					} else if(var3.stackSize != var9 || this.playerController.isInCreativeMode()) {
						this.entityRenderer.itemRenderer.func_9449_b();
					}
				}
			}

			if(var2 && var1 == 1) {
				ItemStack var10 = this.thePlayer.inventory.getCurrentItem();
				if(var10 != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, var10)) {
					this.entityRenderer.itemRenderer.func_9450_c();
				}
			}

		}
	}

	public void toggleFullscreen() {
		try {
			this.fullscreen = !this.fullscreen;
			if(this.fullscreen) {
				Display.setDisplayMode(Display.getDesktopDisplayMode());
				this.displayWidth = Display.getDisplayMode().getWidth();
				this.displayHeight = Display.getDisplayMode().getHeight();
				if(this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if(this.displayHeight <= 0) {
					this.displayHeight = 1;
				}
			} else {
				if(this.mcCanvas != null) {
					this.displayWidth = this.mcCanvas.getWidth();
					this.displayHeight = this.mcCanvas.getHeight();
				} else {
					this.displayWidth = this.tempDisplayWidth;
					this.displayHeight = this.tempDisplayHeight;
				}

				if(this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if(this.displayHeight <= 0) {
					this.displayHeight = 1;
				}
			}

			if(this.currentScreen != null) {
				this.resize(this.displayWidth, this.displayHeight);
			}

			Display.setFullscreen(this.fullscreen);
			Display.update();
		} catch (Exception var2) {
			var2.printStackTrace();
		}

	}

	private void resize(int var1, int var2) {
		if(var1 <= 0) {
			var1 = 1;
		}

		if(var2 <= 0) {
			var2 = 1;
		}

		this.displayWidth = var1;
		this.displayHeight = var2;
		if(this.currentScreen != null) {
			ScaledResolution var3 = new ScaledResolution(this.gameSettings, var1, var2);
			int var4 = var3.getScaledWidth();
			int var5 = var3.getScaledHeight();
			this.currentScreen.setWorldAndResolution(this, var4, var5);
		}

	}

	private void func_28001_B() {
		(new ThreadCheckHasPaid(this)).start();
	}

	public void runTick() {
		if(this.field_35001_ab > 0) {
			--this.field_35001_ab;
		}

		if(this.ticksRan == 6000) {
			this.func_28001_B();
		}

		this.statFileWriter.func_27178_d();
		this.ingameGUI.updateTick();
		this.entityRenderer.getMouseOver(1.0F);
		int var3;
		if(this.thePlayer != null) {
			IChunkProvider var1 = this.theWorld.getIChunkProvider();
			if(var1 instanceof ChunkProviderLoadOrGenerate) {
				ChunkProviderLoadOrGenerate var2 = (ChunkProviderLoadOrGenerate)var1;
				var3 = MathHelper.floor_float((float)((int)this.thePlayer.posX)) >> 4;
				int var4 = MathHelper.floor_float((float)((int)this.thePlayer.posZ)) >> 4;
				var2.setCurrentChunkOver(var3, var4);
			}
		}

		if(!this.isGamePaused && this.theWorld != null) {
			this.playerController.updateController();
		}

		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.renderEngine.getTexture("/terrain.png"));
		if(!this.isGamePaused) {
			this.renderEngine.updateDynamicTextures();
		}

		if(this.currentScreen == null && this.thePlayer != null) {
			if(this.thePlayer.health <= 0) {
				this.displayGuiScreen((GuiScreen)null);
			} else if(this.thePlayer.isPlayerSleeping() && this.theWorld != null && this.theWorld.multiplayerWorld) {
				this.displayGuiScreen(new GuiSleepMP());
			}
		} else if(this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isPlayerSleeping()) {
			this.displayGuiScreen((GuiScreen)null);
		}

		if(this.currentScreen != null) {
			this.leftClickCounter = 10000;
		}

		if(this.currentScreen != null) {
			this.currentScreen.handleInput();
			if(this.currentScreen != null) {
				this.currentScreen.guiParticles.update();
				this.currentScreen.updateScreen();
			}
		}

		if(this.currentScreen == null || this.currentScreen.allowUserInput) {
			while(Mouse.next()) {
				KeyBinding.setKeyBindState(Mouse.getEventButton() - 100, Mouse.getEventButtonState());
				if(Mouse.getEventButtonState()) {
					KeyBinding.onTick(Mouse.getEventButton() - 100);
				}

				long var5 = System.currentTimeMillis() - this.systemTime;
				if(var5 <= 200L) {
					var3 = Mouse.getEventDWheel();
					if(var3 != 0) {
						this.thePlayer.inventory.changeCurrentItem(var3);
						if(this.gameSettings.field_22275_C) {
							if(var3 > 0) {
								var3 = 1;
							}

							if(var3 < 0) {
								var3 = -1;
							}

							this.gameSettings.field_22272_F += (float)var3 * 0.25F;
						}
					}

					if(this.currentScreen == null) {
						if(!this.inGameHasFocus && Mouse.getEventButtonState()) {
							this.setIngameFocus();
						}
					} else if(this.currentScreen != null) {
						this.currentScreen.handleMouseInput();
					}
				}
			}

			if(this.leftClickCounter > 0) {
				--this.leftClickCounter;
			}

			while(Keyboard.next()) {
				this.thePlayer.handleKeyPress(Keyboard.getEventKey(), Keyboard.getEventKeyState()); //Spout handle key presses
				KeyBinding.setKeyBindState(Keyboard.getEventKey(), Keyboard.getEventKeyState());
				if(Keyboard.getEventKeyState()) {
					KeyBinding.onTick(Keyboard.getEventKey());
				}

				if(Keyboard.getEventKeyState()) {
					if(Keyboard.getEventKey() == 87) {
						this.toggleFullscreen();
					} else {
						if(this.currentScreen != null) {
							this.currentScreen.handleKeyboardInput();
						} else {
							if(Keyboard.getEventKey() == 1) {
								this.displayInGameMenu();
							}

							if(Keyboard.getEventKey() == 31 && Keyboard.isKeyDown(61)) {
								this.forceReload();
							}

							if(Keyboard.getEventKey() == 33 && Keyboard.isKeyDown(61)) {
								boolean var6 = Keyboard.isKeyDown(42) | Keyboard.isKeyDown(54);
								this.gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, var6?-1:1);
							}

							if(Keyboard.getEventKey() == 59) {
								this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
							}

							if(Keyboard.getEventKey() == 61) {
								this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
							}

							if(Keyboard.getEventKey() == 63) {
								this.gameSettings.thirdPersonView = !this.gameSettings.thirdPersonView;
							}
						}

						for(int var7 = 0; var7 < 9; ++var7) {
							if(Keyboard.getEventKey() == 2 + var7) {
								this.thePlayer.inventory.currentItem = var7;
							}
						}
					}
				}
			}

			while(this.gameSettings.keyBindInventory.isPressed()) {
				this.displayGuiScreen(new GuiInventory(this.thePlayer));
			}

			while(this.gameSettings.keyBindDrop.isPressed()) {
				this.thePlayer.dropCurrentItem();
			}

			while(this.isMultiplayerWorld() && this.gameSettings.keyBindChat.isPressed()) {
				this.displayGuiScreen(new GuiChat());
			}

			if(this.thePlayer.func_35196_Z()) {
				if(!this.gameSettings.keyBindUseItem.pressed) {
					this.playerController.func_35638_c(this.thePlayer);
				}
			} else {
				while(this.gameSettings.keyBindAttack.isPressed()) {
					this.clickMouse(0);
				}

				while(this.gameSettings.keyBindUseItem.isPressed()) {
					this.clickMouse(1);
				}

				while(this.gameSettings.keyBindPickBlock.isPressed()) {
					this.clickMiddleMouseButton();
				}
			}

			if(this.gameSettings.keyBindUseItem.pressed && this.field_35001_ab == 0 && !this.thePlayer.func_35196_Z()) {
				this.clickMouse(1);
			}

			this.sendClickBlockToController(0, this.currentScreen == null && this.gameSettings.keyBindAttack.pressed && this.inGameHasFocus);
		}

		if(this.theWorld != null) {
			if(this.thePlayer != null) {
				++this.joinPlayerCounter;
				if(this.joinPlayerCounter == 30) {
					this.joinPlayerCounter = 0;
					this.theWorld.joinEntityInSurroundings(this.thePlayer);
				}
			}

			this.theWorld.difficultySetting = this.gameSettings.difficulty;
			if(this.theWorld.multiplayerWorld) {
				this.theWorld.difficultySetting = 1;
			}

			if(!this.isGamePaused) {
				this.entityRenderer.updateRenderer();
			}

			if(!this.isGamePaused) {
				this.renderGlobal.updateClouds();
			}

			if(!this.isGamePaused) {
				if(this.theWorld.field_27172_i > 0) {
					--this.theWorld.field_27172_i;
				}

				this.theWorld.updateEntities();
			}

			if(!this.isGamePaused || this.isMultiplayerWorld()) {
				this.theWorld.setAllowedMobSpawns(this.gameSettings.difficulty > 0, true);
				this.theWorld.tick();
			}

			if(!this.isGamePaused && this.theWorld != null) {
				this.theWorld.randomDisplayUpdates(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
			}

			if(!this.isGamePaused) {
				this.effectRenderer.updateEffects();
			}
		}

		this.systemTime = System.currentTimeMillis();
	}

	private void forceReload() {
		System.out.println("FORCING RELOAD!");
		this.sndManager = new SoundManager();
		this.sndManager.loadSoundSettings(this.gameSettings);
		this.downloadResourcesThread.reloadResources();
	}

	public boolean isMultiplayerWorld() {
		return this.theWorld != null && this.theWorld.multiplayerWorld;
	}

	public void startWorld(String var1, String var2, WorldSettings var3) {
		this.changeWorld1((World)null);
		System.gc();
		if(this.saveLoader.isOldMapFormat(var1)) {
			this.convertMapFormat(var1, var2);
		} else {
			if(this.loadingScreen != null) {
				this.loadingScreen.printText("Switching level");
				this.loadingScreen.displayLoadingString("");
			}

			ISaveHandler var4 = this.saveLoader.getSaveLoader(var1, false);
			World var5 = null;
			var5 = new World(var4, var2, var3);
			if(var5.isNewWorld) {
				this.statFileWriter.readStat(StatList.createWorldStat, 1);
				this.statFileWriter.readStat(StatList.startGameStat, 1);
				this.changeWorld2(var5, "Generating level");
			} else {
				this.statFileWriter.readStat(StatList.loadWorldStat, 1);
				this.statFileWriter.readStat(StatList.startGameStat, 1);
				this.changeWorld2(var5, "Loading level");
			}
		}

	}

	public void usePortal() {
		System.out.println("Toggling dimension!!");
		if(this.thePlayer.dimension == -1) {
			this.thePlayer.dimension = 0;
		} else {
			this.thePlayer.dimension = -1;
		}

		this.theWorld.setEntityDead(this.thePlayer);
		this.thePlayer.isDead = false;
		double var1 = this.thePlayer.posX;
		double var3 = this.thePlayer.posZ;
		double var5 = 8.0D;
		World var7;
		if(this.thePlayer.dimension == -1) {
			var1 /= var5;
			var3 /= var5;
			this.thePlayer.setLocationAndAngles(var1, this.thePlayer.posY, var3, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
			if(this.thePlayer.isEntityAlive()) {
				this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
			}

			var7 = null;
			var7 = new World(this.theWorld, WorldProvider.getProviderForDimension(-1));
			this.changeWorld(var7, "Entering the Nether", this.thePlayer);
		} else {
			var1 *= var5;
			var3 *= var5;
			this.thePlayer.setLocationAndAngles(var1, this.thePlayer.posY, var3, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
			if(this.thePlayer.isEntityAlive()) {
				this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
			}

			var7 = null;
			var7 = new World(this.theWorld, WorldProvider.getProviderForDimension(0));
			this.changeWorld(var7, "Leaving the Nether", this.thePlayer);
		}

		this.thePlayer.worldObj = this.theWorld;
		if(this.thePlayer.isEntityAlive()) {
			this.thePlayer.setLocationAndAngles(var1, this.thePlayer.posY, var3, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
			this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
			(new Teleporter()).placeInPortal(this.theWorld, this.thePlayer);
		}

	}

	public void changeWorld1(World var1) {
		this.changeWorld2(var1, "");
	}

	public void changeWorld2(World var1, String var2) {
		this.changeWorld(var1, var2, (EntityPlayer)null);
	}

	public void changeWorld(World var1, String var2, EntityPlayer var3) {
		// Spout Start
		SpoutClient.getInstance().loadAddons();
		SpoutClient.getInstance().enableAddons(AddonLoadOrder.PREWORLD);
		// Spout End
		this.statFileWriter.func_27175_b();
		this.statFileWriter.syncStats();
		this.renderViewEntity = null;
		if(this.loadingScreen != null) {
			this.loadingScreen.printText(var2);
			this.loadingScreen.displayLoadingString("");
		}

		this.sndManager.playStreaming((String)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		if(this.theWorld != null) {
			this.theWorld.saveWorldIndirectly(this.loadingScreen);
		}

		this.theWorld = var1;
		if(var1 != null) {
			if(this.playerController != null) {
				this.playerController.func_717_a(var1);
			}

			if(!this.isMultiplayerWorld()) {
				if(var3 == null) {
					this.thePlayer = (EntityPlayerSP)var1.func_4085_a(EntityPlayerSP.class);
				}
			} else if(this.thePlayer != null) {
				this.thePlayer.preparePlayerToSpawn();
				if(var1 != null) {
					var1.entityJoinedWorld(this.thePlayer);
				}
			}

			if(!var1.multiplayerWorld) {
				this.preloadWorld(var2);
			}

			if(this.thePlayer == null) {
				this.thePlayer = (EntityPlayerSP)this.playerController.createPlayer(var1);
				this.thePlayer.preparePlayerToSpawn();
				this.playerController.flipPlayer(this.thePlayer);
			}

			this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
			if(this.renderGlobal != null) {
				this.renderGlobal.changeWorld(var1);
			}

			if(this.effectRenderer != null) {
				this.effectRenderer.clearEffects(var1);
			}

			this.playerController.func_6473_b(this.thePlayer);
			if(var3 != null) {
				var1.emptyMethod1();
			}

			IChunkProvider var4 = var1.getIChunkProvider();
			if(var4 instanceof ChunkProviderLoadOrGenerate) {
				ChunkProviderLoadOrGenerate var5 = (ChunkProviderLoadOrGenerate)var4;
				int var6 = MathHelper.floor_float((float)((int)this.thePlayer.posX)) >> 4;
				int var7 = MathHelper.floor_float((float)((int)this.thePlayer.posZ)) >> 4;
				var5.setCurrentChunkOver(var6, var7);
			}

			var1.spawnPlayerWithLoadedChunks(this.thePlayer);
			if(var1.isNewWorld) {
				var1.saveWorldIndirectly(this.loadingScreen);
			}

			this.renderViewEntity = this.thePlayer;
			// Spout Start
			SpoutClient.getInstance().onWorldEnter();
			SpoutClient.getInstance().enableAddons(AddonLoadOrder.POSTWORLD);
			// Spout End
		} else {
			// Spout Start
			this.thePlayer = null;
			if (renderEngine.oldPack != null) {
				renderEngine.texturePack.setTexturePack(renderEngine.oldPack);
				renderEngine.oldPack = null;
			}
			renderEngine.refreshTextures();
			SpoutClient.getInstance().onWorldExit();
			SpoutClient.getInstance().disableAddons();
			// Spout End
		}

		System.gc();
		this.systemTime = 0L;
	}

	private void convertMapFormat(String var1, String var2) {
		this.loadingScreen.printText("Converting World to " + this.saveLoader.func_22178_a());
		this.loadingScreen.displayLoadingString("This may take a while :)");
		this.saveLoader.convertMapFormat(var1, this.loadingScreen);
		this.startWorld(var1, var2, new WorldSettings(0L, 0, true));
	}

	private void preloadWorld(String var1) {
		if(this.loadingScreen != null) {
			this.loadingScreen.printText(var1);
			this.loadingScreen.displayLoadingString("Building terrain");
		}

		short var2 = 128;
		if(this.playerController.func_35643_e()) {
			var2 = 64;
		}

		int var3 = 0;
		int var4 = var2 * 2 / 16 + 1;
		var4 *= var4;
		IChunkProvider var5 = this.theWorld.getIChunkProvider();
		ChunkCoordinates var6 = this.theWorld.getSpawnPoint();
		if(this.thePlayer != null) {
			var6.posX = (int)this.thePlayer.posX;
			var6.posZ = (int)this.thePlayer.posZ;
		}

		if(var5 instanceof ChunkProviderLoadOrGenerate) {
			ChunkProviderLoadOrGenerate var7 = (ChunkProviderLoadOrGenerate)var5;
			var7.setCurrentChunkOver(var6.posX >> 4, var6.posZ >> 4);
		}

		for(int var10 = -var2; var10 <= var2; var10 += 16) {
			for(int var8 = -var2; var8 <= var2; var8 += 16) {
				if(this.loadingScreen != null) {
					this.loadingScreen.setLoadingProgress(var3++ * 100 / var4);
				}

				this.theWorld.getBlockId(var6.posX + var10, 64, var6.posZ + var8);
				if(!this.playerController.func_35643_e()) {
/*
					while(true) {
						if(this.theWorld.updatingLighting()) {
							continue;
						}
					}
*/
				}
			}
		}

		if(!this.playerController.func_35643_e()) {
			if(this.loadingScreen != null) {
				this.loadingScreen.displayLoadingString("Simulating world for a bit");
			}

			boolean var9 = true;
			this.theWorld.dropOldChunks();
		}

	}

	public void installResource(String var1, File var2) {
		int var3 = var1.indexOf("/");
		String var4 = var1.substring(0, var3);
		var1 = var1.substring(var3 + 1);
		if(var4.equalsIgnoreCase("sound")) {
			this.sndManager.addSound(var1, var2);
		} else if(var4.equalsIgnoreCase("newsound")) {
			this.sndManager.addSound(var1, var2);
		} else if(var4.equalsIgnoreCase("streaming")) {
			this.sndManager.addStreaming(var1, var2);
		} else if(var4.equalsIgnoreCase("music")) {
			this.sndManager.addMusic(var1, var2);
		} else if(var4.equalsIgnoreCase("newmusic")) {
			this.sndManager.addMusic(var1, var2);
		}

	}

	public OpenGlCapsChecker getOpenGlCapsChecker() {
		return this.glCapabilities;
	}

	public String debugInfoRenders() {
		return this.renderGlobal.getDebugInfoRenders();
	}

	public String func_6262_n() {
		return this.renderGlobal.getDebugInfoEntities();
	}

	public String func_21002_o() {
		return this.theWorld.func_21119_g();
	}

	public String debugInfoEntities() {
		return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.getDebugLoadedEntities();
	}

	public void respawn(boolean var1, int var2) {
		if(!this.theWorld.multiplayerWorld && !this.theWorld.worldProvider.canRespawnHere()) {
			this.usePortal();
		}

		ChunkCoordinates var3 = null;
		ChunkCoordinates var4 = null;
		boolean var5 = true;
		if(this.thePlayer != null && !var1) {
			var3 = this.thePlayer.getPlayerSpawnCoordinate();
			if(var3 != null) {
				var4 = EntityPlayer.verifyRespawnCoordinates(this.theWorld, var3);
				if(var4 == null) {
					this.thePlayer.addChatMessage("tile.bed.notValid");
				}
			}
		}

		if(var4 == null) {
			var4 = this.theWorld.getSpawnPoint();
			var5 = false;
		}

		IChunkProvider var6 = this.theWorld.getIChunkProvider();
		if(var6 instanceof ChunkProviderLoadOrGenerate) {
			ChunkProviderLoadOrGenerate var7 = (ChunkProviderLoadOrGenerate)var6;
			var7.setCurrentChunkOver(var4.posX >> 4, var4.posZ >> 4);
		}

		this.theWorld.setSpawnLocation();
		this.theWorld.updateEntityList();
		int var8 = 0;
		if(this.thePlayer != null) {
			var8 = this.thePlayer.entityId;
			this.theWorld.setEntityDead(this.thePlayer);
		}

		this.renderViewEntity = null;
		this.thePlayer = (EntityPlayerSP)this.playerController.createPlayer(this.theWorld);
		this.thePlayer.dimension = var2;
		this.renderViewEntity = this.thePlayer;
		this.thePlayer.preparePlayerToSpawn();
		if(var5) {
			this.thePlayer.setPlayerSpawnCoordinate(var3);
			this.thePlayer.setLocationAndAngles((double)((float)var4.posX + 0.5F), (double)((float)var4.posY + 0.1F), (double)((float)var4.posZ + 0.5F), 0.0F, 0.0F);
		}

		this.playerController.flipPlayer(this.thePlayer);
		this.theWorld.spawnPlayerWithLoadedChunks(this.thePlayer);
		this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
		this.thePlayer.entityId = var8;
		this.thePlayer.func_6420_o();
		this.playerController.func_6473_b(this.thePlayer);
		this.preloadWorld("Respawning");
		if(this.currentScreen instanceof GuiGameOver) {
			this.displayGuiScreen((GuiScreen)null);
		}

	}

	public static void startMainThread1(String var0, String var1) {
		startMainThread(var0, var1, (String)null);
	}

	public static void startMainThread(String var0, String var1, String var2) {
		boolean var3 = false;
		Frame var5 = new Frame("Minecraft");
		Canvas var6 = new Canvas();
		var5.setLayout(new BorderLayout());
		var5.add(var6, "Center");
		var6.setPreferredSize(new Dimension(854, 480));
		var5.pack();
		var5.setLocationRelativeTo((Component)null);
		MinecraftImpl var7 = new MinecraftImpl(var5, var6, (MinecraftApplet)null, 854, 480, var3, var5);
		Thread var8 = new Thread(var7, "Minecraft main thread");
		var8.setPriority(10);
		var7.minecraftUri = "www.minecraft.net";
		if(var0 != null && var1 != null) {
			var7.session = new Session(var0, var1);
		} else {
			var7.session = new Session("Player" + System.currentTimeMillis() % 1000L, "");
		}

		if(var2 != null) {
			String[] var9 = var2.split(":");
			var7.setServer(var9[0], Integer.parseInt(var9[1]));
		}

		var5.setVisible(true);
		var5.addWindowListener(new GameWindowListener(var7, var8));
		var8.start();
	}

	public NetClientHandler getSendQueue() {
		return this.thePlayer instanceof EntityClientPlayerMP?((EntityClientPlayerMP)this.thePlayer).sendQueue:null;
	}

	public static void main(String[] var0) {
		String var1 = null;
		String var2 = null;
		var1 = "Player" + System.currentTimeMillis() % 1000L;
		if(var0.length > 0) {
			var1 = var0[0];
		}

		var2 = "-";
		if(var0.length > 1) {
			var2 = var0[1];
		}

		startMainThread1(var1, var2);
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

	public boolean lineIsCommand(String var1) {
		if(var1.startsWith("/")) {
			;
		}

		return false;
	}

	private void clickMiddleMouseButton() {
		if(this.objectMouseOver != null) {
			int var1 = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
			if(var1 == Block.grass.blockID) {
				var1 = Block.dirt.blockID;
			}

			if(var1 == Block.stairDouble.blockID) {
				var1 = Block.stairSingle.blockID;
			}

			if(var1 == Block.bedrock.blockID) {
				var1 = Block.stone.blockID;
			}

			this.thePlayer.inventory.setCurrentItem(var1, this.playerController instanceof PlayerControllerTest);
		}

	}

}
