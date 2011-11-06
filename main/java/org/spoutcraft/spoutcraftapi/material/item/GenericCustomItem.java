package org.spoutcraft.spoutcraftapi.material.item;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.BlockFace;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.inventory.MaterialManager;
import org.spoutcraft.spoutcraftapi.material.CustomItem;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

public class GenericCustomItem extends GenericItem implements CustomItem {
	public static MaterialManager mm;
	private final String fullName;
	private final Addon addon;
	private final int customId;
	public String texture;

	public GenericCustomItem(MaterialManager manager, Addon addon, String name, int customId) {
		super(name, 318, customId);
		mm = manager;
		this.fullName = addon.getDescription().getName() + name;
		this.customId = customId;
		this.addon = addon;
		this.setName(name);
		MaterialData.addCustomItem(this);
	}

	public GenericCustomItem(Addon addon, String name) {
		this(Spoutcraft.getClient().getMaterialManager(), addon, name, mm.registerCustomItemName(addon, addon.getDescription().getName() + name));
	}

	public GenericCustomItem(Addon addon, String name, String texture) {
		this(addon, name);
		this.setTexture(texture);
	}

	@Override
	public void setName(String name) {
		super.setName(name);
		mm.setItemName(this, name);
	}

	public int getCustomId() {
		return customId;
	}

	public String getFullName() {
		return fullName;
	}

	public Addon getAddon() {
		return addon;
	}

	public CustomItem setTexture(String texture) {
		this.texture = texture;
		mm.setItemTexture(this, addon, texture);
		return this;
	}

	public String getTexture() {
		return texture;
	}

	public boolean onItemInteract(Player player, Block block, BlockFace face) {
		return true;
	}

}
