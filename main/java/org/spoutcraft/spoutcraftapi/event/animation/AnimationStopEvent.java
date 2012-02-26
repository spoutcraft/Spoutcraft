package org.spoutcraft.spoutcraftapi.event.animation;

import org.spoutcraft.spoutcraftapi.animation.Animation;
import org.spoutcraft.spoutcraftapi.event.HandlerList;

public class AnimationStopEvent extends AnimationEvent<AnimationStopEvent> {

	private HandlerList<AnimationStopEvent> handlers = new HandlerList<AnimationStopEvent>();
	
	public AnimationStopEvent(Animation animation) {
		super(animation);
	}

	@Override
	public HandlerList<AnimationStopEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "AnimationStopEvent";
	}
}
