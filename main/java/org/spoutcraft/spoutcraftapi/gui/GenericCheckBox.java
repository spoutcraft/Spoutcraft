package org.spoutcraft.spoutcraftapi.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.spoutcraftapi.Spoutcraft;

public class GenericCheckBox extends GenericButton implements CheckBox {

	boolean checked = false;
	
	public GenericCheckBox(String text) {
		super(text);
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 1;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		checked = input.readBoolean();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeBoolean(checked);
	}

	@Override
	public WidgetType getType() {
		return WidgetType.CheckBox;
	}

	@Override
	public void render() {
		Spoutcraft.getClient().getRenderDelegate().render((GenericCheckBox)this);
	}

	public boolean isChecked() {
		return checked;
	}

	public CheckBox setChecked(boolean checked) {
		this.checked = checked;
		return this;
	}

}
