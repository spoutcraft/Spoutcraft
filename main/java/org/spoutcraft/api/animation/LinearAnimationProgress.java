package org.spoutcraft.api.animation;

public class LinearAnimationProgress implements AnimationProgress {

	@Override
	public double getValueAt(double progress) {
		return progress;
	}
}
