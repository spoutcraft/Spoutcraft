package net.minecraft.src;

import net.minecraft.client.Minecraft;
// Spout Start
import org.bukkit.ChatColor;
import org.spoutcraft.client.special.Resources;
// Spout End

public class EntityOtherPlayerMP extends EntityPlayer {
	private boolean isItemInUse = false;
	private int otherPlayerMPPosRotationIncrements;
	private double otherPlayerMPX;
	private double otherPlayerMPY;
	private double otherPlayerMPZ;
	private double otherPlayerMPYaw;
	private double otherPlayerMPPitch;

	public EntityOtherPlayerMP(World par1World, String par2Str) {
		super(par1World);
		this.username = par2Str;
		this.yOffset = 0.0F;
		this.stepHeight = 0.0F;

		if (par2Str != null && par2Str.length() > 0) {
			// Spout Start
			this.skinUrl = "http://cdn.spout.org/game/vanilla/skin/" + ChatColor.stripColor(par2Str) + ".png";
			this.vip = Resources.getVIP(ChatColor.stripColor(par2Str));
			// Spout End
		}

		this.noClip = true;
		this.field_71082_cx = 0.25F;
		this.renderDistanceWeight = 10.0D;
		// Spout Start
		if (vip != null) {
			displayName = vip.getTitle();
		} else {
			displayName = username;
		}
		this.worldObj.releaseEntitySkin(this);
		worldObj.obtainEntitySkin(this);
		// Spout End
	}

	/**
	 * sets the players height back to normal after doing things like sleeping and dieing
	 */
	protected void resetHeight() {
		this.yOffset = 0.0F;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		return true;
	}

	/**
	 * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX, posY,
	 * posZ, yaw, pitch
	 */
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
		this.otherPlayerMPX = par1;
		this.otherPlayerMPY = par3;
		this.otherPlayerMPZ = par5;
		this.otherPlayerMPYaw = (double)par7;
		this.otherPlayerMPPitch = (double)par8;
		this.otherPlayerMPPosRotationIncrements = par9;
	}

	public void updateCloak() {
		// Spout Start
		if (this.cloakUrl == null || this.playerCloakUrl == null) {
			super.updateCloak();
		}
		// Spout End
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		this.field_71082_cx = 0.0F;
		super.onUpdate();
		this.prevLimbYaw = this.limbYaw;
		double var1 = this.posX - this.prevPosX;
		double var3 = this.posZ - this.prevPosZ;
		float var5 = MathHelper.sqrt_double(var1 * var1 + var3 * var3) * 4.0F;

		if (var5 > 1.0F) {
			var5 = 1.0F;
		}

		this.limbYaw += (var5 - this.limbYaw) * 0.4F;
		this.limbSwing += this.limbYaw;

		if (!this.isItemInUse && this.isEating() && this.inventory.mainInventory[this.inventory.currentItem] != null) {
			ItemStack var6 = this.inventory.mainInventory[this.inventory.currentItem];
			this.setItemInUse(this.inventory.mainInventory[this.inventory.currentItem], Item.itemsList[var6.itemID].getMaxItemUseDuration(var6));
			this.isItemInUse = true;
		} else if (this.isItemInUse && !this.isEating()) {
			this.clearItemInUse();
			this.isItemInUse = false;
		}
	}

	public float getShadowSize() {
		return 0.0F;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use
	 * this to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		super.updateEntityActionState();

		if (this.otherPlayerMPPosRotationIncrements > 0) {
			double var1 = this.posX + (this.otherPlayerMPX - this.posX) / (double)this.otherPlayerMPPosRotationIncrements;
			double var3 = this.posY + (this.otherPlayerMPY - this.posY) / (double)this.otherPlayerMPPosRotationIncrements;
			double var5 = this.posZ + (this.otherPlayerMPZ - this.posZ) / (double)this.otherPlayerMPPosRotationIncrements;
			double var7;

			for (var7 = this.otherPlayerMPYaw - (double)this.rotationYaw; var7 < -180.0D; var7 += 360.0D) {
				;
			}

			while (var7 >= 180.0D) {
				var7 -= 360.0D;
			}

			this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.otherPlayerMPPosRotationIncrements);
			this.rotationPitch = (float)((double)this.rotationPitch + (this.otherPlayerMPPitch - (double)this.rotationPitch) / (double)this.otherPlayerMPPosRotationIncrements);
			--this.otherPlayerMPPosRotationIncrements;
			this.setPosition(var1, var3, var5);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}

		this.prevCameraYaw = this.cameraYaw;
		float var9 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		float var2 = (float)Math.atan(-this.motionY * 0.20000000298023224D) * 15.0F;

		if (var9 > 0.1F) {
			var9 = 0.1F;
		}

		if (!this.onGround || this.getHealth() <= 0) {
			var9 = 0.0F;
		}

		if (this.onGround || this.getHealth() <= 0) {
			var2 = 0.0F;
		}

		this.cameraYaw += (var9 - this.cameraYaw) * 0.4F;
		this.cameraPitch += (var2 - this.cameraPitch) * 0.8F;
	}

	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
	 */
	public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack) {
		if (par1 == 0) {
			this.inventory.mainInventory[this.inventory.currentItem] = par2ItemStack;
		} else {
			this.inventory.armorInventory[par1 - 1] = par2ItemStack;
		}
	}

	public float getEyeHeight() {
		return 1.82F;
	}

	public void sendChatToPlayer(String par1Str) {
		// Spout Start - Removed
		// TODO: Something?
		//Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(par1Str);
		// Spout End
	}

	/**
	 * Returns true if the command sender is allowed to use the given command.
	 */
	public boolean canCommandSenderUseCommand(int par1, String par2Str) {
		return false;
	}

	/**
	 * Return the position for this command sender.
	 */
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates(MathHelper.floor_double(this.posX + 0.5D), MathHelper.floor_double(this.posY + 0.5D), MathHelper.floor_double(this.posZ + 0.5D));
	}
}
