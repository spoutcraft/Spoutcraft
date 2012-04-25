package org.spoutcraft.spoutcraftapi.gui;

import java.io.IOException;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;

public class GenericSlot extends GenericControl implements Slot {
	private ItemStack stack = new ItemStack(0);
	private int depth = 16;

	public WidgetType getType() {
		return WidgetType.Slot;
	}

	public void render() {
		Spoutcraft.getRenderDelegate().render(this);
	}

	public ItemStack getItem() {
		if(stack == null) {
			stack = new ItemStack(0);
		}
		return stack.clone();
	}

	public Slot setItem(ItemStack item) {
		if(item == null) {
			stack = new ItemStack(0);
			setTooltip("");
			return this;
		}
		stack = item.clone();
		setTooltip(Spoutcraft.getMaterialManager().getToolTip(stack));
		return this;
	}

	public boolean onItemPut(ItemStack item) {
		return true;
	}

	public boolean onItemTake(ItemStack item) {
		return true;
	}

	public void onItemShiftClicked() {
	}

	public boolean onItemExchange(ItemStack current, ItemStack cursor) {
		return true;
	}

	public int getDepth() {
		return depth;
	}

	public Slot setDepth(int depth) {
		this.depth = depth;
		return this;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		setItem(new ItemStack(input.readInt(), (int) input.readShort(), input.readShort()));
		depth = input.readInt();
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(stack.getTypeId());
		output.writeShort((short)stack.getAmount());
		output.writeShort(stack.getDurability());
		output.writeInt(depth);
	}
}
