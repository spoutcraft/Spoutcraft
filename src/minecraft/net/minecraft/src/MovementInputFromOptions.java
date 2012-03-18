package net.minecraft.src;

import org.spoutcraft.client.config.ConfigReader;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GameSettings;
import net.minecraft.src.MovementInput;

public class MovementInputFromOptions extends MovementInput {

	private boolean[] movementKeyStates = new boolean[10];
	public GameSettings gameSettings; //Spout


	public MovementInputFromOptions(GameSettings par1GameSettings) {
		this.gameSettings = par1GameSettings;
	}

	public void checkKeyForMovementInput(int var1, boolean var2) {
		byte var3 = -1;
		if(var1 == this.gameSettings.keyBindForward.keyCode) {
			var3 = 0;
		}

		if(var1 == this.gameSettings.keyBindBack.keyCode) {
			var3 = 1;
		}

		if(var1 == this.gameSettings.keyBindLeft.keyCode) {
			var3 = 2;
		}

		if(var1 == this.gameSettings.keyBindRight.keyCode) {
			var3 = 3;
		}

		if(var1 == this.gameSettings.keyBindJump.keyCode) {
			var3 = 4;
		}

		if(var1 == this.gameSettings.keyBindSneak.keyCode) {
			var3 = 5;
		}

		if(var3 >= 0) {
			this.movementKeyStates[var3] = var2;
		}

	}

	public void resetKeyState() {
		for(int var1 = 0; var1 < 10; ++var1) {
			this.movementKeyStates[var1] = false;
		}

	}

	public void updatePlayerMoveState(EntityPlayer par1EntityPlayer) {
		this.moveStrafe = 0.0F;
		this.moveForward = 0.0F;
		if (this.gameSettings.keyBindForward.pressed || par1EntityPlayer.autoforwardToggle) { //Spout
			++this.moveForward;
		}

		if (this.gameSettings.keyBindBack.pressed) {
			--this.moveForward;
		}

		if (this.gameSettings.keyBindLeft.pressed) {
			++this.moveStrafe;
		}

		if (this.gameSettings.keyBindRight.pressed) {
			--this.moveStrafe;
		}
		
		//Spout start
		this.flyingDown = this.gameSettings.keyFlyDown.pressed;
		this.flyingUp = this.gameSettings.keyFlyUp.pressed;
		if (par1EntityPlayer.capabilities.isFlying){
			this.moveStrafe = 0.0F;
			this.moveForward = 0.0F;
			if (this.gameSettings.keyFlyForward.pressed || par1EntityPlayer.autoforwardToggle) { //Spout
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
		//Spout end

		this.jump = this.gameSettings.keyBindJump.pressed;
		this.sneak = this.gameSettings.keyBindSneak.pressed || par1EntityPlayer.sneakToggle; //Spout
		if (this.sneak) {
			this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
			this.moveForward = (float)((double)this.moveForward * 0.3D);
		}

	}
}
