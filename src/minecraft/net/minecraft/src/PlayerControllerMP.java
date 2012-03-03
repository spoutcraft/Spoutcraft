package net.minecraft.src;

import org.spoutcraft.spoutcraftapi.Spoutcraft; // Spout
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet102WindowClick;
import net.minecraft.src.Packet107CreativeSetSlot;
import net.minecraft.src.Packet108EnchantItem;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.Packet15Place;
import net.minecraft.src.Packet16BlockItemSwitch;
import net.minecraft.src.Packet7UseEntity;
import net.minecraft.src.PlayerController;
import net.minecraft.src.PlayerControllerCreative;
import net.minecraft.src.World;

public class PlayerControllerMP extends PlayerController {

	private int currentBlockX = -1;
	private int currentBlockY = -1;
	private int currentblockZ = -1;
	private float curBlockDamageMP = 0.0F;
	private float prevBlockDamageMP = 0.0F;
	private float stepSoundTickCounter = 0.0F;
	private int blockHitDelay = 0;
	private boolean isHittingBlock = false;
	private boolean creativeMode;
	private NetClientHandler netClientHandler;
	private int currentPlayerItem = 0;

	public PlayerControllerMP(Minecraft par1Minecraft, NetClientHandler par2NetClientHandler) {
		super(par1Minecraft);
		this.netClientHandler = par2NetClientHandler;
	}

	public void setCreative(boolean par1) {
		this.creativeMode = par1;
		if (this.creativeMode) {
			PlayerControllerCreative.enableAbilities(this.mc.thePlayer);
			// Spout start
			Spoutcraft.getClient().getActivePlayer().getMainScreen().toggleSurvivalHUD(false);
			// Spout end
		} else {
			PlayerControllerCreative.disableAbilities(this.mc.thePlayer);
			// Spout start
			Spoutcraft.getClient().getActivePlayer().getMainScreen().toggleSurvivalHUD(true);
			// Spout end
		}

	}

	public void flipPlayer(EntityPlayer par1EntityPlayer) {
		par1EntityPlayer.rotationYaw = -180.0F;
	}

	public boolean shouldDrawHUD() {
		return !this.creativeMode;
	}

	public boolean onPlayerDestroyBlock(int par1, int par2, int par3, int par4) {
		if (this.creativeMode) {
			return super.onPlayerDestroyBlock(par1, par2, par3, par4);
		} else {
			int var5 = this.mc.theWorld.getBlockId(par1, par2, par3);
			boolean var6 = super.onPlayerDestroyBlock(par1, par2, par3, par4);
			ItemStack var7 = this.mc.thePlayer.getCurrentEquippedItem();
			if (var7 != null) {
				var7.onDestroyBlock(var5, par1, par2, par3, this.mc.thePlayer);
				if (var7.stackSize == 0) {
					var7.onItemDestroyedByUse(this.mc.thePlayer);
					this.mc.thePlayer.destroyCurrentEquippedItem();
				}
			}

			return var6;
		}
	}

	public void clickBlock(int par1, int par2, int par3, int par4) {
		if (this.creativeMode) {
			this.netClientHandler.addToSendQueue(new Packet14BlockDig(0, par1, par2, par3, par4));
			PlayerControllerCreative.clickBlockCreative(this.mc, this, par1, par2, par3, par4);
			this.blockHitDelay = 5;
		} else if (!this.isHittingBlock || par1 != this.currentBlockX || par2 != this.currentBlockY || par3 != this.currentblockZ) {
			this.netClientHandler.addToSendQueue(new Packet14BlockDig(0, par1, par2, par3, par4));
			int var5 = this.mc.theWorld.getBlockId(par1, par2, par3);
			if (var5 > 0 && this.curBlockDamageMP == 0.0F) {
				Block.blocksList[var5].onBlockClicked(this.mc.theWorld, par1, par2, par3, this.mc.thePlayer);
			}

			if (var5 > 0 && Block.blocksList[var5].blockStrength(this.mc.thePlayer) >= 1.0F) {
				this.onPlayerDestroyBlock(par1, par2, par3, par4);
			} else {
				this.isHittingBlock = true;
				this.currentBlockX = par1;
				this.currentBlockY = par2;
				this.currentblockZ = par3;
				this.curBlockDamageMP = 0.0F;
				this.prevBlockDamageMP = 0.0F;
				this.stepSoundTickCounter = 0.0F;
			}
		}

	}

	public void resetBlockRemoving() {
		this.curBlockDamageMP = 0.0F;
		this.isHittingBlock = false;
	}

	public void onPlayerDamageBlock(int par1, int par2, int par3, int par4) {
		this.syncCurrentPlayItem();
		if (this.blockHitDelay > 0) {
			--this.blockHitDelay;
		} else if (this.creativeMode) {
			this.blockHitDelay = 5;
			this.netClientHandler.addToSendQueue(new Packet14BlockDig(0, par1, par2, par3, par4));
			PlayerControllerCreative.clickBlockCreative(this.mc, this, par1, par2, par3, par4);
		} else {
			if (par1 == this.currentBlockX && par2 == this.currentBlockY && par3 == this.currentblockZ) {
				int var5 = this.mc.theWorld.getBlockId(par1, par2, par3);
				if (var5 == 0) {
					this.isHittingBlock = false;
					return;
				}

				Block var6 = Block.blocksList[var5];
				this.curBlockDamageMP += var6.blockStrength(this.mc.thePlayer);
				if (this.stepSoundTickCounter % 4.0F == 0.0F && var6 != null) {
					this.mc.sndManager.playSound(var6.stepSound.getStepSound(), (float)par1 + 0.5F, (float)par2 + 0.5F, (float)par3 + 0.5F, (var6.stepSound.getVolume() + 1.0F) / 8.0F, var6.stepSound.getPitch() * 0.5F);
				}

				++this.stepSoundTickCounter;
				if (this.curBlockDamageMP >= 1.0F) {
					this.isHittingBlock = false;
					this.netClientHandler.addToSendQueue(new Packet14BlockDig(2, par1, par2, par3, par4));
					this.onPlayerDestroyBlock(par1, par2, par3, par4);
					this.curBlockDamageMP = 0.0F;
					this.prevBlockDamageMP = 0.0F;
					this.stepSoundTickCounter = 0.0F;
					this.blockHitDelay = 5;
				}
			} else {
				this.clickBlock(par1, par2, par3, par4);
			}

		}
	}

