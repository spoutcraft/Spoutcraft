package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.File;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.Download;
import org.getspout.spout.io.FileUtil;
import org.getspout.spout.io.FileDownloadThread;
import org.getspout.spout.sound.QueuedSound;

public class PacketDownloadMusic implements SpoutPacket{
	int x, y, z;
	int volume, distance;
	boolean soundEffect, notify;
	String url, plugin;
	public PacketDownloadMusic() {
		
	}

	@Override
	public int getNumBytes() {
		return 22 + PacketUtil.getNumBytes(url) + PacketUtil.getNumBytes(plugin);
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		url = PacketUtil.readString(input, 255);
		plugin = PacketUtil.readString(input, 255);
		distance = input.readInt();
		x = input.readInt();
		y = input.readInt();
		z = input.readInt();
		volume = input.readInt();
		soundEffect = input.readBoolean();
		notify = input.readBoolean();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		PacketUtil.writeString(output, url);
		PacketUtil.writeString(output, plugin);
		output.writeInt(distance);
		output.writeInt(x);
		output.writeInt(y);
		output.writeInt(z);
		output.writeInt(volume);
		output.writeBoolean(soundEffect);
		output.writeBoolean(notify);
	}


	@Override
	public void run(int PlayerId) {
		File directory = new File(FileUtil.getAudioCacheDirectory(), plugin);
		if (!directory.exists()){	
			directory.mkdir();
		}
		String fileName = FileUtil.getFileName(url);
		if (!FileUtil.isAudioFile(fileName)) {
			System.out.println("Rejecting download of invalid audio: " + fileName);
			return;
		}
		File song = new File(directory, fileName);
		QueuedSound action = new QueuedSound(song, x, y, z, volume, distance, soundEffect);
		Download download = new Download(fileName, directory, url, action);
		action.setNotify(!download.isDownloaded() && notify);
		if (!download.isDownloaded() && notify) {
			SpoutClient.getInstance().getActivePlayer().showAchievement("Downloading Music...", fileName, 2256 /*Gold Record*/);
		}
		FileDownloadThread.getInstance().addToDownloadQueue(download);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketDownloadMusic;
	}

}
