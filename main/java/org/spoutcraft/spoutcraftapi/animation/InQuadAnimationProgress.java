package org.spoutcraft.spoutcraftapi.animation;

import org.spoutcraft.spoutcraftapi.animation.AnimationProgress;

public class InQuadAnimationProgress implements AnimationProgress {

	private int strength;
	
	public InQuadAnimationProgress() {
		this(2);
	}
	
	public InQuadAnimationProgress(int strength) {
		this.strength = strength;
	}
	
	public double getValueAt(double progress) {
		return Math.pow(progress, strength);
	}
}