	public void setPartialTime(float par1) {
		if (this.curBlockDamageMP <= 0.0F) {
			this.mc.ingameGUI.damageGuiPartialTime = 0.0F;
			this.mc.renderGlobal.damagePartialTime = 0.0F;
		} else {
			float var2 = this.prevBlockDamageMP + (this.curBlockDamageMP - this.prevBlockDamageMP) * par1;
			this.mc.ingameGUI.damageGuiPartialTime = var2;
			this.mc.renderGlobal.damagePartialTime = var2;
		}

	}

	public float getBlockReachDistance() {
		return this.creativeMode?5.0F:4.5F;
	}

	public void onWorldChange(World par1World) {
		super.onWorldChange(par1World);
	}

	public void updateController() {
		this.syncCurrentPlayItem();
		this.prevBlockDamageMP = this.curBlockDamageMP;
		this.mc.sndManager.playRandomMusicIfReady();
	}

	private void syncCurrentPlayItem() {
		int var1 = this.mc.thePlayer.inventory.currentItem;
		if (var1 != this.currentPlayerItem) {
			this.currentPlayerItem = var1;
			this.netClientHandler.addToSendQueue(new Packet16BlockItemSwitch(this.currentPlayerItem));
		}

	}

	public boolean onPlayerRightClick(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack, int par4, int par5, int par6, int par7) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet15Place(par4, par5, par6, par7, par1EntityPlayer.inventory.getCurrentItem()));
		int var8 = par2World.getBlockId(par4, par5, par6);
		if (var8 > 0 && Block.blocksList[var8].blockActivated(par2World, par4, par5, par6, par1EntityPlayer)) {
			return true;
		} else if (par3ItemStack == null) {
			return false;
		} else if (this.creativeMode) {
			int var9 = par3ItemStack.getItemDamage();
			int var10 = par3ItemStack.stackSize;
			boolean var11 = par3ItemStack.useItem(par1EntityPlayer, par2World, par4, par5, par6, par7);
			par3ItemStack.setItemDamage(var9);
			par3ItemStack.stackSize = var10;
			return var11;
		} else {
			return par3ItemStack.useItem(par1EntityPlayer, par2World, par4, par5, par6, par7);
		}
	}

	public boolean sendUseItem(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet15Place(-1, -1, -1, 255, par1EntityPlayer.inventory.getCurrentItem()));
		// Spout Start
		if (par3ItemStack != null) {
			return super.sendUseItem(par1EntityPlayer, par2World, par3ItemStack);
		}
		return true; // Success on using empty item
		// Spout End
	}

	public EntityPlayer createPlayer(World par1World) {
		return new EntityClientPlayerMP(this.mc, par1World, this.mc.session, this.netClientHandler);
	}

	public void attackEntity(EntityPlayer par1EntityPlayer, Entity par2Entity) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet7UseEntity(par1EntityPlayer.entityId, par2Entity.entityId, 1));
		par1EntityPlayer.attackTargetEntityWithCurrentItem(par2Entity);
	}

	public void interactWithEntity(EntityPlayer par1EntityPlayer, Entity par2Entity) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet7UseEntity(par1EntityPlayer.entityId, par2Entity.entityId, 0));
		par1EntityPlayer.useCurrentItemOnEntity(par2Entity);
	}

	public ItemStack windowClick(int par1, int par2, int par3, boolean par4, EntityPlayer par5EntityPlayer) {
		short var6 = par5EntityPlayer.craftingInventory.getNextTransactionID(par5EntityPlayer.inventory);
		ItemStack var7 = super.windowClick(par1, par2, par3, par4, par5EntityPlayer);
		this.netClientHandler.addToSendQueue(new Packet102WindowClick(par1, par2, par3, par4, var7, var6));
		return var7;
	}

	public void func_40593_a(int par1, int par2) {
		this.netClientHandler.addToSendQueue(new Packet108EnchantItem(par1, par2));
	}

	public void sendSlotPacket(ItemStack par1ItemStack, int par2) {
		if (this.creativeMode) {
			this.netClientHandler.addToSendQueue(new Packet107CreativeSetSlot(par2, par1ItemStack));
		}

	}

	public void func_35639_a(ItemStack par1ItemStack) {
		if (this.creativeMode && par1ItemStack != null) {
			this.netClientHandler.addToSendQueue(new Packet107CreativeSetSlot(-1, par1ItemStack));
		}

	}

	public void func_20086_a(int par1, EntityPlayer par2EntityPlayer) {
		if (par1 != -9999) {
			;
		}
	}

	public void onStoppedUsingItem(EntityPlayer par1EntityPlayer) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet14BlockDig(5, 0, 0, 0, 255));
		super.onStoppedUsingItem(par1EntityPlayer);
	}

	public boolean func_35642_f() {
		return true;
	}

	public boolean isNotCreative() {
		return !this.creativeMode;
	}

	public boolean isInCreativeMode() {
		return this.creativeMode;
	}

	public boolean extendedReach() {
		return this.creativeMode;
	}
}
