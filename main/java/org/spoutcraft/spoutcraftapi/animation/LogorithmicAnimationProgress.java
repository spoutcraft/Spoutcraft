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
package org.spoutcraft.spoutcraftapi.animation;

public class LogorithmicAnimationProgress implements AnimationProgress {

	private int strength;
	private double slog;
	
	public LogorithmicAnimationProgress() {
		this(20);
	}
	
	public LogorithmicAnimationProgress(int strength) {
		this.strength = strength;
		this.slog = Math.log(strength + 1);
	}
	
	public double getValueAt(double progress) {
		return Math.log(progress * strength + 1) / slog;
	}
}
