package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class MusInputStream extends InputStream {
	private int hash;
	private InputStream inputStream;
	byte buffer[];
	final CodecMus codec;

	public MusInputStream(CodecMus codecmus, URL url, InputStream inputstream) {
		codec = codecmus;

		buffer = new byte[1];
		inputStream = inputstream;
		String s = url.getPath();
		s = s.substring(s.lastIndexOf("/") + 1);
		hash = s.hashCode();
	}

	public int read() {
		int i = read(buffer, 0, 1);
		if (i < 0) {
			return i;
		}
		else {
			return buffer[0];
		}
	}

	public int read(byte abyte0[], int i, int j) {
		try {
			j = inputStream.read(abyte0, i, j);
		}
		catch (IOException e) {
			return 0;
		}
		for (int k = 0; k < j; k++) {
			byte byte0 = abyte0[i + k] ^= hash >> 8;
			hash = hash * 0x1dba038f + 0x14ee3 * byte0;
		}

		return j;
	}
}
