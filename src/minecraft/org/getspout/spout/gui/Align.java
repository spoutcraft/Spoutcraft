package org.getspout.spout.gui;

import java.util.HashMap;

public enum Align {
	FIRST(0),
	SECOND(1),
	THIRD(2),
	;
	
	private final int type;
	Align(int type) {
		this.type = type;
	}
	
	public int getId() {
		return type;
	}
	
	private static final HashMap<Integer, Align> lookupId = new HashMap<Integer, Align>();
	
	static {
		for (Align t : values()) {
			lookupId.put(t.type, t);
		}
	}
	
	public static Align getAlign(int id) {
		return lookupId.get(id);
	}
}