package net.minecraft.src;

final class J_JsonEscapedString {
	private final String escapedString;

	J_JsonEscapedString(String s) {
		escapedString = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\b", "\\b").replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
	}

	public String toString() {
		return escapedString;
	}
}
