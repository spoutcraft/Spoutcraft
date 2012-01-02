package org.spoutcraft.client.gui.settings;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.client.gui.SafeButton;

public class ResetButton extends SafeButton {
	
	VideoSettings parent;
	
	public ResetButton(VideoSettings parent) {
		setText("Reset to default");
		setTooltip("Resets every setting to the default.");
		this.parent = parent;
	}

	@Override
	protected void executeAction() {
		ConfigReader.restoreDefaults();
		ConfigReader.write();
	
		SpoutClient.getHandle().displayGuiScreen(new VideoSettings(parent.parent));
	}
}
