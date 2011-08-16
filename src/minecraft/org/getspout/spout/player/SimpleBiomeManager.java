package org.getspout.spout.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleBiomeManager implements BiomeManager {
	
	private HashMap<String,Boolean> changedSnow = new HashMap<String,Boolean>();
	private HashMap<String,Boolean> changedRain = new HashMap<String,Boolean>();
	private List<String> defaultSnow = new ArrayList<String>();
	private List<String> defaultRain = new ArrayList<String>();
	
	public SimpleBiomeManager() {
		defaultSnow.add("Tundra");
		defaultSnow.add("Taiga");
		defaultSnow.add("Ice Desert");
		
		defaultRain.add("Desert");
		defaultRain.add("Ice Desert");
		defaultRain.add("Hell");
		defaultRain.add("Sky");
	}

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
		
		if(defaultSnow.contains(biome)) {
			changedSnow.put(biome, true);
		}
		else {
			changedSnow.put(biome,false);
		}
		
		if(defaultRain.contains(biome)) {
			changedRain.put(biome, false);
		}
		else {
			changedRain.put(biome,true);
		}
	}
}
