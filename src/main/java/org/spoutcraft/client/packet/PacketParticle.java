/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.packet;

import java.io.IOException;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityFX;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.api.util.Location;
import org.spoutcraft.api.util.Vector;

public class PacketParticle implements SpoutPacket {
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
				x += (r.nextBoolean() ? 2 : -2) * r.nextFloat();
				y += (r.nextBoolean() ? 2 : -2) * r.nextFloat();
				z += (r.nextBoolean() ? 2 : -2) * r.nextFloat();
			}
			EntityFX particle = Minecraft.theMinecraft.renderGlobal.func_40193_b(name, x, y, z, motion.getX(), motion.getY(), motion.getZ(), 256D);
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
