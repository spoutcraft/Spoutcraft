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
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class GenericButton extends GenericControl implements Button {

	protected GenericLabel label = new GenericLabel();
	protected String disabledText = "";
	protected Color hoverColor = new Color(1f, 1f, 0.627F);
	protected float scale = 1.0F;

	public GenericButton() {

	}

	public GenericButton(String text) {
		setText(text);
	}

	@Override
	public int getNumBytes() {
		return super.getNumBytes() + label.getNumBytes() + PacketUtil.getNumBytes(getDisabledText()) + 9;
	}

	public int getVersion() {
		return super.getVersion() + 3;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		label.readData(input);
		setDisabledText(PacketUtil.readString(input));
		setHoverColor(PacketUtil.readColor(input));
		scale = input.readFloat();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		label.writeData(output);
		PacketUtil.writeString(output, getDisabledText());
		PacketUtil.writeColor(output, getHoverColor());
		output.writeFloat(scale);
	}

	public String getText() {
		return label.getText();
	}

	public Button setText(String text) {
		label.setText(text);
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

	public WidgetAnchor getAlign() {
		return label.getAlign();
	}

	public Widget setAlign(WidgetAnchor pos) {
		label.setAlign(pos);
		return this;
	}

	public Color getTextColor() {
		return label.getTextColor();
	}

	public Button setTextColor(Color color) {
		label.setTextColor(color);
		return this;
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

	public void onButtonClick(ButtonClickEvent event) {
		
	}

	public Label setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	public float getScale() {
		return scale;
	}
}
