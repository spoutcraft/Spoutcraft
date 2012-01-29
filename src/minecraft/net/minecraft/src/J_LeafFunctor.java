package net.minecraft.src;

abstract class J_LeafFunctor
	implements J_Functor {
	J_LeafFunctor() {
	}

	public final Object applyTo(Object obj) {
		if (!matchesNode(obj)) {
			throw J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_27322_a(this);
		}
		else {
			return typeSafeApplyTo(obj);
		}
	}

	protected abstract Object typeSafeApplyTo(Object obj);
}
