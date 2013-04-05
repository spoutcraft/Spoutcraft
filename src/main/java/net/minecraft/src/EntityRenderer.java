package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
// MCPatcher Start
import com.prupe.mcpatcher.mod.ColorizeWorld;
import com.prupe.mcpatcher.mod.Colorizer;
import com.prupe.mcpatcher.mod.Lightmap;
import com.prupe.mcpatcher.mod.RenderPass;
// MCPatcher End
// Spout Start
import org.lwjgl.util.vector.*;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.spoutworth.SpoutWorth;
// Spout End

public class EntityRenderer {
	public static boolean anaglyphEnable = false;

	/** Anaglyph field (0=R, 1=GB) */
	public static int anaglyphField;

	/** A reference to the Minecraft object. */
	private Minecraft mc;
	private float farPlaneDistance = 0.0F;
	public ItemRenderer itemRenderer;

	/** Entity renderer update count */
	private int rendererUpdateCount;

	/** Pointed entity */
	private Entity pointedEntity = null;
	private MouseFilter mouseFilterXAxis = new MouseFilter();
	private MouseFilter mouseFilterYAxis = new MouseFilter();

	/** Mouse filter dummy 1 */
	private MouseFilter mouseFilterDummy1 = new MouseFilter();

	/** Mouse filter dummy 2 */
	private MouseFilter mouseFilterDummy2 = new MouseFilter();

	/** Mouse filter dummy 3 */
	private MouseFilter mouseFilterDummy3 = new MouseFilter();

	/** Mouse filter dummy 4 */
	private MouseFilter mouseFilterDummy4 = new MouseFilter();
	private float thirdPersonDistance = 4.0F;

	/** Third person distance temp */
	private float thirdPersonDistanceTemp = 4.0F;
	private float debugCamYaw = 0.0F;
	private float prevDebugCamYaw = 0.0F;
	private float debugCamPitch = 0.0F;
	private float prevDebugCamPitch = 0.0F;

	/** Smooth cam yaw */
	private float smoothCamYaw;

	/** Smooth cam pitch */
	private float smoothCamPitch;

	/** Smooth cam filter X */
	private float smoothCamFilterX;

	/** Smooth cam filter Y */
	private float smoothCamFilterY;

	/** Smooth cam partial ticks */
	private float smoothCamPartialTicks;
	private float debugCamFOV = 0.0F;
	private float prevDebugCamFOV = 0.0F;
	private float camRoll = 0.0F;
	private float prevCamRoll = 0.0F;

	/**
	 * The texture id of the blocklight/skylight texture used for lighting effects
	 */
	public int lightmapTexture;

	/**
	 * Colors computed in updateLightmap() and loaded into the lightmap emptyTexture
	 */
	private int[] lightmapColors;

	/** FOV modifier hand */
	private float fovModifierHand;

	/** FOV modifier hand prev */
	private float fovModifierHandPrev;

	/** FOV multiplier temp */
	private float fovMultiplierTemp;
	private float field_82831_U;
	private float field_82832_V;

	/** Cloud fog mode */
	private boolean cloudFog = false;
	private double cameraZoom = 1.0D;
	private double cameraYaw = 0.0D;
	private double cameraPitch = 0.0D;

	/** Previous frame time in milliseconds */
	private long prevFrameTime = Minecraft.getSystemTime();

	/** End time of last render (ns) */
	private long renderEndNanoTime = 0L;

	/**
	 * Is set, updateCameraAndRender() calls updateLightmap(); set by updateTorchFlicker()
	 */
	private boolean lightmapUpdateNeeded = false;

	/** Torch flicker X */
	// MCPatcher Start
	public float torchFlickerX = 0.0F;
	// MCPatcher End

	/** Torch flicker DX */
	float torchFlickerDX = 0.0F;

	/** Torch flicker Y */
	float torchFlickerY = 0.0F;

	/** Torch flicker DY */
	float torchFlickerDY = 0.0F;
	private Random random = new Random();

	/** Rain sound counter */
	private int rainSoundCounter = 0;

	/** Rain X coords */
	float[] rainXCoords;

	/** Rain Y coords */
	float[] rainYCoords;
	volatile int field_78523_k = 0;
	volatile int field_78520_l = 0;

