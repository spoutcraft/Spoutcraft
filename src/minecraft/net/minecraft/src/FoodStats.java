package net.minecraft.src;

public class FoodStats {
	private int foodLevel;
	private float foodSaturationLevel;
	private float foodExhaustionLevel;
	private int foodTickTimer;
	private int prevFoodLevel;

	public FoodStats() {
		foodTickTimer = 0;
		foodLevel = 20;
		prevFoodLevel = 20;
		foodSaturationLevel = 5F;
	}

	public void addStats(int i, float f) {
		foodLevel = Math.min(i + foodLevel, 20);
		foodSaturationLevel = Math.min(foodSaturationLevel + (float)i * f * 2.0F, foodLevel);
	}

	public void addStatsFrom(ItemFood itemfood) {
		addStats(itemfood.getHealAmount(), itemfood.getSaturationModifier());
	}

	public void onUpdate(EntityPlayer entityplayer) {
		int i = entityplayer.worldObj.difficultySetting;
		prevFoodLevel = foodLevel;
		if (foodExhaustionLevel > 4F) {
			foodExhaustionLevel -= 4F;
			if (foodSaturationLevel > 0.0F) {
				foodSaturationLevel = Math.max(foodSaturationLevel - 1.0F, 0.0F);
			}
			else if (i > 0) {
				foodLevel = Math.max(foodLevel - 1, 0);
			}
		}
		if (foodLevel >= 18 && entityplayer.shouldHeal()) {
			foodTickTimer++;
			if (foodTickTimer >= 80) {
				entityplayer.heal(1);
				foodTickTimer = 0;
			}
		}
		else if (foodLevel <= 0) {
			foodTickTimer++;
			if (foodTickTimer >= 80) {
				if (entityplayer.getEntityHealth() > 10 || i >= 3 || entityplayer.getEntityHealth() > 1 && i >= 2) {
					entityplayer.attackEntityFrom(DamageSource.starve, 1);
				}
				foodTickTimer = 0;
			}
		}
		else {
			foodTickTimer = 0;
		}
	}

	public void readStatsFromNBT(NBTTagCompound nbttagcompound) {
		if (nbttagcompound.hasKey("foodLevel")) {
			foodLevel = nbttagcompound.getInteger("foodLevel");
			foodTickTimer = nbttagcompound.getInteger("foodTickTimer");
			foodSaturationLevel = nbttagcompound.getFloat("foodSaturationLevel");
			foodExhaustionLevel = nbttagcompound.getFloat("foodExhaustionLevel");
		}
	}

	public void writeStatsToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("foodLevel", foodLevel);
		nbttagcompound.setInteger("foodTickTimer", foodTickTimer);
		nbttagcompound.setFloat("foodSaturationLevel", foodSaturationLevel);
		nbttagcompound.setFloat("foodExhaustionLevel", foodExhaustionLevel);
	}

	public int getFoodLevel() {
		return foodLevel;
	}

	public int getPrevFoodLevel() {
		return prevFoodLevel;
	}

	public boolean needFood() {
		return foodLevel < 20;
	}

	public void addExhaustion(float f) {
		foodExhaustionLevel = Math.min(foodExhaustionLevel + f, 40F);
	}

	public float getFoodSaturationLevel() {
		return foodSaturationLevel;
	}

	public void setFoodLevel(int i) {
		foodLevel = i;
	}

	public void setFoodSaturationLevel(float f) {
		foodSaturationLevel = f;
	}
}
