/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;

public class PacketAllowVisualCheats implements SpoutPacket{
	private boolean cheating = false;
	private boolean sky = false;
	private boolean clearwater = false;
	private boolean cloudheight = false;
	private boolean stars = false;
	private boolean weather = false;
	private boolean time = false;

	public PacketAllowVisualCheats() {

	}
	
	public PacketAllowVisualCheats(boolean allow, boolean sky, boolean clearwater, boolean cloudheight, boolean stars, boolean weather, boolean time) {
		this.cheating = allow;
		this.sky = sky;
		this.clearwater = clearwater;
		this.cloudheight = cloudheight;
		this.stars = stars;
		this.weather = weather;
		this.time = time;
	}

	public int getNumBytes() {
		return 7;
	}

	public void readData(DataInputStream input) throws IOException {
		cheating = input.readBoolean();
                sky = input.readBoolean();
                clearwater = input.readBoolean();
                cloudheight = input.readBoolean();
                stars = input.readBoolean();
                weather = input.readBoolean();
                time = input.readBoolean();    		
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeBoolean(cheating);
                output.writeBoolean(sky);
                output.writeBoolean(clearwater);
                output.writeBoolean(cloudheight);
                output.writeBoolean(stars);
                output.writeBoolean(weather);
                output.writeBoolean(time);	
	}

	public void run(int playerId) {
		SpoutClient.getInstance().setCheatMode(cheating);
		SpoutClient.getInstance().setVisualCheats(sky, clearwater, cloudheight, stars, weather, time);
	}

	public PacketType getPacketType() {
		return PacketType.PacketAllowVisualCheats;
	}
	
	public int getVersion() {
		return 1;
	}

	public void failure(int playerId) {
		
	}

}
