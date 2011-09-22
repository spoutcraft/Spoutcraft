package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Achievement;
import net.minecraft.src.AchievementList;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityCrit2FX;
import net.minecraft.src.EntityPickupFX;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiChest;
import net.minecraft.src.GuiCrafting;
import net.minecraft.src.GuiDispenser;
import net.minecraft.src.GuiEditSign;
import net.minecraft.src.GuiFurnace;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MouseFilter;
import net.minecraft.src.MovementInput;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Potion;
import net.minecraft.src.Session;
import net.minecraft.src.StatBase;
import net.minecraft.src.TileEntityDispenser;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.TileEntitySign;
import net.minecraft.src.World;
//Spout start
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.packet.CustomPacket;
import org.getspout.spout.packet.PacketRenderDistance;
import org.getspout.spout.player.ClientPlayer;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;
//Spout end

public class EntityPlayerSP extends EntityPlayer {

	public MovementInput movementInput;
	protected Minecraft mc;
	protected int field_35224_c = 0;
	public int field_35221_d = 0;
	public float field_35222_e;
	public float field_35223_ap;
	public float field_35226_aq;
	public float field_35225_ar;
	private MouseFilter field_21903_bJ = new MouseFilter();
	private MouseFilter field_21904_bK = new MouseFilter();
	private MouseFilter field_21902_bL = new MouseFilter();
	//Spout start
	public FixedLocation lastClickLocation = null;
	private KeyBinding fogKey = null;
	//Spout end


	public EntityPlayerSP(Minecraft var1, World var2, Session var3, int var4) {
		super(var2);
		this.mc = var1;
		this.dimension = var4;
		if(var3 != null && var3.username != null && var3.username.length() > 0) {
			this.skinUrl = "http://s3.amazonaws.com/MinecraftSkins/" + var3.username + ".png";
		}

		this.username = var3.username;
		//Spout start
		spoutEntity = new ClientPlayer(this);
		SpoutClient.getInstance().player = (ClientPlayer) spoutEntity;
		//Spout end
	}

	public void moveEntity(double var1, double var3, double var5) {
		super.moveEntity(var1, var3, var5);
	}

	public void updateEntityActionState() {
		super.updateEntityActionState();
		this.moveStrafing = this.movementInput.moveStrafe;
		this.moveForward = this.movementInput.moveForward;
		this.isJumping = this.movementInput.jump;
		this.field_35226_aq = this.field_35222_e;
		this.field_35225_ar = this.field_35223_ap;
		this.field_35223_ap = (float)((double)this.field_35223_ap + (double)(this.rotationPitch - this.field_35223_ap) * 0.5D);
		this.field_35222_e = (float)((double)this.field_35222_e + (double)(this.rotationYaw - this.field_35222_e) * 0.5D);
	}

