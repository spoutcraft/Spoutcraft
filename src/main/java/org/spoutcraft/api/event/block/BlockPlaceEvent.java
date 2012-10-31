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
package org.spoutcraft.api.event.block;

import org.spoutcraft.api.block.Block;
import org.spoutcraft.api.block.BlockState;
import org.spoutcraft.api.entity.Player;
import org.spoutcraft.api.event.Cancellable;
import org.spoutcraft.api.event.HandlerList;
import org.spoutcraft.api.inventory.ItemStack;

public class BlockPlaceEvent extends BlockEvent<BlockPlaceEvent> implements Cancellable {
	protected Player player;
	protected BlockState replacedBlockState;
	protected Block placedAgainst;
	protected ItemStack itemInHand;
	protected boolean canBuild;

	protected BlockPlaceEvent(Block placedBlock, BlockState replacedBlockState, Block placedAgainst, ItemStack itemInHand, Player player, boolean canBuild) {
		super(placedBlock);
		this.player = player;
		this.replacedBlockState = replacedBlockState;
		this.placedAgainst = placedAgainst;
		this.itemInHand = itemInHand;
		this.canBuild = canBuild;
	}

	public boolean canBuild() {
		return canBuild;
	}

	public Block getBlockPlaced() {
		return block;
	}

	public Block getBlockAgainst() {
		return placedAgainst;
	}

	public BlockState getReplacedBlockState() {
		return replacedBlockState;
	}

	public ItemStack getItemInHand() {
		return itemInHand;
	}

	public void setBuild(boolean canBuild) {
		this.canBuild = canBuild;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	public static final HandlerList<BlockPlaceEvent> handlers = new HandlerList<BlockPlaceEvent>();

	@Override
	public HandlerList<BlockPlaceEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "Block Place Event";
	}
}
