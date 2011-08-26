package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;


public class PacketMovementModifiers implements SpoutPacket{
	double gravityMod = 1;
	double walkingMod = 1;
	double swimmingMod = 1;
	
	public PacketMovementModifiers() {
		
	}
	
	public PacketMovementModifiers(double gravity, double walking, double swimming) {
		this.gravityMod = gravity;
		this.walkingMod = walking;
		this.swimmingMod = swimming;
	}

	@Override
	public int getNumBytes() {
		return 24;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		gravityMod = input.readDouble();
		walkingMod = input.readDouble();
		swimmingMod = input.readDouble();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeDouble(gravityMod);
		output.writeDouble(walkingMod);
		output.writeDouble(swimmingMod);
	}

	@Override
	public void run(int playerId) {
		Minecraft.theMinecraft.thePlayer.gravityMod = gravityMod;
		Minecraft.theMinecraft.thePlayer.walkingMod = walkingMod;
		Minecraft.theMinecraft.thePlayer.swimmingMod = swimmingMod;
	}

	@Override
	public void failure(int playerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketMovementModifiers;
	}

	@Override
	public int getVersion() {
		return 0;
	}

}
