package net.minecraft.src;

import java.util.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

abstract class GuiSlotStats extends GuiSlot {
	protected int field_27268_b;
	protected List field_27273_c;
	protected Comparator field_27272_d;
	protected int field_27271_e;
	protected int field_27270_f;
	final GuiStats field_27269_g;

	protected GuiSlotStats(GuiStats guistats) {
		super(GuiStats.getMinecraft1(guistats), guistats.width, guistats.height, 32, guistats.height - 64, 20);
		field_27269_g = guistats;
		field_27268_b = -1;
		field_27271_e = -1;
		field_27270_f = 0;
		func_27258_a(false);
		func_27259_a(true, 20);
	}

	protected void elementClicked(int i, boolean flag) {
	}

	protected boolean isSelected(int i) {
		return false;
	}

	protected void drawBackground() {
		field_27269_g.drawDefaultBackground();
	}

	protected void func_27260_a(int i, int j, Tessellator tessellator) {
		if (!Mouse.isButtonDown(0)) {
			field_27268_b = -1;
		}
		if (field_27268_b == 0) {
			GuiStats.drawSprite(field_27269_g, (i + 115) - 18, j + 1, 0, 0);
		}
		else {
			GuiStats.drawSprite(field_27269_g, (i + 115) - 18, j + 1, 0, 18);
		}
		if (field_27268_b == 1) {
			GuiStats.drawSprite(field_27269_g, (i + 165) - 18, j + 1, 0, 0);
		}
		else {
			GuiStats.drawSprite(field_27269_g, (i + 165) - 18, j + 1, 0, 18);
		}
		if (field_27268_b == 2) {
			GuiStats.drawSprite(field_27269_g, (i + 215) - 18, j + 1, 0, 0);
		}
		else {
			GuiStats.drawSprite(field_27269_g, (i + 215) - 18, j + 1, 0, 18);
		}
		if (field_27271_e != -1) {
			char c = 'O';
			byte byte0 = 18;
			if (field_27271_e == 1) {
				c = '\201';
			}
			else if (field_27271_e == 2) {
				c = '\263';
			}
			if (field_27270_f == 1) {
				byte0 = 36;
			}
			GuiStats.drawSprite(field_27269_g, i + c, j + 1, byte0, 0);
		}
	}

	protected void func_27255_a(int i, int j) {
		field_27268_b = -1;
		if (i >= 79 && i < 115) {
			field_27268_b = 0;
		}
		else if (i >= 129 && i < 165) {
			field_27268_b = 1;
		}
		else if (i >= 179 && i < 215) {
			field_27268_b = 2;
		}
		if (field_27268_b >= 0) {
			func_27266_c(field_27268_b);
			GuiStats.getMinecraft2(field_27269_g).sndManager.playSoundFX("random.click", 1.0F, 1.0F);
		}
	}

	protected final int getSize() {
		return field_27273_c.size();
	}

	protected final StatCrafting func_27264_b(int i) {
		return (StatCrafting)field_27273_c.get(i);
	}

	protected abstract String func_27263_a(int i);

	protected void func_27265_a(StatCrafting statcrafting, int i, int j, boolean flag) {
		if (statcrafting != null) {
			String s = statcrafting.func_27084_a(GuiStats.getStatsFileWriter(field_27269_g).writeStat(statcrafting));
			field_27269_g.drawString(GuiStats.getFontRenderer4(field_27269_g), s, i - GuiStats.getFontRenderer5(field_27269_g).getStringWidth(s), j + 5, flag ? 0xffffff : 0x909090);
		}
		else {
			String s1 = "-";
			field_27269_g.drawString(GuiStats.getFontRenderer6(field_27269_g), s1, i - GuiStats.getFontRenderer7(field_27269_g).getStringWidth(s1), j + 5, flag ? 0xffffff : 0x909090);
		}
	}

	protected void func_27257_b(int i, int j) {
		if (j < top || j > bottom) {
			return;
		}
		int k = func_27256_c(i, j);
		int l = field_27269_g.width / 2 - 92 - 16;
		if (k >= 0) {
			if (i < l + 40 || i > l + 40 + 20) {
				return;
			}
			StatCrafting statcrafting = func_27264_b(k);
			func_27267_a(statcrafting, i, j);
		}
		else {
			String s = "";
			if (i >= (l + 115) - 18 && i <= l + 115) {
				s = func_27263_a(0);
			}
			else if (i >= (l + 165) - 18 && i <= l + 165) {
				s = func_27263_a(1);
			}
			else if (i >= (l + 215) - 18 && i <= l + 215) {
				s = func_27263_a(2);
			}
			else {
				return;
			}
			s = (new StringBuilder()).append("").append(StringTranslate.getInstance().translateKey(s)).toString().trim();
			if (s.length() > 0) {
				int i1 = i + 12;
				int j1 = j - 12;
				int k1 = GuiStats.getFontRenderer8(field_27269_g).getStringWidth(s);
				GuiStats.drawGradientRect(field_27269_g, i1 - 3, j1 - 3, i1 + k1 + 3, j1 + 8 + 3, 0xc0000000, 0xc0000000);
				GuiStats.getFontRenderer9(field_27269_g).drawStringWithShadow(s, i1, j1, -1);
			}
		}
	}

	protected void func_27267_a(StatCrafting statcrafting, int i, int j) {
		if (statcrafting == null) {
			return;
		}
		Item item = Item.itemsList[statcrafting.func_25072_b()];
		String s = (new StringBuilder()).append("").append(StringTranslate.getInstance().translateNamedKey(item.getItemName())).toString().trim();
		if (s.length() > 0) {
			int k = i + 12;
			int l = j - 12;
			int i1 = GuiStats.getFontRenderer10(field_27269_g).getStringWidth(s);
			GuiStats.drawGradientRect1(field_27269_g, k - 3, l - 3, k + i1 + 3, l + 8 + 3, 0xc0000000, 0xc0000000);
			GuiStats.getFontRenderer11(field_27269_g).drawStringWithShadow(s, k, l, -1);
		}
	}

	protected void func_27266_c(int i) {
		if (i != field_27271_e) {
			field_27271_e = i;
			field_27270_f = -1;
		}
		else if (field_27270_f == -1) {
			field_27270_f = 1;
		}
		else {
			field_27271_e = -1;
			field_27270_f = 0;
		}
		Collections.sort(field_27273_c, field_27272_d);
	}
}
