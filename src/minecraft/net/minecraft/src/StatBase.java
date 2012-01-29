package net.minecraft.src;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class StatBase {
	public final int statId;
	private final String statName;
	public boolean isIndependent;
	public String statGuid;
	private final IStatType type;
	private static NumberFormat numberFormat;
	public static IStatType simpleStatType = new StatTypeSimple();
	private static DecimalFormat decimalFormat = new DecimalFormat("########0.00");
	public static IStatType timeStatType = new StatTypeTime();
	public static IStatType distanceStatType = new StatTypeDistance();

	public StatBase(int i, String s, IStatType istattype) {
		isIndependent = false;
		statId = i;
		statName = s;
		type = istattype;
	}

	public StatBase(int i, String s) {
		this(i, s, simpleStatType);
	}

	public StatBase initIndependentStat() {
		isIndependent = true;
		return this;
	}

	public StatBase registerStat() {
		if (StatList.oneShotStats.containsKey(Integer.valueOf(statId))) {
			throw new RuntimeException((new StringBuilder()).append("Duplicate stat id: \"").append(((StatBase)StatList.oneShotStats.get(Integer.valueOf(statId))).statName).append("\" and \"").append(statName).append("\" at id ").append(statId).toString());
		}
		else {
			StatList.field_25188_a.add(this);
			StatList.oneShotStats.put(Integer.valueOf(statId), this);
			statGuid = AchievementMap.getGuid(statId);
			return this;
		}
	}

	public boolean isAchievement() {
		return false;
	}

	public String func_27084_a(int i) {
		return type.format(i);
	}

	public String func_44020_i() {
		return statName;
	}

	public String toString() {
		return StatCollector.translateToLocal(statName);
	}

	static NumberFormat getNumberFormat() {
		return numberFormat;
	}

	static DecimalFormat getDecimalFormat() {
		return decimalFormat;
	}

	static {
		numberFormat = NumberFormat.getIntegerInstance(Locale.US);
	}
}
