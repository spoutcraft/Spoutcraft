package net.minecraft.src;

import java.util.Arrays;

public final class J_JsonNodeSelectors {
	private J_JsonNodeSelectors() {
	}

	public static J_JsonNodeSelector func_27349_a(Object aobj[]) {
		return chainOn(aobj, new J_JsonNodeSelector(new J_JsonStringNodeSelector()));
	}

	public static J_JsonNodeSelector func_27346_b(Object aobj[]) {
		return chainOn(aobj, new J_JsonNodeSelector(new J_JsonArrayNodeSelector()));
	}

	public static J_JsonNodeSelector func_27353_c(Object aobj[]) {
		return chainOn(aobj, new J_JsonNodeSelector(new J_JsonObjectNodeSelector()));
	}

	public static J_JsonNodeSelector func_27348_a(String s) {
		return func_27350_a(J_JsonNodeFactories.aJsonString(s));
	}

	public static J_JsonNodeSelector func_27350_a(J_JsonStringNode j_jsonstringnode) {
		return new J_JsonNodeSelector(new J_JsonFieldNodeSelector(j_jsonstringnode));
	}

	public static J_JsonNodeSelector func_27351_b(String s) {
		return func_27353_c(new Object[0]).with(func_27348_a(s));
	}

	public static J_JsonNodeSelector func_27347_a(int i) {
		return new J_JsonNodeSelector(new J_JsonElementNodeSelector(i));
	}

	public static J_JsonNodeSelector func_27354_b(int i) {
		return func_27346_b(new Object[0]).with(func_27347_a(i));
	}

	private static J_JsonNodeSelector chainOn(Object aobj[], J_JsonNodeSelector j_jsonnodeselector) {
		J_JsonNodeSelector j_jsonnodeselector1 = j_jsonnodeselector;
		for (int i = aobj.length - 1; i >= 0; i--) {
			if (aobj[i] instanceof Integer) {
				j_jsonnodeselector1 = chainedJsonNodeSelector(func_27354_b(((Integer)aobj[i]).intValue()), j_jsonnodeselector1);
				continue;
			}
			if (aobj[i] instanceof String) {
				j_jsonnodeselector1 = chainedJsonNodeSelector(func_27351_b((String)aobj[i]), j_jsonnodeselector1);
			}
			else {
				throw new IllegalArgumentException((new StringBuilder()).append("Element [").append(aobj[i]).append("] of path elements").append(" [").append(Arrays.toString(aobj)).append("] was of illegal type [").append(aobj[i].getClass().getCanonicalName()).append("]; only Integer and String are valid.").toString());
			}
		}

		return j_jsonnodeselector1;
	}

	private static J_JsonNodeSelector chainedJsonNodeSelector(J_JsonNodeSelector j_jsonnodeselector, J_JsonNodeSelector j_jsonnodeselector1) {
		return new J_JsonNodeSelector(new J_ChainedFunctor(j_jsonnodeselector, j_jsonnodeselector1));
	}
}
