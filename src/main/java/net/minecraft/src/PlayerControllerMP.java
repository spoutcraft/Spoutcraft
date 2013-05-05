package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerControllerMP {

	/** The Minecraft instance. */
	private final Minecraft mc;
	private final NetClientHandler netClientHandler;

	/** PosX of the current block being destroyed */
	private int currentBlockX = -1;

	/** PosY of the current block being destroyed */
	private int currentBlockY = -1;

	/** PosZ of the current block being destroyed */
	private int currentblockZ = -1;
	private ItemStack field_85183_f = null;

	/** Current block damage (MP) */
	private float curBlockDamageMP = 0.0F;

	/**
	 * Tick counter, when it hits 4 it resets back to 0 and plays the step sound
	 */
	private float stepSoundTickCounter = 0.0F;

	/**
	 * Delays the first damage on the block after the first click on the block
	 */
	private int blockHitDelay = 0;

	/** Tells if the player is hitting a block */
	private boolean isHittingBlock = false;

	/** Current game type for the player */
	private EnumGameType currentGameType;

	/** Index of the current item held by the player in the inventory hotbar */
	private int currentPlayerItem;

	public PlayerControllerMP(Minecraft par1Minecraft, NetClientHandler par2NetClientHandler) {
		this.currentGameType = EnumGameType.SURVIVAL;
		this.currentPlayerItem = 0;
		this.mc = par1Minecraft;
		this.netClientHandler = par2NetClientHandler;
	}

	/**
	 * Block dig operation in creative mode (instantly digs the block).
	 */
	public static void clickBlockCreative(Minecraft par0Minecraft, PlayerControllerMP par1PlayerControllerMP, int par2, int par3, int par4, int par5) {
		if (!par0Minecraft.theWorld.extinguishFire(par0Minecraft.thePlayer, par2, par3, par4, par5)) {
			par1PlayerControllerMP.onPlayerDestroyBlock(par2, par3, par4, par5);
		}
	}

	/**
	 * Sets player capabilities depending on current gametype. params: player
	 */
	public void setPlayerCapabilities(EntityPlayer par1EntityPlayer) {
		this.currentGameType.configurePlayerCapabilities(par1EntityPlayer.capabilities);
	}

	/**
	 * If modified to return true, the player spins around slowly around (0, 68.5, 0). The GUI is disabled, the view is set
	 * to first person, and both chat and menu are disabled. Unless the server is modified to ignore illegal stances,
	 * attempting to enter a world at all will result in an immediate kick due to an illegal stance. Appears to be left-
	 * over debug, or demo code.
	 */
	public boolean enableEverythingIsScrewedUpMode() {
		return false;
	}

	/**
	 * Sets the game type for the player.
	 */
	public void setGameType(EnumGameType par1EnumGameType) {
		this.currentGameType = par1EnumGameType;
		this.currentGameType.configurePlayerCapabilities(this.mc.thePlayer.capabilities);
	}

	/**
	 * Flips the player around. Args: player
	 */
	public void flipPlayer(EntityPlayer par1EntityPlayer) {
		par1EntityPlayer.rotationYaw = -180.0F;
	}

	public boolean shouldDrawHUD() {
		return this.currentGameType.isSurvivalOrAdventure();
	}

	/**
	 * Called when a player completes the destruction of a block
	 */
	public boolean onPlayerDestroyBlock(int par1, int par2, int par3, int par4) {
		if (this.currentGameType.isAdventure() && !this.mc.thePlayer.canCurrentToolHarvestBlock(par1, par2, par3)) {
			return false;
		} else {
			WorldClient var5 = this.mc.theWorld;
			Block var6 = Block.blocksList[var5.getBlockId(par1, par2, par3)];

			if (var6 == null) {
				return false;
			} else {
				var5.playAuxSFX(2001, par1, par2, par3, var6.blockID + (var5.getBlockMetadata(par1, par2, par3) << 12));
				int var7 = var5.getBlockMetadata(par1, par2, par3);
				boolean var8 = var5.setBlockToAir(par1, par2, par3);

				if (var8) {
					var6.onBlockDestroyedByPlayer(var5, par1, par2, par3, var7);
				}

				this.currentBlockY = -1;

				if (!this.currentGameType.isCreative()) {
					ItemStack var9 = this.mc.thePlayer.getCurrentEquippedItem();

					if (var9 != null) {
						var9.onBlockDestroyed(var5, var6.blockID, par1, par2, par3, this.mc.thePlayer);

						if (var9.stackSize == 0) {
							this.mc.thePlayer.destroyCurrentEquippedItem();
						}
					}
				}

				return var8;
			}
		}
	}

	/**
	 * Called by Minecraft class when the player is hitting a block with an item. Args: x, y, z, side
	 */
	public void clickBlock(int par1, int par2, int par3, int par4) {
		if (!this.currentGameType.isAdventure() || this.mc.thePlayer.canCurrentToolHarvestBlock(par1, par2, par3)) {
			if (this.currentGameType.isCreative()) {
				this.netClientHandler.addToSendQueue(new Packet14BlockDig(0, par1, par2, par3, par4));
				clickBlockCreative(this.mc, this, par1, par2, par3, par4);
				this.blockHitDelay = 5;
			} else if (!this.isHittingBlock || !this.sameToolAndBlock(par1, par2, par3)) {
				if (this.isHittingBlock) {
					this.netClientHandler.addToSendQueue(new Packet14BlockDig(1, this.currentBlockX, this.currentBlockY, this.currentblockZ, par4));
				}

				this.netClientHandler.addToSendQueue(new Packet14BlockDig(0, par1, par2, par3, par4));
				int var5 = this.mc.theWorld.getBlockId(par1, par2, par3);

				if (var5 > 0 && this.curBlockDamageMP == 0.0F) {
					Block.blocksList[var5].onBlockClicked(this.mc.theWorld, par1, par2, par3, this.mc.thePlayer);
				}

				// Spout Start
				if (var5 > 0 && Block.blocksList[var5].getPlayerRelativeBlockHardness(this.mc.thePlayer) >= 1.0F) {
				// Spout End
					this.onPlayerDestroyBlock(par1, par2, par3, par4);
				} else {
					this.isHittingBlock = true;
					this.currentBlockX = par1;
					this.currentBlockY = par2;
					this.currentblockZ = par3;
					this.field_85183_f = this.mc.thePlayer.getHeldItem();
					this.curBlockDamageMP = 0.0F;
					this.stepSoundTickCounter = 0.0F;
					this.mc.theWorld.destroyBlockInWorldPartially(this.mc.thePlayer.entityId, this.currentBlockX, this.currentBlockY, this.currentblockZ, (int)(this.curBlockDamageMP * 10.0F) - 1);
				}
			}
		}
	}

	/**
	 * Resets current block damage and isHittingBlock
	 */
	public void resetBlockRemoving() {
		if (this.isHittingBlock) {
			this.netClientHandler.addToSendQueue(new Packet14BlockDig(1, this.currentBlockX, this.currentBlockY, this.currentblockZ, -1));
		}

		this.isHittingBlock = false;
		this.curBlockDamageMP = 0.0F;
		this.mc.theWorld.destroyBlockInWorldPartially(this.mc.thePlayer.entityId, this.currentBlockX, this.currentBlockY, this.currentblockZ, -1);
	}

	/**
	 * Called when a player damages a block and updates damage counters
	 */
	public void onPlayerDamageBlock(int par1, int par2, int par3, int par4) {
		this.syncCurrentPlayItem();

		if (this.blockHitDelay > 0) {
			--this.blockHitDelay;
		} else if (this.currentGameType.isCreative()) {
			this.blockHitDelay = 5;
			this.netClientHandler.addToSendQueue(new Packet14BlockDig(0, par1, par2, par3, par4));
			clickBlockCreative(this.mc, this, par1, par2, par3, par4);
		} else {
			if (this.sameToolAndBlock(par1, par2, par3)) {
				int var5 = this.mc.theWorld.getBlockId(par1, par2, par3);

				if (var5 == 0) {
					this.isHittingBlock = false;
					return;
				}

				Block var6 = Block.blocksList[var5];
				// Spout Start
				this.curBlockDamageMP += var6.getPlayerRelativeBlockHardness(this.mc.thePlayer);
				// Spout End

				if (this.stepSoundTickCounter % 4.0F == 0.0F && var6 != null) {
					this.mc.sndManager.playSound(var6.stepSound.getStepSound(), (float)par1 + 0.5F, (float)par2 + 0.5F, (float)par3 + 0.5F, (var6.stepSound.getVolume() + 1.0F) / 8.0F, var6.stepSound.getPitch() * 0.5F);
				}

				++this.stepSoundTickCounter;

				if (this.curBlockDamageMP >= 1.0F) {
					this.isHittingBlock = false;
					this.netClientHandler.addToSendQueue(new Packet14BlockDig(2, par1, par2, par3, par4));
					this.onPlayerDestroyBlock(par1, par2, par3, par4);
					this.curBlockDamageMP = 0.0F;
					this.stepSoundTickCounter = 0.0F;
					this.blockHitDelay = 5;
				}

				this.mc.theWorld.destroyBlockInWorldPartially(this.mc.thePlayer.entityId, this.currentBlockX, this.currentBlockY, this.currentblockZ, (int)(this.curBlockDamageMP * 10.0F) - 1);
			} else {
				this.clickBlock(par1, par2, par3, par4);
			}
		}
	}

	/**
	 * player reach distance = 4F
	 */
	public float getBlockReachDistance() {
		return this.currentGameType.isCreative() ? 5.0F : 4.5F;
	}

	public void updateController() {
		this.syncCurrentPlayItem();
		this.mc.sndManager.playRandomMusicIfReady();
	}

	private boolean sameToolAndBlock(int par1, int par2, int par3) {
		ItemStack var4 = this.mc.thePlayer.getHeldItem();
		boolean var5 = this.field_85183_f == null && var4 == null;

		if (this.field_85183_f != null && var4 != null) {
			var5 = var4.itemID == this.field_85183_f.itemID && ItemStack.areItemStackTagsEqual(var4, this.field_85183_f) && (var4.isItemStackDamageable() || var4.getItemDamage() == this.field_85183_f.getItemDamage());
		}

		return par1 == this.currentBlockX && par2 == this.currentBlockY && par3 == this.currentblockZ && var5;
	}

	/**
	 * Syncs the current player item with the server
	 */
	private void syncCurrentPlayItem() {
		int var1 = this.mc.thePlayer.inventory.currentItem;

		if (var1 != this.currentPlayerItem) {
			this.currentPlayerItem = var1;
			this.netClientHandler.addToSendQueue(new Packet16BlockItemSwitch(this.currentPlayerItem));
		}
	}

	/**
	 * Handles a players right click. Args: player, world, x, y, z, side, hitVec
	 */
	public boolean onPlayerRightClick(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack, int par4, int par5, int par6, int par7, Vec3 par8Vec3) {
		this.syncCurrentPlayItem();
		float var9 = (float)par8Vec3.xCoord - (float)par4;
		float var10 = (float)par8Vec3.yCoord - (float)par5;
		float var11 = (float)par8Vec3.zCoord - (float)par6;
		boolean var12 = false;
		int var13;

		if (!par1EntityPlayer.isSneaking() || par1EntityPlayer.getHeldItem() == null) {
			var13 = par2World.getBlockId(par4, par5, par6);

			if (var13 > 0 && Block.blocksList[var13].onBlockActivated(par2World, par4, par5, par6, par1EntityPlayer, par7, var9, var10, var11)) {
				var12 = true;
			}
		}

		if (!var12 && par3ItemStack != null && par3ItemStack.getItem() instanceof ItemBlock) {
			ItemBlock var16 = (ItemBlock)par3ItemStack.getItem();

			if (!var16.canPlaceItemBlockOnSide(par2World, par4, par5, par6, par7, par1EntityPlayer, par3ItemStack)) {
				return false;
			}
		}

		this.netClientHandler.addToSendQueue(new Packet15Place(par4, par5, par6, par7, par1EntityPlayer.inventory.getCurrentItem(), var9, var10, var11));

		if (var12) {
			return true;
		} else if (par3ItemStack == null) {
			return false;
		} else if (this.currentGameType.isCreative()) {
			var13 = par3ItemStack.getItemDamage();
			int var14 = par3ItemStack.stackSize;
			boolean var15 = par3ItemStack.tryPlaceItemIntoWorld(par1EntityPlayer, par2World, par4, par5, par6, par7, var9, var10, var11);
			par3ItemStack.setItemDamage(var13);
			par3ItemStack.stackSize = var14;
			return var15;
		} else {
			return par3ItemStack.tryPlaceItemIntoWorld(par1EntityPlayer, par2World, par4, par5, par6, par7, var9, var10, var11);
		}
	}

	/**
	 * Notifies the server of things like consuming food, etc...
	 */
	public boolean sendUseItem(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet15Place(-1, -1, -1, 255, par1EntityPlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
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

	public EntityClientPlayerMP func_78754_a(World par1World) {
		return new EntityClientPlayerMP(this.mc, par1World, this.mc.session, this.netClientHandler);
	}

	/**
	 * Attacks an entity
	 */
	public void attackEntity(EntityPlayer par1EntityPlayer, Entity par2Entity) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet7UseEntity(par1EntityPlayer.entityId, par2Entity.entityId, 1));
		par1EntityPlayer.attackTargetEntityWithCurrentItem(par2Entity);
	}

	public boolean func_78768_b(EntityPlayer par1EntityPlayer, Entity par2Entity) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet7UseEntity(par1EntityPlayer.entityId, par2Entity.entityId, 0));
		return par1EntityPlayer.interactWith(par2Entity);
	}

	public ItemStack windowClick(int par1, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) {
		short var6 = par5EntityPlayer.openContainer.getNextTransactionID(par5EntityPlayer.inventory);
		ItemStack var7 = par5EntityPlayer.openContainer.slotClick(par2, par3, par4, par5EntityPlayer);
		this.netClientHandler.addToSendQueue(new Packet102WindowClick(par1, par2, par3, par4, var7, var6));
		return var7;
	}

	/**
	 * GuiEnchantment uses this during multiplayer to tell PlayerControllerMP to send a packet indicating the enchantment
	 * action the player has taken.
	 */
	public void sendEnchantPacket(int par1, int par2) {
		this.netClientHandler.addToSendQueue(new Packet108EnchantItem(par1, par2));
	}

	/**
	 * Used in PlayerControllerMP to update the server with an ItemStack in a slot.
	 */
	public void sendSlotPacket(ItemStack par1ItemStack, int par2) {
		if (this.currentGameType.isCreative()) {
			this.netClientHandler.addToSendQueue(new Packet107CreativeSetSlot(par2, par1ItemStack));
		}
	}

	public void func_78752_a(ItemStack par1ItemStack) {
		if (this.currentGameType.isCreative() && par1ItemStack != null) {
			this.netClientHandler.addToSendQueue(new Packet107CreativeSetSlot(-1, par1ItemStack));
		}
	}

	public void onStoppedUsingItem(EntityPlayer par1EntityPlayer) {
		this.syncCurrentPlayItem();
		this.netClientHandler.addToSendQueue(new Packet14BlockDig(5, 0, 0, 0, 255));
		par1EntityPlayer.stopUsingItem();
	}

	public boolean func_78763_f() {
		return true;
	}

	/**
	 * Checks if the player is not creative, used for checking if it should break a block instantly
	 */
	public boolean isNotCreative() {
		return !this.currentGameType.isCreative();
	}

	/**
	 * returns true if player is in creative mode
	 */
	public boolean isInCreativeMode() {
		return this.currentGameType.isCreative();
	}

	/**
	 * true for hitting entities far away.
	 */
	public boolean extendedReach() {
		return this.currentGameType.isCreative();
	}
}
