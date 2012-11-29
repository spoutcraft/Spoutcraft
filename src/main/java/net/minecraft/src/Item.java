package net.minecraft.src;

import java.util.List;
import java.util.Random;
// Spout Start
import org.spoutcraft.client.item.SpoutItem;
// Spout End

public class Item {
	private CreativeTabs tabToDisplayOn = null;

	/** The RNG used by the Item subclasses. */
	protected static Random itemRand = new Random();

	/** A 32000 elements Item array. */
	public static Item[] itemsList = new Item[32000];
	public static Item shovelSteel = (new ItemSpade(0, EnumToolMaterial.IRON)).setIconCoord(2, 5).setItemName("shovelIron");
	public static Item pickaxeSteel = (new ItemPickaxe(1, EnumToolMaterial.IRON)).setIconCoord(2, 6).setItemName("pickaxeIron");
	public static Item axeSteel = (new ItemAxe(2, EnumToolMaterial.IRON)).setIconCoord(2, 7).setItemName("hatchetIron");
	public static Item flintAndSteel = (new ItemFlintAndSteel(3)).setIconCoord(5, 0).setItemName("flintAndSteel");
	public static Item appleRed = (new ItemFood(4, 4, 0.3F, false)).setIconCoord(10, 0).setItemName("apple");
	public static Item bow = (new ItemBow(5)).setIconCoord(5, 1).setItemName("bow");
	public static Item arrow = (new Item(6)).setIconCoord(5, 2).setItemName("arrow").setCreativeTab(CreativeTabs.tabCombat);
	public static Item coal = (new ItemCoal(7)).setIconCoord(7, 0).setItemName("coal");
	public static Item diamond = (new Item(8)).setIconCoord(7, 3).setItemName("diamond").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item ingotIron = (new Item(9)).setIconCoord(7, 1).setItemName("ingotIron").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item ingotGold = (new Item(10)).setIconCoord(7, 2).setItemName("ingotGold").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item swordSteel = (new ItemSword(11, EnumToolMaterial.IRON)).setIconCoord(2, 4).setItemName("swordIron");
	public static Item swordWood = (new ItemSword(12, EnumToolMaterial.WOOD)).setIconCoord(0, 4).setItemName("swordWood");
	public static Item shovelWood = (new ItemSpade(13, EnumToolMaterial.WOOD)).setIconCoord(0, 5).setItemName("shovelWood");
	public static Item pickaxeWood = (new ItemPickaxe(14, EnumToolMaterial.WOOD)).setIconCoord(0, 6).setItemName("pickaxeWood");
	public static Item axeWood = (new ItemAxe(15, EnumToolMaterial.WOOD)).setIconCoord(0, 7).setItemName("hatchetWood");
	public static Item swordStone = (new ItemSword(16, EnumToolMaterial.STONE)).setIconCoord(1, 4).setItemName("swordStone");
	public static Item shovelStone = (new ItemSpade(17, EnumToolMaterial.STONE)).setIconCoord(1, 5).setItemName("shovelStone");
	public static Item pickaxeStone = (new ItemPickaxe(18, EnumToolMaterial.STONE)).setIconCoord(1, 6).setItemName("pickaxeStone");
	public static Item axeStone = (new ItemAxe(19, EnumToolMaterial.STONE)).setIconCoord(1, 7).setItemName("hatchetStone");
	public static Item swordDiamond = (new ItemSword(20, EnumToolMaterial.EMERALD)).setIconCoord(3, 4).setItemName("swordDiamond");
	public static Item shovelDiamond = (new ItemSpade(21, EnumToolMaterial.EMERALD)).setIconCoord(3, 5).setItemName("shovelDiamond");
	public static Item pickaxeDiamond = (new ItemPickaxe(22, EnumToolMaterial.EMERALD)).setIconCoord(3, 6).setItemName("pickaxeDiamond");
	public static Item axeDiamond = (new ItemAxe(23, EnumToolMaterial.EMERALD)).setIconCoord(3, 7).setItemName("hatchetDiamond");
	public static Item stick = (new Item(24)).setIconCoord(5, 3).setFull3D().setItemName("stick").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item bowlEmpty = (new Item(25)).setIconCoord(7, 4).setItemName("bowl").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item bowlSoup = (new ItemSoup(26, 6)).setIconCoord(8, 4).setItemName("mushroomStew");
	public static Item swordGold = (new ItemSword(27, EnumToolMaterial.GOLD)).setIconCoord(4, 4).setItemName("swordGold");
	public static Item shovelGold = (new ItemSpade(28, EnumToolMaterial.GOLD)).setIconCoord(4, 5).setItemName("shovelGold");
	public static Item pickaxeGold = (new ItemPickaxe(29, EnumToolMaterial.GOLD)).setIconCoord(4, 6).setItemName("pickaxeGold");
	public static Item axeGold = (new ItemAxe(30, EnumToolMaterial.GOLD)).setIconCoord(4, 7).setItemName("hatchetGold");
	public static Item silk = (new ItemReed(31, Block.tripWire)).setIconCoord(8, 0).setItemName("string").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item feather = (new Item(32)).setIconCoord(8, 1).setItemName("feather").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item gunpowder = (new Item(33)).setIconCoord(8, 2).setItemName("sulphur").setPotionEffect(PotionHelper.gunpowderEffect).setCreativeTab(CreativeTabs.tabMaterials);
	public static Item hoeWood = (new ItemHoe(34, EnumToolMaterial.WOOD)).setIconCoord(0, 8).setItemName("hoeWood");
	public static Item hoeStone = (new ItemHoe(35, EnumToolMaterial.STONE)).setIconCoord(1, 8).setItemName("hoeStone");
	public static Item hoeSteel = (new ItemHoe(36, EnumToolMaterial.IRON)).setIconCoord(2, 8).setItemName("hoeIron");
	public static Item hoeDiamond = (new ItemHoe(37, EnumToolMaterial.EMERALD)).setIconCoord(3, 8).setItemName("hoeDiamond");
	public static Item hoeGold = (new ItemHoe(38, EnumToolMaterial.GOLD)).setIconCoord(4, 8).setItemName("hoeGold");
	public static Item seeds = (new ItemSeeds(39, Block.crops.blockID, Block.tilledField.blockID)).setIconCoord(9, 0).setItemName("seeds");
	public static Item wheat = (new Item(40)).setIconCoord(9, 1).setItemName("wheat").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item bread = (new ItemFood(41, 5, 0.6F, false)).setIconCoord(9, 2).setItemName("bread");
	public static Item helmetLeather = (new ItemArmor(42, EnumArmorMaterial.CLOTH, 0, 0)).setIconCoord(0, 0).setItemName("helmetCloth");
	public static Item plateLeather = (new ItemArmor(43, EnumArmorMaterial.CLOTH, 0, 1)).setIconCoord(0, 1).setItemName("chestplateCloth");
	public static Item legsLeather = (new ItemArmor(44, EnumArmorMaterial.CLOTH, 0, 2)).setIconCoord(0, 2).setItemName("leggingsCloth");
	public static Item bootsLeather = (new ItemArmor(45, EnumArmorMaterial.CLOTH, 0, 3)).setIconCoord(0, 3).setItemName("bootsCloth");
	public static Item helmetChain = (new ItemArmor(46, EnumArmorMaterial.CHAIN, 1, 0)).setIconCoord(1, 0).setItemName("helmetChain");
	public static Item plateChain = (new ItemArmor(47, EnumArmorMaterial.CHAIN, 1, 1)).setIconCoord(1, 1).setItemName("chestplateChain");
	public static Item legsChain = (new ItemArmor(48, EnumArmorMaterial.CHAIN, 1, 2)).setIconCoord(1, 2).setItemName("leggingsChain");
	public static Item bootsChain = (new ItemArmor(49, EnumArmorMaterial.CHAIN, 1, 3)).setIconCoord(1, 3).setItemName("bootsChain");
	public static Item helmetSteel = (new ItemArmor(50, EnumArmorMaterial.IRON, 2, 0)).setIconCoord(2, 0).setItemName("helmetIron");
	public static Item plateSteel = (new ItemArmor(51, EnumArmorMaterial.IRON, 2, 1)).setIconCoord(2, 1).setItemName("chestplateIron");
	public static Item legsSteel = (new ItemArmor(52, EnumArmorMaterial.IRON, 2, 2)).setIconCoord(2, 2).setItemName("leggingsIron");
	public static Item bootsSteel = (new ItemArmor(53, EnumArmorMaterial.IRON, 2, 3)).setIconCoord(2, 3).setItemName("bootsIron");
	public static Item helmetDiamond = (new ItemArmor(54, EnumArmorMaterial.DIAMOND, 3, 0)).setIconCoord(3, 0).setItemName("helmetDiamond");
	public static Item plateDiamond = (new ItemArmor(55, EnumArmorMaterial.DIAMOND, 3, 1)).setIconCoord(3, 1).setItemName("chestplateDiamond");
	public static Item legsDiamond = (new ItemArmor(56, EnumArmorMaterial.DIAMOND, 3, 2)).setIconCoord(3, 2).setItemName("leggingsDiamond");
	public static Item bootsDiamond = (new ItemArmor(57, EnumArmorMaterial.DIAMOND, 3, 3)).setIconCoord(3, 3).setItemName("bootsDiamond");
	public static Item helmetGold = (new ItemArmor(58, EnumArmorMaterial.GOLD, 4, 0)).setIconCoord(4, 0).setItemName("helmetGold");
	public static Item plateGold = (new ItemArmor(59, EnumArmorMaterial.GOLD, 4, 1)).setIconCoord(4, 1).setItemName("chestplateGold");
	public static Item legsGold = (new ItemArmor(60, EnumArmorMaterial.GOLD, 4, 2)).setIconCoord(4, 2).setItemName("leggingsGold");
	public static Item bootsGold = (new ItemArmor(61, EnumArmorMaterial.GOLD, 4, 3)).setIconCoord(4, 3).setItemName("bootsGold");
	public static Item flint = (new SpoutItem(62)).setIconCoord(6, 0).setItemName("flint").setCreativeTab(CreativeTabs.tabMaterials); // Spout changed to SpoutItemBlock
	public static Item porkRaw = (new ItemFood(63, 3, 0.3F, true)).setIconCoord(7, 5).setItemName("porkchopRaw");
	public static Item porkCooked = (new ItemFood(64, 8, 0.8F, true)).setIconCoord(8, 5).setItemName("porkchopCooked");
	public static Item painting = (new ItemHangingEntity(65, EntityPainting.class)).setIconCoord(10, 1).setItemName("painting");
	public static Item appleGold = (new ItemAppleGold(66, 4, 1.2F, false)).setAlwaysEdible().setPotionEffect(Potion.regeneration.id, 5, 0, 1.0F).setIconCoord(11, 0).setItemName("appleGold");
	public static Item sign = (new ItemSign(67)).setIconCoord(10, 2).setItemName("sign");
	public static Item doorWood = (new ItemDoor(68, Material.wood)).setIconCoord(11, 2).setItemName("doorWood");
	public static Item bucketEmpty = (new ItemBucket(69, 0)).setIconCoord(10, 4).setItemName("bucket").setMaxStackSize(16);
	public static Item bucketWater = (new ItemBucket(70, Block.waterMoving.blockID)).setIconCoord(11, 4).setItemName("bucketWater").setContainerItem(bucketEmpty);
	public static Item bucketLava = (new ItemBucket(71, Block.lavaMoving.blockID)).setIconCoord(12, 4).setItemName("bucketLava").setContainerItem(bucketEmpty);
	public static Item minecartEmpty = (new ItemMinecart(72, 0)).setIconCoord(7, 8).setItemName("minecart");
	public static Item saddle = (new ItemSaddle(73)).setIconCoord(8, 6).setItemName("saddle");
	public static Item doorSteel = (new ItemDoor(74, Material.iron)).setIconCoord(12, 2).setItemName("doorIron");
	public static Item redstone = (new ItemRedstone(75)).setIconCoord(8, 3).setItemName("redstone").setPotionEffect(PotionHelper.redstoneEffect);
	public static Item snowball = (new ItemSnowball(76)).setIconCoord(14, 0).setItemName("snowball");
	public static Item boat = (new ItemBoat(77)).setIconCoord(8, 8).setItemName("boat");
	public static Item leather = (new Item(78)).setIconCoord(7, 6).setItemName("leather").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item bucketMilk = (new ItemBucketMilk(79)).setIconCoord(13, 4).setItemName("milk").setContainerItem(bucketEmpty);
	public static Item brick = (new Item(80)).setIconCoord(6, 1).setItemName("brick").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item clay = (new Item(81)).setIconCoord(9, 3).setItemName("clay").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item reed = (new ItemReed(82, Block.reed)).setIconCoord(11, 1).setItemName("reeds").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item paper = (new Item(83)).setIconCoord(10, 3).setItemName("paper").setCreativeTab(CreativeTabs.tabMisc);
	public static Item book = (new Item(84)).setIconCoord(11, 3).setItemName("book").setCreativeTab(CreativeTabs.tabMisc);
	public static Item slimeBall = (new Item(85)).setIconCoord(14, 1).setItemName("slimeball").setCreativeTab(CreativeTabs.tabMisc);
	public static Item minecartCrate = (new ItemMinecart(86, 1)).setIconCoord(7, 9).setItemName("minecartChest");
	public static Item minecartPowered = (new ItemMinecart(87, 2)).setIconCoord(7, 10).setItemName("minecartFurnace");
	public static Item egg = (new ItemEgg(88)).setIconCoord(12, 0).setItemName("egg");
	public static Item compass = (new Item(89)).setIconCoord(6, 3).setItemName("compass").setCreativeTab(CreativeTabs.tabTools);
	public static Item fishingRod = (new ItemFishingRod(90)).setIconCoord(5, 4).setItemName("fishingRod");
	public static Item pocketSundial = (new Item(91)).setIconCoord(6, 4).setItemName("clock").setCreativeTab(CreativeTabs.tabTools);
	public static Item lightStoneDust = (new Item(92)).setIconCoord(9, 4).setItemName("yellowDust").setPotionEffect(PotionHelper.glowstoneEffect).setCreativeTab(CreativeTabs.tabMaterials);
	public static Item fishRaw = (new ItemFood(93, 2, 0.3F, false)).setIconCoord(9, 5).setItemName("fishRaw");
	public static Item fishCooked = (new ItemFood(94, 5, 0.6F, false)).setIconCoord(10, 5).setItemName("fishCooked");
	public static Item dyePowder = (new ItemDye(95)).setIconCoord(14, 4).setItemName("dyePowder");
	public static Item bone = (new Item(96)).setIconCoord(12, 1).setItemName("bone").setFull3D().setCreativeTab(CreativeTabs.tabMisc);
	public static Item sugar = (new Item(97)).setIconCoord(13, 0).setItemName("sugar").setPotionEffect(PotionHelper.sugarEffect).setCreativeTab(CreativeTabs.tabMaterials);
	public static Item cake = (new ItemReed(98, Block.cake)).setMaxStackSize(1).setIconCoord(13, 1).setItemName("cake").setCreativeTab(CreativeTabs.tabFood);
	public static Item bed = (new ItemBed(99)).setMaxStackSize(1).setIconCoord(13, 2).setItemName("bed");
	public static Item redstoneRepeater = (new ItemReed(100, Block.redstoneRepeaterIdle)).setIconCoord(6, 5).setItemName("diode").setCreativeTab(CreativeTabs.tabRedstone);
	public static Item cookie = (new ItemFood(101, 2, 0.1F, false)).setIconCoord(12, 5).setItemName("cookie");
	public static ItemMap map = (ItemMap)(new ItemMap(102)).setIconCoord(12, 3).setItemName("map");

