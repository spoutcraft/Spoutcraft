package org.spoutcraft.spoutcraftapi.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.spoutcraftapi.Spoutcraft;

public class GenericEntityWidget extends GenericWidget implements EntityWidget {
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
		Spoutcraft.getClient().getRenderDelegate().render(this);
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
