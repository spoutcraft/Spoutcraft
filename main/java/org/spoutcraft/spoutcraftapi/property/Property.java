package org.spoutcraft.spoutcraftapi.property;

import org.spoutcraft.spoutcraftapi.animation.Animatable;

public abstract class Property {
	public abstract Object get();
	public abstract void set(Object value);
	public Animatable getAnimatableValue(){
		Object value = get();
		if(value instanceof Animatable){
			return (Animatable)value;
		}
		
		return null;
	}
}
