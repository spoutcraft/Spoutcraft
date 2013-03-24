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

import org.spoutcraft.api.gui.RenderDelegate;
import org.spoutcraft.api.gui.WidgetManager;
import org.spoutcraft.api.inventory.MaterialManager;
import org.spoutcraft.api.keyboard.KeyBindingManager;
import org.spoutcraft.api.player.BiomeManager;
import org.spoutcraft.api.player.SkyManager;
import org.spoutcraft.api.property.PropertyInterface;
import org.spoutcraft.client.entity.CraftCameraEntity;

public interface Client extends PropertyInterface {
	public String getName();

	public String getVersion();

	public Logger getLogger();

	public File getUpdateFolder();

	public SkyManager getSkyManager();

	public KeyBindingManager getKeyBindingManager();

	public BiomeManager getBiomeManager();

	public MaterialManager getMaterialManager();

	public boolean isSpoutEnabled();

	public long getServerVersion();

	public File getAudioCache();

	public File getTemporaryCache();

	public File getTextureCache();

	public File getTexturePackFolder();

	public File getSelectedTexturePackZip();

	public File getStatsFolder();

	public long getTick();

	public Mode getMode();

	public RenderDelegate getRenderDelegate();

	public enum Mode {
		Single_Player,
		Multiplayer,
		Menu;
	}

	public WidgetManager getWidgetManager();

	public boolean hasPermission(String permission);
}
