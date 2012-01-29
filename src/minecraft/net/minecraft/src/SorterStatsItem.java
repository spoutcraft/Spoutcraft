package net.minecraft.src;

import java.util.Comparator;

class SorterStatsItem
	implements Comparator {
	final GuiStats statsGUI;
	final GuiSlotStatsItem slotStatsItemGUI;

	SorterStatsItem(GuiSlotStatsItem guislotstatsitem, GuiStats guistats) {
		slotStatsItemGUI = guislotstatsitem;
		statsGUI = guistats;
	}

	public int func_27371_a(StatCrafting statcrafting, StatCrafting statcrafting1) {
		int i = statcrafting.func_25072_b();
		int j = statcrafting1.func_25072_b();
		StatBase statbase = null;
		StatBase statbase1 = null;
		if (slotStatsItemGUI.field_27271_e == 0) {
			statbase = StatList.objectBreakStats[i];
			statbase1 = StatList.objectBreakStats[j];
		}
		else if (slotStatsItemGUI.field_27271_e == 1) {
			statbase = StatList.objectCraftStats[i];
			statbase1 = StatList.objectCraftStats[j];
		}
		else if (slotStatsItemGUI.field_27271_e == 2) {
			statbase = StatList.objectUseStats[i];
			statbase1 = StatList.objectUseStats[j];
		}
		if (statbase != null || statbase1 != null) {
			if (statbase == null) {
				return 1;
			}
			if (statbase1 == null) {
				return -1;
			}
			int k = GuiStats.getStatsFileWriter(slotStatsItemGUI.field_27275_a).writeStat(statbase);
			int l = GuiStats.getStatsFileWriter(slotStatsItemGUI.field_27275_a).writeStat(statbase1);
			if (k != l) {
				return (k - l) * slotStatsItemGUI.field_27270_f;
			}
		}
		return i - j;
	}

	public int compare(Object obj, Object obj1) {
		return func_27371_a((StatCrafting)obj, (StatCrafting)obj1);
	}
}
