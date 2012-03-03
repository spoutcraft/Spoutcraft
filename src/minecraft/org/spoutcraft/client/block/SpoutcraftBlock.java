/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
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
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.block;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.BlockRedstoneWire;

import org.spoutcraft.spoutcraftapi.material.Liquid;
import org.spoutcraft.spoutcraftapi.material.Material;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.block.Biome;
import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.BlockFace;
import org.spoutcraft.spoutcraftapi.block.BlockState;
import org.spoutcraft.spoutcraftapi.block.Chunk;
import org.spoutcraft.spoutcraftapi.util.FastLocation;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;

public class SpoutcraftBlock implements Block{
	private final SpoutcraftChunk chunk;
	private final int x;
	private final int y;
	private final int z;

	public SpoutcraftBlock(SpoutcraftChunk chunk, int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.chunk = chunk;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public float getHardness() {
		int index = getIndex();
		if (chunk.hardnessOverrides.containsKey(index)) {
			return chunk.hardnessOverrides.get(index);
		}
		return net.minecraft.src.Block.blocksList[getTypeId()].getHardness();
	}

	public void setHardness(float hardness) {
		chunk.hardnessOverrides.put(getIndex(), hardness);
	}

	public void resetHardness() {
		chunk.hardnessOverrides.remove(getIndex());
	}

	public int getBlockPower() {
		return getBlockPower(BlockFace.SELF);
	}

	public byte getLightLevel() {
		return (byte) chunk.getHandle().worldObj.getBlockLightValue(x, y, z);
	}

	public int getTypeId() {
		return chunk.getHandle().worldObj.getBlockId(x, y, z);
	}

	public World getWorld() {
		return chunk.getWorld();
	}

	public boolean isEmpty() {
		return getTypeId() == 0;
	}

	public boolean isLiquid() {
		return getType() instanceof Liquid;
	}

	public void setData(byte data) {
		setData(data, true);
	}

	public boolean setTypeId(int type) {
		return setTypeId(type, true);
	}

	public Chunk getChunk() {
		return chunk;
	}

	public byte getData() {
		return (byte) chunk.getHandle().worldObj.getBlockMetadata(x, y, z);
	}

	public void setData(byte data, boolean applyPhyiscs) {
		if (applyPhyiscs) {
			chunk.getHandle().worldObj.setBlockMetadataWithNotify(x, y, z, data);
		} else {
			chunk.getHandle().worldObj.setBlockMetadata(x, y, z, data);
		}

	}

	public boolean setTypeId(int type, boolean applyPhysics) {
		if (applyPhysics) {
			return chunk.getHandle().worldObj.setBlockWithNotify(x, y, z, type);
		}
		return chunk.getHandle().worldObj.setBlock(x, y, z, type);
	}

	public boolean setTypeIdAndData(int type, byte data, boolean applyPhysics) {
		if (applyPhysics) {
			chunk.getHandle().worldObj.setBlockAndMetadataWithNotify(x, y, z, type, data);
		}
		return chunk.getHandle().worldObj.setBlockAndMetadata(x, y, z, type, data);
	}

	public Block getRelative(int modX, int modY, int modZ) {
		return getWorld().getBlockAt(getX() + modX, getY() + modY, getZ() + modZ);
	}

	public Block getRelative(BlockFace face) {
		return getRelative(face, 1);
	}

	public Block getRelative(BlockFace face, int distance) {
		return getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
	}

	public FixedLocation getLocation() {
		return new FastLocation(x, y, z, 0, 0, getWorld());
	}

	public BlockFace getFace(Block block) {
		BlockFace[] values = BlockFace.values();

		for (BlockFace face : values) {
			if ((this.getX() + face.getModX() == block.getX()) && (this.getY() + face.getModY() == block.getY()) && (this.getZ() + face.getModZ() == block.getZ())) {
				return face;
			}
		}

		return null;
	}

	public BlockState getState() {
		Material material = getType();

		if (material == MaterialData.sign || material == MaterialData.signPost || material == MaterialData.wallSign) {
			return new CraftSign(this);
		}
		if (material == MaterialData.chest) {
			return new CraftChest(this);
		}
		if (material == MaterialData.burningfurnace || material == MaterialData.furnace) {
			return new CraftFurnace(this);
		}
		if (material == MaterialData.dispenser) {
			return new CraftDispenser(this);
		}
		if (material == MaterialData.monsterSpawner) {
			return new CraftCreatureSpawner(this);
		}
		if (material == MaterialData.noteblock) {
			return new CraftCreatureSpawner(this);
		}
		if (material == MaterialData.jukebox) {
			return new CraftJukebox(this);
		}
		return new CraftBlockState(this);
	}

	public Biome getBiome() {
		return biomeBaseToBiome(chunk.getHandle().worldObj.getWorldChunkManager().getBiomeGenAt(x, z));
	}

	public static final Biome biomeBaseToBiome(BiomeGenBase base) {
		if (base == BiomeGenBase.swampland) {
			return Biome.SWAMPLAND;
		} else if (base == BiomeGenBase.forest) {
			return Biome.FOREST;
		} else if (base == BiomeGenBase.taiga) {
			return Biome.TAIGA;
		} else if (base == BiomeGenBase.desert) {
			return Biome.DESERT;
		} else if (base == BiomeGenBase.plains) {
			return Biome.PLAINS;
		} else if (base == BiomeGenBase.hell) {
			return Biome.HELL;
		} else if (base == BiomeGenBase.sky) {
			return Biome.SKY;
		} else if (base == BiomeGenBase.river) {
			return Biome.RIVER;
		} else if (base == BiomeGenBase.extremeHills) {
			return Biome.MOUNTAINS;
		} else if (base == BiomeGenBase.ocean) {
			return Biome.OCEAN;
		}

		return null;
	}

	public boolean isBlockPowered() {
		return chunk.getHandle().worldObj.isBlockGettingPowered(x, y, z);
	}

	public boolean isBlockIndirectlyPowered() {
		return chunk.getHandle().worldObj.isBlockIndirectlyGettingPowered(x, y, z);
	}

	public boolean isBlockFacePowered(BlockFace face) {
		return chunk.getHandle().worldObj.isBlockProvidingPowerTo(x, y, z, blockFaceToNotch(face));
	}

	public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
		return chunk.getHandle().worldObj.isBlockIndirectlyProvidingPowerTo(x, y, z, blockFaceToNotch(face));
	}

