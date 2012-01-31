package org.spoutcraft.client.gui.singleplayer;

import org.spoutcraft.client.gui.FilterItem;
import org.spoutcraft.client.gui.FilterModel;
import org.spoutcraft.spoutcraftapi.ChatColor;
import org.spoutcraft.spoutcraftapi.event.screen.TextFieldChangeEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;

public class WorldSearchField extends GenericTextField implements FilterItem{
	private FilterModel model;
	
	public WorldSearchField(FilterModel model) {
		this.model = model;
		model.getFilters().add(this);
		setPlaceholder(ChatColor.GRAY + "Search ...");
	}

	@Override
	public void onTextFieldChange(TextFieldChangeEvent event) {
		model.refresh();
	}

	public boolean matches(Object obj) {
		if(getText().trim().isEmpty()) {
			return true;
		}
		if(obj instanceof WorldItem) {
			WorldItem world = (WorldItem) obj;
			if(world.getWorld().getWorldName().toLowerCase().contains(getText().toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	
}
