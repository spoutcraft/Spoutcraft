package org.getspout.spout.gui;

import java.util.HashMap;

public enum WidgetAnchor {
	TOP_LEFT(0),
	TOP_CENTER(1),
	TOP_RIGHT(2),
	CENTER_LEFT(3),
	CENTER_CENTER(4),
	CENTER_RIGHT(5),
	BOTTOM_LEFT(6),
	BOTTOM_CENTER(7),
	BOTTOM_RIGHT(8),
	SCALE(9),
	;
	
	private final int id;
	WidgetAnchor(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	private static final HashMap<Integer, WidgetAnchor> lookupId = new HashMap<Integer, WidgetAnchor>();
	
	static {
		for (WidgetAnchor t : values()) {
			lookupId.put(t.getId(), t);
		}
	}
	
	public static WidgetAnchor getAnchorFromId(int id) {
		return lookupId.get(id);
	}
}