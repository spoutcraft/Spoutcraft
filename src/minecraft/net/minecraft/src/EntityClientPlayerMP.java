package net.minecraft.src;

import net.minecraft.client.Minecraft;
//Spout Start
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.packet.*;
import org.spoutcraft.spoutcraftapi.gui.ScreenType;
//Spout End

public class EntityClientPlayerMP extends EntityPlayerSP {
	public NetClientHandler sendQueue;
	private double oldPosX;
	private double oldMinY;
	private double oldPosY;
	private double oldPosZ;
	private float oldRotationYaw;
	private float oldRotationPitch;
	private boolean wasOnGround = false;
	private boolean shouldStopSneaking = false;
	private boolean wasSneaking = false;
	private int field_71168_co = 0;
	private boolean hasSetHealth = false;

	public EntityClientPlayerMP(Minecraft par1Minecraft, World par2World, Session par3Session, NetClientHandler par4NetClientHandler) {
		super(par1Minecraft, par2World, par3Session, 0);
		this.sendQueue = par4NetClientHandler;
	}

	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		return false;
	}

	public void heal(int par1) {}

	public void onUpdate() {
		if(this.worldObj.blockExists(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ))) {
			super.onUpdate();
			this.sendMotionUpdates();
		}
		
		//Spout start
		if (!this.isSprinting() && runToggle) {
			if (canSprint()) {
				setSprinting(true);
				this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 4));
			}
		}
		//Spout end
	}

	public void sendMotionUpdates() {
		boolean var1 = this.isSprinting();

		if(var1 != this.wasSneaking) {
			if(var1) {
				this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 4));
			} else {
				this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 5));
			}

			this.wasSneaking = var1;
		}

		boolean var2 = this.isSneaking();
		if (var2 != this.shouldStopSneaking) {
			if(var2) {
				this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 1));
			} else {
				this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 2));
			}

			this.shouldStopSneaking = var2;
		}

		double var3 = this.posX - this.oldPosX;
		double var5 = this.boundingBox.minY - this.oldMinY;

		double var7 = this.posY - this.oldPosY;
		double var9 = (double)(this.rotationYaw - this.oldRotationYaw);
		double var11 = (double)(this.rotationPitch - this.oldRotationPitch);
		boolean var13 = var3 * var3 + var5 * var5 + var7 * var7 > 9.0E-4D || this.field_71168_co >= 20;
		boolean var14 = var9 != 0.0D || var11 != 0.0D;

		if(this.ridingEntity != null) {
			if(var14) {
				this.sendQueue.addToSendQueue(new Packet11PlayerPosition(this.motionX, -999.0D, -999.0D, this.motionZ, this.onGround));
			} else {
				this.sendQueue.addToSendQueue(new Packet13PlayerLookMove(this.motionX, -999.0D, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
			}

			var13 = false;
		} else if(var13 && var14) {
			this.sendQueue.addToSendQueue(new Packet13PlayerLookMove(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
		} else if(var13) {
			this.sendQueue.addToSendQueue(new Packet11PlayerPosition(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.onGround));
		} else if (var14) {
			this.sendQueue.addToSendQueue(new Packet12PlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
		} else if (this.wasOnGround != this.onGround) {
			this.sendQueue.addToSendQueue(new Packet10Flying(this.onGround));
		}

		++this.field_71168_co;

		this.wasOnGround = this.onGround;
		if(var13) {
			this.oldPosX = this.posX;
			this.oldMinY = this.boundingBox.minY;
			this.oldPosY = this.posY;
			this.oldPosZ = this.posZ;
			this.field_71168_co = 0;
		}

		if(var14) {
			this.oldRotationYaw = this.rotationYaw;
			this.oldRotationPitch = this.rotationPitch;
		}
	}

	public EntityItem dropOneItem() {
		this.sendQueue.addToSendQueue(new Packet14BlockDig(4, 0, 0, 0, 0));
		return null;
	}

	protected void joinEntityItemWithWorld(EntityItem par1EntityItem) {}

	public void sendChatMessage(String par1Str) {
		this.sendQueue.addToSendQueue(new Packet3Chat(par1Str));
	}

	public void swingItem() {
		super.swingItem();
		this.sendQueue.addToSendQueue(new Packet18Animation(this, 1));
	}

	public void respawnPlayer() {
		this.sendQueue.addToSendQueue(new Packet205ClientCommand(1));
	}

	public void damageEntity(DamageSource par1DamageSource, int par2) { //Spout protected -> public
		this.setEntityHealth(this.getHealth() - par2);
		//Spout start
		GuiChat.interruptChat();
		//Spout end
	}

	public void closeScreen() {
		this.sendQueue.addToSendQueue(new Packet101CloseWindow(this.craftingInventory.windowId));
		this.inventory.setItemStack((ItemStack)null);
		super.closeScreen();
	}

	public void setHealth(int par1) {
		if (this.hasSetHealth) {
			super.setHealth(par1);
		} else {
			this.setEntityHealth(par1);
			this.hasSetHealth = true;
		}
	}

	public void addStat(StatBase par1StatBase, int par2) {
		if(par1StatBase != null) {
			if(par1StatBase.isIndependent) {
				super.addStat(par1StatBase, par2);
			}
		}
	}

	public void incrementStat(StatBase par1StatBase, int par2) {
		if(par1StatBase != null) {
			if(!par1StatBase.isIndependent) {
				super.addStat(par1StatBase, par2);
			}

		}
	}

	public void func_71016_p() {
		this.sendQueue.addToSendQueue(new Packet202PlayerAbilities(this.capabilities));
	}

	public boolean func_71066_bF() {
		return true;
	}

	//Spout Start
	@Override
	public void handleKeyPress(int i, boolean keyReleased) {
		if (SpoutClient.getInstance().isSpoutEnabled()) {
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketKeyPress((byte)i, keyReleased, (MovementInputFromOptions)movementInput, ScreenType.GAME_SCREEN));
		}

		super.handleKeyPress(i, keyReleased);
	}

	@Override
	public void updateCloak() {
		if (this.cloakUrl == null || this.playerCloakUrl == null) {
			super.updateCloak();
		}
	}
	//Spout End
}
