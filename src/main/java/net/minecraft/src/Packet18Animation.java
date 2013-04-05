package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
// Spout Start
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.packet.PacketFullVersion;
import org.spoutcraft.client.packet.PacketRenderDistance;
// Spout End

public class Packet18Animation extends Packet {

	/** The entity ID, in this case it's the player ID. */
	public int entityId;
	public int animate;
	// Spout Start
	Minecraft mc = SpoutClient.getHandle();
	// Spout End

	public Packet18Animation() {}

	public Packet18Animation(Entity par1Entity, int par2) {
		this.entityId = par1Entity.entityId;
		this.animate = par2;
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.entityId = par1DataInputStream.readInt();
		this.animate = par1DataInputStream.readByte();
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeInt(this.entityId);
		par1DataOutputStream.writeByte(this.animate);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		// Spout Start
		if (entityId == -42) {
			SpoutClient.getInstance().setSpoutVersion(1);
			((NetClientHandler) par1NetHandler).addToSendQueue(this);
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketRenderDistance((byte)Minecraft.theMinecraft.gameSettings.renderDistance));
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketFullVersion(SpoutClient.getClientVersion()));
			System.out.println("Detected SpoutPlugin enabled server.");
			if (this.mc.currentScreen instanceof GuiDownloadTerrain) { 
				this.mc.displayGuiScreen(null, false);
				this.mc.displayGuiScreen(new org.spoutcraft.client.gui.precache.GuiPrecache());
			}
		} else {
			par1NetHandler.handleAnimation(this);
		}
		// Spout End
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 5;
	}
}
