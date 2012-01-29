package net.minecraft.src;

import java.text.NumberFormat;

final class StatTypeSimple
	implements IStatType {
	StatTypeSimple() {
	}

	public String format(int i) {
		return StatBase.getNumberFormat().format(i);
	}
}