	/**
	 * Item introduced on 1.7 version, is a shear to cut leaves (you can keep the block) or get wool from sheeps.
	 */
	public static ItemShears shears = (ItemShears)(new ItemShears(103)).setIconCoord(13, 5).setItemName("shears");
	public static Item melon = (new ItemFood(104, 2, 0.3F, false)).setIconCoord(13, 6).setItemName("melon");
	public static Item pumpkinSeeds = (new ItemSeeds(105, Block.pumpkinStem.blockID, Block.tilledField.blockID)).setIconCoord(13, 3).setItemName("seeds_pumpkin");
	public static Item melonSeeds = (new ItemSeeds(106, Block.melonStem.blockID, Block.tilledField.blockID)).setIconCoord(14, 3).setItemName("seeds_melon");
	public static Item beefRaw = (new ItemFood(107, 3, 0.3F, true)).setIconCoord(9, 6).setItemName("beefRaw");
	public static Item beefCooked = (new ItemFood(108, 8, 0.8F, true)).setIconCoord(10, 6).setItemName("beefCooked");
	public static Item chickenRaw = (new ItemFood(109, 2, 0.3F, true)).setPotionEffect(Potion.hunger.id, 30, 0, 0.3F).setIconCoord(9, 7).setItemName("chickenRaw");
	public static Item chickenCooked = (new ItemFood(110, 6, 0.6F, true)).setIconCoord(10, 7).setItemName("chickenCooked");
	public static Item rottenFlesh = (new ItemFood(111, 4, 0.1F, true)).setPotionEffect(Potion.hunger.id, 30, 0, 0.8F).setIconCoord(11, 5).setItemName("rottenFlesh");
	public static Item enderPearl = (new ItemEnderPearl(112)).setIconCoord(11, 6).setItemName("enderPearl");
	public static Item blazeRod = (new Item(113)).setIconCoord(12, 6).setItemName("blazeRod").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item ghastTear = (new Item(114)).setIconCoord(11, 7).setItemName("ghastTear").setPotionEffect(PotionHelper.ghastTearEffect).setCreativeTab(CreativeTabs.tabBrewing);
	public static Item goldNugget = (new Item(115)).setIconCoord(12, 7).setItemName("goldNugget").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item netherStalkSeeds = (new ItemSeeds(116, Block.netherStalk.blockID, Block.slowSand.blockID)).setIconCoord(13, 7).setItemName("netherStalkSeeds").setPotionEffect("+4");
	public static ItemPotion potion = (ItemPotion)(new ItemPotion(117)).setIconCoord(13, 8).setItemName("potion");
	public static Item glassBottle = (new ItemGlassBottle(118)).setIconCoord(12, 8).setItemName("glassBottle");
	public static Item spiderEye = (new ItemFood(119, 2, 0.8F, false)).setPotionEffect(Potion.poison.id, 5, 0, 1.0F).setIconCoord(11, 8).setItemName("spiderEye").setPotionEffect(PotionHelper.spiderEyeEffect);
	public static Item fermentedSpiderEye = (new Item(120)).setIconCoord(10, 8).setItemName("fermentedSpiderEye").setPotionEffect(PotionHelper.fermentedSpiderEyeEffect).setCreativeTab(CreativeTabs.tabBrewing);
	public static Item blazePowder = (new Item(121)).setIconCoord(13, 9).setItemName("blazePowder").setPotionEffect(PotionHelper.blazePowderEffect).setCreativeTab(CreativeTabs.tabBrewing);
	public static Item magmaCream = (new Item(122)).setIconCoord(13, 10).setItemName("magmaCream").setPotionEffect(PotionHelper.magmaCreamEffect).setCreativeTab(CreativeTabs.tabBrewing);
	public static Item brewingStand = (new ItemReed(123, Block.brewingStand)).setIconCoord(12, 10).setItemName("brewingStand").setCreativeTab(CreativeTabs.tabBrewing);
	public static Item cauldron = (new ItemReed(124, Block.cauldron)).setIconCoord(12, 9).setItemName("cauldron").setCreativeTab(CreativeTabs.tabBrewing);
	public static Item eyeOfEnder = (new ItemEnderEye(125)).setIconCoord(11, 9).setItemName("eyeOfEnder");
	public static Item speckledMelon = (new Item(126)).setIconCoord(9, 8).setItemName("speckledMelon").setPotionEffect(PotionHelper.speckledMelonEffect).setCreativeTab(CreativeTabs.tabBrewing);
	public static Item monsterPlacer = (new ItemMonsterPlacer(127)).setIconCoord(9, 9).setItemName("monsterPlacer");

