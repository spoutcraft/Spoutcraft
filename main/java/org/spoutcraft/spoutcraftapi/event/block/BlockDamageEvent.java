package org.spoutcraft.spoutcraftapi.event.block;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.event.Cancellable;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;

public class BlockDamageEvent extends BlockEvent implements Cancellable {
	
	protected boolean cancel = false;
	protected ItemStack itemInHand;
	protected boolean instaBreak;
	protected Player player;

	protected BlockDamageEvent(Block block, Player player, ItemStack itemInHand, boolean instaBreak) {
		super(Type.BLOCK_DAMAGE, block);
		this.player = player;
		this.itemInHand = itemInHand;
		this.instaBreak = instaBreak;
	}
	
	public boolean getInstaBreak() {
		return instaBreak;
	}
	
	public ItemStack getItemInHand() {
		return itemInHand;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setInstaBreak(boolean instaBreak) {
		this.instaBreak = instaBreak;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

}
