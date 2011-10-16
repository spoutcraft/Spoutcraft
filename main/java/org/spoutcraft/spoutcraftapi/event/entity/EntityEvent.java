package org.spoutcraft.spoutcraftapi.event.entity;

import org.spoutcraft.spoutcraftapi.entity.Entity;
import org.spoutcraft.spoutcraftapi.event.Event;

public abstract class EntityEvent<TEvent extends EntityEvent<TEvent>> extends Event<TEvent>{
	
	protected Entity entity;
	
	protected EntityEvent(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
}
