package org.getspout.spout.texture;

import java.io.File;
import net.minecraft.src.TexturePackCustom;
import net.minecraft.src.WorldRenderer;
import net.minecraft.src.Spout;

public class TexturePackAction implements Runnable {
	private final File texturePackFile;
	
	public TexturePackAction(String filename, File directory) {
		this.texturePackFile = new File(directory, filename);
	}
	
	public void run() {
		TexturePackCustom pack = new TexturePackCustom(texturePackFile);
		if (Spout.getGameInstance().renderEngine.oldPack == null) {
			Spout.getGameInstance().renderEngine.oldPack = Spout.getGameInstance().renderEngine.texturePack.selectedTexturePack;
		}
		Spout.getGameInstance().renderEngine.texturePack.setTexturePack(pack);
		Spout.getGameInstance().renderEngine.refreshTextures();
		if (Spout.getGameInstance().renderGlobal != null && Spout.getGameInstance().renderGlobal.worldRenderers != null) {
			WorldRenderer[] renderers = Spout.getGameInstance().renderGlobal.worldRenderers;
			for(int i = 0; i < renderers.length; i++) {
				renderers[i].markDirty();
			}
		}
	}
}