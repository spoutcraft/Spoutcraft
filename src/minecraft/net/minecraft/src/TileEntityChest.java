package net.minecraft.src;

import java.util.Random;

public class TileEntityChest extends TileEntity
	implements IInventory {
	private ItemStack chestContents[];
	public boolean adjacentChestChecked;
	public TileEntityChest adjacentChestZNeg;
	public TileEntityChest adjacentChestXPos;
	public TileEntityChest adjacentChestXNeg;
	public TileEntityChest adjacentChestZPos;
	public float lidAngle;
	public float prevLidAngle;
	public int numUsingPlayers;
	private int ticksSinceSync;

	public TileEntityChest() {
		chestContents = new ItemStack[36];
		adjacentChestChecked = false;
	}

	public int getSizeInventory() {
		return 27;
	}

	public ItemStack getStackInSlot(int i) {
		return chestContents[i];
	}

	public ItemStack decrStackSize(int i, int j) {
		if (chestContents[i] != null) {
			if (chestContents[i].stackSize <= j) {
				ItemStack itemstack = chestContents[i];
				chestContents[i] = null;
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = chestContents[i].splitStack(j);
			if (chestContents[i].stackSize == 0) {
				chestContents[i] = null;
			}
			onInventoryChanged();
			return itemstack1;
		}
		else {
			return null;
		}
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		chestContents[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	public String getInvName() {
		return "Chest";
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		chestContents = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 0xff;
			if (j >= 0 && j < chestContents.length) {
				chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < chestContents.length; i++) {
			if (chestContents[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				chestContents[i].writeToNBT(nbttagcompound1);
				nbttaglist.setTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		}
		return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
	}

	public void updateContainingBlockInfo() {
		super.updateContainingBlockInfo();
		adjacentChestChecked = false;
	}

	public void checkForAdjacentChests() {
		if (adjacentChestChecked) {
			return;
		}
		adjacentChestChecked = true;
		adjacentChestZNeg = null;
		adjacentChestXPos = null;
		adjacentChestXNeg = null;
		adjacentChestZPos = null;
		if (worldObj.getBlockId(xCoord - 1, yCoord, zCoord) == Block.chest.blockID) {
			adjacentChestXNeg = (TileEntityChest)worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord);
		}
		if (worldObj.getBlockId(xCoord + 1, yCoord, zCoord) == Block.chest.blockID) {
			adjacentChestXPos = (TileEntityChest)worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord);
		}
		if (worldObj.getBlockId(xCoord, yCoord, zCoord - 1) == Block.chest.blockID) {
			adjacentChestZNeg = (TileEntityChest)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1);
		}
		if (worldObj.getBlockId(xCoord, yCoord, zCoord + 1) == Block.chest.blockID) {
			adjacentChestZPos = (TileEntityChest)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1);
		}
		if (adjacentChestZNeg != null) {
			adjacentChestZNeg.updateContainingBlockInfo();
		}
		if (adjacentChestZPos != null) {
			adjacentChestZPos.updateContainingBlockInfo();
		}
		if (adjacentChestXPos != null) {
			adjacentChestXPos.updateContainingBlockInfo();
		}
		if (adjacentChestXNeg != null) {
			adjacentChestXNeg.updateContainingBlockInfo();
		}
	}

	public void updateEntity() {
		super.updateEntity();
		checkForAdjacentChests();
		if ((++ticksSinceSync % 20) * 4 == 0) {
			worldObj.playNoteAt(xCoord, yCoord, zCoord, 1, numUsingPlayers);
		}
		prevLidAngle = lidAngle;
		float f = 0.1F;
		if (numUsingPlayers > 0 && lidAngle == 0.0F && adjacentChestZNeg == null && adjacentChestXNeg == null) {
			double d = (double)xCoord + 0.5D;
			double d1 = (double)zCoord + 0.5D;
			if (adjacentChestZPos != null) {
				d1 += 0.5D;
			}
			if (adjacentChestXPos != null) {
				d += 0.5D;
			}
			worldObj.playSoundEffect(d, (double)yCoord + 0.5D, d1, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}
		if (numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F) {
			float f1 = lidAngle;
			if (numUsingPlayers > 0) {
				lidAngle += f;
			}
			else {
				lidAngle -= f;
			}
			if (lidAngle > 1.0F) {
				lidAngle = 1.0F;
			}
			float f2 = 0.5F;
			if (lidAngle < f2 && f1 >= f2 && adjacentChestZNeg == null && adjacentChestXNeg == null) {
				double d2 = (double)xCoord + 0.5D;
				double d3 = (double)zCoord + 0.5D;
				if (adjacentChestZPos != null) {
					d3 += 0.5D;
				}
				if (adjacentChestXPos != null) {
					d2 += 0.5D;
				}
				worldObj.playSoundEffect(d2, (double)yCoord + 0.5D, d3, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
			if (lidAngle < 0.0F) {
				lidAngle = 0.0F;
			}
		}
	}

	public void onTileEntityPowered(int i, int j) {
		if (i == 1) {
			numUsingPlayers = j;
		}
	}

	public void openChest() {
		numUsingPlayers++;
		worldObj.playNoteAt(xCoord, yCoord, zCoord, 1, numUsingPlayers);
	}

	public void closeChest() {
		numUsingPlayers--;
		worldObj.playNoteAt(xCoord, yCoord, zCoord, 1, numUsingPlayers);
	}

	public void invalidate() {
		updateContainingBlockInfo();
		checkForAdjacentChests();
		super.invalidate();
	}
}
