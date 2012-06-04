package org.spoutcraft.client.packet;

import java.io.IOException;

import net.minecraft.client.Minecraft;

import org.spoutcraft.client.entity.EntityText;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;

public class PacketSpawnTextEntity implements SpoutPacket {
	private String text;
	private double posX, posY, posZ, moveX, moveY, moveZ;
	private int duration;
	private float scale;

	public PacketSpawnTextEntity() {
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		text = input.readString();
		posX = input.readDouble();
		posY = input.readDouble();
		posZ = input.readDouble();
		scale = input.readFloat();
		duration = input.readInt();
		moveX = input.readDouble();
		moveY = input.readDouble();
		moveZ = input.readDouble();
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeString(text);
		output.writeDouble(posX);
		output.writeDouble(posY);
		output.writeDouble(posZ);
		output.writeFloat(scale);
		output.writeInt(duration);
		output.writeDouble(moveX);
		output.writeDouble(moveY);
		output.writeDouble(moveZ);
	}

	@Override
	public void run(int playerId) {
		EntityText entity = new EntityText(Minecraft.theMinecraft.theWorld);
		entity.setPosition(posX, posY, posZ);
		entity.setScale(scale);
		entity.setText(text);
		entity.setRotateWithPlayer(true);
		entity.motionX = moveX;
		entity.motionY = moveY;
		entity.motionZ = moveZ;
		entity.setDuration(duration);
		Minecraft.theMinecraft.theWorld.spawnEntityInWorld(entity);
	}

	@Override
	public void failure(int playerId) {
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketSpawnTextEntity;
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