	public void onLivingUpdate() {
		if(this.field_35221_d > 0) {
			--this.field_35221_d;
			if(this.field_35221_d == 0) {
				this.shouldSprint(false);
			}
		}

		if(this.field_35224_c > 0) {
			--this.field_35224_c;
		}

		if(this.mc.playerController.func_35643_e()) {
			this.posX = this.posZ = 0.5D;
			this.posX = 0.0D;
			this.posZ = 0.0D;
			this.rotationYaw = (float)this.ticksExisted / 12.0F;
			this.rotationPitch = 10.0F;
			this.posY = 68.5D;
		} else {
			if(!this.mc.statFileWriter.hasAchievementUnlocked(AchievementList.openInventory)) {
				this.mc.guiAchievement.queueAchievementInformation(AchievementList.openInventory);
			}

			this.prevTimeInPortal = this.timeInPortal;
			if(this.inPortal) {
				if(!this.worldObj.multiplayerWorld && this.ridingEntity != null) {
					this.mountEntity((Entity)null);
				}

				if(this.mc.currentScreen != null) {
					this.mc.displayGuiScreen((GuiScreen)null);
				}

				if(this.timeInPortal == 0.0F) {
					this.mc.sndManager.playSoundFX("portal.trigger", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
				}

				this.timeInPortal += 0.0125F;
				if(this.timeInPortal >= 1.0F) {
					this.timeInPortal = 1.0F;
					if(!this.worldObj.multiplayerWorld) {
						this.timeUntilPortal = 10;
						this.mc.sndManager.playSoundFX("portal.travel", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
						this.mc.usePortal();
					}
				}

				this.inPortal = false;
			} else if(this.func_35160_a(Potion.potionConfusion) && this.func_35167_b(Potion.potionConfusion).func_35802_b() > 60) {
				this.timeInPortal += 0.006666667F;
				if(this.timeInPortal > 1.0F) {
					this.timeInPortal = 1.0F;
				}
			} else {
				if(this.timeInPortal > 0.0F) {
					this.timeInPortal -= 0.05F;
				}

				if(this.timeInPortal < 0.0F) {
					this.timeInPortal = 0.0F;
				}
			}

			if(this.timeUntilPortal > 0) {
				--this.timeUntilPortal;
			}

			boolean var1 = this.movementInput.jump;
			float var2 = 0.8F;
			boolean var3 = this.movementInput.moveForward >= var2;
			this.movementInput.updatePlayerMoveState(this);
			if(this.func_35196_Z()) {
				this.movementInput.moveStrafe *= 0.2F;
				this.movementInput.moveForward *= 0.2F;
				this.field_35224_c = 0;
			}

			if(this.movementInput.sneak && this.ySize < 0.2F) {
				this.ySize = 0.2F;
			}

			this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double)this.width * 0.35D);
			this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double)this.width * 0.35D);
			this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double)this.width * 0.35D);
			this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double)this.width * 0.35D);
			boolean var4 = (float)this.func_35191_at().func_35765_a() > 6.0F || this.mc.playerController.isInCreativeMode();
			if(this.onGround && !var3 && this.movementInput.moveForward >= var2 && !this.func_35117_Q() && var4 && !this.func_35196_Z()) {
				if(this.field_35224_c == 0) {
					this.field_35224_c = 7;
				} else {
					this.shouldSprint(true);
					this.field_35224_c = 0;
				}
			}

			if(this.func_35117_Q() && (this.movementInput.moveForward < var2 || this.isCollidedHorizontally || !var4)) {
				this.shouldSprint(false);
			}

			if(this.capabilities.field_35758_c && !var1 && this.movementInput.jump) {
				if(this.field_35216_aw == 0) {
					this.field_35216_aw = 7;
				} else {
					this.capabilities.isFlying = !this.capabilities.isFlying;
					this.field_35216_aw = 0;
				}
			}

			if(this.capabilities.isFlying) {
				if(this.movementInput.sneak) {
					this.motionY -= 0.15D;
				}

				if(this.movementInput.jump) {
					this.motionY += 0.15D;
				}
			}

			super.onLivingUpdate();
			if(this.onGround && this.capabilities.isFlying) {
				this.capabilities.isFlying = false;
			}

		}
	}

	public float getFOVMultiplier() {
		float var1 = 1.0F;
		if(this.capabilities.isFlying) {
			var1 *= 1.1F;
		}
		
		var1 *= (this.field_35169_bv * this.func_35166_t_() / this.field_35215_ba + 1.0F) / 2.0F;
		if(this.func_35196_Z() && this.func_35195_X().itemID == Item.bow.shiftedIndex) {
			int var2 = this.func_35192_aa();
			float var3 = (float)var2 / 20.0F;
			if(var3 > 1.0F) {
				var3 = 1.0F;
			} else {
				var3 *= var3;
			}

			var1 *= 1.0F - var3 * 0.15F;
		}

		return var1;
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setInteger("Score", this.score);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.score = var1.getInteger("Score");
	}

	public void closeScreen() {
		super.closeScreen();
		this.mc.displayGuiScreen((GuiScreen)null);
	}

	public void displayGUIEditSign(TileEntitySign var1) {
		this.mc.displayGuiScreen(new GuiEditSign(var1));
	}

	public void displayGUIChest(IInventory var1) {
		this.mc.displayGuiScreen(new GuiChest(this.inventory, var1));
	}

	public void displayWorkbenchGUI(int var1, int var2, int var3) {
		this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj, var1, var2, var3));
	}

	public void displayGUIFurnace(TileEntityFurnace var1) {
		this.mc.displayGuiScreen(new GuiFurnace(this.inventory, var1));
	}

	public void displayGUIDispenser(TileEntityDispenser var1) {
		this.mc.displayGuiScreen(new GuiDispenser(this.inventory, var1));
	}

	public void func_35200_b(Entity var1) {
		this.mc.effectRenderer.addEffect(new EntityCrit2FX(this.mc.theWorld, var1));
	}

	public void onItemPickup(Entity var1, int var2) {
		this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, var1, this, -0.5F));
	}

	public int getPlayerArmorValue() {
		return this.inventory.getTotalArmorValue();
	}

	public void sendChatMessage(String var1) {}

	public boolean isSneaking() {
		return this.movementInput.sneak && !this.sleeping;
	}

	public void setHealth(int var1) {
		int var2 = this.health - var1;
		if(var2 <= 0) {
			this.health = var1;
			if(var2 < 0) {
				this.heartsLife = this.heartsHalvesLife / 2;
			}
		} else {
			this.field_9346_af = var2;
			this.prevHealth = this.health;
			this.heartsLife = this.heartsHalvesLife;
			this.damageEntity(DamageSource.generic, var2);
			this.hurtTime = this.maxHurtTime = 10;
		}

	}

	public void respawnPlayer() {
		this.mc.respawn(false, 0);
	}

	public void func_6420_o() {}

	public void addChatMessage(String var1) {
		this.mc.ingameGUI.addChatMessageTranslate(var1);
	}

	public void addStat(StatBase var1, int var2) {
		if(var1 != null) {
			if(var1.isAchievement()) {
				Achievement var3 = (Achievement)var1;
				if(var3.parentAchievement == null || this.mc.statFileWriter.hasAchievementUnlocked(var3.parentAchievement)) {
					if(!this.mc.statFileWriter.hasAchievementUnlocked(var3)) {
						this.mc.guiAchievement.queueTakenAchievement(var3);
					}

					this.mc.statFileWriter.readStat(var1, var2);
				}
			} else {
				this.mc.statFileWriter.readStat(var1, var2);
			}

		}
	}

	private boolean isBlockTranslucent(int var1, int var2, int var3) {
		return this.worldObj.isBlockNormalCube(var1, var2, var3);
	}

	protected boolean pushOutOfBlocks(double var1, double var3, double var5) {
		int var7 = MathHelper.floor_double(var1);
		int var8 = MathHelper.floor_double(var3);
		int var9 = MathHelper.floor_double(var5);
		double var10 = var1 - (double)var7;
		double var12 = var5 - (double)var9;
		if(this.isBlockTranslucent(var7, var8, var9) || this.isBlockTranslucent(var7, var8 + 1, var9)) {
			boolean var14 = !this.isBlockTranslucent(var7 - 1, var8, var9) && !this.isBlockTranslucent(var7 - 1, var8 + 1, var9);
			boolean var15 = !this.isBlockTranslucent(var7 + 1, var8, var9) && !this.isBlockTranslucent(var7 + 1, var8 + 1, var9);
			boolean var16 = !this.isBlockTranslucent(var7, var8, var9 - 1) && !this.isBlockTranslucent(var7, var8 + 1, var9 - 1);
			boolean var17 = !this.isBlockTranslucent(var7, var8, var9 + 1) && !this.isBlockTranslucent(var7, var8 + 1, var9 + 1);
			byte var18 = -1;
			double var19 = 9999.0D;
			if(var14 && var10 < var19) {
				var19 = var10;
				var18 = 0;
			}

			if(var15 && 1.0D - var10 < var19) {
				var19 = 1.0D - var10;
				var18 = 1;
			}

			if(var16 && var12 < var19) {
				var19 = var12;
				var18 = 4;
			}

			if(var17 && 1.0D - var12 < var19) {
				var19 = 1.0D - var12;
				var18 = 5;
			}

			float var21 = 0.1F;
			if(var18 == 0) {
				this.motionX = (double)(-var21);
			}

			if(var18 == 1) {
				this.motionX = (double)var21;
			}

			if(var18 == 4) {
				this.motionZ = (double)(-var21);
			}

			if(var18 == 5) {
				this.motionZ = (double)var21;
			}
		}

		return false;
	}

	public void shouldSprint(boolean var1) {
		super.shouldSprint(var1);
		if(!var1) {
			this.field_35221_d = 0;
		} else {
			this.field_35221_d = 600;
		}

	}

	public void setXPStats(int var1, int var2, int var3) {
		this.currentXP = var1;
		this.totalXP = var2;
		this.playerLevel = var3;
	}
	
	//Spout
	@Override
	public void handleKeyPress(int key, boolean keyReleased) {
		((MovementInputFromOptions)this.movementInput).checkKeyForMovementInput(key, keyReleased);
		if (keyReleased) {
			final GameSettings settings = SpoutClient.getHandle().gameSettings;
			if (key == settings.keyBindToggleFog.keyCode) {
				byte view = (byte)settings.renderDistance;
				byte newView = (byte) SpoutClient.getInstance().getActivePlayer().getNextRenderDistance().getValue();
				fogKey = settings.keyBindToggleFog;
				settings.keyBindToggleFog = new KeyBinding("key.fog", -1);
				if (view != newView) {
					settings.renderDistance = newView;
					if (this instanceof EntityClientPlayerMP && Spoutcraft.getClient().isSpoutEnabled()) {
						((EntityClientPlayerMP)this).sendQueue.addToSendQueue(new CustomPacket(new PacketRenderDistance((byte)newView)));
					}
				}
			}
		}
	}
	
	@Override
	public void onUpdate() {
		if (fogKey != null) {
			SpoutClient.getHandle().gameSettings.keyBindToggleFog = fogKey;
			fogKey = null;
		}
		super.onUpdate();
	}
	//Spout end
}
