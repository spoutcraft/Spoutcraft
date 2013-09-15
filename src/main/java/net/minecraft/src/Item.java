package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import org.spoutcraft.client.item.SpoutItem;

public class Item {
	protected static final UUID field_111210_e = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
	private CreativeTabs tabToDisplayOn;	

	/** The RNG used by the Item subclasses. */
	protected static Random itemRand = new Random();

	/** A 32000 elements Item array. */
	public static Item[] itemsList = new Item[32000];
	public static Item shovelIron = (new ItemSpade(0, EnumToolMaterial.IRON)).setUnlocalizedName("shovelIron").setTextureName("iron_shovel");
	public static Item pickaxeIron = (new ItemPickaxe(1, EnumToolMaterial.IRON)).setUnlocalizedName("pickaxeIron").setTextureName("iron_pickaxe");
	public static Item axeIron = (new ItemAxe(2, EnumToolMaterial.IRON)).setUnlocalizedName("hatchetIron").setTextureName("iron_axe");
	public static Item flintAndSteel = (new ItemFlintAndSteel(3)).setUnlocalizedName("flintAndSteel").setTextureName("flint_and_steel");
	public static Item appleRed = (new ItemFood(4, 4, 0.3F, false)).setUnlocalizedName("apple").setTextureName("apple");
	public static ItemBow bow = (ItemBow)(new ItemBow(5)).setUnlocalizedName("bow").setTextureName("bow");
	public static Item arrow = (new Item(6)).setUnlocalizedName("arrow").setCreativeTab(CreativeTabs.tabCombat).setTextureName("arrow");
	public static Item coal = (new ItemCoal(7)).setUnlocalizedName("coal").setTextureName("coal");
	public static Item diamond = (new Item(8)).setUnlocalizedName("diamond").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("diamond");
	public static Item ingotIron = (new Item(9)).setUnlocalizedName("ingotIron").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("iron_ingot");
	public static Item ingotGold = (new Item(10)).setUnlocalizedName("ingotGold").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("gold_ingot");
	public static Item swordIron = (new ItemSword(11, EnumToolMaterial.IRON)).setUnlocalizedName("swordIron").setTextureName("iron_sword");
	public static Item swordWood = (new ItemSword(12, EnumToolMaterial.WOOD)).setUnlocalizedName("swordWood").setTextureName("wood_sword");
	public static Item shovelWood = (new ItemSpade(13, EnumToolMaterial.WOOD)).setUnlocalizedName("shovelWood").setTextureName("wood_shovel");
	public static Item pickaxeWood = (new ItemPickaxe(14, EnumToolMaterial.WOOD)).setUnlocalizedName("pickaxeWood").setTextureName("wood_pickaxe");
	public static Item axeWood = (new ItemAxe(15, EnumToolMaterial.WOOD)).setUnlocalizedName("hatchetWood").setTextureName("wood_axe");
	public static Item swordStone = (new ItemSword(16, EnumToolMaterial.STONE)).setUnlocalizedName("swordStone").setTextureName("stone_sword");
	public static Item shovelStone = (new ItemSpade(17, EnumToolMaterial.STONE)).setUnlocalizedName("shovelStone").setTextureName("stone_shovel");
	public static Item pickaxeStone = (new ItemPickaxe(18, EnumToolMaterial.STONE)).setUnlocalizedName("pickaxeStone").setTextureName("stone_pickaxe");
	public static Item axeStone = (new ItemAxe(19, EnumToolMaterial.STONE)).setUnlocalizedName("hatchetStone").setTextureName("stone_axe");
	public static Item swordDiamond = (new ItemSword(20, EnumToolMaterial.EMERALD)).setUnlocalizedName("swordDiamond").setTextureName("diamond_sword");
	public static Item shovelDiamond = (new ItemSpade(21, EnumToolMaterial.EMERALD)).setUnlocalizedName("shovelDiamond").setTextureName("diamond_shovel");
	public static Item pickaxeDiamond = (new ItemPickaxe(22, EnumToolMaterial.EMERALD)).setUnlocalizedName("pickaxeDiamond").setTextureName("diamond_pickaxe");
	public static Item axeDiamond = (new ItemAxe(23, EnumToolMaterial.EMERALD)).setUnlocalizedName("hatchetDiamond").setTextureName("diamond_axe");
	public static Item stick = (new Item(24)).setFull3D().setUnlocalizedName("stick").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("stick");
	public static Item bowlEmpty = (new Item(25)).setUnlocalizedName("bowl").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("bowl");
	public static Item bowlSoup = (new ItemSoup(26, 6)).setUnlocalizedName("mushroomStew").setTextureName("mushroom_stew");
	public static Item swordGold = (new ItemSword(27, EnumToolMaterial.GOLD)).setUnlocalizedName("swordGold").setTextureName("gold_sword");
	public static Item shovelGold = (new ItemSpade(28, EnumToolMaterial.GOLD)).setUnlocalizedName("shovelGold").setTextureName("gold_shovel");
	public static Item pickaxeGold = (new ItemPickaxe(29, EnumToolMaterial.GOLD)).setUnlocalizedName("pickaxeGold").setTextureName("gold_pickaxe");
	public static Item axeGold = (new ItemAxe(30, EnumToolMaterial.GOLD)).setUnlocalizedName("hatchetGold").setTextureName("gold_axe");
	public static Item silk = (new ItemReed(31, Block.tripWire)).setUnlocalizedName("string").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("string");
	public static Item feather = (new Item(32)).setUnlocalizedName("feather").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("feather");
	public static Item gunpowder = (new Item(33)).setUnlocalizedName("sulphur").setPotionEffect(PotionHelper.gunpowderEffect).setCreativeTab(CreativeTabs.tabMaterials).setTextureName("gunpowder");
	public static Item hoeWood = (new ItemHoe(34, EnumToolMaterial.WOOD)).setUnlocalizedName("hoeWood").setTextureName("wood_hoe");
	public static Item hoeStone = (new ItemHoe(35, EnumToolMaterial.STONE)).setUnlocalizedName("hoeStone").setTextureName("stone_hoe");
	public static Item hoeIron = (new ItemHoe(36, EnumToolMaterial.IRON)).setUnlocalizedName("hoeIron").setTextureName("iron_hoe");
	public static Item hoeDiamond = (new ItemHoe(37, EnumToolMaterial.EMERALD)).setUnlocalizedName("hoeDiamond").setTextureName("diamond_hoe");
	public static Item hoeGold = (new ItemHoe(38, EnumToolMaterial.GOLD)).setUnlocalizedName("hoeGold").setTextureName("gold_hoe");
	public static Item seeds = (new ItemSeeds(39, Block.crops.blockID, Block.tilledField.blockID)).setUnlocalizedName("seeds").setTextureName("seeds_wheat");
	public static Item wheat = (new Item(40)).setUnlocalizedName("wheat").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("wheat");
	public static Item bread = (new ItemFood(41, 5, 0.6F, false)).setUnlocalizedName("bread").setTextureName("bread");
	public static ItemArmor helmetLeather = (ItemArmor)(new ItemArmor(42, EnumArmorMaterial.CLOTH, 0, 0)).setUnlocalizedName("helmetCloth").setTextureName("leather_helmet");
	public static ItemArmor plateLeather = (ItemArmor)(new ItemArmor(43, EnumArmorMaterial.CLOTH, 0, 1)).setUnlocalizedName("chestplateCloth").setTextureName("leather_chestplate");
	public static ItemArmor legsLeather = (ItemArmor)(new ItemArmor(44, EnumArmorMaterial.CLOTH, 0, 2)).setUnlocalizedName("leggingsCloth").setTextureName("leather_leggings");
	public static ItemArmor bootsLeather = (ItemArmor)(new ItemArmor(45, EnumArmorMaterial.CLOTH, 0, 3)).setUnlocalizedName("bootsCloth").setTextureName("leather_boots");
	public static ItemArmor helmetChain = (ItemArmor)(new ItemArmor(46, EnumArmorMaterial.CHAIN, 1, 0)).setUnlocalizedName("helmetChain").setTextureName("chainmail_helmet");
	public static ItemArmor plateChain = (ItemArmor)(new ItemArmor(47, EnumArmorMaterial.CHAIN, 1, 1)).setUnlocalizedName("chestplateChain").setTextureName("chainmail_chestplate");
	public static ItemArmor legsChain = (ItemArmor)(new ItemArmor(48, EnumArmorMaterial.CHAIN, 1, 2)).setUnlocalizedName("leggingsChain").setTextureName("chainmail_leggings");
	public static ItemArmor bootsChain = (ItemArmor)(new ItemArmor(49, EnumArmorMaterial.CHAIN, 1, 3)).setUnlocalizedName("bootsChain").setTextureName("chainmail_boots");
	public static ItemArmor helmetIron = (ItemArmor)(new ItemArmor(50, EnumArmorMaterial.IRON, 2, 0)).setUnlocalizedName("helmetIron").setTextureName("iron_helmet");
	public static ItemArmor plateIron = (ItemArmor)(new ItemArmor(51, EnumArmorMaterial.IRON, 2, 1)).setUnlocalizedName("chestplateIron").setTextureName("iron_chestplate");
	public static ItemArmor legsIron = (ItemArmor)(new ItemArmor(52, EnumArmorMaterial.IRON, 2, 2)).setUnlocalizedName("leggingsIron").setTextureName("iron_leggings");
	public static ItemArmor bootsIron = (ItemArmor)(new ItemArmor(53, EnumArmorMaterial.IRON, 2, 3)).setUnlocalizedName("bootsIron").setTextureName("iron_boots");
	public static ItemArmor helmetDiamond = (ItemArmor)(new ItemArmor(54, EnumArmorMaterial.DIAMOND, 3, 0)).setUnlocalizedName("helmetDiamond").setTextureName("diamond_helmet");
	public static ItemArmor plateDiamond = (ItemArmor)(new ItemArmor(55, EnumArmorMaterial.DIAMOND, 3, 1)).setUnlocalizedName("chestplateDiamond").setTextureName("diamond_chestplate");
	public static ItemArmor legsDiamond = (ItemArmor)(new ItemArmor(56, EnumArmorMaterial.DIAMOND, 3, 2)).setUnlocalizedName("leggingsDiamond").setTextureName("diamond_leggings");
	public static ItemArmor bootsDiamond = (ItemArmor)(new ItemArmor(57, EnumArmorMaterial.DIAMOND, 3, 3)).setUnlocalizedName("bootsDiamond").setTextureName("diamond_boots");
	public static ItemArmor helmetGold = (ItemArmor)(new ItemArmor(58, EnumArmorMaterial.GOLD, 4, 0)).setUnlocalizedName("helmetGold").setTextureName("gold_helmet");
	public static ItemArmor plateGold = (ItemArmor)(new ItemArmor(59, EnumArmorMaterial.GOLD, 4, 1)).setUnlocalizedName("chestplateGold").setTextureName("gold_chestplate");
	public static ItemArmor legsGold = (ItemArmor)(new ItemArmor(60, EnumArmorMaterial.GOLD, 4, 2)).setUnlocalizedName("leggingsGold").setTextureName("gold_leggings");
	public static ItemArmor bootsGold = (ItemArmor)(new ItemArmor(61, EnumArmorMaterial.GOLD, 4, 3)).setUnlocalizedName("bootsGold").setTextureName("gold_boots");
		
