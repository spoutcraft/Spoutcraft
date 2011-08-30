package org.spoutcraft.api.animation;

public class LogorithmicAnimationProgress implements AnimationProgress {

	private int strength;
	private double slog;
	
	public LogorithmicAnimationProgress() {
		this(20);
	}
	
	public LogorithmicAnimationProgress(int strength) {
		this.strength = strength;
		this.slog = Math.log(strength + 1);
	}
	
	@Override
	public double getValueAt(double progress) {
		return Math.log(progress * strength + 1) / slog;
	}
}
