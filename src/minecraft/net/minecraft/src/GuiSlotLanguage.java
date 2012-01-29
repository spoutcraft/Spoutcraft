package net.minecraft.src;

import java.util.*;
import net.minecraft.client.Minecraft;

class GuiSlotLanguage extends GuiSlot {
	private ArrayList field_44013_b;
	private TreeMap field_44014_c;
	final GuiLanguage field_44015_a;

	public GuiSlotLanguage(GuiLanguage guilanguage) {
		super(guilanguage.mc, guilanguage.width, guilanguage.height, 32, (guilanguage.height - 65) + 4, 18);
		field_44015_a = guilanguage;
		field_44014_c = StringTranslate.getInstance().func_44022_b();
		field_44013_b = new ArrayList();
		String s;
		for (Iterator iterator = field_44014_c.keySet().iterator(); iterator.hasNext(); field_44013_b.add(s)) {
			s = (String)iterator.next();
		}
	}

	protected int getSize() {
		return field_44013_b.size();
	}

	protected void elementClicked(int i, boolean flag) {
		StringTranslate.getInstance().func_44023_a((String)field_44013_b.get(i));
		field_44015_a.mc.fontRenderer.func_44032_a(StringTranslate.getInstance().func_46110_d());
		GuiLanguage.func_44005_a(field_44015_a).field_44018_Q = (String)field_44013_b.get(i);
		field_44015_a.fontRenderer.func_46123_b(StringTranslate.func_46109_d(GuiLanguage.func_44005_a(field_44015_a).field_44018_Q));
		GuiLanguage.func_46028_b(field_44015_a).displayString = StringTranslate.getInstance().translateKey("gui.done");
	}

	protected boolean isSelected(int i) {
		return ((String)field_44013_b.get(i)).equals(StringTranslate.getInstance().func_44024_c());
	}

	protected int getContentHeight() {
		return getSize() * 18;
	}

	protected void drawBackground() {
		field_44015_a.drawDefaultBackground();
	}

	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
		field_44015_a.fontRenderer.func_46123_b(true);
		field_44015_a.drawCenteredString(field_44015_a.fontRenderer, (String)field_44014_c.get(field_44013_b.get(i)), field_44015_a.width / 2, k + 1, 0xffffff);
		field_44015_a.fontRenderer.func_46123_b(StringTranslate.func_46109_d(GuiLanguage.func_44005_a(field_44015_a).field_44018_Q));
	}
}
