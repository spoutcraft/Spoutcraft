package net.minecraft.src;

import net.minecraft.src.EnumOptions;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSlider;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StringTranslate;
//Spout Start
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.packet.CustomPacket;
import org.getspout.spout.packet.PacketRenderDistance;
import org.lwjgl.opengl.Display;
import org.spoutcraft.spoutcraftapi.entity.ActivePlayer;
//Spout End
public class GuiVideoSettings extends GuiScreen {

	private GuiScreen parentGuiScreen;
	protected String field_22107_a = "Video Settings";
	private GameSettings guiGameSettings;
	//Spout Start
	private static EnumOptions[] videoOptions = new EnumOptions[]{EnumOptions.GRAPHICS, EnumOptions.RENDER_DISTANCE, EnumOptions.AO_LEVEL, EnumOptions.FRAMERATE_LIMIT, EnumOptions.ANAGLYPH, EnumOptions.VIEW_BOBBING, EnumOptions.GUI_SCALE, EnumOptions.ADVANCED_OPENGL, EnumOptions.FOG_FANCY, EnumOptions.FOG_START, /*EnumOptions.MIPMAP_LEVEL, EnumOptions.MIPMAP_TYPE, */EnumOptions.LOAD_FAR, EnumOptions.PRELOADED_CHUNKS, EnumOptions.SMOOTH_FPS, EnumOptions.BRIGHTNESS};
	private int lastMouseX = 0;
	private int lastMouseY = 0;
	private long mouseStillTime = 0L;
	//Spout End


