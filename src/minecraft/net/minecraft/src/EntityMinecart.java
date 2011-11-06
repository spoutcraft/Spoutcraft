package net.minecraft.src;

import java.util.List;

import org.getspout.spout.entity.CraftCaveSpider;
import org.getspout.spout.entity.CraftMinecart;
import org.getspout.spout.entity.CraftPoweredMinecart;
import org.getspout.spout.entity.CraftStorageMinecart;

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
	public int minecartCurrentDamage;
	public int minecartTimeSinceHit;
	public int minecartRockDirection;
	private boolean field_856_i;
	public int minecartType;
	public int fuel;
	public double pushX;
	public double pushZ;
	private static final int[][][] field_855_j = new int[][][]{{{0, 0, -1}, {0, 0, 1}}, {{-1, 0, 0}, {1, 0, 0}}, {{-1, -1, 0}, {1, 0, 0}}, {{-1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, {-1, 0, 0}}, {{0, 0, -1}, {-1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
	private int field_9415_k;
	private double field_9414_l;
	private double field_9413_m;
	private double field_9412_n;
	private double field_9411_o;
	private double field_9410_p;
	private double field_9409_q;
	private double field_9408_r;
	private double field_9407_s;

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

	private EntityMinecart(World var1) { //Spout public -> private
		super(var1);
		this.cargoItems = new ItemStack[27]; //Spout 36 -> 27
		this.minecartCurrentDamage = 0;
		this.minecartTimeSinceHit = 0;
		this.minecartRockDirection = 1;
		this.field_856_i = false;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.7F);
		this.yOffset = this.height / 2.0F;
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {}

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
			this.minecartRockDirection = -this.minecartRockDirection;
			this.minecartTimeSinceHit = 10;
			this.setBeenAttacked();
			this.minecartCurrentDamage += var2 * 10;
			if(this.minecartCurrentDamage > 40) {
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
		System.out.println("Animating hurt");
		this.minecartRockDirection = -this.minecartRockDirection;
		this.minecartTimeSinceHit = 10;
		this.minecartCurrentDamage += this.minecartCurrentDamage * 10;
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
		if(this.minecartTimeSinceHit > 0) {
			--this.minecartTimeSinceHit;
		}

		if(this.minecartCurrentDamage > 0) {
			--this.minecartCurrentDamage;
		}

		double var7;
		if(this.worldObj.multiplayerWorld && this.field_9415_k > 0) {
			if(this.field_9415_k > 0) {
				double var46 = this.posX + (this.field_9414_l - this.posX) / (double)this.field_9415_k;
				double var47 = this.posY + (this.field_9413_m - this.posY) / (double)this.field_9415_k;
				double var5 = this.posZ + (this.field_9412_n - this.posZ) / (double)this.field_9415_k;

				for(var7 = this.field_9411_o - (double)this.rotationYaw; var7 < -180.0D; var7 += 360.0D) {
					;
				}

				while(var7 >= 180.0D) {
					var7 -= 360.0D;
				}

				this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.field_9415_k);
				this.rotationPitch = (float)((double)this.rotationPitch + (this.field_9410_p - (double)this.rotationPitch) / (double)this.field_9415_k);
				--this.field_9415_k;
				this.setPosition(var46, var47, var5);
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

			double var4 = this.maxSpeed; //Spout
			boolean var6 = false;
			var7 = 0.0078125D;
			int var9 = this.worldObj.getBlockId(var1, var2, var3);
			if(BlockRail.isRailBlock(var9)) {
				Vec3D var10 = this.func_514_g(this.posX, this.posY, this.posZ);
				int var11 = this.worldObj.getBlockMetadata(var1, var2, var3);
				this.posY = (double)var2;
				boolean var12 = false;
				boolean var13 = false;
				if(var9 == Block.railPowered.blockID) {
					var12 = (var11 & 8) != 0;
					var13 = !var12;
				}

				if(((BlockRail)Block.blocksList[var9]).getIsPowered()) {
					var11 &= 7;
				}

				if(var11 >= 2 && var11 <= 5) {
					this.posY = (double)(var2 + 1);
				}

				if(var11 == 2) {
					this.motionX -= var7;
				}

				if(var11 == 3) {
					this.motionX += var7;
				}

				if(var11 == 4) {
					this.motionZ += var7;
				}

				if(var11 == 5) {
					this.motionZ -= var7;
				}

				int[][] var14 = field_855_j[var11];
				double var15 = (double)(var14[1][0] - var14[0][0]);
				double var17 = (double)(var14[1][2] - var14[0][2]);
				double var19 = Math.sqrt(var15 * var15 + var17 * var17);
				double var21 = this.motionX * var15 + this.motionZ * var17;
				if(var21 < 0.0D) {
					var15 = -var15;
					var17 = -var17;
				}

				double var23 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
				this.motionX = var23 * var15 / var19;
				this.motionZ = var23 * var17 / var19;
				double var25;
				if(var13) {
					var25 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
					if(var25 < 0.03D) {
						this.motionX *= 0.0D;
						this.motionY *= 0.0D;
						this.motionZ *= 0.0D;
					} else {
						this.motionX *= 0.5D;
						this.motionY *= 0.0D;
						this.motionZ *= 0.5D;
					}
				}

				var25 = 0.0D;
				double var27 = (double)var1 + 0.5D + (double)var14[0][0] * 0.5D;
				double var29 = (double)var3 + 0.5D + (double)var14[0][2] * 0.5D;
				double var31 = (double)var1 + 0.5D + (double)var14[1][0] * 0.5D;
				double var33 = (double)var3 + 0.5D + (double)var14[1][2] * 0.5D;
				var15 = var31 - var27;
				var17 = var33 - var29;
				double var35;
				double var39;
				double var37;
				if(var15 == 0.0D) {
					this.posX = (double)var1 + 0.5D;
					var25 = this.posZ - (double)var3;
				} else if(var17 == 0.0D) {
					this.posZ = (double)var3 + 0.5D;
					var25 = this.posX - (double)var1;
				} else {
					var35 = this.posX - var27;
					var37 = this.posZ - var29;
					var39 = (var35 * var15 + var37 * var17) * 2.0D;
					var25 = var39;
				}

				this.posX = var27 + var15 * var25;
				this.posZ = var29 + var17 * var25;
				this.setPosition(this.posX, this.posY + (double)this.yOffset, this.posZ);
				var35 = this.motionX;
				var37 = this.motionZ;
				if(this.riddenByEntity != null) {
					var35 *= 0.75D;
					var37 *= 0.75D;
				}

				if(var35 < -var4) {
					var35 = -var4;
				}

				if(var35 > var4) {
					var35 = var4;
				}

				if(var37 < -var4) {
					var37 = -var4;
				}

				if(var37 > var4) {
					var37 = var4;
				}

				this.moveEntity(var35, 0.0D, var37);
				if(var14[0][1] != 0 && MathHelper.floor_double(this.posX) - var1 == var14[0][0] && MathHelper.floor_double(this.posZ) - var3 == var14[0][2]) {
					this.setPosition(this.posX, this.posY + (double)var14[0][1], this.posZ);
				} else if(var14[1][1] != 0 && MathHelper.floor_double(this.posX) - var1 == var14[1][0] && MathHelper.floor_double(this.posZ) - var3 == var14[1][2]) {
					this.setPosition(this.posX, this.posY + (double)var14[1][1], this.posZ);
				}

				if (this.riddenByEntity != null || !this.slowWhenEmpty) { //Spout
					this.motionX *= 0.996999979019165D;
					this.motionY *= 0.0D;
					this.motionZ *= 0.996999979019165D;
				} else {
					if(this.minecartType == 2) {
						var39 = (double)MathHelper.sqrt_double(this.pushX * this.pushX + this.pushZ * this.pushZ);
						if(var39 > 0.01D) {
							var6 = true;
							this.pushX /= var39;
							this.pushZ /= var39;
							double var41 = 0.04D;
							this.motionX *= 0.800000011920929D;
							this.motionY *= 0.0D;
							this.motionZ *= 0.800000011920929D;
							this.motionX += this.pushX * var41;
							this.motionZ += this.pushZ * var41;
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

				Vec3D var54 = this.func_514_g(this.posX, this.posY, this.posZ);
				if(var54 != null && var10 != null) {
					double var40 = (var10.yCoord - var54.yCoord) * 0.05D;
					var23 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
					if(var23 > 0.0D) {
						this.motionX = this.motionX / var23 * (var23 + var40);
						this.motionZ = this.motionZ / var23 * (var23 + var40);
					}

					this.setPosition(this.posX, var54.yCoord, this.posZ);
				}

				int var53 = MathHelper.floor_double(this.posX);
				int var52 = MathHelper.floor_double(this.posZ);
				if(var53 != var1 || var52 != var3) {
					var23 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
					this.motionX = var23 * (double)(var53 - var1);
					this.motionZ = var23 * (double)(var52 - var3);
				}

				double var42;
				if(this.minecartType == 2) {
					var42 = (double)MathHelper.sqrt_double(this.pushX * this.pushX + this.pushZ * this.pushZ);
					if(var42 > 0.01D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.0010D) {
						this.pushX /= var42;
						this.pushZ /= var42;
						if(this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0D) {
							this.pushX = 0.0D;
							this.pushZ = 0.0D;
						} else {
							this.pushX = this.motionX;
							this.pushZ = this.motionZ;
						}
					}
				}

				if(var12) {
					var42 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
					if(var42 > 0.01D) {
						double var44 = 0.06D;
						this.motionX += this.motionX / var42 * var44;
						this.motionZ += this.motionZ / var42 * var44;
					} else if(var11 == 1) {
						if(this.worldObj.isBlockNormalCube(var1 - 1, var2, var3)) {
							this.motionX = 0.02D;
						} else if(this.worldObj.isBlockNormalCube(var1 + 1, var2, var3)) {
							this.motionX = -0.02D;
						}
					} else if(var11 == 0) {
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
					//Spout start
					this.motionX *= this.derailedX;
					this.motionY *= this.derailedY;
					this.motionZ *= this.derailedZ;
					//Spout end
				}

				this.moveEntity(this.motionX, this.motionY, this.motionZ);
				if(!this.onGround) {
					//Spout start
					this.motionX *= flyingX;
					this.motionY *= flyingY;
					this.motionZ *= flyingZ;
					//Spout end
				}
			}

			this.rotationPitch = 0.0F;
			double var48 = this.prevPosX - this.posX;
			double var49 = this.prevPosZ - this.posZ;
			if(var48 * var48 + var49 * var49 > 0.0010D) {
				this.rotationYaw = (float)(Math.atan2(var49, var48) * 180.0D / 3.141592653589793D);
				if(this.field_856_i) {
					this.rotationYaw += 180.0F;
				}
			}

			double var50;
			for(var50 = (double)(this.rotationYaw - this.prevRotationYaw); var50 >= 180.0D; var50 -= 360.0D) {
				;
			}

			while(var50 < -180.0D) {
				var50 += 360.0D;
			}

			if(var50 < -170.0D || var50 >= 170.0D) {
				this.rotationYaw += 180.0F;
				this.field_856_i = !this.field_856_i;
			}

			this.setRotation(this.rotationYaw, this.rotationPitch);
			List var16 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
			if(var16 != null && var16.size() > 0) {
				for(int var51 = 0; var51 < var16.size(); ++var51) {
					Entity var18 = (Entity)var16.get(var51);
					if(var18 != this.riddenByEntity && var18.canBePushed() && var18 instanceof EntityMinecart) {
						var18.applyEntityCollision(this);
					}
				}
			}

			if(this.riddenByEntity != null && this.riddenByEntity.isDead) {
				//Spout start
				riddenByEntity.ridingEntity = null;
				//Spout end
				this.riddenByEntity = null;
			}

			if(var6 && this.rand.nextInt(4) == 0) {
				--this.fuel;
				if(this.fuel < 0) {
					this.pushX = this.pushZ = 0.0D;
				}

				this.worldObj.spawnParticle("largesmoke", this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D);
			}

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
					this.cargoItems[var5] = ItemStack.func_35864_a(var4);
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
						double var14 = var10 * var1.motionZ + var12 * var1.prevPosX;
						var14 *= var14;
						if(var14 > 5.0D) {
							return;
						}

						double var16 = var1.motionX + this.motionX;
						double var18 = var1.motionZ + this.motionZ;
						if(((EntityMinecart)var1).minecartType == 2 && this.minecartType != 2) {
							this.motionX *= 0.20000000298023224D;
							this.motionZ *= 0.20000000298023224D;
							this.addVelocity(var1.motionX - var2, 0.0D, var1.motionZ - var4);
							var1.motionX *= 0.699999988079071D;
							var1.motionZ *= 0.699999988079071D;
						} else if(((EntityMinecart)var1).minecartType != 2 && this.minecartType == 2) {
							var1.motionX *= 0.20000000298023224D;
							var1.motionZ *= 0.20000000298023224D;
							var1.addVelocity(this.motionX + var2, 0.0D, this.motionZ + var4);
							this.motionX *= 0.699999988079071D;
							this.motionZ *= 0.699999988079071D;
						} else {
							var16 /= 2.0D;
							var18 /= 2.0D;
							this.motionX *= 0.20000000298023224D;
							this.motionZ *= 0.20000000298023224D;
							this.addVelocity(var16 - var2, 0.0D, var18 - var4);
							var1.motionX *= 0.20000000298023224D;
							var1.motionZ *= 0.20000000298023224D;
							var1.addVelocity(var16 + var2, 0.0D, var18 + var4);
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

				this.fuel += 1200;
			}

			this.pushX = this.posX - var1.posX;
			this.pushZ = this.posZ - var1.posZ;
		}

		return true;
	}

	public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
		this.field_9414_l = var1;
		this.field_9413_m = var3;
		this.field_9412_n = var5;
		this.field_9411_o = (double)var7;
		this.field_9410_p = (double)var8;
		this.field_9415_k = var9 + 2;
		this.motionX = this.field_9409_q;
		this.motionY = this.field_9408_r;
		this.motionZ = this.field_9407_s;
	}

	public void setVelocity(double var1, double var3, double var5) {
		this.field_9409_q = this.motionX = var1;
		this.field_9408_r = this.motionY = var3;
		this.field_9407_s = this.motionZ = var5;
	}

	public boolean canInteractWith(EntityPlayer var1) {
		return this.isDead?false:var1.getDistanceSqToEntity(this) <= 64.0D;
	}

	public void openChest() {}

	public void closeChest() {}

}
