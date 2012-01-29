package net.minecraft.src;

public class PlayerCapabilities {
	public boolean disableDamage;
	public boolean isFlying;
	public boolean allowFlying;
	public boolean depleteBuckets;

	public PlayerCapabilities() {
		disableDamage = false;
		isFlying = false;
		allowFlying = false;
		depleteBuckets = false;
	}

	public void writeCapabilitiesToNBT(NBTTagCompound nbttagcompound) {
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		nbttagcompound1.setBoolean("invulnerable", disableDamage);
		nbttagcompound1.setBoolean("flying", disableDamage);
		nbttagcompound1.setBoolean("mayfly", allowFlying);
		nbttagcompound1.setBoolean("instabuild", depleteBuckets);
		nbttagcompound.setTag("abilities", nbttagcompound1);
	}

	public void readCapabilitiesFromNBT(NBTTagCompound nbttagcompound) {
		if (nbttagcompound.hasKey("abilities")) {
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("abilities");
			disableDamage = nbttagcompound1.getBoolean("invulnerable");
			isFlying = nbttagcompound1.getBoolean("flying");
			allowFlying = nbttagcompound1.getBoolean("mayfly");
			depleteBuckets = nbttagcompound1.getBoolean("instabuild");
		}
	}
}
