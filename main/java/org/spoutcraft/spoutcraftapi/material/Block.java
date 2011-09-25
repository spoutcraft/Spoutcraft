package org.spoutcraft.spoutcraftapi.material;

import org.spoutcraft.spoutcraftapi.sound.SoundEffect;

public interface Block extends Material{
	
	public SoundEffect getStepSound();
	
	public Block setStepSound(SoundEffect sound);
	
	public float getFriction();
	
	public Block setFriction(float slip);
	
	public float getHardness();
	
	public Block setHardness(float hardness);
	
	public float getExplosionResistence();
	
	public Block setExplosionResistence(float resistence);
	
	public boolean isOpaque();
	
	public Block setOpaque(boolean opaque);
	
	public int getLightLevel();
	
	public Block setLightLevel(int level);

}
