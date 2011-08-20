package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;

public class PacketBiomeWeather implements SpoutPacket {
	public byte biome;
	public byte weather;

	@Override
	public int getNumBytes() {
		return 2;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		biome = input.readByte();
		weather = input.readByte();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeByte(biome);
		output.writeByte(weather);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketBiomeWeather;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
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

	@Override
	public void failure(int playerId) {
		
	}
}
