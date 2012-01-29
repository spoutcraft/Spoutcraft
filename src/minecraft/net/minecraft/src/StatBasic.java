package net.minecraft.src;

import java.util.List;

public class StatBasic extends StatBase {
	public StatBasic(int i, String s, IStatType istattype) {
		super(i, s, istattype);
	}

	public StatBasic(int i, String s) {
		super(i, s);
	}

	public StatBase registerStat() {
		super.registerStat();
		StatList.generalStats.add(this);
		return this;
	}
}
