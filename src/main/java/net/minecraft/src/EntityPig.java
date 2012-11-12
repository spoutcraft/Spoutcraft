package net.minecraft.src;

import org.spoutcraft.client.entity.CraftPig; // Spout

public class EntityPig extends EntityAnimal {

	/** AI task for player control. */
	private final EntityAIControlledByPlayer aiControlledByPlayer;

	public EntityPig(World par1World) {
		super(par1World);
		this.texture = "/mob/pig.png";
		this.setSize(0.9F, 0.9F);
		this.getNavigator().setAvoidsWater(true);
		float var2 = 0.25F;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
		this.tasks.addTask(2, this.aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.34F));
		this.tasks.addTask(3, new EntityAIMate(this, var2));
		this.tasks.addTask(4, new EntityAITempt(this, 0.3F, Item.carrotOnAStick.shiftedIndex, false));
		this.tasks.addTask(4, new EntityAITempt(this, 0.3F, Item.carrot.shiftedIndex, false));
		this.tasks.addTask(5, new EntityAIFollowParent(this, 0.28F));
		this.tasks.addTask(6, new EntityAIWander(this, var2));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		// Spout Start
		this.spoutEntity = new CraftPig(this);
		// Spout End
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	public int getMaxHealth() {
		return 10;
	}

	protected void updateAITasks() {
		super.updateAITasks();
	}

	/**
	 * returns true if all the conditions for steering the entity are met. For pigs, this is true if it is being ridden by
	 * a player and the player is holding a carrot-on-a-stick
	 */
	public boolean canBeSteered() {
		ItemStack var1 = ((EntityPlayer)this.riddenByEntity).getHeldItem();
		return var1 != null && var1.itemID == Item.carrotOnAStick.shiftedIndex;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("Saddle", this.getSaddled());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setSaddled(par1NBTTagCompound.getBoolean("Saddle"));
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.pig.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.pig.say";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.pig.death";
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	protected void playStepSound(int par1, int par2, int par3, int par4) {
		this.func_85030_a("mob.pig.step", 0.15F, 1.0F);
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer par1EntityPlayer) {
		if (super.interact(par1EntityPlayer)) {
			return true;
		} else if (this.getSaddled() && !this.worldObj.isRemote && (this.riddenByEntity == null || this.riddenByEntity == par1EntityPlayer)) {
			par1EntityPlayer.mountEntity(this);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return this.isBurning() ? Item.porkCooked.shiftedIndex : Item.porkRaw.shiftedIndex;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean par1, int par2) {
		int var3 = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + par2);

		for (int var4 = 0; var4 < var3; ++var4) {
			if (this.isBurning()) {
				this.dropItem(Item.porkCooked.shiftedIndex, 1);
			} else {
				this.dropItem(Item.porkRaw.shiftedIndex, 1);
			}
		}

		if (this.getSaddled()) {
			this.dropItem(Item.saddle.shiftedIndex, 1);
		}
	}

	/**
	 * Returns true if the pig is saddled.
	 */
	public boolean getSaddled() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	/**
	 * Set or remove the saddle of the pig.
	 */
	public void setSaddled(boolean par1) {
		if (par1) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
		}
	}

	/**
	 * Called when a lightning bolt hits the entity.
	 */
	public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt) {
		if (!this.worldObj.isRemote) {
			EntityPigZombie var2 = new EntityPigZombie(this.worldObj);
			var2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.worldObj.spawnEntityInWorld(var2);
			this.setDead();
		}
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float par1) {
		super.fall(par1);

		if (par1 > 5.0F && this.riddenByEntity instanceof EntityPlayer) {
			((EntityPlayer)this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
		}
	}

	/**
	 * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
	 */
	public EntityPig spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
		return new EntityPig(this.worldObj);
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
	 * the animal type)
	 */
	public boolean isBreedingItem(ItemStack par1ItemStack) {
		return par1ItemStack != null && par1ItemStack.itemID == Item.carrot.shiftedIndex;
	}

	/**
	 * Return the AI task for player control.
	 */
	public EntityAIControlledByPlayer getAIControlledByPlayer() {
		return this.aiControlledByPlayer;
	}

	public EntityAgeable func_90011_a(EntityAgeable par1EntityAgeable) {
		return this.spawnBabyAnimal(par1EntityAgeable);
	}
}
