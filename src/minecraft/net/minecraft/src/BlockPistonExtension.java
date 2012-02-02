/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev license version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * SpoutAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://getspout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockPistonBase;
import net.minecraft.src.Facing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockPistonExtension extends Block {
	private int field_31053_a = -1;

	public BlockPistonExtension(int var1, int var2) {
		super(var1, var2, Material.piston);
		this.setStepSound(soundStoneFootstep);
		this.setHardness(0.5F);
	}

	public void func_31052_a_(int var1) {
		this.field_31053_a = var1;
	}

	public void func_31051_a() {
		this.field_31053_a = -1;
	}

	public void onBlockRemoval(World var1, int var2, int var3, int var4) {
		super.onBlockRemoval(var1, var2, var3, var4);
		int var5 = var1.getBlockMetadata(var2, var3, var4);
		int var6 = Facing.field_31057_a[func_31050_c(var5)];
		var2 += Facing.offsetsXForSide[var6];
		var3 += Facing.offsetsYForSide[var6];
		var4 += Facing.offsetsZForSide[var6];
		int var7 = var1.getBlockId(var2, var3, var4);
		if (var7 == Block.pistonBase.blockID || var7 == Block.pistonStickyBase.blockID) {
			var5 = var1.getBlockMetadata(var2, var3, var4);
			if (BlockPistonBase.isExtended(var5)) {
				Block.blocksList[var7].dropBlockAsItem(var1, var2, var3, var4, var5, 0);
				var1.setBlockWithNotify(var2, var3, var4, 0);
			}
		}
	}

	public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
		int var3 = func_31050_c(var2);
		return var1 == var3 ? (this.field_31053_a >= 0 ? this.field_31053_a : ((var2 & 8) != 0 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture)) : (var1 == Facing.field_31057_a[var3] ? 107 : 108);
	}

	public int getRenderType() {
		return 17;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		return false;
	}

	public boolean canPlaceBlockOnSide(World var1, int var2, int var3, int var4, int var5) {
		return false;
	}

	public int quantityDropped(Random var1) {
		return 0;
	}

	public void getCollidingBoundingBoxes(World var1, int var2, int var3, int var4, AxisAlignedBB var5, ArrayList var6) {
		int var7 = var1.getBlockMetadata(var2, var3, var4);
		switch (func_31050_c(var7)) {
			case 0:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
				super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
				this.setBlockBounds(0.375F, 0.25F, 0.375F, 0.625F, 1.0F, 0.625F);
				super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
				break;
			case 1:
				this.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
				super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
				this.setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.75F, 0.625F);
				super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
				break;
			case 2:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
				super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
				this.setBlockBounds(0.25F, 0.375F, 0.25F, 0.75F, 0.625F, 1.0F);
				super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
				break;
			case 3:
				this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
				super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
				this.setBlockBounds(0.25F, 0.375F, 0.0F, 0.75F, 0.625F, 0.75F);
				super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
				break;
			case 4:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
				super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
				this.setBlockBounds(0.375F, 0.25F, 0.25F, 0.625F, 0.75F, 1.0F);
				super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
				break;
			case 5:
				this.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
				this.setBlockBounds(0.0F, 0.375F, 0.25F, 0.75F, 0.625F, 0.75F);
				super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
		}

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
		int var5 = var1.getBlockMetadata(var2, var3, var4);
		switch (func_31050_c(var5)) {
			case 0:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
				break;
			case 1:
				this.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 2:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
				break;
			case 3:
				this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
				break;
			case 4:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
				break;
			case 5:
				this.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		int var6 = func_31050_c(var1.getBlockMetadata(var2, var3, var4));
		int var7 = var1.getBlockId(var2 - Facing.offsetsXForSide[var6], var3 - Facing.offsetsYForSide[var6], var4 - Facing.offsetsZForSide[var6]);
		if (var7 != Block.pistonBase.blockID && var7 != Block.pistonStickyBase.blockID) {
			var1.setBlockWithNotify(var2, var3, var4, 0);
		}
		else {
			Block.blocksList[var7].onNeighborBlockChange(var1, var2 - Facing.offsetsXForSide[var6], var3 - Facing.offsetsYForSide[var6], var4 - Facing.offsetsZForSide[var6], var5);
		}
	}

	public static int func_31050_c(int var0) {
		if (var0 >= Facing.field_31057_a.length) return 0; // Spout: Make sure this returns a valid index for the array.
		return var0 & 7;
	}
}
