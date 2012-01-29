package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;

public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer {
	private Map entityHashMap;

	public TileEntityMobSpawnerRenderer() {
		entityHashMap = new HashMap();
	}

	public void renderTileEntityMobSpawner(TileEntityMobSpawner tileentitymobspawner, double d, double d1, double d2,
	        float f) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d + 0.5F, (float)d1, (float)d2 + 0.5F);
		Entity entity = (Entity)entityHashMap.get(tileentitymobspawner.getMobID());
		if (entity == null) {
			entity = EntityList.createEntityInWorld(tileentitymobspawner.getMobID(), null);
			entityHashMap.put(tileentitymobspawner.getMobID(), entity);
		}
		if (entity != null) {
			entity.setWorld(tileentitymobspawner.worldObj);
			float f1 = 0.4375F;
			GL11.glTranslatef(0.0F, 0.4F, 0.0F);
			GL11.glRotatef((float)(tileentitymobspawner.yaw2 + (tileentitymobspawner.yaw - tileentitymobspawner.yaw2) * (double)f) * 10F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-30F, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0.0F, -0.4F, 0.0F);
			GL11.glScalef(f1, f1, f1);
			entity.setLocationAndAngles(d, d1, d2, 0.0F, 0.0F);
			RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, f);
		}
		GL11.glPopMatrix();
	}

	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2,
	        float f) {
		renderTileEntityMobSpawner((TileEntityMobSpawner)tileentity, d, d1, d2, f);
	}
}
