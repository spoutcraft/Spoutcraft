/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;

import net.minecraft.src.Packet;
import net.minecraft.src.NetHandler;

public class CustomPacket extends Packet{
	public SpoutPacket packet;
	private boolean success = false;
	private static final int[] nags;
	protected static final int NAG_MSG_AMT = 10;
	protected static boolean outdated = false;
	
	static {
		nags = new int[PacketType.values().length];
		for (int i = 0; i < PacketType.values().length; i++) {
			nags[i] = NAG_MSG_AMT;
		}
	}

	public CustomPacket() {
		
	}
	
	public CustomPacket(SpoutPacket packet) {
		this.packet = packet;
	}


	public int getPacketSize() {
		if(packet == null) {
			return 8;
		} else {
			return packet.getNumBytes() + 8;
		}
	}


	public void readPacketData(DataInputStream input) throws IOException {
		final boolean prevOutdated = outdated;
		int packetId = -1;
		packetId = input.readShort();
		int version = input.readShort(); //packet version
		int length = input.readInt(); //packet size
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
				//Keep server admins from going insane :p
				if (nags[packetId]-- > 0) {
					System.out.println("Invalid Packet Id: " + packetId + ". Current v: " + packet.getVersion() + " Receieved v: " + version + " Skipping contents.");
				}
				outdated = outdated ? true : version > packet.getVersion();
			}
			else {
				packet.readData(input);
				success = true;
			}
		}
		catch (IOException e) {
			throw new IOException(e);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("readData() for packetId " + packetId + " threw an exception");
		}
		if (prevOutdated != outdated) {
			SpoutClient.getInstance().getActivePlayer().showAchievement("Update Available!", "New Spoutcraft update!", 323 /*Sign*/);
		}
	}


	public void writePacketData(DataOutputStream output) throws IOException {
		if(packet == null) {
			output.writeShort(-1);
			output.writeShort(-1);
			output.writeInt(0);
			return;
		}
		//System.out.println("Writing Packet Data for " + packet.getPacketType());
		output.writeShort(packet.getPacketType().getId());
		output.writeShort(packet.getVersion());
		output.writeInt(getPacketSize() - 8);
		packet.writeData(output);
	}


	public void processPacket(NetHandler netHandler) {
		if(packet != null) {
			if (success) {
				try {
					packet.run(SpoutClient.getHandle().thePlayer.entityId);
				}
				catch (Exception e) {
					System.out.println("------------------------");
					System.out.println("Unexpected Exception: " + e.getClass());
					e.printStackTrace();
					System.out.println("------------------------");
				}
			}
			else {
				try {
					packet.failure(SpoutClient.getHandle().thePlayer.entityId);
				}
				catch (Exception e) {
					System.out.println("------------------------");
					System.out.println("Unexpected Exception: " + e.getClass());
					e.printStackTrace();
					System.out.println("------------------------");
				}
			}
			
		}
	}
	
	public static void addClassMapping() {
		addIdClassMapping(195, true, true, CustomPacket.class);
	}
}
