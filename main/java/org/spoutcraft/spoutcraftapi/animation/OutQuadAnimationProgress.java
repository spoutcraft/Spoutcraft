package org.spoutcraft.spoutcraftapi.animation;

public class OutQuadAnimationProgress implements AnimationProgress {

	private int strength;
	
	public OutQuadAnimationProgress() {
		this(2);
	}
	
	public OutQuadAnimationProgress(int strength) {
		this.strength = strength;
	}
	
	public double getValueAt(double progress) {
		return -Math.abs(Math.pow(progress - 1, strength)) + 1;
	}
}