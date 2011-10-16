package org.spoutcraft.spoutcraftapi.material.item;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.material.Item;

public class GenericItem implements Item {

	private final int id;
	private final int data;
	private final boolean subtypes;

	public GenericItem(int id, int data, boolean subtypes) {
		this.id = id;
		this.data = data;
		this.subtypes = subtypes;
	}

	protected GenericItem(int id, int data) {
		this(id, data, false);
	}

	public GenericItem(int id) {
		this(id, 0, false);
	}

	public int getRawId() {
		return id;
	}

	public int getRawData() {
		return data;
	}

	public boolean hasSubtypes() {
		return subtypes;
	}

	public String getName() {
		return Spoutcraft.getClient().getItemManager().getItemName(id, (short) data);
	}

	public void setName(String name) {
		Spoutcraft.getClient().getItemManager().setItemName(id, (short) data, name);
	}

}
