/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org//>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
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
package org.spoutcraft.client.packet;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.PacketDecompressionThread;
import org.spoutcraft.client.SpoutClient;

public class CustomPacket extends Packet {
	public SpoutPacket packet;
	private boolean success = false;
	private static final int[] nags;
	protected static final int NAG_MSG_AMT = 10;
	protected static boolean outdated = false;

	static {
		int packets = PacketType.values()[PacketType.values().length - 1].getId();
		nags = new int[packets];
		for (int i = 0; i < packets; i++) {
			nags[i] = NAG_MSG_AMT;
		}
	}

	public CustomPacket() {
	}

	public CustomPacket(SpoutPacket packet) {
		this.packet = packet;
	}

	public int getPacketSize() {
		//if (packet == null) {
			return 8;
		//} else {
		//	return packet.getNumBytes() + 8;
		//}
	}

	@Override
	public void readPacketData(DataInput var1) throws IOException {
		SpoutClient.getInstance().setSpoutActive(true);
		final boolean prevOutdated = outdated;
		int packetId;
		packetId = var1.readShort();
		int version = var1.readShort(); // Packet version
		int length = var1.readInt(); // Packet size
		//System.out.println("Reading Packet: " + PacketType.getPacketFromId(packetId) + " Size: " + length + " bytes, version: " + version);
		if (packetId > -1 && version > -1) {
			try {
				this.packet = PacketType.getPacketFromId(packetId).getPacketClass().newInstance();
			} catch (Exception e) {
				System.out.println("Failed to identify packet ID: " + packetId);
				//e.printStackTrace();
			}
		}
		try {
			if (this.packet == null) {
				var1.skipBytes(length);
				System.out.println("Unknown packet " + packetId + ". Skipping contents.");
				return;
			} else if (packet.getVersion() != version) {
				var1.skipBytes(length);
				// Keep server admins from going insane
				if (nags[packetId]-- > 0) {
					System.out.println("Invalid Packet ID: " + packetId + ". Current v: " + packet.getVersion() + " Receieved v: " + version + " Skipping contents.");
				}
				outdated = outdated || version > packet.getVersion();
			} else {
				byte[] data = new byte[length];
				var1.readFully(data);

				SpoutInputStream stream = new SpoutInputStream(ByteBuffer.wrap(data));
				packet.readData(stream);
				success = true;
			}
		} catch (Exception e) {
			System.out.println("------------------------");
			System.out.println("Unexpected Exception: " + PacketType.getPacketFromId(packetId) + ", " + packetId);
			e.printStackTrace();
			System.out.println("------------------------");
		}
		if (prevOutdated != outdated) {
			SpoutClient.getInstance().getActivePlayer().showAchievement("Update Available!", "New Spoutcraft update!", 323 /*Sign*/);
		}
	}

	@Override
	public void writePacketData(DataOutput var1) throws IOException {
		if (packet == null) {
			var1.writeShort(-1);
			var1.writeShort(-1);
			var1.writeInt(0);
			return;
		}
		//System.out.println("Writing Packet Data for " + packet.getPacketType());
		var1.writeShort(packet.getPacketType().getId());
		var1.writeShort(packet.getVersion());
		final SpoutOutputStream stream = new SpoutOutputStream();
		packet.writeData(stream);
		ByteBuffer buffer = stream.getRawBuffer();
		byte[] data = new byte[buffer.capacity() - buffer.remaining()];
		System.arraycopy(buffer.array(), 0, data, 0, data.length);

		var1.writeInt(data.length);
		var1.write(data, 0, data.length);
	}

	@Override
	public void processPacket(NetHandler netHandler) {
		if (packet != null) {
			if (success) {
				if (packet instanceof CompressablePacket) {
					PacketDecompressionThread.add((CompressablePacket)packet);
				} else {
					SpoutClient.getHandle().mcProfiler.startSection("spoutpacket_" + packet.getClass().getSimpleName());
					try {
						packet.run(SpoutClient.getHandle().thePlayer.entityId);
					} catch (Exception e) {
						System.out.println("------------------------");
						System.out.println("Unexpected Exception: " + packet.getPacketType());
						e.printStackTrace();
						System.out.println("------------------------");
					}
					SpoutClient.getHandle().mcProfiler.endSection();
				}
			} else {
				try {
					packet.failure(SpoutClient.getHandle().thePlayer.entityId);
				} catch (Exception e) {
					System.out.println("------------------------");
					System.out.println("Unexpected Exception: " + packet.getPacketType());
					e.printStackTrace();
					System.out.println("------------------------");
				}
			}
		}
	}
}
