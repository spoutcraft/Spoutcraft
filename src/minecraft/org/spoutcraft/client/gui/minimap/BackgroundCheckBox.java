package org.spoutcraft.client.gui.minimap;

import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class BackgroundCheckBox extends GenericCheckBox{
	public BackgroundCheckBox() {
		super("Background");
		setChecked(MinimapConfig.getInstance().isShowBackground());
		setTooltip("Background\nON - Shows the standard 'ancient map' style background\nOFF - no background for the minimap");
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		MinimapConfig.getInstance().setShowBackground(isChecked());
	}
}
