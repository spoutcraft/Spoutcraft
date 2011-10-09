package net.minecraft.src;

import net.minecraft.src.GuiMultiplayer;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;
import net.minecraft.src.ThreadPollServers;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.getspout.spout.gui.server.ServerSlot;
import org.getspout.spout.io.CustomTextureManager;

public class GuiSlotServer extends GuiSlot {
	
	// $FF: synthetic field
	final GuiMultiplayer field_35410_a;


	public GuiSlotServer(GuiMultiplayer var1) {
		super(var1.mc, var1.width, var1.height, 32, var1.height - 88, 36);
		this.field_35410_a = var1;
	}

	protected int getSize() {
		return GuiMultiplayer.getSize(this.field_35410_a).size();
	}

	@Override
	protected void elementInfo(int var1) {
		GuiMultiplayer.onElementInfo(this.field_35410_a, var1);
	}
	
	protected void elementClicked(int var1, boolean var2) {
		GuiMultiplayer.onElementSelected(this.field_35410_a, var1);
		boolean var3 = GuiMultiplayer.getSelectedWorld(this.field_35410_a) >= 0 && GuiMultiplayer.getSelectedWorld(this.field_35410_a) < this.getSize();
		GuiMultiplayer.getSelectButton(this.field_35410_a).enabled = var3;
		GuiMultiplayer.getSelectAdd(this.field_35410_a).enabled = var3;
		if(var2 && var3) {
			GuiMultiplayer.selectWorld(this.field_35410_a, var1);
		}

	}

	protected boolean isSelected(int var1) {
		return var1 == GuiMultiplayer.getSelectedWorld(this.field_35410_a);
	}

	protected int getContentHeight() {
		return GuiMultiplayer.getSize(this.field_35410_a).size() * 36;
	}

	protected void drawBackground() {
		this.field_35410_a.drawDefaultBackground();
	}

	protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
		ServerSlot var6 = (ServerSlot)GuiMultiplayer.getSize(this.field_35410_a).get(var1);
		synchronized(GuiMultiplayer.getSyncObject()) {
			if(GuiMultiplayer.getPingLimit() < 5 && !var6.pinging) {
				var6.pinging = true;
				var6.ping = -2L;
				var6.msg = "";
				var6.status = "";
				GuiMultiplayer.incrementPingLimit();
				(new ThreadPollServers(this, var6)).start();
			}
		}

		this.field_35410_a.drawString(this.field_35410_a.fontRenderer, var6.name, var2 + 2, var3 + 1, 16777215);
		this.field_35410_a.drawString(this.field_35410_a.fontRenderer, var6.msg, var2 + 2, var3 + 12, 8421504);
		this.field_35410_a.drawString(this.field_35410_a.fontRenderer, var6.status, var2 + 215 - this.field_35410_a.fontRenderer.getStringWidth(var6.status), var3 + 12, 8421504);
		this.field_35410_a.drawString(this.field_35410_a.fontRenderer, var6.ip + (var6.port.length() > 0 ? ":" : "") + var6.port, var2 + 2, var3 + 12 + 11, 3158064);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		Texture serverInfoTexture = CustomTextureManager.getTextureFromJar("/res/info.png");
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef(var2 + 220, var3, 0); // moves texture into place
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, serverInfoTexture.getTextureID());
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, 13, -90, 0.0D, 0.0D); // draw corners
		tessellator.addVertexWithUV(10, 13, -90, serverInfoTexture.getWidth(), 0.0D);
		tessellator.addVertexWithUV(10, 0.0D, -90, serverInfoTexture.getWidth(), serverInfoTexture.getHeight());
		tessellator.addVertexWithUV(0.0D, 0.0D, -90, 0.0D, serverInfoTexture.getHeight());
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		
		this.field_35410_a.mc.renderEngine.bindTexture(this.field_35410_a.mc.renderEngine.getTexture("/gui/icons.png"));
		boolean var7 = false;
		boolean var8 = false;
		String var9 = "";
		byte var12;
		int var13;
		if(var6.pinging && var6.ping != -2L) {
			var12 = 0;
			var8 = false;
			if(var6.ping < 0L) {
				var13 = 5;
			} else if(var6.ping < 150L) {
				var13 = 0;
			} else if(var6.ping < 300L) {
				var13 = 1;
			} else if(var6.ping < 600L) {
				var13 = 2;
			} else if(var6.ping < 1000L) {
				var13 = 3;
			} else {
				var13 = 4;
			}

			if(var6.ping < 0L) {
				var9 = "(no connection)";
			} else {
				var9 = var6.ping + "ms";
			}
		} else {
			var12 = 1;
			var13 = (int)(System.currentTimeMillis() / 100L + (long)(var1 * 2) & 7L);
			if(var13 > 4) {
				var13 = 8 - var13;
			}

			var9 = "Polling..";
		}

		this.field_35410_a.drawTexturedModalRect(var2 + 205, var3, 0 + var12 * 10, 176 + var13 * 8, 10, 8);
		byte var10 = 4;
		if(this.field_35409_k >= var2 + 205 - var10 && this.field_35408_l >= var3 - var10 && this.field_35409_k <= var2 + 205 + 10 + var10 && this.field_35408_l <= var3 + 8 + var10) {
			GuiMultiplayer.tooltip(this.field_35410_a, var9);
		}
	}

}