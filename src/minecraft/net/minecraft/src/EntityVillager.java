package net.minecraft.src;

import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.EntityAIAvoidEntity;
import net.minecraft.src.EntityAIFollowGolem;
import net.minecraft.src.EntityAIMoveIndoors;
import net.minecraft.src.EntityAIMoveTwardsRestriction;
import net.minecraft.src.EntityAIOpenDoor;
import net.minecraft.src.EntityAIPlay;
import net.minecraft.src.EntityAIRestrictOpenDoor;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAIVillagerMate;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityAIWatchClosest2;
import net.minecraft.src.EntityAgeable;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Village;
import net.minecraft.src.World;

import org.spoutcraft.client.entity.CraftVillager; //Spout

public class EntityVillager extends EntityAgeable {

	public int field_48131_b; //Spout private -> public
	private boolean field_48132_c;
	private boolean field_48130_d;
	Village field_48133_a;

	public EntityVillager(World par1World) {
		this(par1World, 0);
		//Spout start
        this.spoutEntity = new CraftVillager(this);
        //Spout end
	}

	public EntityVillager(World par1World, int par2) {
		super(par1World);
		this.field_48131_b = 0;
		this.field_48132_c = false;
		this.field_48130_d = false;
		this.field_48133_a = null;
		this.func_48124_d_(par2);
		this.setTextureByProfession();
		this.moveSpeed = 0.5F;
		this.func_48084_aL().func_48673_b(true);
		this.func_48084_aL().func_48664_a(true);
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

	protected void func_48097_s_() {
		if (--this.field_48131_b <= 0) {
			this.worldObj.field_48465_A.func_48565_a(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
			this.field_48131_b = 70 + this.rand.nextInt(50);
			this.field_48133_a = this.worldObj.field_48465_A.func_48564_a(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);
			if (this.field_48133_a == null) {
				this.func_48083_aW();
			} else {
				ChunkCoordinates var1 = this.field_48133_a.func_48539_a();
				this.func_48082_b(var1.posX, var1.posY, var1.posZ, this.field_48133_a.func_48531_b());
			}
		}

		super.func_48097_s_();
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
		par1NBTTagCompound.setInteger("Profession", this.func_48129_t());
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.func_48124_d_(par1NBTTagCompound.getInteger("Profession"));
		this.setTextureByProfession();
	}

	private void setTextureByProfession() {
		this.texture = "/mob/villager/villager.png";
		if (this.func_48129_t() == 0) {
			this.texture = "/mob/villager/farmer.png";
		}

		if (this.func_48129_t() == 1) {
			this.texture = "/mob/villager/librarian.png";
		}

		if (this.func_48129_t() == 2) {
			this.texture = "/mob/villager/priest.png";
		}

		if (this.func_48129_t() == 3) {
			this.texture = "/mob/villager/smith.png";
		}

		if (this.func_48129_t() == 4) {
			this.texture = "/mob/villager/butcher.png";
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

	public void func_48124_d_(int par1) {
		this.dataWatcher.updateObject(16, Integer.valueOf(par1));
	}

	public int func_48129_t() {
		return this.dataWatcher.getWatchableObjectInt(16);
	}

	public boolean func_48126_w_() {
		return this.field_48132_c;
	}

	public void func_48128_a(boolean par1) {
		this.field_48132_c = par1;
	}

	public void func_48127_b(boolean par1) {
		this.field_48130_d = par1;
	}

	public boolean func_48125_w() {
		return this.field_48130_d;
	}

	public void func_48086_a(EntityLiving par1EntityLiving) {
		super.func_48086_a(par1EntityLiving);
		if (this.field_48133_a != null && par1EntityLiving != null) {
			this.field_48133_a.func_48534_a(par1EntityLiving);
		}

	}
}
