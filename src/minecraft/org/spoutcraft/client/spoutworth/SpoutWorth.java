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
package org.spoutcraft.client.spoutworth;

import net.minecraft.client.Minecraft;

import org.spoutcraft.client.config.ConfigReader;

import gnu.trove.iterator.TLongIterator;
import gnu.trove.list.linked.TLongLinkedList;

public class SpoutWorth {
	private static SpoutWorth instance = null;
	public static SpoutWorth getInstance() {
		if (instance == null) {
			instance = new SpoutWorth();
		}
		return instance;
	}
	
	private static final int MAX_FPS_TRACKED = 1000;
	private static final int IDEAL_FPS = 60;
	
	TLongLinkedList fpsList = new TLongLinkedList();
	long avgFPS = -1;
	long appearanceCooldown = 0;
	long currentFPS = IDEAL_FPS;
	long renderingCooldown;
	String debug;
	boolean belowIdeal = false;
	
	public void updateFPS(long fps) {
		fpsList.add(fps);
		if (fpsList.size() > MAX_FPS_TRACKED) {
			fpsList.removeAt(0);
		}
		avgFPS = -1;
		currentFPS = fps;
		doTick();
	}
	
	public long getAverageFPS() {
		if (avgFPS != -1){
			return avgFPS;
		}
		if (fpsList.size() == 0) {
			return IDEAL_FPS;
		}
		long total = 0;
		TLongIterator i = fpsList.iterator();
		while(i.hasNext()) {
			total += i.next();
		}
		avgFPS = Math.max(1, total / fpsList.size());
		return avgFPS;
	}
	
	public long getIdealFPS() {
		return Math.min(getAverageFPS() + 10, IDEAL_FPS);
	}
	
	public void doTick() {
		if (renderingCooldown > 0) {
			renderingCooldown--;
		}
		if (!ConfigReader.automatePerformance) {
			return;
		}
		
		ConfigReader.performance = 0;
		ConfigReader.farView = false;

		long idealFPS = getIdealFPS();
		//calculate how close we are to acheiving ideal FPS
		long percent = getAverageFPS() * 100 / idealFPS;
		long percentCur = currentFPS * 100 / idealFPS;
		
		//Increase our ideal standards if we are looking for best performance
		if (ConfigReader.automateMode == 0) {
			percent -= 10;
			percentCur -= 10;
		}
		//Decrease our ideal standards if we are looking for balanced performance
		else if (ConfigReader.automateMode == 1) {
			percent += 15;
			percentCur += 15;
		}
		//Decrease our ideal standards if we are looking for best appearance
		else if (ConfigReader.automateMode == 2) {
			percent += 40;
			percentCur += 40;
		}
		
		boolean improving = percentCur > percent;
		
		belowIdeal = percent < 75;
		debug = "Ideal Percent: " + percent + " Current Percent: " + percentCur + " Improving: " + improving;
		
		//drastic changes to temporarily improve fps
		if (percentCur < 50 && !improving && isFullyLoaded()) {
			renderingCooldown = Math.max(renderingCooldown, (percentCur < 33 ? 7 : 4));
		}
		
		if (percent > 200) {
			if (--appearanceCooldown <= 0) {
				appearanceCooldown = 600;
				increaseAppearance();
				increaseAppearance();
			}
		}
		else if (percent > 150 || (percent > 125 && improving)) {
			if (--appearanceCooldown <= 0) {
				appearanceCooldown = 600;
				increaseAppearance();
			}
		}
		else if (percent < 40) {
			decreaseAppearance(true, improving ? 10 : 0);
			appearanceCooldown = 120;
		}
		else if (percent < 66) {
			if (--appearanceCooldown <= 0) {
				decreaseAppearance(improving, 0);
				appearanceCooldown = 240;
			}
		}
		else if (percent < 85) {
			if (--appearanceCooldown <= 0) {
				decreaseAppearance();
				appearanceCooldown = 480;
			}
		}
	}
	
	public boolean isFullyLoaded() {
		return fpsList.size() == SpoutWorth.MAX_FPS_TRACKED;
	}
	
	public boolean isBelowIdeal() {
		return belowIdeal && ConfigReader.automatePerformance;
	}
	
	public String getDebugText(){
		return debug;
	}
	
	public boolean isRenderingHalted() {
		return renderingCooldown > 0L && ConfigReader.automatePerformance;
	}
	
