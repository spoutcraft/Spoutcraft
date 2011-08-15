package org.getspout.spout.player;

public interface BiomeManager {
	
	public void setSnowEnabled(String biome, boolean bool);
	
	public void setRainEnabled(String biome, boolean bool);
	
	public boolean getSnowChanged(String biome);
	
	public boolean getRainChanged(String biome);
	
	public boolean getSnowEnabled(String biome);
	
	public boolean getRainEnabled(String biome);
	
	public void resetWeather(String biome);

}
