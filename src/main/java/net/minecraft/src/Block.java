package net.minecraft.src;

import java.util.List;
import java.util.Random;
// MCPatcher Start
import com.prupe.mcpatcher.mod.ColorizeBlock;
// MCPatcher End
// Spout Start
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import gnu.trove.map.hash.TIntFloatHashMap;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.material.CustomBlock;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.api.util.FastLocation;
import org.spoutcraft.api.util.FixedLocation;
import org.spoutcraft.client.block.SpoutcraftChunk;
// Spout End

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

	/**
	 * Flag if block ID should use the brightest neighbor light value as its own
	 */
	public static boolean[] useNeighborBrightness = new boolean[4096];
	public static final Block stone = (new BlockStone(1)).setHardness(1.5F).setResistance(10.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("stone");
	public static final BlockGrass grass = (BlockGrass)(new BlockGrass(2)).setHardness(0.6F).setStepSound(soundGrassFootstep).getIndirectPowerOutput("grass");
	public static final Block dirt = (new BlockDirt(3)).setHardness(0.5F).setStepSound(soundGravelFootstep).getIndirectPowerOutput("dirt");
	public static final Block cobblestone = (new Block(4, Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("stonebrick").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block planks = (new BlockWood(5)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("wood");
	public static final Block sapling = (new BlockSapling(6)).setHardness(0.0F).setStepSound(soundGrassFootstep).getIndirectPowerOutput("sapling");
	public static final Block bedrock = (new Block(7, Material.rock)).setBlockUnbreakable().setResistance(6000000.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("bedrock").disableStats().setCreativeTab(CreativeTabs.tabBlock);
	public static final BlockFluid waterMoving = (BlockFluid)(new BlockFlowing(8, Material.water)).setHardness(100.0F).setLightOpacity(3).getIndirectPowerOutput("water").disableStats();
	public static final Block waterStill = (new BlockStationary(9, Material.water)).setHardness(100.0F).setLightOpacity(3).getIndirectPowerOutput("water").disableStats();
	public static final BlockFluid lavaMoving = (BlockFluid)(new BlockFlowing(10, Material.lava)).setHardness(0.0F).setLightValue(1.0F).getIndirectPowerOutput("lava").disableStats();

	/** Stationary lava source block */
	public static final Block lavaStill = (new BlockStationary(11, Material.lava)).setHardness(100.0F).setLightValue(1.0F).getIndirectPowerOutput("lava").disableStats();
	public static final Block sand = (new BlockSand(12)).setHardness(0.5F).setStepSound(soundSandFootstep).getIndirectPowerOutput("sand");
	public static final Block gravel = (new BlockGravel(13)).setHardness(0.6F).setStepSound(soundGravelFootstep).getIndirectPowerOutput("gravel");
	public static final Block oreGold = (new BlockOre(14)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("oreGold");
	public static final Block oreIron = (new BlockOre(15)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("oreIron");
	public static final Block oreCoal = (new BlockOre(16)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("oreCoal");
	public static final Block wood = (new BlockLog(17)).setHardness(2.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("log");
	public static final BlockLeaves leaves = (BlockLeaves)(new BlockLeaves(18)).setHardness(0.2F).setLightOpacity(1).setStepSound(soundGrassFootstep).getIndirectPowerOutput("leaves");
	public static final Block sponge = (new BlockSponge(19)).setHardness(0.6F).setStepSound(soundGrassFootstep).getIndirectPowerOutput("sponge");
	public static final Block glass = (new BlockGlass(20, Material.glass, false)).setHardness(0.3F).setStepSound(soundGlassFootstep).getIndirectPowerOutput("glass");
	public static final Block oreLapis = (new BlockOre(21)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("oreLapis");
	public static final Block blockLapis = (new Block(22, Material.rock)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("blockLapis").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block dispenser = (new BlockDispenser(23)).setHardness(3.5F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("dispenser");
	public static final Block sandStone = (new BlockSandStone(24)).setStepSound(soundStoneFootstep).setHardness(0.8F).getIndirectPowerOutput("sandStone");
	public static final Block music = (new BlockNote(25)).setHardness(0.8F).getIndirectPowerOutput("musicBlock");
	public static final Block bed = (new BlockBed(26)).setHardness(0.2F).getIndirectPowerOutput("bed").disableStats();
	public static final Block railPowered = (new BlockRailPowered(27)).setHardness(0.7F).setStepSound(soundMetalFootstep).getIndirectPowerOutput("goldenRail");
	public static final Block railDetector = (new BlockDetectorRail(28)).setHardness(0.7F).setStepSound(soundMetalFootstep).getIndirectPowerOutput("detectorRail");
	public static final BlockPistonBase pistonStickyBase = (BlockPistonBase)(new BlockPistonBase(29, true)).getIndirectPowerOutput("pistonStickyBase");
	public static final Block web = (new BlockWeb(30)).setLightOpacity(1).setHardness(4.0F).getIndirectPowerOutput("web");
	public static final BlockTallGrass tallGrass = (BlockTallGrass)(new BlockTallGrass(31)).setHardness(0.0F).setStepSound(soundGrassFootstep).getIndirectPowerOutput("tallgrass");
	public static final BlockDeadBush deadBush = (BlockDeadBush)(new BlockDeadBush(32)).setHardness(0.0F).setStepSound(soundGrassFootstep).getIndirectPowerOutput("deadbush");
	public static final BlockPistonBase pistonBase = (BlockPistonBase)(new BlockPistonBase(33, false)).getIndirectPowerOutput("pistonBase");
	public static final BlockPistonExtension pistonExtension = new BlockPistonExtension(34);
	public static final Block cloth = (new BlockCloth()).setHardness(0.8F).setStepSound(soundClothFootstep).getIndirectPowerOutput("cloth");
	public static final BlockPistonMoving pistonMoving = new BlockPistonMoving(36);
	public static final BlockFlower plantYellow = (BlockFlower)(new BlockFlower(37)).setHardness(0.0F).setStepSound(soundGrassFootstep).getIndirectPowerOutput("flower");
	public static final BlockFlower plantRed = (BlockFlower)(new BlockFlower(38)).setHardness(0.0F).setStepSound(soundGrassFootstep).getIndirectPowerOutput("rose");
	public static final BlockFlower mushroomBrown = (BlockFlower)(new BlockMushroom(39, "mushroom_brown")).setHardness(0.0F).setStepSound(soundGrassFootstep).setLightValue(0.125F).getIndirectPowerOutput("mushroom");
	public static final BlockFlower mushroomRed = (BlockFlower)(new BlockMushroom(40, "mushroom_red")).setHardness(0.0F).setStepSound(soundGrassFootstep).getIndirectPowerOutput("mushroom");
	public static final Block blockGold = (new BlockOreStorage(41)).setHardness(3.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).getIndirectPowerOutput("blockGold");
	public static final Block blockSteel = (new BlockOreStorage(42)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).getIndirectPowerOutput("blockIron");

	/** stoneDoubleSlab */
	public static final BlockHalfSlab stoneDoubleSlab = (BlockHalfSlab)(new BlockStep(43, true)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("stoneSlab");

	/** stoneSingleSlab */
	public static final BlockHalfSlab stoneSingleSlab = (BlockHalfSlab)(new BlockStep(44, false)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("stoneSlab");
	public static final Block brick = (new Block(45, Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("brick").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block tnt = (new BlockTNT(46)).setHardness(0.0F).setStepSound(soundGrassFootstep).getIndirectPowerOutput("tnt");
	public static final Block bookShelf = (new BlockBookshelf(47)).setHardness(1.5F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("bookshelf");
	public static final Block cobblestoneMossy = (new Block(48, Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("stoneMoss").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block obsidian = (new BlockObsidian(49)).setHardness(50.0F).setResistance(2000.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("obsidian");
	public static final Block torchWood = (new BlockTorch(50)).setHardness(0.0F).setLightValue(0.9375F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("torch");
	public static final BlockFire fire = (BlockFire)(new BlockFire(51)).setHardness(0.0F).setLightValue(1.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("fire").disableStats();
	public static final Block mobSpawner = (new BlockMobSpawner(52)).setHardness(5.0F).setStepSound(soundMetalFootstep).getIndirectPowerOutput("mobSpawner").disableStats();
	public static final Block stairsWoodOak = (new BlockStairs(53, planks, 0)).getIndirectPowerOutput("stairsWood");
	public static final BlockChest chest = (BlockChest)(new BlockChest(54, 0)).setHardness(2.5F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("chest");
	public static final BlockRedstoneWire redstoneWire = (BlockRedstoneWire)(new BlockRedstoneWire(55)).setHardness(0.0F).setStepSound(soundPowderFootstep).getIndirectPowerOutput("redstoneDust").disableStats();
	public static final Block oreDiamond = (new BlockOre(56)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("oreDiamond");
	public static final Block blockDiamond = (new BlockOreStorage(57)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).getIndirectPowerOutput("blockDiamond");
	public static final Block workbench = (new BlockWorkbench(58)).setHardness(2.5F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("workbench");
	public static final Block crops = (new BlockCrops(59)).getIndirectPowerOutput("crops");
	public static final Block tilledField = (new BlockFarmland(60)).setHardness(0.6F).setStepSound(soundGravelFootstep).getIndirectPowerOutput("farmland");
	public static final Block furnaceIdle = (new BlockFurnace(61, false)).setHardness(3.5F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("furnace").setCreativeTab(CreativeTabs.tabDecorations);
	public static final Block furnaceBurning = (new BlockFurnace(62, true)).setHardness(3.5F).setStepSound(soundStoneFootstep).setLightValue(0.875F).getIndirectPowerOutput("furnace");
	public static final Block signPost = (new BlockSign(63, TileEntitySign.class, true)).setHardness(1.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("sign").disableStats();
	public static final Block doorWood = (new BlockDoor(64, Material.wood)).setHardness(3.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("doorWood").disableStats();
	public static final Block ladder = (new BlockLadder(65)).setHardness(0.4F).setStepSound(soundLadderFootstep).getIndirectPowerOutput("ladder");
	public static final Block rail = (new BlockRail(66)).setHardness(0.7F).setStepSound(soundMetalFootstep).getIndirectPowerOutput("rail");
	public static final Block stairsCobblestone = (new BlockStairs(67, cobblestone, 0)).getIndirectPowerOutput("stairsStone");
	public static final Block signWall = (new BlockSign(68, TileEntitySign.class, false)).setHardness(1.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("sign").disableStats();
	public static final Block lever = (new BlockLever(69)).setHardness(0.5F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("lever");
	public static final Block pressurePlateStone = (new BlockPressurePlate(70, "stone", Material.rock, EnumMobType.mobs)).setHardness(0.5F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("pressurePlate");
	public static final Block doorSteel = (new BlockDoor(71, Material.iron)).setHardness(5.0F).setStepSound(soundMetalFootstep).getIndirectPowerOutput("doorIron").disableStats();
	public static final Block pressurePlatePlanks = (new BlockPressurePlate(72, "wood", Material.wood, EnumMobType.everything)).setHardness(0.5F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("pressurePlate");
	public static final Block oreRedstone = (new BlockRedstoneOre(73, false)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("oreRedstone").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block oreRedstoneGlowing = (new BlockRedstoneOre(74, true)).setLightValue(0.625F).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("oreRedstone");
	public static final Block torchRedstoneIdle = (new BlockRedstoneTorch(75, false)).setHardness(0.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("notGate");
	public static final Block torchRedstoneActive = (new BlockRedstoneTorch(76, true)).setHardness(0.0F).setLightValue(0.5F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("notGate").setCreativeTab(CreativeTabs.tabRedstone);
	public static final Block stoneButton = (new BlockButtonStone(77)).setHardness(0.5F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("button");
	public static final Block snow = (new BlockSnow(78)).setHardness(0.1F).setStepSound(soundSnowFootstep).getIndirectPowerOutput("snow").setLightOpacity(0);
	public static final Block ice = (new BlockIce(79)).setHardness(0.5F).setLightOpacity(3).setStepSound(soundGlassFootstep).getIndirectPowerOutput("ice");
	public static final Block blockSnow = (new BlockSnowBlock(80)).setHardness(0.2F).setStepSound(soundSnowFootstep).getIndirectPowerOutput("snow");
	public static final Block cactus = (new BlockCactus(81)).setHardness(0.4F).setStepSound(soundClothFootstep).getIndirectPowerOutput("cactus");
	public static final Block blockClay = (new BlockClay(82)).setHardness(0.6F).setStepSound(soundGravelFootstep).getIndirectPowerOutput("clay");
	public static final Block reed = (new BlockReed(83)).setHardness(0.0F).setStepSound(soundGrassFootstep).getIndirectPowerOutput("reeds").disableStats();
	public static final Block jukebox = (new BlockJukeBox(84)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("jukebox");
	public static final Block fence = (new BlockFence(85, "wood", Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("fence");
	public static final Block pumpkin = (new BlockPumpkin(86, false)).setHardness(1.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("pumpkin");
	public static final Block netherrack = (new BlockNetherrack(87)).setHardness(0.4F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("hellrock");
	public static final Block slowSand = (new BlockSoulSand(88)).setHardness(0.5F).setStepSound(soundSandFootstep).getIndirectPowerOutput("hellsand");
	public static final Block glowStone = (new BlockGlowStone(89, Material.glass)).setHardness(0.3F).setStepSound(soundGlassFootstep).setLightValue(1.0F).getIndirectPowerOutput("lightgem");

	/** The purple teleport blocks inside the obsidian circle */
	public static final BlockPortal portal = (BlockPortal)(new BlockPortal(90)).setHardness(-1.0F).setStepSound(soundGlassFootstep).setLightValue(0.75F).getIndirectPowerOutput("portal");
	public static final Block pumpkinLantern = (new BlockPumpkin(91, true)).setHardness(1.0F).setStepSound(soundWoodFootstep).setLightValue(1.0F).getIndirectPowerOutput("litpumpkin");
	public static final Block cake = (new BlockCake(92)).setHardness(0.5F).setStepSound(soundClothFootstep).getIndirectPowerOutput("cake").disableStats();
	public static final BlockRedstoneRepeater redstoneRepeaterIdle = (BlockRedstoneRepeater)(new BlockRedstoneRepeater(93, false)).setHardness(0.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("diode").disableStats();
	public static final BlockRedstoneRepeater redstoneRepeaterActive = (BlockRedstoneRepeater)(new BlockRedstoneRepeater(94, true)).setHardness(0.0F).setLightValue(0.625F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("diode").disableStats();

	/**
	 * April fools secret locked chest, only spawns on new chunks on 1st April.
	 */
	public static final Block lockedChest = (new BlockLockedChest(95)).setHardness(0.0F).setLightValue(1.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("lockedchest").setTickRandomly(true);
	public static final Block trapdoor = (new BlockTrapDoor(96, Material.wood)).setHardness(3.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("trapdoor").disableStats();
	public static final Block silverfish = (new BlockSilverfish(97)).setHardness(0.75F).getIndirectPowerOutput("monsterStoneEgg");
	public static final Block stoneBrick = (new BlockStoneBrick(98)).setHardness(1.5F).setResistance(10.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("stonebricksmooth");
	public static final Block mushroomCapBrown = (new BlockMushroomCap(99, Material.wood, 0)).setHardness(0.2F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("mushroom");
	public static final Block mushroomCapRed = (new BlockMushroomCap(100, Material.wood, 1)).setHardness(0.2F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("mushroom");
	public static final Block fenceIron = (new BlockPane(101, "fenceIron", "fenceIron", Material.iron, true)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).getIndirectPowerOutput("fenceIron");
	public static final Block thinGlass = (new BlockPane(102, "glass", "thinglass_top", Material.glass, false)).setHardness(0.3F).setStepSound(soundGlassFootstep).getIndirectPowerOutput("thinGlass");
	public static final Block melon = (new BlockMelon(103)).setHardness(1.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("melon");
	public static final Block pumpkinStem = (new BlockStem(104, pumpkin)).setHardness(0.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("pumpkinStem");
	public static final Block melonStem = (new BlockStem(105, melon)).setHardness(0.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("pumpkinStem");
	public static final Block vine = (new BlockVine(106)).setHardness(0.2F).setStepSound(soundGrassFootstep).getIndirectPowerOutput("vine");
	public static final Block fenceGate = (new BlockFenceGate(107)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("fenceGate");
	public static final Block stairsBrick = (new BlockStairs(108, brick, 0)).getIndirectPowerOutput("stairsBrick");
	public static final Block stairsStoneBrick = (new BlockStairs(109, stoneBrick, 0)).getIndirectPowerOutput("stairsStoneBrickSmooth");
	public static final BlockMycelium mycelium = (BlockMycelium)(new BlockMycelium(110)).setHardness(0.6F).setStepSound(soundGrassFootstep).getIndirectPowerOutput("mycel");
	public static final Block waterlily = (new BlockLilyPad(111)).setHardness(0.0F).setStepSound(soundGrassFootstep).getIndirectPowerOutput("waterlily");
	public static final Block netherBrick = (new Block(112, Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("netherBrick").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block netherFence = (new BlockFence(113, "netherBrick", Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("netherFence");
	public static final Block stairsNetherBrick = (new BlockStairs(114, netherBrick, 0)).getIndirectPowerOutput("stairsNetherBrick");
	public static final Block netherStalk = (new BlockNetherStalk(115)).getIndirectPowerOutput("netherStalk");
	public static final Block enchantmentTable = (new BlockEnchantmentTable(116)).setHardness(5.0F).setResistance(2000.0F).getIndirectPowerOutput("enchantmentTable");
	public static final Block brewingStand = (new BlockBrewingStand(117)).setHardness(0.5F).setLightValue(0.125F).getIndirectPowerOutput("brewingStand");
	public static final BlockCauldron cauldron = (BlockCauldron)(new BlockCauldron(118)).setHardness(2.0F).getIndirectPowerOutput("cauldron");
	public static final Block endPortal = (new BlockEndPortal(119, Material.portal)).setHardness(-1.0F).setResistance(6000000.0F);
	public static final Block endPortalFrame = (new BlockEndPortalFrame(120)).setStepSound(soundGlassFootstep).setLightValue(0.125F).setHardness(-1.0F).getIndirectPowerOutput("endPortalFrame").setResistance(6000000.0F).setCreativeTab(CreativeTabs.tabDecorations);

	/** The rock found in The End. */
	public static final Block whiteStone = (new Block(121, Material.rock)).setHardness(3.0F).setResistance(15.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("whiteStone").setCreativeTab(CreativeTabs.tabBlock);
	public static final Block dragonEgg = (new BlockDragonEgg(122)).setHardness(3.0F).setResistance(15.0F).setStepSound(soundStoneFootstep).setLightValue(0.125F).getIndirectPowerOutput("dragonEgg");
	public static final Block redstoneLampIdle = (new BlockRedstoneLight(123, false)).setHardness(0.3F).setStepSound(soundGlassFootstep).getIndirectPowerOutput("redstoneLight").setCreativeTab(CreativeTabs.tabRedstone);
	public static final Block redstoneLampActive = (new BlockRedstoneLight(124, true)).setHardness(0.3F).setStepSound(soundGlassFootstep).getIndirectPowerOutput("redstoneLight");
	public static final BlockHalfSlab woodDoubleSlab = (BlockHalfSlab)(new BlockWoodSlab(125, true)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("woodSlab");
	public static final BlockHalfSlab woodSingleSlab = (BlockHalfSlab)(new BlockWoodSlab(126, false)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("woodSlab");
	public static final Block cocoaPlant = (new BlockCocoa(127)).setHardness(0.2F).setResistance(5.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("cocoa");
	public static final Block stairsSandStone = (new BlockStairs(128, sandStone, 0)).getIndirectPowerOutput("stairsSandStone");
	public static final Block oreEmerald = (new BlockOre(129)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("oreEmerald");
	public static final Block enderChest = (new BlockEnderChest(130)).setHardness(22.5F).setResistance(1000.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("enderChest").setLightValue(0.5F);
	public static final BlockTripWireSource tripWireSource = (BlockTripWireSource)(new BlockTripWireSource(131)).getIndirectPowerOutput("tripWireSource");
	public static final Block tripWire = (new BlockTripWire(132)).getIndirectPowerOutput("tripWire");
	public static final Block blockEmerald = (new BlockOreStorage(133)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).getIndirectPowerOutput("blockEmerald");
	public static final Block stairsWoodSpruce = (new BlockStairs(134, planks, 1)).getIndirectPowerOutput("stairsWoodSpruce");
	public static final Block stairsWoodBirch = (new BlockStairs(135, planks, 2)).getIndirectPowerOutput("stairsWoodBirch");
	public static final Block stairsWoodJungle = (new BlockStairs(136, planks, 3)).getIndirectPowerOutput("stairsWoodJungle");
	public static final Block commandBlock = (new BlockCommandBlock(137)).getIndirectPowerOutput("commandBlock");
	public static final BlockBeacon beacon = (BlockBeacon)(new BlockBeacon(138)).getIndirectPowerOutput("beacon").setLightValue(1.0F);
	public static final Block cobblestoneWall = (new BlockWall(139, cobblestone)).getIndirectPowerOutput("cobbleWall");
	public static final Block flowerPot = (new BlockFlowerPot(140)).setHardness(0.0F).setStepSound(soundPowderFootstep).getIndirectPowerOutput("flowerPot");
	public static final Block carrot = (new BlockCarrot(141)).getIndirectPowerOutput("carrots");
	public static final Block potato = (new BlockPotato(142)).getIndirectPowerOutput("potatoes");
	public static final Block woodenButton = (new BlockButtonWood(143)).setHardness(0.5F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("button");
	public static final Block skull = (new BlockSkull(144)).setHardness(1.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("skull");
	public static final Block anvil = (new BlockAnvil(145)).setHardness(5.0F).setStepSound(soundAnvilFootstep).setResistance(2000.0F).getIndirectPowerOutput("anvil");
	public static final Block chestTrapped = (new BlockChest(146, 1)).setHardness(2.5F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("chestTrap");
	public static final Block pressurePlateGold = (new BlockPressurePlateWeighted(147, "blockGold", Material.iron, 64)).setHardness(0.5F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("weightedPlate_light");
	public static final Block pressurePlateIron = (new BlockPressurePlateWeighted(148, "blockIron", Material.iron, 640)).setHardness(0.5F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("weightedPlate_heavy");
	public static final BlockComparator redstoneComparatorIdle = (BlockComparator)(new BlockComparator(149, false)).setHardness(0.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("comparator").disableStats();
	public static final BlockComparator redstoneComparatorActive = (BlockComparator)(new BlockComparator(150, true)).setHardness(0.0F).setLightValue(0.625F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("comparator").disableStats();
	public static final BlockDaylightDetector daylightSensor = (BlockDaylightDetector)(new BlockDaylightDetector(151)).setHardness(0.2F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("daylightDetector");
	public static final Block blockRedstone = (new BlockPoweredOre(152)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).getIndirectPowerOutput("blockRedstone");
	public static final Block oreNetherQuartz = (new BlockOre(153)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("netherquartz");
	public static final BlockHopper hopperBlock = (BlockHopper)(new BlockHopper(154)).setHardness(3.0F).setResistance(8.0F).setStepSound(soundWoodFootstep).getIndirectPowerOutput("hopper");
	public static final Block blockNetherQuartz = (new BlockQuartz(155)).setStepSound(soundStoneFootstep).setHardness(0.8F).getIndirectPowerOutput("quartzBlock");
	public static final Block stairCompactNetherQuartz = (new BlockStairs(156, blockNetherQuartz, 0)).getIndirectPowerOutput("stairsQuartz");
	public static final Block railActivator = (new BlockRailPowered(157)).setHardness(0.7F).setStepSound(soundMetalFootstep).getIndirectPowerOutput("activatorRail");
	public static final Block dropper = (new BlockDropper(158)).setHardness(3.5F).setStepSound(soundStoneFootstep).getIndirectPowerOutput("dropper");

	/**
	 * The index of the texture to be displayed for this block. May vary based on graphics settings. Mostly seems to come
	 * from terrain.png, and the index is 0-based (grass is 0).
	 */
	public int blockIndexInTexture;

	/** ID of the block. */
	public final int blockID;

	/** Indicates how many hits it takes to break a block. */
	// Spout Start - protected to public
	public float blockHardness;
	// Spout End

	/** Indicates the blocks resistance to explosions. */
	// Spout Start - protected to public
	public float blockResistance;
	// Spout End

	/**
	 * set to true when Block's constructor is called through the chain of super()'s. Note: Never used
	 */
	protected boolean blockConstructorCalled = true;

	/**
	 * If this field is true, the block is counted for statistics (mined or placed)
	 */
	protected boolean enableStats = true;

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

	/** The unlocalized name of this block. */
	private String unlocalizedName;
	protected Icon blockIcon;

	private String blockName;
	// Spout Start
	public static short[] customIds = null;
	// Spout End

	protected Block(int par1, Material par2Material) {
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
	 * This method is called on a block after all other blocks gets already created. You can use it to reference and
	 * configure something on the block that needs the others ones.
	 */
	protected void initializeBlock() {}

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
	// Spout Start - protected to public
	public Block setLightOpacity(int par1) {
	// Spout End
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
		return var1 == null ? false : var1.blockMaterial.isOpaque() && var1.renderAsNormalBlock() && !var1.canProvidePower();
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
	// Spout Start
	public float getBlockBrightness(IBlockAccess par1IBlockAccess, int x, int y, int z) {
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
	// Spout Start
	public int getMixedBrightnessForBlock(IBlockAccess par1IBlockAccess, int x, int y, int z) {
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
	public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return this.getBlockTextureFromSideAndMetadata(par5, par1IBlockAccess.getBlockMetadata(par2, par3, par4));
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	public Icon getBlockTextureFromSideAndMetadata(int par1, int par2) {
		return this.blockIcon;
	}

	/**
	 * Returns the block texture based on the side being looked at.  Args: side
	 */
	public final Icon getBlockTextureFromSide(int par1) {
		return this.getBlockTextureFromSideAndMetadata(par1, 0);
	}

	/**
	 * Returns the bounding box of the wired rectangular prism to render.
	 */
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return AxisAlignedBB.getAABBPool().getAABB((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY, (double)par4 + this.maxZ);
	}

	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the mask.)
	 * Parameters: World, X, Y, Z, mask, list, colliding entity
	 */
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
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
		return AxisAlignedBB.getAABBPool().getAABB((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY, (double)par4 + this.maxZ);
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
	public int tickRate(World par1World) {
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
	// TODO: This should be combined into one method.
	public float getPlayerRelativeBlockHardness(EntityPlayer par1EntityPlayer, World par2World, int par3, int par4, int par5) {
		float var6 = this.getBlockHardness(par2World, par3, par4, par5);
		return var6 < 0.0F ? 0.0F : (!par1EntityPlayer.canHarvestBlock(this) ? par1EntityPlayer.getCurrentPlayerStrVsBlock(this, false) / var6 / 100.0F : par1EntityPlayer.getCurrentPlayerStrVsBlock(this, true) / var6 / 30.0F);
	}

	// Spout Start
	/**
	 * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
	 * EntityPlayer.
	 */
	public float getPlayerRelativeBlockHardness(EntityPlayer entityhuman) {
		if (entityhuman instanceof EntityPlayerSP) {
			FixedLocation target = Spoutcraft.getActivePlayer().getLastClickedLocation();
			if (target != null) {
				int x = (int) target.getX();
				int y = (int) target.getY();
				int z = (int) target.getZ();
				SpoutcraftChunk chunk = Spoutcraft.getChunkAt(entityhuman.worldObj, x, y, z);
				short customId = chunk.getCustomBlockId(x, y, z);
				if (customId > 0) {
					CustomBlock b = MaterialData.getCustomBlock(customId);
					return b.getHardness() < 0.0F ? 0.0F : (!entityhuman.canHarvestBlock(this) ? entityhuman.getCurrentPlayerStrVsBlock(this, false) / b.getHardness() / 100.0F : entityhuman.getCurrentPlayerStrVsBlock(this, true) / b.getHardness() / 30.0F);
				}
			}
		}
		return this.blockHardness < 0.0F ? 0.0F : (!entityhuman.canHarvestBlock(this) ? entityhuman.getCurrentPlayerStrVsBlock(this, false) / this.blockHardness / 100.0F : entityhuman.getCurrentPlayerStrVsBlock(this, true) / this.blockHardness / 30.0F);
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

	// Spout Start - getBlockHardness version with removed random params.
	public float getHardness() {
		return this.blockHardness;
	}
	// Spout End

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
	public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4, Explosion par5Explosion) {}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
	 */
	public int getRenderBlockPass() {
		return 0;
	}

	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5, ItemStack par6ItemStack) {
		return this.canPlaceBlockOnSide(par1World, par2, par3, par4, par5);
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

	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
	 */
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
		return par9;
	}

	/**
	 * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
	 */
	// Spout Start
	public void onBlockClicked(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		if (var5 instanceof EntityPlayerSP) {
			FixedLocation location = new FastLocation(var2, var3, var4, 0, 0);
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
		// MCPatcher Start
		return ColorizeBlock.colorizeBlock(this);
		// MCPatcher End
	}

	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called when
	 * first determining what to render.
	 */
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		// MCPatcher Start
		return ColorizeBlock.colorizeBlock(this, par2, par3, par4, par1IBlockAccess.getBlockMetadata(par2, par3, par4));
		// MCPatcher End
	}

	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
	 * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X, Y,
	 * Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return 0;
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
	 * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
	 * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return 0;
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
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {}

	/**
	 * Called after a block is placed
	 */
	public void onPostBlockPlaced(World par1World, int par2, int par3, int par4, int par5) {}

	/**
	 * Returns the indirect signal strength being outputted by the given block in the *opposite* of the given direction.
	 * Args: World, X, Y, Z, direction. If isBlockNormalCube returns true, standard redstone propagation rules will apply
	 * instead and this will not be called. 
	 */
	public Block getIndirectPowerOutput(String par1Str) {
		this.unlocalizedName = par1Str;
		return this;
	}

	/**
	 * Gets the localized name of this block. Used for the statistics page.
	 */
	public String getLocalizedName() {
		return StatCollector.translateToLocal(this.getUnlocalizedName() + ".name");
	}

	/**
	 * Returns the unlocalized name of this block.
	 */
	public String getUnlocalizedName() {
		return "tile." + this.unlocalizedName;
	}

	/**
	 * Returns the unlocalized name without the tile. prefix. Caution: client-only.
	 */
	public String getUnlocalizedName2() { 
		return this.unlocalizedName;
	}

	/**
	 * Called when the block receives a BlockEvent - see World.addBlockEvent. By default, passes it on to the tile entity
	 * at this location. Args: world, x, y, z, blockID, EventID, event parameter
	 */
	public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
		return false;
	}

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

	/**
	 * Return whether this block can drop from an explosion.
	 */
	public boolean canDropFromExplosion(Explosion par1Explosion) {
		return true;
	}

	/**
	 * Returns true if the given block ID is equivalent to this one. Example: redstoneTorchOn matches itself and
	 * redstoneTorchOff, and vice versa. Most blocks only match themselves.
	 */
	public boolean isAssociatedBlockID(int par1) { 
		return this.blockID == par1;
	}

	/**
	 * Static version of isAssociatedBlockID.
	 */
	public static boolean isAssociatedBlockID(int par0, int par1) {
		return par0 == par1 ? true : (par0 != 0 && par1 != 0 && blocksList[par0] != null && blocksList[par1] != null ? blocksList[par0].isAssociatedBlockID(par1) : false);
	} 

	/**
	 * If this returns true, then comparators facing away from this block will use the value from
	 * getComparatorInputOverride instead of the actual redstone signal strength.
	 */
	public boolean hasComparatorInputOverride() {
		return false;
	} 

	/**
	 * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
	 * strength when this block inputs to a comparator.
	 */
	public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
		return 0;
	} 

	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This is
	 * the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon(this.unlocalizedName);
	} 

	public String func_94327_t_() {
		return null;
	}

	static {
		Item.itemsList[cloth.blockID] = (new ItemCloth(cloth.blockID - 256)).setUnlocalizedName("cloth");
		Item.itemsList[wood.blockID] = (new ItemMultiTextureTile(wood.blockID - 256, wood, BlockLog.woodType)).setUnlocalizedName("log");
		Item.itemsList[planks.blockID] = (new ItemMultiTextureTile(planks.blockID - 256, planks, BlockWood.woodType)).setUnlocalizedName("wood");
		Item.itemsList[silverfish.blockID] = (new ItemMultiTextureTile(silverfish.blockID - 256, silverfish, BlockSilverfish.silverfishStoneTypes)).setUnlocalizedName("monsterStoneEgg");
		Item.itemsList[stoneBrick.blockID] = (new ItemMultiTextureTile(stoneBrick.blockID - 256, stoneBrick, BlockStoneBrick.STONE_BRICK_TYPES)).setUnlocalizedName("stonebricksmooth");
		Item.itemsList[sandStone.blockID] = (new ItemMultiTextureTile(sandStone.blockID - 256, sandStone, BlockSandStone.SAND_STONE_TYPES)).setUnlocalizedName("sandStone");
		Item.itemsList[blockNetherQuartz.blockID] = (new ItemMultiTextureTile(blockNetherQuartz.blockID - 256, blockNetherQuartz, BlockQuartz.quartzBlockTypes)).setUnlocalizedName("quartzBlock");	     
		Item.itemsList[stoneSingleSlab.blockID] = (new ItemSlab(stoneSingleSlab.blockID - 256, stoneSingleSlab, stoneDoubleSlab, false)).setUnlocalizedName("stoneSlab");
		Item.itemsList[stoneDoubleSlab.blockID] = (new ItemSlab(stoneDoubleSlab.blockID - 256, stoneSingleSlab, stoneDoubleSlab, true)).setUnlocalizedName("stoneSlab");
		Item.itemsList[woodSingleSlab.blockID] = (new ItemSlab(woodSingleSlab.blockID - 256, woodSingleSlab, woodDoubleSlab, false)).setUnlocalizedName("woodSlab");
		Item.itemsList[woodDoubleSlab.blockID] = (new ItemSlab(woodDoubleSlab.blockID - 256, woodSingleSlab, woodDoubleSlab, true)).setUnlocalizedName("woodSlab");
		Item.itemsList[sapling.blockID] = (new ItemMultiTextureTile(sapling.blockID - 256, sapling, BlockSapling.WOOD_TYPES)).setUnlocalizedName("sapling");
		Item.itemsList[leaves.blockID] = (new ItemLeaves(leaves.blockID - 256)).setUnlocalizedName("leaves");
		Item.itemsList[vine.blockID] = new ItemColored(vine.blockID - 256, false);
		Item.itemsList[tallGrass.blockID] = (new ItemColored(tallGrass.blockID - 256, true)).setBlockNames(new String[] {"shrub", "grass", "fern"});
		Item.itemsList[snow.blockID] = new ItemSnow(snow.blockID - 256, snow);
		Item.itemsList[waterlily.blockID] = new ItemLilyPad(waterlily.blockID - 256);
		Item.itemsList[pistonBase.blockID] = new ItemPiston(pistonBase.blockID - 256);
		Item.itemsList[pistonStickyBase.blockID] = new ItemPiston(pistonStickyBase.blockID - 256);
		Item.itemsList[cobblestoneWall.blockID] = (new ItemMultiTextureTile(cobblestoneWall.blockID - 256, cobblestoneWall, BlockWall.types)).setUnlocalizedName("cobbleWall");
		Item.itemsList[anvil.blockID] = (new ItemAnvilBlock(anvil)).setUnlocalizedName("anvil");

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
