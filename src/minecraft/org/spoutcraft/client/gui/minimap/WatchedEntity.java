package org.spoutcraft.client.gui.minimap;

import java.util.HashMap;
import org.newdawn.slick.opengl.Texture;
import org.spoutcraft.client.io.CustomTextureManager;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityBlaze;
import net.minecraft.src.EntityCaveSpider;
import net.minecraft.src.EntityChicken;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityCreeper;
import net.minecraft.src.EntityDragon;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.EntityGhast;
import net.minecraft.src.EntityIronGolem;
import net.minecraft.src.EntityMagmaCube;
import net.minecraft.src.EntityMooshroom;
import net.minecraft.src.EntityOcelot;
import net.minecraft.src.EntityPig;
import net.minecraft.src.EntityPigZombie;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.EntitySnowman;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.EntitySquid;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.EntityZombie;

public class WatchedEntity {
	public static HashMap<Class<? extends Entity>, String> mobFaceTextures = new HashMap<Class<? extends Entity>, String>();
	public static HashMap<Class<? extends Entity>, Texture> mobFaceTextureBindings = new HashMap<Class<? extends Entity>, Texture>();

	static {
		mobFaceTextures.put(EntityBlaze.class, "blaze.png");
		mobFaceTextures.put(EntityCaveSpider.class, "cave_spider.png");
		mobFaceTextures.put(EntityChicken.class, "chicken.png");
		mobFaceTextures.put(EntityCow.class, "cow.png");
		mobFaceTextures.put(EntityCreeper.class, "creeper.png");
		mobFaceTextures.put(EntityDragon.class, "ender_dragon.png");
		mobFaceTextures.put(EntityEnderman.class, "enderman.png");
		mobFaceTextures.put(EntityGhast.class, "ghast.png");
		mobFaceTextures.put(EntityPlayer.class, "human.png");
		mobFaceTextures.put(EntityIronGolem.class, "iron_golem.png");
		mobFaceTextures.put(EntityMagmaCube.class, "magma_cube.png");
		mobFaceTextures.put(EntityMooshroom.class, "mooshroom.png");
		mobFaceTextures.put(EntityOcelot.class, "ocelot.png");
		mobFaceTextures.put(EntityPig.class, "pig.png");
		mobFaceTextures.put(EntitySheep.class, "sheep.png");
		mobFaceTextures.put(EntitySilverfish.class, "silverfish.png");
		mobFaceTextures.put(EntitySkeleton.class, "skeleton.png");
		mobFaceTextures.put(EntitySlime.class, "slime.png");
		mobFaceTextures.put(EntitySnowman.class, "snow_golem.png");
		mobFaceTextures.put(EntitySpider.class, "spider.png");
		mobFaceTextures.put(EntitySquid.class, "squid.png");
		mobFaceTextures.put(EntityVillager.class, "villager.png");
		mobFaceTextures.put(EntityWolf.class, "wolf.png");
		mobFaceTextures.put(EntityZombie.class, "zombie.png");
		mobFaceTextures.put(EntityPigZombie.class, "zombie_pigman.png");

	}

	public Entity entity;
	private final String path;
	public WatchedEntity(Entity entity) {
		this.entity = entity;
		path = "/res/mobface/" + mobFaceTextures.get(entity.getClass());
	}

	public Texture getTexture() {
		return CustomTextureManager.getTextureFromJar(path);
	}
}
