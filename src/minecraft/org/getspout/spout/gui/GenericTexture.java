package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import org.lwjgl.opengl.GL11;
import net.minecraft.src.Tessellator;
import org.getspout.spout.io.CustomTextureManager;
import org.getspout.spout.packet.PacketUtil;
import org.newdawn.slick.opengl.TextureLoader;

public class GenericTexture extends GenericWidget implements Texture {
	protected String Url = null;
	org.newdawn.slick.opengl.Texture texture = null;
	public GenericTexture() {
		
	}
	
	public GenericTexture(String Url) {
		this.Url = Url;
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
		String path = CustomTextureManager.getTextureFromUrl(getUrl());
		if (path == null) {
			return;
		}
		if (texture == null) {
			try {
				texture = TextureLoader.getTexture("PNG", new FileInputStream(path), true);
			}
			catch (IOException e) { }
			if (texture == null) {
				System.out.println("Error loading texture: " + path);
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
		return Url;
	}

	@Override
	public Texture setUrl(String Url) {
		if (getUrl() != null) {
			//TODO release image?
		}
		this.Url = Url;
		if (getUrl() != null) {
			CustomTextureManager.downloadTexture(Url);
		}
		return this;
	}

}
