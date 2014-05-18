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
package org.spoutcraft.client.precache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.newdawn.slick.opengl.Texture;

import net.minecraft.src.Minecraft;
import net.minecraft.src.GuiDownloadTerrain;

import org.bukkit.ChatColor;

import org.spoutcraft.api.block.design.GenericBlockDesign;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.material.CustomBlock;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.precache.GuiPrecache;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.io.FileUtil;
import org.spoutcraft.client.packet.builtin.PacketRequestPrecache;
import org.spoutcraft.client.packet.builtin.PacketPreCacheCompleted;

public class PrecacheManager {
	public static boolean spoutDebug = false;
	/**
	 * PrecacheTuple - Holds Plugin Name, Version, and CRC
	 * Boolean - Is this cached or not?
	 */
	public static HashMap<PrecacheTuple, Boolean> plugins = new HashMap<PrecacheTuple, Boolean>();
	/**
	 * Adds a plugin tuple to the hashmap, and checks if it's cached and valid
	 * @param plugin
	 */
	public static void addPlugin(PrecacheTuple plugin) {
		// Grab precache file
		File target = getPluginPreCacheFile(plugin);
		// Does it exist locally?
		if (target.exists()) {
			// Is the CRC the same as the one sent from SpoutPlugin?
			if (plugin.getCrc() == FileUtil.getCRC(target, new byte[(int) target.length()])) {
				// It's cached, continue on
				plugins.put(plugin, true);
				return;
			}
		}
		// Either it doesn't exist or CRC failed, either or it isn't cached.
		File temp = new File(FileUtil.getCacheDir(), plugin.getPlugin());
		if (temp.exists() && temp.isDirectory()) {
			FileUtil.deleteDirectory(temp);
		}
		plugins.put(plugin, false);
	}

	/**
	 * Resets the plugins. Useful to clear out previous entries when starting a new login sequence.
	 */
	public static void reset() {
		plugins.clear();
	}

	/**
	 * Returns the tuple that matches to parameters
	 * @param plugin
	 * @param version
	 */
	public static PrecacheTuple getPrecacheTuple(String plugin, String version) {
		for (Entry entry : plugins.entrySet()) {
			PrecacheTuple tuple = (PrecacheTuple)entry.getKey();
			if (tuple.getPlugin().equalsIgnoreCase(plugin) && tuple.getVersion().equalsIgnoreCase(version)) {
				return tuple;
			}
		}
		return null;
	}

	/**
	 * Sets the given plugin precache tuple to a cached status.
	 * @param plugin
	 */
	public static void setCached(PrecacheTuple plugin) {
		plugins.put(plugin, true);
	}

