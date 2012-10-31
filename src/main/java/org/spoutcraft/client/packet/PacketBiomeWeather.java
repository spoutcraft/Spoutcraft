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

public class PacketBiomeWeather implements SpoutPacket {
	public byte biome;
	public byte weather;

	public int getNumBytes() {
		return 2;
	}

	public void readData(SpoutInputStream input) throws IOException {
		biome = (byte) input.read();
		weather = (byte) input.read();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.write(biome);
		output.write(weather);
	}

	public PacketType getPacketType() {
		return PacketType.PacketBiomeWeather;
	}

	public int getVersion() {
		return 0;
	}

	public void run(int PlayerId) {
		String biomeString = "";

		switch(biome) {
			case 0: biomeString = "Rainforest"; break;
			case 1: biomeString = "Swampland"; break;
			case 2: biomeString = "Seasonal Forest"; break;
			case 3: biomeString = "Forest"; break;
			case 4: biomeString = "Savanna"; break;
			case 5: biomeString = "Shrubland"; break;
			case 6: biomeString = "Taiga"; break;
			case 7: biomeString = "Desert"; break;
			case 8: biomeString = "Plains"; break;
			case 9: biomeString = "Ice Desert"; break;
			case 10: biomeString = "Tundra"; break;
			case 11: biomeString = "Hell"; break;
			case 12: biomeString = "Sky"; break;
			case 13: biomeString = "Ocean"; break;
			case 14: biomeString = "River"; break;
			case 15: biomeString = "Extreme Hills"; break;
			case 16: biomeString = "FrozenOcean"; break;
			case 17: biomeString = "FrozenRiver"; break;
			case 18: biomeString = "Ice Plains"; break;
			case 19: biomeString = "Ice Mountains"; break;
			case 20: biomeString = "MushroomIsland"; break;
			case 21: biomeString = "MushroomIslandShore"; break;
			default: break;
		}

		SpoutClient.getInstance().getBiomeManager().resetWeather(biomeString);

		switch(weather) {
			case 0:
				SpoutClient.getInstance().getBiomeManager().setSnowEnabled(biomeString, false);
				SpoutClient.getInstance().getBiomeManager().setRainEnabled(biomeString, false);
				break;
			case 1:
				SpoutClient.getInstance().getBiomeManager().setSnowEnabled(biomeString, false);
				SpoutClient.getInstance().getBiomeManager().setRainEnabled(biomeString, true);
				break;
			case 2:
				SpoutClient.getInstance().getBiomeManager().setSnowEnabled(biomeString, true);
				SpoutClient.getInstance().getBiomeManager().setRainEnabled(biomeString, false);
				break;
			case 3:
				SpoutClient.getInstance().getBiomeManager().resetWeather(biomeString);
				break;
			default:
				SpoutClient.getInstance().getBiomeManager().resetWeather(biomeString);
				break;
		}
	}

	public void failure(int playerId) {
	}
}
