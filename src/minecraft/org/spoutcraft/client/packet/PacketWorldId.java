package org.spoutcraft.client.packet;

import java.io.IOException;
import java.util.UUID;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.SpoutcraftWorld;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;

public class PacketWorldId implements SpoutPacket {

	UUID worldId;
	
	public PacketWorldId() {
	}
	
	@Override
	public void readData(SpoutInputStream input) throws IOException {
		worldId = input.readUUID();
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeUUID(worldId);
	}

	@Override
	public void run(int playerId) {
		((SpoutcraftWorld) SpoutClient.getInstance().player.getWorld()).setId(worldId);
	}

	@Override
	public void failure(int playerId) {
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketWorldId;
	}

	@Override
	public int getVersion() {
		return 0;
	}

}
