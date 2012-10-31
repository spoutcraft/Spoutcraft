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

import org.spoutcraft.api.UnsafeClass;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;

@UnsafeClass
public class GenericPopup extends GenericScreen implements PopupScreen {
	protected boolean transparent = false, pause = true;
	protected double mouseX, mouseY;

	public GenericPopup() {
	}

	public int getVersion() {
		return super.getVersion() + 0;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		this.setTransparent(input.readBoolean());
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.writeBoolean(isTransparent());
	}

	public boolean isTransparent() {
		return transparent;
	}

	public PopupScreen setTransparent(boolean value) {
		this.transparent = value;
		return this;
	}

	public WidgetType getType() {
		return WidgetType.PopupScreen;
	}

	public boolean close() {
		if (getScreen() != null) {
			if (getScreen() instanceof InGameHUD) {
				((InGameHUD) getScreen()).closePopup();
			}
		}
		return false;
	}

	public ScreenType getScreenType() {
		return ScreenType.CUSTOM_SCREEN;
	}

	public boolean isPausingGame() {
		return pause;
	}

	public void setPauseGame(boolean v) {
		pause = v;
	}
}
