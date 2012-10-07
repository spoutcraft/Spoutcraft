package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import net.minecraft.client.Minecraft;

// Spout Start
import org.lwjgl.opengl.GL11;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.chunkcache.ChunkNetCache;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.gui.minimap.ZanMinimap;
import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.ChatTextBox;
import org.spoutcraft.api.gui.InGameHUD;
import org.spoutcraft.api.gui.ServerPlayerList;
import org.spoutcraft.api.player.ChatMessage;
// Spout End

public class GuiIngame extends Gui
{
	private static RenderItem itemRenderer = new RenderItem();

	// Spout Start
	private final ZanMinimap map = new ZanMinimap();
	private static boolean needsUpdate = true;
	// Spout End

	public static final Random rand = new Random(); // Spout private -> public static final
	private Minecraft mc;
	private int updateCounter;

	/** The string specifying which record music is playing */
	private String recordPlaying;

	/** How many ticks the record playing message will be displayed */
	private int recordPlayingUpFor;
	private boolean recordIsPlaying;

	/** Damage partial time (GUI) */
	public float damageGuiPartialTime;

	/** Previous frame vignette brightness (slowly changes by 1% each frame) */
	float prevVignetteBrightness;

	public GuiIngame(Minecraft par1Minecraft)
	{
		//rand = new Random();	// Spout removed
		updateCounter = 0;
		recordPlaying = "";
		recordPlayingUpFor = 0;
		recordIsPlaying = false;
		prevVignetteBrightness = 1.0F;
		mc = par1Minecraft;
	}

	/**
	 * Render the ingame overlay with quick icon bar, ...
	 */
	// Spout Start
	// TODO Rewrite again, it's in a horrible state, i'm surprised it works...
	// Most of function rewritten
	public void renderGameOverlay(float f, boolean flag, int i, int j)
	{
		InGameHUD mainScreen = SpoutClient.getInstance().getActivePlayer().getMainScreen();

		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int screenWidth = scaledRes.getScaledWidth();
		int screenHeight = scaledRes.getScaledHeight();
		FontRenderer font = this.mc.fontRenderer;
		this.mc.entityRenderer.setupOverlayRendering();
		GL11.glEnable(3042 /*GL_BLEND*/);
		if(Minecraft.isFancyGraphicsEnabled()) {
			this.renderVignette(this.mc.thePlayer.getBrightness(f), screenWidth, screenHeight);
		}

		ItemStack helmet = this.mc.thePlayer.inventory.armorItemInSlot(3);
		if(this.mc.gameSettings.thirdPersonView == 0 && helmet != null && helmet.itemID == Block.pumpkin.blockID) {
			this.renderPumpkinBlur(screenWidth, screenHeight);
		}

		if(!this.mc.thePlayer.isPotionActive(Potion.confusion)) {
			float var10 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * f;
			if(var10 > 0.0F) {
				this.renderPortalOverlay(var10, screenWidth, screenHeight);
			}
		}
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, this.mc.renderEngine.getTexture("/gui/gui.png"));
		InventoryPlayer var11 = this.mc.thePlayer.inventory;
		this.zLevel = -90.0F;
		this.drawTexturedModalRect(screenWidth / 2 - 91, screenHeight - 22, 0, 0, 182, 22);
		this.drawTexturedModalRect(screenWidth / 2 - 91 - 1 + var11.currentItem * 20, screenHeight - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, this.mc.renderEngine.getTexture("/gui/icons.png"));
		GL11.glEnable(3042 /* GL_BLEND */);
		GL11.glBlendFunc(775, 769);
		this.drawTexturedModalRect(screenWidth / 2 - 7, screenHeight / 2 - 7, 0, 0, 16, 16);
		GL11.glDisable(3042 /* GL_BLEND */);

		GuiIngame.rand.setSeed((long) (this.updateCounter * 312871));
		int var15;
		int var17;

		this.renderBossHealth();

		//better safe than sorry
		SpoutClient.enableSandbox();
		
		//Toggle visibility if needed
		if(needsUpdate && mainScreen.getHealthBar().isVisible() == mc.playerController.isInCreativeMode()) {
			mainScreen.toggleSurvivalHUD(!mc.playerController.isInCreativeMode());
		}
		needsUpdate = false;

		// Hunger Bar Begin
		mainScreen.getHungerBar().render();
		// Hunger Bar End

