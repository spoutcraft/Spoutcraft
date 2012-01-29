package net.minecraft.src;

final class J_ChainedFunctor
	implements J_Functor {
	private final J_JsonNodeSelector parentJsonNodeSelector;
	private final J_JsonNodeSelector childJsonNodeSelector;

	J_ChainedFunctor(J_JsonNodeSelector j_jsonnodeselector, J_JsonNodeSelector j_jsonnodeselector1) {
		parentJsonNodeSelector = j_jsonnodeselector;
		childJsonNodeSelector = j_jsonnodeselector1;
	}

	public boolean matchesNode(Object obj) {
		return parentJsonNodeSelector.matches(obj) && childJsonNodeSelector.matches(parentJsonNodeSelector.getValue(obj));
	}

	public Object applyTo(Object obj) {
		Object obj1;
		try {
			obj1 = parentJsonNodeSelector.getValue(obj);
		}
		catch (J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_jsonnodedoesnotmatchchainedjsonnodeselectorexception) {
			throw J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_27321_b(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, parentJsonNodeSelector);
		}
		Object obj2;
		try {
			obj2 = childJsonNodeSelector.getValue(obj1);
		}
		catch (J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_jsonnodedoesnotmatchchainedjsonnodeselectorexception1) {
			throw J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_27323_a(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception1, parentJsonNodeSelector);
		}
		return obj2;
	}

	public String shortForm() {
		return childJsonNodeSelector.shortForm();
	}

	public String toString() {
		return (new StringBuilder()).append(parentJsonNodeSelector.toString()).append(", with ").append(childJsonNodeSelector.toString()).toString();
	}
}
