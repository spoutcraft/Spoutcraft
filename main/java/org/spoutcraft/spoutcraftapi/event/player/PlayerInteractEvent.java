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
package org.spoutcraft.spoutcraftapi.event.player;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.BlockFace;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.event.Cancellable;
import org.spoutcraft.spoutcraftapi.event.HandlerList;
import org.spoutcraft.spoutcraftapi.event.block.Action;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;

public class PlayerInteractEvent extends PlayerEvent<PlayerInteractEvent> implements Cancellable {

	protected Block clickedBlock;
	protected BlockFace blockFace;
	protected Action action;
	protected ItemStack item;

	protected PlayerInteractEvent(Player player, Action action, ItemStack item, Block clickedBlock, BlockFace blockFace) {
		super(player);
		this.clickedBlock = clickedBlock;
		this.blockFace = blockFace;
		this.action = action;
		this.item = item;
	}

	public Action getAction() {
		return action;
	}

	public BlockFace getBlockFace() {
		return blockFace;
	}

	public ItemStack getItem() {
		return item;
	}

	public boolean hasBlock() {
		return this.clickedBlock != null;
	}

	public boolean hasItem() {
		return this.item != null;
	}

	public Block getClickedBlock() {
		return clickedBlock;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	public static final HandlerList<PlayerInteractEvent> handlers = new HandlerList<PlayerInteractEvent>();
	
	@Override
	public HandlerList<PlayerInteractEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "Player Interact Event";
	}

}
