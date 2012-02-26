package org.spoutcraft.spoutcraftapi.event.animation;

import org.spoutcraft.spoutcraftapi.animation.Animation;
import org.spoutcraft.spoutcraftapi.event.HandlerList;

public class AnimationPauseEvent extends AnimationEvent<AnimationPauseEvent> {
	
	private HandlerList<AnimationPauseEvent> handlers = new HandlerList<AnimationPauseEvent>();

	public AnimationPauseEvent(Animation animation) {
		super(animation);
	}

	@Override
	public HandlerList<AnimationPauseEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "AnimationPauseEvent";
	}
}
