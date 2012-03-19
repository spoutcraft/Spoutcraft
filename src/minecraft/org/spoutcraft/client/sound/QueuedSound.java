/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.sound;

import java.io.File;

import net.minecraft.src.SoundManager;

import org.spoutcraft.client.SpoutClient;

public class QueuedSound implements Runnable {
	File song;
	int x, y, z, volume, distance;
	boolean soundEffect, notify;

	public QueuedSound(File song, int x, int y, int z, int volume, int distance, boolean soundEffect) {
		this.song = song;
		this.x = x;
		this.y = y;
		this.z = z;
		this.volume = volume;
		this.distance = distance;
		this.soundEffect = soundEffect;
	}

	public void setNotify(boolean notify) {
		this.notify = notify;
	}

	public void run() {
		play();
	}

	private boolean play() {
		if (song.exists()) {
			if (notify) {
				SpoutClient.getInstance().getActivePlayer().showAchievement("Download Complete!", song.getName(), 2256 /*Gold Record*/);
			}
			SoundManager sndManager = SpoutClient.getHandle().sndManager;
			if (!sndManager.hasSoundEffect(song.getName().toString(), 0) && soundEffect) {
				sndManager.addCustomSoundEffect(song.getName().toString(), song);
			}
			if (!sndManager.hasMusic(song.getName().toString(), 0) && !soundEffect) {
				sndManager.addCustomMusic(song.getName().toString(), song);
			}
			if (!soundEffect) {
				sndManager.playMusic(song.getName().toString(), 0, x, y, z, volume / 100F, distance);
			} else {
				sndManager.playCustomSoundEffect(song.getName().toString(), x, y, z, volume / 100F, distance);
			}
			return true;
		}
		return false;
	}
}
