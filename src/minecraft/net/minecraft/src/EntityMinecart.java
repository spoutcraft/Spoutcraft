package net.minecraft.src;

import java.util.List;
// Spout Start
import org.getspout.spout.entity.CraftCaveSpider;
import org.getspout.spout.entity.CraftMinecart;
import org.getspout.spout.entity.CraftPoweredMinecart;
import org.getspout.spout.entity.CraftStorageMinecart;
// Spout End
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockRail;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;

public class EntityMinecart extends Entity implements IInventory {

	private ItemStack[] cargoItems;
	private int fuel;
	private boolean field_856_i;
	public int minecartType;
	public double pushX;
	public double pushZ;
	private static final int[][][] field_855_j = new int[][][]{{{0, 0, -1}, {0, 0, 1}}, {{-1, 0, 0}, {1, 0, 0}}, {{-1, -1, 0}, {1, 0, 0}}, {{-1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, {-1, 0, 0}}, {{0, 0, -1}, {-1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
	private int minecartPosRotationIncrements;
	private double minecartX;
	private double minecartY;
	private double minecartZ;
	private double minecartYaw;
	private double minecartPitch;
	private double velocityX;
	private double velocityY;
	private double velocityZ;
	//Spout start
	public boolean slowWhenEmpty = true;
	public double derailedX = 0.5;
	public double derailedY = 0.5;
	public double derailedZ = 0.5;
	public double flyingX = 0.95;
	public double flyingY = 0.95;
	public double flyingZ = 0.95;
	public double maxSpeed = 0.4D;

	public ItemStack[] getContents() {
		return this.cargoItems;
	}
	//Spout end


	public EntityMinecart(World var1) {
		super(var1);
		this.cargoItems = new ItemStack[27]; // Spout 36 -> 27
		this.fuel = 0;
		this.field_856_i = false;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.7F);
		this.yOffset = this.height / 2.0F;
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {
		this.dataWatcher.addObject(16, new Byte((byte)0));
		this.dataWatcher.addObject(17, new Integer(0));
		this.dataWatcher.addObject(18, new Integer(1));
		this.dataWatcher.addObject(19, new Integer(0));
	}

	public AxisAlignedBB getCollisionBox(Entity var1) {
		return var1.boundingBox;
	}

	public AxisAlignedBB getBoundingBox() {
		return null;
	}

	public boolean canBePushed() {
		return true;
	}

	public EntityMinecart(World var1, double var2, double var4, double var6, int var8) {
		this(var1);
		this.setPosition(var2, var4 + (double)this.yOffset, var6);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = var2;
		this.prevPosY = var4;
		this.prevPosZ = var6;
		this.minecartType = var8;
		
		//Spout start
		if (minecartType == CraftMinecart.Type.Minecart.getId()) {
			this.spoutEntity = new CraftMinecart(this);
		}
		else if (minecartType == CraftMinecart.Type.PoweredMinecart.getId()) {
			this.spoutEntity = new CraftPoweredMinecart(this);
		}
		else {
			this.spoutEntity = new CraftStorageMinecart(this);
		}
		//Spout end
	}

	public double getMountedYOffset() {
		return (double)this.height * 0.0D - 0.30000001192092896D;
	}

	public boolean attackEntityFrom(DamageSource var1, int var2) {
		if(!this.worldObj.multiplayerWorld && !this.isDead) {
			this.func_41029_h(-this.func_41030_m());
			this.func_41028_c(10);
			this.setBeenAttacked();
			this.func_41024_b(this.func_41025_i() + var2 * 10);
			if(this.func_41025_i() > 40) {
				if(this.riddenByEntity != null) {
					this.riddenByEntity.mountEntity(this);
				}

				this.setEntityDead();
				this.dropItemWithOffset(Item.minecartEmpty.shiftedIndex, 1, 0.0F);
				if(this.minecartType == 1) {
					EntityMinecart var3 = this;

					for(int var4 = 0; var4 < var3.getSizeInventory(); ++var4) {
						ItemStack var5 = var3.getStackInSlot(var4);
						if(var5 != null) {
							float var6 = this.rand.nextFloat() * 0.8F + 0.1F;
							float var7 = this.rand.nextFloat() * 0.8F + 0.1F;
							float var8 = this.rand.nextFloat() * 0.8F + 0.1F;

							while(var5.stackSize > 0) {
								int var9 = this.rand.nextInt(21) + 10;
								if(var9 > var5.stackSize) {
									var9 = var5.stackSize;
								}

								var5.stackSize -= var9;
								EntityItem var10 = new EntityItem(this.worldObj, this.posX + (double)var6, this.posY + (double)var7, this.posZ + (double)var8, new ItemStack(var5.itemID, var9, var5.getItemDamage()));
								float var11 = 0.05F;
								var10.motionX = (double)((float)this.rand.nextGaussian() * var11);
								var10.motionY = (double)((float)this.rand.nextGaussian() * var11 + 0.2F);
								var10.motionZ = (double)((float)this.rand.nextGaussian() * var11);
								this.worldObj.entityJoinedWorld(var10);
							}
						}
					}

					this.dropItemWithOffset(Block.chest.blockID, 1, 0.0F);
				} else if(this.minecartType == 2) {
					this.dropItemWithOffset(Block.stoneOvenIdle.blockID, 1, 0.0F);
				}
			}

			return true;
		} else {
			return true;
		}
	}

	public void performHurtAnimation() {
		this.func_41029_h(-this.func_41030_m());
		this.func_41028_c(10);
		this.func_41024_b(this.func_41025_i() + this.func_41025_i() * 10);
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public void setEntityDead() {
		for(int var1 = 0; var1 < this.getSizeInventory(); ++var1) {
			ItemStack var2 = this.getStackInSlot(var1);
			if(var2 != null) {
				float var3 = this.rand.nextFloat() * 0.8F + 0.1F;
				float var4 = this.rand.nextFloat() * 0.8F + 0.1F;
				float var5 = this.rand.nextFloat() * 0.8F + 0.1F;

				while(var2.stackSize > 0) {
					int var6 = this.rand.nextInt(21) + 10;
					if(var6 > var2.stackSize) {
						var6 = var2.stackSize;
					}

					var2.stackSize -= var6;
					EntityItem var7 = new EntityItem(this.worldObj, this.posX + (double)var3, this.posY + (double)var4, this.posZ + (double)var5, new ItemStack(var2.itemID, var6, var2.getItemDamage()));
					float var8 = 0.05F;
					var7.motionX = (double)((float)this.rand.nextGaussian() * var8);
					var7.motionY = (double)((float)this.rand.nextGaussian() * var8 + 0.2F);
					var7.motionZ = (double)((float)this.rand.nextGaussian() * var8);
					this.worldObj.entityJoinedWorld(var7);
				}
			}
		}

		super.setEntityDead();
	}

	public void onUpdate() {
		if(this.func_41023_l() > 0) {
			this.func_41028_c(this.func_41023_l() - 1);
		}

		if(this.func_41025_i() > 0) {
			this.func_41024_b(this.func_41025_i() - 1);
		}

		if(this.func_41026_g() && this.rand.nextInt(4) == 0) {
			this.worldObj.spawnParticle("largesmoke", this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D);
		}

		if(this.worldObj.multiplayerWorld) {
			if(this.minecartPosRotationIncrements > 0) {
				double var45 = this.posX + (this.minecartX - this.posX) / (double)this.minecartPosRotationIncrements;
				double var46 = this.posY + (this.minecartY - this.posY) / (double)this.minecartPosRotationIncrements;
				double var5 = this.posZ + (this.minecartZ - this.posZ) / (double)this.minecartPosRotationIncrements;

				double var7;
				for(var7 = this.minecartYaw - (double)this.rotationYaw; var7 < -180.0D; var7 += 360.0D) {
					;
				}

				while(var7 >= 180.0D) {
					var7 -= 360.0D;
				}

				this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.minecartPosRotationIncrements);
				this.rotationPitch = (float)((double)this.rotationPitch + (this.minecartPitch - (double)this.rotationPitch) / (double)this.minecartPosRotationIncrements);
				--this.minecartPosRotationIncrements;
				this.setPosition(var45, var46, var5);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			} else {
				this.setPosition(this.posX, this.posY, this.posZ);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			}

		} else {
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			this.motionY -= 0.03999999910593033D;
			int var1 = MathHelper.floor_double(this.posX);
			int var2 = MathHelper.floor_double(this.posY);
			int var3 = MathHelper.floor_double(this.posZ);
			if(BlockRail.isRailBlockAt(this.worldObj, var1, var2 - 1, var3)) {
				--var2;
			}

			double var4 = this.maxspeed; //Spout
			double var6 = 0.0078125D;
			int var8 = this.worldObj.getBlockId(var1, var2, var3);
			if(BlockRail.isRailBlock(var8)) {
				Vec3D var9 = this.func_514_g(this.posX, this.posY, this.posZ);
				int var10 = this.worldObj.getBlockMetadata(var1, var2, var3);
				this.posY = (double)var2;
				boolean var11 = false;
				boolean var12 = false;
				if(var8 == Block.railPowered.blockID) {
					var11 = (var10 & 8) != 0;
					var12 = !var11;
				}

				if(((BlockRail)Block.blocksList[var8]).getIsPowered()) {
					var10 &= 7;
				}

				if(var10 >= 2 && var10 <= 5) {
					this.posY = (double)(var2 + 1);
				}

				if(var10 == 2) {
					this.motionX -= var6;
				}

				if(var10 == 3) {
					this.motionX += var6;
				}

				if(var10 == 4) {
					this.motionZ += var6;
				}

				if(var10 == 5) {
					this.motionZ -= var6;
				}

				int[][] var13 = field_855_j[var10];
				double var14 = (double)(var13[1][0] - var13[0][0]);
				double var16 = (double)(var13[1][2] - var13[0][2]);
				double var18 = Math.sqrt(var14 * var14 + var16 * var16);
				double var20 = this.motionX * var14 + this.motionZ * var16;
				if(var20 < 0.0D) {
					var14 = -var14;
					var16 = -var16;
				}

				double var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
				this.motionX = var22 * var14 / var18;
				this.motionZ = var22 * var16 / var18;
				double var24;
				if(var12) {
					var24 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
					if(var24 < 0.03D) {
						this.motionX *= 0.0D;
						this.motionY *= 0.0D;
						this.motionZ *= 0.0D;
					} else {
						this.motionX *= 0.5D;
						this.motionY *= 0.0D;
						this.motionZ *= 0.5D;
					}
				}

				var24 = 0.0D;
				double var26 = (double)var1 + 0.5D + (double)var13[0][0] * 0.5D;
				double var28 = (double)var3 + 0.5D + (double)var13[0][2] * 0.5D;
				double var30 = (double)var1 + 0.5D + (double)var13[1][0] * 0.5D;
				double var32 = (double)var3 + 0.5D + (double)var13[1][2] * 0.5D;
				var14 = var30 - var26;
				var16 = var32 - var28;
				double var34;
				double var38;
				double var36;
				if(var14 == 0.0D) {
					this.posX = (double)var1 + 0.5D;
					var24 = this.posZ - (double)var3;
				} else if(var16 == 0.0D) {
					this.posZ = (double)var3 + 0.5D;
					var24 = this.posX - (double)var1;
				} else {
					var34 = this.posX - var26;
					var36 = this.posZ - var28;
					var38 = (var34 * var14 + var36 * var16) * 2.0D;
					var24 = var38;
				}

				this.posX = var26 + var14 * var24;
				this.posZ = var28 + var16 * var24;
				this.setPosition(this.posX, this.posY + (double)this.yOffset, this.posZ);
				var34 = this.motionX;
				var36 = this.motionZ;
				if(this.riddenByEntity != null) {
					var34 *= 0.75D;
					var36 *= 0.75D;
				}

				if(var34 < -var4) {
					var34 = -var4;
				}

				if(var34 > var4) {
					var34 = var4;
				}

				if(var36 < -var4) {
					var36 = -var4;
				}

				if(var36 > var4) {
					var36 = var4;
				}

				this.moveEntity(var34, 0.0D, var36);
				if(var13[0][1] != 0 && MathHelper.floor_double(this.posX) - var1 == var13[0][0] && MathHelper.floor_double(this.posZ) - var3 == var13[0][2]) {
					this.setPosition(this.posX, this.posY + (double)var13[0][1], this.posZ);
				} else if(var13[1][1] != 0 && MathHelper.floor_double(this.posX) - var1 == var13[1][0] && MathHelper.floor_double(this.posZ) - var3 == var13[1][2]) {
					this.setPosition(this.posX, this.posY + (double)var13[1][1], this.posZ);
				}

				if(this.riddenByEntity != null || !this.slowWhenEmpty) { // Spout
					this.motionX *= 0.996999979019165D;
					this.motionY *= 0.0D;
					this.motionZ *= 0.996999979019165D;
				} else {
					if(this.minecartType == 2) {
						var38 = (double)MathHelper.sqrt_double(this.pushX * this.pushX + this.pushZ * this.pushZ);
						if(var38 > 0.01D) {
							this.pushX /= var38;
							this.pushZ /= var38;
							double var40 = 0.04D;
							this.motionX *= 0.800000011920929D;
							this.motionY *= 0.0D;
							this.motionZ *= 0.800000011920929D;
							this.motionX += this.pushX * var40;
							this.motionZ += this.pushZ * var40;
						} else {
							this.motionX *= 0.8999999761581421D;
							this.motionY *= 0.0D;
							this.motionZ *= 0.8999999761581421D;
						}
					}

					this.motionX *= 0.9599999785423279D;
					this.motionY *= 0.0D;
					this.motionZ *= 0.9599999785423279D;
				}

				Vec3D var52 = this.func_514_g(this.posX, this.posY, this.posZ);
				if(var52 != null && var9 != null) {
					double var39 = (var9.yCoord - var52.yCoord) * 0.05D;
					var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
					if(var22 > 0.0D) {
						this.motionX = this.motionX / var22 * (var22 + var39);
						this.motionZ = this.motionZ / var22 * (var22 + var39);
					}

					this.setPosition(this.posX, var52.yCoord, this.posZ);
				}

				int var51 = MathHelper.floor_double(this.posX);
				int var53 = MathHelper.floor_double(this.posZ);
				if(var51 != var1 || var53 != var3) {
					var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
					this.motionX = var22 * (double)(var51 - var1);
					this.motionZ = var22 * (double)(var53 - var3);
				}

				double var41;
				if(this.minecartType == 2) {
					var41 = (double)MathHelper.sqrt_double(this.pushX * this.pushX + this.pushZ * this.pushZ);
					if(var41 > 0.01D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.0010D) {
						this.pushX /= var41;
						this.pushZ /= var41;
						if(this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0D) {
							this.pushX = 0.0D;
							this.pushZ = 0.0D;
						} else {
							this.pushX = this.motionX;
							this.pushZ = this.motionZ;
						}
					}
				}

				if(var11) {
					var41 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
					if(var41 > 0.01D) {
						double var43 = 0.06D;
						this.motionX += this.motionX / var41 * var43;
						this.motionZ += this.motionZ / var41 * var43;
					} else if(var10 == 1) {
						if(this.worldObj.isBlockNormalCube(var1 - 1, var2, var3)) {
							this.motionX = 0.02D;
						} else if(this.worldObj.isBlockNormalCube(var1 + 1, var2, var3)) {
							this.motionX = -0.02D;
						}
					} else if(var10 == 0) {
						if(this.worldObj.isBlockNormalCube(var1, var2, var3 - 1)) {
							this.motionZ = 0.02D;
						} else if(this.worldObj.isBlockNormalCube(var1, var2, var3 + 1)) {
							this.motionZ = -0.02D;
						}
					}
				}
			} else {
				if(this.motionX < -var4) {
					this.motionX = -var4;
				}

				if(this.motionX > var4) {
					this.motionX = var4;
				}

				if(this.motionZ < -var4) {
					this.motionZ = -var4;
				}

				if(this.motionZ > var4) {
					this.motionZ = var4;
				}

				if(this.onGround) {
					this.motionX *= 0.5D;
					this.motionY *= 0.5D;
					this.motionZ *= 0.5D;
				}

				this.moveEntity(this.motionX, this.motionY, this.motionZ);
				if(!this.onGround) {
					this.motionX *= 0.949999988079071D;
					this.motionY *= 0.949999988079071D;
					this.motionZ *= 0.949999988079071D;
				}
			}

			this.rotationPitch = 0.0F;
			double var47 = this.prevPosX - this.posX;
			double var48 = this.prevPosZ - this.posZ;
			if(var47 * var47 + var48 * var48 > 0.0010D) {
				this.rotationYaw = (float)(Math.atan2(var48, var47) * 180.0D / 3.141592653589793D);
				if(this.field_856_i) {
					this.rotationYaw += 180.0F;
				}
			}

			double var49;
			for(var49 = (double)(this.rotationYaw - this.prevRotationYaw); var49 >= 180.0D; var49 -= 360.0D) {
				;
			}

			while(var49 < -180.0D) {
				var49 += 360.0D;
			}

			if(var49 < -170.0D || var49 >= 170.0D) {
				this.rotationYaw += 180.0F;
				this.field_856_i = !this.field_856_i;
			}

			this.setRotation(this.rotationYaw, this.rotationPitch);
			List var15 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
			if(var15 != null && var15.size() > 0) {
				for(int var50 = 0; var50 < var15.size(); ++var50) {
					Entity var17 = (Entity)var15.get(var50);
					if(var17 != this.riddenByEntity && var17.canBePushed() && var17 instanceof EntityMinecart) {
						var17.applyEntityCollision(this);
					}
				}
			}

			if(this.riddenByEntity != null && this.riddenByEntity.isDead) {
				if(this.riddenByEntity.ridingEntity == this) {
					this.riddenByEntity.ridingEntity = null;
				}

				this.riddenByEntity = null;
			}

			if(this.fuel > 0) {
				--this.fuel;
			}

			if(this.fuel <= 0) {
				this.pushX = this.pushZ = 0.0D;
			}

			this.func_41027_b(this.fuel > 0);
		}
	}

	public Vec3D func_515_a(double var1, double var3, double var5, double var7) {
		int var9 = MathHelper.floor_double(var1);
		int var10 = MathHelper.floor_double(var3);
		int var11 = MathHelper.floor_double(var5);
		if(BlockRail.isRailBlockAt(this.worldObj, var9, var10 - 1, var11)) {
			--var10;
		}

		int var12 = this.worldObj.getBlockId(var9, var10, var11);
		if(!BlockRail.isRailBlock(var12)) {
			return null;
		} else {
			int var13 = this.worldObj.getBlockMetadata(var9, var10, var11);
			if(((BlockRail)Block.blocksList[var12]).getIsPowered()) {
				var13 &= 7;
			}

			var3 = (double)var10;
			if(var13 >= 2 && var13 <= 5) {
				var3 = (double)(var10 + 1);
			}

			int[][] var14 = field_855_j[var13];
			double var15 = (double)(var14[1][0] - var14[0][0]);
			double var17 = (double)(var14[1][2] - var14[0][2]);
			double var19 = Math.sqrt(var15 * var15 + var17 * var17);
			var15 /= var19;
			var17 /= var19;
			var1 += var15 * var7;
			var5 += var17 * var7;
			if(var14[0][1] != 0 && MathHelper.floor_double(var1) - var9 == var14[0][0] && MathHelper.floor_double(var5) - var11 == var14[0][2]) {
				var3 += (double)var14[0][1];
			} else if(var14[1][1] != 0 && MathHelper.floor_double(var1) - var9 == var14[1][0] && MathHelper.floor_double(var5) - var11 == var14[1][2]) {
				var3 += (double)var14[1][1];
			}

			return this.func_514_g(var1, var3, var5);
		}
	}

	public Vec3D func_514_g(double var1, double var3, double var5) {
		int var7 = MathHelper.floor_double(var1);
		int var8 = MathHelper.floor_double(var3);
		int var9 = MathHelper.floor_double(var5);
		if(BlockRail.isRailBlockAt(this.worldObj, var7, var8 - 1, var9)) {
			--var8;
		}

		int var10 = this.worldObj.getBlockId(var7, var8, var9);
		if(BlockRail.isRailBlock(var10)) {
			int var11 = this.worldObj.getBlockMetadata(var7, var8, var9);
			var3 = (double)var8;
			if(((BlockRail)Block.blocksList[var10]).getIsPowered()) {
				var11 &= 7;
			}

			if(var11 >= 2 && var11 <= 5) {
				var3 = (double)(var8 + 1);
			}

			int[][] var12 = field_855_j[var11];
			double var13 = 0.0D;
			double var15 = (double)var7 + 0.5D + (double)var12[0][0] * 0.5D;
			double var17 = (double)var8 + 0.5D + (double)var12[0][1] * 0.5D;
			double var19 = (double)var9 + 0.5D + (double)var12[0][2] * 0.5D;
			double var21 = (double)var7 + 0.5D + (double)var12[1][0] * 0.5D;
			double var23 = (double)var8 + 0.5D + (double)var12[1][1] * 0.5D;
			double var25 = (double)var9 + 0.5D + (double)var12[1][2] * 0.5D;
			double var27 = var21 - var15;
			double var29 = (var23 - var17) * 2.0D;
			double var31 = var25 - var19;
			if(var27 == 0.0D) {
				var1 = (double)var7 + 0.5D;
				var13 = var5 - (double)var9;
			} else if(var31 == 0.0D) {
				var5 = (double)var9 + 0.5D;
				var13 = var1 - (double)var7;
			} else {
				double var33 = var1 - var15;
				double var35 = var5 - var19;
				double var37 = (var33 * var27 + var35 * var31) * 2.0D;
				var13 = var37;
			}

			var1 = var15 + var27 * var13;
			var3 = var17 + var29 * var13;
			var5 = var19 + var31 * var13;
			if(var29 < 0.0D) {
				++var3;
			}

			if(var29 > 0.0D) {
				var3 += 0.5D;
			}

			return Vec3D.createVector(var1, var3, var5);
		} else {
			return null;
		}
	}

	protected void writeEntityToNBT(NBTTagCompound var1) {
		var1.setInteger("Type", this.minecartType);
		if(this.minecartType == 2) {
			var1.setDouble("PushX", this.pushX);
			var1.setDouble("PushZ", this.pushZ);
			var1.setShort("Fuel", (short)this.fuel);
		} else if(this.minecartType == 1) {
			NBTTagList var2 = new NBTTagList();

			for(int var3 = 0; var3 < this.cargoItems.length; ++var3) {
				if(this.cargoItems[var3] != null) {
					NBTTagCompound var4 = new NBTTagCompound();
					var4.setByte("Slot", (byte)var3);
					this.cargoItems[var3].writeToNBT(var4);
					var2.setTag(var4);
				}
			}

			var1.setTag("Items", var2);
		}

	}

	protected void readEntityFromNBT(NBTTagCompound var1) {
		this.minecartType = var1.getInteger("Type");
		if(this.minecartType == 2) {
			this.pushX = var1.getDouble("PushX");
			this.pushZ = var1.getDouble("PushZ");
			this.fuel = var1.getShort("Fuel");
		} else if(this.minecartType == 1) {
			NBTTagList var2 = var1.getTagList("Items");
			this.cargoItems = new ItemStack[this.getSizeInventory()];

			for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
				NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
				int var5 = var4.getByte("Slot") & 255;
				if(var5 >= 0 && var5 < this.cargoItems.length) {
					this.cargoItems[var5] = ItemStack.loadItemStackFromNBT(var4);
				}
			}
		}

	}

	public float getShadowSize() {
		return 0.0F;
	}

	public void applyEntityCollision(Entity var1) {
		if(!this.worldObj.multiplayerWorld) {
			if(var1 != this.riddenByEntity) {
				if(var1 instanceof EntityLiving && !(var1 instanceof EntityPlayer) && this.minecartType == 0 && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01D && this.riddenByEntity == null && var1.ridingEntity == null) {
					var1.mountEntity(this);
				}

				double var2 = var1.posX - this.posX;
				double var4 = var1.posZ - this.posZ;
				double var6 = var2 * var2 + var4 * var4;
				if(var6 >= 9.999999747378752E-5D) {
					var6 = (double)MathHelper.sqrt_double(var6);
					var2 /= var6;
					var4 /= var6;
					double var8 = 1.0D / var6;
					if(var8 > 1.0D) {
						var8 = 1.0D;
					}

					var2 *= var8;
					var4 *= var8;
					var2 *= 0.10000000149011612D;
					var4 *= 0.10000000149011612D;
					var2 *= (double)(1.0F - this.entityCollisionReduction);
					var4 *= (double)(1.0F - this.entityCollisionReduction);
					var2 *= 0.5D;
					var4 *= 0.5D;
					if(var1 instanceof EntityMinecart) {
						double var10 = var1.posX - this.posX;
						double var12 = var1.posZ - this.posZ;
						Vec3D var14 = Vec3D.createVector(var10, 0.0D, var12).normalize();
						Vec3D var15 = Vec3D.createVector((double)MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F), 0.0D, (double)MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F)).normalize();
						double var16 = Math.abs(var14.dotProduct(var15));
						if(var16 < 0.800000011920929D) {
							return;
						}

						double var18 = var1.motionX + this.motionX;
						double var20 = var1.motionZ + this.motionZ;
						if(((EntityMinecart)var1).minecartType == 2 && this.minecartType != 2) {
							this.motionX *= 0.20000000298023224D;
							this.motionZ *= 0.20000000298023224D;
							this.addVelocity(var1.motionX - var2, 0.0D, var1.motionZ - var4);
							var1.motionX *= 0.949999988079071D;
							var1.motionZ *= 0.949999988079071D;
						} else if(((EntityMinecart)var1).minecartType != 2 && this.minecartType == 2) {
							var1.motionX *= 0.20000000298023224D;
							var1.motionZ *= 0.20000000298023224D;
							var1.addVelocity(this.motionX + var2, 0.0D, this.motionZ + var4);
							this.motionX *= 0.949999988079071D;
							this.motionZ *= 0.949999988079071D;
						} else {
							var18 /= 2.0D;
							var20 /= 2.0D;
							this.motionX *= 0.20000000298023224D;
							this.motionZ *= 0.20000000298023224D;
							this.addVelocity(var18 - var2, 0.0D, var20 - var4);
							var1.motionX *= 0.20000000298023224D;
							var1.motionZ *= 0.20000000298023224D;
							var1.addVelocity(var18 + var2, 0.0D, var20 + var4);
						}
					} else {
						this.addVelocity(-var2, 0.0D, -var4);
						var1.addVelocity(var2 / 4.0D, 0.0D, var4 / 4.0D);
					}
				}

			}
		}
	}

	public int getSizeInventory() {
		return 27;
	}

	public ItemStack getStackInSlot(int var1) {
		return this.cargoItems[var1];
	}

	public ItemStack decrStackSize(int var1, int var2) {
		if(this.cargoItems[var1] != null) {
			ItemStack var3;
			if(this.cargoItems[var1].stackSize <= var2) {
				var3 = this.cargoItems[var1];
				this.cargoItems[var1] = null;
				return var3;
			} else {
				var3 = this.cargoItems[var1].splitStack(var2);
				if(this.cargoItems[var1].stackSize == 0) {
					this.cargoItems[var1] = null;
				}

				return var3;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int var1, ItemStack var2) {
		this.cargoItems[var1] = var2;
		if(var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
			var2.stackSize = this.getInventoryStackLimit();
		}

	}

	public String getInvName() {
		return "Minecart";
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public void onInventoryChanged() {}

	public boolean interact(EntityPlayer var1) {
		if(this.minecartType == 0) {
			if(this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != var1) {
				return true;
			}

			if(!this.worldObj.multiplayerWorld) {
				var1.mountEntity(this);
			}
		} else if(this.minecartType == 1) {
			if(!this.worldObj.multiplayerWorld) {
				var1.displayGUIChest(this);
			}
		} else if(this.minecartType == 2) {
			ItemStack var2 = var1.inventory.getCurrentItem();
			if(var2 != null && var2.itemID == Item.coal.shiftedIndex) {
				if(--var2.stackSize == 0) {
					var1.inventory.setInventorySlotContents(var1.inventory.currentItem, (ItemStack)null);
				}

				this.fuel += 3600;
			}

			this.pushX = this.posX - var1.posX;
			this.pushZ = this.posZ - var1.posZ;
		}

		return true;
	}

	public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
		this.minecartX = var1;
		this.minecartY = var3;
		this.minecartZ = var5;
		this.minecartYaw = (double)var7;
		this.minecartPitch = (double)var8;
		this.minecartPosRotationIncrements = var9 + 2;
		this.motionX = this.velocityX;
		this.motionY = this.velocityY;
		this.motionZ = this.velocityZ;
	}

	public void setVelocity(double var1, double var3, double var5) {
		this.velocityX = this.motionX = var1;
		this.velocityY = this.motionY = var3;
		this.velocityZ = this.motionZ = var5;
	}

	public boolean isUseableByPlayer(EntityPlayer var1) {
		return this.isDead?false:var1.getDistanceSqToEntity(this) <= 64.0D;
	}

	protected boolean func_41026_g() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	protected void func_41027_b(boolean var1) {
		if(var1) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(this.dataWatcher.getWatchableObjectByte(16) | 1)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(this.dataWatcher.getWatchableObjectByte(16) & -2)));
		}

	}

	public void openChest() {}

	public void closeChest() {}

	public void func_41024_b(int var1) {
		this.dataWatcher.updateObject(19, Integer.valueOf(var1));
	}

	public int func_41025_i() {
		return this.dataWatcher.getWatchableObjectInt(19);
	}

	public void func_41028_c(int var1) {
		this.dataWatcher.updateObject(17, Integer.valueOf(var1));
	}

	public int func_41023_l() {
		return this.dataWatcher.getWatchableObjectInt(17);
	}

	public void func_41029_h(int var1) {
		this.dataWatcher.updateObject(18, Integer.valueOf(var1));
	}

	public int func_41030_m() {
		return this.dataWatcher.getWatchableObjectInt(18);
	}

}
