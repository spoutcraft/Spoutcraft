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
package org.spoutcraft.client.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Download {
	protected final String filename;
	protected final File directory;
	protected final String url;
	protected final Runnable action;
	protected int progress = 0;

	public Download(String filename, File directory, String url, Runnable action) {
		this.filename = filename;
		this.directory = directory;
		this.url = url;
		this.action = action;
	}

	public File getTempFile() {
		File downloading = new File(FileUtil.getTempDir(), "downloading");
		downloading.mkdir();
		return new File(downloading, filename);
	}

	public boolean isDownloaded() {
		return (new File(directory, filename)).exists();
	}

	public void move() {
		File current = getTempFile();
		if (current.exists()) {
			File destination = new File(directory, filename);
			try {
				FileUtils.moveFile(current, destination);
			} catch (IOException e) {}
		}
	}

	public String getDownloadUrl() {
		return url;
	}

	public Runnable getCompletedAction() {
		return action;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Download) {
			Download temp = (Download)obj;
			return temp.filename.equals(this.filename) && temp.directory.getPath().equals(this.directory.getPath()) && temp.url.equals(this.url);
		}
		return false;
	}

	synchronized public void setProgress(int p) {
		progress = p;
	}

	synchronized public int getProgress() {
		return progress;
	}
}
