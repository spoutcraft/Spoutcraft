package net.minecraft.src;

final class StepSoundSand extends StepSound {
	StepSoundSand(String s, float f, float f1) {
		super(s, f, f1);
	}

	public String stepSoundDir() {
		return "step.gravel";
	}
}
