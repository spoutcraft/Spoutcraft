/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
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
package org.spoutcraft.client.player;

import net.minecraft.client.Minecraft;

import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.player.SkyManager;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.CustomTextureManager;

public class SimpleSkyManager implements SkyManager {
	private int cloudHeight = -999;
	private int starFrequency = 1500;
	private int sunPercent = 100;
	private int moonPercent = 100;
	private String sunUrl = null;
	private String moonUrl = null;
	private Color skyColor = null, fogColor = null, cloudColor = null;

	public int getCloudHeight() {
		if (cloudHeight == -999) {
			return (int)SpoutClient.getHandle().theWorld.provider.getCloudHeight();
		}
		return cloudHeight;
	}

	public void setCloudHeight(int y) {
		this.cloudHeight = y;
	}

	public boolean isCloudsVisible() {
		return getCloudHeight() > -1;
	}

	public void setCloudsVisible(boolean visible) {
		if (isCloudsVisible() != visible) {
			setCloudHeight(visible ? 108 : -1);
		}
	}

	public int getStarFrequency() {
		return starFrequency;
	}

	public void setStarFrequency(int frequency) {
		starFrequency = frequency;
		starFrequency = Math.min(starFrequency, 1000000);
		Minecraft.theMinecraft.renderGlobal.refreshStars();
	}

	public boolean isStarsVisible() {
		return starFrequency > -1;
	}

	public void setStarsVisible(boolean visible) {
		if (isStarsVisible() != visible) {
			setStarFrequency(visible ? 1500 : -1);
		}
	}

	public int getSunSizePercent() {
		return sunPercent;
	}

	public void setSunSizePercent(int percent) {
		sunPercent = percent;
	}

	public boolean isSunVisible() {
		return sunPercent > -1;
	}

	public void setSunVisible(boolean visible) {
		if (isSunVisible() != visible) {
			setSunSizePercent(visible ? 100 : -1);
		}
	}

	public String getSunTextureUrl() {
		return sunUrl;
	}

	public void setSunTextureUrl(String Url) {
		if (sunUrl != null) {
			// TODO release image?
		}
		sunUrl = Url;
		if (Url != null) {
			CustomTextureManager.downloadTexture(Url);
		}
	}

	public int getMoonSizePercent() {
		return moonPercent;
	}

	public void setMoonSizePercent(int percent) {
		moonPercent = percent;
	}

	public boolean isMoonVisible() {
		return moonPercent > -1;
	}

	public void setMoonVisible(boolean visible) {
		if (isMoonVisible() != visible) {
			setMoonSizePercent(visible ? 100 : -1);
		}
	}

	public String getMoonTextureUrl() {
		return moonUrl;
	}

	public void setMoonTextureUrl(String Url) {
		if (moonUrl != null) {
			// TODO release image?
		}
		moonUrl = Url;
		if (Url != null) {
			CustomTextureManager.downloadTexture(Url);
		}
	}

	public void setSkyColor(float red, float green, float blue) {
		skyColor.setRed(red).setGreen(green).setBlue(blue);
	}

	public void setSkyColor(Color color) {
		if (color!=null) {
			skyColor = color.clone();
		} else {
			skyColor = null;
		}
	}

	public Color getSkyColor() {
		if (skyColor == null) {
			return null;
		}
		return skyColor.clone();
	}

	public void setFogColor(Color color) {
		if (color!=null) {
			this.fogColor = color.clone();
		} else {
			fogColor = null;
		}
	}

	public Color getFogColor() {
		if (fogColor == null) {
			return null;
		}
		return fogColor.clone();
	}

	public void setCloudColor(Color color) {
		if (color!=null) {
			this.cloudColor = color.clone();
		} else {
			cloudColor = null;
		}
	}

	public Color getCloudColor() {
		return this.cloudColor;
	}
}
