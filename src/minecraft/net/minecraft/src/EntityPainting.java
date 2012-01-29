package net.minecraft.src;

import java.util.*;

public class EntityPainting extends Entity {
	private int tickCounter1;
	public int direction;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public EnumArt art;

	public EntityPainting(World world) {
		super(world);
		tickCounter1 = 0;
		direction = 0;
		yOffset = 0.0F;
		setSize(0.5F, 0.5F);
	}

	public EntityPainting(World world, int i, int j, int k, int l) {
		this(world);
		xPosition = i;
		yPosition = j;
		zPosition = k;
		ArrayList arraylist = new ArrayList();
		EnumArt aenumart[] = EnumArt.values();
		int i1 = aenumart.length;
		for (int j1 = 0; j1 < i1; j1++) {
			EnumArt enumart = aenumart[j1];
			art = enumart;
			func_412_b(l);
			if (canStay()) {
				arraylist.add(enumart);
			}
		}

		if (arraylist.size() > 0) {
			art = (EnumArt)arraylist.get(rand.nextInt(arraylist.size()));
		}
		func_412_b(l);
	}

	public EntityPainting(World world, int i, int j, int k, int l, String s) {
		this(world);
		xPosition = i;
		yPosition = j;
		zPosition = k;
		EnumArt aenumart[] = EnumArt.values();
		int i1 = aenumart.length;
		int j1 = 0;
		do {
			if (j1 >= i1) {
				break;
			}
			EnumArt enumart = aenumart[j1];
			if (enumart.title.equals(s)) {
				art = enumart;
				break;
			}
			j1++;
		}
		while (true);
		func_412_b(l);
	}

	protected void entityInit() {
	}

	public void func_412_b(int i) {
		direction = i;
		prevRotationYaw = rotationYaw = i * 90;
		float f = art.sizeX;
		float f1 = art.sizeY;
		float f2 = art.sizeX;
		if (i == 0 || i == 2) {
			f2 = 0.5F;
		}
		else {
			f = 0.5F;
		}
		f /= 32F;
		f1 /= 32F;
		f2 /= 32F;
		float f3 = (float)xPosition + 0.5F;
		float f4 = (float)yPosition + 0.5F;
		float f5 = (float)zPosition + 0.5F;
		float f6 = 0.5625F;
		if (i == 0) {
			f5 -= f6;
		}
		if (i == 1) {
			f3 -= f6;
		}
		if (i == 2) {
			f5 += f6;
		}
		if (i == 3) {
			f3 += f6;
		}
		if (i == 0) {
			f3 -= func_411_c(art.sizeX);
		}
		if (i == 1) {
			f5 += func_411_c(art.sizeX);
		}
		if (i == 2) {
			f3 += func_411_c(art.sizeX);
		}
		if (i == 3) {
			f5 -= func_411_c(art.sizeX);
		}
		f4 += func_411_c(art.sizeY);
		setPosition(f3, f4, f5);
		float f7 = -0.00625F;
		boundingBox.setBounds(f3 - f - f7, f4 - f1 - f7, f5 - f2 - f7, f3 + f + f7, f4 + f1 + f7, f5 + f2 + f7);
	}

	private float func_411_c(int i) {
		if (i == 32) {
			return 0.5F;
		}
		return i != 64 ? 0.0F : 0.5F;
	}

	public void onUpdate() {
		if (tickCounter1++ == 100 && !worldObj.multiplayerWorld) {
			tickCounter1 = 0;
			if (!canStay()) {
				setEntityDead();
				worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.painting)));
			}
		}
	}

	public boolean canStay() {
		if (worldObj.getCollidingBoundingBoxes(this, boundingBox).size() > 0) {
			return false;
		}
		int i = art.sizeX / 16;
		int j = art.sizeY / 16;
		int k = xPosition;
		int l = yPosition;
		int i1 = zPosition;
		if (direction == 0) {
			k = MathHelper.floor_double(posX - (double)((float)art.sizeX / 32F));
		}
		if (direction == 1) {
			i1 = MathHelper.floor_double(posZ - (double)((float)art.sizeX / 32F));
		}
		if (direction == 2) {
			k = MathHelper.floor_double(posX - (double)((float)art.sizeX / 32F));
		}
		if (direction == 3) {
			i1 = MathHelper.floor_double(posZ - (double)((float)art.sizeX / 32F));
		}
		l = MathHelper.floor_double(posY - (double)((float)art.sizeY / 32F));
		for (int j1 = 0; j1 < i; j1++) {
			for (int k1 = 0; k1 < j; k1++) {
				Material material;
				if (direction == 0 || direction == 2) {
					material = worldObj.getBlockMaterial(k + j1, l + k1, zPosition);
				}
				else {
					material = worldObj.getBlockMaterial(xPosition, l + k1, i1 + j1);
				}
				if (!material.isSolid()) {
					return false;
				}
			}
		}

		List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox);
		for (int l1 = 0; l1 < list.size(); l1++) {
			if (list.get(l1) instanceof EntityPainting) {
				return false;
			}
		}

		return true;
	}

	public boolean canBeCollidedWith() {
		return true;
	}

	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		if (!isDead && !worldObj.multiplayerWorld) {
			setEntityDead();
			setBeenAttacked();
			worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.painting)));
		}
		return true;
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setByte("Dir", (byte)direction);
		nbttagcompound.setString("Motive", art.title);
		nbttagcompound.setInteger("TileX", xPosition);
		nbttagcompound.setInteger("TileY", yPosition);
		nbttagcompound.setInteger("TileZ", zPosition);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		direction = nbttagcompound.getByte("Dir");
		xPosition = nbttagcompound.getInteger("TileX");
		yPosition = nbttagcompound.getInteger("TileY");
		zPosition = nbttagcompound.getInteger("TileZ");
		String s = nbttagcompound.getString("Motive");
		EnumArt aenumart[] = EnumArt.values();
		int i = aenumart.length;
		for (int j = 0; j < i; j++) {
			EnumArt enumart = aenumart[j];
			if (enumart.title.equals(s)) {
				art = enumart;
			}
		}

		if (art == null) {
			art = EnumArt.Kebab;
		}
		func_412_b(direction);
	}

	public void moveEntity(double d, double d1, double d2) {
		if (!worldObj.multiplayerWorld && d * d + d1 * d1 + d2 * d2 > 0.0D) {
			setEntityDead();
			worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.painting)));
		}
	}

	public void addVelocity(double d, double d1, double d2) {
		if (!worldObj.multiplayerWorld && d * d + d1 * d1 + d2 * d2 > 0.0D) {
			setEntityDead();
			worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.painting)));
		}
	}
}
