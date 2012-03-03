package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;
import net.minecraft.src.WorldType;

import org.spoutcraft.client.DataMiningThread; //Spout

public class Packet1Login extends Packet {

	public int protocolVersion;
	public String username;
	public WorldType terrainType;
	public int serverMode;
	public int field_48170_e;
	public byte difficultySetting;
	public byte worldHeight;
	public byte maxPlayers;

	public Packet1Login() {}

	public Packet1Login(String par1Str, int par2) {
		this.username = par1Str;
		this.protocolVersion = par2;
	}

	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.protocolVersion = par1DataInputStream.readInt();
		this.username = readString(par1DataInputStream, 16);
		String var2 = readString(par1DataInputStream, 16);
		this.terrainType = WorldType.parseWorldType(var2);
		if (this.terrainType == null) {
			this.terrainType = WorldType.field_48635_b;
		}

		this.serverMode = par1DataInputStream.readInt();
		this.field_48170_e = par1DataInputStream.readInt();
		this.difficultySetting = par1DataInputStream.readByte();
		this.worldHeight = par1DataInputStream.readByte();
		this.maxPlayers = par1DataInputStream.readByte();
	}

	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeInt(this.protocolVersion);
		writeString(this.username, par1DataOutputStream);
		if (this.terrainType == null) {
			writeString("", par1DataOutputStream);
		} else {
			writeString(this.terrainType.func_48628_a(), par1DataOutputStream);
		}

		par1DataOutputStream.writeInt(this.serverMode);
		par1DataOutputStream.writeInt(this.field_48170_e);
		par1DataOutputStream.writeByte(this.difficultySetting);
		par1DataOutputStream.writeByte(this.worldHeight);
		par1DataOutputStream.writeByte(this.maxPlayers);
	}

	public void processPacket(NetHandler par1NetHandler) {
		//Spout Start
		DataMiningThread.getInstance().onLogin();
		//Spout End
		par1NetHandler.handleLogin(this);
	}

	public int getPacketSize() {
		int var1 = 0;
		if (this.terrainType != null) {
			var1 = this.terrainType.func_48628_a().length();
		}

		return 4 + this.username.length() + 4 + 7 + 7 + var1;
	}
}
