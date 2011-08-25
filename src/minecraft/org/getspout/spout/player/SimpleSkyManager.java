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
package org.getspout.spout.player;

import net.minecraft.client.Minecraft;

import org.getspout.spout.io.CustomTextureManager;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.Color;

public class SimpleSkyManager implements SkyManager{
	private int cloudHeight = -999;
	private int starFrequency = 1500;
	private int sunPercent = 100;
	private int moonPercent = 100;
	private String sunUrl = null;
	private String moonUrl = null;
	private Color skyColor = null, fogColor = null, cloudColor = null;
	
	@Override
	public int getCloudHeight() {
		if (cloudHeight == -999) {
			return (int)SpoutClient.getHandle().theWorld.worldProvider.getCloudHeight();
		}
		return cloudHeight;
	}

	@Override
	public void setCloudHeight(int y) {
		this.cloudHeight = y;
	}

	@Override
	public boolean isCloudsVisible() {
		return getCloudHeight() > -1;
	}

	@Override
	public void setCloudsVisible(boolean visible) {
		if (isCloudsVisible() != visible) {
			setCloudHeight(visible ? 108 : -1);
		}
	}

	@Override
	public int getStarFrequency() {
		return starFrequency;
	}

	@Override
	public void setStarFrequency(int frequency) {
		starFrequency = frequency;
		Minecraft.theMinecraft.renderGlobal.refreshStars();
	}

	@Override
	public boolean isStarsVisible() {
		return starFrequency > -1;
	}

	@Override
	public void setStarsVisible(boolean visible) {
		if (isStarsVisible() != visible) {
			setStarFrequency(visible ? 1500 : -1);
		}
	}

	@Override
	public int getSunSizePercent() {
		return sunPercent;
	}

	@Override
	public void setSunSizePercent(int percent) {
		sunPercent = percent;
	}

	@Override
	public boolean isSunVisible() {
		return sunPercent > -1;
	}

	@Override
	public void setSunVisible(boolean visible) {
		if (isSunVisible() != visible) {
			setSunSizePercent(visible ? 100 : -1);
		}
	}

	@Override
	public String getSunTextureUrl() {
		return sunUrl;
	}

	@Override
	public void setSunTextureUrl(String Url) {
		if (sunUrl != null) {
			//TODO release image?
		}
		sunUrl = Url;
		if (Url != null) {
			CustomTextureManager.downloadTexture(Url);
		}
	}

	@Override
	public int getMoonSizePercent() {
		return moonPercent;
	}

	@Override
	public void setMoonSizePercent(int percent) {
		moonPercent = percent;
	}

	@Override
	public boolean isMoonVisible() {
		return moonPercent > -1;
	}

	@Override
	public void setMoonVisible(boolean visible) {
		if (isMoonVisible() != visible) {
			setMoonSizePercent(visible ? 100 : -1);
		}
	}

	@Override
	public String getMoonTextureUrl() {
		return moonUrl;
	}

	@Override
	public void setMoonTextureUrl(String Url) {
		if (moonUrl != null) {
			//TODO release image?
		}
		moonUrl = Url;
		if (Url != null) {
			CustomTextureManager.downloadTexture(Url);
		}
	}
	
	@Override
	public void setSkyColor(float red, float green, float blue) {
		skyColor.setRed(red).setGreen(green).setBlue(blue);
	}

	@Override
	public void setSkyColor(Color color) {
		if(color!=null)
	   		skyColor = color.clone();
	   	else
	   		skyColor = null;
	}
	
	@Override
	public Color getSkyColor() {
		if(skyColor == null){
			return null;
		}
		return skyColor.clone();
	}
	
	@Override
	public void setFogColor(Color color) {
		if(color!=null)
			this.fogColor = color.clone();
		else
			fogColor = null;
	}
	
	@Override
	public Color getFogColor() {
		if(fogColor == null){
			return null;
		}
		return fogColor.clone();
	}
	
	@Override
	public void setCloudColor(Color color){
		if(color!=null)
			this.cloudColor = color.clone();
		else
			cloudColor = null;
	}
	
	@Override 
	public Color getCloudColor(){
		return this.cloudColor;
	}
}
