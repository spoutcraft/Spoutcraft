package org.spoutcraft.client.gui.controls;

import org.spoutcraft.client.gui.SafeButton;

public class DeleteControlButton extends SafeButton {

	private GuiControls parent;

	public DeleteControlButton(GuiControls parent) {
		setText("Delete");
		this.parent = parent;
	}

	@Override
	protected void executeAction() {
		parent.deleteCurrentControl();
	}

}
