// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;

//Spout Start
import org.getspout.spout.entity.EntitySkinType;
//Spout End

// Referenced classes of package net.minecraft.src:
//            EntityAnimal, DataWatcher, NBTTagCompound, World, 
//            EntityPlayer, EntitySheep, AxisAlignedBB, Entity, 
//            InventoryPlayer, ItemStack, Item, ItemFood, 
//            MathHelper, EntityArrow, EntityLiving

public class EntityWolf extends EntityAnimal
{

	public EntityWolf(World world)
	{
		super(world);
		looksWithInterest = false;
		texture = "/mob/wolf.png";
		setSize(0.8F, 0.8F);
		moveSpeed = 1.1F;
		health = 8;
	}

	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte)0));
		dataWatcher.addObject(17, "");
		dataWatcher.addObject(18, new Integer(health));
	}

	protected boolean canTriggerWalking()
	{
		return false;
	}

	public String getEntityTexture()
	{
		//Spout Start
		if(isWolfTamed())
		{

			return EntitySkinType.getTexture(EntitySkinType.WOLF_TAMED, this, "/mob/wolf_tame.png");
		}
		if(isWolfAngry())
		{
			return EntitySkinType.getTexture(EntitySkinType.WOLF_ANGRY, this, "/mob/wolf_angry.png");
		} else
		{
			return super.getEntityTexture();
		}
		//Spout End
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Angry", isWolfAngry());
		nbttagcompound.setBoolean("Sitting", isWolfSitting());
		if(getWolfOwner() == null)
		{
			nbttagcompound.setString("Owner", "");
		} else
		{
			nbttagcompound.setString("Owner", getWolfOwner());
		}
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readEntityFromNBT(nbttagcompound);
		setWolfAngry(nbttagcompound.getBoolean("Angry"));
		setWolfSitting(nbttagcompound.getBoolean("Sitting"));
		String s = nbttagcompound.getString("Owner");
		if(s.length() > 0)
		{
			setWolfOwner(s);
			setWolfTamed(true);
		}
	}

	protected boolean canDespawn()
	{
		return !isWolfTamed();
	}

	protected String getLivingSound()
	{
		if(isWolfAngry())
		{
			return "mob.wolf.growl";
		}
		if(rand.nextInt(3) == 0)
		{
			if(isWolfTamed() && dataWatcher.getWatchableObjectInt(18) < 10)
			{
				return "mob.wolf.whine";
			} else
			{
				return "mob.wolf.panting";
			}
		} else
		{
			return "mob.wolf.bark";
		}
	}

	protected String getHurtSound()
	{
		return "mob.wolf.hurt";
	}

	protected String getDeathSound()
	{
		return "mob.wolf.death";
	}

	protected float getSoundVolume()
	{
		return 0.4F;
	}

	protected int getDropItemId()
	{
		return -1;
	}

	protected void updatePlayerActionState()
	{
		super.updatePlayerActionState();
		if(!hasAttacked && !hasPath() && isWolfTamed() && ridingEntity == null)
		{
			EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getWolfOwner());
			if(entityplayer != null)
			{
				float f = entityplayer.getDistanceToEntity(this);
				if(f > 5F)
				{
					getPathOrWalkableBlock(entityplayer, f);
				}
			} else
				if(!isInWater())
				{
					setWolfSitting(true);
				}
		} else
			if(playerToAttack == null && !hasPath() && !isWolfTamed() && worldObj.rand.nextInt(100) == 0)
			{
				List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntitySheep.class, AxisAlignedBB.getBoundingBoxFromPool(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
				if(!list.isEmpty())
				{
					setTarget((Entity)list.get(worldObj.rand.nextInt(list.size())));
				}
			}
		if(isInWater())
		{
			setWolfSitting(false);
		}
		if(!worldObj.multiplayerWorld)
		{
			dataWatcher.updateObject(18, Integer.valueOf(health));
		}
	}

	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		looksWithInterest = false;
		if(hasCurrentTarget() && !hasPath() && !isWolfAngry())
		{
			Entity entity = getCurrentTarget();
			if(entity instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer)entity;
				ItemStack itemstack = entityplayer.inventory.getCurrentItem();
				if(itemstack != null)
				{
					if(!isWolfTamed() && itemstack.itemID == Item.bone.shiftedIndex)
					{
						looksWithInterest = true;
					} else
						if(isWolfTamed() && (Item.itemsList[itemstack.itemID] instanceof ItemFood))
						{
							looksWithInterest = ((ItemFood)Item.itemsList[itemstack.itemID]).getIsWolfsFavoriteMeat();
						}
				}
			}
		}
		if(!isMultiplayerEntity && isWolfShaking && !field_25052_g && !hasPath() && onGround)
		{
			field_25052_g = true;
			timeWolfIsShaking = 0.0F;
			prevTimeWolfIsShaking = 0.0F;
			worldObj.func_9425_a(this, (byte)8);
		}
	}

	public void onUpdate()
	{
		super.onUpdate();
		field_25054_c = field_25048_b;
		if(looksWithInterest)
		{
			field_25048_b = field_25048_b + (1.0F - field_25048_b) * 0.4F;
		} else
		{
			field_25048_b = field_25048_b + (0.0F - field_25048_b) * 0.4F;
		}
		if(looksWithInterest)
		{
			numTicksToChaseTarget = 10;
		}
		if(isWet())
		{
			isWolfShaking = true;
			field_25052_g = false;
			timeWolfIsShaking = 0.0F;
			prevTimeWolfIsShaking = 0.0F;
		} else
			if((isWolfShaking || field_25052_g) && field_25052_g)
			{
				if(timeWolfIsShaking == 0.0F)
				{
					worldObj.playSoundAtEntity(this, "mob.wolf.shake", getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
				}
				prevTimeWolfIsShaking = timeWolfIsShaking;
				timeWolfIsShaking += 0.05F;
				if(prevTimeWolfIsShaking >= 2.0F)
				{
					isWolfShaking = false;
					field_25052_g = false;
					prevTimeWolfIsShaking = 0.0F;
					timeWolfIsShaking = 0.0F;
				}
				if(timeWolfIsShaking > 0.4F)
				{
					float f = (float)boundingBox.minY;
					int i = (int)(MathHelper.sin((timeWolfIsShaking - 0.4F) * 3.141593F) * 7F);
					for(int j = 0; j < i; j++)
					{
						float f1 = (rand.nextFloat() * 2.0F - 1.0F) * width * 0.5F;
						float f2 = (rand.nextFloat() * 2.0F - 1.0F) * width * 0.5F;
						worldObj.spawnParticle("splash", posX + (double)f1, f + 0.8F, posZ + (double)f2, motionX, motionY, motionZ);
					}

				}
			}
	}

	public boolean getWolfShaking()
	{
		return isWolfShaking;
	}

	public float getShadingWhileShaking(float f)
	{
		return 0.75F + ((prevTimeWolfIsShaking + (timeWolfIsShaking - prevTimeWolfIsShaking) * f) / 2.0F) * 0.25F;
	}

	public float getShakeAngle(float f, float f1)
	{
		float f2 = (prevTimeWolfIsShaking + (timeWolfIsShaking - prevTimeWolfIsShaking) * f + f1) / 1.8F;
		if(f2 < 0.0F)
		{
			f2 = 0.0F;
		} else
			if(f2 > 1.0F)
			{
				f2 = 1.0F;
			}
		return MathHelper.sin(f2 * 3.141593F) * MathHelper.sin(f2 * 3.141593F * 11F) * 0.15F * 3.141593F;
	}

	public float getInterestedAngle(float f)
	{
		return (field_25054_c + (field_25048_b - field_25054_c) * f) * 0.15F * 3.141593F;
	}

	public float getEyeHeight()
	{
		return height * 0.8F;
	}

	protected int func_25026_x()
	{
		if(isWolfSitting())
		{
			return 20;
		} else
		{
			return super.func_25026_x();
		}
	}

	private void getPathOrWalkableBlock(Entity entity, float f)
	{
		PathEntity pathentity = worldObj.getPathToEntity(this, entity, 16F);
		if(pathentity == null && f > 12F)
		{
			int i = MathHelper.floor_double(entity.posX) - 2;
			int j = MathHelper.floor_double(entity.posZ) - 2;
			int k = MathHelper.floor_double(entity.boundingBox.minY);
			for(int l = 0; l <= 4; l++)
			{
				for(int i1 = 0; i1 <= 4; i1++)
				{
					if((l < 1 || i1 < 1 || l > 3 || i1 > 3) && worldObj.isBlockNormalCube(i + l, k - 1, j + i1) && !worldObj.isBlockNormalCube(i + l, k, j + i1) && !worldObj.isBlockNormalCube(i + l, k + 1, j + i1))
					{
						setLocationAndAngles((float)(i + l) + 0.5F, k, (float)(j + i1) + 0.5F, rotationYaw, rotationPitch);
						return;
					}
				}

			}

		} else
		{
			setPathToEntity(pathentity);
		}
	}

	protected boolean isMovementCeased()
	{
		return isWolfSitting() || field_25052_g;
	}

	public boolean attackEntityFrom(Entity entity, int i)
	{
		setWolfSitting(false);
		if(entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
		{
			i = (i + 1) / 2;
		}
		if(super.attackEntityFrom(entity, i))
		{
			if(!isWolfTamed() && !isWolfAngry())
			{
				if(entity instanceof EntityPlayer)
				{
					setWolfAngry(true);
					playerToAttack = entity;
				}
				if((entity instanceof EntityArrow) && ((EntityArrow)entity).owner != null)
				{
					entity = ((EntityArrow)entity).owner;
				}
				if(entity instanceof EntityLiving)
				{
					List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityWolf.class, AxisAlignedBB.getBoundingBoxFromPool(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
					Iterator iterator = list.iterator();
					do
					{
						if(!iterator.hasNext())
						{
							break;
						}
						Entity entity1 = (Entity)iterator.next();
						EntityWolf entitywolf = (EntityWolf)entity1;
						if(!entitywolf.isWolfTamed() && entitywolf.playerToAttack == null)
						{
							entitywolf.playerToAttack = entity;
							if(entity instanceof EntityPlayer)
							{
								entitywolf.setWolfAngry(true);
							}
						}
					} while(true);
				}
			} else
				if(entity != this && entity != null)
				{
					if(isWolfTamed() && (entity instanceof EntityPlayer) && ((EntityPlayer)entity).username.equalsIgnoreCase(getWolfOwner()))
					{
						return true;
					}
					playerToAttack = entity;
				}
			return true;
		} else
		{
			return false;
		}
	}

	protected Entity findPlayerToAttack()
	{
		if(isWolfAngry())
		{
			return worldObj.getClosestPlayerToEntity(this, 16D);
		} else
		{
			return null;
		}
	}

	protected void attackEntity(Entity entity, float f)
	{
		if(f > 2.0F && f < 6F && rand.nextInt(10) == 0)
		{
			if(onGround)
			{
				double d = entity.posX - posX;
				double d1 = entity.posZ - posZ;
				float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
				motionX = (d / (double)f1) * 0.5D * 0.80000001192092896D + motionX * 0.20000000298023224D;
				motionZ = (d1 / (double)f1) * 0.5D * 0.80000001192092896D + motionZ * 0.20000000298023224D;
				motionY = 0.40000000596046448D;
			}
		} else
			if((double)f < 1.5D && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
			{
				attackTime = 20;
				byte byte0 = 2;
				if(isWolfTamed())
				{
					byte0 = 4;
				}
				entity.attackEntityFrom(this, byte0);
			}
	}

	public boolean interact(EntityPlayer entityplayer)
	{
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if(!isWolfTamed())
		{
			if(itemstack != null && itemstack.itemID == Item.bone.shiftedIndex && !isWolfAngry())
			{
				itemstack.stackSize--;
				if(itemstack.stackSize <= 0)
				{
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
				}
				if(!worldObj.multiplayerWorld)
				{
					if(rand.nextInt(3) == 0)
					{
						setWolfTamed(true);
						setPathToEntity(null);
						setWolfSitting(true);
						health = 20;
						setWolfOwner(entityplayer.username);
						showHeartsOrSmokeFX(true);
						worldObj.func_9425_a(this, (byte)7);
					} else
					{
						showHeartsOrSmokeFX(false);
						worldObj.func_9425_a(this, (byte)6);
					}
				}
				return true;
			}
		} else
		{
			if(itemstack != null && (Item.itemsList[itemstack.itemID] instanceof ItemFood))
			{
				ItemFood itemfood = (ItemFood)Item.itemsList[itemstack.itemID];
				if(itemfood.getIsWolfsFavoriteMeat() && dataWatcher.getWatchableObjectInt(18) < 20)
				{
					itemstack.stackSize--;
					if(itemstack.stackSize <= 0)
					{
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
					}
					heal(((ItemFood)Item.porkRaw).getHealAmount());
					return true;
				}
			}
			if(entityplayer.username.equalsIgnoreCase(getWolfOwner()))
			{
				if(!worldObj.multiplayerWorld)
				{
					setWolfSitting(!isWolfSitting());
					isJumping = false;
					setPathToEntity(null);
				}
				return true;
			}
		}
		return false;
	}

	void showHeartsOrSmokeFX(boolean flag)
	{
		String s = "heart";
		if(!flag)
		{
			s = "smoke";
		}
		for(int i = 0; i < 7; i++)
		{
			double d = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			double d2 = rand.nextGaussian() * 0.02D;
			worldObj.spawnParticle(s, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
		}

	}

	public void handleHealthUpdate(byte byte0)
	{
		if(byte0 == 7)
		{
			showHeartsOrSmokeFX(true);
		} else
			if(byte0 == 6)
			{
				showHeartsOrSmokeFX(false);
			} else
				if(byte0 == 8)
				{
					field_25052_g = true;
					timeWolfIsShaking = 0.0F;
					prevTimeWolfIsShaking = 0.0F;
				} else
				{
					super.handleHealthUpdate(byte0);
				}
	}

	public float setTailRotation()
	{
		if(isWolfAngry())
		{
			return 1.53938F;
		}
		if(isWolfTamed())
		{
			return (0.55F - (float)(20 - dataWatcher.getWatchableObjectInt(18)) * 0.02F) * 3.141593F;
		} else
		{
			return 0.6283185F;
		}
	}

	public int getMaxSpawnedInChunk()
	{
		return 8;
	}

	public String getWolfOwner()
	{
		return dataWatcher.getWatchableObjectString(17);
	}

	public void setWolfOwner(String s)
	{
		dataWatcher.updateObject(17, s);
	}

	public boolean isWolfSitting()
	{
		return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setWolfSitting(boolean flag)
	{
		byte byte0 = dataWatcher.getWatchableObjectByte(16);
		if(flag)
		{
			dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 1)));
		} else
		{
			dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -2)));
		}
	}

	public boolean isWolfAngry()
	{
		return (dataWatcher.getWatchableObjectByte(16) & 2) != 0;
	}

	public void setWolfAngry(boolean flag)
	{
		byte byte0 = dataWatcher.getWatchableObjectByte(16);
		if(flag)
		{
			dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 2)));
		} else
		{
			dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -3)));
		}
	}

	public boolean isWolfTamed()
	{
		return (dataWatcher.getWatchableObjectByte(16) & 4) != 0;
	}

	public void setWolfTamed(boolean flag)
	{
		byte byte0 = dataWatcher.getWatchableObjectByte(16);
		if(flag)
		{
			dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 4)));
		} else
		{
			dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -5)));
		}
	}

	private boolean looksWithInterest;
	private float field_25048_b;
	private float field_25054_c;
	private boolean isWolfShaking;
	private boolean field_25052_g;
	private float timeWolfIsShaking;
	private float prevTimeWolfIsShaking;
}
