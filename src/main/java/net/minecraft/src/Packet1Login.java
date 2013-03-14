package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
// Spout Start
import org.spoutcraft.client.DataMiningThread;
// Spout End

public class Packet1Login extends Packet {

	/** The player's entity ID */
	public int clientEntityId = 0;
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
	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.clientEntityId = par1DataInputStream.readInt();
		String var2 = readString(par1DataInputStream, 16);
		this.terrainType = WorldType.parseWorldType(var2);

		if (this.terrainType == null) {
			this.terrainType = WorldType.DEFAULT;
		}

		byte var3 = par1DataInputStream.readByte();
		this.hardcoreMode = (var3 & 8) == 8;
		int var4 = var3 & -9;
		this.gameType = EnumGameType.getByID(var4);
		this.dimension = par1DataInputStream.readByte();
		this.difficultySetting = par1DataInputStream.readByte();
		this.worldHeight = par1DataInputStream.readByte();
		this.maxPlayers = par1DataInputStream.readByte();
	}

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeInt(this.clientEntityId);
		writeString(this.terrainType == null ? "" : this.terrainType.getWorldTypeName(), par1DataOutputStream);
		int var2 = this.gameType.getID();

		if (this.hardcoreMode) {
			var2 |= 8;
		}

		par1DataOutputStream.writeByte(var2);
		par1DataOutputStream.writeByte(this.dimension);
		par1DataOutputStream.writeByte(this.difficultySetting);
		par1DataOutputStream.writeByte(this.worldHeight);
		par1DataOutputStream.writeByte(this.maxPlayers);
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
