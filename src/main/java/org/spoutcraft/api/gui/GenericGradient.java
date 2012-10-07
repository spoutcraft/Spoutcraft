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
import org.spoutcraft.api.UnsafeClass;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;

@UnsafeClass
public class GenericGradient extends GenericWidget implements Gradient {
	protected Color color1 = new Color(0.06F, 0.06F, 0.06F, 0.75F), color2 = new Color(0.06F, 0.06F, 0.06F, 0.82F);
	protected Orientation axis = Orientation.VERTICAL;

	public GenericGradient() {
	}

	public Gradient setTopColor(Color color) {
		this.color1 = color;
		return this;
	}

	public Gradient setBottomColor(Color color) {
		this.color2 = color;
		return this;
	}

	public Color getTopColor() {
		return this.color1;
	}

	public Color getBottomColor() {
		return this.color2;
	}

	public WidgetType getType() {
		return WidgetType.Gradient;
	}

	@Override
	public int getVersion() {
		return super.getVersion() + 2;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		this.setTopColor(input.readColor());
		this.setBottomColor(input.readColor());
		this.setOrientation(Orientation.getOrientationFromId(input.read()));
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.writeColor(getTopColor());
		output.writeColor(getBottomColor());
		output.write(getOrientation().getId());
	}

	@Override
	public Gradient copy() {
		return ((Gradient)super.copy()).setTopColor(getTopColor()).setBottomColor(getBottomColor());
	}
	public void render() {
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}

	public Gradient setOrientation(Orientation axis) {
		this.axis = axis;
		return this;
	}

	public Orientation getOrientation() {
		return axis;
	}
}
