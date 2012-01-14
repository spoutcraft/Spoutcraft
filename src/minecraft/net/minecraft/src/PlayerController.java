package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.PlayerControllerCreative;
import net.minecraft.src.World;

public abstract class PlayerController {

	protected final Minecraft mc;
	public boolean isInTestMode = false;

	public PlayerController(Minecraft var1) {
		this.mc = var1;
	}

	public void onWorldChange(World var1) {}

	public abstract void clickBlock(int var1, int var2, int var3, int var4);

	public boolean onPlayerDestroyBlock(int var1, int var2, int var3, int var4) {
		World var5 = this.mc.theWorld;
		Block var6 = Block.blocksList[var5.getBlockId(var1, var2, var3)];
		if(var6 == null) {
			return false;
		}
		else {
			var5.playAuxSFX(2001, var1, var2, var3, var6.blockID + var5.getBlockMetadata(var1, var2, var3) * 256);
			int var7 = var5.getBlockMetadata(var1, var2, var3);
			boolean var8 = var5.setBlockWithNotify(var1, var2, var3, 0);
			if(var6 != null && var8) {
				var6.onBlockDestroyedByPlayer(var5, var1, var2, var3, var7);
			}

			return var8;
		}
	}

	public abstract void onPlayerDamageBlock(int var1, int var2, int var3, int var4);

	public abstract void resetBlockRemoving();

	public void setPartialTime(float var1) {}

	public abstract float getBlockReachDistance();

	public boolean sendUseItem(EntityPlayer var1, World var2, ItemStack var3) {
        //Spout Start
		if (var3 == null) return true;
		//Spout End
		int var4 = var3.stackSize;
		ItemStack var5 = var3.useItemRightClick(var2, var1);
		if(var5 == var3 && (var5 == null || var5.stackSize == var4)) {
			return false;
		}
		else {
			var1.inventory.mainInventory[var1.inventory.currentItem] = var5;
			if(var5.stackSize == 0) {
				var1.inventory.mainInventory[var1.inventory.currentItem] = null;
			}

			return true;
		}
	}

	public void flipPlayer(EntityPlayer var1) {}

	public void updateController() {}

	public abstract boolean shouldDrawHUD();

	public void func_6473_b(EntityPlayer var1) {
		PlayerControllerCreative.disableAbilities(var1);
	}

	public abstract boolean onPlayerRightClick(EntityPlayer var1, World var2, ItemStack var3, int var4, int var5, int var6, int var7);

	public EntityPlayer createPlayer(World var1) {
		return new EntityPlayerSP(this.mc, var1, this.mc.session, var1.worldProvider.worldType);
	}

	public void interactWithEntity(EntityPlayer var1, Entity var2) {
		var1.useCurrentItemOnEntity(var2);
	}

	public void attackEntity(EntityPlayer var1, Entity var2) {
		var1.attackTargetEntityWithCurrentItem(var2);
	}

	public ItemStack windowClick(int var1, int var2, int var3, boolean var4, EntityPlayer var5) {
		return var5.craftingInventory.slotClick(var2, var3, var4, var5);
	}

	public void func_20086_a(int var1, EntityPlayer var2) {
		var2.craftingInventory.onCraftGuiClosed(var2);
		var2.craftingInventory = var2.inventorySlots;
	}

	public void func_40593_a(int var1, int var2) {}

	public boolean func_35643_e() {
		return false;
	}

	public void onStoppedUsingItem(EntityPlayer var1) {
		var1.stopUsingItem();
	}

	public boolean func_35642_f() {
		return false;
	}

	public boolean func_35641_g() {
		return true;
	}

	public boolean isInCreativeMode() {
		return false;
	}

	public boolean extendedReach() {
		return false;
	}

	public void func_35637_a(ItemStack var1, int var2) {}

	public void func_35639_a(ItemStack var1) {}
}
