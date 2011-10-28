package org.getspout.spout.gui.settings;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.config.ConfigReader;
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
