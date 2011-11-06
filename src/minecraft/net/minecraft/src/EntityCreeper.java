package net.minecraft.src;

import org.getspout.spout.entity.CraftCreeper;

import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLightningBolt;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.Item;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntityCreeper extends EntityMob {

	int timeSinceIgnited;
	int lastActiveTime;


	public EntityCreeper(World var1) {
		super(var1);
		this.texture = "/mob/creeper.png";
		//Spout start
		this.spoutEntity = new CraftCreeper(this);
		//Spout end
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte)-1));
		this.dataWatcher.addObject(17, Byte.valueOf((byte)0));
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		if(this.dataWatcher.getWatchableObjectByte(17) == 1) {
			var1.setBoolean("powered", true);
		}

	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.dataWatcher.updateObject(17, Byte.valueOf((byte)(var1.getBoolean("powered")?1:0)));
	}

	protected void attackBlockedEntity(Entity var1, float var2) {
		if(!this.worldObj.multiplayerWorld) {
			if(this.timeSinceIgnited > 0) {
				this.setCreeperState(-1);
				--this.timeSinceIgnited;
				if(this.timeSinceIgnited < 0) {
					this.timeSinceIgnited = 0;
				}
			}

		}
	}

	public void onUpdate() {
		this.lastActiveTime = this.timeSinceIgnited;
		if(this.worldObj.multiplayerWorld) {
			int var1 = this.getCreeperState();
			if(var1 > 0 && this.timeSinceIgnited == 0) {
				this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
			}

			this.timeSinceIgnited += var1;
			if(this.timeSinceIgnited < 0) {
				this.timeSinceIgnited = 0;
			}

			if(this.timeSinceIgnited >= 30) {
				this.timeSinceIgnited = 30;
			}
		}

		super.onUpdate();
		if(this.entityToAttack == null && this.timeSinceIgnited > 0) {
			this.setCreeperState(-1);
			--this.timeSinceIgnited;
			if(this.timeSinceIgnited < 0) {
				this.timeSinceIgnited = 0;
			}
		}

	}

	protected String getHurtSound() {
		return "mob.creeper";
	}

	protected String getDeathSound() {
		return "mob.creeperdeath";
	}

	public void onDeath(DamageSource var1) {
		super.onDeath(var1);
		if(var1.getEntity() instanceof EntitySkeleton) {
			this.dropItem(Item.record13.shiftedIndex + this.rand.nextInt(2), 1);
		}

	}

	protected void attackEntity(Entity var1, float var2) {
		if(!this.worldObj.multiplayerWorld) {
			int var3 = this.getCreeperState();
			if((var3 > 0 || var2 >= 3.0F) && (var3 <= 0 || var2 >= 7.0F)) {
				this.setCreeperState(-1);
				--this.timeSinceIgnited;
				if(this.timeSinceIgnited < 0) {
					this.timeSinceIgnited = 0;
				}
			} else {
				if(this.timeSinceIgnited == 0) {
					this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
				}

				this.setCreeperState(1);
				++this.timeSinceIgnited;
				if(this.timeSinceIgnited >= 30) {
					if(this.getPowered()) {
						this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 6.0F);
					} else {
						this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3.0F);
					}

					this.setEntityDead();
				}

				this.hasAttacked = true;
			}

		}
	}

	public boolean getPowered() {
		return this.dataWatcher.getWatchableObjectByte(17) == 1;
	}
	
	//Spout start
	public void setPowered(boolean power) {
		this.dataWatcher.updateObject(17, power ? 1 : 0);
	}
	//Spout end

	public float setCreeperFlashTime(float var1) {
		return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * var1) / 28.0F;
	}

	protected int getDropItemId() {
		return Item.gunpowder.shiftedIndex;
	}

	private int getCreeperState() {
		return this.dataWatcher.getWatchableObjectByte(16);
	}

	private void setCreeperState(int var1) {
		this.dataWatcher.updateObject(16, Byte.valueOf((byte)var1));
	}

	public void onStruckByLightning(EntityLightningBolt var1) {
		super.onStruckByLightning(var1);
		this.dataWatcher.updateObject(17, Byte.valueOf((byte)1));
	}
}
