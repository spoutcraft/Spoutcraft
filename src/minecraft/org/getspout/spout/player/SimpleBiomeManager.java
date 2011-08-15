package org.getspout.spout.player;

import java.util.HashMap;

public class SimpleBiomeManager implements BiomeManager {
	
	private HashMap<String,Boolean> changedSnow = new HashMap<String,Boolean>();
	private HashMap<String,Boolean> changedRain = new HashMap<String,Boolean>();

	@Override
	public void setSnowEnabled(String biome, boolean bool) {
		
		changedSnow.put(biome, bool);
		
	}

	@Override
	public void setRainEnabled(String biome, boolean bool) {
		
		changedRain.put(biome, bool);
		
	}
	
	@Override
	public boolean getSnowChanged(String biome) {
		boolean bool = false;
		if(changedSnow.containsKey(biome)) {
			bool = true;
		}
		
		return bool;
	}
	
	@Override
	public boolean getRainChanged(String biome) {
		boolean bool = false;
		if(changedRain.containsKey(biome)) {
			bool = true;
		}
		
		return bool;
	}

	@Override
	public boolean getSnowEnabled(String biome) {
		boolean bool = false;
		bool = changedSnow.get(biome);
		
		return bool;
	}
	
	@Override
	public boolean getRainEnabled(String biome) {
		boolean bool = false;
		bool = changedRain.get(biome);
		
		return bool;
	}
	
	@Override
	public void resetWeather(String biome) {
		if(changedSnow.containsKey(biome)) {
			changedSnow.remove(biome);
		}
		
		if(changedRain.containsKey(biome)) {
			changedRain.remove(biome);
		}
	}
}
