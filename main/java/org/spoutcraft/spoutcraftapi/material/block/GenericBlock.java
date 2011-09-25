package org.spoutcraft.spoutcraftapi.material.block;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.material.Block;
import org.spoutcraft.spoutcraftapi.sound.SoundEffect;

public class GenericBlock implements Block{
	private final int id;
	private final int data;
	private final boolean subtypes;
	private SoundEffect stepSound = SoundEffect.STONE;
	
	private GenericBlock(int id, int data, boolean subtypes) {
		this.id = id;
		this.data = data;
		this.subtypes = subtypes;
	}
	
	protected GenericBlock(int id, int data) {
		this(id, data, true);
	}
	
	protected GenericBlock(int id) {
		this(id, 0, false);
	}

	public int getRawId() {
		return id;
	}

	public int getRawData() {
		return data;
	}

	public boolean hasSubtypes() {
		return subtypes;
	}

	public String getName() {
		return Spoutcraft.getClient().getItemManager().getItemName(id, (short)data);
	}

	public void setName(String name) {
		Spoutcraft.getClient().getItemManager().setItemName(id, (short)data, name);
	}

	public SoundEffect getStepSound() {
		return stepSound;
	}

	public Block setStepSound(SoundEffect sound) {
		stepSound = sound;
		return this;
	}

	public float getFriction() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Block setFriction(float friction) {
		// TODO Auto-generated method stub
		return null;
	}

	public float getHardness() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Block setHardness(float hardness) {
		// TODO Auto-generated method stub
		return null;
	}

	public float getExplosionResistence() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Block setExplosionResistence(float resistence) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isOpaque() {
		// TODO Auto-generated method stub
		return false;
	}

	public Block setOpaque(boolean opaque) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getLightLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Block setLightLevel(int level) {
		// TODO Auto-generated method stub
		return null;
	}
}
