package net.minecraft.src;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityBlaze;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityCaveSpider;
import net.minecraft.src.EntityChicken;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityCreeper;
import net.minecraft.src.EntityDragon;
import net.minecraft.src.EntityEgg;
import net.minecraft.src.EntityEnderCrystal;
import net.minecraft.src.EntityEnderEye;
import net.minecraft.src.EntityEnderPearl;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.EntityExpBottle;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityFireball;
import net.minecraft.src.EntityFishHook;
import net.minecraft.src.EntityGhast;
import net.minecraft.src.EntityGiantZombie;
import net.minecraft.src.EntityIronGolem;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLightningBolt;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMagmaCube;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityMooshroom;
import net.minecraft.src.EntityOcelot;
import net.minecraft.src.EntityPainting;
import net.minecraft.src.EntityPig;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPotion;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.EntitySmallFireball;
import net.minecraft.src.EntitySnowball;
import net.minecraft.src.EntitySnowman;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.EntitySquid;
import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GameSettings;
import net.minecraft.src.Item;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.ModelChicken;
import net.minecraft.src.ModelCow;
import net.minecraft.src.ModelOcelot;
import net.minecraft.src.ModelPig;
import net.minecraft.src.ModelSheep1;
import net.minecraft.src.ModelSheep2;
import net.minecraft.src.ModelSkeleton;
import net.minecraft.src.ModelSlime;
import net.minecraft.src.ModelSquid;
import net.minecraft.src.ModelWolf;
import net.minecraft.src.ModelZombie;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.Render;
import net.minecraft.src.RenderArrow;
import net.minecraft.src.RenderBiped;
import net.minecraft.src.RenderBlaze;
import net.minecraft.src.RenderBoat;
import net.minecraft.src.RenderChicken;
import net.minecraft.src.RenderCow;
import net.minecraft.src.RenderCreeper;
import net.minecraft.src.RenderDragon;
import net.minecraft.src.RenderEnderCrystal;
import net.minecraft.src.RenderEnderman;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderEntity;
import net.minecraft.src.RenderFallingSand;
import net.minecraft.src.RenderFireball;
import net.minecraft.src.RenderFish;
import net.minecraft.src.RenderGhast;
import net.minecraft.src.RenderGiantZombie;
import net.minecraft.src.RenderIronGolem;
import net.minecraft.src.RenderItem;
import net.minecraft.src.RenderLightningBolt;
import net.minecraft.src.RenderLiving;
import net.minecraft.src.RenderMagmaCube;
import net.minecraft.src.RenderMinecart;
import net.minecraft.src.RenderMooshroom;
import net.minecraft.src.RenderOcelot;
import net.minecraft.src.RenderPainting;
import net.minecraft.src.RenderPig;
import net.minecraft.src.RenderPlayer;
import net.minecraft.src.RenderSheep;
import net.minecraft.src.RenderSilverfish;
import net.minecraft.src.RenderSlime;
import net.minecraft.src.RenderSnowMan;
import net.minecraft.src.RenderSnowball;
import net.minecraft.src.RenderSpider;
import net.minecraft.src.RenderSquid;
import net.minecraft.src.RenderTNTPrimed;
import net.minecraft.src.RenderVillager;
import net.minecraft.src.RenderWolf;
import net.minecraft.src.RenderXPOrb;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;
//Spout Start
import org.lwjgl.opengl.GL13;
import org.spoutcraft.client.entity.EntityText;
import org.spoutcraft.client.entity.EntityTexture;
import org.spoutcraft.client.entity.RenderText;
import org.spoutcraft.client.entity.RenderTexture;

//Spout end

public class RenderManager {

	private Map entityRenderMap = new HashMap();
	public static RenderManager instance = new RenderManager();
	private FontRenderer fontRenderer;
	public static double renderPosX;
	public static double renderPosY;
	public static double renderPosZ;
	public RenderEngine renderEngine;
	public ItemRenderer itemRenderer;
	public World worldObj;
	public EntityLiving livingPlayer;
	public float playerViewY;
	public float playerViewX;
	public GameSettings options;
	public double field_1222_l;
	public double field_1221_m;
	public double field_1220_n;

