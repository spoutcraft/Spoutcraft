/*
 * This file is part of Spoutcraft API (http://wiki.getspout.org/).
 * 
 * Spoutcraft API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.event.screen;

import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.event.HandlerList;
import org.spoutcraft.spoutcraftapi.gui.Screen;
import org.spoutcraft.spoutcraftapi.gui.ScreenType;
import org.spoutcraft.spoutcraftapi.gui.Slider;

public class SliderDragEvent extends ScreenEvent<SliderDragEvent> {
	protected Slider slider;
	protected float position;
	protected float old;
	public SliderDragEvent(Player player, Screen screen, Slider slider, float position) {
		super(player, screen, ScreenType.CUSTOM_SCREEN);
		this.slider = slider;
		this.position = position;
		this.old = slider.getSliderPosition();
	}
	
	public Slider getSlider() {
		return slider;
	}
	
	public float getOldPosition() {
		return old;
	}
	
	public float getNewPosition() {
		return position;
	}
	
	public void setNewPosition(float position) {
		this.position = position;
	}

	public static final HandlerList<SliderDragEvent> handlers = new HandlerList<SliderDragEvent>();

	@Override
	public HandlerList<SliderDragEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "Slider Drag Event";
	}
}
