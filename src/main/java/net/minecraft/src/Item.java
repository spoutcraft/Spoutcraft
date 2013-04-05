package net.minecraft.src;

import java.util.List;
import java.util.Random;
// Spout Start
import org.spoutcraft.client.item.SpoutItem;
// Spout End
// MCPatcher Start
import com.prupe.mcpatcher.mod.CITUtils;
// MCPatcher End

public class Item {
	private CreativeTabs tabToDisplayOn = null;

	/** The RNG used by the Item subclasses. */
	protected static Random itemRand = new Random();

	/** A 32000 elements Item array. */
	public static Item[] itemsList = new Item[32000];
	public static Item shovelSteel = (new ItemSpade(0, EnumToolMaterial.IRON)).setUnlocalizedName("shovelIron");
	public static Item pickaxeSteel = (new ItemPickaxe(1, EnumToolMaterial.IRON)).setUnlocalizedName("pickaxeIron");
	public static Item axeSteel = (new ItemAxe(2, EnumToolMaterial.IRON)).setUnlocalizedName("hatchetIron");
	public static Item flintAndSteel = (new ItemFlintAndSteel(3)).setUnlocalizedName("flintAndSteel");
	public static Item appleRed = (new ItemFood(4, 4, 0.3F, false)).setUnlocalizedName("apple");
	public static ItemBow bow = (ItemBow)(new ItemBow(5)).setUnlocalizedName("bow");
	public static Item arrow = (new Item(6)).setUnlocalizedName("arrow").setCreativeTab(CreativeTabs.tabCombat);
	public static Item coal = (new ItemCoal(7)).setUnlocalizedName("coal");
	public static Item diamond = (new Item(8)).setUnlocalizedName("diamond").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item ingotIron = (new Item(9)).setUnlocalizedName("ingotIron").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item ingotGold = (new Item(10)).setUnlocalizedName("ingotGold").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item swordSteel = (new ItemSword(11, EnumToolMaterial.IRON)).setUnlocalizedName("swordIron");
	public static Item swordWood = (new ItemSword(12, EnumToolMaterial.WOOD)).setUnlocalizedName("swordWood");
	public static Item shovelWood = (new ItemSpade(13, EnumToolMaterial.WOOD)).setUnlocalizedName("shovelWood");
	public static Item pickaxeWood = (new ItemPickaxe(14, EnumToolMaterial.WOOD)).setUnlocalizedName("pickaxeWood");
	public static Item axeWood = (new ItemAxe(15, EnumToolMaterial.WOOD)).setUnlocalizedName("hatchetWood");
	public static Item swordStone = (new ItemSword(16, EnumToolMaterial.STONE)).setUnlocalizedName("swordStone");
	public static Item shovelStone = (new ItemSpade(17, EnumToolMaterial.STONE)).setUnlocalizedName("shovelStone");
	public static Item pickaxeStone = (new ItemPickaxe(18, EnumToolMaterial.STONE)).setUnlocalizedName("pickaxeStone");
	public static Item axeStone = (new ItemAxe(19, EnumToolMaterial.STONE)).setUnlocalizedName("hatchetStone");
	public static Item swordDiamond = (new ItemSword(20, EnumToolMaterial.EMERALD)).setUnlocalizedName("swordDiamond");
	public static Item shovelDiamond = (new ItemSpade(21, EnumToolMaterial.EMERALD)).setUnlocalizedName("shovelDiamond");
	public static Item pickaxeDiamond = (new ItemPickaxe(22, EnumToolMaterial.EMERALD)).setUnlocalizedName("pickaxeDiamond");
	public static Item axeDiamond = (new ItemAxe(23, EnumToolMaterial.EMERALD)).setUnlocalizedName("hatchetDiamond");
	public static Item stick = (new Item(24)).setFull3D().setUnlocalizedName("stick").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item bowlEmpty = (new Item(25)).setUnlocalizedName("bowl").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item bowlSoup = (new ItemSoup(26, 6)).setUnlocalizedName("mushroomStew");
	public static Item swordGold = (new ItemSword(27, EnumToolMaterial.GOLD)).setUnlocalizedName("swordGold");
	public static Item shovelGold = (new ItemSpade(28, EnumToolMaterial.GOLD)).setUnlocalizedName("shovelGold");
	public static Item pickaxeGold = (new ItemPickaxe(29, EnumToolMaterial.GOLD)).setUnlocalizedName("pickaxeGold");
	public static Item axeGold = (new ItemAxe(30, EnumToolMaterial.GOLD)).setUnlocalizedName("hatchetGold");
	public static Item silk = (new ItemReed(31, Block.tripWire)).setUnlocalizedName("string").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item feather = (new Item(32)).setUnlocalizedName("feather").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item gunpowder = (new Item(33)).setUnlocalizedName("sulphur").setPotionEffect(PotionHelper.gunpowderEffect).setCreativeTab(CreativeTabs.tabMaterials);
	public static Item hoeWood = (new ItemHoe(34, EnumToolMaterial.WOOD)).setUnlocalizedName("hoeWood");
	public static Item hoeStone = (new ItemHoe(35, EnumToolMaterial.STONE)).setUnlocalizedName("hoeStone");
	public static Item hoeSteel = (new ItemHoe(36, EnumToolMaterial.IRON)).setUnlocalizedName("hoeIron");
	public static Item hoeDiamond = (new ItemHoe(37, EnumToolMaterial.EMERALD)).setUnlocalizedName("hoeDiamond");
	public static Item hoeGold = (new ItemHoe(38, EnumToolMaterial.GOLD)).setUnlocalizedName("hoeGold");
	public static Item seeds = (new ItemSeeds(39, Block.crops.blockID, Block.tilledField.blockID)).setUnlocalizedName("seeds");
	public static Item wheat = (new Item(40)).setUnlocalizedName("wheat").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item bread = (new ItemFood(41, 5, 0.6F, false)).setUnlocalizedName("bread");
	public static ItemArmor helmetLeather = (ItemArmor)(new ItemArmor(42, EnumArmorMaterial.CLOTH, 0, 0)).setUnlocalizedName("helmetCloth");
	public static ItemArmor plateLeather = (ItemArmor)(new ItemArmor(43, EnumArmorMaterial.CLOTH, 0, 1)).setUnlocalizedName("chestplateCloth");
	public static ItemArmor legsLeather = (ItemArmor)(new ItemArmor(44, EnumArmorMaterial.CLOTH, 0, 2)).setUnlocalizedName("leggingsCloth");
	public static ItemArmor bootsLeather = (ItemArmor)(new ItemArmor(45, EnumArmorMaterial.CLOTH, 0, 3)).setUnlocalizedName("bootsCloth");
	public static ItemArmor helmetChain = (ItemArmor)(new ItemArmor(46, EnumArmorMaterial.CHAIN, 1, 0)).setUnlocalizedName("helmetChain");
	public static ItemArmor plateChain = (ItemArmor)(new ItemArmor(47, EnumArmorMaterial.CHAIN, 1, 1)).setUnlocalizedName("chestplateChain");
	public static ItemArmor legsChain = (ItemArmor)(new ItemArmor(48, EnumArmorMaterial.CHAIN, 1, 2)).setUnlocalizedName("leggingsChain");
	public static ItemArmor bootsChain = (ItemArmor)(new ItemArmor(49, EnumArmorMaterial.CHAIN, 1, 3)).setUnlocalizedName("bootsChain");
	public static ItemArmor helmetSteel = (ItemArmor)(new ItemArmor(50, EnumArmorMaterial.IRON, 2, 0)).setUnlocalizedName("helmetIron");
	public static ItemArmor plateSteel = (ItemArmor)(new ItemArmor(51, EnumArmorMaterial.IRON, 2, 1)).setUnlocalizedName("chestplateIron");
	public static ItemArmor legsSteel = (ItemArmor)(new ItemArmor(52, EnumArmorMaterial.IRON, 2, 2)).setUnlocalizedName("leggingsIron");
	public static ItemArmor bootsSteel = (ItemArmor)(new ItemArmor(53, EnumArmorMaterial.IRON, 2, 3)).setUnlocalizedName("bootsIron");
	public static ItemArmor helmetDiamond = (ItemArmor)(new ItemArmor(54, EnumArmorMaterial.DIAMOND, 3, 0)).setUnlocalizedName("helmetDiamond");
	public static ItemArmor plateDiamond = (ItemArmor)(new ItemArmor(55, EnumArmorMaterial.DIAMOND, 3, 1)).setUnlocalizedName("chestplateDiamond");
	public static ItemArmor legsDiamond = (ItemArmor)(new ItemArmor(56, EnumArmorMaterial.DIAMOND, 3, 2)).setUnlocalizedName("leggingsDiamond");
	public static ItemArmor bootsDiamond = (ItemArmor)(new ItemArmor(57, EnumArmorMaterial.DIAMOND, 3, 3)).setUnlocalizedName("bootsDiamond");
	public static ItemArmor helmetGold = (ItemArmor)(new ItemArmor(58, EnumArmorMaterial.GOLD, 4, 0)).setUnlocalizedName("helmetGold");
	public static ItemArmor plateGold = (ItemArmor)(new ItemArmor(59, EnumArmorMaterial.GOLD, 4, 1)).setUnlocalizedName("chestplateGold");
	public static ItemArmor legsGold = (ItemArmor)(new ItemArmor(60, EnumArmorMaterial.GOLD, 4, 2)).setUnlocalizedName("leggingsGold");
	public static ItemArmor bootsGold = (ItemArmor)(new ItemArmor(61, EnumArmorMaterial.GOLD, 4, 3)).setUnlocalizedName("bootsGold");
	// Spout Start - Changed to SpoutItemBlock
	public static Item flint = (new SpoutItem(62)).setUnlocalizedName("flint").setCreativeTab(CreativeTabs.tabMaterials);
	// Spout End
	public static Item porkRaw = (new ItemFood(63, 3, 0.3F, true)).setUnlocalizedName("porkchopRaw");
	public static Item porkCooked = (new ItemFood(64, 8, 0.8F, true)).setUnlocalizedName("porkchopCooked");
	public static Item painting = (new ItemHangingEntity(65, EntityPainting.class)).setUnlocalizedName("painting");
	public static Item appleGold = (new ItemAppleGold(66, 4, 1.2F, false)).setAlwaysEdible().setPotionEffect(Potion.regeneration.id, 5, 0, 1.0F).setUnlocalizedName("appleGold");
	public static Item sign = (new ItemSign(67)).setUnlocalizedName("sign");
	public static Item doorWood = (new ItemDoor(68, Material.wood)).setUnlocalizedName("doorWood");
	public static Item bucketEmpty = (new ItemBucket(69, 0)).setUnlocalizedName("bucket").setMaxStackSize(16);
	public static Item bucketWater = (new ItemBucket(70, Block.waterMoving.blockID)).setUnlocalizedName("bucketWater").setContainerItem(bucketEmpty);
	public static Item bucketLava = (new ItemBucket(71, Block.lavaMoving.blockID)).setUnlocalizedName("bucketLava").setContainerItem(bucketEmpty);
	public static Item minecartEmpty = (new ItemMinecart(72, 0)).setUnlocalizedName("minecart");
	public static Item saddle = (new ItemSaddle(73)).setUnlocalizedName("saddle");
	public static Item doorSteel = (new ItemDoor(74, Material.iron)).setUnlocalizedName("doorIron");
	public static Item redstone = (new ItemRedstone(75)).setUnlocalizedName("redstone").setPotionEffect(PotionHelper.redstoneEffect);
	public static Item snowball = (new ItemSnowball(76)).setUnlocalizedName("snowball");
	public static Item boat = (new ItemBoat(77)).setUnlocalizedName("boat");
	public static Item leather = (new Item(78)).setUnlocalizedName("leather").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item bucketMilk = (new ItemBucketMilk(79)).setUnlocalizedName("milk").setContainerItem(bucketEmpty);
	public static Item brick = (new Item(80)).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item clay = (new Item(81)).setUnlocalizedName("clay").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item reed = (new ItemReed(82, Block.reed)).setUnlocalizedName("reeds").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item paper = (new Item(83)).setUnlocalizedName("paper").setCreativeTab(CreativeTabs.tabMisc);
	public static Item book = (new ItemBook(84)).setUnlocalizedName("book").setCreativeTab(CreativeTabs.tabMisc);
	public static Item slimeBall = (new Item(85)).setUnlocalizedName("slimeball").setCreativeTab(CreativeTabs.tabMisc);
	public static Item minecartCrate = (new ItemMinecart(86, 1)).setUnlocalizedName("minecartChest");
	public static Item minecartPowered = (new ItemMinecart(87, 2)).setUnlocalizedName("minecartFurnace");
	public static Item egg = (new ItemEgg(88)).setUnlocalizedName("egg");
	public static Item compass = (new Item(89)).setUnlocalizedName("compass").setCreativeTab(CreativeTabs.tabTools);
	public static ItemFishingRod fishingRod = (ItemFishingRod)(new ItemFishingRod(90)).setUnlocalizedName("fishingRod");
	public static Item pocketSundial = (new Item(91)).setUnlocalizedName("clock").setCreativeTab(CreativeTabs.tabTools);
	public static Item lightStoneDust = (new Item(92)).setUnlocalizedName("yellowDust").setPotionEffect(PotionHelper.glowstoneEffect).setCreativeTab(CreativeTabs.tabMaterials);
	public static Item fishRaw = (new ItemFood(93, 2, 0.3F, false)).setUnlocalizedName("fishRaw");
	public static Item fishCooked = (new ItemFood(94, 5, 0.6F, false)).setUnlocalizedName("fishCooked");
	public static Item dyePowder = (new ItemDye(95)).setUnlocalizedName("dyePowder");
	public static Item bone = (new Item(96)).setUnlocalizedName("bone").setFull3D().setCreativeTab(CreativeTabs.tabMisc);
	public static Item sugar = (new Item(97)).setUnlocalizedName("sugar").setPotionEffect(PotionHelper.sugarEffect).setCreativeTab(CreativeTabs.tabMaterials);
	public static Item cake = (new ItemReed(98, Block.cake)).setMaxStackSize(1).setUnlocalizedName("cake").setCreativeTab(CreativeTabs.tabFood);
	public static Item bed = (new ItemBed(99)).setMaxStackSize(1).setUnlocalizedName("bed");
	public static Item redstoneRepeater = (new ItemReed(100, Block.redstoneRepeaterIdle)).setUnlocalizedName("diode").setCreativeTab(CreativeTabs.tabRedstone);
	public static Item cookie = (new ItemFood(101, 2, 0.1F, false)).setUnlocalizedName("cookie");
	public static ItemMap map = (ItemMap)(new ItemMap(102)).setUnlocalizedName("map");

