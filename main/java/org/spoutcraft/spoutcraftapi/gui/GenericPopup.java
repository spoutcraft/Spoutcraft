/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.spoutcraft.spoutcraftapi.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GenericPopup extends GenericScreen implements PopupScreen{
	protected boolean transparent = false;
	protected double mouseX, mouseY;
	public GenericPopup() {
		
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 1;
	}
	
	public int getVersion() {
		return super.getVersion() + 0;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		this.setTransparent(input.readBoolean());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
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
				((InGameHUD)getScreen()).closePopup();
			}
		}
		return false;
	}

	public double getMouseX() {
		return mouseX;
	}
	
	public PopupScreen setMouseX(double mouseX) {
		this.mouseX = mouseX;
		return this;
	}

	public double getMouseY() {
		return mouseY;
	}
	
	public PopupScreen setMouseY(double mouseY) {
		this.mouseY = mouseY;
		return this;
	}

}
