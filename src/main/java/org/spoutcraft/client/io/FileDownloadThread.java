/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class FileDownloadThread extends Thread {
	private static FileDownloadThread instance = null;
	private final ConcurrentLinkedQueue<Download> downloads = new ConcurrentLinkedQueue<Download>();
	private final ConcurrentLinkedQueue<Runnable> actions = new ConcurrentLinkedQueue<Runnable>();
	private final HashSet<String> failedUrls = new HashSet<String>();
	private final byte[] buffer = new byte[1024*1024];
	private volatile String activeDownload = null;
	private boolean spoutDebug = false;
	public static AtomicLong preCacheCompleted = new AtomicLong(0L);

	protected FileDownloadThread() {
		super("File Download Thread");
	}

	public static FileDownloadThread getInstance() {
		if (instance == null) {
			instance = new FileDownloadThread();
			instance.start();
		}
		return instance;
	}

	public void addToDownloadQueue(Download download) {
		downloads.add(download);
	}

	public boolean isDownloading(String url) {
		Iterator<Download> i = downloads.iterator();
		while (i.hasNext()) {
			Download download = i.next();
			if (download.getDownloadUrl().equals(url)) {
				return true;
			}
		}
		return false;
	}

	public void onTick() {
		if (!actions.isEmpty()) {
			Iterator<Runnable> i = actions.iterator();
			while (i.hasNext()) {
				Runnable action = i.next();
				try {
					action.run();
				} catch(Exception e) {
					System.out.println("Could not run Runnable for download finish:");
					e.printStackTrace();
				}
				i.remove();
			}
		}
	}

	public void abort() {
		this.interrupt();
		downloads.clear();
		actions.clear();
		failedUrls.clear();
	}

	public String getActiveDownload() {
		return activeDownload;
	}

	public int getDownloadsRemaining() {
		return downloads.size();
	}

	public void run() {
		while (true) {
			Download next = downloads.poll();
			if (next != null && !failedUrls.contains(next.getDownloadUrl())) {
				try {
					if (!next.isDownloaded()) {
						if (spoutDebug) {
							System.out.println("Downloading File: " + next.getDownloadUrl());
						}
						activeDownload = FileUtil.getFileName(next.getDownloadUrl());
						URL url = new URL(next.getDownloadUrl());
						URLConnection conn = url.openConnection();
						conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
						conn.setReadTimeout(10000); //10s timeout
						InputStream in = conn.getInputStream();

						FileOutputStream fos = new FileOutputStream(next.getTempFile());
						BufferedOutputStream bos = new BufferedOutputStream(fos);

						long length = conn.getContentLength();
						int bytes;
						long totalBytes = 0;
						long last = 0;

						long step = Math.max(1024*1024, length / 8);

						while ((bytes = in.read(buffer)) >= 0) {
							bos.write(buffer, 0, bytes);
							totalBytes += bytes;
							next.setProgress((int) (((double)totalBytes / (double)length) * 100));
							if (length > 0 && totalBytes > (last + step)) {
								last = totalBytes;
								long mb = totalBytes/(1024*1024);
								if (spoutDebug) {
									System.out.println("Downloading: " + next.getDownloadUrl() + " " + mb + "MB/" + (length/(1024*1024)));
								}
							}
							try {
								Thread.sleep(25);
							} catch (InterruptedException e) {
							}
						}
						in.close();
						bos.close();
						next.move();
						if (spoutDebug) {
							System.out.println("File moved to: " + next.directory.getCanonicalPath());
						}
						try {
							sleep(10); // Cool off after heavy network useage
						} catch (InterruptedException e) {}
					}
					if (next.getCompletedAction() != null) {
						actions.add(next.getCompletedAction());
					}
				} catch (Exception e) {
					failedUrls.add(next.getDownloadUrl());
					if (spoutDebug) {
						System.out.println("Download of " + next.getDownloadUrl() + " Failed!");
					}
				}
				activeDownload = null;
			} else {
				try {
					sleep(100);
				} catch (InterruptedException e) {}
			}
		}
	}
}
