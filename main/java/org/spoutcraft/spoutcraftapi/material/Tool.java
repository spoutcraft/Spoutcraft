package org.spoutcraft.spoutcraftapi.material;

public interface Tool extends Item {
	
	public short getDurability();
	
	public Tool setDurability(short durability);

	public float getStrengthModifier(Block block);
	
	public Tool setStrengthModifier(Block block, float modifier);
	
	public Block[] getStrengthModifiedBlocks();
}
