package net.minecraft.src;

public class StepSound {
	public final String stepSoundName;
	public final float stepSoundVolume;
	public final float stepSoundPitch;

	public StepSound(String s, float f, float f1) {
		stepSoundName = s;
		stepSoundVolume = f;
		stepSoundPitch = f1;
	}

	public float getVolume() {
		return stepSoundVolume;
	}

	public float getPitch() {
		return stepSoundPitch;
	}

	public String stepSoundDir() {
		return (new StringBuilder()).append("step.").append(stepSoundName).toString();
	}

	public String stepSoundDir2() {
		return (new StringBuilder()).append("step.").append(stepSoundName).toString();
	}
}
