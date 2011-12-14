package org.spoutcraft.spoutcraftapi.gui;

public interface ScrollArea extends Scrollable, Screen {
	/**
	 * Recalculates height and width for all widgets.
	 * This is useful when the size of a widget changed while it was already attached to the ScrollArea.
	 */
	public void updateInnerSize();
}
