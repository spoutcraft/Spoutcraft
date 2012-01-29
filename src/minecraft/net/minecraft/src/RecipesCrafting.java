package net.minecraft.src;

public class RecipesCrafting {
	public RecipesCrafting() {
	}

	public void addRecipes(CraftingManager craftingmanager) {
		craftingmanager.addRecipe(new ItemStack(Block.chest), new Object[] {
		            "###", "# #", "###", Character.valueOf('#'), Block.planks
		        });
		craftingmanager.addRecipe(new ItemStack(Block.stoneOvenIdle), new Object[] {
		            "###", "# #", "###", Character.valueOf('#'), Block.cobblestone
		        });
		craftingmanager.addRecipe(new ItemStack(Block.workbench), new Object[] {
		            "##", "##", Character.valueOf('#'), Block.planks
		        });
		craftingmanager.addRecipe(new ItemStack(Block.sandStone), new Object[] {
		            "##", "##", Character.valueOf('#'), Block.sand
		        });
		craftingmanager.addRecipe(new ItemStack(Block.stoneBrick, 4), new Object[] {
		            "##", "##", Character.valueOf('#'), Block.stone
		        });
		craftingmanager.addRecipe(new ItemStack(Block.fenceIron, 16), new Object[] {
		            "###", "###", Character.valueOf('#'), Item.ingotIron
		        });
		craftingmanager.addRecipe(new ItemStack(Block.thinGlass, 16), new Object[] {
		            "###", "###", Character.valueOf('#'), Block.glass
		        });
	}
}
