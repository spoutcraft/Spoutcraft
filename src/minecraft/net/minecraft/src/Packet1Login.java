package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;
//Spout Start
import org.getspout.spout.DataMiningThread;
//Spout End

public class Packet1Login extends Packet {

	public int protocolVersion;
	public String username;
	public long mapSeed;
	public int field_35249_d;
	public byte field_35250_e;
	public byte field_35247_f;
	public byte field_35248_g;
	public byte field_35251_h;


	public Packet1Login() {}

	public Packet1Login(String var1, int var2) {
		this.username = var1;
		this.protocolVersion = var2;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.protocolVersion = var1.readInt();
		this.username = readString(var1, 16);
		this.mapSeed = var1.readLong();
		this.field_35249_d = var1.readInt();
		this.field_35250_e = var1.readByte();
		this.field_35247_f = var1.readByte();
		this.field_35248_g = var1.readByte();
		this.field_35251_h = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.protocolVersion);
		writeString(this.username, var1);
		var1.writeLong(this.mapSeed);
		var1.writeInt(this.field_35249_d);
		var1.writeByte(this.field_35250_e);
		var1.writeByte(this.field_35247_f);
		var1.writeByte(this.field_35248_g);
		var1.writeByte(this.field_35251_h);
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
