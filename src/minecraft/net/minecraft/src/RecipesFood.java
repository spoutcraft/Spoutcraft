package net.minecraft.src;

public class RecipesFood {
	public RecipesFood() {
	}

	public void addRecipes(CraftingManager craftingmanager) {
		craftingmanager.addShapelessRecipe(new ItemStack(Item.bowlSoup), new Object[] {
		            Block.mushroomBrown, Block.mushroomRed, Item.bowlEmpty
		        });
		craftingmanager.addRecipe(new ItemStack(Item.cookie, 8), new Object[] {
		            "#X#", Character.valueOf('X'), new ItemStack(Item.dyePowder, 1, 3), Character.valueOf('#'), Item.wheat
		        });
		craftingmanager.addRecipe(new ItemStack(Block.melon), new Object[] {
		            "MMM", "MMM", "MMM", Character.valueOf('M'), Item.melon
		        });
		craftingmanager.addRecipe(new ItemStack(Item.melonSeeds), new Object[] {
		            "M", Character.valueOf('M'), Item.melon
		        });
		craftingmanager.addRecipe(new ItemStack(Item.pumpkinSeeds, 4), new Object[] {
		            "M", Character.valueOf('M'), Block.pumpkin
		        });
		craftingmanager.addShapelessRecipe(new ItemStack(Item.fermentedSpiderEye), new Object[] {
		            Item.spiderEye, Block.mushroomBrown, Item.sugar
		        });
		craftingmanager.addShapelessRecipe(new ItemStack(Item.speckledMelon), new Object[] {
		            Item.melon, Item.goldNugget
		        });
		craftingmanager.addShapelessRecipe(new ItemStack(Item.blazePowder, 2), new Object[] {
		            Item.blazeRod
		        });
		craftingmanager.addShapelessRecipe(new ItemStack(Item.magmaCream), new Object[] {
		            Item.blazePowder, Item.slimeBall
		        });
	}
}
