package org.spoutcraft.spoutcraftapi.animation;

public class InQuadAnimationProgress implements AnimationProgress {

	private int strength;
	
	public InQuadAnimationProgress() {
		this(2);
	}
	
	public InQuadAnimationProgress(int strength) {
		this.strength = strength;
	}
	
	@Override
	public double getValueAt(double progress) {
		return Math.pow(progress, strength);
	}
}