	/**
	 * Bottle o' Enchanting. Drops between 1 and 3 experience orbs when thrown.
	 */
	public static Item expBottle = (new ItemExpBottle(128)).setIconCoord(11, 10).setItemName("expBottle");

	/**
	 * Fire Charge. When used in a dispenser it fires a fireball similiar to a Ghast's.
	 */
	public static Item fireballCharge = (new ItemFireball(129)).setIconCoord(14, 2).setItemName("fireball");
	public static Item writableBook = (new ItemWritableBook(130)).setIconCoord(11, 11).setItemName("writingBook").setCreativeTab(CreativeTabs.tabMisc);
	public static Item writtenBook = (new ItemEditableBook(131)).setIconCoord(12, 11).setItemName("writtenBook");
	public static Item emerald = (new Item(132)).setIconCoord(10, 11).setItemName("emerald").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item itemFrame = (new ItemHangingEntity(133, EntityItemFrame.class)).setIconCoord(14, 12).setItemName("frame");
	public static Item flowerPot = (new ItemReed(134, Block.flowerPot)).setIconCoord(13, 11).setItemName("flowerPot").setCreativeTab(CreativeTabs.tabDecorations);
	public static Item carrot = (new ItemSeedFood(135, 4, 0.6F, Block.carrot.blockID, Block.tilledField.blockID)).setIconCoord(8, 7).setItemName("carrots");
	public static Item potato = (new ItemSeedFood(136, 1, 0.3F, Block.potato.blockID, Block.tilledField.blockID)).setIconCoord(7, 7).setItemName("potato");
	public static Item bakedPotato = (new ItemFood(137, 6, 0.6F, false)).setIconCoord(6, 7).setItemName("potatoBaked");
	public static Item poisonousPotato = (new ItemFood(138, 2, 0.3F, false)).setPotionEffect(Potion.poison.id, 5, 0, 0.6F).setIconCoord(6, 8).setItemName("potatoPoisonous");
	public static ItemEmptyMap emptyMap = (ItemEmptyMap)(new ItemEmptyMap(139)).setIconCoord(13, 12).setItemName("emptyMap");
	public static Item goldenCarrot = (new ItemFood(140, 6, 1.2F, false)).setIconCoord(6, 9).setItemName("carrotGolden").setPotionEffect(PotionHelper.field_82818_l);
	public static Item skull = (new ItemSkull(141)).setItemName("skull");
	public static Item carrotOnAStick = (new ItemCarrotOnAStick(142)).setIconCoord(6, 6).setItemName("carrotOnAStick");
	public static Item netherStar = (new ItemSimpleFoiled(143)).setIconCoord(9, 11).setItemName("netherStar").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item pumpkinPie = (new ItemFood(144, 8, 0.3F, false)).setIconCoord(8, 9).setItemName("pumpkinPie").setCreativeTab(CreativeTabs.tabFood);
	public static Item record13 = (new ItemRecord(2000, "13")).setIconCoord(0, 15).setItemName("record");
	public static Item recordCat = (new ItemRecord(2001, "cat")).setIconCoord(1, 15).setItemName("record");
	public static Item recordBlocks = (new ItemRecord(2002, "blocks")).setIconCoord(2, 15).setItemName("record");
	public static Item recordChirp = (new ItemRecord(2003, "chirp")).setIconCoord(3, 15).setItemName("record");
	public static Item recordFar = (new ItemRecord(2004, "far")).setIconCoord(4, 15).setItemName("record");
	public static Item recordMall = (new ItemRecord(2005, "mall")).setIconCoord(5, 15).setItemName("record");
	public static Item recordMellohi = (new ItemRecord(2006, "mellohi")).setIconCoord(6, 15).setItemName("record");
	public static Item recordStal = (new ItemRecord(2007, "stal")).setIconCoord(7, 15).setItemName("record");
	public static Item recordStrad = (new ItemRecord(2008, "strad")).setIconCoord(8, 15).setItemName("record");
	public static Item recordWard = (new ItemRecord(2009, "ward")).setIconCoord(9, 15).setItemName("record");
	public static Item record11 = (new ItemRecord(2010, "11")).setIconCoord(10, 15).setItemName("record");
	public static Item field_85180_cf = (new ItemRecord(2011, "wait")).setIconCoord(11, 15).setItemName("record");

