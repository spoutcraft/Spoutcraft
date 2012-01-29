package net.minecraft.src;

import java.util.Comparator;

public class EntitySorter
	implements Comparator {
	private double entityPosX;
	private double entityPosY;
	private double entityPosZ;

	public EntitySorter(Entity entity) {
		entityPosX = -entity.posX;
		entityPosY = -entity.posY;
		entityPosZ = -entity.posZ;
	}

	public int sortByDistanceToEntity(WorldRenderer worldrenderer, WorldRenderer worldrenderer1) {
		double d = (double)worldrenderer.posXPlus + entityPosX;
		double d1 = (double)worldrenderer.posYPlus + entityPosY;
		double d2 = (double)worldrenderer.posZPlus + entityPosZ;
		double d3 = (double)worldrenderer1.posXPlus + entityPosX;
		double d4 = (double)worldrenderer1.posYPlus + entityPosY;
		double d5 = (double)worldrenderer1.posZPlus + entityPosZ;
		return (int)(((d * d + d1 * d1 + d2 * d2) - (d3 * d3 + d4 * d4 + d5 * d5)) * 1024D);
	}

	public int compare(Object obj, Object obj1) {
		return sortByDistanceToEntity((WorldRenderer)obj, (WorldRenderer)obj1);
	}
}
