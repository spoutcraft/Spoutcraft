package org.spoutcraft.spoutcraftapi.material.block;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.material.Block;
import org.spoutcraft.spoutcraftapi.sound.SoundEffect;

public class GenericBlock implements Block{
	private final int id;
	private final int data;
	private final boolean subtypes;
	private final String name;
	private String customName;
	private SoundEffect stepSound = SoundEffect.STONE;
	
	private GenericBlock(String name, int id, int data, boolean subtypes) {
		this.name = name;
		this.id = id;
		this.data = data;
		this.subtypes = subtypes;
	}
	
	protected GenericBlock(String name, int id, int data) {
		this(name, id, data, true);
	}
	
	protected GenericBlock(String name, int id) {
		this(name, id, 0, false);
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
		if(customName != null) {
			return customName;
		}
		return name;
	}

	public void setName(String name) {
		this.customName = name;
		Spoutcraft.getClient().getMaterialManager().setItemName(this, name);
	}

	public SoundEffect getStepSound() {
		return stepSound;
	}

	public Block setStepSound(SoundEffect sound) {
		stepSound = sound;
		return this;
	}

	public float getFriction() {
		return Spoutcraft.getClient().getMaterialManager().getFriction(this);
	}

	public Block setFriction(float friction) {
		Spoutcraft.getClient().getMaterialManager().setFriction(this, friction);
		return this;
	}

	public float getHardness() {
		return Spoutcraft.getClient().getMaterialManager().getHardness(this);
	}

	public Block setHardness(float hardness) {
		Spoutcraft.getClient().getMaterialManager().setHardness(this, hardness);
		return this;
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
		return Spoutcraft.getClient().getMaterialManager().isOpaque(this);
	}

	public Block setOpaque(boolean opaque) {
		Spoutcraft.getClient().getMaterialManager().setOpaque(this, opaque);
		return this;
	}

	public int getLightLevel() {
		return Spoutcraft.getClient().getMaterialManager().getLightLevel(this);
	}

	public Block setLightLevel(int level) {
		Spoutcraft.getClient().getMaterialManager().setLightLevel(this, level);
		return this;
	}
}
