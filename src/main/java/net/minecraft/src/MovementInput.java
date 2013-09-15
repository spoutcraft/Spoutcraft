package net.minecraft.src;

public class MovementInput {

	/**
	 * The speed at which the player is strafing. Postive numbers to the left and negative to the right.
	 */
	public float moveStrafe;

	/**
	 * The speed at which the player is moving forward. Negative numbers will move backwards.
	 */
	public float moveForward;
	public boolean jump;
	public boolean sneak;
	// Spout Start
	public boolean flyingUp;
	public boolean flyingDown;
	// Spout End

	// Spout Start - Keep parameter. TODO: Add a reason why.
	public void updatePlayerMoveState(EntityPlayer par1EntityPlayer) {}
	// Spout End
}
