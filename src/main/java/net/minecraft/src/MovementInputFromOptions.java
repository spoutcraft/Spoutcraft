package net.minecraft.src;

public class MovementInputFromOptions extends MovementInput {
	// Spout Start - private to public
	public GameSettings gameSettings;
	// Spout End

	public MovementInputFromOptions(GameSettings par1GameSettings) {
		this.gameSettings = par1GameSettings;
	}

	// Spout Start - Keep parameter
	public void updatePlayerMoveState(EntityPlayer par1EntityPlayer) {
	// Spout End
		this.moveStrafe = 0.0F;
		this.moveForward = 0.0F;

		// Spout Start - Forward toggle
		if (this.gameSettings.keyBindForward.pressed || par1EntityPlayer.autoforwardToggle) {
		// Spout End
			++this.moveForward;
		}

		// Spout Start - Forward toggle
		if (this.gameSettings.keyBindBack.pressed || par1EntityPlayer.autoBackwardToggle) {
		// Spout End
			--this.moveForward;
		}

		if (this.gameSettings.keyBindLeft.pressed) {
			++this.moveStrafe;
		}

		if (this.gameSettings.keyBindRight.pressed) {
			--this.moveStrafe;
		}
		// Spout Start
		this.flyingDown = this.gameSettings.keyFlyDown.pressed;
		this.flyingUp = this.gameSettings.keyFlyUp.pressed;
		if (par1EntityPlayer.capabilities.isFlying) {
			this.moveStrafe = 0.0F;
			this.moveForward = 0.0F;

			if (this.gameSettings.keyFlyForward.pressed || par1EntityPlayer.autoforwardToggle) {
				++this.moveForward;
			}

			if (this.gameSettings.keyFlyBack.pressed) {
				--this.moveForward;
			}

			if (this.gameSettings.keyFlyLeft.pressed) {
				++this.moveStrafe;
			}

			if (this.gameSettings.keyFlyRight.pressed) {
				--this.moveStrafe;
			}
		}
		// Spout End

		this.jump = this.gameSettings.keyBindJump.pressed;
		// Spout Start - Sneak toggle
		this.sneak = this.gameSettings.keyBindSneak.pressed || par1EntityPlayer.sneakToggle;
		// Spout End

		if (this.sneak) {
			this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
			this.moveForward = (float)((double)this.moveForward * 0.3D);
		}
	}
}
