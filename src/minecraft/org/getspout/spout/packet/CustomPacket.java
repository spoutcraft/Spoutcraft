package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;

import net.minecraft.src.Packet;
import net.minecraft.src.NetHandler;

public class CustomPacket extends Packet{
	SpoutPacket packet;

	public CustomPacket() {
		
	}
	
	public CustomPacket(SpoutPacket packet) {
		this.packet = packet;
	}

	@Override
	public int getPacketSize() {
		if(packet == null) {
			return 8;
		} else {
			return packet.getNumBytes() + 8;
		}
	}

	@Override
	public void readPacketData(DataInputStream input) throws IOException {
		int packetId = -1;
		packetId = input.readShort();
		int length = input.readInt(); //packet size
		int version = input.readShort(); //packet version
		if (packetId > -1 && version > -1) {
			try {
				this.packet = PacketType.getPacketFromId(packetId).getPacketClass().newInstance();
			}
			catch (Exception e) {
				System.out.println("Failed to identify packet id: " + packetId);
				//e.printStackTrace();
			}
		}
		try {
			if(this.packet == null) {
				input.skipBytes(length);
				System.out.println("Unknown packet " + packetId + ". Skipping contents.");
				return;
			}
			else if (packet.getVersion() != version) {
				input.skipBytes(length);
				System.out.println("Invalid Packet Id: " + packetId + ". Current v: " + packet.getVersion() + " Receieved v: " + version + " Skipping contents.");
			}
			else {
				packet.readData(input);
				//System.out.println("Reading Packet Data for " +  PacketType.getPacketFromId(packetId));
			}
		}
		catch (IOException e) {
			throw new IOException(e);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("readData() for packetId " + packetId + " threw an exception");
		}
	}

	@Override
	public void writePacketData(DataOutputStream output) throws IOException {
		if(packet == null) {
			output.writeShort(-1);
			output.writeInt(0);
			output.writeShort(-1);
			return;
		}
		//System.out.println("Writing Packet Data for " + packet.getPacketType());
		output.writeShort(packet.getPacketType().getId());
		output.writeInt(getPacketSize() - 8);
		output.writeShort(packet.getVersion());
		packet.writeData(output);
	}

	@Override
	public void processPacket(NetHandler netHandler) {
		if(packet != null) {
			packet.run(SpoutClient.getHandle().thePlayer.entityId);
		}
	}
	
	public static void addClassMapping() {
		addIdClassMapping(195, true, true, CustomPacket.class);
	}
}
