package org.spoutcraft.spoutcraftapi.gui;

import java.io.IOException;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.UnsafeClass;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;

@UnsafeClass
public class GenericCheckBox extends GenericButton implements CheckBox {

	boolean checked = false;
	
	public GenericCheckBox() {
		super();
	}
	
	public GenericCheckBox(String text) {
		super(text);
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		checked = input.readBoolean();
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
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
