package net.minecraft.src;

import org.spoutcraft.spoutcraftapi.Spoutcraft;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet102WindowClick;
import net.minecraft.src.Packet107CreativeSetSlot;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.Packet15Place;
import net.minecraft.src.Packet16BlockItemSwitch;
import net.minecraft.src.Packet7UseEntity;
import net.minecraft.src.PlayerController;
import net.minecraft.src.PlayerControllerTest;
import net.minecraft.src.World;

public class PlayerControllerMP extends PlayerController {

	private int currentBlockX = -1;
	private int currentBlockY = -1;
	private int currentblockZ = -1;
	private float curBlockDamageMP = 0.0F;
	private float prevBlockDamageMP = 0.0F;
	private float field_9441_h = 0.0F;
	private int blockHitDelay = 0;
	private boolean isHittingBlock = false;
	private boolean field_35649_k;
	private NetClientHandler netClientHandler;
	private int currentPlayerItem = 0;


	public PlayerControllerMP(Minecraft var1, NetClientHandler var2) {
		super(var1);
		this.netClientHandler = var2;
	}

	public void func_35648_a(boolean var1) {
		this.field_35649_k = var1;
		if(this.field_35649_k) {
			PlayerControllerTest.func_35646_d(this.mc.thePlayer);
			//Spout start
			Spoutcraft.getClient().getActivePlayer().getMainScreen().toggleSurvivalHUD(false);
			//Spout end
		} else {
			PlayerControllerTest.func_35645_e(this.mc.thePlayer);
			//Spout start
			Spoutcraft.getClient().getActivePlayer().getMainScreen().toggleSurvivalHUD(true);
			//Spout end
		}

	}

	public void flipPlayer(EntityPlayer var1) {
		var1.rotationYaw = -180.0F;
	}

	public boolean sendBlockRemoved(int var1, int var2, int var3, int var4) {
		if(this.field_35649_k) {
			return false;
		} else {
			int var5 = this.mc.theWorld.getBlockId(var1, var2, var3);
			boolean var6 = super.sendBlockRemoved(var1, var2, var3, var4);
			ItemStack var7 = this.mc.thePlayer.getCurrentEquippedItem();
			if(var7 != null) {
				var7.onDestroyBlock(var5, var1, var2, var3, this.mc.thePlayer);
				if(var7.stackSize == 0) {
					var7.onItemDestroyedByUse(this.mc.thePlayer);
					this.mc.thePlayer.destroyCurrentEquippedItem();
				}
			}

			return var6;
		}
	}

	public void clickBlock(int var1, int var2, int var3, int var4) {
		if(this.field_35649_k) {
			this.netClientHandler.addToSendQueue(new Packet14BlockDig(0, var1, var2, var3, var4));
			PlayerControllerTest.func_35644_a(this.mc, this, var1, var2, var3, var4);
			this.blockHitDelay = 5;
		} else if(!this.isHittingBlock || var1 != this.currentBlockX || var2 != this.currentBlockY || var3 != this.currentblockZ) {
			this.netClientHandler.addToSendQueue(new Packet14BlockDig(0, var1, var2, var3, var4));
			int var5 = this.mc.theWorld.getBlockId(var1, var2, var3);
			if(var5 > 0 && this.curBlockDamageMP == 0.0F) {
				Block.blocksList[var5].onBlockClicked(this.mc.theWorld, var1, var2, var3, this.mc.thePlayer);
			}

			if(var5 > 0 && Block.blocksList[var5].blockStrength(this.mc.thePlayer) >= 1.0F) {
				this.sendBlockRemoved(var1, var2, var3, var4);
			} else {
				this.isHittingBlock = true;
				this.currentBlockX = var1;
				this.currentBlockY = var2;
				this.currentblockZ = var3;
				this.curBlockDamageMP = 0.0F;
				this.prevBlockDamageMP = 0.0F;
				this.field_9441_h = 0.0F;
			}
		}

	}

	public void resetBlockRemoving() {
		this.curBlockDamageMP = 0.0F;
		this.isHittingBlock = false;
	}

	public void sendBlockRemoving(int var1, int var2, int var3, int var4) {
		this.syncCurrentPlayItem();
		if(this.blockHitDelay > 0) {
			--this.blockHitDelay;
		} else if(this.field_35649_k) {
			this.blockHitDelay = 5;
			this.netClientHandler.addToSendQueue(new Packet14BlockDig(0, var1, var2, var3, var4));
			PlayerControllerTest.func_35644_a(this.mc, this, var1, var2, var3, var4);
		} else {
			if(var1 == this.currentBlockX && var2 == this.currentBlockY && var3 == this.currentblockZ) {
				int var5 = this.mc.theWorld.getBlockId(var1, var2, var3);
				if(var5 == 0) {
					this.isHittingBlock = false;
					return;
				}

				Block var6 = Block.blocksList[var5];
				this.curBlockDamageMP += var6.blockStrength(this.mc.thePlayer);
				if(this.field_9441_h % 4.0F == 0.0F && var6 != null) {
					this.mc.sndManager.playSound(var6.stepSound.stepSoundDir2(), (float)var1 + 0.5F, (float)var2 + 0.5F, (float)var3 + 0.5F, (var6.stepSound.getVolume() + 1.0F) / 8.0F, var6.stepSound.getPitch() * 0.5F);
				}

				++this.field_9441_h;
				if(this.curBlockDamageMP >= 1.0F) {
					this.isHittingBlock = false;
					this.netClientHandler.addToSendQueue(new Packet14BlockDig(2, var1, var2, var3, var4));
					this.sendBlockRemoved(var1, var2, var3, var4);
					this.curBlockDamageMP = 0.0F;
					this.prevBlockDamageMP = 0.0F;
					this.field_9441_h = 0.0F;
					this.blockHitDelay = 5;
				}
			} else {
				this.clickBlock(var1, var2, var3, var4);
			}

		}
	}

