package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;

public final class J_JsonObjectNodeBuilder
	implements J_JsonNodeBuilder {
	private final List fieldBuilders = new LinkedList();

	J_JsonObjectNodeBuilder() {
	}

	public J_JsonObjectNodeBuilder withFieldBuilder(J_JsonFieldBuilder j_jsonfieldbuilder) {
		fieldBuilders.add(j_jsonfieldbuilder);
		return this;
	}

	public J_JsonRootNode func_27235_a() {
		return J_JsonNodeFactories.aJsonObject(new J_JsonObjectNodeList(this));
	}

	public J_JsonNode buildNode() {
		return func_27235_a();
	}

	static List func_27236_a(J_JsonObjectNodeBuilder j_jsonobjectnodebuilder) {
		return j_jsonobjectnodebuilder.fieldBuilders;
	}
}
