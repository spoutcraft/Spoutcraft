package net.minecraft.src;

final class J_JsonNullNodeBuilder
	implements J_JsonNodeBuilder {
	J_JsonNullNodeBuilder() {
	}

	public J_JsonNode buildNode() {
		return J_JsonNodeFactories.aJsonNull();
	}
}
