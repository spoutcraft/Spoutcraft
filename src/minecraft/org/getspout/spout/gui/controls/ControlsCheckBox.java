package org.getspout.spout.gui.controls;

import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class ControlsCheckBox extends GenericCheckBox {

	private GuiControls gui;
	
	public ControlsCheckBox(GuiControls gui, String text) {
		this.gui = gui;
		setText(text);
		setChecked(true);
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		gui.getModel().refresh();
	}
	
}
