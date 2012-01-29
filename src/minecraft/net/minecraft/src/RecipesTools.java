package net.minecraft.src;

public class RecipesTools {
	private String recipePatterns[][] = {
		{
			"XXX", " # ", " # "
		}, {
			"X", "#", "#"
		}, {
			"XX", "X#", " #"
		}, {
			"XX", " #", " #"
		}
	};
	private Object recipeItems[][];

	public RecipesTools() {
		recipeItems = (new Object[][] {
		            new Object[] {
		                Block.planks, Block.cobblestone, Item.ingotIron, Item.diamond, Item.ingotGold
		            }, new Object[] {
		                Item.pickaxeWood, Item.pickaxeStone, Item.pickaxeSteel, Item.pickaxeDiamond, Item.pickaxeGold
		            }, new Object[] {
		                Item.shovelWood, Item.shovelStone, Item.shovelSteel, Item.shovelDiamond, Item.shovelGold
		            }, new Object[] {
		                Item.axeWood, Item.axeStone, Item.axeSteel, Item.axeDiamond, Item.axeGold
		            }, new Object[] {
		                Item.hoeWood, Item.hoeStone, Item.hoeSteel, Item.hoeDiamond, Item.hoeGold
		            }
		        });
	}

	public void addRecipes(CraftingManager craftingmanager) {
		for (int i = 0; i < recipeItems[0].length; i++) {
			Object obj = recipeItems[0][i];
			for (int j = 0; j < recipeItems.length - 1; j++) {
				Item item = (Item)recipeItems[j + 1][i];
				craftingmanager.addRecipe(new ItemStack(item), new Object[] {
				            recipePatterns[j], Character.valueOf('#'), Item.stick, Character.valueOf('X'), obj
				        });
			}
		}

		craftingmanager.addRecipe(new ItemStack(Item.shears), new Object[] {
		            " #", "# ", Character.valueOf('#'), Item.ingotIron
		        });
	}
}