	/**
	 * Item introduced on 1.7 version, is a shear to cut leaves (you can keep the block) or get wool from sheeps.
	 */
	public static ItemShears shears = (ItemShears)(new ItemShears(103)).setUnlocalizedName("shears");
	public static Item melon = (new ItemFood(104, 2, 0.3F, false)).setUnlocalizedName("melon");
	public static Item pumpkinSeeds = (new ItemSeeds(105, Block.pumpkinStem.blockID, Block.tilledField.blockID)).setUnlocalizedName("seeds_pumpkin");
	public static Item melonSeeds = (new ItemSeeds(106, Block.melonStem.blockID, Block.tilledField.blockID)).setUnlocalizedName("seeds_melon");
	public static Item beefRaw = (new ItemFood(107, 3, 0.3F, true)).setUnlocalizedName("beefRaw");
	public static Item beefCooked = (new ItemFood(108, 8, 0.8F, true)).setUnlocalizedName("beefCooked");
	public static Item chickenRaw = (new ItemFood(109, 2, 0.3F, true)).setPotionEffect(Potion.hunger.id, 30, 0, 0.3F).setUnlocalizedName("chickenRaw");
	public static Item chickenCooked = (new ItemFood(110, 6, 0.6F, true)).setUnlocalizedName("chickenCooked");
	public static Item rottenFlesh = (new ItemFood(111, 4, 0.1F, true)).setPotionEffect(Potion.hunger.id, 30, 0, 0.8F).setUnlocalizedName("rottenFlesh");
	public static Item enderPearl = (new ItemEnderPearl(112)).setUnlocalizedName("enderPearl");
	public static Item blazeRod = (new Item(113)).setUnlocalizedName("blazeRod").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item ghastTear = (new Item(114)).setUnlocalizedName("ghastTear").setPotionEffect(PotionHelper.ghastTearEffect).setCreativeTab(CreativeTabs.tabBrewing);
	public static Item goldNugget = (new Item(115)).setUnlocalizedName("goldNugget").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item netherStalkSeeds = (new ItemSeeds(116, Block.netherStalk.blockID, Block.slowSand.blockID)).setUnlocalizedName("netherStalkSeeds").setPotionEffect("+4");
	public static ItemPotion potion = (ItemPotion)(new ItemPotion(117)).setUnlocalizedName("potion");
	public static Item glassBottle = (new ItemGlassBottle(118)).setUnlocalizedName("glassBottle");
	public static Item spiderEye = (new ItemFood(119, 2, 0.8F, false)).setPotionEffect(Potion.poison.id, 5, 0, 1.0F).setUnlocalizedName("spiderEye").setPotionEffect(PotionHelper.spiderEyeEffect);
	public static Item fermentedSpiderEye = (new Item(120)).setUnlocalizedName("fermentedSpiderEye").setPotionEffect(PotionHelper.fermentedSpiderEyeEffect).setCreativeTab(CreativeTabs.tabBrewing);
	public static Item blazePowder = (new Item(121)).setUnlocalizedName("blazePowder").setPotionEffect(PotionHelper.blazePowderEffect).setCreativeTab(CreativeTabs.tabBrewing);
	public static Item magmaCream = (new Item(122)).setUnlocalizedName("magmaCream").setPotionEffect(PotionHelper.magmaCreamEffect).setCreativeTab(CreativeTabs.tabBrewing);
	public static Item brewingStand = (new ItemReed(123, Block.brewingStand)).setUnlocalizedName("brewingStand").setCreativeTab(CreativeTabs.tabBrewing);
	public static Item cauldron = (new ItemReed(124, Block.cauldron)).setUnlocalizedName("cauldron").setCreativeTab(CreativeTabs.tabBrewing);
	public static Item eyeOfEnder = (new ItemEnderEye(125)).setUnlocalizedName("eyeOfEnder");
	public static Item speckledMelon = (new Item(126)).setUnlocalizedName("speckledMelon").setPotionEffect(PotionHelper.speckledMelonEffect).setCreativeTab(CreativeTabs.tabBrewing);
	public static Item monsterPlacer = (new ItemMonsterPlacer(127)).setUnlocalizedName("monsterPlacer");

