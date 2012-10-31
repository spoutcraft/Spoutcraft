/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spoutcraft.api.gui;

import java.io.IOException;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;

public abstract class GenericControl extends GenericWidget implements Control{
	protected boolean enabled = true;
	protected Color color = new Color(0.878F, 0.878F, 0.878F);
	protected Color disabledColor = new Color(0.625F, 0.625F, 0.625F);
	protected boolean focus = false;

	public GenericControl() {
	}

	public int getVersion() {
		return super.getVersion() + 3;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		setEnabled(input.readBoolean());
		setColor(input.readColor());
		setDisabledColor(input.readColor());
		setFocus(input.readBoolean());
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.writeBoolean(isEnabled());
		output.writeColor(getColor());
		output.writeColor(getDisabledColor());
		output.writeBoolean(isFocus());
	}

	public boolean isEnabled() {
		return enabled;
	}

	public Control setEnabled(boolean enable) {
		enabled = enable;
		return this;
	}

	public Color getColor() {
		return color;
	}

	public Control setColor(Color color) {
		this.color = color;
		return this;
	}

	public Color getDisabledColor() {
		return disabledColor;
	}

	public Control setDisabledColor(Color color) {
		this.disabledColor = color;
		return this;
	}

	public boolean isFocus() {
		return focus;
	}

	public Control setFocus(boolean focus) {
		if (this.focus != focus) {
			this.focus = focus;
			Spoutcraft.getWidgetManager().sendFocusUpdate(this, focus);
		}
		return this;
	}

	@Override
	public Control copy() {
		return ((Control)super.copy()).setEnabled(isEnabled()).setColor(getColor().clone()).setDisabledColor(getDisabledColor().clone());
	}

	public boolean onKeyPressed(Keyboard key) {
		return false;
	}

	public boolean onKeyReleased(Keyboard key) {
		return false;
	}
}
