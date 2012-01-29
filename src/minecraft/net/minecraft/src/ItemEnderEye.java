package net.minecraft.src;

import java.util.Random;

public class ItemEnderEye extends Item {
	public ItemEnderEye(int i) {
		super(i);
	}

	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
		int i1 = world.getBlockId(i, j, k);
		int j1 = world.getBlockMetadata(i, j, k);
		if (entityplayer.canPlayerEdit(i, j, k) && i1 == Block.endPortalFrame.blockID && !BlockEndPortalFrame.isEnderEyeInserted(j1)) {
			if (world.multiplayerWorld) {
				return true;
			}
			world.setBlockMetadataWithNotify(i, j, k, j1 + 4);
			itemstack.stackSize--;
			for (int k1 = 0; k1 < 16; k1++) {
				double d = (float)i + (5F + itemRand.nextFloat() * 6F) / 16F;
				double d1 = (float)j + 0.8125F;
				double d2 = (float)k + (5F + itemRand.nextFloat() * 6F) / 16F;
				double d3 = 0.0D;
				double d4 = 0.0D;
				double d5 = 0.0D;
				world.spawnParticle("smoke", d, d1, d2, d3, d4, d5);
			}

			int l1 = j1 & 3;
			int i2 = 0;
			int j2 = 0;
			boolean flag = false;
			boolean flag1 = true;
			int k2 = Direction.field_35867_f[l1];
			for (int l2 = -2; l2 <= 2; l2++) {
				int l3 = i + Direction.field_35871_a[k2] * l2;
				int l4 = k + Direction.field_35870_b[k2] * l2;
				int l5 = world.getBlockId(l3, j, l4);
				if (l5 != Block.endPortalFrame.blockID) {
					continue;
				}
				int l6 = world.getBlockMetadata(l3, j, l4);
				if (!BlockEndPortalFrame.isEnderEyeInserted(l6)) {
					flag1 = false;
					break;
				}
				if (!flag) {
					i2 = l2;
					j2 = l2;
					flag = true;
				}
				else {
					j2 = l2;
				}
			}

			if (flag1 && j2 == i2 + 2) {
				int i3 = i2;
				do {
					if (i3 > j2) {
						break;
					}
					int i4 = i + Direction.field_35871_a[k2] * i3;
					int i5 = k + Direction.field_35870_b[k2] * i3;
					i4 += Direction.field_35871_a[l1] * 4;
					i5 += Direction.field_35870_b[l1] * 4;
					int i6 = world.getBlockId(i4, j, i5);
					int i7 = world.getBlockMetadata(i4, j, i5);
					if (i6 != Block.endPortalFrame.blockID || !BlockEndPortalFrame.isEnderEyeInserted(i7)) {
						flag1 = false;
						break;
					}
					i3++;
				}
				while (true);
				label0:
				for (int j3 = i2 - 1; j3 <= j2 + 1; j3 += 4) {
					int j4 = 1;
					do {
						if (j4 > 3) {
							continue label0;
						}
						int j5 = i + Direction.field_35871_a[k2] * j3;
						int j6 = k + Direction.field_35870_b[k2] * j3;
						j5 += Direction.field_35871_a[l1] * j4;
						j6 += Direction.field_35870_b[l1] * j4;
						int j7 = world.getBlockId(j5, j, j6);
						int k7 = world.getBlockMetadata(j5, j, j6);
						if (j7 != Block.endPortalFrame.blockID || !BlockEndPortalFrame.isEnderEyeInserted(k7)) {
							flag1 = false;
							continue label0;
						}
						j4++;
					}
					while (true);
				}

				if (flag1) {
					for (int k3 = i2; k3 <= j2; k3++) {
						for (int k4 = 1; k4 <= 3; k4++) {
							int k5 = i + Direction.field_35871_a[k2] * k3;
							int k6 = k + Direction.field_35870_b[k2] * k3;
							k5 += Direction.field_35871_a[l1] * k4;
							k6 += Direction.field_35870_b[l1] * k4;
							world.setBlockWithNotify(k5, j, k6, Block.endPortal.blockID);
						}
					}
				}
			}
			return true;
		}
		else {
			return false;
		}
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		MovingObjectPosition movingobjectposition = func_40402_a(world, entityplayer, false);
		if (movingobjectposition != null && movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
			int i = world.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
			if (i == Block.endPortalFrame.blockID) {
				return itemstack;
			}
		}
		if (!world.multiplayerWorld) {
			ChunkPosition chunkposition = world.func_40477_b("Stronghold", (int)entityplayer.posX, (int)entityplayer.posY, (int)entityplayer.posZ);
			if (chunkposition != null) {
				EntityEnderEye entityendereye = new EntityEnderEye(world, entityplayer.posX, (entityplayer.posY + 1.6200000000000001D) - (double)entityplayer.yOffset, entityplayer.posZ);
				entityendereye.func_40090_a(chunkposition.x, chunkposition.y, chunkposition.z);
				world.spawnEntityInWorld(entityendereye);
				world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
				world.playAuxSFXAtEntity(null, 1002, (int)entityplayer.posX, (int)entityplayer.posY, (int)entityplayer.posZ, 0);
				if (!entityplayer.capabilities.depleteBuckets) {
					itemstack.stackSize--;
				}
			}
		}
		return itemstack;
	}
}
