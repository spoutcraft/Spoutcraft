package org.spoutcraft.client.gui;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.gui.GenericTexture;

public class ClientTexture extends GenericTexture {
	public ClientTexture(String path) {
		super(path);
	}
	
	public ClientTexture() {
		super();
	}
	
	@Override
	public void render() {
		GL11.glTranslatef(getX(), getY(), 0);
		Texture t = CustomTextureManager.getTextureFromJar(getUrl());
		if (t != null) {
			MCRenderDelegate d = (MCRenderDelegate) Spoutcraft.getRenderDelegate();
			d.drawTexture(t, (int) getWidth(), (int) getHeight(), isDrawingAlphaChannel());
		}
	}
	
	@Override
	public org.spoutcraft.spoutcraftapi.gui.Texture setUrl(String url) {
		Texture t = CustomTextureManager.getTextureFromJar(url);
		if (t != null) {
			setOriginalHeight(t.getImageHeight());
			setOriginalWidth(t.getImageWidth());
		}
		return super.setUrl(url);
	}
}
