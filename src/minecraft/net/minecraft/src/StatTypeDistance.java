package net.minecraft.src;

import java.text.DecimalFormat;

final class StatTypeDistance
	implements IStatType {
	StatTypeDistance() {
	}

	public String format(int i) {
		int j = i;
		double d = (double)j / 100D;
		double d1 = d / 1000D;
		if (d1 > 0.5D) {
			return (new StringBuilder()).append(StatBase.getDecimalFormat().format(d1)).append(" km").toString();
		}
		if (d > 0.5D) {
			return (new StringBuilder()).append(StatBase.getDecimalFormat().format(d)).append(" m").toString();
		}
		else {
			return (new StringBuilder()).append(i).append(" cm").toString();
		}
	}
}
