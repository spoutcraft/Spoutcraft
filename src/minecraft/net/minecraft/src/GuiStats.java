package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiStats extends GuiScreen {
	private static RenderItem renderItem = new RenderItem();
	protected GuiScreen parentGui;
	protected String statsTitle;
	private GuiSlotStatsGeneral slotGeneral;
	private GuiSlotStatsItem slotItem;
	private GuiSlotStatsBlock slotBlock;
	private StatFileWriter statFileWriter;
	private GuiSlot selectedSlot;

	public GuiStats(GuiScreen guiscreen, StatFileWriter statfilewriter) {
		statsTitle = "Select world";
		selectedSlot = null;
		parentGui = guiscreen;
		statFileWriter = statfilewriter;
	}

	public void initGui() {
		statsTitle = StatCollector.translateToLocal("gui.stats");
		slotGeneral = new GuiSlotStatsGeneral(this);
		slotGeneral.registerScrollButtons(controlList, 1, 1);
		slotItem = new GuiSlotStatsItem(this);
		slotItem.registerScrollButtons(controlList, 1, 1);
		slotBlock = new GuiSlotStatsBlock(this);
		slotBlock.registerScrollButtons(controlList, 1, 1);
		selectedSlot = slotGeneral;
		addHeaderButtons();
	}

	public void addHeaderButtons() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		controlList.add(new GuiButton(0, width / 2 + 4, height - 28, 150, 20, stringtranslate.translateKey("gui.done")));
		controlList.add(new GuiButton(1, width / 2 - 154, height - 52, 100, 20, stringtranslate.translateKey("stat.generalButton")));
		GuiButton guibutton;
		controlList.add(guibutton = new GuiButton(2, width / 2 - 46, height - 52, 100, 20, stringtranslate.translateKey("stat.blocksButton")));
		GuiButton guibutton1;
		controlList.add(guibutton1 = new GuiButton(3, width / 2 + 62, height - 52, 100, 20, stringtranslate.translateKey("stat.itemsButton")));
		if (slotBlock.getSize() == 0) {
			guibutton.enabled = false;
		}
		if (slotItem.getSize() == 0) {
			guibutton1.enabled = false;
		}
	}

	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 0) {
			mc.displayGuiScreen(parentGui);
		}
		else if (guibutton.id == 1) {
			selectedSlot = slotGeneral;
		}
		else if (guibutton.id == 3) {
			selectedSlot = slotItem;
		}
		else if (guibutton.id == 2) {
			selectedSlot = slotBlock;
		}
		else {
			selectedSlot.actionPerformed(guibutton);
		}
	}

	public void drawScreen(int i, int j, float f) {
		selectedSlot.drawScreen(i, j, f);
		drawCenteredString(fontRenderer, statsTitle, width / 2, 20, 0xffffff);
		super.drawScreen(i, j, f);
	}

	private void drawItemSprite(int i, int j, int k) {
		drawButtonBackground(i + 1, j + 1);
		GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		RenderHelper.func_41089_c();
		renderItem.drawItemIntoGui(fontRenderer, mc.renderEngine, k, 0, Item.itemsList[k].getIconFromDamage(0), i + 2, j + 2);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
	}

	private void drawButtonBackground(int i, int j) {
		drawSprite(i, j, 0, 0);
	}

	private void drawSprite(int i, int j, int k, int l) {
		int i1 = mc.renderEngine.getTexture("/gui/slot.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i1);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(i + 0, j + 18, zLevel, (float)(k + 0) * 0.0078125F, (float)(l + 18) * 0.0078125F);
		tessellator.addVertexWithUV(i + 18, j + 18, zLevel, (float)(k + 18) * 0.0078125F, (float)(l + 18) * 0.0078125F);
		tessellator.addVertexWithUV(i + 18, j + 0, zLevel, (float)(k + 18) * 0.0078125F, (float)(l + 0) * 0.0078125F);
		tessellator.addVertexWithUV(i + 0, j + 0, zLevel, (float)(k + 0) * 0.0078125F, (float)(l + 0) * 0.0078125F);
		tessellator.draw();
	}

	static Minecraft getMinecraft(GuiStats guistats) {
		return guistats.mc;
	}

	static FontRenderer getFontRenderer1(GuiStats guistats) {
		return guistats.fontRenderer;
	}

	static StatFileWriter getStatsFileWriter(GuiStats guistats) {
		return guistats.statFileWriter;
	}

	static FontRenderer getFontRenderer2(GuiStats guistats) {
		return guistats.fontRenderer;
	}

	static FontRenderer getFontRenderer3(GuiStats guistats) {
		return guistats.fontRenderer;
	}

	static Minecraft getMinecraft1(GuiStats guistats) {
		return guistats.mc;
	}

	static void drawSprite(GuiStats guistats, int i, int j, int k, int l) {
		guistats.drawSprite(i, j, k, l);
	}

	static Minecraft getMinecraft2(GuiStats guistats) {
		return guistats.mc;
	}

	static FontRenderer getFontRenderer4(GuiStats guistats) {
		return guistats.fontRenderer;
	}

	static FontRenderer getFontRenderer5(GuiStats guistats) {
		return guistats.fontRenderer;
	}

	static FontRenderer getFontRenderer6(GuiStats guistats) {
		return guistats.fontRenderer;
	}

	static FontRenderer getFontRenderer7(GuiStats guistats) {
		return guistats.fontRenderer;
	}

	static FontRenderer getFontRenderer8(GuiStats guistats) {
		return guistats.fontRenderer;
	}

	static void drawGradientRect(GuiStats guistats, int i, int j, int k, int l, int i1, int j1) {
		guistats.drawGradientRect(i, j, k, l, i1, j1);
	}

	static FontRenderer getFontRenderer9(GuiStats guistats) {
		return guistats.fontRenderer;
	}

	static FontRenderer getFontRenderer10(GuiStats guistats) {
		return guistats.fontRenderer;
	}

	static void drawGradientRect1(GuiStats guistats, int i, int j, int k, int l, int i1, int j1) {
		guistats.drawGradientRect(i, j, k, l, i1, j1);
	}

	static FontRenderer getFontRenderer11(GuiStats guistats) {
		return guistats.fontRenderer;
	}

	static void drawItemSprite(GuiStats guistats, int i, int j, int k) {
		guistats.drawItemSprite(i, j, k);
	}
}
