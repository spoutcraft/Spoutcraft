/*
 * This file is part of Spoutcraft (http://spout.org).
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.player.RenderDistance;

import net.minecraft.src.*;
import net.minecraft.client.Minecraft;

public class PacketRenderDistance implements SpoutPacket{
	protected byte view = -1;
	protected byte max = -1;
	protected byte min = -1;
	public PacketRenderDistance() {
		
	}
	
	public PacketRenderDistance(byte view) {
		this.view = view;
	}

	public int getNumBytes() {
		return 3;
	}

	public void readData(DataInputStream input) throws IOException {
		view = input.readByte();
		max = input.readByte();
		min = input.readByte();
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.write(view);
		output.write(max);
		output.write(min);
	}

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


	public PacketType getPacketType() {
		return PacketType.PacketRenderDistance;
	}

	public int getVersion() {
		return 0;
	}

	public void failure(int playerId) {
		
	}

}
