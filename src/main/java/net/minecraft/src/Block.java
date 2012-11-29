package net.minecraft.src;

import gnu.trove.map.hash.TIntFloatHashMap; // Spout

import java.util.ArrayList; // Spout
import java.util.List;
import java.util.Random;

// Spout start
import org.spoutcraft.client.block.SpoutcraftChunk;
import org.spoutcraft.api.entity.ActivePlayer;
import org.spoutcraft.api.material.CustomBlock;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.api.util.FastLocation;
import org.spoutcraft.api.util.FixedLocation;
// Spout end

public class Block {

	/**
	 * used as foreach item, if item.tab = current tab, display it on the screen
	 */
	private CreativeTabs displayOnCreativeTab;
	public static final StepSound soundPowderFootstep = new StepSound("stone", 1.0F, 1.0F);
	public static final StepSound soundWoodFootstep = new StepSound("wood", 1.0F, 1.0F);
	public static final StepSound soundGravelFootstep = new StepSound("gravel", 1.0F, 1.0F);
	public static final StepSound soundGrassFootstep = new StepSound("grass", 1.0F, 1.0F);
	public static final StepSound soundStoneFootstep = new StepSound("stone", 1.0F, 1.0F);
	public static final StepSound soundMetalFootstep = new StepSound("stone", 1.0F, 1.5F);
	public static final StepSound soundGlassFootstep = new StepSoundStone("stone", 1.0F, 1.0F);
	public static final StepSound soundClothFootstep = new StepSound("cloth", 1.0F, 1.0F);
	public static final StepSound soundSandFootstep = new StepSound("sand", 1.0F, 1.0F);
	public static final StepSound soundSnowFootstep = new StepSound("snow", 1.0F, 1.0F);
	public static final StepSound soundLadderFootstep = new StepSoundSand("ladder", 1.0F, 1.0F);
	public static final StepSound soundAnvilFootstep = new StepSoundAnvil("anvil", 0.3F, 1.0F);

	/** List of ly/ff (BlockType) containing the already registered blocks. */
	public static final Block[] blocksList = new Block[4096];

	/**
	 * An array of 4096 booleans corresponding to the result of the isOpaqueCube() method for each block ID
	 */
	public static final boolean[] opaqueCubeLookup = new boolean[4096];

	/** How much light is subtracted for going through this block */
	public static final int[] lightOpacity = new int[4096];

	/** Array of booleans that tells if a block can grass */
	public static final boolean[] canBlockGrass = new boolean[4096];

	/** Amount of light emitted */
	public static final int[] lightValue = new int[4096];
	public static final boolean[] requiresSelfNotify = new boolean[4096];

