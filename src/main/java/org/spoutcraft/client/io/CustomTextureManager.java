/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org/>
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
package org.spoutcraft.client.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import net.minecraft.src.Minecraft;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

import org.spoutcraft.api.block.design.BlockDesign;
import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.material.CustomBlock;
import org.spoutcraft.api.material.CustomItem;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.block.SpoutcraftChunk;
import org.spoutcraft.client.gui.minimap.ZanMinimap;
import org.spoutcraft.client.SpoutcraftWorld;

public class CustomTextureManager {
	static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	static HashMap<String, File> cacheTextureFiles = new HashMap<String, File>();

	public static void downloadTexture(String url) {
		downloadTexture(null, url, false);
	}

	public static void downloadTexture(String url, boolean ignoreEnding) {
		downloadTexture(null, url, ignoreEnding);
	}

	public static void downloadTexture(String plugin, String url, boolean ignoreEnding) {
		String fileName = FileUtil.getFileName(url);
		if (!ignoreEnding && !FileUtil.isImageFile(fileName)) {
			System.out.println("Rejecting download of invalid texture: " + fileName);
		} else if (!isTextureDownloading(url) && !isTextureDownloaded(plugin, url)) {
			File dir = FileUtil.getTempDir();
			if (plugin != null) {
				dir = new File(FileUtil.getCacheDir(), plugin);
				dir.mkdir();
			}
			Download download = new Download(fileName, dir, url, null);
			FileDownloadThread.getInstance().addToDownloadQueue(download);
		}
	}
	
	public static void downloadTexture(String plugin, String url) {
		downloadTexture(plugin, url, false);
	}

	public static boolean isTextureDownloading(String url) {
		return FileDownloadThread.getInstance().isDownloading(url);
	}

	public static boolean isTextureDownloaded(String url) {
		return getTextureFile(null, url).exists();
	}

	public static boolean isTextureDownloaded(String addon, String url) {
		return getTextureFile(addon, url).exists();
	}

	public static File getTextureFile(String plugin, String url) {
		String fileName = FileUtil.getFileName(url);
		File cache = cacheTextureFiles.get(plugin + File.separator + fileName);
		if (cache != null) {
			return cache;
		}
		if (plugin != null) {
			File file = FileUtil.findFile(plugin, fileName);
			if (file != null) {
				cacheTextureFiles.put(plugin + File.separator + fileName, file);
				return file;
			}
		}
		return new File(FileUtil.getTempDir(), fileName);
	}

	public static Texture getTextureFromPath(String path) {
		if (textures.containsKey(path)) {
			return textures.get(path);
		}
		Texture texture = null;
		try {
			FileInputStream stream = new FileInputStream(path);
			if (stream.available() > 0) {
				texture = TextureLoader.getTexture(path.toLowerCase().endsWith(".png") ? "PNG" : "JPG", stream, true,  GL11.GL_NEAREST);
			}
			stream.close();
		} catch (IOException e) {
		}

		if (texture != null) {
			textures.put(path, texture);
		}
		return texture;
	}

	public static Texture getTextureFromJar(String path) {
		if (textures.containsKey(path)) {
			return textures.get(path);
		}
		
		Texture texture = null;
		// Check inside jar
		try {
			InputStream stream = Minecraft.class.getResourceAsStream(path);
			texture = TextureLoader.getTexture(path.toLowerCase().endsWith(".png") ? "PNG" : "JPG", stream, true,  GL11.GL_NEAREST);
			stream.close();
		} catch (Exception e) { }
		// Check MCP/Eclipse Path
		if (texture == null) {
			String pathToJar;
			File jar = new File(CustomTextureManager.class.getProtectionDomain().getCodeSource().getLocation().getFile());
			try {
				pathToJar = jar.getCanonicalPath();
			} catch (IOException e1) {
				pathToJar = jar.getAbsolutePath();
			}
			try {
				pathToJar = URLDecoder.decode(pathToJar, "UTF-8");
			} catch (java.io.UnsupportedEncodingException ignore) { }

			File relative = new File(pathToJar + "/../../src/main/resources" + path.substring(4));

			try {
				pathToJar = relative.getCanonicalPath();
			} catch (IOException e) {
				pathToJar = relative.getAbsolutePath();
			}

			texture = getTextureFromPath(pathToJar);
		}

		if (texture != null) {
			textures.put(path, texture);
		}

		return texture;
	}

	public static void resetTextures() {
		for (Texture texture : textures.values()) {
			texture.release();
		}
		cacheTextureFiles.clear();
		textures.clear();
		ZanMinimap.instance.texman.reset();
	}

	public static Texture getTextureFromUrl(String url) {
		Texture tex = getTextureFromUrl(null, url);
		if (tex == null) {
			CustomTextureManager.downloadTexture(url, true);
			tex = CustomTextureManager.getTextureFromPath(FileUtil.getAssetsDir().getPath()+"/block/spout.png");
		}
		return tex;
	}

	public static Texture getTextureFromUrl(String plugin, String url) {
		File texture = getTextureFile(plugin, url);
		try {
			return getTextureFromPath(texture.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getTexturePathFromUrl(String url) { // Only used for sky, null checks not needed!
		return getTexturePathFromUrl(null, url);
	}

	public static String getTexturePathFromUrl(String plugin, String url) { // Only used above!
		if (!isTextureDownloaded(plugin, url)) {
			return null;
		}
		File download = new File(FileUtil.getTempDir(), FileUtil.getFileName(url));
		try {
			return download.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Don't call this method, for future use.
	protected Texture getTextureFromCoords(World world, int x, int y, int z) {
		short customId = 0;
		Texture texture = null;
		if (SpoutClient.getInstance().getRawWorld() != null) {
			SpoutcraftChunk sChunk = Spoutcraft.getChunkAt(SpoutClient.getInstance().getRawWorld(), x, y, z);
			customId = sChunk.getCustomBlockId(x, y, z);	
			short[] customBlockIds = sChunk.getCustomBlockIds();
			byte[] customBlockData = sChunk.getCustomBlockData();			

			if (customId > 0) {
				CustomBlock block = MaterialData.getCustomBlock(customId);
				if (block != null) {
					BlockDesign design = block.getBlockDesign(customBlockData[customId]);
					if (design != null) {
						texture = getTextureFromUrl(block.getAddon(), design.getTextureURL());					
					}
				}
			}
		}
		return texture;
	}

	// Don't call this method, for future use.
	protected Texture getTextureFromItemStack(ItemStack itemStack) {
		BlockDesign design = null;
		Texture texture = null;
		org.spoutcraft.api.material.CustomItem item = MaterialData.getCustomItem(itemStack.getItemDamage());
		if (item != null) {
			String textureURI = item.getTexture();
			if (textureURI == null) {
				org.spoutcraft.api.material.CustomBlock block = MaterialData.getCustomBlock(itemStack.getItemDamage());
				design = block != null ? block.getBlockDesign() : null;
				textureURI = design != null ? design.getTextureURL() : null;
			}
			if (textureURI != null) {
				texture = CustomTextureManager.getTextureFromUrl(item.getAddon(), textureURI);				
			}			
		}
		return texture;
	}
}