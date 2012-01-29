package net.minecraft.src;

public final class J_JsonStringNodeBuilder
	implements J_JsonNodeBuilder {
	private final String field_27244_a;

	J_JsonStringNodeBuilder(String s) {
		field_27244_a = s;
	}

	public J_JsonStringNode func_27243_a() {
		return J_JsonNodeFactories.aJsonString(field_27244_a);
	}

	public J_JsonNode buildNode() {
		return func_27243_a();
	}
}
