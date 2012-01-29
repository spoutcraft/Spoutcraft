package net.minecraft.src;

import java.util.List;
import java.util.Map;

public abstract class J_JsonNode {
	J_JsonNode() {
	}

	public abstract EnumJsonNodeType getType();

	public abstract String getText();

	public abstract Map getFields();

	public abstract List getElements();

	public final String getStringValue(Object aobj[]) {
		return (String)wrapExceptionsFor(J_JsonNodeSelectors.func_27349_a(aobj), this, aobj);
	}

	public final List getArrayNode(Object aobj[]) {
		return (List)wrapExceptionsFor(J_JsonNodeSelectors.func_27346_b(aobj), this, aobj);
	}

	private Object wrapExceptionsFor(J_JsonNodeSelector j_jsonnodeselector, J_JsonNode j_jsonnode, Object aobj[]) {
		try {
			return j_jsonnodeselector.getValue(j_jsonnode);
		}
		catch (J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_jsonnodedoesnotmatchchainedjsonnodeselectorexception) {
			throw J_JsonNodeDoesNotMatchPathElementsException.jsonNodeDoesNotMatchPathElementsException(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, aobj, J_JsonNodeFactories.aJsonArray(new J_JsonNode[] {
			            j_jsonnode
			        }));
		}
	}
}