		// Armor Bar Begin
		mainScreen.getArmorBar().render();
		// Armor Bar End

		// Health Bar Begin
		mainScreen.getHealthBar().render();
		// Health Bar End

		// Bubble Bar Begin
		mainScreen.getBubbleBar().render();
		// Bubble Bar End

		// Exp Bar Begin
		mainScreen.getExpBar().render();
		// Exp Bar End

		SpoutClient.disableSandbox();
		
		map.onRenderTick();

		GL11.glDisable(3042 /* GL_BLEND */);
		GL11.glEnable('\u803a');
		RenderHelper.enableGUIStandardItemLighting();

		for (var15 = 0; var15 < 9; ++var15) {
			int x = screenWidth / 2 - 90 + var15 * 20 + 2;
			var17 = screenHeight - 16 - 3;
			this.renderInventorySlot(var15, x, var17, f);
		}
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable('\u803a');

		if (this.mc.thePlayer.getSleepTimer() > 0) {
			GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
			GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
			var15 = this.mc.thePlayer.getSleepTimer();
			float var26 = (float)var15 / 100.0F;
			if(var26 > 1.0F) {
				var26 = 1.0F - (float)(var15 - 100) / 10.0F;
			}

			var17 = (int)(220.0F * var26) << 24 | 1052704;
			this.drawRect(0, 0, screenWidth, screenHeight, var17);
			GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
			GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
		}

		SpoutClient.enableSandbox();
		mainScreen.render();
		SpoutClient.disableSandbox();

