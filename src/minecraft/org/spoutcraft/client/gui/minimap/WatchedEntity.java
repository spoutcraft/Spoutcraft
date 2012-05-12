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
		mobFaceTextures.put(EntityBlaze.class, "Blaze.png");
		mobFaceTextures.put(EntityCaveSpider.class, "Cave_Spider.png");
		mobFaceTextures.put(EntityChicken.class, "Chicken.png");
		mobFaceTextures.put(EntityCow.class, "Cow.png");
		mobFaceTextures.put(EntityCreeper.class, "Creeper.png");
		mobFaceTextures.put(EntityDragon.class, "Ender_Dragon.png");
		mobFaceTextures.put(EntityEnderman.class, "Enderman.png");
		mobFaceTextures.put(EntityGhast.class, "Ghast.png");
		mobFaceTextures.put(EntityPlayer.class, "Human.png");
		mobFaceTextures.put(EntityIronGolem.class, "Iron_Golem.png");
		mobFaceTextures.put(EntityMagmaCube.class, "Magma_Cube.png");
		mobFaceTextures.put(EntityMooshroom.class, "Mooshroom.png");
		mobFaceTextures.put(EntityOcelot.class, "Ocelot.png");
		mobFaceTextures.put(EntityPig.class, "Pig.png");
		mobFaceTextures.put(EntitySheep.class, "Sheep.png");
		mobFaceTextures.put(EntitySilverfish.class, "Silverfish.png");
		mobFaceTextures.put(EntitySkeleton.class, "Skeleton.png");
		mobFaceTextures.put(EntitySlime.class, "Slime.png");
		mobFaceTextures.put(EntitySnowman.class, "Snow_Golem.png");
		mobFaceTextures.put(EntitySpider.class, "Spider.png");
		mobFaceTextures.put(EntitySquid.class, "Squid.png");
		mobFaceTextures.put(EntityVillager.class, "Villager.png");
		mobFaceTextures.put(EntityWolf.class, "Wolf.png");
		mobFaceTextures.put(EntityZombie.class, "Zombie.png");
		mobFaceTextures.put(EntityPigZombie.class, "Zombie_Pigman.png");
		
	}

	public Entity entity;
	private final String path;
	public WatchedEntity(Entity entity) {
		this.entity = entity;
		path = "/res/minimap/mobfaces/" + mobFaceTextures.get(entity.getClass());
	}
	
	public Texture getTexture() {
		return CustomTextureManager.getTextureFromJar(path);
	}
}
