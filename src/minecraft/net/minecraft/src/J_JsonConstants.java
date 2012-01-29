package net.minecraft.src;

import java.util.List;
import java.util.Map;

final class J_JsonConstants extends J_JsonNode {
	static final J_JsonConstants NULL;
	static final J_JsonConstants TRUE;
	static final J_JsonConstants FALSE;
	private final EnumJsonNodeType jsonNodeType;

	private J_JsonConstants(EnumJsonNodeType enumjsonnodetype) {
		jsonNodeType = enumjsonnodetype;
	}

	public EnumJsonNodeType getType() {
		return jsonNodeType;
	}

	public String getText() {
		throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
	}

	public Map getFields() {
		throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
	}

	public List getElements() {
		throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
	}

	static {
		NULL = new J_JsonConstants(EnumJsonNodeType.NULL);
		TRUE = new J_JsonConstants(EnumJsonNodeType.TRUE);
		FALSE = new J_JsonConstants(EnumJsonNodeType.FALSE);
	}
}
