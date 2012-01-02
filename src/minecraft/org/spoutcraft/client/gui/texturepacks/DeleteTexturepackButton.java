package org.spoutcraft.client.gui.texturepacks;

import org.spoutcraft.client.gui.SafeButton;

public class DeleteTexturepackButton extends SafeButton {
	private GuiTexturePacks parent;

	public DeleteTexturepackButton(GuiTexturePacks parent) {
		setText("Delete");
		this.parent = parent;
	}

	@Override
	protected void executeAction() {
		parent.deleteCurrentTexturepack();
	}

}
