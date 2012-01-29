package net.minecraft.src;

public class SaveFormatComparator
	implements Comparable {
	private final String fileName;
	private final String displayName;
	private final long lastTimePlayed;
	private final long sizeOnDisk;
	private final boolean requiresConversion;
	private final int gameType;
	private final boolean hardcore;

	public SaveFormatComparator(String s, String s1, long l, long l1, int i,
	        boolean flag, boolean flag1) {
		fileName = s;
		displayName = s1;
		lastTimePlayed = l;
		sizeOnDisk = l1;
		gameType = i;
		requiresConversion = flag;
		hardcore = flag1;
	}

	public String getFileName() {
		return fileName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public boolean requiresConversion() {
		return requiresConversion;
	}

	public long getLastTimePlayed() {
		return lastTimePlayed;
	}

	public int compareTo(SaveFormatComparator saveformatcomparator) {
		if (lastTimePlayed < saveformatcomparator.lastTimePlayed) {
			return 1;
		}
		if (lastTimePlayed > saveformatcomparator.lastTimePlayed) {
			return -1;
		}
		else {
			return fileName.compareTo(saveformatcomparator.fileName);
		}
	}

	public int getGameType() {
		return gameType;
	}

	public boolean isHardcoreModeEnabled() {
		return hardcore;
	}

	public int compareTo(Object obj) {
		return compareTo((SaveFormatComparator)obj);
	}
}
