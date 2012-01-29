package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

class RailLogic {
	private World worldObj;
	private int trackX;
	private int trackY;
	private int trackZ;
	private final boolean isPoweredRail;
	private List connectedTracks;
	final BlockRail rail;

	public RailLogic(BlockRail blockrail, World world, int i, int j, int k) {
		rail = blockrail;

		connectedTracks = new ArrayList();
		worldObj = world;
		trackX = i;
		trackY = j;
		trackZ = k;
		int l = world.getBlockId(i, j, k);
		int i1 = world.getBlockMetadata(i, j, k);
		if (BlockRail.isPoweredBlockRail((BlockRail)Block.blocksList[l])) {
			isPoweredRail = true;
			i1 &= -9;
		}
		else {
			isPoweredRail = false;
		}
		setConnections(i1);
	}

	private void setConnections(int i) {
		connectedTracks.clear();
		if (i == 0) {
			connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ - 1));
			connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ + 1));
		}
		else if (i == 1) {
			connectedTracks.add(new ChunkPosition(trackX - 1, trackY, trackZ));
			connectedTracks.add(new ChunkPosition(trackX + 1, trackY, trackZ));
		}
		else if (i == 2) {
			connectedTracks.add(new ChunkPosition(trackX - 1, trackY, trackZ));
			connectedTracks.add(new ChunkPosition(trackX + 1, trackY + 1, trackZ));
		}
		else if (i == 3) {
			connectedTracks.add(new ChunkPosition(trackX - 1, trackY + 1, trackZ));
			connectedTracks.add(new ChunkPosition(trackX + 1, trackY, trackZ));
		}
		else if (i == 4) {
			connectedTracks.add(new ChunkPosition(trackX, trackY + 1, trackZ - 1));
			connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ + 1));
		}
		else if (i == 5) {
			connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ - 1));
			connectedTracks.add(new ChunkPosition(trackX, trackY + 1, trackZ + 1));
		}
		else if (i == 6) {
			connectedTracks.add(new ChunkPosition(trackX + 1, trackY, trackZ));
			connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ + 1));
		}
		else if (i == 7) {
			connectedTracks.add(new ChunkPosition(trackX - 1, trackY, trackZ));
			connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ + 1));
		}
		else if (i == 8) {
			connectedTracks.add(new ChunkPosition(trackX - 1, trackY, trackZ));
			connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ - 1));
		}
		else if (i == 9) {
			connectedTracks.add(new ChunkPosition(trackX + 1, trackY, trackZ));
			connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ - 1));
		}
	}

	private void refreshConnectedTracks() {
		for (int i = 0; i < connectedTracks.size(); i++) {
			RailLogic raillogic = getMinecartTrackLogic((ChunkPosition)connectedTracks.get(i));
			if (raillogic == null || !raillogic.isConnectedTo(this)) {
				connectedTracks.remove(i--);
			}
			else {
				connectedTracks.set(i, new ChunkPosition(raillogic.trackX, raillogic.trackY, raillogic.trackZ));
			}
		}
	}

	private boolean isMinecartTrack(int i, int j, int k) {
		if (BlockRail.isRailBlockAt(worldObj, i, j, k)) {
			return true;
		}
		if (BlockRail.isRailBlockAt(worldObj, i, j + 1, k)) {
			return true;
		}
		return BlockRail.isRailBlockAt(worldObj, i, j - 1, k);
	}

	private RailLogic getMinecartTrackLogic(ChunkPosition chunkposition) {
		if (BlockRail.isRailBlockAt(worldObj, chunkposition.x, chunkposition.y, chunkposition.z)) {
			return new RailLogic(rail, worldObj, chunkposition.x, chunkposition.y, chunkposition.z);
		}
		if (BlockRail.isRailBlockAt(worldObj, chunkposition.x, chunkposition.y + 1, chunkposition.z)) {
			return new RailLogic(rail, worldObj, chunkposition.x, chunkposition.y + 1, chunkposition.z);
		}
		if (BlockRail.isRailBlockAt(worldObj, chunkposition.x, chunkposition.y - 1, chunkposition.z)) {
			return new RailLogic(rail, worldObj, chunkposition.x, chunkposition.y - 1, chunkposition.z);
		}
		else {
			return null;
		}
	}

	private boolean isConnectedTo(RailLogic raillogic) {
		for (int i = 0; i < connectedTracks.size(); i++) {
			ChunkPosition chunkposition = (ChunkPosition)connectedTracks.get(i);
			if (chunkposition.x == raillogic.trackX && chunkposition.z == raillogic.trackZ) {
				return true;
			}
		}

		return false;
	}

	private boolean isInTrack(int i, int j, int k) {
		for (int l = 0; l < connectedTracks.size(); l++) {
			ChunkPosition chunkposition = (ChunkPosition)connectedTracks.get(l);
			if (chunkposition.x == i && chunkposition.z == k) {
				return true;
			}
		}

		return false;
	}

	private int getAdjacentTracks() {
		int i = 0;
		if (isMinecartTrack(trackX, trackY, trackZ - 1)) {
			i++;
		}
		if (isMinecartTrack(trackX, trackY, trackZ + 1)) {
			i++;
		}
		if (isMinecartTrack(trackX - 1, trackY, trackZ)) {
			i++;
		}
		if (isMinecartTrack(trackX + 1, trackY, trackZ)) {
			i++;
		}
		return i;
	}

	private boolean canConnectTo(RailLogic raillogic) {
		if (isConnectedTo(raillogic)) {
			return true;
		}
		if (connectedTracks.size() == 2) {
			return false;
		}
		if (connectedTracks.size() == 0) {
			return true;
		}
		ChunkPosition chunkposition = (ChunkPosition)connectedTracks.get(0);
		return raillogic.trackY != trackY || chunkposition.y != trackY ? true : true;
	}

	private void connectToNeighbor(RailLogic raillogic) {
		connectedTracks.add(new ChunkPosition(raillogic.trackX, raillogic.trackY, raillogic.trackZ));
		boolean flag = isInTrack(trackX, trackY, trackZ - 1);
		boolean flag1 = isInTrack(trackX, trackY, trackZ + 1);
		boolean flag2 = isInTrack(trackX - 1, trackY, trackZ);
		boolean flag3 = isInTrack(trackX + 1, trackY, trackZ);
		byte byte0 = -1;
		if (flag || flag1) {
			byte0 = 0;
		}
		if (flag2 || flag3) {
			byte0 = 1;
		}
		if (!isPoweredRail) {
			if (flag1 && flag3 && !flag && !flag2) {
				byte0 = 6;
			}
			if (flag1 && flag2 && !flag && !flag3) {
				byte0 = 7;
			}
			if (flag && flag2 && !flag1 && !flag3) {
				byte0 = 8;
			}
			if (flag && flag3 && !flag1 && !flag2) {
				byte0 = 9;
			}
		}
		if (byte0 == 0) {
			if (BlockRail.isRailBlockAt(worldObj, trackX, trackY + 1, trackZ - 1)) {
				byte0 = 4;
			}
			if (BlockRail.isRailBlockAt(worldObj, trackX, trackY + 1, trackZ + 1)) {
				byte0 = 5;
			}
		}
		if (byte0 == 1) {
			if (BlockRail.isRailBlockAt(worldObj, trackX + 1, trackY + 1, trackZ)) {
				byte0 = 2;
			}
			if (BlockRail.isRailBlockAt(worldObj, trackX - 1, trackY + 1, trackZ)) {
				byte0 = 3;
			}
		}
		if (byte0 < 0) {
			byte0 = 0;
		}
		int i = byte0;
		if (isPoweredRail) {
			i = worldObj.getBlockMetadata(trackX, trackY, trackZ) & 8 | byte0;
		}
		worldObj.setBlockMetadataWithNotify(trackX, trackY, trackZ, i);
	}

	private boolean canConnectFrom(int i, int j, int k) {
		RailLogic raillogic = getMinecartTrackLogic(new ChunkPosition(i, j, k));
		if (raillogic == null) {
			return false;
		}
		else {
			raillogic.refreshConnectedTracks();
			return raillogic.canConnectTo(this);
		}
	}

	public void refreshTrackShape(boolean flag, boolean flag1) {
		boolean flag2 = canConnectFrom(trackX, trackY, trackZ - 1);
		boolean flag3 = canConnectFrom(trackX, trackY, trackZ + 1);
		boolean flag4 = canConnectFrom(trackX - 1, trackY, trackZ);
		boolean flag5 = canConnectFrom(trackX + 1, trackY, trackZ);
		byte byte0 = -1;
		if ((flag2 || flag3) && !flag4 && !flag5) {
			byte0 = 0;
		}
		if ((flag4 || flag5) && !flag2 && !flag3) {
			byte0 = 1;
		}
		if (!isPoweredRail) {
			if (flag3 && flag5 && !flag2 && !flag4) {
				byte0 = 6;
			}
			if (flag3 && flag4 && !flag2 && !flag5) {
				byte0 = 7;
			}
			if (flag2 && flag4 && !flag3 && !flag5) {
				byte0 = 8;
			}
			if (flag2 && flag5 && !flag3 && !flag4) {
				byte0 = 9;
			}
		}
		if (byte0 == -1) {
			if (flag2 || flag3) {
				byte0 = 0;
			}
			if (flag4 || flag5) {
				byte0 = 1;
			}
			if (!isPoweredRail) {
				if (flag) {
					if (flag3 && flag5) {
						byte0 = 6;
					}
					if (flag4 && flag3) {
						byte0 = 7;
					}
					if (flag5 && flag2) {
						byte0 = 9;
					}
					if (flag2 && flag4) {
						byte0 = 8;
					}
				}
				else {
					if (flag2 && flag4) {
						byte0 = 8;
					}
					if (flag5 && flag2) {
						byte0 = 9;
					}
					if (flag4 && flag3) {
						byte0 = 7;
					}
					if (flag3 && flag5) {
						byte0 = 6;
					}
				}
			}
		}
		if (byte0 == 0) {
			if (BlockRail.isRailBlockAt(worldObj, trackX, trackY + 1, trackZ - 1)) {
				byte0 = 4;
			}
			if (BlockRail.isRailBlockAt(worldObj, trackX, trackY + 1, trackZ + 1)) {
				byte0 = 5;
			}
		}
		if (byte0 == 1) {
			if (BlockRail.isRailBlockAt(worldObj, trackX + 1, trackY + 1, trackZ)) {
				byte0 = 2;
			}
			if (BlockRail.isRailBlockAt(worldObj, trackX - 1, trackY + 1, trackZ)) {
				byte0 = 3;
			}
		}
		if (byte0 < 0) {
			byte0 = 0;
		}
		setConnections(byte0);
		int i = byte0;
		if (isPoweredRail) {
			i = worldObj.getBlockMetadata(trackX, trackY, trackZ) & 8 | byte0;
		}
		if (flag1 || worldObj.getBlockMetadata(trackX, trackY, trackZ) != i) {
			worldObj.setBlockMetadataWithNotify(trackX, trackY, trackZ, i);
			for (int j = 0; j < connectedTracks.size(); j++) {
				RailLogic raillogic = getMinecartTrackLogic((ChunkPosition)connectedTracks.get(j));
				if (raillogic == null) {
					continue;
				}
				raillogic.refreshConnectedTracks();
				if (raillogic.canConnectTo(this)) {
					raillogic.connectToNeighbor(this);
				}
			}
		}
	}

	static int getNAdjacentTracks(RailLogic raillogic) {
		return raillogic.getAdjacentTracks();
	}
}
