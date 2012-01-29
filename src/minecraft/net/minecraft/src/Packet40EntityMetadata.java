package net.minecraft.src;

import java.io.*;
import java.util.List;

public class Packet40EntityMetadata extends Packet {
	public int entityId;
	private List metadata;

	public Packet40EntityMetadata() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		entityId = datainputstream.readInt();
		metadata = DataWatcher.readWatchableObjects(datainputstream);
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(entityId);
		DataWatcher.writeObjectsInListToStream(metadata, dataoutputstream);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleEntityMetadata(this);
	}

	public int getPacketSize() {
		return 5;
	}

	public List getMetadata() {
		return metadata;
	}
}
