package net.minecraft.src;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ChunkFilePattern
	implements FilenameFilter {
	public static final Pattern dataFilenamePattern = Pattern.compile("c\\.(-?[0-9a-z]+)\\.(-?[0-9a-z]+)\\.dat");

	private ChunkFilePattern() {
	}

	public boolean accept(File file, String s) {
		Matcher matcher = dataFilenamePattern.matcher(s);
		return matcher.matches();
	}

	ChunkFilePattern(Empty2 empty2) {
		this();
	}
}
