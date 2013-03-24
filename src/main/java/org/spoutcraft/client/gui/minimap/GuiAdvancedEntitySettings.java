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
package org.spoutcraft.client.gui.minimap;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map.Entry;

import net.minecraft.src.Entity;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericScrollArea;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.GuiSpoutScreen;

public class GuiAdvancedEntitySettings extends GuiSpoutScreen {
	GuiMinimapMenu parent = null;
	GenericScrollArea scroll;
	GenericButton buttonDone;
	GenericLabel title;
	LinkedList<EntityVisibilityCheckbox> checks = new LinkedList<EntityVisibilityCheckbox>();

	public GuiAdvancedEntitySettings(GuiMinimapMenu guiMinimapMenu) {
		parent = guiMinimapMenu;
	}

	@Override
	protected void createInstances() {
		title = new GenericLabel("Filter Mobs");
		buttonDone = new GenericButton("Done");
		scroll = new GenericScrollArea();
		for (Entry<Class<? extends Entity>, String> e : WatchedEntity.mobFaceTextures.entrySet()) {
			EntityVisibilityCheckbox ch = new EntityVisibilityCheckbox(e.getKey(), e.getValue());
			scroll.attachWidget("Spoutcraft", ch);
			checks.add(ch);
		}
		Collections.sort(checks, new Comparator<EntityVisibilityCheckbox>() {
			public int compare(EntityVisibilityCheckbox o1, EntityVisibilityCheckbox o2) {
				return o1.getText().compareTo(o2.getText());
			}
		});

		getScreen().attachWidgets("Spoutcraft", buttonDone, title, scroll);
	}

	@Override
	protected void layoutWidgets() {
		title.setX(width / 2 - SpoutClient.getHandle().fontRenderer.getStringWidth(title.getText()) / 2);
		title.setY(10);

		scroll.setGeometry(0, 25, width, height - 25 - 30);

		int needed = 315;
		int top = 5;
		int i = 0;
		int left = width / 2 - needed / 2;
		int center = left + 100 + 5;
		int right = center + 100 + 5;
		for (EntityVisibilityCheckbox ch : checks) {
			ch.setGeometry(0, top, 100, 20);
			switch(i%3) {
				case 0:
					ch.setX(left);
					break;
				case 1:
					ch.setX(center);
					break;
				case 2:
					ch.setX(right);
					break;
			}
			i++;
			if (i%3==0) {
				top += 22;
			}
		}
		scroll.updateInnerSize();

		buttonDone.setGeometry(width / 2 + 5, height - 25, 150, 20);
	}

	@Override
	protected void buttonClicked(Button btn) {
		if (btn == buttonDone) {
			mc.displayGuiScreen(parent);
		}
	}
}