		if (this.mc.gameSettings.showDebugInfo) {
			this.mc.mcProfiler.startSection("debug");
			GL11.glPushMatrix();
			if (Configuration.getFastDebug() != 2) {
				font.drawStringWithShadow("Minecraft 1.3.2 (" + this.mc.debug + ")", 2, 2, 16777215);
				font.drawStringWithShadow(this.mc.debugInfoRenders(), 2, 12, 16777215);
				font.drawStringWithShadow(this.mc.getEntityDebug(), 2, 22, 16777215);
				font.drawStringWithShadow(this.mc.debugInfoEntities(), 2, 32, 16777215);
				font.drawStringWithShadow(this.mc.getWorldProviderName(), 2, 42, 16777215);
				long var41 = Runtime.getRuntime().maxMemory();
				long var34 = Runtime.getRuntime().totalMemory();
				long var42 = Runtime.getRuntime().freeMemory();
				long var43 = var34 - var42;
				String var45 = "Used memory: " + var43 * 100L / var41 + "% (" + var43 / 1024L / 1024L + "MB) of " + var41 / 1024L / 1024L + "MB";
				this.drawString(font, var45, screenWidth - font.getStringWidth(var45) - 2, 2, 14737632);
				var45 = "Allocated memory: " + var34 * 100L / var41 + "% (" + var34 / 1024L / 1024L + "MB)";
				this.drawString(font, var45, screenWidth - font.getStringWidth(var45) - 2, 12, 14737632);
				if(SpoutClient.getInstance().isCoordsCheat()) {
					this.drawString(font, String.format("x: %.5f", new Object[] {Double.valueOf(this.mc.thePlayer.posX)}), 2, 64, 14737632);
					this.drawString(font, String.format("y: %.3f (feet pos, %.3f eyes pos)", new Object[] {Double.valueOf(this.mc.thePlayer.boundingBox.minY), Double.valueOf(this.mc.thePlayer.posY)}), 2, 72, 14737632);
					this.drawString(font, String.format("z: %.5f", new Object[] {Double.valueOf(this.mc.thePlayer.posZ)}), 2, 80, 14737632);
					this.drawString(font, "f: " + (MathHelper.floor_double((double)(this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3), 2, 88, 14737632);
				}
				int var47 = MathHelper.floor_double(this.mc.thePlayer.posX);
				int var22 = MathHelper.floor_double(this.mc.thePlayer.posY);
				int var233 = MathHelper.floor_double(this.mc.thePlayer.posZ);
	
				if (this.mc.theWorld != null && this.mc.theWorld.blockExists(var47, var22, var233)) {
					Chunk var48 = this.mc.theWorld.getChunkFromBlockCoords(var47, var233);
					this.drawString(font, "lc: " + (var48.getTopFilledSegment() + 15) + " b: " + var48.getBiomeGenForWorldCoords(var47 & 15, var233 & 15, this.mc.theWorld.getWorldChunkManager()).biomeName + " bl: " + var48.getSavedLightValue(EnumSkyBlock.Block, var47 & 15, var22, var233 & 15) + " sl: " + var48.getSavedLightValue(EnumSkyBlock.Sky, var47 & 15, var22, var233 & 15) + " rl: " + var48.getBlockLightValue(var47 & 15, var22, var233 & 15, 0), 2, 96, 14737632);
				}
	
				this.drawString(font, String.format("ws: %.3f, fs: %.3f, g: %b", new Object[] {Float.valueOf(this.mc.thePlayer.capabilities.getWalkSpeed()), Float.valueOf(this.mc.thePlayer.capabilities.getFlySpeed()), Boolean.valueOf(this.mc.thePlayer.onGround)}), 2, 104, 14737632);
				// Spout Start
				boolean cacheInUse = ChunkNetCache.cacheInUse.get();
				int y = 115;
				font.drawStringWithShadow("Network Info", 2, y += 11, 0xFFFFFF);
				if (!cacheInUse) {
					font.drawStringWithShadow("Chunk Network Cache: Inactive", 22, y += 11, 0xE0E0E0);
				} else {
					font.drawStringWithShadow("Chunk Network Cache: Active", 22, y += 11, 0xE0E0E0);
					font.drawStringWithShadow("Cache hit: " + ChunkNetCache.hitPercentage.get() + "%", 22, y += 10, 0xE0E0E0);
				}
				font.drawStringWithShadow("Average Cube Size: " + ChunkNetCache.averageChunkSize.get() / 10.0 + " bytes", 22, y += 10, 0xE0E0E0);
				long logTime = System.currentTimeMillis() - ChunkNetCache.loggingStart.get();
				long kbpsUp = (80000L * ChunkNetCache.totalPacketUp.get()) / 1024 / logTime;
				long kbpsDown = (80000L * ChunkNetCache.totalPacketDown.get()) / 1024 / logTime;
				font.drawStringWithShadow("Upstream: " + (kbpsUp / 10.0) + "kbps (" + (ChunkNetCache.totalPacketUp.get() / 1024) + "kB)", 22, y += 11, 0xE0E0E0);
				font.drawStringWithShadow("Downstream: " + (kbpsDown / 10.0) + "kbps (" + (ChunkNetCache.totalPacketDown.get() / 1024) + "kB)", 22, y += 11, 0xE0E0E0);
				// Spout End
			} else {
				font.drawStringWithShadow(Integer.toString(Minecraft.framesPerSecond), 4, 2, 0xFFE303);
			}
			GL11.glPopMatrix();
			this.mc.mcProfiler.endSection();
		}

		if(this.recordPlayingUpFor > 0) {
			float var24 = (float)this.recordPlayingUpFor - f;
			int fontColor = (int)(var24 * 256.0F / 20.0F);
			if(fontColor > 255) {
				fontColor = 255;
			}

			if(fontColor > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(screenWidth / 2), (float)(screenHeight - 48), 0.0F);
				GL11.glEnable(3042 /*GL_BLEND*/);
				GL11.glBlendFunc(770, 771);
				var17 = 16777215;
				if(this.recordIsPlaying) {
					var17 = java.awt.Color.HSBtoRGB(var24 / 50.0F, 0.7F, 0.6F) & 16777215;
				}

				font.drawString(this.recordPlaying, -font.getStringWidth(this.recordPlaying) / 2, -4, var17 + (fontColor << 24));
				GL11.glDisable(3042 /*GL_BLEND*/);
				GL11.glPopMatrix();
			}
		}

		SpoutClient.enableSandbox();
		//boolean chatOpen = mainScreen.getChatBar().isVisible() && mc.currentScreen instanceof GuiChat;
		//int lines = chatOpen ? mainScreen.getChatTextBox().getNumVisibleChatLines() : mainScreen.getChatTextBox().getNumVisibleLines();

//		GL11.glEnable(3042 /*GL_BLEND*/);
//		GL11.glBlendFunc(770, 771);
//		GL11.glDisable(3008 /*GL_ALPHA_TEST*/);

		ChatTextBox chatTextWidget = mainScreen.getChatTextBox();
		GL11.glPushMatrix();
		boolean chatOpen = mainScreen.getChatBar().isVisible() && mc.currentScreen instanceof GuiChat;
		if (chatTextWidget.isVisible() || chatOpen) {
			chatTextWidget.setChatOpen(chatOpen);
			chatTextWidget.render();
		}
		GL11.glPopMatrix();
//		if (chatTextWidget.isVisible()) {
//			int viewedLine = 0;
//			for (int line = SpoutClient.getInstance().getChatManager().chatScroll; line < Math.min(chatMessageList.size(), (lines + SpoutClient.getInstance().getChatManager().chatScroll)); line++) {
//				if (chatOpen || chatMessageList.get(line).updateCounter < chatTextWidget.getFadeoutTicks()) {
//					double opacity = 1.0D - chatMessageList.get(line).updateCounter / (double)chatTextWidget.getFadeoutTicks();
//					opacity *= 10D;
//					if(opacity < 0.0D) {
//						opacity = 0.0D;
//					}
//					if(opacity > 1.0D) {
//						opacity = 1.0D;
//					}
//					opacity *= opacity;
//					int color = chatOpen ? 255 : (int)(255D * opacity);
//					if (color > 0) {
//						int x = 2 + chatTextWidget.getX();
//						int y = chatTextWidget.getY() + (-viewedLine * 9);
//						String chat = chatMessageList.get(line).message;
//						chat = SpoutClient.getInstance().getChatManager().formatChatColors(chat);
//
//						boolean mentioned = false;
//						if (ConfigReader.highlightMentions) {
//							String[] split = chat.toLowerCase().split(":");
//							if (split.length == 1) {
//								split = chat.toLowerCase().split(">");
//							}
//							if (split.length > 1) {
//								String name = this.mc.thePlayer.username.toLowerCase();
//								if (!split[0].contains(name)) {
//									for (int part = 1; part < split.length; part++) {
//										if (split[part].contains(name)) {
//											mentioned = true;
//											break;
//										}
//									}
//								}
//							}
//						}
//
//						if (mentioned) {
//							drawRect(x, y - 1, x + 320, y + 8, RED);
//						}
//						else {
//							drawRect(x, y - 1, x + 320, y + 8, color / 2 << 24);
//						}
//						GL11.glEnable(3042 /*GL_BLEND*/);
//						font.drawStringWithShadow(chat, x, y, 0xffffff + (color << 24));
//					}
//					viewedLine++;
//				}
//			}
//		}
		SpoutClient.disableSandbox();

		ServerPlayerList playerList = mainScreen.getServerPlayerList();
		if(this.mc.thePlayer instanceof EntityClientPlayerMP && this.mc.gameSettings.keyBindPlayerList.pressed && playerList.isVisible()) {
			NetClientHandler var41 = ((EntityClientPlayerMP)this.mc.thePlayer).sendQueue;
			List var44 = var41.playerInfoList;
			int var40 = var41.currentServerMaxPlayers;
			int var38 = var40;
			int var16;
			for(var16 = 1; var38 > 20; var38 = (var40 + var16 - 1) / var16) {
				++var16;
			}

			var17 = 300 / var16;
			if(var17 > 150) {
				var17 = 150;
			}

			int var18 = (screenWidth - var16 * var17) / 2;
			byte var46 = 10;
			this.drawRect(var18 - 1, var46 - 1, var18 + var17 * var16, var46 + 9 * var38, Integer.MIN_VALUE);

			for(int var20 = 0; var20 < var40; ++var20) {
				int var47 = var18 + var20 % var16 * var17;
				int var22 = var46 + var20 / var16 * 9;
				this.drawRect(var47, var22, var47 + var17 - 1, var22 + 8, 553648127);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
				if(var20 < var44.size()) {
					GuiPlayerInfo var50 = (GuiPlayerInfo)var44.get(var20);
					font.drawStringWithShadow(var50.name, var47, var22, 16777215);
					this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/icons.png"));
					boolean var48 = false;
					boolean var53 = false;
					byte var49 = 0;
					var53 = false;
					byte var54;
					if(var50.responseTime < 0) {
						var54 = 5;
					} else if(var50.responseTime < 150) {
						var54 = 0;
					} else if(var50.responseTime < 300) {
						var54 = 1;
					} else if(var50.responseTime < 600) {
						var54 = 2;
					} else if(var50.responseTime < 1000) {
						var54 = 3;
					} else {
						var54 = 4;
					}

					this.zLevel += 100.0F;
					this.drawTexturedModalRect(var47 + var17 - 12, var22, 0 + var49 * 10, 176 + var54 * 8, 10, 8);
					this.zLevel -= 100.0F;
				}
			}
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
		GL11.glDisable(3042 /*GL_BLEND*/);
	}

	/**
	 * Renders dragon's (boss) health on the HUD
	 */
	private void renderBossHealth()
	{
		if (RenderDragon.entityDragon == null)
		{
			return;
		}

		EntityDragon entitydragon = RenderDragon.entityDragon;
		RenderDragon.entityDragon = null;
		FontRenderer fontrenderer = mc.fontRenderer;
		ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int i = scaledresolution.getScaledWidth();
		char c = '\266';
		int j = i / 2 - c / 2;
		int k = (int)(((float)entitydragon.getDragonHealth() / (float)entitydragon.getMaxHealth()) * (float)(c + 1));
		byte byte0 = 12;
		drawTexturedModalRect(j, byte0, 0, 74, c, 5);
		drawTexturedModalRect(j, byte0, 0, 74, c, 5);

		if (k > 0)
		{
			drawTexturedModalRect(j, byte0, 0, 79, k, 5);
		}
		String s = "Boss health";
		fontrenderer.drawStringWithShadow(s, i / 2 - fontrenderer.getStringWidth(s) / 2, byte0 - 10, 0xff00ff);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/icons.png"));
	}

	private void renderPumpkinBlur(int par1, int par2)
	{
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("%blur%/misc/pumpkinblur.png"));
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, par2, -90D, 0.0D, 1.0D);
		tessellator.addVertexWithUV(par1, par2, -90D, 1.0D, 1.0D);
		tessellator.addVertexWithUV(par1, 0.0D, -90D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, -90D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Renders the vignette. Args: vignetteBrightness, width, height
	 */
	private void renderVignette(float par1, int par2, int par3)
	{
		par1 = 1.0F - par1;

		if (par1 < 0.0F)
		{
			par1 = 0.0F;
		}

		if (par1 > 1.0F)
		{
			par1 = 1.0F;
		}

		prevVignetteBrightness += (double)(par1 - prevVignetteBrightness) * 0.01D;
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
		GL11.glColor4f(prevVignetteBrightness, prevVignetteBrightness, prevVignetteBrightness, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("%blur%/misc/vignette.png"));
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, par3, -90D, 0.0D, 1.0D);
		tessellator.addVertexWithUV(par2, par3, -90D, 1.0D, 1.0D);
		tessellator.addVertexWithUV(par2, 0.0D, -90D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, -90D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * Renders the portal overlay. Args: portalStrength, width, height
	 */
	private void renderPortalOverlay(float par1, int par2, int par3)
	{
		if (par1 < 1.0F)
		{
			par1 *= par1;
			par1 *= par1;
			par1 = par1 * 0.8F + 0.2F;
		}

		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, par1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain.png"));
		float f = (float)(Block.portal.blockIndexInTexture % 16) / 16F;
		float f1 = (float)(Block.portal.blockIndexInTexture / 16) / 16F;
		float f2 = (float)(Block.portal.blockIndexInTexture % 16 + 1) / 16F;
		float f3 = (float)(Block.portal.blockIndexInTexture / 16 + 1) / 16F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, par3, -90D, f, f3);
		tessellator.addVertexWithUV(par2, par3, -90D, f2, f3);
		tessellator.addVertexWithUV(par2, 0.0D, -90D, f2, f1);
		tessellator.addVertexWithUV(0.0D, 0.0D, -90D, f, f1);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Renders the specified item of the inventory slot at the specified location. Args: slot, x, y, partialTick
	 */
	private void renderInventorySlot(int par1, int par2, int par3, float par4)
	{
		ItemStack itemstack = mc.thePlayer.inventory.mainInventory[par1];

		if (itemstack == null)
		{
			return;
		}

		float f = (float)itemstack.animationsToGo - par4;

		if (f > 0.0F)
		{
			GL11.glPushMatrix();
			float f1 = 1.0F + f / 5F;
			GL11.glTranslatef(par2 + 8, par3 + 12, 0.0F);
			GL11.glScalef(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
			GL11.glTranslatef(-(par2 + 8), -(par3 + 12), 0.0F);
		}

		itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, par2, par3);

		if (f > 0.0F)
		{
			GL11.glPopMatrix();
		}

		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, par2, par3);
	}

	/**
	 * The update tick for the ingame UI
	 */
	public void updateTick()
	{
		if(Spoutcraft.getActivePlayer() != null) {
			Spoutcraft.getActivePlayer().getMainScreen().getChatTextBox().increaseAge();
		}
		
		if (recordPlayingUpFor > 0)
		{
			recordPlayingUpFor--;
		}

		updateCounter++;
	}

	/**
	 * Clear all chat messages.
	 */
	public void clearChatMessages()
	{
		ChatTextBox.clearChat();
	}

	/**
	 * Adds a chat message to the list of chat messages. Args: msg
	 */
	public void addChatMessage(String message) {
		/* Spout start */
		if (!Configuration.isShowJoinMessages() && message.toLowerCase().contains("joined the game")) {
			return;
		}

		String mess[] = (mc.fontRenderer.func_50113_d(message, 320)).split("\n");
		for(int i=0;i<mess.length;i++)
		{
			SpoutClient.enableSandbox();
			if (Spoutcraft.getActivePlayer() != null) {
				ChatTextBox.addChatMessage(ChatMessage.parseMessage(mess[i]));
			}
			else {
				ChatTextBox.addChatMessage(new ChatMessage(mess[i], mess[i]));
			}
			SpoutClient.disableSandbox();
		}
		/* Spout end */
	}
	// public void addChatMessage(String message) {
		// /* Spout start */
		// if (!ConfigReader.showJoinMessages && message.toLowerCase().contains("joined the game")) {
			// return;
		// }
		// SpoutClient.enableSandbox();
		// if (Spoutcraft.getActivePlayer() != null) {
			// ChatTextBox.addChatMessage(ChatMessage.parseMessage(message));
		// }
		// else {
			// ChatTextBox.addChatMessage(new ChatMessage(message, message));
		// }
		// SpoutClient.disableSandbox();
		// /* Spout end */

		// int i;

		// for (; mc.fontRenderer.getStringWidth(message) > 320; message = message.substring(i)) {
			// for (i = 1; i < message.length() && mc.fontRenderer.getStringWidth(message.substring(0, i + 1)) <= 320; i++) { }
			// addChatMessage(message.substring(0, i));
		// }
	// }

	public void setRecordPlayingMessage(String par1Str)
	{
		recordPlaying = (new StringBuilder()).append("Now playing: ").append(par1Str).toString();
		recordPlayingUpFor = 60;
		recordIsPlaying = true;
	}

	/**
	 * Adds the string to chat message after translate it with the language file.
	 */
	public void addChatMessageTranslate(String par1Str, Object ... par2ArrayOfObj)
	{
		StringTranslate stringtranslate = StringTranslate.getInstance();
		String s = stringtranslate.translateKeyFormat(par1Str, par2ArrayOfObj);
		addChatMessage(s);
	}
	
	public boolean isChatOpen() {
		return this.mc.currentScreen instanceof GuiChat;
	}
	
	public ChatClickData func_50012_a(int par1, int par2) {
		if (!this.isChatOpen()) {
			return null;
		} else {
			ScaledResolution var3 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
			int chatScroll = SpoutClient.getInstance().getChatManager().chatScroll;
			//don't ask, it's much better than vanilla matching though
			par2 = par2 / var3.getScaleFactor() - 33 - ((20 - Math.min(20, chatScroll)) / 2);
			par1 = par1 / var3.getScaleFactor() - 3;
			if (par1 >= 0 && par2 >= 0) {
				int var4 = Math.min(20, ChatTextBox.getNumChatMessages());
				if (par1 <= 320 && par2 < this.mc.fontRenderer.FONT_HEIGHT * var4 + var4) {
					int var5 = par2 / (this.mc.fontRenderer.FONT_HEIGHT + 1) + chatScroll;
					return new ChatClickData(this.mc.fontRenderer, new ChatLine(this.mc.ingameGUI.getUpdateCounter(), ChatTextBox.getChatMessageAt(var5), par2), par1, par2 - (var5 - chatScroll) * this.mc.fontRenderer.FONT_HEIGHT + var5);
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}
	
	public int getUpdateCounter() {
 		return this.updateCounter++;
 	}
	
	public static void dirtySurvival() {
		needsUpdate = true;
	}
}
