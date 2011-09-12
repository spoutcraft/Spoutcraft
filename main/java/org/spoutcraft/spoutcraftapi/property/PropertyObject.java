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
package org.spoutcraft.spoutcraftapi.property;

import java.util.HashMap;

import org.spoutcraft.spoutcraftapi.property.PropertyInterface;

public class PropertyObject implements PropertyInterface {
	private HashMap<String, Property> properties = new HashMap<String, Property>();

	protected void addProperty(String name, Property delegate) {
		properties.put(name, delegate);
	}

	public Object getProperty(String name) {
		if (properties.containsKey(name)) {
			return properties.get(name).get();
		}
		return null;
	}

	public void setProperty(String name, Object value) {
		if (properties.containsKey(name)) {
			properties.get(name).set(value);
		}
	}

	public Property getPropertyDelegate(String name) {
		return properties.get(name);
	}
}
