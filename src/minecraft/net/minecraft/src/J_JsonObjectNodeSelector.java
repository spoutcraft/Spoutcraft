package net.minecraft.src;

import java.util.Map;

final class J_JsonObjectNodeSelector extends J_LeafFunctor {
	J_JsonObjectNodeSelector() {
	}

	public boolean func_27070_a(J_JsonNode j_jsonnode) {
		return EnumJsonNodeType.OBJECT == j_jsonnode.getType();
	}

	public String shortForm() {
		return "A short form object";
	}

	public Map func_27071_b(J_JsonNode j_jsonnode) {
		return j_jsonnode.getFields();
	}

	public String toString() {
		return "an object";
	}

	public Object typeSafeApplyTo(Object obj) {
		return func_27071_b((J_JsonNode)obj);
	}

	public boolean matchesNode(Object obj) {
		return func_27070_a((J_JsonNode)obj);
	}
}
