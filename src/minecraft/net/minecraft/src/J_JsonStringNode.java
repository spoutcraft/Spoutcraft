package net.minecraft.src;

import java.util.List;
import java.util.Map;

public final class J_JsonStringNode extends J_JsonNode
	implements Comparable {
	private final String value;

	J_JsonStringNode(String s) {
		if (s == null) {
			throw new NullPointerException("Attempt to construct a JsonString with a null value.");
		}
		else {
			value = s;
			return;
		}
	}

	public EnumJsonNodeType getType() {
		return EnumJsonNodeType.STRING;
	}

	public String getText() {
		return value;
	}

	public Map getFields() {
		throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
	}

	public List getElements() {
		throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		else {
			J_JsonStringNode j_jsonstringnode = (J_JsonStringNode)obj;
			return value.equals(j_jsonstringnode.value);
		}
	}

	public int hashCode() {
		return value.hashCode();
	}

	public String toString() {
		return (new StringBuilder()).append("JsonStringNode value:[").append(value).append("]").toString();
	}

	public int func_27223_a(J_JsonStringNode j_jsonstringnode) {
		return value.compareTo(j_jsonstringnode.value);
	}

	public int compareTo(Object obj) {
		return func_27223_a((J_JsonStringNode)obj);
	}
}
