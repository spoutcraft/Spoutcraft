package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;

final class J_JsonNodeList extends ArrayList {
	final Iterable field_27405_a;

	J_JsonNodeList(Iterable iterable) {
		field_27405_a = iterable;

		J_JsonNode j_jsonnode;
		for (Iterator iterator = field_27405_a.iterator(); iterator.hasNext(); add(j_jsonnode)) {
			j_jsonnode = (J_JsonNode)iterator.next();
		}
	}
}
