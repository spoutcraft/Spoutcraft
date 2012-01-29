package net.minecraft.src;

import java.util.*;

public class StatList {
	protected static Map oneShotStats = new HashMap();
	public static List field_25188_a = new ArrayList();
	public static List generalStats = new ArrayList();
	public static List itemStats = new ArrayList();
	public static List objectMineStats = new ArrayList();
	public static StatBase startGameStat = (new StatBasic(1000, "stat.startGame")).initIndependentStat().registerStat();
	public static StatBase createWorldStat = (new StatBasic(1001, "stat.createWorld")).initIndependentStat().registerStat();
	public static StatBase loadWorldStat = (new StatBasic(1002, "stat.loadWorld")).initIndependentStat().registerStat();
	public static StatBase joinMultiplayerStat = (new StatBasic(1003, "stat.joinMultiplayer")).initIndependentStat().registerStat();
	public static StatBase leaveGameStat = (new StatBasic(1004, "stat.leaveGame")).initIndependentStat().registerStat();
	public static StatBase minutesPlayedStat;
	public static StatBase distanceWalkedStat;
	public static StatBase distanceSwumStat;
	public static StatBase distanceFallenStat;
	public static StatBase distanceClimbedStat;
	public static StatBase distanceFlownStat;
	public static StatBase distanceDoveStat;
	public static StatBase distanceByMinecartStat;
	public static StatBase distanceByBoatStat;
	public static StatBase distanceByPigStat;
	public static StatBase jumpStat = (new StatBasic(2010, "stat.jump")).initIndependentStat().registerStat();
	public static StatBase dropStat = (new StatBasic(2011, "stat.drop")).initIndependentStat().registerStat();
	public static StatBase damageDealtStat = (new StatBasic(2020, "stat.damageDealt")).registerStat();
	public static StatBase damageTakenStat = (new StatBasic(2021, "stat.damageTaken")).registerStat();
	public static StatBase deathsStat = (new StatBasic(2022, "stat.deaths")).registerStat();
	public static StatBase mobKillsStat = (new StatBasic(2023, "stat.mobKills")).registerStat();
	public static StatBase playerKillsStat = (new StatBasic(2024, "stat.playerKills")).registerStat();
	public static StatBase fishCaughtStat = (new StatBasic(2025, "stat.fishCaught")).registerStat();
	public static StatBase mineBlockStatArray[] = initMinableStats("stat.mineBlock", 0x1000000);
	public static StatBase objectCraftStats[];
	public static StatBase objectUseStats[];
	public static StatBase objectBreakStats[];
	private static boolean blockStatsInitialized = false;
	private static boolean itemStatsInitialized = false;

	public StatList() {
	}

	public static void func_27360_a() {
	}

	public static void initBreakableStats() {
		objectUseStats = initUsableStats(objectUseStats, "stat.useItem", 0x1020000, 0, Block.blocksList.length);
		objectBreakStats = func_25149_b(objectBreakStats, "stat.breakItem", 0x1030000, 0, Block.blocksList.length);
		blockStatsInitialized = true;
		initCraftableStats();
	}

	public static void initStats() {
		objectUseStats = initUsableStats(objectUseStats, "stat.useItem", 0x1020000, Block.blocksList.length, 32000);
		objectBreakStats = func_25149_b(objectBreakStats, "stat.breakItem", 0x1030000, Block.blocksList.length, 32000);
		itemStatsInitialized = true;
		initCraftableStats();
	}

	public static void initCraftableStats() {
		if (!blockStatsInitialized || !itemStatsInitialized) {
			return;
		}
		HashSet hashset = new HashSet();
		IRecipe irecipe;
		for (Iterator iterator = CraftingManager.getInstance().getRecipeList().iterator(); iterator.hasNext(); hashset.add(Integer.valueOf(irecipe.getRecipeOutput().itemID))) {
			irecipe = (IRecipe)iterator.next();
		}

		ItemStack itemstack;
		for (Iterator iterator1 = FurnaceRecipes.smelting().getSmeltingList().values().iterator(); iterator1.hasNext(); hashset.add(Integer.valueOf(itemstack.itemID))) {
			itemstack = (ItemStack)iterator1.next();
		}

		objectCraftStats = new StatBase[32000];
		Iterator iterator2 = hashset.iterator();
		do {
			if (!iterator2.hasNext()) {
				break;
			}
			Integer integer = (Integer)iterator2.next();
			if (Item.itemsList[integer.intValue()] != null) {
				String s = StatCollector.translateToLocalFormatted("stat.craftItem", new Object[] {
				            Item.itemsList[integer.intValue()].getStatName()
				        });
				objectCraftStats[integer.intValue()] = (new StatCrafting(0x1010000 + integer.intValue(), s, integer.intValue())).registerStat();
			}
		}
		while (true);
		replaceAllSimilarBlocks(objectCraftStats);
	}