	/**
	 * Flag if block ID should use the brightest neighbor light value as its own
	 */
	public static boolean[] useNeighborBrightness = new boolean[4096];
	public static final Block stone = (new BlockStone(1, 1)).setHardness(1.5F).setResistance(10.0F).setStepSound(soundStoneFootstep).setBlockName("stone");
	public static final BlockGrass grass = (BlockGrass)(new BlockGrass(2)).setHardness(0.6F).setStepSound(soundGrassFootstep).setBlockName("grass");
	public static final Block dirt = (new BlockDirt(3, 2)).setHardness(0.5F).setStepSound(soundGravelFootstep).setBlockName("dirt");
	public static final Block cobblestone = (new Block(4, 16, Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setBlockName("stonebrick").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block planks = (new BlockWood(5)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep).setBlockName("wood").setRequiresSelfNotify();
	public static final Block sapling = (new BlockSapling(6, 15)).setHardness(0.0F).setStepSound(soundGrassFootstep).setBlockName("sapling").setRequiresSelfNotify();
	public static final Block bedrock = (new Block(7, 17, Material.rock)).setBlockUnbreakable().setResistance(6000000.0F).setStepSound(soundStoneFootstep).setBlockName("bedrock").disableStats().setCreativeTab(CreativeTabs.tabBlock);
	public static final Block waterMoving = (new BlockFlowing(8, Material.water)).setHardness(100.0F).setLightOpacity(3).setBlockName("water").disableStats().setRequiresSelfNotify();
	public static final Block waterStill = (new BlockStationary(9, Material.water)).setHardness(100.0F).setLightOpacity(3).setBlockName("water").disableStats().setRequiresSelfNotify();
	public static final Block lavaMoving = (new BlockFlowing(10, Material.lava)).setHardness(0.0F).setLightValue(1.0F).setLightOpacity(255).setBlockName("lava").disableStats().setRequiresSelfNotify();

	/** Stationary lava source block */
	public static final Block lavaStill = (new BlockStationary(11, Material.lava)).setHardness(100.0F).setLightValue(1.0F).setLightOpacity(255).setBlockName("lava").disableStats().setRequiresSelfNotify();
	public static final Block sand = (new BlockSand(12, 18)).setHardness(0.5F).setStepSound(soundSandFootstep).setBlockName("sand");
	public static final Block gravel = (new BlockGravel(13, 19)).setHardness(0.6F).setStepSound(soundGravelFootstep).setBlockName("gravel");
	public static final Block oreGold = (new BlockOre(14, 32)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setBlockName("oreGold");
	public static final Block oreIron = (new BlockOre(15, 33)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setBlockName("oreIron");
	public static final Block oreCoal = (new BlockOre(16, 34)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setBlockName("oreCoal");
	public static final Block wood = (new BlockLog(17)).setHardness(2.0F).setStepSound(soundWoodFootstep).setBlockName("log").setRequiresSelfNotify();
	public static final BlockLeaves leaves = (BlockLeaves)(new BlockLeaves(18, 52)).setHardness(0.2F).setLightOpacity(1).setStepSound(soundGrassFootstep).setBlockName("leaves").setRequiresSelfNotify();
	public static final Block sponge = (new BlockSponge(19)).setHardness(0.6F).setStepSound(soundGrassFootstep).setBlockName("sponge");
	public static final Block glass = (new BlockGlass(20, 49, Material.glass, false)).setHardness(0.3F).setStepSound(soundGlassFootstep).setBlockName("glass");
	public static final Block oreLapis = (new BlockOre(21, 160)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setBlockName("oreLapis");
	public static final Block blockLapis = (new Block(22, 144, Material.rock)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setBlockName("blockLapis").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block dispenser = (new BlockDispenser(23)).setHardness(3.5F).setStepSound(soundStoneFootstep).setBlockName("dispenser").setRequiresSelfNotify();
	public static final Block sandStone = (new BlockSandStone(24)).setStepSound(soundStoneFootstep).setHardness(0.8F).setBlockName("sandStone").setRequiresSelfNotify();
	public static final Block music = (new BlockNote(25)).setHardness(0.8F).setBlockName("musicBlock").setRequiresSelfNotify();
	public static final Block bed = (new BlockBed(26)).setHardness(0.2F).setBlockName("bed").disableStats().setRequiresSelfNotify();
	public static final Block railPowered = (new BlockRail(27, 179, true)).setHardness(0.7F).setStepSound(soundMetalFootstep).setBlockName("goldenRail").setRequiresSelfNotify();
	public static final Block railDetector = (new BlockDetectorRail(28, 195)).setHardness(0.7F).setStepSound(soundMetalFootstep).setBlockName("detectorRail").setRequiresSelfNotify();
	public static final Block pistonStickyBase = (new BlockPistonBase(29, 106, true)).setBlockName("pistonStickyBase").setRequiresSelfNotify();
	public static final Block web = (new BlockWeb(30, 11)).setLightOpacity(1).setHardness(4.0F).setBlockName("web");
	public static final BlockTallGrass tallGrass = (BlockTallGrass)(new BlockTallGrass(31, 39)).setHardness(0.0F).setStepSound(soundGrassFootstep).setBlockName("tallgrass");
	public static final BlockDeadBush deadBush = (BlockDeadBush)(new BlockDeadBush(32, 55)).setHardness(0.0F).setStepSound(soundGrassFootstep).setBlockName("deadbush");
	public static final Block pistonBase = (new BlockPistonBase(33, 107, false)).setBlockName("pistonBase").setRequiresSelfNotify();
	public static final BlockPistonExtension pistonExtension = (BlockPistonExtension)(new BlockPistonExtension(34, 107)).setRequiresSelfNotify();
	public static final Block cloth = (new BlockCloth()).setHardness(0.8F).setStepSound(soundClothFootstep).setBlockName("cloth").setRequiresSelfNotify();
	public static final BlockPistonMoving pistonMoving = new BlockPistonMoving(36);
	public static final BlockFlower plantYellow = (BlockFlower)(new BlockFlower(37, 13)).setHardness(0.0F).setStepSound(soundGrassFootstep).setBlockName("flower");
	public static final BlockFlower plantRed = (BlockFlower)(new BlockFlower(38, 12)).setHardness(0.0F).setStepSound(soundGrassFootstep).setBlockName("rose");
	public static final BlockFlower mushroomBrown = (BlockFlower)(new BlockMushroom(39, 29)).setHardness(0.0F).setStepSound(soundGrassFootstep).setLightValue(0.125F).setBlockName("mushroom");
	public static final BlockFlower mushroomRed = (BlockFlower)(new BlockMushroom(40, 28)).setHardness(0.0F).setStepSound(soundGrassFootstep).setBlockName("mushroom");
	public static final Block blockGold = (new BlockOreStorage(41, 23)).setHardness(3.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setBlockName("blockGold");
	public static final Block blockSteel = (new BlockOreStorage(42, 22)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setBlockName("blockIron");

	/** stoneDoubleSlab */
	public static final BlockHalfSlab stoneDoubleSlab = (BlockHalfSlab)(new BlockStep(43, true)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setBlockName("stoneSlab");

	/** stoneSingleSlab */
	public static final BlockHalfSlab stoneSingleSlab = (BlockHalfSlab)(new BlockStep(44, false)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setBlockName("stoneSlab");
	public static final Block brick = (new Block(45, 7, Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setBlockName("brick").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block tnt = (new BlockTNT(46, 8)).setHardness(0.0F).setStepSound(soundGrassFootstep).setBlockName("tnt");
	public static final Block bookShelf = (new BlockBookshelf(47, 35)).setHardness(1.5F).setStepSound(soundWoodFootstep).setBlockName("bookshelf");
	public static final Block cobblestoneMossy = (new Block(48, 36, Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setBlockName("stoneMoss").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block obsidian = (new BlockObsidian(49, 37)).setHardness(50.0F).setResistance(2000.0F).setStepSound(soundStoneFootstep).setBlockName("obsidian");
	public static final Block torchWood = (new BlockTorch(50, 80)).setHardness(0.0F).setLightValue(0.9375F).setStepSound(soundWoodFootstep).setBlockName("torch").setRequiresSelfNotify();
	public static final BlockFire fire = (BlockFire)(new BlockFire(51, 31)).setHardness(0.0F).setLightValue(1.0F).setStepSound(soundWoodFootstep).setBlockName("fire").disableStats();
	public static final Block mobSpawner = (new BlockMobSpawner(52, 65)).setHardness(5.0F).setStepSound(soundMetalFootstep).setBlockName("mobSpawner").disableStats();
	public static final Block stairCompactPlanks = (new BlockStairs(53, planks, 0)).setBlockName("stairsWood").setRequiresSelfNotify();
	public static final Block chest = (new BlockChest(54)).setHardness(2.5F).setStepSound(soundWoodFootstep).setBlockName("chest").setRequiresSelfNotify();
	public static final Block redstoneWire = (new BlockRedstoneWire(55, 164)).setHardness(0.0F).setStepSound(soundPowderFootstep).setBlockName("redstoneDust").disableStats().setRequiresSelfNotify();
	public static final Block oreDiamond = (new BlockOre(56, 50)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setBlockName("oreDiamond");
	public static final Block blockDiamond = (new BlockOreStorage(57, 24)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setBlockName("blockDiamond");
	public static final Block workbench = (new BlockWorkbench(58)).setHardness(2.5F).setStepSound(soundWoodFootstep).setBlockName("workbench");
	public static final Block crops = (new BlockCrops(59, 88)).setBlockName("crops");
	public static final Block tilledField = (new BlockFarmland(60)).setHardness(0.6F).setStepSound(soundGravelFootstep).setBlockName("farmland").setRequiresSelfNotify();
	public static final Block stoneOvenIdle = (new BlockFurnace(61, false)).setHardness(3.5F).setStepSound(soundStoneFootstep).setBlockName("furnace").setRequiresSelfNotify().setCreativeTab(CreativeTabs.tabDecorations);
	public static final Block stoneOvenActive = (new BlockFurnace(62, true)).setHardness(3.5F).setStepSound(soundStoneFootstep).setLightValue(0.875F).setBlockName("furnace").setRequiresSelfNotify();
	public static final Block signPost = (new BlockSign(63, TileEntitySign.class, true)).setHardness(1.0F).setStepSound(soundWoodFootstep).setBlockName("sign").disableStats().setRequiresSelfNotify();
	public static final Block doorWood = (new BlockDoor(64, Material.wood)).setHardness(3.0F).setStepSound(soundWoodFootstep).setBlockName("doorWood").disableStats().setRequiresSelfNotify();
	public static final Block ladder = (new BlockLadder(65, 83)).setHardness(0.4F).setStepSound(soundLadderFootstep).setBlockName("ladder").setRequiresSelfNotify();
	public static final Block rail = (new BlockRail(66, 128, false)).setHardness(0.7F).setStepSound(soundMetalFootstep).setBlockName("rail").setRequiresSelfNotify();
	public static final Block stairCompactCobblestone = (new BlockStairs(67, cobblestone, 0)).setBlockName("stairsStone").setRequiresSelfNotify();
	public static final Block signWall = (new BlockSign(68, TileEntitySign.class, false)).setHardness(1.0F).setStepSound(soundWoodFootstep).setBlockName("sign").disableStats().setRequiresSelfNotify();
	public static final Block lever = (new BlockLever(69, 96)).setHardness(0.5F).setStepSound(soundWoodFootstep).setBlockName("lever").setRequiresSelfNotify();
	public static final Block pressurePlateStone = (new BlockPressurePlate(70, stone.blockIndexInTexture, EnumMobType.mobs, Material.rock)).setHardness(0.5F).setStepSound(soundStoneFootstep).setBlockName("pressurePlate").setRequiresSelfNotify();
	public static final Block doorSteel = (new BlockDoor(71, Material.iron)).setHardness(5.0F).setStepSound(soundMetalFootstep).setBlockName("doorIron").disableStats().setRequiresSelfNotify();
	public static final Block pressurePlatePlanks = (new BlockPressurePlate(72, planks.blockIndexInTexture, EnumMobType.everything, Material.wood)).setHardness(0.5F).setStepSound(soundWoodFootstep).setBlockName("pressurePlate").setRequiresSelfNotify();
	public static final Block oreRedstone = (new BlockRedstoneOre(73, 51, false)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setBlockName("oreRedstone").setRequiresSelfNotify().setCreativeTab(CreativeTabs.tabBlock);
	public static final Block oreRedstoneGlowing = (new BlockRedstoneOre(74, 51, true)).setLightValue(0.625F).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setBlockName("oreRedstone").setRequiresSelfNotify();
	public static final Block torchRedstoneIdle = (new BlockRedstoneTorch(75, 115, false)).setHardness(0.0F).setStepSound(soundWoodFootstep).setBlockName("notGate").setRequiresSelfNotify();
	public static final Block torchRedstoneActive = (new BlockRedstoneTorch(76, 99, true)).setHardness(0.0F).setLightValue(0.5F).setStepSound(soundWoodFootstep).setBlockName("notGate").setRequiresSelfNotify().setCreativeTab(CreativeTabs.tabRedstone);
	public static final Block stoneButton = (new BlockButton(77, stone.blockIndexInTexture, false)).setHardness(0.5F).setStepSound(soundStoneFootstep).setBlockName("button").setRequiresSelfNotify();
	public static final Block snow = (new BlockSnow(78, 66)).setHardness(0.1F).setStepSound(soundSnowFootstep).setBlockName("snow").setRequiresSelfNotify().setLightOpacity(0);
	public static final Block ice = (new BlockIce(79, 67)).setHardness(0.5F).setLightOpacity(3).setStepSound(soundGlassFootstep).setBlockName("ice");
	public static final Block blockSnow = (new BlockSnowBlock(80, 66)).setHardness(0.2F).setStepSound(soundSnowFootstep).setBlockName("snow");
	public static final Block cactus = (new BlockCactus(81, 70)).setHardness(0.4F).setStepSound(soundClothFootstep).setBlockName("cactus");
	public static final Block blockClay = (new BlockClay(82, 72)).setHardness(0.6F).setStepSound(soundGravelFootstep).setBlockName("clay");
	public static final Block reed = (new BlockReed(83, 73)).setHardness(0.0F).setStepSound(soundGrassFootstep).setBlockName("reeds").disableStats();
	public static final Block jukebox = (new BlockJukeBox(84, 74)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setBlockName("jukebox").setRequiresSelfNotify();
	public static final Block fence = (new BlockFence(85, 4)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep).setBlockName("fence");
	public static final Block pumpkin = (new BlockPumpkin(86, 102, false)).setHardness(1.0F).setStepSound(soundWoodFootstep).setBlockName("pumpkin").setRequiresSelfNotify();
	public static final Block netherrack = (new BlockNetherrack(87, 103)).setHardness(0.4F).setStepSound(soundStoneFootstep).setBlockName("hellrock");
	public static final Block slowSand = (new BlockSoulSand(88, 104)).setHardness(0.5F).setStepSound(soundSandFootstep).setBlockName("hellsand");
	public static final Block glowStone = (new BlockGlowStone(89, 105, Material.glass)).setHardness(0.3F).setStepSound(soundGlassFootstep).setLightValue(1.0F).setBlockName("lightgem");

	/** The purple teleport blocks inside the obsidian circle */
	public static final BlockPortal portal = (BlockPortal)(new BlockPortal(90, 14)).setHardness(-1.0F).setStepSound(soundGlassFootstep).setLightValue(0.75F).setBlockName("portal");
	public static final Block pumpkinLantern = (new BlockPumpkin(91, 102, true)).setHardness(1.0F).setStepSound(soundWoodFootstep).setLightValue(1.0F).setBlockName("litpumpkin").setRequiresSelfNotify();
	public static final Block cake = (new BlockCake(92, 121)).setHardness(0.5F).setStepSound(soundClothFootstep).setBlockName("cake").disableStats().setRequiresSelfNotify();
	public static final Block redstoneRepeaterIdle = (new BlockRedstoneRepeater(93, false)).setHardness(0.0F).setStepSound(soundWoodFootstep).setBlockName("diode").disableStats().setRequiresSelfNotify();
	public static final Block redstoneRepeaterActive = (new BlockRedstoneRepeater(94, true)).setHardness(0.0F).setLightValue(0.625F).setStepSound(soundWoodFootstep).setBlockName("diode").disableStats().setRequiresSelfNotify();

	/**
	 * April fools secret locked chest, only spawns on new chunks on 1st April.
	 */
	public static final Block lockedChest = (new BlockLockedChest(95)).setHardness(0.0F).setLightValue(1.0F).setStepSound(soundWoodFootstep).setBlockName("lockedchest").setTickRandomly(true).setRequiresSelfNotify();
	public static final Block trapdoor = (new BlockTrapDoor(96, Material.wood)).setHardness(3.0F).setStepSound(soundWoodFootstep).setBlockName("trapdoor").disableStats().setRequiresSelfNotify();
	public static final Block silverfish = (new BlockSilverfish(97)).setHardness(0.75F).setBlockName("monsterStoneEgg");
	public static final Block stoneBrick = (new BlockStoneBrick(98)).setHardness(1.5F).setResistance(10.0F).setStepSound(soundStoneFootstep).setBlockName("stonebricksmooth");
	public static final Block mushroomCapBrown = (new BlockMushroomCap(99, Material.wood, 142, 0)).setHardness(0.2F).setStepSound(soundWoodFootstep).setBlockName("mushroom").setRequiresSelfNotify();
	public static final Block mushroomCapRed = (new BlockMushroomCap(100, Material.wood, 142, 1)).setHardness(0.2F).setStepSound(soundWoodFootstep).setBlockName("mushroom").setRequiresSelfNotify();
	public static final Block fenceIron = (new BlockPane(101, 85, 85, Material.iron, true)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setBlockName("fenceIron");
	public static final Block thinGlass = (new BlockPane(102, 49, 148, Material.glass, false)).setHardness(0.3F).setStepSound(soundGlassFootstep).setBlockName("thinGlass");
	public static final Block melon = (new BlockMelon(103)).setHardness(1.0F).setStepSound(soundWoodFootstep).setBlockName("melon");
	public static final Block pumpkinStem = (new BlockStem(104, pumpkin)).setHardness(0.0F).setStepSound(soundWoodFootstep).setBlockName("pumpkinStem").setRequiresSelfNotify();
	public static final Block melonStem = (new BlockStem(105, melon)).setHardness(0.0F).setStepSound(soundWoodFootstep).setBlockName("pumpkinStem").setRequiresSelfNotify();
	public static final Block vine = (new BlockVine(106)).setHardness(0.2F).setStepSound(soundGrassFootstep).setBlockName("vine").setRequiresSelfNotify();
	public static final Block fenceGate = (new BlockFenceGate(107, 4)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep).setBlockName("fenceGate").setRequiresSelfNotify();
	public static final Block stairsBrick = (new BlockStairs(108, brick, 0)).setBlockName("stairsBrick").setRequiresSelfNotify();
	public static final Block stairsStoneBrickSmooth = (new BlockStairs(109, stoneBrick, 0)).setBlockName("stairsStoneBrickSmooth").setRequiresSelfNotify();
	public static final BlockMycelium mycelium = (BlockMycelium)(new BlockMycelium(110)).setHardness(0.6F).setStepSound(soundGrassFootstep).setBlockName("mycel");
	public static final Block waterlily = (new BlockLilyPad(111, 76)).setHardness(0.0F).setStepSound(soundGrassFootstep).setBlockName("waterlily");
	public static final Block netherBrick = (new Block(112, 224, Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setBlockName("netherBrick").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block netherFence = (new BlockFence(113, 224, Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setBlockName("netherFence");
	public static final Block stairsNetherBrick = (new BlockStairs(114, netherBrick, 0)).setBlockName("stairsNetherBrick").setRequiresSelfNotify();
	public static final Block netherStalk = (new BlockNetherStalk(115)).setBlockName("netherStalk").setRequiresSelfNotify();
	public static final Block enchantmentTable = (new BlockEnchantmentTable(116)).setHardness(5.0F).setResistance(2000.0F).setBlockName("enchantmentTable");
	public static final Block brewingStand = (new BlockBrewingStand(117)).setHardness(0.5F).setLightValue(0.125F).setBlockName("brewingStand").setRequiresSelfNotify();
	public static final Block cauldron = (new BlockCauldron(118)).setHardness(2.0F).setBlockName("cauldron").setRequiresSelfNotify();
	public static final Block endPortal = (new BlockEndPortal(119, Material.portal)).setHardness(-1.0F).setResistance(6000000.0F);
	public static final Block endPortalFrame = (new BlockEndPortalFrame(120)).setStepSound(soundGlassFootstep).setLightValue(0.125F).setHardness(-1.0F).setBlockName("endPortalFrame").setRequiresSelfNotify().setResistance(6000000.0F).setCreativeTab(CreativeTabs.tabDecorations);
	public static final Block whiteStone = (new Block(121, 175, Material.rock)).setHardness(3.0F).setResistance(15.0F).setStepSound(soundStoneFootstep).setBlockName("whiteStone").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block dragonEgg = (new BlockDragonEgg(122, 167)).setHardness(3.0F).setResistance(15.0F).setStepSound(soundStoneFootstep).setLightValue(0.125F).setBlockName("dragonEgg");
	public static final Block redstoneLampIdle = (new BlockRedstoneLight(123, false)).setHardness(0.3F).setStepSound(soundGlassFootstep).setBlockName("redstoneLight").setCreativeTab(CreativeTabs.tabRedstone);
	public static final Block redstoneLampActive = (new BlockRedstoneLight(124, true)).setHardness(0.3F).setStepSound(soundGlassFootstep).setBlockName("redstoneLight");
	public static final BlockHalfSlab woodDoubleSlab = (BlockHalfSlab)(new BlockWoodSlab(125, true)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep).setBlockName("woodSlab");
	public static final BlockHalfSlab woodSingleSlab = (BlockHalfSlab)(new BlockWoodSlab(126, false)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep).setBlockName("woodSlab");
	public static final Block cocoaPlant = (new BlockCocoa(127)).setHardness(0.2F).setResistance(5.0F).setStepSound(soundWoodFootstep).setBlockName("cocoa").setRequiresSelfNotify();
	public static final Block stairsSandStone = (new BlockStairs(128, sandStone, 0)).setBlockName("stairsSandStone").setRequiresSelfNotify();
	public static final Block oreEmerald = (new BlockOre(129, 171)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setBlockName("oreEmerald");
	public static final Block enderChest = (new BlockEnderChest(130)).setHardness(22.5F).setResistance(1000.0F).setStepSound(soundStoneFootstep).setBlockName("enderChest").setRequiresSelfNotify().setLightValue(0.5F);
	public static final BlockTripWireSource tripWireSource = (BlockTripWireSource)(new BlockTripWireSource(131)).setBlockName("tripWireSource").setRequiresSelfNotify();
	public static final Block tripWire = (new BlockTripWire(132)).setBlockName("tripWire").setRequiresSelfNotify();
	public static final Block blockEmerald = (new BlockOreStorage(133, 25)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setBlockName("blockEmerald");
	public static final Block stairsWoodSpruce = (new BlockStairs(134, planks, 1)).setBlockName("stairsWoodSpruce").setRequiresSelfNotify();
	public static final Block stairsWoodBirch = (new BlockStairs(135, planks, 2)).setBlockName("stairsWoodBirch").setRequiresSelfNotify();
	public static final Block stairsWoodJungle = (new BlockStairs(136, planks, 3)).setBlockName("stairsWoodJungle").setRequiresSelfNotify();
	public static final Block commandBlock = (new BlockCommandBlock(137)).setBlockName("commandBlock");
	public static final Block beacon = (new BlockBeacon(138)).setBlockName("beacon").setLightValue(1.0F);
	public static final Block cobblestoneWall = (new BlockWall(139, cobblestone)).setBlockName("cobbleWall");
	public static final Block flowerPot = (new BlockFlowerPot(140)).setHardness(0.0F).setStepSound(soundPowderFootstep).setBlockName("flowerPot");
	public static final Block carrot = (new BlockCarrot(141)).setBlockName("carrots");
	public static final Block potato = (new BlockPotato(142)).setBlockName("potatoes");
	public static final Block woodenButton = (new BlockButton(143, planks.blockIndexInTexture, true)).setHardness(0.5F).setStepSound(soundWoodFootstep).setBlockName("button").setRequiresSelfNotify();
	public static final Block skull = (new BlockSkull(144)).setHardness(1.0F).setStepSound(soundStoneFootstep).setBlockName("skull").setRequiresSelfNotify();
	public static final Block anvil = (new BlockAnvil(145)).setHardness(5.0F).setStepSound(soundAnvilFootstep).setResistance(2000.0F).setBlockName("anvil").setRequiresSelfNotify();

	/**
	 * The index of the texture to be displayed for this block. May vary based on graphics settings. Mostly seems to come
	 * from terrain.png, and the index is 0-based (grass is 0).
	 */
	public int blockIndexInTexture;

	/** ID of the block. */
	public final int blockID;

	/** Indicates how many hits it takes to break a block. */
	public float blockHardness; // Spout protected -> public

	/** Indicates the blocks resistance to explosions. */
	public float blockResistance; // Spout protected -> public

	/**
	 * set to true when Block's constructor is called through the chain of super()'s. Note: Never used
	 */
	protected boolean blockConstructorCalled;

	/**
	 * If this field is true, the block is counted for statistics (mined or placed)
	 */
	protected boolean enableStats;

	/**
	 * Flags whether or not this block is of a type that needs random ticking. Ref-counted by ExtendedBlockStorage in order
	 * to broadly cull a chunk from the random chunk update list for efficiency's sake.
	 */
	protected boolean needsRandomTick;

	/** true if the Block contains a Tile Entity */
	protected boolean isBlockContainer;

	/** minimum X for the block bounds (local coordinates) */
	protected double minX;

	/** minimum Y for the block bounds (local coordinates) */
	protected double minY;

	/** minimum Z for the block bounds (local coordinates) */
	protected double minZ;

	/** maximum X for the block bounds (local coordinates) */
	protected double maxX;

	/** maximum Y for the block bounds (local coordinates) */
	protected double maxY;

	/** maximum Z for the block bounds (local coordinates) */
	protected double maxZ;

	/** Sound of stepping on the block */
	public StepSound stepSound;
	public float blockParticleGravity;

	/** Block material definition. */
	public final Material blockMaterial;

	/**
	 * Determines how much velocity is maintained while moving on top of this block
	 */
	public float slipperiness;
	private String blockName;
	// Spout Start
	public static short[] customIds = null;
	// Spout End

	protected Block(int par1, Material par2Material) {
		this.blockConstructorCalled = true;
		this.enableStats = true;
		this.stepSound = soundPowderFootstep;
		this.blockParticleGravity = 1.0F;
		this.slipperiness = 0.6F;

		if (blocksList[par1] != null) {
			throw new IllegalArgumentException("Slot " + par1 + " is already occupied by " + blocksList[par1] + " when adding " + this);
		} else {
			this.blockMaterial = par2Material;
			blocksList[par1] = this;
			this.blockID = par1;
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			opaqueCubeLookup[par1] = this.isOpaqueCube();
			lightOpacity[par1] = this.isOpaqueCube() ? 255 : 0;
			canBlockGrass[par1] = !par2Material.getCanBlockGrass();
		}
	}

	/**
	 * Blocks with this attribute will not notify all near blocks when it's metadata change. The default behavior is always
	 * notify every neightbor block when anything changes.
	 */
	protected Block setRequiresSelfNotify() {
		requiresSelfNotify[this.blockID] = true;
		return this;
	}

	/**
	 * This method is called on a block after all other blocks gets already created. You can use it to reference and
	 * configure something on the block that needs the others ones.
	 */
	protected void initializeBlock() {}

	protected Block(int par1, int par2, Material par3Material) {
		this(par1, par3Material);
		this.blockIndexInTexture = par2;
	}

	/**
	 * Sets the footstep sound for the block. Returns the object for convenience in constructing.
	 */
	protected Block setStepSound(StepSound par1StepSound) {
		this.stepSound = par1StepSound;
		return this;
	}

	/**
	 * Sets how much light is blocked going through this block. Returns the object for convenience in constructing.
	 */
	public Block setLightOpacity(int par1) { // Spout protected -> public
		lightOpacity[this.blockID] = par1;
		return this;
	}

	/**
	 * Sets the amount of light emitted by a block from 0.0f to 1.0f (converts internally to 0-15). Returns the object for
	 * convenience in constructing.
	 */
	protected Block setLightValue(float par1) {
		lightValue[this.blockID] = (int)(15.0F * par1);
		return this;
	}

	/**
	 * Sets the the blocks resistance to explosions. Returns the object for convenience in constructing.
	 */
	protected Block setResistance(float par1) {
		this.blockResistance = par1 * 3.0F;
		return this;
	}

	public static boolean isNormalCube(int par0) {
		Block var1 = blocksList[par0];
		return var1 == null ? false : var1.blockMaterial.isOpaque() && var1.renderAsNormalBlock();
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return true;
	}

	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		return !this.blockMaterial.blocksMovement();
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 0;
	}

	/**
	 * Sets how many hits it takes to break a block.
	 */
	protected Block setHardness(float par1) {
		this.blockHardness = par1;

		if (this.blockResistance < par1 * 5.0F) {
			this.blockResistance = par1 * 5.0F;
		}

		return this;
	}

	/**
	 * This method will make the hardness of the block equals to -1, and the block is indestructible.
	 */
	protected Block setBlockUnbreakable() {
		this.setHardness(-1.0F);
		return this;
	}

	/**
	 * Returns the block hardness at a location. Args: world, x, y, z
	 */
	public float getBlockHardness(World par1World, int par2, int par3, int par4) {
		return this.blockHardness;
	}

	/**
	 * Sets whether this block type will receive random update ticks
	 */
	protected Block setTickRandomly(boolean par1) {
		this.needsRandomTick = par1;
		return this;
	}

	/**
	 * Returns whether or not this block is of a type that needs random ticking. Called for ref-counting purposes by
	 * ExtendedBlockStorage in order to broadly cull a chunk from the random chunk update list for efficiency's sake.
	 */
	public boolean getTickRandomly() {
		return this.needsRandomTick;
	}

	public boolean hasTileEntity() {
		return this.isBlockContainer;
	}

	/**
	 * Sets the bounds of the block.  minX, minY, minZ, maxX, maxY, maxZ
	 */
	protected final void setBlockBounds(float par1, float par2, float par3, float par4, float par5, float par6) {
		this.minX = (double)par1;
		this.minY = (double)par2;
		this.minZ = (double)par3;
		this.maxX = (double)par4;
		this.maxY = (double)par5;
		this.maxZ = (double)par6;
	}

	/**
	 * How bright to render this block based on the light its receiving. Args: iBlockAccess, x, y, z
	 */
	public float getBlockBrightness(IBlockAccess par1IBlockAccess, int x, int y, int z) {
		// Spout Start
		int light = lightValue[par1IBlockAccess.getBlockId(x, y, z)];
		if (customIds != null) {
			int key = ((x & 0xF) << 12) | ((z & 0xF) << 8) | (y & 256);
			short customId = customIds[key];
			if (customId > 0) {
				CustomBlock block = MaterialData.getCustomBlock(customId);
				if (block != null) {
					light = block.getLightLevel();
				}
			}
		}
		return par1IBlockAccess.getBrightness(x, y, z, light);
		// Spout End
	}

	/**
	 * Goes straight to getLightBrightnessForSkyBlocks for Blocks, does some fancy computing for Fluids
	 */
	public int getMixedBrightnessForBlock(IBlockAccess par1IBlockAccess, int x, int y, int z) {
		// Spout Start
		int light = lightValue[par1IBlockAccess.getBlockId(x, y, z)];
		if (customIds != null) {
			int key = ((x & 0xF) << 12) | ((z & 0xF) << 8) | (y & 256);
			short customId = customIds[key];
			if (customId > 0) {
				CustomBlock block = MaterialData.getCustomBlock(customId);
				if (block != null) {
					light = block.getLightLevel();
				}
			}
		}
		return par1IBlockAccess.getLightBrightnessForSkyBlocks(x, y, z, light);
		// Spout End
	}

	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	 * coordinates.  Args: blockAccess, x, y, z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return par5 == 0 && this.minY > 0.0D ? true : (par5 == 1 && this.maxY < 1.0D ? true : (par5 == 2 && this.minZ > 0.0D ? true : (par5 == 3 && this.maxZ < 1.0D ? true : (par5 == 4 && this.minX > 0.0D ? true : (par5 == 5 && this.maxX < 1.0D ? true : !par1IBlockAccess.isBlockOpaqueCube(par2, par3, par4))))));
	}

	/**
	 * Returns Returns true if the given side of this block type should be rendered (if it's solid or not), if the adjacent
	 * block is at the given coordinates. Args: blockAccess, x, y, z, side
	 */
	public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return par1IBlockAccess.getBlockMaterial(par2, par3, par4).isSolid();
	}

	/**
	 * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
	 */
	public int getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return this.getBlockTextureFromSideAndMetadata(par5, par1IBlockAccess.getBlockMetadata(par2, par3, par4));
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
		return this.getBlockTextureFromSide(par1);
	}

	/**
	 * Returns the block texture based on the side being looked at.  Args: side
	 */
	public int getBlockTextureFromSide(int par1) {
		return this.blockIndexInTexture;
	}

	/**
	 * Returns the bounding box of the wired rectangular prism to render.
	 */
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY, (double)par4 + this.maxZ);
	}

	/**
	 * if the specified block is in the given AABB, add its collision bounding box to the given list
	 */
	public void addCollidingBlockToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
		AxisAlignedBB var8 = this.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);

		if (var8 != null && par5AxisAlignedBB.intersectsWith(var8)) {
			par6List.add(var8);
		}
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
	 * cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY, (double)par4 + this.maxZ);
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube() {
		return true;
	}

	/**
	 * Returns whether this block is collideable based on the arguments passed in Args: blockMetaData, unknownFlag
	 */
	public boolean canCollideCheck(int par1, boolean par2) {
		return this.isCollidable();
	}

	/**
	 * Returns if this block is collidable (only used by Fire). Args: x, y, z
	 */
	public boolean isCollidable() {
		return true;
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {}

	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {}

	/**
	 * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
	 */
	public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {}

	/**
	 * How many world ticks before ticking
	 */
	public int tickRate() {
		return 10;
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update, as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(Random par1Random) {
		return 1;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, Random par2Random, int par3) {
		return this.blockID;
	}

	/**
	 * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
	 * EntityPlayer.
	 */
	// Spout Start
	public final float getPlayerRelativeBlockHardness(EntityPlayer entityhuman) {
		if (entityhuman instanceof EntityPlayerSP) {
			ActivePlayer player = (ActivePlayer) ((EntityPlayerSP) entityhuman).spoutEntity;
			FixedLocation target = player.getLastClickedLocation();
			if (target != null) {

				org.spoutcraft.api.material.Block b = target.getBlock().getType();
				if (b instanceof CustomBlock) {
					return b.getHardness() < 0.0F ? 0.0F : (!entityhuman.canHarvestBlock(this) ? 1.0F / b.getHardness() / 100.0F : entityhuman.getCurrentPlayerStrVsBlock(this) / b.getHardness() / 30.0F);
				}

				int x = (int) target.getX();
				int y = (int) target.getY();
				int z = (int) target.getZ();
				int index = ((x & 0xF) << player.getWorld().getXBitShifts()) | ((z & 0xF) << player.getWorld().getZBitShifts()) | (y & (player.getWorld().getMaxHeight() - 1));
				SpoutcraftChunk chunk = (SpoutcraftChunk) target.getWorld().getChunkAt(target);
				TIntFloatHashMap hardnessOverrides = chunk.hardnessOverrides;
				if (hardnessOverrides.containsKey(index)) {
					return hardnessOverrides.get(index);
				}
			}
		}
		return this.blockHardness < 0.0F ? 0.0F : (!entityhuman.canHarvestBlock(this) ? 1.0F / this.blockHardness / 100.0F : entityhuman.getCurrentPlayerStrVsBlock(this) / this.blockHardness / 30.0F);
	}
	// Spout End

	/**
	 * Drops the specified block items
	 */
	public final void dropBlockAsItem(World par1World, int par2, int par3, int par4, int par5, int par6) {
		this.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, 1.0F, par6);
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
		if (!par1World.isRemote) {
			int var8 = this.quantityDroppedWithBonus(par7, par1World.rand);

			for (int var9 = 0; var9 < var8; ++var9) {
				if (par1World.rand.nextFloat() <= par6) {
					int var10 = this.idDropped(par5, par1World.rand, par7);

					if (var10 > 0) {
						this.dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(var10, 1, this.damageDropped(par5)));
					}
				}
			}
		}
	}

	/**
	 * Spawns EntityItem in the world for the given ItemStack if the world is not remote.
	 */
	protected void dropBlockAsItem_do(World par1World, int par2, int par3, int par4, ItemStack par5ItemStack) {
		if (!par1World.isRemote && par1World.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
			float var6 = 0.7F;
			double var7 = (double)(par1World.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
			double var9 = (double)(par1World.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
			double var11 = (double)(par1World.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
			EntityItem var13 = new EntityItem(par1World, (double)par2 + var7, (double)par3 + var9, (double)par4 + var11, par5ItemStack);
			var13.delayBeforeCanPickup = 10;
			par1World.spawnEntityInWorld(var13);
		}
	}

	/**
	 * called by spawner, ore, redstoneOre blocks
	 */
	protected void dropXpOnBlockBreak(World par1World, int par2, int par3, int par4, int par5) {
		if (!par1World.isRemote) {
			while (par5 > 0) {
				int var6 = EntityXPOrb.getXPSplit(par5);
				par5 -= var6;
				par1World.spawnEntityInWorld(new EntityXPOrb(par1World, (double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, var6));
			}
		}
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	public int damageDropped(int par1) {
		return 0;
	}
	
	// Spout start
	public float getHardness() { // getBlockHardness version with removed random params
 		return this.blockHardness;
 	}
	// Spout end

	/**
	 * Returns how much this block can resist explosions from the passed in entity.
	 */
	public float getExplosionResistance(Entity par1Entity) {
		return this.blockResistance / 5.0F;
	}

	/**
	 * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world, x,
	 * y, z, startVec, endVec
	 */
	public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3) {
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
		par5Vec3 = par5Vec3.addVector((double)(-par2), (double)(-par3), (double)(-par4));
		par6Vec3 = par6Vec3.addVector((double)(-par2), (double)(-par3), (double)(-par4));
		Vec3 var7 = par5Vec3.getIntermediateWithXValue(par6Vec3, this.minX);
		Vec3 var8 = par5Vec3.getIntermediateWithXValue(par6Vec3, this.maxX);
		Vec3 var9 = par5Vec3.getIntermediateWithYValue(par6Vec3, this.minY);
		Vec3 var10 = par5Vec3.getIntermediateWithYValue(par6Vec3, this.maxY);
		Vec3 var11 = par5Vec3.getIntermediateWithZValue(par6Vec3, this.minZ);
		Vec3 var12 = par5Vec3.getIntermediateWithZValue(par6Vec3, this.maxZ);

		if (!this.isVecInsideYZBounds(var7)) {
			var7 = null;
		}

		if (!this.isVecInsideYZBounds(var8)) {
			var8 = null;
		}

		if (!this.isVecInsideXZBounds(var9)) {
			var9 = null;
		}

		if (!this.isVecInsideXZBounds(var10)) {
			var10 = null;
		}

		if (!this.isVecInsideXYBounds(var11)) {
			var11 = null;
		}

		if (!this.isVecInsideXYBounds(var12)) {
			var12 = null;
		}

		Vec3 var13 = null;

		if (var7 != null && (var13 == null || par5Vec3.squareDistanceTo(var7) < par5Vec3.squareDistanceTo(var13))) {
			var13 = var7;
		}

		if (var8 != null && (var13 == null || par5Vec3.squareDistanceTo(var8) < par5Vec3.squareDistanceTo(var13))) {
			var13 = var8;
		}

		if (var9 != null && (var13 == null || par5Vec3.squareDistanceTo(var9) < par5Vec3.squareDistanceTo(var13))) {
			var13 = var9;
		}

		if (var10 != null && (var13 == null || par5Vec3.squareDistanceTo(var10) < par5Vec3.squareDistanceTo(var13))) {
			var13 = var10;
		}

		if (var11 != null && (var13 == null || par5Vec3.squareDistanceTo(var11) < par5Vec3.squareDistanceTo(var13))) {
			var13 = var11;
		}

		if (var12 != null && (var13 == null || par5Vec3.squareDistanceTo(var12) < par5Vec3.squareDistanceTo(var13))) {
			var13 = var12;
		}

		if (var13 == null) {
			return null;
		} else {
			byte var14 = -1;

			if (var13 == var7) {
				var14 = 4;
			}

			if (var13 == var8) {
				var14 = 5;
			}

			if (var13 == var9) {
				var14 = 0;
			}

			if (var13 == var10) {
				var14 = 1;
			}

			if (var13 == var11) {
				var14 = 2;
			}

			if (var13 == var12) {
				var14 = 3;
			}

			return new MovingObjectPosition(par2, par3, par4, var14, var13.addVector((double)par2, (double)par3, (double)par4));
		}
	}

	/**
	 * Checks if a vector is within the Y and Z bounds of the block.
	 */
	private boolean isVecInsideYZBounds(Vec3 par1Vec3) {
		return par1Vec3 == null ? false : par1Vec3.yCoord >= this.minY && par1Vec3.yCoord <= this.maxY && par1Vec3.zCoord >= this.minZ && par1Vec3.zCoord <= this.maxZ;
	}

	/**
	 * Checks if a vector is within the X and Z bounds of the block.
	 */
	private boolean isVecInsideXZBounds(Vec3 par1Vec3) {
		return par1Vec3 == null ? false : par1Vec3.xCoord >= this.minX && par1Vec3.xCoord <= this.maxX && par1Vec3.zCoord >= this.minZ && par1Vec3.zCoord <= this.maxZ;
	}

	/**
	 * Checks if a vector is within the X and Y bounds of the block.
	 */
	private boolean isVecInsideXYBounds(Vec3 par1Vec3) {
		return par1Vec3 == null ? false : par1Vec3.xCoord >= this.minX && par1Vec3.xCoord <= this.maxX && par1Vec3.yCoord >= this.minY && par1Vec3.yCoord <= this.maxY;
	}

	/**
	 * Called upon the block being destroyed by an explosion
	 */
	public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4) {}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
	 */
	public int getRenderBlockPass() {
		return 0;
	}

	/**
	 * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
	 */
	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5) {
		return this.canPlaceBlockAt(par1World, par2, par3, par4);
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		int var5 = par1World.getBlockId(par2, par3, par4);
		return var5 == 0 || blocksList[var5].blockMaterial.isReplaceable();
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		return false;
	}

	/**
	 * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
	 */
	public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity) {}

	public int func_85104_a(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
		return par9;
	}

	/**
	 * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
	 */
	// Spout Start
	public void onBlockClicked(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		if (var5 instanceof EntityPlayerSP) {
			FixedLocation location = new FastLocation(var2, var3, var4, 0, 0, var1.world);
			((EntityPlayerSP)var5).lastClickLocation = location;
		}
	}
	// Spout End

	/**
	 * Can add to the passed in vector for a movement vector to be applied to the entity. Args: x, y, z, entity, vec3d
	 */
	public void velocityToAddToEntity(World par1World, int par2, int par3, int par4, Entity par5Entity, Vec3 par6Vec3) {}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {}

	/**
	 * returns the block bounderies minX value
	 */
	public final double getBlockBoundsMinX() {
		return this.minX;
	}

	/**
	 * returns the block bounderies maxX value
	 */
	public final double getBlockBoundsMaxX() {
		return this.maxX;
	}

	/**
	 * returns the block bounderies minY value
	 */
	public final double getBlockBoundsMinY() {
		return this.minY;
	}

	/**
	 * returns the block bounderies maxY value
	 */
	public final double getBlockBoundsMaxY() {
		return this.maxY;
	}

	/**
	 * returns the block bounderies minZ value
	 */
	public final double getBlockBoundsMinZ() {
		return this.minZ;
	}

	/**
	 * returns the block bounderies maxZ value
	 */
	public final double getBlockBoundsMaxZ() {
		return this.maxZ;
	}

	public int getBlockColor() {
		return 16777215;
	}

	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	public int getRenderColor(int par1) {
		return 16777215;
	}

	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called when
	 * first determining what to render.
	 */
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		return 16777215;
	}

	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
	 * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X, Y,
	 * Z, side
	 */
	public boolean isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return false;
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 */
	public boolean canProvidePower() {
		return false;
	}

	/**
	 * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
	 */
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {}

	/**
	 * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z, side
	 */
	public boolean isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return false;
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {}

	/**
	 * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the block
	 * and l is the block's subtype/damage.
	 */
	public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
		par2EntityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
		par2EntityPlayer.addExhaustion(0.025F);

		if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(par2EntityPlayer)) {
			ItemStack var8 = this.createStackedBlock(par6);

			if (var8 != null) {
				this.dropBlockAsItem_do(par1World, par3, par4, par5, var8);
			}
		} else {
			int var7 = EnchantmentHelper.getFortuneModifier(par2EntityPlayer);
			this.dropBlockAsItem(par1World, par3, par4, par5, par6, var7);
		}
	}

	/**
	 * Return true if a player with Silk Touch can harvest this block directly, and not its normal drops.
	 */
	protected boolean canSilkHarvest() {
		return this.renderAsNormalBlock() && !this.isBlockContainer;
	}

	/**
	 * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage and
	 * is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
	 */
	protected ItemStack createStackedBlock(int par1) {
		int var2 = 0;

		if (this.blockID >= 0 && this.blockID < Item.itemsList.length && Item.itemsList[this.blockID].getHasSubtypes()) {
			var2 = par1;
		}

		return new ItemStack(this.blockID, 1, var2);
	}

	/**
	 * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
	 */
	public int quantityDroppedWithBonus(int par1, Random par2Random) {
		return this.quantityDropped(par2Random);
	}

	/**
	 * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
	 */
	public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
		return true;
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving) {}

	public void func_85105_g(World par1World, int par2, int par3, int par4, int par5) {}

	/**
	 * set name of block from language file
	 */
	public Block setBlockName(String par1Str) {
		this.blockName = "tile." + par1Str;
		return this;
	}

	/**
	 * gets the localized version of the name of this block using StatCollector.translateToLocal. Used for the statistic
	 * page.
	 */
	public String translateBlockName() {
		return StatCollector.translateToLocal(this.getBlockName() + ".name");
	}

	public String getBlockName() {
		return this.blockName;
	}

	/**
	 * Called when the block receives a BlockEvent - see World.addBlockEvent. By default, passes it on to the tile entity
	 * at this location. Args: world, x, y, z, blockID, EventID, event parameter
	 */
	public void onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {}

	/**
	 * Return the state of blocks statistics flags - if the block is counted for mined and placed.
	 */
	public boolean getEnableStats() {
		return this.enableStats;
	}

	/**
	 * Disable statistics for the block, the block will no count for mined or placed.
	 */
	protected Block disableStats() {
		this.enableStats = false;
		return this;
	}

	/**
	 * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility and
	 * stop pistons
	 */
	public int getMobilityFlag() {
		return this.blockMaterial.getMaterialMobility();
	}

	/**
	 * Returns the default ambient occlusion value based on block opacity
	 */
	public float getAmbientOcclusionLightValue(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		return par1IBlockAccess.isBlockNormalCube(par2, par3, par4) ? 0.2F : 1.0F;
	}

	/**
	 * Block's chance to react to an entity falling on it.
	 */
	public void onFallenUpon(World par1World, int par2, int par3, int par4, Entity par5Entity, float par6) {}

	/**
	 * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
	 */
	public int idPicked(World par1World, int par2, int par3, int par4) {
		return this.blockID;
	}

	/**
	 * Get the block's damage value (for use with pick block).
	 */
	public int getDamageValue(World par1World, int par2, int par3, int par4) {
		return this.damageDropped(par1World.getBlockMetadata(par2, par3, par4));
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
	}

	/**
	 * Returns the CreativeTab to display the given block on.
	 */
	public CreativeTabs getCreativeTabToDisplayOn() {
		return this.displayOnCreativeTab;
	}

	/**
	 * Sets the CreativeTab to display this block on.
	 */
	public Block setCreativeTab(CreativeTabs par1CreativeTabs) {
		this.displayOnCreativeTab = par1CreativeTabs;
		return this;
	}

	/**
	 * Called when the block is attempted to be harvested
	 */
	public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {}

	/**
	 * Called when this block is set (with meta data).
	 */
	public void onSetBlockIDWithMetaData(World par1World, int par2, int par3, int par4, int par5) {}

	/**
	 * currently only used by BlockCauldron to incrament meta-data during rain
	 */
	public void fillWithRain(World par1World, int par2, int par3, int par4) {}

	public boolean func_82505_u_() {
		return false;
	}

	public boolean func_82506_l() {
		return true;
	}

	public boolean func_85103_a(Explosion par1Explosion) {
		return true;
	}

	static {
		Item.itemsList[cloth.blockID] = (new ItemCloth(cloth.blockID - 256)).setItemName("cloth");
		Item.itemsList[wood.blockID] = (new ItemMultiTextureTile(wood.blockID - 256, wood, BlockLog.woodType)).setItemName("log");
		Item.itemsList[planks.blockID] = (new ItemMultiTextureTile(planks.blockID - 256, planks, BlockWood.woodType)).setItemName("wood");
		Item.itemsList[silverfish.blockID] = (new ItemMultiTextureTile(silverfish.blockID - 256, silverfish, BlockSilverfish.silverfishStoneTypes)).setItemName("monsterStoneEgg");
		Item.itemsList[stoneBrick.blockID] = (new ItemMultiTextureTile(stoneBrick.blockID - 256, stoneBrick, BlockStoneBrick.STONE_BRICK_TYPES)).setItemName("stonebricksmooth");
		Item.itemsList[sandStone.blockID] = (new ItemMultiTextureTile(sandStone.blockID - 256, sandStone, BlockSandStone.SAND_STONE_TYPES)).setItemName("sandStone");
		Item.itemsList[stoneSingleSlab.blockID] = (new ItemSlab(stoneSingleSlab.blockID - 256, stoneSingleSlab, stoneDoubleSlab, false)).setItemName("stoneSlab");
		Item.itemsList[stoneDoubleSlab.blockID] = (new ItemSlab(stoneDoubleSlab.blockID - 256, stoneSingleSlab, stoneDoubleSlab, true)).setItemName("stoneSlab");
		Item.itemsList[woodSingleSlab.blockID] = (new ItemSlab(woodSingleSlab.blockID - 256, woodSingleSlab, woodDoubleSlab, false)).setItemName("woodSlab");
		Item.itemsList[woodDoubleSlab.blockID] = (new ItemSlab(woodDoubleSlab.blockID - 256, woodSingleSlab, woodDoubleSlab, true)).setItemName("woodSlab");
		Item.itemsList[sapling.blockID] = (new ItemMultiTextureTile(sapling.blockID - 256, sapling, BlockSapling.WOOD_TYPES)).setItemName("sapling");
		Item.itemsList[leaves.blockID] = (new ItemLeaves(leaves.blockID - 256)).setItemName("leaves");
		Item.itemsList[vine.blockID] = new ItemColored(vine.blockID - 256, false);
		Item.itemsList[tallGrass.blockID] = (new ItemColored(tallGrass.blockID - 256, true)).setBlockNames(new String[] {"shrub", "grass", "fern"});
		Item.itemsList[waterlily.blockID] = new ItemLilyPad(waterlily.blockID - 256);
		Item.itemsList[pistonBase.blockID] = new ItemPiston(pistonBase.blockID - 256);
		Item.itemsList[pistonStickyBase.blockID] = new ItemPiston(pistonStickyBase.blockID - 256);
		Item.itemsList[cobblestoneWall.blockID] = (new ItemMultiTextureTile(cobblestoneWall.blockID - 256, cobblestoneWall, BlockWall.types)).setItemName("cobbleWall");
		Item.itemsList[anvil.blockID] = (new ItemAnvilBlock(anvil)).setItemName("anvil");

		for (int var0 = 0; var0 < 256; ++var0) {
			if (blocksList[var0] != null) {
				if (Item.itemsList[var0] == null) {
					Item.itemsList[var0] = new ItemBlock(var0 - 256);
					blocksList[var0].initializeBlock();
				}

				boolean var1 = false;

				if (var0 > 0 && blocksList[var0].getRenderType() == 10) {
					var1 = true;
				}

				if (var0 > 0 && blocksList[var0] instanceof BlockHalfSlab) {
					var1 = true;
				}

				if (var0 == tilledField.blockID) {
					var1 = true;
				}

				if (canBlockGrass[var0]) {
					var1 = true;
				}

				if (lightOpacity[var0] == 0) {
					var1 = true;
				}

				useNeighborBrightness[var0] = var1;
			}
		}

		canBlockGrass[0] = true;
		StatList.initBreakableStats();
	}
}
