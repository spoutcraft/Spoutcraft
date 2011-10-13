package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet101CloseWindow;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet12PlayerLook;
import net.minecraft.src.Packet13PlayerLookMove;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.Packet18Animation;
import net.minecraft.src.Packet19EntityAction;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.Packet9Respawn;
import net.minecraft.src.Session;
import net.minecraft.src.StatBase;
import net.minecraft.src.World;
//Spout Start
import org.getspout.spout.packet.*;
import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.gui.ScreenType;
//Spout End

public class EntityClientPlayerMP extends EntityPlayerSP {

	public NetClientHandler sendQueue;
	private int field_9380_bx = 0;
	private boolean field_21093_bH = false;
	private double oldPosX;
	private double field_9378_bz;
	private double oldPosY;
	private double oldPosZ;
	private float oldRotationYaw;
	private float oldRotationPitch;
	private boolean field_9382_bF = false;
	private boolean field_35227_cs = false;
	private boolean wasSneaking = false;
	private int field_12242_bI = 0;


	public EntityClientPlayerMP(Minecraft var1, World var2, Session var3, NetClientHandler var4) {
		super(var1, var2, var3, 0);
		this.sendQueue = var4;
	}

	public boolean attackEntityFrom(DamageSource var1, int var2) {
		return false;
	}

	public void heal(int var1) {}

	public void onUpdate() {
		World var10000 = this.worldObj;
		int var10001 = MathHelper.floor_double(this.posX);
		this.worldObj.getClass();
		if(var10000.blockExists(var10001, 128 / 2, MathHelper.floor_double(this.posZ))) {
			super.onUpdate();
			this.onUpdate2();
		}
	}

	public void onUpdate2() {
		if(this.field_9380_bx++ == 20) {
			this.sendInventoryChanged();
			this.field_9380_bx = 0;
		}

		boolean var1 = this.func_35117_Q();
		if(var1 != this.wasSneaking) {
			if(var1) {
				this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 4));
			} else {
				this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 5));
			}

			this.wasSneaking = var1;
		}

		boolean var2 = this.isSneaking();
		if(var2 != this.field_35227_cs) {
			if(var2) {
				this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 1));
			} else {
				this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 2));
			}

			this.field_35227_cs = var2;
		}

		double var3 = this.posX - this.oldPosX;
		double var5 = this.boundingBox.minY - this.field_9378_bz;
		double var7 = this.posY - this.oldPosY;
		double var9 = this.posZ - this.oldPosZ;
		double var11 = (double)(this.rotationYaw - this.oldRotationYaw);
		double var13 = (double)(this.rotationPitch - this.oldRotationPitch);
		boolean var15 = var5 != 0.0D || var7 != 0.0D || var3 != 0.0D || var9 != 0.0D;
		boolean var16 = var11 != 0.0D || var13 != 0.0D;
		//Spout start
		if (sendQueue.cached == null) {
		if(this.ridingEntity != null) {
			if(var16) {
				this.sendQueue.addToSendQueue(new Packet11PlayerPosition(this.motionX, -999.0D, -999.0D, this.motionZ, this.onGround));
			} else {
				this.sendQueue.addToSendQueue(new Packet13PlayerLookMove(this.motionX, -999.0D, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
			}

			var15 = false;
		} else if(var15 && var16) {
			this.sendQueue.addToSendQueue(new Packet13PlayerLookMove(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
			this.field_12242_bI = 0;
		} else if(var15) {
			this.sendQueue.addToSendQueue(new Packet11PlayerPosition(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.onGround));
			this.field_12242_bI = 0;
		} else if(var16) {
			this.sendQueue.addToSendQueue(new Packet12PlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
			this.field_12242_bI = 0;
		} else {
			this.sendQueue.addToSendQueue(new Packet10Flying(this.onGround));
			if(this.field_9382_bF == this.onGround && this.field_12242_bI <= 200) {
				++this.field_12242_bI;
			} else {
				this.field_12242_bI = 0;
			}
		}
		}
		//Spout End

		this.field_9382_bF = this.onGround;
		if(var15) {
			this.oldPosX = this.posX;
			this.field_9378_bz = this.boundingBox.minY;
			this.oldPosY = this.posY;
			this.oldPosZ = this.posZ;
		}

		if(var16) {
			this.oldRotationYaw = this.rotationYaw;
			this.oldRotationPitch = this.rotationPitch;
		}

	}

	public void dropCurrentItem() {
		this.sendQueue.addToSendQueue(new Packet14BlockDig(4, 0, 0, 0, 0));
	}

	private void sendInventoryChanged() {}

	protected void joinEntityItemWithWorld(EntityItem var1) {}

	public void sendChatMessage(String var1) {
		this.sendQueue.addToSendQueue(new Packet3Chat(var1));
	}

	public void swingItem() {
		super.swingItem();
		this.sendQueue.addToSendQueue(new Packet18Animation(this, 1));
	}

	public void respawnPlayer() {
		this.sendInventoryChanged();
		NetClientHandler var10000 = this.sendQueue;
		//Packet9Respawn var10001 = new Packet9Respawn;
		byte var10003 = (byte)this.dimension;
		byte var10004 = (byte)this.worldObj.difficultySetting;
		long var10005 = this.worldObj.getRandomSeed();
		this.worldObj.getClass();
		Packet9Respawn var10001 = new Packet9Respawn(var10003, var10004, var10005, 128, 0);
		var10000.addToSendQueue(var10001);
	}

	protected void damageEntity(DamageSource var1, int var2) {
		this.health -= var2;
	}

	public void closeScreen() {
		this.sendQueue.addToSendQueue(new Packet101CloseWindow(this.craftingInventory.windowId));
		this.inventory.setItemStack((ItemStack)null);
		super.closeScreen();
	}

	public void setHealth(int var1) {
		if(this.field_21093_bH) {
			super.setHealth(var1);
		} else {
			this.health = var1;
			this.field_21093_bH = true;
		}

	}

	public void addStat(StatBase var1, int var2) {
		if(var1 != null) {
			if(var1.isIndependent) {
				super.addStat(var1, var2);
			}

		}
	}

	public void func_27027_b(StatBase var1, int var2) {
		if(var1 != null) {
			if(!var1.isIndependent) {
				super.addStat(var1, var2);
			}

		}
	}
	//Spout Start
	@Override
	public void handleKeyPress(int i, boolean keyReleased) {
		//key bindings
		if (keyReleased) {
			String keyName = org.lwjgl.input.Keyboard.getKeyName(i);
			if (keyName != null && keyName.length() == 1) {
				char ch = keyName.charAt(0);
				if (SpoutClient.getInstance().getChatManager().getBoundCommand(ch) != null) {
					SpoutClient.getInstance().getChatManager().sendChat(SpoutClient.getInstance().getChatManager().getBoundCommand(ch));
				}
			}
		}
		if (SpoutClient.getInstance().isSpoutEnabled()) {
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketKeyPress((byte)i, keyReleased, (MovementInputFromOptions)movementInput, ScreenType.GAME_SCREEN));
			SpoutClient.getInstance().getKeyBindingManager().pressKey(i, keyReleased, ScreenType.GAME_SCREEN.getCode());
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