	public GuiVideoSettings(GuiScreen var1, GameSettings var2) {
		this.parentGuiScreen = var1;
		this.guiGameSettings = var2;
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.field_22107_a = var1.translateKey("options.videoTitle");
		int var2 = 0;
		EnumOptions[] var3 = videoOptions;
		int var4 = var3.length;
		//Spout Start
		int var5;
		for(var5 = 0; var5 < var4; ++var5) {
			EnumOptions option = var3[var5];
			int var7 = this.width / 2 - 155 + var2 % 2 * 160;
			int var8 = this.height / 6 + 21 * (var2 / 2) - 10;
			if(!option.getEnumFloat()) {
				this.controlList.add(new GuiSmallButton(option.returnEnumOrdinal(), var7, var8, option, this.guiGameSettings.getKeyBinding(option)));
			} else {
				this.controlList.add(new GuiSlider(option.returnEnumOrdinal(), var7, var8, option, this.guiGameSettings.getKeyBinding(option), this.guiGameSettings.getOptionFloatValue(option)));
			}
			
			if (((GuiButton)controlList.get(controlList.size() - 1)).enabled) {
				((GuiButton)controlList.get(controlList.size() - 1)).enabled = SpoutClient.getInstance().isCheatMode() || !option.isVisualCheating();
			}
			if (((GuiButton)controlList.get(controlList.size() - 1)).enabled) {
				((GuiButton)controlList.get(controlList.size() - 1)).enabled = Config.canUseMipmaps() || !option.getEnumString().toLowerCase().contains("mipmap");
			}
			

			++var2;
		}
		//Spout End
		//Spout Start
		var5 = this.height / 6 + 21 * (var2 / 2) - 10;
		int var9 = this.width / 2 - 155 + 0;
		if (this.mc.theWorld != null) {
			this.controlList.add(new GuiButton(201, this.width / 2 - 100 - 55, this.height / 6 + 168 - 10, 310, 20, "Optimize Video Settings For This Computer"));
		}
		this.controlList.add(new GuiSmallButton(100, var9, var5, "Animations..."));
		var9 = this.width / 2 - 155 + 160;
		this.controlList.add(new GuiSmallButton(101, var9, var5, "Details..."));
		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, var1.translateKey("gui.done")));
		//Spout End
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			int var2 = this.guiGameSettings.guiScale;
			if(var1.id < 100 && var1 instanceof GuiSmallButton) {
				//Spout Start
				int change = 1;
				GuiButton guibutton = var1;
				if (EnumOptions.getEnumOptions(guibutton.id) == EnumOptions.RENDER_DISTANCE && SpoutClient.getInstance().isCheatMode()) {
					byte view = (byte)guiGameSettings.renderDistance;
					ActivePlayer activePlayer = SpoutClient.getInstance().getActivePlayer();
					if (activePlayer != null) {
						byte newView = (byte) activePlayer.getNextRenderDistance().getValue();
						guiGameSettings.renderDistance = newView;
						change = 0;
						if (view != newView && this.mc.isMultiplayerWorld()) {
							((EntityClientPlayerMP)SpoutClient.getHandle().thePlayer).sendQueue.addToSendQueue(new CustomPacket(new PacketRenderDistance((byte)newView)));
						}
					}
				}
				if (change != 0) {
					guiGameSettings.setOptionValue(((GuiSmallButton)guibutton).returnEnumOptions(), change);
					guibutton.displayString = guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
				}
			 //Spout End
			}
			//Spout Start
			if(var1.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.parentGuiScreen);
			}

			if(var1.id == 100) {
				this.mc.gameSettings.saveOptions();
				GuiAnimationSettingsOF var2 = new GuiAnimationSettingsOF(this, this.guiGameSettings);
				this.mc.displayGuiScreen(var2);
			}

			if(var1.id == 101) {
				this.mc.gameSettings.saveOptions();
				GuiDetailSettingsOF var5 = new GuiDetailSettingsOF(this, this.guiGameSettings);
				this.mc.displayGuiScreen(var5);
			}
			if (var1.id == 201) {
				int cores = Runtime.getRuntime().availableProcessors();
				int fps = Math.max(1, net.minecraft.client.Minecraft.framesPerSecond);
				System.out.println("Optimizing Settings, Cores: " + cores + " FPS: " + fps);
				GameSettings settings = this.mc.gameSettings;
				if (fps > 150) {
					settings.ofFogFancy = true;
					settings.fancyGraphics = true;
					settings.ofPreloadedChunks = 6;
					settings.ofOcclusionFancy = true;
					settings.ofChunkUpdates = 3;
					if (cores > 1) {
						settings.ofSmoothFps = true;
						settings.ofPreloadedChunks = 8;
					}
					settings.renderDistance = 0;
					this.mc.renderGlobal.markAllRenderersDirty();
				}
				else if (fps > 100) {
					settings.ofChunkUpdates = 2;
					settings.ofFogFancy = true;
					settings.fancyGraphics = true;
					settings.ofPreloadedChunks = 2;
					settings.ofOcclusionFancy = true;
					settings.renderDistance = 0;
					this.mc.renderGlobal.markAllRenderersDirty();
				}
				else if (fps > 60) {
					settings.ofPreloadedChunks = 0;
					settings.ofOcclusionFancy = false;
					//Ideal range
				}
				else if (fps > 30) {
					settings.ofLoadFar = false;
					settings.ofPreloadedChunks = 0;
					settings.ofOcclusionFancy = false;
					settings.ofSmoothFps = false;
					settings.ofOcclusionFancy = false;
					settings.advancedOpengl = false;
					settings.ofClearWater = false;
					settings.fancyGraphics = false;
					settings.ofFarView = false;
					settings.renderDistance = Math.max(1, settings.renderDistance);
					settings.limitFramerate = 0;
					this.mc.renderGlobal.loadRenderers();
				}
				else if (fps > 20) {
					settings.ofFogFancy = false;
					settings.ofLoadFar = false;
					settings.ofPreloadedChunks = 0;
					settings.ofOcclusionFancy = false;
					settings.ofSmoothFps = false;
					settings.ofOcclusionFancy = false;
					settings.advancedOpengl = false;
					settings.ofClearWater = false;
					settings.fancyGraphics = false;
					settings.ofChunkUpdates = 1;
					settings.ofFarView = false;
					settings.renderDistance = Math.max(2, settings.renderDistance);
					settings.limitFramerate = 0;
					this.mc.renderGlobal.loadRenderers();
				}
				else {
					settings.ofFogFancy = false;
					settings.ofLoadFar = false;
					settings.ofPreloadedChunks = 0;
					settings.ofOcclusionFancy = false;
					settings.ofSmoothFps = false;
					settings.ofOcclusionFancy = false;
					settings.advancedOpengl = false;
					settings.ofClearWater = false;
					settings.fancyGraphics = false;
					settings.ofChunkUpdates = 1;
					settings.ofFarView = false;
					settings.renderDistance = 3;
					settings.limitFramerate = 0;
					this.mc.renderGlobal.loadRenderers();
				}
			}

			if(var1.id != EnumOptions.BRIGHTNESS.ordinal() && var1.id != EnumOptions.AO_LEVEL.ordinal()) {
				ScaledResolution var6 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
				int var3 = var6.getScaledWidth();
				int var4 = var6.getScaledHeight();
				this.setWorldAndResolution(this.mc, var3, var4);
			}
			//Spout End
		}
	}
	//Spout Start
	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.field_22107_a, this.width / 2, 20, 16777215);
		super.drawScreen(var1, var2, var3);
		if(var1 == this.lastMouseX && var2 == this.lastMouseY) {
			short var4 = 700;
			if(System.currentTimeMillis() >= this.mouseStillTime + (long)var4) {
				int var5 = this.width / 2 - 150;
				int var6 = this.height / 6 - 5;
				if(var2 <= var6 + 98) {
					var6 += 105;
				}

				int var7 = var5 + 150 + 150;
				int var8 = var6 + 84 + 10;
				GuiButton var9 = this.getSelectedButton(var1, var2);
				if(var9 != null) {
					String var10 = this.getButtonName(var9.displayString);
					String[] tooltips = this.getTooltipLines(var10);
					if(tooltips == null) {
						return;
					}

					this.drawGradientRect(var5, var6, var7, var8, -536870912, -536870912);

					for(int var12 = 0; var12 < tooltips.length; ++var12) {
						String var13 = tooltips[var12];
						this.fontRenderer.drawStringWithShadow(var13, var5 + 5, var6 + 5 + var12 * 11, 14540253);
					}
				}

			}
		} else {
			this.lastMouseX = var1;
			this.lastMouseY = var2;
			this.mouseStillTime = System.currentTimeMillis();
		}
	}

	private String[] getTooltipLines(String option) {
		final String[] cheating = {"This option has been disabled by the server because", " it is considered cheating.", " ", "Contact your admin if you would like it enabled"};
		final String[] mipmap = {"This option has been disabled because your texture pack", " does not support mipmaps.", " ", "Full mipmap support for all texture packs is planned"};
		if (option.equals("Graphics")) {
			return new String[]{"Visual quality", "  Fast  - lower quality, faster", "  Fancy - higher quality, slower", "Changes the appearance of clouds, leaves, water,", "shadows and grass sides."};
		}
		else if (option.equals("Render Distance")) {
			return new String[]{"Visible distance", "  Far - 256m (slower)", "  Normal - 128m", "  Short - 64m (faster)", "  Tiny - 32m (fastest)"};
		}
		else if (option.equals("Smooth Lighting")) {
			return new String[]{"Smooth lighting", "  OFF - no smooth lighting (faster)", "  1% - light smooth lighting (slower)", "  100% - dark smooth lighting (slower)"};
		}
		else if (option.equals("Performance")) {
			return new String[]{"FPS Limit", "  Max FPS - no limit (fastest)", "  Balanced - limit 120 FPS (slower)", "  Power saver - limit 40 FPS (slowest)", "  VSync - limit to monitor framerate (60, 30, 20)", "Balanced and Power saver decrease the FPS even if", "the limit value is not reached."};
		}
		else if (option.equals("3D Anaglyph")) {
			return new String[]{"3D mode used with red-cyan 3D glasses."};
		}
		else if (option.equals("View Bobbing")) {
			return new String[]{"More realistic movement.", "When using mipmaps set it to OFF for best results."};
		}
		else if (option.equals("GUI Scale")) {
			return new String[]{"GUI Scale", "Smaller GUI might be faster"};
		}
		else if (option.equals("Advanced OpenGL")) {
			return new String[]{"Detect and render only visible geometry", "  OFF - all geometry is rendered (slower)", "  Fast - only visible geometry is rendered (fastest)", "  Fancy - conservative, avoids visual artifacts (faster)", "The option is available only if it is supported by the ", "graphic card."};
		}
		else if (option.equals("Fog")) {
			return new String[]{"Fog type", "  Fast - faster fog", "  Fancy - slower fog, looks better", "The fancy fog is available only if it is supported by the ", "graphic card."};
		}
		else if (option.equals("Fog Start")) {
			if (!SpoutClient.getInstance().isCheatMode()) {
				return cheating;
			}
			return new String[]{"Fog start", "  0.2 - the fog starts near the player", "  0.8 - the fog starts far from the player", "This option usually does not affect the performance."};
		}
		else if (option.equals("Mipmap Level")) {
			if (!Config.canUseMipmaps()) {
				return mipmap;
			}
			return new String[]{"Visual effect which makes distant objects look better", "by smoothing the texture details", "  OFF - no smoothing", "  1 - minimum smoothing", "  4 - maximum smoothing", "This option usually does not affect the performance."};
		}
		else if (option.equals("Mipmap Type")) {
			if (!Config.canUseMipmaps()) {
				return mipmap;
			}
			return new String[]{"Visual effect which makes distant objects look better", "by smoothing the texture details", "  Nearest - rough smoothing", "  Linear - fine smoothing", "This option usually does not affect the performance."};
		}
		else if (option.equals("Load Far")) {
			return new String[]{"Loads the world chunks at distance Far.", "Switching the render distance does not cause all chunks ", "to be loaded again.", "  OFF - world chunks loaded up to render distance", "  ON - world chunks loaded at distance Far, allows", "		 fast render distance switching"};
		}
		else if (option.equals("Preloaded Chunks")) {
			return new String[]{"Defines an area in which no chunks will be loaded", "  OFF - after 5m new chunks will be loaded", "  2 - after 32m  new chunks will be loaded", "  8 - after 128m new chunks will be loaded", "Higher values need more time to load all the chunks"};
		}
		else if (option.equals("Smooth FPS")) {
			return new String[]{"Stabilizes FPS by flushing the graphic driver buffers", "  OFF - no stabilization, FPS may fluctuate", "  ON - FPS stabilization", "This option is graphic driver dependant and its effect", "is not always visible"};
		}
		else if (option.equals("Brightness")) {
			if (!SpoutClient.getInstance().isCheatMode()) {
				return cheating;
			}
			return new String[]{"Increases the brightness of darker objects", "  OFF - standard brightness", "  100% - maximum brightness for darker objects", "This options does not change the brightness of ", "fully black objects"};
		}
		return null;
	}

	private String getButtonName(String var1) {
		int var2 = var1.indexOf(58);
		return var2 < 0?var1:var1.substring(0, var2);
	}

	private GuiButton getSelectedButton(int var1, int var2) {
		for(int var3 = 0; var3 < this.controlList.size(); ++var3) {
			GuiButton var4 = (GuiButton)this.controlList.get(var3);
			boolean var5 = var1 >= var4.xPosition && var2 >= var4.yPosition && var1 < var4.xPosition + var4.width && var2 < var4.yPosition + var4.height;
			if(var5) {
				return var4;
			}
		}

		return null;
	}
	//Spout End

}
