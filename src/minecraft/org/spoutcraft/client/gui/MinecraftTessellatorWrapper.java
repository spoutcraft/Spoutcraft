/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Tessellator;

import org.spoutcraft.spoutcraftapi.gui.MinecraftTessellator;

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
		Tessellator.instance.setTranslationD(x, y, z);
	}

	public int getMCTexture(String texture) {
		return Minecraft.theMinecraft.renderEngine.getTexture(texture);
	}
}
