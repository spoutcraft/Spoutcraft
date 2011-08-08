package org.getspout.spout.texture;

import java.io.File;

import org.getspout.spout.client.SpoutClient;

import net.minecraft.client.Minecraft;
import net.minecraft.src.TexturePackCustom;
import net.minecraft.src.WorldRenderer;

public class TexturePackAction implements Runnable {
	private final File texturePackFile;
	
	public TexturePackAction(String filename, File directory) {
		this.texturePackFile = new File(directory, filename);
	}
	
	public void run() {
		TexturePackCustom pack = new TexturePackCustom(texturePackFile);
		Minecraft game = SpoutClient.getHandle();
		if (game.renderEngine.oldPack == null) {
			game.renderEngine.oldPack = game.renderEngine.texturePack.selectedTexturePack;
		}
		game.renderEngine.texturePack.setTexturePack(pack);
		game.renderEngine.refreshTextures();
		if (game.renderGlobal != null && game.renderGlobal.worldRenderers != null) {
			WorldRenderer[] renderers = game.renderGlobal.worldRenderers;
			for(int i = 0; i < renderers.length; i++) {
				renderers[i].markDirty();
			}
		}
	}
}