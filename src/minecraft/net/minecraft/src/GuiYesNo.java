package net.minecraft.src;

import java.util.List;

public class GuiYesNo extends GuiScreen {
	private GuiScreen parentScreen;
	private String message1;
	private String message2;
	private String buttonText1;
	private String buttonText2;
	private int worldNumber;

	public GuiYesNo(GuiScreen guiscreen, String s, String s1, String s2, String s3, int i) {
		parentScreen = guiscreen;
		message1 = s;
		message2 = s1;
		buttonText1 = s2;
		buttonText2 = s3;
		worldNumber = i;
	}

	public void initGui() {
		controlList.add(new GuiSmallButton(0, (width / 2 - 155) + 0, height / 6 + 96, buttonText1));
		controlList.add(new GuiSmallButton(1, (width / 2 - 155) + 160, height / 6 + 96, buttonText2));
	}

	protected void actionPerformed(GuiButton guibutton) {
		parentScreen.deleteWorld(guibutton.id == 0, worldNumber);
	}

	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, message1, width / 2, 70, 0xffffff);
		drawCenteredString(fontRenderer, message2, width / 2, 90, 0xffffff);
		super.drawScreen(i, j, f);
	}
}
