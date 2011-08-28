package net.minecraft.src;

public class BlockVar extends Block {

	int[] sides;
	protected BlockVar(int i, Material material, int[] sides1) {
		super(i, 0, material);
		sides = sides1;
	}
	
	public int getBlockTextureFromSide(int side)
    {
    	return sides[side];
    }
}
