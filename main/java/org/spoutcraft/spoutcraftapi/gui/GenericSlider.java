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

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.event.screen.SliderDragEvent;

public class GenericSlider extends GenericControl implements Slider {
	protected Label label = new GenericLabel();
	protected float slider = 0.5f;
	protected boolean dragged = false;

	public GenericSlider() {

	}
	
	public GenericSlider(String text) {
		label.setText(text);
	}

	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 4 + label.getNumBytes();
	}

	public int getVersion() {
		return super.getVersion() + 1;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setSliderPosition(input.readFloat());
		label.readData(input);
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
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

	@Override
	public Slider copy() {
		return ((Slider)super.copy()).setSliderPosition(getSliderPosition());
	}

	public void onSliderDrag(SliderDragEvent event) {
		
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

}
