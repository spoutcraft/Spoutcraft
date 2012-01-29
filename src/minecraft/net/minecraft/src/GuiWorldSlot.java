package net.minecraft.src;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

class GuiWorldSlot extends GuiSlot {
	final GuiSelectWorld parentWorldGui;

	public GuiWorldSlot(GuiSelectWorld guiselectworld) {
		super(guiselectworld.mc, guiselectworld.width, guiselectworld.height, 32, guiselectworld.height - 64, 36);
		parentWorldGui = guiselectworld;
	}

	protected int getSize() {
		return GuiSelectWorld.getSize(parentWorldGui).size();
	}

	protected void elementClicked(int i, boolean flag) {
		GuiSelectWorld.onElementSelected(parentWorldGui, i);
		boolean flag1 = GuiSelectWorld.getSelectedWorld(parentWorldGui) >= 0 && GuiSelectWorld.getSelectedWorld(parentWorldGui) < getSize();
		GuiSelectWorld.getSelectButton(parentWorldGui).enabled = flag1;
		GuiSelectWorld.getRenameButton(parentWorldGui).enabled = flag1;
		GuiSelectWorld.getDeleteButton(parentWorldGui).enabled = flag1;
		if (flag && flag1) {
			parentWorldGui.selectWorld(i);
		}
	}

	protected boolean isSelected(int i) {
		return i == GuiSelectWorld.getSelectedWorld(parentWorldGui);
	}

	protected int getContentHeight() {
		return GuiSelectWorld.getSize(parentWorldGui).size() * 36;
	}

	protected void drawBackground() {
		parentWorldGui.drawDefaultBackground();
	}

	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
		SaveFormatComparator saveformatcomparator = (SaveFormatComparator)GuiSelectWorld.getSize(parentWorldGui).get(i);
		String s = saveformatcomparator.getDisplayName();
		if (s == null || MathHelper.stringNullOrLengthZero(s)) {
			s = (new StringBuilder()).append(GuiSelectWorld.func_22087_f(parentWorldGui)).append(" ").append(i + 1).toString();
		}
		String s1 = saveformatcomparator.getFileName();
		s1 = (new StringBuilder()).append(s1).append(" (").append(GuiSelectWorld.getDateFormatter(parentWorldGui).format(new Date(saveformatcomparator.getLastTimePlayed()))).toString();
		s1 = (new StringBuilder()).append(s1).append(")").toString();
		String s2 = "";
		if (saveformatcomparator.requiresConversion()) {
			s2 = (new StringBuilder()).append(GuiSelectWorld.func_22088_h(parentWorldGui)).append(" ").append(s2).toString();
		}
		else {
			s2 = GuiSelectWorld.func_35315_i(parentWorldGui)[saveformatcomparator.getGameType()];
		}
		if (saveformatcomparator.isHardcoreModeEnabled()) {
			s2 = (new StringBuilder()).append("\2474").append(StatCollector.translateToLocal("gameMode.hardcore")).append("\2478").toString();
		}
		parentWorldGui.drawString(parentWorldGui.fontRenderer, s, j + 2, k + 1, 0xffffff);
		parentWorldGui.drawString(parentWorldGui.fontRenderer, s1, j + 2, k + 12, 0x808080);
		parentWorldGui.drawString(parentWorldGui.fontRenderer, s2, j + 2, k + 12 + 10, 0x808080);
	}
}
