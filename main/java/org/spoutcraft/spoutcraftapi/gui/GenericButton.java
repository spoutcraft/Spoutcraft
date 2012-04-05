/*
 * This file is part of SpoutcraftAPI (http://wiki.getspout.org/).
 * 
 * SpoutcraftAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutcraftAPI is distributed in the hope that it will be useful,
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
import org.spoutcraft.spoutcraftapi.UnsafeClass;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

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

	@Override
	public int getNumBytes() {
		return super.getNumBytes() + label.getNumBytes() + PacketUtil.getNumBytes(getDisabledText()) + 9;
	}

	public int getVersion() {
		return super.getVersion() + 4;
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

	public void onButtonClick(ButtonClickEvent event) {
		
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
