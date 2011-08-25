package org.getspout.spout.gui.predownload;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.CustomScreen;
import org.getspout.spout.gui.server.GuiFavorites;
import org.getspout.spout.io.FileDownloadThread;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet0KeepAlive;
import net.minecraft.src.World;

public class GuiPredownload extends GuiScreen{
	private NetClientHandler netHandler;
	private int updateCounter = 0;
	private int joinCounter = -2;
	public CustomScreen queuedScreen = null;


	public GuiPredownload(NetClientHandler handler) {
		this.netHandler = handler;
	}

	protected void keyTyped(char var1, int var2) {}

	public void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 50, this.height / 2 + 75, 100, 20, "Abort"));
	}
	
	protected void actionPerformed(GuiButton button) {
		this.mc.theWorld.sendQuittingDisconnectingPacket();
		this.mc.changeWorld1((World)null);
		this.mc.displayGuiScreen(new GuiFavorites(new GuiMainMenu()));
		FileDownloadThread.getInstance().abort();
	}

	public void updateScreen() {
		++this.updateCounter;
		if (this.updateCounter % 20 == 0) {
			this.netHandler.addToSendQueue(new Packet0KeepAlive());
			joinCounter--;
		}
		

		if (this.netHandler != null) {
			this.netHandler.processReadPackets();
		}
		
		if (updateCounter > 60 && !SpoutClient.getInstance().isSpoutEnabled() && joinCounter < -1) {
			joinCounter = 3;
		}
		
		if (updateCounter > 200 && FileDownloadThread.getInstance().getActiveDownload() == null && FileDownloadThread.getInstance().getDownloadsRemaining() == 0 && joinCounter < -1) {
			joinCounter = 3;
			
		}
		
		if (joinCounter == -1) {
			if (netHandler.cached != null) {
				netHandler.handleFlying(netHandler.cached);
				netHandler.cached = null;
			}
			this.mc.displayGuiScreen(queuedScreen);
		}
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawBackground(0);
		this.drawCenteredString(this.fontRenderer, "Predownloading Files:", this.width / 2, this.height / 2 - 50, 16777215);
		String file = FileDownloadThread.getInstance().getActiveDownload();
		if (file == null) {
			file = "No Active Download...";
		}
		this.drawCenteredString(this.fontRenderer, file, this.width / 2, this.height / 2 - 36, 16777215);
		this.drawCenteredString(this.fontRenderer, "Files Remaining: " + FileDownloadThread.getInstance().getDownloadsRemaining(), this.width / 2, this.height / 2 - 22, 16777215);
		
		if (joinCounter > -1) {
			this.drawCenteredString(this.fontRenderer, "Joining world in " + joinCounter + "...", this.width / 2, this.height / 2 + 50, 0xAADD00);
		}
		else if (updateCounter < 60 && !SpoutClient.getInstance().isSpoutEnabled()) {
			this.drawCenteredString(this.fontRenderer, "Spout not detected!", this.width / 2, this.height / 2+ 50, 0xFF0000);
		}
		else if (SpoutClient.getInstance().isSpoutEnabled()) {
			this.drawCenteredString(this.fontRenderer, "Spout detected", this.width / 2, this.height / 2 + 50, 0xAADD00);
		}
		
		super.drawScreen(var1, var2, var3);
	}
}