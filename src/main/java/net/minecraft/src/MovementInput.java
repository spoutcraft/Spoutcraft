package net.minecraft.src;

import net.minecraft.src.EntityPlayer;

public class MovementInput {

	/**
	 * The speed at which the player is strafing. Postive numbers to the left and negative to the right.
	 */
	public float moveStrafe = 0.0F;

	/**
	 * The speed at which the player is moving forward. Negative numbers will move backwards.
	 */
	public float moveForward = 0.0F;
	public boolean jump = false;
	public boolean sneak = false;
	// Spout Start
	public boolean flyingUp = false;
	public boolean flyingDown = false;
	// Spout End

	public void updatePlayerMoveState(EntityPlayer par1EntityPlayer) {} // Spout - kept parameter. TODO add a reason why
}
