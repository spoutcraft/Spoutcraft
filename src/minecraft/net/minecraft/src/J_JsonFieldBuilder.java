package net.minecraft.src;

final class J_JsonFieldBuilder {
	private J_JsonNodeBuilder key;
	private J_JsonNodeBuilder valueBuilder;

	private J_JsonFieldBuilder() {
	}

	static J_JsonFieldBuilder aJsonFieldBuilder() {
		return new J_JsonFieldBuilder();
	}

	J_JsonFieldBuilder withKey(J_JsonNodeBuilder j_jsonnodebuilder) {
		key = j_jsonnodebuilder;
		return this;
	}

	J_JsonFieldBuilder withValue(J_JsonNodeBuilder j_jsonnodebuilder) {
		valueBuilder = j_jsonnodebuilder;
		return this;
	}

	J_JsonStringNode func_27303_b() {
		return (J_JsonStringNode)key.buildNode();
	}

	J_JsonNode buildValue() {
		return valueBuilder.buildNode();
	}
}
