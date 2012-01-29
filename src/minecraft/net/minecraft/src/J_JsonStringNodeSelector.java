package net.minecraft.src;

final class J_JsonStringNodeSelector extends J_LeafFunctor {
	J_JsonStringNodeSelector() {
	}

	public boolean func_27072_a(J_JsonNode j_jsonnode) {
		return EnumJsonNodeType.STRING == j_jsonnode.getType();
	}

	public String shortForm() {
		return "A short form string";
	}

	public String func_27073_b(J_JsonNode j_jsonnode) {
		return j_jsonnode.getText();
	}

	public String toString() {
		return "a value that is a string";
	}

	public Object typeSafeApplyTo(Object obj) {
		return func_27073_b((J_JsonNode)obj);
	}

	public boolean matchesNode(Object obj) {
		return func_27072_a((J_JsonNode)obj);
	}
}
