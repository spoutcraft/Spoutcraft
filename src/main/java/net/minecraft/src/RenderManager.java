package net.minecraft.src;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.lwjgl.opengl.GL11;
// Spout Start
import org.spoutcraft.client.entity.EntityText;
import org.spoutcraft.client.entity.EntityTexture;
import org.spoutcraft.client.entity.RenderText;
import org.spoutcraft.client.entity.RenderTexture;
// Spout End

public class RenderManager {

	/** A map of entity classes and the associated renderer. */
	// Spout Start - private to public
	public Map entityRenderMap = new HashMap();
	// Spout End

	/** The static instance of RenderManager. */
	public static RenderManager instance = new RenderManager();

	/** Renders fonts */
	private FontRenderer fontRenderer;
	public static double renderPosX;
	public static double renderPosY;
	public static double renderPosZ;
	public RenderEngine renderEngine;
	public ItemRenderer itemRenderer;

	/** Reference to the World object. */
	public World worldObj;

	/** Rendermanager's variable for the player */
	public EntityLiving livingPlayer;
	public EntityLiving field_96451_i;
	public float playerViewY;
	public float playerViewX;

	/** Reference to the GameSettings object. */
	public GameSettings options;
	public double viewerPosX;
	public double viewerPosY;
	public double viewerPosZ;
	public static boolean field_85095_o = false;

