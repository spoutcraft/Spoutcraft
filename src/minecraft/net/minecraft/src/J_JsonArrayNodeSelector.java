package net.minecraft.src;

import java.util.List;

final class J_JsonArrayNodeSelector extends J_LeafFunctor {
	J_JsonArrayNodeSelector() {
	}

	public boolean matchesNode_(J_JsonNode j_jsonnode) {
		return EnumJsonNodeType.ARRAY == j_jsonnode.getType();
	}

	public String shortForm() {
		return "A short form array";
	}

	public List typeSafeApplyTo(J_JsonNode j_jsonnode) {
		return j_jsonnode.getElements();
	}

	public String toString() {
		return "an array";
	}

	public Object typeSafeApplyTo(Object obj) {
		return typeSafeApplyTo((J_JsonNode)obj);
	}

	public boolean matchesNode(Object obj) {
		return matchesNode_((J_JsonNode)obj);
	}
}
