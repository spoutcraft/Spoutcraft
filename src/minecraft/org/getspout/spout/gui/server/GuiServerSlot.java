package org.getspout.spout.gui.server;

import org.getspout.spout.client.SpoutClient;

import net.minecraft.src.*;

public class GuiServerSlot extends GuiSlot {

	final GuiMultiplayer parentServerGui;


	public GuiServerSlot(GuiMultiplayer var1) {
		super(SpoutClient.getHandle(), var1.width, var1.height, 32, var1.height - 88, 36);
		this.parentServerGui = var1;
	}

	public int getSize() {
		return GuiMultiplayer.getSize(this.parentServerGui).size();
	}

	public void elementClicked(int var1, boolean var2) {
		GuiMultiplayer.onElementSelected(this.parentServerGui, var1);
		boolean var3 = GuiMultiplayer.getSelectedWorld(this.parentServerGui) >= 0 && GuiMultiplayer.getSelectedWorld(this.parentServerGui) < this.getSize();
		GuiMultiplayer.getSelectButton(this.parentServerGui).enabled = var3;
		GuiMultiplayer.getSelectAdd(this.parentServerGui).enabled = var3;
		if(var2 && var3) {
			this.parentServerGui.selectWorld(var1);
		}

	}

	public boolean isSelected(int var1) {
		return var1 == GuiMultiplayer.getSelectedWorld(this.parentServerGui);
	}

	public int getContentHeight() {
		return GuiMultiplayer.getSize(this.parentServerGui).size() * 36;
	}

	public void drawBackground() {
		this.parentServerGui.drawDefaultBackground();
	}

	public void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
		try {
			String var6 = ((ServerSlot)this.parentServerGui.serverList.get(var1)).name;
			if(var6 == null || MathHelper.stringNullOrLengthZero(var6)) {
				var6 = GuiMultiplayer.func_22087_f(this.parentServerGui) + " " + "";
			}

			String var7 = "";
			var7 = ((ServerSlot)this.parentServerGui.serverList.get(var1)).players + "" + "/" + ((ServerSlot)this.parentServerGui.serverList.get(var1)).maxPlayers + "";
			var7 = var7 + ", " + ((ServerSlot)this.parentServerGui.serverList.get(var1)).country;
			String var10 = "";
			this.parentServerGui.drawString(SpoutClient.getHandle().fontRenderer, var6, var2 + 2, var3 + 1, 16777215);
			this.parentServerGui.drawString(SpoutClient.getHandle().fontRenderer, var7, var2 + 2, var3 + 12, 8421504);
			this.parentServerGui.drawString(SpoutClient.getHandle().fontRenderer, var10, var2 + 2, var3 + 12 + 10, 8421504);
		} catch (Exception var11) {
			;
		}

	}
}
