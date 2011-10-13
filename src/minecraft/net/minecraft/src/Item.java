// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.PrintStream;
import java.util.Random;

import org.getspout.spout.item.SpoutItem; //Spout Custom item block

// Referenced classes of package net.minecraft.src:
//            ItemStack, StatCollector, EnumAction, ItemSpade, 
//            EnumToolMaterial, ItemPickaxe, ItemAxe, ItemFlintAndSteel, 
//            ItemFood, ItemBow, ItemCoal, ItemSword, 
//            ItemSoup, ItemHoe, ItemSeeds, Block, 
//            ItemArmor, ItemPainting, Potion, ItemSign, 
//            ItemDoor, Material, ItemBucket, ItemMinecart, 
//            ItemSaddle, ItemRedstone, ItemSnowball, ItemBoat, 
//            ItemReed, ItemEgg, ItemFishingRod, ItemDye, 
//            ItemBed, ItemMap, ItemShears, ItemRecord, 
//            StatList, EntityPlayer, World, EntityLiving, 
//            Entity

public class Item
{

    protected Item(int i)
    {
        maxStackSize = 64;
        maxDamage = 0;
        bFull3D = false;
        hasSubtypes = false;
        containerItem = null;
        shiftedIndex = 256 + i;
        if(itemsList[256 + i] != null)
        {
            System.out.println((new StringBuilder()).append("CONFLICT @ ").append(i).toString());
        }
        itemsList[256 + i] = this;
    }

    public Item setIconIndex(int i)
    {
        iconIndex = i;
        return this;
    }

    public Item setMaxStackSize(int i)
    {
        maxStackSize = i;
        return this;
    }

    public Item setIconCoord(int i, int j)
    {
        iconIndex = i + j * 16;
        return this;
    }

    public int getIconFromDamage(int i)
    {
        return iconIndex;
    }

