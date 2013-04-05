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
public class GenericTextField extends GenericControl implements TextField {
	public static final int PADDING = 4;
	public static final int LINE_HEIGHT = 10;
	public static final int LINE_SPACING = 2;
	private static final char MASK_MAXLINES = 0x7F; // bits 1-7
	private static final char MASK_TABINDEX = 0x3F80; // bits 8-4
	private static final char FLAG_PASSWORD = 0x4000; // bit 15
	//private static final char FLAG_FOCUS = 0x8000; // bit 16 focus is already set in Control.
	protected boolean password = false;
	protected TextProcessor textProcessor;
	protected int tabIndex = 0;
	protected Color fieldColor = new Color(0, 0, 0);
	protected Color borderColor = new Color(0.625F, 0.625F, 0.625F);
	protected String placeholder = "";

	public GenericTextField() {
		this.textProcessor = new GenericTextProcessor();
	}

	@Override
	public int getVersion() {
		return super.getVersion() + 3;
	}

	public String getActualText() {
		return textProcessor.getText().length() == 0 ? getPlaceholder() : textProcessor.getText();
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		setFieldColor(input.readColor());
		setBorderColor(input.readColor());
		char c = input.readChar();
		setPasswordField((c & FLAG_PASSWORD) > 0);
		setMaximumLines(c & MASK_MAXLINES);
		setTabIndex((c & MASK_TABINDEX) >>> 7);
		setCursorPosition(input.readChar());
		setMaximumCharacters(input.readChar());
		setText(input.readString());
		setPlaceholder(input.readString());
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.writeColor(getFieldColor());
		output.writeColor( getBorderColor());
		output.writeChar((char) (getMaximumLines() & MASK_MAXLINES | (getTabIndex() << 7) & MASK_TABINDEX | (isPasswordField() ? FLAG_PASSWORD : 0)));
		output.writeChar((char) getCursorPosition());
		output.writeChar((char) getMaximumCharacters());
		output.writeString(getText());
		output.writeString(getPlaceholder());
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
		processor.setWidth(getTextProcessor().getWidth());
		processor.setText(getTextProcessor().getText());
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
			setTextProcessor((password) ? new PasswordTextProcessor() : new GenericTextProcessor());
		}
		this.password = password;
		return this;
	}

	@Override
	public Widget setWidth(int width) {
		textProcessor.setWidth(Math.max(0, width - (PADDING << 1)));
		return super.setWidth(width);
	}

	@Override
	public Control setFocus(boolean focus) {
		if (focus) {
			Keyboard.setRepeatingEvents(true);
		}
		return super.setFocus(focus);
	}

	@Override
	public TextField copy() {
		// ignore focus parameter which would lead to strange behaviour!
		return ((TextField) super.copy()).setText(getText()).setCursorPosition(getCursorPosition()).setMaximumCharacters(getMaximumCharacters()).setFieldColor(getFieldColor()).setBorderColor(getBorderColor()).setMaximumLines(getMaximumLines()).setTabIndex(getTabIndex()).setPasswordField(isPasswordField()).setPlaceholder(getPlaceholder());
	}

	public void onTypingFinished() {
	}

	public TextField setPlaceholder(String text) {
		placeholder = text;
		return this;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	@Override
	public Widget setGeometry(Rectangle geometry) {
		textProcessor.setWidth(geometry.getWidth());
		return super.setGeometry(geometry);
	}

	@Override
	public Widget setGeometry(int x, int y, int width, int height) {
		textProcessor.setWidth(width);
		return super.setGeometry(x, y, width, height);
	}

	@Override
	public void onTextFieldChange() {
	}
}
