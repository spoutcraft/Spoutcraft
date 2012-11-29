package org.spoutcraft.client.precache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import net.minecraft.client.Minecraft;

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
	 * Tuple plugin -> boolean isCached
	 */
	public static HashMap<PrecacheTuple, Boolean> plugins = new HashMap<PrecacheTuple, Boolean>();
	
	/**
	 * Adds a plugin tuple to the hashmap, and checks if its cached and valid
	 * @param plugin
	 */
	public static void addPlugin(PrecacheTuple plugin) {
		//check plugin precache.
		boolean isCached = false;
		long crc = -1;
		File target = getPluginPreCacheFile(plugin);
		if (target.exists()) {
			crc = FileUtil.getCRC(target, new byte[(int) target.length()]);
			if (crc == plugin.getCrc()) {
				isCached = true;
			}
		}
		
		System.out.println("Plugin Precache: " +
				plugin.getPlugin() + " " +
				plugin.getVersion() + " || " +
				"isCached= "+ String.valueOf(isCached)+
				" || ServerCRC= " + String.valueOf(plugin.getCrc()) + " " +
				"ClientCRC= " + String.valueOf(crc));
		plugins.put(plugin, isCached);
	}
	
	/**
	 * resets the plugins. Useful to clear out previous entries when starting a new login sequence.
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
		if (plugins.containsKey(plugin)) {
			plugins.put(plugin, true);
		}
	}
	
	/**
	 * Checks if there is a plugin precache still needing to be cached.
	 * @return true if a plugin needs to be cached, false is everything is already cached.
	 */
	public static boolean hasNextCache() {
		for( boolean cached : plugins.values()) {
			if (cached == false) return true;
		}
		return false;
	}
	
	/**
	 * gets the next item to be cached
	 * @return
	 */
	public static PrecacheTuple getNextToCache() {
		for( Entry entry : plugins.entrySet()) {
			if ( ((Boolean)entry.getValue()).booleanValue() == false ) {
				return (PrecacheTuple)entry.getKey();
			}
		}
		return null;
	}
	
	/**
	 * Starts the next cache
	 */
	public static void doNextCache() {
		if (!hasNextCache()) {
			loadPrecache(true);
			return;
		}
		
		PrecacheTuple next = getNextToCache();
		
		if (next == null) {
			loadPrecache(true);
			return;
		}
		
		setPreloadGuiText("Downloading " + next.getPlugin() + " " + next.getVersion());
		
		SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketRequestPrecache(next.getPlugin()));
		
	}
	
	public static File getPluginPreCacheFile(PrecacheTuple plugin) {
		return getPluginPreCacheFile(plugin.getPlugin(), plugin.getVersion());
	}
	
	public static File getPluginPreCacheFile(String plugin, String version) {
		return new File(FileUtil.getCacheDir(), plugin+"_"+version+".zip");
	}
	
	public static void loadPrecache(boolean reloadRenderer) {
		
		setPreloadGuiText("Loading Textures...");
		
		//unzip
		File cacheRoot = FileUtil.getCacheDir();
		
		for(Entry entry : plugins.entrySet()) {
			try {
				PrecacheTuple plugin = (PrecacheTuple)entry.getKey();
				boolean isCached = (Boolean)entry.getValue();
				
				if(isCached == true) {
					ZipFile zip = new ZipFile(getPluginPreCacheFile(plugin));
					Enumeration zipEntries = zip.entries();
					
					while (zipEntries.hasMoreElements())
					{
						ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();
						if (zipEntry.isDirectory()) {
							continue;
						}
						File target = new File(cacheRoot, plugin.getPlugin() + "/" + zipEntry.getName());
						
						if (target.exists() && zipEntry.getCrc() == FileUtil.getCRC(target, new byte[(int) target.length()])) {
							continue;
						}
						
						//at this point if it exists its not a vlid crc
						if (target.exists()) {
							target.delete();
						}
						
						//parent folder, not target
						if(!target.getParentFile().exists()) {
							target.getParentFile().mkdirs();
						}
						
						BufferedInputStream is = new BufferedInputStream(zip.getInputStream(zipEntry));
						int currentByte;
						byte[] buf = new byte[1024];
						
						FileOutputStream fos = new FileOutputStream(target);
						BufferedOutputStream dest = new BufferedOutputStream(fos, 1024);
						
						while ((currentByte = is.read(buf, 0, 1024)) != -1) {
							dest.write(buf, 0, currentByte);
						}
						dest.flush();
						dest.close();
						is.close();
						
						if (target.exists() && target.getName().endsWith(".sbd")) {
							loadDesign(target);
						}
						else if (target.exists() && FileUtil.isImageFile(target.getName())) {
							CustomTextureManager.getTextureFromUrl(plugin.getPlugin(), target.getName());
						}
						
					}
					
					zip.close();
				}
			} catch (ZipException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (reloadRenderer == true) {
			if (Minecraft.theMinecraft.theWorld != null) {
				Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
			}
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
