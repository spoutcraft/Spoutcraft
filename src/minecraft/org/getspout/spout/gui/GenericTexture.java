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
import org.lwjgl.opengl.GL11;
import net.minecraft.src.Tessellator;
import org.getspout.spout.io.CustomTextureManager;
import org.getspout.spout.packet.PacketUtil;

public class GenericTexture extends GenericWidget implements Texture {
	protected String url = null;
	org.newdawn.slick.opengl.Texture texture = null;
	public GenericTexture() {
		
	}
	
	public GenericTexture(String url) {
		this.url = url;
	}

	@Override
	public WidgetType getType() {
		return WidgetType.Texture;
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + PacketUtil.getNumBytes(getUrl());
	}
	
	public int getVersion() {
		return super.getVersion() + 0;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		this.setUrl(PacketUtil.readString(input));
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		PacketUtil.writeString(output, getUrl());
	}
	
	@Override
	public void render() {
		if (texture == null) {
			texture = CustomTextureManager.getTextureFromUrl(getPlugin(), getUrl());
			if (texture == null) {
				return;
			}
		}
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef((float) getScreenX(), (float) getScreenY(), 0); //moves texture into place
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, getHeight(), -90, 0.0D, 0.0D); //draw corners
		tessellator.addVertexWithUV(getWidth(), getHeight(), -90, texture.getWidth(), 0.0D);
		tessellator.addVertexWithUV(getWidth(), 0.0D, -90, texture.getWidth(), texture.getHeight());
		tessellator.addVertexWithUV(0.0D, 0.0D, -90, 0.0D, texture.getHeight());
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public Texture setUrl(String url) {
		if (getUrl() != null) {
			texture = null;
		}
		this.url = url;
		if (getUrl() != null) {
			CustomTextureManager.downloadTexture(getPlugin(), url);
		}
		return this;
	}

}
