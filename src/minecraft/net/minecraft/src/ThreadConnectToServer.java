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
	
	final String field_48479_b;
	
	final int port;
	
	final GuiConnecting connectingGui;

	ThreadConnectToServer(GuiConnecting par1GuiConnecting, Minecraft par2Minecraft, String par3Str, int par4) {
		this.connectingGui = par1GuiConnecting;
		this.mc = par2Minecraft;
		this.field_48479_b = par3Str;
		this.port = par4;
	}

	// Spout start
	public void run() {
		try {
			GuiConnecting.setNetClientHandler(this.connectingGui, new NetClientHandler(this.mc, this.field_48479_b, this.port));
			if (GuiConnecting.isCancelled(this.connectingGui)) {
				return;
			}

			GuiConnecting.getNetClientHandler(this.connectingGui).addToSendQueue(new Packet2Handshake(this.mc.session.username, this.field_48479_b, this.port));
		} catch (UnknownHostException var2) {
			if (GuiConnecting.isCancelled(this.connectingGui)) {
				return;
			}

			mc.displayGuiScreen(new GuiDisconnected("connect.failed", "disconnect.genericReason", new Object[] { (new StringBuilder()).append("Unknown host '").append(field_48479_b).append("'").toString() }));
		} catch (ConnectException var3) {
			if (GuiConnecting.isCancelled(this.connectingGui)) {
				return;
			}

			mc.displayGuiScreen(new GuiDisconnected("connect.failed", "disconnect.genericReason", new Object[] { var3.getMessage() }));
		} catch (Exception var4) {
			if (GuiConnecting.isCancelled(this.connectingGui)) {
				return;
			}

			var4.printStackTrace();
			mc.displayGuiScreen(new GuiDisconnected("connect.failed", "disconnect.genericReason", new Object[] { var4.toString() }));
		}
	}
	// Spout end
}
