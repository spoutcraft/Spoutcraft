package org.getspout.spout.packet;

public enum ScreenAction {
	Open(0),
	Close(1),
	;
	
	private final byte id;
	ScreenAction(int id) {
		this.id = (byte)id;
	}
	
	public int getId() {
		return id;
	}
	
	public static ScreenAction getScreenActionFromId(int id) {
		for (ScreenAction action : values()) {
			if (action.getId() == id) {
				return action;
			}
		}
		return null;
	}
}
