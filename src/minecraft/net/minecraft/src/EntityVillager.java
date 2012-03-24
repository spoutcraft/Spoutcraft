package net.minecraft.src;

import org.spoutcraft.client.entity.CraftVillager; //Spout

public class EntityVillager extends EntityAgeable {
	public int randomTickDivider; //Spout private -> public
	private boolean isMatingFlag;
	private boolean isPlayingFlag;
	Village villageObj;

	public EntityVillager(World par1World) {
		this(par1World, 0);
		//Spout start
		this.spoutEntity = new CraftVillager(this);
		//Spout end
	}

	public EntityVillager(World par1World, int par2) {
		super(par1World);
		this.randomTickDivider = 0;
		this.isMatingFlag = false;
		this.isPlayingFlag = false;
		this.villageObj = null;
		this.setProfession(par2);
		this.texture = "/mob/villager/villager.png";
		this.moveSpeed = 0.5F;
		this.getNavigator().setBreakDoors(true);
		this.getNavigator().func_48664_a(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.3F, 0.35F));
		this.tasks.addTask(2, new EntityAIMoveIndoors(this));
		this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
		this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(5, new EntityAIMoveTwardsRestriction(this, 0.3F));
		this.tasks.addTask(6, new EntityAIVillagerMate(this));
		this.tasks.addTask(7, new EntityAIFollowGolem(this));
		this.tasks.addTask(8, new EntityAIPlay(this, 0.32F));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityVillager.class, 5.0F, 0.02F));
		this.tasks.addTask(9, new EntityAIWander(this, 0.3F));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		//Spout start
		this.spoutEntity = new CraftVillager(this);
		//Spout end
	}

	public boolean isAIEnabled() {
		return true;
	}

	protected void updateAITick() {
		if (--this.randomTickDivider <= 0) {
			this.worldObj.villageCollectionObj.addVillagerPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
			this.randomTickDivider = 70 + this.rand.nextInt(50);
			this.villageObj = this.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);
			if (this.villageObj == null) {
				this.detachHome();
			} else {
				ChunkCoordinates var1 = this.villageObj.getCenter();
				this.setHomeArea(var1.posX, var1.posY, var1.posZ, this.villageObj.getVillageRadius());
			}
		}

		super.updateAITick();
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Integer.valueOf(0));
	}

	public int getMaxHealth() {
		return 20;
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("Profession", this.getProfession());
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setProfession(par1NBTTagCompound.getInteger("Profession"));
		}

	public String getTexture() {
		switch(this.getProfession()) {
		case 0:
			return "/mob/villager/farmer.png";
		case 1:
			return "/mob/villager/librarian.png";
		case 2:
			return "/mob/villager/priest.png";
		case 3:
			return "/mob/villager/smith.png";
		case 4:
			return "/mob/villager/butcher.png";
		default:
			return super.getTexture();
		}
	}

	protected boolean canDespawn() {
		return false;
	}

	protected String getLivingSound() {
		return "mob.villager.default";
	}

	protected String getHurtSound() {
		return "mob.villager.defaulthurt";
	}

	protected String getDeathSound() {
		return "mob.villager.defaultdeath";
	}

	public void setProfession(int par1) {
		this.dataWatcher.updateObject(16, Integer.valueOf(par1));
	}

	public int getProfession() {
		return this.dataWatcher.getWatchableObjectInt(16);
	}

	public boolean getIsMatingFlag() {
		return this.isMatingFlag;
	}

	public void setIsMatingFlag(boolean par1) {
		this.isMatingFlag = par1;
	}

	public void setIsPlayingFlag(boolean par1) {
		this.isPlayingFlag = par1;
	}

	public boolean getIsPlayingFlag() {
		return this.isPlayingFlag;
	}

	public void setRevengeTarget(EntityLiving par1EntityLiving) {
		super.setRevengeTarget(par1EntityLiving);
		if (this.villageObj != null && par1EntityLiving != null) {
			this.villageObj.addOrRenewAgressor(par1EntityLiving);
		}
	}
}