    public final int getIconIndex(ItemStack itemstack)
    {
        return getIconFromDamage(itemstack.getItemDamage());
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        return false;
    }

    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        return 1.0F;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        return itemstack;
    }

    public ItemStack func_35413_b(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        return itemstack;
    }

    public int getItemStackLimit()
    {
        return maxStackSize;
    }

    public int getPlacedBlockMetadata(int i)
    {
        return 0;
    }

    public boolean getHasSubtypes()
    {
        return hasSubtypes;
    }

    protected Item setHasSubtypes(boolean flag)
    {
        hasSubtypes = flag;
        return this;
    }

    public int getMaxDamage()
    {
        return maxDamage;
    }

    protected Item setMaxDamage(int i)
    {
        maxDamage = i;
        return this;
    }

    public boolean isDamageable()
    {
        return maxDamage > 0 && !hasSubtypes;
    }

    public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1)
    {
        return false;
    }

    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving)
    {
        return false;
    }

    public int getDamageVsEntity(Entity entity)
    {
        return 1;
    }

    public boolean canHarvestBlock(Block block)
    {
        return false;
    }

    public void saddleEntity(ItemStack itemstack, EntityLiving entityliving)
    {
    }

    public Item setFull3D()
    {
        bFull3D = true;
        return this;
    }

    public boolean isFull3D()
    {
        return bFull3D;
    }

    public boolean shouldRotateAroundWhenRendering()
    {
        return false;
    }

    public Item setItemName(String s)
    {
        itemName = (new StringBuilder()).append("item.").append(s).toString();
        return this;
    }

    public String getItemName()
    {
        return itemName;
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        return itemName;
    }

    public Item setContainerItem(Item item)
    {
        if(maxStackSize > 1)
        {
            throw new IllegalArgumentException("Max stack size must be 1 for items with crafting results");
        } else
        {
            containerItem = item;
            return this;
        }
    }

    public Item getContainerItem()
    {
        return containerItem;
    }

    public boolean hasContainerItem()
    {
        return containerItem != null;
    }

    public String getStatName()
    {
        return StatCollector.translateToLocal((new StringBuilder()).append(getItemName()).append(".name").toString());
    }

    public int getColorFromDamage(int i)
    {
        return 0xffffff;
    }

    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
    }

    public void onCreated(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
    }

    public EnumAction func_35412_b(ItemStack itemstack)
    {
        return EnumAction.none;
    }

    public int func_35411_c(ItemStack itemstack)
    {
        return 0;
    }

    public void func_35414_a(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
    {
    }

    protected static Random itemRand = new Random();
    public static Item itemsList[] = new Item[32000];
    public static Item shovelSteel;
    public static Item pickaxeSteel;
    public static Item axeSteel;
    public static Item flintAndSteel = (new ItemFlintAndSteel(3)).setIconCoord(5, 0).setItemName("flintAndSteel");
    public static Item appleRed = (new ItemFood(4, 4, 0.3F, false)).setIconCoord(10, 0).setItemName("apple");
    public static Item bow = (new ItemBow(5)).setIconCoord(5, 1).setItemName("bow");
    public static Item arrow = (new Item(6)).setIconCoord(5, 2).setItemName("arrow");
    public static Item coal = (new ItemCoal(7)).setIconCoord(7, 0).setItemName("coal");
    public static Item diamond = (new Item(8)).setIconCoord(7, 3).setItemName("emerald");
    public static Item ingotIron = (new Item(9)).setIconCoord(7, 1).setItemName("ingotIron");
    public static Item ingotGold = (new Item(10)).setIconCoord(7, 2).setItemName("ingotGold");
    public static Item swordSteel;
    public static Item swordWood;
    public static Item shovelWood;
    public static Item pickaxeWood;
    public static Item axeWood;
    public static Item swordStone;
    public static Item shovelStone;
    public static Item pickaxeStone;
    public static Item axeStone;
    public static Item swordDiamond;
    public static Item shovelDiamond;
    public static Item pickaxeDiamond;
    public static Item axeDiamond;
    public static Item stick = (new Item(24)).setIconCoord(5, 3).setFull3D().setItemName("stick");
    public static Item bowlEmpty = (new Item(25)).setIconCoord(7, 4).setItemName("bowl");
    public static Item bowlSoup = (new ItemSoup(26, 8)).setIconCoord(8, 4).setItemName("mushroomStew");
    public static Item swordGold;
    public static Item shovelGold;
    public static Item pickaxeGold;
    public static Item axeGold;
    public static Item silk = (new Item(31)).setIconCoord(8, 0).setItemName("string");
    public static Item feather = (new Item(32)).setIconCoord(8, 1).setItemName("feather");
    public static Item gunpowder = (new Item(33)).setIconCoord(8, 2).setItemName("sulphur");
    public static Item hoeWood;
    public static Item hoeStone;
    public static Item hoeSteel;
    public static Item hoeDiamond;
    public static Item hoeGold;
    public static Item seeds;
    public static Item wheat = (new Item(40)).setIconCoord(9, 1).setItemName("wheat");
    public static Item bread = (new ItemFood(41, 5, 0.6F, false)).setIconCoord(9, 2).setItemName("bread");
    public static Item helmetLeather = (new ItemArmor(42, 0, 0, 0)).setIconCoord(0, 0).setItemName("helmetCloth");
    public static Item plateLeather = (new ItemArmor(43, 0, 0, 1)).setIconCoord(0, 1).setItemName("chestplateCloth");
    public static Item legsLeather = (new ItemArmor(44, 0, 0, 2)).setIconCoord(0, 2).setItemName("leggingsCloth");
    public static Item bootsLeather = (new ItemArmor(45, 0, 0, 3)).setIconCoord(0, 3).setItemName("bootsCloth");
    public static Item helmetChain = (new ItemArmor(46, 1, 1, 0)).setIconCoord(1, 0).setItemName("helmetChain");
    public static Item plateChain = (new ItemArmor(47, 1, 1, 1)).setIconCoord(1, 1).setItemName("chestplateChain");
    public static Item legsChain = (new ItemArmor(48, 1, 1, 2)).setIconCoord(1, 2).setItemName("leggingsChain");
    public static Item bootsChain = (new ItemArmor(49, 1, 1, 3)).setIconCoord(1, 3).setItemName("bootsChain");
    public static Item helmetSteel = (new ItemArmor(50, 2, 2, 0)).setIconCoord(2, 0).setItemName("helmetIron");
    public static Item plateSteel = (new ItemArmor(51, 2, 2, 1)).setIconCoord(2, 1).setItemName("chestplateIron");
    public static Item legsSteel = (new ItemArmor(52, 2, 2, 2)).setIconCoord(2, 2).setItemName("leggingsIron");
    public static Item bootsSteel = (new ItemArmor(53, 2, 2, 3)).setIconCoord(2, 3).setItemName("bootsIron");
    public static Item helmetDiamond = (new ItemArmor(54, 3, 3, 0)).setIconCoord(3, 0).setItemName("helmetDiamond");
    public static Item plateDiamond = (new ItemArmor(55, 3, 3, 1)).setIconCoord(3, 1).setItemName("chestplateDiamond");
    public static Item legsDiamond = (new ItemArmor(56, 3, 3, 2)).setIconCoord(3, 2).setItemName("leggingsDiamond");
    public static Item bootsDiamond = (new ItemArmor(57, 3, 3, 3)).setIconCoord(3, 3).setItemName("bootsDiamond");
    public static Item helmetGold = (new ItemArmor(58, 1, 4, 0)).setIconCoord(4, 0).setItemName("helmetGold");
    public static Item plateGold = (new ItemArmor(59, 1, 4, 1)).setIconCoord(4, 1).setItemName("chestplateGold");
    public static Item legsGold = (new ItemArmor(60, 1, 4, 2)).setIconCoord(4, 2).setItemName("leggingsGold");
    public static Item bootsGold = (new ItemArmor(61, 1, 4, 3)).setIconCoord(4, 3).setItemName("bootsGold");
    public static Item flint = (new SpoutItem(62)).setIconCoord(6, 0).setItemName("flint"); //Spout changed to SpoutItemBlock
    public static Item porkRaw = (new ItemFood(63, 3, 0.3F, true)).setIconCoord(7, 5).setItemName("porkchopRaw");
    public static Item porkCooked = (new ItemFood(64, 8, 0.8F, true)).setIconCoord(8, 5).setItemName("porkchopCooked");
    public static Item painting = (new ItemPainting(65)).setIconCoord(10, 1).setItemName("painting");
    public static Item appleGold;
    public static Item sign = (new ItemSign(67)).setIconCoord(10, 2).setItemName("sign");
    public static Item doorWood;
    public static Item bucketEmpty;
    public static Item bucketWater;
    public static Item bucketLava;
    public static Item minecartEmpty = (new ItemMinecart(72, 0)).setIconCoord(7, 8).setItemName("minecart");
    public static Item saddle = (new ItemSaddle(73)).setIconCoord(8, 6).setItemName("saddle");
    public static Item doorSteel;
    public static Item redstone = (new ItemRedstone(75)).setIconCoord(8, 3).setItemName("redstone");
    public static Item snowball = (new ItemSnowball(76)).setIconCoord(14, 0).setItemName("snowball");
    public static Item boat = (new ItemBoat(77)).setIconCoord(8, 8).setItemName("boat");
    public static Item leather = (new Item(78)).setIconCoord(7, 6).setItemName("leather");
    public static Item bucketMilk;
    public static Item brick = (new Item(80)).setIconCoord(6, 1).setItemName("brick");
    public static Item clay = (new Item(81)).setIconCoord(9, 3).setItemName("clay");
    public static Item reed;
    public static Item paper = (new Item(83)).setIconCoord(10, 3).setItemName("paper");
    public static Item book = (new Item(84)).setIconCoord(11, 3).setItemName("book");
    public static Item slimeBall = (new Item(85)).setIconCoord(14, 1).setItemName("slimeball");
    public static Item minecartCrate = (new ItemMinecart(86, 1)).setIconCoord(7, 9).setItemName("minecartChest");
    public static Item minecartPowered = (new ItemMinecart(87, 2)).setIconCoord(7, 10).setItemName("minecartFurnace");
    public static Item egg = (new ItemEgg(88)).setIconCoord(12, 0).setItemName("egg");
    public static Item compass = (new Item(89)).setIconCoord(6, 3).setItemName("compass");
    public static Item fishingRod = (new ItemFishingRod(90)).setIconCoord(5, 4).setItemName("fishingRod");
    public static Item pocketSundial = (new Item(91)).setIconCoord(6, 4).setItemName("clock");
    public static Item lightStoneDust = (new Item(92)).setIconCoord(9, 4).setItemName("yellowDust");
    public static Item fishRaw = (new ItemFood(93, 2, 0.3F, false)).setIconCoord(9, 5).setItemName("fishRaw");
    public static Item fishCooked = (new ItemFood(94, 5, 0.6F, false)).setIconCoord(10, 5).setItemName("fishCooked");
    public static Item dyePowder = (new ItemDye(95)).setIconCoord(14, 4).setItemName("dyePowder");
    public static Item bone = (new Item(96)).setIconCoord(12, 1).setItemName("bone").setFull3D();
    public static Item sugar = (new Item(97)).setIconCoord(13, 0).setItemName("sugar").setFull3D();
    public static Item cake;
    public static Item bed = (new ItemBed(99)).setMaxStackSize(1).setIconCoord(13, 2).setItemName("bed");
    public static Item redstoneRepeater;
    public static Item cookie = (new ItemFood(101, 1, 0.1F, false)).setIconCoord(12, 5).setItemName("cookie");
    public static ItemMap map = (ItemMap)(new ItemMap(102)).setIconCoord(12, 3).setItemName("map");
    public static ItemShears shears = (ItemShears)(new ItemShears(103)).setIconCoord(13, 5).setItemName("shears");
    public static Item melon = (new ItemFood(104, 2, 0.3F, false)).setIconCoord(13, 6).setItemName("melon");
    public static Item pumpkinSeeds;
    public static Item melonSeeds;
    public static Item beefRaw = (new ItemFood(107, 3, 0.3F, true)).setIconCoord(9, 6).setItemName("beefRaw");
    public static Item beefCooked = (new ItemFood(108, 8, 0.8F, true)).setIconCoord(10, 6).setItemName("beefCooked");
    public static Item chickenRaw;
    public static Item chickenCooked = (new ItemFood(110, 6, 0.6F, true)).setIconCoord(10, 7).setItemName("chickenCooked");
    public static Item rottenFlesh;
    public static Item enderPearl = (new Item(112)).setIconCoord(11, 6).setItemName("enderPearl");
    public static Item record13 = (new ItemRecord(2000, "13")).setIconCoord(0, 15).setItemName("record");
    public static Item recordCat = (new ItemRecord(2001, "cat")).setIconCoord(1, 15).setItemName("record");
    public final int shiftedIndex;
    protected int maxStackSize;
    private int maxDamage;
    protected int iconIndex;
    protected boolean bFull3D;
    protected boolean hasSubtypes;
    private Item containerItem;
    private String itemName;

    static 
    {
        shovelSteel = (new ItemSpade(0, EnumToolMaterial.IRON)).setIconCoord(2, 5).setItemName("shovelIron");
        pickaxeSteel = (new ItemPickaxe(1, EnumToolMaterial.IRON)).setIconCoord(2, 6).setItemName("pickaxeIron");
        axeSteel = (new ItemAxe(2, EnumToolMaterial.IRON)).setIconCoord(2, 7).setItemName("hatchetIron");
        swordSteel = (new ItemSword(11, EnumToolMaterial.IRON)).setIconCoord(2, 4).setItemName("swordIron");
        swordWood = (new ItemSword(12, EnumToolMaterial.WOOD)).setIconCoord(0, 4).setItemName("swordWood");
        shovelWood = (new ItemSpade(13, EnumToolMaterial.WOOD)).setIconCoord(0, 5).setItemName("shovelWood");
        pickaxeWood = (new ItemPickaxe(14, EnumToolMaterial.WOOD)).setIconCoord(0, 6).setItemName("pickaxeWood");
        axeWood = (new ItemAxe(15, EnumToolMaterial.WOOD)).setIconCoord(0, 7).setItemName("hatchetWood");
        swordStone = (new ItemSword(16, EnumToolMaterial.STONE)).setIconCoord(1, 4).setItemName("swordStone");
        shovelStone = (new ItemSpade(17, EnumToolMaterial.STONE)).setIconCoord(1, 5).setItemName("shovelStone");
        pickaxeStone = (new ItemPickaxe(18, EnumToolMaterial.STONE)).setIconCoord(1, 6).setItemName("pickaxeStone");
        axeStone = (new ItemAxe(19, EnumToolMaterial.STONE)).setIconCoord(1, 7).setItemName("hatchetStone");
        swordDiamond = (new ItemSword(20, EnumToolMaterial.EMERALD)).setIconCoord(3, 4).setItemName("swordDiamond");
        shovelDiamond = (new ItemSpade(21, EnumToolMaterial.EMERALD)).setIconCoord(3, 5).setItemName("shovelDiamond");
        pickaxeDiamond = (new ItemPickaxe(22, EnumToolMaterial.EMERALD)).setIconCoord(3, 6).setItemName("pickaxeDiamond");
        axeDiamond = (new ItemAxe(23, EnumToolMaterial.EMERALD)).setIconCoord(3, 7).setItemName("hatchetDiamond");
        swordGold = (new ItemSword(27, EnumToolMaterial.GOLD)).setIconCoord(4, 4).setItemName("swordGold");
        shovelGold = (new ItemSpade(28, EnumToolMaterial.GOLD)).setIconCoord(4, 5).setItemName("shovelGold");
        pickaxeGold = (new ItemPickaxe(29, EnumToolMaterial.GOLD)).setIconCoord(4, 6).setItemName("pickaxeGold");
        axeGold = (new ItemAxe(30, EnumToolMaterial.GOLD)).setIconCoord(4, 7).setItemName("hatchetGold");
        hoeWood = (new ItemHoe(34, EnumToolMaterial.WOOD)).setIconCoord(0, 8).setItemName("hoeWood");
        hoeStone = (new ItemHoe(35, EnumToolMaterial.STONE)).setIconCoord(1, 8).setItemName("hoeStone");
        hoeSteel = (new ItemHoe(36, EnumToolMaterial.IRON)).setIconCoord(2, 8).setItemName("hoeIron");
        hoeDiamond = (new ItemHoe(37, EnumToolMaterial.EMERALD)).setIconCoord(3, 8).setItemName("hoeDiamond");
        hoeGold = (new ItemHoe(38, EnumToolMaterial.GOLD)).setIconCoord(4, 8).setItemName("hoeGold");
        seeds = (new ItemSeeds(39, Block.crops.blockID)).setIconCoord(9, 0).setItemName("seeds");
        appleGold = (new ItemFood(66, 10, 1.2F, false)).func_35424_o().func_35425_a(Potion.potionRegeneration.id, 30, 0, 1.0F).setIconCoord(11, 0).setItemName("appleGold");
        doorWood = (new ItemDoor(68, Material.wood)).setIconCoord(11, 2).setItemName("doorWood");
        bucketEmpty = (new ItemBucket(69, 0)).setIconCoord(10, 4).setItemName("bucket");
        bucketWater = (new ItemBucket(70, Block.waterMoving.blockID)).setIconCoord(11, 4).setItemName("bucketWater").setContainerItem(bucketEmpty);
        bucketLava = (new ItemBucket(71, Block.lavaMoving.blockID)).setIconCoord(12, 4).setItemName("bucketLava").setContainerItem(bucketEmpty);
        doorSteel = (new ItemDoor(74, Material.iron)).setIconCoord(12, 2).setItemName("doorIron");
        bucketMilk = (new ItemBucket(79, -1)).setIconCoord(13, 4).setItemName("milk").setContainerItem(bucketEmpty);
        reed = (new ItemReed(82, Block.reed)).setIconCoord(11, 1).setItemName("reeds");
        cake = (new ItemReed(98, Block.cake)).setMaxStackSize(1).setIconCoord(13, 1).setItemName("cake");
        redstoneRepeater = (new ItemReed(100, Block.redstoneRepeaterIdle)).setIconCoord(6, 5).setItemName("diode");
        pumpkinSeeds = (new ItemSeeds(105, Block.pumpkinStem.blockID)).setIconCoord(13, 3).setItemName("seeds_pumpkin");
        melonSeeds = (new ItemSeeds(106, Block.field_35283_bu.blockID)).setIconCoord(14, 3).setItemName("seeds_melon");
        chickenRaw = (new ItemFood(109, 2, 0.3F, true)).func_35425_a(Potion.potionHunger.id, 30, 0, 0.3F).setIconCoord(9, 7).setItemName("chickenRaw");
        rottenFlesh = (new ItemFood(111, 4, 0.1F, true)).func_35425_a(Potion.potionHunger.id, 30, 0, 0.8F).setIconCoord(11, 5).setItemName("rottenFlesh");
        StatList.func_25151_b();
    }
}