	/** Item index + 256 */
	public final int shiftedIndex;

	/** Maximum size of the stack. */
	protected int maxStackSize = 64;

	/** Maximum damage an item can handle. */
	private int maxDamage = 0;

	/** Icon index in the icons table. */
	protected int iconIndex;

	/** If true, render the object in full 3D, like weapons and tools. */
	protected boolean bFull3D = false;

	/**
	 * Some items (like dyes) have multiple subtypes on same item, this is field define this behavior
	 */
	protected boolean hasSubtypes = false;
	private Item containerItem = null;
	private String potionEffect = null;

	/** full name of item from language file */
	private String itemName;

	protected Item(int par1) {
		this.shiftedIndex = 256 + par1;

		if (itemsList[256 + par1] != null) {
			System.out.println("CONFLICT @ " + par1);
		}

		itemsList[256 + par1] = this;
	}

	/**
	 * Sets the icon index for this item. Returns the item.
	 */
	public Item setIconIndex(int par1) {
		this.iconIndex = par1;
		return this;
	}

	public Item setMaxStackSize(int par1) {
		this.maxStackSize = par1;
		return this;
	}

	public Item setIconCoord(int par1, int par2) {
		this.iconIndex = par1 + par2 * 16;
		return this;
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	public int getIconFromDamage(int par1) {
		return this.iconIndex;
	}

	/**
	 * Returns the icon index of the stack given as argument.
	 */
	public final int getIconIndex(ItemStack par1ItemStack) {
		return this.getIconFromDamage(par1ItemStack.getItemDamage());
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		return false;
	}

	/**
	 * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
	 * sword
	 */
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
		return 1.0F;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	/**
	 * Returns the maximum size of the stack for a specific item. *Isn't this more a Set than a Get?*
	 */
	public int getItemStackLimit() {
		return this.maxStackSize;
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	public int getMetadata(int par1) {
		return 0;
	}

	public boolean getHasSubtypes() {
		return this.hasSubtypes;
	}

	protected Item setHasSubtypes(boolean par1) {
		this.hasSubtypes = par1;
		return this;
	}

	/**
	 * Returns the maximum damage an item can take.
	 */
	public int getMaxDamage() {
		return this.maxDamage;
	}

	/**
	 * set max damage of an Item
	 */
	protected Item setMaxDamage(int par1) {
		this.maxDamage = par1;
		return this;
	}

	public boolean isDamageable() {
		return this.maxDamage > 0 && !this.hasSubtypes;
	}

	/**
	 * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise the
	 * damage on the stack.
	 */
	public boolean hitEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving) {
		return false;
	}

	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving) {
		return false;
	}

