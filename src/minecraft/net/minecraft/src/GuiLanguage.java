package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class GuiLanguage extends GuiScreen {
	protected GuiScreen field_44009_a;
	private int field_44007_b;
	private GuiSlotLanguage field_44008_c;
	private final GameSettings field_44006_d;
	private GuiSmallButton field_46029_e;

	public GuiLanguage(GuiScreen guiscreen, GameSettings gamesettings) {
		field_44007_b = -1;
		field_44009_a = guiscreen;
		field_44006_d = gamesettings;
	}

	public void initGui() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		controlList.add(field_46029_e = new GuiSmallButton(6, width / 2 - 75, height - 38, stringtranslate.translateKey("gui.done")));
		field_44008_c = new GuiSlotLanguage(this);
		field_44008_c.registerScrollButtons(controlList, 7, 8);
	}

	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id != 5) {
			if (guibutton.id == 6) {
				field_44006_d.saveOptions();
				mc.displayGuiScreen(field_44009_a);
			}
			else {
				field_44008_c.actionPerformed(guibutton);
			}
		}
	}

	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
	}

	protected void mouseMovedOrUp(int i, int j, int k) {
		super.mouseMovedOrUp(i, j, k);
	}

	public void drawScreen(int i, int j, float f) {
		field_44008_c.drawScreen(i, j, f);
		if (field_44007_b <= 0) {
			mc.texturePackList.updateAvaliableTexturePacks();
			field_44007_b += 20;
		}
		StringTranslate stringtranslate = StringTranslate.getInstance();
		drawCenteredString(fontRenderer, stringtranslate.translateKey("options.language"), width / 2, 16, 0xffffff);
		drawCenteredString(fontRenderer, (new StringBuilder()).append("(").append(stringtranslate.translateKey("options.languageWarning")).append(")").toString(), width / 2, height - 56, 0x808080);
		super.drawScreen(i, j, f);
	}

	public void updateScreen() {
		super.updateScreen();
		field_44007_b--;
	}

	static GameSettings func_44005_a(GuiLanguage guilanguage) {
		return guilanguage.field_44006_d;
	}

	static GuiSmallButton func_46028_b(GuiLanguage guilanguage) {
		return guilanguage.field_46029_e;
	}
}