	// Spout Start - Changed to SpoutItemBlock
	public static Item flint = (new SpoutItem(62)).setUnlocalizedName("flint").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("flint");
	// Spout End
	
	public static Item porkRaw = (new ItemFood(63, 3, 0.3F, true)).setUnlocalizedName("porkchopRaw").setTextureName("porkchop_raw");
	public static Item porkCooked = (new ItemFood(64, 8, 0.8F, true)).setUnlocalizedName("porkchopCooked").setTextureName("porkchop_cooked");
	public static Item painting = (new ItemHangingEntity(65, EntityPainting.class)).setUnlocalizedName("painting").setTextureName("painting");
	public static Item appleGold = (new ItemAppleGold(66, 4, 1.2F, false)).setAlwaysEdible().setPotionEffect(Potion.regeneration.id, 5, 1, 1.0F).setUnlocalizedName("appleGold").setTextureName("apple_golden");
	public static Item sign = (new ItemSign(67)).setUnlocalizedName("sign").setTextureName("sign");
	public static Item doorWood = (new ItemDoor(68, Material.wood)).setUnlocalizedName("doorWood").setTextureName("door_wood");
	public static Item bucketEmpty = (new ItemBucket(69, 0)).setUnlocalizedName("bucket").setMaxStackSize(16).setTextureName("bucket_empty");
	public static Item bucketWater = (new ItemBucket(70, Block.waterMoving.blockID)).setUnlocalizedName("bucketWater").setContainerItem(bucketEmpty).setTextureName("bucket_water");
	public static Item bucketLava = (new ItemBucket(71, Block.lavaMoving.blockID)).setUnlocalizedName("bucketLava").setContainerItem(bucketEmpty).setTextureName("bucket_lava");
	public static Item minecartEmpty = (new ItemMinecart(72, 0)).setUnlocalizedName("minecart").setTextureName("minecart_normal");
	public static Item saddle = (new ItemSaddle(73)).setUnlocalizedName("saddle").setTextureName("saddle");
	public static Item doorIron = (new ItemDoor(74, Material.iron)).setUnlocalizedName("doorIron").setTextureName("door_iron");
	public static Item redstone = (new ItemRedstone(75)).setUnlocalizedName("redstone").setPotionEffect(PotionHelper.redstoneEffect).setTextureName("redstone_dust");
	public static Item snowball = (new ItemSnowball(76)).setUnlocalizedName("snowball").setTextureName("snowball");
	public static Item boat = (new ItemBoat(77)).setUnlocalizedName("boat").setTextureName("boat");
	public static Item leather = (new Item(78)).setUnlocalizedName("leather").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("leather");
	public static Item bucketMilk = (new ItemBucketMilk(79)).setUnlocalizedName("milk").setContainerItem(bucketEmpty).setTextureName("bucket_milk");
	public static Item brick = (new Item(80)).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("brick");
	public static Item clay = (new Item(81)).setUnlocalizedName("clay").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("clay_ball");
	public static Item reed = (new ItemReed(82, Block.reed)).setUnlocalizedName("reeds").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("reeds");
	public static Item paper = (new Item(83)).setUnlocalizedName("paper").setCreativeTab(CreativeTabs.tabMisc).setTextureName("paper");
	public static Item book = (new ItemBook(84)).setUnlocalizedName("book").setCreativeTab(CreativeTabs.tabMisc).setTextureName("book_normal");
	public static Item slimeBall = (new Item(85)).setUnlocalizedName("slimeball").setCreativeTab(CreativeTabs.tabMisc).setTextureName("slimeball");
	public static Item minecartCrate = (new ItemMinecart(86, 1)).setUnlocalizedName("minecartChest").setTextureName("minecart_chest");
	public static Item minecartPowered = (new ItemMinecart(87, 2)).setUnlocalizedName("minecartFurnace").setTextureName("minecart_furnace");
	public static Item egg = (new ItemEgg(88)).setUnlocalizedName("egg").setTextureName("egg");
	public static Item compass = (new Item(89)).setUnlocalizedName("compass").setCreativeTab(CreativeTabs.tabTools).setTextureName("compass");
	public static ItemFishingRod fishingRod = (ItemFishingRod)(new ItemFishingRod(90)).setUnlocalizedName("fishingRod").setTextureName("fishing_rod");
	public static Item pocketSundial = (new Item(91)).setUnlocalizedName("clock").setCreativeTab(CreativeTabs.tabTools).setTextureName("clock");
	public static Item glowstone = (new Item(92)).setUnlocalizedName("yellowDust").setPotionEffect(PotionHelper.glowstoneEffect).setCreativeTab(CreativeTabs.tabMaterials).setTextureName("glowstone_dust");
	public static Item fishRaw = (new ItemFood(93, 2, 0.3F, false)).setUnlocalizedName("fishRaw").setTextureName("fish_raw");
	public static Item fishCooked = (new ItemFood(94, 5, 0.6F, false)).setUnlocalizedName("fishCooked").setTextureName("fish_cooked");
	public static Item dyePowder = (new ItemDye(95)).setUnlocalizedName("dyePowder").setTextureName("dye_powder");
	public static Item bone = (new Item(96)).setUnlocalizedName("bone").setFull3D().setCreativeTab(CreativeTabs.tabMisc).setTextureName("bone");
	public static Item sugar = (new Item(97)).setUnlocalizedName("sugar").setPotionEffect(PotionHelper.sugarEffect).setCreativeTab(CreativeTabs.tabMaterials).setTextureName("sugar");
	public static Item cake = (new ItemReed(98, Block.cake)).setMaxStackSize(1).setUnlocalizedName("cake").setCreativeTab(CreativeTabs.tabFood).setTextureName("cake");
	public static Item bed = (new ItemBed(99)).setMaxStackSize(1).setUnlocalizedName("bed").setTextureName("bed");
	public static Item redstoneRepeater = (new ItemReed(100, Block.redstoneRepeaterIdle)).setUnlocalizedName("diode").setCreativeTab(CreativeTabs.tabRedstone).setTextureName("repeater");
	public static Item cookie = (new ItemFood(101, 2, 0.1F, false)).setUnlocalizedName("cookie").setTextureName("cookie");
	public static ItemMap map = (ItemMap)(new ItemMap(102)).setUnlocalizedName("map").setTextureName("map_filled");