	/**
	 * Bottle o' Enchanting. Drops between 1 and 3 experience orbs when thrown.
	 */
	public static Item expBottle = (new ItemExpBottle(128)).setUnlocalizedName("expBottle");

	/**
	 * Fire Charge. When used in a dispenser it fires a fireball similiar to a Ghast's.
	 */
	public static Item fireballCharge = (new ItemFireball(129)).setUnlocalizedName("fireball");
	public static Item writableBook = (new ItemWritableBook(130)).setUnlocalizedName("writingBook").setCreativeTab(CreativeTabs.tabMisc);
	public static Item writtenBook = (new ItemEditableBook(131)).setUnlocalizedName("writtenBook");
	public static Item emerald = (new Item(132)).setUnlocalizedName("emerald").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item itemFrame = (new ItemHangingEntity(133, EntityItemFrame.class)).setUnlocalizedName("frame");
	public static Item flowerPot = (new ItemReed(134, Block.flowerPot)).setUnlocalizedName("flowerPot").setCreativeTab(CreativeTabs.tabDecorations);
	public static Item carrot = (new ItemSeedFood(135, 4, 0.6F, Block.carrot.blockID, Block.tilledField.blockID)).setUnlocalizedName("carrots");
	public static Item potato = (new ItemSeedFood(136, 1, 0.3F, Block.potato.blockID, Block.tilledField.blockID)).setUnlocalizedName("potato");
	public static Item bakedPotato = (new ItemFood(137, 6, 0.6F, false)).setUnlocalizedName("potatoBaked");
	public static Item poisonousPotato = (new ItemFood(138, 2, 0.3F, false)).setPotionEffect(Potion.poison.id, 5, 0, 0.6F).setUnlocalizedName("potatoPoisonous");
	public static ItemEmptyMap emptyMap = (ItemEmptyMap)(new ItemEmptyMap(139)).setUnlocalizedName("emptyMap");
	public static Item goldenCarrot = (new ItemFood(140, 6, 1.2F, false)).setUnlocalizedName("carrotGolden").setPotionEffect(PotionHelper.field_82818_l);
	public static Item skull = (new ItemSkull(141)).setUnlocalizedName("skull");
	public static Item carrotOnAStick = (new ItemCarrotOnAStick(142)).setUnlocalizedName("carrotOnAStick");
	public static Item netherStar = (new ItemSimpleFoiled(143)).setUnlocalizedName("netherStar").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item pumpkinPie = (new ItemFood(144, 8, 0.3F, false)).setUnlocalizedName("pumpkinPie").setCreativeTab(CreativeTabs.tabFood);
	public static Item firework = (new ItemFirework(145)).setUnlocalizedName("fireworks");
	public static Item fireworkCharge = (new ItemFireworkCharge(146)).setUnlocalizedName("fireworksCharge").setCreativeTab(CreativeTabs.tabMisc);
	public static ItemEnchantedBook enchantedBook = (ItemEnchantedBook)(new ItemEnchantedBook(147)).setMaxStackSize(1).setUnlocalizedName("enchantedBook");
	public static Item field_94585_bY = (new ItemReed(148, Block.redstoneComparatorIdle)).setUnlocalizedName("comparator").setCreativeTab(CreativeTabs.tabRedstone);
	public static Item field_94584_bZ = (new Item(149)).setUnlocalizedName("netherbrick").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item field_94583_ca = (new Item(150)).setUnlocalizedName("netherquartz").setCreativeTab(CreativeTabs.tabMaterials);
	public static Item tntMinecart = (new ItemMinecart(151, 3)).setUnlocalizedName("minecartTnt");
	public static Item hopperMinecart = (new ItemMinecart(152, 5)).setUnlocalizedName("minecartHopper");
	public static Item record13 = (new ItemRecord(2000, "13")).setUnlocalizedName("record");
	public static Item recordCat = (new ItemRecord(2001, "cat")).setUnlocalizedName("record");
	public static Item recordBlocks = (new ItemRecord(2002, "blocks")).setUnlocalizedName("record");
	public static Item recordChirp = (new ItemRecord(2003, "chirp")).setUnlocalizedName("record");
	public static Item recordFar = (new ItemRecord(2004, "far")).setUnlocalizedName("record");
	public static Item recordMall = (new ItemRecord(2005, "mall")).setUnlocalizedName("record");
	public static Item recordMellohi = (new ItemRecord(2006, "mellohi")).setUnlocalizedName("record");
	public static Item recordStal = (new ItemRecord(2007, "stal")).setUnlocalizedName("record");
	public static Item recordStrad = (new ItemRecord(2008, "strad")).setUnlocalizedName("record");
	public static Item recordWard = (new ItemRecord(2009, "ward")).setUnlocalizedName("record");
	public static Item record11 = (new ItemRecord(2010, "11")).setUnlocalizedName("record");
	public static Item recordWait = (new ItemRecord(2011, "wait")).setUnlocalizedName("record");