	public int getBlockPower(BlockFace face) {
		int power = 0;
		BlockRedstoneWire wire = (BlockRedstoneWire) net.minecraft.src.Block.redstoneWire;
		net.minecraft.src.World world = chunk.getHandle().worldObj;
		if ((face == BlockFace.DOWN || face == BlockFace.SELF) && world.isBlockProvidingPowerTo(x, y - 1, z, 0)) {
			power = wire.getMaxCurrentStrength(world, x, y - 1, z, power);
		}
		if ((face == BlockFace.UP || face == BlockFace.SELF) && world.isBlockProvidingPowerTo(x, y + 1, z, 1)) {
			power = wire.getMaxCurrentStrength(world, x, y + 1, z, power);
		}
		if ((face == BlockFace.EAST || face == BlockFace.SELF) && world.isBlockProvidingPowerTo(x, y, z - 1, 2)) {
			power = wire.getMaxCurrentStrength(world, x, y, z - 1, power);
		}
		if ((face == BlockFace.WEST || face == BlockFace.SELF) && world.isBlockProvidingPowerTo(x, y, z + 1, 3)) {
			power = wire.getMaxCurrentStrength(world, x, y, z + 1, power);
		}
		if ((face == BlockFace.NORTH || face == BlockFace.SELF) && world.isBlockProvidingPowerTo(x - 1, y, z, 4)) {
			power = wire.getMaxCurrentStrength(world, x - 1, y, z, power);
		}
		if ((face == BlockFace.SOUTH || face == BlockFace.SELF) && world.isBlockProvidingPowerTo(x + 1, y, z, 5)) {
			power = wire.getMaxCurrentStrength(world, x + 1, y, z, power);
		}
		return power > 0 ? power : (face == BlockFace.SELF ? isBlockIndirectlyPowered() : isBlockFaceIndirectlyPowered(face)) ? 15 : 0;
	}

	public double getTemperature() {
		return chunk.getHandle().worldObj.func_48454_a(x, z).func_48411_i();
	}

	public double getHumidity() {
		return chunk.getHandle().worldObj.func_48454_a(x, z).func_48414_h();
	}

	private int getIndex() {
		return (x & 0xF) << 11 | (z & 0xF) << 7 | (y & 0x7F);
	}

	/**
	 * Notch uses a 0-5 to mean DOWN, UP, EAST, WEST, NORTH, SOUTH
	 * in that order all over. This method is convenience to convert for us.
	 *
	 * @return BlockFace the BlockFace represented by this number
	 */
	public static BlockFace notchToBlockFace(int notch) {
		switch (notch) {
		case 0:
			return BlockFace.DOWN;
		case 1:
			return BlockFace.UP;
		case 2:
			return BlockFace.EAST;
		case 3:
			return BlockFace.WEST;
		case 4:
			return BlockFace.NORTH;
		case 5:
			return BlockFace.SOUTH;
		default:
			return BlockFace.SELF;
		}
	}

	public static int blockFaceToNotch(BlockFace face) {
		switch(face) {
			case DOWN:
				return 0;
			case UP:
				return 1;
			case EAST:
				return 2;
			case WEST:
				return 3;
			case NORTH:
				return 4;
			case SOUTH:
				return 5;
			default:
				return 7;
		}
	}

	public org.spoutcraft.spoutcraftapi.material.Block getType() {
		short customId = chunk.getCustomBlockId(x, y, z);
		if (customId != 0) {
			return MaterialData.getCustomBlock(customId);
		}
		return MaterialData.getBlock(getTypeId(), getData());
	}

	@Override
	public boolean equals(Object o) {
		return this == o;
	}

	@Override
	public String toString() {
		return "Block{" + "chunk=" + chunk + "x=" + x + "y=" + y + "z=" + z + '}';
	}
}
