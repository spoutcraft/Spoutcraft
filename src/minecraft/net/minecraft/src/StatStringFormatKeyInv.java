package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class StatStringFormatKeyInv
	implements IStatStringFormat {
	final Minecraft mc;

	public StatStringFormatKeyInv(Minecraft minecraft) {
		mc = minecraft;
	}

	public String formatString(String s) {
		try {
			return String.format(s, new Object[] {
			            GameSettings.getKeyDisplayString(mc.gameSettings.keyBindInventory.keyCode)
			        });
		}
		catch (Exception exception) {
			return (new StringBuilder()).append("Error: ").append(exception.getLocalizedMessage()).toString();
		}
	}
}
