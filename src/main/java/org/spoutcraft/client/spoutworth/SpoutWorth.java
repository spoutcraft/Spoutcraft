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
package org.spoutcraft.client.spoutworth;

import gnu.trove.iterator.TLongIterator;
import gnu.trove.list.linked.TLongLinkedList;

import net.minecraft.client.Minecraft;

import org.spoutcraft.client.config.Configuration;

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
		if (avgFPS != -1) {
			return avgFPS;
		}
		if (fpsList.size() == 0) {
			return IDEAL_FPS;
		}
		long total = 0;
		TLongIterator i = fpsList.iterator();
		while (i.hasNext()) {
			total += i.next();
		}
		avgFPS = Math.max(1, total / fpsList.size());
		return avgFPS;
	}

	public long getIdealFPS() {
		return Math.min(getAverageFPS() + 10, IDEAL_FPS);
	}

	public void doTick() {
		Configuration.setSignDistance(Math.max(8, Configuration.getSignDistance()));
		if (!Configuration.isAutomatePerformance()) {
			return;
		}

		Configuration.setPerformance(0);
		Configuration.setFarView(false);

		long idealFPS = getIdealFPS();
		// Calculate how close we are to acheiving ideal FPS
		long percent = getAverageFPS() * 100 / idealFPS;
		long percentCur = currentFPS * 100 / idealFPS;

		// Increase our ideal standards if we are looking for best performance
		if (Configuration.getAutomateMode() == 0) {
			percent -= 10;
			percentCur -= 10;
		} else if (Configuration.getAutomateMode() == 1) { // Decrease our ideal standards if we are looking for balanced performance
			percent += 15;
			percentCur += 15;
		} else if (Configuration.getAutomateMode() == 2) { // Decrease our ideal standards if we are looking for best appearance
			percent += 40;
			percentCur += 40;
		}

		boolean improving = percentCur > percent;

		belowIdeal = percent < 75;
		debug = "Ideal Percent: " + percent + " Current Percent: " + percentCur + " Improving: " + improving;

		if (percent > 200) {
			if (--appearanceCooldown <= 0) {
				appearanceCooldown = 600;
				increaseAppearance();
				increaseAppearance();
			}
		} else if (percent > 150 || (percent > 125 && improving)) {
			if (--appearanceCooldown <= 0) {
				appearanceCooldown = 600;
				increaseAppearance();
			}
		} else if (percent < 40) {
			decreaseAppearance(true, improving ? 10 : 0);
			appearanceCooldown = 120;
		} else if (percent < 66) {
			if (--appearanceCooldown <= 0) {
				decreaseAppearance(improving, 0);
				appearanceCooldown = 240;
			}
		} else if (percent < 85) {
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
		return belowIdeal && Configuration.isAutomatePerformance();
	}

	public String getDebugText() {
		return debug;
	}

	public void increaseAppearance() {
		if (!Configuration.isSky()) {
			Configuration.setSky(true);
			Configuration.write();
			return;
		}
		if (!Configuration.isStars()) {
			Configuration.setStars(true);
			Configuration.write();
			return;
		}
		if (!Configuration.isFancyWater()) {
			Configuration.setFancyWater(true);
			Configuration.write();
			return;
		}
		if (!Configuration.isFancyParticles()) {
			Configuration.setFancyParticles(true);
			Configuration.write();
			return;
		}
		if (!Configuration.isFancyFog()) {
			Configuration.setFancyFog(true);
			Configuration.write();
			return;
		}
		if (!Configuration.isFancyGrass()) {
			Configuration.setFancyGrass(true);
			Configuration.write();
			return;
		}
		if (!Configuration.isFancyBiomeColors()) {
			Configuration.setFancyBiomeColors(true);
			Configuration.write();
			return;
		}
		if (!Configuration.isFancyLight()) {
			Configuration.setFancyLight(true);
			Configuration.write();
			return;
		}
		if (!Configuration.isFancyLight()) {
			Configuration.setFancyLight(true);
			Configuration.write();
			return;
		}
		if (!Configuration.isFancyClouds()) {
			Configuration.setFancyClouds(true);
			Configuration.write();
			return;
		}
		if (!Configuration.isFancyTrees()) {
			Configuration.setFancyTrees(true);
			Configuration.write();
			return;
		}
		if (!Configuration.isFancyWeather()) {
			Configuration.setFancyWeather(true);
			Configuration.write();
			return;
		}
		if (Configuration.getBetterGrass() == 1) {
			Configuration.setBetterGrass(2);
			Configuration.write();
			return;
		}
		if (Configuration.getRenderDistance() != 0) {
			Configuration.setRenderDistance(Configuration.getRenderDistance() - 1);
			Minecraft.theMinecraft.gameSettings.renderDistance = Configuration.getRenderDistance();
			Configuration.write();
			return;
		}
		if (Configuration.getAdvancedOpenGL() != 0) {
			Configuration.setAdvancedOpenGL(0);
			Minecraft.theMinecraft.gameSettings.advancedOpengl = false;
			Configuration.write();
			return;
		}
		if (Configuration.getSignDistance() != Integer.MAX_VALUE) {
			Configuration.setSignDistance(Configuration.getSignDistance() * 2);
			if (Configuration.getSignDistance() >= 128) {
				Configuration.setSignDistance(Integer.MAX_VALUE);
			}
			Configuration.write();
			return;
		}
	}

	public void decreaseAppearance() {
		decreaseAppearance(false, 0);
	}

	public void decreaseAppearance(boolean drastic, long haltRenderingCount) {
		int downgrade = drastic ? 2 : 1;

		if (Configuration.isSky()) {
			Configuration.setSky(false);
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
		if (Configuration.getChunkRenderPasses() > 1) {
			Configuration.setChunkRenderPasses(1);
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
		if (Configuration.getRenderDistance() < 2) {
			Configuration.setRenderDistance(Configuration.getRenderDistance() + 1);
			Minecraft.theMinecraft.gameSettings.renderDistance = Configuration.getRenderDistance();
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
		if (Configuration.getAdvancedOpenGL() == 2 || Configuration.getAdvancedOpenGL() == 0) {
			Configuration.setAdvancedOpenGL(1);
			Minecraft.theMinecraft.gameSettings.advancedOpengl = true;
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
		if (Configuration.getSignDistance() > 8) {
			Configuration.setSignDistance(Configuration.getSignDistance() / 2);
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}

		if (Configuration.isFancyParticles()) {
			Configuration.setFancyParticles(false);
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
		if (Configuration.isFancyFog()) {
			Configuration.setFancyFog(false);
			Configuration.write();
			if (--downgrade == 0) {
				return;
			}
		}
		if (Configuration.isFancyGrass()) {
			Configuration.setFancyGrass(false);
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
		if (Configuration.isFancyBiomeColors()) {
			Configuration.setFancyBiomeColors(false);
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
		if (Configuration.isStars()) {
			Configuration.setStars(false);
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
		if (Configuration.isFancyLight()) {
			Configuration.setFancyLight(false);
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
		if (Configuration.isFancyLight()) {
			Configuration.setFancyLight(false);
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
		if (Configuration.isFancyClouds()) {
			Configuration.setFancyClouds(false);
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
		if (Configuration.isFancyTrees()) {
			Configuration.setFancyTrees(false);
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
		if (Configuration.isFancyWeather()) {
			Configuration.setFancyWeather(false);
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
		if (Configuration.getBetterGrass() == 2) {
			Configuration.setBetterGrass(1);
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
		if (Configuration.isFancyWater()) {
			Configuration.setFancyWater(false);
			if (--downgrade == 0) {
				Configuration.write();
				return;
			}
		}
	}
}
