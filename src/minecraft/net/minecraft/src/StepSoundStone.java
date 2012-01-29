package net.minecraft.src;

final class StepSoundStone extends StepSound {
	StepSoundStone(String s, float f, float f1) {
		super(s, f, f1);
	}

	public String stepSoundDir() {
		return "random.glass";
	}
}
