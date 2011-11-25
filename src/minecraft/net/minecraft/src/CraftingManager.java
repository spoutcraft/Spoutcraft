// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.PrintStream;
import java.util.*;

// Referenced classes of package net.minecraft.src:
//				RecipesTools, RecipesWeapons, RecipesIngots, RecipesFood, 
//				RecipesCrafting, RecipesArmor, RecipesDyes, ItemStack, 
//				Item, Block, RecipeSorter, ShapedRecipes, 
//				ShapelessRecipes, IRecipe, InventoryCrafting

public class CraftingManager
{

	 public static final CraftingManager getInstance()
	 {
		  return instance;
	 }

	 private CraftingManager()
	 {
		  recipes = new ArrayList();
		  (new RecipesTools()).addRecipes(this);
		  (new RecipesWeapons()).addRecipes(this);
		  (new RecipesIngots()).addRecipes(this);
		  (new RecipesFood()).addRecipes(this);
		  (new RecipesCrafting()).addRecipes(this);
		  (new RecipesArmor()).addRecipes(this);
		  (new RecipesDyes()).addRecipes(this);
		  addRecipe(new ItemStack(Item.paper, 3), new Object[] {
				"###", Character.valueOf('#'), Item.reed
		  });
		  addRecipe(new ItemStack(Item.book, 1), new Object[] {
				"#", "#", "#", Character.valueOf('#'), Item.paper
		  });
		  addRecipe(new ItemStack(Block.fence, 2), new Object[] {
				"###", "###", Character.valueOf('#'), Item.stick
		  });
		  addRecipe(new ItemStack(Block.fenceGate, 1), new Object[] {
				"#W#", "#W#", Character.valueOf('#'), Item.stick, Character.valueOf('W'), Block.planks
		  });
		  addRecipe(new ItemStack(Block.jukebox, 1), new Object[] {
				"###", "#X#", "###", Character.valueOf('#'), Block.planks, Character.valueOf('X'), Item.diamond
		  });
		  addRecipe(new ItemStack(Block.music, 1), new Object[] {
				"###", "#X#", "###", Character.valueOf('#'), Block.planks, Character.valueOf('X'), Item.redstone
		  });
		  addRecipe(new ItemStack(Block.bookShelf, 1), new Object[] {
				"###", "XXX", "###", Character.valueOf('#'), Block.planks, Character.valueOf('X'), Item.book
		  });
		  addRecipe(new ItemStack(Block.blockSnow, 1), new Object[] {
				"##", "##", Character.valueOf('#'), Item.snowball
		  });
		  addRecipe(new ItemStack(Block.blockClay, 1), new Object[] {
				"##", "##", Character.valueOf('#'), Item.clay
		  });
		  addRecipe(new ItemStack(Block.brick, 1), new Object[] {
				"##", "##", Character.valueOf('#'), Item.brick
		  });
		  addRecipe(new ItemStack(Block.glowStone, 1), new Object[] {
				"##", "##", Character.valueOf('#'), Item.lightStoneDust
		  });
		  addRecipe(new ItemStack(Block.cloth, 1), new Object[] {
				"##", "##", Character.valueOf('#'), Item.silk
		  });
		  addRecipe(new ItemStack(Block.tnt, 1), new Object[] {
				"X#X", "#X#", "X#X", Character.valueOf('X'), Item.gunpowder, Character.valueOf('#'), Block.sand
		  });
		  addRecipe(new ItemStack(Block.stairSingle, 3, 3), new Object[] {
				"###", Character.valueOf('#'), Block.cobblestone
		  });
		  addRecipe(new ItemStack(Block.stairSingle, 3, 0), new Object[] {
				"###", Character.valueOf('#'), Block.stone
		  });
		  addRecipe(new ItemStack(Block.stairSingle, 3, 1), new Object[] {
				"###", Character.valueOf('#'), Block.sandStone
		  });
		  addRecipe(new ItemStack(Block.stairSingle, 3, 2), new Object[] {
				"###", Character.valueOf('#'), Block.planks
		  });
		  addRecipe(new ItemStack(Block.stairSingle, 3, 4), new Object[] {
				"###", Character.valueOf('#'), Block.brick
		  });
		  addRecipe(new ItemStack(Block.stairSingle, 3, 5), new Object[] {
				"###", Character.valueOf('#'), Block.stoneBrick
		  });
		  addRecipe(new ItemStack(Block.ladder, 2), new Object[] {
				"# #", "###", "# #", Character.valueOf('#'), Item.stick
		  });
		  addRecipe(new ItemStack(Item.doorWood, 1), new Object[] {
				"##", "##", "##", Character.valueOf('#'), Block.planks
		  });
		  addRecipe(new ItemStack(Block.trapdoor, 2), new Object[] {
				"###", "###", Character.valueOf('#'), Block.planks
		  });
		  addRecipe(new ItemStack(Item.doorSteel, 1), new Object[] {
				"##", "##", "##", Character.valueOf('#'), Item.ingotIron
		  });
		  addRecipe(new ItemStack(Item.sign, 1), new Object[] {
				"###", "###", " X ", Character.valueOf('#'), Block.planks, Character.valueOf('X'), Item.stick
		  });
		  addRecipe(new ItemStack(Item.cake, 1), new Object[] {
				"AAA", "BEB", "CCC", Character.valueOf('A'), Item.bucketMilk, Character.valueOf('B'), Item.sugar, Character.valueOf('C'), Item.wheat, Character.valueOf('E'), 
				Item.egg
		  });
		  addRecipe(new ItemStack(Item.sugar, 1), new Object[] {
				"#", Character.valueOf('#'), Item.reed
		  });
		  addRecipe(new ItemStack(Block.planks, 4), new Object[] {
				"#", Character.valueOf('#'), Block.wood
		  });
		  addRecipe(new ItemStack(Item.stick, 4), new Object[] {
				"#", "#", Character.valueOf('#'), Block.planks
		  });
		  addRecipe(new ItemStack(Block.torchWood, 4), new Object[] {
				"X", "#", Character.valueOf('X'), Item.coal, Character.valueOf('#'), Item.stick
		  });
		  addRecipe(new ItemStack(Block.torchWood, 4), new Object[] {
				"X", "#", Character.valueOf('X'), new ItemStack(Item.coal, 1, 1), Character.valueOf('#'), Item.stick
		  });
		  addRecipe(new ItemStack(Item.bowlEmpty, 4), new Object[] {
				"# #", " # ", Character.valueOf('#'), Block.planks
		  });
		  addRecipe(new ItemStack(Block.rail, 16), new Object[] {
				"X X", "X#X", "X X", Character.valueOf('X'), Item.ingotIron, Character.valueOf('#'), Item.stick
		  });
		  addRecipe(new ItemStack(Block.railPowered, 6), new Object[] {
				"X X", "X#X", "XRX", Character.valueOf('X'), Item.ingotGold, Character.valueOf('R'), Item.redstone, Character.valueOf('#'), Item.stick
		  });
		  addRecipe(new ItemStack(Block.railDetector, 6), new Object[] {
				"X X", "X#X", "XRX", Character.valueOf('X'), Item.ingotIron, Character.valueOf('R'), Item.redstone, Character.valueOf('#'), Block.pressurePlateStone
		  });
		  addRecipe(new ItemStack(Item.minecartEmpty, 1), new Object[] {
				"# #", "###", Character.valueOf('#'), Item.ingotIron
		  });
		  addRecipe(new ItemStack(Block.pumpkinLantern, 1), new Object[] {
				"A", "B", Character.valueOf('A'), Block.pumpkin, Character.valueOf('B'), Block.torchWood
		  });
		  addRecipe(new ItemStack(Item.minecartCrate, 1), new Object[] {
				"A", "B", Character.valueOf('A'), Block.chest, Character.valueOf('B'), Item.minecartEmpty
		  });
		  addRecipe(new ItemStack(Item.minecartPowered, 1), new Object[] {
				"A", "B", Character.valueOf('A'), Block.stoneOvenIdle, Character.valueOf('B'), Item.minecartEmpty
		  });
		  addRecipe(new ItemStack(Item.boat, 1), new Object[] {
				"# #", "###", Character.valueOf('#'), Block.planks
		  });
		  addRecipe(new ItemStack(Item.bucketEmpty, 1), new Object[] {
				"# #", " # ", Character.valueOf('#'), Item.ingotIron
		  });
		  addRecipe(new ItemStack(Item.flintAndSteel, 1), new Object[] {
				"A ", " B", Character.valueOf('A'), Item.ingotIron, Character.valueOf('B'), Item.flint
		  });
		  addRecipe(new ItemStack(Item.bread, 1), new Object[] {
				"###", Character.valueOf('#'), Item.wheat
		  });
		  addRecipe(new ItemStack(Block.stairCompactPlanks, 4), new Object[] {
				"#  ", "## ", "###", Character.valueOf('#'), Block.planks
		  });
		  addRecipe(new ItemStack(Item.fishingRod, 1), new Object[] {
				"  #", " #X", "# X", Character.valueOf('#'), Item.stick, Character.valueOf('X'), Item.silk
		  });
		  addRecipe(new ItemStack(Block.stairCompactCobblestone, 4), new Object[] {
				"#  ", "## ", "###", Character.valueOf('#'), Block.cobblestone
		  });
		  addRecipe(new ItemStack(Block.stairsBrick, 4), new Object[] {
				"#  ", "## ", "###", Character.valueOf('#'), Block.brick
		  });
		  addRecipe(new ItemStack(Block.stairsStoneBrickSmooth, 4), new Object[] {
				"#  ", "## ", "###", Character.valueOf('#'), Block.stoneBrick
		  });
		  addRecipe(new ItemStack(Item.painting, 1), new Object[] {
				"###", "#X#", "###", Character.valueOf('#'), Item.stick, Character.valueOf('X'), Block.cloth
		  });
		  addRecipe(new ItemStack(Item.appleGold, 1), new Object[] {
				"###", "#X#", "###", Character.valueOf('#'), Block.blockGold, Character.valueOf('X'), Item.appleRed
		  });
		  addRecipe(new ItemStack(Block.lever, 1), new Object[] {
				"X", "#", Character.valueOf('#'), Block.cobblestone, Character.valueOf('X'), Item.stick
		  });
		  addRecipe(new ItemStack(Block.torchRedstoneActive, 1), new Object[] {
				"X", "#", Character.valueOf('#'), Item.stick, Character.valueOf('X'), Item.redstone
		  });
		  addRecipe(new ItemStack(Item.redstoneRepeater, 1), new Object[] {
				"#X#", "III", Character.valueOf('#'), Block.torchRedstoneActive, Character.valueOf('X'), Item.redstone, Character.valueOf('I'), Block.stone
		  });
		  addRecipe(new ItemStack(Item.pocketSundial, 1), new Object[] {
				" # ", "#X#", " # ", Character.valueOf('#'), Item.ingotGold, Character.valueOf('X'), Item.redstone
		  });
		  addRecipe(new ItemStack(Item.compass, 1), new Object[] {
				" # ", "#X#", " # ", Character.valueOf('#'), Item.ingotIron, Character.valueOf('X'), Item.redstone
		  });
		  addRecipe(new ItemStack(Item.map, 1), new Object[] {
				"###", "#X#", "###", Character.valueOf('#'), Item.paper, Character.valueOf('X'), Item.compass
		  });
		  addRecipe(new ItemStack(Block.button, 1), new Object[] {
				"#", "#", Character.valueOf('#'), Block.stone
		  });
		  addRecipe(new ItemStack(Block.pressurePlateStone, 1), new Object[] {
				"##", Character.valueOf('#'), Block.stone
		  });
		  addRecipe(new ItemStack(Block.pressurePlatePlanks, 1), new Object[] {
				"##", Character.valueOf('#'), Block.planks
		  });
		  addRecipe(new ItemStack(Block.dispenser, 1), new Object[] {
				"###", "#X#", "#R#", Character.valueOf('#'), Block.cobblestone, Character.valueOf('X'), Item.bow, Character.valueOf('R'), Item.redstone
		  });
		  addRecipe(new ItemStack(Block.pistonBase, 1), new Object[] {
				"TTT", "#X#", "#R#", Character.valueOf('#'), Block.cobblestone, Character.valueOf('X'), Item.ingotIron, Character.valueOf('R'), Item.redstone, Character.valueOf('T'), 
				Block.planks
		  });
		  addRecipe(new ItemStack(Block.pistonStickyBase, 1), new Object[] {
				"S", "P", Character.valueOf('S'), Item.slimeBall, Character.valueOf('P'), Block.pistonBase
		  });
		  addRecipe(new ItemStack(Item.bed, 1), new Object[] {
				"###", "XXX", Character.valueOf('#'), Block.cloth, Character.valueOf('X'), Block.planks
		  });
		  Collections.sort(recipes, new RecipeSorter(this));
		  System.out.println((new StringBuilder()).append(recipes.size()).append(" recipes").toString());
	 }

