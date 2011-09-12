/*
 * This file is part of Spoutcraft API (http://wiki.getspout.org/).
 * 
 * Spoutcraft API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.animation;

import java.util.Timer;
import java.util.TimerTask;

public class Animation {
	public enum Direction {
		FORWARD(1),
		BACKWARD(-1);
		
		public final double modifier;
		private Direction(double mod){
			this.modifier = mod;
		}
	}
	public enum State {
		STOPPED, PAUSED, RUNNING;
	}
	
	private Animatable startValue = null, endValue = null;
	private Number startNumber = null, endNumber = null;
	
	private int duration;

	int currentTime;
	private Direction direction;
	private State state;
	private AnimationProgress animationProgress = new LinearAnimationProgress();
	private ValueSetDelegate property;
	private static Timer timer = new Timer();
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
	
	public void start(){
		this.state = State.RUNNING;
		switch(direction){
		case FORWARD:
			currentTime = 0;
			break;
		case BACKWARD:
			currentTime = duration;
			break;
		}
		timer.schedule(animator, 0, 50);
	}
	
	public void pause(){
		this.state = State.PAUSED;
		animator.cancel();
	}
	
	public void resume(){
		if(this.state == State.PAUSED){
			this.state = State.RUNNING;
			timer.schedule(animator, 0, 50);
		}
	}
	
	public void stop(){
		this.state = State.STOPPED;
		switch(direction){
		case FORWARD:
			this.currentTime = duration;
			break;
		case BACKWARD:
			this.currentTime = 0;
			break;
		}
		animator.cancel();
		property.set(getCurrentValue());
	}
	
	public Animatable getCurrentValue(){
		return startValue.getValueAt(getAnimationProgress().getValueAt((double)currentTime/(double)duration), startValue, endValue);
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

	private class AnimationRunnable extends TimerTask{
		public Animation animation;
		public AnimationRunnable(Animation ani) {
			animation = ani;
		}
		
		@Override
		public void run() {
			if(animation.getState()!=Animation.State.RUNNING){
				return;
			}
			int time = animation.getCurrentTime();
			time += 50 * animation.getDirection().modifier; //For the direction
			if(time >= animation.getDuration() && animation.getDirection() == Direction.FORWARD){
				animation.stop();
				time = animation.getDuration();
			}
			if(time <= 0 && animation.getDirection() == Direction.BACKWARD){
				animation.stop();
				time = 0;
			}
			animation.setCurrentTime(time);
			if(startNumber == null){
				Animatable value = animation.getCurrentValue();
				animation.getValueDelegate().set(value);
			} else {
				Number value = animation.getCurrentValueNumber();
				animation.getValueDelegate().set(value);
			}
		}
	}

	public Number getCurrentValueNumber() {
		// TODO Auto-generated method stub
		return null;
	}
}
