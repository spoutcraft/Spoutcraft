/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.gui.singleplayer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.minecraft.src.ISaveFormat;
import net.minecraft.src.SaveFormatComparator;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.WorldInfo;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.MCRenderDelegate;
import org.spoutcraft.spoutcraftapi.gui.ListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;
import org.spoutcraft.spoutcraftapi.gui.MinecraftFont;

public class WorldItem implements ListWidgetItem {

	private ListWidget widget;
	private WorldInfo world;
	private String fileName;
	private SaveFormatComparator save;
	private static DateFormat dateFormatter = new SimpleDateFormat();
	
	public WorldItem(SaveFormatComparator save, ISaveFormat access) {
		this.save = save;
		this.setWorld(access.getWorldInfo(save.getFileName()));
		this.setFileName(save.getFileName());
	}

	public void setListWidget(ListWidget widget) {
		this.widget = widget;
	}

	public ListWidget getListWidget() {
		return widget;
	}

	public int getHeight() {
		return 26;
	}

	public void render(int x, int y, int width, int height) {
		MCRenderDelegate r = (MCRenderDelegate) SpoutClient.getInstance().getRenderDelegate();
		MinecraftFont f = r.getMinecraftFont();
		StringTranslate stringtranslate = StringTranslate.getInstance();
		f.drawString(getWorld().getWorldName(), x + 2, y + 2, 0xffffffff);
		String type = getGameplayString(getWorld().getGameType());
		if(save.isHardcoreModeEnabled()) {
			type = stringtranslate.translateKey("gameMode.hardcore");
		}
		f.drawString(type, x + 2, y + 13, 0xffaaaaaa);
		String date = "Last played: " + dateFormatter.format(new Date(getWorld().getLastTimePlayed()));
		int w = f.getTextWidth(date);
		f.drawString(date, x + width - 2 - w, y + 2, 0xffaaaaaa);
		String heightText = "Height: " + 256;
		w = f.getTextWidth(heightText);
		f.drawString(heightText, x + width - 2 - w, y + 13, 0xffaaaaaa);
	}

	public void onClick(int x, int y, boolean doubleClick) {
	}
	
	public static String getGameplayString(int id) {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		switch(id) {
		case 0:
			return stringtranslate.translateKey("gameMode.survival");
		case 1:
			return stringtranslate.translateKey("gameMode.creative");
		}
		return "Unknown gameplay id "+id;
	}

	private void setWorld(WorldInfo world) {
		this.world = world;
	}
	
	public WorldInfo getWorld() {
		return world;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
}
