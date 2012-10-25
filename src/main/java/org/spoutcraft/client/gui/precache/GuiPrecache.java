package org.spoutcraft.client.gui.precache;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.WidgetAnchor;

public class GuiPrecache extends GuiScreen {
	
	public GenericLabel statusText;
	
	@Override
	public void initGui() {
		
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		
		statusText = new GenericLabel();
		statusText.setAnchor(WidgetAnchor.CENTER_CENTER);
		statusText.setAlign(WidgetAnchor.CENTER_CENTER);
		statusText.setText("Checking Plugin Caches...");
		
		getScreen().attachWidget(spoutcraft, statusText);
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.drawBackground(0);
		super.drawScreen(par1, par2, par3);
	}
}
