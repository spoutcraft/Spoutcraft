package org.spoutcraft.spoutcraftapi.material;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.spoutcraft.spoutcraftapi.material.block.Air;
import org.spoutcraft.spoutcraftapi.material.block.DoubleSlabs;
import org.spoutcraft.spoutcraftapi.material.block.GenericLiquid;
import org.spoutcraft.spoutcraftapi.material.block.Grass;
import org.spoutcraft.spoutcraftapi.material.block.LongGrass;
import org.spoutcraft.spoutcraftapi.material.block.Sapling;
import org.spoutcraft.spoutcraftapi.material.block.Slab;
import org.spoutcraft.spoutcraftapi.material.block.Solid;
import org.spoutcraft.spoutcraftapi.material.block.Tree;
import org.spoutcraft.spoutcraftapi.material.block.Wool;
import org.spoutcraft.spoutcraftapi.material.item.Coal;
import org.spoutcraft.spoutcraftapi.material.item.Dye;
import org.spoutcraft.spoutcraftapi.material.item.GenericArmor;
import org.spoutcraft.spoutcraftapi.material.item.GenericFood;
import org.spoutcraft.spoutcraftapi.material.item.GenericItem;
import org.spoutcraft.spoutcraftapi.material.item.GenericTool;
import org.spoutcraft.spoutcraftapi.material.item.GenericWeapon;
import org.spoutcraft.spoutcraftapi.util.map.TIntPairObjectHashMap;

