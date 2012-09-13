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

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.packet.PacketFullVersion;
import org.spoutcraft.client.packet.PacketRenderDistance;
import org.spoutcraft.client.packet.PacketClientAddons;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.addon.ServerAddon;
// Spout End

public class Packet18Animation extends Packet {

	/** The entity ID, in this case it's the player ID. */
	public int entityId;
	public int animate;

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
			List<Addon> addons = new ArrayList<Addon>(Arrays.asList(SpoutClient.getInstance().getAddonManager().getAddons()));
			for (Iterator<Addon> i = addons.iterator(); i.hasNext(); ) {
				Addon a = i.next();
				if(!Spoutcraft.getAddonStore().isEnabled(a) || (a instanceof ServerAddon)) i.remove();
			}
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketClientAddons(addons.toArray(new Addon[0])));
			System.out.println("Detected Spout server.");
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
