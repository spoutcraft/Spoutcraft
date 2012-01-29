package net.minecraft.src;

public class MovementInput {
	public float moveStrafe;
	public float moveForward;
	public boolean field_1177_c;
	public boolean jump;
	public boolean sneak;

	public MovementInput() {
		moveStrafe = 0.0F;
		moveForward = 0.0F;
		field_1177_c = false;
		jump = false;
		sneak = false;
	}

	public void updatePlayerMoveState(EntityPlayer entityplayer) {
	}
}
