package net.minecraft.src;

import java.text.DecimalFormat;

final class StatTypeTime
	implements IStatType {
	StatTypeTime() {
	}

	public String format(int i) {
		double d = (double)i / 20D;
		double d1 = d / 60D;
		double d2 = d1 / 60D;
		double d3 = d2 / 24D;
		double d4 = d3 / 365D;
		if (d4 > 0.5D) {
			return (new StringBuilder()).append(StatBase.getDecimalFormat().format(d4)).append(" y").toString();
		}
		if (d3 > 0.5D) {
			return (new StringBuilder()).append(StatBase.getDecimalFormat().format(d3)).append(" d").toString();
		}
		if (d2 > 0.5D) {
			return (new StringBuilder()).append(StatBase.getDecimalFormat().format(d2)).append(" h").toString();
		}
		if (d1 > 0.5D) {
			return (new StringBuilder()).append(StatBase.getDecimalFormat().format(d1)).append(" m").toString();
		}
		else {
			return (new StringBuilder()).append(d).append(" s").toString();
		}
	}
}
