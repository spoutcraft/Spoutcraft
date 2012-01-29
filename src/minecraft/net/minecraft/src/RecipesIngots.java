package net.minecraft.src;

public class RecipesIngots {
	private Object recipeItems[][];

	public RecipesIngots() {
		recipeItems = (new Object[][] {
		            new Object[] {
		                Block.blockGold, new ItemStack(Item.ingotGold, 9)
		            }, new Object[] {
		                Block.blockSteel, new ItemStack(Item.ingotIron, 9)
		            }, new Object[] {
		                Block.blockDiamond, new ItemStack(Item.diamond, 9)
		            }, new Object[] {
		                Block.blockLapis, new ItemStack(Item.dyePowder, 9, 4)
		            }
		        });
	}

	public void addRecipes(CraftingManager craftingmanager) {
		for (int i = 0; i < recipeItems.length; i++) {
			Block block = (Block)recipeItems[i][0];
			ItemStack itemstack = (ItemStack)recipeItems[i][1];
			craftingmanager.addRecipe(new ItemStack(block), new Object[] {
			            "###", "###", "###", Character.valueOf('#'), itemstack
			        });
			craftingmanager.addRecipe(itemstack, new Object[] {
			            "#", Character.valueOf('#'), block
			        });
		}

		craftingmanager.addRecipe(new ItemStack(Item.ingotGold), new Object[] {
		            "###", "###", "###", Character.valueOf('#'), Item.goldNugget
		        });
		craftingmanager.addRecipe(new ItemStack(Item.goldNugget, 9), new Object[] {
		            "#", Character.valueOf('#'), Item.ingotGold
		        });
	}
}
