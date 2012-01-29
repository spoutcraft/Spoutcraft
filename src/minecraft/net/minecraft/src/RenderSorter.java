package net.minecraft.src;

import java.util.Comparator;

public class RenderSorter
	implements Comparator {
	private EntityLiving baseEntity;

	public RenderSorter(EntityLiving entityliving) {
		baseEntity = entityliving;
	}

	public int doCompare(WorldRenderer worldrenderer, WorldRenderer worldrenderer1) {
		boolean flag = worldrenderer.isInFrustum;
		boolean flag1 = worldrenderer1.isInFrustum;
		if (flag && !flag1) {
			return 1;
		}
		if (flag1 && !flag) {
			return -1;
		}
		double d = worldrenderer.distanceToEntitySquared(baseEntity);
		double d1 = worldrenderer1.distanceToEntitySquared(baseEntity);
		if (d < d1) {
			return 1;
		}
		if (d > d1) {
			return -1;
		}
		else {
			return worldrenderer.chunkIndex >= worldrenderer1.chunkIndex ? -1 : 1;
		}
	}

	public int compare(Object obj, Object obj1) {
		return doCompare((WorldRenderer)obj, (WorldRenderer)obj1);
	}
}