	/** Fog color buffer */
	FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);

	/** red component of the fog color */
	float fogColorRed;

	/** green component of the fog color */
	float fogColorGreen;

	/** blue component of the fog color */
	float fogColorBlue;

	/** Fog color 2 */
	private float fogColor2;

	/** Fog color 1 */
	private float fogColor1;

	/**
	 * Debug view direction (0=OFF, 1=Front, 2=Right, 3=Back, 4=Left, 5=TiltLeft, 6=TiltRight)
	 */
	public int debugViewDirection;

	// Spout Start
	private WorldProvider updatedWorldProvider = null;
	private boolean showDebugInfo = false;
	public static Matrix4f view = new Matrix4f();
	public static Matrix4f projection = new Matrix4f();
	public int betterGrassLoop;
	// Spout End

	public EntityRenderer(Minecraft par1Minecraft) {
		this.mc = par1Minecraft;
		this.itemRenderer = new ItemRenderer(par1Minecraft);
		this.lightmapTexture = par1Minecraft.renderEngine.allocateAndSetupTexture(new BufferedImage(16, 16, 1));
		this.lightmapColors = new int[256];
	}

	/**
	 * Updates the entity renderer
	 */
	public void updateRenderer() {
		this.updateFovModifierHand();
		this.updateTorchFlicker();
		this.fogColor2 = this.fogColor1;
		this.thirdPersonDistanceTemp = this.thirdPersonDistance;
		this.prevDebugCamYaw = this.debugCamYaw;
		this.prevDebugCamPitch = this.debugCamPitch;
		this.prevDebugCamFOV = this.debugCamFOV;
		this.prevCamRoll = this.camRoll;
		float var1;
		float var2;

		if (this.mc.gameSettings.smoothCamera) {
			var1 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			var2 = var1 * var1 * var1 * 8.0F;
			this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * var2);
			this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * var2);
			this.smoothCamPartialTicks = 0.0F;
			this.smoothCamYaw = 0.0F;
			this.smoothCamPitch = 0.0F;
		}

		if (this.mc.renderViewEntity == null) {
			this.mc.renderViewEntity = this.mc.thePlayer;
		}

		var1 = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(this.mc.renderViewEntity.posX), MathHelper.floor_double(this.mc.renderViewEntity.posY), MathHelper.floor_double(this.mc.renderViewEntity.posZ));
		var2 = (float)(3 - this.mc.gameSettings.renderDistance) / 3.0F;
		float var3 = var1 * (1.0F - var2) + var2;
		this.fogColor1 += (var3 - this.fogColor1) * 0.1F;
		++this.rendererUpdateCount;
		this.itemRenderer.updateEquippedItem();
		this.addRainParticles();
		this.field_82832_V = this.field_82831_U;

		if (BossStatus.field_82825_d) {
			this.field_82831_U += 0.05F;

			if (this.field_82831_U > 1.0F) {
				this.field_82831_U = 1.0F;
			}

			BossStatus.field_82825_d = false;
		} else if (this.field_82831_U > 0.0F) {
			this.field_82831_U -= 0.0125F;
		}
	}

	/**
	 * Finds what block or object the mouse is over at the specified partial tick time. Args: partialTickTime
	 */
	public void getMouseOver(float par1) {
		if (this.mc.renderViewEntity != null) {
			if (this.mc.theWorld != null) {
				this.mc.pointedEntityLiving = null;
				double var2 = (double)this.mc.playerController.getBlockReachDistance();
				this.mc.objectMouseOver = this.mc.renderViewEntity.rayTrace(var2, par1);
				double var4 = var2;
				Vec3 var6 = this.mc.renderViewEntity.getPosition(par1);

				if (this.mc.playerController.extendedReach()) {
					var2 = 6.0D;
					var4 = 6.0D;
				} else {
					if (var2 > 3.0D) {
						var4 = 3.0D;
					}

					var2 = var4;
				}

				if (this.mc.objectMouseOver != null) {
					var4 = this.mc.objectMouseOver.hitVec.distanceTo(var6);
				}

				Vec3 var7 = this.mc.renderViewEntity.getLook(par1);
				Vec3 var8 = var6.addVector(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2);
				this.pointedEntity = null;
				float var9 = 1.0F;
				List var10 = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.renderViewEntity, this.mc.renderViewEntity.boundingBox.addCoord(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2).expand((double)var9, (double)var9, (double)var9));
				double var11 = var4;

				for (int var13 = 0; var13 < var10.size(); ++var13) {
					Entity var14 = (Entity)var10.get(var13);

					if (var14.canBeCollidedWith()) {
						float var15 = var14.getCollisionBorderSize();
						AxisAlignedBB var16 = var14.boundingBox.expand((double)var15, (double)var15, (double)var15);
						MovingObjectPosition var17 = var16.calculateIntercept(var6, var8);

						if (var16.isVecInside(var6)) {
							if (0.0D < var11 || var11 == 0.0D) {
								this.pointedEntity = var14;
								var11 = 0.0D;
							}
						} else if (var17 != null) {
							double var18 = var6.distanceTo(var17.hitVec);

							if (var18 < var11 || var11 == 0.0D) {
								this.pointedEntity = var14;
								var11 = var18;
							}
						}
					}
				}

				if (this.pointedEntity != null && (var11 < var4 || this.mc.objectMouseOver == null)) {
					this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity);

					if (this.pointedEntity instanceof EntityLiving) {
						this.mc.pointedEntityLiving = (EntityLiving)this.pointedEntity;
					}
				}
			}
		}
	}

	/**
	 * Update FOV modifier hand
	 */
	private void updateFovModifierHand() {
		EntityPlayerSP var1 = (EntityPlayerSP)this.mc.renderViewEntity;
		this.fovMultiplierTemp = var1.getFOVMultiplier();
		this.fovModifierHandPrev = this.fovModifierHand;
		this.fovModifierHand += (this.fovMultiplierTemp - this.fovModifierHand) * 0.5F;

		if (this.fovModifierHand > 1.5F) {
			this.fovModifierHand = 1.5F;
		}

		if (this.fovModifierHand < 0.1F) {
			this.fovModifierHand = 0.1F;
		}
	}

	/**
	 * Changes the field of view of the player depending on if they are underwater or not
	 */
	private float getFOVModifier(float par1, boolean par2) {
		if (this.debugViewDirection > 0) {
			return 90.0F;
		} else {
			EntityPlayer var3 = (EntityPlayer)this.mc.renderViewEntity;
			float var4 = 70.0F;

			if (par2) {
				var4 += this.mc.gameSettings.fovSetting * 40.0F;
				var4 *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * par1;
			}

			if (var3.getHealth() <= 0) {
				float var5 = (float)var3.deathTime + par1;
				var4 /= (1.0F - 500.0F / (var5 + 500.0F)) * 2.0F + 1.0F;
			}

			int var6 = ActiveRenderInfo.getBlockIdAtEntityViewpoint(this.mc.theWorld, var3, par1);

			if (var6 != 0 && Block.blocksList[var6].blockMaterial == Material.water) {
				var4 = var4 * 60.0F / 70.0F;
			}

			return var4 + this.prevDebugCamFOV + (this.debugCamFOV - this.prevDebugCamFOV) * par1;
		}
	}

	private void hurtCameraEffect(float par1) {
		EntityLiving var2 = this.mc.renderViewEntity;
		float var3 = (float)var2.hurtTime - par1;
		float var4;

		if (var2.getHealth() <= 0) {
			var4 = (float)var2.deathTime + par1;
			GL11.glRotatef(40.0F - 8000.0F / (var4 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if (var3 >= 0.0F) {
			var3 /= (float)var2.maxHurtTime;
			var3 = MathHelper.sin(var3 * var3 * var3 * var3 * (float)Math.PI);
			var4 = var2.attackedAtYaw;
			GL11.glRotatef(-var4, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(var4, 0.0F, 1.0F, 0.0F);
		}
	}

	/**
	 * Setups all the GL settings for view bobbing. Args: partialTickTime
	 */
	private void setupViewBobbing(float par1) {
		if (this.mc.renderViewEntity instanceof EntityPlayer) {
			EntityPlayer var2 = (EntityPlayer)this.mc.renderViewEntity;
			float var3 = var2.distanceWalkedModified - var2.prevDistanceWalkedModified;
			float var4 = -(var2.distanceWalkedModified + var3 * par1);
			float var5 = var2.prevCameraYaw + (var2.cameraYaw - var2.prevCameraYaw) * par1;
			float var6 = var2.prevCameraPitch + (var2.cameraPitch - var2.prevCameraPitch) * par1;
			GL11.glTranslatef(MathHelper.sin(var4 * (float)Math.PI) * var5 * 0.5F, -Math.abs(MathHelper.cos(var4 * (float)Math.PI) * var5), 0.0F);
			GL11.glRotatef(MathHelper.sin(var4 * (float)Math.PI) * var5 * 3.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(Math.abs(MathHelper.cos(var4 * (float)Math.PI - 0.2F) * var5) * 5.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var6, 1.0F, 0.0F, 0.0F);
		}
	}

	/**
	 * sets up player's eye (or camera in third person mode)
	 */
	private void orientCamera(float par1) {
		EntityLiving var2 = this.mc.renderViewEntity;
		float var3 = var2.yOffset - 1.62F;
		double var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double)par1;
		double var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double)par1 - (double)var3;
		double var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double)par1;
		GL11.glRotatef(this.prevCamRoll + (this.camRoll - this.prevCamRoll) * par1, 0.0F, 0.0F, 1.0F);

		if (var2.isPlayerSleeping()) {
			var3 = (float)((double)var3 + 1.0D);
			GL11.glTranslatef(0.0F, 0.3F, 0.0F);

			if (!this.mc.gameSettings.debugCamEnable) {
				int var10 = this.mc.theWorld.getBlockId(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posY), MathHelper.floor_double(var2.posZ));

				if (var10 == Block.bed.blockID) {
					int var11 = this.mc.theWorld.getBlockMetadata(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posY), MathHelper.floor_double(var2.posZ));
					int var12 = var11 & 3;
					GL11.glRotatef((float)(var12 * 90), 0.0F, 1.0F, 0.0F);
				}

				GL11.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * par1 + 180.0F, 0.0F, -1.0F, 0.0F);
				GL11.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * par1, -1.0F, 0.0F, 0.0F);
			}
		} else if (this.mc.gameSettings.thirdPersonView > 0) {
			double var27 = (double)(this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * par1);
			float var13;
			float var28;

			if (this.mc.gameSettings.debugCamEnable) {
				var28 = this.prevDebugCamYaw + (this.debugCamYaw - this.prevDebugCamYaw) * par1;
				var13 = this.prevDebugCamPitch + (this.debugCamPitch - this.prevDebugCamPitch) * par1;
				GL11.glTranslatef(0.0F, 0.0F, (float)(-var27));
				GL11.glRotatef(var13, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(var28, 0.0F, 1.0F, 0.0F);
			} else {
				var28 = var2.rotationYaw;
				var13 = var2.rotationPitch;

				if (this.mc.gameSettings.thirdPersonView == 2) {
					var13 += 180.0F;
				}

				double var14 = (double)(-MathHelper.sin(var28 / 180.0F * (float)Math.PI) * MathHelper.cos(var13 / 180.0F * (float)Math.PI)) * var27;
				double var16 = (double)(MathHelper.cos(var28 / 180.0F * (float)Math.PI) * MathHelper.cos(var13 / 180.0F * (float)Math.PI)) * var27;
				double var18 = (double)(-MathHelper.sin(var13 / 180.0F * (float)Math.PI)) * var27;

				for (int var20 = 0; var20 < 8; ++var20) {
					float var21 = (float)((var20 & 1) * 2 - 1);
					float var22 = (float)((var20 >> 1 & 1) * 2 - 1);
					float var23 = (float)((var20 >> 2 & 1) * 2 - 1);
					var21 *= 0.1F;
					var22 *= 0.1F;
					var23 *= 0.1F;
					MovingObjectPosition var24 = this.mc.theWorld.rayTraceBlocks(this.mc.theWorld.getWorldVec3Pool().getVecFromPool(var4 + (double)var21, var6 + (double)var22, var8 + (double)var23), this.mc.theWorld.getWorldVec3Pool().getVecFromPool(var4 - var14 + (double)var21 + (double)var23, var6 - var18 + (double)var22, var8 - var16 + (double)var23));

					if (var24 != null) {
						double var25 = var24.hitVec.distanceTo(this.mc.theWorld.getWorldVec3Pool().getVecFromPool(var4, var6, var8));

						if (var25 < var27) {
							var27 = var25;
						}
					}
				}

				if (this.mc.gameSettings.thirdPersonView == 2) {
					GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				}

				GL11.glRotatef(var2.rotationPitch - var13, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(var2.rotationYaw - var28, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0.0F, 0.0F, (float)(-var27));
				GL11.glRotatef(var28 - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(var13 - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
			}
		} else {
			GL11.glTranslatef(0.0F, 0.0F, -0.1F);
		}

		if (!this.mc.gameSettings.debugCamEnable) {
			GL11.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * par1, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * par1 + 180.0F, 0.0F, 1.0F, 0.0F);
		}

		GL11.glTranslatef(0.0F, var3, 0.0F);
		var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double)par1;
		var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double)par1 - (double)var3;
		var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double)par1;
		this.cloudFog = this.mc.renderGlobal.hasCloudFog(var4, var6, var8, par1);
	}

	/**
	 * sets up projection, view effects, camera position/rotation
	 */
	private void setupCameraTransform(float par1, int par2) {
		// Spout Start
		this.farPlaneDistance = (float) (32 << 3 - this.mc.gameSettings.renderDistance);
		if (Configuration.isFarView()) {
			if (this.farPlaneDistance < 256.0F) {
				this.farPlaneDistance *= 3.0F;
			} else {
				this.farPlaneDistance *= 2.0F;
			}
		}

		if (Configuration.isFancyFog()) {
			this.farPlaneDistance *= 0.95F;
		} else {
			this.farPlaneDistance *= 0.83F;
		}
		// Spout End
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float var3 = 0.07F;

		if (this.mc.gameSettings.anaglyph) {
			GL11.glTranslatef((float)(-(par2 * 2 - 1)) * var3, 0.0F, 0.0F);
		}

		if (this.cameraZoom != 1.0D) {
			GL11.glTranslatef((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
			GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0D);
		}

		GLU.gluPerspective(this.getFOVModifier(par1, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
		float var4;

		if (this.mc.playerController.enableEverythingIsScrewedUpMode()) {
			var4 = 0.6666667F;
			GL11.glScalef(1.0F, var4, 1.0F);
		}

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		if (this.mc.gameSettings.anaglyph) {
			GL11.glTranslatef((float)(par2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}

		this.hurtCameraEffect(par1);

		if (this.mc.gameSettings.viewBobbing) {
			this.setupViewBobbing(par1);
		}

		var4 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * par1;

		if (var4 > 0.0F) {
			byte var5 = 20;

			if (this.mc.thePlayer.isPotionActive(Potion.confusion)) {
				var5 = 7;
			}

			float var6 = 5.0F / (var4 * var4 + 5.0F) - var4 * 0.04F;
			var6 *= var6;
			GL11.glRotatef(((float)this.rendererUpdateCount + par1) * (float)var5, 0.0F, 1.0F, 1.0F);
			GL11.glScalef(1.0F / var6, 1.0F, 1.0F);
			GL11.glRotatef(-((float)this.rendererUpdateCount + par1) * (float)var5, 0.0F, 1.0F, 1.0F);
		}

		this.orientCamera(par1);

		if (this.debugViewDirection > 0) {
			int var7 = this.debugViewDirection - 1;

			if (var7 == 1) {
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			}

			if (var7 == 2) {
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}

			if (var7 == 3) {
				GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			}

			if (var7 == 4) {
				GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			}

			if (var7 == 5) {
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
			}
		}
	}

	/**
	 * Render player hand
	 */
	private void renderHand(float par1, int par2) {
		if (this.debugViewDirection <= 0) {
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			float var3 = 0.07F;

			if (this.mc.gameSettings.anaglyph) {
				GL11.glTranslatef((float)(-(par2 * 2 - 1)) * var3, 0.0F, 0.0F);
			}

			if (this.cameraZoom != 1.0D) {
				GL11.glTranslatef((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
				GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0D);
			}

			GLU.gluPerspective(this.getFOVModifier(par1, false), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);

			if (this.mc.playerController.enableEverythingIsScrewedUpMode()) {
				float var4 = 0.6666667F;
				GL11.glScalef(1.0F, var4, 1.0F);
			}

			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();

			if (this.mc.gameSettings.anaglyph) {
				GL11.glTranslatef((float)(par2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
			}

			GL11.glPushMatrix();
			this.hurtCameraEffect(par1);

			if (this.mc.gameSettings.viewBobbing) {
				this.setupViewBobbing(par1);
			}

			if (this.mc.gameSettings.thirdPersonView == 0 && !this.mc.renderViewEntity.isPlayerSleeping() && !this.mc.gameSettings.hideGUI && !this.mc.playerController.enableEverythingIsScrewedUpMode()) {
				this.enableLightmap((double)par1);
				this.itemRenderer.renderItemInFirstPerson(par1);
				this.disableLightmap((double)par1);
			}

			GL11.glPopMatrix();

			if (this.mc.gameSettings.thirdPersonView == 0 && !this.mc.renderViewEntity.isPlayerSleeping()) {
				this.itemRenderer.renderOverlays(par1);
				this.hurtCameraEffect(par1);
			}

			if (this.mc.gameSettings.viewBobbing) {
				this.setupViewBobbing(par1);
			}
		}
	}

	/**
	 * Disable secondary texture unit used by lightmap
	 */
	public void disableLightmap(double par1) {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	/**
	 * Enable lightmap in secondary texture unit
	 */
	public void enableLightmap(double par1) {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		float var3 = 0.00390625F;
		GL11.glScalef(var3, var3, var3);
		GL11.glTranslatef(8.0F, 8.0F, 8.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.lightmapTexture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		this.mc.renderEngine.resetBoundTexture();
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	/**
	 * Recompute a random value that is applied to block color in updateLightmap()
	 */
	private void updateTorchFlicker() {
		this.torchFlickerDX = (float)((double)this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
		this.torchFlickerDY = (float)((double)this.torchFlickerDY + (Math.random() - Math.random()) * Math.random() * Math.random());
		this.torchFlickerDX = (float)((double)this.torchFlickerDX * 0.9D);
		this.torchFlickerDY = (float)((double)this.torchFlickerDY * 0.9D);
		this.torchFlickerX += (this.torchFlickerDX - this.torchFlickerX) * 1.0F;
		this.torchFlickerY += (this.torchFlickerDY - this.torchFlickerY) * 1.0F;
		this.lightmapUpdateNeeded = true;
	}

	private void updateLightmap(float par1) {
		WorldClient var2 = this.mc.theWorld;

		if (!Lightmap.computeLightmap(this, var2, par1)) {
			if (var2 != null) {
				for (int var3 = 0; var3 < 256; ++var3) {
					float var4 = var2.getSunBrightness(1.0F) * 0.95F + 0.05F;
					float var5 = var2.provider.lightBrightnessTable[var3 / 16] * var4;
					float var6 = var2.provider.lightBrightnessTable[var3 % 16] * (this.torchFlickerX * 0.1F + 1.5F);

					if (var2.lastLightningBolt > 0) {
						var5 = var2.provider.lightBrightnessTable[var3 / 16];
					}

					float var7 = var5 * (var2.getSunBrightness(1.0F) * 0.65F + 0.35F);
					float var8 = var5 * (var2.getSunBrightness(1.0F) * 0.65F + 0.35F);
					float var11 = var6 * ((var6 * 0.6F + 0.4F) * 0.6F + 0.4F);
					float var12 = var6 * (var6 * var6 * 0.6F + 0.4F);
					float var13 = var7 + var6;
					float var14 = var8 + var11;
					float var15 = var5 + var12;
					var13 = var13 * 0.96F + 0.03F;
					var14 = var14 * 0.96F + 0.03F;
					var15 = var15 * 0.96F + 0.03F;
					float var16;

					if (this.field_82831_U > 0.0F) {
						var16 = this.field_82832_V + (this.field_82831_U - this.field_82832_V) * par1;
						var13 = var13 * (1.0F - var16) + var13 * 0.7F * var16;
						var14 = var14 * (1.0F - var16) + var14 * 0.6F * var16;
						var15 = var15 * (1.0F - var16) + var15 * 0.6F * var16;
					}

					if (var2.provider.dimensionId == 1) {
						var13 = 0.22F + var6 * 0.75F;
						var14 = 0.28F + var11 * 0.75F;
						var15 = 0.25F + var12 * 0.75F;
					}

					float var17;

					if (this.mc.thePlayer.isPotionActive(Potion.nightVision)) {
						var16 = this.getNightVisionBrightness(this.mc.thePlayer, par1);
						var17 = 1.0F / var13;

						if (var17 > 1.0F / var14) {
							var17 = 1.0F / var14;
						}

						if (var17 > 1.0F / var15) {
							var17 = 1.0F / var15;
						}

						var13 = var13 * (1.0F - var16) + var13 * var17 * var16;
						var14 = var14 * (1.0F - var16) + var14 * var17 * var16;
						var15 = var15 * (1.0F - var16) + var15 * var17 * var16;
					}

					if (var13 > 1.0F) {
						var13 = 1.0F;
					}

					if (var14 > 1.0F) {
						var14 = 1.0F;
					}

					if (var15 > 1.0F) {
						var15 = 1.0F;
					}

					var16 = this.mc.gameSettings.gammaSetting;
					var17 = 1.0F - var13;
					float var18 = 1.0F - var14;
					float var19 = 1.0F - var15;
					var17 = 1.0F - var17 * var17 * var17 * var17;
					var18 = 1.0F - var18 * var18 * var18 * var18;
					var19 = 1.0F - var19 * var19 * var19 * var19;
					var13 = var13 * (1.0F - var16) + var17 * var16;
					var14 = var14 * (1.0F - var16) + var18 * var16;
					var15 = var15 * (1.0F - var16) + var19 * var16;
					var13 = var13 * 0.96F + 0.03F;
					var14 = var14 * 0.96F + 0.03F;
					var15 = var15 * 0.96F + 0.03F;

					if (var13 > 1.0F) {
						var13 = 1.0F;
					}

					if (var14 > 1.0F) {
						var14 = 1.0F;
					}

					if (var15 > 1.0F) {
						var15 = 1.0F;
					}

					if (var13 < 0.0F) {
						var13 = 0.0F;
					}

					if (var14 < 0.0F) {
						var14 = 0.0F;
					}

					if (var15 < 0.0F) {
						var15 = 0.0F;
					}

					short var20 = 255;
					int var21 = (int)(var13 * 255.0F);
					int var22 = (int)(var14 * 255.0F);
					int var23 = (int)(var15 * 255.0F);
					this.lightmapColors[var3] = var20 << 24 | var21 << 16 | var22 << 8 | var23;
				}

				this.mc.renderEngine.createTextureFromBytes(this.lightmapColors, 16, 16, this.lightmapTexture);
			}
		}
	}

	/**
	 * Gets the night vision brightness
	 */
	private float getNightVisionBrightness(EntityPlayer par1EntityPlayer, float par2) {
		int var3 = par1EntityPlayer.getActivePotionEffect(Potion.nightVision).getDuration();
		return var3 > 200 ? 1.0F : 0.7F + MathHelper.sin(((float)var3 - par2) * (float)Math.PI * 0.2F) * 0.3F;
	}

	/**
	 * Will update any inputs that effect the camera angle (mouse) and then render the world and GUI
	 */
	public void updateCameraAndRender(float par1) {
		// Spout Start
		World world = this.mc.theWorld;
		Block.leaves.setGraphicsLevel(Configuration.isFancyTrees());
		if (!Configuration.isWeather() && world != null && world.worldInfo != null) {
			world.worldInfo.setRaining(false);
		}

		if (world != null) {
			long rawTime = world.getWorldTime();
			long time = rawTime % 24000L;
			if (Configuration.getTime() == 2) { // Day
				if (time <= 1000L) {
					world.worldInfo.setWorldTime(rawTime - time + 1001L);
				}

				if (time >= 11000L) {
					world.worldInfo.setWorldTime(rawTime - time + 24001L);
				}
			} else if (Configuration.getTime() == 1) { // Night
				if (time <= 14000L) {
					world.worldInfo.setWorldTime(rawTime - time + 14001L);
				}

				if (time >= 22000L) {
					world.worldInfo.setWorldTime(rawTime - time + 24000L + 14001L);
				}
			}
		}
		// Spout End

		this.mc.mcProfiler.startSection("lightTex");

		if (this.lightmapUpdateNeeded) {
			this.updateLightmap(par1);
		}

		this.mc.mcProfiler.endSection();
		boolean var2 = Display.isActive();

		if (!var2 && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
			if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
				this.mc.displayInGameMenu();
			}
		} else {
			this.prevFrameTime = Minecraft.getSystemTime();
		}

		this.mc.mcProfiler.startSection("mouse");

		if (this.mc.inGameHasFocus && var2) {
			this.mc.mouseHelper.mouseXYChange();
			float var3 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			float var4 = var3 * var3 * var3 * 8.0F;
			float var5 = (float)this.mc.mouseHelper.deltaX * var4;
			float var6 = (float)this.mc.mouseHelper.deltaY * var4;
			byte var7 = 1;

			if (this.mc.gameSettings.invertMouse) {
				var7 = -1;
			}

			if (this.mc.gameSettings.smoothCamera) {
				this.smoothCamYaw += var5;
				this.smoothCamPitch += var6;
				float var8 = par1 - this.smoothCamPartialTicks;
				this.smoothCamPartialTicks = par1;
				var5 = this.smoothCamFilterX * var8;
				var6 = this.smoothCamFilterY * var8;
				this.mc.thePlayer.setAngles(var5, var6 * (float)var7);
			} else {
				this.mc.thePlayer.setAngles(var5, var6 * (float)var7);
			}
		}

		this.mc.mcProfiler.endSection();

		if (!this.mc.skipRenderWorld) {
			anaglyphEnable = this.mc.gameSettings.anaglyph;
			ScaledResolution var13 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
			int var14 = var13.getScaledWidth();
			int var15 = var13.getScaledHeight();
			int var16 = Mouse.getX() * var14 / this.mc.displayWidth;
			int var17 = var15 - Mouse.getY() * var15 / this.mc.displayHeight - 1;
			int var18 = performanceToFps(this.mc.gameSettings.limitFramerate);

			if (this.mc.theWorld != null) {
				this.mc.mcProfiler.startSection("level");

				if (this.mc.gameSettings.limitFramerate == 0) {
					this.renderWorld(par1, 0L);
				} else {
					this.renderWorld(par1, this.renderEndNanoTime + (long)(1000000000 / var18));
				}

				this.renderEndNanoTime = System.nanoTime();
				this.mc.mcProfiler.endStartSection("gui");

				if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
					// Spout Start
					if (Configuration.getFastDebug() != 0) {
						if (Minecraft.isDebugInfoEnabled()) {
							this.showDebugInfo = !this.showDebugInfo;
						}

						if (this.showDebugInfo) {
							this.mc.gameSettings.showDebugInfo = true;
						}
					}
					// Spout End

					this.mc.ingameGUI.renderGameOverlay(par1, this.mc.currentScreen != null, var16, var17);
					// Spout Start
					if (Configuration.getFastDebug() != 0) {
						this.mc.gameSettings.showDebugInfo = false;
					}
					// Spout End
				}

				this.mc.mcProfiler.endSection();
			} else {
				GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				this.setupOverlayRendering();
				this.renderEndNanoTime = System.nanoTime();
			}

			if (this.mc.currentScreen != null) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

				try {
					this.mc.currentScreen.drawScreen(var16, var17, par1);
				} catch (Throwable var12) {
					CrashReport var10 = CrashReport.makeCrashReport(var12, "Rendering screen");
					CrashReportCategory var11 = var10.makeCategory("Screen render details");
					var11.addCrashSectionCallable("Screen name", new CallableScreenName(this));
					var11.addCrashSectionCallable("Mouse location", new CallableMouseLocation(this, var16, var17));
					var11.addCrashSectionCallable("Screen size", new CallableScreenSize(this, var13));
					throw new ReportedException(var10);
				}

				if (this.mc.currentScreen!= null) {
					this.mc.currentScreen.drawScreenPre(var16, var17, par1);
				} else {
					System.out.println("SpoutDebug:  CurrentScreen was null prior to trying to drawScreenPre");
				}

				if (this.mc.currentScreen != null && this.mc.currentScreen.guiParticles != null) {
					this.mc.currentScreen.guiParticles.draw(par1);
				}
			}
		}
	}

	public void renderWorld(float par1, long par2) {
		this.mc.mcProfiler.startSection("lightTex");

		if (this.lightmapUpdateNeeded) {
			this.updateLightmap(par1);
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		if (this.mc.renderViewEntity == null) {
			this.mc.renderViewEntity = this.mc.thePlayer;
		}

		this.mc.mcProfiler.endStartSection("pick");
		this.getMouseOver(par1);
		EntityLiving var4 = this.mc.renderViewEntity;
		RenderGlobal var5 = this.mc.renderGlobal;
		EffectRenderer var6 = this.mc.effectRenderer;
		double var7 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)par1;
		double var9 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)par1;
		double var11 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)par1;
		this.mc.mcProfiler.endStartSection("center");

		// Spout Start
		for (int var13 = 0; var13 < (this.mc.gameSettings.anaglyph ? 2 : 1); ++var13) {
		// Spout End
			if (this.mc.gameSettings.anaglyph) {
				anaglyphField = var13;

				if (anaglyphField == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			this.mc.mcProfiler.endStartSection("clear");
			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			this.updateFogColor(par1);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			this.mc.mcProfiler.endStartSection("camera");
			this.setupCameraTransform(par1, var13);
			ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
			this.mc.mcProfiler.endStartSection("frustrum");
			ClippingHelperImpl.getInstance();

			// Spout Start
			if (this.mc.gameSettings.renderDistance < 2 || Configuration.isFarView()) {
			// Spout End
				this.setupFog(-1, par1);
				this.mc.mcProfiler.endStartSection("sky");
				var5.renderSky(par1);
			}

			GL11.glEnable(GL11.GL_FOG);
			this.setupFog(1, par1);

			// MCPatcher Start
			if (RenderPass.setAmbientOcclusion(this.mc.gameSettings.ambientOcclusion >= 2)) {
			// MCPatcher End
				GL11.glShadeModel(GL11.GL_SMOOTH);
			}

			this.mc.mcProfiler.endStartSection("culling");
			Frustrum var14 = new Frustrum();
			var14.setPosition(var7, var9, var11);
			this.mc.renderGlobal.clipRenderersByFrustum(var14, par1);

			if (var13 == 0) {
				this.mc.mcProfiler.endStartSection("updatechunks");

				while (!this.mc.renderGlobal.updateRenderers(var4, false) && par2 != 0L) {
					long var15 = par2 - System.nanoTime();

					if (var15 < 0L || var15 > 1000000000L) {
						break;
					}
				}
			}

			if (var4.posY < 128.0D) {
				this.renderCloudsCheck(var5, par1);
			}
			this.mc.mcProfiler.endStartSection("prepareterrain");
			this.setupFog(0, par1);
			GL11.glEnable(GL11.GL_FOG);
			this.mc.renderEngine.bindTexture("/terrain.png");
			RenderHelper.disableStandardItemLighting();
			this.mc.mcProfiler.endStartSection("terrain");
			var5.sortAndRender(var4, 0, (double)par1);
			// MCPatcher Start
			RenderPass.doRenderPass(var5, var4, 2, (double)par1);
			// MCPatcher End
			GL11.glShadeModel(GL11.GL_FLAT);
			EntityPlayer var17;

			if (this.debugViewDirection == 0) {
				RenderHelper.enableStandardItemLighting();
				this.mc.mcProfiler.endStartSection("entities");
				var5.renderEntities(var4.getPosition(par1), var14, par1);
				this.enableLightmap((double)par1);
				this.mc.mcProfiler.endStartSection("litParticles");
				var6.renderLitParticles(var4, par1);
				RenderHelper.disableStandardItemLighting();
				this.setupFog(0, par1);
				this.mc.mcProfiler.endStartSection("particles");
				var6.renderParticles(var4, par1);
				this.disableLightmap((double)par1);

				if (this.mc.objectMouseOver != null && var4.isInsideOfMaterial(Material.water) && var4 instanceof EntityPlayer && !this.mc.gameSettings.hideGUI) {
					var17 = (EntityPlayer)var4;
					GL11.glDisable(GL11.GL_ALPHA_TEST);
					this.mc.mcProfiler.endStartSection("outline");
					var5.drawBlockBreaking(var17, this.mc.objectMouseOver, 0, var17.inventory.getCurrentItem(), par1);
					var5.drawSelectionBox(var17, this.mc.objectMouseOver, 0, var17.inventory.getCurrentItem(), par1);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDepthMask(true);
			this.setupFog(0, par1);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_CULL_FACE);
			this.mc.renderEngine.bindTexture("/terrain.png");

			if (this.mc.gameSettings.fancyGraphics) {
				this.mc.mcProfiler.endStartSection("water");

				// Spout End
				if (RenderPass.setAmbientOcclusion(this.mc.gameSettings.ambientOcclusion >= 2)) {
				// Spout Start
					GL11.glShadeModel(GL11.GL_SMOOTH);
				}

				GL11.glColorMask(false, false, false, false);
				int var18 = var5.sortAndRender(var4, 1, (double)par1);

				if (this.mc.gameSettings.anaglyph) {
					if (anaglyphField == 0) {
						GL11.glColorMask(false, true, true, true);
					} else {
						GL11.glColorMask(true, false, false, true);
					}
				} else {
					GL11.glColorMask(true, true, true, true);
				}

				if (var18 > 0) {
					var5.renderAllRenderLists(1, (double)par1);
				}

				GL11.glShadeModel(GL11.GL_FLAT);
			} else {
				this.mc.mcProfiler.endStartSection("water");
				var5.sortAndRender(var4, 1, (double)par1);
			}

			// MCPatcher Start
			this.renderRainSnow(par1);
			RenderPass.doRenderPass(var5, var4, 3, (double)par1);
			// MCPatcher End
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);

			if (this.cameraZoom == 1.0D && var4 instanceof EntityPlayer && !this.mc.gameSettings.hideGUI && this.mc.objectMouseOver != null && !var4.isInsideOfMaterial(Material.water)) {
				var17 = (EntityPlayer)var4;
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				this.mc.mcProfiler.endStartSection("outline");
				var5.drawBlockBreaking(var17, this.mc.objectMouseOver, 0, var17.inventory.getCurrentItem(), par1);
				var5.drawSelectionBox(var17, this.mc.objectMouseOver, 0, var17.inventory.getCurrentItem(), par1);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			this.mc.mcProfiler.endStartSection("destroyProgress");
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			var5.drawBlockDamageTexture(Tessellator.instance, (EntityPlayer)var4, par1);
			GL11.glDisable(GL11.GL_BLEND);
			this.mc.mcProfiler.endStartSection("weather");
			this.renderRainSnow(par1);
			GL11.glDisable(GL11.GL_FOG);

			if (var4.posY >= 128.0D) {
				this.renderCloudsCheck(var5, par1);
			}

			this.mc.mcProfiler.endStartSection("hand");

			if (this.cameraZoom == 1.0D) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				this.renderHand(par1, var13);
			}

			if (!this.mc.gameSettings.anaglyph) {
				this.mc.mcProfiler.endSection();
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
		this.mc.mcProfiler.endSection();
	}

	/**
	 * Render clouds if enabled
	 */
	private void renderCloudsCheck(RenderGlobal par1RenderGlobal, float par2) {
		if (this.mc.gameSettings.shouldRenderClouds()) {
			this.mc.mcProfiler.endStartSection("clouds");
			GL11.glPushMatrix();
			this.setupFog(0, par2);
			GL11.glEnable(GL11.GL_FOG);
			par1RenderGlobal.renderClouds(par2);
			GL11.glDisable(GL11.GL_FOG);
			this.setupFog(1, par2);
			GL11.glPopMatrix();
		}
	}

	private void addRainParticles() {
		float var1 = this.mc.theWorld.getRainStrength(1.0F);

		// Spout Start
		if (!Configuration.isWeather()) {
			return;
		}
		if (SpoutWorth.getInstance().isBelowIdeal()) {
			var1 /= 4.0F;
		}
		if (!Configuration.isFancyWeather()) {
			var1 /= 2.0F;
		}
		// Spout End

		if (!this.mc.gameSettings.fancyGraphics) {
			var1 /= 2.0F;
		}

		if (var1 != 0.0F) {
			this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
			EntityLiving var2 = this.mc.renderViewEntity;
			WorldClient var3 = this.mc.theWorld;
			int var4 = MathHelper.floor_double(var2.posX);
			int var5 = MathHelper.floor_double(var2.posY);
			int var6 = MathHelper.floor_double(var2.posZ);
			byte var7 = 10;
			double var8 = 0.0D;
			double var10 = 0.0D;
			double var12 = 0.0D;
			int var14 = 0;
			int var15 = (int)(100.0F * var1 * var1);

			if (this.mc.gameSettings.particleSetting == 1) {
				var15 >>= 1;
			} else if (this.mc.gameSettings.particleSetting == 2) {
				var15 = 0;
			}

			for (int var16 = 0; var16 < var15; ++var16) {
				int var17 = var4 + this.random.nextInt(var7) - this.random.nextInt(var7);
				int var18 = var6 + this.random.nextInt(var7) - this.random.nextInt(var7);
				int var19 = var3.getPrecipitationHeight(var17, var18);
				int var20 = var3.getBlockId(var17, var19 - 1, var18);
				BiomeGenBase var21 = var3.getBiomeGenForCoords(var17, var18);

				if (var19 <= var5 + var7 && var19 >= var5 - var7 && var21.canSpawnLightningBolt() && var21.getFloatTemperature() >= 0.2F) {
					float var22 = this.random.nextFloat();
					float var23 = this.random.nextFloat();

					if (var20 > 0) {
						if (Block.blocksList[var20].blockMaterial == Material.lava) {
							this.mc.effectRenderer.addEffect(new EntitySmokeFX(var3, (double)((float)var17 + var22), (double)((float)var19 + 0.1F) - Block.blocksList[var20].getBlockBoundsMinY(), (double)((float)var18 + var23), 0.0D, 0.0D, 0.0D));
						} else {
							++var14;

							if (this.random.nextInt(var14) == 0) {
								var8 = (double)((float)var17 + var22);
								var10 = (double)((float)var19 + 0.1F) - Block.blocksList[var20].getBlockBoundsMinY();
								var12 = (double)((float)var18 + var23);
							}

							this.mc.effectRenderer.addEffect(new EntityRainFX(var3, (double)((float)var17 + var22), (double)((float)var19 + 0.1F) - Block.blocksList[var20].getBlockBoundsMinY(), (double)((float)var18 + var23)));
						}
					}
				}
			}

			if (var14 > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
				this.rainSoundCounter = 0;

				if (var10 > var2.posY + 1.0D && var3.getPrecipitationHeight(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posZ)) > MathHelper.floor_double(var2.posY)) {
					this.mc.theWorld.playSound(var8, var10, var12, "ambient.weather.rain", 0.1F, 0.5F, false);
				} else {
					this.mc.theWorld.playSound(var8, var10, var12, "ambient.weather.rain", 0.2F, 1.0F, false);
				}
			}
		}
	}

	/**
	 * Render rain and snow
	 */
	protected void renderRainSnow(float par1) {
		// Spout Start
		if (!Configuration.isWeather()) {
			return;
		}
		// Spout End
		float var2 = this.mc.theWorld.getRainStrength(par1);

		if (var2 > 0.0F) {
			this.enableLightmap((double)par1);

			if (this.rainXCoords == null) {
				this.rainXCoords = new float[1024];
				this.rainYCoords = new float[1024];

				for (int var3 = 0; var3 < 32; ++var3) {
					for (int var4 = 0; var4 < 32; ++var4) {
						float var5 = (float)(var4 - 16);
						float var6 = (float)(var3 - 16);
						float var7 = MathHelper.sqrt_float(var5 * var5 + var6 * var6);
						this.rainXCoords[var3 << 5 | var4] = -var6 / var7;
						this.rainYCoords[var3 << 5 | var4] = var5 / var7;
					}
				}
			}

			EntityLiving var41 = this.mc.renderViewEntity;
			WorldClient var42 = this.mc.theWorld;
			int var43 = MathHelper.floor_double(var41.posX);
			int var44 = MathHelper.floor_double(var41.posY);
			int var45 = MathHelper.floor_double(var41.posZ);
			Tessellator var8 = Tessellator.instance;
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
			this.mc.renderEngine.bindTexture("/environment/snow.png");
			double var9 = var41.lastTickPosX + (var41.posX - var41.lastTickPosX) * (double)par1;
			double var11 = var41.lastTickPosY + (var41.posY - var41.lastTickPosY) * (double)par1;
			double var13 = var41.lastTickPosZ + (var41.posZ - var41.lastTickPosZ) * (double)par1;
			int var15 = MathHelper.floor_double(var11);
			byte var16 = 5;

			// Spout Start
			if (Configuration.isFancyWeather()) {
			// Spout End
				var16 = 10;
			}

			boolean var17 = false;
			byte var18 = -1;
			float var19 = (float)this.rendererUpdateCount + par1;

			if (this.mc.gameSettings.fancyGraphics) {
				var16 = 10;
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			var17 = false;

			for (int var20 = var45 - var16; var20 <= var45 + var16; ++var20) {
				for (int var21 = var43 - var16; var21 <= var43 + var16; ++var21) {
					int var22 = (var20 - var45 + 16) * 32 + var21 - var43 + 16;
					float var23 = this.rainXCoords[var22] * 0.5F;
					float var24 = this.rainYCoords[var22] * 0.5F;
					BiomeGenBase var25 = var42.getBiomeGenForCoords(var21, var20);

					if (var25.canSpawnLightningBolt() || var25.getEnableSnow()) {
						int var26 = var42.getPrecipitationHeight(var21, var20);
						int var27 = var44 - var16;
						int var28 = var44 + var16;

						if (var27 < var26) {
							var27 = var26;
						}

						if (var28 < var26) {
							var28 = var26;
						}

						float var29 = 1.0F;
						int var30 = var26;

						if (var26 < var15) {
							var30 = var15;
						}

						if (var27 != var28) {
							this.random.setSeed((long)(var21 * var21 * 3121 + var21 * 45238971 ^ var20 * var20 * 418711 + var20 * 13761));
							float var31 = var25.getFloatTemperature();
							double var35;
							float var32;

							if (var42.getWorldChunkManager().getTemperatureAtHeight(var31, var26) >= 0.15F) {
								if (var18 != 0) {
									if (var18 >= 0) {
										var8.draw();
									}

									var18 = 0;
									this.mc.renderEngine.bindTexture("/environment/rain.png");
									var8.startDrawingQuads();
								}

								var32 = ((float)(this.rendererUpdateCount + var21 * var21 * 3121 + var21 * 45238971 + var20 * var20 * 418711 + var20 * 13761 & 31) + par1) / 32.0F * (3.0F + this.random.nextFloat());
								double var33 = (double)((float)var21 + 0.5F) - var41.posX;
								var35 = (double)((float)var20 + 0.5F) - var41.posZ;
								float var37 = MathHelper.sqrt_double(var33 * var33 + var35 * var35) / (float)var16;
								float var38 = 1.0F;
								var8.setBrightness(var42.getLightBrightnessForSkyBlocks(var21, var30, var20, 0));
								var8.setColorRGBA_F(var38, var38, var38, ((1.0F - var37 * var37) * 0.5F + 0.5F) * var2);
								var8.setTranslation(-var9 * 1.0D, -var11 * 1.0D, -var13 * 1.0D);
								var8.addVertexWithUV((double)((float)var21 - var23) + 0.5D, (double)var27, (double)((float)var20 - var24) + 0.5D, (double)(0.0F * var29), (double)((float)var27 * var29 / 4.0F + var32 * var29));
								var8.addVertexWithUV((double)((float)var21 + var23) + 0.5D, (double)var27, (double)((float)var20 + var24) + 0.5D, (double)(1.0F * var29), (double)((float)var27 * var29 / 4.0F + var32 * var29));
								var8.addVertexWithUV((double)((float)var21 + var23) + 0.5D, (double)var28, (double)((float)var20 + var24) + 0.5D, (double)(1.0F * var29), (double)((float)var28 * var29 / 4.0F + var32 * var29));
								var8.addVertexWithUV((double)((float)var21 - var23) + 0.5D, (double)var28, (double)((float)var20 - var24) + 0.5D, (double)(0.0F * var29), (double)((float)var28 * var29 / 4.0F + var32 * var29));
								var8.setTranslation(0.0D, 0.0D, 0.0D);
							} else {
								if (var18 != 1) {
									if (var18 >= 0) {
										var8.draw();
									}

									var18 = 1;
									this.mc.renderEngine.bindTexture("/environment/snow.png");
									var8.startDrawingQuads();
								}

								var32 = ((float)(this.rendererUpdateCount & 511) + par1) / 512.0F;
								float var46 = this.random.nextFloat() + var19 * 0.01F * (float)this.random.nextGaussian();
								float var34 = this.random.nextFloat() + var19 * (float)this.random.nextGaussian() * 0.001F;
								var35 = (double)((float)var21 + 0.5F) - var41.posX;
								double var47 = (double)((float)var20 + 0.5F) - var41.posZ;
								float var39 = MathHelper.sqrt_double(var35 * var35 + var47 * var47) / (float)var16;
								float var40 = 1.0F;
								var8.setBrightness((var42.getLightBrightnessForSkyBlocks(var21, var30, var20, 0) * 3 + 15728880) / 4);
								var8.setColorRGBA_F(var40, var40, var40, ((1.0F - var39 * var39) * 0.3F + 0.5F) * var2);
								var8.setTranslation(-var9 * 1.0D, -var11 * 1.0D, -var13 * 1.0D);
								var8.addVertexWithUV((double)((float)var21 - var23) + 0.5D, (double)var27, (double)((float)var20 - var24) + 0.5D, (double)(0.0F * var29 + var46), (double)((float)var27 * var29 / 4.0F + var32 * var29 + var34));
								var8.addVertexWithUV((double)((float)var21 + var23) + 0.5D, (double)var27, (double)((float)var20 + var24) + 0.5D, (double)(1.0F * var29 + var46), (double)((float)var27 * var29 / 4.0F + var32 * var29 + var34));
								var8.addVertexWithUV((double)((float)var21 + var23) + 0.5D, (double)var28, (double)((float)var20 + var24) + 0.5D, (double)(1.0F * var29 + var46), (double)((float)var28 * var29 / 4.0F + var32 * var29 + var34));
								var8.addVertexWithUV((double)((float)var21 - var23) + 0.5D, (double)var28, (double)((float)var20 - var24) + 0.5D, (double)(0.0F * var29 + var46), (double)((float)var28 * var29 / 4.0F + var32 * var29 + var34));
								var8.setTranslation(0.0D, 0.0D, 0.0D);
							}
						}
					}
				}
			}

			if (var18 >= 0) {
				var8.draw();
			}

			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			this.disableLightmap((double)par1);
		}
	}

	/**
	 * Setup orthogonal projection for rendering GUI screen overlays
	 */
	public void setupOverlayRendering() {
		ScaledResolution var1 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, var1.getScaledWidth_double(), var1.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	/**
	 * calculates fog and calls glClearColor
	 */
	private void updateFogColor(float par1) {
		WorldClient var2 = this.mc.theWorld;
		EntityLiving var3 = this.mc.renderViewEntity;
		float var4 = 1.0F / (float)(4 - this.mc.gameSettings.renderDistance);
		// MCPatcher Start
		ColorizeWorld.setupForFog(var3);

		if (ColorizeWorld.computeFogColor(var2, par1)) {
			this.fogColorRed = Colorizer.setColor[0];
			this.fogColorGreen = Colorizer.setColor[1];
			this.fogColorBlue = Colorizer.setColor[2];
		}
		// MCPatcher End

		var4 = 1.0F - (float)Math.pow((double)var4, 0.25D);
		Vec3 var5 = var2.getSkyColor(this.mc.renderViewEntity, par1);
		float var6 = (float)var5.xCoord;
		float var7 = (float)var5.yCoord;
		float var8 = (float)var5.zCoord;
		Vec3 var9 = var2.getFogColor(par1);
		this.fogColorRed = (float)var9.xCoord;
		this.fogColorGreen = (float)var9.yCoord;
		this.fogColorBlue = (float)var9.zCoord;
		float var11;

		if (this.mc.gameSettings.renderDistance < 2) {
			Vec3 var10 = MathHelper.sin(var2.getCelestialAngleRadians(par1)) > 0.0F ? var2.getWorldVec3Pool().getVecFromPool(-1.0D, 0.0D, 0.0D) : var2.getWorldVec3Pool().getVecFromPool(1.0D, 0.0D, 0.0D);
			var11 = (float)var3.getLook(par1).dotProduct(var10);

			if (var11 < 0.0F) {
				var11 = 0.0F;
			}

			if (var11 > 0.0F) {
				float[] var12 = var2.provider.calcSunriseSunsetColors(var2.getCelestialAngle(par1), par1);

				if (var12 != null) {
					var11 *= var12[3];
					this.fogColorRed = this.fogColorRed * (1.0F - var11) + var12[0] * var11;
					this.fogColorGreen = this.fogColorGreen * (1.0F - var11) + var12[1] * var11;
					this.fogColorBlue = this.fogColorBlue * (1.0F - var11) + var12[2] * var11;
				}
			}
		}

		this.fogColorRed += (var6 - this.fogColorRed) * var4;
		this.fogColorGreen += (var7 - this.fogColorGreen) * var4;
		this.fogColorBlue += (var8 - this.fogColorBlue) * var4;
		float var19 = var2.getRainStrength(par1);
		float var20;

		if (var19 > 0.0F) {
			var11 = 1.0F - var19 * 0.5F;
			var20 = 1.0F - var19 * 0.4F;
			this.fogColorRed *= var11;
			this.fogColorGreen *= var11;
			this.fogColorBlue *= var20;
		}

		var11 = var2.getWeightedThunderStrength(par1);

		if (var11 > 0.0F) {
			var20 = 1.0F - var11 * 0.5F;
			this.fogColorRed *= var20;
			this.fogColorGreen *= var20;
			this.fogColorBlue *= var20;
		}

		int var21 = ActiveRenderInfo.getBlockIdAtEntityViewpoint(this.mc.theWorld, var3, par1);

		if (this.cloudFog) {
			Vec3 var13 = var2.getCloudColour(par1);
			this.fogColorRed = (float)var13.xCoord;
			this.fogColorGreen = (float)var13.yCoord;
			this.fogColorBlue = (float)var13.zCoord;
		} else if (var21 != 0 && Block.blocksList[var21].blockMaterial == Material.water) {
			this.fogColorRed = 0.02F;
			this.fogColorGreen = 0.02F;
			this.fogColorBlue = 0.2F;
			this.fogColorRed = Colorizer.setColor[0];
			this.fogColorGreen = Colorizer.setColor[1];
			this.fogColorBlue = Colorizer.setColor[2];
		} else if (var21 != 0 && Block.blocksList[var21].blockMaterial == Material.lava) {
			this.fogColorRed = 0.6F;
			this.fogColorGreen = 0.1F;
			this.fogColorBlue = 0.0F;
		}

		float var22 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * par1;
		this.fogColorRed *= var22;
		this.fogColorGreen *= var22;
		this.fogColorBlue *= var22;
		double var14 = (var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * (double)par1) * var2.provider.getVoidFogYFactor();

		if (var3.isPotionActive(Potion.blindness)) {
			int var16 = var3.getActivePotionEffect(Potion.blindness).getDuration();

			if (var16 < 20) {
				var14 *= (double)(1.0F - (float)var16 / 20.0F);
			} else {
				var14 = 0.0D;
			}
		}

		if (var14 < 1.0D) {
			if (var14 < 0.0D) {
				var14 = 0.0D;
			}

			var14 *= var14;
			this.fogColorRed = (float)((double)this.fogColorRed * var14);
			this.fogColorGreen = (float)((double)this.fogColorGreen * var14);
			this.fogColorBlue = (float)((double)this.fogColorBlue * var14);
		}

		float var23;

		if (this.field_82831_U > 0.0F) {
			var23 = this.field_82832_V + (this.field_82831_U - this.field_82832_V) * par1;
			this.fogColorRed = this.fogColorRed * (1.0F - var23) + this.fogColorRed * 0.7F * var23;
			this.fogColorGreen = this.fogColorGreen * (1.0F - var23) + this.fogColorGreen * 0.6F * var23;
			this.fogColorBlue = this.fogColorBlue * (1.0F - var23) + this.fogColorBlue * 0.6F * var23;
		}

		float var17;

		if (var3.isPotionActive(Potion.nightVision)) {
			var23 = this.getNightVisionBrightness(this.mc.thePlayer, par1);
			var17 = 1.0F / this.fogColorRed;

			if (var17 > 1.0F / this.fogColorGreen) {
				var17 = 1.0F / this.fogColorGreen;
			}

			if (var17 > 1.0F / this.fogColorBlue) {
				var17 = 1.0F / this.fogColorBlue;
			}

			this.fogColorRed = this.fogColorRed * (1.0F - var23) + this.fogColorRed * var17 * var23;
			this.fogColorGreen = this.fogColorGreen * (1.0F - var23) + this.fogColorGreen * var17 * var23;
			this.fogColorBlue = this.fogColorBlue * (1.0F - var23) + this.fogColorBlue * var17 * var23;
		}

		if (this.mc.gameSettings.anaglyph) {
			var23 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
			var17 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
			float var18 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
			this.fogColorRed = var23;
			this.fogColorGreen = var17;
			this.fogColorBlue = var18;
		}

		GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
	}

	/**
	 * Sets up the fog to be rendered. If the arg passed in is -1 the fog starts at 0 and goes to 80% of far plane distance
	 * and is used for sky rendering.
	 */
	private void setupFog(int par1, float par2) {
		EntityLiving var3 = this.mc.renderViewEntity;
		boolean var4 = false;

		if (var3 instanceof EntityPlayer) {
			var4 = ((EntityPlayer)var3).capabilities.isCreativeMode;
		}

		if (par1 == 999) {
			GL11.glFog(GL11.GL_FOG_COLOR, this.setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
			GL11.glFogf(GL11.GL_FOG_END, 8.0F);

			if (GLContext.getCapabilities().GL_NV_fog_distance) {
				GL11.glFogi(34138, 34139);
			}

			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
		} else {
			GL11.glFog(GL11.GL_FOG_COLOR, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
			GL11.glNormal3f(0.0F, -1.0F, 0.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			int var5 = ActiveRenderInfo.getBlockIdAtEntityViewpoint(this.mc.theWorld, var3, par2);
			float var6;

			if (var3.isPotionActive(Potion.blindness)) {
				var6 = 5.0F;
				int var7 = var3.getActivePotionEffect(Potion.blindness).getDuration();

				if (var7 < 20) {
					var6 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float)var7 / 20.0F);
				}

				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);

				if (par1 < 0) {
					GL11.glFogf(GL11.GL_FOG_START, 0.0F);
					GL11.glFogf(GL11.GL_FOG_END, var6 * 0.8F);
				} else {
					GL11.glFogf(GL11.GL_FOG_START, var6 * 0.25F);
					GL11.glFogf(GL11.GL_FOG_END, var6);
				}

				if (GLContext.getCapabilities().GL_NV_fog_distance) {
					GL11.glFogi(34138, 34139);
				}
			} else {
				float var8;
				float var9;
				float var10;
				float var11;
				float var12;

				if (this.cloudFog) {
					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
					// Spout Start
					float density = 0.1F;
					if (var3.isPotionActive(Potion.waterBreathing)) {
						density = 0.05F;
					}
					if (Configuration.isClearWater()) {
						density = 0.02F;
					}
					GL11.glFogf(GL11.GL_FOG_DENSITY, density);
					// Spout End
					GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
					var6 = 1.0F;
					var12 = 1.0F;
					var8 = 1.0F;

					if (this.mc.gameSettings.anaglyph) {
						var9 = (var6 * 30.0F + var12 * 59.0F + var8 * 11.0F) / 100.0F;
						var10 = (var6 * 30.0F + var12 * 70.0F) / 100.0F;
						var11 = (var6 * 30.0F + var8 * 70.0F) / 100.0F;
					}
				} else if (var5 > 0 && Block.blocksList[var5].blockMaterial == Material.water) {
					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
					// Spout Start
					float density = 0.1F;
					if (Configuration.isClearWater() || (var3.isPotionActive(Potion.waterBreathing))) {
						density = 0.05F;
					} else {
						density = 0.1F;
					}
					 GL11.glFogf(GL11.GL_FOG_DENSITY, density);
					// Spout End
					var6 = 0.4F;
					var12 = 0.4F;
					var8 = 0.9F;

					if (this.mc.gameSettings.anaglyph) {
						var9 = (var6 * 30.0F + var12 * 59.0F + var8 * 11.0F) / 100.0F;
						var10 = (var6 * 30.0F + var12 * 70.0F) / 100.0F;
						var11 = (var6 * 30.0F + var8 * 70.0F) / 100.0F;
					}
				} else if (var5 > 0 && Block.blocksList[var5].blockMaterial == Material.lava) {
					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
					GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
					var6 = 0.4F;
					var12 = 0.3F;
					var8 = 0.3F;

					if (this.mc.gameSettings.anaglyph) {
						var9 = (var6 * 30.0F + var12 * 59.0F + var8 * 11.0F) / 100.0F;
						var10 = (var6 * 30.0F + var12 * 70.0F) / 100.0F;
						var11 = (var6 * 30.0F + var8 * 70.0F) / 100.0F;
					}
				} else {
					var6 = this.farPlaneDistance;

					if (this.mc.theWorld.provider.getWorldHasVoidParticles() && !var4) {
						double var13 = (double)((var3.getBrightnessForRender(par2) & 15728640) >> 20) / 16.0D + (var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * (double)par2 + 4.0D) / 32.0D;

						if (var13 < 1.0D) {
							if (var13 < 0.0D) {
								var13 = 0.0D;
							}

							var13 *= var13;
							var9 = 100.0F * (float)var13;

							if (var9 < 5.0F) {
								var9 = 5.0F;
							}

							if (var6 > var9) {
								var6 = var9;
							}
						}
					}

					// Spout Start
					if (!Configuration.isVoidFog()) {
						var6 = 0.8F * this.farPlaneDistance;
						var12 = this.farPlaneDistance;
					}
					// Spout End

					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);

					if (par1 < 0) {
						GL11.glFogf(GL11.GL_FOG_START, 0.0F);
						GL11.glFogf(GL11.GL_FOG_END, var6 * 0.8F);
					} else {
						GL11.glFogf(GL11.GL_FOG_START, var6 * 0.25F);
						GL11.glFogf(GL11.GL_FOG_END, var6);
					}

					if (GLContext.getCapabilities().GL_NV_fog_distance) {
						// Spout Start
						//TODO: Previous fog calculation may work better.
						//GL11.glFogi(34138, 34139);
						if (Configuration.isFancyFog()) {
							GL11.glFogi('\u855a', '\u855b');
						} else {
							GL11.glFogi('\u855a', '\u855c');
						}
						// Spout End
					}

					if (this.mc.theWorld.provider.doesXZShowFog((int)var3.posX, (int)var3.posZ)) {
						GL11.glFogf(GL11.GL_FOG_START, var6 * 0.05F);
						GL11.glFogf(GL11.GL_FOG_END, Math.min(var6, 192.0F) * 0.5F);
					}
				}
			}

			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
		}
	}

	/**
	 * Update and return fogColorBuffer with the RGBA values passed as arguments
	 */
	private FloatBuffer setFogColorBuffer(float par1, float par2, float par3, float par4) {
		this.fogColorBuffer.clear();
		this.fogColorBuffer.put(par1).put(par2).put(par3).put(par4);
		this.fogColorBuffer.flip();
		return this.fogColorBuffer;
	}

	/**
	 * Converts performance value (0-2) to FPS (35-200)
	 */
	public static int performanceToFps(int par0) {
		short var1 = 200;

		if (par0 == 1) {
			var1 = 120;
		}

		if (par0 == 2) {
			var1 = 35;
		}

		return var1;
	}

	/**
	 * Get minecraft reference from the EntityRenderer
	 */
	static Minecraft getRendererMinecraft(EntityRenderer par0EntityRenderer) {
		return par0EntityRenderer.mc;
	}

	// MCPatcher Start
	public float getNightVisionStrength(float var1) {
		return this.mc.thePlayer.isPotionActive(Potion.nightVision) ? this.getNightVisionBrightness(this.mc.thePlayer, var1) : 0.0F;
	}
	// MCPatcher End
}
