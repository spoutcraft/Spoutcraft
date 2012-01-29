package net.minecraft.src;

final class J_JsonNumberNodeBuilder
	implements J_JsonNodeBuilder {
	private final J_JsonNode field_27239_a;

	J_JsonNumberNodeBuilder(String s) {
		field_27239_a = J_JsonNodeFactories.aJsonNumber(s);
	}

	public J_JsonNode buildNode() {
		return field_27239_a;
	}
}
