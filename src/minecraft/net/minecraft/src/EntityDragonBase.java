package net.minecraft.src;

public class EntityDragonBase extends EntityLiving {
	protected int maxHealth;

	public EntityDragonBase(World world) {
		super(world);
		maxHealth = 100;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public boolean attackEntityFromPart(DragonPart dragonpart, DamageSource damagesource, int i) {
		return attackEntityFrom(damagesource, i);
	}

	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		return false;
	}

	protected boolean superAttackFrom(DamageSource damagesource, int i) {
		return super.attackEntityFrom(damagesource, i);
	}
}
