package org.spoutcraft.client.gui.minimap;

import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class DeathpointsCheckBox extends GenericCheckBox{
	public DeathpointsCheckBox() {
		super("Deathpoints");
		setChecked(MinimapConfig.getInstance().isDeathpoints());
		setTooltip("Deathpoints\nAdd waypoints to the map every time you die, with\na timestamp of when you died. Deathpoints can\n be removed and altered like regular waypoints.");
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		MinimapConfig.getInstance().setDeathpoints(isChecked());
	}
}
