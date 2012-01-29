package net.minecraft.src;

public class ChunkBlockMap {
	private static byte convTable[];

	public ChunkBlockMap() {
	}

	public static void removeUnknownBlockIDs(byte abyte0[]) {
		for (int i = 0; i < abyte0.length; i++) {
			abyte0[i] = convTable[abyte0[i] & 0xff];
		}
	}

	static {
		convTable = new byte[256];
		try {
			for (int i = 0; i < 256; i++) {
				byte byte0 = (byte)i;
				if (byte0 != 0 && Block.blocksList[byte0 & 0xff] == null) {
					byte0 = 0;
				}
				convTable[i] = byte0;
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
