package org.getspout.spout.gui.controls;

import org.spoutcraft.spoutcraftapi.gui.AbstractListModel;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;

public class ControlsModel extends AbstractListModel {
	GuiControls gui;
	
	public ControlsModel() {
		
	}
	
	public void setCurrentGui(GuiControls gui) {
		this.gui = gui;
	}

	@Override
	public ListWidgetItem getItem(int row) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
		gui.updateButtons();
	}

}
