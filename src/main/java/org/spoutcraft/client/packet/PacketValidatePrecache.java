package org.spoutcraft.client.packet;

import java.io.File;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.src.MovementInputFromOptions;

import org.spoutcraft.api.gui.ScreenType;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.FileUtil;

public class PacketValidatePrecache implements SpoutPacket {
	
	long crc;
	String serverName;
	
	public PacketValidatePrecache() {
	}
	
	public PacketValidatePrecache(long crc, String serverName) {
		this.crc = crc;
		this.serverName = serverName;
	}
	
	@Override
	public void readData(SpoutInputStream input) throws IOException {
		crc = input.readLong();
		serverName = input.readString();
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeLong(crc);
		output.writeString(serverName);
	}

	@Override
	public void run(int playerId) {
		
		//if crc matches, use ours. if not, request new precache
		String zName;
		if (serverName==null || serverName.isEmpty()) {
			zName = "unnamedserver.zip";
		} else {
			zName = serverName.replaceAll("[^a-zA-Z0-9]", "").trim() + ".zip";
		}
		
		File cachedZip = new File(FileUtil.getCacheDir(), zName);
		long crc = FileUtil.getCRC(cachedZip, new byte[(int) cachedZip.length()]);
		
		System.out.println("Received Precache Validation Request: Server CRC = " + String.valueOf(this.crc) + ", Client CRC = " + String.valueOf(crc));
		
		if (!cachedZip.exists() || crc != this.crc) {
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketRequestPrecache());
		} else {
			FileUtil.loadPrecache(cachedZip);
		}
	}

	@Override
	public void failure(int playerId) {
		
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketValidatePrecache;
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
