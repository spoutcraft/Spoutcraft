package org.spoutcraft.spoutcraftapi.animation;

public class ExponentialAnimationProgress implements AnimationProgress {

	private int strength;
	
	public ExponentialAnimationProgress() {
		this(20);
	}
	
	public ExponentialAnimationProgress(int strength) {
		this.strength = strength;
	}
	
	public double getValueAt(double progress) {
		return (Math.pow(strength, progress) - 1) / (strength - 1);
	}
}
