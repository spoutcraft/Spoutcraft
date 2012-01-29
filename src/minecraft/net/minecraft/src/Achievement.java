package net.minecraft.src;

import java.util.List;

public class Achievement extends StatBase {
	public final int displayColumn;
	public final int displayRow;
	public final Achievement parentAchievement;
	private final String achievementDescription;
	private IStatStringFormat statStringFormatter;
	public final ItemStack theItemStack;
	private boolean isSpecial;

	public Achievement(int i, String s, int j, int k, Item item, Achievement achievement) {
		this(i, s, j, k, new ItemStack(item), achievement);
	}

	public Achievement(int i, String s, int j, int k, Block block, Achievement achievement) {
		this(i, s, j, k, new ItemStack(block), achievement);
	}

	public Achievement(int i, String s, int j, int k, ItemStack itemstack, Achievement achievement) {
		super(0x500000 + i, (new StringBuilder()).append("achievement.").append(s).toString());
		theItemStack = itemstack;
		achievementDescription = (new StringBuilder()).append("achievement.").append(s).append(".desc").toString();
		displayColumn = j;
		displayRow = k;
		if (j < AchievementList.minDisplayColumn) {
			AchievementList.minDisplayColumn = j;
		}
		if (k < AchievementList.minDisplayRow) {
			AchievementList.minDisplayRow = k;
		}
		if (j > AchievementList.maxDisplayColumn) {
			AchievementList.maxDisplayColumn = j;
		}
		if (k > AchievementList.maxDisplayRow) {
			AchievementList.maxDisplayRow = k;
		}
		parentAchievement = achievement;
	}

	public Achievement setIndependent() {
		isIndependent = true;
		return this;
	}

	public Achievement setSpecial() {
		isSpecial = true;
		return this;
	}

	public Achievement registerAchievement() {
		super.registerStat();
		AchievementList.achievementList.add(this);
		return this;
	}

	public boolean isAchievement() {
		return true;
	}

	public String getDescription() {
		if (statStringFormatter != null) {
			return statStringFormatter.formatString(StatCollector.translateToLocal(achievementDescription));
		}
		else {
			return StatCollector.translateToLocal(achievementDescription);
		}
	}

	public Achievement setStatStringFormatter(IStatStringFormat istatstringformat) {
		statStringFormatter = istatstringformat;
		return this;
	}

	public boolean getSpecial() {
		return isSpecial;
	}

	public StatBase registerStat() {
		return registerAchievement();
	}

	public StatBase initIndependentStat() {
		return setIndependent();
	}
}
