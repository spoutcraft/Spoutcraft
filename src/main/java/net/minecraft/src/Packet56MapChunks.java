package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
// Spout Start
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
// Spout End
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

// Spout Start
import org.spoutcraft.client.chunkcache.ChunkNetCache;
// Spout End

public class Packet56MapChunks extends Packet {
	private int[] field_73589_c;
	private int[] field_73586_d;
	public int[] field_73590_a;
	public int[] field_73588_b;
	private byte[] field_73587_e;
	private byte[][] field_73584_f;
	private int field_73585_g;
	private static byte[] field_73591_h = new byte[0];

	public Packet56MapChunks() {}

	public Packet56MapChunks(List par1List) {
		int var2 = par1List.size();
		this.field_73589_c = new int[var2];
		this.field_73586_d = new int[var2];
		this.field_73590_a = new int[var2];
		this.field_73588_b = new int[var2];
		this.field_73584_f = new byte[var2][];
		int var3 = 0;

		for (int var4 = 0; var4 < var2; ++var4) {
			Chunk var5 = (Chunk)par1List.get(var4);
			Packet51MapChunkData var6 = Packet51MapChunk.getMapChunkData(var5, true, 65535);

			if (field_73591_h.length < var3 + var6.field_74582_a.length) {
				byte[] var7 = new byte[var3 + var6.field_74582_a.length];
				System.arraycopy(field_73591_h, 0, var7, 0, field_73591_h.length);
				field_73591_h = var7;
			}

			System.arraycopy(var6.field_74582_a, 0, field_73591_h, var3, var6.field_74582_a.length);
			var3 += var6.field_74582_a.length;
			this.field_73589_c[var4] = var5.xPosition;
			this.field_73586_d[var4] = var5.zPosition;
			this.field_73590_a[var4] = var6.field_74580_b;
			this.field_73588_b[var4] = var6.field_74581_c;
			this.field_73584_f[var4] = var6.field_74582_a;
		}

		Deflater var11 = new Deflater(-1);

		try {
			var11.setInput(field_73591_h, 0, var3);
			var11.finish();
			this.field_73587_e = new byte[var3];
			this.field_73585_g = var11.deflate(this.field_73587_e);
		} finally {
			var11.end();
		}
	}
	// Spout Start
	private Reference<byte[]> inflateBufferCache = new SoftReference<byte[]>(null);
	// Spout End
	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		short var2 = par1DataInputStream.readShort();
		this.field_73585_g = par1DataInputStream.readInt();
		this.field_73589_c = new int[var2];
		this.field_73586_d = new int[var2];
		this.field_73590_a = new int[var2];
		this.field_73588_b = new int[var2];
		this.field_73584_f = new byte[var2][];

		if (field_73591_h.length < this.field_73585_g) {
			field_73591_h = new byte[this.field_73585_g];
		}

		par1DataInputStream.readFully(field_73591_h, 0, this.field_73585_g);
		
		// Spout Start
		byte[] inflateBuffer = inflateBufferCache.get();
		int requiredLength = 196864 * var2;
		if (inflateBuffer == null || inflateBuffer.length < requiredLength) {
			inflateBuffer = new byte[requiredLength];
			inflateBufferCache = new SoftReference<byte[]>(inflateBuffer);
		}
		Inflater var4 = new Inflater();
		var4.setInput(field_73591_h, 0, this.field_73585_g);

		int length = 0;
		try {
			length = var4.inflate(inflateBuffer);
		} catch (DataFormatException var11) {
			throw new IOException("Bad compressed data format");
		} finally {
			var4.end();
		}
		
		byte[] var3 = ChunkNetCache.handle(inflateBuffer, length, this.field_73585_g, 16 * var2, Integer.MAX_VALUE, Integer.MAX_VALUE);
		// Spout End

		int var5 = 0;

		for (int var6 = 0; var6 < var2; ++var6) {
			this.field_73589_c[var6] = par1DataInputStream.readInt();
			this.field_73586_d[var6] = par1DataInputStream.readInt();
			this.field_73590_a[var6] = par1DataInputStream.readShort();
			this.field_73588_b[var6] = par1DataInputStream.readShort();
			int var7 = 0;
			int var8;

			for (var8 = 0; var8 < 16; ++var8) {
				var7 += this.field_73590_a[var6] >> var8 & 1;
			}

			var8 = 2048 * 5 * var7 + 256;
			this.field_73584_f[var6] = new byte[var8];
			System.arraycopy(var3, var5, this.field_73584_f[var6], 0, var8);
			var5 += var8;
		}
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeShort(this.field_73589_c.length);
		par1DataOutputStream.writeInt(this.field_73585_g);
		par1DataOutputStream.write(this.field_73587_e, 0, this.field_73585_g);

		for (int var2 = 0; var2 < this.field_73589_c.length; ++var2) {
			par1DataOutputStream.writeInt(this.field_73589_c[var2]);
			par1DataOutputStream.writeInt(this.field_73586_d[var2]);
			par1DataOutputStream.writeShort((short)(this.field_73590_a[var2] & 65535));
			par1DataOutputStream.writeShort((short)(this.field_73588_b[var2] & 65535));
		}
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		par1NetHandler.handleMapChunks(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		return 6 + this.field_73585_g + 12 * this.func_73581_d();
	}

	public int func_73582_a(int par1) {
		return this.field_73589_c[par1];
	}

	public int func_73580_b(int par1) {
		return this.field_73586_d[par1];
	}

	public int func_73581_d() {
		return this.field_73589_c.length;
	}

	public byte[] func_73583_c(int par1) {
		return this.field_73584_f[par1];
	}
}
