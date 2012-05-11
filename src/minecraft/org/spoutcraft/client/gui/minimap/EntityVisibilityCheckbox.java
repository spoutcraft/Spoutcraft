package org.spoutcraft.client.gui.minimap;

import net.minecraft.src.Entity;

import org.spoutcraft.spoutcraftapi.gui.CheckBox;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class EntityVisibilityCheckbox extends GenericCheckBox {
	private Class<? extends Entity> clazz;
	private boolean save = true;
	
	public EntityVisibilityCheckbox(Class<? extends Entity> clazz) {
		this.clazz = clazz;
		String name = clazz.getSimpleName().replaceFirst("Entity", "");
		name = name.replaceAll("([A-Z])", " $1").trim();
		setText(name);
		save = false;
		setChecked(MinimapConfig.getInstance().isEntityVisible(clazz));
		save = true;
	}

	@Override
	public CheckBox setChecked(boolean checked) {
		if(save) {
			MinimapConfig.getInstance().setEntityVisible(clazz, checked);
			MinimapConfig.getInstance().save();
		}
		return super.setChecked(checked);
	}
	
	
}
