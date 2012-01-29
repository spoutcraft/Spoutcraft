package net.minecraft.src;

public class RecipesWeapons {
	private String recipePatterns[][] = {
		{
			"X", "X", "#"
		}
	};
	private Object recipeItems[][];

	public RecipesWeapons() {
		recipeItems = (new Object[][] {
		            new Object[] {
		                Block.planks, Block.cobblestone, Item.ingotIron, Item.diamond, Item.ingotGold
		            }, new Object[] {
		                Item.swordWood, Item.swordStone, Item.swordSteel, Item.swordDiamond, Item.swordGold
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

		craftingmanager.addRecipe(new ItemStack(Item.bow, 1), new Object[] {
		            " #X", "# X", " #X", Character.valueOf('X'), Item.silk, Character.valueOf('#'), Item.stick
		        });
		craftingmanager.addRecipe(new ItemStack(Item.arrow, 4), new Object[] {
		            "X", "#", "Y", Character.valueOf('Y'), Item.feather, Character.valueOf('X'), Item.flint, Character.valueOf('#'), Item.stick
		        });
	}
}
