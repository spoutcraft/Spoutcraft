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

public class PacketAllowVisualCheats implements SpoutPacket {
	private boolean sky = false;
	private boolean clearwater = false;
	private boolean cloudheight = false;
	private boolean stars = false;
	private boolean weather = false;
	private boolean time = false;
	private boolean coords = false;
	private boolean brightness = false;
	private boolean entitylabel = false;
	private boolean renderdistance = false;

	public PacketAllowVisualCheats() {

	}
	
	public PacketAllowVisualCheats(boolean tsky, boolean tclearwater, boolean tcloudheight, boolean tstars, boolean tweather, boolean ttime, boolean tcoords, boolean tbrightness, boolean tentitylabel, boolean trenderdistance) {
		this.sky = tsky;
		this.clearwater = tclearwater;
		this.cloudheight = tcloudheight;
		this.stars = tstars;
		this.weather = tweather;
		this.time = ttime;
		this.coords = tcoords;
		this.brightness = tbrightness;
		this.entitylabel = tentitylabel;
		this.renderdistance = trenderdistance;
	}

	public int getNumBytes() {
		return 10;
	}

	public void readData(DataInputStream input) throws IOException {
            this.sky = input.readBoolean();
            this.clearwater = input.readBoolean();
            this.cloudheight = input.readBoolean();
            this.stars = input.readBoolean();
            this.weather = input.readBoolean();
            this.time = input.readBoolean();  
            this.coords = input.readBoolean();
            this.brightness = input.readBoolean();
            this.entitylabel = input.readBoolean();
            this.renderdistance = input.readBoolean();
	}

	public void writeData(DataOutputStream output) throws IOException {
            output.writeBoolean(sky);
            output.writeBoolean(clearwater);
            output.writeBoolean(cloudheight);
            output.writeBoolean(stars);
            output.writeBoolean(weather);
            output.writeBoolean(time);
            output.writeBoolean(coords);
            output.writeBoolean(brightness);
            output.writeBoolean(entitylabel);
            output.writeBoolean(renderdistance);
	}

	public void run(int playerId) {
		SpoutClient.getInstance().setVisualCheats(sky, clearwater, cloudheight, stars, weather, time, coords, brightness, entitylabel, renderdistance);
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
