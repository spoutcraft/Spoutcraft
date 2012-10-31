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
import java.io.File;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.Download;
import org.spoutcraft.client.io.FileDownloadThread;
import org.spoutcraft.client.io.FileUtil;
import org.spoutcraft.client.sound.QueuedSound;

public class PacketDownloadMusic implements SpoutPacket {
	int x, y, z;
	int volume, distance;
	boolean soundEffect, notify;
	String url, plugin;
	public PacketDownloadMusic() {
	}

	public void readData(SpoutInputStream input) throws IOException {
		url = input.readString();
		plugin = input.readString();
		distance = input.readInt();
		x = input.readInt();
		y = input.readInt();
		z = input.readInt();
		volume = input.readInt();
		soundEffect = input.readBoolean();
		notify = input.readBoolean();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeString(url);
		output.writeString(plugin);
		output.writeInt(distance);
		output.writeInt(x);
		output.writeInt(y);
		output.writeInt(z);
		output.writeInt(volume);
		output.writeBoolean(soundEffect);
		output.writeBoolean(notify);
	}

	public void run(int PlayerId) {
		File directory = new File(FileUtil.getTempDir(), plugin);
		if (!directory.exists()) {
			directory.mkdir();
		}
		String fileName = FileUtil.getFileName(url);
		if (!FileUtil.isAudioFile(fileName)) {
			System.out.println("Rejecting download of invalid audio: " + fileName);
			return;
		}
		File song = FileUtil.findFile(plugin, fileName);
		if (song != null && song.exists()) {
			QueuedSound action = new QueuedSound(song, x, y, z, volume, distance, soundEffect);
			action.run();
			return;
		} else {
			song = new File(directory, fileName);
		}

		QueuedSound action = new QueuedSound(song, x, y, z, volume, distance, soundEffect);
		Download download = new Download(fileName, directory, url, action);
		action.setNotify(!download.isDownloaded() && notify);
		if (!download.isDownloaded() && notify) {
			SpoutClient.getInstance().getActivePlayer().showAchievement("Downloading Music...", fileName, 2256 /*Gold Record*/);
		}
		FileDownloadThread.getInstance().addToDownloadQueue(download);
	}

	public PacketType getPacketType() {
		return PacketType.PacketDownloadMusic;
	}

	public int getVersion() {
		return 0;
	}

	public void failure(int playerId) {
	}
}
