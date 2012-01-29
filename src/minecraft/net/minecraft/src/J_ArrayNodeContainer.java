package net.minecraft.src;

class J_ArrayNodeContainer
	implements J_NodeContainer {
	final J_JsonArrayNodeBuilder nodeBuilder;
	final J_JsonListenerToJdomAdapter listenerToJdomAdapter;

	J_ArrayNodeContainer(J_JsonListenerToJdomAdapter j_jsonlistenertojdomadapter, J_JsonArrayNodeBuilder j_jsonarraynodebuilder) {
		listenerToJdomAdapter = j_jsonlistenertojdomadapter;
		nodeBuilder = j_jsonarraynodebuilder;
	}

	public void addNode(J_JsonNodeBuilder j_jsonnodebuilder) {
		nodeBuilder.withElement(j_jsonnodebuilder);
	}

	public void addField(J_JsonFieldBuilder j_jsonfieldbuilder) {
		throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to an array.");
	}
}
