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

import org.spoutcraft.api.animation.Animatable;

public class Point implements Animatable{
	private int x, y;

	public Point() {
		x = 0;
		y = 0;
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(Point other) {
		this.x = other.x;
		this.y = other.y;
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

	public Point copy() {
		return new Point(this);
	}

	@Override
	public String toString() {
		return "Point{x=" + x + "; y=" + y + "}";
	}

	public Animatable getValueAt(double p, Animatable startValue, Animatable endValue) {
		Point p1 = (Point) startValue;
		Point p2 = (Point) endValue;
		int x = (int) (p1.x + (p2.x - p1.x) * p);
		int y = (int) (p1.y + (p2.y - p1.y) * p);
		return new Point(x, y);
	}
}
