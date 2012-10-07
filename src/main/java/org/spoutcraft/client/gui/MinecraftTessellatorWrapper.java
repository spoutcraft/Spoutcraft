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
package org.spoutcraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Tessellator;

import org.spoutcraft.api.gui.MinecraftTessellator;

public class MinecraftTessellatorWrapper implements MinecraftTessellator {
	public void draw() {
		Tessellator.instance.draw();
	}

	public void startDrawingQuads() {
		Tessellator.instance.startDrawingQuads();
	}

	public void startDrawing(int drawMode) {
		Tessellator.instance.startDrawing(drawMode);
	}

	public void setBrightness(int brightness) {
		Tessellator.instance.setBrightness(brightness);
	}

	public void setTextureUV(double s, double t) {
		Tessellator.instance.setTextureUV(s, t);
	}

	public void setColorOpaqueFloat(float red, float green, float blue) {
		Tessellator.instance.setColorOpaque_F(red, green, blue);
	}

	public void setColorRGBAFloat(float red, float green, float blue, float alpha) {
		Tessellator.instance.setColorRGBA_F(red, green, blue, alpha);
	}

	public void setColorOpaque(int red, int green, int blue) {
		Tessellator.instance.setColorOpaque(red, green, blue);
	}

	public void setColorRGBA(int red, int green, int blue, int alpha) {
		Tessellator.instance.setColorRGBA(red, green, blue, alpha);
	}

	public void addVertexWithUV(double x, double y, double z, double s, double t) {
		Tessellator.instance.addVertexWithUV(x, y, z, s, t);
	}

	public void addVertex(double x, double y, double z) {
		Tessellator.instance.addVertex(x, y, z);
	}

	public void setColorOpaqueInt(int color) {
		Tessellator.instance.setColorOpaque_I(color);
	}

	public void setColorRGBAInt(int color, int alpha) {
		Tessellator.instance.setColorRGBA_I(color, alpha);
	}

	public void disableColor() {
		Tessellator.instance.disableColor();
	}

	public void setNormal(float x, float y, float z) {
		Tessellator.instance.setNormal(x, y, z);
	}

	public void setTranslation(double x, double y, double z) {
		Tessellator.instance.setTranslation(x, y, z);
	}

	public int getMCTexture(String texture) {
		return Minecraft.theMinecraft.renderEngine.getTexture(texture);
	}
}