	/**
	 * Item introduced on 1.7 version, is a shear to cut leaves (you can keep the block) or get wool from sheeps.
	 */
	public static ItemShears shears = (ItemShears)(new ItemShears(103)).setUnlocalizedName("shears").setTextureName("shears");
	public static Item melon = (new ItemFood(104, 2, 0.3F, false)).setUnlocalizedName("melon").setTextureName("melon");
	public static Item pumpkinSeeds = (new ItemSeeds(105, Block.pumpkinStem.blockID, Block.tilledField.blockID)).setUnlocalizedName("seeds_pumpkin").setTextureName("seeds_pumpkin");
	public static Item melonSeeds = (new ItemSeeds(106, Block.melonStem.blockID, Block.tilledField.blockID)).setUnlocalizedName("seeds_melon").setTextureName("seeds_melon");
	public static Item beefRaw = (new ItemFood(107, 3, 0.3F, true)).setUnlocalizedName("beefRaw").setTextureName("beef_raw");
	public static Item beefCooked = (new ItemFood(108, 8, 0.8F, true)).setUnlocalizedName("beefCooked").setTextureName("beef_cooked");
	public static Item chickenRaw = (new ItemFood(109, 2, 0.3F, true)).setPotionEffect(Potion.hunger.id, 30, 0, 0.3F).setUnlocalizedName("chickenRaw").setTextureName("chicken_raw");
	public static Item chickenCooked = (new ItemFood(110, 6, 0.6F, true)).setUnlocalizedName("chickenCooked").setTextureName("chicken_cooked");
	public static Item rottenFlesh = (new ItemFood(111, 4, 0.1F, true)).setPotionEffect(Potion.hunger.id, 30, 0, 0.8F).setUnlocalizedName("rottenFlesh").setTextureName("rotten_flesh");
	public static Item enderPearl = (new ItemEnderPearl(112)).setUnlocalizedName("enderPearl").setTextureName("ender_pearl");
	public static Item blazeRod = (new Item(113)).setUnlocalizedName("blazeRod").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("blaze_rod");
	public static Item ghastTear = (new Item(114)).setUnlocalizedName("ghastTear").setPotionEffect(PotionHelper.ghastTearEffect).setCreativeTab(CreativeTabs.tabBrewing).setTextureName("ghast_tear");
	public static Item goldNugget = (new Item(115)).setUnlocalizedName("goldNugget").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("gold_nugget");
	public static Item netherStalkSeeds = (new ItemSeeds(116, Block.netherStalk.blockID, Block.slowSand.blockID)).setUnlocalizedName("netherStalkSeeds").setPotionEffect("+4").setTextureName("nether_wart");
	public static ItemPotion potion = (ItemPotion)(new ItemPotion(117)).setUnlocalizedName("potion").setTextureName("potion");
	public static Item glassBottle = (new ItemGlassBottle(118)).setUnlocalizedName("glassBottle").setTextureName("potion_bottle_empty");
	public static Item spiderEye = (new ItemFood(119, 2, 0.8F, false)).setPotionEffect(Potion.poison.id, 5, 0, 1.0F).setUnlocalizedName("spiderEye").setPotionEffect(PotionHelper.spiderEyeEffect).setTextureName("spider_eye");
	public static Item fermentedSpiderEye = (new Item(120)).setUnlocalizedName("fermentedSpiderEye").setPotionEffect(PotionHelper.fermentedSpiderEyeEffect).setCreativeTab(CreativeTabs.tabBrewing).setTextureName("spider_eye_fermented");
	public static Item blazePowder = (new Item(121)).setUnlocalizedName("blazePowder").setPotionEffect(PotionHelper.blazePowderEffect).setCreativeTab(CreativeTabs.tabBrewing).setTextureName("blaze_powder");
	public static Item magmaCream = (new Item(122)).setUnlocalizedName("magmaCream").setPotionEffect(PotionHelper.magmaCreamEffect).setCreativeTab(CreativeTabs.tabBrewing).setTextureName("magma_cream");
	public static Item brewingStand = (new ItemReed(123, Block.brewingStand)).setUnlocalizedName("brewingStand").setCreativeTab(CreativeTabs.tabBrewing).setTextureName("brewing_stand");
	public static Item cauldron = (new ItemReed(124, Block.cauldron)).setUnlocalizedName("cauldron").setCreativeTab(CreativeTabs.tabBrewing).setTextureName("cauldron");
	public static Item eyeOfEnder = (new ItemEnderEye(125)).setUnlocalizedName("eyeOfEnder").setTextureName("ender_eye");
	public static Item speckledMelon = (new Item(126)).setUnlocalizedName("speckledMelon").setPotionEffect(PotionHelper.speckledMelonEffect).setCreativeTab(CreativeTabs.tabBrewing).setTextureName("melon_speckled");
	public static Item monsterPlacer = (new ItemMonsterPlacer(127)).setUnlocalizedName("monsterPlacer").setTextureName("spawn_egg");

