/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
	public boolean tryPlaceIntoWorld(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int face, float xOffset, float yOffset, float zOffset) {
		if (stack.itemID == MaterialData.flint.getRawId()) {
			int damage = stack.getItemDamage();
			if (damage >= 1024) {
				CustomBlock block = MaterialData.getCustomBlock(damage);
				// Item with no block component, return success
				if (block == null) {
					return true;
				}
				if (onBlockItemUse(block, player, world, x, y, z, face, xOffset, yOffset, zOffset)) {
					return true;
				}
				return false;
			}
		}
		return super.tryPlaceIntoWorld(stack, player, world, x, y, z, face, xOffset, yOffset, zOffset);
	}

	// From super class
	public boolean onBlockItemUse(CustomBlock block, EntityPlayer player, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
		if (world.getBlockId(x, y, z) == Block.snow.blockID) {
			side = 0;
		} else {
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
		if (world.canPlaceEntityOnSide(id, x, y, z, false, side, player)) {
			Block var8 = Block.blocksList[id];
			if (world.setBlockAndMetadataWithNotify(x, y, z, id, 0)) {
				Block.blocksList[id].updateBlockMetadata(world, x, y, z, side, xOffset, yOffset, zOffset);
				Block.blocksList[id].onBlockPlacedBy(world, x, y, z, player);

				world.world.getChunkAt(x, y, z).setCustomBlockId(x, y, z, (short) block.getCustomId());

				world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), var8.stepSound.stepSoundName, (var8.stepSound.getVolume() + 1.0F) / 2.0F, var8.stepSound.getPitch() * 0.8F);
			}

			return true;
		}
		return false;
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return shiftedIndex == MaterialData.flint.getRawId() ? false : super.hasEffect(par1ItemStack);
	}
}
