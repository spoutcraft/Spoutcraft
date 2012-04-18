package org.spoutcraft.client.chunkcache;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class HeightMapSaveThread extends Thread {
	
	private Queue<HeightMap> saveQueue = new LinkedBlockingDeque<HeightMap>();
	private boolean running;

	public void addMap(HeightMap heightMap) {
		synchronized (saveQueue) {			
			saveQueue.add(heightMap);
		}
		if(!running) {
			start();
		}
	}
	
	@Override
	public void run() {
		running = true;
		synchronized (saveQueue) {
			while(!saveQueue.isEmpty()) {
				saveQueue.poll().save();
			}
		}
		running = false;
	}
}
