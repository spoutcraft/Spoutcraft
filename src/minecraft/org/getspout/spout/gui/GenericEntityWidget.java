package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.entity.EntityManager;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.Entity;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderManager;

public class GenericEntityWidget extends GenericWidget implements EntityWidget {
	private Entity entity = null;
	private int entityId = 0;

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setEntityID(input.readInt());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(entityId);
	}

	@Override
	public WidgetType getType() {
		return WidgetType.EntityWidget;
	}

	@Override
	public void render() {
		entity = SpoutClient.getInstance().getEntityFromId(entityId);
		if(entity == null)
			return;
		GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
		GL11.glPushMatrix();
		GL11.glTranslatef(getX() + 30, getY() + 50, 50F);
		RenderHelper.enableStandardItemLighting();
        float f1 = 30F;
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(entity.prevRotationYaw, 0, 1.0F, 0);
		//RenderHelper.enableStandardItemLighting();
        RenderManager.instance.playerViewY = 180F;
        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
	}

	@Override
	public void setEntityID(int entity) {
		this.entityId = entity;
	}

	@Override
	public int getEntityID() {
		return entityId;
	}
}