public class MaterialData {
	private final static TIntPairObjectHashMap<Material> idMap = new TIntPairObjectHashMap<Material>();
	private static boolean initiated = false;
	public static final Block air = new Air();
	public static final Block stone = new Solid(1);
	public static final Block grass = new Grass();
	public static final Block dirt = new Solid(3);
	public static final Block cobblestone = new Solid(4);
	public static final Block wood = new Solid(5);
	public static final Block sapling = new Sapling(0);
	public static final Block spruceSapling = new Sapling(1);
	public static final Block birchSapling = new Sapling(2);
	public static final Block bedrock = new Solid(7);
	public static final Block water = new GenericLiquid(9, true);
	public static final Block stationaryWater = new GenericLiquid(10, false);
	public static final Block lava = new GenericLiquid(11, true);
	public static final Block stationaryLava = new GenericLiquid(12, false);
	public static final Block sand = new Solid(12, true);
	public static final Block gravel = new Solid(13, true);
	public static final Block goldOre = new Solid(14);
	public static final Block ironOre = new Solid(15);
	public static final Block coalOre = new Solid(16);
	public static final Block log = new Tree(17, 0);
	public static final Block spruceLog = new Tree(17, 1);
	public static final Block birchLog = new Tree(17, 2);
	public static final Block leaves = new Tree(18, 0);
	public static final Block spruceLeaves = new Tree(18, 1);
	public static final Block birchLeaves= new Tree(18, 2);
	public static final Block sponge = new Solid(19);
	public static final Block glass = new Solid(20);
	public static final Block lapisOre = new Solid(21);
	public static final Block lapisBlock = new Solid(22);
	public static final Block dispenser = new Solid(23);
	public static final Block sandstone = new Solid(24);
	public static final Block noteblock = new Solid(25);
	public static final Block bedBlock = new Solid(26);
	public static final Block poweredRail = new Solid(27);
	public static final Block detectorRail = new Solid(28);
	public static final Block pistonStickyBase = new Solid(29);
	public static final Block web = new Solid(30);
	public static final Block deadShrub = new LongGrass(31, 0);
	public static final Block tallGrass = new LongGrass(31, 1);
	public static final Block fern = new LongGrass(31, 2);
	public static final Block deadBush = new LongGrass(32, 0);
	public static final Block pistonBase = new Solid(33);
	public static final Block pistonExtension = new Solid(34);
	public static final Block whiteWool = new Wool(35,0);
	public static final Block orangeWool = new Wool(35,1);
	public static final Block magentaWool = new Wool(35,2);
	public static final Block lightBlueWool = new Wool(35,3);
	public static final Block yellowWool = new Wool(35,4);
	public static final Block limeWool = new Wool(35,5);
	public static final Block pinkWool = new Wool(35,6);
	public static final Block greyWool = new Wool(35,7);
	public static final Block lightGreyWool = new Wool(35,8);
	public static final Block cyanWool = new Wool(35,9);
	public static final Block purpleWool = new Wool(35,10);
	public static final Block blueWool = new Wool(35,11);
	public static final Block brownWool = new Wool(35,12);
	public static final Block greenWool = new Wool(35,13);
	public static final Block redWool = new Wool(35,14);
	public static final Block blackWool = new Wool(35,15);
	public static final Block movedByPiston = new Solid(36);
	public static final Block dandelion = new Solid(37);
	public static final Block rose = new Solid(38);
	public static final Block brownMushroom = new Solid(39);
	public static final Block redMushroom = new Solid(40);
	public static final Block goldBlock = new Solid(41);
	public static final Block ironBlock = new Solid(42);
	public static final Block stoneDoubleSlabs = new DoubleSlabs(43,0);
	public static final Block sandstoneDoubleSlabs = new DoubleSlabs(43,1);
	public static final Block woodenDoubleSlabs = new DoubleSlabs(43,2);
	public static final Block cobblestoneDoubleSlabs = new DoubleSlabs(43,3);
	public static final Block brickDoubleSlabs = new DoubleSlabs(43,4);
	public static final Block stoneBrickDoubleSlabs = new DoubleSlabs(43,5);
	public static final Block stoneSlab = new Slab(44,0);
	public static final Block sandstoneSlab = new Slab(44,1);
	public static final Block woodenSlab = new Slab(44,2);
	public static final Block cobblestoneSlab = new Slab(44,3);
	public static final Block brickSlab = new Slab(44,4);
	public static final Block stoneBrickSlab = new Slab(44,5);
	public static final Block brick = new Solid(45);
	public static final Block tnt = new Solid(46);
	public static final Block bookshelf = new Solid(47);
	public static final Block mossStone = new Solid(48);
	public static final Block obsidian = new Solid(49);
	public static final Block torch = new Solid(50);
	public static final Block fire = new Solid(51);
	public static final Block monsterSpawner = new Solid(52);
	public static final Block woodenStairs = new Solid(53);
	public static final Block chest = new Solid(54);
	public static final Block redstoneWire = new Solid(55);
	public static final Block diamondOre = new Solid(56);
	public static final Block diamondBlock = new Solid(57);
	public static final Block craftingTable = new Solid(58);
	public static final Block crops = new Solid(59);
	public static final Block farmland = new Solid(60);
	public static final Block furnace = new Solid(61);
	public static final Block burningfurnace = new Solid(62);
	public static final Block signPost = new Solid(63);
	public static final Block woodenDoorBlock = new Solid(64);
	public static final Block ladders = new Solid(65);
	public static final Block rails = new Solid(66);
	public static final Block cobblestoneStairs = new Solid(67);
	public static final Block wallSign = new Solid(68);
	public static final Block lever = new Solid(69);
	public static final Block stonePressurePlate = new Solid(70);
	public static final Block ironDoorBlock = new Solid(71);
	public static final Block woodenPressurePlate = new Solid(72);
	public static final Block redstoneOre = new Solid(73);
	public static final Block glowingRedstoneOre = new Solid(74);
	public static final Block redstoneTorchOff = new Solid(75);
	public static final Block redstoneTorchOn = new Solid(76);
	public static final Block stoneButton = new Solid(77);
	public static final Block snow = new Solid(78);
	public static final Block ice = new Solid(79);
	public static final Block snowBlock = new Solid(80);
	public static final Block cactus = new Solid(81);
	public static final Block clayBlock = new Solid(82);
	public static final Block sugarCaneBlock = new Solid(83);
	public static final Block jukebox = new Solid(84);
	public static final Block fence = new Solid(85);
	public static final Block pumpkin = new Solid(86);
	public static final Block netherrack = new Solid(87);
	public static final Block soulSand = new Solid(88);
	public static final Block glowstoneBlock = new Solid(89);
	public static final Block portal = new Solid(90);
	public static final Block jackOLantern = new Solid(91);
	public static final Block cakeBlock = new Solid(92);
	public static final Block redstoneRepeaterOff = new Solid(93);
	public static final Block redstoneRepeaterOn = new Solid(94);
	public static final Block lockedChest = new Solid(95);
	public static final Block trapdoor = new Solid(96);
	public static final Block silverfishStone = new Solid(97);
	public static final Block stoneBricks = new Solid(98);
	public static final Block hugeRedMushroom = new Solid(99);
	public static final Block hugeBrownMushroom = new Solid(100);
	public static final Block ironBars = new Solid(101);
	public static final Block glassPane = new Solid(102);
	public static final Block watermelon = new Solid(103);
	public static final Block pumpkinStem = new Solid(104);
	public static final Block melonStem = new Solid(105);
	public static final Block vines = new Solid(106);
	public static final Block fenceGate = new Solid(107);
	public static final Block brickStairs = new Solid(108);
	public static final Block stoneBrickStairs = new Solid(109);
	
