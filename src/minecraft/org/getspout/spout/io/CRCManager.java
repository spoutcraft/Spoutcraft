package org.getspout.spout.io;

import java.util.HashMap;

public class CRCManager {
	private static HashMap<String, Long> fileNameToCRC = new HashMap<String, Long>();
	
	public static long getCRC(String fileName) {
		Long result = fileNameToCRC.get(fileName);
		if (result == null) {
			return -1;
		}
		return result;
	}
	
	public static void setCRC(String fileName, long crc) {
		fileNameToCRC.put(fileName, crc);
	}
	
	public static void clear() {
		fileNameToCRC.clear();
	}

}
