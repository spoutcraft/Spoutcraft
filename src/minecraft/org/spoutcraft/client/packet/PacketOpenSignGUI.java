package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.client.SpoutClient;

import net.minecraft.src.GuiEditSign;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySign;
import net.minecraft.src.World;

public class PacketOpenSignGUI implements SpoutPacket {
	int x,y,z;

	public int getNumBytes() {
		return 12; //Never be too lazy to calculate !
	}


	public void readData(DataInputStream input) throws IOException {
		x = input.readInt();
		y = input.readInt();
		z = input.readInt();
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(x);
		output.writeInt(y);
		output.writeInt(z);
	}

	public void run(int playerId) {
		World world = SpoutClient.getHandle().theWorld;
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te!=null && te instanceof TileEntitySign){
			TileEntitySign sign = (TileEntitySign)te;
			GuiEditSign gui = new GuiEditSign(sign);
			SpoutClient.getHandle().displayGuiScreen(gui);
		}
	}

	public void failure(int playerId) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketOpenSignGUI;
	}

	public int getVersion() {
		return 0;
	}
}
