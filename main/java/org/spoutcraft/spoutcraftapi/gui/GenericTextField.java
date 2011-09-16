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


import org.lwjgl.input.Keyboard;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class GenericTextField extends GenericControl implements TextField{
	
	public static final int PADDING = 4;
	public static final int LINE_HEIGHT = 10;
	public static final int LINE_SPACING = 2;
	
	private static final char MASK_MAXLINES = 0x7F; // bits 1–7
	private static final char MASK_TABINDEX = 0x3F80; // bits 8–14
	private static final char FLAG_PASSWORD = 0x4000; // bit 15
	private static final char FLAG_FOCUS = 0x8000; // bit 16
		
	protected boolean password = false;
	protected TextProcessor textProcessor;
	protected int tabIndex = 0;
	protected Color fieldColor = new Color(0, 0, 0);
	protected Color borderColor = new Color(0.625F, 0.625F, 0.625F);
	
	public GenericTextField() {
		this.textProcessor = new GenericTextProcessor();
	}

	public int getNumBytes() {
		return super.getNumBytes() + 16 + PacketUtil.getNumBytes(textProcessor.getText());
	}

	public int getVersion() {
		return super.getVersion() + 1;
	}

	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setFieldColor(PacketUtil.readColor(input));
		setBorderColor(PacketUtil.readColor(input));
		char c = input.readChar();
		setPasswordField((c & FLAG_PASSWORD) > 0);
		setMaximumLines(c & MASK_MAXLINES);
		setTabIndex((c & MASK_TABINDEX) >>> 7);
		setFocus((c & FLAG_FOCUS) > 0);
		setCursorPosition(input.readChar());
		setMaximumCharacters(input.readChar());
		setText(PacketUtil.readString(input));
	}

	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		PacketUtil.writeColor(output, getFieldColor());
		PacketUtil.writeColor(output, getBorderColor());
		output.writeChar((char) (getMaximumLines() & MASK_MAXLINES | (getTabIndex() << 7) & MASK_TABINDEX  | (isPasswordField() ? FLAG_PASSWORD : 0) | (isFocus() ? FLAG_FOCUS : 0)));
		output.writeChar(getCursorPosition());
		output.writeChar(getMaximumCharacters());
		PacketUtil.writeString(output, getText());
	}
	
	public PopupScreen getScreen() {
		return (PopupScreen) super.getScreen();
	}

	public int getCursorPosition() {
		return textProcessor.getCursor();
	}

	public TextField setCursorPosition(int position) {
		textProcessor.setCursor(position);
		return this;
	}

	public String getText() {
		return textProcessor.getText();
	}

	public TextField setText(String text) {
		textProcessor.setText(text);
		return this;
	}

	public int getMaximumCharacters() {
		return textProcessor.getMaximumCharacters();
	}

	public TextField setMaximumCharacters(int max) {
		textProcessor.setMaximumCharacters(Math.max(0, max));
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

	public TextProcessor getTextProcessor() {
		return textProcessor;
	}

	public TextField setTextProcessor(TextProcessor processor) {
		this.textProcessor = processor;
		return this;
	}

	public int getMaximumLines() {
		return textProcessor.getMaximumLines();
	}

	public TextField setMaximumLines(int max) {
		// if max equals 0 calculate how many lines this text field can hold
		textProcessor.setMaximumLines((max > 0) ? max : (int) Math.floor((getHeight() - (PADDING << 1) + LINE_SPACING) / (LINE_HEIGHT + LINE_SPACING)));
		return this;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public TextField setTabIndex(int index) {
		this.tabIndex = index;
		return this;
	}

	public boolean isPasswordField() {
		return password;
	}

	public TextField setPasswordField(boolean password) {
		if (this.password != password) {
			TextProcessor processor = (password) ? new PasswordTextProcessor() : new GenericTextProcessor();
			processor.setWidth(getTextProcessor().getWidth());
			processor.setText(getTextProcessor().getText());
			setTextProcessor(processor);
		}
		this.password = password;
		return this;
	}

	public Widget setWidth(int width) {
		textProcessor.setWidth(Math.max(0, width - 2* PADDING));
		return super.setWidth(width);
	}

	public Control setFocus(boolean focus) {
		if (focus) Keyboard.enableRepeatEvents(true);
		return super.setFocus(focus);
	}
}