	public void increaseAppearance() {
		if (!ConfigReader.fancyWater) {
			ConfigReader.fancyWater = true;
			ConfigReader.write();
			return;
		}
		if (!ConfigReader.fancyParticles) {
			ConfigReader.fancyParticles = true;
			ConfigReader.write();
			return;
		}
		if (!ConfigReader.fancyFog) {
			ConfigReader.fancyFog = true;
			ConfigReader.write();
			return;
		}
		if (!ConfigReader.fancyGrass) {
			ConfigReader.fancyGrass = true;
			ConfigReader.write();
			return;
		}
		if (!ConfigReader.fancyBiomeColors) {
			ConfigReader.fancyBiomeColors = true;
			ConfigReader.write();
			return;
		}
		if (!ConfigReader.fancyLight) {
			ConfigReader.fancyLight = true;
			ConfigReader.write();
			return;
		}
		if (!ConfigReader.fancyLight) {
			ConfigReader.fancyLight = true;
			ConfigReader.write();
			return;
		}
		if (!ConfigReader.fancyClouds) {
			ConfigReader.fancyClouds = true;
			ConfigReader.write();
			return;
		}
		if (!ConfigReader.fancyTrees) {
			ConfigReader.fancyTrees = true;
			ConfigReader.write();
			return;
		}
		if (!ConfigReader.fancyWeather) {
			ConfigReader.fancyWeather = true;
			ConfigReader.write();
			return;
		}
		if (ConfigReader.betterGrass == 1) {
			ConfigReader.betterGrass = 2;
			ConfigReader.write();
			return;
		}
		if (ConfigReader.renderDistance != 0) {
			ConfigReader.renderDistance--;
			Minecraft.theMinecraft.gameSettings.renderDistance = ConfigReader.renderDistance;
			ConfigReader.write();
			return;
		}
		if (ConfigReader.dynamicUpdates) {
			ConfigReader.dynamicUpdates = false;
			ConfigReader.write();
			return;
		}
		if (ConfigReader.chunkUpdates < 3) {
			ConfigReader.chunkUpdates++;
			ConfigReader.write();
			return;
		}
		if (ConfigReader.advancedOpenGL != 0) {
			ConfigReader.advancedOpenGL = 0;
			Minecraft.theMinecraft.gameSettings.advancedOpengl = false;
			ConfigReader.write();
			return;
		}
		if (ConfigReader.signDistance != Integer.MAX_VALUE) {
			ConfigReader.signDistance *= 2;
			if (ConfigReader.signDistance == 128) {
				ConfigReader.signDistance = Integer.MAX_VALUE;
			}
			ConfigReader.write();
			return;
		}
	}
	
	public void decreaseAppearance() {
		decreaseAppearance(false, 0);
	}
	
	public void decreaseAppearance(boolean drastic, long haltRenderingCount) {
		renderingCooldown = haltRenderingCount;
		
		int downgrade = drastic ? 3 : 1;
		
		if (!ConfigReader.dynamicUpdates) {
			ConfigReader.dynamicUpdates = true;
			ConfigReader.write();
			return;
		}
		if (ConfigReader.renderDistance < 2) {
			ConfigReader.renderDistance++;
			Minecraft.theMinecraft.gameSettings.renderDistance = ConfigReader.renderDistance;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
		if (ConfigReader.chunkUpdates > 1) {
			ConfigReader.chunkUpdates = 0;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
		if (ConfigReader.advancedOpenGL == 2 || ConfigReader.advancedOpenGL == 0) {
			ConfigReader.advancedOpenGL = 1;
			Minecraft.theMinecraft.gameSettings.advancedOpengl = true;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
		if (ConfigReader.signDistance > 8) {
			ConfigReader.signDistance /= 2;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
		
		if (ConfigReader.fancyParticles) {
			ConfigReader.fancyParticles = false;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
		if (ConfigReader.fancyFog) {
			ConfigReader.fancyFog = false;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
		if (ConfigReader.fancyGrass) {
			ConfigReader.fancyGrass = false;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
		if (ConfigReader.fancyBiomeColors) {
			ConfigReader.fancyBiomeColors = false;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
		if (ConfigReader.fancyLight) {
			ConfigReader.fancyLight = false;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
		if (ConfigReader.fancyLight) {
			ConfigReader.fancyLight = false;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
		if (ConfigReader.fancyClouds) {
			ConfigReader.fancyClouds = false;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
		if (ConfigReader.fancyTrees) {
			ConfigReader.fancyTrees = false;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
		if (ConfigReader.fancyWeather) {
			ConfigReader.fancyWeather = false;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
		if (ConfigReader.betterGrass == 2) {
			ConfigReader.betterGrass = 1;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
		if (ConfigReader.fancyWater) {
			ConfigReader.fancyWater = false;
			ConfigReader.write();
			if (--downgrade == 0) return;
		}
	}

}
