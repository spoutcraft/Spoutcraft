package org.spoutcraft.spoutcraftapi.gui;

public enum ScrollBarPolicy {
	/**
	 * Shows the scrollbar when getMaximumScrollPosition is greater than 0
	 */
	SHOW_IF_NEEDED,
	/**
	 * Never show the scrollbar. However, you'll still be able to scroll with the scroll wheel or your trackpad or with arrow keys if the widget implemented that (like the list widget).
	 */
	SHOW_NEVER,
	/**
	 * Always show the scrollbar
	 */
	SHOW_ALWAYS,
	;
}
