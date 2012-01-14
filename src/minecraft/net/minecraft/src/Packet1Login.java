package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.EnumWorldType;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;
//Spout Start
import org.spoutcraft.client.DataMiningThread;

public class Packet1Login extends Packet {

	public int protocolVersion;
	public String username;
	public long mapSeed;
	public EnumWorldType field_46032_d;
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
		String var2 = readString(var1, 16);
		this.field_46032_d = EnumWorldType.func_46135_a(var2);
		if(this.field_46032_d == null) {
			this.field_46032_d = EnumWorldType.DEFAULT;
		}
		
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
		if(this.field_46032_d == null) {
			writeString("", var1);
		} else {
			writeString(this.field_46032_d.name(), var1);
		}
		
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
		int var1 = 0;
		if(this.field_46032_d != null) {
			var1 = this.field_46032_d.name().length();
		}

		return 4 + this.username.length() + 4 + 7 + 4 + var1;
	}
}
