/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spoutcraft.client.gui.settings.controls;

import org.spoutcraft.api.gui.Control;
import org.spoutcraft.api.gui.GenericTextField;
import org.spoutcraft.client.config.Configuration;

public class ResizeScreenshotWidthField extends GenericTextField {
	public ResizeScreenshotWidthField() {
		setTooltip("The width to resize the screenshot to.");
		setText("");
		setPlaceholder("" + Configuration.getResizedScreenshotWidth());
		setMaximumCharacters(4);
	}

	@Override
	public void onTick() {
		setEnabled(Configuration.isResizeScreenshots());
		super.onTick();
	}

	@Override
	public void onTypingFinished() {
		if (getText().equals("")) return;
		try {
			int width = Integer.parseInt(getText());
			if (width < 1) throw new RuntimeException("Must be at least 1");
			Configuration.setResizedScreenshotWidth(width);
			Configuration.write();
			setPlaceholder("" + width);
		} catch(Exception e) {
			setText("");
			setPlaceholder("Error");
			setFocus(false);
		}
	}

	@Override
	public Control setFocus(boolean focus) {
		super.setFocus(focus);
		if (!focus) {
			try {
				int width = Integer.parseInt(getText());
				if (width < 1) throw new RuntimeException("Must be at least 1");
				Configuration.setResizedScreenshotWidth(width);
				Configuration.write();
				setText("");
				setPlaceholder("" + width);
			} catch(Exception e) {
				setText("");
				setPlaceholder("Error");
			}
		}
		return this;
	}
}
