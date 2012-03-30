/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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

public class PacketAllowVisualCheats implements SpoutPacket {
	private boolean sky = false;
	private boolean clearwater = false;
	private boolean stars = false;
	private boolean weather = false;
	private boolean time = false;
	private boolean coords = false;
	private boolean entitylabel = false;
	private boolean voidfog = false;

	public PacketAllowVisualCheats() {

	}

	public PacketAllowVisualCheats(boolean tsky, boolean tclearwater, boolean tstars, boolean tweather, boolean ttime, boolean tcoords, boolean tentitylabel, boolean tvoidfog) {
		this.sky = tsky;
		this.clearwater = tclearwater;
		this.stars = tstars;
		this.weather = tweather;
		this.time = ttime;
		this.coords = tcoords;
		this.entitylabel = tentitylabel;
		this.voidfog = tvoidfog;
	}

	public int getNumBytes() {
		return 8;
	}

	public void readData(DataInputStream input) throws IOException {
		this.sky = input.readBoolean();
		this.clearwater = input.readBoolean();
		this.stars = input.readBoolean();
		this.weather = input.readBoolean();
		this.time = input.readBoolean();
		this.coords = input.readBoolean();
		this.entitylabel = input.readBoolean();
		this.voidfog = input.readBoolean();
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeBoolean(sky);
		output.writeBoolean(clearwater);
		output.writeBoolean(stars);
		output.writeBoolean(weather);
		output.writeBoolean(time);
		output.writeBoolean(coords);
		output.writeBoolean(entitylabel);
		output.writeBoolean(voidfog);
	}

	public void run(int playerId) {
		SpoutClient.getInstance().setVisualCheats(sky, clearwater, stars, weather, time, coords, entitylabel, voidfog);
	}

	public PacketType getPacketType() {
		return PacketType.PacketAllowVisualCheats;
	}

	public int getVersion() {
		return 3;
	}

	public void failure(int playerId) {

	}
}