	public static final Item ironShovel = new GenericTool(256);
	public static final Item ironPickaxe = new GenericTool(257);
	public static final Item ironAxe = new GenericTool(258);
	public static final Item flintAndSteel = new GenericTool(259);
	public static final Item redApple = new GenericFood(260);
	public static final Item bow = new GenericWeapon(261);
	public static final Item arrow = new GenericItem(262);
	public static final Item coal = new Coal(263,0);
	public static final Item charcoal = new Coal(263,1);
	public static final Item diamond = new GenericItem(264);
	public static final Item ironIngot = new GenericItem(265);
	public static final Item goldIngot = new GenericItem(266);
	public static final Item ironSword = new GenericWeapon(267);
	public static final Item woodenSword = new GenericWeapon(268);
	public static final Item woodenShovel = new GenericTool(269);
	public static final Item woodenPickaxe = new GenericTool(270);
	public static final Item woodenAxe = new GenericTool(271);
	public static final Item stoneSword = new GenericWeapon(272);
	public static final Item stoneShovel = new GenericTool(273);
	public static final Item stonePickaxe = new GenericTool(274);
	public static final Item stoneAxe = new GenericTool(275);
	public static final Item diamondSword = new GenericWeapon(276);
	public static final Item diamondShovel = new GenericTool(277);
	public static final Item diamondPickaxe = new GenericTool(278);
	public static final Item diamondAxe = new GenericTool(279);
	public static final Item stick = new GenericItem(280);
	public static final Item bowl = new GenericItem(281);
	public static final Item mushroomSoup = new GenericFood(282);
	public static final Item goldSword = new GenericWeapon(283);
	public static final Item goldShovel = new GenericTool(284);
	public static final Item goldPickaxe = new GenericTool(285);
	public static final Item goldAxe = new GenericTool(286);
	public static final Item string = new GenericItem(287);
	public static final Item feather = new GenericItem(288);
	public static final Item gunpowder = new GenericItem(289);
	public static final Item woodenHoe = new GenericTool(290);
	public static final Item stoneHoe = new GenericTool(291);
	public static final Item ironHoe = new GenericTool(292);
	public static final Item diamondHoe = new GenericTool(293);
	public static final Item goldHoe = new GenericTool(294);
	public static final Item seeds = new GenericItem(295);
	public static final Item wheat = new GenericItem(296);
	public static final Item bread = new GenericFood(297);
	public static final Item leatherCap = new GenericArmor(298);
	public static final Item leatherTunic = new GenericArmor(299);
	public static final Item leatherPants = new GenericArmor(300);
	public static final Item leatherBoots = new GenericArmor(301);
	public static final Item chainHelmet = new GenericArmor(302);
	public static final Item chainChestplate = new GenericArmor(303);
	public static final Item chainLeggings = new GenericArmor(304);
	public static final Item chainBoots = new GenericArmor(305);
	public static final Item ironHelmet = new GenericArmor(306);
	public static final Item ironChestplate = new GenericArmor(307);
	public static final Item ironLeggings = new GenericArmor(308);
	public static final Item ironBoots = new GenericArmor(309);
	public static final Item diamondHelmet = new GenericArmor(310);
	public static final Item diamondChestplate = new GenericArmor(311);
	public static final Item diamondLeggings = new GenericArmor(312);
	public static final Item diamondBoots = new GenericArmor(313);
	public static final Item goldHelmet = new GenericArmor(314);
	public static final Item goldChestplate = new GenericArmor(315);
	public static final Item goldLeggings = new GenericArmor(316);
	public static final Item goldBoots = new GenericArmor(317);
	public static final Item flint = new GenericItem(318);
	public static final Item rawPorkchop = new GenericFood(319);
	public static final Item cookedPorkchop = new GenericFood(320);
	public static final Item paintings = new GenericItem(321);
	public static final Item goldenApple = new GenericFood(322);
	public static final Item sign = new GenericItem(323);
	public static final Item woodenDoor = new GenericItem(324);
	public static final Item bucket = new GenericItem(325);
	public static final Item waterBucket = new GenericItem(326);
	public static final Item lavaBucket = new GenericItem(327);
	public static final Item minecart = new GenericItem(328);
	public static final Item saddle = new GenericItem(329);
	public static final Item ironDoor = new GenericItem(330);
	public static final Item redstone = new GenericItem(331);
	public static final Item snowball = new GenericItem(332);
	public static final Item boat = new GenericItem(333);
	public static final Item leather = new GenericItem(334);
	public static final Item milk = new GenericItem(335);
	public static final Item clayBrick = new GenericItem(336);
	public static final Item clay = new GenericItem(337);
	public static final Item sugarCane = new GenericItem(338);
	public static final Item paper = new GenericItem(339);
	public static final Item book = new GenericItem(340);
	public static final Item slimeball = new GenericItem(341);
	public static final Item minecartChest = new GenericItem(342);
	public static final Item minecartFurnace = new GenericItem(343);
	public static final Item egg = new GenericItem(344);
	public static final Item compass = new GenericItem(345);
	public static final Item fishingRod = new GenericTool(346);
	public static final Item clock = new GenericItem(347);
	public static final Item glowstoneDust = new GenericItem(348);
	public static final Item rawFish = new GenericFood(349);
	public static final Item cookedFish = new GenericFood(350);
	public static final Item inkSac = new Dye(351,0);
	public static final Item roseRed = new Dye(351,1);
	public static final Item cactusGreen = new Dye(351,2);
	public static final Item cocoaBeans = new Dye(351,3);
	public static final Item lapisLazuli = new Dye(351,4);
	public static final Item purpleDye = new Dye(351,5);
	public static final Item cyanDye = new Dye(351,6);
	public static final Item lightGrayDye = new Dye(351,7);
	public static final Item grayDye = new Dye(351,8);
	public static final Item pinkDye = new Dye(351,9);
	public static final Item limeDye = new Dye(351,10);
	public static final Item dandelionYellow = new Dye(351,11);
	public static final Item lightBlueDye = new Dye(351,12);
	public static final Item magentaDye = new Dye(351,13);
	public static final Item orangeDye = new Dye(351,14);
	public static final Item boneMeal = new Dye(351,15);
	public static final Item bone = new GenericItem(352);
	public static final Item sugar = new GenericItem(353);
	public static final Item cake = new GenericFood(354);
	public static final Item bed = new GenericItem(355);
	public static final Item redstoneRepeater = new GenericItem(356);
	public static final Item cookie = new GenericFood(357);
	public static final Item map = new GenericItem(358);
	public static final Item shears = new GenericTool(359);
	public static final Item melonSlice = new GenericFood(360);
	public static final Item pumpkinSeeds = new GenericItem(361);
	public static final Item melonSeeds = new GenericItem(362);
	public static final Item rawBeef = new GenericFood(363);
	public static final Item steak = new GenericFood(364);
	public static final Item rawChicken = new GenericFood(365);
	public static final Item cookedChicken = new GenericFood(366);
	public static final Item rottenFlesh = new GenericFood(367);
	public static final Item enderPearl = new GenericItem(368);
	public static final Item goldMusicDisc = new GenericItem(2256);
	public static final Item greenMusicDisc = new GenericItem(2257);
	
