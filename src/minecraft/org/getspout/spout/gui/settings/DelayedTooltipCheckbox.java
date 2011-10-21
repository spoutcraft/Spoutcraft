package org.getspout.spout.gui.settings;

import org.getspout.spout.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class DelayedTooltipCheckbox extends GenericCheckBox {
	public DelayedTooltipCheckbox() {
		super("Delayed Tooltips");
		setTooltip("If ticked, widgets wait 500 ms\nuntil they show their tooltips.");
		setChecked(ConfigReader.delayedTooltips);
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.delayedTooltips = !ConfigReader.delayedTooltips;
		ConfigReader.write();
	}
}
