package net.minecraft.src;

import org.getspout.spout.entity.CraftEnderman;

import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;

public class EntityEnderman extends EntityMob {

	private static boolean[] canCarryBlocks = new boolean[256];
	public boolean field_35187_a = false;
	private int field_35184_d = 0;
	private int field_35185_e = 0;


	public EntityEnderman(World var1) {
		super(var1);
		this.texture = "/mob/enderman.png";
		this.moveSpeed = 0.2F;
		this.attackStrength = 5;
		this.setSize(0.6F, 2.9F);
		this.stepHeight = 1.0F;
		//Spout start
		this.spoutEntity = new CraftEnderman(this);
		//Spout end
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte)0));
		this.dataWatcher.addObject(17, new Byte((byte)0));
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setShort("carried", (short)this.func_35176_r());
		var1.setShort("carriedData", (short)this.func_35180_s());
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.func_35177_b(var1.getShort("carried"));
		this.func_35181_c(var1.getShort("carryingData"));
	}

	protected Entity findPlayerToAttack() {
		EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 64.0D);
		if(var1 != null) {
			if(this.shouldAttackPlayer(var1)) {
				if(this.field_35185_e++ == 5) {
					this.field_35185_e = 0;
					return var1;
				}
			} else {
				this.field_35185_e = 0;
			}
		}

		return null;
	}

	public int func_35115_a(float var1) {
		return super.func_35115_a(var1);
	}

	public float getEntityBrightness(float var1) {
		return super.getEntityBrightness(var1);
	}

	private boolean shouldAttackPlayer(EntityPlayer var1) {
		ItemStack var2 = var1.inventory.armorInventory[3];
		if(var2 != null && var2.itemID == Block.pumpkin.blockID) {
			return false;
		} else {
			Vec3D var3 = var1.getLook(1.0F).normalize();
			Vec3D var4 = Vec3D.createVector(this.posX - var1.posX, this.boundingBox.minY + (double)(this.height / 2.0F) - var1.posY + (double)var1.getEyeHeight(), this.posZ - var1.posZ);
			double var5 = var4.lengthVector();
			var4 = var4.normalize();
			double var7 = var3.func_35612_b(var4);
			return var7 > 1.0D - 0.025D / var5?var1.canEntityBeSeen(this):false;
		}
	}

	public void onLivingUpdate() {
		if(this.isWet()) {
			this.attackEntityFrom(DamageSource.drown, 1);
		}

		this.field_35187_a = this.entityToAttack != null;
		this.moveSpeed = this.entityToAttack != null?4.5F:0.3F;
		int var1;
		if(!this.worldObj.multiplayerWorld) {
			int var2;
			int var3;
			int var4;
			if(this.func_35176_r() == 0) {
				if(this.rand.nextInt(20) == 0) {
					var1 = MathHelper.floor_double(this.posX - 2.0D + this.rand.nextDouble() * 4.0D);
					var2 = MathHelper.floor_double(this.posY + this.rand.nextDouble() * 3.0D);
					var3 = MathHelper.floor_double(this.posZ - 2.0D + this.rand.nextDouble() * 4.0D);
					var4 = this.worldObj.getBlockId(var1, var2, var3);
					if(canCarryBlocks[var4]) {
						this.func_35177_b(this.worldObj.getBlockId(var1, var2, var3));
						this.func_35181_c(this.worldObj.getBlockMetadata(var1, var2, var3));
						this.worldObj.setBlockWithNotify(var1, var2, var3, 0);
					}
				}
			} else if(this.rand.nextInt(2000) == 0) {
				var1 = MathHelper.floor_double(this.posX - 1.0D + this.rand.nextDouble() * 2.0D);
				var2 = MathHelper.floor_double(this.posY + this.rand.nextDouble() * 2.0D);
				var3 = MathHelper.floor_double(this.posZ - 1.0D + this.rand.nextDouble() * 2.0D);
				var4 = this.worldObj.getBlockId(var1, var2, var3);
				int var5 = this.worldObj.getBlockId(var1, var2 - 1, var3);
				if(var4 == 0 && var5 > 0 && Block.blocksList[var5].renderAsNormalBlock()) {
					this.worldObj.setBlockAndMetadataWithNotify(var1, var2, var3, this.func_35176_r(), this.func_35180_s());
					this.func_35177_b(0);
				}
			}
		}

		for(var1 = 0; var1 < 2; ++var1) {
			this.worldObj.spawnParticle("portal", this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
		}

		if(this.worldObj.isDaytime() && !this.worldObj.multiplayerWorld) {
			float var6 = this.getEntityBrightness(1.0F);
			if(var6 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (var6 - 0.4F) * 2.0F) {
				this.fire = 300;
			}
		}

		this.isJumping = false;
		if(this.entityToAttack != null) {
			this.faceEntity(this.entityToAttack, 100.0F, 100.0F);
		}

		if(!this.worldObj.multiplayerWorld) {
			if(this.entityToAttack != null) {
				if(this.entityToAttack instanceof EntityPlayer && this.shouldAttackPlayer((EntityPlayer)this.entityToAttack)) {
					this.moveStrafing = this.moveForward = 0.0F;
					this.moveSpeed = 0.0F;
					if(this.entityToAttack.getDistanceSqToEntity(this) < 16.0D) {
						this.func_35178_q();
					}

					this.field_35184_d = 0;
				} else if(this.entityToAttack.getDistanceSqToEntity(this) > 256.0D && this.field_35184_d++ >= 30 && this.func_35182_c(this.entityToAttack)) {
					this.field_35184_d = 0;
				}
			} else {
				this.field_35184_d = 0;
			}
		}

		super.onLivingUpdate();
	}

	protected boolean func_35178_q() {
		double var1 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
		double var3 = this.posY + (double)(this.rand.nextInt(64) - 32);
		double var5 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
		return this.func_35179_a_(var1, var3, var5);
	}

	protected boolean func_35182_c(Entity var1) {
		Vec3D var2 = Vec3D.createVector(this.posX - var1.posX, this.boundingBox.minY + (double)(this.height / 2.0F) - var1.posY + (double)var1.getEyeHeight(), this.posZ - var1.posZ);
		var2 = var2.normalize();
		double var3 = 16.0D;
		double var5 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - var2.xCoord * var3;
		double var7 = this.posY + (double)(this.rand.nextInt(16) - 8) - var2.yCoord * var3;
		double var9 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - var2.zCoord * var3;
		return this.func_35179_a_(var5, var7, var9);
	}

	protected boolean func_35179_a_(double var1, double var3, double var5) {
		double var7 = this.posX;
		double var9 = this.posY;
		double var11 = this.posZ;
		this.posX = var1;
		this.posY = var3;
		this.posZ = var5;
		boolean var13 = false;
		int var14 = MathHelper.floor_double(this.posX);
		int var15 = MathHelper.floor_double(this.posY);
		int var16 = MathHelper.floor_double(this.posZ);
		int var18;
		if(this.worldObj.blockExists(var14, var15, var16)) {
			boolean var17 = false;

			while(!var17 && var15 > 0) {
				var18 = this.worldObj.getBlockId(var14, var15 - 1, var16);
				if(var18 != 0 && Block.blocksList[var18].blockMaterial.getIsSolid()) {
					var17 = true;
				} else {
					--this.posY;
					--var15;
				}
			}

			if(var17) {
				this.setPosition(this.posX, this.posY, this.posZ);
				if(this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox)) {
					var13 = true;
				}
			}
		}

		if(!var13) {
			this.setPosition(var7, var9, var11);
			return false;
		} else {
			short var30 = 128;

			for(var18 = 0; var18 < var30; ++var18) {
				double var19 = (double)var18 / ((double)var30 - 1.0D);
				float var21 = (this.rand.nextFloat() - 0.5F) * 0.2F;
				float var22 = (this.rand.nextFloat() - 0.5F) * 0.2F;
				float var23 = (this.rand.nextFloat() - 0.5F) * 0.2F;
				double var24 = var7 + (this.posX - var7) * var19 + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D;
				double var26 = var9 + (this.posY - var9) * var19 + this.rand.nextDouble() * (double)this.height;
				double var28 = var11 + (this.posZ - var11) * var19 + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D;
				this.worldObj.spawnParticle("portal", var24, var26, var28, (double)var21, (double)var22, (double)var23);
			}

			return true;
		}
	}

	protected String getLivingSound() {
		return "mob.zombie";
	}

	protected String getHurtSound() {
		return "mob.zombiehurt";
	}

	protected String getDeathSound() {
		return "mob.zombiedeath";
	}

	protected int getDropItemId() {
		return Item.enderPearl.shiftedIndex;
	}

	protected void dropFewItems(boolean var1) {
		int var2 = this.getDropItemId();
		if(var2 > 0) {
			int var3 = this.rand.nextInt(2);

			for(int var4 = 0; var4 < var3; ++var4) {
				this.dropItem(var2, 1);
			}
		}

	}

	public void func_35177_b(int var1) {
		this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var1 & 255)));
	}

	public int func_35176_r() {
		return this.dataWatcher.getWatchableObjectByte(16);
	}

	public void func_35181_c(int var1) {
		this.dataWatcher.updateObject(17, Byte.valueOf((byte)(var1 & 255)));
	}

	public int func_35180_s() {
		return this.dataWatcher.getWatchableObjectByte(17);
	}

	static {
		canCarryBlocks[Block.stone.blockID] = true;
		canCarryBlocks[Block.grass.blockID] = true;
		canCarryBlocks[Block.dirt.blockID] = true;
		canCarryBlocks[Block.cobblestone.blockID] = true;
		canCarryBlocks[Block.planks.blockID] = true;
		canCarryBlocks[Block.sand.blockID] = true;
		canCarryBlocks[Block.gravel.blockID] = true;
		canCarryBlocks[Block.oreGold.blockID] = true;
		canCarryBlocks[Block.oreIron.blockID] = true;
		canCarryBlocks[Block.oreCoal.blockID] = true;
		canCarryBlocks[Block.wood.blockID] = true;
		canCarryBlocks[Block.leaves.blockID] = true;
		canCarryBlocks[Block.sponge.blockID] = true;
		canCarryBlocks[Block.glass.blockID] = true;
		canCarryBlocks[Block.oreLapis.blockID] = true;
		canCarryBlocks[Block.blockLapis.blockID] = true;
		canCarryBlocks[Block.sandStone.blockID] = true;
		canCarryBlocks[Block.cloth.blockID] = true;
		canCarryBlocks[Block.plantYellow.blockID] = true;
		canCarryBlocks[Block.plantRed.blockID] = true;
		canCarryBlocks[Block.mushroomBrown.blockID] = true;
		canCarryBlocks[Block.mushroomRed.blockID] = true;
		canCarryBlocks[Block.blockGold.blockID] = true;
		canCarryBlocks[Block.blockSteel.blockID] = true;
		canCarryBlocks[Block.brick.blockID] = true;
		canCarryBlocks[Block.tnt.blockID] = true;
		canCarryBlocks[Block.bookShelf.blockID] = true;
		canCarryBlocks[Block.cobblestoneMossy.blockID] = true;
		canCarryBlocks[Block.oreDiamond.blockID] = true;
		canCarryBlocks[Block.blockDiamond.blockID] = true;
		canCarryBlocks[Block.workbench.blockID] = true;
		canCarryBlocks[Block.oreRedstone.blockID] = true;
		canCarryBlocks[Block.oreRedstoneGlowing.blockID] = true;
		canCarryBlocks[Block.ice.blockID] = true;
		canCarryBlocks[Block.cactus.blockID] = true;
		canCarryBlocks[Block.blockClay.blockID] = true;
		canCarryBlocks[Block.pumpkin.blockID] = true;
		canCarryBlocks[Block.netherrack.blockID] = true;
		canCarryBlocks[Block.slowSand.blockID] = true;
		canCarryBlocks[Block.glowStone.blockID] = true;
		canCarryBlocks[Block.pumpkinLantern.blockID] = true;
		canCarryBlocks[Block.stoneBrick.blockID] = true;
		canCarryBlocks[Block.field_35286_bo.blockID] = true;
		canCarryBlocks[Block.field_35287_bp.blockID] = true;
		canCarryBlocks[Block.melon.blockID] = true;
	}
}
