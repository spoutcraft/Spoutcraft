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

import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.FontRenderer;

public class PasswordTextProcessor implements TextProcessor
{
	protected static final char CHAR_ASTERISK = '*';
	
	protected int charLimit = 0;
	protected int width = 0;
	protected int cursor = 0;
	protected StringBuffer textBuffer = new StringBuffer();
	protected FontRenderer fontRenderer = Minecraft.theMinecraft.fontRenderer;
	protected final int CHAR_ASTERISK_WIDTH = fontRenderer.getStringWidth(String.valueOf(CHAR_ASTERISK));
	protected int maxAsteriskChars = 0;
	
	public PasswordTextProcessor() {
		
	}
	
	public void clear() {
		textBuffer.delete(0, textBuffer.length());
		cursor = 0;
	}

	public int getCursor() {
		return cursor;
	}

	public void setCursor(int cursor) {
		this.cursor = cursor;
		correctCursor();
	}
	
	protected void correctCursor() {
		cursor = Math.max(0, Math.min(cursor, textBuffer.length()));
	}
	
	public int[] getCursor2D() {
		return new int[]{0, cursor};
	}

	public int getMaximumCharacters() {
		return charLimit;
	}
	
	public void setMaximumCharacters(int max) {
		this.charLimit = max;
	}

	public int getMaximumLines() {
		return 1;
	}
	
	public void setMaximumLines(int max) {
		// ignore (can only be 1)
	}

	public String getText() {
		return textBuffer.toString();
	}

	public void setText(String str) {
		clear();
		if (str.length() > 0) {
			if (charLimit > 0 && str.length() > charLimit) {
				str = str.substring(0, charLimit);
			}
			if (str.length() > maxAsteriskChars) {
				str = str.substring(0, maxAsteriskChars);
			}
			textBuffer.append(str);
			cursor = textBuffer.length();
		}
	}
	
	protected boolean isRangeValid(int start, int end) {
		return start >= 0 && end <= textBuffer.length() && start < end;
	}
	
	protected boolean insert(char c) {
		if (textBuffer.length()+1 > maxAsteriskChars || (charLimit > 0 && textBuffer.length() >= charLimit)) {
			return false;
		}
		textBuffer.insert(cursor++, c);
		return true;
	}
	
	protected boolean delete(int start, int end, int cursorPos) {
		if (isRangeValid(start, end)) {
			textBuffer.delete(start, end);
			cursor = cursorPos;
			correctCursor();
			return true;
		}
		return false;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		maxAsteriskChars = (int) Math.floor(width / CHAR_ASTERISK_WIDTH);
	}
	
	public Iterator<String> iterator() {
		final char[] hiddenText = new char[textBuffer.length()];
		for (int i=0; i<textBuffer.length(); ++i) {
			hiddenText[i] = CHAR_ASTERISK;
		}
		
		Iterator<String> iter = new Iterator<String>() {
			int iteratorPos = 0;
			String s = new String(hiddenText);
			
			public void remove() {
			}
			
			public String next() {
				++iteratorPos;
				return s;
			}
			
			public boolean hasNext() {
				return iteratorPos == 0;
			}
		};
		return iter;
	}
	
	public boolean handleInput(char key, int keyId)
	{
		boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
		switch (keyId) {
			case Keyboard.KEY_BACK:
				if (ctrl)	return delete(0, cursor, 0);
				else		return delete(cursor-1, cursor, cursor-1);
			// –––––––––––––––––––––––
			case Keyboard.KEY_DELETE:
				if (ctrl) 	return delete(cursor, textBuffer.length(), cursor);
				else 		return delete(cursor, cursor+1, cursor);
			// –––––––––––––––––––––––
			case Keyboard.KEY_UP:
			case Keyboard.KEY_HOME:
				ctrl = true;
			case Keyboard.KEY_LEFT:
				if (ctrl)	cursor = 0;
				else		cursor = Math.max(0, --cursor);
				return false;
			// –––––––––––––––––––––––
			case Keyboard.KEY_DOWN:
			case Keyboard.KEY_END:
				ctrl = true;
			case Keyboard.KEY_RIGHT:
				if (ctrl)	cursor = textBuffer.length();
				else		cursor = Math.min(textBuffer.length(), ++cursor);
				return false;
			// –––––––––––––––––––––––
			case Keyboard.KEY_D:
			case Keyboard.KEY_C:
				if (ctrl) {
					clear();
					return true;
				}
			// –––––––––––––––––––––––
			default:
				if (ChatAllowedCharacters.allowedCharacters.indexOf(key) > -1) {
					return insert(key);
				}
		}
		return false;
	}

}
