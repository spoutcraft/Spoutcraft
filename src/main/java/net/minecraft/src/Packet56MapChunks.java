package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
// Spout Start
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import org.spoutcraft.client.chunkcache.ChunkNetCache;
// Spout End

public class Packet56MapChunks extends Packet {
	private int[] chunkPostX;
	private int[] chunkPosZ;
	public int[] field_73590_a;
	public int[] field_73588_b;

	/** The compressed chunk data buffer */
	private byte[] chunkDataBuffer;
	private byte[][] field_73584_f;

	/** total size of the compressed data */
	private int dataLength;
	
	/**
	 * Whether or not the chunk data contains a light nibble array. This is true in the main world, false in the end +
	 * nether.
	 */
	private boolean skyLightSent;
	private static byte[] chunkDataNotCompressed = new byte[0];

	public Packet56MapChunks() {}

	public Packet56MapChunks(List par1List) {
		int var2 = par1List.size();
		this.chunkPostX = new int[var2];
		this.chunkPosZ = new int[var2];
		this.field_73590_a = new int[var2];
		this.field_73588_b = new int[var2];
		this.field_73584_f = new byte[var2][];
		this.skyLightSent = !par1List.isEmpty() && !((Chunk)par1List.get(0)).worldObj.provider.hasNoSky;
		int var3 = 0;

		for (int var4 = 0; var4 < var2; ++var4) {
			Chunk var5 = (Chunk)par1List.get(var4);
			Packet51MapChunkData var6 = Packet51MapChunk.getMapChunkData(var5, true, 65535);

			if (chunkDataNotCompressed.length < var3 + var6.compressedData.length) {
				byte[] var7 = new byte[var3 + var6.compressedData.length];
				System.arraycopy(chunkDataNotCompressed, 0, var7, 0, chunkDataNotCompressed.length);
				chunkDataNotCompressed = var7;
			}

			System.arraycopy(var6.compressedData, 0, chunkDataNotCompressed, var3, var6.compressedData.length);
			var3 += var6.compressedData.length;
			this.chunkPostX[var4] = var5.xPosition;
			this.chunkPosZ[var4] = var5.zPosition;
			this.field_73590_a[var4] = var6.chunkExistFlag;
			this.field_73588_b[var4] = var6.chunkHasAddSectionFlag;
			this.field_73584_f[var4] = var6.compressedData;
		}

		Deflater var11 = new Deflater(-1);

		try {
			var11.setInput(chunkDataNotCompressed, 0, var3);
			var11.finish();
			this.chunkDataBuffer = new byte[var3];
			this.dataLength = var11.deflate(this.chunkDataBuffer);
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
		this.dataLength = par1DataInputStream.readInt();
		this.skyLightSent = par1DataInputStream.readBoolean();
		this.chunkPostX = new int[var2];
		this.chunkPosZ = new int[var2];
		this.field_73590_a = new int[var2];
		this.field_73588_b = new int[var2];
		this.field_73584_f = new byte[var2][];

		if (chunkDataNotCompressed.length < this.dataLength) {
			chunkDataNotCompressed = new byte[this.dataLength];
		}

		par1DataInputStream.readFully(chunkDataNotCompressed, 0, this.dataLength);

		// Spout Start
		byte[] inflateBuffer = inflateBufferCache.get();
		int requiredLength = 196864 * var2;
		if (inflateBuffer == null || inflateBuffer.length < requiredLength) {
			inflateBuffer = new byte[requiredLength];
			inflateBufferCache = new SoftReference<byte[]>(inflateBuffer);
		}
		Inflater var4 = new Inflater();
		var4.setInput(chunkDataNotCompressed, 0, this.dataLength);

		int length = 0;
		try {
			length = var4.inflate(inflateBuffer);
		} catch (DataFormatException var12) {
			throw new IOException("Bad compressed data format");
		} finally {
			var4.end();
		}

		byte[] var3 = ChunkNetCache.handle(inflateBuffer, length, this.dataLength, 16 * var2, Integer.MAX_VALUE, Integer.MAX_VALUE);
		// Spout End

		int var5 = 0;

		for (int var6 = 0; var6 < var2; ++var6) {
			this.chunkPostX[var6] = par1DataInputStream.readInt();
			this.chunkPosZ[var6] = par1DataInputStream.readInt();
			this.field_73590_a[var6] = par1DataInputStream.readShort();
			this.field_73588_b[var6] = par1DataInputStream.readShort();
			int var7 = 0;
			int var8 = 0;
			int var9;

			for (var9 = 0; var9 < 16; ++var9) {
				var7 += this.field_73590_a[var6] >> var9 & 1;
				var8 += this.field_73588_b[var6] >> var9 & 1;
			}

			var9 = 2048 * 4 * var7 + 256;
			var9 += 2048 * var8;

			if (this.skyLightSent) {
				var9 += 2048 * var7;
			}

			this.field_73584_f[var6] = new byte[var9];
			System.arraycopy(var3, var5, this.field_73584_f[var6], 0, var9);
			var5 += var9;
		}
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeShort(this.chunkPostX.length);
		par1DataOutputStream.writeInt(this.dataLength);
		par1DataOutputStream.writeBoolean(this.skyLightSent);
		par1DataOutputStream.write(this.chunkDataBuffer, 0, this.dataLength);

		for (int var2 = 0; var2 < this.chunkPostX.length; ++var2) {
			par1DataOutputStream.writeInt(this.chunkPostX[var2]);
			par1DataOutputStream.writeInt(this.chunkPosZ[var2]);
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
		return 6 + this.dataLength + 12 * this.getNumberOfChunkInPacket();
	}

	public int getChunkPosX(int par1) {
		return this.chunkPostX[par1];
	}

	public int getChunkPosZ(int par1) {
		return this.chunkPosZ[par1];
	}

	public int getNumberOfChunkInPacket() {
		return this.chunkPostX.length;
	}

	public byte[] getChunkCompressedData(int par1) {
		return this.field_73584_f[par1];
	}
}
