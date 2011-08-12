package org.getspout.spout.io;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileDownloadThread extends Thread{
	private static FileDownloadThread instance = null;
	private final ConcurrentLinkedQueue<Download> downloads = new ConcurrentLinkedQueue<Download>();
	private final ConcurrentLinkedQueue<Runnable> actions = new ConcurrentLinkedQueue<Runnable>();
	private final byte[] buffer = new byte[1024*1024];
	
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
	
	public void addToDownloadQueue(Download download){
		downloads.add(download);
	}
	
	public boolean isDownloading(String url) {
		Iterator<Download> i = downloads.iterator();
		while(i.hasNext()) {
			Download download = i.next();
			if (download.getDownloadUrl().equals(url)) {
				return true;
			}
		}
		return false;
	}
	
	public void onTick() {
		Iterator<Runnable> i = actions.iterator();
		while(i.hasNext()) {
			Runnable action = i.next();
			action.run();
			i.remove();
		}
	}
	
	public void run() {
		while(true) {
			Download next = downloads.poll();
			if (next != null) {
				try {
					if (!next.isDownloaded()) {
						System.out.println("Downloading File: " + next.getDownloadUrl());
						URL url = new URL(next.getDownloadUrl());
						URLConnection conn = url.openConnection();
						InputStream in = conn.getInputStream();
						
						FileOutputStream fos = new FileOutputStream(next.getTempFile());
						
						long length = conn.getContentLength();
						int bytes;
						long totalBytes = 0;
						long last = 0;
						
						long step = Math.max(1024*1024, length / 8);
						
						while ((bytes = in.read(buffer)) >= 0) {
							fos.write(buffer, 0, bytes);
							totalBytes += bytes;
							if (length > 0 && totalBytes > (last + step)) {
								last = totalBytes;
								long mb = totalBytes/(1024*1024);
								System.out.println("Downloading File: " + next.getDownloadUrl() + " " + mb + "MB/" + (length/(1024*1024)));
							}
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								
							}
						}
						in.close();
						fos.close();
						next.move();
						System.out.println("File moved to: " + next.directory.getCanonicalPath());
						try {
							sleep(10); //cool off after heavy network useage
						} catch (InterruptedException e) {}
					}
					if (next.getCompletedAction() != null) {
						actions.add(next.getCompletedAction());
					}
				}
				catch (Exception e) {
					System.out.println("-----------------------");
					System.out.println("Download Failed!");
					e.printStackTrace();
					System.out.println("-----------------------");
				}
			}
			else {
				try {
					sleep(100);
				} catch (InterruptedException e) {}
			}
		}
	}
}
