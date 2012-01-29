package net.minecraft.src;

import java.util.Map;

final class J_JsonFieldNodeSelector extends J_LeafFunctor {
	final J_JsonStringNode field_27066_a;

	J_JsonFieldNodeSelector(J_JsonStringNode j_jsonstringnode) {
		field_27066_a = j_jsonstringnode;
	}

	public boolean func_27065_a(Map map) {
		return map.containsKey(field_27066_a);
	}

	public String shortForm() {
		return (new StringBuilder()).append("\"").append(field_27066_a.getText()).append("\"").toString();
	}

	public J_JsonNode func_27064_b(Map map) {
		return (J_JsonNode)map.get(field_27066_a);
	}

	public String toString() {
		return (new StringBuilder()).append("a field called [\"").append(field_27066_a.getText()).append("\"]").toString();
	}

	public Object typeSafeApplyTo(Object obj) {
		return func_27064_b((Map)obj);
	}

	public boolean matchesNode(Object obj) {
		return func_27065_a((Map)obj);
	}
}
