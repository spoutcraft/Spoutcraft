package org.spoutcraft.spoutcraftapi.event.animation;

import org.spoutcraft.spoutcraftapi.animation.Animation;
import org.spoutcraft.spoutcraftapi.event.Event;

public abstract class AnimationEvent<TEvent extends AnimationEvent<TEvent>> extends Event<TEvent> {
	private Animation animation;
	
	protected AnimationEvent(Animation animation) {
		this.animation = animation;
	}
	
	public Animation getAnimation() {
		return animation;
	}
}
