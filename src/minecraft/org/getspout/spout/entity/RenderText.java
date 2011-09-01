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

import org.getspout.spout.client.SpoutClient;
import net.minecraft.src.Entity;
import net.minecraft.src.RenderEntity;
import net.minecraft.src.RenderHelper;

public class RenderText extends RenderEntity {

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {
		EntityText entitytext = (EntityText)entity;
		String text = entitytext.getText();
		yaw = entitytext.isRotateWithPlayer()?-this.renderManager.playerViewY:yaw;
		RenderHelper.disableStandardItemLighting();
		SpoutClient.getHandle().fontRenderer.renderStringInGame(text, x, y, z, yaw, 0, entitytext.getScale());	
		RenderHelper.enableStandardItemLighting();
	}
}
