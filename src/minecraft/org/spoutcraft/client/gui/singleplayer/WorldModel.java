package org.spoutcraft.client.gui.singleplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.PlayerControllerCreative;
import net.minecraft.src.PlayerControllerSP;
import net.minecraft.src.SaveFormatComparator;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.ButtonUpdater;
import org.spoutcraft.client.gui.FilterItem;
import org.spoutcraft.client.gui.FilterModel;
import org.spoutcraft.spoutcraftapi.gui.AbstractListModel;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;

public class WorldModel extends FilterModel {

	ISaveFormat access;
	List<SaveFormatComparator> worlds;
	private ButtonUpdater buttonUpdater;

	public WorldModel(ButtonUpdater updater) {
		refresh();
		this.buttonUpdater = updater;
	}

	protected void refreshContents() {
		access = SpoutClient.getHandle().getSaveLoader();
		worlds = access.getSaveList();
		Collections.sort(worlds);
		items.clear();
		for(SaveFormatComparator world:worlds) {
			WorldItem item = new WorldItem(world, access);
			items.add(item);
		}
		
		
		sizeChanged();
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
		buttonUpdater.updateButtons();
		if(doubleClick) {
			playWorld(item);
		}
	}

	public void playWorld(int i) {
		Minecraft mc = SpoutClient.getHandle();
		int gameType = worlds.get(i).getGameType();
		if (gameType == 0)
		{
			mc.playerController = new PlayerControllerSP(mc);
		}
		else
		{
			mc.playerController = new PlayerControllerCreative(mc);
		}
		String s = worlds.get(i).getFileName();
		if (s == null)
		{
			s = (new StringBuilder()).append("World").append(i).toString();
		}
		mc.startWorld(s, worlds.get(i).getDisplayName(), null);
		mc.displayGuiScreen(null);
	}

	public void deleteWorld(int selectedRow) {
		access.deleteWorldDirectory(worlds.get(selectedRow).getFileName());
		System.out.println("Deleted world.");
		refresh();
		buttonUpdater.updateButtons();
	}

	/**
	 * Renames a world
	 * @param oldworld the name of the world to be renamed
	 * @param newworld the new name of the world
	 */
	public void rename(String oldworld, String newworld) {
		access.renameWorld(oldworld, newworld);
		refresh();
	}

}
