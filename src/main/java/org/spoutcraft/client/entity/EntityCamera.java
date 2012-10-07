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
package org.spoutcraft.client.entity;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayerSP;

import org.spoutcraft.client.SpoutClient;

public class EntityCamera extends EntityPlayerSP {
	public EntityCamera(CraftCameraEntity spoutEntity) {
		super(SpoutClient.getHandle(), SpoutClient.getHandle().theWorld, SpoutClient.getHandle().session, SpoutClient.getHandle().thePlayer.dimension);
		yOffset = 1.62F;
		this.spoutEntity = spoutEntity;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public void onEntityUpdate() { }

	@Override
	public void onDeath(DamageSource source) { }

	@Override
	public boolean isEntityAlive() {
		return true;
	}

	public void setRotation(float yaw, float pitch) {
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;

		prevRotationYaw = rotationYaw;
		prevRotationPitch = rotationPitch;
		rotationYaw = yaw;
		rotationPitch = pitch;
	}
}
