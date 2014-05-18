/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org/>
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
package org.spoutcraft.client.packet.builtin;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import net.minecraft.src.Minecraft;
import net.minecraft.src.ScreenShotHelper;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.packet.PacketType;
import org.spoutcraft.client.packet.SpoutPacket;

public class PacketScreenshot extends SpoutPacket {
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

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		isRequest = input.readBoolean();
		if (!isRequest) {
			int ssLen = input.readInt();
			ssAsPng = new byte[ssLen];
			input.read(ssAsPng);
		}
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		if (ssAsPng == null) {
			output.writeBoolean(true);
		} else {
			output.writeBoolean(false);
			output.writeInt(ssAsPng.length);
			output.write(ssAsPng);
		}
	}

	@Override
	public void run(int playerId) {
		if (!isRequest) {
			return; // we can't do anything!
		}
		try {
			SpoutClient.getInstance().getActivePlayer().showAchievement("Sending screenshot...", "Screenshot requested", 321);
			BufferedImage screenshot = ScreenShotHelper.getScreenshot(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
			PacketScreenshot packet = new PacketScreenshot(screenshot);
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(packet);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			SpoutClient.getInstance().getActivePlayer().showAchievement("Sending screenshot...", "Failed!", 321);
		}
	}

	@Override
	public void failure(int playerId) {
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketScreenshot;
	}

	@Override
	public int getVersion() {
		return 2;
	}
}
