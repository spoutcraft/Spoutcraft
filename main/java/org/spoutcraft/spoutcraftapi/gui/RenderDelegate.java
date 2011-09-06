package org.spoutcraft.spoutcraftapi.gui;

public interface RenderDelegate {
	
	public void render(ArmorBar bar);
	
	public void render(BubbleBar bar);
	
	public void render(GenericButton button);
	
	public void render(GenericGradient gradient);
	
	public void render(GenericItemWidget item);
	
	public void render(GenericLabel label);
	
	public void render(GenericSlider slider);
	
	public void render(GenericTextField text);
	
	public void render(GenericTexture texture);
	
	public void render(HealthBar bar);
	
	public void render(GenericEntityWidget entityWidget);

	public void downloadTexture(String plugin, String url);

	public int getTextWidth(String text);
	
	public int getScreenWidth();
	
	public int getScreenHeight();

}
