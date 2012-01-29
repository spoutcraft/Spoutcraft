package net.minecraft.src;

import java.util.*;

class J_JsonObjectNodeList extends HashMap {
	final J_JsonObjectNodeBuilder nodeBuilder;

	J_JsonObjectNodeList(J_JsonObjectNodeBuilder j_jsonobjectnodebuilder) {
		nodeBuilder = j_jsonobjectnodebuilder;

		J_JsonFieldBuilder j_jsonfieldbuilder;
		for (Iterator iterator = J_JsonObjectNodeBuilder.func_27236_a(nodeBuilder).iterator(); iterator.hasNext(); put(j_jsonfieldbuilder.func_27303_b(), j_jsonfieldbuilder.buildValue())) {
			j_jsonfieldbuilder = (J_JsonFieldBuilder)iterator.next();
		}
	}
}
