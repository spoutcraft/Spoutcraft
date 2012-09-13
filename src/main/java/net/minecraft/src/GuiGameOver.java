package net.minecraft.src;

import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import org.spoutcraft.client.gui.mainmenu.MainMenu;

public class GuiGameOver extends GuiScreen {
	private int cooldownTimer;

	public void initGui() {
		this.controlList.clear();
		if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
			this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, StatCollector.translateToLocal("deathScreen.deleteWorld")));
		} else {
			this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 72, StatCollector.translateToLocal("deathScreen.respawn")));
			this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96, StatCollector.translateToLocal("deathScreen.titleScreen")));
			if (this.mc.session == null) {
				((GuiButton)this.controlList.get(1)).enabled = false;
			}
		}

		GuiButton var2;
		for (Iterator var1 = this.controlList.iterator(); var1.hasNext(); var2.enabled = false) {
			var2 = (GuiButton)var1.next();
		}
	}

	protected void keyTyped(char par1, int par2) {}

	protected void actionPerformed(GuiButton par1GuiButton) {
		switch (par1GuiButton.id) {
			case 1:
				this.mc.thePlayer.respawnPlayer();
				this.mc.displayGuiScreen((GuiScreen)null);
				break;
	
			case 2:
				this.mc.theWorld.sendQuittingDisconnectingPacket();
				this.mc.loadWorld((WorldClient)null);
				// Spout Start
				//this.mc.displayGuiScreen(new GuiMainMenu());
				this.mc.displayGuiScreen(new MainMenu());
				// Spout End
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		boolean var4 = this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
		String var5 = var4?StatCollector.translateToLocal("deathScreen.title.hardcore"):StatCollector.translateToLocal("deathScreen.title");
		this.drawCenteredString(this.fontRenderer, var5, this.width / 2 / 2, 30, 16777215);
		GL11.glPopMatrix();
		if (var4) {
			this.drawCenteredString(this.fontRenderer, StatCollector.translateToLocal("deathScreen.hardcoreInfo"), this.width / 2, 144, 16777215);
		}

		this.drawCenteredString(this.fontRenderer, StatCollector.translateToLocal("deathScreen.score") + ": \u00a7e" + this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
		super.drawScreen(par1, par2, par3);
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	public void updateScreen() {
		super.updateScreen();
		++this.cooldownTimer;
		GuiButton var2;
		if (this.cooldownTimer == 20) {
			for (Iterator var1 = this.controlList.iterator(); var1.hasNext(); var2.enabled = true) {
				var2 = (GuiButton)var1.next();
			}
		}
	}
}
