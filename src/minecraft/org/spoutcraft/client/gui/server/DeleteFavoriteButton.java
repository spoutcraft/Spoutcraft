package org.spoutcraft.client.gui.server;

import org.spoutcraft.client.gui.SafeButton;

public class DeleteFavoriteButton extends SafeButton {
	private GuiFavorites parent;
	
	public DeleteFavoriteButton(GuiFavorites parent) {
		this.parent = parent;
		setText("Delete");
	}
	
	@Override
	protected void executeAction() {
		parent.deleteCurrentFavorite();
	}

}
