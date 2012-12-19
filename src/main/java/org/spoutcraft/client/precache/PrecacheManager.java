/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.*;

import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import org.bukkit.ChatColor;

import org.spoutcraft.api.block.design.GenericBlockDesign;
import org.spoutcraft.api.material.CustomBlock;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.precache.GuiPrecache;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.io.FileUtil;
import org.spoutcraft.client.packet.PacketRequestPrecache;

public class PrecacheManager {
	/**
	 * PrecacheTuple - Holds Plugin Name, Version, and CRC
	 * Boolean - Is this cached or not?
	 */
	public static HashMap<PrecacheTuple, Boolean> plugins = new HashMap<PrecacheTuple, Boolean>();
	
	/**
	 * Adds a plugin tuple to the hashmap, and checks if its cached and valid
	 * @param plugin
	 */
	public static void addPlugin(PrecacheTuple plugin) {
		//Grab precache file
		File target = getPluginPreCacheFile(plugin);
		//Does it exist locally?
		if (target.exists()) {
			//Is the crc the same as the one sent from SpoutPlugin?
			if (plugin.getCrc() == FileUtil.getCRC(target, new byte[(int) target.length()])) {
				//Its cached, continue on
				plugins.put(plugin, true);
				return;
			}
		}
		//Either it doesn't exist or crc failed, either or it isn't cached.
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
			if(tuple.getPlugin().equalsIgnoreCase(plugin) && tuple.getVersion().equalsIgnoreCase(version)) {
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
		//Loop through the list of plugins
		for (Entry entry : plugins.entrySet()) {
			//Get the status of the entry (cached or not)
			if ((Boolean) entry.getValue()) {
				//Its cached, continue
				continue;
			}
			//It wasn't cached so return this entry to be cached
			return (PrecacheTuple) entry.getKey();
		}
		//Nothing is left to cache, return null
		return null;
	}
	
	/**
	 * Starts the next cache
	 */
	public static void doNextCache() {
		//Check if there is any thing left to be cached
		if (!hasNextCache()) {
			//Nothing is left to cache, proceed to load the cache
			loadPrecache(true);
			return;
		}
		final PrecacheTuple next = getNextToCache();

		//Let the user know we are precaching
		setPreloadGuiText(ChatColor.BLUE + "Spoutcraft" + "\n"+" "+ "\n"+ ChatColor.WHITE + "Downloading Custom Content for:  " + ChatColor.ITALIC + next.getPlugin() + " " + next.getVersion());

		//Send SpoutPlugin a request for the pre-cache zip
		SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketRequestPrecache(next.getPlugin()));
	}
	
	public static File getPluginPreCacheFile(PrecacheTuple plugin) {
		return getPluginPreCacheFile(plugin.getPlugin(), plugin.getVersion());
	}
	
	public static File getPluginPreCacheFile(String plugin, String version) {
		return new File(FileUtil.getCacheDir(), plugin+"_"+version+".zip");
	}
	
	public static void loadPrecache(boolean reloadRenderer) {
		//Unzip
		File cacheRoot = FileUtil.getCacheDir();
		//Loop through the plugins
		for(Entry entry : plugins.entrySet()) {
			//Grab the tuple
			final PrecacheTuple toCache = (PrecacheTuple) entry.getKey();
			final File extractDir = new File(cacheRoot, toCache.getPlugin() + "_" + toCache.getVersion()); //Ex. /cache/pluginname/
			//The extracted file exists and is a directory and isn't empty, move on
			if (extractDir.exists() && extractDir.isDirectory() && !((List<File>) FileUtils.listFiles(extractDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)).isEmpty()) {
				//Do nothing
			} else {
				extractDir.mkdirs(); //Make the directories to unzip to
				final byte[] BUFFER = new byte[10000000]; //~9MB buffer
				try  {
					//Read in a zip stream
					final File temp = new File(cacheRoot, toCache.getPlugin() + "_" + toCache.getVersion() + ".zip");
					System.out.println(temp.getName());
					final ZipInputStream stream = new ZipInputStream(new FileInputStream(temp));

					//Grab the first entry in the zip
					ZipEntry inner = stream.getNextEntry();
					while (inner != null) {
						//Grab the entry's name
						String innerName = inner.getName();
						//Construct an output stream for the entry
						final FileOutputStream writeInner = new FileOutputStream(new File(extractDir, innerName));
						int i;
						//Read in the file. The file will be limited to the 9MB buffer
						while ((i = stream.read(BUFFER, 0, BUFFER.length)) > -1) {
							//Write the buffer
							writeInner.write(BUFFER, 0, i);
						}
						//Close the writable buffer
						writeInner.close();
						//Close the zip stream for this entry
						stream.closeEntry();
						//Grab the next entry in the zip
						inner = stream.getNextEntry();
					}
					//Finally close the stream altogether
					stream.close();
				} catch (Exception e ) {
					e.printStackTrace();
				}
			}
		}
		for (File file : (List<File>) FileUtils.listFiles(cacheRoot, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)) {
			if (file.getName().endsWith(".zip")) {
				continue;
			}
			System.out.println("Loading: " + file.getName());
			if (file.getName().endsWith(".sbd")) {
				loadDesign(file);
			}
			else if (FileUtil.isImageFile(file.getName())) {
				CustomTextureManager.getTextureFromUrl(file.getName());
			}
		}
		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}
		
		closePreloadGui();
	}
	
	public static void showPreloadGui() {
		//display precache gui.
		SpoutClient.getHandle().displayGuiScreen(new GuiPrecache(), false);
		setPreloadGuiText("Checking Plugin Caches...");
	}
	
	public static void closePreloadGui() {
		if (SpoutClient.getHandle().currentScreen instanceof GuiPrecache) {
			// Closes downloading terrain
			SpoutClient.getHandle().displayGuiScreen(null, false);
			// Prevent closing a plugin created menu from opening the downloading terrain
			SpoutClient.getHandle().clearPreviousScreen();
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new org.spoutcraft.client.packet.PacketPreCacheCompleted());		
		}
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
			DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			customId = in.readShort();
			data = in.readByte();
			design = new GenericBlockDesign();	
			design.read(in);
			
		} catch (EOFException e) {
			//data input stream always throws EOFException when complete
			//e.printStackTrace();
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
