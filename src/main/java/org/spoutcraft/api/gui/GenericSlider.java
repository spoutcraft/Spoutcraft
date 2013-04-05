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
package org.spoutcraft.api.gui;

import java.io.IOException;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.UnsafeClass;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;

@UnsafeClass
public class GenericSlider extends GenericControl implements Slider {
	protected Label label = new GenericLabel();
	protected float slider = 0.5f;
	protected boolean dragged = false;

	public GenericSlider() {
	}

	public GenericSlider(String text) {
		label.setText(text);
	}

	public int getVersion() {
		return super.getVersion() + 2;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		setSliderPosition(input.readFloat());
		label.readData(input);
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.writeFloat(getSliderPosition());
		label.writeData(output);
	}

	public float getSliderPosition() {
		return slider;
	}

	public Slider setSliderPosition(float value) {
		if (value > 1f) {
			value = 1f;
		} else if (value < 0f) {
			value = 0f;
		}
		slider = value;
		return this;
	}

	@Override
	public double getScreenX() {
		return super.getScreenX();
	}

	@Override
	public double getScreenY() {
		return super.getScreenY();
	}

	public WidgetType getType() {
		return WidgetType.Slider;
	}

	public void render() {
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}

	public boolean isDragging() {
		return dragged;
	}

	public Slider setDragging(boolean dragged) {
		this.dragged = dragged;
		return this;
	}

	public void onSliderDrag(float oldPos, float newPos) {
	}

	@Override
	public Slider copy() {
		return ((Slider)super.copy()).setSliderPosition(getSliderPosition());
	}

	public String getText() {
		return label.getText();
	}

	public Color getTextColor() {
		return label.getTextColor();
	}

	public boolean isAuto() {
		return label.isAuto();
	}

	public WidgetAnchor getAlign() {
		return label.getAlign();
	}

	public Label setScale(float scale) {
		label.setScale(scale);
		return this;
	}

	public float getScale() {
		return label.getScale();
	}

	public Slider setText(String text) {
		label.setText(text);
		return this;
	}

	public Slider setTextColor(Color color) {
		label.setTextColor(color);
		return this;
	}

	public Slider setAuto(boolean auto) {
		label.setAuto(auto);
		return this;
	}

	public Slider setAlign(WidgetAnchor align) {
		label.setAlign(align);
		return this;
	}

	public Label setShadow(boolean shadow) {
		label.setShadow(shadow);
		return this;
	}

	public boolean hasShadow() {
		return label.hasShadow();
	}
}
