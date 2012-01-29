package net.minecraft.src;

class J_FieldNodeContainer
	implements J_NodeContainer {
	final J_JsonFieldBuilder fieldBuilder;
	final J_JsonListenerToJdomAdapter listenerToJdomAdapter;

	J_FieldNodeContainer(J_JsonListenerToJdomAdapter j_jsonlistenertojdomadapter, J_JsonFieldBuilder j_jsonfieldbuilder) {
		listenerToJdomAdapter = j_jsonlistenertojdomadapter;
		fieldBuilder = j_jsonfieldbuilder;
	}

	public void addNode(J_JsonNodeBuilder j_jsonnodebuilder) {
		fieldBuilder.withValue(j_jsonnodebuilder);
	}

	public void addField(J_JsonFieldBuilder j_jsonfieldbuilder) {
		throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to a field.");
	}
}
