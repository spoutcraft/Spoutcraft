package net.minecraft.src;

public class ChatLine {
	public String message;
	public int updateCounter;

	public ChatLine(String s) {
		message = s;
		updateCounter = 0;
	}
}
