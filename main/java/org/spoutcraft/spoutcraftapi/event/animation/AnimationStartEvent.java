package org.spoutcraft.spoutcraftapi.event.animation;

import org.spoutcraft.spoutcraftapi.animation.Animation;
import org.spoutcraft.spoutcraftapi.event.HandlerList;

public class AnimationStartEvent extends AnimationEvent<AnimationStartEvent> {
	private HandlerList<AnimationStartEvent> handlers = new HandlerList<AnimationStartEvent>();

	public AnimationStartEvent(Animation animation) {
		super(animation);
	}

	@Override
	public HandlerList<AnimationStartEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "AnimationStartEvent";
	}

}
