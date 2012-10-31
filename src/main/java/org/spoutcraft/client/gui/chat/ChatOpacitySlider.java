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
package org.spoutcraft.client.gui.chat;

import org.spoutcraft.api.event.screen.SliderDragEvent;
import org.spoutcraft.api.gui.GenericSlider;
import org.spoutcraft.client.config.Configuration;

public class ChatOpacitySlider extends GenericSlider {
	public ChatOpacitySlider() {
		setSliderPosition(Configuration.getChatOpacity());
		updateText();
	}

	private void updateText() {
		setText("Background Opacity: " + (int) (getSliderPosition()*100) + "%");
	}

	@Override
	public void onSliderDrag(SliderDragEvent event) {
		Configuration.setChatOpacity(event.getNewPosition());
		updateText();
	}
}
