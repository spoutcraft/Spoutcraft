package org.spoutcraft.api.animation;

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
	
	private Animateable startValue, endValue;
	
	private int duration;

	int currentTime;
	private Direction direction;
	private State state;
	private AnimationProgress animationProgress = new LinearAnimationProgress();
	private PropertyDelegate property;
	private static Timer timer = new Timer();
	private AnimationRunnable animator = new AnimationRunnable(this);
	
	public State getState() {
		return state;
	}
	
	public Animateable getStartValue() {
		return startValue;
	}
	
	public void setStartValue(Animateable startValue) {
		this.startValue = startValue;
	}
	
	public Animateable getEndValue() {
		return endValue;
	}
	
	public void setEndValue(Animateable endValue) {
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
		this.currentTime = duration;
		animator.cancel();
	}
	
	public Animateable getCurrentValue(){
		return startValue.getValueAt(getAnimationProgress().getValueAt((double)currentTime/(double)duration), startValue, endValue);
	}
	
	public void setProperty(PropertyDelegate property) {
		this.property = property;
	}

	public PropertyDelegate getProperty() {
		return property;
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
			Animateable value = animation.getCurrentValue();
			animation.getProperty().set(value);
		}
	}
}
