package org.getspout.spout.gui.server;

import org.getspout.spout.client.SpoutClient;

import net.minecraft.src.*;

public class GuiFavoritesSlot extends GuiSlot {

	final GuiFavorites parentServerGui;


	public GuiFavoritesSlot(GuiFavorites var1) {
		super(SpoutClient.getHandle(), var1.width, var1.height, 22, var1.height - 77, 36);
		this.parentServerGui = var1;
	}

	public int getSize() {
		return GuiFavorites.getSize(this.parentServerGui).size();
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
		String var6 = ((ServerSlot)this.parentServerGui.serverList.get(var1)).name;
		if(var6 == null || MathHelper.stringNullOrLengthZero(var6)) {
			var6 = GuiFavorites.func_22087_f(this.parentServerGui) + " " + "";
		}

		String var7 = "";
		var7 = ((ServerSlot)this.parentServerGui.serverList.get(var1)).ip;
		this.parentServerGui.drawString(SpoutClient.getHandle().fontRenderer, var7, var2 + 2, var3 + 1, 16777215);
		this.parentServerGui.drawString(SpoutClient.getHandle().fontRenderer, var6, var2 + 2, var3 + 12, 8421504);
	}
}
