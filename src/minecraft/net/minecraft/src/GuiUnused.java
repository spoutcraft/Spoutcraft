package net.minecraft.src;

public class GuiUnused extends GuiScreen {
	private String message1;
	private String message2;

	public void initGui() {
	}

	public void drawScreen(int i, int j, float f) {
		drawGradientRect(0, 0, width, height, 0xff402020, 0xff501010);
		drawCenteredString(fontRenderer, message1, width / 2, 90, 0xffffff);
		drawCenteredString(fontRenderer, message2, width / 2, 110, 0xffffff);
		super.drawScreen(i, j, f);
	}

	protected void keyTyped(char c, int i) {
	}
}
