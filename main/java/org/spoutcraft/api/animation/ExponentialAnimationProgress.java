package org.spoutcraft.api.animation;

public class ExponentialAnimationProgress implements AnimationProgress {

	private int strength;
	
	public ExponentialAnimationProgress() {
		this(20);
	}
	
	public ExponentialAnimationProgress(int strength) {
		this.strength = strength;
	}
	
	@Override
	public double getValueAt(double progress) {
		return (Math.pow(strength, progress) - 1) / (strength - 1);
	}
}
