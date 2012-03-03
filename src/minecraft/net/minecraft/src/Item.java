package net.minecraft.src;

import java.util.List;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumAction;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemAppleGold;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemAxe;
import net.minecraft.src.ItemBed;
import net.minecraft.src.ItemBoat;
import net.minecraft.src.ItemBow;
import net.minecraft.src.ItemBucket;
import net.minecraft.src.ItemBucketMilk;
import net.minecraft.src.ItemCoal;
import net.minecraft.src.ItemDoor;
import net.minecraft.src.ItemDye;
import net.minecraft.src.ItemEgg;
import net.minecraft.src.ItemEnderEye;
import net.minecraft.src.ItemEnderPearl;
import net.minecraft.src.ItemExpBottle;
import net.minecraft.src.ItemFireball;
import net.minecraft.src.ItemFishingRod;
import net.minecraft.src.ItemFlintAndSteel;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemGlassBottle;
import net.minecraft.src.ItemHoe;
import net.minecraft.src.ItemMap;
import net.minecraft.src.ItemMinecart;
import net.minecraft.src.ItemMonsterPlacer;
import net.minecraft.src.ItemPainting;
import net.minecraft.src.ItemPickaxe;
import net.minecraft.src.ItemPotion;
import net.minecraft.src.ItemRecord;
import net.minecraft.src.ItemRedstone;
import net.minecraft.src.ItemReed;
import net.minecraft.src.ItemSaddle;
import net.minecraft.src.ItemSeeds;
import net.minecraft.src.ItemShears;
import net.minecraft.src.ItemSign;
import net.minecraft.src.ItemSnowball;
import net.minecraft.src.ItemSoup;
import net.minecraft.src.ItemSpade;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ItemSword;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionHelper;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StatList;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
//Spout start
import org.spoutcraft.client.item.SpoutItem;
//Spout end

public class Item {

