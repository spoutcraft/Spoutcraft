package net.minecraft.src;

import org.lwjgl.opengl.GL11;

class GuiSlotServer extends GuiSlot {
	final GuiMultiplayer parentGui;

	public GuiSlotServer(GuiMultiplayer par1GuiMultiplayer) {
		super(par1GuiMultiplayer.mc, par1GuiMultiplayer.width, par1GuiMultiplayer.height, 32, par1GuiMultiplayer.height - 64, 36);
		this.parentGui = par1GuiMultiplayer;
	}

	protected int getSize() {
		return GuiMultiplayer.getServerList(this.parentGui).size();
	}

	protected void elementClicked(int par1, boolean par2) {
		GuiMultiplayer.setSelectedServer(this.parentGui, par1);
		boolean var3 = GuiMultiplayer.getSelectedServer(this.parentGui) >= 0 && GuiMultiplayer.getSelectedServer(this.parentGui) < this.getSize();
		GuiMultiplayer.getButtonSelect(this.parentGui).enabled = var3;
		GuiMultiplayer.getButtonEdit(this.parentGui).enabled = var3;
		GuiMultiplayer.getButtonDelete(this.parentGui).enabled = var3;
		if (par2 && var3) {
			GuiMultiplayer.joinServer(this.parentGui, par1);
		}
	}

	protected boolean isSelected(int par1) {
		return par1 == GuiMultiplayer.getSelectedServer(this.parentGui);
	}

	protected int getContentHeight() {
		return GuiMultiplayer.getServerList(this.parentGui).size() * 36;
	}

	protected void drawBackground() {
		this.parentGui.drawDefaultBackground();
	}

	protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator) {
		ServerData var6 = (ServerData)GuiMultiplayer.getServerList(this.parentGui).get(par1);
		synchronized(GuiMultiplayer.getLock()) {
			if (GuiMultiplayer.getThreadsPending() < 5 && !var6.field_78841_f) {
				var6.field_78841_f = true;
				var6.field_78844_e = -2L;
				var6.serverMOTD = "";
				var6.field_78846_c = "";
				GuiMultiplayer.incrementThreadsPending();
				(new ThreadPollServers(this, var6)).start();
			}
		}

		this.parentGui.drawString(this.parentGui.fontRenderer, var6.serverName, par2 + 2, par3 + 1, 16777215);
		this.parentGui.drawString(this.parentGui.fontRenderer, var6.serverMOTD, par2 + 2, par3 + 12, 8421504);
		this.parentGui.drawString(this.parentGui.fontRenderer, var6.field_78846_c, par2 + 215 - this.parentGui.fontRenderer.getStringWidth(var6.field_78846_c), par3 + 12, 8421504);
		this.parentGui.drawString(this.parentGui.fontRenderer, var6.serverIP, par2 + 2, par3 + 12 + 11, 3158064);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.parentGui.mc.renderEngine.bindTexture(this.parentGui.mc.renderEngine.getTexture("/gui/icons.png"));
		String var9 = "";
		byte var7;
		int var8;
		if (var6.field_78841_f && var6.field_78844_e != -2L) {
			var7 = 0;
			boolean var12 = false;
			if (var6.field_78844_e < 0L) {
				var8 = 5;
			} else if (var6.field_78844_e < 150L) {
				var8 = 0;
			} else if (var6.field_78844_e < 300L) {
				var8 = 1;
			} else if (var6.field_78844_e < 600L) {
				var8 = 2;
			} else if (var6.field_78844_e < 1000L) {
				var8 = 3;
			} else {
				var8 = 4;
			}

			if (var6.field_78844_e < 0L) {
				var9 = "(no connection)";
			} else {
				var9 = var6.field_78844_e + "ms";
			}
		} else {
			var7 = 1;
			var8 = (int)(System.currentTimeMillis() / 100L + (long)(par1 * 2) & 7L);
			if (var8 > 4) {
				var8 = 8 - var8;
			}

			var9 = "Polling..";
		}

		this.parentGui.drawTexturedModalRect(par2 + 205, par3, 0 + var7 * 10, 176 + var8 * 8, 10, 8);
		byte var10 = 4;
		if (this.mouseX >= par2 + 205 - var10 && this.mouseY >= par3 - var10 && this.mouseX <= par2 + 205 + 10 + var10 && this.mouseY <= par3 + 8 + var10) {
			GuiMultiplayer.setTooltipText(this.parentGui, var9);
		}
	}
}
