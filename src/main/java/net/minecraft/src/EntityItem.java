package net.minecraft.src;

import java.util.Iterator;

//Spout start
import org.spoutcraft.client.entity.CraftItem;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
//Spout end

public class EntityItem extends Entity {

	/** The item stack of this EntityItem. */
	public ItemStack item;

	/**
	 * The age of this EntityItem (used to animate it up and down as well as expire it)
	 */
	public int age = 0;
	public int delayBeforeCanPickup;

	/** The health of this EntityItem. (For example, damage for tools) */
	private int health = 5;

	/** The EntityItem's random initial float height. */
	public float hoverStart = (float)(Math.random() * Math.PI * 2.0D);

	public EntityItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack) {
		super(par1World);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(par2, par4, par6);
		this.item = par8ItemStack;
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.motionX = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
		this.motionY = 0.20000000298023224D;
		this.motionZ = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
		//Spout start
		this.spoutEntity = new CraftItem(this);
		//Spout end
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	protected boolean canTriggerWalking() {
		return false;
	}

	public EntityItem(World par1World) {
		super(par1World);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
	}

	protected void entityInit() {}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();

		if (this.delayBeforeCanPickup > 0) {
			--this.delayBeforeCanPickup;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= 0.03999999910593033D;
		this.pushOutOfBlocks(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		boolean var1 = (int)this.prevPosX != (int)this.posX || (int)this.prevPosY != (int)this.posY || (int)this.prevPosZ != (int)this.posZ;

		if (var1) {
			if (this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) == Material.lava) {
				this.motionY = 0.20000000298023224D;
				this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
				this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
				this.worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
			}

			if (!this.worldObj.isRemote) {
				Iterator var2 = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(0.5D, 0.0D, 0.5D)).iterator();

				while (var2.hasNext()) {
					EntityItem var3 = (EntityItem)var2.next();
					this.func_70289_a(var3);
				}
			}
		}

		float var4 = 0.98F;

		if (this.onGround) {
			var4 = 0.58800006F;
			int var5 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

			if (var5 > 0) {
				var4 = Block.blocksList[var5].slipperiness * 0.98F;
				//Spout start
				if (!worldObj.isRemote) {
					int x = MathHelper.floor_double(this.posX);
					int y = MathHelper.floor_double(this.boundingBox.minY) - 1;
					int z = MathHelper.floor_double(this.posZ);
					short customId = worldObj.world.getChunkAt(x, y, z).getCustomBlockId(x, y, z);
					if (customId > 0) {
						CustomBlock block = MaterialData.getCustomBlock(customId);
						if (block != null) {
							var4 = block.getFriction() * 0.98F;
						}
					}
				}
				//Spout end
			}
		}

		this.motionX *= (double)var4;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= (double)var4;

		if (this.onGround) {
			this.motionY *= -0.5D;
		}

		++this.age;

		if (this.age >= 6000) {
			this.setDead();
		}
	}

	public boolean func_70289_a(EntityItem par1EntityItem) {
		if (par1EntityItem == this) {
			return false;
		} else if (par1EntityItem.isEntityAlive() && this.isEntityAlive()) {
			if (par1EntityItem.item.getItem() != this.item.getItem()) {
				return false;
			} else if (par1EntityItem.item.getItem().getHasSubtypes() && par1EntityItem.item.getItemDamage() != this.item.getItemDamage()) {
				return false;
			} else if (par1EntityItem.item.stackSize < this.item.stackSize) {
				return par1EntityItem.func_70289_a(this);
			} else if (par1EntityItem.item.stackSize + this.item.stackSize > par1EntityItem.item.getMaxStackSize()) {
				return false;
			} else {
				par1EntityItem.item.stackSize += this.item.stackSize;
				par1EntityItem.delayBeforeCanPickup = Math.max(par1EntityItem.delayBeforeCanPickup, this.delayBeforeCanPickup);
				par1EntityItem.age = Math.min(par1EntityItem.age, this.age);
				this.setDead();
				return true;
			}
		} else {
			return false;
		}
	}

	public void func_70288_d() {
		this.age = 4800;
	}

	/**
	 * Returns if this entity is in water and will end up adding the waters velocity to the entity
	 */
	public boolean handleWaterMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this);
	}

	/**
	 * Will deal the specified amount of damage to the entity if the entity isn't immune to fire damage. Args: amountDamage
	 */
	protected void dealFireDamage(int par1) {
		this.attackEntityFrom(DamageSource.inFire, par1);
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		this.setBeenAttacked();
		this.health -= par2;

		if (this.health <= 0) {
			this.setDead();
		}

		return false;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setShort("Health", (short)((byte)this.health));
		par1NBTTagCompound.setShort("Age", (short)this.age);

		if (this.item != null) {
			par1NBTTagCompound.setCompoundTag("Item", this.item.writeToNBT(new NBTTagCompound()));
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		this.health = par1NBTTagCompound.getShort("Health") & 255;
		this.age = par1NBTTagCompound.getShort("Age");
		NBTTagCompound var2 = par1NBTTagCompound.getCompoundTag("Item");
		this.item = ItemStack.loadItemStackFromNBT(var2);

		if (this.item == null) {
			this.setDead();
		}
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
		if (!this.worldObj.isRemote) {
			int var2 = this.item.stackSize;

			if (this.delayBeforeCanPickup == 0 && par1EntityPlayer.inventory.addItemStackToInventory(this.item)) {
				if (this.item.itemID == Block.wood.blockID) {
					par1EntityPlayer.triggerAchievement(AchievementList.mineWood);
				}

				if (this.item.itemID == Item.leather.shiftedIndex) {
					par1EntityPlayer.triggerAchievement(AchievementList.killCow);
				}

				if (this.item.itemID == Item.diamond.shiftedIndex) {
					par1EntityPlayer.triggerAchievement(AchievementList.diamonds);
				}

				if (this.item.itemID == Item.blazeRod.shiftedIndex) {
					par1EntityPlayer.triggerAchievement(AchievementList.blazeRod);
				}

				this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				par1EntityPlayer.onItemPickup(this, var2);

				if (this.item.stackSize <= 0) {
					this.setDead();
				}
			}
		}
	}

	/**
	 * Gets the username of the entity.
	 */
	public String getEntityName() {
		return StatCollector.translateToLocal("item." + this.item.func_77977_a());
	}

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 */
	public boolean canAttackWithItem() {
		return false;
	}
}
