package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;
//Spout Start
import org.spoutcraft.client.DataMiningThread;

public class Packet1Login extends Packet {

	public int protocolVersion;
	public String username;
	public long mapSeed;
	public int serverMode;
	public byte worldType;
	public byte difficultySetting;
	public byte worldHeight;
	public byte maxPlayers;


	public Packet1Login() {}

	public Packet1Login(String var1, int var2) {
		this.username = var1;
		this.protocolVersion = var2;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.protocolVersion = var1.readInt();
		this.username = readString(var1, 16);
		this.mapSeed = var1.readLong();
		this.serverMode = var1.readInt();
		this.worldType = var1.readByte();
		this.difficultySetting = var1.readByte();
		this.worldHeight = var1.readByte();
		this.maxPlayers = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.protocolVersion);
		writeString(this.username, var1);
		var1.writeLong(this.mapSeed);
		var1.writeInt(this.serverMode);
		var1.writeByte(this.worldType);
		var1.writeByte(this.difficultySetting);
		var1.writeByte(this.worldHeight);
		var1.writeByte(this.maxPlayers);
	}

	public void processPacket(NetHandler var1) {
		//Spout Start
		DataMiningThread.getInstance().onLogin();
		//Spout End
		var1.handleLogin(this);
	}

	public int getPacketSize() {
		return 4 + this.username.length() + 4 + 7 + 4;
	}
}
