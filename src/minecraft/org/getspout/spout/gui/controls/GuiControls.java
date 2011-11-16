package org.getspout.spout.gui.controls;

import org.getspout.spout.gui.ButtonUpdater;
import org.getspout.spout.gui.GuiSpoutScreen;
import org.spoutcraft.spoutcraftapi.gui.AbstractListModel;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericListView;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollArea;
import org.spoutcraft.spoutcraftapi.gui.Label;
import org.spoutcraft.spoutcraftapi.gui.ScrollArea;

public class GuiControls extends GuiSpoutScreen implements ButtonUpdater{
	private Label labelTitle;
	private Button buttonMainMenu, buttonAdd, buttonEdit, buttonRemove;
	private ScrollArea filter;
	private GenericListView view;
	private static ControlsModel model = new ControlsModel();
	
	protected void createInstances() {
		labelTitle = new GenericLabel("Controls");
		buttonMainMenu = new GenericButton("Main Menu");
		buttonAdd = new GenericButton("Add");
		buttonAdd.setTooltip("Add Shortcut");
		buttonEdit = new GenericButton("Edit");
		buttonEdit.setTooltip("Edit Shortcut");
		buttonRemove = new GenericButton("Remove");
		buttonRemove.setTooltip("Remove Shortcut");
		filter = new GenericScrollArea();
		view = new GenericListView(model);
		model.setCurrentGui(this);
	}
	
	protected void layoutWidgets() {
		
	}

	@Override
	protected void buttonClicked(Button btn) {
		
	}

	public void updateButtons() {
		
	}

}