	/**
	 * Bottle o' Enchanting. Drops between 1 and 3 experience orbs when thrown.
	 */
	public static Item expBottle = (new ItemExpBottle(128)).setUnlocalizedName("expBottle").setTextureName("experience_bottle");

	/**
	 * Fire Charge. When used in a dispenser it fires a fireball similiar to a Ghast's.
	 */
	public static Item fireballCharge = (new ItemFireball(129)).setUnlocalizedName("fireball").setTextureName("fireball");
	public static Item writableBook = (new ItemWritableBook(130)).setUnlocalizedName("writingBook").setCreativeTab(CreativeTabs.tabMisc).setTextureName("book_writable");
	public static Item writtenBook = (new ItemEditableBook(131)).setUnlocalizedName("writtenBook").setTextureName("book_written");
	public static Item emerald = (new Item(132)).setUnlocalizedName("emerald").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("emerald");
	public static Item itemFrame = (new ItemHangingEntity(133, EntityItemFrame.class)).setUnlocalizedName("frame").setTextureName("item_frame");
	public static Item flowerPot = (new ItemReed(134, Block.flowerPot)).setUnlocalizedName("flowerPot").setCreativeTab(CreativeTabs.tabDecorations).setTextureName("flower_pot");
	public static Item carrot = (new ItemSeedFood(135, 4, 0.6F, Block.carrot.blockID, Block.tilledField.blockID)).setUnlocalizedName("carrots").setTextureName("carrot");
	public static Item potato = (new ItemSeedFood(136, 1, 0.3F, Block.potato.blockID, Block.tilledField.blockID)).setUnlocalizedName("potato").setTextureName("potato");
	public static Item bakedPotato = (new ItemFood(137, 6, 0.6F, false)).setUnlocalizedName("potatoBaked").setTextureName("potato_baked");
	public static Item poisonousPotato = (new ItemFood(138, 2, 0.3F, false)).setPotionEffect(Potion.poison.id, 5, 0, 0.6F).setUnlocalizedName("potatoPoisonous").setTextureName("potato_poisonous");
	public static ItemEmptyMap emptyMap = (ItemEmptyMap)(new ItemEmptyMap(139)).setUnlocalizedName("emptyMap").setTextureName("map_empty");
	public static Item goldenCarrot = (new ItemFood(140, 6, 1.2F, false)).setUnlocalizedName("carrotGolden").setPotionEffect(PotionHelper.goldenCarrotEffect).setTextureName("carrot_golden");
	public static Item skull = (new ItemSkull(141)).setUnlocalizedName("skull").setTextureName("skull");
	public static Item carrotOnAStick = (new ItemCarrotOnAStick(142)).setUnlocalizedName("carrotOnAStick").setTextureName("carrot_on_a_stick");
	public static Item netherStar = (new ItemSimpleFoiled(143)).setUnlocalizedName("netherStar").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("nether_star");
	public static Item pumpkinPie = (new ItemFood(144, 8, 0.3F, false)).setUnlocalizedName("pumpkinPie").setCreativeTab(CreativeTabs.tabFood).setTextureName("pumpkin_pie");
	public static Item firework = (new ItemFirework(145)).setUnlocalizedName("fireworks").setTextureName("fireworks");
	public static Item fireworkCharge = (new ItemFireworkCharge(146)).setUnlocalizedName("fireworksCharge").setCreativeTab(CreativeTabs.tabMisc).setTextureName("fireworks_charge");
	public static ItemEnchantedBook enchantedBook = (ItemEnchantedBook)(new ItemEnchantedBook(147)).setMaxStackSize(1).setUnlocalizedName("enchantedBook").setTextureName("book_enchanted");
	public static Item comparator = (new ItemReed(148, Block.redstoneComparatorIdle)).setUnlocalizedName("comparator").setCreativeTab(CreativeTabs.tabRedstone).setTextureName("comparator");
	public static Item netherrackBrick = (new Item(149)).setUnlocalizedName("netherbrick").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("netherbrick");
	public static Item netherQuartz = (new Item(150)).setUnlocalizedName("netherquartz").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("quartz");
	public static Item minecartTnt = (new ItemMinecart(151, 3)).setUnlocalizedName("minecartTnt").setTextureName("minecart_tnt");
	public static Item minecartHopper = (new ItemMinecart(152, 5)).setUnlocalizedName("minecartHopper").setTextureName("minecart_hopper");
	public static Item horseArmorIron = (new Item(161)).setUnlocalizedName("horsearmormetal").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc).setTextureName("iron_horse_armor");
	public static Item horseArmorGold = (new Item(162)).setUnlocalizedName("horsearmorgold").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc).setTextureName("gold_horse_armor");
	public static Item horseArmorDiamond = (new Item(163)).setUnlocalizedName("horsearmordiamond").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc).setTextureName("diamond_horse_armor");
	public static Item leash = (new ItemLeash(164)).setUnlocalizedName("leash").setTextureName("lead");
	public static Item nameTag = (new ItemNameTag(165)).setUnlocalizedName("nameTag").setTextureName("name_tag");
	public static Item record13 = (new ItemRecord(2000, "13")).setUnlocalizedName("record").setTextureName("record_13");
	public static Item recordCat = (new ItemRecord(2001, "cat")).setUnlocalizedName("record").setTextureName("record_cat");
	public static Item recordBlocks = (new ItemRecord(2002, "blocks")).setUnlocalizedName("record").setTextureName("record_blocks");
	public static Item recordChirp = (new ItemRecord(2003, "chirp")).setUnlocalizedName("record").setTextureName("record_chirp");
	public static Item recordFar = (new ItemRecord(2004, "far")).setUnlocalizedName("record").setTextureName("record_far");
	public static Item recordMall = (new ItemRecord(2005, "mall")).setUnlocalizedName("record").setTextureName("record_mall");
	public static Item recordMellohi = (new ItemRecord(2006, "mellohi")).setUnlocalizedName("record").setTextureName("record_mellohi");
	public static Item recordStal = (new ItemRecord(2007, "stal")).setUnlocalizedName("record").setTextureName("record_stal");
	public static Item recordStrad = (new ItemRecord(2008, "strad")).setUnlocalizedName("record").setTextureName("record_strad");
	public static Item recordWard = (new ItemRecord(2009, "ward")).setUnlocalizedName("record").setTextureName("record_ward");
	public static Item record11 = (new ItemRecord(2010, "11")).setUnlocalizedName("record").setTextureName("record_11");
	public static Item recordWait = (new ItemRecord(2011, "wait")).setUnlocalizedName("record").setTextureName("record_wait");

	/** The ID of this item. */
	public final int itemID;

	/** Maximum size of the stack. */
	protected int maxStackSize = 64;

	/** Maximum damage an item can handle. */
	private int maxDamage;

	/** If true, render the object in full 3D, like weapons and tools. */
	protected boolean bFull3D;

	/**
	 * Some items (like dyes) have multiple subtypes on same item, this is field define this behavior
	 */
	protected boolean hasSubtypes;
	private Item containerItem;
	private String potionEffect;

	/** The unlocalized name of this item. */
	private String unlocalizedName;

	/** Icon index in the icons table. */
	protected Icon itemIcon;
	
	/** The string associated with this Item's Icon. */
	protected String iconString;

	protected Item(int par1) {
		this.itemID = 256 + par1;

		if (itemsList[256 + par1] != null) {
			System.out.println("CONFLICT @ " + par1);
		}

		itemsList[256 + par1] = this;
	}

	public Item setMaxStackSize(int par1) {
		this.maxStackSize = par1;
		return this;
	}

	/**
	 * Returns 0 for /terrain.png, 1 for /gui/items.png
	 */
	public int getSpriteNumber() {
		return 1;
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	public Icon getIconFromDamage(int par1) {
		return this.itemIcon;
	}

	/**
	 * Returns the icon index of the stack given as argument.
	 */
	public final Icon getIconIndex(ItemStack par1ItemStack) {
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

	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
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
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
		return false;
	}

	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
		return false;
	}

	/**
	 * Returns if the item (tool) can harvest results from the block type.
	 */
	public boolean canHarvestBlock(Block par1Block) {
		return false;
	}

	/**
	 * Returns true if the item can be used on the given entity, e.g. shears on sheep.
	 */
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase) {
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
	 * Sets the unlocalized name of this item to the string passed as the parameter, prefixed by "item."
	 */
	public Item setUnlocalizedName(String par1Str) {
		this.unlocalizedName = par1Str;
		return this;
	}

	/**
	 * Translates the unlocalized name of this item, but without the .name suffix, so the translation fails and the
	 * unlocalized name itself is returned.
	 */
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		String var2 = this.getUnlocalizedName(par1ItemStack);
		return var2 == null ? "" : StatCollector.translateToLocal(var2);
	}

	/**
	 * Returns the unlocalized name of this item.
	 */
	public String getUnlocalizedName() {
		return "item." + this.unlocalizedName;
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have different
	 * names based on their damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return "item." + this.unlocalizedName;
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
		return StatCollector.translateToLocal(this.getUnlocalizedName() + ".name");
	}

	public String getItemStackDisplayName(ItemStack par1ItemStack) {
		return StatCollector.translateToLocal(this.getUnlocalizedName(par1ItemStack) + ".name");
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
		return ("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(par1ItemStack) + ".name")).trim();
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
	public Icon getIconFromDamageForRenderPass(int par1, int par2) {
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

	/**
	 * Returns true if players can use this item to affect the world (e.g. placing blocks, placing ender eyes in portal)
	 * when not in creative
	 */
	public boolean canItemEditBlocks() {
		return true;
	}

	/**
	 * Return whether this item is repairable in an anvil.
	 */
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return false;
	}

	public void registerIcons(IconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon(this.getIconString());
	}

	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
	 */
	public Multimap getItemAttributeModifiers() {
		return HashMultimap.create();
	}

	protected Item setTextureName(String par1Str) {
		this.iconString = par1Str;
		return this;
	}

	/**
	 * Returns the string associated with this Item's Icon.
	 */
	protected String getIconString() {
		return this.iconString == null ? "MISSING_ICON_ITEM_" + this.itemID + "_" + this.unlocalizedName : this.iconString;
	}

	static {
		StatList.initStats();
	}
}
