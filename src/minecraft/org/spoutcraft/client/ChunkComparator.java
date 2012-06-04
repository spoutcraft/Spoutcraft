package org.spoutcraft.client;

import java.util.Comparator;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Chunk;
import net.minecraft.src.MathHelper;

public class ChunkComparator implements Comparator<Chunk>{
	private int playerX = MathHelper.floor_double(Minecraft.theMinecraft.thePlayer.posX / 16.0D);
	private int playerZ = MathHelper.floor_double(Minecraft.theMinecraft.thePlayer.posZ / 16.0D);

	@Override
	public int compare(Chunk o1, Chunk o2) {
		int x1 = (o1.xPosition - playerX) * (o1.xPosition - playerX);
		int z1 = (o1.zPosition - playerZ) * (o1.zPosition - playerZ);

		int x2 = (o2.xPosition - playerX) * (o2.xPosition - playerX);
		int z2 = (o2.zPosition - playerZ) * (o2.zPosition - playerZ);
		return (x2 + z2) - (x1 + z1);
	}

}
