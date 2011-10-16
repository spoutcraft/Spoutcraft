/*
 * This file is part of Spoutcraft API (http://wiki.getspout.org/).
 * 
 * Spoutcraft API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.gui;

import java.util.UUID;

import org.spoutcraft.spoutcraftapi.Spoutcraft;

public class ExpBar extends GenericWidget {

	public ExpBar() {
		super();
		setX(427 / 2 - 91); // 122
		setY(211);
		setAnchor(WidgetAnchor.BOTTOM_CENTER);
	}

	@Override
	public double getScreenX() {
		double mid = getScreen() != null ? getScreen().getWidth() / 2 : 427 / 2D;
		double diff = super.getScreenX() - mid - 31;
		return getScreen() != null ? getScreen().getWidth() / 2D - diff : this.getX();
	}

	@Override
	public double getScreenY() {
		int diff = (int) (240 - this.getY());
		return getScreen() != null ? getScreen().getHeight() - diff : this.getY();
	}

	@Override
	public UUID getId() {
		return new UUID(0, 6);
	}

	public WidgetType getType() {
		return WidgetType.ExpBar;
	}

	public void render() {
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}

	@Override
	public int getVersion() {
		return super.getVersion() + 1;
	}
}
