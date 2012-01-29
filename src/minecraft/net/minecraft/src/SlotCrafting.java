package net.minecraft.src;

public class SlotCrafting extends Slot {
	private final IInventory craftMatrix;
	private EntityPlayer thePlayer;

	public SlotCrafting(EntityPlayer entityplayer, IInventory iinventory, IInventory iinventory1, int i, int j, int k) {
		super(iinventory1, i, j, k);
		thePlayer = entityplayer;
		craftMatrix = iinventory;
	}

	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}

	public void onPickupFromSlot(ItemStack itemstack) {
		itemstack.onCrafting(thePlayer.worldObj, thePlayer);
		if (itemstack.itemID == Block.workbench.blockID) {
			thePlayer.addStat(AchievementList.buildWorkBench, 1);
		}
		else if (itemstack.itemID == Item.pickaxeWood.shiftedIndex) {
			thePlayer.addStat(AchievementList.buildPickaxe, 1);
		}
		else if (itemstack.itemID == Block.stoneOvenIdle.blockID) {
			thePlayer.addStat(AchievementList.buildFurnace, 1);
		}
		else if (itemstack.itemID == Item.hoeWood.shiftedIndex) {
			thePlayer.addStat(AchievementList.buildHoe, 1);
		}
		else if (itemstack.itemID == Item.bread.shiftedIndex) {
			thePlayer.addStat(AchievementList.makeBread, 1);
		}
		else if (itemstack.itemID == Item.cake.shiftedIndex) {
			thePlayer.addStat(AchievementList.bakeCake, 1);
		}
		else if (itemstack.itemID == Item.pickaxeStone.shiftedIndex) {
			thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
		}
		else if (itemstack.itemID == Item.swordWood.shiftedIndex) {
			thePlayer.addStat(AchievementList.buildSword, 1);
		}
		else if (itemstack.itemID == Block.enchantmentTable.blockID) {
			thePlayer.addStat(AchievementList.enchantments, 1);
		}
		else if (itemstack.itemID == Block.bookShelf.blockID) {
			thePlayer.addStat(AchievementList.bookcase, 1);
		}
		for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
			ItemStack itemstack1 = craftMatrix.getStackInSlot(i);
			if (itemstack1 == null) {
				continue;
			}
			craftMatrix.decrStackSize(i, 1);
			if (!itemstack1.getItem().hasContainerItem()) {
				continue;
			}
			ItemStack itemstack2 = new ItemStack(itemstack1.getItem().getContainerItem());
			if (itemstack1.getItem().func_46059_i(itemstack1) && thePlayer.inventory.addItemStackToInventory(itemstack2)) {
				continue;
			}
			if (craftMatrix.getStackInSlot(i) == null) {
				craftMatrix.setInventorySlotContents(i, itemstack2);
			}
			else {
				thePlayer.dropPlayerItem(itemstack2);
			}
		}
	}
}