	private RenderManager() {
		this.entityRenderMap.put(EntitySpider.class, new RenderSpider());
		this.entityRenderMap.put(EntityCaveSpider.class, new RenderSpider());
		this.entityRenderMap.put(EntityPig.class, new RenderPig(new ModelPig(), new ModelPig(0.5F), 0.7F));
		this.entityRenderMap.put(EntitySheep.class, new RenderSheep(new ModelSheep2(), new ModelSheep1(), 0.7F));
		this.entityRenderMap.put(EntityCow.class, new RenderCow(new ModelCow(), 0.7F));
		this.entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(new ModelCow(), 0.7F));
		this.entityRenderMap.put(EntityWolf.class, new RenderWolf(new ModelWolf(), 0.5F));
		this.entityRenderMap.put(EntityChicken.class, new RenderChicken(new ModelChicken(), 0.3F));
		this.entityRenderMap.put(EntityOcelot.class, new RenderOcelot(new ModelOcelot(), 0.4F));
		this.entityRenderMap.put(EntitySilverfish.class, new RenderSilverfish());
		this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper());
		this.entityRenderMap.put(EntityEnderman.class, new RenderEnderman());
		this.entityRenderMap.put(EntitySnowman.class, new RenderSnowMan());
		this.entityRenderMap.put(EntitySkeleton.class, new RenderBiped(new ModelSkeleton(), 0.5F));
		this.entityRenderMap.put(EntityBlaze.class, new RenderBlaze());
		this.entityRenderMap.put(EntityZombie.class, new RenderBiped(new ModelZombie(), 0.5F));
		this.entityRenderMap.put(EntitySlime.class, new RenderSlime(new ModelSlime(16), new ModelSlime(0), 0.25F));
		this.entityRenderMap.put(EntityMagmaCube.class, new RenderMagmaCube());
		this.entityRenderMap.put(EntityPlayer.class, new RenderPlayer());
		this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(new ModelZombie(), 0.5F, 6.0F));
		this.entityRenderMap.put(EntityGhast.class, new RenderGhast());
		this.entityRenderMap.put(EntitySquid.class, new RenderSquid(new ModelSquid(), 0.7F));
		this.entityRenderMap.put(EntityVillager.class, new RenderVillager());
		this.entityRenderMap.put(EntityIronGolem.class, new RenderIronGolem());
		this.entityRenderMap.put(EntityLiving.class, new RenderLiving(new ModelBiped(), 0.5F));
		this.entityRenderMap.put(EntityDragon.class, new RenderDragon());
		this.entityRenderMap.put(EntityEnderCrystal.class, new RenderEnderCrystal());
		this.entityRenderMap.put(Entity.class, new RenderEntity());
		this.entityRenderMap.put(EntityPainting.class, new RenderPainting());
		this.entityRenderMap.put(EntityArrow.class, new RenderArrow());
		this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball(Item.snowball.getIconFromDamage(0)));
		this.entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball(Item.enderPearl.getIconFromDamage(0)));
		this.entityRenderMap.put(EntityEnderEye.class, new RenderSnowball(Item.eyeOfEnder.getIconFromDamage(0)));
		this.entityRenderMap.put(EntityEgg.class, new RenderSnowball(Item.egg.getIconFromDamage(0)));
		this.entityRenderMap.put(EntityPotion.class, new RenderSnowball(154));
		this.entityRenderMap.put(EntityExpBottle.class, new RenderSnowball(Item.field_48438_bD.getIconFromDamage(0)));
		this.entityRenderMap.put(EntityFireball.class, new RenderFireball(2.0F));
		this.entityRenderMap.put(EntitySmallFireball.class, new RenderFireball(0.5F));
		this.entityRenderMap.put(EntityItem.class, new RenderItem());
		this.entityRenderMap.put(EntityXPOrb.class, new RenderXPOrb());
		this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed());
		this.entityRenderMap.put(EntityFallingSand.class, new RenderFallingSand());
		this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart());
		this.entityRenderMap.put(EntityBoat.class, new RenderBoat());
		this.entityRenderMap.put(EntityFishHook.class, new RenderFish());
		this.entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt());
		//Spout Start
		this.entityRenderMap.put(EntityText.class, new RenderText());
		this.entityRenderMap.put(EntityTexture.class, new RenderTexture());
		//Spout End
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

	public void cacheActiveRenderInfo(World par1World, RenderEngine par2RenderEngine, FontRenderer par3FontRenderer, EntityLiving par4EntityLiving, GameSettings par5GameSettings, float par6) {
		this.worldObj = par1World;
		this.renderEngine = par2RenderEngine;
		this.options = par5GameSettings;
		this.livingPlayer = par4EntityLiving;
		this.fontRenderer = par3FontRenderer;
		if (par4EntityLiving.isPlayerSleeping()) {
			int var7 = par1World.getBlockId(MathHelper.floor_double(par4EntityLiving.posX), MathHelper.floor_double(par4EntityLiving.posY), MathHelper.floor_double(par4EntityLiving.posZ));
			if (var7 == Block.bed.blockID) {
				int var8 = par1World.getBlockMetadata(MathHelper.floor_double(par4EntityLiving.posX), MathHelper.floor_double(par4EntityLiving.posY), MathHelper.floor_double(par4EntityLiving.posZ));
				int var9 = var8 & 3;
				this.playerViewY = (float)(var9 * 90 + 180);
				this.playerViewX = 0.0F;
			}
		} else {
			this.playerViewY = par4EntityLiving.prevRotationYaw + (par4EntityLiving.rotationYaw - par4EntityLiving.prevRotationYaw) * par6;
			this.playerViewX = par4EntityLiving.prevRotationPitch + (par4EntityLiving.rotationPitch - par4EntityLiving.prevRotationPitch) * par6;
		}

		if (par5GameSettings.thirdPersonView == 2) {
			this.playerViewY += 180.0F;
		}

		this.field_1222_l = par4EntityLiving.lastTickPosX + (par4EntityLiving.posX - par4EntityLiving.lastTickPosX) * (double)par6;
		this.field_1221_m = par4EntityLiving.lastTickPosY + (par4EntityLiving.posY - par4EntityLiving.lastTickPosY) * (double)par6;
		this.field_1220_n = par4EntityLiving.lastTickPosZ + (par4EntityLiving.posZ - par4EntityLiving.lastTickPosZ) * (double)par6;
	}

	public void renderEntity(Entity par1Entity, float par2) {
		double var3 = par1Entity.lastTickPosX + (par1Entity.posX - par1Entity.lastTickPosX) * (double)par2;
		double var5 = par1Entity.lastTickPosY + (par1Entity.posY - par1Entity.lastTickPosY) * (double)par2;
		double var7 = par1Entity.lastTickPosZ + (par1Entity.posZ - par1Entity.lastTickPosZ) * (double)par2;
		float var9 = par1Entity.prevRotationYaw + (par1Entity.rotationYaw - par1Entity.prevRotationYaw) * par2;
		int var10 = par1Entity.getEntityBrightnessForRender(par2);
		if (par1Entity.isBurning()) {
			var10 = 15728880;
		}

		int var11 = var10 % 65536;
		int var12 = var10 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapEnabled, (float)var11 / 1.0F, (float)var12 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.renderEntityWithPosYaw(par1Entity, var3 - renderPosX, var5 - renderPosY, var7 - renderPosZ, var9, par2);
	}

	public void renderEntityWithPosYaw(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		Render var10 = this.getEntityRenderObject(par1Entity);
		if (var10 != null) {
			var10.doRender(par1Entity, par2, par4, par6, par8, par9);
			var10.doRenderShadowAndFire(par1Entity, par2, par4, par6, par8, par9);
		}

	}

	public void set(World par1World) {
		this.worldObj = par1World;
	}

	public double getDistanceToCamera(double par1, double par3, double par5) {
		double var7 = par1 - this.field_1222_l;
		double var9 = par3 - this.field_1221_m;
		double var11 = par5 - this.field_1220_n;
		return var7 * var7 + var9 * var9 + var11 * var11;
	}

	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}

}
