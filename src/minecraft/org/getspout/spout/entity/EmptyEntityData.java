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
package org.getspout.spout.entity;

import java.util.HashMap;

public class EmptyEntityData extends EntityData{

	@Override
	public byte getTextureToRender() {
		return 0;
	}
	
	@Override
	public void setTextureToRender(byte textureToRender) {
		
	}
	
	@Override
	public HashMap<Byte, String> getCustomTextures() {
		return null;
	}

	@Override
	public double getWalkingMod() {
		return 1D;
	}

	public void setWalkingMod(double walkingMod) {

	}

	@Override
	public double getGravityMod() {
		return 1D;
	}

	@Override
	public void setGravityMod(double gravityMod) {

	}

	@Override
	public double getSwimmingMod() {
		return 1D;
	}

	@Override
	public void setSwimmingMod(double swimmingMod) {

	}

	@Override
	public double getJumpingMod() {
		return 1D;
	}

	@Override
	public void setJumpingMod(double jumpingMod) {

	}

	@Override
	public double getAirspeedMod() {
		return 1D;
	}

	@Override
	public void setAirspeedMod(double airspeedMod) {

	}

}