	protected static Random itemRand = new Random();
	public static Item[] itemsList = new Item[32000];
	public static Item shovelSteel = (new ItemSpade(0, EnumToolMaterial.IRON)).setIconCoord(2, 5).setItemName("shovelIron");
	public static Item pickaxeSteel = (new ItemPickaxe(1, EnumToolMaterial.IRON)).setIconCoord(2, 6).setItemName("pickaxeIron");
	public static Item axeSteel = (new ItemAxe(2, EnumToolMaterial.IRON)).setIconCoord(2, 7).setItemName("hatchetIron");
	public static Item flintAndSteel = (new ItemFlintAndSteel(3)).setIconCoord(5, 0).setItemName("flintAndSteel");
	public static Item appleRed = (new ItemFood(4, 4, 0.3F, false)).setIconCoord(10, 0).setItemName("apple");
	public static Item bow = (new ItemBow(5)).setIconCoord(5, 1).setItemName("bow");
	public static Item arrow = (new Item(6)).setIconCoord(5, 2).setItemName("arrow");
	public static Item coal = (new ItemCoal(7)).setIconCoord(7, 0).setItemName("coal");
	public static Item diamond = (new Item(8)).setIconCoord(7, 3).setItemName("emerald");
	public static Item ingotIron = (new Item(9)).setIconCoord(7, 1).setItemName("ingotIron");
	public static Item ingotGold = (new Item(10)).setIconCoord(7, 2).setItemName("ingotGold");
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
	public static Item stick = (new Item(24)).setIconCoord(5, 3).setFull3D().setItemName("stick");
	public static Item bowlEmpty = (new Item(25)).setIconCoord(7, 4).setItemName("bowl");
	public static Item bowlSoup = (new ItemSoup(26, 8)).setIconCoord(8, 4).setItemName("mushroomStew");
	public static Item swordGold = (new ItemSword(27, EnumToolMaterial.GOLD)).setIconCoord(4, 4).setItemName("swordGold");
	public static Item shovelGold = (new ItemSpade(28, EnumToolMaterial.GOLD)).setIconCoord(4, 5).setItemName("shovelGold");
	public static Item pickaxeGold = (new ItemPickaxe(29, EnumToolMaterial.GOLD)).setIconCoord(4, 6).setItemName("pickaxeGold");
	public static Item axeGold = (new ItemAxe(30, EnumToolMaterial.GOLD)).setIconCoord(4, 7).setItemName("hatchetGold");
	public static Item silk = (new Item(31)).setIconCoord(8, 0).setItemName("string");
	public static Item feather = (new Item(32)).setIconCoord(8, 1).setItemName("feather");
	public static Item gunpowder = (new Item(33)).setIconCoord(8, 2).setItemName("sulphur").setPotionEffect(PotionHelper.gunpowderEffect);
	public static Item hoeWood = (new ItemHoe(34, EnumToolMaterial.WOOD)).setIconCoord(0, 8).setItemName("hoeWood");
	public static Item hoeStone = (new ItemHoe(35, EnumToolMaterial.STONE)).setIconCoord(1, 8).setItemName("hoeStone");
	public static Item hoeSteel = (new ItemHoe(36, EnumToolMaterial.IRON)).setIconCoord(2, 8).setItemName("hoeIron");
	public static Item hoeDiamond = (new ItemHoe(37, EnumToolMaterial.EMERALD)).setIconCoord(3, 8).setItemName("hoeDiamond");
	public static Item hoeGold = (new ItemHoe(38, EnumToolMaterial.GOLD)).setIconCoord(4, 8).setItemName("hoeGold");
	public static Item seeds = (new ItemSeeds(39, Block.crops.blockID, Block.tilledField.blockID)).setIconCoord(9, 0).setItemName("seeds");
	public static Item wheat = (new Item(40)).setIconCoord(9, 1).setItemName("wheat");
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
	public static Item flint = (new SpoutItem(62)).setIconCoord(6, 0).setItemName("flint"); //Spout changed to SpoutItemBlock
	public static Item porkRaw = (new ItemFood(63, 3, 0.3F, true)).setIconCoord(7, 5).setItemName("porkchopRaw");
	public static Item porkCooked = (new ItemFood(64, 8, 0.8F, true)).setIconCoord(8, 5).setItemName("porkchopCooked");
	public static Item painting = (new ItemPainting(65)).setIconCoord(10, 1).setItemName("painting");
	public static Item appleGold = (new ItemAppleGold(66, 4, 1.2F, false)).setAlwaysEdible().setPotionEffect(Potion.regeneration.id, 5, 0, 1.0F).setIconCoord(11, 0).setItemName("appleGold");
	public static Item sign = (new ItemSign(67)).setIconCoord(10, 2).setItemName("sign");
	public static Item doorWood = (new ItemDoor(68, Material.wood)).setIconCoord(11, 2).setItemName("doorWood");
	public static Item bucketEmpty = (new ItemBucket(69, 0)).setIconCoord(10, 4).setItemName("bucket");
	public static Item bucketWater = (new ItemBucket(70, Block.waterMoving.blockID)).setIconCoord(11, 4).setItemName("bucketWater").setContainerItem(bucketEmpty);
	public static Item bucketLava = (new ItemBucket(71, Block.lavaMoving.blockID)).setIconCoord(12, 4).setItemName("bucketLava").setContainerItem(bucketEmpty);
	public static Item minecartEmpty = (new ItemMinecart(72, 0)).setIconCoord(7, 8).setItemName("minecart");
	public static Item saddle = (new ItemSaddle(73)).setIconCoord(8, 6).setItemName("saddle");
	public static Item doorSteel = (new ItemDoor(74, Material.iron)).setIconCoord(12, 2).setItemName("doorIron");
	public static Item redstone = (new ItemRedstone(75)).setIconCoord(8, 3).setItemName("redstone").setPotionEffect(PotionHelper.redstoneEffect);
	public static Item snowball = (new ItemSnowball(76)).setIconCoord(14, 0).setItemName("snowball");
	public static Item boat = (new ItemBoat(77)).setIconCoord(8, 8).setItemName("boat");
	public static Item leather = (new Item(78)).setIconCoord(7, 6).setItemName("leather");
	public static Item bucketMilk = (new ItemBucketMilk(79)).setIconCoord(13, 4).setItemName("milk").setContainerItem(bucketEmpty);
	public static Item brick = (new Item(80)).setIconCoord(6, 1).setItemName("brick");
	public static Item clay = (new Item(81)).setIconCoord(9, 3).setItemName("clay");
	public static Item reed = (new ItemReed(82, Block.reed)).setIconCoord(11, 1).setItemName("reeds");
	public static Item paper = (new Item(83)).setIconCoord(10, 3).setItemName("paper");
	public static Item book = (new Item(84)).setIconCoord(11, 3).setItemName("book");
	public static Item slimeBall = (new Item(85)).setIconCoord(14, 1).setItemName("slimeball");
	public static Item minecartCrate = (new ItemMinecart(86, 1)).setIconCoord(7, 9).setItemName("minecartChest");
	public static Item minecartPowered = (new ItemMinecart(87, 2)).setIconCoord(7, 10).setItemName("minecartFurnace");
	public static Item egg = (new ItemEgg(88)).setIconCoord(12, 0).setItemName("egg");
	public static Item compass = (new Item(89)).setIconCoord(6, 3).setItemName("compass");
	public static Item fishingRod = (new ItemFishingRod(90)).setIconCoord(5, 4).setItemName("fishingRod");
	public static Item pocketSundial = (new Item(91)).setIconCoord(6, 4).setItemName("clock");
	public static Item lightStoneDust = (new Item(92)).setIconCoord(9, 4).setItemName("yellowDust").setPotionEffect(PotionHelper.glowstoneEffect);
	public static Item fishRaw = (new ItemFood(93, 2, 0.3F, false)).setIconCoord(9, 5).setItemName("fishRaw");
	public static Item fishCooked = (new ItemFood(94, 5, 0.6F, false)).setIconCoord(10, 5).setItemName("fishCooked");
	public static Item dyePowder = (new ItemDye(95)).setIconCoord(14, 4).setItemName("dyePowder");
	public static Item bone = (new Item(96)).setIconCoord(12, 1).setItemName("bone").setFull3D();
	public static Item sugar = (new Item(97)).setIconCoord(13, 0).setItemName("sugar").setPotionEffect(PotionHelper.sugarEffect);
	public static Item cake = (new ItemReed(98, Block.cake)).setMaxStackSize(1).setIconCoord(13, 1).setItemName("cake");
	public static Item bed = (new ItemBed(99)).setMaxStackSize(1).setIconCoord(13, 2).setItemName("bed");
	public static Item redstoneRepeater = (new ItemReed(100, Block.redstoneRepeaterIdle)).setIconCoord(6, 5).setItemName("diode");
	public static Item cookie = (new ItemFood(101, 1, 0.1F, false)).setIconCoord(12, 5).setItemName("cookie");
	public static ItemMap map = (ItemMap)(new ItemMap(102)).setIconCoord(12, 3).setItemName("map");
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
	public static Item blazeRod = (new Item(113)).setIconCoord(12, 6).setItemName("blazeRod");
	public static Item ghastTear = (new Item(114)).setIconCoord(11, 7).setItemName("ghastTear").setPotionEffect(PotionHelper.ghastTearEffect);
	public static Item goldNugget = (new Item(115)).setIconCoord(12, 7).setItemName("goldNugget");
	public static Item netherStalkSeeds = (new ItemSeeds(116, Block.netherStalk.blockID, Block.slowSand.blockID)).setIconCoord(13, 7).setItemName("netherStalkSeeds").setPotionEffect("+4");
	public static ItemPotion potion = (ItemPotion)(new ItemPotion(117)).setIconCoord(13, 8).setItemName("potion");
	public static Item glassBottle = (new ItemGlassBottle(118)).setIconCoord(12, 8).setItemName("glassBottle");
	public static Item spiderEye = (new ItemFood(119, 2, 0.8F, false)).setPotionEffect(Potion.poison.id, 5, 0, 1.0F).setIconCoord(11, 8).setItemName("spiderEye").setPotionEffect(PotionHelper.spiderEyeEffect);
	public static Item fermentedSpiderEye = (new Item(120)).setIconCoord(10, 8).setItemName("fermentedSpiderEye").setPotionEffect(PotionHelper.fermentedSpiderEyeEffect);
	public static Item blazePowder = (new Item(121)).setIconCoord(13, 9).setItemName("blazePowder").setPotionEffect(PotionHelper.blazePowderEffect);
	public static Item magmaCream = (new Item(122)).setIconCoord(13, 10).setItemName("magmaCream").setPotionEffect(PotionHelper.magmaCreamEffect);
	public static Item brewingStand = (new ItemReed(123, Block.brewingStand)).setIconCoord(12, 10).setItemName("brewingStand");
	public static Item cauldron = (new ItemReed(124, Block.cauldron)).setIconCoord(12, 9).setItemName("cauldron");
	public static Item eyeOfEnder = (new ItemEnderEye(125)).setIconCoord(11, 9).setItemName("eyeOfEnder");
	public static Item speckledMelon = (new Item(126)).setIconCoord(9, 8).setItemName("speckledMelon").setPotionEffect(PotionHelper.speckledMelonEffect);
	public static Item monsterPlacer = (new ItemMonsterPlacer(127)).setIconCoord(9, 9).setItemName("monsterPlacer");
	public static Item field_48438_bD = (new ItemExpBottle(128)).setIconCoord(11, 10).setItemName("expBottle");
	public static Item field_48439_bE = (new ItemFireball(129)).setIconCoord(14, 2).setItemName("fireball");
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
	public final int shiftedIndex;
	protected int maxStackSize = 64;
	private int maxDamage = 0;
	protected int iconIndex;
	protected boolean bFull3D = false;
	protected boolean hasSubtypes = false;
	private Item containerItem = null;
	private String potionEffect = null;
	private String itemName;

