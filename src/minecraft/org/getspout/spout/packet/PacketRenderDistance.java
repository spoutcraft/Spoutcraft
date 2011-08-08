package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.player.RenderDistance;

import net.minecraft.src.*;
import net.minecraft.client.Minecraft;

public class PacketRenderDistance implements SpoutPacket{
	protected byte view;
	protected byte max = -1;
	protected byte min = -1;
	public PacketRenderDistance() {
		
	}
	
	public PacketRenderDistance(byte view) {
		this.view = view;
	}

	@Override
	public int getNumBytes() {
		return 3;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		view = input.readByte();
		max = input.readByte();
		min = input.readByte();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.write(view);
		output.write(max);
		output.write(min);
	}

	@Override
	public void run(int PlayerId) {
		Minecraft game = SpoutClient.getHandle();
		if (game != null) {
				GameSettings settings = game.gameSettings;
				if (view > -1 && view < 4) {
					settings.renderDistance = view;
				}
		}
		if (min > -1 && min < 4)
				SpoutClient.getInstance().getActivePlayer().setMinimumView(RenderDistance.getRenderDistanceFromValue(min));
		if (max > -1 && max < 4)
			SpoutClient.getInstance().getActivePlayer().setMaximumView(RenderDistance.getRenderDistanceFromValue(max));
		if (min == -2)
			SpoutClient.getInstance().getActivePlayer().setMinimumView(RenderDistance.TINY);
		if (max == -2)
			SpoutClient.getInstance().getActivePlayer().setMinimumView(RenderDistance.FAR);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketRenderDistance;
	}

}
