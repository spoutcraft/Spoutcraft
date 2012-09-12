/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spoutcraft.client.gui.settings;

import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.gui.Control;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;

public class ResizeScreenshotHeightField extends GenericTextField {
	public ResizeScreenshotHeightField() {
		setTooltip("The height to resize the screenshot to.");
		setText("");
		setPlaceholder("" + ConfigReader.resizedScreenshotHeight);
		setMaximumCharacters(4);
	}

	@Override
	public void onTick() {
		setEnabled(ConfigReader.resizeScreenshots);
		super.onTick();
	}

	@Override
	public void onTypingFinished() {
		if (getText().equals("")) return;
		try {
			int height = Integer.parseInt(getText());
			if (height < 1) throw new RuntimeException("Must be at least 1");
			ConfigReader.resizedScreenshotHeight = height;
			ConfigReader.write();
			setPlaceholder("" + height);
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
				int height = Integer.parseInt(getText());
				if (height < 1) throw new RuntimeException("Must be at least 1");
				ConfigReader.resizedScreenshotHeight = height;
				ConfigReader.write();
				setText("");
				setPlaceholder("" + height);
			} catch(Exception e) {
				setText("");
				setPlaceholder("Error");
			}
		}
		return this;
	}
}
