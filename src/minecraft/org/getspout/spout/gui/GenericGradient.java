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
package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.packet.PacketUtil;
import org.lwjgl.opengl.GL11;
import org.spoutcraft.spoutcraftapi.gui.Color;

import net.minecraft.src.Tessellator;

public class GenericGradient extends GenericWidget implements Gradient {
	
	protected Color color1 = new Color(0.06F, 0.06F, 0.06F, 0.75F), color2 = new Color(0.06F, 0.06F, 0.06F, 0.82F);
	
	public GenericGradient() {
		
	}
	
	public Gradient setTopColor(Color color) {
		this.color1 = color;
		return this;
	}
	
	public Gradient setBottomColor(Color color) {
		this.color2 = color;
		return this;
	}
	
	public Color getTopColor() {
		return this.color1;
	}
	
	public Color getBottomColor() {
		return this.color2;
	}

	@Override
	public WidgetType getType() {
		return WidgetType.Gradient;
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 32;
	}
	
	public int getVersion() {
		return super.getVersion() + 0;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		this.setTopColor(PacketUtil.readColor(input));
		this.setBottomColor(PacketUtil.readColor(input));
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		PacketUtil.writeColor(output, getTopColor());
		PacketUtil.writeColor(output, getBottomColor());
	}
	
	@Override
	public void render() {
		GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
		GL11.glBlendFunc(770, 771);
		GL11.glShadeModel(7425 /*GL_SMOOTH*/);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(color1.getRedF(), color1.getGreenF(), color1.getBlueF(), color1.getAlphaF());
		tessellator.addVertex(getWidth() + getScreenX(), getScreenY(), 0.0D);
		tessellator.addVertex(getScreenX(), getScreenY(), 0.0D);
		tessellator.setColorRGBA_F(color2.getRedF(), color2.getGreenF(), color2.getBlueF(), color2.getAlphaF());
		tessellator.addVertex(getScreenX(), getHeight() + getScreenY(), 0.0D);
		tessellator.addVertex(getWidth() + getScreenX(), getHeight() + getScreenY(), 0.0D);
		tessellator.draw();
		GL11.glShadeModel(7424 /*GL_FLAT*/);
		GL11.glDisable(3042 /*GL_BLEND*/);
		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
	}

}
