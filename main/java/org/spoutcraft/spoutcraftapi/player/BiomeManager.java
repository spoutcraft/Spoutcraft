/*
 * This file is part of Spoutcraft API (http://wiki.getspout.org/).
 * 
 * Spoutcraft API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.player;

public interface BiomeManager {
	
	public void setSnowEnabled(String biome, boolean bool);
	
	public void setRainEnabled(String biome, boolean bool);
	
	public boolean getSnowChanged(String biome);
	
	public boolean getRainChanged(String biome);
	
	public boolean getSnowEnabled(String biome);
	
	public boolean getRainEnabled(String biome);
	
	public void resetWeather(String biome);

}