	private static void init() {
		if (!initiated) {
			Field[] fields = MaterialData.class.getFields();
			for (Field f : fields) {
				f.setAccessible(true);
				if (Modifier.isStatic(f.getModifiers())) {
					try {
						Object value = f.get(null);
						if (value instanceof Material) {
							Material mat = (Material)value;
							idMap.put(mat.getRawId(), mat.getRawData(), mat);
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			initiated = true;
		}
	}
	
	public static Material getMaterial(int id) {
		return getMaterial(id, (short)0);
	}
	
	public static Material getMaterial(int id, short data) {
		init();
		Material mat = (Material) idMap.get(id, 0); //Test if they id has subtypes first
		if (mat == null || !mat.hasSubtypes()) {
			return mat;
		}
		return (Material) idMap.get(id, data);
	}
	
	public static Block getBlock(int id) {
		return getBlock(id, (short)0);
	}
	
	public static Block getBlock(int id, short data) {
		Material mat = getMaterial(id, data);
		if (mat instanceof Block) {
			return (Block)mat;
		}
		return null;
	}
	
	public static Item getItem(int id) {
		return getItem(id, (short)0);
	}
	
	public static Item getItem(int id, short data) {
		Material mat = getMaterial(id, data);
		if (mat instanceof Item) {
			return (Item)mat;
		}
		return null;
	}

}
