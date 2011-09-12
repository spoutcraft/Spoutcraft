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
package org.spoutcraft.spoutcraftapi.animation;

import org.spoutcraft.spoutcraftapi.property.PropertyInterface;

public class PropertyAnimation extends Animation {
	private String propertyName;
	private PropertyInterface object;
	public PropertyAnimation(PropertyInterface object, String property){
		propertyName = property;
		this.object = object;
		setValueDelegate(new PropertyDelegate(object, propertyName));
		Animatable value = (Animatable) object.getProperty(propertyName);
		setStartValue(value);
		setEndValue(value);
	}
	
	public void setProperty(String name){
		propertyName = name;
		setValueDelegate(new PropertyDelegate(object, propertyName));
	}
	
	public String getProperty(){
		return propertyName;
	}
	
	public PropertyInterface getAnimatedObject(){
		return object;
	}
	
	public void setAnimatedObject(PropertyInterface object){
		this.object = object;
		setValueDelegate(new PropertyDelegate(object, propertyName));
	}
}
