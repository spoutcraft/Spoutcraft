package org.spoutcraft.client.packet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import net.java.games.util.plugins.Plugin;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.precache.GuiPrecache;
import org.spoutcraft.client.io.FileUtil;
import org.spoutcraft.client.precache.PrecacheManager;
import org.spoutcraft.client.precache.PrecacheTuple;

public class PacketValidatePrecache implements SpoutPacket {
	
	int count;
	PrecacheTuple[] plugins;
	
	public PacketValidatePrecache() {
	}
	
	@Override
	public void readData(SpoutInputStream input) throws IOException {
		count = input.readInt();
		plugins = new PrecacheTuple[count];
		if (count > 0) {
			for(int i = 0; i<count; i++) {
				String plugin = input.readString();
				String version = input.readString();
				long crc = input.readLong();
				plugins[i] = new PrecacheTuple(plugin, version, crc);
			}
		}
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(count);
		for (int i=0; i<count; i++) {
			output.writeString(plugins[i].getPlugin());
			output.writeString(plugins[i].getVersion());
			output.writeLong(plugins[i].getCrc());
		}
	}

	@Override
	public void run(int playerId) {
		
		PrecacheManager.showPreloadGui();
		
		PrecacheManager.reset();
		
		//fill precache list
		for (PrecacheTuple plugin : plugins) {
			PrecacheManager.addPlugin(plugin);
		}
		
		if(PrecacheManager.hasNextCache()) {
			PrecacheManager.doNextCache();
		} else {
			PrecacheManager.loadPrecache(false);
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
