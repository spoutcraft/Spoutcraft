package net.minecraft.src;

import java.util.Random;

public class Particle {
	private static Random rand = new Random();
	public double posX;
	public double posY;
	public double prevPosX;
	public double prevPosY;
	public double velocityX;
	public double velocityY;
	public double accelScale;
	public boolean isDead;
	public int timeTick;
	public int timeLimit;
	public double tintRed;
	public double tintGreen;
	public double tintBlue;
	public double tintAlpha;
	public double prevTintRed;
	public double prevTintGreen;
	public double prevTintBlue;
	public double prevTintAlpha;

	public void update(GuiParticle guiparticle) {
		posX += velocityX;
		posY += velocityY;
		velocityX *= accelScale;
		velocityY *= accelScale;
		velocityY += 0.10000000000000001D;
		if (++timeTick > timeLimit) {
			setDead();
		}
		tintAlpha = 2D - ((double)timeTick / (double)timeLimit) * 2D;
		if (tintAlpha > 1.0D) {
			tintAlpha = 1.0D;
		}
		tintAlpha = tintAlpha * tintAlpha;
		tintAlpha *= 0.5D;
	}

	public void preUpdate() {
		prevTintRed = tintRed;
		prevTintGreen = tintGreen;
		prevTintBlue = tintBlue;
		prevTintAlpha = tintAlpha;
		prevPosX = posX;
		prevPosY = posY;
	}

	public void setDead() {
		isDead = true;
	}
}
