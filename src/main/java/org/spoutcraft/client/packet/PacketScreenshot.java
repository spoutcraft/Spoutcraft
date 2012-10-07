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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ScreenShotHelper;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;

public class PacketScreenshot implements SpoutPacket {
	byte[] ssAsPng = null;
	boolean isRequest = false;

	public PacketScreenshot() {
		isRequest = true;
	}

	public PacketScreenshot(BufferedImage ss) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(ss, "png", baos);
		baos.flush();
		ssAsPng = baos.toByteArray();
		baos.close();
	}

	public int getNumBytes() {
		if (ssAsPng == null) {
			return 1;
		}
		return ssAsPng.length + 5;
	}

	public void readData(SpoutInputStream input) throws IOException {
		isRequest = input.readBoolean();
		if (!isRequest) {
			int ssLen = input.readInt();
			ssAsPng = new byte[ssLen];
			input.read(ssAsPng);
		}
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		if (ssAsPng == null) {
			output.writeBoolean(true);
		} else {
			output.writeBoolean(false);
			output.writeInt(ssAsPng.length);
			output.write(ssAsPng);
		}
	}

	public void run(int playerId) {
		if (!isRequest) {
			return; // we can't do anything!
		}
		try {
			SpoutClient.getInstance().getActivePlayer().showAchievement("Sending screenshot...", "Screenshot requested", 321);
			BufferedImage screenshot = ScreenShotHelper.getScreenshot(Minecraft.theMinecraft.displayWidth, Minecraft.theMinecraft.displayHeight);
			PacketScreenshot packet = new PacketScreenshot(screenshot);
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(packet);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			SpoutClient.getInstance().getActivePlayer().showAchievement("Sending screenshot...", "Failed!", 321);
		}
	}

	public void failure(int playerId) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketScreenshot;
	}

	public int getVersion() {
		return 1;
	}
}
