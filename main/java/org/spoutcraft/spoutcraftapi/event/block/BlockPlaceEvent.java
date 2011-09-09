package org.spoutcraft.spoutcraftapi.event.block;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.BlockState;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.event.Cancellable;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;

public class BlockPlaceEvent extends BlockEvent implements Cancellable {

	protected boolean cancel = false;
	protected Player player;
	protected BlockState replacedBlockState;
	protected Block placedAgainst;
	protected ItemStack itemInHand;
	protected boolean canBuild;

	protected BlockPlaceEvent(Block placedBlock, BlockState replacedBlockState, Block placedAgainst, ItemStack itemInHand, Player player, boolean canBuild) {
		super(Type.BLOCK_PLACE, placedBlock);
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
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;

	}

}
