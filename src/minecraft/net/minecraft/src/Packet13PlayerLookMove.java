package net.minecraft.src;

import java.io.*;

public class Packet13PlayerLookMove extends Packet10Flying {
	public Packet13PlayerLookMove() {
		rotating = true;
		moving = true;
	}

	public Packet13PlayerLookMove(double d, double d1, double d2, double d3, float f, float f1, boolean flag) {
		xPosition = d;
		yPosition = d1;
		stance = d2;
		zPosition = d3;
		yaw = f;
		pitch = f1;
		onGround = flag;
		rotating = true;
		moving = true;
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		xPosition = datainputstream.readDouble();
		yPosition = datainputstream.readDouble();
		stance = datainputstream.readDouble();
		zPosition = datainputstream.readDouble();
		yaw = datainputstream.readFloat();
		pitch = datainputstream.readFloat();
		super.readPacketData(datainputstream);
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeDouble(xPosition);
		dataoutputstream.writeDouble(yPosition);
		dataoutputstream.writeDouble(stance);
		dataoutputstream.writeDouble(zPosition);
		dataoutputstream.writeFloat(yaw);
		dataoutputstream.writeFloat(pitch);
		super.writePacketData(dataoutputstream);
	}

	public int getPacketSize() {
		return 41;
	}
}
