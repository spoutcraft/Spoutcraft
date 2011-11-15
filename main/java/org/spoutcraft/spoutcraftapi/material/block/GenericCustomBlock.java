package org.spoutcraft.spoutcraftapi.material.block;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.block.design.BlockDesign;
import org.spoutcraft.spoutcraftapi.block.design.GenericBlockDesign;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;
import org.spoutcraft.spoutcraftapi.inventory.MaterialManager;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.CustomItem;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.material.item.GenericCustomItem;

public abstract class GenericCustomBlock extends GenericBlock implements CustomBlock {
	public BlockDesign design = new GenericBlockDesign();
	public static MaterialManager mm;
	private final String fullName;
	private final int customID;
	private final Addon addon;
	private final CustomItem item;
	private final int blockId;
	public int customMetaData = 0;
	private float hardness = 1.5F;
	private float friction = 0.6F;
	private int lightLevel = 0;

	/**
	 * Creates a GenericCustomBlock with no model yet.
	 * 
	 * @param plugin creating the block
	 * @param name of the block
	 * @param isOpaque true if you want the block solid
	 * @param material manager
	 */
	public GenericCustomBlock(Addon addon, String name, boolean isOpaque, MaterialManager manager) {
		this(addon, name, isOpaque, new GenericCustomItem(addon, name), manager);
	}
	
	public GenericCustomBlock(Addon addon, String name, boolean isOpaque, CustomItem item, MaterialManager manager) {
		super(name, isOpaque ? 1 : 20);
		mm = manager;
		this.blockId = isOpaque ? 1 :20;
		this.addon = addon;
		this.item = item;
		this.fullName = item.getFullName();
		this.customID = item.getCustomId();
		MaterialData.addCustomBlock(this);
		this.setItemDrop(mm.getCustomItemStack(this, 1));
	}
	
	/**
	 * Creates a GenericCustomBlock with no model yet.
	 * 
	 * @param plugin creating the block
	 * @param name of the block
	 * @param isOpaque true if you want the block solid
	 */
	public GenericCustomBlock(Addon addon, String name, boolean isOpaque) {
		this(addon, name, isOpaque, Spoutcraft.getClient().getMaterialManager());
	}

	/**
	 * Creates a GenericCustomBlock with a specified Design and metadata
	 * 
	 * @param plugin creating the block
	 * @param name of the block
	 * @param isOpaque true if you want the block solid
	 * @param design to use for the block
	 * @param customMetaData for the block
	 */
	public GenericCustomBlock(Addon addon, String name, boolean isOpaque, BlockDesign design, int customMetaData) {
		this(addon, name, isOpaque);
		this.customMetaData = customMetaData;
		setBlockDesign(design);
	}
	
	/**
	 * Creates a GenericCustomBlock with a specified Design and metadata
	 * 
	 * @param plugin creating the block
	 * @param name of the block
	 * @param isOpaque true if you want the block solid
	 * @param design to use for the block
	 * @param customMetaData for the block
	 * @param material manager
	 */
	public GenericCustomBlock(Addon addon, String name, boolean isOpaque, BlockDesign design, int customMetaData, MaterialManager manager) {
		this(addon, name, isOpaque, manager);
		this.customMetaData = customMetaData;
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
	
	@Override
	public void setName(String name) {
		super.setName(name);
		item.setName(name);
	}

	public BlockDesign getBlockDesign() {
		return design;
	}

	public CustomBlock setBlockDesign(BlockDesign design) {
		this.design = design;
		mm.setCustomBlockDesign(this, design);
		mm.setCustomBlockDesign(this.getBlockItem(), design);
		mm.setCustomItemBlock(item, this);

		return this;
	}

	public int getCustomId() {
		return customID;
	}

	public String getFullName() {
		return fullName;
	}

	public CustomBlock setCustomMetaData(int customMetaData) {
		this.customMetaData = customMetaData;

		return this;
	}

	public int getCustomMetaData() {
		return customMetaData;
	}

	public Addon getAddon() {
		return addon;
	}

	public CustomItem getBlockItem() {
		return item;
	}
	
	@Override
	public int getRawId() {
		return this.item.getRawId();
	}
	
	@Override
	public int getRawData() {
		return this.item.getCustomId();
	}
	
	public int getBlockId() {
		return this.blockId;
	}
	
	public CustomBlock setItemDrop(ItemStack item) {
		mm.registerItemDrop(this, item);
		return this;
	}
	
	@Override
	public float getHardness() {
		return hardness;
	}
	
	@Override
	public CustomBlock setHardness(float hardness) {
		this.hardness = hardness;
		return this;
	}
	
	@Override
	public float getFriction() {
		return friction;
	}
	
	@Override
	public CustomBlock setFriction(float friction) {
		this.friction = friction;
		return this;
	}
	
	@Override
	public int getLightLevel() {
		return lightLevel;
	}
	
	@Override
	public CustomBlock setLightLevel(int level) {
		lightLevel = level;
		return this;
	}
}
