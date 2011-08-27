package net.minecraft.src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.ChatLine;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiChat;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
//Spout Start
import org.getspout.spout.chunkcache.ChunkCache;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.*;
import org.getspout.spout.player.ChatManager;
//Spout End

public class GuiIngame extends Gui {

	private static RenderItem itemRenderer = new RenderItem();
	//Spout Improved Chat Start
	//Increased default size, efficiency reasons
	public List<ChatLine> chatMessageList = new ArrayList<ChatLine>(2500);
	//Spout Improved Chat End
	public static final Random rand = new Random(); //Spout private -> public static final
	private Minecraft mc;
	public String field_933_a = null;
	private int updateCounter = 0;
	private String recordPlaying = "";
	private int recordPlayingUpFor = 0;
	private boolean field_22065_l = false;
	public float damageGuiPartialTime;
	float prevVignetteBrightness = 1.0F;


	public GuiIngame(Minecraft var1) {
		this.mc = var1;
	}

	//Spout Start
	//Most of function rewritten
	public void renderGameOverlay(float var1, boolean var2, int var3, int var4) {
		
		SpoutClient.getInstance().onTick();
		InGameHUD mainScreen = SpoutClient.getInstance().getActivePlayer().getMainScreen();

		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int screenWidth = scaledRes.getScaledWidth();
		int screenHeight = scaledRes.getScaledHeight();
		FontRenderer font = this.mc.fontRenderer;
		this.mc.entityRenderer.func_905_b();
		GL11.glEnable(3042 /*GL_BLEND*/);
		if(Minecraft.isFancyGraphicsEnabled()) {
			this.renderVignette(this.mc.thePlayer.getEntityBrightness(var1), screenWidth, screenHeight);
		}

		ItemStack helmet = this.mc.thePlayer.inventory.armorItemInSlot(3);
		if(!this.mc.gameSettings.thirdPersonView && helmet != null && helmet.itemID == Block.pumpkin.blockID) {
			this.renderPumpkinBlur(screenWidth, screenHeight);
		}

		float var10 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * var1;
		if(var10 > 0.0F) {
			this.renderPortalOverlay(var10, screenWidth, screenHeight);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/gui/gui.png"));
		InventoryPlayer var11 = this.mc.thePlayer.inventory;
		this.zLevel = -90.0F;
		this.drawTexturedModalRect(screenWidth / 2 - 91, screenHeight - 22, 0, 0, 182, 22);
		this.drawTexturedModalRect(screenWidth / 2 - 91 - 1 + var11.currentItem * 20, screenHeight - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/gui/icons.png"));
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glBlendFunc(775, 769);
		this.drawTexturedModalRect(screenWidth / 2 - 7, screenHeight / 2 - 7, 0, 0, 16, 16);
		GL11.glDisable(3042 /*GL_BLEND*/);
		
		GuiIngame.rand.setSeed((long)(this.updateCounter * 312871));
		int var15;
		int var17;
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
		if(this.mc.playerController.shouldDrawHUD()) {
			
			//Armor Bar Begin
			mainScreen.getArmorBar().render();
			//Armor Bar End
			
			//Health Bar Begin
			mainScreen.getHealthBar().render();
			//Health Bar End

			//Bubble Bar Begin
			mainScreen.getBubbleBar().render();
			//Bubble Bar End
			
			GL11.glDisable(3042 /*GL_BLEND*/);
			GL11.glEnable('\u803a');
			GL11.glPushMatrix();
			GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
			RenderHelper.enableStandardItemLighting();
			GL11.glPopMatrix();

			for(var15 = 0; var15 < 9; ++var15) {
				int x = screenWidth / 2 - 90 + var15 * 20 + 2;
				var17 = screenHeight - 16 - 3;
				this.renderInventorySlot(var15, x, var17, var1);
			}
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable('\u803a');
		}
		
		if(this.mc.thePlayer.func_22060_M() > 0) {
			GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
			GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
			var15 = this.mc.thePlayer.func_22060_M();
			float var26 = (float)var15 / 100.0F;
			if(var26 > 1.0F) {
				var26 = 1.0F - (float)(var15 - 100) / 10.0F;
			}

			var17 = (int)(220.0F * var26) << 24 | 1052704;
			this.drawRect(0, 0, screenWidth, screenHeight, var17);
			GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
			GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
		}
		
		mainScreen.render();
		
		String var23;
		if(this.mc.playerController.shouldDrawHUD() && this.mc.gameSettings.showDebugInfo) {
			GL11.glPushMatrix();
			if(Minecraft.hasPaidCheckTime > 0L) {
				GL11.glTranslatef(0.0F, 32.0F, 0.0F);
			}

			font.drawStringWithShadow("Minecraft Beta 1.7.3 (" + this.mc.debug + ")", 2, 2, 16777215);
			font.drawStringWithShadow(this.mc.func_6241_m(), 2, 12, 16777215);
			font.drawStringWithShadow(this.mc.func_6262_n(), 2, 22, 16777215);
			font.drawStringWithShadow(this.mc.func_6245_o(), 2, 32, 16777215);
			font.drawStringWithShadow(this.mc.func_21002_o(), 2, 42, 16777215);
			long maxMem = Runtime.getRuntime().maxMemory();
			long totalMem = Runtime.getRuntime().totalMemory();
			long freeMem = Runtime.getRuntime().freeMemory();
			long usedMem = totalMem - freeMem;
			var23 = "Used memory: " + usedMem * 100L / maxMem + "% (" + usedMem / 1024L / 1024L + "MB) of " + maxMem / 1024L / 1024L + "MB";
			this.drawString(font, var23, screenWidth - font.getStringWidth(var23) - 2, 2, 14737632);
			var23 = "Allocated memory: " + totalMem * 100L / maxMem + "% (" + totalMem / 1024L / 1024L + "MB)";
			this.drawString(font, var23, screenWidth - font.getStringWidth(var23) - 2, 12, 14737632);
			//No Cheating!
			int offset = 0;
			if (SpoutClient.getInstance().isCheatMode()) {
				this.drawString(font, "x: " + this.mc.thePlayer.posX, 2, 64, 14737632);
				this.drawString(font, "y: " + this.mc.thePlayer.posY, 2, 72, 14737632);
				this.drawString(font, "z: " + this.mc.thePlayer.posZ, 2, 80, 14737632);
				this.drawString(font, "f: " + (MathHelper.floor_double((double)(this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3), 2, 88, 14737632);
				offset = 40;
			}
			if (mc.isMultiplayerWorld() && SpoutClient.getInstance().isSpoutEnabled()) {
				this.drawString(font, "Spout Map Data Cache Info:", 2, 64 + offset, 0xE0E000);
				this.drawString(font, "Average packet size: " + ChunkCache.averageChunkSize.get() + " bytes", 2, 72 + offset, 14737632);
				this.drawString(font, "Cache hit percent: " + ChunkCache.hitPercentage.get(), 2, 80 + offset, 14737632);
				long currentTime = System.currentTimeMillis();
				long downBandwidth = (8 * ChunkCache.totalPacketDown.get()) / (currentTime - ChunkCache.loggingStart.get());
				long upBandwidth = (8 * ChunkCache.totalPacketUp.get()) / (currentTime - ChunkCache.loggingStart.get());
				this.drawString(font, "Bandwidth (Up): " + upBandwidth + "kbps", 2, 88 + offset, 14737632);
				this.drawString(font, "Bandwidth (Down): " + downBandwidth + "kbps", 2, 96 + offset, 14737632);
			}
			GL11.glPopMatrix();
		}

		if(this.recordPlayingUpFor > 0) {
			float var24 = (float)this.recordPlayingUpFor - var1;
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
				if(this.field_22065_l) {
					var17 = Color.HSBtoRGB(var24 / 50.0F, 0.7F, 0.6F) & 16777215;
				}

				font.drawString(this.recordPlaying, -font.getStringWidth(this.recordPlaying) / 2, -4, var17 + (fontColor << 24));
				GL11.glDisable(3042 /*GL_BLEND*/);
				GL11.glPopMatrix();
			}
		}

		boolean chatOpen = mainScreen.getChatBar().isVisible() && mc.currentScreen instanceof GuiChat;
		int lines = chatOpen ? mainScreen.getChatTextBox().getNumVisibleChatLines() : mainScreen.getChatTextBox().getNumVisibleLines();

		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, (float)(screenHeight - 48), 0.0F);
		
		ChatTextBox chatTextWidget = mainScreen.getChatTextBox();
		if (this.mc.playerController.shouldDrawHUD() && chatTextWidget.isVisible()) {
			int viewedLine = 0;
			for (int line = SpoutClient.getInstance().getChatManager().chatScroll; line < Math.min(chatMessageList.size() - 1, (lines + SpoutClient.getInstance().getChatManager().chatScroll)); line++) {
				if (chatOpen || chatMessageList.get(line).updateCounter < chatTextWidget.getFadeoutTicks()) {
					double opacity = 1.0D - chatMessageList.get(line).updateCounter / (double)chatTextWidget.getFadeoutTicks();
					opacity *= 10D;
					if(opacity < 0.0D) {
						opacity = 0.0D;
					}
					if(opacity > 1.0D) {
						opacity = 1.0D;
					}
					opacity *= opacity;
					int color = chatOpen ? 255 : (int)(255D * opacity);
					if (color > 0) {
						int x = 2;
						int y = -viewedLine * 9;
						String chat = chatMessageList.get(line).message;
						chat = SpoutClient.getInstance().getChatManager().formatChatColors(chat);
						chat = ChatManager.formatUrl(chat);
						//TODO add support for opening URL in browser if clicked?
						drawRect(x, y - 1, x + 320, y + 8, color / 2 << 24);
						GL11.glEnable(3042 /*GL_BLEND*/);
						font.drawStringWithShadow(chat, x, y, 0xffffff + (color << 24));
					}
					viewedLine++;
				}
			}
		}

		GL11.glPopMatrix();
		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
		GL11.glDisable(3042 /*GL_BLEND*/);
	}

	private void renderPumpkinBlur(int var1, int var2) {
		GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("%blur%/misc/pumpkinblur.png"));
		Tessellator var3 = Tessellator.instance;
		var3.startDrawingQuads();
		var3.addVertexWithUV(0.0D, (double)var2, -90.0D, 0.0D, 1.0D);
		var3.addVertexWithUV((double)var1, (double)var2, -90.0D, 1.0D, 1.0D);
		var3.addVertexWithUV((double)var1, 0.0D, -90.0D, 1.0D, 0.0D);
		var3.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		var3.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderVignette(float var1, int var2, int var3) {
		var1 = 1.0F - var1;
		if(var1 < 0.0F) {
			var1 = 0.0F;
		}

		if(var1 > 1.0F) {
			var1 = 1.0F;
		}

		this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(var1 - this.prevVignetteBrightness) * 0.01D);
		GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(0, 769);
		GL11.glColor4f(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("%blur%/misc/vignette.png"));
		Tessellator var4 = Tessellator.instance;
		var4.startDrawingQuads();
		var4.addVertexWithUV(0.0D, (double)var3, -90.0D, 0.0D, 1.0D);
		var4.addVertexWithUV((double)var2, (double)var3, -90.0D, 1.0D, 1.0D);
		var4.addVertexWithUV((double)var2, 0.0D, -90.0D, 1.0D, 0.0D);
		var4.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		var4.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBlendFunc(770, 771);
	}

	private void renderPortalOverlay(float var1, int var2, int var3) {
		if(var1 < 1.0F) {
			var1 *= var1;
			var1 *= var1;
			var1 = var1 * 0.8F + 0.2F;
		}

		GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
		GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, var1);
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/terrain.png"));
		float var4 = (float)(Block.portal.blockIndexInTexture % 16) / 16.0F;
		float var5 = (float)(Block.portal.blockIndexInTexture / 16) / 16.0F;
		float var6 = (float)(Block.portal.blockIndexInTexture % 16 + 1) / 16.0F;
		float var7 = (float)(Block.portal.blockIndexInTexture / 16 + 1) / 16.0F;
		Tessellator var8 = Tessellator.instance;
		var8.startDrawingQuads();
		var8.addVertexWithUV(0.0D, (double)var3, -90.0D, (double)var4, (double)var7);
		var8.addVertexWithUV((double)var2, (double)var3, -90.0D, (double)var6, (double)var7);
		var8.addVertexWithUV((double)var2, 0.0D, -90.0D, (double)var6, (double)var5);
		var8.addVertexWithUV(0.0D, 0.0D, -90.0D, (double)var4, (double)var5);
		var8.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderInventorySlot(int var1, int var2, int var3, float var4) {
		ItemStack var5 = this.mc.thePlayer.inventory.mainInventory[var1];
		if(var5 != null) {
			float var6 = (float)var5.animationsToGo - var4;
			if(var6 > 0.0F) {
				GL11.glPushMatrix();
				float var7 = 1.0F + var6 / 5.0F;
				GL11.glTranslatef((float)(var2 + 8), (float)(var3 + 12), 0.0F);
				GL11.glScalef(1.0F / var7, (var7 + 1.0F) / 2.0F, 1.0F);
				GL11.glTranslatef((float)(-(var2 + 8)), (float)(-(var3 + 12)), 0.0F);
			}

			itemRenderer.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, var5, var2, var3);
			if(var6 > 0.0F) {
				GL11.glPopMatrix();
			}

			itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, var5, var2, var3);
		}
	}

	public void updateTick() {
		if(this.recordPlayingUpFor > 0) {
			--this.recordPlayingUpFor;
		}

		++this.updateCounter;

		for(int var1 = 0; var1 < this.chatMessageList.size(); ++var1) {
			++((ChatLine)this.chatMessageList.get(var1)).updateCounter;
		}

	}

	public void clearChatMessages() {
		this.chatMessageList.clear();
	}

	public void addChatMessage(String var1) {
		while(this.mc.fontRenderer.getStringWidth(var1) > 320) {
			int var2;
			for(var2 = 1; var2 < var1.length() && this.mc.fontRenderer.getStringWidth(var1.substring(0, var2 + 1)) <= 320; ++var2) {
				;
			}

			this.addChatMessage(var1.substring(0, var2));
			var1 = var1.substring(var2);
		}

		this.chatMessageList.add(0, new ChatLine(var1));
		//Spout Improved Chat Start
		while(this.chatMessageList.size() > 3000) {
			this.chatMessageList.remove(this.chatMessageList.size() - 1);
		}
		//Spout Improved Chat End
	}

	public void setRecordPlayingMessage(String var1) {
		this.recordPlaying = "Now playing: " + var1;
		this.recordPlayingUpFor = 60;
		this.field_22065_l = true;
	}

	public void addChatMessageTranslate(String var1) {
		StringTranslate var2 = StringTranslate.getInstance();
		String var3 = var2.translateKey(var1);
		this.addChatMessage(var3);
	}

}
