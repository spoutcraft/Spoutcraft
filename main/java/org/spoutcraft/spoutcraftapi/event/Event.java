package org.spoutcraft.spoutcraftapi.event;

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
		/**
		* CLIENT EVENTS
		*/
		KEY_UP(Category.CLIENT),
		KEY_DOWN(Category.CLIENT),
		RENDER_DISTANCE_CHANGE(Category.CLIENT),
		MOUSE_MOVE(Category.CLIENT),
		MOUSE_DOWN(Category.CLIENT),
		MOUSE_UP(Category.CLIENT),

		/**
		* Represents a custom event, isn't actually used
		*/
		CUSTOM_EVENT(Category.MISCELLANEOUS);
		
		private final Category category;
		
		private Type(Category category) {
            this.category = category;
        }

        /**
         * Gets the Category assigned to this event
         *
         * @return Category of this Event.Type
         */
        public Category getCategory() {
            return category;
        }
	}
	
	public enum Category {
        PLAYER,
        ENTITY,
        BLOCK,
        LIVING_ENTITY,
        WEATHER,
        VEHICLE,
        WORLD,
        CLIENT,
        INVENTORY,
        MISCELLANEOUS;
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
