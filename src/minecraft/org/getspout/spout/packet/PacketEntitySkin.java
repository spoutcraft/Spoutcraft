package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.entity.EntityManager;

public class PacketEntitySkin implements SpoutPacket {
	protected String texture = "";
	protected int entityId;
	protected boolean isMainTexture = true;
	@Override
	public int getNumBytes() {
		return PacketUtil.getNumBytes(texture) + 4 + 1;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		entityId = input.readInt();
		isMainTexture = input.readBoolean();
		texture = PacketUtil.readString(input);
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(entityId);
		output.writeBoolean(isMainTexture);
		PacketUtil.writeString(output, texture);
	}

	@Override
	public void run(int PlayerId) {
		if(texture.equals("[reset]")){
			texture = null;
		}
		EntityManager manager = SpoutClient.getInstance().getEntityManager();
		if(isMainTexture){
			manager.setTexture(entityId, texture);
		} else {
			manager.setAlternateTexture(entityId, texture);
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketEntitySkin;
	}

	@Override
	public int getVersion() {
		return 0;
	}

}
