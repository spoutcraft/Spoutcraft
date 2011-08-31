package org.spoutcraft.spoutcraftapi.entity;

public interface Projectile extends Entity {

	public LivingEntity getShooter();

	public void setShooter(LivingEntity shooter);

	public boolean doesBounce();

	public void setBounce(boolean doesBounce);
}
