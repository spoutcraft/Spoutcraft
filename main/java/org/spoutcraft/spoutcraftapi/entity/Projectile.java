package org.spoutcraft.spoutcraftapi.entity;

import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;

public interface Projectile extends Entity {

	public LivingEntity getShooter();

	public void setShooter(LivingEntity shooter);

	public boolean doesBounce();

	public void setBounce(boolean doesBounce);
}
