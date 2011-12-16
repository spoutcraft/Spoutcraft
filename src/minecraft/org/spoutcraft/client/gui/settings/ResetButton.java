package org.spoutcraft.client.gui.settings;

import org.spoutcraft.client.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;

public class ResetButton extends GenericButton {
	
	VideoSettings parent;
	
	public ResetButton(VideoSettings parent) {
		setText("Reset to default");
		setTooltip("Resets every setting to the default.");
		this.parent = parent;
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.restoreDefaults();
		ConfigReader.write();

		SpoutClient.getHandle().displayGuiScreen(new VideoSettings(parent.parent));
	}
}
