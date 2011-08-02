package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;
//Spout Start
import net.minecraft.client.Minecraft;
import org.getspout.spout.packet.*;
//Spout End

public class Packet3Chat extends Packet {

	public String message;


	public Packet3Chat() {}

	public Packet3Chat(String var1) {
		if(var1.length() > 119) {
			var1 = var1.substring(0, 119);
		}

		this.message = var1;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.message = readString(var1, 119);
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		writeString(this.message, var1);
	}

	public void processPacket(NetHandler nethandler) {
		//Spout Start
		boolean proc = false;
		if (!Spout.isEnabled() || Spout.getReloadPacket() != null) {
			String processed = Spout.colorToString(message);
			System.out.println(processed);
			if (processed.split("\\.").length == 3) {
				Spout.setVersion(processed);
				if (Spout.isEnabled()) {
					proc = true;
					System.out.println("Spout SP Enabled");
					((NetClientHandler)nethandler).addToSendQueue(new Packet3Chat("/" + Spout.getClientVersionString()));
					//Let Spout know we just reloaded
					if (Spout.getReloadPacket() != null) {
						((NetClientHandler)nethandler).addToSendQueue(new CustomPacket(Spout.getReloadPacket()));
						Spout.setReloadPacket(null);
					}
					//Also need to send the render distance
					Minecraft game = Spout.getGameInstance();
					if (game != null && Spout.getVersion() > 5) {
						final GameSettings settings = game.gameSettings;
						((NetClientHandler)nethandler).addToSendQueue(new CustomPacket(new PacketRenderDistance((byte)settings.renderDistance)));
					}
				}
			}
		}
		if (!proc) {
			//Normal message handling
			nethandler.handleChat(this);
		}
		//Spout End
	}

	public int getPacketSize() {
		return this.message.length();
	}
}
