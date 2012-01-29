package net.minecraft.src;

import java.util.*;

public final class J_JsonArrayNodeBuilder
	implements J_JsonNodeBuilder {
	private final List elementBuilders = new LinkedList();

	J_JsonArrayNodeBuilder() {
	}

	public J_JsonArrayNodeBuilder withElement(J_JsonNodeBuilder j_jsonnodebuilder) {
		elementBuilders.add(j_jsonnodebuilder);
		return this;
	}

	public J_JsonRootNode build() {
		LinkedList linkedlist = new LinkedList();
		J_JsonNodeBuilder j_jsonnodebuilder;
		for (Iterator iterator = elementBuilders.iterator(); iterator.hasNext(); linkedlist.add(j_jsonnodebuilder.buildNode())) {
			j_jsonnodebuilder = (J_JsonNodeBuilder)iterator.next();
		}

		return J_JsonNodeFactories.aJsonArray(linkedlist);
	}

	public J_JsonNode buildNode() {
		return build();
	}
}
