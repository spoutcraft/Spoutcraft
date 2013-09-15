package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.spoutcraft.client.DataMiningThread;

public class Packet1Login extends Packet {

	/** The player's entity ID */
	public int clientEntityId;
	public WorldType terrainType;
	public boolean hardcoreMode;
	public EnumGameType gameType;

	/** -1: The Nether, 0: The Overworld, 1: The End */
	public int dimension;

	/** The difficulty setting byte. */
	public byte difficultySetting;

	/** Defaults to 128 */
	public byte worldHeight;

	/** The maximum players. */
	public byte maxPlayers;

	public Packet1Login() {}

	public Packet1Login(int par1, WorldType par2WorldType, EnumGameType par3EnumGameType, boolean par4, int par5, int par6, int par7, int par8) {
		this.clientEntityId = par1;
		this.terrainType = par2WorldType;
		this.dimension = par5;
		this.difficultySetting = (byte)par6;
		this.gameType = par3EnumGameType;
		this.worldHeight = (byte)par7;
		this.maxPlayers = (byte)par8;
		this.hardcoreMode = par4;
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public void readPacketData(DataInput par1DataInput) throws IOException {
		this.clientEntityId = par1DataInput.readInt();
		String var2 = readString(par1DataInput, 16);
		this.terrainType = WorldType.parseWorldType(var2);

		if (this.terrainType == null) {
			this.terrainType = WorldType.DEFAULT;
		}

		byte var3 = par1DataInput.readByte();
		this.hardcoreMode = (var3 & 8) == 8;
		int var4 = var3 & -9;
		this.gameType = EnumGameType.getByID(var4);
		this.dimension = par1DataInput.readByte();
		this.difficultySetting = par1DataInput.readByte();
		this.worldHeight = par1DataInput.readByte();
		this.maxPlayers = par1DataInput.readByte();
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutput par1DataOutput) throws IOException {
		par1DataOutput.writeInt(this.clientEntityId);
		writeString(this.terrainType == null ? "" : this.terrainType.getWorldTypeName(), par1DataOutput);
		int var2 = this.gameType.getID();

		if (this.hardcoreMode) {
			var2 |= 8;
		}

		par1DataOutput.writeByte(var2);
		par1DataOutput.writeByte(this.dimension);
		par1DataOutput.writeByte(this.difficultySetting);
		par1DataOutput.writeByte(this.worldHeight);
		par1DataOutput.writeByte(this.maxPlayers);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(NetHandler par1NetHandler) {
		// Spout Start
		DataMiningThread.getInstance().onLogin();
		// Spout End
		par1NetHandler.handleLogin(this);
	}

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public int getPacketSize() {
		int var1 = 0;

		if (this.terrainType != null) {
			var1 = this.terrainType.getWorldTypeName().length();
		}

		return 6 + 2 * var1 + 4 + 4 + 1 + 1 + 1;
	}
}
