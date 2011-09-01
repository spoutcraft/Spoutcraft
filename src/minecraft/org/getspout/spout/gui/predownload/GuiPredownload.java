package org.getspout.spout.gui.predownload;

import org.getspout.spout.ReconnectManager;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.CustomScreen;
import org.getspout.spout.io.FileDownloadThread;
import org.getspout.spout.packet.PacketManager;
import org.getspout.spout.packet.PacketPreCacheCompleted;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet0KeepAlive;
import org.lwjgl.input.Keyboard;

public class GuiPredownload extends GuiScreen{
	private NetClientHandler netHandler;
	private int updateCounter = 0;
	private int joinCounter = 6;
	private int downloadDelayCounter = 2;
	public CustomScreen queuedScreen = null;
	private boolean activeDownload = false;
	private boolean paused = false;
	private boolean allPacketsReceived = false;
	private GuiScreen parent = null;

	public GuiPredownload(NetClientHandler handler) {
		this.netHandler = handler;
		parent = Minecraft.theMinecraft.currentScreen;
	}

	public void initGui() {
		this.controlList.clear();
		this.mc.setIngameFocus(false);
		//this.controlList.add(new GuiButton(0, this.width / 2 - 50, this.height / 2 + 100, 100, 20, "Abort"));
	}

	protected void actionPerformed(GuiButton button) {
		/*this.mc.theWorld.sendQuittingDisconnectingPacket();
		this.mc.changeWorld1((World)null);
		this.mc.displayGuiScreen(new GuiFavorites(new GuiMainMenu()));
		FileDownloadThread.getInstance().abort();
		 */
	}

	public void updateScreen() {
		//Increase counters, send keep alive packets
		++this.updateCounter;
		if (this.updateCounter % 20 == 0) {
			this.netHandler.addToSendQueue(new Packet0KeepAlive());
			if (!paused) {
				joinCounter--;
				downloadDelayCounter--;
			}
		}
		
		//Keep the player from falling
		Minecraft.theMinecraft.thePlayer.gravityMod = 0D;
		Minecraft.theMinecraft.thePlayer.movementInput.updatePlayerMoveState(Minecraft.theMinecraft.thePlayer);

		//Read packets
		if (this.netHandler != null) {
			this.netHandler.processReadPackets();
		}

		//Check for active downloads
		activeDownload = false;
		if (FileDownloadThread.getInstance().getActiveDownload() != null || FileDownloadThread.getInstance().getDownloadsRemaining() > 0) {
			if (this.updateCounter % 20 == 0) {
				if (!paused) {
					joinCounter++;
				}
			}
			activeDownload = true;
		}

		//Check to see if we got the file cache complete packet
		if (FileDownloadThread.preCacheCompleted.get() > System.currentTimeMillis() - 1000) {
			allPacketsReceived = true;
		}

		boolean fastEnd = (ReconnectManager.serverTeleport && allPacketsReceived && !activeDownload);
		
		if (downloadDelayCounter < 0 && parent != null) {
			parent = null;
			fastEnd = allPacketsReceived && !activeDownload;
			if (!fastEnd) {
				joinCounter = 6;
			}
		}

		if (joinCounter <= -1 || fastEnd) {
			ReconnectManager.serverTeleport = false;
			Minecraft.theMinecraft.thePlayer.gravityMod = 1D;
			this.mc.displayGuiScreen(null);
			this.mc.displayGuiScreen(queuedScreen);
			if (netHandler.cached != null) {
				netHandler.addToSendQueue(netHandler.cached);
				netHandler.cached = null;
			}
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketPreCacheCompleted());
		}
	}

	@Override
	protected void keyTyped(char ch, int key) {
		if (key == Keyboard.KEY_P) {
			paused = !paused;
			joinCounter = Math.max(joinCounter, 3);
		}
		else if (allPacketsReceived && !activeDownload && !paused && key == Keyboard.KEY_J) {
			joinCounter = -1;
		}
	}

	public void drawScreen(int var1, int var2, float var3) {
		if (parent != null) {
			parent.drawScreen(var1, var2, var3);
			return;
		}
		//this.drawBackground(0);
		if (!ReconnectManager.serverTeleport) { // Prevents screen from flashing when teleporting
			if (!paused) { 
				this.drawCenteredString(this.fontRenderer, "Predownloading Files:", this.width / 2, this.height / 2 - 50, 16777215);
				String file = FileDownloadThread.getInstance().getActiveDownload();
				if (file == null) {
					file = "No Active Download...";
				}
				this.drawCenteredString(this.fontRenderer, file, this.width / 2, this.height / 2 - 36, 16777215);
				this.drawCenteredString(this.fontRenderer, "Files Remaining: " + FileDownloadThread.getInstance().getDownloadsRemaining(), this.width / 2, this.height / 2 - 22, 16777215);

				if (activeDownload) {
					this.drawCenteredString(this.fontRenderer, "Waiting for downloads to finish...", this.width / 2, this.height / 2 + 50, 0xFFFF00);
				}
				else if (joinCounter > -1) {
					this.drawCenteredString(this.fontRenderer, "Joining world in " + joinCounter + "...", this.width / 2, this.height / 2 + 50, 0xAADD00);
				}

				if (!SpoutClient.getInstance().isSpoutEnabled()) {
					this.drawCenteredString(this.fontRenderer, "Spout not detected!", this.width / 2, this.height / 2 + 60, 0xFF0000);
				}
				else {
					this.drawCenteredString(this.fontRenderer, "Spout detected", this.width / 2, this.height / 2 + 60, 0xAADD00);
				}

				if (allPacketsReceived && !activeDownload) {
					this.drawCenteredString(this.fontRenderer, "Press 'J' to skip and join immediately!", this.width / 2, this.height / 2 + 75, 0x00FF00);
				}

				this.drawCenteredString(this.fontRenderer, "press 'P' to keep looking around!", this.width / 2, this.height / 2 + 85, 0xFF4500);
			}
			else {
				this.drawCenteredString(this.fontRenderer, "press 'P' to unpause!", this.width / 2, this.height / 2 + 85, 0xFF4500);
			}
		}

		super.drawScreen(var1, var2, var3);
	}
}