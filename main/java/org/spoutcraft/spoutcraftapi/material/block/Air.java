package org.spoutcraft.spoutcraftapi.material.block;

import org.spoutcraft.spoutcraftapi.material.Block;
import org.spoutcraft.spoutcraftapi.sound.SoundEffect;

public class Air extends GenericBlock implements Block {

	public Air() {
		super(0);
	}
	
	public SoundEffect getStepSound() {
		return null;
	}

	public Block setStepSound(SoundEffect sound) {
		return null;
	}

	@Override
	public float getFriction() {
		return 0;
	}

	@Override
	public Block setFriction(float slip) {
		return this;
	}

	@Override
	public float getHardness() {
		return 0;
	}

	@Override
	public Block setHardness(float hardness) {
		return this;
	}

	@Override
	public float getExplosionResistence() {
		return 0;
	}

	@Override
	public Block setExplosionResistence(float resistence) {
		return this;
	}

	@Override
	public boolean isOpaque() {
		return false;
	}

	@Override
	public Block setOpaque(boolean opaque) {
		return this;
	}

	@Override
	public int getLightLevel() {
		return 0;
	}

	@Override
	public Block setLightLevel(int level) {
		return this;
	}

}
