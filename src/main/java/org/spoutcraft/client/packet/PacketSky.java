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

import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.api.player.SkyManager;
import org.spoutcraft.client.SpoutClient;

public class PacketSky implements SpoutPacket {
	private int cloudY, stars, sunPercent, moonPercent;
	private Color skyColor, fogColor, cloudColor;
	String sun = "";
	String moon = "";
	public PacketSky() {
	}

	public PacketSky(int cloudY, int stars, int sunPercent, int moonPercent, Color skyColor) {
		this.cloudY = cloudY;
		this.stars = stars;
		this.sunPercent = sunPercent;
		this.moonPercent = moonPercent;
		this.skyColor = skyColor.clone();
	}

	public PacketSky(String sunUrl, String moonUrl) {
		this.cloudY = 0;
		this.stars = 0;
		this.sunPercent = 0;
		this.moonPercent = 0;
		this.sun = sunUrl;
		this.moon = moonUrl;
	}

	public void readData(SpoutInputStream input) throws IOException {
		cloudY = input.readInt();
		stars = input.readInt();
		sunPercent = input.readInt();
		moonPercent = input.readInt();
		sun = input.readString();
		moon = input.readString();
		skyColor = input.readColor();
		fogColor = input.readColor();
		cloudColor = input.readColor();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(cloudY);
		output.writeInt(stars);
		output.writeInt(sunPercent);
		output.writeInt(moonPercent);
		output.writeString(sun);
		output.writeString(moon);
		output.writeColor(skyColor);
		output.writeColor(fogColor);
		output.writeColor(cloudColor);
	}

	public void run(int PlayerId) {
		if (cloudY != 0) {
			SpoutClient.getInstance().getSkyManager().setCloudHeight(cloudY);
		}
		if (stars != 0) {
			SpoutClient.getInstance().getSkyManager().setStarFrequency(stars);
		}
		if (sunPercent != 0) {
			SpoutClient.getInstance().getSkyManager().setSunSizePercent(sunPercent);
		}
		if (moonPercent != 0) {
			SpoutClient.getInstance().getSkyManager().setMoonSizePercent(moonPercent);
		}
		if (sun != null) {
			if (sun.equals("[reset]")) {
				SpoutClient.getInstance().getSkyManager().setSunTextureUrl(null);
			} else if (sun.length() > 5) {
				SpoutClient.getInstance().getSkyManager().setSunTextureUrl(sun);
			}
		}
		if (moon != null) {
			if (moon.equals("[reset]")) {
				SpoutClient.getInstance().getSkyManager().setMoonTextureUrl(null);
			} else if (moon.length() > 5) {
				SpoutClient.getInstance().getSkyManager().setMoonTextureUrl(moon);
			}
		}
		SkyManager sky = SpoutClient.getInstance().getSkyManager();

		// Sky
		if (skyColor.isOverride()) {
			sky.setSkyColor(null);
		} else if (!skyColor.isInvalid()) {
			sky.setSkyColor(skyColor);
		}

		// Fog
		if (fogColor.isOverride()) {
			sky.setFogColor(null);
		} else if (!fogColor.isInvalid()) {
			sky.setFogColor(fogColor);
		}

		// Cloud
		if (cloudColor.isOverride()) {
			sky.setCloudColor(null);
		} else if (!cloudColor.isInvalid()) {
			sky.setCloudColor(cloudColor);
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketSky;
	}

	public int getVersion() {
		return 2;
	}

	public void failure(int playerId) {
	}
}
