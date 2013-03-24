/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.api.animation;

import java.util.Timer;
import java.util.TimerTask;

import org.spoutcraft.api.Spoutcraft;

public class Animation {
	public enum Direction {
		FORWARD(1),
		BACKWARD(-1);

		public final double modifier;

		private Direction(double mod) {
			this.modifier = mod;
		}
	}

	public enum State {
		STOPPED,
		PAUSED,
		RUNNING;
	}

	private Animatable startValue = null, endValue = null;
	private Number startNumber = null, endNumber = null;

	private int duration;

	int currentTime;
	private Direction direction = Direction.FORWARD;
	private State state = State.STOPPED;
	private AnimationProgress animationProgress = new LinearAnimationProgress();
	private ValueSetDelegate property;
	private static Timer timer = new Timer();
	private int delay = 1000 / 60;

	private AnimationRunnable animator = new AnimationRunnable(this);

	public State getState() {
		return state;
	}

	public Animatable getStartValue() {
		return startValue;
	}

	public void setStartValue(Animatable startValue) {
		this.startValue = startValue;
	}

	public Animatable getEndValue() {
		return endValue;
	}

	public void setEndValue(Animatable endValue) {
		this.endValue = endValue;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setAnimationProgress(AnimationProgress animationProgress) {
		this.animationProgress = animationProgress;
	}

	public AnimationProgress getAnimationProgress() {
		return animationProgress;
	}

	public void start() {
		this.state = State.RUNNING;
		switch (direction) {
			case FORWARD:
				currentTime = 0;
				break;
			case BACKWARD:
				currentTime = duration;
				break;
		}
		timer.schedule(animator, delay, delay);
	}

	public void pause() {
		this.state = State.PAUSED;
		animator.cancel();
	}

	public void resume() {
		if (this.state == State.PAUSED) {
			this.state = State.RUNNING;
			timer.schedule(animator, delay, delay);
		}
	}

	public void stop() {
		this.state = State.STOPPED;
		switch (direction) {
			case FORWARD:
				this.currentTime = duration;
				break;
			case BACKWARD:
				this.currentTime = 0;
				break;
		}
		animator.cancel();
		if (startValue != null) {
			property.set(getCurrentValue());
		} else {
			property.set(getCurrentValueNumber());
		}
	}

	public Animatable getCurrentValue() {
		return startValue.getValueAt(getAnimationProgress().getValueAt((double) currentTime / (double) duration), startValue, endValue);
	}

	public void setValueDelegate(ValueSetDelegate property) {
		this.property = property;
	}

	public ValueSetDelegate getValueDelegate() {
		return property;
	}

	public void setStartNumber(Number startNumber) {
		this.startNumber = startNumber;
	}

	public Number getStartNumber() {
		return startNumber;
	}

	public void setEndNumber(Number endNumber) {
		this.endNumber = endNumber;
	}

	public Number getEndNumber() {
		return endNumber;
	}

	public void setFramesPerSecond(int fps) {
		delay = 1000 / fps;
	}

	private class AnimationRunnable extends TimerTask {
		public Animation animation;

		public AnimationRunnable(Animation ani) {
			animation = ani;
		}

		@Override
		public void run() {
			try {
				if (animation.getState() != Animation.State.RUNNING) {
					cancel();
					return;
				}
				int time = animation.getCurrentTime();
				time += delay * animation.getDirection().modifier; // For the direction
				if (time >= animation.getDuration() && animation.getDirection() == Direction.FORWARD) {
					animation.stop();
					time = animation.getDuration();
				}
				if (time <= 0 && animation.getDirection() == Direction.BACKWARD) {
					animation.stop();
					time = 0;
				}
				animation.setCurrentTime(time);
				if (startNumber == null) {
					Animatable value = animation.getCurrentValue();
					animation.getValueDelegate().set(value);
				} else {
					Number value = animation.getCurrentValueNumber();
					animation.getValueDelegate().set(value);
				}
			} catch (Exception e) {
				e.printStackTrace();
				animation.state = Animation.State.STOPPED;
				cancel();
			}
		}
	}

	public Number getCurrentValueNumber() {
		double p = (double) currentTime / (double) duration;
		if (startNumber instanceof Integer) {
			int p1 = (Integer) startNumber, p2 = (Integer) endNumber;
			return (int) (p1 + (p2 - p1)*p);
		}
		if (startNumber instanceof Double) {
			double p1 = (Double) startNumber, p2 = (Double) endNumber;
			return p1 + (p2 - p1)*p;
		}
		if (startNumber instanceof Long) {
			long p1 = (Long) startNumber, p2 = (Long) endNumber;
			return (long) (p1 + (p2 - p1)*p);
		}
		if (startNumber instanceof Float) {
			float p1 = (Float) startNumber, p2 = (Float) endNumber;
			return (float) (p1 + (p2 - p1)*p);
		}
		if (startNumber instanceof Short) {
			short p1 = (Short) startNumber, p2 = (Short) endNumber;
			return (short) (p1 + (p2 - p1)*p);
		}
		if (startNumber instanceof Byte) {
			byte p1 = (Byte) startNumber, p2 = (Byte) endNumber;
			return (byte) (p1 + (p2 - p1)*p);
		}
		throw new IllegalStateException("Numbers of type " + startNumber.getClass().getSimpleName() + " cannot be used.");
	}
}
