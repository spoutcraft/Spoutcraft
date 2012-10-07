/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.gui.minimap;

import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

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

import org.spoutcraft.client.io.CustomTextureManager;

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
