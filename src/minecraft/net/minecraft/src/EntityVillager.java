package net.minecraft.src;

import net.minecraft.src.EntityCreature;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

import org.spoutcraft.client.entity.CraftVillager; //Spout

public class EntityVillager extends EntityCreature {

	public int profession; //Spout private -> public


	public EntityVillager(World var1) {
		this(var1, 0);
        //Spout start
        this.spoutEntity = new CraftVillager(this);
        //Spout end
	}

	public EntityVillager(World var1, int var2) {
		super(var1);
		this.profession = var2;
		this.setTextureByProfession();
		this.moveSpeed = 0.5F;
	}

	public int getMaxHealth() {
		return 20;
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setInteger("Profession", this.profession);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.profession = var1.getInteger("Profession");
		this.setTextureByProfession();
	}

	private void setTextureByProfession() {
		this.texture = "/mob/villager/villager.png";
		if(this.profession == 0) {
			this.texture = "/mob/villager/farmer.png";
		}

		if(this.profession == 1) {
			this.texture = "/mob/villager/librarian.png";
		}

		if(this.profession == 2) {
			this.texture = "/mob/villager/priest.png";
		}

		if(this.profession == 3) {
			this.texture = "/mob/villager/smith.png";
		}

		if(this.profession == 4) {
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
}
