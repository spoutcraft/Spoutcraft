package net.minecraft.src;

public class PotionHealth extends Potion {
	public PotionHealth(int i, boolean flag, int j) {
		super(i, flag, j);
	}

	public boolean isInstant() {
		return true;
	}

	public boolean isReady(int i, int j) {
		return i >= 1;
	}
}
