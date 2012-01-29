package net.minecraft.src;

public class EntityDamageSourceIndirect extends EntityDamageSource {
	private Entity damageSourceProjectile;

	public EntityDamageSourceIndirect(String s, Entity entity, Entity entity1) {
		super(s, entity);
		damageSourceProjectile = entity1;
	}

	public Entity getSourceOfDamage() {
		return damageSourceEntity;
	}

	public Entity getEntity() {
		return damageSourceProjectile;
	}
}