	/**
	 * Returns the damage against a given entity.
	 */
	public int getDamageVsEntity(Entity par1Entity) {
		return 1;
	}

	/**
	 * Returns if the item (tool) can harvest results from the block type.
	 */
	public boolean canHarvestBlock(Block par1Block) {
		return false;
	}

	/**
	 * dye sheep, place saddles, etc ...
	 */
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving) {
		return false;
	}

	/**
	 * Sets bFull3D to True and return the object.
	 */
	public Item setFull3D() {
		this.bFull3D = true;
		return this;
	}

	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	public boolean isFull3D() {
		return this.bFull3D;
	}

	/**
	 * Returns true if this item should be rotated by 180 degrees around the Y axis when being held in an entities hands.
	 */
	public boolean shouldRotateAroundWhenRendering() {
		return false;
	}

	/**
	 * set name of item from language file
	 */
	public Item setItemName(String par1Str) {
		this.itemName = "item." + par1Str;
		return this;
	}

	public String getLocalItemName(ItemStack par1ItemStack) {
		String var2 = this.getItemNameIS(par1ItemStack);
		return var2 == null ? "" : StatCollector.translateToLocal(var2);
	}

	public String getItemName() {
		return this.itemName;
	}

	public String getItemNameIS(ItemStack par1ItemStack) {
		return this.itemName;
	}

	public Item setContainerItem(Item par1Item) {
		this.containerItem = par1Item;
		return this;
	}

	/**
	 * If this returns true, after a recipe involving this item is crafted the container item will be added to the player's
	 * inventory instead of remaining in the crafting grid.
	 */
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack par1ItemStack) {
		return true;
	}

	/**
	 * If this function returns true (or the item is damageable), the ItemStack's NBT tag will be sent to the client.
	 */
	public boolean getShareTag() {
		return true;
	}

	public Item getContainerItem() {
		return this.containerItem;
	}

	/**
	 * True if this Item has a container item (a.k.a. crafting result)
	 */
	public boolean hasContainerItem() {
		return this.containerItem != null;
	}

	public String getStatName() {
		return StatCollector.translateToLocal(this.getItemName() + ".name");
	}

	public String func_77653_i(ItemStack par1ItemStack) {
		return StatCollector.translateToLocal(this.getItemNameIS(par1ItemStack) + ".name");
	}

	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		return 16777215;
	}

	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and update
	 * it's contents.
	 */
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {}

	/**
	 * Called when item is crafted/smelted. Used only by maps so far.
	 */
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {}

	/**
	 * false for all Items except sub-classes of ItemMapBase
	 */
	public boolean isMap() {
		return false;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 0;
	}

	/**
	 * called when the player releases the use item button. Args: itemstack, world, entityplayer, itemInUseCount
	 */
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {}

	/**
	 * Sets the string representing this item's effect on a potion when used as an ingredient.
	 */
	protected Item setPotionEffect(String par1Str) {
		this.potionEffect = par1Str;
		return this;
	}

	/**
	 * Returns a string representing what this item does to a potion.
	 */
	public String getPotionEffect() {
		return this.potionEffect;
	}

	/**
	 * Returns true if this item serves as a potion ingredient (its ingredient information is not null).
	 */
	public boolean isPotionIngredient() {
		return this.potionEffect != null;
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {}

	public String getItemDisplayName(ItemStack par1ItemStack) {
		return ("" + StringTranslate.getInstance().translateNamedKey(this.getLocalItemName(par1ItemStack))).trim();
	}

	public boolean hasEffect(ItemStack par1ItemStack) {
		return par1ItemStack.isItemEnchanted();
	}

	/**
	 * Return an item rarity from EnumRarity
	 */
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return par1ItemStack.isItemEnchanted() ? EnumRarity.rare : EnumRarity.common;
	}

	/**
	 * Checks isDamagable and if it cannot be stacked
	 */
	public boolean isItemTool(ItemStack par1ItemStack) {
		return this.getItemStackLimit() == 1 && this.isDamageable();
	}

	protected MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer, boolean par3) {
		float var4 = 1.0F;
		float var5 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * var4;
		float var6 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * var4;
		double var7 = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * (double)var4;
		double var9 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * (double)var4 + 1.62D - (double)par2EntityPlayer.yOffset;
		double var11 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * (double)var4;
		Vec3 var13 = par1World.getWorldVec3Pool().getVecFromPool(var7, var9, var11);
		float var14 = MathHelper.cos(-var6 * 0.017453292F - (float)Math.PI);
		float var15 = MathHelper.sin(-var6 * 0.017453292F - (float)Math.PI);
		float var16 = -MathHelper.cos(-var5 * 0.017453292F);
		float var17 = MathHelper.sin(-var5 * 0.017453292F);
		float var18 = var15 * var16;
		float var20 = var14 * var16;
		double var21 = 5.0D;
		Vec3 var23 = var13.addVector((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
		return par1World.rayTraceBlocks_do_do(var13, var23, par3, !par3);
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based on material.
	 */
	public int getItemEnchantability() {
		return 0;
	}

	public boolean requiresMultipleRenderPasses() {
		return false;
	}

	/**
	 * Gets an icon index based on an item's damage value and the given render pass
	 */
	public int getIconFromDamageForRenderPass(int par1, int par2) {
		return this.getIconFromDamage(par1);
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
	}

	/**
	 * gets the CreativeTab this item is displayed on
	 */
	public CreativeTabs getCreativeTab() {
		return this.tabToDisplayOn;
	}

	/**
	 * returns this;
	 */
	public Item setCreativeTab(CreativeTabs par1CreativeTabs) {
		this.tabToDisplayOn = par1CreativeTabs;
		return this;
	}

	public boolean func_82788_x() {
		return true;
	}

	/**
	 * Return whether this item is repairable in an anvil.
	 */
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return false;
	}

	static {
		StatList.initStats();
	}
}
