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

import java.util.HashMap;

public class WidgetType {
	private static HashMap<WidgetType, Integer> lookupClass = new HashMap<WidgetType, Integer>();
	private static HashMap<Integer, WidgetType> lookupId = new HashMap<Integer, WidgetType>();
	private static int lastId = 0;

	public static WidgetType Label = new WidgetType(GenericLabel.class, 0);
	public static WidgetType HealthBar = new WidgetType(HealthBar.class, 1);
	public static WidgetType BubbleBar = new WidgetType(BubbleBar.class, 2);
	public static WidgetType ChatBar = new WidgetType(ChatBar.class, 3);
	public static WidgetType ChatTextBox = new WidgetType(ChatTextBox.class, 4);
	public static WidgetType ArmorBar = new WidgetType(ArmorBar.class, 5);
	public static WidgetType Texture = new WidgetType(GenericTexture.class, 6);
	public static WidgetType PopupScreen = new WidgetType(GenericPopup.class, 7);
	public static WidgetType InGameScreen = new WidgetType(null, 8);
	public static WidgetType ItemWidget = new WidgetType(GenericItemWidget.class, 9);
	public static WidgetType Button = new WidgetType(GenericButton.class, 10);
	public static WidgetType Slider = new WidgetType(GenericSlider.class, 11);
	public static WidgetType TextField = new WidgetType(GenericTextField.class, 12);
	public static WidgetType Gradient = new WidgetType(GenericGradient.class, 13);
	// Container has the 14, dude!
	public static WidgetType EntityWidget = new WidgetType(GenericEntityWidget.class, 15);
	public static WidgetType OverlayScreen = new WidgetType(GenericOverlayScreen.class, 16);
	public static WidgetType HungerBar = new WidgetType(HungerBar.class, 17);
	public static WidgetType ExpBar = new WidgetType(ExpBar.class, 18);

	private final int id;
	private final boolean client;
	private final Class<? extends Widget> widgetClass;

	public WidgetType(Class<? extends Widget> widget) {
		widgetClass = widget;
		id = lastId;
		lastId++;
		lookupClass.put(this, id);
		lookupId.put(id, this); 
		client = false;
	}

	private WidgetType(Class<? extends Widget> widget, int id) {
		widgetClass = widget;
		this.id = id;
		if (id > lastId) {
			lastId = id;
		}
		lookupClass.put(this, id);
		lookupId.put(id, this);
		client = false;
	}

	public WidgetType(Class<? extends Widget> widget, int id, boolean client) {
		widgetClass = widget;
		this.id = id;
		if (id > lastId) {
			lastId = id;
		}
		lookupClass.put(this, id);
		lookupId.put(id, this);
		this.client = client;
	}

	public int getId() {
		return id;
	}

	public Class<? extends Widget> getWidgetClass() {
		return widgetClass;
	}

	public static Integer getWidgetId(Class<? extends Widget> widget) {
		return lookupClass.get(widget);
	}

	public static WidgetType getWidgetFromId(int id) {
		return lookupId.get(id);
	}

	public static int getNumWidgetTypes() {
		return lastId;
	}

	public boolean isClientOnly() {
		return client;
	}

}