	/**
	 * Checks if there is a plugin precache still needing to be cached.
	 * @return true if a plugin needs to be cached, false is everything is already cached.
	 */
	public static boolean hasNextCache() {
		for (boolean cached : plugins.values()) {
			if (!cached) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the next item to be cached
	 * @return The PrecacheTuple to cache or null if not found
	 */
	public static PrecacheTuple getNextToCache() {
		// Loop through the list of plugins
		for (Entry entry : plugins.entrySet()) {
			// Get the status of the entry (cached or not)
			if ((Boolean) entry.getValue()) {
				// It's cached, continue
				continue;
			}
			// It wasn't cached so return this entry to be cached
			return (PrecacheTuple) entry.getKey();
		}
		// Nothing is left to cache, return null
		return null;
	}

	/**
	 * Starts the next cache
	 */
	public static void doNextCache() {
		// Check if there is any thing left to be cached
		if (!hasNextCache()) {
			// Nothing is left to cache, proceed to load the cache
			loadPrecache();
			return;
		}
		final PrecacheTuple next = getNextToCache();

		// Let the user know we are precaching
		if (spoutDebug) {
			setPreloadGuiText(ChatColor.BLUE + "Spoutcraft" + "\n" + " " + "\n" + ChatColor.WHITE + "Downloading Custom Content for:  " + ChatColor.ITALIC + next.getPlugin() + " " + next.getVersion());
		}
		// Send SpoutPlugin a request for the pre-cache zip
		SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketRequestPrecache(next.getPlugin()));
	}

	public static File getPluginPreCacheFile(PrecacheTuple plugin) {
		return getPluginPreCacheFile(plugin.getPlugin(), plugin.getVersion());
	}

	public static File getPluginPreCacheFile(String plugin, String version) {
		return new File(FileUtil.getCacheDir(), plugin + ".zip");
	}

	public static void loadPrecache() {
		// Unzip
		final File cacheRoot = FileUtil.getCacheDir();

		for (Entry entry : plugins.entrySet()) {
			// Grab the tuple
			final PrecacheTuple toCache = (PrecacheTuple) entry.getKey();
			final File extractDir = new File(cacheRoot, toCache.getPlugin()); //Ex. /cache/pluginname/
			if (spoutDebug) {
				System.out.println("[Spoutcraft] Reading: " + extractDir.getName() + ".zip");
			}
			// Make the directories to unzip to
			extractDir.mkdirs();
			try  {
				// Read in a zip stream
				final ZipInputStream stream = new ZipInputStream(new FileInputStream(getPluginPreCacheFile(toCache)));
				final ReadableByteChannel read = Channels.newChannel(stream);
				// Grab the first entry in the zip
				ZipEntry inner = stream.getNextEntry();
				while (inner != null) {
					// Construct an output stream for the entry
					final File toExtract = new File(extractDir, inner.getName());
					if (!toExtract.exists()) {
						final FileOutputStream write = new FileOutputStream(toExtract);
						write.getChannel().transferFrom(read, 0, Long.MAX_VALUE);
						// Close the writable buffer
						write.close();
					}
					// Close the zip stream for this entry
					stream.closeEntry();
					// Grab the next entry in the zip
					inner = stream.getNextEntry();
				}
				// Finally close the stream altogether
				stream.close();
			} catch (Exception e ) {
				e.printStackTrace();
			}
		}
		for (Entry entry : plugins.entrySet()) {
			final File dir = new File(cacheRoot, ((PrecacheTuple) entry.getKey()).getPlugin());
			if (!dir.isDirectory()) {
				continue;
			}
			final File[] files = dir.listFiles();
			for (File file : files) {
				if (file.getName().endsWith(".sbd")) {
					if (spoutDebug) {
						System.out.println("[Spoutcraft] Loading Spout Block Design: " + file.getName() + " from: " + file.getParent());
					}
					loadDesign(file);
				} else if (FileUtil.isImageFile(file.getName())) {
					if (spoutDebug) {
						System.out.println("[Spoutcraft] Loading image: " + file.getName() + " from: " + file.getParent());
					}
					Texture tex = CustomTextureManager.getTextureFromUrl(((PrecacheTuple) entry.getKey()).getPlugin(),file.getName());
					if (spoutDebug && tex == null) {
						System.out.println("[Spoutcraft] Precache tried to load a null image: " + tex);
					}
				}
			}
		}

		if (Minecraft.getMinecraft().theWorld != null) {
			Minecraft.getMinecraft().renderGlobal.updateAllRenderers();
			if (spoutDebug) {
				System.out.println("[Spoutcraft] Updating renderer...");
			}
		}

		closePreloadGui();
	}

	public static void showPreloadGui() {
		// Display precache GUI
		if (SpoutClient.getHandle().currentScreen instanceof GuiDownloadTerrain) {
			SpoutClient.getHandle().displayGuiScreen(new GuiPrecache(), false);
			//setPreloadGuiText("Checking plugin caches...");
		}
	}

	public static void closePreloadGui() {
		if (SpoutClient.getHandle().currentScreen instanceof GuiPrecache) {
			// Closes downloading terrain
			SpoutClient.getHandle().displayGuiScreen(null, false);
			// Prevent closing a plugin created menu from opening the downloading terrain
			SpoutClient.getHandle().clearPreviousScreen();
		}
		SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketPreCacheCompleted());
	}

	public static void setPreloadGuiText(String text) {
		if (SpoutClient.getHandle().currentScreen instanceof GuiPrecache) {
			((GuiPrecache)SpoutClient.getHandle().currentScreen).statusText.setText(text);
			((GuiPrecache)SpoutClient.getHandle().currentScreen).statusText.onTick();
		}
	}

	public static void loadDesign(File file) {
		short customId = -1;
		byte data = 0;
		GenericBlockDesign design = null;

		try {
			final FileInputStream stream = new FileInputStream(file);
			final FileChannel read = stream.getChannel();
			final MappedByteBuffer buffer = read.map(FileChannel.MapMode.READ_ONLY, 0, read.size());
			stream.close();
			customId = buffer.getShort();
			data = buffer.get();
			design = new GenericBlockDesign();
			design.read(new SpoutInputStream(buffer));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (design != null && design.isReset()) {
				design = null;
			}
			if (customId != -1) {
				CustomBlock block = MaterialData.getCustomBlock(customId);
				if (block != null) {
					block.setBlockDesign(design, data);
				}
			}
		}
	}
}
