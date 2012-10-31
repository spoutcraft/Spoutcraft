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
package org.spoutcraft.api.gui;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.spoutcraft.api.UnsafeClass;
import org.spoutcraft.api.animation.Animatable;
import org.spoutcraft.api.property.Property;
import org.spoutcraft.api.property.PropertyInterface;
import org.spoutcraft.api.property.PropertyObject;

@UnsafeClass
public class Rectangle extends PropertyObject implements PropertyInterface, Animatable {
	int width, height, x, y;

	public Rectangle(int x, int y, int width, int height) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);

		initProperties();
	}

	private void initProperties() {
		addProperty("x", new Property() {
			public void set(Object value) {
				setX((Integer) value);
			}

			public Object get() {
				return getX();
			}
		});
		addProperty("y", new Property() {
			public void set(Object value) {
				setY((Integer) value);
			}

			public Object get() {
				return getY();
			}
		});
		addProperty("width", new Property() {
			public void set(Object value) {
				setWidth((Integer) value);
			}

			public Object get() {
				return getWidth();
			}
		});
		addProperty("height", new Property() {
			public void set(Object value) {
				setHeight((Integer) value);
			}

			public Object get() {
				return getHeight();
			}
		});
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getTop() {
		return getY();
	}

	public int getLeft() {
		return getX();
	}

	public int getBottom() {
		return getY() + getHeight();
	}

	public int getRight() {
		return getX() + getWidth();
	}

	/**
	 * Shifts the position by given x and y.
	 * @param x
	 * @param y
	 */
	public void moveBy(int x, int y) {
		this.x += x;
		this.y += y;
	}

	/**
	 * Resizes the rect with given width and height
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Moves rect to given x and y position.
	 * @param x
	 * @param y
	 */
	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Animatable getValueAt(double p, Animatable startValue, Animatable endValue) {
		int w, h, x, y;
		Rectangle p1 = (Rectangle) startValue;
		Rectangle p2 = (Rectangle) endValue;
		h = p1.height;
		w = p1.width;
		x = p1.x;
		y = p1.y;
		h += (p2.height - h) * p;
		w += (p2.width - w) * p;
		x += (p2.x - x) * p;
		y += (p2.y - y) * p;
		return new Rectangle(x, y, w, h);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Rectangle) {
			Rectangle other = (Rectangle) obj;
			return (new EqualsBuilder()).append(width, other.width).append(height, other.height).append(x, other.x).append(y, other.y).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(width).append(height).append(x).append(y).toHashCode();
	}

	@Override
	public String toString() {
		return super.toString() + "{ x: "+x+" y: "+y+" width: "+width+" height: "+height+" }";
	}

	public Rectangle clone() {
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Copies the values from the other instance to this instance
	 * @param other the Rectangle to copy the values from.
	 */
	public void copy(Rectangle other) {
		setX(other.x);
		setY(other.y);
		setWidth(other.width);
		setHeight(other.height);
	}
}