	/** The ID of this item. */
	public final int itemID;

	/** Maximum size of the stack. */
	protected int maxStackSize = 64;

	/** Maximum damage an item can handle. */
	private int maxDamage = 0;

	/** If true, render the object in full 3D, like weapons and tools. */
	protected boolean bFull3D = false;

	/**
	 * Some items (like dyes) have multiple subtypes on same item, this is field define this behavior
	 */
	protected boolean hasSubtypes = false;
	private Item containerItem = null;
	private String potionEffect = null;

	/** The unlocalized name of this item. */
	private String unlocalizedName;

	/** Icon index in the icons table. */
	protected Icon iconIndex;

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
		return this.iconIndex;
	}

	/**
	 * Returns the icon index of the stack given as argument.
	 */
	public final Icon getIconIndex(ItemStack par1ItemStack) {
		return CITUtils.getIcon(this.getIconFromDamage(par1ItemStack.getItemDamage()), this, par1ItemStack);
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
	 * Called when a player right clicks an entity with an item.
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
	 * Sets the unlocalized name of this item to the string passed as the parameter, prefixed by "item."
	 */
	public Item setUnlocalizedName(String par1Str) {
		this.unlocalizedName = par1Str;
		return this;
	}

	/**
	 * Gets the localized name of the given item stack.
	 */
	public String getLocalizedName(ItemStack par1ItemStack) {
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

	public String func_77653_i(ItemStack par1ItemStack) {
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
		return ("" + StringTranslate.getInstance().translateNamedKey(this.getLocalizedName(par1ItemStack))).trim();
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

	public boolean func_82788_x() {
		return true;
	}

	/**
	 * Return whether this item is repairable in an anvil.
	 */
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return false;
	}

	public void updateIcons(IconRegister par1IconRegister) {
		this.iconIndex = par1IconRegister.registerIcon(this.unlocalizedName);
	}

	static {
		StatList.initStats();
	}
}
