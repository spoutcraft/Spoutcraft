/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.texture;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.src.TexturePackCustom;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.WorldRenderer;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.MipMapUtils;

public class TexturePackAction implements Runnable {
	private final File texturePackFile;

	public TexturePackAction(String filename, File directory) {
		this.texturePackFile = new File(directory, filename);
	}

	public void run() {
		TexturePackCustom pack = new TexturePackCustom(texturePackFile.getName().substring(0, texturePackFile.getName().length() - 4), texturePackFile, TexturePackList.func_98143_h());
		Minecraft game = SpoutClient.getHandle();
		if (game.renderEngine.oldPack == null) {
			game.renderEngine.oldPack = game.renderEngine.texturePack.selectedTexturePack;
		}
		game.renderEngine.texturePack.setTexturePack(pack);
		game.renderEngine.refreshTextures();
		MipMapUtils.update();
		if (game.renderGlobal != null && game.renderGlobal.worldRenderers != null) {
			WorldRenderer[] renderers = game.renderGlobal.worldRenderers;
			for (int i = 0; i < renderers.length; i++) {
				renderers[i].markDirty();
			}
		}
	}
}
