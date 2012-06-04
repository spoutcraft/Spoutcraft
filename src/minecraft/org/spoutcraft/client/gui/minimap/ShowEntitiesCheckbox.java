package org.spoutcraft.client.gui.minimap;

import org.spoutcraft.spoutcraftapi.gui.CheckBox;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class ShowEntitiesCheckbox extends GenericCheckBox {
	public ShowEntitiesCheckbox() {
		setText("Show Mobs");
		setChecked(MinimapConfig.getInstance().isShowingEntities());
	}

	@Override
	public CheckBox setChecked(boolean checked) {
		MinimapConfig.getInstance().setShowEntities(checked);
		MinimapConfig.getInstance().save();
		return super.setChecked(checked);
	}
}
