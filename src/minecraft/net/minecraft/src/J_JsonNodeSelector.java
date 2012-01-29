package net.minecraft.src;

public final class J_JsonNodeSelector {
	final J_Functor valueGetter;

	J_JsonNodeSelector(J_Functor j_functor) {
		valueGetter = j_functor;
	}

	public boolean matches(Object obj) {
		return valueGetter.matchesNode(obj);
	}

	public Object getValue(Object obj) {
		return valueGetter.applyTo(obj);
	}

	public J_JsonNodeSelector with(J_JsonNodeSelector j_jsonnodeselector) {
		return new J_JsonNodeSelector(new J_ChainedFunctor(this, j_jsonnodeselector));
	}

	String shortForm() {
		return valueGetter.shortForm();
	}

	public String toString() {
		return valueGetter.toString();
	}
}
