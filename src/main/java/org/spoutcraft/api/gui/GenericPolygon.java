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

import java.util.LinkedList;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import org.spoutcraft.api.Spoutcraft;

public class GenericPolygon extends GenericWidget implements Polygon {
	LinkedList<Pair<Point, Color>> points = new LinkedList<Pair<Point,Color>>();
	Color lastColor = null;

	public WidgetType getType() {
		return WidgetType.Polygon;
	}

	public void render() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(770, 771);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		MinecraftTessellator t = Spoutcraft.getTessellator();
		GL11.glTranslated(getActualX(), getActualY(), 0);
		t.startDrawingQuads();
		for (Pair<Point, Color> point:points) {
			Point p = point.getLeft();
			Color c = point.getRight();
			t.setColorRGBAFloat(c.getRedF(), c.getGreenF(), c.getBlueF(), c.getAlphaF());
			t.addVertex(p.getX(), p.getY(), 0);

		}
		t.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public Polygon addPoint(Point p) throws IllegalStateException {
		if (lastColor == null) {
			throw new IllegalStateException("No color set.");
		}
		return addPoint(p, lastColor);
	}

	public Polygon addPoint(int x, int y) throws IllegalStateException {
		return addPoint(new Point(x, y));
	}

	public Polygon addPoint(Point p, Color c) {
		lastColor = c.clone();
		Pair<Point, Color> toAdd = Pair.of(p, c);
		points.add(toAdd);
		return this;
	}

	public Polygon addPoint(int x, int y, Color c) {
		return addPoint(new Point(x, y), c);
	}

	public LinkedList<Point> getPoints() {
		// TODO Auto-generated method stub
		return null;
	}

	public LinkedList<Color> getColors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getWidth() {
		// TODO Auto-generated method stub
		return super.getWidth();
	}

	@Override
	public double getHeight() {
		// TODO Auto-generated method stub
		return super.getHeight();
	}
}
