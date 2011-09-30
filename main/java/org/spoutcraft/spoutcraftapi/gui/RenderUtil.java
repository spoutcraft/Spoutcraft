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

import org.lwjgl.opengl.GL11;
import org.spoutcraft.spoutcraftapi.Spoutcraft;

public class RenderUtil {
	
	/**
	 * Draws a polygon that approximates a circle, for large values of numSegments
	 * @param cx x coordinate or the center of the circle
	 * @param cy y coordinate for the center of the circle
	 * @param r radius of the circle
	 * @param numSegments to draw (number of sides to the polygon. Large values > 50 approximate a circle)
	 * @param thickness of the line to draw
	 */
	public static void drawCircle(float cx, float cy, float r, int numSegments, float thickness) { 
		float theta = 2 * 3.1415926F / ((float)numSegments); 
		float c = (float) Math.cos(theta);//precalculate the sine and cosine
		float s = (float) Math.sin(theta);
		float t;

		float x = r;//we start at angle = 0 
		float y = 0; 
		
		GL11.glBegin(GL11.GL_LINE_LOOP); 
		GL11.glLineWidth(thickness);
		for(int ii = 0; ii < numSegments; ii++) { 
			
			GL11.glVertex2f(x + cx, y + cy);//output vertex 
			
			//apply the rotation matrix
			t = x;
			x = c * x - s * y;
			y = s * t + c * y;
		} 
		GL11.glEnd(); 
	}
	
	public static void drawRectangle(int x, int y, int width, int height, int color) {
		int temp;
		if (x < width) {
			temp = x;
			x = width;
			width = temp;
		}

		if (y < height) {
			temp = y;
			y = height;
			height = temp;
		}

		float alpha = (float) (color >> 24 & 255) / 255.0F;
		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(red, green, blue, alpha);
		MinecraftTessellator tessellator = Spoutcraft.getTessellator();
		tessellator.startDrawingQuads();
		tessellator.addVertex((double) x, (double) height, 0.0D);
		tessellator.addVertex((double) width, (double) height, 0.0D);
		tessellator.addVertex((double) width, (double) y, 0.0D);
		tessellator.addVertex((double) x, (double) y, 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawGradientRectangle(int x, int y, int gradientX, int gradientY, int colorOne, int colorTwo) {
		float alphaOne = (float) (colorOne >> 24 & 255) / 255.0F;
		float redOne = (float) (colorOne >> 16 & 255) / 255.0F;
		float greenOne = (float) (colorOne >> 8 & 255) / 255.0F;
		float blueOne = (float) (colorOne & 255) / 255.0F;
		float alphaTwo = (float) (colorTwo >> 24 & 255) / 255.0F;
		float redTwo = (float) (colorTwo >> 16 & 255) / 255.0F;
		float greenTwo = (float) (colorTwo >> 8 & 255) / 255.0F;
		float blueTwo = (float) (colorTwo & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(770, 771);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		MinecraftTessellator tessellator = Spoutcraft.getTessellator();
		tessellator.startDrawingQuads();
		tessellator.setColorRGBAFloat(redOne, greenOne, blueOne, alphaOne);
		tessellator.addVertex((double) gradientX, (double) y, 0.0D);
		tessellator.addVertex((double) x, (double) y, 0.0D);
		tessellator.setColorRGBAFloat(redTwo, greenTwo, blueTwo, alphaTwo);
		tessellator.addVertex((double) x, (double) gradientY, 0.0D);
		tessellator.addVertex((double) gradientX, (double) gradientY, 0.0D);
		tessellator.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void drawTexturedModalRectangle(int x, int y, int var3, int modalX, int modalY, int modalZ, float zLevel) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		MinecraftTessellator tessellator = Spoutcraft.getTessellator();
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double) x, (double) (y + modalZ), (double) zLevel, (double) ((float) (var3) * var7), (double) ((float) (modalX + modalZ) * var8));
		tessellator.addVertexWithUV((double) (x + modalY), (double) (y + modalZ), (double) zLevel, (double) ((float) (var3 + modalY) * var7), (double) ((float) (modalX + modalZ) * var8));
		tessellator.addVertexWithUV((double) (x + modalY), (double) (y), (double) zLevel, (double) ((float) (var3 + modalY) * var7), (double) ((float) (modalX) * var8));
		tessellator.addVertexWithUV((double) x, (double) y, (double) zLevel, (double) ((float) (var3) * var7), (double) ((float) (modalX) * var8));
		tessellator.draw();
	}
	// Spout End

}
