package org.spoutcraft.spoutcraftapi.gui;

import java.util.HashMap;

public enum ScrollBarPolicy {
	/**
	 * Shows the scrollbar when getMaximumScrollPosition is greater than 0
	 */
	SHOW_IF_NEEDED(0),
	/**
	 * Never show the scrollbar. However, you'll still be able to scroll with the scroll wheel or your trackpad or with arrow keys if the widget implemented that (like the list widget).
	 */
	SHOW_NEVER(1),
	/**
	 * Always show the scrollbar
	 */
	SHOW_ALWAYS(2),
	;
	
	private final int id;
	private static HashMap<Integer, ScrollBarPolicy> ids = new HashMap<Integer, ScrollBarPolicy>();
	ScrollBarPolicy(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static ScrollBarPolicy getById(int id) {
		return ids.get(id);
	}
	
	static {
		for(ScrollBarPolicy s:values()) {
			ids.put(s.id, s);
		}
	}
}
