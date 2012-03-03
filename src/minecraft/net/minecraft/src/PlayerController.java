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

	public PlayerController(Minecraft par1Minecraft) {
		this.mc = par1Minecraft;
	}

	public void onWorldChange(World par1World) {}

	public abstract void clickBlock(int var1, int var2, int var3, int var4);

	public boolean onPlayerDestroyBlock(int par1, int par2, int par3, int par4) {
		World var5 = this.mc.theWorld;
		Block var6 = Block.blocksList[var5.getBlockId(par1, par2, par3)];
		if (var6 == null) {
			return false;
		} else {
			var5.playAuxSFX(2001, par1, par2, par3, var6.blockID + (var5.getBlockMetadata(par1, par2, par3) << 12));
			int var7 = var5.getBlockMetadata(par1, par2, par3);
			boolean var8 = var5.setBlockWithNotify(par1, par2, par3, 0);
			if (var6 != null && var8) {
				var6.onBlockDestroyedByPlayer(var5, par1, par2, par3, var7);
			}

			return var8;
		}
	}

	public abstract void onPlayerDamageBlock(int var1, int var2, int var3, int var4);

	public abstract void resetBlockRemoving();

	public void setPartialTime(float par1) {}

	public abstract float getBlockReachDistance();

	public boolean sendUseItem(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack) {
		//Spout Start
		if (par1EntityPlayer == null) return true;
		//Spout End
		int var4 = par3ItemStack.stackSize;
		ItemStack var5 = par3ItemStack.useItemRightClick(par2World, par1EntityPlayer);
		if (var5 == par3ItemStack && (var5 == null || var5.stackSize == var4)) {
			return false;
		} else {
			par1EntityPlayer.inventory.mainInventory[par1EntityPlayer.inventory.currentItem] = var5;
			if (var5.stackSize == 0) {
				par1EntityPlayer.inventory.mainInventory[par1EntityPlayer.inventory.currentItem] = null;
			}

			return true;
		}
	}

	public void flipPlayer(EntityPlayer par1EntityPlayer) {}

	public void updateController() {}

	public abstract boolean shouldDrawHUD();

	public void func_6473_b(EntityPlayer par1EntityPlayer) {
		PlayerControllerCreative.disableAbilities(par1EntityPlayer);
	}

	public abstract boolean onPlayerRightClick(EntityPlayer var1, World var2, ItemStack var3, int var4, int var5, int var6, int var7);

	public EntityPlayer createPlayer(World par1World) {
		return new EntityPlayerSP(this.mc, par1World, this.mc.session, par1World.worldProvider.worldType);
	}

	public void interactWithEntity(EntityPlayer par1EntityPlayer, Entity par2Entity) {
		par1EntityPlayer.useCurrentItemOnEntity(par2Entity);
	}

	public void attackEntity(EntityPlayer par1EntityPlayer, Entity par2Entity) {
		par1EntityPlayer.attackTargetEntityWithCurrentItem(par2Entity);
	}

	public ItemStack windowClick(int par1, int par2, int par3, boolean par4, EntityPlayer par5EntityPlayer) {
		return par5EntityPlayer.craftingInventory.slotClick(par2, par3, par4, par5EntityPlayer);
	}

	public void func_20086_a(int par1, EntityPlayer par2EntityPlayer) {
		par2EntityPlayer.craftingInventory.onCraftGuiClosed(par2EntityPlayer);
		par2EntityPlayer.craftingInventory = par2EntityPlayer.inventorySlots;
	}

	public void func_40593_a(int par1, int par2) {}

	public boolean func_35643_e() {
		return false;
	}

	public void onStoppedUsingItem(EntityPlayer par1EntityPlayer) {
		par1EntityPlayer.stopUsingItem();
	}

	public boolean func_35642_f() {
		return false;
	}

	public boolean isNotCreative() {
		return true;
	}

	public boolean isInCreativeMode() {
		return false;
	}

	public boolean extendedReach() {
		return false;
	}

	public void sendSlotPacket(ItemStack par1ItemStack, int par2) {}

	public void func_35639_a(ItemStack par1ItemStack) {}
}