	private static StatBase[] initMinableStats(String s, int i) {
		StatBase astatbase[] = new StatBase[256];
		for (int j = 0; j < 256; j++) {
			if (Block.blocksList[j] != null && Block.blocksList[j].getEnableStats()) {
				String s1 = StatCollector.translateToLocalFormatted(s, new Object[] {
				            Block.blocksList[j].translateBlockName()
				        });
				astatbase[j] = (new StatCrafting(i + j, s1, j)).registerStat();
				objectMineStats.add((StatCrafting)astatbase[j]);
			}
		}

		replaceAllSimilarBlocks(astatbase);
		return astatbase;
	}

	private static StatBase[] initUsableStats(StatBase astatbase[], String s, int i, int j, int k) {
		if (astatbase == null) {
			astatbase = new StatBase[32000];
		}
		for (int l = j; l < k; l++) {
			if (Item.itemsList[l] == null) {
				continue;
			}
			String s1 = StatCollector.translateToLocalFormatted(s, new Object[] {
			            Item.itemsList[l].getStatName()
			        });
			astatbase[l] = (new StatCrafting(i + l, s1, l)).registerStat();
			if (l >= Block.blocksList.length) {
				itemStats.add((StatCrafting)astatbase[l]);
			}
		}

		replaceAllSimilarBlocks(astatbase);
		return astatbase;
	}

	private static StatBase[] func_25149_b(StatBase astatbase[], String s, int i, int j, int k) {
		if (astatbase == null) {
			astatbase = new StatBase[32000];
		}
		for (int l = j; l < k; l++) {
			if (Item.itemsList[l] != null && Item.itemsList[l].isDamageable()) {
				String s1 = StatCollector.translateToLocalFormatted(s, new Object[] {
				            Item.itemsList[l].getStatName()
				        });
				astatbase[l] = (new StatCrafting(i + l, s1, l)).registerStat();
			}
		}

		replaceAllSimilarBlocks(astatbase);
		return astatbase;
	}

	private static void replaceAllSimilarBlocks(StatBase astatbase[]) {
		replaceSimilarBlocks(astatbase, Block.waterStill.blockID, Block.waterMoving.blockID);
		replaceSimilarBlocks(astatbase, Block.lavaStill.blockID, Block.lavaStill.blockID);
		replaceSimilarBlocks(astatbase, Block.pumpkinLantern.blockID, Block.pumpkin.blockID);
		replaceSimilarBlocks(astatbase, Block.stoneOvenActive.blockID, Block.stoneOvenIdle.blockID);
		replaceSimilarBlocks(astatbase, Block.oreRedstoneGlowing.blockID, Block.oreRedstone.blockID);
		replaceSimilarBlocks(astatbase, Block.redstoneRepeaterActive.blockID, Block.redstoneRepeaterIdle.blockID);
		replaceSimilarBlocks(astatbase, Block.torchRedstoneActive.blockID, Block.torchRedstoneIdle.blockID);
		replaceSimilarBlocks(astatbase, Block.mushroomRed.blockID, Block.mushroomBrown.blockID);
		replaceSimilarBlocks(astatbase, Block.stairDouble.blockID, Block.stairSingle.blockID);
		replaceSimilarBlocks(astatbase, Block.grass.blockID, Block.dirt.blockID);
		replaceSimilarBlocks(astatbase, Block.tilledField.blockID, Block.dirt.blockID);
	}

	private static void replaceSimilarBlocks(StatBase astatbase[], int i, int j) {
		if (astatbase[i] != null && astatbase[j] == null) {
			astatbase[j] = astatbase[i];
			return;
		}
		else {
			field_25188_a.remove(astatbase[i]);
			objectMineStats.remove(astatbase[i]);
			generalStats.remove(astatbase[i]);
			astatbase[i] = astatbase[j];
			return;
		}
	}

	public static StatBase getOneShotStat(int i) {
		return (StatBase)oneShotStats.get(Integer.valueOf(i));
	}

	static {
		minutesPlayedStat = (new StatBasic(1100, "stat.playOneMinute", StatBase.timeStatType)).initIndependentStat().registerStat();
		distanceWalkedStat = (new StatBasic(2000, "stat.walkOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();
		distanceSwumStat = (new StatBasic(2001, "stat.swimOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();
		distanceFallenStat = (new StatBasic(2002, "stat.fallOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();
		distanceClimbedStat = (new StatBasic(2003, "stat.climbOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();
		distanceFlownStat = (new StatBasic(2004, "stat.flyOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();
		distanceDoveStat = (new StatBasic(2005, "stat.diveOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();
		distanceByMinecartStat = (new StatBasic(2006, "stat.minecartOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();
		distanceByBoatStat = (new StatBasic(2007, "stat.boatOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();
		distanceByPigStat = (new StatBasic(2008, "stat.pigOneCm", StatBase.distanceStatType)).initIndependentStat().registerStat();
		AchievementList.func_27374_a();
	}
}
