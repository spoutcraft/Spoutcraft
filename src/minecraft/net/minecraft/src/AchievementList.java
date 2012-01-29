package net.minecraft.src;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class AchievementList {
	public static int minDisplayColumn;
	public static int minDisplayRow;
	public static int maxDisplayColumn;
	public static int maxDisplayRow;
	public static List achievementList;
	public static Achievement openInventory;
	public static Achievement mineWood;
	public static Achievement buildWorkBench;
	public static Achievement buildPickaxe;
	public static Achievement buildFurnace;
	public static Achievement acquireIron;
	public static Achievement buildHoe;
	public static Achievement makeBread;
	public static Achievement bakeCake;
	public static Achievement buildBetterPickaxe;
	public static Achievement cookFish;
	public static Achievement onARail;
	public static Achievement buildSword;
	public static Achievement killEnemy;
	public static Achievement killCow;
	public static Achievement flyPig;
	public static Achievement snipeSkeleton;
	public static Achievement diamonds;
	public static Achievement portal;
	public static Achievement ghast;
	public static Achievement blazeRod;
	public static Achievement potion;
	public static Achievement theEnd;
	public static Achievement theEnd2;
	public static Achievement enchantments;
	public static Achievement overkill;
	public static Achievement bookcase;

	public AchievementList() {
	}

	public static void func_27374_a() {
	}

	static {
		achievementList = new ArrayList();
		openInventory = (new Achievement(0, "openInventory", 0, 0, Item.book, null)).setIndependent().registerAchievement();
		mineWood = (new Achievement(1, "mineWood", 2, 1, Block.wood, openInventory)).registerAchievement();
		buildWorkBench = (new Achievement(2, "buildWorkBench", 4, -1, Block.workbench, mineWood)).registerAchievement();
		buildPickaxe = (new Achievement(3, "buildPickaxe", 4, 2, Item.pickaxeWood, buildWorkBench)).registerAchievement();
		buildFurnace = (new Achievement(4, "buildFurnace", 3, 4, Block.stoneOvenActive, buildPickaxe)).registerAchievement();
		acquireIron = (new Achievement(5, "acquireIron", 1, 4, Item.ingotIron, buildFurnace)).registerAchievement();
		buildHoe = (new Achievement(6, "buildHoe", 2, -3, Item.hoeWood, buildWorkBench)).registerAchievement();
		makeBread = (new Achievement(7, "makeBread", -1, -3, Item.bread, buildHoe)).registerAchievement();
		bakeCake = (new Achievement(8, "bakeCake", 0, -5, Item.cake, buildHoe)).registerAchievement();
		buildBetterPickaxe = (new Achievement(9, "buildBetterPickaxe", 6, 2, Item.pickaxeStone, buildPickaxe)).registerAchievement();
		cookFish = (new Achievement(10, "cookFish", 2, 6, Item.fishCooked, buildFurnace)).registerAchievement();
		onARail = (new Achievement(11, "onARail", 2, 3, Block.rail, acquireIron)).setSpecial().registerAchievement();
		buildSword = (new Achievement(12, "buildSword", 6, -1, Item.swordWood, buildWorkBench)).registerAchievement();
		killEnemy = (new Achievement(13, "killEnemy", 8, -1, Item.bone, buildSword)).registerAchievement();
		killCow = (new Achievement(14, "killCow", 7, -3, Item.leather, buildSword)).registerAchievement();
		flyPig = (new Achievement(15, "flyPig", 8, -4, Item.saddle, killCow)).setSpecial().registerAchievement();
		snipeSkeleton = (new Achievement(16, "snipeSkeleton", 7, 0, Item.bow, killEnemy)).setSpecial().registerAchievement();
		diamonds = (new Achievement(17, "diamonds", -1, 5, Item.diamond, acquireIron)).registerAchievement();
		portal = (new Achievement(18, "portal", -1, 7, Block.obsidian, diamonds)).registerAchievement();
		ghast = (new Achievement(19, "ghast", -4, 8, Item.ghastTear, portal)).setSpecial().registerAchievement();
		blazeRod = (new Achievement(20, "blazeRod", 0, 9, Item.blazeRod, portal)).registerAchievement();
		potion = (new Achievement(21, "potion", 2, 8, Item.potion, blazeRod)).registerAchievement();
		theEnd = (new Achievement(22, "theEnd", 3, 10, Item.eyeOfEnder, blazeRod)).setSpecial().registerAchievement();
		theEnd2 = (new Achievement(23, "theEnd2", 4, 13, Block.dragonEgg, theEnd)).setSpecial().registerAchievement();
		enchantments = (new Achievement(24, "enchantments", -4, 4, Block.enchantmentTable, diamonds)).registerAchievement();
		overkill = (new Achievement(25, "overkill", -4, 1, Item.swordDiamond, enchantments)).setSpecial().registerAchievement();
		bookcase = (new Achievement(26, "bookcase", -3, 6, Block.bookShelf, enchantments)).registerAchievement();
		System.out.println((new StringBuilder()).append(achievementList.size()).append(" achievements").toString());
	}
}
