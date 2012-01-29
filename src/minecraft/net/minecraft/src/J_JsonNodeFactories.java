package net.minecraft.src;

import java.util.Arrays;
import java.util.Map;

public final class J_JsonNodeFactories {
	private J_JsonNodeFactories() {
	}

	public static J_JsonNode aJsonNull() {
		return J_JsonConstants.NULL;
	}

	public static J_JsonNode aJsonTrue() {
		return J_JsonConstants.TRUE;
	}

	public static J_JsonNode aJsonFalse() {
		return J_JsonConstants.FALSE;
	}

	public static J_JsonStringNode aJsonString(String s) {
		return new J_JsonStringNode(s);
	}

	public static J_JsonNode aJsonNumber(String s) {
		return new J_JsonNumberNode(s);
	}

	public static J_JsonRootNode aJsonArray(Iterable iterable) {
		return new J_JsonArray(iterable);
	}

	public static J_JsonRootNode aJsonArray(J_JsonNode aj_jsonnode[]) {
		return aJsonArray(Arrays.asList(aj_jsonnode));
	}

	public static J_JsonRootNode aJsonObject(Map map) {
		return new J_JsonObject(map);
	}
}
