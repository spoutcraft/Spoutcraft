package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException; // Spout

public class Packet250CustomPayload extends Packet {
	public String channel;
	public int length;
	public byte[] data;
	
	// Spout - start - they really should at least have a constructor
	public Packet250CustomPayload() {
	}
	
	public Packet250CustomPayload(String channel, String data) {
		this(channel, getBytes(data));
	}
	
	public Packet250CustomPayload(String channel, byte[] data) {
		if (data == null) {
			data = new byte[0];
		} else if (channel == null) {
			channel = "";
		}
		this.channel = channel;
		this.data = data;
		this.length = data.length;
	}
	
	private static byte[] getBytes(String data) {
			try {
				return data.getBytes("UTF8");
			} catch (UnsupportedEncodingException e) {
				return new byte[0];
			}
	}
	// Spout - end

	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.channel = readString(par1DataInputStream, 16);
		this.length = par1DataInputStream.readShort();

		if (this.length > 0 && this.length < 32767) {
			this.data = new byte[this.length];
			par1DataInputStream.readFully(this.data);
		}
	}

	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		writeString(this.channel, par1DataOutputStream);
		par1DataOutputStream.writeShort((short)this.length);

		if (this.data != null) {
			par1DataOutputStream.write(this.data);
		}
	}

	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleCustomPayload(this);
	}

	public int getPacketSize() {
		return 2 + this.channel.length() * 2 + 2 + this.length;
	}
}
