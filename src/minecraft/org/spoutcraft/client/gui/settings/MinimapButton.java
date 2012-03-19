package org.spoutcraft.client.gui.settings;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.minimap.GuiMinimapMenu;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;

public class MinimapButton extends GenericButton {
	GameSettingsScreen parent;
	
	public MinimapButton(GameSettingsScreen parent) {
		super("Minimap Options");
		this.parent = parent;
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		SpoutClient.getHandle().displayGuiScreen(new GuiMinimapMenu(parent));
	}
}
