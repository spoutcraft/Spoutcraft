package net.minecraft.src;

import net.minecraft.src.EntityCreature;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

import org.spoutcraft.client.entity.CraftVillager; //Spout

public class EntityVillager extends EntityCreature {

	private int field_40141_a;


	public EntityVillager(World var1) {
		this(var1, 0);
        //Spout start
        this.spoutEntity = new CraftVillager(this);
        //Spout end
	}

	public EntityVillager(World var1, int var2) {
		super(var1);
		this.field_40141_a = var2;
		this.func_40140_ac();
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
		var1.setInteger("Profession", this.field_40141_a);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.field_40141_a = var1.getInteger("Profession");
		this.func_40140_ac();
	}

	private void func_40140_ac() {
		this.texture = "/mob/villager/villager.png";
		if(this.field_40141_a == 0) {
			this.texture = "/mob/villager/farmer.png";
		}

		if(this.field_40141_a == 1) {
			this.texture = "/mob/villager/librarian.png";
		}

		if(this.field_40141_a == 2) {
			this.texture = "/mob/villager/priest.png";
		}

		if(this.field_40141_a == 3) {
			this.texture = "/mob/villager/smith.png";
		}

		if(this.field_40141_a == 4) {
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
