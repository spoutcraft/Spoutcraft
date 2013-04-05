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
package org.spoutcraft.client.gui;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericTextField;

public class GuiTextDialog extends GuiSpoutScreen {
	private String title, text;
	private GenericLabel labelTitle;
	private GenericTextField textText;
	private GenericButton buttonCancel, buttonDone;
	private DialogEventHandler handler;
	private GuiScreen parent;

	public GuiTextDialog(String title, String text, DialogEventHandler handler, GuiScreen parent) {
		this.setTitle(title);
		this.setText(text);
		this.handler = handler;
		this.parent = parent;
	}

	protected void createInstances() {
		labelTitle = new GenericLabel(title);
		textText = new GenericTextField();
		textText.setWidth(200);
		textText.setText(text);

		buttonCancel = new GenericButton("Cancel");
		buttonDone = new GenericButton("Done");

		getScreen().attachWidgets("Spoutcraft", labelTitle, textText, buttonCancel, buttonDone);
	}

	protected void layoutWidgets() {
		final int totalHeight = 11 + 5 + 20 + 5 + 20;
		int top = height / 2 - totalHeight / 2;
		labelTitle.setWidth(200).setHeight(11).setX(width / 2 - 100).setY(top);
		top += 16;
		textText.setWidth(200).setHeight(20).setX(width / 2 - 100).setY(top);
		top += 25;
		buttonCancel.setWidth(95).setHeight(20).setX(width / 2 - 100).setY(top);
		buttonDone.setWidth(100).setHeight(20).setX(width / 2).setY(top);
	}

	protected void buttonClicked(Button btn) {
		if (btn == buttonCancel) {
			handler.onCancel(this);
			mc.displayGuiScreen(parent);
		}
		if (btn == buttonDone) {
			handler.onDone(this);
			mc.displayGuiScreen(parent);
		}
	}

	public void setTitle(String title) {
		this.title = title;
		if (labelTitle != null) {
			labelTitle.setText(title);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setText(String text) {
		this.text = text;
		if (textText != null) {
			textText.setText(text); // Redundant text is redundant
		}
	}

	public String getText() {
		text = textText.getText();
		return text;
	}

	public interface DialogEventHandler {
		public void onDone(GuiTextDialog dialog);
		public void onCancel(GuiTextDialog dialog);
	}
}
