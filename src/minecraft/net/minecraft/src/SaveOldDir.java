package net.minecraft.src;

import java.io.File;
import java.util.List;

public class SaveOldDir extends SaveHandler {
	public SaveOldDir(File file, String s, boolean flag) {
		super(file, s, flag);
	}

	public IChunkLoader getChunkLoader(WorldProvider worldprovider) {
		File file = getSaveDirectory();
		if (worldprovider instanceof WorldProviderHell) {
			File file1 = new File(file, "DIM-1");
			file1.mkdirs();
			return new ThreadedChunkLoader(file1);
		}
		if (worldprovider instanceof WorldProviderEnd) {
			File file2 = new File(file, "DIM1");
			file2.mkdirs();
			return new ThreadedChunkLoader(file2);
		}
		else {
			return new ThreadedChunkLoader(file);
		}
	}

	public void saveWorldInfoAndPlayer(WorldInfo worldinfo, List list) {
		worldinfo.setSaveVersion(19132);
		super.saveWorldInfoAndPlayer(worldinfo, list);
	}
}
