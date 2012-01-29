package net.minecraft.src;

import java.io.File;
import java.util.List;

public class SaveHandlerMP
	implements ISaveHandler {
	public SaveHandlerMP() {
	}

	public WorldInfo loadWorldInfo() {
		return null;
	}

	public void checkSessionLock() {
	}

	public IChunkLoader getChunkLoader(WorldProvider worldprovider) {
		return null;
	}

	public void saveWorldInfoAndPlayer(WorldInfo worldinfo, List list) {
	}

	public void saveWorldInfo(WorldInfo worldinfo) {
	}

	public File getMapFile(String s) {
		return null;
	}

	public String getSaveDirectoryName() {
		return "none";
	}
}
