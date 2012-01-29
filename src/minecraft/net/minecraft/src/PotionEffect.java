package net.minecraft.src;

import java.io.PrintStream;

public class PotionEffect {
	private int potionID;
	private int duration;
	private int amplifier;

	public PotionEffect(int i, int j, int k) {
		potionID = i;
		duration = j;
		amplifier = k;
	}

	public PotionEffect(PotionEffect potioneffect) {
		potionID = potioneffect.potionID;
		duration = potioneffect.duration;
		amplifier = potioneffect.amplifier;
	}

	public void combine(PotionEffect potioneffect) {
		if (potionID != potioneffect.potionID) {
			System.err.println("This method should only be called for matching effects!");
		}
		if (potioneffect.amplifier > amplifier) {
			amplifier = potioneffect.amplifier;
			duration = potioneffect.duration;
		}
		else if (potioneffect.amplifier == amplifier && duration < potioneffect.duration) {
			duration = potioneffect.duration;
		}
	}

	public int getPotionID() {
		return potionID;
	}

	public int getDuration() {
		return duration;
	}

	public int getAmplifier() {
		return amplifier;
	}

	public boolean onUpdate(EntityLiving entityliving) {
		if (duration > 0) {
			if (Potion.potionTypes[potionID].isReady(duration, amplifier)) {
				performEffect(entityliving);
			}
			deincrementDuration();
		}
		return duration > 0;
	}

	private int deincrementDuration() {
		return --duration;
	}

	public void performEffect(EntityLiving entityliving) {
		if (duration > 0) {
			Potion.potionTypes[potionID].performEffect(entityliving, amplifier);
		}
	}

	public String getEffectName() {
		return Potion.potionTypes[potionID].getName();
	}

	public int hashCode() {
		return potionID;
	}

	public String toString() {
		String s = "";
		if (getAmplifier() > 0) {
			s = (new StringBuilder()).append(getEffectName()).append(" x ").append(getAmplifier() + 1).append(", Duration: ").append(getDuration()).toString();
		}
		else {
			s = (new StringBuilder()).append(getEffectName()).append(", Duration: ").append(getDuration()).toString();
		}
		if (Potion.potionTypes[potionID].func_40612_i()) {
			return (new StringBuilder()).append("(").append(s).append(")").toString();
		}
		else {
			return s;
		}
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof PotionEffect)) {
			return false;
		}
		else {
			PotionEffect potioneffect = (PotionEffect)obj;
			return potionID == potioneffect.potionID && amplifier == potioneffect.amplifier && duration == potioneffect.duration;
		}
	}
}
