package org.spoutcraft.spoutcraftapi.gui;

public interface MinecraftTessellator {
	
	public void draw();

	public void startDrawingQuads();
	
	public void setTextureUV(double s, double t);
	
	public void setColorOpaqueFloat(float red, float green, float blue);
	
	public void setColorRGBAFloat(float red, float green, float blue, float alpha);
	
	public void setColorOpaque(int red, int green, int blue);
	
	public void setColorRGBA(int red, int green, int blue, int alpha);
	
	public void addVertexWithUV(double x, double y, double z, double s, double t);
	
	public void addVertex(double x, double y, double z);
	
	public void setColorOpaqueInt(int color);
	
	public void setColorRGBAInt(int color, int alpha);
	
	public void disableColor();
	
	public void setNormal(float x, float y, float z);
	
	public void setTranslation(double x, double y, double z);
}
