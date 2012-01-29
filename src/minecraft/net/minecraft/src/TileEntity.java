package net.minecraft.src;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class TileEntity {
	private static Map nameToClassMap = new HashMap();
	private static Map classToNameMap = new HashMap();
	public World worldObj;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	protected boolean tileEntityInvalid;
	public int blockMetadata;
	public Block blockType;

	public TileEntity() {
		blockMetadata = -1;
	}

	private static void addMapping(Class class1, String s) {
		if (classToNameMap.containsKey(s)) {
			throw new IllegalArgumentException((new StringBuilder()).append("Duplicate id: ").append(s).toString());
		}
		else {
			nameToClassMap.put(s, class1);
			classToNameMap.put(class1, s);
			return;
		}
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		xCoord = nbttagcompound.getInteger("x");
		yCoord = nbttagcompound.getInteger("y");
		zCoord = nbttagcompound.getInteger("z");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		String s = (String)classToNameMap.get(getClass());
		if (s == null) {
			throw new RuntimeException((new StringBuilder()).append(getClass()).append(" is missing a mapping! This is a bug!").toString());
		}
		else {
			nbttagcompound.setString("id", s);
			nbttagcompound.setInteger("x", xCoord);
			nbttagcompound.setInteger("y", yCoord);
			nbttagcompound.setInteger("z", zCoord);
			return;
		}
	}

	public void updateEntity() {
	}

	public static TileEntity createAndLoadEntity(NBTTagCompound nbttagcompound) {
		TileEntity tileentity = null;
		try {
			Class class1 = (Class)nameToClassMap.get(nbttagcompound.getString("id"));
			if (class1 != null) {
				tileentity = (TileEntity)class1.newInstance();
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		if (tileentity != null) {
			tileentity.readFromNBT(nbttagcompound);
		}
		else {
			System.out.println((new StringBuilder()).append("Skipping TileEntity with id ").append(nbttagcompound.getString("id")).toString());
		}
		return tileentity;
	}

	public int getBlockMetadata() {
		if (blockMetadata == -1) {
			blockMetadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		}
		return blockMetadata;
	}

	public void onInventoryChanged() {
		if (worldObj != null) {
			blockMetadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			worldObj.updateTileEntityChunkAndDoNothing(xCoord, yCoord, zCoord, this);
		}
	}

	public double getDistanceFrom(double d, double d1, double d2) {
		double d3 = ((double)xCoord + 0.5D) - d;
		double d4 = ((double)yCoord + 0.5D) - d1;
		double d5 = ((double)zCoord + 0.5D) - d2;
		return d3 * d3 + d4 * d4 + d5 * d5;
	}

	public Block getBlockType() {
		if (blockType == null) {
			blockType = Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)];
		}
		return blockType;
	}

	public boolean isInvalid() {
		return tileEntityInvalid;
	}

	public void invalidate() {
		tileEntityInvalid = true;
	}

	public void validate() {
		tileEntityInvalid = false;
	}

	public void onTileEntityPowered(int i, int j) {
	}

	public void updateContainingBlockInfo() {
		blockType = null;
		blockMetadata = -1;
	}

	static Class _mthclass$(String s) {
		try {
			return Class.forName(s);
		}
		catch (ClassNotFoundException classnotfoundexception) {
			throw new NoClassDefFoundError(classnotfoundexception.getMessage());
		}
	}

	static {
		addMapping(net.minecraft.src.TileEntityFurnace.class, "Furnace");
		addMapping(net.minecraft.src.TileEntityChest.class, "Chest");
		addMapping(net.minecraft.src.TileEntityRecordPlayer.class, "RecordPlayer");
		addMapping(net.minecraft.src.TileEntityDispenser.class, "Trap");
		addMapping(net.minecraft.src.TileEntitySign.class, "Sign");
		addMapping(net.minecraft.src.TileEntityMobSpawner.class, "MobSpawner");
		addMapping(net.minecraft.src.TileEntityNote.class, "Music");
		addMapping(net.minecraft.src.TileEntityPiston.class, "Piston");
		addMapping(net.minecraft.src.TileEntityBrewingStand.class, "Cauldron");
		addMapping(net.minecraft.src.TileEntityEnchantmentTable.class, "EnchantTable");
		addMapping(net.minecraft.src.TileEntityEndPortal.class, "Airportal");
	}
}
