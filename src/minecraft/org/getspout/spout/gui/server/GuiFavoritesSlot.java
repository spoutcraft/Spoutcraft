/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spout.gui.server;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.CustomTextureManager;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import net.minecraft.src.*;

public class GuiFavoritesSlot extends GuiSlot {

	public final GuiFavorites parentServerGui;

	public GuiFavoritesSlot(GuiFavorites var1) {
		super(SpoutClient.getHandle(), var1.width, var1.height, 22, var1.height - 77, 36);
		this.parentServerGui = var1;
	}

	public int getSize() {
		return GuiFavorites.getSize(this.parentServerGui).size();
	}

	@Override
	protected void elementInfo(int var1) {
		GuiFavorites.onElementInfo(this.parentServerGui, var1);
	}
	
	public void elementClicked(int var1, boolean var2) {
		GuiFavorites.onElementSelected(this.parentServerGui, var1);
		boolean var3 = GuiFavorites.getSelectedWorld(this.parentServerGui) >= 0 && GuiFavorites.getSelectedWorld(this.parentServerGui) < this.getSize();
		GuiFavorites.getSelectButton(this.parentServerGui).enabled = var3;
		GuiFavorites.getDeleteButton(this.parentServerGui).enabled = var3;
		GuiFavorites.getUpButton(this.parentServerGui).enabled = var3;
		GuiFavorites.getDownButton(this.parentServerGui).enabled = var3;
		if(var2 && var3) {
			this.parentServerGui.selectWorld(var1);
		}

	}

	public boolean isSelected(int var1) {
		return var1 == GuiFavorites.getSelectedWorld(this.parentServerGui);
	}

	public int getContentHeight() {
		return GuiFavorites.getSize(this.parentServerGui).size() * 36;
	}

	public void drawBackground() {
		this.parentServerGui.drawDefaultBackground();
	}

	public void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
		ServerSlot var6 = (ServerSlot)GuiFavorites.getSize(this.parentServerGui).get(var1);
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
		
		this.parentServerGui.drawString(SpoutClient.getHandle().fontRenderer, var6.name, var2 + 2, var3 + 1, 16777215);
		this.parentServerGui.drawString(SpoutClient.getHandle().fontRenderer, var6.msg, var2 + 2, var3 + 12, 8421504);
		this.parentServerGui.drawString(SpoutClient.getHandle().fontRenderer, var6.status, var2 + 215 - SpoutClient.getHandle().fontRenderer.getStringWidth(var6.status), var3 + 12, 8421504);
		this.parentServerGui.drawString(SpoutClient.getHandle().fontRenderer, var6.ip + (var6.port.length() > 0 ? ":" : "") + var6.port, var2 + 2, var3 + 12 + 11, 3158064);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		if (var6.uniqueid > 0) {
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
		}
		
		SpoutClient.getHandle().renderEngine.bindTexture(SpoutClient.getHandle().renderEngine.getTexture("/gui/icons.png"));
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

		this.parentServerGui.drawTexturedModalRect(var2 + 205, var3, 0 + var12 * 10, 176 + var13 * 8, 10, 8);
		byte var10 = 4;
		if(this.field_35409_k >= var2 + 205 - var10 && this.field_35408_l >= var3 - var10 && this.field_35409_k <= var2 + 205 + 10 + var10 && this.field_35408_l <= var3 + 8 + var10) {
			GuiFavorites.tooltip(this.parentServerGui, var9);
		}
	}
}
