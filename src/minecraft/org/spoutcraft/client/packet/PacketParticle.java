package org.spoutcraft.client.packet;

import java.io.IOException;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityFX;

import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;
import org.spoutcraft.spoutcraftapi.util.Location;
import org.spoutcraft.spoutcraftapi.util.Vector;

public class PacketParticle implements SpoutPacket{
	String name;
	Location location;
	Vector motion;
	float scale, gravity, particleRed, particleBlue, particleGreen;
	int maxAge, amount;
	
	@Override
	public void readData(SpoutInputStream input) throws IOException {
		name = input.readString();
		location = input.readLocation();
		motion = input.readVector();
		scale = input.readFloat();
		gravity = input.readFloat();
		particleRed = input.readFloat();
		particleBlue = input.readFloat();
		particleGreen = input.readFloat();
		maxAge = input.readInt();
		amount = Math.min(1000, input.readInt());
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
	
	}

	@Override
	public void run(int playerId) {
		Random r = new Random();
		for (int i = 0; i < amount; i++) {
			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();
			if (amount > 1)	{
				x = location.getX() + (r.nextBoolean() ? 2 : -2) * r.nextFloat();
				y = location.getY() + (r.nextBoolean() ? 2 : -2) * r.nextFloat();
				z = location.getZ() + (r.nextBoolean() ? 2 : -2) * r.nextFloat();
			}
			EntityFX particle = Minecraft.theMinecraft.renderGlobal.func_40193_b(name, x, y, z, motion.getX(), motion.getY(), motion.getZ());
			if (particle != null) {
				if (scale > 0) {
					particle.particleScale = scale;
				}
				particle.particleGravity = gravity;
				if (particleRed >= 0F && particleRed <= 1F) {
					particle.particleRed = particleRed;
				}
				if (particleBlue >= 0F && particleBlue <= 1F) {
					particle.particleBlue = particleBlue;
				}
				if (particleGreen >= 0F && particleGreen <= 1F) {
					particle.particleGreen = particleGreen;
				}
				particle.particleMaxAge = maxAge;
			}
		}
	}

	@Override
	public void failure(int playerId) {

	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketParticle;
	}

	@Override
	public int getVersion() {
		return 0;
	}

}
