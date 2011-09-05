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
package org.getspout.spout.gui;

import java.util.HashMap;

public enum WidgetType {
	Label(0, GenericLabel.class),
	HealthBar(1, HealthBar.class),
	BubbleBar(2, BubbleBar.class),
	ChatBar(3, ChatBar.class),
	ChatTextBox(4, ChatTextBox.class),
	ArmorBar(5, ArmorBar.class),
	Texture(6, GenericTexture.class),
	PopupScreen(7, GenericPopup.class),
	InGameScreen(8, InGameScreen.class),
	ItemWidget(9, GenericItemWidget.class),
	Button(10, GenericButton.class),
	Slider(11, GenericSlider.class),
	TextField(12, GenericTextField.class),
	Gradient(13, GenericGradient.class), 
	//Container has id 14!
	EntityWidget(15, GenericEntityWidget.class),
	;
	
	private final int id;
	private final Class<? extends Widget> widgetClass;
	private static final HashMap<Integer, WidgetType> lookupId = new HashMap<Integer, WidgetType>();
	WidgetType(final int id, final Class<? extends Widget> widgetClass) {
		this.id = id;
		this.widgetClass = widgetClass;
	}
	
	public int getId() {
		return id;
	}
	
	public Class<? extends Widget> getWidgetClass() {
		return widgetClass;
	}
	
	public static WidgetType getWidgetFromId(int id) {
		return lookupId.get(id);
	}
	
	static {
		for (WidgetType packet : values()) {
			lookupId.put(packet.getId(), packet);
		}
	}

}