	private RenderManager() {
		this.entityRenderMap.put(EntitySpider.class, new RenderSpider());
		this.entityRenderMap.put(EntityCaveSpider.class, new RenderSpider());
		this.entityRenderMap.put(EntityPig.class, new RenderPig(new ModelPig(), new ModelPig(0.5F), 0.7F));
		this.entityRenderMap.put(EntitySheep.class, new RenderSheep(new ModelSheep2(), new ModelSheep1(), 0.7F));
		this.entityRenderMap.put(EntityCow.class, new RenderCow(new ModelCow(), 0.7F));
		this.entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(new ModelCow(), 0.7F));
		this.entityRenderMap.put(EntityWolf.class, new RenderWolf(new ModelWolf(), new ModelWolf(), 0.5F));
		this.entityRenderMap.put(EntityChicken.class, new RenderChicken(new ModelChicken(), 0.3F));
		this.entityRenderMap.put(EntityOcelot.class, new RenderOcelot(new ModelOcelot(), 0.4F));
		this.entityRenderMap.put(EntitySilverfish.class, new RenderSilverfish());
		this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper());
		this.entityRenderMap.put(EntityEnderman.class, new RenderEnderman());
		this.entityRenderMap.put(EntitySnowman.class, new RenderSnowMan());
		this.entityRenderMap.put(EntitySkeleton.class, new RenderSkeleton());
		this.entityRenderMap.put(EntityWitch.class, new RenderWitch());
		this.entityRenderMap.put(EntityBlaze.class, new RenderBlaze());
		this.entityRenderMap.put(EntityZombie.class, new RenderZombie());
		this.entityRenderMap.put(EntitySlime.class, new RenderSlime(new ModelSlime(16), new ModelSlime(0), 0.25F));
		this.entityRenderMap.put(EntityMagmaCube.class, new RenderMagmaCube());
		this.entityRenderMap.put(EntityPlayer.class, new RenderPlayer());
		this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(new ModelZombie(), 0.5F, 6.0F));
		this.entityRenderMap.put(EntityGhast.class, new RenderGhast());
		this.entityRenderMap.put(EntitySquid.class, new RenderSquid(new ModelSquid(), 0.7F));
		this.entityRenderMap.put(EntityVillager.class, new RenderVillager());
		this.entityRenderMap.put(EntityIronGolem.class, new RenderIronGolem());
		this.entityRenderMap.put(EntityLiving.class, new RenderLiving(new ModelBiped(), 0.5F));
		this.entityRenderMap.put(EntityBat.class, new RenderBat());
		this.entityRenderMap.put(EntityDragon.class, new RenderDragon());
		this.entityRenderMap.put(EntityEnderCrystal.class, new RenderEnderCrystal());
		this.entityRenderMap.put(EntityWither.class, new RenderWither());
		this.entityRenderMap.put(Entity.class, new RenderEntity());
		this.entityRenderMap.put(EntityPainting.class, new RenderPainting());
		this.entityRenderMap.put(EntityItemFrame.class, new RenderItemFrame());
		this.entityRenderMap.put(EntityArrow.class, new RenderArrow());
		this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball(Item.snowball));
		this.entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball(Item.enderPearl));
		this.entityRenderMap.put(EntityEnderEye.class, new RenderSnowball(Item.eyeOfEnder));
		this.entityRenderMap.put(EntityEgg.class, new RenderSnowball(Item.egg));
		this.entityRenderMap.put(EntityPotion.class, new RenderSnowball(Item.potion, 16384));
		this.entityRenderMap.put(EntityExpBottle.class, new RenderSnowball(Item.expBottle));
		this.entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball(Item.firework));
		this.entityRenderMap.put(EntityLargeFireball.class, new RenderFireball(2.0F));
		this.entityRenderMap.put(EntitySmallFireball.class, new RenderFireball(0.5F));
		this.entityRenderMap.put(EntityWitherSkull.class, new RenderWitherSkull());
		this.entityRenderMap.put(EntityItem.class, new RenderItem());
		this.entityRenderMap.put(EntityXPOrb.class, new RenderXPOrb());
		this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed());
		this.entityRenderMap.put(EntityFallingSand.class, new RenderFallingSand());
		this.entityRenderMap.put(EntityMinecartTNT.class, new RenderTntMinecart());
		this.entityRenderMap.put(EntityMinecartMobSpawner.class, new RenderMinecartMobSpawner());
		this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart());
		this.entityRenderMap.put(EntityBoat.class, new RenderBoat());
		this.entityRenderMap.put(EntityFishHook.class, new RenderFish());
		this.entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt());
		// Spout Start
		this.entityRenderMap.put(EntityText.class, new RenderText());
		this.entityRenderMap.put(EntityTexture.class, new RenderTexture());
		// Spout End
		Iterator var1 = this.entityRenderMap.values().iterator();

		while (var1.hasNext()) {
			Render var2 = (Render)var1.next();
			var2.setRenderManager(this);
		}
	}

	public Render getEntityClassRenderObject(Class par1Class) {
		Render var2 = (Render)this.entityRenderMap.get(par1Class);

		if (var2 == null && par1Class != Entity.class) {
			var2 = this.getEntityClassRenderObject(par1Class.getSuperclass());
			this.entityRenderMap.put(par1Class, var2);
		}

		return var2;
	}

	public Render getEntityRenderObject(Entity par1Entity) {
		return this.getEntityClassRenderObject(par1Entity.getClass());
	}

	/**
	 * Caches the current frame's active render info, including the current World, RenderEngine, GameSettings and
	 * FontRenderer settings, as well as interpolated player position, pitch and yaw.
	 */
	public void cacheActiveRenderInfo(World par1World, RenderEngine par2RenderEngine, FontRenderer par3FontRenderer, EntityLiving par4EntityLiving, EntityLiving par5EntityLiving, GameSettings par6GameSettings, float par7) {
		this.worldObj = par1World;
		this.renderEngine = par2RenderEngine;
		this.options = par6GameSettings;
		this.livingPlayer = par4EntityLiving;
		this.field_96451_i = par5EntityLiving;
		this.fontRenderer = par3FontRenderer;

		if (par4EntityLiving.isPlayerSleeping()) {
			int var8 = par1World.getBlockId(MathHelper.floor_double(par4EntityLiving.posX), MathHelper.floor_double(par4EntityLiving.posY), MathHelper.floor_double(par4EntityLiving.posZ));

			if (var8 == Block.bed.blockID) {
				int var9 = par1World.getBlockMetadata(MathHelper.floor_double(par4EntityLiving.posX), MathHelper.floor_double(par4EntityLiving.posY), MathHelper.floor_double(par4EntityLiving.posZ));
				int var10 = var9 & 3;
				this.playerViewY = (float)(var10 * 90 + 180);
				this.playerViewX = 0.0F;
			}
		} else {
			this.playerViewY = par4EntityLiving.prevRotationYaw + (par4EntityLiving.rotationYaw - par4EntityLiving.prevRotationYaw) * par7;
			this.playerViewX = par4EntityLiving.prevRotationPitch + (par4EntityLiving.rotationPitch - par4EntityLiving.prevRotationPitch) * par7;
		}

		if (par6GameSettings.thirdPersonView == 2) {
			this.playerViewY += 180.0F;
		}

		this.viewerPosX = par4EntityLiving.lastTickPosX + (par4EntityLiving.posX - par4EntityLiving.lastTickPosX) * (double)par7;
		this.viewerPosY = par4EntityLiving.lastTickPosY + (par4EntityLiving.posY - par4EntityLiving.lastTickPosY) * (double)par7;
		this.viewerPosZ = par4EntityLiving.lastTickPosZ + (par4EntityLiving.posZ - par4EntityLiving.lastTickPosZ) * (double)par7;
	}

	/**
	 * Will render the specified entity at the specified partial tick time. Args: entity, partialTickTime
	 */
	public void renderEntity(Entity par1Entity, float par2) {
		if (par1Entity.ticksExisted == 0) {
			par1Entity.lastTickPosX = par1Entity.posX;
			par1Entity.lastTickPosY = par1Entity.posY;
			par1Entity.lastTickPosZ = par1Entity.posZ;
		}

		double var3 = par1Entity.lastTickPosX + (par1Entity.posX - par1Entity.lastTickPosX) * (double)par2;
		double var5 = par1Entity.lastTickPosY + (par1Entity.posY - par1Entity.lastTickPosY) * (double)par2;
		double var7 = par1Entity.lastTickPosZ + (par1Entity.posZ - par1Entity.lastTickPosZ) * (double)par2;
		float var9 = par1Entity.prevRotationYaw + (par1Entity.rotationYaw - par1Entity.prevRotationYaw) * par2;
		int var10 = par1Entity.getBrightnessForRender(par2);

		if (par1Entity.isBurning()) {
			var10 = 15728880;
		}

		int var11 = var10 % 65536;
		int var12 = var10 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var11 / 1.0F, (float)var12 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.renderEntityWithPosYaw(par1Entity, var3 - renderPosX, var5 - renderPosY, var7 - renderPosZ, var9, par2);
	}

	/**
	 * Renders the specified entity with the passed in position, yaw, and partialTickTime. Args: entity, x, y, z, yaw,
	 * partialTickTime
	 */
	public void renderEntityWithPosYaw(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		Render var10 = null;

		try {
			var10 = this.getEntityRenderObject(par1Entity);

			if (var10 != null && this.renderEngine != null) {
				if (field_85095_o && !par1Entity.isInvisible()) {
					try {
						this.func_85094_b(par1Entity, par2, par4, par6, par8, par9);
					} catch (Throwable var17) {
						throw new ReportedException(CrashReport.makeCrashReport(var17, "Rendering entity hitbox in world"));
					}
				}

				// Spout Start
				var10.setRenderManager(this);
				// Spout End

				try {
					var10.doRender(par1Entity, par2, par4, par6, par8, par9);
				// Spout Start - Ignore NullPointerExceptions, the old way
				} catch(NullPointerException ignore) {
				// Spout End
				} catch (Throwable var16) {
					throw new ReportedException(CrashReport.makeCrashReport(var16, "Rendering entity in world"));
				}

				try {
					var10.doRenderShadowAndFire(par1Entity, par2, par4, par6, par8, par9);
				// Spout Start - Ignore NullPointerExceptions, the old way
				} catch(NullPointerException ignore) {
				// Spout End
				} catch (Throwable var15) {
					throw new ReportedException(CrashReport.makeCrashReport(var15, "Post-rendering entity in world"));
				}
			}
		// Spout Start - Ignore NullPointerExceptions, the old way
		} catch(NullPointerException ignore) {
		// Spout End
		} catch (Throwable var18) {
			CrashReport var12 = CrashReport.makeCrashReport(var18, "Rendering entity in world");
			CrashReportCategory var13 = var12.makeCategory("Entity being rendered");
			par1Entity.func_85029_a(var13);
			CrashReportCategory var14 = var12.makeCategory("Renderer details");
			var14.addCrashSection("Assigned renderer", var10);
			var14.addCrashSection("Location", CrashReportCategory.func_85074_a(par2, par4, par6));
			var14.addCrashSection("Rotation", Float.valueOf(par8));
			var14.addCrashSection("Delta", Float.valueOf(par9));
			throw new ReportedException(var12);
		}
	}

	private void func_85094_b(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPushMatrix();
		Tessellator var10 = Tessellator.instance;
		var10.startDrawingQuads();
		var10.setColorRGBA(255, 255, 255, 32);
		double var11 = (double)(-par1Entity.width / 2.0F);
		double var13 = (double)(-par1Entity.width / 2.0F);
		double var15 = (double)(par1Entity.width / 2.0F);
		double var17 = (double)(-par1Entity.width / 2.0F);
		double var19 = (double)(-par1Entity.width / 2.0F);
		double var21 = (double)(par1Entity.width / 2.0F);
		double var23 = (double)(par1Entity.width / 2.0F);
		double var25 = (double)(par1Entity.width / 2.0F);
		double var27 = (double)par1Entity.height;
		var10.addVertex(par2 + var11, par4 + var27, par6 + var13);
		var10.addVertex(par2 + var11, par4, par6 + var13);
		var10.addVertex(par2 + var15, par4, par6 + var17);
		var10.addVertex(par2 + var15, par4 + var27, par6 + var17);
		var10.addVertex(par2 + var23, par4 + var27, par6 + var25);
		var10.addVertex(par2 + var23, par4, par6 + var25);
		var10.addVertex(par2 + var19, par4, par6 + var21);
		var10.addVertex(par2 + var19, par4 + var27, par6 + var21);
		var10.addVertex(par2 + var15, par4 + var27, par6 + var17);
		var10.addVertex(par2 + var15, par4, par6 + var17);
		var10.addVertex(par2 + var23, par4, par6 + var25);
		var10.addVertex(par2 + var23, par4 + var27, par6 + var25);
		var10.addVertex(par2 + var19, par4 + var27, par6 + var21);
		var10.addVertex(par2 + var19, par4, par6 + var21);
		var10.addVertex(par2 + var11, par4, par6 + var13);
		var10.addVertex(par2 + var11, par4 + var27, par6 + var13);
		var10.draw();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
	}

	/**
	 * World sets this RenderManager's worldObj to the world provided
	 */
	public void set(World par1World) {
		this.worldObj = par1World;
	}

	public double getDistanceToCamera(double par1, double par3, double par5) {
		double var7 = par1 - this.viewerPosX;
		double var9 = par3 - this.viewerPosY;
		double var11 = par5 - this.viewerPosZ;
		return var7 * var7 + var9 * var9 + var11 * var11;
	}

	/**
	 * Returns the font renderer
	 */
	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}

	public void updateIcons(IconRegister par1IconRegister) {
		Iterator var2 = this.entityRenderMap.values().iterator();

		while (var2.hasNext()) {
			Render var3 = (Render)var2.next();
			var3.updateIcons(par1IconRegister);
		}
	}
}