	public void setPartialTime(float var1) {
		if(this.curBlockDamageMP <= 0.0F) {
			this.mc.ingameGUI.damageGuiPartialTime = 0.0F;
			this.mc.renderGlobal.damagePartialTime = 0.0F;
		} else {
			float var2 = this.prevBlockDamageMP + (this.curBlockDamageMP - this.prevBlockDamageMP) * var1;
			this.mc.ingameGUI.damageGuiPartialTime = var2;
			this.mc.renderGlobal.damagePartialTime = var2;
		}

	}

	public float getBlockReachDistance() {
		return this.field_35649_k?5.0F:4.0F;
	}

	public void func_717_a(World var1) {
		super.func_717_a(var1);
	}

	public void updateController() {
		this.syncCurrentPlayItem();
		this.prevBlockDamageMP = this.curBlockDamageMP;
		this.mc.sndManager.playRandomMusicIfReady();
	}

	private void syncCurrentPlayItem() {
		int var1 = this.mc.thePlayer.inventory.currentItem;
		if(var1 != this.currentPlayerItem) {
			this.currentPlayerItem = var1;
			this.netClientHandler.addToSendQueue(new Packet16BlockItemSwitch(this.currentPlayerItem));
		}

	}

	public boolean sendPlaceBlock(EntityPlayer var1, World var2, ItemStack var3, int var4, int var5, int var6, int var7) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet15Place(var4, var5, var6, var7, var1.inventory.getCurrentItem()));
		int var8 = var2.getBlockId(var4, var5, var6);
		if(var8 > 0 && Block.blocksList[var8].blockActivated(var2, var4, var5, var6, var1)) {
			return true;
		} else if(var3 == null) {
			return false;
		} else if(this.field_35649_k) {
			int var9 = var3.getItemDamage();
			int var10 = var3.stackSize;
			boolean var11 = var3.useItem(var1, var2, var4, var5, var6, var7);
			var3.setItemDamage(var9);
			var3.stackSize = var10;
			return var11;
		} else {
			return var3.useItem(var1, var2, var4, var5, var6, var7);
		}
	}

	public boolean sendUseItem(EntityPlayer var1, World var2, ItemStack var3) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet15Place(-1, -1, -1, 255, var1.inventory.getCurrentItem()));
		//Spout Start
		if (var3 != null) {
			boolean var4 = super.sendUseItem(var1, var2, var3);
			return var4;
		}
		return true; //Success on using empty item
		//Spout End
	}

	public EntityPlayer createPlayer(World var1) {
		return new EntityClientPlayerMP(this.mc, var1, this.mc.session, this.netClientHandler);
	}

	public void attackEntity(EntityPlayer var1, Entity var2) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet7UseEntity(var1.entityId, var2.entityId, 1));
		var1.attackTargetEntityWithCurrentItem(var2);
	}

	public void interactWithEntity(EntityPlayer var1, Entity var2) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet7UseEntity(var1.entityId, var2.entityId, 0));
		var1.useCurrentItemOnEntity(var2);
	}

	public ItemStack windowClick(int var1, int var2, int var3, boolean var4, EntityPlayer var5) {
		short var6 = var5.craftingInventory.func_20111_a(var5.inventory);
		ItemStack var7 = super.windowClick(var1, var2, var3, var4, var5);
		this.netClientHandler.addToSendQueue(new Packet102WindowClick(var1, var2, var3, var4, var7, var6));
		return var7;
	}

	public void func_35637_a(ItemStack var1, int var2) {
		if(this.field_35649_k) {
			int var3 = -1;
			int var4 = 0;
			int var5 = 0;
			if(var1 != null) {
				var3 = var1.itemID;
				var4 = var1.stackSize > 0?var1.stackSize:1;
				var5 = var1.getItemDamage();
			}

			this.netClientHandler.addToSendQueue(new Packet107CreativeSetSlot(var2, var3, var4, var5));
		}

	}

	public void func_35639_a(ItemStack var1) {
		if(this.field_35649_k && var1 != null) {
			this.netClientHandler.addToSendQueue(new Packet107CreativeSetSlot(-1, var1.itemID, var1.stackSize, var1.getItemDamage()));
		}

	}

	public void func_20086_a(int var1, EntityPlayer var2) {
		if(var1 != -9999) {
			;
		}
	}

	public void func_35638_c(EntityPlayer var1) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet14BlockDig(5, 0, 0, 0, 255));
		super.func_35638_c(var1);
	}

	public boolean func_35642_f() {
		return true;
	}

	public boolean func_35641_g() {
		return !this.field_35649_k;
	}

	public boolean isInCreativeMode() {
		return this.field_35649_k;
	}

	public boolean func_35636_i() {
		return this.field_35649_k;
	}
    
    //Spout Start
	@Override
	public boolean shouldDrawHUD() {
		return !this.field_35649_k;
	}
	//Spout End
}
