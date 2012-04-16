package org.spoutcraft.client.gui.minimap;

import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class HeightMapCheckBox extends GenericCheckBox {
	public HeightMapCheckBox() {
		super("Show Height map");
		setChecked(MinimapConfig.getInstance().isHeightmap());
		setTooltip("If enabled, shows a heightmap in the minimap and the overview map.");
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		MinimapConfig.getInstance().setHeightmap(this.isChecked());
	}
}
