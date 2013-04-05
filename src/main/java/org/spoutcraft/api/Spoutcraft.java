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
package org.spoutcraft.api;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.src.Chunk;
import net.minecraft.src.World;

import org.spoutcraft.api.Client.Mode;
import org.spoutcraft.api.gui.MinecraftFont;
import org.spoutcraft.api.gui.MinecraftTessellator;
import org.spoutcraft.api.gui.RenderDelegate;
import org.spoutcraft.api.gui.WidgetManager;
import org.spoutcraft.api.inventory.MaterialManager;
import org.spoutcraft.api.keyboard.KeyBindingManager;
import org.spoutcraft.api.player.BiomeManager;
import org.spoutcraft.api.player.SkyManager;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.SpoutcraftWorld;
import org.spoutcraft.client.block.SpoutcraftChunk;
import org.spoutcraft.client.player.ClientPlayer;

public final class Spoutcraft {
	private static SpoutClient client = null;

	private Spoutcraft() {
	}

	public static void setClient(SpoutClient argClient) {
		if (client != null) {
			throw new UnsupportedOperationException("Cannot redefine singleton Client");
		}
		client = argClient;
	}

	/**
	 * Gets the client singleton interface
	 * @return client
	 */
	public static Client getClient() {
		return client;
	}

	/**
	 * Gets the name with the version of this client.
	 *
	 * Example: 'Spoutcraft_555'
	 * @return name
	 */
	public static String getName() {
		return client.getName();
	}

	/**
	 * Gets the build version of the client.
	 * @return version
	 */
	public static String getVersion() {
		return client.getVersion();
	}

	/**
	 * Gets the build version of Spout on the server, or -1 if none exists
	 * @return version
	 */
	public static long getServerVersion() {
		return client.getServerVersion();
	}

	/**
	 * Gets a logger that will print logs to the users .spoutcraft/logs folder, or terminal if it is active
	 * @return logger
	 */
	public static Logger getLogger() {
		return client.getLogger();
	}

	public static File getUpdateFolder() {
		return client.getUpdateFolder();
	}

	public static Mode getMode() {
		return client.getMode();
	}

	public static SkyManager getSkyManager() {
		return client.getSkyManager();
	}

	public static KeyBindingManager getKeyBindingManager() {
		return client.getKeyBindingManager();
	}

	public static RenderDelegate getRenderDelegate() {
		return client.getRenderDelegate();
	}

	public static MinecraftFont getMinecraftFont() {
		return client.getRenderDelegate().getMinecraftFont();
	}

	public static MinecraftTessellator getTessellator() {
		return client.getRenderDelegate().getTessellator();
	}

	public static BiomeManager getBiomeManager() {
		return client.getBiomeManager();
	}

	public static MaterialManager getMaterialManager() {
		return client.getMaterialManager();
	}

	public static boolean isSpoutEnabled() {
		return client.isSpoutEnabled();
	}

	public static File getAddonFolder() {
		return client.getAddonFolder();
	}

	public static File getAudioCache() {
		return client.getAudioCache();
	}

	public static File getTemporaryCache() {
		return client.getTemporaryCache();
	}

	public static File getTextureCache() {
		return client.getTextureCache();
	}

	public static File getTexturePackFolder() {
		return client.getTexturePackFolder();
	}

	public static File getStatsFolder() {
		return client.getStatsFolder();
	}

	public static File getSelectedTexturePackZip() {
		return client.getSelectedTexturePackZip();
	}

	public static long getTick() {
		return client.getTick();
	}

	public static WidgetManager getWidgetManager() {
		return client.getWidgetManager();
	}

	public static boolean hasPermission(String permission) {
		return client.hasPermission(permission);
	}

	public static SpoutcraftChunk getChunkAt(World world, int x, int y, int z) {
		return getChunk(world, x >> 4, z >> 4);
	}

	public static SpoutcraftChunk getChunk(World world, int chunkX, int chunkZ) {
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		if (chunk != null) {
			return chunk.spoutChunk;
		}
		return null;
	}

	public static SpoutcraftWorld getWorld() {
		return SpoutClient.getInstance().getRawWorld().world;
	}

	public static ClientPlayer getActivePlayer() {
		return ClientPlayer.getInstance();
	}
}
