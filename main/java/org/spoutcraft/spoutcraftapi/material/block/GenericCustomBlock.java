package org.spoutcraft.spoutcraftapi.material.block;

import java.io.IOException;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.block.BlockFace;
import org.spoutcraft.spoutcraftapi.block.design.BlockDesign;
import org.spoutcraft.spoutcraftapi.block.design.GenericBlockDesign;
import org.spoutcraft.spoutcraftapi.entity.Entity;
import org.spoutcraft.spoutcraftapi.entity.LivingEntity;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.material.Block;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.CustomItem;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.material.item.GenericCustomItem;

public class GenericCustomBlock implements CustomBlock {
	public BlockDesign design[] = new GenericBlockDesign[256];
	private ItemStack drop = null;
	private String name;
	private String fullName;
	private int customId;
	private Addon addon;
	private CustomItem item;
	private int blockId;
	private boolean opaque;
	private float hardness = 1.5F;
	private float friction = 0.6F;
	private int lightLevel = 0;
	
	/**
	 * Creates a GenericCustomBlock with no values, used for serialization purposes only.
	 */
	public GenericCustomBlock() {
		
	}

	/**
	 * Creates a GenericCustomBlock with no model yet.
	 * 
	 * @param addon creating the block
	 * @param name of the block
	 * @param isOpaque true if you want the block solid
	 */
	public GenericCustomBlock(Addon addon, String name, boolean isOpaque) {
		this(addon, name, isOpaque, new GenericCustomItem(addon, name));
	}
	
	/**
	 * Creates a GenericCustomBlock with a specified Design and metadata
	 * 
	 * @param addon creating the block
	 * @param name of the block
	 * @param isOpaque true if you want the block solid
	 * @param item to use for the block
	 */
	public GenericCustomBlock(Addon addon, String name, boolean isOpaque, CustomItem item) {
		opaque = isOpaque;
		this.blockId = isOpaque ? 1 :20;
		this.addon = addon;
		this.item = item;
		this.name = item.getName();
		this.fullName = item.getFullName();
		this.customId = item.getCustomId();
		MaterialData.addCustomBlock(this);
		this.setItemDrop(new ItemStack(this, 1));
	}

	/**
	 * Creates a GenericCustomBlock with a specified Design and metadata
	 * 
	 * @param addon creating the block
	 * @param name of the block
	 * @param isOpaque true if you want the block solid
	 * @param design to use for the block
	 */
	public GenericCustomBlock(Addon addon, String name, boolean isOpaque, BlockDesign design) {
		this(addon, name, isOpaque);
		setBlockDesign(design);
	}
	
	/**
	 * Creates a basic GenericCustomblock with no design that is opaque/solid.
	 * 
	 * @param plugin creating the block
	 * @param name of the block
	 */
	public GenericCustomBlock(Addon addon, String name) {
		this(addon, name, true);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		if (item != null) {
			item.setName(name);
		}
	}

	public BlockDesign getBlockDesign() {
		return getBlockDesign(0);
	}

	public CustomBlock setBlockDesign(BlockDesign design) {
		return setBlockDesign(design, 0);
	}

	public BlockDesign getBlockDesign(int id) {
		return design[id + 128];
	}

	public CustomBlock setBlockDesign(BlockDesign design, int id) {
		this.design[id + 128] = design;
		return this;
	}
	
	public boolean isOpaque() {
		return opaque;
	}
	
	public Block setOpaque(boolean opaque) {
		this.opaque = opaque;
		return this;
	}

	public boolean hasSubtypes() {
		return true;
	}

	public int getCustomId() {
		return customId;
	}

	public String getFullName() {
		return fullName;
	}
	
	public String getNotchianName() {
		return getName();
	}

	public Addon getAddon() {
		return addon;
	}

	public CustomItem getBlockItem() {
		return item;
	}
	
	public int getRawId() {
		return this.item.getRawId();
	}
	
	public int getRawData() {
		return this.item.getCustomId();
	}
	
	public int getBlockId() {
		return this.blockId;
	}
	
	public ItemStack getItemDrop() {
		return drop.clone();
	}
	
	public CustomBlock setItemDrop(ItemStack item) {
		drop = item != null ? item.clone() : null;
		return this;
	}
	
	public float getHardness() {
		return hardness;
	}
	
	public CustomBlock setHardness(float hardness) {
		this.hardness = hardness;
		return this;
	}
	
	public float getFriction() {
		return friction;
	}
	
	public CustomBlock setFriction(float friction) {
		this.friction = friction;
		return this;
	}
	
	public int getLightLevel() {
		return lightLevel;
	}
	
	public CustomBlock setLightLevel(int level) {
		lightLevel = level;
		return this;
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int changedId) {
	}

	public void onBlockPlace(World world, int x, int y, int z) {
	}

	public void onBlockPlace(World world, int x, int y, int z, LivingEntity living) {
	}

	public void onBlockDestroyed(World world, int x, int y, int z) {
	}

	public void onBlockDestroyed(World world, int x, int y, int z, LivingEntity living) {
	}

	public boolean onBlockInteract(World world, int x, int y, int z, Player player) {
		return false;
	}

	public void onEntityMoveAt(World world, int x, int y, int z, Entity entity) {
	}

	public void onBlockClicked(World world, int x, int y, int z, Player player) {		
	}

	public boolean isProvidingPowerTo(World world, int x, int y, int z,	BlockFace face) {
		return false;
	}

	public boolean isIndirectlyProvidingPowerTo(World world, int x, int y, int z, BlockFace face) {
		return false;
	}

	public void readData(SpoutInputStream input) throws IOException {
		customId = input.readInt();
		setName(input.readString());
		String addonName = input.readString();
		addon = Spoutcraft.getAddonManager().getOrCreateAddon(addonName);
		fullName = addon.getDescription().getFullName() + "." + getName();
		opaque = input.readBoolean();
		setFriction(input.readFloat());
		setHardness(input.readFloat());
		setLightLevel(input.readInt());
		item = new GenericCustomItem(addon, name, customId);
		MaterialData.addCustomBlock(this);
		this.setItemDrop(new ItemStack(this, 1));
		this.blockId = isOpaque() ? 1 :20;
	}

	public int getVersion() {
		return 0;
	}
}
