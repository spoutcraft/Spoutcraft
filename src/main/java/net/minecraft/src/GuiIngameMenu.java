package net.minecraft.src;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.chunkcache.HeightMapAgent;
import org.spoutcraft.client.gui.server.GuiFavorites;
import org.spoutcraft.client.gui.settings.GuiAdvancedOptions;
import org.spoutcraft.client.gui.settings.GuiSimpleOptions;

public class GuiIngameMenu extends GuiScreen
{
	/** Also counts the number of updates, not certain as to why yet. */
	private int updateCounter2;

	/** Counts the number of screen updates. */
	private int updateCounter;

	public GuiIngameMenu()
	{
		updateCounter2 = 0;
		updateCounter = 0;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui()
	{
		updateCounter2 = 0;
		controlList.clear();
		byte byte0 = -16;
		controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + byte0, StatCollector.translateToLocal("menu.returnToMenu")));

		if (!mc.isIntegratedServerRunning())
		{
			((GuiButton)controlList.get(0)).displayString = StatCollector.translateToLocal("menu.disconnect");
		}

		controlList.add(new GuiButton(4, width / 2 - 100, height / 4 + 24 + byte0, StatCollector.translateToLocal("menu.returnToGame")));
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + byte0, 98, 20, StatCollector.translateToLocal("menu.options")));
		GuiButton var3;
		this.controlList.add(var3 = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + byte0, 98, 20, StatCollector.translateToLocal("menu.shareToLan")));
		controlList.add(new GuiButton(5, width / 2 - 100, height / 4 + 48 + byte0, 98, 20, StatCollector.translateToLocal("gui.achievements")));
		controlList.add(new GuiButton(6, width / 2 + 2, height / 4 + 48 + byte0, 98, 20, StatCollector.translateToLocal("gui.stats")));
		var3.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().func_71344_c();
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		switch(par1GuiButton.id) {
		case 0:
			this.mc.displayGuiScreen(GuiSimpleOptions.constructOptionsScreen(this)); // Spout
			break;
		case 1:
			// Spout Start
			HeightMapAgent.save();
			// Spout End
			par1GuiButton.enabled = false;			
			mc.statFileWriter.readStat(StatList.leaveGameStat, 1);
			mc.theWorld.sendQuittingDisconnectingPacket();
			mc.loadWorld(null);
			// Spout Start
			this.mc.displayGuiScreen(SpoutClient.getInstance().getServerManager().getJoinedFrom()); // Spout
			// Spout End
			
		case 2:
		case 3:
		default:
			break;
		case 4:
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
			break;
		case 5:
			mc.displayGuiScreen(new GuiAchievements(mc.statFileWriter));
			break;
		case 6:
			mc.displayGuiScreen(new GuiStats(this, mc.statFileWriter));
			break;
		case 7:
			this.mc.displayGuiScreen(new GuiShareToLan(this));
			break;
		}
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		super.updateScreen();
		updateCounter++;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3)
	{
		drawDefaultBackground();

		drawCenteredString(fontRenderer, "Game menu", width / 2, 40, 0xffffff);
		super.drawScreen(par1, par2, par3);
	}
}
