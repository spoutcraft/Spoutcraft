package org.spoutcraft.api.event;

public abstract class Event {
	
	private final Type type;
	private final String name;
	
	protected Event(Type type) {
		this.type = type;
		this.name = null;
	}
	
	protected Event(String name) {
		this.type = Type.CUSTOM_EVENT;
		this.name = name;
	}
	
	/**
	 * @return the type
	 */
	public final Type getType() {
		return type;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}
	
	public static enum Type {
		CUSTOM_EVENT
	}
	
	public static enum Priority {
		Lowest,
		Low,
		Normal,
		High,
		Highest,
		Monitor;
	}
	
}
