package net.minecraft.src;

public class GuiTextField extends Gui {
	private final FontRenderer fontRenderer;
	private final int xPos;
	private final int yPos;
	private final int width;
	private final int height;
	private String text;
	private int maxStringLength;
	private int cursorCounter;
	public boolean isFocused;
	public boolean isEnabled;
	private GuiScreen parentGuiScreen;

	public GuiTextField(GuiScreen guiscreen, FontRenderer fontrenderer, int i, int j, int k, int l, String s) {
		isFocused = false;
		isEnabled = true;
		parentGuiScreen = guiscreen;
		fontRenderer = fontrenderer;
		xPos = i;
		yPos = j;
		width = k;
		height = l;
		setText(s);
	}

	public void setText(String s) {
		text = s;
	}

	public String getText() {
		return text;
	}

	public void updateCursorCounter() {
		cursorCounter++;
	}

	public void textboxKeyTyped(char c, int i) {
		if (!isEnabled || !isFocused) {
			return;
		}
		if (c == '\t') {
			parentGuiScreen.selectNextField();
		}
		if (c == '\026') {
			String s;
			int j;
			s = GuiScreen.getClipboardString();
			if (s == null) {
				s = "";
			}
			j = 32 - text.length();
			if (j > s.length()) {
				j = s.length();
			}
			if (j > 0) {
				text += s.substring(0, j);
			}
		}
		if (i == 14 && text.length() > 0) {
			text = text.substring(0, text.length() - 1);
		}
		if ((ChatAllowedCharacters.allowedCharacters.indexOf(c) >= 0 || c > ' ') && (text.length() < maxStringLength || maxStringLength == 0)) {
			text += c;
		}
	}

	public void mouseClicked(int i, int j, int k) {
		boolean flag = isEnabled && i >= xPos && i < xPos + width && j >= yPos && j < yPos + height;
		setFocused(flag);
	}

	public void setFocused(boolean flag) {
		if (flag && !isFocused) {
			cursorCounter = 0;
		}
		isFocused = flag;
	}

	public void drawTextBox() {
		drawRect(xPos - 1, yPos - 1, xPos + width + 1, yPos + height + 1, 0xffa0a0a0);
		drawRect(xPos, yPos, xPos + width, yPos + height, 0xff000000);
		if (isEnabled) {
			boolean flag = isFocused && (cursorCounter / 6) % 2 == 0;
			drawString(fontRenderer, (new StringBuilder()).append(text).append(flag ? "_" : "").toString(), xPos + 4, yPos + (height - 8) / 2, 0xe0e0e0);
		}
		else {
			drawString(fontRenderer, text, xPos + 4, yPos + (height - 8) / 2, 0x707070);
		}
	}

	public void setMaxStringLength(int i) {
		maxStringLength = i;
	}
}
