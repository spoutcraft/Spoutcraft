package org.spoutcraft.client.gui.minimap;

import net.minecraft.client.Minecraft;

public class MinimapUtils {
	
	public static boolean insideCircle(int centerX, int centerY, int radius, int x, int y) {
		double squareDist = (centerX - x) * (centerX - x) + (centerY - y) * (centerY - y);
		return squareDist <= radius * radius;
	}
	
	public static String getWorldName() {
		String worldname = Minecraft.theMinecraft.theWorld.getWorldInfo().getWorldName();
		if (worldname.equals("MpServer")) {
			return org.spoutcraft.client.gui.error.GuiConnectionLost.lastServerIp;
		}
		return worldname;
	}

}
