package org.getspout.spout.gui.addon;

import java.util.ArrayList;
import java.util.List;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.addon.ServerAddon;
import org.spoutcraft.spoutcraftapi.gui.AbstractListModel;
import org.spoutcraft.spoutcraftapi.gui.ListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;
import org.spoutcraft.spoutcraftapi.gui.MinecraftFont;

public class LocalAddonsModel extends AbstractListModel {
	
	private GuiAddonsLocal gui = null;
	private List<AddonItem> items = new ArrayList<LocalAddonsModel.AddonItem>();

	public LocalAddonsModel(GuiAddonsLocal guiAddonsLocal) {
		gui = guiAddonsLocal;
		updateAddons();
	}
	
	public void updateAddons() {
		for(Addon addon:Spoutcraft.getAddonManager().getAddons()) {
			if(addon.getDescription().getName().equals("Spoutcraft") || addon instanceof ServerAddon) {
				continue;
			}
			items.add(new AddonItem(addon));
		}
	}

	@Override
	public ListWidgetItem getItem(int row) {
		if(row >= 0 && row < getSize()) {
			return items.get(row);
		}
		return null;
	}

	@Override
	public int getSize() {
		return items.size();
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
		gui.updateButtons();
	}
	
	public class AddonItem implements ListWidgetItem {
		
		private Addon addon;
		private ListWidget widget;
		private String authors;
		
		public AddonItem(Addon a) {
			addon = a;
			authors = "";
			for(String author:a.getDescription().getAuthors()) {
				if(!authors.isEmpty()) {
					authors += ", ";
				}
				authors+=author;
			}
		}
		
		@Override
		public void setListWidget(ListWidget widget) {
			this.widget = widget;
		}

		@Override
		public ListWidget getListWidget() {
			return widget;
		}

		@Override
		public int getHeight() {
			return 20;
		}

		@Override
		public void render(int x, int y, int width, int height) {
			MinecraftFont font = Spoutcraft.getMinecraftFont();
			font.drawString(addon.getDescription().getName(), x+2, y+2, 0xffffffff);
			font.drawString(authors, x+2, y+11, 0xffaaaaaa);
			String version = addon.getDescription().getVersion();
			int vwidth = font.getTextWidth(version);
			font.drawString(version, x + width - vwidth - 2, y+2, 0xffffffff);
		}

		@Override
		public void onClick(int x, int y, boolean doubleClick) {
			
		}
		
		public Addon getAddon() {
			return addon;
		}
		
	}

}
