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
package org.spoutcraft.client.controls;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.Item;

import org.spoutcraft.api.ChatColor;
import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericListWidget;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.api.gui.ListWidget;
import org.spoutcraft.api.gui.ListWidgetItem;
import org.spoutcraft.api.gui.RenderUtil;
import org.spoutcraft.api.keyboard.AbstractBinding;
import org.spoutcraft.client.gui.GuiSpoutScreen;
import org.spoutcraft.client.gui.ScreenUtil;

public class GuiAmbigousInput extends GuiSpoutScreen {
	private Label title;
	private Button buttonCancel;
	private GenericListWidget list;
	protected GuiScreen parent;
	private ArrayList<AbstractBinding> bindings;
	private AbstractBinding pressed = null;

	public GuiAmbigousInput(ArrayList<AbstractBinding> bindings, GuiScreen parent) {
		this.parent = parent;
		this.bindings = bindings;
	}

	protected void createInstances() {
		title = new GenericLabel("Ambigous bindings\n" + ChatColor.GRAY + "The key you pressed has multiple bindings assigned.\n" + ChatColor.GRAY + "Please choose which action you want to summon.");
		buttonCancel = new GenericButton("Cancel");
		list = new GenericListWidget();

		int i = 1;
		for (AbstractBinding binding:bindings) {
			list.addItem(new BindingItem(i, binding));
			i++;
		}

		getScreen().attachWidgets("Spoutcraft", title, buttonCancel, list);
	}

	protected void layoutWidgets() {
		int swidth = mc.fontRenderer.getStringWidth(title.getText());
		title.setGeometry(5, 7, width - 10, 0);
		int top = 5 + 11 * 3 + 5;

		list.setGeometry(5, top, width - 10, height - top - 30);

		top += list.getHeight();

		buttonCancel.setGeometry(width - 205, top + 5, 200, 20);

		list.setFocus(true);
		list.setSelection(0);
	}

	@Override
	protected void keyTyped(char var1, int var2) {
		super.keyTyped(var1, var2);
		try {
			int i = Integer.valueOf(var1 + "");
			if (i == 0) {
				i = 10;
			}
			if (bindings.size() >= i) {
				AbstractBinding binding = bindings.get(i - 1);
				summon(binding);
			}
		} catch(IllegalArgumentException e) {}
	}

	@Override
	public void buttonClicked(Button btn) {
		if (btn == buttonCancel) {
			mc.displayGuiScreen(parent);
		}
	}

	public class BindingItem implements ListWidgetItem {
		private AbstractBinding binding;
		private int index;

		public BindingItem(int index, AbstractBinding binding) {
			this.binding = binding;
			this.index = index;
		}

		public void setListWidget(ListWidget widget) {
			// TODO Auto-generated method stub

		}

		public ListWidget getListWidget() {
			// TODO Auto-generated method stub
			return null;
		}

		public int getHeight() {
			return 13;
		}

		public void render(int x, int y, int width, int height) {
			if (index <= 10) {
				if (pressed != binding) {
					RenderUtil.drawRectangle(x + 1, y + 1, x + 12, y + 12, 0xff888888);
					RenderUtil.drawGradientRectangle(x + 2, y + 2, x + 11, y + 11, 0xffffffff, 0xffaaaaaa);
					mc.fontRenderer.drawString(index + "", x + 4, y + 3, 0xff333333);
				} else {
					RenderUtil.drawRectangle(x + 1, y + 1, x + 12, y + 12, 0xff333333);
					RenderUtil.drawGradientRectangle(x + 2, y + 2, x + 11, y + 11, 0xff666666, 0xffaaaaaa);
					mc.fontRenderer.drawString(index + "", x + 4, y + 3, 0xff000000);
				}
			}
			mc.fontRenderer.drawString(binding.getTitle(), x + 15, y + 3, 0xffffffff);
		}

		public void onClick(int x, int y, boolean doubleClick) {
			if (x == -1 && y == -1 && !doubleClick) {
				return;
			}
			summon(binding);
			if (!doubleClick) {
				Spoutcraft.getActivePlayer().showAchievement(ChatColor.WHITE + "You can use 1-9", "on your keyboard too", Item.book.itemID);
			}
		}

		public AbstractBinding getBinding() {
			return binding;
		}

	}

	protected void summon(final AbstractBinding binding) {
		if (pressed != null) {
			return;
		}
		Timer t = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				mc.displayGuiScreen(parent);
				binding.summon(binding.getKey(), false, ScreenUtil.getType(parent).getCode());
				binding.summon(binding.getKey(), true, ScreenUtil.getType(parent).getCode());
			}
		};
		t.schedule(task, 200);
		pressed  = binding;
	}
}
