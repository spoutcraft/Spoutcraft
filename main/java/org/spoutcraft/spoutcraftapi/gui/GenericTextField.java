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

public class GenericTextField extends GenericControl implements TextField{
	
	protected String text = "";
	protected int cursor = 0;
	protected int maxChars = 16;
	protected Color fieldColor = new Color(0, 0, 0);
	protected Color borderColor = new Color(0.625F, 0.625F, 0.625F);
	public GenericTextField() {

	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 40 + PacketUtil.getNumBytes(text);
	}
	
	public int getVersion() {
		return super.getVersion() + 1;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setCursorPosition(input.readInt());
		setFieldColor(PacketUtil.readColor(input));
		setBorderColor(PacketUtil.readColor(input));
		setMaximumCharacters(input.readInt());
		setText(PacketUtil.readString(input));
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(getCursorPosition());
		PacketUtil.writeColor(output, getFieldColor());
		PacketUtil.writeColor(output, getBorderColor());
		output.writeInt(getMaximumCharacters());
		PacketUtil.writeString(output, getText());
	}
	
	@Override
	public PopupScreen getScreen() {
		return (PopupScreen)super.getScreen();
	}

	public int getCursorPosition() {
		return cursor;
	}

	public TextField setCursorPosition(int position) {
		this.cursor = position;
		return this;
	}

	public String getText() {
		return text;
	}

	public TextField setText(String text) {
		this.text = text;
		return this;
	}
	
	public int getMaximumCharacters() {
		return maxChars;
	}
	
	public TextField setMaximumCharacters(int max) {
		this.maxChars = max;
		return this;
	}

	public Color getFieldColor() {
		return fieldColor;
	}

	public TextField setFieldColor(Color color) {
		this.fieldColor = color;
		return this;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public TextField setBorderColor(Color color) {
		this.borderColor = color;
		return this;
	}
	
	public WidgetType getType() {
		return WidgetType.TextField;
	}

	public void render() {
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}

}
