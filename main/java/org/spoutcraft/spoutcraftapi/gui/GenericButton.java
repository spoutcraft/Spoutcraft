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
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class GenericButton extends GenericControl implements Button {

	protected GenericLabel label = new GenericLabel();
	protected String disabledText = "";
	protected Color hoverColor = new Color(1, 1, 0.627F);
	public GenericButton() {
		
	}
	
	public GenericButton(String text) {
		setText(text);
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + label.getNumBytes() + PacketUtil.getNumBytes(getDisabledText()) + 16;
	}
	
	public int getVersion() {
		return super.getVersion() + 1;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		label.readData(input);
		setDisabledText(PacketUtil.readString(input));
		setHoverColor(PacketUtil.readColor(input));
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		label.writeData(output);
		PacketUtil.writeString(output, getDisabledText());
		PacketUtil.writeColor(output, getHoverColor());
	}
	
	@Override
	public PopupScreen getScreen() {
		return (PopupScreen)super.getScreen();
	}

	@Override
	public String getText() {
		return label.getText();
	}

	@Override
	public Label setText(String text) {
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
	
	@Override
	public WidgetAnchor getAlign() {
		return label.getAlign();
	}
	
	@Override
	public Widget setAlign(WidgetAnchor pos) {
		label.setAlign(pos);
		return this;
	}

	@Override
	public Color getTextColor() {
		return label.getTextColor();
	}

	@Override
	public Label setTextColor(Color color) {
		label.setTextColor(color);
		return this;
	}

	@Override
	public String getDisabledText() {
		return disabledText;
	}

	@Override
	public Button setDisabledText(String text) {
		disabledText = text;
		return this;
	}
	
	@Override
	public Color getHoverColor() {
		return hoverColor;
	}
	
	@Override
	public Button setHoverColor(Color color) {
		this.hoverColor = color;
		return this;
	}
	
	@Override
	public WidgetType getType() {
		return WidgetType.Button;
	}
	
	@Override
	public void render() {
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}

	@Override
	public Label setAuto(boolean auto) {
		label.setAuto(auto);
		return this;
	}

	@Override
	public boolean getAuto() {
		return label.getAuto();
	}

}
