package net.minecraft.src;

import net.minecraft.src.EntitySlime;
import net.minecraft.src.Item;
import net.minecraft.src.World;

import org.spoutcraft.client.entity.CraftMagmaCube; //Spout

public class EntityMagmaCube extends EntitySlime {

	public EntityMagmaCube(World var1) {
		super(var1);
		this.texture = "/mob/lava.png";
		this.isImmuneToFire = true;
		this.landMovementFactor = 0.2F;
		//Spout start
		this.spoutEntity = new CraftMagmaCube(this);
		//Spout end
	}

	public boolean getCanSpawnHere() {
		return this.worldObj.difficultySetting > 0 && this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox);
	}

	public int getTotalArmorValue() {
		return this.getSlimeSize() * 3;
	}

	public int getEntityBrightnessForRender(float var1) {
		return 15728880;
	}

	public float getEntityBrightness(float var1) {
		return 1.0F;
	}

	protected String func_40135_ac() {
		return "flame";
	}

	protected EntitySlime func_40132_ae() {
		return new EntityMagmaCube(this.worldObj);
	}

	protected int getDropItemId() {
		return Item.magmaCream.shiftedIndex;

	}



	protected void dropFewItems(boolean var1, int var2) {

		int var3 = this.getDropItemId();

		if(var3 > 0 && this.getSlimeSize() > 1) {

			int var4 = this.rand.nextInt(4) - 2;

			if(var2 > 0) {

				var4 += this.rand.nextInt(var2 + 1);

			}


			for(int var5 = 0; var5 < var4; ++var5) {

				this.dropItem(var3, 1);

			}

		}

	}

	public boolean isBurning() {
		return false;
	}

	protected int func_40131_af() {
		return super.func_40131_af() * 4;
	}

	protected void func_40136_ag() {
		this.field_40139_a *= 0.9F;
	}

	protected void jump() {
		this.motionY = (double)(0.42F + (float)this.getSlimeSize() * 0.1F);
		this.isAirBorne = true;
	}

	protected void fall(float var1) {}

	protected boolean func_40137_ah() {
		return true;
	}

	protected int func_40130_ai() {
		return super.func_40130_ai() + 2;
	}

	protected String getHurtSound() {
		return "mob.slime";
	}

	protected String getDeathSound() {
		return "mob.slime";
	}

	protected String func_40138_aj() {
		return this.getSlimeSize() > 1?"mob.magmacube.big":"mob.magmacube.small";
	}

	public boolean handleLavaMovement() {
		return false;
	}

	protected boolean func_40134_ak() {
		return true;
	}
}
