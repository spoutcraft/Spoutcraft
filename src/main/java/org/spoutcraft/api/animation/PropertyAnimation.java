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
package org.spoutcraft.api.animation;

import org.spoutcraft.api.property.PropertyInterface;

public class PropertyAnimation extends Animation {
	private String propertyName;
	private PropertyInterface object;

	public PropertyAnimation(PropertyInterface object, String property) {
		propertyName = property;
		this.object = object;
		setValueDelegate(new PropertyDelegate(object, propertyName));
		Object val = object.getProperty(propertyName);
		if (val instanceof Animatable) {
			Animatable value = (Animatable) val;
			setStartValue(value);
			setEndValue(value);
		} else if (val instanceof Number) {
			Number num = (Number) val;
			setStartNumber(num);
			setEndNumber(num);
		} else {
			throw new IllegalStateException("Only subclasses of Number or Animatable allowed!");
		}
	}

	public void setProperty(String name) {
		propertyName = name;
		setValueDelegate(new PropertyDelegate(object, propertyName));
	}

	public String getProperty() {
		return propertyName;
	}

	public PropertyInterface getAnimatedObject() {
		return object;
	}

	public void setAnimatedObject(PropertyInterface object) {
		this.object = object;
		setValueDelegate(new PropertyDelegate(object, propertyName));
	}
}
