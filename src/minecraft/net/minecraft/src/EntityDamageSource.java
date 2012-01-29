package net.minecraft.src;

public class EntityDamageSource extends DamageSource {
	protected Entity damageSourceEntity;

	public EntityDamageSource(String s, Entity entity) {
		super(s);
		damageSourceEntity = entity;
	}

	public Entity getEntity() {
		return damageSourceEntity;
	}
}
