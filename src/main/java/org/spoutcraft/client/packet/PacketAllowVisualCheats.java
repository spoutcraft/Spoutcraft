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

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;

public class PacketAllowVisualCheats implements SpoutPacket {
	private boolean cheatsky = false;
	private boolean forcesky = false;
	private boolean showsky = true;
	private boolean cheatclearwater = false;
	private boolean forceclearwater = false;	
	private boolean showclearwater = false;
	private boolean cheatstars = false;
	private boolean forcestars = false;
	private boolean showstars = true;
	private boolean cheatweather = false;
	private boolean forceweather = false;
	private boolean showweather = true;
	private boolean time = false;
	private boolean coords = false;
	private boolean entitylabel = false;
	private boolean cheatvoidfog = false;
	private boolean forcevoidfog = false;
	private boolean showvoidfog = true;
	private boolean flyspeed = false;

	public PacketAllowVisualCheats() {
	}

	public PacketAllowVisualCheats(boolean tsky, boolean fsky, boolean ssky, boolean tclearwater, boolean fclearwater, boolean sclearwater, boolean tstars, boolean fstars, boolean sstars, boolean tweather, boolean fweather, boolean sweather, boolean ttime, boolean tcoords, boolean tentitylabel, boolean tvoidfog, boolean fvoidfog, boolean svoidfog, boolean tflyspeed) {
		this.cheatsky = tsky;
		this.forcesky = fsky;
		this.showsky = ssky;
		this.cheatclearwater = tclearwater;
		this.forceclearwater = fclearwater;
		this.showclearwater = sclearwater;
		this.cheatstars = tstars;
		this.forcestars = fstars;
		this.showstars = sstars;
		this.cheatweather = tweather;
		this.forceweather = fweather;
		this.showweather = sweather;
		this.time = ttime;
		this.coords = tcoords;
		this.entitylabel = tentitylabel;
		this.cheatvoidfog = tvoidfog;
		this.forcevoidfog = fvoidfog;
		this.showvoidfog = svoidfog;
		this.flyspeed = tflyspeed;
	}

	public void readData(SpoutInputStream input) throws IOException {
		cheatsky = input.readBoolean();
		forcesky = input.readBoolean();
		showsky = input.readBoolean();
		cheatclearwater = input.readBoolean();
		forceclearwater = input.readBoolean();
		showclearwater = input.readBoolean();
		cheatstars = input.readBoolean();
		forcestars = input.readBoolean();
		showstars = input.readBoolean();
		cheatweather = input.readBoolean();
		forceweather = input.readBoolean();
		showweather = input.readBoolean();
		time = input.readBoolean();
		coords = input.readBoolean();
		entitylabel = input.readBoolean();
		cheatvoidfog = input.readBoolean();
		forcevoidfog = input.readBoolean();
		showvoidfog = input.readBoolean();
		flyspeed = input.readBoolean();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeBoolean(cheatsky);
		output.writeBoolean(forcesky);
		output.writeBoolean(showsky);
		output.writeBoolean(cheatclearwater);		
		output.writeBoolean(forceclearwater);
		output.writeBoolean(showclearwater);
		output.writeBoolean(cheatstars);
		output.writeBoolean(forcestars);
		output.writeBoolean(showstars);
		output.writeBoolean(cheatweather);
		output.writeBoolean(forceweather);
		output.writeBoolean(showweather);
		output.writeBoolean(time);
		output.writeBoolean(coords);
		output.writeBoolean(entitylabel);
		output.writeBoolean(cheatvoidfog);
		output.writeBoolean(forcevoidfog);
		output.writeBoolean(showvoidfog);
		output.writeBoolean(flyspeed);
	}

	public void run(int playerId) {		
		SpoutClient.getInstance().setVisualCheats(cheatsky, forcesky, showsky, cheatclearwater, forceclearwater, showclearwater, cheatstars, forcestars, showstars, cheatweather, forceweather, showweather, time, coords, entitylabel, cheatvoidfog, forcevoidfog, showvoidfog, flyspeed);
	}

	public PacketType getPacketType() {
		return PacketType.PacketAllowVisualCheats;
	}

	public int getVersion() {
		return 4;
	}

	public void failure(int playerId) {
	}
}
