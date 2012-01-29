package net.minecraft.src;

import java.util.List;

class GuiSlotStatsGeneral extends GuiSlot {
	final GuiStats field_27276_a;

	public GuiSlotStatsGeneral(GuiStats guistats) {
		super(GuiStats.getMinecraft(guistats), guistats.width, guistats.height, 32, guistats.height - 64, 10);
		field_27276_a = guistats;
		func_27258_a(false);
	}

	protected int getSize() {
		return StatList.generalStats.size();
	}

	protected void elementClicked(int i, boolean flag) {
	}

	protected boolean isSelected(int i) {
		return false;
	}

	protected int getContentHeight() {
		return getSize() * 10;
	}

	protected void drawBackground() {
		field_27276_a.drawDefaultBackground();
	}

	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
		StatBase statbase = (StatBase)StatList.generalStats.get(i);
		field_27276_a.drawString(GuiStats.getFontRenderer1(field_27276_a), StatCollector.translateToLocal(statbase.func_44020_i()), j + 2, k + 1, i % 2 != 0 ? 0x909090 : 0xffffff);
		String s = statbase.func_27084_a(GuiStats.getStatsFileWriter(field_27276_a).writeStat(statbase));
		field_27276_a.drawString(GuiStats.getFontRenderer2(field_27276_a), s, (j + 2 + 213) - GuiStats.getFontRenderer3(field_27276_a).getStringWidth(s), k + 1, i % 2 != 0 ? 0x909090 : 0xffffff);
	}
}
