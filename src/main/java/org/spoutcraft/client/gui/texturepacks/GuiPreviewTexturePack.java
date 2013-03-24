/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.gui.texturepacks;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.Item;
import net.minecraft.src.StringTranslate;

import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericItemWidget;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericScrollArea;
import org.spoutcraft.api.gui.ItemWidget;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.api.gui.ScrollArea;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.api.inventory.ItemStack;
import org.spoutcraft.client.gui.GuiSpoutScreen;

public class GuiPreviewTexturePack extends GuiSpoutScreen {
	private GuiScreen parent;
	private GenericButton buttonDone;
	private ScrollArea scroll;
	private Label title;

	private List<ItemWidget> previewIcons = new LinkedList<ItemWidget>();

	public GuiPreviewTexturePack(GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	protected void createInstances() {
		StringTranslate t = StringTranslate.getInstance();

		buttonDone = new GenericButton(t.translateKey("gui.done", "Done"));
		scroll = new GenericScrollArea();
		title = new GenericLabel(t.translateKey("spout.texturepack.preview.title", "Texture Pack Preview"));
		getScreen().attachWidgets("Spoutcraft", scroll, buttonDone, title);

		for (Item item : Item.itemsList) {
			if (item == null) {
				continue;
			}
			GenericItemWidget icon = new GenericItemWidget(new ItemStack(item.itemID));
			previewIcons.add(icon);
		}
		scroll.attachWidgets("Spoutcraft", previewIcons.toArray(new Widget[0]));
	}

	@Override
	protected void layoutWidgets() {
		int top = 10;

		int swidth = mc.fontRenderer.getStringWidth(title.getText());
		title.setY(top).setX(width / 2 - swidth / 2).setHeight(11).setWidth(swidth);

		scroll.setGeometry(5, title.getY() + 16, width - 10, height - title.getY() - 16 - 5 - 5 - 20);

		buttonDone.setGeometry(width / 2 - 50, height - 25, 100, 20);

		int SIZE = 32;

		int fitting = (width - 26) / (SIZE + 5);

		int i = 0;
		int line = 0;
		for (ItemWidget icon : previewIcons) {
			icon.setGeometry(0, 0, SIZE, SIZE);
			icon.setX(i % fitting * (SIZE + 5) + 5);
			icon.setY(line * (SIZE + 5) + 5);
			if (i % fitting == fitting - 1) {
				line ++;
			}
			i++;
		}
		scroll.updateInnerSize();
	}

	@Override
	protected void buttonClicked(Button btn) {
		if (btn == buttonDone) {
			mc.displayGuiScreen(parent);
		}
	}
}
