package net.minecraft.src;

public enum EnumAction {
	none("none", 0),
	eat("eat", 1),
	drink("drink", 2),
	block("block", 3),
	bow("bow", 4);

	private static final EnumAction allActions[] = (new EnumAction[] {
		none, eat, drink, block, bow
	});

	private EnumAction(String s, int i) {
	}
}
