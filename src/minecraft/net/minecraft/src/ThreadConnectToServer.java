package net.minecraft.src;

import java.net.ConnectException;
import java.net.UnknownHostException;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiDisconnected;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet2Handshake;

class ThreadConnectToServer extends Thread {
	final Minecraft mc;
	
	final String ip;
	
	final int port;
	
	final GuiConnecting connectingGui;

	ThreadConnectToServer(GuiConnecting par1GuiConnecting, Minecraft par2Minecraft, String par3Str, int par4) {
		this.connectingGui = par1GuiConnecting;
		this.mc = par2Minecraft;
		this.ip = par3Str;
		this.port = par4;
	}

	public void run() {
		try {
			GuiConnecting.setNetClientHandler(this.connectingGui, new NetClientHandler(this.mc, this.ip, this.port));
			if (GuiConnecting.isCancelled(this.connectingGui)) {
				return;
			}

			// Spout - start
			GuiConnecting.getNetClientHandler(this.connectingGui).addToSendQueue(new Packet250CustomPayload("REGISTER", "AutoProto:HShake"));
			GuiConnecting.getNetClientHandler(this.connectingGui).addToSendQueue(new Packet250CustomPayload("REGISTER", "ChkCache:setHash"));
			GuiConnecting.getNetClientHandler(this.connectingGui).addToSendQueue(new Packet250CustomPayload("AutoProto:HShake", "VanillaProtocol"));
			// Spout - end
			GuiConnecting.getNetClientHandler(this.connectingGui).addToSendQueue(new Packet2Handshake(this.mc.session.username, this.ip, this.port));
		} catch (UnknownHostException var2) {
			if (GuiConnecting.isCancelled(this.connectingGui)) {
				return;
			}

			displayConnectionIssue(ip, port, "Unknown host \'" + this.ip + "\'");
		} catch (ConnectException var3) {
			if (GuiConnecting.isCancelled(this.connectingGui)) {
				return;
			}

			//Spout start
			if (var3.getMessage().toLowerCase().contains("connection refused")) {
				displayConnectionIssue(ip, port, "The server is not currently online!");
			} else {
				displayConnectionIssue(ip, port, var3.getMessage());
			}
			//Spout end
		} catch (Exception var4) {
			if (GuiConnecting.isCancelled(this.connectingGui)) {
				return;
			}

			var4.printStackTrace();
			//Spout Start
			displayConnectionIssue(ip, port, var4.toString());
			//Spout End
		}
	}
	
	//Spout Start
	private void displayConnectionIssue(String ip, int port, String message) {
		org.spoutcraft.client.gui.error.GuiConnectionLost.lastServerIp = ip;
		org.spoutcraft.client.gui.error.GuiConnectionLost.lastServerPort = port;
		this.mc.displayGuiScreen(new org.spoutcraft.client.gui.error.GuiConnectionLost(message));
	}
	//Spout End
}
