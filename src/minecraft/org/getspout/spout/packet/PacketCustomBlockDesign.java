package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.spoutcraftapi.block.design.BlockDesign;
import org.spoutcraft.spoutcraftapi.block.design.GenericBlockDesign;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

public class PacketCustomBlockDesign implements SpoutPacket {
	
	private short customId;
	private GenericBlockDesign design;
	
	public PacketCustomBlockDesign() {
		
	}
	
	public int getNumBytes() {
		int designBytes = (design == null) ?  (new GenericBlockDesign().getResetNumBytes()) : design.getNumBytes();
		return designBytes + 2;
	}
	
	public void readData(DataInputStream input) throws IOException {
		customId = input.readShort();
		design = new GenericBlockDesign();
		design.read(input);
		if (design.getReset()) {
			design = null;
		}
	}
	
	public void writeData(DataOutputStream output) throws IOException {
		output.writeShort(customId);
		if (design != null) {
			design.write(output);
		}
		else {
			new GenericBlockDesign().writeReset(output);
		}
	}
	
	public void run(int id) {
		CustomBlock block = MaterialData.getCustomBlock(customId);
		if (block != null) {
			BlockDesign old = block.getBlockDesign();
			block.setBlockDesign(design);
			System.out.println("Received Block Design For " + customId + " " + block + ", old design: " + old + " new design " + block.getBlockDesign());
		}
	}


	public void failure(int id) {
		
	}

	public PacketType getPacketType() {
		return PacketType.PacketCustomBlockDesign;
	}


	public int getVersion() {
		return new GenericBlockDesign().getVersion() + 2;
	}

}

