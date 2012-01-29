package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class EntityPotion extends EntityThrowable {
	private int potionDamage;

	public EntityPotion(World world) {
		super(world);
	}

	public EntityPotion(World world, EntityLiving entityliving, int i) {
		super(world, entityliving);
		potionDamage = i;
	}

	public EntityPotion(World world, double d, double d1, double d2,
	        int i) {
		super(world, d, d1, d2);
		potionDamage = i;
	}

	protected float func_40075_e() {
		return 0.05F;
	}

	protected float func_40077_c() {
		return 0.5F;
	}

	protected float func_40074_d() {
		return -20F;
	}

	public int getPotionDamage() {
		return potionDamage;
	}

	protected void onThrowableCollision(MovingObjectPosition movingobjectposition) {
		if (!worldObj.multiplayerWorld) {
			List list = Item.potion.getEffectNamesFromDamage(potionDamage);
			if (list != null && !list.isEmpty()) {
				AxisAlignedBB axisalignedbb = boundingBox.expand(4D, 2D, 4D);
				List list1 = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityLiving.class, axisalignedbb);
				if (list1 != null && !list1.isEmpty()) {
					for (Iterator iterator = list1.iterator(); iterator.hasNext();) {
						Entity entity = (Entity)iterator.next();
						double d = getDistanceSqToEntity(entity);
						if (d < 16D) {
							double d1 = 1.0D - Math.sqrt(d) / 4D;
							if (entity == movingobjectposition.entityHit) {
								d1 = 1.0D;
							}
							Iterator iterator1 = list.iterator();
							while (iterator1.hasNext()) {
								PotionEffect potioneffect = (PotionEffect)iterator1.next();
								int i = potioneffect.getPotionID();
								if (Potion.potionTypes[i].isInstant()) {
									Potion.potionTypes[i].affectEntity(throwingEntity, (EntityLiving)entity, potioneffect.getAmplifier(), d1);
								}
								else {
									int j = (int)(d1 * (double)potioneffect.getDuration() + 0.5D);
									if (j > 20) {
										((EntityLiving)entity).addPotionEffect(new PotionEffect(i, j, potioneffect.getAmplifier()));
									}
								}
							}
						}
					}
				}
			}
			worldObj.playAuxSFX(2002, (int)Math.round(posX), (int)Math.round(posY), (int)Math.round(posZ), potionDamage);
			setEntityDead();
		}
	}
}
