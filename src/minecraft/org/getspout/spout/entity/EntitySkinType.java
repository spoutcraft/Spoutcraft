package org.getspout.spout.entity;

import net.minecraft.src.EntityLiving;

public enum EntitySkinType {
	DEFAULT(0),
	SPIDER_EYES(1),
	SHEEP_FUR(2),
	WOLF_ANGRY(3),
	WOLF_TAMED(4),
	PIG_SADDLE(5),
	GHAST_MOUTH(6),
	;
	
	private final byte id;
	private EntitySkinType(int id){
		this.id = (byte)id;
	}
	
	public byte getId(){
		return id;
	}
	
	public static String getTexture(EntitySkinType type, EntityLiving entity, String std){
		String texture = entity.getCustomTexture(type.getId());
		if(texture == null)
		{
			texture = std;
		}
		return texture;
	}
}