	protected Item(int par1) {
		this.shiftedIndex = 256 + par1;
		if (itemsList[256 + par1] != null) {
			System.out.println("CONFLICT @ " + par1);
		}

		itemsList[256 + par1] = this;
	}

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

	public int getIconFromDamage(int par1) {
		return this.iconIndex;
	}

	public final int getIconIndex(ItemStack par1ItemStack) {
		return this.getIconFromDamage(par1ItemStack.getItemDamage());
	}

	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7) {
		return false;
	}

	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
		return 1.0F;
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	public int getItemStackLimit() {
		return this.maxStackSize;
	}

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

	public int getMaxDamage() {
		return this.maxDamage;
	}

	protected Item setMaxDamage(int par1) {
		this.maxDamage = par1;
		return this;
	}

	public boolean isDamageable() {
		return this.maxDamage > 0 && !this.hasSubtypes;
	}

	public boolean hitEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving) {
		return false;
	}

	public boolean onBlockDestroyed(ItemStack par1ItemStack, int par2, int par3, int par4, int par5, EntityLiving par6EntityLiving) {
		return false;
	}

	public int getDamageVsEntity(Entity par1Entity) {
		return 1;
	}

	public boolean canHarvestBlock(Block par1Block) {
		return false;
	}

	public void useItemOnEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving) {}

	public Item setFull3D() {
		this.bFull3D = true;
		return this;
	}

	public boolean isFull3D() {
		return this.bFull3D;
	}

	public boolean shouldRotateAroundWhenRendering() {
		return false;
	}

	public Item setItemName(String par1Str) {
		this.itemName = "item." + par1Str;
		return this;
	}

	public String getLocalItemName(ItemStack par1ItemStack) {
		String var2 = this.getItemNameIS(par1ItemStack);
		return var2 == null?"":StatCollector.translateToLocal(var2);
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

	public boolean doesContainerItemLeaveCraftingGrid(ItemStack par1ItemStack) {
		return true;
	}

	public boolean func_46056_k() {
		return false;
	}

	public Item getContainerItem() {
		return this.containerItem;
	}

	public boolean hasContainerItem() {
		return this.containerItem != null;
	}

	public String getStatName() {
		return StatCollector.translateToLocal(this.getItemName() + ".name");
	}

	public int getColorFromDamage(int par1, int par2) {
		return 16777215;
	}

	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {}

	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {}

	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}

	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 0;
	}

	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {}

	protected Item setPotionEffect(String par1Str) {
		this.potionEffect = par1Str;
		return this;
	}

	public String getPotionEffect() {
		return this.potionEffect;
	}

	public boolean isPotionIngredient() {
		return this.potionEffect != null;
	}

	public void addInformation(ItemStack par1ItemStack, List par2List) {}

	public String getItemDisplayName(ItemStack par1ItemStack) {
		String var2 = ("" + StringTranslate.getInstance().translateNamedKey(this.getLocalItemName(par1ItemStack))).trim();
		return var2;
	}

	public boolean hasEffect(ItemStack par1ItemStack) {
		return par1ItemStack.isItemEnchanted();
	}

	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return par1ItemStack.isItemEnchanted()?EnumRarity.rare:EnumRarity.common;
	}

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
		Vec3D var13 = Vec3D.createVector(var7, var9, var11);
		float var14 = MathHelper.cos(-var6 * 0.017453292F - 3.1415927F);
		float var15 = MathHelper.sin(-var6 * 0.017453292F - 3.1415927F);
		float var16 = -MathHelper.cos(-var5 * 0.017453292F);
		float var17 = MathHelper.sin(-var5 * 0.017453292F);
		float var18 = var15 * var16;
		float var20 = var14 * var16;
		double var21 = 5.0D;
		Vec3D var23 = var13.addVector((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
		MovingObjectPosition var24 = par1World.rayTraceBlocks_do_do(var13, var23, par3, !par3);
		return var24;
	}

	public int getItemEnchantability() {
		return 0;
	}

	public boolean func_46058_c() {
		return false;
	}

	public int func_46057_a(int par1, int par2) {
		return this.getIconFromDamage(par1);
	}

	static {
		StatList.initStats();
	}
}
