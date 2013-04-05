/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.item;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumAction;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.material.CustomBlock;
import org.spoutcraft.api.material.CustomItem;
import org.spoutcraft.api.material.Food;
import org.spoutcraft.api.material.MaterialData;

public class SpoutItem extends Item {
	public SpoutItem(int blockId) {
		super(blockId);
		this.setHasSubtypes(true);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		CustomItem customItem = MaterialData.getCustomItem(item.getItemDamage());
		if (customItem instanceof Food) {
			if (player.canEat(false)) {
				player.setItemInUse(item, 32);
			}
		}
		return item;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack item) {
		CustomItem customItem = MaterialData.getCustomItem(item.getItemDamage());
		if (customItem instanceof Food) {
			return EnumAction.eat;
		}
		return EnumAction.none;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int face, float xOffset, float yOffset, float zOffset) {
		if (stack.itemID == MaterialData.flint.getRawId()) {
			int damage = stack.getItemDamage();
			if (damage >= 1024) {
				CustomBlock block = MaterialData.getCustomBlock(damage);
				// Item with no block component, return success
				if (block == null) {
					return true;
				}
				if (onItemUse(stack, block, player, world, x, y, z, face, xOffset, yOffset, zOffset)) {
					return true;
				}
				return false;
			}
		}
		return super.onItemUse(stack, player, world, x, y, z, face, xOffset, yOffset, zOffset);
	}

	// From ItemBlock.onItemUse class
	public boolean onItemUse(ItemStack item, CustomBlock block, EntityPlayer player, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
		int var11 = world.getBlockId(x, y, z);

		if (var11 == Block.snow.blockID && (world.getBlockMetadata(x, y, z) & 7) < 1) {
			side = 1;
		} else if (var11 != Block.vine.blockID && var11 != Block.tallGrass.blockID && var11 != Block.deadBush.blockID) {
			if (side == 0) {
				--y;
			}

			if (side == 1) {
				++y;
			}

			if (side == 2) {
				--z;
			}

			if (side == 3) {
				++z;
			}

			if (side == 4) {
				--x;
			}

			if (side == 5) {
				++x;
			}
		}

		int id = block.getBlockId();

		if (item.stackSize == 0) {
			return false;
		} else if (!player.canPlayerEdit(x, y, z, side, item)) {
			return false;
		} else if (y == 255) {
			return false;
		} else if (world.canPlaceEntityOnSide(id, x, y, z, false, side, player, item)) {
			Block var12 = Block.blocksList[id];
			int var13 = this.getMetadata(item.getItemDamage());
			int var14 = Block.blocksList[id].onBlockPlaced(world, x, y, z, side, xOffset, yOffset, zOffset, var13);

			if (world.setBlock(x, y, z, id, var14, 3)) {
				if (world.getBlockId(x, y, z) == id) {
					Block.blocksList[id].onBlockPlacedBy(world, x, y, z, player, item);
					Block.blocksList[id].onPostBlockPlaced(world, x, y, z, var14);
				}
				Spoutcraft.getChunkAt(world, x, y, z).setCustomBlockId(x, y, z, (short) block.getCustomId());

				world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), var12.stepSound.getPlaceSound(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
				--item.stackSize;
			}

			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return itemID == MaterialData.flint.getRawId() ? false : super.hasEffect(par1ItemStack);
	}
}
