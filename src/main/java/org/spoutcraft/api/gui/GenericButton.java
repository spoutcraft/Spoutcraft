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
public class GenericButton extends GenericControl implements Button {
	protected GenericLabel label = new GenericLabel();
	protected String disabledText = "";
	protected Color hoverColor = new Color(1f, 1f, 0.627F);
	protected float scale = 1.0F;
	protected int innerWidth = 0;
	public GenericButton() {
		label.setAlign(WidgetAnchor.TOP_CENTER);
	}

	public GenericButton(String text) {
		label.setAlign(WidgetAnchor.TOP_CENTER);
		setText(text);
	}

	public int getVersion() {
		return super.getVersion() + 4;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		label.readData(input);
		setDisabledText(input.readString());
		setHoverColor(input.readColor());
		scale = input.readFloat();
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		label.writeData(output);
		output.writeString(getDisabledText());
		output.writeColor(getHoverColor());
		output.writeFloat(scale);
	}

	public String getText() {
		return label.getText();
	}

	public Button setText(String text) {
		label.setText(text);
		return this;
	}

	public WidgetAnchor getAlign() {
		return label.getAlign();
	}

	public Button setAlign(WidgetAnchor pos) {
		label.setAlign(pos);
		return this;
	}

	public Color getTextColor() {
		return this.getColor();
	}

	public Button setTextColor(Color color) {
		return (Button) this.setColor(color);
	}

	public String getDisabledText() {
		return disabledText;
	}

	public Button setDisabledText(String text) {
		disabledText = text;
		return this;
	}

	public Color getHoverColor() {
		return hoverColor;
	}

	public Button setHoverColor(Color color) {
		this.hoverColor = color;
		return this;
	}

	public WidgetType getType() {
		return WidgetType.Button;
	}

	public void render() {
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}

	public Button setInnerWidth(int width) {
		innerWidth = width;
		return this;
	}

	public double getInnerWidth() {
		return innerWidth;
	}

	public Button setAuto(boolean auto) {
		label.setAuto(auto);
		return this;
	}

	public boolean isAuto() {
		return label.isAuto();
	}

	public Button copy() {
		return ((Button)super.copy()).setDisabledText(getDisabledText()).setText(getText()).setAuto(isAuto()).setTextColor(getTextColor()).setHoverColor(getHoverColor());
	}

	public void onButtonClick() {
	}

	public Label setScale(float scale) {
		this.scale = scale;
		return this;
	}

	public float getScale() {
		return scale;
	}

	@Override
	public Widget setWidth(int width) {
		label.setWidth(width);
		return super.setWidth(width);
	}

	public Label setShadow(boolean shadow) {
		label.setShadow(shadow);
		return this;
	}

	public boolean hasShadow() {
		return label.hasShadow();
	}
}
