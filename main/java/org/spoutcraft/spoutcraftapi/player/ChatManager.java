package org.spoutcraft.spoutcraftapi.player;

import java.util.List;

public interface ChatManager {
	public List<String> getIgnoredPlayers();
	public List<String> getWordsToHighlight();
	public boolean isHighlightingWords();
	public boolean isIgnoringPlayers();
	public String formatChatColors(String text);
	public int getScroll();
	public boolean isShowingJoins();
	public boolean isUsingRegex();
	public float getOpacity();
}
