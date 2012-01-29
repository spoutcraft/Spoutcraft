package net.minecraft.src;

class J_ObjectNodeContainer
	implements J_NodeContainer {
	final J_JsonObjectNodeBuilder nodeBuilder;
	final J_JsonListenerToJdomAdapter listenerToJdomAdapter;

	J_ObjectNodeContainer(J_JsonListenerToJdomAdapter j_jsonlistenertojdomadapter, J_JsonObjectNodeBuilder j_jsonobjectnodebuilder) {
		listenerToJdomAdapter = j_jsonlistenertojdomadapter;
		nodeBuilder = j_jsonobjectnodebuilder;
	}

	public void addNode(J_JsonNodeBuilder j_jsonnodebuilder) {
		throw new RuntimeException("Coding failure in Argo:  Attempt to add a node to an object.");
	}

	public void addField(J_JsonFieldBuilder j_jsonfieldbuilder) {
		nodeBuilder.withFieldBuilder(j_jsonfieldbuilder);
	}
}