	 public void addRecipe(ItemStack itemstack, Object aobj[]) //Spout default -> public
	 {
		  String s = "";
		  int i = 0;
		  int j = 0;
		  int k = 0;
		  if(aobj[i] instanceof String[])
		  {
				String as[] = (String[])aobj[i++];
				for(int l = 0; l < as.length; l++)
				{
					 String s2 = as[l];
					 k++;
					 j = s2.length();
					 s = (new StringBuilder()).append(s).append(s2).toString();
				}

		  } else
		  {
				while(aobj[i] instanceof String) 
				{
					 String s1 = (String)aobj[i++];
					 k++;
					 j = s1.length();
					 s = (new StringBuilder()).append(s).append(s1).toString();
				}
		  }
		  HashMap hashmap = new HashMap();
		  for(; i < aobj.length; i += 2)
		  {
				Character character = (Character)aobj[i];
				ItemStack itemstack1 = null;
				if(aobj[i + 1] instanceof Item)
				{
					 itemstack1 = new ItemStack((Item)aobj[i + 1]);
				} else
				if(aobj[i + 1] instanceof Block)
				{
					 itemstack1 = new ItemStack((Block)aobj[i + 1], 1, -1);
				} else
				if(aobj[i + 1] instanceof ItemStack)
				{
					 itemstack1 = (ItemStack)aobj[i + 1];
				}
				hashmap.put(character, itemstack1);
		  }

		  ItemStack aitemstack[] = new ItemStack[j * k];
		  for(int i1 = 0; i1 < j * k; i1++)
		  {
				char c = s.charAt(i1);
				if(hashmap.containsKey(Character.valueOf(c)))
				{
					 aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c))).copy();
				} else
				{
					 aitemstack[i1] = null;
				}
		  }

		  recipes.add(new ShapedRecipes(j, k, aitemstack, itemstack));
	 }

	 public void addShapelessRecipe(ItemStack itemstack, Object aobj[]) //Spout default -> public
	 {
		  ArrayList arraylist = new ArrayList();
		  Object aobj1[] = aobj;
		  int i = aobj1.length;
		  for(int j = 0; j < i; j++)
		  {
				Object obj = aobj1[j];
				if(obj instanceof ItemStack)
				{
					 arraylist.add(((ItemStack)obj).copy());
					 continue;
				}
				if(obj instanceof Item)
				{
					 arraylist.add(new ItemStack((Item)obj));
					 continue;
				}
				if(obj instanceof Block)
				{
					 arraylist.add(new ItemStack((Block)obj));
				} else
				{
					 throw new RuntimeException("Invalid shapeless recipy!");
				}
		  }

		  recipes.add(new ShapelessRecipes(itemstack, arraylist));
	 }

	 public ItemStack findMatchingRecipe(InventoryCrafting inventorycrafting)
	 {
		  for(int i = 0; i < recipes.size(); i++)
		  {
				IRecipe irecipe = (IRecipe)recipes.get(i);
				if(irecipe.matches(inventorycrafting))
				{
					 return irecipe.getCraftingResult(inventorycrafting);
				}
		  }

		  return null;
	 }

	 public List getRecipeList()
	 {
		  return recipes;
	 }

	 private static final CraftingManager instance = new CraftingManager();
	 private List recipes;

}
