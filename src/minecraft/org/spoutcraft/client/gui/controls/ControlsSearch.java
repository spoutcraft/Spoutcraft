package org.spoutcraft.client.gui.controls;

import org.bukkit.ChatColor;
import org.spoutcraft.spoutcraftapi.event.screen.TextFieldChangeEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;

public class ControlsSearch extends GenericTextField {
	private GuiControls gui;
	
	public ControlsSearch(GuiControls gui) {
		this.gui = gui;
		setPlaceholder(ChatColor.GRAY+"Search");
	}
	
	@Override
	public void onTextFieldChange(TextFieldChangeEvent event) {
		gui.getModel().refresh();
	}
}
