package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;
//Spout start
import org.spoutcraft.client.SpoutClient;
//Spout end

public class Packet51MapChunk extends Packet {
	public int xCh;
	public int zCh;
	public int yChMin;
	public int yChMax;
	public byte[] chunkData;
	public boolean includeInitialize;
	private int tempLength;
	private int field_48178_h;
	private static byte[] temp = new byte[0];

	public Packet51MapChunk() {
		this.isChunkDataPacket = true;
	}

	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.xCh = par1DataInputStream.readInt();
		this.zCh = par1DataInputStream.readInt();
		this.includeInitialize = par1DataInputStream.readBoolean();
		this.yChMin = par1DataInputStream.readShort();
		this.yChMax = par1DataInputStream.readShort();
		this.tempLength = par1DataInputStream.readInt();
		this.field_48178_h = par1DataInputStream.readInt();
		if (temp.length < this.tempLength) {
			temp = new byte[this.tempLength];
		}

		par1DataInputStream.readFully(temp, 0, this.tempLength);
		int var2 = 0;

		int var3;
		for (var3 = 0; var3 < 16; ++var3) {
			var2 += this.yChMin >> var3 & 1;
		}

		var3 = 12288 * var2;
		if (this.includeInitialize) {
			var3 += 256;
		}

		this.chunkData = new byte[var3];
		Inflater var4 = new Inflater();
		var4.setInput(temp, 0, this.tempLength);

		try {
			var4.inflate(this.chunkData);
			// Spout - start
			
			//System.out.println("Loading Chunk (" + field_48177_a + ", " + field_48175_b + ")");
			//TODO: fix!
			//if (SpoutClient.getInstance().isSpoutEnabled())
			//	this.chunk = org.spoutcraft.client.chunkcache.ChunkCache.handle(this.chunk, var3, this.chunkSize, xPosition >> 4, zPosition >> 4);
			// Spout - end
		} catch (DataFormatException var9) {
			throw new IOException("Bad compressed data format");
		} finally {
			var4.end();
		}
	}

	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeInt(this.xCh);
		par1DataOutputStream.writeInt(this.zCh);
		par1DataOutputStream.writeBoolean(this.includeInitialize);
		par1DataOutputStream.writeShort((short)(this.yChMin & 65535));
		par1DataOutputStream.writeShort((short)(this.yChMax & 65535));
		par1DataOutputStream.writeInt(this.tempLength);
		par1DataOutputStream.writeInt(this.field_48178_h);
		par1DataOutputStream.write(this.chunkData, 0, this.tempLength);
	}

	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.func_48487_a(this);
	}

	public int getPacketSize() {
		return 17 + this.tempLength;
	}
}
