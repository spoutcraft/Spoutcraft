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

import net.minecraft.src.Entity;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

import org.bukkit.ChatColor;

import org.spoutcraft.client.SpoutClient;

public class EntityText extends Entity {
	private String text;

	private boolean rotateWithPlayer = true;

	private float scale = 1.0F;

	private int duration;

	public EntityText(World var1) {
		super(var1);
		setText(ChatColor.RED + "test " + ChatColor.GREEN + "Some Green Text");
		setPosition(-125, 90, 1501);
		updateGeometry();
		this.clientonly = true;
		isDead = false;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound var1) {
		text = var1.getString("text");
		rotateWithPlayer = var1.getBoolean("rotateWithPlayer");
		scale = var1.getFloat("scale");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound var1) {
		var1.setString("text", text);
		var1.setBoolean("rotateWithPlayer", rotateWithPlayer);
		var1.setFloat("scale", scale);
	}

	public void setText(String text) {
		this.text = text;
		updateGeometry();
	}

	public String getText() {
		return text;
	}

	public void setRotateWithPlayer(boolean rotateWithPlayer) {
		this.rotateWithPlayer = rotateWithPlayer;
	}

	public boolean isRotateWithPlayer() {
		return rotateWithPlayer;
	}

	public void setScale(float scale) {
		this.scale = scale;
		updateGeometry();
	}

	public float getScale() {
		return scale;
	}

	private void updateGeometry() {
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		this.width = (float) font.getStringWidth(text) * 0.0139F * scale;
		this.height = 0.124F * scale;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (duration > 0) {
			if (duration < ticksExisted) {
				this.setDead();
			}
		}
		moveEntity(motionX, motionY, motionZ);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}
}
